package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.processor.LinkChecker;
import edu.java.bot.processor.commands.CommandHandler;
import edu.java.bot.processor.trackers.GitHubTracker;
import edu.java.bot.processor.trackers.StackOverflowTracker;
import edu.java.bot.processor.trackers.URITracker;
import edu.java.bot.service.ResponseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
        ApplicationConfig appConfig = new ApplicationConfig("");
        var bot = new TelegramBot(appConfig.telegramToken());
        UserRegistry userRegistry = new UserRegistry();
        CommandHandler commandHandler = new CommandHandler(userRegistry);
        URITracker trackers = new GitHubTracker(new StackOverflowTracker(null));
        LinkChecker linkChecker = new LinkChecker(trackers);
        var responseService = new ResponseService(commandHandler, userRegistry, linkChecker);
        Bot myBot = new Bot(bot, responseService);
        myBot.start();
    }
}
