package com.cs.core.config.strategy.email;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailNotificationConfiguration {
  
  @Value("${pxpconfiguration.smtpUsername}")
  protected String smtpUsername;
  
  @Value("${pxpconfiguration.smtpPassword}")
  protected String smtpPassword;
  
  @Value("${pxpconfiguration.smtpHost}")
  protected String smtpHost;
  
  @Value("${pxpconfiguration.smtpPort}")
  protected String smtpPort;
  
  Session session;
  
  @PostConstruct
  public void createSessionInstance()
  {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);
    
    session = Session.getInstance(props, new javax.mail.Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(smtpUsername, smtpPassword);
      }
    });
  }
  
  public Session getSessionInstance()
  {
    return session;
  }
  
}
