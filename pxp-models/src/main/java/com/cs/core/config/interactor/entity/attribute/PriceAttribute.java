package com.cs.core.config.interactor.entity.attribute;

public class PriceAttribute extends AbstractUnitAttribute implements IPriceAttribute {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isSales;
  protected Boolean         isOrder;
  protected Boolean         isPromotional;
  
  @Override
  public String getRendererType()
  {
    return Renderer.PRICE.name();
  }
  
  @Override
  public Boolean getIsSales()
  {
    return isSales;
  }
  
  @Override
  public void setIsSales(Boolean isSales)
  {
    this.isSales = isSales;
  }
  
  @Override
  public Boolean getIsOrder()
  {
    return isOrder;
  }
  
  @Override
  public void setIsOrder(Boolean isOrder)
  {
    this.isOrder = isOrder;
  }
  
  @Override
  public Boolean getIsPromotional()
  {
    return isPromotional;
  }
  
  @Override
  public void setIsPromotional(Boolean isPromotional)
  {
    this.isPromotional = isPromotional;
  }
}
