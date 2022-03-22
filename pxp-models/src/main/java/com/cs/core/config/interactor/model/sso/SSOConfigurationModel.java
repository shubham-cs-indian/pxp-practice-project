package com.cs.core.config.interactor.model.sso;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.sso.ISSOConfiguration;
import com.cs.core.config.interactor.entity.sso.SSOConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SSOConfigurationModel implements ISSOConfigurationModel {
  
  private static final long serialVersionUID = 1L;
  private ISSOConfiguration entity;
  
  public SSOConfigurationModel()
  {
    entity = new SSOConfiguration();
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
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  public String getDomain()
  {
    return entity.getDomain();
  }
  
  @Override
  public void setDomain(String domain)
  {
    entity.setDomain(domain);
  }
  
  public String getIdp()
  {
    return entity.getIdp();
  }
  
  @Override
  public void setIdp(String idp)
  {
    entity.setIdp(idp);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @JsonIgnore
  @Override
  public String getDescription()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setDescription(String description)
  {
  }
  
  @JsonIgnore
  @Override
  public String getTooltip()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTooltip(String tooltip)
  {
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsMandatory()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsStandard()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsStandard(Boolean isStandard)
  {
  }
  
  @JsonIgnore
  @Override
  public String getPlaceholder()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPlaceholder(String placeholder)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLabel()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLabel(String label)
  {
  }
  
  @JsonIgnore
  @Override
  public String getIcon()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIcon(String icon)
  {
  }
  
  @JsonIgnore
  @Override
  public String getIconKey()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIconKey(String icon)
  {
  }
  
  @JsonIgnore
  @Override
  public String getType()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setType(String type)
  {
  }
}
