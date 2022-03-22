package com.cs.core.workflow.get;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;

public interface IDownloadWFInstanceService
    extends IRuntimeService<IGetProcessInstanceModel, IResponseModelForProcessInstance> {
  
}
