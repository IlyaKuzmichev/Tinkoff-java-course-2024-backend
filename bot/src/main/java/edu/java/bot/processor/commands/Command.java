package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import java.util.Optional;

public abstract class Command {
    protected UserRegistry userRegistry;
    private Command nextCommand;

    public Command() {}

    public Command(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    public abstract String command();

    public abstract String description();

    protected abstract Optional<String> execute(Update update);

    public Optional<String> handle(Update update) {
        if (update.message().text().equals(command())) {
            return execute(update);
        }
        if (nextCommand == null) {
            return Optional.empty();
        }
        return nextCommand.handle(update);
    }

    public void setNextCommand(Command nextCommand) {
        this.nextCommand = nextCommand;
    }
}
