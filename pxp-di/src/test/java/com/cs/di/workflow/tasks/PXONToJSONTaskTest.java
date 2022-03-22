package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * This is to test 
 * @author mayuri.wankhade
 *
 */
public class PXONToJSONTaskTest extends DiIntegrationTestConfig {
	
	public static final String path = "src/test/resources/pxon/export#1000012.pxon";
	public static final String invalidPath = "src/test/resources/pxon/test/test.txt";
	public static final String fileNotFound = "src/test/resources/pxon/test.pxon";
	public static final String fileContentInvalid = "src/test/resources/pxon/export#1000343.pxon";
  
	@Autowired
	PXONToJSONTask PXONToJSONTask ;
	
	/**
	 * tested for valid PXON
	 * @throws Exception 
	 */
  @Test
  public void generateFromPXONValidTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToJSONTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { path });     
    PXONToJSONTask.transform(workflowTaskModel);
    System.out.println(workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
  }
	
	/**
	 * tested with valid folder with invalid fileName
	 * @throws Exception 
	 */
  //@Test
  public void fileNotFound() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToJSONTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { fileNotFound });
    PXONToJSONTask.transform(workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus(workflowTaskModel, MessageType.ERROR,
        "Input parameter PXON_FILE_PATH is Invalid."));
  }
	
  /**
   * tested with invalid path
   * @throws Exception 
   */
  //@Test
  public void invalidPath() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToJSONTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { invalidPath });
    PXONToJSONTask.transform(workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus(workflowTaskModel, MessageType.ERROR,
        "Input parameter PXON_FILE_PATH is Invalid."));
  }
  
  /**
   * tested with invalid content
   * @throws Exception 
   */
  //@Test
  public void fileContentInvalid() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToJSONTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { fileContentInvalid });
    PXONToJSONTask.transform(workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus(workflowTaskModel, MessageType.ERROR,
        "Error occured during processing of file: "+fileContentInvalid));
  }
  
  /**
   * tested with invalid content
   */
   @Test
  public void testValidInputs()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(AbstractFromPXONTask.PXON_FILE_PATH, path);
    List<String> listOfInvalidParams = (List<String>) PXONToJSONTask.validate(inputs);
    assertNull(listOfInvalidParams);
    
  }
  
   @Test
  public void testInValidInputs()
  {
    
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(AbstractFromPXONTask.PXON_FILE_PATH, invalidPath);
    List<String> listOfInvalidParams = (List<String>) PXONToJSONTask.validate(inputs);
    assertTrue(listOfInvalidParams.contains(AbstractFromPXONTask.PXON_FILE_PATH));
  }

}
