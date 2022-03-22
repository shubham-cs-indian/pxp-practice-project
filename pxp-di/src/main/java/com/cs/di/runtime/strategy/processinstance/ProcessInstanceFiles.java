package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.workflow.get.IProcessInstanceFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProcessInstanceFiles extends AbstractRuntimeInteractor<IProcessInstanceFileModel, IResponseModelForProcessInstance> implements IProcessInstanceFiles {

  @Autowired
  IProcessInstanceFilesService processInstanceFilesService;

  @Override
  public IResponseModelForProcessInstance executeInternal(IProcessInstanceFileModel model) throws Exception
  {

    return processInstanceFilesService.execute(model);
  }

}
