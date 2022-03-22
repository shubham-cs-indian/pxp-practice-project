package com.cs.core.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.AbstractGetInstanceForOnboarding;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;

@Component
public class GetAssetInstanceForOnboarding extends
    AbstractGetInstanceForOnboarding<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetAssetInstanceForOnboarding {
  
  @Autowired
  protected IGetAssetInstanceForCustomTab               getAssetInstanceForCustomTab;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy               getAssetInstanceTypeStrategy;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;

  @Override
  protected IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModelForCustomTab getKlassInstanceTreeStrategyModel)
      throws Exception
  {
    return getAssetInstanceForCustomTab.execute(getKlassInstanceTreeStrategyModel);
  }

  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception
  {
    return getConfigDetailsWithoutPermissionsStrategy.execute(idParameterModel);
  }

  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    return getAssetInstanceTypeStrategy.execute(new IdParameterModel(id));
  }
  
}
