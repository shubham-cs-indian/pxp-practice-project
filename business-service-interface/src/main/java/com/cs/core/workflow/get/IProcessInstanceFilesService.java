package com.cs.core.workflow.get;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;

public interface IProcessInstanceFilesService extends IRuntimeService<IProcessInstanceFileModel, IResponseModelForProcessInstance> {
  
}
