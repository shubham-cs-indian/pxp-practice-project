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
public class ListAppendTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  ListAppendTask listAppendTask;
  
  @Test
  @SuppressWarnings("unchecked")
  public void testWithValidInputs() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listAppendTask",
        new String[] { ListAppendTask.LIST_TO_WHICH_APPEND, ListAppendTask.APPEND_ITEM },
        new Object[] { Arrays.asList(new String[] { "KING", "QUEEN", "JOKER" }), "RAJA" });
    listAppendTask.executeTask(model);
    List<Object> inputList = (List<Object>) model.getInputParameters().get(ListAppendTask.LIST_TO_WHICH_APPEND);
    List<Object> outputList = (List<Object>) model.getOutputParameters().get(ListAppendTask.APPEND_RESULT_LIST);
    System.out.println("listAppendTaskTest :: testWithValidInputs :: output list " + outputList);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(outputList.size() == (inputList.size() + 1));
  }
 
  @Test
  public void nullAppendList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listAppendTask",
        new String[] { ListAppendTask.LIST_TO_WHICH_APPEND, ListAppendTask.APPEND_ITEM },
        new Object[] { null, Arrays.asList(new String[] { "RAJA" }) });
    listAppendTask.executeTask(model);
    System.out.println("listAppendTaskTest :: nullAppendList :: ");
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TOWHICH_APPEND is null."));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void nullAppendItem()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listAppendTask",
        new String[] { ListAppendTask.LIST_TO_WHICH_APPEND, ListAppendTask.APPEND_ITEM },
        new Object[] { Arrays.asList(new String[] { "KING", "QUEEN", "JOKER" }), null });
    listAppendTask.executeTask(model);
    List<Object> outputList = (List<Object>) model.getOutputParameters().get(ListAppendTask.APPEND_RESULT_LIST);
    System.out.println("listAppendTaskTest :: nullAppendItem :: output list " + outputList);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter APPEND_ITEM is null."));
    
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void emptyAppenList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listAppendTask",
        new String[] { ListAppendTask.LIST_TO_WHICH_APPEND, ListAppendTask.APPEND_ITEM },
        new Object[] {  Arrays.asList(new String[] {}), Arrays.asList(new String[] { "RAJA" }) });
    listAppendTask.executeTask(model);
    List<Object> inputList = (List<Object>) model.getInputParameters().get(ListAppendTask.LIST_TO_WHICH_APPEND);
    List<Object> outputList = (List<Object>) model.getOutputParameters().get(ListAppendTask.APPEND_RESULT_LIST);
    System.out.println("listAppendTaskTest :: emptyAppenList :: output list " + outputList);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(outputList.size() == (inputList.size() + 1));
  }  
}

