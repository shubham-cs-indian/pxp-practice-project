package com.cs.pim.runtime.interactor.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.textassetinstance.IGetArchivedTextAssetInstanceForVersionTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArchivedTextAssetInstanceForVersionTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArchivedTextAssetInstanceForVersionTab {
  
  @Autowired
  protected IGetArchivedTextAssetInstanceForVersionTabService getArchivedTextAssetInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    
    return getArchivedTextAssetInstanceForVersionTabService.execute(dataModel);
  }
}
