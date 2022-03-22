package com.cs.core.config.strategy.usecase.klass.gridview;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.gridcontent.ConfigDetailsForContentGridViewModel;
import com.cs.core.runtime.interactor.model.gridcontent.IConfigDetailsForContentGridViewModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetKlassConfigRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForContentGridViewStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForContentGridViewStrategy {
  
  @Override
  public IConfigDetailsForContentGridViewModel execute(IGetKlassConfigRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_CONTENT_GRID_VIEW, model,
        ConfigDetailsForContentGridViewModel.class);
  }
}
