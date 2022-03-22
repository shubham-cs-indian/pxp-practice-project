package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollectionElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SavePropertyCollectionModel implements ISavePropertyCollectionModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected IPropertyCollection              entity;
  protected List<IPropertyCollectionElement> addedElements;
  protected List<IPropertyCollectionElement> modifiedElements;
  protected List<IPropertyCollectionElement> deletedElements;
  protected String                           addedComplexAttributeId;
  protected IAddedTabModel                   addedTab;
  protected String                           deletedTab;
  
  public SavePropertyCollectionModel()
  {
    this.entity = new PropertyCollection();
  }
  
  public SavePropertyCollectionModel(IPropertyCollection entity)
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
  public List<IPropertyCollectionElement> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setAddedElements(List<IPropertyCollectionElement> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<IPropertyCollectionElement> getModifiedElements()
  {
    if (modifiedElements == null) {
      modifiedElements = new ArrayList<>();
    }
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setModifiedElements(List<IPropertyCollectionElement> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public List<IPropertyCollectionElement> getDeletedElements()
  {
    if (deletedElements == null) {
      deletedElements = new ArrayList<>();
    }
    return deletedElements;
  }
  
  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setDeletedElements(List<IPropertyCollectionElement> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public String getAddedComplexAttributeId()
  {
    return addedComplexAttributeId;
  }
  
  @Override
  public void setAddedComplexAttributeId(String addedComplexAttributeId)
  {
    this.addedComplexAttributeId = addedComplexAttributeId;
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
  public IAddedTabModel getAddedTab()
  {
    return addedTab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setAddedTab(IAddedTabModel addedTab)
  {
    this.addedTab = addedTab;
  }
  
  @Override
  public String getDeletedTab()
  {
    return deletedTab;
  }
  
  @Override
  public void setDeletedTab(String deletedTab)
  {
    this.deletedTab = deletedTab;
  }
  
  /**
   * *********************** ignore properties
   * ************************************************
   */
  @JsonIgnore
  @Override
  public List<String> getAttributeIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getTagIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTagIds(List<String> tagIds)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getPropertySequenceIds()
  {
    return null;
  }

  @JsonIgnore
  @Override
  public void setPropertySequenceIds(List<String> propertySequenceIds)
  {
    
  }
}
