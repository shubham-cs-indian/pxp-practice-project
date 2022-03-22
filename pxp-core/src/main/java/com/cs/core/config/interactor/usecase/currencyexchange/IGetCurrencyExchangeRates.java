package com.cs.core.config.interactor.usecase.currencyexchange;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.currencyexchange.ICurrencyExchangeRatesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCurrencyExchangeRates
    extends IGetConfigInteractor<IModel, ICurrencyExchangeRatesResponseModel> {
  
}
