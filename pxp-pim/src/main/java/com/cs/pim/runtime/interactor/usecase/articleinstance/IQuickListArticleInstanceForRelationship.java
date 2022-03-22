package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IQuickListArticleInstanceForRelationship
    extends IRuntimeInteractor<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel> {
  
}
