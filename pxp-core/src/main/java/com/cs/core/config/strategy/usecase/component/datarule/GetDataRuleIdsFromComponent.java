package com.cs.core.config.strategy.usecase.component.datarule;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.component.IGetRuleIdsFromComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("getDataRuleIdsFromComponent")
public class GetDataRuleIdsFromComponent extends OrientDBBaseStrategy
    implements IGetRuleIdsFromComponent {
  
  public static final String useCase = "GetRuleIdsFromComponent";
  
  @Autowired
  ISessionContext            context;
  
  @Override
  @SuppressWarnings("unchecked")
  public IIdsListParameterModel execute(IIdParameterModel model) throws Exception
  {
    return super.execute(useCase, model, IdsListParameterModel.class);
  }
}
