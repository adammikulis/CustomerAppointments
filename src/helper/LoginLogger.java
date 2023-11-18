package helper;

import model.AppointmentList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginLogger {

    private static String filename = "login_activity.txt";
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Readable format without decimals

    public static void logLoginAttempt(String username, String password, boolean loginSuccess) throws IOException {
        try (FileWriter fw = new FileWriter(filename, true); // Appends to existing file or creates one if not existing
        PrintWriter pw = new PrintWriter(fw)) {
            String formattedDateTime = AppointmentList.convertLocalToUTC(LocalDateTime.now()).format(dateTimeFormat);
            pw.printf(String.format("Username: %s, Password: %s, DateTime (UTC): %s, Success: %s%n", username, password, formattedDateTime, loginSuccess));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
