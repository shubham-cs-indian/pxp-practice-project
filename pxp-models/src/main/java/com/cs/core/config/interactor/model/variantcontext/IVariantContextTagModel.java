package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IVariantContextTagModel extends IModel {
  
  public static final String TAG_ID     = "tagId";
  public static final String LABEL      = "label";
  public static final String TAG_VALUES = "tagValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IVariantContextTagValuesModel> getTagValues();
  
  public void setTagValues(List<IVariantContextTagValuesModel> tagValues);
}
