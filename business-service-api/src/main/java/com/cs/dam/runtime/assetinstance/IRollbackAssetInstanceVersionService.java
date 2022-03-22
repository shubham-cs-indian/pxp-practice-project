package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public interface IRollbackAssetInstanceVersionService extends IRuntimeService<IKlassInstanceVersionRollbackModel,IGetKlassInstanceModel>{
  
}

