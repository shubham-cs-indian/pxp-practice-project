package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.ISaveAssetInstanceRelationshipsService;

@Service("saveAssetInstanceRelationships")
public class SaveAssetInstanceRelationships extends
    AbstractRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel> implements ISaveAssetInstanceRelationships {
  
  @Autowired
  protected ISaveAssetInstanceRelationshipsService saveAssetInstanceRelationshipsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {
    return saveAssetInstanceRelationshipsService.execute(klassInstancesModel);
  }
  
}
