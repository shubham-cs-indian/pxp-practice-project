package com.cs.dam.runtime.interactor.usecase.assetinstance;


import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IDeleteAssetInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;


@Service
public class DeleteAssetInstance extends AbstractRuntimeInteractor<IDeleteKlassInstanceRequestModel, IDeleteKlassInstanceResponseModel>
    implements IDeleteAssetInstance {

  @Autowired
  protected IDeleteAssetInstanceService deleteAssetInstanceService;
  
  @Override
  protected IDeleteKlassInstanceResponseModel executeInternal(IDeleteKlassInstanceRequestModel deleteAssetRequestModel) throws Exception
  {
    return deleteAssetInstanceService.execute(deleteAssetRequestModel);
  }
}
