package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetInstancesDetailForComparisonModel extends IModel {
  
  public static final String SIDE2LINKEDVARIANTIDS = "side2LinkedVariantIds";
  public static final String PRODUCTLISTSTOCOMPARE = "productListsToCompare";
  
  public List<String> getSide2LinkedVariantIds();
  
  public void setSide2LinkedVariantIds(List<String> SIDE2LINKEDVARIANTIDS);
  
  public List<IIdAndBaseType> getProductListsToCompare();
  
  public void setProductListsToCompare(List<IIdAndBaseType> productListsToCompare);
}
