package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class BooleanCheckTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  @Autowired
  BooleanCheckTask   booleanCheckTask;

  /**
   * Valid test for true output.
   * 
   * @throws Exception
   */
  @Test
  public void trueOutputTest() throws Exception
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { BooleanCheckTask.BOOLEAN_CONDITION},
        new Object[] { "123>247"});
    booleanCheckTask.executeTask(model);
    assertTrue((boolean)model.getOutputParameters().get(ExecutionStatusCheckTask.BOOLEAN_RESULT) == true);
  }
  
  /**
   * Valid test for false output.
   * @throws Exception
   */
  @Test
  public void falseOutputTest() throws Exception
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { BooleanCheckTask.BOOLEAN_CONDITION},
        new Object[] { "(1<=2) && (3>8)"});
    booleanCheckTask.executeTask(model);
    assertTrue((boolean)model.getOutputParameters().get(ExecutionStatusCheckTask.BOOLEAN_RESULT) == false);
  }
}
