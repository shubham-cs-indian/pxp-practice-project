package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IQuickListSupplierInstanceForRelationship
    extends IRuntimeInteractor<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel> {
  
}
