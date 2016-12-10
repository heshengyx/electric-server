package com.grgbanking.electric.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grgbanking.electric.handler.WorkServerHandler;

public class WorkNioServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkNioServer.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	// 监听端口
	private int port;

	public WorkNioServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		// 配置服务器端NIO线程组
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							// 设置超时时间
							ch.pipeline().addLast(
									new IdleStateHandler(20, 15, 10));
							// 以("\n")为结尾分割的解码器
							ch.pipeline()
									.addLast(
											new LineBasedFrameDecoder(
													Integer.MAX_VALUE));
							ch.pipeline().addLast(new WorkServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口,同步等待
			ChannelFuture f = b.bind(port).sync();
			LOGGER.info("服务启动成功，在端口[{}]监听服务", port);
			// 等待服务器端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			LOGGER.info("服务正在关闭中...finally");
			// 优雅退出，释放NIO线程组
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void shutdown() {
		if (workerGroup != null && bossGroup != null) {
			try {
				LOGGER.info("服务正在关闭中...");
				workerGroup.shutdownGracefully().syncUninterruptibly();
				bossGroup.shutdownGracefully().syncUninterruptibly();
				LOGGER.info("服务关闭成功");
			} catch (Exception e) {
				LOGGER.error("服务关闭异常", e);
			}

		}
	}
}
