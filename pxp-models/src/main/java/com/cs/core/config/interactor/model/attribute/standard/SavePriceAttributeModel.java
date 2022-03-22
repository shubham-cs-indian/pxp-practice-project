package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.PriceAttribute;

public class SavePriceAttributeModel extends AbstractSaveUnitAttributeModel
    implements IPriceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SavePriceAttributeModel()
  {
    super(new PriceAttribute(), Renderer.PRICE.toString());
  }
  
  public SavePriceAttributeModel(PriceAttribute attribute)
  {
    super(attribute, Renderer.PRICE.toString());
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public Boolean getIsSales()
  {
    return ((PriceAttribute) attribute).getIsSales();
  }
  
  @Override
  public void setIsSales(Boolean isSales)
  {
    ((PriceAttribute) attribute).setIsSales(isSales);
  }
  
  @Override
  public Boolean getIsOrder()
  {
    return ((PriceAttribute) attribute).getIsOrder();
  }
  
  @Override
  public void setIsOrder(Boolean isOrder)
  {
    ((PriceAttribute) attribute).setIsOrder(isOrder);
  }
  
  @Override
  public Boolean getIsPromotional()
  {
    return ((PriceAttribute) attribute).getIsPromotional();
  }
  
  @Override
  public void setIsPromotional(Boolean isPromotional)
  {
    ((PriceAttribute) attribute).setIsPromotional(isPromotional);
  }
}
