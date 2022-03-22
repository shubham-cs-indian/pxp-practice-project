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
 * Test the insertion of item at specified index in a given list 
 * @author mayuri.wankhade
 *
 */
public class ListInsertPositionTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired  
  ListInsertPositionTask listInsertPositionTask;
  
  /**
   * Valid numeric index is passed
   * valid index = INSERT_INTO_LIST.size+1 
   * above would give valid index
   */
  @Test
  public void validTestData()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","3",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListInsertPositionTask.INSERT_RESULT_LIST);
    System.out.println("ListInsertPositionTaskTest :: validIndexPassed :: output list size " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.contains("INSERT_TEST"));
  }
  
  @Test
  public void zeroIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","0",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: nullIndexPassed :: value for INSERT_AT_INDEX "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_AT_INDEX));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter INSERT_AT_INDEX is Invalid."));
  }
  
  @Test
  public void nullIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST",null,Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: nullIndexPassed :: value for INSERT_AT_INDEX "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_AT_INDEX));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter INSERT_AT_INDEX is null."));
  }
  
  @Test
  public void emptyIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: emptyIndexPassed :: value for INSERT_AT_INDEX "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_AT_INDEX));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter INSERT_AT_INDEX is empty."));
  }
  
  /**
   * passed value to index = "index"
   * parsing of value causes error 
   */
  @Test
  public void nonNumericIndexPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","index",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: invalidIndexPassed :: value for INSERT_AT_INDEX "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_AT_INDEX));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter INSERT_AT_INDEX is Invalid."));
  }
  
  @Test
  public void indexGrtThanListSizedPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","4",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: indexGrtThanListSizedPassed :: value for INSERT_AT_INDEX "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_AT_INDEX));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter INSERT_AT_INDEX is Invalid."));
  }
  
  @Test
  public void nullInsertIntoList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","3",null});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: nullInsertIntoList :: value for INSERT_INTO_LIST "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_INTO_LIST));
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter INSERT_INTO_LIST is null."));
  }
  
  @Test
  public void emptyInsertIntoList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "INSERT_TEST","1",Collections.EMPTY_LIST});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: emptyInsertIntoList :: value for INSERT_INTO_LIST "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_INTO_LIST));
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListInsertPositionTask.INSERT_RESULT_LIST);
    System.out.println("ListInsertPositionTaskTest :: emptyInsertIntoList :: output "+output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter INSERT_INTO_LIST is empty."));
    assertTrue(output.contains("INSERT_TEST"));
  }
  
  /**
   * Passing INSERT_ITEM = null
   * null is getting inserted at given index
   * e.g. [null]
   */
  @Test
  public void emptyInsertItemWithEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { "","1",Collections.EMPTY_LIST});
    listInsertPositionTask.executeTask(model);
    System.out.println("ListInsertPositionTaskTest :: emptyInsertItemWithEmptyList :: value for INSERT_ITEM "
        + model.getInputParameters().get(ListInsertPositionTask.INSERT_ITEM));
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListInsertPositionTask.INSERT_RESULT_LIST);
    System.out.println("ListInsertPositionTaskTest :: emptyInsertItemWithEmptyList :: output "+output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * Passing INSERT_ITEM = null
   * null is getting inserted at given index
   * e.g. [JSON, EXCEL, null]
   */
  @Test
  public void nullInsertItemWithNonEmptyList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listInsertPositionTask",
        new String[] { ListInsertPositionTask.INSERT_ITEM, ListInsertPositionTask.INSERT_AT_INDEX,ListInsertPositionTask.INSERT_INTO_LIST },
        new Object[] { null,"3",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listInsertPositionTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListInsertPositionTask.INSERT_RESULT_LIST);
    System.out.println("ListInsertPositionTaskTest :: emptyInsertItemWithNonEmptyList :: output  " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.contains(null));
  }
  
}
