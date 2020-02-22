package net.dodian.old.net.channel;

import com.google.common.base.Objects;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.dodian.extend.events.player.PlayerSessionEventListener;
import net.dodian.old.net.NetworkConstants;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.SessionState;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of netty's {@link SimpleChannelInboundHandler} to handle
 * all of netty's incoming events..
 * 
 * @author Professor Oak
 */
@Sharable
@Component
@Scope("prototype")
public final class ChannelEventHandler extends SimpleChannelInboundHandler<Object> {

	public final List<PlayerSessionEventListener> playerSessionEvents;

	@Autowired
	public ChannelEventHandler(Optional<List<PlayerSessionEventListener>> playerSessionEvents) {
		this.playerSessionEvents = playerSessionEvents.orElse(new ArrayList<>());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();

		if (session == null) {
			throw new IllegalStateException("session == null");
		}

		playerSessionEvents.forEach(event -> event.onInitialize(session, msg));
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();

		if (session == null) {
			throw new IllegalStateException("session == null");
		}

		Player player = session.getPlayer();

		if(player == null) {
			return;
		}

		//Queue the player for logout
		if(player.getSession().getState() == SessionState.LOGGED_IN || player.getSession().getState() == SessionState.REQUESTED_LOG_OUT) {
			if(!World.getPlayerRemoveQueue().contains(player)) {
				
				//Close all open interfaces..
				player.getPacketSender().sendInterfaceRemoval();
				
				//After 60 seconds, it will force a logout.
				player.getForcedLogoutTimer().start(60);
				
				//Add player to logout queue.
				World.getPlayerRemoveQueue().add(player);
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (!NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream()
				.anyMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}

		ctx.channel().close();
	}

}
