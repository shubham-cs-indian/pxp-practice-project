package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.model.executionstatus.ExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.OutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class ExecutionStatusCheckTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  @Autowired
  ExecutionStatusCheckTask   executionStatusCheckTask;
  
  public static final String InputJson = "{\"errorOccurred\":false,\"executionStatusTableMap\":{\"SUMMARY\":[],\"ERROR\":[],\"WARNING\":[],\"INFORMATION\":[],\"SUCCESS\":[{\"messageCode\":\"GEN035\",\"messageType\":\"S\",\"objectCodes\":[\"CAMUNDA_TASK\",\"CAMUNDA_TASK_ID\"],\"objectValues\":[\"com.cs.di.workflow.tasks.ListInsertPositionTask\",\"listInsertPositionTask\"]}]}}";

  /**
   * Valid test for true output
   * 
   * @throws Exception
   */
  @Test
  public void trueOutputTest() throws Exception
  {
    IExecutionStatus<OutputExecutionStatusModel> executionStatusTable = ObjectMapperUtil.readValue(InputJson, ExecutionStatus.class);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { ExecutionStatusCheckTask.EXECUTION_STATUS_MAP, ExecutionStatusCheckTask.MESSAGE_TYPE },
        new Object[] { executionStatusTable, "SUCCESS" });
    executionStatusCheckTask.executeTask(model);
    assertTrue((boolean)model.getOutputParameters().get(ExecutionStatusCheckTask.BOOLEAN_RESULT) == true);
  }
  
  /**
   * Valid test for false output.
   * 
   * @throws Exception
   */
  @Test
  public void falseOutputTest() throws Exception
  {
    IExecutionStatus<OutputExecutionStatusModel> executionStatusTable = ObjectMapperUtil.readValue(InputJson, ExecutionStatus.class);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { ExecutionStatusCheckTask.EXECUTION_STATUS_MAP, ExecutionStatusCheckTask.MESSAGE_TYPE },
        new Object[] { executionStatusTable , "ERROR" });
    executionStatusCheckTask.executeTask(model);
    assertTrue((boolean)model.getOutputParameters().get(ExecutionStatusCheckTask.BOOLEAN_RESULT) == false);
  }
  
  /**
   * Test for if executionStatusTable is null.
   *  
   * @throws Exception
   */
  @Test
  public void executionStatusMapNull() throws Exception
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { ExecutionStatusCheckTask.EXECUTION_STATUS_MAP, ExecutionStatusCheckTask.MESSAGE_TYPE },
        new Object[] { null, "ERROR" });
    executionStatusCheckTask.executeTask(model);
    assertTrue(model.getExecutionStatusTable().isErrorOccurred() == true);
  }
  
  /**
   * Test for if messageType  is null.
   *  
   * @throws Exception
   */
  @Test
  public void messageTypeNull() throws Exception
  {
    IExecutionStatus<OutputExecutionStatusModel> executionStatusTable = ObjectMapperUtil.readValue(InputJson, ExecutionStatus.class);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("executionStatusCheckTask",
        new String[] { ExecutionStatusCheckTask.EXECUTION_STATUS_MAP, ExecutionStatusCheckTask.MESSAGE_TYPE },
        new Object[] { executionStatusTable , null });
    executionStatusCheckTask.executeTask(model);
    assertTrue((boolean)model.getOutputParameters().get(ExecutionStatusCheckTask.BOOLEAN_RESULT) == false);
  }
}
