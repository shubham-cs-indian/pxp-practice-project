package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickListForRelationships;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IQuickListSupplierInstanceForRelationship;

@Service
public class QuickListSupplierInstanceForRelationship extends
    AbstractInstanceQuickListForRelationships<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel>
    implements IQuickListSupplierInstanceForRelationship {
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IRelationshipInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new SupplierKlassNotFoundException();
    }
  }
}
