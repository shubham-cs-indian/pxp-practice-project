package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * this Class would test Task PXONToExcelTask
 * 
 * @author mayuri.wankhade
 *
 */
public class PXONToExcelTaskTest extends DiIntegrationTestConfig {
  
  public static final String path             = "src/test/resources/pxon/export#1000012.pxon";
  public static final String articleVariant   = "src/test/resources/pxon/export#1000004.pxon";
  public static final String relationshipData = "src/test/resources/pxon/export#1000029.pxon";
  public static final String tagData          = "src/test/resources/pxon/export#1000017.pxon";
  public static final String invalidPath      = "src/test/resources/pxon/export#10000.pxon";
  
  @Autowired
  PXONToExcelTask            PXONToExcelTask;
  
  /**
   * tested for valid PXON
   * 
   * @throws Exception
   */
  @Test
  public void generateFromPXONValidTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { path });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" generateFromPXONValidTestData " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested with ArticleVariant PXON
   * 
   * @throws Exception
   */
  // @Test
  public void testArticleVariants() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { articleVariant });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testArticleVariants " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested with Articles PXON
   * 
   * @throws Exception
   */
  // @Test
  public void testMultipleArticles() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { path });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testMultipleArticles " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested for valid Assets PXON
   * 
   * @throws Exception
   */
  // @Test
  public void testMultipleAssets() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { path });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testMultipleAssets " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested for valid Relationships PXON
   * 
   * @throws Exception
   */
  // @Test
  public void testMultipleRelationships() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { path });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testMultipleRelationships " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested for valid Relationship PXON
   * 
   * @throws Exception
   */
  @Test
  public void testRelationship() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { relationshipData });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testRelationship " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested for valid Article/Asset/Relationship PXON
   * 
   * @throws Exception
   */
  @Test
  public void testWithAllCombination() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { tagData });
    PXONToExcelTask.transform(workflowTaskModel);
    RDBMSLogger.instance().info(" testWithAllCombination " + workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA"));
    Map<String, Object> output = (Map<String, Object>) workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
    assertTrue(!output.isEmpty());
  }
  
  /**
   * tested for invalid PXON_FILE_PATH PXON
   * 
   * @throws Exception
   */
  @Test
  public void testWithInvalidTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("PXONToExcelTask",
        new String[] { AbstractFromPXONTask.PXON_FILE_PATH }, new String[] { invalidPath });
    PXONToExcelTask.transform(workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus(workflowTaskModel, MessageType.ERROR, "Input parameter PXON_FILE_PATH is Invalid."));
  }
  
  /**
   * tested with invalid content
   */
  @Test
  public void testValidInputs()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(AbstractFromPXONTask.PXON_FILE_PATH, path);
    List<String> listOfInvalidParams = (List<String>) PXONToExcelTask.validate(inputs);
    assertNull(listOfInvalidParams);
  }
  
  @Test
  public void testInValidInputs()
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(AbstractFromPXONTask.PXON_FILE_PATH, invalidPath);
    List<String> listOfInvalidParams = (List<String>) PXONToExcelTask.validate(inputs);
    assertTrue(listOfInvalidParams.contains(AbstractFromPXONTask.PXON_FILE_PATH));
  }
  
}
