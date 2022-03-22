package com.cs.core.runtime.config.strategy.usecase.datatransfer;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.GetConfigDetailsToPrepareDataForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.IGetConfigDetailsToPrepareDataForLanguageInheritanceModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetConfigDetailsToPrepareDataForLanguageInheritanceStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsToPrepareDataForLanguageInheritanceStrategy {

  @Override
  public IGetConfigDetailsToPrepareDataForLanguageInheritanceModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_TO_PREPARE_DATA_FOR_LANGUAGE_INHERITANCE, model, GetConfigDetailsToPrepareDataForLanguageInheritanceModel.class);
  }
  
}
