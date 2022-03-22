package com.cs.core.config.interactor.entity.goldenrecord;

import java.util.ArrayList;
import java.util.List;

public class MergeEffectType implements IMergeEffectType {
  
  private static final long serialVersionUID = 1L;
  protected String          entityId;
  protected String          type;
  protected String          entityType;
  protected List<String>    supplierIds;
  
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
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public List<String> getSupplierIds()
  {
    if (supplierIds == null) {
      supplierIds = new ArrayList<>();
    }
    return supplierIds;
  }
  
  @Override
  public void setSupplierIds(List<String> supplierIds)
  {
    this.supplierIds = supplierIds;
  }
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getCode()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCode(String code)
  {
    // TODO Auto-generated method stub
    
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
