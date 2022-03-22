package com.cs.core.config.interactor.entity.comment;

import java.util.ArrayList;
import java.util.List;

public class Comment implements IComment {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          text;
  protected String          postedBy;
  protected String          timestamp;
  protected List<String>    attachments;
  protected String          icon;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getText()
  {
    return text;
  }
  
  @Override
  public void setText(String text)
  {
    this.text = text;
  }
  
  @Override
  public String getPostedBy()
  {
    return postedBy;
  }
  
  @Override
  public void setPostedBy(String postedBy)
  {
    this.postedBy = postedBy;
  }
  
  @Override
  public List<String> getAttachments()
  {
    if(attachments == null) {
      attachments = new ArrayList<String>();
    }
    return attachments;
  }
  
  @Override
  public void setAttachments(List<String> attachments)
  {
    this.attachments = attachments;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getTimestamp()
  {
    return timestamp;
  }
  
  @Override
  public void setTimestamp(String timestamp)
  {
    this.timestamp = timestamp;
  }
}
