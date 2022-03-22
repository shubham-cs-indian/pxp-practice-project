package com.cs.ui.config.controller.usecase.user;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.user.IGetAllOffboardingEndpointsForUser;
import com.cs.core.runtime.interactor.model.configuration.GetOffboardingEndpointsByUserRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/config")
public class GetAllOffboardingEndpointsForUserController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllOffboardingEndpointsForUser getAllOffboardingEndpointsForUser;
  
  @RequestMapping(value = "/user/offboardingendpoint/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id,
      @RequestBody GetOffboardingEndpointsByUserRequestModel model) throws Exception
  {
    model.setId(id);
    return createResponse(getAllOffboardingEndpointsForUser.execute(model));
  }
}
