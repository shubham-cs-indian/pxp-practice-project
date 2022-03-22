package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickListForRelationships;
import org.springframework.stereotype.Service;

@Service
public class QuickListAssetInstanceForRelationship extends
    AbstractInstanceQuickListForRelationships<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel>
    implements IQuickListAssetInstanceForRelationship {
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IRelationshipInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException();
    }
  }
}
