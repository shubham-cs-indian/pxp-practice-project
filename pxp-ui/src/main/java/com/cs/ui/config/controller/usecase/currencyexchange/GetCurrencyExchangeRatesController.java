package com.cs.ui.config.controller.usecase.currencyexchange;

import com.cs.core.config.interactor.usecase.currencyexchange.IGetCurrencyExchangeRates;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetCurrencyExchangeRatesController extends BaseController {
  
  @Autowired
  protected IGetCurrencyExchangeRates getCurrencyExchangeRates;
  
  @RequestMapping(value = "/exchangerates", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getCurrencyExchangeRates.execute(null));
  }
}
