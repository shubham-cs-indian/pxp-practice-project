package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IUniqueSelectorModel extends IModel {
  
  public static final String ID               = "id";
  public static final String SELECTION_VALUES = "selectionValues";
  
  public String getId();
  
  public void setId(String id);
  
  public List<IVariantContextTagModel> getSelectionValues();
  
  public void setSelectionValues(List<IVariantContextTagModel> selectionValues);
}
