package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

import java.util.List;

public interface IDeleteKlassInstanceResponseModel extends IBulkResponseModel {
  
  public void setSuccess(List<String> success);
}
