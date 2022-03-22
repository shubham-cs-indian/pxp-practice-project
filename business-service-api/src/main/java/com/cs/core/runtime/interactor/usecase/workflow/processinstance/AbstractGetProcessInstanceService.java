package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.process.IBGPProcessModel;

public abstract class AbstractGetProcessInstanceService<P extends IIdParameterModel, R extends IBGPProcessModel>
    extends AbstractRuntimeService<P, R> {
 
  protected abstract String getProcessDefination(String service) throws Exception;
}
