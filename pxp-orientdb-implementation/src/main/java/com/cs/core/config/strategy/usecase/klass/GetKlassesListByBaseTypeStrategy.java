package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetKlassesListByBaseTypeStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetKlassesListByBaseTypeStrategy extends OrientDBBaseStrategy
    implements IGetKlassesListByBaseTypeStrategy {
  
  @Override
  public IGetConfigDataEntityResponseModel execute(IGetKlassesByBaseTypeModel model)
      throws Exception
  {
    return execute(GET_KLASSES_LIST_BY_BASE_TYPE, model, GetConfigDataEntityResponseModel.class);
  }
}
