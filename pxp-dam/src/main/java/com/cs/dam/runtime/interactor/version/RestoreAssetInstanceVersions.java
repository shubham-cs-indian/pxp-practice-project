package com.cs.dam.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractRestoreInstanceVersions;
import com.cs.dam.runtime.assetinstance.IRestoreAssetInstanceVersionsService;

@Service
public class RestoreAssetInstanceVersions extends AbstractRestoreInstanceVersions<IMoveKlassInstanceVersionsModel,IMoveKlassInstanceVersionsSuccessModel>
implements IRestoreAssetInstanceVersions {
  
  
  @Autowired
  IRestoreAssetInstanceVersionsService restoreAssetInstanceVersionsService;
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  { 
    return restoreAssetInstanceVersionsService.execute(dataModel);    
  }
  
}
