package Controller;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class EmailSender {
    private static final String EMAIL_FROM = "maram.bouaziz@esprit.tn";
    private static final String APP_PASSWORD = "lued iazj ssse lczp";


    public static void sendEmail(String email_to, String subject, String messageBody) throws Exception {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
        message.setSubject(subject);
        message.setText(messageBody);
        Transport.send(message);
    }

    private static Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM,APP_PASSWORD);
            }
        });
    }

    private static Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("email.smtp.auth", "true");
        prop.put("email.smtp.starttls.enable", "true");
        prop.put("email.smtp.host", "smtp.gmail.com");
        prop.put("email.smtp.port", "587");
        prop.put("email.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}
