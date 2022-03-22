package com.cs.dam.runtime.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;

/**
 * 
 * @author mrunali.dhenge
 * 
 *         Delete asset from shared container of swift server and shared object
 *         id from assetmisctable
 *         
 */
@Service
public class DeleteAssetWithoutTIVsFromSharedSwiftServerService extends AbstractRuntimeService<IIdParameterModel, IIdParameterModel>
    implements IDeleteAssetWithoutTIVsFromSharedSwiftServerService {
  
  @Autowired
  protected ThreadPoolExecutorUtil threadPoolTaskExecutorUtil;
  
  private static final String      IS_NOT_PRESENT_SHARED_OBJECT_ID = "NotPresent";
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    IAssetMiscDAO assetMiscDAO = AssetMiscDAO.getInstance();
    String isPresentSharedObjectId = assetMiscDAO.getAssetMiscRecordById(Long.parseLong(model.getId()));
    if (!isPresentSharedObjectId.equalsIgnoreCase(IS_NOT_PRESENT_SHARED_OBJECT_ID)) {
      IIdParameterModel dataModel = new IdParameterModel();
      dataModel.setId(isPresentSharedObjectId);
      threadPoolTaskExecutorUtil.prepareRequestModel(dataModel, IDeleteAssetFromSharedSwiftServerService.class.getName());
      assetMiscDAO.updateAssetMiscRecordWithSharedObjectId("", Long.parseLong(model.getId()));
    }
    return null;
  }
}
