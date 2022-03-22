package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.filter.ISortModel;

public interface IAppliedSortModel extends ISortModel {
  
  public static final String CODE       = "code";
  public static final String LABEL      = "label";
  public static final String SORT_FIELD = "sortField";
  public static final String SORT_ORDER = "sortOrder";
  public static final String IS_NUMERIC = "isNumeric";
  public static final String ICON       = "icon";
  public static final String ICON_KEY 	= "iconKey";
  
  public void setCode(String code);
  public String getCode();
  
  public void setLabel(String label);
  public String getLabel();
  
  public String getIcon();
  public void setIcon(String icon);
  
  public String getIconKey();
  public void setIconKey(String iconKey);
}
