package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

import java.util.List;

public interface IBulkCreateKlassInstanceCloneResponseModel extends IBulkResponseModel {
  
  public void setSuccess(List<IGetKlassInstanceModel> success);
}
