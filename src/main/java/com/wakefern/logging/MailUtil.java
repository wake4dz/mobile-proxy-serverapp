package com.wakefern.logging;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MailUtil {
	private final static Logger logger = LogManager.getLogger(MailUtil.class);

	public static void sendEmail(String subject, String body) {

		try {
			final String username = LogUtil.mailSmtpUser;

			//DZ: need to put these setting in the properties file
			final String password = LogUtil.mailSmtpPassword;

			Properties props = new Properties();
			props.put("mail.smtp.auth", LogUtil.mailSmtpAuth );
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", LogUtil.mailSmtpHost);
			props.put("mail.smtp.port", LogUtil.mailSmtpPort);

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			StringBuffer sb = new StringBuffer();

			for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
				sb.append(entry.getKey() + ", ");
			}

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(LogUtil.fromEmailAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sb.toString()));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

			logger.info("Done sending an email to " + sb.toString());
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
		}
	}
}