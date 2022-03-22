package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IProcessInstanceFiles extends IRuntimeInteractor<IProcessInstanceFileModel, IResponseModelForProcessInstance> {
  
}
