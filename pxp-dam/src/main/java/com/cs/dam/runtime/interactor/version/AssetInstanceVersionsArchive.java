package com.cs.dam.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractDeleteArticleInstanceVersion;
import com.cs.dam.runtime.assetinstance.IAssetInstanceVersionsArchiveService;

@Service
public class AssetInstanceVersionsArchive extends
    AbstractDeleteArticleInstanceVersion<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IAssetInstanceVersionsArchive {
  
  @Autowired 
  protected IAssetInstanceVersionsArchiveService assetInstanceVersionsArchiveService; 
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel execute(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  {
    return assetInstanceVersionsArchiveService.execute(dataModel);
  }
  
}
