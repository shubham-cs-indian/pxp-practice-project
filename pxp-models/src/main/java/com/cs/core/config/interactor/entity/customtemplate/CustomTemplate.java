package com.cs.core.config.interactor.entity.customtemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class CustomTemplate implements ICustomTemplate {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          type;
  protected Long            versionId;
  protected String          code;
  protected List<String>    variantContextIds;
  protected List<String>    natureRelationshipIds;
  protected List<String>    relationshipIds;
  protected List<String>    propertyCollectionIds;
  
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
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
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
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
  public List<String> getPropertyCollectionIds()
  {
    if (propertyCollectionIds == null) {
      propertyCollectionIds = new ArrayList<>();
    }
    return propertyCollectionIds;
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    this.propertyCollectionIds = propertyCollectionIds;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    if (natureRelationshipIds == null) {
      natureRelationshipIds = new ArrayList<>();
    }
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
  
  @Override
  public List<String> getContextIds()
  {
    if (variantContextIds == null) {
      variantContextIds = new ArrayList<>();
    }
    return variantContextIds;
  }
  
  @Override
  public void setContextIds(List<String> variantContextIds)
  {
    this.variantContextIds = variantContextIds;
  }
  
  
  /**
   * ******************** ignored properties *********************
   */
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
}
