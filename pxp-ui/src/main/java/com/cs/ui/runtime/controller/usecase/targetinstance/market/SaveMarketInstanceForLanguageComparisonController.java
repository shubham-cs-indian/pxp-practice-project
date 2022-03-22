package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.targetinstance.MarketInstanceSaveModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.ISaveMarketInstanceForLanguageComparison;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveMarketInstanceForLanguageComparisonController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  public ISaveMarketInstanceForLanguageComparison saveMarketInstanceForLanguageComparison;
  
  @RequestMapping(value = "/marketinstances/languagecomparison", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody MarketInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    return createResponse(saveMarketInstanceForLanguageComparison.execute(klassInstanceModels));
  }
}
