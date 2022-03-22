package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IQuickListAssetInstanceForRelationship
    extends IRuntimeInteractor<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel> {
  
}
