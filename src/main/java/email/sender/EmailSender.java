package email.sender;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    private String username;
    private String password;
    private Session session;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

    }

    public void sendEmail(String to,String subject, String text) throws MessagingException{
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
        System.out.println("Email sent to " + to);
    }
    public String sendBatchEmails(List<String> recipients, String subject, String text) {
       StringBuilder results = new StringBuilder();
        for (String to : recipients) {
            try {
                sendEmail(to, subject, text);
                results.append("Email sent to ").append(to).append("\n");
            }catch (MessagingException e) {
                results.append("Failed to send to ").append(to).append(": ").append(e.getMessage()).append("\n");
            }
        }
        return results.toString();

    }

}
