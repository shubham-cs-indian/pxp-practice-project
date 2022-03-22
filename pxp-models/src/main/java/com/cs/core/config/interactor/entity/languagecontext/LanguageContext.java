package com.cs.core.config.interactor.entity.languagecontext;

import java.util.ArrayList;
import java.util.List;

public class LanguageContext implements ILanguageContext {
  
  private static final long serialVersionUID     = 1L;
  protected String          id;
  protected String          name;
  protected String          languageId;
  protected Boolean         isDefault;
  protected List<String>    attributeInstanceIds = new ArrayList<>();
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getLanguageId()
  {
    return languageId;
  }
  
  @Override
  public void setLanguageId(String languageId)
  {
    this.languageId = languageId;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  @Override
  public List<String> getAttributeInstanceIds()
  {
    return attributeInstanceIds;
  }
  
  @Override
  public void setAttributeInstanceIds(List<String> attributeInstanceIds)
  {
    this.attributeInstanceIds = attributeInstanceIds;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
