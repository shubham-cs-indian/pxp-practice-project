package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.klassinstance.GetConflictSourcesInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;
import org.springframework.stereotype.Component;

@Component
public class GetConflictSourcesInformationConfigStrategy extends OrientDBBaseStrategy
    implements IGetConflictSourcesInformationConfigStrategy {
  
  @Override
  public IGetConflictSourcesInformationModel execute(IConflictSourcesRequestModel dataModel)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFLICT_SOURCES_INFORMATION, dataModel,
        GetConflictSourcesInformationModel.class);
  }
}
