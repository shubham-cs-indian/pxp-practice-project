package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveRelationshipInstances;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

@Service
public class SaveArticleInstanceRelationshipsService
    extends AbstractSaveRelationshipInstances<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceRelationshipsService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }
  
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEARTIKLERELATIONSHIP;
  }
}
