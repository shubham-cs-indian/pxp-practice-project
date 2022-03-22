package com.cs.core.runtime.interactor.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
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
public class GetArticleInstanceForOnboarding extends
    AbstractGetInstanceForOnboarding<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetArticleInstanceForOnboarding {
  
  @Autowired
  protected IGetArticleInstanceForCustomTab             getArticleInstanceForCustomTab;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy               getArticleInstanceTypeStrategy;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModelForCustomTab getKlassInstanceTreeStrategyModel)
      throws Exception
  {
    return getArticleInstanceForCustomTab.execute(getKlassInstanceTreeStrategyModel);
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
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return getArticleInstanceTypeStrategy.execute(idParameterModel);
  }
}
