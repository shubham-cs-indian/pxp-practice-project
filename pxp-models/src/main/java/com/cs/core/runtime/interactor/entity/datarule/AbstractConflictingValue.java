package com.cs.core.runtime.interactor.entity.datarule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractConflictingValue implements IConflictingValue {
  
  private static final long         serialVersionUID = 1L;
  
  protected String                  couplingType;
  protected IConflictingValueSource source;
  protected Boolean                 isMandatory;
  protected Boolean                 isShould;
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public IConflictingValueSource getSource()
  {
    return source;
  }
  
  @JsonDeserialize(as = AbstractConflictingValueSource.class)
  @Override
  public void setSource(IConflictingValueSource source)
  {
    this.source = source;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public Boolean getIsShould()
  {
    return isShould;
  }
  
  @Override
  public void setIsShould(Boolean isShould)
  {
    this.isShould = isShould;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
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
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
