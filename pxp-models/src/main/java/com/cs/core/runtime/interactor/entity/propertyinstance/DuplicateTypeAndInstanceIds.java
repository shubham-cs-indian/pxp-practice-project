package com.cs.core.runtime.interactor.entity.propertyinstance;

import java.util.ArrayList;
import java.util.List;

public class DuplicateTypeAndInstanceIds implements IDuplicateTypeAndInstanceIds {
  
  private static final long serialVersionUID          = 1L;
  protected String          typeId;
  protected List<String>    duplicateKlassInstanceIds = new ArrayList<>();
  
  @Override
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }
  
  @Override
  public List<String> getDuplicateKlassInstanceIds()
  {
    return duplicateKlassInstanceIds;
  }
  
  @Override
  public void setDuplicateKlassInstanceIds(List<String> duplicateKlassInstanceIds)
  {
    this.duplicateKlassInstanceIds = duplicateKlassInstanceIds;
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
