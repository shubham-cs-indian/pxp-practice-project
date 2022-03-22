package com.cs.core.config.interactor.entity.module;

import java.util.ArrayList;

public interface IScreen {
  
  String getId();
  
  void setId(String id);
  
  String getName();
  
  void setName(String name);
  
  String getTitle();
  
  void setTitle(String title);
  
  String getIcon();
  
  void setIcon(String icon);
  
  boolean getIsVisible();
  
  void setIsVisible(boolean isVisible);
  
  boolean getIsDefault();
  
  void setIsDefault(boolean isDefault);
  
  String getType();
  
  void setType(String type);
  
  String getUrl();
  
  void setUrl(String url);
  
  ArrayList<IModule> getModules();
  
  void setModules(ArrayList<IModule> modules);
  
  Boolean getCanFilter();
  
  void setCanFilter(Boolean canFilter);
  
  Boolean getCanSort();
  
  void setCanSort(Boolean canSort);
  
  Boolean getCanFilterTaxonomy();
  
  void setCanFilterTaxonomy(Boolean canFilterTaxonomy);
  
  Boolean getIsBookmarkEnabled();
  
  void setIsBookmarkEnabled(Boolean isBookmarkEnabled);
  
  Boolean getIsStaicCollectionEnabled();
  
  void setIsStaicCollectionEnabled(Boolean isStaicCollectionEnabled);
  
  ArrayList<String> getEntities();
  
  void setEntities(ArrayList<String> entities);
}
