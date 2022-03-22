package com.cs.core.config.interactor.usecase.currencyexchange;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.currencyexchange.CurrencyExchangeRatesResponseModel;
import com.cs.core.config.interactor.model.currencyexchange.ICurrencyExchangeRatesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class GetCurrencyExchangeRates
    extends AbstractGetConfigInteractor<IModel, ICurrencyExchangeRatesResponseModel>
    implements IGetCurrencyExchangeRates {
  
  @Override
  public ICurrencyExchangeRatesResponseModel executeInternal(IModel dataModel) throws Exception
  {
    InputStream stream = GetCurrencyExchangeRates.class.getClassLoader()
        .getResourceAsStream("CurrencyExchangeRates.json");
    Reader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1);
    Map<String, Map<String, Double>> exchangeRates = ObjectMapperUtil.readValue(reader,
        new TypeReference<Map<String, Map<String, Double>>>()
        {
          
        });
    return new CurrencyExchangeRatesResponseModel(exchangeRates);
  }
}
