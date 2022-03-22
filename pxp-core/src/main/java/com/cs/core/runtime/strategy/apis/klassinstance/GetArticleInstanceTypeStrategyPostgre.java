package com.cs.core.runtime.strategy.apis.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import org.springframework.stereotype.Component;

@Component("getArticleInstanceTypeStrategyPostgre")
public class GetArticleInstanceTypeStrategyPostgre implements IGetKlassInstanceTypeStrategy {
  
  @Override
  public IKlassInstanceTypeModel execute(IIdParameterModel model) throws Exception
  {
    return new KlassInstanceTypeModel();
  }
}
