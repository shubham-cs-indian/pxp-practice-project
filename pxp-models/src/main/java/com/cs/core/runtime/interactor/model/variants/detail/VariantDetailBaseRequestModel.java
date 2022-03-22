package com.cs.core.runtime.interactor.model.variants.detail;

import java.util.ArrayList;
import java.util.List;

public class VariantDetailBaseRequestModel implements IVariantDetailBaseRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    variants;
  
  @Override
  public String getInstanceId()
  {
    return id;
  }
  
  @Override
  public void setInstanceId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getVariantIds()
  {
    return variants;
  }
  
  @Override
  public void setVariantIds(List<String> variants)
  {
    if (variants == null) {
      variants = new ArrayList<>();
    }
    this.variants = variants;
  }
}
