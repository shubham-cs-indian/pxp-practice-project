package com.cs.core.config.interactor.entity.comment;

import java.io.Serializable;
import java.util.List;

public interface IComment extends Serializable {
  
  public static final String ID          = "id";
  public static final String TEXT        = "text";
  public static final String POSTED_BY   = "postedBy";
  public static final String TIMESTAMP   = "timeStamp";
  public static final String ATTACHMENTS = "attachments";
  public static final String ICON        = "icon";
  
  public String getId();
  
  public void setId(String id);
  
  public String getText();
  
  public void setText(String text);
  
  public String getPostedBy();
  
  public void setPostedBy(String postedBy);
  
  public List<String> getAttachments();
  
  public void setAttachments(List<String> attachments);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getTimestamp();
  
  public void setTimestamp(String timestamp);
}
