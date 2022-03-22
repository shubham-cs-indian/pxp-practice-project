package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * This class test the Reverse operation for given list
 * and returns the reversed list
 * @author Subham.Shaw
 */
@SuppressWarnings("unchecked")
public class ListReverseTaskTest extends DiMockitoTestConfig {    
  
  @InjectMocks
  @Autowired  
  ListReverseTask listReverseTask;
  
  @Test
  public void validListPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listReverseTask",
        new String[] { ListReverseTask.LIST_TO_REVERSE },
        new Object[] { Arrays.asList(new String[] { "JSON", "EXCEL", "XML", "PXON" })});
    listReverseTask.executeTask(model);
    List<Object> inputListToReverse = (List<Object>) model.getInputParameters().get(ListReverseTask.LIST_TO_REVERSE);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListReverseTask.RESULT_REVERSED_LIST);
    System.out.println("ListReverseTaskTest :: validListPassed :: output list size " + output.size());
    System.out.println("Input list:");
    inputListToReverse.forEach(System.out::println);
    System.out.println("Output list:");
    output.forEach(System.out::println);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListToReverse.size());
  }
  
  /**
   * To test null list
   */
  @Test
  public void nullInputList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listReverseTask",
        new String[] { ListReverseTask.LIST_TO_REVERSE },
        new Object[] { null });
    listReverseTask.executeTask(model);
    System.out.println("ListReverseTaskTest :: null List :: ");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TO_REVERSE is null."));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * To test empty list
   */
  @Test
  public void emptyInputList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listReverseTask",
        new String[] { ListReverseTask.LIST_TO_REVERSE },
        new Object[] { Collections.EMPTY_LIST });
    listReverseTask.executeTask(model);
    List<Object> inputListToReverse = (List<Object>) model.getInputParameters().get(ListReverseTask.LIST_TO_REVERSE);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListReverseTask.RESULT_REVERSED_LIST);
    System.out.println("ListReverseTaskTest :: empty List :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListToReverse.size());
  }
    
}
