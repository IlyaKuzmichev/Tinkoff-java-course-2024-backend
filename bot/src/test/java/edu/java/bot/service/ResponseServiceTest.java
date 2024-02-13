package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.User;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.database.UserState;
import edu.java.bot.processor.LinkChecker;
import edu.java.bot.processor.commands.CommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResponseServiceTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private UserRegistry userRegistry;
    @Mock
    private CommandHandler commandHandler;
    @Mock
    private LinkChecker linkChecker;
    @Mock
    private User user;
    Long chatId;
    ResponseService responseService;

    @BeforeEach
    public void setUp() {
        chatId = 42L;
        Mockito.doReturn(message).when(update).message();
        Mockito.doReturn(chat).when(message).chat();
        Mockito.doReturn(chatId).when(chat).id();

        responseService = new ResponseService(commandHandler, userRegistry, linkChecker);
    }
    @Test
    public void testLinkAddProcess() {
        Mockito.doReturn("https://github.com").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_TRACK_URI).when(user).getState();
        Mockito.doReturn(true).when(linkChecker).isValidLink();
        Mockito.doReturn(true).when(linkChecker).isPossibleToTrack();
        Mockito.doReturn("github.com").when(linkChecker).getHost();
        Mockito.doReturn(true).when(userRegistry).addLink(user, "github.com", "https://github.com");
        assertEquals("Link successfully added for tracking", responseService.getAnswer(update));
    }

    @Test
    public void testLinkRemoveProcess() {
        Mockito.doReturn("https://github.com").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_UNTRACK_URI).when(user).getState();
        Mockito.doReturn(true).when(linkChecker).isValidLink();
        Mockito.doReturn(true).when(linkChecker).isPossibleToTrack();
        Mockito.doReturn("github.com").when(linkChecker).getHost();
        Mockito.doReturn(true).when(userRegistry).removeLink(user, "github.com", "https://github.com");
        assertEquals("Link successfully removed from tracking list", responseService.getAnswer(update));
    }

    @Test
    public void testUnsupportedLinkAddProcess() {
        Mockito.doReturn("https://github.com").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_TRACK_URI).when(user).getState();
        Mockito.doReturn(true).when(linkChecker).isValidLink();
        Mockito.doReturn(true).when(linkChecker).isPossibleToTrack();
        Mockito.doReturn("github.com").when(linkChecker).getHost();
        Mockito.doReturn(false).when(userRegistry).addLink(user, "github.com", "https://github.com");
        assertEquals("Link is already tracking", responseService.getAnswer(update));
    }

    @Test
    public void testUnsupportedLinkRemoveProcess() {
        Mockito.doReturn("https://github.com").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_UNTRACK_URI).when(user).getState();
        Mockito.doReturn(true).when(linkChecker).isValidLink();
        Mockito.doReturn(true).when(linkChecker).isPossibleToTrack();
        Mockito.doReturn("github.com").when(linkChecker).getHost();
        Mockito.doReturn(false).when(userRegistry).removeLink(user, "github.com", "https://github.com");
        assertEquals("Nothing to remove, you don't contain this link in tracking list", responseService.getAnswer(update));
    }

    @Test
    public void testNotValidLinkForTracking() {
        Mockito.doReturn("invalid").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_UNTRACK_URI).when(user).getState();
        Mockito.doReturn(false).when(linkChecker).isValidLink();
        assertEquals("Not valid link", responseService.getAnswer(update));
    }

    @Test
    public void testNotValidResourceForTracking() {
        Mockito.doReturn("invalid").when(message).text();
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.WAIT_UNTRACK_URI).when(user).getState();
        Mockito.doReturn(true).when(linkChecker).isValidLink();
        Mockito.doReturn(false).when(linkChecker).isPossibleToTrack();
        assertEquals("Not valid resource for tracking", responseService.getAnswer(update));
    }

    @Test
    public void testNotValidMessageForTheBotReceived() {
        Mockito.doReturn(Optional.of(user)).when(userRegistry).getUser(chatId);
        Mockito.doReturn(UserState.BASE).when(user).getState();
        assertEquals("Operation not supported", responseService.getAnswer(update));
    }

    @Test
    public void testNoRegistrationOfUser() {
        Mockito.doReturn(Optional.empty()).when(userRegistry).getUser(chatId);
        Mockito.doReturn(Optional.empty()).when(commandHandler).handle(update);
        assertEquals("Please, pass the registration with /start command", responseService.getAnswer(update));
    }

    @Test
    public void testCommandReceived() {
        Mockito.doReturn(Optional.empty()).when(userRegistry).getUser(chatId);
        Mockito.doReturn(Optional.of("Testcase")).when(commandHandler).handle(update);
        assertEquals("Testcase", responseService.getAnswer(update));
    }
}
