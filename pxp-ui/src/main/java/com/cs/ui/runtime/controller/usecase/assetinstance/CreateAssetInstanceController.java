package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.assetinstance.CreateAssetInstanceModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.ICreateAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateAssetInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateAssetInstance createAssetInstance;
  
  @RequestMapping(value = "/assetinstances", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateAssetInstanceModel klassInstanceModel)
      throws Exception
  {
    return createResponse(createAssetInstance.execute(klassInstanceModel));
  }
}
