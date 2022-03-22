package com.cs.core.runtime.interactor.model.mail;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MailNotificationModel implements IMailNotificationModel {


  private static final long serialVersionUID = 1L;

  protected String          sender;
  
  protected List<String>    recipients       = new ArrayList<String>();
  
  protected String          subject;
  
  protected String          body;
  
  protected List<String>    recipientsCC     = new ArrayList<String>();
  
  protected List<String>    recipientsBCC    = new ArrayList<String>();
  
  protected List<File>      attachments      = new ArrayList<>();

  protected String          contentType;

  @Override
  public String getSender()
  {
    return sender;
  }
  
  @Override
  public void setSender(String sender)
  {
    this.sender = sender;
  }
  
  @Override
  public List<String> getRecipients()
  {
    return recipients;
  }
  
  @Override
  public void setRecipients(List<String> recipients)
  {
    this.recipients = recipients;
  }
  
  @Override
  public Boolean addRecipient(String recipient)
  {
    return this.recipients.add(recipient);
  }
  
  @Override
  public String getSubject()
  {
    return subject;
  }
  
  @Override
  public void setSubject(String subject)
  {
    this.subject = subject;
  }
  
  @Override
  public String getBody()
  {
    return body;
  }
  
  @Override
  public void setBody(String body)
  {
    this.body = body;
  }

  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<String> getRecipientsCC()
  {
    return recipientsCC;
  }
  
  @Override
  public void setRecipientsCC(List<String> recipientsCC)
  {
    this.recipientsCC = recipientsCC;
  }
  
  @Override
  public List<String> getRecipientsBCC()
  {
    return recipientsBCC;
  }
  
  @Override
  public void setRecipientsBCC(List<String> recipientsBCC)
  {
    this.recipientsBCC = recipientsBCC;
  }

  @Override
  public List<File> getAttachments()
  {
    return attachments;
  }

  @Override
  public void setAttachments(List<File> attachments)
  {
    this.attachments = attachments;
  }

  @Override
  public String getContentType()
  {
    return contentType;
  }

  @Override
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }


}
