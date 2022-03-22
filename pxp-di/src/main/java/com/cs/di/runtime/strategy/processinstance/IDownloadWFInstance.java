package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;

public interface IDownloadWFInstance
    extends IRuntimeInteractor<IGetProcessInstanceModel, IResponseModelForProcessInstance> {
  
}
