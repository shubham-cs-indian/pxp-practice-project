package com.cs.dam.runtime.assetinstance;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.Constants;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.articleinstance.ArticleInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.klassinstance.AbstractDeleteKlassInstancesService;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteTIVAndMainAssetFromSharedSwiftServerService;

@Service
public class DeleteAssetInstanceService
    extends AbstractDeleteKlassInstancesService<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteAssetInstanceService {
  
  @Resource(name = "assetException")
  private Properties                                            assetException;
  
  @Autowired
  protected ThreadPoolExecutorUtil                              threadPoolTaskExecutorUtil;
  
  @Autowired
  protected TransactionThreadData                               transactionThreadData;
  
  @Autowired
  protected IFetchAssetConfigurationDetails                     fetchAssetConfigurationDetails;
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IDeleteKlassInstanceRequestModel deleteAssetRequestModel) throws Exception
  {
    try {
      List<IDeleteKlassInstanceModel> deleteRequests = deleteAssetRequestModel.getDeleteRequest();
      IDeleteKlassInstanceResponseModel deleteAssetResponse = super.executeInternal(deleteAssetRequestModel);
      prepareDataForSharedLinkDeletion(deleteRequests);
      return deleteAssetResponse;
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(assetException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new ArticleInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
  
  // Prepare data for shared asset deletion and call it through thread pool
  // executor.
  private void prepareDataForSharedLinkDeletion(List<IDeleteKlassInstanceModel> deleteRequests) throws Exception
  {
    for (IDeleteKlassInstanceModel deleteRequest : deleteRequests) {
      if (Constants.ASSET_INSTANCE_BASE_TYPE.equalsIgnoreCase(deleteRequest.getBaseType())) {
        List<String> deleteAssetIds = deleteRequest.getIds();
        IIdsListParameterModel dataModel = new IdsListParameterModel();
        dataModel.setIds(deleteAssetIds);
        threadPoolTaskExecutorUtil.prepareRequestModel(dataModel, IDeleteTIVAndMainAssetFromSharedSwiftServerService.class.getName());
      }
    }
  }
}
