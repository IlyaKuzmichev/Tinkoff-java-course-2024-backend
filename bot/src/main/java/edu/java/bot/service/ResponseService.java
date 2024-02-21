package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.User;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import edu.java.bot.processor.LinkChecker;
import edu.java.bot.processor.commands.Command;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class ResponseService {
    private static final String NOT_VALID_LINK = "Not valid link";
    private static final String NOT_VALID_RESOURCE = "Not valid resource for tracking";
    private static final String ALREADY_TRACKING = "Link is already tracking";
    private static final String SUCCESS_ADD = "Link successfully added for tracking";
    private static final String NOT_CONTAINS_LINK = "Nothing to remove, you don't contain this link in tracking list";
    private static final String SUCCESS_REMOVE = "Link successfully removed from tracking list";
    private static final String NOT_SUPPORTED = "Operation not supported";
    private static final String NEED_REGISTRATION = "Please, pass the registration with /start command";
    private final Map<String, Command> commandMap;
    private final LinkChecker linkChecker;
    private final UserRegistry userRegistry;

    public ResponseService(Map<String, Command> commandMap, UserRegistry userRegistry, LinkChecker linkChecker) {
        this.commandMap = commandMap;
        this.userRegistry = userRegistry;
        this.linkChecker = linkChecker;
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
        var user = userRegistry.getUser(update.message().chat().id());
        if (user.isPresent() && user.get().getState() != UserState.BASE) {
            var message = userTrackingProcess(user.get(), update.message().text());
            user.get().setState(UserState.BASE);
            return message;
        }
        return user.isPresent() ? NOT_SUPPORTED : NEED_REGISTRATION;
    }

    private String userTrackingProcess(User user, String link) {
        linkChecker.loadLink(link);
        if (!linkChecker.isValidLink()) {
            return NOT_VALID_LINK;
        }
        if (!linkChecker.isPossibleToTrack()) {
            return NOT_VALID_RESOURCE;
        }
        return linkProcess(user, linkChecker.getHost(), link);
    }

    private String linkProcess(User user, String domain, String link) {
        if (user.getState() == UserState.WAIT_TRACK_URI) {
            return userRegistry.addLink(user, domain, link) ? SUCCESS_ADD : ALREADY_TRACKING;
        } else {
            return userRegistry.removeLink(user, domain, link) ? SUCCESS_REMOVE : NOT_CONTAINS_LINK;
        }
    }
}
