package com.cs.core.config.strategy.usecase.condition;

import com.cs.core.config.interactor.model.condition.ConditionInformationModel;
import com.cs.core.config.interactor.model.condition.IConditionInformationModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetConditionsStrategy extends OrientDBBaseStrategy
    implements IGetAllConditionsStrategy {
  
  public static final String useCase = "GetConditions";
  
  @Override
  public IListModel<IConditionInformationModel> execute(IConditionModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return super.execute(useCase, requestMap,
        new TypeReference<ListModel<ConditionInformationModel>>()
        {
          
        });
  }
}
