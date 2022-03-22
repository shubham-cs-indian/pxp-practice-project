package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractInstanceQuickListForRelationships;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.IQuickListMarketInstanceForRelationship;

@Service
public class QuickListMarketInstanceForRelationship extends
    AbstractInstanceQuickListForRelationships<IRelationshipInstanceQuickListModel, IGetKlassInstanceTreeModel>
    implements IQuickListMarketInstanceForRelationship {
  
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(
      IRelationshipInstanceQuickListModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException();
    }
  }
}
