package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.GetGridEditPropertiesResponseModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getAllGridEditablePropertiesStrategy")
public class GetAllGridEditablePropertiesStrategy extends OrientDBBaseStrategy
    implements IGetAllGridEditablePropertiesStrategy {
  
  @Override
  public IGetGridEditPropertiesResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ALL_GRID_EDITABLE_PROPERTIES, model,
        GetGridEditPropertiesResponseModel.class);
  }
}
