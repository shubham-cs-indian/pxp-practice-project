package com.cs.core.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceRelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceRelationships extends AbstractRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetAssetInstanceRelationships {
 
  @Autowired
  protected IGetAssetInstanceRelationshipsService getAssetInstanceRelationshipsService;

  @Override protected IGetKlassInstanceRelationshipPaginationModel executeInternal(IGetKlassInstanceRelationshipsStrategyModel model)
      throws Exception
  {
    return getAssetInstanceRelationshipsService.execute(model);
  }
}
