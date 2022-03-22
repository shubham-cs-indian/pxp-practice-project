package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class ListRemoveItemsTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired  
  ListRemoveAllOccurrencesTask listRemoveItemsTask;
  
  /**
   * Valid numeric index is passed
   * valid index = REMOVE_FROM_LIST.size+1 
   * above would give valid index
   */
  @Test
  public void validTestData()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listRemoveItemsTask",
        new String[] { ListRemoveAllOccurrencesTask.REMOVE_ITEM, ListRemoveAllOccurrencesTask.REMOVE_FROM_LIST},
        new Object[] { "2",Arrays.asList(new String[] { "1", "2", "3", "2"})});
    listRemoveItemsTask.executeTask(model);
    List<Object> output = (List<Object>) model.getOutputParameters().get(ListRemoveAllOccurrencesTask.REMOVED_RESULT_LIST);
    System.out.println("ListRemoveItemsTask :: validTestPassed :: output list size " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(Arrays.asList(new String[] { "1", "3"}).equals(output));
  }
}
