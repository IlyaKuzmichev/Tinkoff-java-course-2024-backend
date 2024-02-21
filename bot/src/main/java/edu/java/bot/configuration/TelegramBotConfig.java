package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.MyTelegramBot;
import edu.java.bot.processor.trackers.GitHubTracker;
import edu.java.bot.processor.trackers.StackOverflowTracker;
import edu.java.bot.processor.trackers.URITracker;
import edu.java.bot.service.ResponseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Bean
    public MyTelegramBot bot(ApplicationConfig applicationConfig, ResponseService responseService) {
        var bot = new MyTelegramBot(new TelegramBot(applicationConfig.telegramToken()), responseService);
        bot.start();
        return bot;
    }

    @Bean
    public URITracker uriTracker() {
        return new GitHubTracker(new StackOverflowTracker(null));
    }
}
