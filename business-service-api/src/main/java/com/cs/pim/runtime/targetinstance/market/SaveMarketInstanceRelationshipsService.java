package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveRelationshipInstances;

@Service
public class SaveMarketInstanceRelationshipsService
    extends AbstractSaveRelationshipInstances<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveMarketInstanceRelationshipsService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
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
