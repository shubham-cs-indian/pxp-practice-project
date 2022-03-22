package com.cs.ui.config.controller.usecase.asset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.asset.IGetAllAssets;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllAssetsController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllAssets getAllAssets;
  
  @RequestMapping(value = "/assets", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllAssets.execute(null));
  }
}
