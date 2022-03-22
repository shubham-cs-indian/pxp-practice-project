package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("deleteProcessDefinationStrategy")
public class DeleteProcessDefinationStrategy extends AbstractCamundaStrategy
    implements IDeleteProcessDefinationStrategy {
  
  @Autowired
  RepositoryService repositoryService;
  
  @Override
  public IModel execute(IIdsListParameterModel model) throws Exception
  {
    try {
      for (String processDefinitionId : model.getIds()) {
        if (processDefinitionId != null) {
          repositoryService.deleteProcessDefinition(processDefinitionId, true);
        }
      }
    }
    catch(NotFoundException e)
    {
      //FIXME: When process definition marked as not executable the processDefinitionId is still the old one and not null
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
    return null;
  }
  
}
