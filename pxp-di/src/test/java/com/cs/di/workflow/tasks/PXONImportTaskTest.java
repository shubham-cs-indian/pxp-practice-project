package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.services.CSProperties;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class PXONImportTaskTest extends DiMockitoTestConfig {
  
  @Autowired
  @InjectMocks
  PXONImportTask   pxonImportTask;
  
  ITransactionData transactionData = new TransactionData();
  
  @Before
  public void setUp2() throws Exception
  {
    transactionData.setDataLanguage("en_US");
    transactionData.setUiLanguage("en_US");
    transactionData.setOrganizationId("-1");
    transactionData.setPhysicalCatalogId("pim");
    transactionData.setEndpointId("");
    transactionData.setUserId("1");
    transactionData.setUserName("admin");
  }
  
  /**
   * Test to check if with valid input task is getting executed successfully
   * 
   * @throws Exception
   */
  @Test
  public void testWithValidInput() throws Exception
  {
    createTestDirectoryAndTestFile("/_pxon_test.pxon");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("PXONImportTask", new String[] { "FILE_NAME" },
        new Object[] { "_pxon_test.pxon" });
    WorkflowModel workflowModel = new WorkflowModel(null, transactionData, null);
    model.setWorkflowModel(workflowModel);
    pxonImportTask.executeTask(model);
    deleteTestDirectory();
    assertNotNull(model.getOutputParameters().get("JOB_ID"));
  }
  
  /**
   * Create a file in test directory
   * 
   * @param fileName
   * @throws Exception
   */
  private void createTestDirectoryAndTestFile(String fileName) throws Exception
  {
    String path = CSProperties.instance().getString("nfs.file.path") + "/" + fileName;
    File file = new File(path);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
  }
  
  /**
   * Delete all test directory
   * 
   * @throws Exception
   */
  private void deleteTestDirectory() throws Exception
  {
    String path = CSProperties.instance().getString("nfs.file.path") + "/" + "test";
    File file = new File(path);
    if (file.exists()) {
      FileUtils.deleteDirectory(file);
    }
  }
  
  /**
   * Test for checking output of task with missing required input parameters
   */
  @Test
  public void testWithMissingRequiredInput()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("PXONImportTask", new String[] { "FILE_NAME" }, new Object[] { "" });
    pxonImportTask.executeTask(model);
    assertTrue(model.getExecutionStatusTable().isErrorOccurred());
  }
  
  /**
   * Test for checking output of task with valid file name but file is not
   * present in the destination
   */
  @Test
  public void testFileNotFound()
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("PXONImportTask", new String[] { "FILE_NAME" }, new Object[] { "test1.pxon" });
    WorkflowModel workflowModel = new WorkflowModel(null, transactionData, null);
    model.setWorkflowModel(workflowModel);
    pxonImportTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "File not found"));
  }
  
  
  /**
   * Test to check valid input parameters
   */
  @Test
  public void testValidInputParameters()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put("FILE_NAME", "testArticle.pxon");
    List<String> listOfInvalidParams = pxonImportTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  
  /**
   * Test to check invalid input parameters
   */
  @Test
  public void testInvalidInputParameters()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put("FILE_NAME", "abc.excel");
    List<String> listOfInvalidParams = pxonImportTask.validate(inputs);
    assertTrue(listOfInvalidParams.contains("FILE_NAME"));
  }
  
  /**
   * Test to check empty required input parameters
   */
  @Test
  public void testEmptyRequiredInputParameters()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put("FILE_NAME", "");
    List<String> listOfInvalidParams = pxonImportTask.validate(inputs);
    assertTrue(listOfInvalidParams.contains("FILE_NAME"));
  }
  
}
