package com.cs.ui.runtime.controller.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.notification.IChangeNotificationStatus;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class ChangeNotificationStatusController  extends BaseController implements IRuntimeController   {
  
  @Autowired
  protected IChangeNotificationStatus changeNotificationStatus;
  
  @RequestMapping(value = "/notificationstatus", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdsListParameterModel idModel)
      throws Exception
  {
    return createResponse(changeNotificationStatus.execute(idModel));
  }
}
