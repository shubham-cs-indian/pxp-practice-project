package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.GetAllDataLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetAllDataLanguagesInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetAllDataLanguageInfoStrategy;

@Component
public class GetAllDataLanguageInfoStrategy extends OrientDBBaseStrategy
    implements IGetAllDataLanguageInfoStrategy {
  
  @Override
  public IGetAllDataLanguagesInfoModel execute(IGetAllDataLanguagesModel model) throws Exception
  {
    return execute(GET_ALL_DATA_LANGUAGE, model, GetAllDataLanguagesInfoModel.class);
  }
}
