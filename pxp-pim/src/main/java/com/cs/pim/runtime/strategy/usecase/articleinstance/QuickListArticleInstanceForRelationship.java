package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickListForRelationships;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IQuickListArticleInstanceForRelationship;
import org.springframework.stereotype.Service;

@Service
public class QuickListArticleInstanceForRelationship extends
    AbstractInstanceQuickListForRelationships<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel>
    implements IQuickListArticleInstanceForRelationship {
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IRelationshipInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException();
    }
  }
}
