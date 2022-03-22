package com.cs.core.runtime.klassinstance;

import com.cs.core.bgprocess.dto.BaseEntityPlanForBulkCloneDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBaseEntityPlanForBulkCloneDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetAllNatureRelationshipsIdsStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveClonePermission;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.configuration.*;
import com.cs.core.runtime.interactor.model.klassinstance.BulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractCreateInstanceBulkClone<P extends ICreateKlassInstanceBulkCloneModel, R extends IBulkCreateKlassInstanceCloneResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                    context;
  
  @Autowired
  protected TransactionThreadData                              transactionThread;
  
  @Autowired
  protected IGetAllNatureRelationshipsIdsStrategy              getAllNatureRelationshipsIdsStrategy;
  
  @Autowired
  protected IGetGlobalPermissionForMultipleNatureTypesStrategy getGlobalPermissionForMultipleNatureTypesStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                rdbmsComponentUtils;
  
  protected abstract Exception getUserNotHaveCreatePermissionException();
  
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    
    IBulkCreateKlassInstanceCloneResponseModel responseModel = new BulkCreateKlassInstanceCloneResponseModel();
    IExceptionModel failure = new ExceptionModel();
    checkPermissions(dataModel, failure);
    List<IGetKlassInstanceModel> klassInstanceModelsToReturn = new ArrayList<>();
    responseModel.setSuccess(klassInstanceModelsToReturn);
    responseModel.setFailure(failure);
    IBaseEntityPlanForBulkCloneDTO bulkCloneDTO = prepareBulkCloneDTO(dataModel);
    this.workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.BULK_CLONING, bulkCloneDTO.toJSON(),
        BGPPriority.MEDIUM);
    return (R) responseModel;
  }
  
  private IBaseEntityPlanForBulkCloneDTO prepareBulkCloneDTO(ICreateKlassInstanceBulkCloneModel dataModel) throws Exception
  {
    List<IIdAndTypeModel> contentsToClone = dataModel.getContentsToClone();
    IBaseEntityPlanForBulkCloneDTO entryData = new BaseEntityPlanForBulkCloneDTO();
    List<Long> entitiesToClone = new ArrayList<>();
    IIdsListParameterModel natureRelationshipIdsList = getAllNatureRelationshipsIdsStrategy.execute(new VoidModel());
    for (IIdAndTypeModel contentToClone : contentsToClone) {
      entitiesToClone.add(Long.parseLong(contentToClone.getId()));
    }
    entryData.setBaseEntityIIDs(entitiesToClone);
    entryData.setAllProperties(true);
    entryData.setNatureRelationshipIds(natureRelationshipIdsList.getIds());
    entryData.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    entryData.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    entryData.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    entryData.setUserId(transactionThread.getTransactionData().getUserId());
    
    return entryData;
  }
  
  private void checkPermissions(ICreateKlassInstanceBulkCloneModel dataModel, IExceptionModel failure) throws Exception
  {
    IGetGlobalPermissionForMultipleNatureTypesRequestModel requestModel = new GetGlobalPermissionForMultipleNatureTypesRequestModel();
    requestModel.setUserId(context.getUserId());
    Set<String> classifierCodes = new HashSet<>();
    List<IIdAndTypeModel> validContentToCloned = new ArrayList<IIdAndTypeModel>();
    Map<String, String> baseEnityIIdVsNatureClassifierCode = new HashMap<String, String>();
    IBaseEntityDAO baseEntityDAO;
    String classifierCode;
    for (IIdAndTypeModel contentToClone : dataModel.getContentsToClone()) {
      baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(contentToClone.getId()));
      classifierCode = (String) baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getClassifierCode();
      classifierCodes.add(classifierCode);
      baseEnityIIdVsNatureClassifierCode.put(contentToClone.getId(), classifierCode);
    }
    requestModel.setKlassIds(classifierCodes);
    IGetGlobalPermissionForMultipleNatureTypesResponseModel permissionResponseModel = getGlobalPermissionForMultipleNatureTypesStrategy
        .execute(requestModel);
    IFunctionPermissionModel functionPermissionMap = permissionResponseModel.getFunctionPermission();
    if (!functionPermissionMap.getCanClone()) {
      throw new UserNotHaveClonePermission();
    }
    Map<String, IGlobalPermission> globalPermissionMap = permissionResponseModel.getGlobalPermission();
    IGlobalPermission globalPermission;
    for (Entry<String, String> classifierInfo : baseEnityIIdVsNatureClassifierCode.entrySet()) {
      IIdAndTypeModel idAndTypeModel = new IdAndTypeModel();
      idAndTypeModel.setId(classifierInfo.getKey());
      try {
        globalPermission = globalPermissionMap.get(classifierInfo.getValue());
        if (!globalPermission.getCanCreate()) {
          throw new UserNotHaveCreatePermission();
        }
        validContentToCloned.add(idAndTypeModel);
      }
      catch (UserNotHaveCreatePermission e) {
        Exception ex = getUserNotHaveCreatePermissionException();
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, String.valueOf(classifierInfo.getKey()), null);
      }
    }
    dataModel.setContentsToClone(validContentToCloned);
  }
  
}
