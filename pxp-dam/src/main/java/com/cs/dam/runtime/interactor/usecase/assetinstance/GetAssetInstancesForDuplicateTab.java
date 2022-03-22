package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceDuplicateTabResponseModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstancesForDuplicateTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("getAssetInstancesForDuplicateTab")
public class GetAssetInstancesForDuplicateTab extends
    AbstractRuntimeInteractor<IGetInstanceRequestModel, IAssetInstanceDuplicateTabResponseModel>
    implements IGetAssetInstancesForDuplicateTab {
  
  @Autowired
  protected IGetAssetInstancesForDuplicateTabService getAssetInstancesForDuplicateTabService;
  

  @Override
  protected IAssetInstanceDuplicateTabResponseModel executeInternal(IGetInstanceRequestModel model)
      throws Exception
  {
    return getAssetInstancesForDuplicateTabService.execute(model);
  }
  
}
