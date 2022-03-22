package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface IBulkSaveProcessEventStrategy extends
    IConfigStrategy<IListModel<ISaveProcessEventModel>, IBulkProcessEventSaveResponseModel> {
  
}
