package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IRelationshipInstanceDeleteResponseModel extends IBulkResponseModel {
  
  public static String SUCCESS = "success";
  
  public void setSuccess(IRelationshipInstanceDeleteSuccessResponseModel success);
}
