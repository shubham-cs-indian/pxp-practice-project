package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.ISaveMarketInstanceRelationshipsService;

@Service("saveMarketInstanceRelationships")
public class SaveMarketInstanceRelationships extends AbstractRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveMarketInstanceRelationships {
  
  @Autowired
  ISaveMarketInstanceRelationshipsService saveMarketInstanceRelationshipsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {
    return saveMarketInstanceRelationshipsService.execute(klassInstancesModel);
  }
}
