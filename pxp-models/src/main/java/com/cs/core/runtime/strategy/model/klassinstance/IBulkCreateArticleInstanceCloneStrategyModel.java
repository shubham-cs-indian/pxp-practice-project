package com.cs.core.runtime.strategy.model.klassinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;

import java.util.List;

public interface IBulkCreateArticleInstanceCloneStrategyModel extends IBulkResponseModel {
  
  public void setSuccess(List<ISaveStrategyInstanceResponseModel> success);
}
