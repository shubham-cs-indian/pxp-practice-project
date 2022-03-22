package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.klass.DeleteKlassInstanceRequestModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.IDeleteAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteAssetInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IDeleteAssetInstance deleteAssetInstance;
  
  @RequestMapping(value = "/assetinstances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteKlassInstanceRequestModel model) throws Exception
  {
    return createResponse(deleteAssetInstance.execute(model));
  }
}
