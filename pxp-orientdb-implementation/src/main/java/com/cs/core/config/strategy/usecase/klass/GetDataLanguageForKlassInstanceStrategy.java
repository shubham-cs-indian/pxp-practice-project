package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetDataLanguageForKlassInstanceStrategy;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetDataLanguageForKlassInstanceStrategy extends OrientDBBaseStrategy
    implements IGetDataLanguageForKlassInstanceStrategy {
  
  
  @Override
  public IListModel<IGetLanguagesInfoModel> execute(IGetAllDataLanguagesModel model)
      throws Exception
  {
    return execute(GET_DATA_LANGUAGE_FOR_KLASS_INSTANCE, model, new TypeReference<ListModel<GetLanguagesInfoModel>>(){});
  }
}
