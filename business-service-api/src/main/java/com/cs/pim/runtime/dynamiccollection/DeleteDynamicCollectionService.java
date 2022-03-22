package com.cs.pim.runtime.dynamiccollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class DeleteDynamicCollectionService
    extends AbstractRuntimeService<IIdsListParameterModel, IBulkResponseModel>
    implements IDeleteDynamicCollectionService
{ 
  
  @Autowired
  RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel deleteModel) throws Exception
  {
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    collectionDAO.deleteCollectionRecords(Long.parseLong(deleteModel.getIds().get(0)));
    
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    returnModel.setSuccess(deleteModel.getIds());
    
    return returnModel;
  }

}
