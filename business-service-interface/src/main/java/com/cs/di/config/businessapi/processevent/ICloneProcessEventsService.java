package com.cs.di.config.businessapi.processevent;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;

public interface ICloneProcessEventsService
    extends ICreateConfigService<IListModel<IConfigCloneEntityInformationModel>, IBulkProcessEventSaveResponseModel> {
  
}
