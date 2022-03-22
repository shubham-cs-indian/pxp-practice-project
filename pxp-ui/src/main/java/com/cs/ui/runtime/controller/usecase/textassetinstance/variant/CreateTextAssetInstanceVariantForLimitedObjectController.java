package com.cs.ui.runtime.controller.usecase.textassetinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.CreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.usecase.variant.textassetinstance.ICreateTextAssetInstanceVariantForLimitedObject;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping("/runtime")
public class CreateTextAssetInstanceVariantForLimitedObjectController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateTextAssetInstanceVariantForLimitedObject createTextAssetInstanceVariantForLimitedObject;
  
  @RequestMapping(value = "/textasset/variant/limitedobject", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateVariantForLimitedObjectRequestModel createVariantModel) throws Exception
  {
    return createResponse(createTextAssetInstanceVariantForLimitedObject.execute(createVariantModel));
  }
  
}
