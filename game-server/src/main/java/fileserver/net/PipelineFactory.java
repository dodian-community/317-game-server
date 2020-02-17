package fileserver.net;

import net.dodian.old.net.NetworkConstants;

import fileserver.net.codec.Decoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * A {@link ChannelInitializer} used for handling a channel's tasks.
 * @author Professor Oak
 */
public final class PipelineFactory extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
				
		// decoders
		pipeline.addLast("decoder", new Decoder());
		
		// handler
		pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.SESSION_TIMEOUT, 0, 0));
		pipeline.addLast("handler", new ChannelHandler());
	}
}
