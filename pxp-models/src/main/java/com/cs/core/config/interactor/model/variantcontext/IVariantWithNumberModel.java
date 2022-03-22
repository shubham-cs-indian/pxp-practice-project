package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IVariantWithNumberModel extends IModel {
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getParent();
  
  public void setParent(String parent);
  
  public int getVariantId();
  
  public void setVariantId(int variantId);
}
