package com.cs.ui.runtime.controller.usecase.message;

public class Message {
  
  private String from;
  private String to;
  private String message;
  private String templateInstanceId;
  private String templateType;
  private String templateName;
  
  @Override
  public String toString()
  {
    return super.toString();
  }
  
  public String getFrom()
  {
    return from;
  }
  
  public void setFrom(String from)
  {
    this.from = from;
  }
  
  public String getTo()
  {
    return to;
  }
  
  public void setTo(String to)
  {
    this.to = to;
  }
  
  public String getMessage()
  {
    return message;
  }
  
  public void setMessage(String content)
  {
    this.message = content;
  }
  
  public String getTemplateInstanceId()
  {
    return templateInstanceId;
  }
  
  public void setTemplateInstanceId(String templateId)
  {
    this.templateInstanceId = templateId;
  }
  
  public String getTemplateType()
  {
    return templateType;
  }
  
  public void setTemplateType(String templateType)
  {
    this.templateType = templateType;
  }
  
  public String getTemplateName()
  {
    return templateName;
  }
  
  public void setTemplateName(String templateName)
  {
    this.templateName = templateName;
  }
}
