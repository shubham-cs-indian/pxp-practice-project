package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArticleInstanceRelationships extends
    IRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel> {
  
}
