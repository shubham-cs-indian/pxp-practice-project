package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IVariantContextTagValuesModel extends IModel {
  
  public static final String TAG_VALUE_ID = "tagValueId";
  public static final String LABEL        = "label";
  public static final String IS_SELECTED  = "isSelected";
  public static final String COLOR        = "color";
  
  public String getTagValueId();
  
  public void setTagValueId(String tagValueId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsSelected();
  
  public void setIsSelected(Boolean isSelected);
  
  public String getColor();
  
  public void setColor(String color);
}
