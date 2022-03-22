package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IGetTextAssetInstanceForCustomTab;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstanceForCustomTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceForCustomTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetTextAssetInstanceForCustomTab {
  
  @Autowired
  protected IGetTextAssetInstanceForCustomTabService getTextAssetInstanceForCustomTabService;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getTextAssetInstanceForCustomTabService.execute(getKlassInstanceTreeStrategyModel);
  }

}
