/*
 * Copyright (c) LinkedIn Corporation. All rights reserved. Licensed under the BSD-2 Clause license.
 * See LINKEDIN_FLASHBACK_LICENSE in the project root for license information.
 */

package com.linkedin.mitm.proxy;

import com.linkedin.mitm.proxy.channel.ChannelMediator;
import com.linkedin.mitm.proxy.channel.ClientChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * Initialize client to proxy channel and channel pipeline.
 *
 * @author shfeng
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel> {
  private final ProxyServer _proxyServer;

  public ProxyInitializer(ProxyServer proxyServer) {
    _proxyServer = proxyServer;
  }

  @Override
  public void initChannel(SocketChannel socketChannel) {
    ChannelPipeline channelPipeline = socketChannel.pipeline();
    channelPipeline.addLast("decoder", new HttpRequestDecoder());
    channelPipeline.addLast("encoder", new HttpResponseEncoder());
    channelPipeline.addLast("idle", new IdleStateHandler(0, 0, _proxyServer.getClientConnectionIdleTimeout()));
    ChannelMediator channelMediator = new ChannelMediator(socketChannel,
        _proxyServer.getProxyModeControllerFactory(),
        _proxyServer.getDownstreamWorkerGroup(),
        _proxyServer.getServerConnectionIdleTimeout(),
        _proxyServer.getAllChannels());
    ClientChannelHandler clientChannelHandler =
        new ClientChannelHandler(channelMediator, _proxyServer.getConnectionFlowRegistry());

    channelPipeline.addLast("handler", clientChannelHandler);
  }
}
