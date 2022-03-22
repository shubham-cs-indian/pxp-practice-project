package com.cs.core.runtime.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.notification.IGetConfigDetailsForNotificationStrategy;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.interactor.entity.notification.EntityInfo;
import com.cs.core.runtime.interactor.model.configuration.GetAllModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.notification.GetAllNotificationsResponseModel;
import com.cs.core.runtime.interactor.model.notification.IGetAllNotificationsResponseModel;
import com.cs.core.runtime.interactor.model.notification.IGetConfigDetailsForNotificationModel;
import com.cs.core.runtime.interactor.model.notification.INotificationModel;
import com.cs.core.runtime.interactor.model.notification.NotificationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dao.INotificationDAO;
import com.cs.di.runtime.entity.idto.INotificationDTO;

@Service
public class GetAllNotificationsForUserService extends AbstractRuntimeService<IGetAllModel, IGetAllNotificationsResponseModel>
    implements IGetAllNotificationsForUserService {
  
  @Autowired
  protected RDBMSComponentUtils                      rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForNotificationStrategy getConfigDetailsForNotificationStrategy;
  
  @Override
  public IGetAllNotificationsResponseModel executeInternal(IGetAllModel dataModel) throws Exception
  {
    IGetAllNotificationsResponseModel returnModel = getAllNotificationForUser(dataModel);
    this.getTaskInfo(returnModel, (String)((GetAllModel)dataModel).getAdditionalProperty("physicalCatalogId"));
    IListModel<IIdAndTypeModel> listModel = new ListModel<>(returnModel.getTaskInfo());
    IGetConfigDetailsForNotificationModel model = getConfigDetailsForNotificationStrategy.execute(listModel);
    returnModel.setUserInfoList(model.getUserList());
    returnModel.setRoleIdLabelMap(model.getRoleIdLabelMap());
    returnModel.setReferencedTasks(model.getReferencedTasks());
    returnModel.setReferencedTags(model.getReferencedTags());
    return returnModel;
  }
  
  private IGetAllNotificationsResponseModel getAllNotificationForUser(IGetAllModel dataModel) throws Exception, RDBMSException
  {
    IGetAllNotificationsResponseModel returnModel = new GetAllNotificationsResponseModel();
    INotificationDAO notificationDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openNotificationDAO();
    List<INotificationModel> allNotificationModel = new ArrayList<>();
    EntityInfo entityInfo = new EntityInfo();
    for (INotificationDTO notificationForUser : notificationDAO.getAllNotificationForUser()) {
      INotificationModel notificationModel = new NotificationModel();
      notificationModel.setId(notificationForUser.getInstanceIID().toString());
      notificationModel.setActedBy(notificationForUser.getActedBy().toString());
      notificationModel.setActedFor(notificationForUser.getActedFor().toString());
      notificationModel.setAction(notificationForUser.getAction());
      notificationModel.setStatus(notificationForUser.getStatus());
      notificationModel.setDescription(notificationForUser.getDescription());
      notificationModel.setCreatedOn(notificationForUser.getCreatedOn());
      notificationModel.setEntityInfo(entityInfo);
      allNotificationModel.add(notificationModel);
    }
    returnModel.setNotifications(allNotificationModel);
    returnModel.setSize(allNotificationModel.size());
    returnModel.setFrom(dataModel.getFrom());
    return returnModel;
  }
  
  protected void getTaskInfo(IGetAllNotificationsResponseModel returnModel, String physicalCatalogId) throws Exception
  {
    Set<ITaskRecordDTO> taskRecords = this.rdbmsComponentUtils.openTaskDAO().getAllTask(this.rdbmsComponentUtils.getUserID(),
        RACIVS.UNDEFINED, physicalCatalogId);
    List<IIdAndTypeModel> taskInfo = returnModel.getTaskInfo();
    taskRecords.forEach(taskRecord -> {
      IIdAndTypeModel idAndTypeModel = new IdAndTypeModel();
      idAndTypeModel.setId(String.valueOf(taskRecord.getTaskIID()));
      idAndTypeModel.setType(taskRecord.getTask().getCode());
      taskInfo.add(idAndTypeModel);
    });
  }
}