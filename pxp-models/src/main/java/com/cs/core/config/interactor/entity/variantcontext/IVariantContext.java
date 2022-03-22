package com.cs.core.config.interactor.entity.variantcontext;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;

import java.util.List;

public interface IVariantContext extends IConfigMasterPropertyEntity {
  
  public static final String LABEL                        = "label";
  public static final String ENTITIES                     = "entities";
  public static final String IS_AUTO_CREATE               = "isAutoCreate";
  public static final String IS_DUPLICATE_VARIANT_ALLOWED = "isDuplicateVariantAllowed";
  public static final String IS_TIME_ENABLED              = "isTimeEnabled";
  public static final String DEFAULT_VIEW                 = "defaultView";
  public static final String DEFAULT_TIME_RANGE           = "defaultTimeRange";
  public static final String TAB_ID                       = "tabId";
  public static final String CONTEXT_IID                  = "contextIID";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
  
  public Boolean getIsDuplicateVariantAllowed();
  
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed);
  
  public Boolean getIsTimeEnabled();
  
  public void setIsTimeEnabled(Boolean isTimeEnabled);
  
  public View getDefaultView();
  
  public void setDefaultView(View defaultView);
  
  public IDefaultTimeRange getDefaultTimeRange();
  
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public long getContextIID();
  
  public void setContextIID(long contextIID);
  
  public enum View
  {
    THUMBNAIL, GRID
  }
}
