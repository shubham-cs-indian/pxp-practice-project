package com.cs.di.workflow.tasks;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.config.strategy.usecase.processevent.GetProcessByConfigStrategy;

public class WorkflowEventTest extends DiIntegrationTestConfig {
  
  @Autowired
  GetProcessByConfigStrategy getProcessByConfig;
  
  @Test
  public void getProcessByConfig() throws Exception
  {
    /*IBusinessProcessTriggerModel model = new BusinessProcessTriggerModel();
    model.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_PROPERTIES_SAVE);
    //model.setAttributeIds(attributeIds);
    model.setBusinessProcessActionType(IBusinessProcessTriggerModel.ActionTypes.AFTER_SAVE);
    IIdsListParameterModel response = getProcessByConfig.execute(model);
    System.out.println(response.getIds());*/
  }
  
  
}
