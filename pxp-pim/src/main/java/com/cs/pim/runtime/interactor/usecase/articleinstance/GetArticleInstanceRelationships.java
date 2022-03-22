package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceRelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceRelationships extends AbstractRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetArticleInstanceRelationships {

  @Autowired
  protected IGetArticleInstanceRelationshipsService getArticleInstanceRelationshipsService;

  @Override
  protected IGetKlassInstanceRelationshipPaginationModel executeInternal(IGetKlassInstanceRelationshipsStrategyModel model)
      throws Exception
  {
    return getArticleInstanceRelationshipsService.execute(model);
  }
}
