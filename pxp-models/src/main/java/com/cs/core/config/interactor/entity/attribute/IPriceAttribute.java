package com.cs.core.config.interactor.entity.attribute;

public interface IPriceAttribute extends IUnitAttribute {
  
  public static final String IS_SALES       = "isSales";
  public static final String IS_ORDER       = "isOrder";
  public static final String IS_PROMOTIONAL = "isPromotional";
  
  public Boolean getIsSales();
  
  public void setIsSales(Boolean isSales);
  
  public Boolean getIsOrder();
  
  public void setIsOrder(Boolean isOrder);
  
  public Boolean getIsPromotional();
  
  public void setIsPromotional(Boolean isPromotional);
}
