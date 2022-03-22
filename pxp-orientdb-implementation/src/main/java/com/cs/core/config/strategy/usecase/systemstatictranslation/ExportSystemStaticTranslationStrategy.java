package com.cs.core.config.strategy.usecase.systemstatictranslation;

import com.cs.core.config.interactor.model.hidden.GetTranslationsResponseHiddenModel;
import com.cs.core.config.interactor.model.hidden.IGetTranslationsResponseHiddenModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class ExportSystemStaticTranslationStrategy extends OrientDBBaseStrategy
    implements IExportSystemStaticTranslationStrategy {
  
  @Override
  public IGetTranslationsResponseHiddenModel execute(IGetTranslationsRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.EXPORT_SYSTEM_STATIC_TRANSLATION, model,
        GetTranslationsResponseHiddenModel.class);
  }
}
