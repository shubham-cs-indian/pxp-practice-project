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
 * This class test the Sort operation for given list
 * in the specified order and returns the sorted list
 * @author Subham.Shaw
 */
@SuppressWarnings("unchecked")
public class ListSortTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  @Autowired
  ListSortTask listSortTask;
  
  @SuppressWarnings("unused")
  @Test
  public void validListPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(
        "listSortTask", new String[] { ListSortTask.LIST_TO_SORT, ListSortTask.SORT_ORDER },
        new Object[] { Arrays.asList(new String[] { "JSON", "EXCEL", "XML", "PXON" }), "asc" });
    listSortTask.executeTask(model);
    String sortOrder = (String) model.getInputParameters().get(ListSortTask.SORT_ORDER);
    List<Object> inputListToSort = (List<Object>) model.getInputParameters().get(ListSortTask.LIST_TO_SORT);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListSortTask.RESULT_SORTED_LIST);
    System.out.println("ListSortTaskTest :: validListPassed :: output list size " + output.size());
    System.out.println("Input list:");
    inputListToSort.forEach(System.out::println);
    System.out.println("Output list:");
    output.forEach(System.out::println);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListToSort.size());
  }
  
  /**
   * To test null list
   */
  @Test
  public void nullInputList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listSortTask",
        new String[] { ListSortTask.LIST_TO_SORT, ListSortTask.SORT_ORDER },
        new Object[] { null, "asc" });
    listSortTask.executeTask(model);
    System.out.println("ListSortTaskTest :: nullToAppenList :: ");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TO_SORT is null."));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * To test empty list
   */
  @Test
  public void emptyInputList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listSortTask",
        new String[] { ListSortTask.LIST_TO_SORT, ListSortTask.SORT_ORDER },
        new Object[] { Collections.EMPTY_LIST, "asc" });
    listSortTask.executeTask(model);
    List<Object> inputListToWhich = (List<Object>) model.getInputParameters().get(ListSortTask.LIST_TO_SORT);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListSortTask.RESULT_SORTED_LIST);
    System.out.println("ListSortTaskTest :: emptyToAppenList :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListToWhich.size());
  }
}
