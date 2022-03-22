package com.cs.di.runtime.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.process.TalendComponentException;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.tasks.TalendTask;

public class DiTalendUtils {
  
  private static final String EXTENSION_SEPARATOR = ".";
  private static final String PATH_SEPARATOR      = "/";
  private static final String PACKAGE_SEPARATOR   = "_";
  private static final String CONTEXT_PARAM       = "--context_param";
  private static final String ZERO                = "0";
  private static final String METHOD_TO_INVOKE    = "runJob";
  private static final String CLASS_FILE          = ".class";
  private static final String EXTENSION_JAR       = "jar";
  
  /**
   * Perform the required process to run Executable
   * 
   * @param dataModel
   * @return
   * @throws Exception
   */
  public static void runTalendExecutable(WorkflowTaskModel dataModel) throws Exception
  {
    try {
      writeToSourceFile(dataModel);
      String exitCode = executeJob(dataModel);
      if (exitCode.equals(ZERO)) {
        readFromOutputFile(dataModel);
      }
      else {
        dataModel.getExecutionStatusTable()
            .addError(MessageCode.GEN012);
        throw new TalendComponentException("Talend Component Failed");
      }
    }
    
    catch (TalendComponentException talendComponentException) {
      throw talendComponentException;
    }
    catch (Exception exception) {
      throw new TalendComponentException(exception.getMessage());
    }
  }
  
  /**
   * set the context parameters, load the executable and run
   * 
   * @param dataModel
   * @param fileNamePrefix
   * @return
   * @throws Exception
   */
  private static String executeJob(WorkflowTaskModel dataModel) throws Exception
  {
    List<URL> urls = prepareFileUrls(dataModel);
    String[] context = prepareContextForEntity(dataModel);
    ClassLoader originalLoader = Thread.currentThread()
        .getContextClassLoader();
    Object response = false;
    try {
      ClassLoader localClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]),
          ClassLoader.getSystemClassLoader()
              .getParent());
      Class<?> loadedClass = null;
      String className = getClassName(dataModel);
      loadedClass = localClassLoader.loadClass(className);
      
      Method methodToInvoke = loadedClass.getDeclaredMethod(METHOD_TO_INVOKE, String[].class);
      Thread.currentThread()
          .setContextClassLoader(localClassLoader);
      Object classInstance = loadedClass.getDeclaredConstructor()
          .newInstance();
      response = methodToInvoke.invoke(classInstance, (Object) context);
    }
    catch (Throwable e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    finally {
      Thread.currentThread()
          .setContextClassLoader(originalLoader);
    }
    
    String[][] array = (String[][]) response;
    String exitCode = array[0][0];
    return exitCode;
  }
  
  /**
   * search for the jar file of the main class and return the full qualified
   * name
   * 
   * @param dataModel
   * @return
   * @throws Exception
   */
  private static String getClassName(WorkflowTaskModel dataModel) throws Exception
  {
    String talendJarName = (String) dataModel.getInputParameters().get(TalendTask.TALEND_EXECUTABLE);
    String fileName = talendJarName.substring(0, talendJarName.lastIndexOf(EXTENSION_SEPARATOR));
    for (int i = 0; i < 2; i++) {
      fileName = fileName.substring(0, fileName.lastIndexOf(PACKAGE_SEPARATOR));
    }
    List<String> classNames = getAllClassNamesInJar(dataModel);
    for (String className : classNames) {
      String extractedClass = className.substring(className.lastIndexOf(EXTENSION_SEPARATOR) + 1);
      if (extractedClass.equalsIgnoreCase(fileName)) {
        return className;
      }
    }
    return null;
  }
  
  /**
   * Fetch list of libs present in libs directory
   * 
   * @param dataModel
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static List<String> getAllClassNamesInJar(WorkflowTaskModel dataModel)
      throws FileNotFoundException, IOException
  {
    
    List<String> classNames = new ArrayList<String>();
    ZipInputStream zip = new ZipInputStream(new FileInputStream(dataModel.getInputParameters()
        .get(TalendTask.TALEND_JOB_PATH) + PATH_SEPARATOR
        + dataModel.getInputParameters()
            .get(TalendTask.TALEND_EXECUTABLE)));
    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
      if (!entry.isDirectory() && entry.getName()
          .endsWith(CLASS_FILE)) {
        String className = entry.getName()
            .replace(PATH_SEPARATOR, EXTENSION_SEPARATOR);
        classNames.add(className.substring(0, className.length() - CLASS_FILE.length()));
      }
    }
    zip.close();
    return classNames;
  }
  
  /**
   * prepares the File Urls
   * 
   * @param dataModel
   * @return
   * @throws MalformedURLException
   * @throws IOException
   */
  private static List<URL> prepareFileUrls(WorkflowTaskModel dataModel)
      throws MalformedURLException, IOException
  {
    List<URL> urls = new ArrayList<URL>();
    String talendJobFilePath = dataModel.getInputParameters()
        .get(TalendTask.TALEND_JOB_PATH) + PATH_SEPARATOR
        + (String) dataModel.getInputParameters()
            .get(TalendTask.TALEND_EXECUTABLE);
    File talendJob = new File(talendJobFilePath);
    urls.add(talendJob.toURI()
        .toURL());
    addSubDependencies(urls, (String) dataModel.getInputParameters()
        .get(TalendTask.EXTERNAL_JARS));
    return urls;
  }
  
  /**
   * Write to Source file
   * 
   * @param dataModel
   * @param fileNamePrefix
   * @throws FileNotFoundException
   */
  private static void writeToSourceFile(WorkflowTaskModel dataModel)
  {
    String inputFile = getInputFileName(dataModel);
    File file = new File(inputFile);
    
    try {
      FileUtils.writeStringToFile(file, dataModel.getInputParameters()
          .get(TalendTask.INPUT_DATA)
          .toString(), StandardCharsets.UTF_8);
    }
    catch (IOException ioException) {
      RDBMSLogger.instance()
          .exception(ioException);
    }
  }
  
  /**
   * returns qualified source file name
   * 
   * @param dataModel
   * @return
   */
  private static String getInputFileName(WorkflowTaskModel dataModel)
  {
    String file = dataModel.getInputParameters()
        .get(TalendTask.SOURCE_FILE_PATH) + PATH_SEPARATOR
        + dataModel.getInputParameters()
            .get(TalendTask.FILE_NAME);
    String inputFile = file + EXTENSION_SEPARATOR + dataModel.getInputParameters()
        .get(TalendTask.FILE_EXTENSION);
    return inputFile;
  }
  
  /**
   * returns qualified destination file name
   * 
   * @param dataModel
   * @return
   */
  private static String getOutputFileName(WorkflowTaskModel dataModel)
  {
    
    String outputFile = dataModel.getInputParameters()
        .get(TalendTask.OUTPUT_FILE_PATH) + PATH_SEPARATOR
        + dataModel.getInputParameters()
            .get(TalendTask.FILE_NAME)
        + EXTENSION_SEPARATOR + dataModel.getInputParameters()
            .get(TalendTask.FILE_EXTENSION);
    return outputFile;
  }
  
  /**
   * prepare context to set during method invocation
   * 
   * @param dataModel
   * @return
   */
  private static String[] prepareContextForEntity(WorkflowTaskModel dataModel)
  {
    List<String> listOfContext = new ArrayList<>();
    String inputFile = getInputFileName(dataModel);
    String outputFile = getOutputFileName(dataModel);
    String fileName = (String) dataModel.getInputParameters()
        .get("fileName");
    listOfContext.add(CONTEXT_PARAM + " sourceFile=" + inputFile);
    listOfContext.add(CONTEXT_PARAM + " outputFile=" + outputFile);
    listOfContext.add(CONTEXT_PARAM + " TargetXmlFolder=" + dataModel.getInputParameters()
        .get(TalendTask.TARGET_XML_FOLDER) + PATH_SEPARATOR + fileName + ".xml");
    listOfContext.add(CONTEXT_PARAM + " TargetJsonfolder=" + dataModel.getInputParameters()
        .get(TalendTask.TARGET_JSON_FOLDER) + PATH_SEPARATOR + fileName + ".json");
    listOfContext.add(
        CONTEXT_PARAM + " archiveSuccessFile=" + TalendTask.ARCHIVE_SUCCESS_FILE + PATH_SEPARATOR);
    listOfContext.add(
        CONTEXT_PARAM + " archiveFailureFile=" + TalendTask.ARCHIVE_FAILURE_FILE + PATH_SEPARATOR);
    listOfContext.add(CONTEXT_PARAM + " errorLog=" + TalendTask.ERROR_LOG_PATH + PATH_SEPARATOR);
    
    return listOfContext.toArray(new String[0]);
    
  }
  
  /**
   * add the all dependent jars required to the URLs
   * 
   * @param urls
   * @param path
   * @throws IOException
   */
  private static void addSubDependencies(List<URL> urls, String path) throws IOException
  {
    File directory = new File(path);
    File[] fList = directory.listFiles();
    
    for (File file : fList) {
      if (!file.isDirectory()) {
        String extension = FilenameUtils.getExtension(file.getName());
        if (extension.equals(EXTENSION_JAR)) {
          urls.add(file.toURI()
              .toURL());
        }
      }
    }
  }
  
  /**
   * Read from output file and set output parameters of the model
   * 
   * @param model
   * @param fileContent
   * @param fileNamePrefix
   * @throws Exception
   */
  private static void readFromOutputFile(WorkflowTaskModel model) throws Exception
  {
    String fileContent = null;
    String outputFile = getOutputFileName(model);
    try {
      fileContent = FileUtils.readFileToString(new File(outputFile), StandardCharsets.UTF_8);
    }
    catch (IOException ioException) {
      RDBMSLogger.instance()
          .exception(ioException);
    }
    model.getOutputParameters()
        .put(TalendTask.OUTPUT_DATA, fileContent);
    model.getExecutionStatusTable()
        .addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN045,
            new String[] { "Talend JAR" });
  }
}
