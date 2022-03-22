package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

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
 * Test the number of occurrence of item in given list
 * @author mangesh.metkari
 *
 */
public class ListCountElementTaskTest extends DiMockitoTestConfig {    
  
  @InjectMocks
  @Autowired  
  ListCountElementTask listCountElementTask;
  
  @Test
  @SuppressWarnings("unchecked")
  public void validInputPassed()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listCountElementTask",
        new String[] { ListCountElementTask.LIST_TO_COUNT_ELEMENT, ListCountElementTask.COUNT_ITEM },
        new Object[] { Arrays.asList(new String[] { "KING", "JOKER", "QUEEN", "JOKER" }), "JOKER" });
    listCountElementTask.executeTask(model);
    
    List<Object> inputList = (List<Object>) model.getInputParameters().get(ListCountElementTask.LIST_TO_COUNT_ELEMENT);
    Object searchItem = model.getInputParameters().get(ListCountElementTask.COUNT_ITEM);
    Object count = model.getOutputParameters().get(ListCountElementTask.RESULT_LISTCOUNTELEMENT);
    Long elementCount = inputList.stream().filter(str -> str.equals(searchItem)).count();
    System.out.println("listCountElementTask :: validInputPassed :: element count " + count);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(elementCount == (Long) count);
  }
  
  @Test
  public void nullListToCountElement()
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listCountElementTask",
        new String[] { ListCountElementTask.LIST_TO_COUNT_ELEMENT, ListCountElementTask.COUNT_ITEM },
        new Object[] { null, "JOKER" });
    listCountElementTask.executeTask(model);
    Object count = model.getOutputParameters().get(ListCountElementTask.RESULT_LISTCOUNTELEMENT);
    System.out.println("listCountElementTask :: nullListToCountElement ::  count "+count);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter LIST_TO_COUNT_ELEMENT is null."));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void nullCountElement()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listCountElementTask",
        new String[] { ListCountElementTask.LIST_TO_COUNT_ELEMENT, ListCountElementTask.COUNT_ITEM },
        new Object[] { Arrays.asList(new String[] { "KING", "QUEEN", "JOKER" }), null});
    listCountElementTask.executeTask(model);
    Object count = model.getOutputParameters().get(ListCountElementTask.RESULT_LISTCOUNTELEMENT);
    System.out.println("listCountElementTask :: nullCountElement :: count "+count);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter COUNT_ITEM is null."));
  }
  
  @Test
  public void emptyListToCountElement()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listCountElementTask",
        new String[] { ListCountElementTask.LIST_TO_COUNT_ELEMENT, ListCountElementTask.COUNT_ITEM },
        new Object[] { Arrays.asList(new String[] { }), "JOKER"});
    listCountElementTask.executeTask(model);
    Object count = model.getOutputParameters().get(ListCountElementTask.RESULT_LISTCOUNTELEMENT);
    System.out.println("listCountElementTask :: emptyListToCountElement :: count " + count);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter LIST_TO_COUNT_ELEMENT is empty."));
  }  
}
