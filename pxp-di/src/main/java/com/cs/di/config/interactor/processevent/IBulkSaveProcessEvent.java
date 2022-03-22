package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface IBulkSaveProcessEvent extends
    ISaveConfigInteractor<IListModel<ISaveProcessEventModel>, IBulkProcessEventSaveResponseModel> {
  
}
