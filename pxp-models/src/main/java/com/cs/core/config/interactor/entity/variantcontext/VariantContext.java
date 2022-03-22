package com.cs.core.config.interactor.entity.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class VariantContext implements IVariantContext {
  
  private static final long   serialVersionUID          = 1L;
  
  protected String            id;
  protected String            label;
  protected String            type;
  protected List<String>      entities;
  protected Boolean           isAutoCreate              = false;
  protected Boolean           isTimeEnabled             = false;
  protected View              defaultView;
  protected Boolean           isDuplicateVariantAllowed = false;
  protected IDefaultTimeRange defaultTimeRange;
  protected String            code;
  protected String            icon;
  protected String            iconKey;
  protected String            tabId;
  protected Boolean           isStandard;
  protected long              contextIID;
  
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getDescription()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setDescription(String description)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    // TODO Auto-generated method stub
    
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
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    // TODO Auto-generated method stub
    
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
    if (defaultView == null) {
      defaultView = View.THUMBNAIL;
    }
    
    return defaultView;
  }
  
  @Override
  public void setDefaultView(View defaultView)
  {
    this.defaultView = defaultView;
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
  
  @Override
  @JsonDeserialize(as = DefaultTimeRange.class)
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    this.defaultTimeRange = defaultTimeRange;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
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
