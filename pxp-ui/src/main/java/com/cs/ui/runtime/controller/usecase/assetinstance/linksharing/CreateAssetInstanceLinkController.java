package com.cs.ui.runtime.controller.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.dam.runtime.interactor.model.assetinstance.linksharing.BulkCreateAssetInstanceLinkRequestModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing.ICreateAssetInstanceLink;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateAssetInstanceLinkController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateAssetInstanceLink  createAssetInstanceLink;
  
  @RequestMapping(value = "/assetinstance/createlink", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody BulkCreateAssetInstanceLinkRequestModel assetInstances) throws Exception
  {
    return createResponse(createAssetInstanceLink.execute(assetInstances));
  }
  
}
