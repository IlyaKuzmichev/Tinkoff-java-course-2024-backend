package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.scrapper.RestScrapperClient;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.database.UserRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class CommandTest {
    @Mock
    protected Update update;
    @Mock
    protected Message message;
    @Mock
    protected Chat chat;
    @Mock
    protected ScrapperClient scrapperClient;
    protected Long chatId;

    @BeforeEach
    public void mocTestPreprocessor() {
        chatId = 1984L;

        Mockito.doReturn(message).when(update).message();
        Mockito.lenient().doReturn(chat).when(message).chat();
        Mockito.lenient().doReturn(chatId).when(chat).id();
    }
}
