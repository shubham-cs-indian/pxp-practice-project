package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.configdetails.ConfigTaskInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllGovernanceTaskStrategy extends OrientDBBaseStrategy
    implements IGetAllGovernanceTaskStrategy {
  
  @Override
  public IListModel<IConfigTaskInformationModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ALL_GOVERNANCE_TASKS, model,
        new TypeReference<ListModel<ConfigTaskInformationModel>>()
        {
          
        });
  }
}
