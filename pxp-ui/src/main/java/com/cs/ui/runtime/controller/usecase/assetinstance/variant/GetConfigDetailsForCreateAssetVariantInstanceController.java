package com.cs.ui.runtime.controller.usecase.assetinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.variant.assetinstance.IGetConfigDetailsForCreateAssetVariantInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetConfigDetailsForCreateAssetVariantInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetConfigDetailsForCreateAssetVariantInstance getConfigDetailsForCreateAssetVariantInstance;
  
  @RequestMapping(value = "/assetvariantinstances/getconfigdata/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    return createResponse(
        getConfigDetailsForCreateAssetVariantInstance.execute(new IdParameterModel(id)));
  }
  
}
