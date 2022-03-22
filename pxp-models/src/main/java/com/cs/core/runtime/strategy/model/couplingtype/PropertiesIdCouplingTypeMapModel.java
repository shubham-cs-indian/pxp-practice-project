package com.cs.core.runtime.strategy.model.couplingtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertiesIdCouplingTypeMapModel implements IPropertiesIdCouplingTypeMapModel {
  
  private static final long                             serialVersionUID = 1L;
  
  protected List<IIdCodeCouplingTypeModel>              transferTags;
  protected List<IIdCodeCouplingTypeModel>              transferInependentAttributes;
  protected Map<String, List<IIdCodeCouplingTypeModel>> transferDependentAttributes;
  
  @Override
  public Map<String, List<IIdCodeCouplingTypeModel>> getTransferDependentAttributes()
  {
    if (transferDependentAttributes == null) {
      transferDependentAttributes = new HashMap<>();
    }
    return transferDependentAttributes;
  }
  
  @Override
  public void setTransferDependentAttributes(
      Map<String, List<IIdCodeCouplingTypeModel>> transferDependentAttributes)
  {
    this.transferDependentAttributes = transferDependentAttributes;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getTransferTags()
  {
    if (transferTags == null) {
      transferTags = new ArrayList<>();
    }
    return transferTags;
  }
  
  @Override
  public void setTransferTags(List<IIdCodeCouplingTypeModel> transferTags)
  {
    this.transferTags = transferTags;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getTransferInependentAttributes()
  {
    if (transferInependentAttributes == null) {
      transferInependentAttributes = new ArrayList<>();
    }
    return transferInependentAttributes;
  }
  
  @Override
  public void setTransferIndependentAttributes(
      List<IIdCodeCouplingTypeModel> transferInependentAttributes)
  {
    this.transferInependentAttributes = transferInependentAttributes;
  }
}
