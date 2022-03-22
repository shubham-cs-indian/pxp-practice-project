package com.cs.core.runtime.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.di.runtime.entity.dao.INotificationDAO;

@Service
public class DeleteNotificationsForUserService extends AbstractRuntimeService<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteNotificationsForUserService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    INotificationDAO notificationDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openNotificationDAO();
    notificationDAO.deleteAllNotificationForUser();
    return new BulkDeleteReturnModel();
    
  }
}
