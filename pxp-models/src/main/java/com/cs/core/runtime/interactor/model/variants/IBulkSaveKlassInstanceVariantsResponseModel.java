package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveKlassInstanceVariantsResponseModel extends IBulkResponseModel {
  
  public void setSuccess(IGetVariantInstancesInTableViewModel tableViewModel);
}
