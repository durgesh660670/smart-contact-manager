package com.durgesh.smart_contact_manager.service;

import org.springframework.stereotype.Service;
import java.util.Properties;
import java.io.File;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class EmailService {

    // this method is responsible for sending attachment email.
    // this method is responsible for sending text only email.
    public  boolean sendEmail(String message, String subject, String to, String from) {

        boolean f = false;
        // variable for gmail host
        String host = "smtp.gmail.com";

        // get the system properites

        Properties properties = System.getProperties();

        // setting important information to properties

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // step 1: to get the session object..

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String password = "xuvjemphqlhsvuiw";
                String userName = "dks660670@gmail.com";

                PasswordAuthentication pa = new PasswordAuthentication(userName, password);

                return pa;
            }
        });

        session.setDebug(true);

        // Step 2: compose the message

        MimeMessage m = new MimeMessage(session);

        try {
            m.setFrom(from);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
            m.setText(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // step 3: send the message using transport class

        try {
            Transport.send(m);
            f = true;
            System.out.println("message sent...");
        } catch (MessagingException e) {

            e.printStackTrace();
        }
        return f;

    }

    public  boolean sendAttach(String message, String subject, String to, String from) {

        boolean f = false;
        // variable for gmail host
        String host = "smtp.gmail.com";

        // get the system properites

        Properties properties = System.getProperties();

        // setting important information to properties

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // step 1: to get the session object..

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String password = "14565220@Du";
                String userName = "dks660670@gmail.com";

                PasswordAuthentication pa = new PasswordAuthentication(userName, password);

                return pa;
            }
        });

        // session.setDebug(true);

        // Step 2: compose the message

        MimeMessage m = new MimeMessage(session);

        try {
            m.setFrom(from);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);

            // adding attachment

            String path = "D:\\Node Js\\HTML and CSS practice projects\\Project2\\img\\bg.jpg";

            MimeMultipart mimeMultipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();

            MimeBodyPart fileBodyPart = new MimeBodyPart();

            textBodyPart.setText(message);

            File file = new File(path);

            // System.out.println("file..." + file);

            fileBodyPart.attachFile(file);
           
         // System.out.println(file);

            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(fileBodyPart);

            m.setContent(mimeMultipart);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // step 3: send the message using transport class

        try {
            Transport.send(m);
            f = true;
            System.out.println("message sent...");
        } catch (MessagingException e) {

            e.printStackTrace();
        }
        return f;

    }

}
