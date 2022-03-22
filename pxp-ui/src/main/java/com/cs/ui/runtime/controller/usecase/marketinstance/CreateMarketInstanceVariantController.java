package com.cs.ui.runtime.controller.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.CreateVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.ICreateMarketInstanceVariant;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping("/runtime")
public class CreateMarketInstanceVariantController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateMarketInstanceVariant createMarketInstanceVariant;
  
  @RequestMapping(value = "/variants/market", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateVariantModel createVariantModel)
      throws Exception
  {
    return createResponse(createMarketInstanceVariant.execute(createVariantModel));
  }

}
