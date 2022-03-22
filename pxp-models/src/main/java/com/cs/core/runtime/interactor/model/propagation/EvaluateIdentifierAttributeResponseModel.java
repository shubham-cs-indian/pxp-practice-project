package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class EvaluateIdentifierAttributeResponseModel
    implements IEvaluateIdentifierAttributeResponseModel {
  
  protected IUpdateSearchableInstanceModel                                 updateSearchableInstanceModel;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> identifierAttributeStatusForOtherInstances;
  
  @Override
  public IUpdateSearchableInstanceModel getUpdateSearchableInstanceModel()
  {
    return updateSearchableInstanceModel;
  }
  
  @Override
  @JsonDeserialize(as = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableInstanceModel(
      IUpdateSearchableInstanceModel updateSearchableInstanceModel)
  {
    this.updateSearchableInstanceModel = updateSearchableInstanceModel;
  }
  
  @Override
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getIdentifierAttributeStatusForOtherInstances()
  {
    return identifierAttributeStatusForOtherInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyInstanceUniquenessEvaluationForPropagationModel.class)
  public void setIdentifierAttributeStatusForOtherInstances(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> identifierAttributeStatusForOtherInstances)
  {
    this.identifierAttributeStatusForOtherInstances = identifierAttributeStatusForOtherInstances;
  }
}
