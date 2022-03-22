package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.BulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.usecase.variant.targetinstance.market.IBulkSaveMarketInstanceVariants;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class BulkSaveMarketInstanceVariantsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IBulkSaveMarketInstanceVariants bulkSaveMarketInstanceVariants;
  
  @RequestMapping(value = "/marketinstance/variants/bulksave", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody BulkSaveInstanceVariantsModel klassInstanceModels)
      throws Exception
  {
    return createResponse(bulkSaveMarketInstanceVariants.execute(klassInstanceModels));
  }
}
