package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetAllLanguageCodesStrategy extends OrientDBBaseStrategy
    implements IGetAllLanguageCodesStrategy {
  
  @Override
  public IIdsListParameterModel execute(IModel model) throws Exception
  {
    return execute(GET_ALL_LANGUAGE_CODES, model, IdsListParameterModel.class);
  }
}
