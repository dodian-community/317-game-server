package net.dodian;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fileserver.FileServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import net.dodian.game.events.EventsProvider;
import net.dodian.game.events.impl.server.ServerStartedUpEvent;
import net.dodian.game.events.impl.server.ServerStartingUpEvent;
import net.dodian.managers.DefinitionsManager;
import net.dodian.old.definitions.NpcDropDefinition;
import net.dodian.old.definitions.ObjectDefinition;
import net.dodian.old.definitions.ShopDefinition;
import net.dodian.old.engine.GameEngine;
import net.dodian.old.engine.task.impl.CombatPoisonEffect;
import net.dodian.old.net.NetworkConstants;
import net.dodian.old.net.channel.ChannelPipelineHandler;
import net.dodian.old.util.PlayerPunishment;
import net.dodian.old.util.ShutdownHook;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.model.dialogue.DialogueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Initializer {

    private final ChannelPipelineHandler channelPipelineHandler;
    private final GameEngine gameEngine;
    private final DefinitionsManager definitionsManager;
    private final EventsProvider eventsProvider;

    @Autowired
    public Initializer(
            ChannelPipelineHandler channelPipelineHandler,
            GameEngine gameEngine,
            DefinitionsManager definitionsManager,
            EventsProvider eventsProvider
    ) {
        this.channelPipelineHandler = channelPipelineHandler;
        this.gameEngine = gameEngine;
        this.definitionsManager = definitionsManager;
        this.eventsProvider = eventsProvider;
    }

    public void initialize() {
        this.eventsProvider.executeListeners(new ServerStartingUpEvent());

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        try {
            Server.getLogger().info("Initializing the game...");

            //Fileserver
            if(GameConstants.JAGGRAB_ENABLED) {
                FileServer.init();
            }

            final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());

            //DEFINITIONS
            Server.getLogger().info("Loading definitions...");

            this.definitionsManager.loadDefinitions();

            serviceLoader.execute(ObjectDefinition::init);
            serviceLoader.execute(RegionClipping::init);

            serviceLoader.execute(() -> ObjectDefinition.parse().load());
            serviceLoader.execute(() -> NpcDropDefinition.parse().load());
            serviceLoader.execute(() -> ShopDefinition.parse().load());
            serviceLoader.execute(() -> DialogueManager.parse().load());

            //OTHERS
            //serviceLoader.execute(ClanChatManager::init);
            serviceLoader.execute(CombatPoisonEffect.CombatPoisonData::init);
            serviceLoader.execute(PlayerPunishment::init);

            //Shutdown the loader
            serviceLoader.shutdown();

            //Make sure the loader is properly shut down
            if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
                throw new IllegalStateException("The background service load took too long!");

            //Bind the port...
            Server.getLogger().info("Binding port "+ NetworkConstants.GAME_PORT+"...");
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
            EventLoopGroup loopGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(loopGroup).channel(NioServerSocketChannel.class)
                    .childHandler(channelPipelineHandler).bind(NetworkConstants.GAME_PORT).syncUninterruptibly();


            //Start the game engine using a {@link ScheduledExecutorService}
            Server.getLogger().info("Starting game engine...");

            //Start game engine..
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build())
                    .scheduleAtFixedRate(gameEngine, 0, GameConstants.GAME_ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);

            Server.getLogger().info("The loader has finished loading utility tasks.");
            Server.getLogger().info("Elvarg is now online on port "+NetworkConstants.GAME_PORT+"!");
        } catch (Exception ex) {
            Server.getLogger().log(java.util.logging.Level.SEVERE, "Could not start Elvarg! Program terminated.", ex);
            System.exit(1);
        }

        this.eventsProvider.executeListeners(new ServerStartedUpEvent());
    }
}
