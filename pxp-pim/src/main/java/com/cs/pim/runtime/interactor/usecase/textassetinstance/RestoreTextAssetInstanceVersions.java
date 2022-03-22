package com.cs.pim.runtime.interactor.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractRestoreInstanceVersions;
import com.cs.pim.runtime.textassetinstance.IRestoreTextAssetInstanceVersionsService;

@Service
public class RestoreTextAssetInstanceVersions
    extends AbstractRestoreInstanceVersions<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreTextAssetInstanceVersions {
  
  @Autowired
  IRestoreTextAssetInstanceVersionsService restoreTextAssetInstanceVersionsService;
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return restoreTextAssetInstanceVersionsService.execute(dataModel);
  }
}