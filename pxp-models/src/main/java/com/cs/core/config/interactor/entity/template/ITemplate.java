package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface ITemplate extends IConfigMasterEntity {
  
  public static final String HEADER_VISIBILITY = "headerVisibility";
  public static final String TABS              = "tabs";
  public static final String CONTEXT_IDS       = "contextIds";
  
  public ITemplateHeaderVisibility getHeaderVisibility();
  
  public void setHeaderVisibility(ITemplateHeaderVisibility headerVisibility);
  
  public List<ITemplateTab> getTabs();
  
  public void setTabs(List<ITemplateTab> tabs);
  
  public List<String> getContextIds();
  
  public void setContextIds(List<String> contextIds);
}
