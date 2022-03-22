package com.cs.ui.runtime.controller.usecase.assetinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.DeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.assetinstance.IDeleteAssetInstanceVariants;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class DeleteAssetInstanceVariantsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IDeleteAssetInstanceVariants deleteTechnicalImageVariant;
  
  @RequestMapping(value = "/assetvariantinstances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteVariantModel deleteVariantsModel) throws Exception
  {
    
    return createResponse(deleteTechnicalImageVariant.execute(deleteVariantsModel));
  }
  
}
