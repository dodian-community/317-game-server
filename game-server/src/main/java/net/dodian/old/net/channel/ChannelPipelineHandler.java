package net.dodian.old.net.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.dodian.old.net.NetworkConstants;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.codec.LoginDecoder;
import net.dodian.old.net.codec.LoginEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles a channels events..
 * 
 * @author Swiffy
 */
@Component
public class ChannelPipelineHandler extends ChannelInitializer<SocketChannel> implements BeanFactoryAware {

	/**
	 * The part of the pipeline that limits connections, and checks for any banned hosts.
	 */
	private final ChannelFilter FILTER = new ChannelFilter();

	/**
	 * The part of the pipeline that handles exceptions caught, channels being read, in-active
	 * channels, and channel triggered events.
	 */
	private final ChannelEventHandler channelEventHandler;
	private BeanFactory beanFactory;

	@Autowired
	public ChannelPipelineHandler(ChannelEventHandler channelEventHandler) {
		this.channelEventHandler = channelEventHandler;
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		final ChannelPipeline pipeline = channel.pipeline();

		PlayerSession playerSession = beanFactory.getBean(PlayerSession.class);

		playerSession.setChannel(channel);
	    channel.attr(NetworkConstants.SESSION_KEY).setIfAbsent(playerSession);
	    
	    pipeline.addLast("channel-filter", FILTER);
	    pipeline.addLast("decoder", new LoginDecoder());
	    pipeline.addLast("encoder", new LoginEncoder());
	    pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.SESSION_TIMEOUT, 0, 0));
	    pipeline.addLast("channel-handler", channelEventHandler);
		
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
