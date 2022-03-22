package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.collections.ConfigDetailsForBookmarkModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForBookmarkStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForBookmarkStrategy {
  
  public static final String useCase = "GetConfigDetailsForBookmark";
  
  @Override
  public IConfigDetailsForBookmarkModel execute(IConfigDetailsForBookmarkRequestModel model)
      throws Exception
  {
    return execute(useCase, model, ConfigDetailsForBookmarkModel.class);
  }
}
