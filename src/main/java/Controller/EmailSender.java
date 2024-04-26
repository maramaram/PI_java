package Controller;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class EmailSender {
    public static void sendActivationEmail(String recipientEmail) {
        try {
            // Créer une session e-mail
            Session session = EmailConfig.getEmailSession();

            // Créer un objet MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("maram.bouziz@esprit.tn"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Activation de compte");
            message.setText("Bonjour,\n\nVotre compte a été activé avec succès.");

            // Envoyer l'e-mail
            Transport.send(message);
            System.out.println("E-mail d'activation envoyé avec succès à : " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail d'activation : " + e.getMessage());
        }
    }
}
