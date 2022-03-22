package com.cs.di.runtime.interactor.usecase.dataIntegration;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.runtime.utils.DiFileUtils;

@Service
@SuppressWarnings("unchecked")
public class DeleteImportedFileFromServer implements IDeleteImportedFileFromServer {
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Override
  public IBulkDeleteReturnModel execute(IListModel<String> dataModel) throws Exception
  {
    List<String> fileIds = (List<String>) dataModel.getList();
    String endointId = transactionThread.getTransactionData().getEndpointId();
    String organizationId = transactionThread.getTransactionData().getOrganizationId();
    
    String endpointPathname = DiFileUtils.getImportedFilesFolderName(endointId, organizationId);
    for (String fileId : fileIds) {
      File file = new File(endpointPathname + fileId);
      file.delete();
    }
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    returnModel.setSuccess(fileIds);
    return returnModel;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
