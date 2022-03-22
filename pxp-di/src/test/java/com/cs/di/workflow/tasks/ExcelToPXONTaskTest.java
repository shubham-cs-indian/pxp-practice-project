package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.endpoint.GetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.GetMappingForImportRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetMappingForImportStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.ExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public class ExcelToPXONTaskTest extends DiIntegrationTestConfig {
  
  public static final String   DEFAULT_LANGUAGE_CODE    = "en_US";
  public static final String   RUNTIME_IMPORT_XLSX_PATH = "src/test/resources/inbound/RuntimeImport.xlsx";
  public static final String   RUNTIME_IMPORT_XLSX      = "RuntimeImport.xlsx";
  
  @Autowired
  ExcelToPXONTask              excelToPXONTask;
  
  @Mock
  IGetMappingForImportStrategy getMappingForImportStrategy;
  
  /**
   * Test case to test transformation of valid article data
   * 
   * @throws Exception
   */
  @Test
  public void generatePXONForValidDataForArticles() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id",
        variantsMap, workflowTaskModel.getExecutionStatusTable(), null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForArticles(workbook, entities, RUNTIME_IMPORT_XLSX, workflowTaskModel.getExecutionStatusTable(),
        workflowTaskModel, localCatalogDAOMap, new HashMap<>(), variantsMap, articlesToBeCreated, DEFAULT_LANGUAGE_CODE, null);
    RDBMSLogger.instance().info("generateFromPXONValidTestData :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * This method is setting all required values for WorkflowTaskModel
   * 
   * @return
   */
  public WorkflowTaskModel getWorkflowTaskModel()
  {
    ITransactionData transactionData = new TransactionData();
    transactionData.setDataLanguage(DEFAULT_LANGUAGE_CODE);
    transactionData.setUiLanguage(DEFAULT_LANGUAGE_CODE);
    transactionData.setOrganizationId("-1");
    transactionData.setPhysicalCatalogId("pim");
    transactionData.setEndpointId("");
    transactionData.setUserId("adminUser");
    transactionData.setUserName("admin");
    IUserSessionDTO userSessionDTO = new UserSessionDTO();
    userSessionDTO.setSessionID("testId12345");
    WorkflowModel workflowModel = new WorkflowModel("excelToPXONTask", transactionData, userSessionDTO);
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("excelToPXONTask", new String[] {}, new Object[] {});
    workflowTaskModel.setWorkflowModel(workflowModel);
    return workflowTaskModel;
  }
  
  /**
   * Read excel file and get workbook
   * 
   * @param excelFilePath
   * @return
   * @throws IOException
   */
  private XSSFWorkbook getExcelWorkbook(String excelFilePath) throws IOException
  {
    return new XSSFWorkbook(new ByteArrayInputStream(Files.readAllBytes(Paths.get(new File(excelFilePath).toURI()))));
  }
  
  /**
   * tested for valid and invalid article ids
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONNullTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id",
        variantsMap, executionStatusTable, null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForArticles(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, new HashMap<>(), variantsMap, articlesToBeCreated, DEFAULT_LANGUAGE_CODE, null);
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Empty or Invalid or Multiple nature klass ids found"));
  }
  
  /**
   * Tested for incorrect secondary classes for articles
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONInvalidArticleData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id",
        variantsMap, executionStatusTable, null);
    List<String> articlesToBeCreated = new ArrayList<>();
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    excelToPXONTask.generatePXONForArticles(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, new HashMap<>(), variantsMap, articlesToBeCreated, DEFAULT_LANGUAGE_CODE, null);
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.WARNING, "Invalid Code : abd"));
  }
  
  /**
   * Tested for valid EXCEL parameters for assets
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONInValidTestDataForAssets() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new HashMap<>();
    /* excelToPXONTask.generatePXONForAssets(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, DEFAULT_LANGUAGE_CODE, null);*/
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Tested for null EXCEL parameters for assets
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONNullAssetsTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new HashMap<>();
    /* excelToPXONTask.generatePXONForAssets(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, DEFAULT_LANGUAGE_CODE, null);*/
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Empty or Invalid or Multiple nature klass ids found"));
  }
  
  /**
   * Tested for incorrect(optional parameter) EXCEL parameters for assets
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONInvalidAssetsTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new HashMap<>();
    /*excelToPXONTask.generatePXONForAssets(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, DEFAULT_LANGUAGE_CODE, null);*/
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : null"));
  }
  
  /**
   * Tested for incorrect required EXCEL parameters for assets
   * 
   * @throws Exception
   */
  @Test
  public void generateToPXONInvalidAssetsTestData1() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new HashMap<>();
    /*excelToPXONTask.generatePXONForAssets(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, DEFAULT_LANGUAGE_CODE, null);*/
    RDBMSLogger.instance().info("generateFromPXONInValidTestData :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.WARNING, "Invalid Code : assetNonNatureClass"));
  }
  
  /**
   * Tested for valid variants
   * 
   * @throws Exception
   */
  // @Test
  public void validTestDataFromVariants() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = new XSSFWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id", variantsMap, executionStatusTable,
        null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForVariants(variantsMap, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        articlesToBeCreated, localCatalogDAOMap, DEFAULT_LANGUAGE_CODE);
    RDBMSLogger.instance().info("generatePXONValidTestDataFromVariants :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * Tested for null EXCEL parameters(variants)
   * 
   * @throws Exception
   */
  @Test
  public void nullTestDataForVariants() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id", variantsMap, executionStatusTable,
        null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForVariants(variantsMap, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        articlesToBeCreated, localCatalogDAOMap, DEFAULT_LANGUAGE_CODE);
    RDBMSLogger.instance().info("generatePXONNullTestDataForVariants :: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(
        DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Empty or Invalid or Multiple nature klass ids found"));
  }
  
  /**
   * tested for incorrect(mandatory parameter) EXCEL parameters(variants)
   * 
   * @throws Exception
   */
  // @Test
  public void invalidMandatoryTestDataForVariants() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id", variantsMap, executionStatusTable,
        null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForVariants(variantsMap, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        articlesToBeCreated, localCatalogDAOMap, DEFAULT_LANGUAGE_CODE);
    RDBMSLogger.instance()
        .info("generatePXONinvalidMandatoryTestDataForVariants:: result :: " + executionStatusTable.getExecutionStatusTableMap());
    entities.forEach(p -> System.out.println(p));
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.WARNING,
        "Error occured during transformation of attribute variant"));
  }
  
  /**
   * Test with valid input parameters
   * 
   * @throws Exception
   */
  //@Test
  public void testValidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "data");
    List<String> validate = excelToPXONTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   * Test for runtime input parameters
   * 
   * @throws Exception
   */
  //@Test
  public void testRuntimeInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "${data}");
    List<String> validate = excelToPXONTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   * Test for invalid input parameter
   * 
   * @throws Exception
   */
  //@Test
  public void testInvalidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("RECEIVED_DATA", "");
    List<String> validate = excelToPXONTask.validate(inputFields);
    assertTrue(validate.contains("RECEIVED_DATA"));
  }
  
  /**
   * tested for valid RELATIONSHIPS
   * 
   * @throws Exception
   */
  @Test
  public void validTestDataFromRelationships() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "relationships", "side1_id", relationshipsMap,
        executionStatusTable, null);
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id",
        variantsMap, workflowTaskModel.getExecutionStatusTable(), null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForArticles(workbook, entities, RUNTIME_IMPORT_XLSX, workflowTaskModel.getExecutionStatusTable(),
        workflowTaskModel, localCatalogDAOMap, relationshipsMap, variantsMap, articlesToBeCreated, DEFAULT_LANGUAGE_CODE, null);
    /*excelToPXONTask.generatePXONForAssets(workbook, entities, RUNTIME_IMPORT_XLSX, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, DEFAULT_LANGUAGE_CODE, null);*/
    excelToPXONTask.generatePXONForRelationships(entities, relationshipsMap, executionStatusTable, workflowTaskModel, localCatalogDAOMap,
        DEFAULT_LANGUAGE_CODE, DiDataType.EXCEL);
    
    RDBMSLogger.instance().info("generatePXONValidTestDataFromRelationships :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(!entities.isEmpty());
  }
  
  /**
   * tested for null EXCEL parameters(RELATIONSHIPS)
   * 
   * @throws Exception
   */
  @Test
  public void invalidTestDataForRelationships() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "relationships", "side1_id", relationshipsMap,
        executionStatusTable, null);
    excelToPXONTask.generatePXONForRelationships(entities, relationshipsMap, executionStatusTable, workflowTaskModel, localCatalogDAOMap,
        DEFAULT_LANGUAGE_CODE, DiDataType.EXCEL);
    
    RDBMSLogger.instance().info("generatePXONValidTestDataFromRelationships :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "Error occured during transformation : side1Id - TEST_ART008"));
  }
  
  /**
   * tested for incorrect(parameter) EXCEL parameters (RELATIONSHIPS)
   * 
   * @throws Exception
   */
  //@Test
  public void testInvalidDataForRelationships() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> relationshipsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "relationships", "side1_id", relationshipsMap,
        executionStatusTable, null);
    excelToPXONTask.generatePXONForRelationships(entities, relationshipsMap, executionStatusTable, workflowTaskModel, localCatalogDAOMap,
        DEFAULT_LANGUAGE_CODE, DiDataType.EXCEL);
    
    RDBMSLogger.instance().info("generatePXONValidTestDataFromRelationships :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(entities.isEmpty());
    DiTestUtil.verifyExecutionStatus2(executionStatusTable, MessageType.ERROR, "null is not Present");
    
  }
  
  /**
   * tested for mapping
   * 
   * @throws Exception
   */
  @Test
  public void generateFromPXONWithMappingTestData() throws Exception
  {
    WorkflowTaskModel workflowTaskModel = getWorkflowTaskModel();
    List<String> entities = new ArrayList<>();
    XSSFWorkbook workbook = getExcelWorkbook(RUNTIME_IMPORT_XLSX_PATH);
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new HashMap<String, ILocaleCatalogDAO>();
    Map<String, List<Map<String, Object>>> entitiesMap = new HashMap<>();
    IGetMappingForImportResponseModel getMappingForImportResponseModel = new GetMappingForImportResponseModel();
    IGetMappingForImportRequestModel getMappingForImportRequestModel = new GetMappingForImportRequestModel();
    when(getMappingForImportStrategy.execute(getMappingForImportRequestModel)).thenReturn(getMappingForImportResponseModel);
    Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
    excelToPXONTask.prepareMapOfRecordsHavingSameId(workbook, RUNTIME_IMPORT_XLSX, "variants", "id",
        variantsMap, workflowTaskModel.getExecutionStatusTable(), null);
    List<String> articlesToBeCreated = new ArrayList<>();
    excelToPXONTask.generatePXONForArticles(workbook, entities, RUNTIME_IMPORT_XLSX, workflowTaskModel.getExecutionStatusTable(),
        workflowTaskModel, localCatalogDAOMap, entitiesMap, variantsMap, articlesToBeCreated, DEFAULT_LANGUAGE_CODE, "Pro100099");
    RDBMSLogger.instance().info("generateFromPXONValidTestData :: result :: ");
    entities.forEach(p -> System.out.println(p));
    assertTrue(!entities.isEmpty());
  }
}