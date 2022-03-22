package com.cs.core.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;


public interface IGetAssetInstanceRelationships extends
    IRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel> {
  
}
