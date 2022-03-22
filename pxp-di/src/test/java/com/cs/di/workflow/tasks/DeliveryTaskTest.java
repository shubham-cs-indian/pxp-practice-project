package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang.ObjectUtils;
import org.assertj.core.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.usecase.component.jms.JmsProducerComponent;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.DeliveryType;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * 
 * @author Priyaranjan kumar Class help to test DeliveryTask component.
 * 
 *
 */

public class DeliveryTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  @Autowired
  DeliveryTask                                  deliveryTask;
  
  @Mock
  JmsProducerComponent                          jmsProducerComponent;
  
  private static final String                   TEST_HOT_FOLDER   = "src\\test\\resources\\TestHotfolder";
  
  final static Map<String, Map<String, Object>> FILE_OBJECT_MAP   = new HashMap<String, Map<String, Object>>();
  
  public static final String                    FOLDER_PATH_CONST = "src\\test\\resources\\DeliveryTaskTestInput";
  public static final String                    INVALID_FOLDER_PATH_CONST = "src/test/DeliveryTaskTestInput";
  public static final String                    ARCHIVAL_PATH_CONST       = "src/test/resources/archival/";
  public static final String                    ERROR_PATH_CONST          = "src/test/resources/errorpath/";
  
  private static final String                   BROKERURL         = "tcp://192.168.134.72";
  private static final String                   DESTINATION       = "PXPFDEV_13567_Q";
  private static final String                   PORT              = "61616";
  private static final String                   TASK_ID           = "deliveryTask";
  
  
  /**
   * Test for hotfolder's basic funtionality i.e. to write iput files in the
   * hotfolder
   */
  @Test
  public void writeToHotFolderTest() throws Exception
  {
    File hotFolder = new File(TEST_HOT_FOLDER);
    if (!hotFolder.exists() && !hotFolder.isDirectory()) {
      hotFolder.mkdir();
    }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER,
            Arrays.asList(new String[] { DiDataType.JSON.toString(), DiDataType.EXCEL.toString() }),
            "test", TEST_HOT_FOLDER });
    
    deliveryTask.executeTask(model);
    assertFalse(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
  /**
   * Test for when user did'nt configured FOLDER_PATH (hotfolder path)
   */
  @Test
  public void nullHotFolderPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER,
            Arrays.asList(new String[] { DiDataType.JSON.toString(), DiDataType.EXCEL.toString() }),
            "test", null });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter FOLDER_PATH is null."));
  }
  
  /**
   * Test for empty value in FOLDER_PATH
   */
 // @Test
  public void emptyHotFolderPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            FILE_OBJECT_MAP, "" });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter FOLDER_PATH is empty."));
  }
  
  /**
   * Test for invalid value of FOLDER_PATH
   */
 // @Test
  public void invalidHotFolderPathTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            FILE_OBJECT_MAP, "D:\\No such HotFolder exists Or something which is not a path" });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter FOLDER_PATH is Invalid."));
  }
  
  /**
   * Test for rare scenario : Providing file's path as a FOLDER_PATH
   */
 // @Test
  public void folderPathIsNotADirectoryTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            FILE_OBJECT_MAP, "src\\test\\resources\\ItsAFileNotFolder.txt" });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter FOLDER_PATH is not a directory"));
  }
  
  //@Test
  public void dataToExportIsNullTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            null, "src\\test\\resources\\ItsAFileNotFolder.txt" });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter DATA_TO_EXPORT is null."));
  }
  
  //@Test
  public void dataToExportIsEmptyTest() throws Exception
  {
    Map<String, Map<String, Object>> filesObjectMap    = new HashMap<String, Map<String, Object>>();
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH },
        new Object[] { DeliveryType.HOTFOLDER, Arrays.asList(new String[] { "JSON", "EXCEL" }),
            filesObjectMap, "src\\test\\resources\\ItsAFileNotFolder.txt" });
    
    deliveryTask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR,
        "Input parameter DATA_TO_EXPORT is empty."));
  }
  
  /**
   * Preparing the input data for hotfolder component
   */
  @BeforeClass
  public static void loadInputData()
  {
    
    Map<String, Object> jsonFileobject = new HashMap<String, Object>();
    Map<String, Object> excelFileobject = new HashMap<String, Object>();
    
    try (Stream<Path> paths = Files.walk(Paths.get(FOLDER_PATH_CONST))) {
      paths.forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          try {
            File file = new File(filePath.toString());
            
            if (file.getName()
                .contains(".json")) {
              readJsonFile(file, jsonFileobject);
            }
            else if (file.getName()
                .contains(".xls")) {
              readExcelFile(file, excelFileobject);
            }
            
          }
          catch (Exception e) {
            // TODO Auto-generated catch block
            RDBMSLogger.instance().exception(e);
          }
        }
      });
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      RDBMSLogger.instance().exception(e);
    }

    FILE_OBJECT_MAP.put("EXCEL", excelFileobject);
    FILE_OBJECT_MAP.put("JSON", jsonFileobject);
    
  }
  
  /**
   * For reading excel file and converting it in Base64 encoded String and to
   * return as a map
   * 
   * @param file
   * @param excelFileobject
   */
  private static void readExcelFile(File file, Map<String, Object> excelFileobject)
  {
    try {
      
      excelFileobject.put(file.getName(), Base64.getEncoder()
          .encodeToString(Files.readAllBytes(Paths.get(file.toURI()))));
      System.out.println(file.getName());
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      RDBMSLogger.instance().exception(e);
    }
    
  }
  
  /**
   * For reading the Json files to return their map
   * 
   * @param file
   * @param jsonFileobject
   */
  private static void readJsonFile(File file, Map<String, Object> jsonFileobject)
  {
    try {
      jsonFileobject.put(file.getName(), Files.readString(Paths.get(file.toURI())));
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      RDBMSLogger.instance().exception(e);
    }
    System.out.println(file.getName());
    
  }
  
  /**
   * Cleaning the files created
   */
  @AfterClass
  public static void cleanUpActivity()
  {
    System.out.println("cleanUpActivity after test excecuted");
    deleteDirectory(Paths.get(TEST_HOT_FOLDER));
  }
  
  /**
   * Delete the folder/file created while testing after JUNIT completed
   * 
   * @param //directoryToBeDeleted
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
   * valid test Data all required values for WorkflowTaskModel to deliver the
   * message in JMS QUEUE . isErrorOccurred flag decides the result
   * @throws Exception 
   */
  //@Test
  public void prepareMessageBodyAndSendToQueueTest() throws Exception {

    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.CLASS_PATH_IP, DeliveryTask.PORT, DeliveryTask.QUEUE_NAME,
            DeliveryTask.DATA_TO_EXPORT },
        new Object[] { DeliveryType.JMS, Arrays.asList(new String[] { "JSON", "EXCEL" }), BROKERURL,
            PORT, DESTINATION, FILE_OBJECT_MAP });
    
    //Mockito.when(jmsProducerComponent.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyString())).thenReturn("checked");
    
    deliveryTask.executeTask(model);

    assertFalse(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
 // @Test
  public void nullQueueNameTest() throws Exception {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.CLASS_PATH_IP, DeliveryTask.PORT, DeliveryTask.QUEUE_NAME,
            DeliveryTask.DATA_TO_EXPORT },
        new Object[] { DeliveryType.JMS, Arrays.asList(new String[] { "JSON", "EXCEL" }), BROKERURL,
            PORT, null, FILE_OBJECT_MAP });
    
    //Mockito.when(jmsProducerComponent.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyString())).thenReturn("checked");
       
    deliveryTask.executeTask(model);
    
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter QUEUE_NAME is null."));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
  //@Test
  public void nullPortTest() throws Exception
  {
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.CLASS_PATH_IP, DeliveryTask.PORT, DeliveryTask.QUEUE_NAME,
            DeliveryTask.DATA_TO_EXPORT },
        new Object[] { DeliveryType.JMS, Arrays.asList(new String[] { "JSON", "EXCEL" }), BROKERURL,
            null, DESTINATION, FILE_OBJECT_MAP });
    
    //Mockito.when(jmsProducerComponent.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyString())).thenReturn("checked");
      
    deliveryTask.executeTask(model);
    
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter PORT is null."));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
  //@Test
  public void nullClassPathIpTest() throws Exception
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.CLASS_PATH_IP, DeliveryTask.PORT, DeliveryTask.QUEUE_NAME,
            DeliveryTask.DATA_TO_EXPORT },
        new Object[] { DeliveryType.JMS, Arrays.asList(new String[] { "JSON", "EXCEL" }), null,
            PORT, DESTINATION, FILE_OBJECT_MAP });
    
    //Mockito.when(jmsProducerComponent.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyString())).thenReturn("checked");
     
    deliveryTask.executeTask(model);
    
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter CLASS_PATH_IP is Invalid."));
    assertTrue(model.getExecutionStatusTable()
        .isErrorOccurred());
   
  }
  
  /**
   * Test to check validate method with valid input values For JMS
   * 
   * @throws Exception
   */
  // @Test
  public void testValidateMethodForValidInputParamtersForJMS() throws Exception
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(DeliveryTask.OUTPUT_METHOD, DiConstants.JMS);
    inputs.put(DeliveryTask.CLASS_PATH_IP, "tcp://192.168.134.72");
    inputs.put(DeliveryTask.PORT, "61616");
    inputs.put(DeliveryTask.QUEUE_NAME, "abc_123");
    inputs.put(DeliveryTask.ACKNOWLEDGEMENT, "true");
    inputs.put(DeliveryTask.ACKNOWLEDGEMENT_QUEUE_NAME, "ab_123");
    inputs.put(DeliveryTask.MESSAGE_TYPE_LIST, "JSON");
    inputs.put(DeliveryTask.DATA_TO_EXPORT, "data");
    List<String> listOfInvalidParams = deliveryTask.validate(inputs);
    assertTrue(listOfInvalidParams.isEmpty());
  }
  
  /**
   * Test to check validate method with valid input values for HOTFolder
   * 
   * @throws Exception
   */
  // @Test
  public void testValidateMethodForValidInputParamtersForHotFolder() throws Exception
  {
    Map<String, Object> inputs = new HashMap<>();
    inputs.put(DeliveryTask.OUTPUT_METHOD, DiConstants.HOTFOLDER);
    inputs.put(DeliveryTask.DESTINATION_PATH, DeliveryTask.DESTINATION_PATH);
    inputs.put(DeliveryTask.MESSAGE_TYPE_LIST, "JSON");
    inputs.put(DeliveryTask.DATA_TO_EXPORT, "data");
    List<String> listOfInvalidParams = deliveryTask.validate(inputs);
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
    inputs.put(DeliveryTask.OUTPUT_METHOD, DiConstants.JMS);
    inputs.put(DeliveryTask.CLASS_PATH_IP, "tcp://192.1");
    inputs.put(DeliveryTask.PORT, "616ivan");
    inputs.put(DeliveryTask.QUEUE_NAME, "abc_123");
    inputs.put(DeliveryTask.ACKNOWLEDGEMENT, "false");
    List<DiDataType> lst = new ArrayList<DiDataType>();
    lst.add(DiDataType.JSON);
    inputs.put(DeliveryTask.MESSAGE_TYPE_LIST,lst);
    inputs.put(DeliveryTask.DATA_TO_EXPORT, "data");
    List<String> listOfInvalidParams = deliveryTask.validate(inputs);
    assertTrue(!listOfInvalidParams.isEmpty());
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
    inputs.put(DeliveryTask.OUTPUT_METHOD, DiConstants.HOTFOLDER);
    inputs.put(DeliveryTask.DESTINATION_PATH, DeliveryTaskTest.INVALID_FOLDER_PATH_CONST);
    List<String> lst = new ArrayList<String>();
    lst.add("Excel");
    inputs.put(DeliveryTask.MESSAGE_TYPE_LIST,lst);
    inputs.put(DeliveryTask.DATA_TO_EXPORT, "data");
    List<String> listOfInvalidParams = deliveryTask.validate(inputs);
    List<String> newlist = new ArrayList<String>();
    newlist.add(DeliveryTask.DESTINATION_PATH);
    assertTrue(ObjectUtils.equals(listOfInvalidParams, newlist));
  }
  
  @Test
  public void filePrefixTest() throws Exception
  {
    File hotFolder = new File(TEST_HOT_FOLDER);
    if (!hotFolder.exists() && !hotFolder.isDirectory()) {
      hotFolder.mkdir();
    }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { DeliveryTask.OUTPUT_METHOD, DeliveryTask.MESSAGE_TYPE_LIST,
            DeliveryTask.DATA_TO_EXPORT, DeliveryTask.DESTINATION_PATH,DeliveryTask.FILE_PREFIX},
        new Object[] { DeliveryType.HOTFOLDER,
            Arrays.asList(new String[] { DiDataType.JSON.toString(), DiDataType.EXCEL.toString() }),
            FILE_OBJECT_MAP, TEST_HOT_FOLDER , "ArticleClass" });
    
    deliveryTask.executeTask(model);
    assertFalse(model.getExecutionStatusTable()
        .isErrorOccurred());
  }
  
}
