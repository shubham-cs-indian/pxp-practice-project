package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetKlassesListByNatureTypeStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetKlassesListByNatureTypeStrategy extends OrientDBBaseStrategy
    implements IGetKlassesListByNatureTypeStrategy {
  
  @Override
  public IGetConfigDataEntityResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_KLASSES_LIST_BY_NATURE_TYPE, model, GetConfigDataEntityResponseModel.class);
  }
}
