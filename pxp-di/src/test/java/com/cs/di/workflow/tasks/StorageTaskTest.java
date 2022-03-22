package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertEquals;
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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "jdk.internal.reflect.*", "javax.management.*" })
@PrepareForTest({ DiValidationUtil.class })
@SuppressStaticInitializationFor("com.cs.workflow.base.AbstractTask")
public class StorageTaskTest {
  
  private static final String      TEST_FOLDER_PATH         = "D:\\SOMFY\\Testfolder";
  private static final String      INVALID_TEST_FOLDER_PATH = "D:>SOMFY\\Testfolder";
  
  final static Map<String, String> FILE_OBJECT_MAP          = new HashMap<String, String>();
  final static Map<String, Object> REQUIRED_INPUT_PARAM_MAP = new HashMap<String, Object>();
  final static List<String>        INVALID_FILE_OBJECT_MAP  = new ArrayList<String>();
  
  public static final String       FOLDER_PATH_CONST        = "D:\\SOMFY\\StorageTaskTestInput";
  
  private static final String      TASK_ID                  = "storageTask";
  
  @Autowired
  @InjectMocks
  private StorageTask              storageTask;
  
  /**
   * This method test  file should be written in the storage path
   * 
   * @throws Exception
   */
   @Test
  @SuppressWarnings("unchecked")
  public void writeToFolderTest() throws Exception
  {
    File folder = new File(TEST_FOLDER_PATH);
    if (!folder.exists() && !folder.isDirectory()) {
      folder.mkdir();
    }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FILES_TO_BE_STORE, StorageTask.FOLDER_PATH },
        new Object[] { FILE_OBJECT_MAP, TEST_FOLDER_PATH });
    
    storageTask.executeTask(model);
    assertFalse(model.getExecutionStatusTable()
        .isErrorOccurred());
    assertEquals(FILE_OBJECT_MAP.size() - ((List<String>) model.getOutputParameters()
        .get(StorageTask.UNSUCCESS_FILES)).size(), ((List<String>) model.getOutputParameters()
            .get(StorageTask.SUCCESS_FILES)).size());
  }
  
  /**
   * This method confirm given file storage path is not valid .
   */
  @Test(expected = java.nio.file.InvalidPathException.class)
  public void invalidFolderPathTest()
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { INVALID_TEST_FOLDER_PATH, FILE_OBJECT_MAP });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    
    // Input path error
    assertTrue(
        ("Input parameter " + StorageTask.FOLDER_PATH + " is Invalid.").equals(errorList.get(0)
            .toString()));
    
  }
  
  /**
   * This method confirm given file storage path is empty .
   */
  
   @Test
  public void emptyFolderPathTest()
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { new String(), FILE_OBJECT_MAP });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + StorageTask.FOLDER_PATH + " is empty.").equals(errorList.get(0)
        .toString()));
    
  }
  
  /**
   * This method confirm given file storage path is NULL .
   */
   @Test
  public void nullFolderPathTest()
  {
    
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { null, FILE_OBJECT_MAP });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + StorageTask.FOLDER_PATH + " is null.").equals(errorList.get(0)
        .toString()));
    
  }
  
  /**
   * This method confirm given Input fileToBeStore is empty .
   */
  
   @Test
  public void emptyInputForFileToBeStoreTest()
  {
     File folder = new File(TEST_FOLDER_PATH);
     if (!folder.exists() && !folder.isDirectory()) {
       folder.mkdir();
     }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { TEST_FOLDER_PATH, new HashMap<String, String>() });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap().get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + StorageTask.FILES_TO_BE_STORE + " is empty.").equals(errorList.get(0).toString()));
    
  }
  
  /**
   * This method confirm given Input fileToBeStore is null .
   */
  
   @Test
  public void nullInputForFileToBeStoreTest()
  {
     File folder = new File(TEST_FOLDER_PATH);
     if (!folder.exists() && !folder.isDirectory()) {
       folder.mkdir();
     }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { TEST_FOLDER_PATH, null });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap().get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + StorageTask.FILES_TO_BE_STORE + " is null.").equals(errorList.get(0).toString()));
    
  }
  
  /**
   * This method confirm given Input fileToBeStore is not valid .
   */
   @Test
  public void invalidInputForFileToBeStoreTest()
  {
     File folder = new File(TEST_FOLDER_PATH);
     if (!folder.exists() && !folder.isDirectory()) {
       folder.mkdir();
     }
    WorkflowTaskModel model = DiTestUtil.createTaskModel(TASK_ID,
        new String[] { StorageTask.FOLDER_PATH, StorageTask.FILES_TO_BE_STORE },
        new Object[] { TEST_FOLDER_PATH, INVALID_FILE_OBJECT_MAP });
    
    storageTask.executeTask(model);
    // check errors
    List<? extends IOutputExecutionStatusModel> errorList = model.getExecutionStatusTable()
        .getExecutionStatusTableMap()
        .get(MessageType.ERROR);
    
    // Input path error
    assertTrue(("Input parameter " + StorageTask.FILES_TO_BE_STORE + " is Invalid.")
        .equals(errorList.get(0)
            .toString()));
    
  }
  
  /**
   * This method confirm given all required Input String should have proper format with valid data .
   */
   @Test
  public void validateTest()
  {
    
    REQUIRED_INPUT_PARAM_MAP.put(StorageTask.FILES_TO_BE_STORE, "FileToBeStore");
    REQUIRED_INPUT_PARAM_MAP.put(StorageTask.FOLDER_PATH, TEST_FOLDER_PATH);
    List<String> invalidInputParameters = storageTask.validate(REQUIRED_INPUT_PARAM_MAP);
    assertTrue(invalidInputParameters.size() == 0);
  }
  
  /**
   * This method confirm given all required Input String should have proper format with invalid data .
   */
   @Test
  public void WithInvalidInputvalidateTest()
  {
    
    REQUIRED_INPUT_PARAM_MAP.put(StorageTask.FILES_TO_BE_STORE, new String());
    REQUIRED_INPUT_PARAM_MAP.put(StorageTask.FOLDER_PATH, INVALID_TEST_FOLDER_PATH);
    List<String> invalidInputParameters = storageTask.validate(REQUIRED_INPUT_PARAM_MAP);
    assertTrue(invalidInputParameters.size() == 2);
  }
  
  /**
   * Preparing the input data for folder component
   */
  @BeforeClass
  public static void loadInputData()
  {
    File folder = new File(FOLDER_PATH_CONST);
    if (!folder.exists() && !folder.isDirectory()) {
      folder.mkdir();
    }
    try (Stream<Path> paths = Files.walk(Paths.get(FOLDER_PATH_CONST))) {
      paths.forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          try {
            File file = new File(filePath.toString());
            
            if (file.getName().contains(".json")) {
              readJsonFile(file, FILE_OBJECT_MAP);
            }
            else if (file.getName().contains(".xls")) {
              readExcelFile(file, FILE_OBJECT_MAP);
            }
            
          }
          catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  /**
   * For reading excel file and converting it in Base64 encoded String and to
   * return as a map
   * @param file
   * @param excelFileobject
   */
  private static void readExcelFile(File file, Map<String, String> excelFileobject)
  {
    try {
      
      excelFileobject.put(file.getName(), Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.toURI()))));
      System.out.println(file.getName());
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  /**
   * For reading the Json files to return their map
   * @param file
   * @param jsonFileobject
   */
  private static void readJsonFile(File file, Map<String, String> jsonFileobject)
  {
    try {
      jsonFileobject.put(file.getName(), Files.readString(Paths.get(file.toURI())));
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(file.getName());
    
  }
  
  /**
   * During testing JUNIT requires file and folder Structure to be created hence
   * cleanup is done after test execution
   */
  @AfterClass
  public static void cleanUpActivity()
  {
    System.out.println("cleanUpActivity after test excecuted");
    deleteDirectory(Paths.get(FOLDER_PATH_CONST));
    deleteDirectory(Paths.get(TEST_FOLDER_PATH));
  }
  
  /**
   * Delete the folder/file created while testing after JUNIT completed
   * 
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
      e.printStackTrace();
    }
  }
  
}
