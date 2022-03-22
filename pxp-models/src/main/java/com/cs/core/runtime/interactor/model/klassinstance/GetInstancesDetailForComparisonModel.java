package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstancesDetailForComparisonModel implements IGetInstancesDetailForComparisonModel {
  
  /**
   * 
   */
  private static final long      serialVersionUID = 1L;
  protected List<String>         side2LinkedVariantIds;
  protected List<IIdAndBaseType> productListsToCompare;
  
  @Override
  public List<String> getSide2LinkedVariantIds()
  {
    return side2LinkedVariantIds;
  }
  
  @Override
  public void setSide2LinkedVariantIds(List<String> side2LinkedVariantIds)
  {
    this.side2LinkedVariantIds = side2LinkedVariantIds;
    
  }
  
  @Override
  public List<IIdAndBaseType> getProductListsToCompare()
  {
    // TODO Auto-generated method stub
    return productListsToCompare;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setProductListsToCompare(List<IIdAndBaseType> productListsToCompare)
  {
    // TODO Auto-generated method stub
    this.productListsToCompare = productListsToCompare;
    
  }
  
}
