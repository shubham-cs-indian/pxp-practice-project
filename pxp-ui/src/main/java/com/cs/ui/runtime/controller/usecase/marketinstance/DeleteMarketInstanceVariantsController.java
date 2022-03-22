package com.cs.ui.runtime.controller.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.DeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IDeleteMarketInstanceVariants;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class DeleteMarketInstanceVariantsController extends BaseController implements IRuntimeController {
  
  @Autowired
  IDeleteMarketInstanceVariants deleteMarketInstanceVariants;
  
  @RequestMapping(value = "/marketvariantinstances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteVariantModel deleteVariantsModel) throws Exception
  {
    
    return createResponse(deleteMarketInstanceVariants.execute(deleteVariantsModel));
  }
  
}
