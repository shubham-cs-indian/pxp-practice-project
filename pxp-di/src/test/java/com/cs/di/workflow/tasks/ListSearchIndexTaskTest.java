package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.model.WorkflowTaskModel;
/**
 * This class test the Search operation for given search item
 * in the specified List and returns the position of
 * given item in the list else 0
 * @author mayuri.wankhade
 *
 */
public class ListSearchIndexTaskTest extends DiMockitoTestConfig{
  
  @InjectMocks
  @Autowired  
  ListSearchIndexTask listSearchIndexTask;
  
  @Test
  public void searchItemAtPos1()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listSearchIndexTask",
        new String[] { ListSearchIndexTask.SEARCH_ITEM, ListSearchIndexTask.SEARCH_IN_LIST},
        new Object[] { "JSON",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listSearchIndexTask.executeTask(model);
    int output = (int)model.getOutputParameters().get(ListSearchIndexTask.SEARCH_RESULT_INDEX);
    System.out.println("ListSearchIndexTaskTest :: searchItemAtPos1 :: item located at " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output==1);
  }
  
  @Test
  public void searchItemAtPos2()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listSearchIndexTask",
        new String[] { ListSearchIndexTask.SEARCH_ITEM, ListSearchIndexTask.SEARCH_IN_LIST},
        new Object[] { "EXCEL",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listSearchIndexTask.executeTask(model);
    int output = (int)model.getOutputParameters().get(ListSearchIndexTask.SEARCH_RESULT_INDEX);
    System.out.println("ListSearchIndexTaskTest :: searchItemAtPos2 :: item located at " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output==2);
  }
  
  /**
   * Search Item is not present in the list
   * expected output = 0
   */
  @Test
  public void invalidSearchItem()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("listSearchIndexTask",
        new String[] { ListSearchIndexTask.SEARCH_ITEM, ListSearchIndexTask.SEARCH_IN_LIST},
        new Object[] { "SEARCH",Arrays.asList(new String[] { "JSON", "EXCEL"})});
    listSearchIndexTask.executeTask(model);
    int output = (int)model.getOutputParameters().get(ListSearchIndexTask.SEARCH_RESULT_INDEX);
    System.out.println("ListSearchIndexTaskTest :: searchItemAtPos2 :: item located at " + output);
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(output==0);
  }
  
}
