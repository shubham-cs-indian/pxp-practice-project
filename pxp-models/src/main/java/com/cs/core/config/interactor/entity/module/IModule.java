package com.cs.core.config.interactor.entity.module;

import java.util.ArrayList;

public interface IModule {
  
  String getId();
  
  void setId(String id);
  
  String getLabel();
  
  void setLabel(String label);
  
  String getClassName();
  
  void setClassName(String className);
  
  String getTitle();
  
  void setTitle(String title);
  
  boolean getIsSelected();
  
  void setIsSelected(boolean isSelected);
  
  boolean getIsVisible();
  
  void setIsVisible(boolean isVisible);
  
  String getUrl();
  
  void setUrl(String url);
  
  ArrayList<String> getEntities();
  
  void setEntities(ArrayList<String> entities);
  
  String getDefaultView();
  
  void setDefaultView(String defaultView);
  
  ArrayList<String> getAllowedViews();
  
  void setAllowedViews(ArrayList<String> allowedViews);
  
  Integer getDefaultZoomLevel();
  
  void setDefaultZoomLevel(Integer defaultZoomLevel);
  
  ArrayList<Integer> getAllowedZoomLevels();
  
  void setAllowedZoomLevels(ArrayList<Integer> allowedZoomLevels);
}
