package com.cs.di.runtime.transfer;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveTransferPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.di.runtime.interactor.model.transfer.ITransferEntityRequestModel;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;

@Service("manualTransferService")
public class ManualTransferService
    extends AbstractRuntimeService<ITransferEntityRequestModel, IModel>
    implements IManualTransferService {
 
  @Autowired
  protected TransactionThreadData transactionThread;

  @Autowired
  protected PermissionUtils       permissionUtils;

  @Override
  protected IModel executeInternal(ITransferEntityRequestModel model) throws Exception
  {
    checkTransferPermission();
   
    Map<String,Object> transferRequestModel = new HashMap<>();
    transferRequestModel.put(ITransferEntityRequestModel.IDS, model.getIds());
    transferRequestModel.put(ITransferEntityRequestModel.DESTINATION_ORGANIZATION_ID, model.getDestinationOrganizationId());
    transferRequestModel.put(ITransferEntityRequestModel.DESTINATION_CATALOG_ID, model.getDestinationCatalogId());
    transferRequestModel.put(ITransferEntityRequestModel.DESTINATION_ENDPOINT_ID, model.getDestinationEndpointId());
    transferRequestModel.put(ITransferEntityRequestModel.AUTHORIZATION_MAPPING_ID, model.getAuthorizationMappingId());
    transferRequestModel.put(ITransferEntityRequestModel.IS_REVISIONABLE_TRANSFER, model.getIsRevisionableTransfer());

    this.workflowUtils.executeApplicationEvent(
        IApplicationTriggerModel.ApplicationActionType.TRANSFER,
        new JSONObject(transferRequestModel).toJSONString(), IBGProcessDTO.BGPPriority.MEDIUM);
    return null;
  }
  
  private void checkTransferPermission() throws Exception
  {
    IFunctionPermissionModel functionPermission = permissionUtils.getFunctionPermissionByUserId();
    if (!functionPermission.getCanTransfer()) {
      throw new UserNotHaveTransferPermission();
    }
  }

}
