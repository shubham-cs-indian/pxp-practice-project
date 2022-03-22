package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.GetLanguagesResponseModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetLanguagesStrategy extends OrientDBBaseStrategy implements IGetLanguagesStrategy {
  
  @Override
  public IGetLanguagesResponseModel execute(IGetLanguagesRequestModel model) throws Exception
  {
    return execute(GET_LANGUAGES, model, GetLanguagesResponseModel.class);
  }
}
