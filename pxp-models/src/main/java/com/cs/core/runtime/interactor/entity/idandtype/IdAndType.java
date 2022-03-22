package com.cs.core.runtime.interactor.entity.idandtype;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IdAndType implements IIdAndType {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          type;
  
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  /** ************* ignored fields ***************** */
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
