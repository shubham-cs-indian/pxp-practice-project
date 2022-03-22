package com.cs.core.workflow.get;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.config.interactor.model.articleimportcomponent.ResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;
import com.cs.di.runtime.utils.DiFileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ProcessInstanceFilesStrategy class contain a logic to generate the Zip file
 * for any workflow process based on workflow process Instance Id .
 * 
 * @author priyaranjan.kumar
 *
 */
@Service
public class ProcessInstanceFilesService extends AbstractRuntimeService<IProcessInstanceFileModel, IResponseModelForProcessInstance>
    implements IProcessInstanceFilesService {
  
  public static final String  ZIP_FILE_EXTENSION = ".zip";
  public static final Integer BUFFER_SIZE        = 4048;
  
  /**
   * This Method take processInstanceId as an input and generate Zip file that
   * contains all files which is processed by given workflow processInstanceId
   * 
   * @param model
   *          Model is contain processInstanceId
   * @return ResponseModelForProcessInstance contain byte stream of Zip file and
   *         Zip file Name
   */
  @Override
  public IResponseModelForProcessInstance executeInternal(IProcessInstanceFileModel model) throws Exception
  {
    String diPprocessingPath = DiFileUtils.getProperty("di.processingPath");
    String processInstanceId = model.getId();
    boolean downloadIndividual = model.getIsDownloadIndividual();
    String processInstanceLocation = diPprocessingPath + File.separator + processInstanceId;
    Path processFilePath = null;
    if (processInstanceId == null || processInstanceId.isEmpty()) {
      return new ResponseModelForProcessInstance();
    } else if (!DiFileUtils.isDirectoryExist(processInstanceLocation)
        || DiFileUtils.isDirectoryEmpty(processInstanceLocation)) {
      return new ResponseModelForProcessInstance();
    }
    if (downloadIndividual == true) {
      String filePath = model.getfilePath();
      String filePathLocation = diPprocessingPath + File.separator + processInstanceId
          + File.separator + filePath;
      processFilePath = Paths.get(filePathLocation);
    }
    else {
    Path processInstancePath = Paths.get(processInstanceLocation);
    processFilePath = Paths.get(diPprocessingPath + File.separator + processInstanceId + ZIP_FILE_EXTENSION);
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(processFilePath.toFile()));
    Files.walkFileTree(processInstancePath, new SimpleFileVisitor<Path>()
    {
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
      {
        zos.putNextEntry(new ZipEntry(processInstancePath.relativize(file)
            .toString()));
        Files.copy(file, zos);
        zos.closeEntry();
        return FileVisitResult.CONTINUE;
      }
    });
    zos.close();
        
  }
    return generateResponseModel(processFilePath);
  }
  
  /**
   * Responsible to generating the response object.
   * 
   * @param filePath
   * @return ResponseModelForProcessInstance
   * @throws FileNotFoundException
   * @throws IOException
   */
  private IResponseModelForProcessInstance generateResponseModel(Path filePath)
      throws FileNotFoundException, IOException
  {
    IResponseModelForProcessInstance returnModel = new ResponseModelForProcessInstance();
    File file = filePath.toFile();
    returnModel.setFileName(file.getName());
    returnModel.setFileStream(Files.readAllBytes(filePath));
    if (filePath.endsWith(ZIP_FILE_EXTENSION)) {
      file.delete();
    }
    return returnModel;
  }

}
