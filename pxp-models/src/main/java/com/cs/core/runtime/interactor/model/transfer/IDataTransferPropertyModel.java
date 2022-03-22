package com.cs.core.runtime.interactor.model.transfer;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IDataTransferPropertyModel extends IModel {
  
  public static final String TRANSFER_DEPENDENT_ATTRIBUTES   = "transferDependentAttributes";
  public static final String TRANSFER_INDEPENDENT_ATTRIBUTES = "transferInependentAttributes";
  public static final String TRANSFER_TAGS                   = "transferTags";
  
  public Map<String, List<IReferencedRelationshipProperty>> getTransferDependentAttributes();
  
  public void setTransferDependentAttributes(
      Map<String, List<IReferencedRelationshipProperty>> transferDependentAttributes);
  
  public List<IReferencedRelationshipProperty> getTransferInependentAttributes();
  
  public void setTransferIndependentAttributes(
      List<IReferencedRelationshipProperty> transferInependentAttributes);
  
  public List<IReferencedRelationshipProperty> getTransferTags();
  
  public void setTransferTags(List<IReferencedRelationshipProperty> transferTags);
}
