package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceBasedOnTaskGetModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstancesBasedOnTaskModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForInstancesBasedOnTasksStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetInstancesBasedOnTaskService<P extends IGetInstancesBasedOnTaskModel, R extends IGetKlassInstanceTreeModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                   context;
  
  @Autowired
  protected IGetConfigDetailsForInstancesBasedOnTasksStrategy getConfigDetailsForInstancesBasedOnTasksStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                               rdbmsComponentUtils;
  
  @Override
  protected R executeInternal(P getKlassInstanceBasedOnTaskStrategyModel) throws Exception
  {
    String loginUserId = context.getUserId();
    
    IConfigDetailsForInstanceBasedOnTaskGetModel configDetails = getConfigDetailsForInstancesBasedOnTasksStrategy
        .execute(new IdParameterModel(loginUserId));
    
    getKlassInstanceBasedOnTaskStrategyModel.setCurrentUserId(loginUserId);
    getKlassInstanceBasedOnTaskStrategyModel
        .setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    getKlassInstanceBasedOnTaskStrategyModel
        .setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    getKlassInstanceBasedOnTaskStrategyModel
        .setTaskIdsHavingReadPermissions(configDetails.getTaskIdsHavingReadPermissions());
    getKlassInstanceBasedOnTaskStrategyModel.setTaskIdsForRolesHavingReadPermission(
        configDetails.getTaskIdsForRolesHavingReadPermission());
    getKlassInstanceBasedOnTaskStrategyModel.setPersonalTaskIds(configDetails.getPersonalTaskIds());
    
    IGetKlassInstanceTreeModel klassInstanceTree = new GetKlassInstanceTreeModel();
    this.getEntityInfo(getKlassInstanceBasedOnTaskStrategyModel, configDetails, klassInstanceTree);
    
    /*return (R) getInstanceBasedOnTaskStrategy.execute(getKlassInstanceBasedOnTaskStrategyModel);*/
    return (R) klassInstanceTree;
  }
  
  protected void getEntityInfo(IGetInstancesBasedOnTaskModel getInstancesBasedOnTaskModel,
      IConfigDetailsForInstanceBasedOnTaskGetModel configDetails,
      IGetKlassInstanceTreeModel klassInstanceTree) throws Exception
  {
    RACIVS vRACIVS = TaskUtil.getRACIVS(getInstancesBasedOnTaskModel.getRoleId());
    
    Set<IBaseEntityDTO> baseEntityRecords = this.rdbmsComponentUtils.openTaskDAO()
        .getAllEntity(this.rdbmsComponentUtils.getUserID(), vRACIVS);
    
    List<IKlassInstanceInformationModel> children = klassInstanceTree.getChildren();
    baseEntityRecords.forEach(baseEntityRecord -> {
      try {
        IKlassInstanceInformationModel klassInstanceInformationModel = new KlassInstanceInformationModel();
        klassInstanceInformationModel.setId(String.valueOf(baseEntityRecord.getBaseEntityIID()));
        klassInstanceInformationModel.setName(baseEntityRecord.getBaseEntityName());
        children.add(klassInstanceInformationModel);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}
