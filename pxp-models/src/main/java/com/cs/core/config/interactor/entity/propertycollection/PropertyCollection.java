package com.cs.core.config.interactor.entity.propertycollection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class PropertyCollection implements IPropertyCollection {
  
  private static final long                  serialVersionUID = 1L;
  
  protected String                           id;
  protected String                           label;
  protected String                           icon             = "";
  protected String                           iconKey          = "";
  protected List<IPropertyCollectionElement> elements;
  protected List<String>                     propertySequenceIds;
  protected Long                             versionId;
  protected Long                             versionTimestamp;
  protected String                           lastModifiedBy;
  protected String                           type;
  protected Boolean                          isStandard;
  protected Boolean                          isForXRay        = false;
  protected Boolean                          isDefaultForXRay = false;
  protected String                           code;
  protected List<String>                     attributeIds;
  protected List<String>                     tagIds;
  
  @Override
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new ArrayList<>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    if (tagIds == null) {
      tagIds = new ArrayList<>();
    }
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public List<IPropertyCollectionElement> getElements()
  {
    if (this.elements == null) {
      this.elements = new ArrayList<>();
    }
    return this.elements;
  }

  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setElements(List<IPropertyCollectionElement> elements)
  {
    this.elements = elements;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
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
  
  @Override
  public Boolean getIsForXRay()
  {
    return isForXRay;
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    this.isForXRay = isForXRay;
  }
  
  @Override
  public Boolean getIsDefaultForXRay()
  {
    return isDefaultForXRay;
  }
  
  @Override
  public void setIsDefaultForXRay(Boolean isDefaultForXRay)
  {
    this.isDefaultForXRay = isDefaultForXRay;
  }

  @Override
  public List<String> getPropertySequenceIds()
  {
    if(propertySequenceIds == null) {
      propertySequenceIds = new ArrayList<>();
    }
    return propertySequenceIds;
  }

  @Override
  public void setPropertySequenceIds(List<String> propertySequenceIds)
  {
    this.propertySequenceIds = propertySequenceIds;
  }
}
