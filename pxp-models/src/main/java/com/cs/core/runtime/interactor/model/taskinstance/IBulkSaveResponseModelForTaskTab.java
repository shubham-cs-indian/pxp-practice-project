package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveResponseModelForTaskTab extends IBulkResponseModel {
  
  public void setSuccess(IListModel<IGetTaskInstanceModel> success);
}