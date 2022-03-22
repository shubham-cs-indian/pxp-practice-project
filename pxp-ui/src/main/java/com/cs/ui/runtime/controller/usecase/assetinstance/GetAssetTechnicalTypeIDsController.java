package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.IGetAssetTechnicalTypeIDs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetTechnicalTypeIDsController extends BaseController {
  
  @Autowired
  IGetAssetTechnicalTypeIDs getAssetTechnicalTypeIDs;
  
  @RequestMapping(value = "/assetinstances/getTechnicalTypeId", method = RequestMethod.POST)
  public IRESTModel getAssetTechnicalTypeId(@RequestBody IdsListParameterModel model)
      throws Exception
  {
    return createResponse(getAssetTechnicalTypeIDs.execute(model));
  }
}
