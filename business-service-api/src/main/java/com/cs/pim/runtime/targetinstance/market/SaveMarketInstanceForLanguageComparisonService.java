package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

@Service
public class SaveMarketInstanceForLanguageComparisonService extends SaveMarketInstanceForTabsService
    implements ISaveMarketInstanceForLanguageComparisonService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IMarketInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException(e);
    }
    return response;
  }
}
