package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;

public interface IEvaluateIdentifierAttributeResponseModel extends IModel {
  
  public static final String UPDATE_SEARCHABLE_INSTANCE_MODEL                = "updateSearchableInstanceModel";
  public static final String IDENTIFIER_ATTRIBUTE_STATUS_FOR_OTHER_INSTANCES = "identifierAttributeStatusForOtherInstances";
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getIdentifierAttributeStatusForOtherInstances();
  
  public void setIdentifierAttributeStatusForOtherInstances(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> identifierAttributeStatusForOtherInstances);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableInstanceModel();
  
  public void setUpdateSearchableInstanceModel(
      IUpdateSearchableInstanceModel updateSearchableInstanceModel);
}
