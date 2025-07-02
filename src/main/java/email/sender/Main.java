package email.sender;
import java.util.List;

public class Main {
 public static void main(String[] args) {
     String yourEmail = "nick.kuczka@gmail.com";
     String yourAppPassword = "tmzcyvavacdqperv";

     EmailSender sender = new EmailSender(yourEmail, yourAppPassword);

//add emails to the list here
     List<String> recipients = List.of(
             ""


             );
     sender.sendBatchEmails(recipients, "test subject", "this is a test email. If you got this dont worry im testing an app.");
 }

}
