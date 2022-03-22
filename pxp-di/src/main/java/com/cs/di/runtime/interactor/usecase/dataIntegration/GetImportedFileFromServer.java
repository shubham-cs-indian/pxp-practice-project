package com.cs.di.runtime.interactor.usecase.dataIntegration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.config.interactor.model.articleimportcomponent.ResponseModelForProcessInstance;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.runtime.utils.DiFileUtils;

@Service
public class GetImportedFileFromServer implements IGetImportedFileFromServer {
  
  private static final String     XLSX_FILE_EXTENSION = ".xlsx";
  
  @Autowired
  protected TransactionThreadData            transactionThread;

  @Override
  public IResponseModelForProcessInstance execute(IdParameterModel model) throws Exception
  {
    String endointId = transactionThread.getTransactionData().getEndpointId();
    String organizationId = transactionThread.getTransactionData().getOrganizationId();
    
    String importedFilesFolderName = DiFileUtils.getImportedFilesFolderName(endointId, organizationId);
    Path filePath = Paths.get(importedFilesFolderName + File.separator + model.getId());
    return generateResponseModel(filePath);
  }
  
  private IResponseModelForProcessInstance generateResponseModel(Path filePath)
      throws FileNotFoundException, IOException
  {
    IResponseModelForProcessInstance returnModel = new ResponseModelForProcessInstance();
    File file = filePath.toFile();
    returnModel.setFileName(file.getName() + XLSX_FILE_EXTENSION);
    returnModel.setFileStream(Files.readAllBytes(file.toPath()));
    return returnModel;
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
