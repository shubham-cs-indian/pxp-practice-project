package com.cs.ui.runtime.controller.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing.IGetAssetInstanceLink;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetInstanceLinkController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetAssetInstanceLink getAssetInstanceLink;
  
  @RequestMapping(value = "/assetinstance/getlink", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdParameterModel model) throws Exception
  {
    return createResponse(getAssetInstanceLink.execute(model));
  }
  
}