package com.cs.core.config.interactor.model.tabs;

import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.tabs.Tab;

import java.util.List;

public class TabModel implements ITabModel {
  
  private static final long serialVersionUID = 1L;
  protected ITab            tab;
  protected Integer         sequence;
  
  public TabModel()
  {
    tab = new Tab();
  }
  
  @Override
  public String getLabel()
  {
    return tab.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    tab.setLabel(label);
  }
  
  @Override
  public String getCode()
  {
    return tab.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    tab.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return tab.getId();
  }
  
  @Override
  public void setId(String id)
  {
    tab.setId(id);
  }
  
  @Override
  public List<String> getPropertySequenceList()
  {
    return tab.getPropertySequenceList();
  }
  
  @Override
  public void setPropertySequenceList(List<String> propertySequenceList)
  {
    tab.setPropertySequenceList(propertySequenceList);
  }
  
  @Override
  public Long getVersionId()
  {
    return tab.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    tab.getVersionId();
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return tab.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    tab.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return tab.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    tab.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Integer getSequence()
  {
    return sequence;
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    this.sequence = sequence;
  }
  
  @Override
  public List<String> getPropertyCollectionIds()
  {
    return tab.getPropertyCollectionIds();
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    tab.setPropertyCollectionIds(propertyCollectionIds);
  }
  
  @Override
  public List<String> getVariantContextIds()
  {
    return tab.getVariantContextIds();
  }
  
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    tab.setVariantContextIds(variantContextIds);
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    return tab.getRelationshipIds();
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.tab.setRelationshipIds(relationshipIds);
  }
  
  @Override
  public String getIcon()
  {
    return tab.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    tab.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return tab.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    tab.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return tab.getType();
  }
  
  @Override
  public void setType(String type)
  {
    tab.setType(type);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return tab.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    tab.setIsStandard(isStandard);
  }
}
