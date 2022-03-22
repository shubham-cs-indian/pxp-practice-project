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
 * Test the append operation on two list
 * @author mayuri.wankhade
 *
 */
public class ListExtendTaskTest extends DiMockitoTestConfig {    
  
  @InjectMocks
  @Autowired  
  ListExtendTask listExtendTask;
  
  @Test
  public void validListPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { Arrays.asList(new String[] { "JSON", "EXCEL" }), Arrays.asList(new String[] { "JSON" }) });
    listExtendTask.executeTask(model);
    List<Object> inputListToWhich = (List<Object>) model.getInputParameters().get(ListExtendTask.LIST_TOWHICH_APPEND);
    List<Object> inputListTo = (List<Object>) model.getInputParameters().get(ListExtendTask.LIST_TOAPPENDED);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListExtendTask.APPENDED_RESULT_LIST);
    System.out.println("ListExtendTaskTest :: validListPassed :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == (inputListToWhich.size() + inputListTo.size()));
  }
  
  @Test
  public void nullToAppenList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { null, Arrays.asList(new String[] { "JSON" }) });
    listExtendTask.executeTask(model);
    System.out.println("ListExtendTaskTest :: nullToAppenList :: ");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TOAPPENDED is null."));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void nullToWhichAppenList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { Arrays.asList(new String[] { "JSON", "EXCEL" }), null });
    listExtendTask.executeTask(model);
    System.out.println("ListExtendTaskTest :: nullToWhichAppenList :: ");
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TOWHICH_APPEND is null."));
  }
  
  @Test
  public void emptyToAppenList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { Collections.EMPTY_LIST, Arrays.asList(new String[] { "JSON" }) });
    listExtendTask.executeTask(model);
    List<Object> inputListToWhich = (List<Object>) model.getInputParameters().get(ListExtendTask.LIST_TOWHICH_APPEND);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListExtendTask.APPENDED_RESULT_LIST);
    System.out.println("ListExtendTaskTest :: emptyToAppenList :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListToWhich.size());
  }
  
  @Test
  public void emptyToWhichAppenList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { Arrays.asList(new String[] { "JSON", "EXCEL" }), Collections.EMPTY_LIST });
    listExtendTask.executeTask(model);
    List<Object> inputListTo = (List<Object>) model.getInputParameters().get(ListExtendTask.LIST_TOAPPENDED);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListExtendTask.APPENDED_RESULT_LIST);
    System.out.println("ListExtendTaskTest :: emptyToWhichAppenList :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == inputListTo.size());
  }  
  
  @Test
  public void emptyBothList()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listExtendTask",
        new String[] { ListExtendTask.LIST_TOAPPENDED, ListExtendTask.LIST_TOWHICH_APPEND },
        new Object[] { Collections.EMPTY_LIST, Collections.EMPTY_LIST });
    listExtendTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListExtendTask.APPENDED_RESULT_LIST);
    System.out.println("ListExtendTaskTest :: emptyBothList :: output list size " + output.size());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output.size() == 0);
  }  
}
