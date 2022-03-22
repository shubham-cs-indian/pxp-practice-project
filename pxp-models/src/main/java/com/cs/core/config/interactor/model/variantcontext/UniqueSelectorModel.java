package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class UniqueSelectorModel implements IUniqueSelectorModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected String                        id;
  protected List<IVariantContextTagModel> selectionValues  = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<IVariantContextTagModel> getSelectionValues()
  {
    return selectionValues;
  }
  
  @JsonDeserialize(contentAs = VariantContextTagModel.class)
  @Override
  public void setSelectionValues(List<IVariantContextTagModel> selectionValues)
  {
    this.selectionValues = selectionValues;
  }
}
