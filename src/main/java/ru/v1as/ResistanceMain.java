package ru.v1as;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.v1as.action.ActionProcessor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ivlasishen
 * on 12.04.2017.
 */
public class ResistanceMain {

    public static void main(String[] args) throws TelegramApiRequestException {
        System.out.println("Bot starting");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        ActionProcessor processor = new ActionProcessor();
        ResistanceBot bot = new ResistanceBot(processor);
        processor.setSender(bot);
        telegramBotsApi.registerBot(bot);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(bot::processActions, 0, 1, TimeUnit.SECONDS);
        System.out.println("Bot started!");
    }

}
