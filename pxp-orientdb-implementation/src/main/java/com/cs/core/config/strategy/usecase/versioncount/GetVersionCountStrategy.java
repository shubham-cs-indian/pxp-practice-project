package com.cs.core.config.strategy.usecase.versioncount;

import com.cs.core.config.interactor.model.version.IVersionCountModel;
import com.cs.core.config.interactor.model.version.VersionCountModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetVersionCountStrategy extends OrientDBBaseStrategy
    implements IGetVersionCountStrategy {
  
  @Override
  public IVersionCountModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_VERSION_COUNT, model, VersionCountModel.class);
  }
}
