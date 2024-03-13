package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/list")
@Qualifier("command_list_add")
public final class ListCommand implements Command {
    private static final String NAME = "/list";
    private static final String DESCRIPTION = "Shows list of tracking links";
    private static final String NO_REGISTRATION = "Need to be registered for tracking links";
    private static final String NO_LINKS = "You have no links for tracking";
    private static final String FIRST_LINE = "Your tracking links:\n";
    private static final String FORMATTED_STRING = "%d. %s\n";
    private final ScrapperClient scrapperClient;

    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String commandName() {
        return NAME;
    }

    @Override
    public String commandDescription() {
        return DESCRIPTION;
    }

    @Override
    public String execute(Update update) {
        StringBuilder builder = new StringBuilder();
        try {
            var response = scrapperClient.listLinks(update.message().chat().id()).block();
            if (response.size() == 0) {
                return NO_LINKS;
            }
            builder.append(FIRST_LINE);
            int counter = 1;
            for (var link : response.links()) {
                builder.append(FORMATTED_STRING.formatted(counter++, link));
            }
        } catch (CustomClientException e) {
            return e.getClientErrorResponse().exceptionMessage();
        }
        return builder.toString();
    }
}
