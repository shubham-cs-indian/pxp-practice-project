package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.variantcontext.IVariantWithNumberModel;

public class VariantWithNumberModel implements IVariantWithNumberModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected int             variantId;
  protected String          parent;
  protected String          label;
  
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
  public String getParent()
  {
    return parent;
  }
  
  @Override
  public void setParent(String parent)
  {
    this.parent = parent;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public int getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(int variantId)
  {
    this.variantId = variantId;
  }
}
