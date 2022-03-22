package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

public interface IAssetInstanceVersionsArchiveService extends
    IRuntimeService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> {
  
}
