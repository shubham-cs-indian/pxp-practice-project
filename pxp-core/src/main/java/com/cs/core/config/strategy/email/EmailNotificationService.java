package com.cs.core.config.strategy.email;

import java.io.File;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.mail.IMailNotificationModel;

@Component
public class EmailNotificationService implements IEmailNotificationService {
  
  @Autowired
  EmailNotificationConfiguration emailNotificationConfiguration;
  
  @Value("${pxpconfiguration.smtpUsername}")
  private String                 smtpUsername;
  
  public IMailNotificationModel execute(IMailNotificationModel model) throws Exception
  {
    try {
      
      emailNotificationConfiguration.createSessionInstance();
      Message message = new MimeMessage(emailNotificationConfiguration.getSessionInstance());
      
      message.setFrom(new InternetAddress(smtpUsername));
      List<String> recipients = model.getRecipients();
      for (String toAddress : recipients) {
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
      }
      List<String> recipientsCC = model.getRecipientsCC();
      for (String toAddress : recipientsCC) {
        message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(toAddress));
      }
      List<String> recipientsBCC = model.getRecipientsBCC();
      for (String toAddress : recipientsBCC) {
        message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(toAddress));
      }
      message.setSubject(model.getSubject());
      
      Multipart multipartFile = new MimeMultipart();
      
      BodyPart mailBodyPart = new MimeBodyPart();
      if (StringUtils.isBlank(model.getContentType())) {
        mailBodyPart.setText(model.getBody());
      }
      else {
        mailBodyPart.setContent(model.getBody(), model.getContentType());
      }
      multipartFile.addBodyPart(mailBodyPart);
      List<File> attachments = model.getAttachments();
      if (attachments != null && !attachments.isEmpty()) {
        for (File attachment : attachments) {
          MimeBodyPart dataBodyPart = new MimeBodyPart();
          dataBodyPart.attachFile(attachment);
          multipartFile.addBodyPart(dataBodyPart);
        }
      }
      message.setContent(multipartFile);
      // Send message
      Transport.send(message);
    }
    catch (MessagingException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
      throw new RuntimeException(e);
    }
    return null;
  }
  
}
