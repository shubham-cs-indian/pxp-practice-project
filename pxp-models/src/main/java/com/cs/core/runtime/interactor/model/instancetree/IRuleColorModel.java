package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRuleColorModel extends IModel {
  
  public static final String IS_RED    = "isRed";
  public static final String IS_ORANGE = "isOrange";
  public static final String IS_YELLOW = "isYellow";
  public static final String IS_GREEN  = "isGreen";
  
  public Boolean getIsRed();
  public void setIsRed(Boolean isRed);
  
  public Boolean getIsOrange();
  public void setIsOrange(Boolean isOrange);
  
  public Boolean getIsYellow();
  public void setIsYellow(Boolean isYellow);
  
  public Boolean getIsGreen();
  public void setIsGreen(Boolean isGreen);
  
}
