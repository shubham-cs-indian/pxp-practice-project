package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IPriceAttribute;
import com.cs.core.config.interactor.entity.attribute.PriceAttribute;

public class PriceAttributeModel extends AbstractUnitAttributeModel
    implements IPriceAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public PriceAttributeModel()
  {
    super(new PriceAttribute(), Renderer.PRICE.toString());
  }
  
  public PriceAttributeModel(IPriceAttribute attribute)
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
    return ((IPriceAttribute) attribute).getIsSales();
  }
  
  @Override
  public void setIsSales(Boolean isSales)
  {
    ((IPriceAttribute) attribute).setIsSales(isSales);
  }
  
  @Override
  public Boolean getIsOrder()
  {
    return ((IPriceAttribute) attribute).getIsOrder();
  }
  
  @Override
  public void setIsOrder(Boolean isOrder)
  {
    ((IPriceAttribute) attribute).setIsOrder(isOrder);
  }
  
  @Override
  public Boolean getIsPromotional()
  {
    return ((IPriceAttribute) attribute).getIsPromotional();
  }
  
  @Override
  public void setIsPromotional(Boolean isPromotional)
  {
    ((IPriceAttribute) attribute).setIsPromotional(isPromotional);
  }
}
