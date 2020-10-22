package net.dodian.discord.listeners.commands;

import lombok.SneakyThrows;
import net.dodian.config.ApplicationConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CommandProvider {

    private final List<CommandBase> commands;
    private final ApplicationConfiguration configuration;

    @Autowired
    public CommandProvider(List<CommandBase> commands, ApplicationConfiguration configuration) {
        this.commands = commands;
        this.configuration = configuration;
    }

    public String parseCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        int prefixLength = configuration.getCommandPrefix().length();
        String commandWithoutPrefix = message.substring(prefixLength);
        String[] args = commandWithoutPrefix.split(" ");

        if (args.length == 0) {
            return null;
        }

        String commandName = args[0];
        String argsWithoutName = commandWithoutPrefix.substring(Math.min(commandName.length() + 1, commandWithoutPrefix.length()));

        Optional<CommandBase> command = commands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(commandName)).findFirst();
        return command
                .map(commandBase -> parseCommand(
                    commandBase,
                    (argsWithoutName.isEmpty() ? null : argsWithoutName.split(" ")),
                    event))
                .orElse(null);
    }

    @SneakyThrows
    private String parseCommand(CommandBase context, String[] args, MessageReceivedEvent event) {
        context.setEvent(event);

        if(!context.isPicoCliCommand()) {
            context.execute(args);
            return "";
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        try {
            CommandLine commandLine = new CommandLine(context)
                    .setOut(printWriter)
                    .setCaseInsensitiveEnumValuesAllowed(true);
            if (args != null && args[0].equalsIgnoreCase("-h")) {
                commandLine.usage(printWriter);
            } else {
                if(args != null) {
                    commandLine.parseArgs(args);
                }
                context.execute(args);
            }
        } catch (Exception e) {
            printWriter.write(e.getMessage());
        } finally {
            stringWriter.close();
            printWriter.close();
            return stringWriter.toString();
        }
    }
}
