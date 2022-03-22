package com.cs.dam.runtime.interactor.version;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetArchivedAssetInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArchivedAssetInstanceForVersionTab
    extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArchivedAssetInstanceForVersionTab {
  
  @Autowired
  protected IGetArchivedAssetInstanceForVersionTabService getArchivedAssetInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(IGetInstanceRequestModel dataModel) throws Exception
  {
    return getArchivedAssetInstanceForVersionTabService.execute(dataModel);
  }
  
}
