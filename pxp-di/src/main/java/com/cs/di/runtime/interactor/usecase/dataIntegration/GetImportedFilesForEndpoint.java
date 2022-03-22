package com.cs.di.runtime.interactor.usecase.dataIntegration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.fileinstance.GetFileNameListResponseModel;
import com.cs.core.runtime.interactor.model.fileinstance.IGetFileNameListResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.di.runtime.utils.DiFileUtils;

@Service
public class GetImportedFilesForEndpoint implements IGetImportedFilesForEndpoint {
  
  @Autowired
  protected TransactionThreadData      transactionThread;
  
  @Autowired
  protected PermissionUtils permissionUtils;
  
  @Override
  public IGetFileNameListResponseModel execute(IIdPaginationModel dataModel) throws Exception
  {
    IGetFileNameListResponseModel responseModel = new GetFileNameListResponseModel();
    List<String> listOfFileNames = new ArrayList<>();
    responseModel.setFileNamesList(listOfFileNames);
    String endointId = transactionThread.getTransactionData().getEndpointId();
    String organizationId = transactionThread.getTransactionData().getOrganizationId();
    String importedFilesFolderName = DiFileUtils.getImportedFilesFolderName(endointId, organizationId);    
    
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    responseModel.setFunctionPermission(functionPermission);
    
    File endpointDirectory = new File(importedFilesFolderName);
    if (!endpointDirectory.exists()) {
      return responseModel;
    }
    File[] importedFiles = endpointDirectory.listFiles();
    int count = importedFiles.length;
    responseModel.setCount(count);
    
    Long from = dataModel.getFrom();
    Long size = dataModel.getSize();
    responseModel.setFrom(from);
    for (Long i = from; i<(long)count && i< (from+size); i++) {
      listOfFileNames.add(importedFiles[i.intValue()].getName());
    }
    return responseModel;
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
