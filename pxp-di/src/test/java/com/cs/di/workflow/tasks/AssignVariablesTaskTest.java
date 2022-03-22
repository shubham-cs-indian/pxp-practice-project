package com.cs.di.workflow.tasks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.DeliveryType;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.IAbstractExecutionReader;

/**
 * This test class is to check if assignment of values from one 
 * variable to another
 * @author Subham.Shaw
 *
 */
public class AssignVariablesTaskTest extends DiMockitoTestConfig {    
  
  @InjectMocks
  @Autowired  
  AssignVariablesTask assignVariablesTask;
  
  @Mock
  IAbstractExecutionReader reader;
  
  @Test
  public void validListPassed()
  {
    Object execution = PowerMockito.mock(Object.class);
    Map<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("Key1", "value1");
    inputMap.put("Key2", "value2");
   // ((VariableScope)execution).setVariable("INPUT_VARIABLES_MAP", inputMap); 
   // System.out.println((VariableScope)execution.getVariable("INPUT_VARIABLES_MAP"));
    when(reader.getVariable(any(), any())).thenReturn(inputMap);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("assignVariablesTask",
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            "test1", "test2" });
    assignVariablesTask.setInputParameters(model, execution, reader);
    System.out.println(model);
  } 
}
