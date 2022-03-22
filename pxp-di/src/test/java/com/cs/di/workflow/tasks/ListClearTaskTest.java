package com.cs.di.workflow.tasks;


import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * Test the append operation on list and new item
 * @author mangesh.metkari
 *
 */
public class ListClearTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  ListClearTask listClearTask;
  
  @Test
  @SuppressWarnings("unchecked")
  public void testWithValidInputs() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listClearTask",
        new String[] { ListClearTask.LIST_TO_CLEAR },
        new Object[] { Arrays.asList(new String[] { "KING", "QUEEN", "JOKER" })});
    List<Object> listToClear = (List<Object>) model.getInputParameters().get(ListClearTask.LIST_TO_CLEAR);
    System.out.println("listClearTaskTest :: emptyClearList :: input list " + listToClear);
    listClearTask.executeTask(model);
    List<Object> clearListResult = (List<Object>) model.getInputParameters().get(ListClearTask.LIST_TO_CLEAR);
    System.out.println("listClearTaskTest :: testWithValidInputs :: output list " + clearListResult);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(clearListResult.size() == 0);
  }
 
  @Test
  public void nullClearList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listClearTask",
        new String[] { ListClearTask.LIST_TO_CLEAR},
        new Object[] { null});
    listClearTask.executeTask(model);
    System.out.println("listClearTaskTest :: nullClearList :: ");
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TO_CLEAR is null."));
  }
  
  
  @Test
  @SuppressWarnings("unchecked")
  public void emptyClearList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listClearTask",
        new String[] { ListClearTask.LIST_TO_CLEAR },
        new Object[] {  Arrays.asList(new String[] {"abc", "abc"})});
    List<Object> listToClear = (List<Object>) model.getInputParameters().get(ListClearTask.LIST_TO_CLEAR);
    listClearTask.executeTask(model);
    List<Object> clearListResult = (List<Object>) model.getInputParameters().get(ListClearTask.LIST_TO_CLEAR);
    System.out.println("listClearTaskTest :: emptyClearList :: output list " + clearListResult);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(clearListResult.size() == (listToClear.size()));
  }  
}

