package com.cs.core.config.interactor.model.variantconfiguration;

import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.variantconfiguration.IVariantConfiguration;
import com.cs.core.config.interactor.entity.variantconfiguration.VariantConfiguration;

public class VariantConfigurationModel implements IVariantConfigurationModel {
  
  protected IVariantConfiguration entity;
  
  public VariantConfigurationModel()
  {
    this.entity = new VariantConfiguration();
    
  }
  
  public VariantConfigurationModel(IVariantConfiguration entity)
  {
    super();
    this.entity = entity;
  }
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public void setIsSelectVariant(Boolean isSelectVariant)
  {
    entity.setIsSelectVariant(isSelectVariant);
  }
  
  @Override
  public Boolean getIsSelectVariant()
  {
    return entity.getIsSelectVariant();
  }
  
  @Override
  public void setId(String Id)
  {
    entity.setId(Id);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
    
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
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    // TODO Auto-generated method stub
  }
  
}
