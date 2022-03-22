package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.klass.GetFilterInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import org.springframework.stereotype.Component;

@Component("getFilterAndSortDataForKlassStrategy")
public class OrientDBGetFilterAndSortDataForKlassStrategy extends OrientDBBaseStrategy
    implements IGetFilterAndSortDataStrategy {
  
  public static final String useCase = "GetFilterAndSortDataForKlass";
  
  @Override
  public IGetFilterInformationModel execute(IGetFilterAndSortDataRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetFilterInformationModel.class);
  }
}
