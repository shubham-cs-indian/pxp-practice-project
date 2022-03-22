package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cs.core.config.interactor.model.language.GetLanguagesResponseModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguagesStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.AssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.GetAssetExtensionsModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.postgres.RDBMSDriver;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "jdk.internal.reflect.*", "javax.management.*" })
@PrepareForTest({ DiUtils.class, DiValidationUtil.class, RDBMSUtils.class })
@SuppressStaticInitializationFor("com.cs.workflow.base.AbstractTask")
public class UpsertAssetToPXONTaskTest {
  
  private static final String        RDBMS_HOME       = System.getenv("PXP_RDBMS_HOME");
  protected static String            sessionID        = "session#" + new Random().nextInt();
  protected static IRDBMSAppDriver   driver           = null;
  protected static ILocaleCatalogDAO localeCatalogDAO = null;
  protected static IUserSessionDAO   userSessionDAO   = null;
  protected static IUserSessionDTO   userSessionDTO   = null;
  protected static ILocaleCatalogDTO localeCatalogDTO = null;
  IConfigurationDAO                  configurationDAO = null;
  
  public static final String         FAILED_FILES     = "FAILED_FILES";
  public static final String         SUCCESS_FILES    = "SUCCESS_FILES";
  @InjectMocks
  UpsertAssetToPXONTask              upsertAssetToPXONTask;
  @Mock
  DiTransformationUtils              diTransformationUtils;
  @Mock
  IGetLanguagesStrategy getLanguagesStrategy;
  @Mock
  IGetAllAssetExtensions             getAllAssetExtensions;
  
  /**
   * Register driver for RDBMS
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void initClass() throws Exception
  {
    IRDBMSDriver postgresRDBMSDriver = new RDBMSDriver();
    RDBMSDriverManager.registerDriver(postgresRDBMSDriver);
    IRDBMSAppDriver RDBMSAppDriver = new RDBMSAppDriver();
    RDBMSAppDriverManager.registerDriver(RDBMSAppDriver);
  }  
  /**
   * Create session and open local catalog for each usecase
   * 
   * @throws Exception
   */
  @Before
  public void init() throws Exception
  {
    // ConfigurationDAO
    configurationDAO = RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO();
    // User session
    userSessionDAO = RDBMSAppDriverManager.getDriver()
        .newUserSessionDAO();
    userSessionDTO = userSessionDAO.openSession("admin", sessionID);
    // LocaleDAO operation
    localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO("en_US", "pim", IStandardConfig.STANDARD_ORGANIZATION_CODE);
    localeCatalogDAO = userSessionDAO.openLocaleCatalog(userSessionDTO, localeCatalogDTO);
  }
  
  /**
   * Stub extension which get from configuration (i.e. orientdb)
   * 
   * @throws Exception
   */
  private void stubExtension() throws Exception
  {
    // extension mocking
    Map<String, List<String>> mockImageExtension = new HashMap<String, List<String>>();
    List<String> mockExtensions = new ArrayList<String>();
    imageExtension(mockExtensions);
    mockImageExtension.put(SystemLevelIds.IMAGE, mockExtensions);
    IGetAssetExtensionsModel getAssetExtensionsModel = new GetAssetExtensionsModel();
    getAssetExtensionsModel.setAssetExtensions(mockImageExtension);
    when(getAllAssetExtensions.execute(any())).thenReturn(getAssetExtensionsModel);
  }
  
  private void stubDiUtils()
  {
    // DiUtils operation
    PowerMockito.mockStatic(DiUtils.class);
    when(DiUtils.createLocaleCatalogDAO(any(), any())).thenReturn(localeCatalogDAO);
  }
  
  /**
   * Stub files
   */
  private void stubAssetFileNames()
  {
    // File map mocking
    Map<String, String> assetMap = new HashMap<String, String>();
    assetMap.put("image.png", "D:\\abc\\image.png\\");
    Map mockAssetFilesMap = Mockito.mock(Map.class);
    when(mockAssetFilesMap.get(DiDataType.ASSET.name())).thenReturn(assetMap);
    PowerMockito.mockStatic(DiValidationUtil.class);
    when(DiValidationUtil.validateAndGetRequiredMap(any(), Mockito.anyString()))
        .thenReturn(mockAssetFilesMap);
  }
  
  /**
   * Stub CamundaComponentUtil
   * 
   * @throws Exception
   */
  private void stubCamundaComponentUtil() throws Exception
  {
    IGetLanguagesResponseModel getLanguagesResponseModel = new GetLanguagesResponseModel();
    getLanguagesResponseModel.setDefaultLanguage("en_US");
    doReturn(getLanguagesResponseModel).when(getLanguagesStrategy)
        .execute(any());
  }
  
  /**
   * stub DiTransformationUtils
   */
  private void stubDiTransformationUtils()
  {
    // upload swift server operation (diTransformationUtils)
    IBulkUploadResponseAssetModel uploadedAssetDetails = new BulkUploadResponseAssetModel();
    IAssetImageKeysModel assetImageKeysModel = this.assetImageKeysModelJSONData();
    List<IAssetKeysModel> assetImageKeysModelList = new ArrayList<IAssetKeysModel>();
    assetImageKeysModelList.add(assetImageKeysModel);
    //uploadedAssetDetails.setSuccess(assetImageKeysModelList);
    
    /* when(diTransformationUtils.uploadAssetToServer(Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString(), Mockito.any())).thenReturn(uploadedAssetDetails);*/
  }
  
  /**
   * Stub RDBMSUtils
   *
   *
   * @throws Exception
   */
  private void stubRDBMSUtils() throws Exception
  {
    // RDBMSUtils
    PowerMockito.mockStatic(RDBMSUtils.class);
    when(RDBMSUtils.newConfigurationDAO()).thenReturn(configurationDAO);
    
    this.stubProperty();
  }
  
  /**
   * Stub property config data for DAM
   * 
   * @throws Exception
   */
  private void stubProperty() throws Exception
  {
    when(RDBMSUtils.getPropertyByCode("I_META_FILE_NAME"))
        .thenReturn(configurationDAO.getPropertyByCode("I_META_FILE_NAME"));
    when(RDBMSUtils.getPropertyByCode("I_DIMENSIONS"))
        .thenReturn(configurationDAO.getPropertyByCode("I_DIMENSIONS"));
    when(RDBMSUtils.getPropertyByCode("I_BIT_DEPTH"))
        .thenReturn(configurationDAO.getPropertyByCode("I_BIT_DEPTH"));
    when(RDBMSUtils.getPropertyByCode("I_RESOLUTION"))
        .thenReturn(configurationDAO.getPropertyByCode("I_RESOLUTION"));
    when(RDBMSUtils.getPropertyByCode("I_META_WIDTH_RESOLUTION"))
        .thenReturn(configurationDAO.getPropertyByCode("I_META_WIDTH_RESOLUTION"));
    when(RDBMSUtils.getPropertyByCode("I_CREATE_DATE"))
        .thenReturn(configurationDAO.getPropertyByCode("I_CREATE_DATE"));
    when(RDBMSUtils.getPropertyByCode("I_MODIFICATION_DATE"))
        .thenReturn(configurationDAO.getPropertyByCode("I_MODIFICATION_DATE"));
    when(RDBMSUtils.getPropertyByCode("I_META_FILE_SIZE"))
        .thenReturn(configurationDAO.getPropertyByCode("I_META_FILE_SIZE"));
    when(RDBMSUtils.getPropertyByCode("I_META_HIGH_RESOLUTION"))
        .thenReturn(configurationDAO.getPropertyByCode("I_META_HIGH_RESOLUTION"));
    when(RDBMSUtils.getPropertyByCode(StandardProperty.nameattribute.name()))
        .thenReturn(configurationDAO.getPropertyByCode(StandardProperty.nameattribute.name()));
  }
  
  /**
   * Stub data for swift server
   * 
   * @return
   */
  private IAssetImageKeysModel assetImageKeysModelJSONData()
  {
    Map<String, Object> metadata = new HashMap<String, Object>();
    metadata.put("File:FileName", "5-2-God-is-in-Nature-1024x681.jpg");
    metadata.put("File:EncodingProcess", "Baseline DCT); Huffman coding");
    metadata.put("File:FileModifyDate", "2020:03:02 18:02:55+05:30");
    metadata.put("JFIF:ResolutionUnit", "None");
    metadata.put("JFIF:XResolution", 1);
    metadata.put("File:MIMEType", "image/jpeg");
    metadata.put("File:FileTypeExtension", "jpg");
    metadata.put("File:BitsPerSample", 8);
    metadata.put("SourceFile",
        "D:/NFS/UploadedAssetEntities/RND1215/5-2-God-is-in-Nature-1024x681.jpg");
    metadata.put("File:Comment", "CREATOR: gd-jpeg v1.0 (using IJG JPEG v80)); quality = 90\n");
    metadata.put("File:FilePermissions", "rw-rw-rw-");
    metadata.put("File:ImageWidth", 1024);
    metadata.put("File:FileCreateDate", "2020:03:02 18:02:55+05:30");
    metadata.put("File:YCbCrSubSampling", "YCbCr4:2:0 (2 2)");
    metadata.put("File:Directory", "D:/NFS/UploadedAssetEntities/RND1215");
    metadata.put("File:FileAccessDate", "2020:03:02 18:02:55+05:30");
    metadata.put("Composite:Megapixels", 0.697);
    metadata.put("JFIF:JFIFVersion", 1.01);
    metadata.put("File:FileSize", "86 kB");
    metadata.put("JFIF:YResolution", 1);
    metadata.put("name", "5-2-God-is-in-Nature-1024x681.jpg");
    metadata.put("File:ColorComponents", 3);
    metadata.put("ExifTool:ExifToolVersion", 10.34);
    metadata.put("File:ImageHeight", 681);
    metadata.put("File:FileType", "JPEG");
    metadata.put("Composite:ImageSize", "1024x681");
    
    /* IAssetImageKeysModel AssetImageKeysModel = new AssetImageKeysModel("ASS1216", "ASS1217",
        metadata, "8db5aa76807a290b7c71002e6d4a086831ee615eb74435892f5ba2bf4299978c", 681, 1024,
        null, "5-2-God-is-in-Nature-1024x681", "image_asset");*/
    
    //return AssetImageKeysModel;
    return null;
  }
  
  /**
   * Stub extension for Image asset
   * 
   * @param mockExtensions
   */
  private void imageExtension(List<String> mockExtensions)
  {
    mockExtensions.add(".jpg");
    mockExtensions.add(".jpeg");
    mockExtensions.add(".png");
    mockExtensions.add(".ico");
    mockExtensions.add(".eps");
    mockExtensions.add(".ai");
    mockExtensions.add(".psd");
    mockExtensions.add(".tif");
    mockExtensions.add(".tiff");
    mockExtensions.add(".gif");
  }
  
  /**
   * Test will convert asset into PXON
   * 
   * @throws Exception
   */
  @Test
  public void generatePXONTestValidData() throws Exception
  {
    this.stubDiUtils();
    this.stubCamundaComponentUtil();
    this.stubRDBMSUtils();
    this.stubAssetFileNames();
    this.stubDiTransformationUtils();
    this.stubExtension();
    
    // Workflow model mocking
    WorkflowTaskModel model = new WorkflowTaskModel();
    WorkflowModel workflowModel = new WorkflowModel(null, null, null);
    model.setWorkflowModel(workflowModel);
    workflowModel.setUserSessionDto(userSessionDTO);
    
    // actual method call
    upsertAssetToPXONTask.executeTask(model);
    
    List<String> failedFile = (List<String>) model.getOutputParameters()
        .get(FAILED_FILES);
    assertTrue(failedFile.size() == 0);
    
    List<String> successFilePXON = (List<String>) model.getOutputParameters()
        .get(SUCCESS_FILES);
    assertTrue(successFilePXON.size() > 0);
    for (String pxon : successFilePXON) {
      System.out.println(pxon);
    }
    
  }
  
  /**
   * Test to check handling of empty files
   */
  @Test
  public void fileNotPresentInPathTest()
  {
    this.stubDiUtils();
    Map mockAssetFilesMap = Mockito.mock(Map.class);
    when(mockAssetFilesMap.get(DiDataType.ASSET.name())).thenReturn(null);
    PowerMockito.mockStatic(DiValidationUtil.class);
    when(DiValidationUtil.validateAndGetRequiredMap(any(), Mockito.anyString()))
        .thenReturn(mockAssetFilesMap);
    
    // Workflow model mocking
    WorkflowTaskModel model = new WorkflowTaskModel();
    WorkflowModel workflowModel = new WorkflowModel(null, null, null);
    model.setWorkflowModel(workflowModel);
    workflowModel.setUserSessionDto(userSessionDTO);
    
    // actual method call
    upsertAssetToPXONTask.executeTask(model);
    
    List<? extends IOutputExecutionStatusModel> list = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.WARNING);
    assertTrue("Input parameter ASSET is null.".equals(list.get(0)
        .toString()));
    
  }
  
  /**
   * Test exception
   * @throws Exception
   */
  @Test
  public void pxonGenerationException() throws Exception
  {
    this.stubDiUtils();
    this.stubCamundaComponentUtil();
    this.stubRDBMSUtils();
    this.stubAssetFileNames();
    this.stubDiTransformationUtils();
    
    // Workflow model mocking
    WorkflowTaskModel model = new WorkflowTaskModel();
    WorkflowModel workflowModel = new WorkflowModel(null, null, null);
    model.setWorkflowModel(workflowModel);
    workflowModel.setUserSessionDto(userSessionDTO);
    // actual method call
    upsertAssetToPXONTask.executeTask(model);
    
    List<? extends IOutputExecutionStatusModel> list = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    assertTrue("Error occured during processing of file".equals(list.get(0)
        .toString()));
    
  }
  
  /**
   * Test with valid input parameters
   * @throws Exception
   */
  @Test
  public void testValidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("ASSET_FILES_MAP", "data");
    List<String> validate = upsertAssetToPXONTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   * Test with runtime input parameters
   * @throws Exception
   */
  @Test
  public void testRuntimeInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("ASSET_FILES_MAP", "${data}");
    List<String> validate = upsertAssetToPXONTask.validate(inputFields);
    assertTrue(validate.isEmpty());
  }
  
  /**
   * Test with invalid input parameters
   * @throws Exception
   */
  @Test
  public void testInvalidInputParams() throws Exception
  {
    Map<String, Object> inputFields = new HashMap<>();
    inputFields.put("ASSET_FILES_MAP", "");
    List<String> validate = upsertAssetToPXONTask.validate(inputFields);
    assertTrue(validate.contains("ASSET_FILES_MAP"));
  }
}
