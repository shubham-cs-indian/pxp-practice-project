package com.cs.core.runtime.interactor.model.transfer;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTransferPropertyModel implements IDataTransferPropertyModel {
  
  private static final long                                    serialVersionUID = 1L;
  
  protected List<IReferencedRelationshipProperty>              transferTags;
  protected List<IReferencedRelationshipProperty>              transferInependentAttributes;
  protected Map<String, List<IReferencedRelationshipProperty>> transferDependentAttributes;
  
  @Override
  public Map<String, List<IReferencedRelationshipProperty>> getTransferDependentAttributes()
  {
    if (transferDependentAttributes == null) {
      transferDependentAttributes = new HashMap<>();
    }
    return transferDependentAttributes;
  }
  
  @Override
  public void setTransferDependentAttributes(
      Map<String, List<IReferencedRelationshipProperty>> transferDependentAttributes)
  {
    this.transferDependentAttributes = transferDependentAttributes;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getTransferTags()
  {
    if (transferTags == null) {
      transferTags = new ArrayList<>();
    }
    return transferTags;
  }
  
  @Override
  public void setTransferTags(List<IReferencedRelationshipProperty> transferTags)
  {
    this.transferTags = transferTags;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getTransferInependentAttributes()
  {
    if (transferInependentAttributes == null) {
      transferInependentAttributes = new ArrayList<>();
    }
    return transferInependentAttributes;
  }
  
  @Override
  public void setTransferIndependentAttributes(
      List<IReferencedRelationshipProperty> transferInependentAttributes)
  {
    this.transferInependentAttributes = transferInependentAttributes;
  }
}
