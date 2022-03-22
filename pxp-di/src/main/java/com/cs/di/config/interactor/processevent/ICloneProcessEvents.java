package com.cs.di.config.interactor.processevent;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;

public interface ICloneProcessEvents extends
    ICreateConfigInteractor<IListModel<IConfigCloneEntityInformationModel>, IBulkProcessEventSaveResponseModel> {
  
}
