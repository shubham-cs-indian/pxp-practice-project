package com.cs.dam.runtime.assetinstance.linksharing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;

/**
 * 
 * @author mrunali.dhenge
 * 
 *         Delete main asset instance and its technical image variants From
 *         shared container of swift server and their shared object id from
 *         assetmisc table
 * 
 */

@Service
public class DeleteTIVAndMainAssetFromSharedSwiftServerService extends AbstractRuntimeService<IIdsListParameterModel, IIdParameterModel>
    implements IDeleteTIVAndMainAssetFromSharedSwiftServerService {
  
  @Autowired
  protected ThreadPoolExecutorUtil                              threadPoolTaskExecutorUtil;
  
  @Override
  protected IIdParameterModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    for (String id : model.getIds()) {
      IAssetMiscDAO assetMiscDAO = AssetMiscDAO.getInstance();
      List<String> sharedObjectIdList = assetMiscDAO.deleteSharedObjectIdForMainAssetAndItsVariantById(Long.parseLong(id));
      for (String sharedObjectId : sharedObjectIdList) {
        IIdParameterModel dataModel = new IdParameterModel();
        dataModel.setId(sharedObjectId);
        
        threadPoolTaskExecutorUtil.prepareRequestModel(dataModel, IDeleteAssetFromSharedSwiftServerService.class.getName());
      }
    }
    
    return null;
  }
  
}
