package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceForOverviewTab;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceForOverviewTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceForOverviewTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetAssetInstanceForOverviewTab {

  @Autowired
  protected IGetAssetInstanceForOverviewTabService getAssetInstanceForOverviewTabService;

  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getAssetInstanceForOverviewTabService.execute(getKlassInstanceTreeStrategyModel);
  }
}
