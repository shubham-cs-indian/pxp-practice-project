package com.cs.ui.runtime.controller.usecase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.GetAllModel;
import com.cs.core.runtime.interactor.usecase.notification.IGetAllNotificationsForUser;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllNotificationsForUserController extends BaseController implements IRuntimeController  {
  
  @Autowired
  protected IGetAllNotificationsForUser getAllNotificationsForUser;
  
  @RequestMapping(value = "/notifications", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetAllModel model, @RequestParam String physicalCatalogId)
      throws Exception
  {
    model.setAdditionalProperty("physicalCatalogId", physicalCatalogId);
    return createResponse(getAllNotificationsForUser.execute(model));
  }
}
