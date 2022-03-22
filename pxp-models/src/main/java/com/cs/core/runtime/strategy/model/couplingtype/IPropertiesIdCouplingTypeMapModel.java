package com.cs.core.runtime.strategy.model.couplingtype;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IPropertiesIdCouplingTypeMapModel extends IModel {
  
  public static final String TRANSFER_DEPENDENT_ATTRIBUTES   = "transferDependentAttributes";
  public static final String TRANSFER_INDEPENDENT_ATTRIBUTES = "transferInependentAttributes";
  public static final String TRANSFER_TAGS                   = "transferTags";
  
  public Map<String, List<IIdCodeCouplingTypeModel>> getTransferDependentAttributes();
  
  public void setTransferDependentAttributes(
      Map<String, List<IIdCodeCouplingTypeModel>> transferDependentAttributes);
  
  public List<IIdCodeCouplingTypeModel> getTransferInependentAttributes();
  
  public void setTransferIndependentAttributes(
      List<IIdCodeCouplingTypeModel> transferInependentAttributes);
  
  public List<IIdCodeCouplingTypeModel> getTransferTags();
  
  public void setTransferTags(List<IIdCodeCouplingTypeModel> transferTags);
}
