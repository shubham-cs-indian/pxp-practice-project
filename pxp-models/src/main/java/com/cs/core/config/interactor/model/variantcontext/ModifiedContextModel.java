package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedContextModel implements IModifiedContextModel {
  
  private static final long                        serialVersionUID          = 1L;
  protected String                                 id;
  protected String                                 label;
  protected String                                 type;
  protected String                                 icon;
  protected String                                 iconKey;
  protected Boolean                                isAutoCreate;
  protected Boolean                                isStandard                = false;
  protected List<IAddedVariantContextTagsModel>    addedTags;
  protected List<IVariantContextModifiedTagsModel> modifiedTags;
  protected List<String>                           deletedTags;
  protected List<String>                           entities;
  protected List<String>                           addedEntities;
  protected List<String>                           deletedEntities;
  protected Boolean                                isTimeEnabled             = false;
  protected View                                   defaultView;
  protected Boolean                                isDuplicateVariantAllowed = false;
  protected IDefaultTimeRange                      defaultTimeRange;
  protected String                                 code;
  protected long                                   contextIID;
  
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
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
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
  public List<IAddedVariantContextTagsModel> getAddedTags()
  {
    
    return addedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = AddedVariantContextTagsModel.class)
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<IVariantContextModifiedTagsModel> getModifiedTags()
  {
    
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantContextModifiedTagsModel.class)
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
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
  public Boolean getIsAutoCreate()
  {
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
  @Override
  public List<String> getAddedEntities()
  {
    if (addedEntities == null) {
      return new ArrayList<String>();
    }
    return addedEntities;
  }
  
  @Override
  public void setAddedEntities(List<String> addedEntities)
  {
    this.addedEntities = addedEntities;
  }
  
  @Override
  public List<String> getDeletedEntities()
  {
    if (deletedEntities == null) {
      return new ArrayList<String>();
    }
    return deletedEntities;
  }
  
  @Override
  public void setDeletedEntities(List<String> deletedEntities)
  {
    this.deletedEntities = deletedEntities;
  }
  
  @Override
  public Boolean getIsTimeEnabled()
  {
    return isTimeEnabled;
  }
  
  @Override
  public void setIsTimeEnabled(Boolean isTimeEnabled)
  {
    this.isTimeEnabled = isTimeEnabled;
  }
  
  @Override
  public View getDefaultView()
  {
    return defaultView;
  }
  
  @Override
  public void setDefaultView(View defaultView)
  {
    this.defaultView = defaultView;
  }
  
  @Override
  public Boolean getIsDuplicateVariantAllowed()
  {
    return isDuplicateVariantAllowed;
  }
  
  @Override
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed)
  {
    this.isDuplicateVariantAllowed = isDuplicateVariantAllowed;
  }
  
  @Override
  public IDefaultTimeRange getDefaultTimeRange()
  {
    return defaultTimeRange;
  }
  
  @JsonDeserialize(as = DefaultTimeRange.class)
  @Override
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    this.defaultTimeRange = defaultTimeRange;
  }
  
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getDescription()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setDescription(String description)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTooltip(String tooltip)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Boolean getIsMandatory()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPlaceholder(String placeholder)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getTabId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTabId(String tabId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public long getContextIID()
  {
    return contextIID;
  }
  
  @Override
  public void setContextIID(long contextIID)
  {
    this.contextIID = contextIID;
  }
}
