package com.cs.core.config.interactor.model.mapping;

public class TagValueMappingModel implements ITagValueMappingModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          tagValue;
  protected String          mappedTagValueId;
  protected Boolean         isIgnoreCase     = false;
  protected Boolean         isAutomapped     = true;
  
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
  public String getTagValue()
  {
    
    return tagValue;
  }
  
  @Override
  public void setTagValue(String tagValue)
  {
    this.tagValue = tagValue;
  }
  
  @Override
  public String getMappedTagValueId()
  {
    
    return mappedTagValueId;
  }
  
  @Override
  public void setMappedTagValueId(String mappedTagValueId)
  {
    this.mappedTagValueId = mappedTagValueId;
  }
  
  @Override
  public Boolean getIsIgnoreCase()
  {
    
    return isIgnoreCase;
  }
  
  @Override
  public void setIsIgnoreCase(Boolean isIgnoreCase)
  {
    this.isIgnoreCase = isIgnoreCase;
  }
  
  @Override
  public Boolean getIsAutomapped()
  {
    return isAutomapped;
  }
  
  @Override
  public void setIsAutomapped(Boolean isAutomapped)
  {
    this.isAutomapped = isAutomapped;
  }
}
