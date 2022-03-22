package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceForLanguageComparisonRequestModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.IGetMarketInstanceForLanguageComparison;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetMarketInstanceForLanguageComparisonController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetMarketInstanceForLanguageComparison getMarketInstanceForLanguageComparison;
  
  @RequestMapping(value = "/marketinstance/language/comparison", method = RequestMethod.POST)
  public IRESTModel execute(
      @RequestBody GetInstanceForLanguageComparisonRequestModel klassInstanceModels)
      throws Exception
  {
    return createResponse(getMarketInstanceForLanguageComparison.execute(klassInstanceModels));
  }
}