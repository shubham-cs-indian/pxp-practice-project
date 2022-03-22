package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.abstrct.AbstractCreateTranslatableInstanceService;
import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTranslatableMarketInstanceService
    extends AbstractCreateTranslatableInstanceService<IMarketInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableMarketInstanceService {
  
  @Autowired
  protected Long marketKlassCounter;
  
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
  
  @Override
  protected Long getCounter()
  {
    return marketKlassCounter++;
  }
}
