package com.cs.core.config.interactor.entity.module;

import java.util.ArrayList;

public interface IScreenModuleMapping {
  
  String getId();
  
  void setId(String id);
  
  String getLogo();
  
  void setLogo(String logo);
  
  String getType();
  
  void setType(String type);
  
  ArrayList<IScreen> getScreens();
  
  void setScreens(ArrayList<IScreen> screens);
}
