package com.cs.ui.config.controller.usecase.iconlibrary;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.asset.GetAllIconsRequestModel;
import com.cs.core.config.interactor.usecase.iconlibrary.IGetAllIcons;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllIconsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllIcons getAllIconController;
  
  @RequestMapping(value = "/getallicons", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetAllIconsRequestModel getAllModel) throws Exception
  {
    return createResponse(getAllIconController.execute(getAllModel));
  }
}
