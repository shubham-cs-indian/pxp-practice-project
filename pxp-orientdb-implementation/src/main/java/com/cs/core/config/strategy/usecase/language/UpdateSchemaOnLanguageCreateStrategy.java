package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class UpdateSchemaOnLanguageCreateStrategy extends OrientDBBaseStrategy
    implements IUpdateSchemaOnCreateLanguageStrategy {
  
  @Override
  public IIdsListParameterModel execute(IUpdateSchemaOnLangaugeCreateModel model) throws Exception
  {
    return execute(UPDATE_SCHEMA_ON_CREATE_LANGUAGE, model, IdsListParameterModel.class);
  }
}
