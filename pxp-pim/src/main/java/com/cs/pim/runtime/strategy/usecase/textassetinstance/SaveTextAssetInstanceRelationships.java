package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISaveTextAssetInstanceRelationships;
import com.cs.pim.runtime.textassetinstance.ISaveTextAssetInstanceRelationshipsService;

@Service("saveTextAssetInstanceRelationships")
public class SaveTextAssetInstanceRelationships extends AbstractRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveTextAssetInstanceRelationships {

  @Autowired
  protected ISaveTextAssetInstanceRelationshipsService saveTextAssetInstanceRelationshipsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {

    return saveTextAssetInstanceRelationshipsService.execute(klassInstancesModel);
  }
}
