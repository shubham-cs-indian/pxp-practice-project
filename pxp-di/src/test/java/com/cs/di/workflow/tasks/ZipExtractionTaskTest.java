package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cs.core.services.CSProperties;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * 
 * Unit testing for zip extraction task
 * 
 * @author bhagwat.bade
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "jdk.internal.reflect.*", "javax.management.*" })
@SuppressStaticInitializationFor("com.cs.workflow.base.AbstractTask")
@PrepareForTest({ DiValidationUtil.class })
public class ZipExtractionTaskTest {
  
  @InjectMocks
  ZipExtractionTask zipExtractionTask;
  
  /**
   * Workflow model mocking
   * 
   * @return
   */
  private WorkflowTaskModel getWorkflowTaskModel()
  {
    WorkflowTaskModel model = new WorkflowTaskModel();
    WorkflowModel workflowModel = new WorkflowModel(null, null, null);
    model.setWorkflowModel(workflowModel);
    return model;
  }
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    CSProperties.init(".//src//test//resources//config//junit-test.properties");
  }
  
  /**
   * Stub file paths
   * 
   * @param model
   * @param inputFolderPath
   * @param isRecursive
   * @param outputFolderPath
   * @param archivePath
   */
  private void stubDiValidationUtil(WorkflowTaskModel model, String inputFolderPath, Boolean isRecursive, String outputFolderPath,
      String archivePath)
  {
    PowerMockito.mockStatic(DiValidationUtil.class);
    when(DiValidationUtil.validateAndGetRequiredString(model, ZipExtractionTask.INPUT_FOLDER_PATH)).thenReturn(inputFolderPath);
    when(DiValidationUtil.validateAndGetOptionalBoolean(model, ZipExtractionTask.IS_RECURSIVE)).thenReturn(isRecursive);
    when(DiValidationUtil.validateAndGetRequiredString(model, ZipExtractionTask.OUTPUT_FOLDER_PATH)).thenReturn(outputFolderPath);
    when(DiValidationUtil.validateAndGetOptionalString(model, ZipExtractionTask.ARCHIVAL_PATH)).thenReturn(archivePath);
  }
  
  /**
   * Test check invalid input, output, archive folder path and exception
   */
  @Test
  public void invalidInputOutputFolderTest()
  {
    WorkflowTaskModel model = getWorkflowTaskModel();
    
    // DiValidationUtil
    String inputFolderPath = "";
    Boolean isRecursive = false;
    String outputFolderPath = "";
    String archivePath = "";
    this.stubDiValidationUtil(model, inputFolderPath, isRecursive, outputFolderPath, archivePath);
    
    this.zipExtractionTask.executeTask(model);
    
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable().getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + ZipExtractionTask.INPUT_FOLDER_PATH + " is Invalid.").equals(errorList.get(0).toString()));
    
    // output path error
    assertTrue(("Input parameter " + ZipExtractionTask.OUTPUT_FOLDER_PATH + " is Invalid.").equals(errorList.get(1).toString()));
    
    // input and output path same
    assertTrue(("Values of " + DeliveryTask.class.getName() + " parameters " + ZipExtractionTask.INPUT_FOLDER_PATH + " and "
        + ZipExtractionTask.OUTPUT_FOLDER_PATH + " cannot be same or overlaping. Respective values are " + inputFolderPath + " and "
        + outputFolderPath).equals(errorList.get(2).toString()));
    
    // Exception handling
    assertTrue(("Error occured in Camunda Task " + ZipExtractionTask.class.getName() + " Id:" + model.getTaskId())
        .equals(errorList.get(3).toString()));
    
    // check warnings
    List<? extends IOutputExecutionStatusModel> warningList = model.getExecutionStatusTable().getExecutionStatusTableMap()
        .get(MessageType.WARNING);
    
    // archival path warning
    assertTrue(("Input parameter " + ZipExtractionTask.ARCHIVAL_PATH + " is Invalid.").equals(warningList.get(0).toString()));
    
  }
  
  /**
   * valid test for zip extraction
   */
  @Test
  public void zipExtractionTaskValidTest()
  {
    // workflow model
    File file = new File("D:\\\\input\\input.zip");
    if (!file.exists())
      return;
    
    WorkflowTaskModel model = getWorkflowTaskModel();
    
    // DiValidationUtil
    String inputFolderPath = "D:\\input";
    Boolean isRecursive = false;
    String outputFolderPath = "D:\\output";
    String archivePath = "D:\\archive";
    this.stubDiValidationUtil(model, inputFolderPath, isRecursive, outputFolderPath, archivePath);
    
    this.zipExtractionTask.executeTask(model);
    
    assertTrue(!file.exists());
    
  }
  
  /**
   * Create a test directory
   * 
   * @throws Exception
   */
  private void createTestDirectory() throws Exception
  {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    String path = nfsDirPath + "/testInput/testOutput/testArchive/";
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }
  
  /**
   * Delete test directory once test executed This would perform the clean up
   * activity
   * 
   * @throws Exception
   */
  @After
  public void deleteTestDirectory() throws Exception
  {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    String path = nfsDirPath + "/testInput";
    File file = new File(path);
    if (file.exists()) {
      FileUtils.deleteDirectory(file);
    }
  }
  
  /**
   * Test to check validate method with valid input values
   * 
   * @throws Exception
   */
  @Test
  public void testValidateMethodForValidInputParamters() throws Exception
  {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    createTestDirectory();
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ZipExtractionTask.INPUT_FOLDER_PATH, nfsDirPath + "/testInput");
    inputs.put(ZipExtractionTask.OUTPUT_FOLDER_PATH, nfsDirPath + "/testInput/testOutput");
    inputs.put(ZipExtractionTask.ARCHIVAL_PATH, nfsDirPath + "/testInput/testOutput/testArchive");
    inputs.put(ZipExtractionTask.IS_RECURSIVE, "true");
    List<String> listOfInvalidParams = zipExtractionTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  /**
   * Test to check validate method with invalid input values
   * 
   * @throws Exception
   */
  @Test
  public void testValidateMethodForInvalidInputParamters() throws Exception
  {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ZipExtractionTask.INPUT_FOLDER_PATH, nfsDirPath + "/xyzInputDir");
    inputs.put(ZipExtractionTask.OUTPUT_FOLDER_PATH, nfsDirPath + "/xyzInputDir/xyzOutputDir");
    inputs.put(ZipExtractionTask.ARCHIVAL_PATH, nfsDirPath + "/xyzInputDir/xyzOutputDir/xyzArchiveDir");
    inputs.put(ZipExtractionTask.IS_RECURSIVE, null);
    List<String> listOfInvalidParams = zipExtractionTask.validate(inputs);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, Arrays.asList(ZipExtractionTask.INPUT_FOLDER_PATH,
        ZipExtractionTask.OUTPUT_FOLDER_PATH, ZipExtractionTask.ARCHIVAL_PATH)));
  }
  
  /**
   * Test to check validate method with valid and invalid input values
   * 
   * @throws Exception
   */
  @Test
  public void testValidateMethodForValidAndInvalidInputParamters() throws Exception
  {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    createTestDirectory();
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ZipExtractionTask.INPUT_FOLDER_PATH, nfsDirPath + "/testInput");
    inputs.put(ZipExtractionTask.OUTPUT_FOLDER_PATH, nfsDirPath + "/testInput/testOutput");
    inputs.put(ZipExtractionTask.ARCHIVAL_PATH, nfsDirPath + "/xyzInputDir/xyzOutputDir/testArchive");
    inputs.put(ZipExtractionTask.IS_RECURSIVE, null);
    List<String> listOfInvalidParams = zipExtractionTask.validate(inputs);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, Arrays.asList(ZipExtractionTask.ARCHIVAL_PATH)));
  }
  
  /**
   * Test to check if INPUT_FOLDER_PATH/OUTPUT_FOLDER_PATH/ARCHIVAL_PATH paths are equal
   * 
   * @throws Exception
   */
  @Test
  public void testValidateForSameInputOutputFolderPath() throws Exception {
    String nfsDirPath = CSProperties.instance().getString("nfs.file.path");
    createTestDirectory();
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ZipExtractionTask.INPUT_FOLDER_PATH, nfsDirPath + "/testInput");
    inputs.put(ZipExtractionTask.OUTPUT_FOLDER_PATH, nfsDirPath + "/testInput/");
    List<String> listOfInvalidParams = zipExtractionTask.validate(inputs);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, Arrays.asList(ZipExtractionTask.OUTPUT_FOLDER_PATH)));
  }
  
}
