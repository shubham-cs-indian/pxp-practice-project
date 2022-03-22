package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.ExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class ConfigToPXONTaskTest extends DiIntegrationTestConfig {
  
  private static final String CONFIG_IMPORT_XLSX             = "ConfigImport.xlsx";
  private static final String CONFIG_TRANSLATION_IMPORT_XLSX = "TranslationImport.xlsx";
  
  public static final String  excelForAttributes             = "src/test/resources/inbound/ConfigImport.xlsx";
  public static final String  excelForTranslations           = "src/test/resources/inbound/TranslationImport.xlsx";
  
  @Autowired
  ConfigToPXONTask            configToPXONTask;
  
  /**
   * This method is setting all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   */
  public WorkflowTaskModel setWorkflowTaskModel(ConfigNodeType configNodeType) throws IOException
  {
    Map<String, Map<String, String>> receivedData = new HashMap<String, Map<String, String>>();
    Map<String, String> test = new HashMap<String, String>();
    test.put("EXCEL", getByteStream());
    receivedData.put("EXCEL", test);
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configToPXONTask",
        new String[] { AbstractConfigToPXONTask.RECEIVED_DATA, AbstractConfigToPXONTask.NODE_TYPE },
        new Object[] { receivedData, configNodeType });
    return workflowTaskModel;
  }
  
  String getByteStream() throws IOException
  {
    return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(new File(excelForAttributes).toURI())));
  }
  
  /**
   * Test attribute with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForAttributesWithValidTestData() throws IOException
  {
    WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.ATTRIBUTE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForAttribute(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test attribute with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForAttributesWithInvalidTestData() throws IOException
  {
    WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.ATTRIBUTE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForAttribute(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : ATT1215"));
  }
  
  /**
   * Test tag with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTagTestData() throws IOException
  {
    WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.TAG);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTags(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test tag with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTagWithInvalidTestData() throws IOException
  {
    WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.TAG);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTags(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : Imp_Master1"));
  }
  
  /**
   * Test Hierarchy with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForHierarchyWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.HIERARCHY);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForHierarchy(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test Hierarchy with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForHierarchyWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.HIERARCHY);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForHierarchy(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : KK"));
  }
  
  /**
   * Test Master taxonomy with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForMasterTaxoWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.MASTERTAXONOMY);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForMasterTaxonomy(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test master taxonomy with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForMasterTaxoWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.MASTERTAXONOMY);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForMasterTaxonomy(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : KK"));
  }
  
  /**
   * Test Property Collection with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForPCWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.PROPERTY_COLLECTION);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForPropertyCollection(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test Property Collection with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForPCWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.PROPERTY_COLLECTION);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForPropertyCollection(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(entities.isEmpty());
  }
  
  /**
   * Test Context with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForContextWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.CONTEXT);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForContext(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test Context with Invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForContextWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.CONTEXT);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForContext(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(entities.isEmpty());
  }
  
  /**
   * Test User import with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForUserWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.USER);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForUsers(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test User import with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForUserWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.USER);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForUsers(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : TestUser2"));
  }
  
  /**
   * Test User import with invalid email address.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForUserWithInvalidEmailAddress() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.USER);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForUsers(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Invalid email id: testgmail.com for code: TestUser5"));
  }
  
  /**
   * Test Task import with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTaskWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TASK);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTasks(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test Task import with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTaskWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TASK);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTasks(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : TaskTest"));
  }
  
  /**
   * Test relationship with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForRelationshipWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.RELATIONSHIP);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForRelationship(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test relationship with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForRelationshipWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.RELATIONSHIP);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForRelationship(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : KK"));
  }
  
  /**
   * Test Klass with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForKlassWithValidTestData() throws IOException
  {
    //WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.KLASS);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForKlass(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(!entities.isEmpty());
    entities.forEach(p -> System.out.println(p));
  }
  
  /**
   * Test Klass with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForKlassWithInValidTestData() throws IOException
  {
    //WorkflowTaskModel workflowTaskModel = setWorkflowTaskModel(ConfigNodeType.KLASS);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForKlass(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable, workflowTaskModel);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : Test_6"));
  }
  
  /**
   * Test DataRule with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForDataRuleWithValidTestData() throws IOException
  {
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForDataRules(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
    entities.forEach(p -> System.out.println(p));
  }
  
  /**
   * Test DataRule with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForDataRuleWithInvalidTestData() throws IOException
  {
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForDataRules(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : SAN1"));
  }
  
  /**
   * Test Golden Record Rule with valid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForGoldenRecordWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.GOLDENRECORDRULE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForGoldenRecordRules(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test Golden Record Rule with invalid data.
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForGoldenRecordWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.GOLDENRECORDRULE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForGoldenRecordRules(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : GoldenRecord1"));
  }
  
  /**
   * Test generation of PXON for valid Tab data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTabWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TAB);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTabs(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test generation of PXON for invalid Tab data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTabWithInvalidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TAB);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTabs(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : test_tab4"));
  }
  
  /**
   * Test generation of PXON for valid Reference data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForReferenceWithValidTestData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.REFERENCE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForReferences(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    System.out.println(entities);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test generation of PXON for invalid Reference data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForReferenceWithInvalidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.REFERENCE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    //configToPXONTask.generatePXONForReferences(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : reference2"));
  }
  
  /**
   * Test generation of PXON for valid Partner data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForPartnerWithValidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.PARTNER);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForPartners(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    System.out.println(entities);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test generation of PXON for invalid Partner data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForPartnerWithInvalidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.PARTNER);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForPartners(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : testSupplier2"));
  }
  
  /**
   * Test generation of PXON for valid Translation data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTranslationWithValidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TRANSLATION);
    String fileStream = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(new File(excelForTranslations).toURI())));;
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(fileStream));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTranslations(workbook, CONFIG_TRANSLATION_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test generation of PXON for invalid Translation data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForTranslationWithInvalidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.TRANSLATION);
    String fileStream = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(new File(excelForTranslations).toURI())));;
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(fileStream));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForTranslations(workbook, CONFIG_TRANSLATION_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : testCode"));
  }
  
  /**
   * Test generation of PXON for valid Language data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForLanguageWithValidDataa() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.LANGUAGE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForLanguages(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Test generation of PXON for invalid Language data
   * 
   * @throws IOException
   */
  @Test
  public void generatePXONForLanguageWithInvalidData() throws IOException
  {
    setWorkflowTaskModel(ConfigNodeType.LANGUAGE);
    InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(getByteStream()));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    List<String> entities = new ArrayList<>();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    configToPXONTask.generatePXONForLanguages(workbook, CONFIG_IMPORT_XLSX, entities, executionStatusTable);
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : test_code2"));
  }
  
}