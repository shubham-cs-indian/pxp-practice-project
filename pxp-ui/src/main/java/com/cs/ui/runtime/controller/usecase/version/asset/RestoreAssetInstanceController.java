package com.cs.ui.runtime.controller.usecase.version.asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.dam.runtime.interactor.version.IRestoreAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class RestoreAssetInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IRestoreAssetInstance restoreAssetInstance;
  
  @RequestMapping(value = "/assetinstances/restore", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(restoreAssetInstance.execute(model));
  }
}
