package helper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginLogger {

    private static String filename = "login_activity.txt";
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logLoginAttempt(String username, String password, boolean loginSuccess) throws IOException {
        try (FileWriter fw = new FileWriter(filename, true)) {
            String formattedDateTime = LocalDateTime.now().format(dateTimeFormat);
            fw.write(String.format("Login Attempt - Username: %s, Password: %s, DateTime: %s, Success %s%n", username, password, formattedDateTime, loginSuccess));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
