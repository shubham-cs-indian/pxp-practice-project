package com.cs.core.runtime.interactor.model.version;

public class KlassInstanceVersionGetModel implements IKlassInstanceVersionGetModel {
  
  private static final long serialVersionUID = 1L;
  protected int             from;
  protected int             size;
  protected String          id;
  protected String          templateId;
  
  @Override
  public int getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
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
  public String getTemplateId()
  {
    
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
}
