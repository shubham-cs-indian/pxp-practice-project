package com.cs.core.runtime.interactor.model.mail;

import java.io.File;
import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IMailNotificationModel extends IModel {
  
  public static final String SENDER         = "sender";
  public static final String RECIPIENTS     = "recipients";
  public static final String RECIPIENTS_CC  = "recipientsCC";
  public static final String RECIPIENTS_BCC = "recipientsBCC";
  public static final String RECIPIENT      = "recipient";
  public static final String SUBJECT        = "subject";
  public static final String BODY           = "body";
  public static final String CONTENTTYPE    = "contentType";

  
  public String getSender();
  
  public void setSender(String sender);
  
  public List<String> getRecipients();
  
  public void setRecipients(List<String> recipients);
  
  public Boolean addRecipient(String recipient);
  
  public String getSubject();
  
  public void setSubject(String subject);
  
  public String getBody();
  
  public void setBody(String body);
  
  public List<String> getRecipientsCC();
  
  public void setRecipientsCC(List<String> recipientsCC);
  
  public List<String> getRecipientsBCC();
  
  public void setRecipientsBCC(List<String> recipientsBCC);
  
  public String getContentType();
  public void setContentType(String contentType);
  
  public List<File> getAttachments();
  public void setAttachments(List<File> attachments);
}
