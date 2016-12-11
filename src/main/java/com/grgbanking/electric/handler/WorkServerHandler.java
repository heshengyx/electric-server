package com.grgbanking.electric.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.grgbanking.electric.enums.OptEnum;
import com.grgbanking.electric.factory.ApplicationContextFactory;
import com.grgbanking.electric.factory.WorkServerFactory;
import com.grgbanking.electric.server.WorkServer;

public class WorkServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkServerHandler.class);

	private WorkServerFactory workServerFactory;

	public WorkServerHandler() {
		this.workServerFactory = ApplicationContextFactory
				.getBean("workServerFactory");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		String ip = insocket.getAddress().getHostAddress();

		ByteBuf buf = (ByteBuf) msg;
		int length = buf.readableBytes();
		if (length > 0) {
			byte[] req = new byte[buf.readableBytes()];// 获得缓冲区可读的字节数
			buf.readBytes(req);

			String body = new String(req, "UTF-8");
			LOGGER.info("终端[{}]，接收消息：{}", new Object[] { ip, body });
			praseMessage(ctx, body, ip);
		}
	}

	/**
	 * 消息处理
	 * @param ctx
	 * @param body
	 * @param ip
	 * @throws UnsupportedEncodingException
	 */
	private void praseMessage(ChannelHandlerContext ctx, String body, String ip)
			throws UnsupportedEncodingException {
		String message = null;
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(body);
		} catch (Exception e) {
			LOGGER.error("请求数据不是JSON格式", e);
			message = "请求数据不是JSON格式";
		}
		if (json != null) {
			String opt = json.optString("type");
			if (StringUtils.isEmpty(opt)) {
				message = "请求数据没有操作标示";
			} else if (OptEnum.UpgradeStart.name().equals(opt)) {
				// 升级消息
				// downFile(ctx, ip, json.optString("Version"));
			} else {
				WorkServer server = workServerFactory.getWorkServer(opt.toUpperCase());
				if (server != null) {
					message = server.handler(body, ip);
				}
			}
		}
		if (!StringUtils.isEmpty(message)) {
			LOGGER.info("终端[{}]，响应消息：{}", new Object[] { ip, message });
			ByteBuf resp = Unpooled.copiedBuffer(message.getBytes("UTF-8"));
			ctx.write(resp);
		}
	}
}
