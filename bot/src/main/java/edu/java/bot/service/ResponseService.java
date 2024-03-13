package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.dto.UserStatus;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import edu.java.bot.processor.LinkChecker;
import edu.java.bot.processor.commands.Command;
import java.net.URI;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class ResponseService {
    private static final String NOT_VALID_LINK = "Not valid link";
    private static final String SUCCESS_ADD = "Link successfully added for tracking";
    private static final String SUCCESS_REMOVE = "Link successfully removed from tracking list";
    private static final String NOT_SUPPORTED = "Operation not supported";
    private final Map<String, Command> commandMap;
    private final LinkChecker linkChecker;
    private final ScrapperClient scrapperClient;

    public ResponseService(Map<String, Command> commandMap, LinkChecker linkChecker, ScrapperClient scrapperClient) {
        this.commandMap = commandMap;
        this.linkChecker = linkChecker;
        this.scrapperClient = scrapperClient;
    }

    public String getAnswer(Update update) {
        var message = update.message().text();
        if (message.startsWith("/")) {
            if (commandMap.containsKey(message)) {
                return commandMap.get(message).execute(update);
            } else {
                return NOT_SUPPORTED;
            }
        }
        return nonCommandHandler(update);
    }

    private String nonCommandHandler(Update update) {
        Long chatId = update.message().chat().id();

        try {
            UserStatus status = scrapperClient.getUserStatus(chatId).block();
            if (status != UserStatus.BASE) {
                String link = update.message().text();
                linkChecker.loadLink(link);
                if (!linkChecker.isPossibleToTrack()) {
                    scrapperClient.setUserStatus(chatId, UserStatus.BASE).block();
                    return NOT_VALID_LINK;
                }
                return linkProcess(chatId, link, status);
            }
        } catch (CustomClientException e) {
            return e.getClientErrorResponse().exceptionMessage();
        }
        return NOT_SUPPORTED;
    }

    private String linkProcess(Long chatId, String link, UserStatus status) {
        if (status == UserStatus.TRACK_LINK) {
            scrapperClient.addLink(chatId, URI.create(link));
            return SUCCESS_ADD;
        } else {
            scrapperClient.removeLink(chatId, URI.create(link));
            return SUCCESS_REMOVE;
        }
    }
}
