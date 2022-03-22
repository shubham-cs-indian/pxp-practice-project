package com.cs.di.workflow.tasks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.assertj.core.util.Arrays;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * 
 * @author mayuri.wankhade Class help to test ReceiverTask component
 *
 */
public class ReceiverTaskTest extends DiMockitoTestConfig {
  
  public static final String FOLDER_PATH_CONST = "src/test/resources/temp/";
  public static final String INVALID_FOLDER_PATH_CONST = "src/test/resources///////temp/";
  public static final String ARCHIVAL_PATH_CONST = "src/test/resources/archival/";
  public static final String ERROR_PATH_CONST = "src/test/resources/errorpath/";
  
  public static final String InputJson = "{\r\n" + 
      "  \"jmsInput\": [\r\n" + 
      "    {\r\n" + 
      "      \"messageType\": \"json\",\r\n" + 
      "      \"message\": [\r\n" + 
      "        {\r\n" + 
      "    },\r\n" + 
      "        {\r\n" + 
      "         \r\n" + 
      "        }\r\n" + 
      "      ]\r\n" + 
      "    },\r\n" + 
      "    {\r\n" + 
      "      \"messageType\": \"xlsx\",\r\n" + 
      "      \"message\": [\r\n" + 
      "        \"fileStream 1\",\r\n" + 
      "        \"fileStream 2\"\r\n" + 
      "      ]\r\n" + 
      "    }\r\n" + 
      "  ]\r\n" + 
      "}";
  
  @InjectMocks
  @Autowired
  ReceiverTask               receiverTask;
  
  @Rule
  public ExpectedException   thrown            = ExpectedException.none();
  
  /**
   * During testing JUNIT requires file and folder Structure to be created hence
   * cleanup is done after test execution
   */
  @AfterClass
  public static void cleanUpActivity()
  {
    System.out.println("cleanUpActivity after test excecuted");
    deleteDirectory(Paths.get(FOLDER_PATH_CONST));
  }
  
  /**
   * valid test Data all required values for WorkflowTaskModel isErrorOccurred
   * flag decides the result
   */
  @Test
  public void excelHotFolderTest() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            dummyFile().getParent(), "true", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    //assertTrue(verifyReceivedData(model, "EXCEL", "test_folder_path.xlsx"));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }
  
  
  /**
   * valid test Data all required values for WorkflowTaskModel isErrorOccurred
   * flag decides the result
   */
  @Test
  public void jsonHotFolderTest() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            dummyFileJson().getParent(), "true", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
   // assertTrue(verifyReceivedData(model,"JSON","test_folder_path.json"));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }
  
  /**
   * method to validate the RECEIVED_DATA
   * 
   * @param model
   * @return
   */
  @SuppressWarnings("unchecked")
  private boolean verifyReceivedData(WorkflowTaskModel model, String fileType,
      String FileNamePassed)
  {
    Map<String, Object> receivedDataMap = (HashMap<String, Object>) model.getOutputParameters()
        .get(ReceiverTask.RECEIVED_DATA);
    receivedDataMap.entrySet()
        .forEach(map -> System.out
            .println("RECEIVED_DATA Key " + map.getKey() + " and value " + map.getValue()));
    Map<String, Object> fileDetailsMap = (HashMap<String, Object>) receivedDataMap.get(fileType);
    fileDetailsMap.entrySet()
        .forEach(map -> System.out
            .println("fileDetailsMap Key " + map.getKey() + " and value " + map.getValue()));
    return fileDetailsMap.keySet()
        .contains(FileNamePassed);
  }
  
  /**
   * create file with invalid extension for Receiver Task This method is setting
   * all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void invalidaFileExtententionWarningTest() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            InvalidDummyFile().getParent(), "true", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Unsupported message type : txt"));
  }
  
  /**
   * Error path not given in Input all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void nullErrorPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            InvalidDummyFile().getParent(), "true", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            null });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter ERROR_PATH is null."));
  }
  
  /**
   * FOLDER PATH not given in Input all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void nullFolderPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }), null,
            "false", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter FOLDER_PATH is null."));
  }
  
  @Test
  public void invalidFolderPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            "invalidtest", "false", createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    System.out.println(model.getExecutionStatusTable()
        .isErrorOccurred());
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter FOLDER_PATH is Invalid."));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
  /**
   * ARCHIVAL_PATH not given in Input all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void nullArchivalPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            dummyFile().getParent(), "false", null, createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Input parameter ARCHIVAL_PATH is null."));
  }
  
  /**
   * Error path not given in Input all required values for WorkflowTaskModel
   * 
   * @return
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void fileNotFoundTest() throws Exception
  {
    File file = new File(FOLDER_PATH_CONST + "FOLDER_PATH/test_folder_path.txt");
    String path = file.getParent();
    System.out.println(path);
    file.deleteOnExit();
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }), path,
            true, createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING,
        "No files found"));
  }
  
  /**
   * create file with invalid extension for Receiver Task This method is setting
   * all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void nullAllowSubFolderAccessWarningTest() throws IOException
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST,
            ReceiverTask.HOT_FOLDER_PATH, ReceiverTask.ALLOW_SUB_FOLDER_ACCESS,
            ReceiverTask.ARCHIVAL_PATH, ReceiverTask.ERROR_PATH },
        new Object[] { DiConstants.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            dummyFile().getParent(), null, createDirectory(ReceiverTask.ARCHIVAL_PATH),
            createDirectory(ReceiverTask.ERROR_PATH) });
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, 
        "Input parameter ALLOW_SUB_FOLDER_ACCESS is null."));
  }
  
  //JMS Test cases Started
  /**
   * valid test Data all required values for WorkflowTaskModel isErrorOccurred
   * flag decides the result
   * @throws Exception 
   */
  @Test
  public void excelJMSTest() throws Exception
  {
    Map<String, String> jmsBody = new HashMap<>();
    jmsBody.put(DiConstants.BODY, InputJson);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, ReceiverTask.JMS_DATA},
        new Object[] { DiConstants.JMS, Arrays.asList(new String[] { "EXCEL" }), jmsBody});
    receiverTask.executeTask(model);
    assertTrue(verifyReceivedDataJMS(model, "EXCEL", 2));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }
  
  /**
   * valid test Data all required values for WorkflowTaskModel isErrorOccurred
   * flag decides the result
   * @throws Exception 
   */
  @Test
  public void jsonJMSTest() throws Exception
  {
    Map<String, String> jmsBody = new HashMap<>();
    jmsBody.put(DiConstants.BODY, InputJson);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, ReceiverTask.JMS_DATA},
        new Object[] { DiConstants.JMS, Arrays.asList(new String[] { "JSON" }), jmsBody});
    receiverTask.executeTask(model);
    assertTrue(verifyReceivedDataJMS(model, "JSON", 2));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred() != true);
  }  
  /**
   * create file with invalid extension for Receiver Task This method is setting
   * all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   * @throws Exception
   */
  @Test
  public void invalidaFileExtententionWarningTestJMS() throws IOException
  {
    Map<String, String> jmsBody = new HashMap<>();
    jmsBody.put(DiConstants.BODY, InputJson);
    WorkflowTaskModel model = DiTestUtil.createTaskModel("receiverTask",
        new String[] { ReceiverTask.INPUT_METHOD, ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, ReceiverTask.JMS_DATA},
        new Object[] { DiConstants.JMS, Arrays.asList(new String[] { "JSON" }), jmsBody});
    receiverTask.executeTask(model);
    // expectation varification
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.WARNING, "Unsupported message type : xlsx"));
  }
  
//JMS Test cases Ended
  
  /**
   * This method is setting all required values for WorkflowTaskModel
   * 
   * @return
   * @throws IOException
   */
  public WorkflowTaskModel setWorkflowTaskModelJMS() throws IOException
  {
    WorkflowTaskModel model = new WorkflowTaskModel();
    model.getInputParameters()
        .put(ReceiverTask.INPUT_METHOD, DiConstants.JMS);
    // model.getInputParameters().put(ProcessConstants.DATA, messageTypeList());
    System.out.println(model.getInputParameters()
        .get(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST));
    return model;
  }
  
  /**
   * create dummy file for testing
   * 
   * @return
   * @throws IOException
   */
  public synchronized File dummyFileJson() throws IOException
  {
    File file = new File(FOLDER_PATH_CONST + "FOLDER_PATH/test_folder_path.json");
    if (!file.exists()) {
      file.getParentFile()
          .mkdirs();
      file.createNewFile();
    }
    System.out.println("File created " + file.getParent());
    return file;
  }
  
  /**
   * create dummy file for testing
   * 
   * @return
   * @throws IOException
   */
  public synchronized File dummyFile() throws IOException
  {
    File file = new File(FOLDER_PATH_CONST + "FOLDER_PATH/test_folder_path.xlsx");
    if (!file.exists()) {
      file.getParentFile()
          .mkdirs();
      file.createNewFile();
    }
    System.out.println("File created " + file.getParent());
    return file;
  }
  
  /**
   * create dummy file for testing
   * 
   * @return
   * @throws IOException
   */
  public synchronized File InvalidDummyFile() throws IOException
  {
    File file = new File(FOLDER_PATH_CONST + "FOLDER_PATH/test_folder_path.txt");
    if (!file.exists()) {
      file.getParentFile()
          .mkdirs();
      file.createNewFile();
    }
    System.out.println("File created " + file.getParent());
    return file;
  }
  
  /**
   * Method to create foledr for ARCHIVAL and ERROR Path
   * 
   * @param path
   * @return
   * @throws IOException
   */
  public String createDirectory(String path) throws IOException
  {
    File dir = new File(FOLDER_PATH_CONST + path);
    if (!dir.exists()) {
      dir.mkdir();
    }
    return dir.getPath();
  }

  /**
   * Delete the folder/file created while testing after JUNIT completed
   * @param rootPath
   */
  public static void deleteDirectory(Path rootPath)
  {
    System.out.println("rootPath :: " + rootPath);
    try {
      if (Files.exists(rootPath))
        Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .peek(System.out::println)
            .forEach(File::delete);
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
  }  
  
  /**
   * method to validate the RECEIVED_DATA
   * 
   * @param model
   * @return
   */
  @SuppressWarnings("unchecked")
  private boolean verifyReceivedDataJMS(WorkflowTaskModel model, String fileType, int fileCount)
  {
    Map<String, Object> receivedDataMap = (HashMap<String, Object>) model.getOutputParameters()
        .get(ReceiverTask.RECEIVED_DATA);
    receivedDataMap.entrySet()
        .forEach(map -> System.out
            .println("RECEIVED_DATA Key " + map.getKey() + " and value " + map.getValue()));
    Map<String, Object> fileDetailsMap = (HashMap<String, Object>) receivedDataMap.get(fileType);
    fileDetailsMap.entrySet()
        .forEach(map -> System.out
            .println("fileDetailsMap Key " + map.getKey() + " and value " + map.getValue()));
    return fileDetailsMap.keySet().size() == fileCount;
  }
  
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
   * Test to check Image extensions
   * 
   * @throws Exception
   */
  @Test
  public void imageExtensionTest() throws Exception
  {
    // Dummy extension for Image
    IGetAssetExtensionsModel getAssetExtensionsModel = Mockito.mock(IGetAssetExtensionsModel.class);
    
    Map<String, List<String>> mockImageExtension = new HashMap<String, List<String>>();
    List<String> mockExtensions = new ArrayList<String>();
    imageExtension(mockExtensions);
    mockImageExtension.put(SystemLevelIds.IMAGE, mockExtensions);
    Mockito.when(getAssetExtensionsModel.getAssetExtensions())
        .thenReturn(mockImageExtension);
    
    // Dummy Strategy configuration call for Image
    IGetAllAssetExtensions mockGetExtensionStrategy = Mockito.mock(IGetAllAssetExtensions.class);
    Mockito.when(mockGetExtensionStrategy.execute(Mockito.any()))
        .thenReturn(getAssetExtensionsModel);
    List<String> supportedExtentions = new ReceiverTask()
        .getSupportedExtentions(DiDataType.ASSET,mockGetExtensionStrategy);
    
    assertTrue(mockExtensions.equals(supportedExtentions));
  }

  /**
   * Test to check Document extensions
   * 
   * @throws Exception
   */
  @Test
  public void documentExtensionTest() throws Exception
  {
    // Dummy extension for document
    IGetAssetExtensionsModel getAssetExtensionsModel = Mockito.mock(IGetAssetExtensionsModel.class);
    
    Map<String, List<String>> mockImageExtension = new HashMap<String, List<String>>();
    List<String> mockExtensions = documentExtension();
    mockImageExtension.put(SystemLevelIds.DOCUMENT, mockExtensions);
    Mockito.when(getAssetExtensionsModel.getAssetExtensions())
        .thenReturn(mockImageExtension);
    
    // Dummy Strategy configuration call for document
    IGetAllAssetExtensions mockGetExtensionStrategy = Mockito.mock(IGetAllAssetExtensions.class);
    Mockito.when(mockGetExtensionStrategy.execute(Mockito.any()))
        .thenReturn(getAssetExtensionsModel);
    List<String> supportedExtentions = new ReceiverTask()
        .getSupportedExtentions(DiDataType.ASSET, mockGetExtensionStrategy);
    
    assertTrue(mockExtensions.equals(supportedExtentions));
  }

  private List<String> documentExtension()
  {
    List<String> mockExtensions = new ArrayList<String>();
    mockExtensions.add(".pdf");
    mockExtensions.add(".ppt");
    mockExtensions.add(".pptx");
    mockExtensions.add(".doc");
    mockExtensions.add(".docx");
    mockExtensions.add(".xls");
    mockExtensions.add(".xlsx");
    mockExtensions.add(".obj");
    mockExtensions.add(".stp");
    mockExtensions.add(".fbx");
    mockExtensions.add(".zip");
    mockExtensions.add(".xtex");
    mockExtensions.add(".indd");
    mockExtensions.add(".idms");
    mockExtensions.add(".idml");
    mockExtensions.add(".indt");
    mockExtensions.add(".idlk");
    mockExtensions.add(".xyz");
    return mockExtensions;
  }
  
  /**
   * Test to check multimedia extensions
   * 
   * @throws Exception
   */
  @Test
  public void multimediaExtensionTest() throws Exception
  {
    // Dummy extension for multimedia
    IGetAssetExtensionsModel getAssetExtensionsModel = Mockito.mock(IGetAssetExtensionsModel.class);
    
    Map<String, List<String>> mockImageExtension = new HashMap<String, List<String>>();
    List<String> mockExtensions = documentExtension();
    mockImageExtension.put(SystemLevelIds.VIDEO, mockExtensions);
    Mockito.when(getAssetExtensionsModel.getAssetExtensions())
        .thenReturn(mockImageExtension);
    
    // Dummy Strategy configuration call for multimedia
    IGetAllAssetExtensions mockGetExtensionStrategy = Mockito.mock(IGetAllAssetExtensions.class);
    Mockito.when(mockGetExtensionStrategy.execute(Mockito.any()))
        .thenReturn(getAssetExtensionsModel);
    List<String> supportedExtentions = new ReceiverTask()
        .getSupportedExtentions(DiDataType.ASSET, mockGetExtensionStrategy);
    assertTrue(mockExtensions.equals(supportedExtentions));
  }
  
  /**
   * 
   */
  @Test
  public void checkDynamicExtensionOnJSON()
  {
    try {
      // Dummy extensionModel
      IGetAssetExtensionsModel getAssetExtensionsModel = Mockito
          .mock(IGetAssetExtensionsModel.class);
      
      // Dummy Strategy configuration call
      IGetAllAssetExtensions mockGetExtensionStrategy = Mockito.mock(IGetAllAssetExtensions.class);
      Mockito.when(mockGetExtensionStrategy.execute(Mockito.any()))
          .thenReturn(getAssetExtensionsModel);
      assertTrue(DiDataType.JSON.getSupportedExtentions().equals(new ReceiverTask().getSupportedExtentions(DiDataType.JSON, mockGetExtensionStrategy)));
    }
    catch (Exception e) {
      assertThat(e.getMessage(),
          equalTo("The extension are not dynamic. Retrieve supported extensions statically"));
    }
  }
  
  @Test
  public void checkDynamicExtensionOnEXCEL()
  {
    try {
      // Dummy extensionModel
      IGetAssetExtensionsModel getAssetExtensionsModel = Mockito
          .mock(IGetAssetExtensionsModel.class);
      
      // Dummy Strategy configuration call
      IGetAllAssetExtensions mockGetExtensionStrategy = Mockito.mock(IGetAllAssetExtensions.class);
      Mockito.when(mockGetExtensionStrategy.execute(Mockito.any()))
          .thenReturn(getAssetExtensionsModel);
      assertTrue(DiDataType.EXCEL.getSupportedExtentions().equals(new ReceiverTask().getSupportedExtentions(DiDataType.EXCEL, mockGetExtensionStrategy)));
    }
    catch (Exception e) {
      assertThat(e.getMessage(),
          equalTo("The extension are not dynamic. Retrieve supported extensions statically"));
    }
  }
  
  /**
   * Test to check validate method with valid input values For JMS
   * 
   * @throws Exception
   */
   @Test
  public void testValidateMethodForValidInputParamtersForJMS() throws Exception
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ReceiverTask.INPUT_METHOD, DiConstants.JMS);
    inputs.put(ReceiverTask.CLASS_PATH_IP, "tcp://192.168.134.72");
    inputs.put(ReceiverTask.PORT, "61616");
    inputs.put(ReceiverTask.QUEUE_NAME, "abc_123");
    inputs.put(ReceiverTask.ACKNOWLEDGEMENT, "true");
    inputs.put(ReceiverTask.ACKNOWLEDGEMENT_QUEUE_NAME, "ab_123");
    inputs.put(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, "JSON");
    inputs.put(ReceiverTask.JMS_DATA, "data");
    List<String> listOfInvalidParams = receiverTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  /**
   * Test to check validate method with valid input values for HOTFolder
   * 
   * @throws Exception
   */
   @Test
  public void testValidateMethodForValidInputParamtersForHotFolder() throws Exception
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ReceiverTask.INPUT_METHOD, DiConstants.HOTFOLDER);
    inputs.put(ReceiverTask.HOT_FOLDER_PATH, ReceiverTaskTest.FOLDER_PATH_CONST);
    inputs.put(ReceiverTask.ARCHIVAL_PATH, ReceiverTaskTest.ARCHIVAL_PATH_CONST);
    inputs.put(ReceiverTask.ERROR_PATH, ReceiverTaskTest.ERROR_PATH_CONST);
    inputs.put(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, "JSON");
    List<String> listOfInvalidParams = receiverTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  /**
   * Test to check validate method with valid and invalid input values For JMS
   * 
   * @throws Exception
   */
   @Test
  public void testValidateMethodForValidAndInvalidInputParamtersForJms() throws Exception
  {
    
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ReceiverTask.INPUT_METHOD, DiConstants.JMS);
    inputs.put(ReceiverTask.CLASS_PATH_IP, "tcp://192.1");
    inputs.put(ReceiverTask.PORT, "616ivan");
    inputs.put(ReceiverTask.QUEUE_NAME, "abc_123");
    inputs.put(ReceiverTask.ACKNOWLEDGEMENT, "false");
    inputs.put(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, "ABC");
    inputs.put(ReceiverTask.JMS_DATA, "data");
    List<String> listOfInvalidParams = receiverTask.validate(inputs);
    List<String> newlist = new ArrayList<String>();
    newlist.add(ReceiverTask.CLASS_PATH_IP);
    newlist.add(ReceiverTask.PORT);
    newlist.add(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, newlist));
  }
  
  /**
   * Test to check validate method with valid and invalid input values For
   * HOTFOLDER
   * 
   * @throws Exception
   */
  @Test
  public void testValidateMethodForValidAndInvalidInputParamtersForHotFolder() throws Exception
  {
    
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(ReceiverTask.INPUT_METHOD, DiConstants.HOTFOLDER);
    inputs.put(ReceiverTask.HOT_FOLDER_PATH, ReceiverTaskTest.INVALID_FOLDER_PATH_CONST);
    inputs.put(ReceiverTask.ARCHIVAL_PATH, ReceiverTaskTest.ARCHIVAL_PATH_CONST);
    inputs.put(ReceiverTask.ERROR_PATH, ReceiverTaskTest.ERROR_PATH_CONST);
    inputs.put(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST, " ");
    List<String> listOfInvalidParams = receiverTask.validate(inputs);
    List<String> newlist = new ArrayList<String>();
    newlist.add(ReceiverTask.HOT_FOLDER_PATH);
    newlist.add(ReceiverTask.ARCHIVAL_PATH);
    newlist.add(ReceiverTask.RECEIVER_MESSAGE_TYPE_LIST);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, newlist));
  }
  
}
