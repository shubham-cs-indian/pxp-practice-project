package com.cs.core.config.strategy.usecase.notification;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.notification.GetConfigDetailsForNotificationModel;
import com.cs.core.runtime.interactor.model.notification.IGetConfigDetailsForNotificationModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetConfigDetailsForNotificationStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForNotificationStrategy {
  
  @Override
  public IGetConfigDetailsForNotificationModel execute(IListModel<IIdAndTypeModel> model)
      throws Exception
  {
    HashMap<String, Object> map = new HashMap<>();
    map.put("list", model.getList());
    return execute(GET_CONFIG_DETAILS_FOR_NOTIFICATION, map,
        GetConfigDetailsForNotificationModel.class);
  }
}
