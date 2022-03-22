package com.cs.dam.runtime.interactor.version;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceForVersionTab
    extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetAssetInstanceForVersionTab {
  
  @Autowired
  protected IGetAssetInstanceForVersionTabService getAssetInstanceForVersionTabService;
  
  @Override
  protected IGetKlassInstanceVersionsForTimeLineModel executeInternal(IGetInstanceRequestModel model) throws Exception
  {
    return getAssetInstanceForVersionTabService.execute(model);
  }
  
}
