package com.cs.core.runtime.taskinstance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.tag.IGetGridTagsStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetTagsByIdStrategy;
import com.cs.core.config.strategy.usecase.task.IGetAllTasksForDashboardStrategy;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceForDashboardRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetConfigDetailsForTasksDashboardModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;


@Service
public class GetTaskCountByTypeService extends
    AbstractRuntimeService<IIdParameterModel, IIdParameterModel> implements IGetTaskCountByTypeService {
  
  @Autowired
  ISessionContext                  context;
  
  @Autowired
  IGetGridTagsStrategy             getGridTagsStrategy;
  
  @Autowired
  IGetAllTasksForDashboardStrategy getAllTasksStrategy;
  
  @Autowired
  protected RDBMSComponentUtils             rdbmsComponentUtils;
  
  //@Autowired
  //IGetAllTasksCountByTypeStrategy  getAllTasksCountByTypeStrategy;
  
  @Autowired
  IGetTagsByIdStrategy             getTagsByIdStrategy;
  
  @Override
  public IIdParameterModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    IIdParameterModel tasksInstanceResponseModel = new IdParameterModel();
    IIdsListParameterModel getTagRequestModel = new IdsListParameterModel();
    getTagRequestModel.getIds().add(SystemLevelIds.TASK_STATUS_TAG);
    IListModel<ITag> tagsList = getTagsByIdStrategy.execute(getTagRequestModel);
    List<String> tagValuesList = tagsList.getList().iterator().next().getTagValuesSequence();
    TransactionData transactionData = transactionThread.getTransactionData();
    
    if (tagValuesList.contains(dataModel.getId())) {
      dataModel.setCurrentUserId(context.getUserId());
      IGetConfigDetailsForTasksDashboardModel configDetails = getAllTasksStrategy.execute(dataModel);
      
      IGetTaskInstanceForDashboardRequestModel requestModel = new GetTaskInstanceForDashboardRequestModel();
      requestModel.setConfigDetails(configDetails);
      requestModel.setUserDetails(dataModel);
      
      ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
      String roleCode = configDetails.getRoleIdsOfCurrentUser().iterator().hasNext()
          ? configDetails.getRoleIdsOfCurrentUser().iterator().next()
          : "";
      String organizationId = transactionData.getOrganizationId();
      if (organizationId.equals(IStandardConfig.STANDARD_ORGANIZATION_CODE)) {
        organizationId = IStandardConfig.STANDARD_ORGANIZATION_RCODE;
      }
      int allPlannedTaskCount = taskRecordDAO.getAllPlannedTaskCount(roleCode, transactionData.getPhysicalCatalogId(), organizationId);
      tasksInstanceResponseModel.setId(String.valueOf(allPlannedTaskCount));
    }
    return tasksInstanceResponseModel;
  }
}
