package com.apo2.biblioteca.util;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilitário responsável pelo disparo de e-mails via protocolo SMTP (utilizando o Mailtrap para homologação).
 * Carrega dinamicamente os parâmetros do arquivo db.properties.
 * 
 * @version 1.0
 */
public class MailSender {

    private static String host;
    private static String port;
    private static String user;
    private static String password;
    private static boolean configurado = false;

    static {
        try (InputStream input = MailSender.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                host = prop.getProperty("smtp.host");
                port = prop.getProperty("smtp.port");
                user = prop.getProperty("smtp.user");
                password = prop.getProperty("smtp.password");
                
                // Valida se as propriedades não são os placeholders originais
                if (host != null && user != null && !user.contains("PLACEHOLDER")) {
                    configurado = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Falha ao carregar as configurações do servidor de e-mail: " + e.getMessage());
        }
    }

    /**
     * Envia um e-mail em formato HTML para um destinatário específico.
     * 
     * @param para     Endereço de e-mail do destinatário.
     * @param assunto  Assunto do e-mail.
     * @param conteudo Conteúdo do e-mail (HTML permitido).
     * @return boolean true se enviado com sucesso, false caso ocorra falha.
     */
    public static boolean enviarEmail(String para, String assunto, String conteudo) {
        if (!configurado) {
            System.out.println("[SMTP Simulado] Enviando e-mail para: " + para);
            System.out.println("Assunto: " + assunto);
            System.out.println("Conteúdo:\n" + conteudo);
            System.out.println("[Aviso] Para enviar e-mails reais via Mailtrap, configure suas credenciais válidas no arquivo db.properties.");
            return true; // Retorna true para simulação e não travar o fluxo de teste do aluno
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Cria a sessão com autenticação de credenciais
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
            message.setSubject(assunto);
            message.setContent(conteudo, "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Erro ao disparar e-mail via SMTP: " + e.getMessage());
            return false;
        }
    }
}

