package net.dodian.old.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.extend.events.player.PlayerSessionAndCharacterLoadEventListener;
import net.dodian.old.net.codec.PacketDecoder;
import net.dodian.old.net.codec.PacketEncoder;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.net.login.LoginResponsePacket;
import net.dodian.old.net.login.LoginResponses;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketBuilder;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.util.Misc;
import net.dodian.old.util.PlayerPunishment;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.PacketProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static net.dodian.old.net.login.LoginResponses.*;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author Lare96
 * @author Swiffy
 * @editor Professor Oak
 */
@Data
@Component
@Scope("prototype")
public class PlayerSession {


	/**
	 * The queue which contains PRIORITIZED packets that will be handled on the next sequence.
	 */
	private final Queue<Packet> prioritizedPacketsQueue = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of packets that will be handled on the next sequence.
	 */
	private final Queue<Packet> packetsQueue = new ConcurrentLinkedQueue<>();
	private final LoginResponses loginResponses;
	private final PacketProvider packetProvider;

	/**
	 * The channel that will manage the connection for this player.
	 */
	private Channel channel;

	/**
	 * The player I/O operations will be executed for.
	 */
	private Player player;

	/**
	 * The current state of this I/O session.
	 */
	private SessionState state = SessionState.LOGGING_IN;

	@Autowired
	public PlayerSession(LoginResponses loginResponses, Player player, PacketProvider packetProvider) {
		this.loginResponses = loginResponses;
		this.player = player;
		this.packetProvider = packetProvider;
		this.player.setSession(this);
	}

	/**
	 * Queues a recently decoded packet received from the channel.
	 * 
	 * @param msg 	The packet that should be queued.
	 */
	public void queuePacket(Packet msg) {
		
		//Are our queues already full?
		//A player may be packet flooding.
		//Simply don't add more packets to the queues.
		int total_size = (packetsQueue.size() + prioritizedPacketsQueue.size());
		if(total_size >= NetworkConstants.PACKET_PROCESS_LIMIT) {
			return;
		}
		
		//Add the packet to the queue.
		//If it's prioritized, add it to the prioritized queue instead.
		if(msg.getOpcode() == PacketConstants.EQUIP_ITEM_OPCODE ||
				msg.getOpcode() == PacketConstants.SPECIAL_ATTACK_OPCODE
				|| msg.getOpcode() == PacketConstants.FIRST_ITEM_ACTION_OPCODE) {
			prioritizedPacketsQueue.add(msg);
		} else {
			packetsQueue.add(msg);
		}
	}

	/**
	 * Processes all of the queued messages from the {@link PacketDecoder} by
	 * polling the internal queue, and then handling them via the handleInputMessage.
	 * This method is called EACH GAME CYCLE.
	 * 
	 * @param priorityOnly	Should only prioritized packets be read?
	 */
	public void handleQueuedPackets(boolean priorityOnly) {

		Packet msg;

		int processed = 0;

		while((msg = prioritizedPacketsQueue.poll()) != null && ++processed < NetworkConstants.PACKET_PROCESS_LIMIT) {
			processPacket(msg);
		}


		if(priorityOnly) {
			return;
		}

		while((msg = packetsQueue.poll()) != null && ++processed < NetworkConstants.PACKET_PROCESS_LIMIT) {
			processPacket(msg);
		}
	}

	/**
	 * Handles an incoming message.
	 * @param msg	The message to handle.
	 */
	public void processPacket(Packet msg) {
		System.out.println("Opcode: " + msg.getOpcode());
		//PacketConstants.PACKETS[msg.getOpcode()].handleMessage(player, msg);
		packetProvider.handlePacket(msg, this.player);
	}

	/**
	 * Queues the {@code msg} for this session to be encoded and sent to the
	 * client.
	 *
	 * @param builder 	the packet to queue.
	 */
	public void write(PacketBuilder builder) {
		try {

			Packet packet = builder.toPacket();
			channel.write(packet);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Processes a packet immediately to be sent to the client.
	 *
	 * @param builder 	the packet to send.
	 */
	public void writeAndFlush(PacketBuilder builder) {
		try {

			Packet packet = builder.toPacket();
			channel.writeAndFlush(packet);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Flushes the channel.
	 */
	public void flush() {
		try {
			channel.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the player I/O operations will be executed for.
	 *
	 * @return the player I/O operations.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the current state of this I/O session.
	 *
	 * @return the current state.
	 */
	public SessionState getState() {
		return state;
	}

	/**
	 * Sets the value for {@link PlayerSession#state}.
	 *
	 * @param state
	 *            the new value to set.
	 */
	public void setState(SessionState state) {
		this.state = state;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Optional<Integer> checkIfAbleToLogin(LoginDetailsMessage loginDetailsMessage) {
		if (World.getPlayers().isFull()) {
			return Optional.of(LOGIN_WORLD_FULL);
		} else if(Server.isUpdating()) {
			return Optional.of(LOGIN_GAME_UPDATE);
		} else if (player.getUsername().startsWith(" ") || player.getUsername().endsWith(" ") || !Misc.isValidName(player.getUsername())) {
			return Optional.of(INVALID_CREDENTIALS_COMBINATION);
		} else if(loginDetailsMessage.getClientVersion() != GameConstants.GAME_VERSION || loginDetailsMessage.getUid() != GameConstants.GAME_UID) {
			return Optional.of(OLD_CLIENT_VERSION);
		} else if(World.getPlayerByName(player.getUsername()) != null || World.getPlayerRemoveQueue().contains(player) || World.getPlayerAddQueue().contains(player)) {
			return Optional.of(LOGIN_ACCOUNT_ONLINE);
		} else if (PlayerPunishment.banned(player.getUsername())) {
			return Optional.of(LOGIN_DISABLED_ACCOUNT);
		} else if (PlayerPunishment.IPBanned(loginDetailsMessage.getHost())) {
			return Optional.of(LOGIN_DISABLED_IP);
		}

		return Optional.empty();
	}

	public void login(LoginDetailsMessage loginDetailsMessage, PlayerSessionAndCharacterLoadEventListener playerSessionEventListener) {
		Player player = this.getPlayer();

		SocketChannel channel = (SocketChannel) loginDetailsMessage.getContext().channel();

		//Update the player
		player.setUsername(loginDetailsMessage.getUsername());
		player.setLongUsername(Misc.stringToLong(loginDetailsMessage.getUsername()));
		player.setPassword(loginDetailsMessage.getPassword());
		player.setHostAddress(loginDetailsMessage.getHost());

		//Get the response code
		Optional<Integer> optionalCanLogin = this.checkIfAbleToLogin(loginDetailsMessage);
		int responseCode = optionalCanLogin.orElse(LOGIN_SUCCESSFUL);

		if(responseCode == LOGIN_SUCCESSFUL) {
			Optional<Integer> optionalResponseCode = playerSessionEventListener.onCharacterLoad(player);
			responseCode = optionalResponseCode.orElse(LOGIN_REJECT_SESSION);
		}

		if(responseCode == NEW_ACCOUNT) {
			player.setNewPlayer(true);
			responseCode = LOGIN_SUCCESSFUL;
		}

		//Write the response and flush the channel
		ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(responseCode, player.getRights()));

		//Close the channel after sending the response if it wasn't a successful login
		if(responseCode != LOGIN_SUCCESSFUL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		playerSessionEventListener.onLoginSuccessful(player);

		//Wait...
		future.awaitUninterruptibly();

		//Replace decoder/encoder to packets
		channel.pipeline().replace("encoder", "encoder",
				new PacketEncoder(loginDetailsMessage.getEncryptor()));

		channel.pipeline().replace("decoder", "decoder",
				new PacketDecoder(loginDetailsMessage.getDecryptor()));

		//Queue the login
		if(!World.getPlayerAddQueue().contains(player)) {
			World.getPlayerAddQueue().add(player);
		}
	}
}
