package com.cs.core.config.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.ConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

@Component
public class GetConfigDetailsForTypeInfoStrategy extends OrientDBBaseStrategy
  implements IGetConfigDetailsForTypeInfoStrategy {
  
  @Override
  public IConfigDetailsForTypeInfoModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_TYPE_INFO, model, ConfigDetailsForTypeInfoModel.class);
  }
}
