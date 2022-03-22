package com.cs.core.runtime.interactor.usecase;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.util.ConfigUtil;

@SuppressWarnings("unchecked")
public abstract class AbstractGetInstanceForOnboarding<P extends IGetInstanceRequestModel, R extends IGetKlassInstanceCustomTabModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected TransactionThreadData         controllerThread;
  
  @Autowired
  protected RDBMSComponentUtils           rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                    configUtil;
  
  @Autowired
  protected VariantInstanceUtils          variantInstanceUtils;

  @Autowired
  protected PermissionUtils               permissionUtils;
  
  @Autowired
  protected RelationshipInstanceUtil      relationshipInstanceUtil;
  
  protected abstract IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModelForCustomTab getKlassInstanceTreeStrategyModel)
      throws Exception;
  
  protected abstract IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception;
  
  protected abstract IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception;
  
  protected R executeInternal(P instanceRequestModel) throws Exception
  {
    long starTime1 = System.currentTimeMillis();
    List<String>  languageInheritance = configUtil.getLanguageInheritance();
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
        .getBaseEntityDAO(Long.parseLong(instanceRequestModel.getId()),languageInheritance);
    
    RDBMSLogger.instance()
    .debug("NA|RDBMS|" + this.getClass()
        .getSimpleName() + "|executeInternal|openBaseEntity| %d ms",
        System.currentTimeMillis() - starTime1);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
        .getConfigRequestModelForCustomTab(instanceRequestModel, baseEntityDAO);

    long starTime2 = System.currentTimeMillis();
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(multiclassificationRequestModel);
    
    RDBMSLogger.instance()
    .debug("NA|OrientDB|" + this.getClass()
        .getSimpleName() + "|executeInternal|getConfigDetails| %d ms",
        System.currentTimeMillis() - starTime2);
    IGetKlassInstanceCustomTabModel returnModel = executeGetKlassInstance(instanceRequestModel,configDetails, baseEntityDAO);
    returnModel.setConfigDetails(configDetails);
    relationshipInstanceUtil.executeGetRelationshipInstance(returnModel, configDetails, baseEntityDAO, rdbmsComponentUtils);
    returnModel.setGlobalPermission(configDetails.getReferencedPermissions().getGlobalPermission());
    return (R) returnModel;
  }
  
  protected IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestModel instanceRequestModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    returnModel.setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
    
    returnModel.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    fillEntityInformation(baseEntityDAO, returnModel);
    return returnModel;
  }
  
  //method overriden in asset specific implementation
  protected void fillEntityInformation(IBaseEntityDAO baseEntityDAO, IGetKlassInstanceCustomTabModel returnModel) throws Exception
  {
   
  }
  
  protected IGetKlassInstanceCustomTabModel propertyLevelTaskinfo(
      IGetKlassInstanceCustomTabModel returnModel, IGetInstanceRequestModel instanceRequestModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    //property level task info
    Set<String> propertyIdsHavingTask = returnModel.getPropertyIdsHavingTask();
    List<ITaskRecordDTO> taskRecords = taskRecordDAO.getAllTaskByBaseEntityIID(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), this.rdbmsComponentUtils.getUserID());
    for (ITaskRecordDTO taskRecord : taskRecords) {
      if(taskRecord.getPropertyIID() != 0) {
        propertyIdsHavingTask.add(RDBMSUtils.getPropertyByIID(taskRecord.getPropertyIID()).getPropertyCode());
      }
    }
    
    // Task Count
    int taskCountOnBaseEntity = taskRecordDAO
        .getTaskCountOnBaseEntity(baseEntityDAO.getBaseEntityDTO()
            .getBaseEntityIID(), this.rdbmsComponentUtils.getUserID());
    returnModel.setTasksCount(taskCountOnBaseEntity);
    
    return returnModel;
  }
}
