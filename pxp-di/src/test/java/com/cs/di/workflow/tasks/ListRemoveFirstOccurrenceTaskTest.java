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

public class ListRemoveFirstOccurrenceTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired  
  ListRemoveFirstOccurrenceTask listRemoveFirstOccurrenceTask;
  
  /**
   * Valid numeric index is passed
   * valid index = REMOVE_FROM_LIST.size+1 
   * above would give valid index
   */
  @Test
  public void validTestData()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveFirstOccurrenceTask",
        new String[] { ListRemoveFirstOccurrenceTask.REMOVE_ITEM, ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST},
        new Object[] { "2",Arrays.asList(new String[] { "1", "2", "3", "2"})});
    listRemoveFirstOccurrenceTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemoveFirstOccurrenceTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemoveFirstOccurrenceTask :: validTestPassed :: output list size " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(Arrays.asList(new String[] { "1", "3", "2"}).equals(output));
  }
   
  @Test
  public void removeFromNullList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveFirstOccurrenceTask",
        new String[] { ListRemoveFirstOccurrenceTask.REMOVE_ITEM, ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST},
        new Object[] { "2", null});
    
    listRemoveFirstOccurrenceTask.executeTask(model);
    System.out.println("ListRemoveFirstOccurrenceTask :: removeFromNullList :: value for REMOVE_FROM_LIST"
        + model.getInputParameters().get(ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_LIST is null."));
  }
  
  @Test
  public void removeFromEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveFirstOccurrenceTask",
        new String[] { ListRemoveFirstOccurrenceTask.REMOVE_ITEM, ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST},
        new Object[] { "2", Collections.EMPTY_LIST});
    
    listRemoveFirstOccurrenceTask.executeTask(model);
    System.out.println("ListRemoveFirstOccurrenceTask :: removeFromEmptyList :: value for REMOVE_FROM_LIST "
        + model.getInputParameters().get(ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST));
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemoveFirstOccurrenceTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemoveFirstOccurrenceTask :: removeFromEmptyList :: output "+output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_FROM_LIST is empty."));
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_ITEM is Invalid."));
  }
  
  /**
   * Passing REMOVE_ITEM = empty
   * Passing REMOVE_FROM_LIST = empty 
   * 
   */
  @Test
  public void emptyRemoveItemWithEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveFirstOccurrenceTask",
        new String[] { ListRemoveFirstOccurrenceTask.REMOVE_ITEM, ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST},
        new Object[] { "", Collections.EMPTY_LIST});
    listRemoveFirstOccurrenceTask.executeTask(model);
    System.out.println("ListRemoveFirstOccurrenceTask :: emptyRemoveItemWithEmptyList :: value for INSERT_ITEM "
        + model.getInputParameters().get(ListRemoveFirstOccurrenceTask.REMOVE_ITEM));
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemoveFirstOccurrenceTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemoveFirstOccurrenceTask :: emptyRemoveItemWithEmptyList :: output "+output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_FROM_LIST is empty."));
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_ITEM is Invalid."));
  }
  
  /**
   * Passing REMOVE_ITEM = null
   * remove the null from input list
   * e.g. [1, 2, null]
   */
  @Test
  public void nullRemoveItemWithNonEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveFirstOccurrenceTask",
        new String[] { ListRemoveFirstOccurrenceTask.REMOVE_ITEM, ListRemoveFirstOccurrenceTask.REMOVE_FROM_LIST},
        new Object[] { null, Arrays.asList(new String[] { "1", "2", null})});
    listRemoveFirstOccurrenceTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemoveFirstOccurrenceTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemoveFirstOccurrenceTask :: nullRemoveItemWithNonEmptyList :: output  " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(Arrays.asList(new String[] { "1", "2"}).equals(output));
  }
  
}
