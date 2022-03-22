package com.cs.ui.runtime.controller.usecase.textassetinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.CreateVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.textassetinstance.ICreateTextAssetInstanceVariant;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class CreateTextAssetInstanceVariantController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateTextAssetInstanceVariant createTextAssetInstanceVariant;
  
  @RequestMapping(value = "/variants/textasset", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateVariantModel createVariantModel) throws Exception
  {
    return createResponse(createTextAssetInstanceVariant.execute(createVariantModel));
  }

}
