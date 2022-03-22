package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.GetChildLanguageCodeAgainstLanguageIdReturnModel;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.interactor.model.language.IGetChildLanguageCodeAgainstLanguageIdReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;


@Component
public class GetChildLanguageCodeVersusLanguageIdStrategy extends OrientDBBaseStrategy
    implements IGetChildLanguageCodeVersusLanguageIdStrategy{

  @Override
  public IGetChildLanguageCodeAgainstLanguageIdReturnModel execute(
      IDeleteLanguageRequestModel model) throws Exception
  {
    return execute(GET_CHILD_LANGUAGE_CODE_VERSUS_LANGUAGE_ID, model, GetChildLanguageCodeAgainstLanguageIdReturnModel.class);
  }
  
}
  
