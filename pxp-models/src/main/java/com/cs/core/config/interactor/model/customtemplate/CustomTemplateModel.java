package com.cs.core.config.interactor.model.customtemplate;

import com.cs.core.config.interactor.entity.customtemplate.CustomTemplate;
import com.cs.core.config.interactor.entity.customtemplate.ICustomTemplate;

import java.util.List;

public class CustomTemplateModel implements ICustomTemplateModel {
  
  private static final long serialVersionUID = 1L;
  protected ICustomTemplate customTemplate;
  
  public CustomTemplateModel()
  {
    customTemplate = new CustomTemplate();
  }
  
  @Override
  public List<String> getPropertyCollectionIds()
  {
    return customTemplate.getPropertyCollectionIds();
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    customTemplate.setPropertyCollectionIds(propertyCollectionIds);
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    return customTemplate.getRelationshipIds();
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    customTemplate.setRelationshipIds(relationshipIds);
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    return customTemplate.getNatureRelationshipIds();
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    customTemplate.setNatureRelationshipIds(natureRelationshipIds);
  }
  
  @Override
  public List<String> getContextIds()
  {
    return customTemplate.getContextIds();
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    customTemplate.setContextIds(contextIds);
  }
  
  @Override
  public String getLabel()
  {
    return customTemplate.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    customTemplate.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return customTemplate.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    customTemplate.setIcon(icon);
  }
  
  
  @Override
  public String getIconKey()
  {
    return customTemplate.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    customTemplate.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return customTemplate.getType();
  }
  
  @Override
  public void setType(String type)
  {
    customTemplate.setType(type);
  }
  
  @Override
  public String getCode()
  {
    return customTemplate.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    customTemplate.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return customTemplate.getId();
  }
  
  @Override
  public void setId(String id)
  {
    customTemplate.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return customTemplate.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    customTemplate.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return customTemplate.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    customTemplate.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return customTemplate.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    customTemplate.setLastModifiedBy(lastModifiedBy);
  }
  
}
