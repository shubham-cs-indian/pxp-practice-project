package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.IBulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IGetValidationInformationModel;

public interface IBulkUploadProcessToServerStrategy extends
    ICamundaStrategy<IListModel<? extends IGetValidationInformationModel>, IBulkSaveProcessEventResponseModel> {
  public static final String IS_CLONE = "isClone";
}
