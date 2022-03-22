package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveAssetInstanceRelationships
    extends IRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel> {
  
}
