package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.templating.GetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetNumberOfVersionsToMaintainStrategy;

@Component
public class GetNumberOfVersionsToMaintainStrategy extends OrientDBBaseStrategy implements IGetNumberOfVersionsToMaintainStrategy {
  
 
  public IGetNumberOfVersionsToMaintainResponseModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_NUMBER_OF_VERSIONS_TO_MAINTAIN, model, GetNumberOfVersionsToMaintainResponseModel.class);
  }
}
