package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteVariantsReturnModel extends IBulkResponseModel {
  
  public IIdsListWithVersionIdModel getSuccess();
  
  public void setSuccess(IIdsListWithVersionIdModel success);
}
