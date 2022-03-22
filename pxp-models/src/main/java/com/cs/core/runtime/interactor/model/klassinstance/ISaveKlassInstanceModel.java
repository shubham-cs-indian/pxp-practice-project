package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveKlassInstanceModel extends IBulkResponseModel {
  
  public void setSuccess(List<? extends IModel> success);
}
