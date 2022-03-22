package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dto.BulkAssetLinkCreationDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBulkAssetLinkCreationDTO;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveSharePermission;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.dam.runtime.interactor.model.assetinstance.linksharing.IBulkCreateAssetInstanceLinkRequestModel;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;

@Service
public class CreateAssetInstanceLink extends AbstractRuntimeInteractor<IBulkCreateAssetInstanceLinkRequestModel, IModel>
    implements ICreateAssetInstanceLink {
  
  @Autowired
  protected TransactionThreadData transactionThread;

  @Autowired
  protected PermissionUtils       permissionUtils;

  protected IIdsListParameterModel executeInternal(IBulkCreateAssetInstanceLinkRequestModel requestModel) throws Exception
  {
    checkSharePermission();

    IIdsListParameterModel returnModel = new IdsListParameterModel();
    TransactionData transactionData = transactionThread.getTransactionData();
    IBulkAssetLinkCreationDTO assetLinkCreationDTO = prepareBulkUpdateDTO(transactionData, requestModel);
    workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.ASSET_LINK_SHARING, assetLinkCreationDTO.toJSON(), BGPPriority.MEDIUM);
    return returnModel;
  }

  private IBulkAssetLinkCreationDTO prepareBulkUpdateDTO(TransactionData transactionData,
      IBulkCreateAssetInstanceLinkRequestModel requestModel)
  {
    IBulkAssetLinkCreationDTO assetLinkCreationDTO = new BulkAssetLinkCreationDTO();
    assetLinkCreationDTO.setMasterAssetIds(requestModel.getMasterAssetIds());
    assetLinkCreationDTO.setTechnicalVariantTypeIds(requestModel.getTechnicalVariantTypeIds());
    assetLinkCreationDTO.setMasterAssetShare(requestModel.getMasterAssetShare());
    assetLinkCreationDTO.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    assetLinkCreationDTO.setDataLanguage(transactionData.getDataLanguage());
    assetLinkCreationDTO.setOrganizationId(transactionData.getOrganizationId());
    return assetLinkCreationDTO;
  }

  private void checkSharePermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanShare()) {
      throw new UserNotHaveSharePermission();
    }
  }

}
