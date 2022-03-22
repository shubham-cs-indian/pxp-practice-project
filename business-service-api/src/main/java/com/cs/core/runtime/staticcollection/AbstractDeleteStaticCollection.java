package com.cs.core.runtime.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

public abstract class AbstractDeleteStaticCollection<P extends IIdsListParameterModel, R extends IBulkResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  private static final String SERVICE       = "ORPHANS_CLEANING";

  
  public abstract CollectionType getCollectionType();
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(IIdsListParameterModel deleteModel) throws Exception
  {
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    for(String id:deleteModel.getIds()) {
      collectionDAO.deleteCollectionRecords(Long.parseLong(id));;
    }
    /* //BGP:
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    StringBuffer assembleJSONBuffer = JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField("CollectionIIDs", deleteModel.getIds().get(0)));
    Long jobIID = BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), SERVICE, "", userPriority, new JSONContent(assembleJSONBuffer.toString()));*/
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, "", userPriority, new JSONContent("{}"));
    
    returnModel.setSuccess(deleteModel.getIds());
    return (R) returnModel;
  }
  
}