package com.cs.di.config.interactor.scheduleworkflow;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;

public interface IModifyScheduleWorkflow extends IConfigInteractor<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
