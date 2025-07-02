package email.sender;
import java.util.List;

public class Main {
 public static void main(String[] args) {
     String yourEmail = "nick.kuczka@gmail.com";
     String yourAppPassword = "tmzcyvavacdqperv";

     EmailSender sender = new EmailSender(yourEmail, yourAppPassword);

     List<String> recipients = List.of(
             "fuelup727@gmail.com"


             );
     sender.sendBatchEmails(recipients, "test subject", "this is a test email. If you got this dont worry im testing an app.");
 }

}
