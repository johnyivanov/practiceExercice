import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Location;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherBot extends TelegramLongPollingBot {
    private String botToken = "6573516434:AAFNqhrwqro6G-DEVquK8l34tLGZ_xgDg0o"; // Replace with your bot token
    private final String openWeatherMapApiKey = "c06ca35b245f1d78309844e201ce6caa"; // Replace with your OpenWeatherMap API key

    private static BotSession botSession;

    private static Timer dailyWeatherTimer = new Timer();
    private static long dailyWeatherInterval = 24 * 60 * 60 * 1000; // 24 hours

    private static List<Integer> subscribedUsers = new ArrayList<>();

   /* public WeatherBot() throws TelegramApiRequestException {
        // Initialize the bot
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        botSession = telegramBotsApi.registerBot(new WeatherBot());

    }
*/
    @Override
    public String getBotUsername() {
        return "WeatherIIIBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText();

            if (text.equals("/start")) {
                sendWelcomeMessage(chatId);
            } else if (text.equals("/subscribe")) {
                subscribeUser(chatId);
            } else if (text.equals("/unsubscribe")) {
                unsubscribeUser(chatId);
            } else if (text.equals("/weather")) {
                // Request location for weather
                sendLocationRequest(chatId);
            } else if (text.equals("/forecast")) {
                // Request daily weather forecast
                sendDailyForecast(chatId);
            } else if (message.hasLocation()) {
                Location location = message.getLocation();
                // Fetch and send the current weather based on the location
                sendCurrentWeather(chatId, location.getLatitude(), location.getLongitude());
            }
        }
    }

    private void sendWelcomeMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Welcome to Your Weather Bot!\n"
                + "You can use the following commands:\n"
                + "/subscribe - Subscribe to daily weather updates\n"
                + "/unsubscribe - Unsubscribe from daily updates\n"
                + "/weather - Get current weather based on your location\n"
                + "/forecast - Get daily weather forecast");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subscribeUser(long chatId) {
        if (!subscribedUsers.contains((int) chatId)) {
            subscribedUsers.add((int) chatId);
            sendSimpleMessage(chatId, "You are now subscribed to daily weather updates.");
        } else {
            sendSimpleMessage(chatId, "You are already subscribed to daily weather updates.");
        }
    }

    private void unsubscribeUser(long chatId) {
        if (subscribedUsers.contains((int) chatId)) {
            subscribedUsers.remove((Integer) (int) chatId);
            sendSimpleMessage(chatId, "You have unsubscribed from daily weather updates.");
        } else {
            sendSimpleMessage(chatId, "You are not subscribed to daily weather updates.");
        }
    }

    private void sendLocationRequest(long chatId) {
        SendLocation locationRequest = new SendLocation();
        locationRequest.setChatId(chatId);
        //locationRequest.setText("Please share your location so I can provide weather information.");
        locationRequest.setReplyMarkup(createLocationKeyboard());
        try {
            execute(locationRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createLocationKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton("Share Location");
        row.add(locationButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private void sendCurrentWeather(long chatId, double latitude, double longitude) {
        // Fetch and send the current weather information using OpenWeatherMap API
        // You will need to implement the logic to make the API request here
        // Parse the weather data and send it to the user as a message
        // Example: sendSimpleMessage(chatId, "The current weather is...");

        // For this example, let's assume we have a method called fetchCurrentWeather
        // that returns the weather information as a string.
        //String weatherInfo = fetchCurrentWeather(latitude, longitude);
        // sendSimpleMessage(chatId, weatherInfo);
    }

    private void sendSimpleMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void sendDailyForecast(long chatId) {
        // Implement logic to fetch and send daily weather forecast
        // This could be scheduled to run daily for subscribed users
        // Example: sendSimpleMessage(chatId, "Here's the daily weather forecast for today...");

        // For this example, we will send a static message.
        // sendSimpleMessage(chatId, "Here's the daily weather forecast for today...");
    }

    public static void main(String[] args) throws TelegramApiRequestException {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(new WeatherBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

       /* // Schedule sending daily weather forecast
        dailyWeatherTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int chatId : subscribedUsers) {
                    sendDailyForecast(chatId);
                }
            }
        }, 0, dailyWeatherInterval);
    }*/
    }

