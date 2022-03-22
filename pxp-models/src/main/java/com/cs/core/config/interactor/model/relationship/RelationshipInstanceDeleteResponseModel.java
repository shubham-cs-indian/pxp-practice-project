package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceDeleteResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceDeleteSuccessResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipInstanceDeleteResponseModel
    implements IRelationshipInstanceDeleteResponseModel {
  
  private static final long                       serialVersionUID = 1L;
  
  IExceptionModel                                 failure;
  IRelationshipInstanceDeleteSuccessResponseModel success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public IRelationshipInstanceDeleteSuccessResponseModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipInstanceDeleteSuccessResponseModel.class)
  public void setSuccess(IRelationshipInstanceDeleteSuccessResponseModel success)
  {
    this.success = success;
  }
}
