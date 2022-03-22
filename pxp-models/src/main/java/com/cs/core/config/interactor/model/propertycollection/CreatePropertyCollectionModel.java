package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollectionElement;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class CreatePropertyCollectionModel implements ICreatePropertyCollectionModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected IPropertyCollection entity;
  protected IAddedTabModel      tab;
  
  public CreatePropertyCollectionModel()
  {
    this.entity = new PropertyCollection();
  }
  
  public CreatePropertyCollectionModel(IPropertyCollection entity)
  {
    this.entity = entity;
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
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
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
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public List<IPropertyCollectionElement> getElements()
  {
    return entity.getElements();
  }

  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setElements(List<IPropertyCollectionElement> elements)
  {
    entity.setElements(elements);
  }

  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public Boolean getIsForXRay()
  {
    return entity.getIsForXRay();
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    entity.setIsForXRay(isForXRay);
  }
  
  @Override
  public Boolean getIsDefaultForXRay()
  {
    return entity.getIsDefaultForXRay();
  }
  
  @Override
  public void setIsDefaultForXRay(Boolean isDefaultForXRay)
  {
    entity.setIsDefaultForXRay(isDefaultForXRay);
  }
  
  @Override
  public IAddedTabModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setTab(IAddedTabModel tab)
  {
    this.tab = tab;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return entity.getAttributeIds();
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    entity.setAttributeIds(attributeIds);
  }
  
  @Override
  public List<String> getTagIds()
  {
    return entity.getTagIds();
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    entity.setTagIds(tagIds);
  }

  @Override
  public List<String> getPropertySequenceIds()
  {
    return entity.getPropertySequenceIds();
  }

  @Override
  public void setPropertySequenceIds(List<String> propertySequenceIds)
  {
    entity.setPropertySequenceIds(propertySequenceIds);
  }
}
