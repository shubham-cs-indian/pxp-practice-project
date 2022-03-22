package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.process.IBGPProcessModel;

public interface IGetBGPProcessInstanceService extends IRuntimeService<IIdParameterModel, IBGPProcessModel> {
  
}