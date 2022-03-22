package com.cs.pim.runtime.interactor.usecase.targetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetTargetInstanceRelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTargetInstanceRelationships extends AbstractRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetTargetInstanceRelationships {
  
  @Autowired
  protected IGetTargetInstanceRelationshipsService getTargetInstanceRelationshipsService;

  @Override protected IGetKlassInstanceRelationshipPaginationModel executeInternal(IGetKlassInstanceRelationshipsStrategyModel model)
      throws Exception
  {
    return getTargetInstanceRelationshipsService.execute(model);
  }
}
