package com.cs.core.config.strategy.usecase.state;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.StateInformationModel;
import com.cs.core.config.interactor.model.state.IStateInformationModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetStatesStrategy extends OrientDBBaseStrategy
    implements IGetAllStatesStrategy {
  
  public static final String useCase = "GetStates";
  
  @Override
  public IListModel<IStateInformationModel> execute(IStateModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<StateInformationModel>>()
    {
      
    });
  }
}
