package com.cs.core.config.authenticator;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrientDBAuthenticator {
  
  @Autowired
  protected String orientDBUser;
  
  @Autowired
  protected String orientDBPassword;
  
  @Bean
  public Authenticator getAuthenticator()
  {
    return new Authenticator()
    {
      
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(orientDBUser, orientDBPassword.toCharArray());
      }
    };
  }
}
