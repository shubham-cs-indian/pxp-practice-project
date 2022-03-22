package com.cs.dam.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.core.runtime.interactor.version.AbstractRollbackInstanceVersion;
import com.cs.dam.runtime.assetinstance.IRollbackAssetInstanceVersionService;

@Service
public class RollbackAssetInstanceVersion extends
    AbstractRollbackInstanceVersion<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel>
    implements IRollbackAssetInstanceVersion {
   
  @Autowired
  protected IRollbackAssetInstanceVersionService rollbackAssetInstanceVersionService;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return rollbackAssetInstanceVersionService.execute(dataModel);
  }
  
}
