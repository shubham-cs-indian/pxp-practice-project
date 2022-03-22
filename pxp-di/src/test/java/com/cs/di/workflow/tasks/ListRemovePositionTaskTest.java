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

public class ListRemovePositionTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired  
  ListRemovePositionTask listRemovePositionTask;
  
  /**
   * Valid numeric index is passed
   * valid index = INSERT_INTO_LIST.size+1 
   * above would give valid index
   */
  @Test
  public void validTestData()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {"2", Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemovePositionTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemovePositionTaskTest :: validIndexPassed :: output list size " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(!output.contains("2"));
  }
  
  @Test
  public void zeroIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {"0", Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: nullIndexPassed :: value for REMOVE_FROM_INDEX "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is Invalid."));
  }
  
  @Test
  public void nullIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {null, Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: nullIndexPassed :: value for REMOVE_FROM_INDEX "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is null."));
  }
  
  @Test
  public void emptyIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {"", Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTask :: emptyIndexPassed :: value for REMOVE_FROM_INDEX "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is empty."));
  }
  
  /**
   * passed value to index = "index"
   * parsing of value causes error 
   */
  @Test
  public void nonNumericIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {"index", Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: invalidIndexPassed :: value for REMOVE_FROM_INDEX "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is Invalid."));
  }
  
  @Test
  public void indexGrtThanListSizedPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX ,ListRemovePositionTask.REMOVE_FROM_LIST },
        new Object[] {"5", Arrays.asList(new String[] { "1", "2", "3"})});
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: indexGrtThanListSizedPassed :: value for REMOVE_FROM_INDEX "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is Invalid."));
  }
  
  @Test
  public void removeFromNullList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX, ListRemovePositionTask.REMOVE_FROM_LIST }, new Object[] { "5", null });
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: nullRemoveFromList :: value for REMOVE_FROM_LIST "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_FROM_LIST is null."));
  }
  
  @Test
  public void removeFromEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemovePositionTask",
        new String[] { ListRemovePositionTask.REMOVE_FROM_INDEX, ListRemovePositionTask.REMOVE_FROM_LIST }, new Object[] { "2", Collections.EMPTY_LIST });
    listRemovePositionTask.executeTask(model);
    System.out.println("ListRemovePositionTaskTest :: emptyInsertIntoList :: value for REMOVE_FROM_LIST "
        + model.getInputParameters().get(ListRemovePositionTask.REMOVE_FROM_LIST));
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemovePositionTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemovePositionTaskTest :: removeFromEmptyList :: output "+output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter REMOVE_FROM_LIST is empty."));
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter REMOVE_FROM_INDEX is Invalid."));

  }
}
