package com.cs.core.runtime.klassinstance;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCreateKlassInstanceCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateKlassInstanceCloneModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCreateKlassInstanceCloneStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForCreateKlassInstanceCloneStrategy extends OrientDBBaseStrategy implements 
  IGetConfigDetailsForCreateKlassInstanceCloneStrategy {
 
  @Override
  public IGetConfigDetailsForCreateKlassInstanceCloneModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_CREATE_KLASS_INSTANCE_CLONE, model, GetConfigDetailsForCreateKlassInstanceCloneModel.class);
  }
}
