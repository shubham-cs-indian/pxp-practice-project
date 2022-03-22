package com.cs.ui.runtime.controller.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing.IGetAssetShareLinkDialogInformation;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetShareLinkDialogInformationController extends BaseController {
  
  @Autowired
  IGetAssetShareLinkDialogInformation getAssetShareLinkDialogInformation;
  
  @RequestMapping(value = "/assetinstances/sharedialoginfo", method = RequestMethod.POST)
  public IRESTModel getAssetTechnicalTypeId(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(getAssetShareLinkDialogInformation.execute(model));
  }
}