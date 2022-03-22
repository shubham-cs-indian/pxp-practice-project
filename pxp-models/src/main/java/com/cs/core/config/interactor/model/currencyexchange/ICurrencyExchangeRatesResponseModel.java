package com.cs.core.config.interactor.model.currencyexchange;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICurrencyExchangeRatesResponseModel extends IModel {
  
  public static final String EXCHANGE_RATES = "exchangeRates";
  
  public Map<String, Map<String, Double>> getExchangeRates();
  
  public void setExchangeRates(Map<String, Map<String, Double>> exchangeRates);
}
