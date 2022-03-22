package com.cs.core.config.interactor.model.currencyexchange;

import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeRatesResponseModel implements ICurrencyExchangeRatesResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  protected Map<String, Map<String, Double>> exchangeRates    = new HashMap<>();
  
  public CurrencyExchangeRatesResponseModel(Map<String, Map<String, Double>> exchangeRates)
  {
    this.exchangeRates = exchangeRates;
  }
  
  @Override
  public Map<String, Map<String, Double>> getExchangeRates()
  {
    return exchangeRates;
  }
  
  @Override
  public void setExchangeRates(Map<String, Map<String, Double>> exchangeRates)
  {
    this.exchangeRates = exchangeRates;
  }
}
