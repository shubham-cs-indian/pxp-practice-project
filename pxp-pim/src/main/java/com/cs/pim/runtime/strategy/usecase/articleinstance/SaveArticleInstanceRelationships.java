package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceRelationships;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceRelationshipsService;

@Service("saveArticleInstanceRelationships")
public class SaveArticleInstanceRelationships extends
    AbstractRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel> implements ISaveArticleInstanceRelationships {
  
  @Autowired
  ISaveArticleInstanceRelationshipsService saveArticleInstanceRelationshipsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {
    return saveArticleInstanceRelationshipsService.execute(klassInstancesModel);
  }
  
}
