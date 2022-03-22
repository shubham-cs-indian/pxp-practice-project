package com.cs.core.runtime.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class DeleteNotificationByIdService extends AbstractRuntimeService<IIdParameterModel, IIdParameterModel>
    implements IDeleteNotificationByIdService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  public IIdParameterModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    rdbmsComponentUtils.getLocaleCatlogDAO().openNotificationDAO().deleteNotificationById(Long.parseLong(dataModel.getId()));
    return null;
  }
}
