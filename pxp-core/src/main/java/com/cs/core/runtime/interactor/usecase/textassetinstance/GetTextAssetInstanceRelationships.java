package com.cs.core.runtime.interactor.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceRelationships extends AbstractRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetTextAssetInstanceRelationships {
  
  @Autowired
  protected IGetTextAssetInstanceRelationships               getTextAssetInstanceTypeStrategy;
  
  @Override
  protected IGetKlassInstanceRelationshipPaginationModel executeInternal(
      IGetKlassInstanceRelationshipsStrategyModel getKlassInstanceRelationshipsStrategyModel)
      throws Exception
  {
    return new GetKlassInstanceRelationshipPaginationModel(); 
  }

  
}
