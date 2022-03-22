package com.cs.core.config.interactor.entity.tabs;

import java.util.List;

public class Tab implements ITab {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          code;
  protected List<String>    propertySequenceList;
  protected String          icon;
  protected String          iconKey;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          type;
  protected List<String>    propertyCollectionIds;
  protected List<String>    variantContextIds;
  protected List<String>    relationshipIds;
  protected Boolean         isStandard       = false;
  
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
  public List<String> getPropertySequenceList()
  {
    return propertySequenceList;
  }
  
  @Override
  public void setPropertySequenceList(List<String> propertySequenceList)
  {
    this.propertySequenceList = propertySequenceList;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
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
  public List<String> getPropertyCollectionIds()
  {
    return propertyCollectionIds;
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    this.propertyCollectionIds = propertyCollectionIds;
  }
  
  @Override
  public List<String> getVariantContextIds()
  {
    return variantContextIds;
  }
  
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    this.variantContextIds = variantContextIds;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
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
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
}
