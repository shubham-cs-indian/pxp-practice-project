package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.stereotype.Service;
import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickListForRelationships;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IQuickListTextAssetInstanceForRelationship;


@Service
public class QuickListTextAssetInstanceForRelationship extends
    AbstractInstanceQuickListForRelationships<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel>
    implements IQuickListTextAssetInstanceForRelationship {
  
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IRelationshipInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException();
    }
  }
}  
