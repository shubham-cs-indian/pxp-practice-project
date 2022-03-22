package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IGetTextAssetInstanceForOverviewTab;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstanceForOverviewTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetTextAssetInstanceForOverviewTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetTextAssetInstanceForOverviewTab {
  
  @Autowired
  protected IGetTextAssetInstanceForOverviewTabService getTextAssetInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getTextAssetInstanceForOverviewTabService.execute(getKlassInstanceTreeStrategyModel);
  }

}
