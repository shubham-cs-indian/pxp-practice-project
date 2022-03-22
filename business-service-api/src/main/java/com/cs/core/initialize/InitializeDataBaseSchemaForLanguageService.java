package com.cs.core.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.config.interactor.model.language.UpdateSchemaOnLanguageCreateModel;
import com.cs.core.config.language.IGetAllLanguageCodesService;
import com.cs.core.config.strategy.usecase.language.IUpdateSchemaOnCreateLanguageStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class InitializeDataBaseSchemaForLanguageService implements IInitializeDataBaseSchemaForLanguageService {
  
  @Autowired
  protected IUpdateSchemaOnCreateLanguageStrategy updateSchemaOnCreateLanguageStrategy;
  
  @Autowired
  protected IGetAllLanguageCodesService           getAllLanguageCodesService;
  
  @Override
  public void execute() throws Exception
  {
    IIdsListParameterModel languagesCodeList = getAllLanguageCodesService.execute(null);
    for (String code : languagesCodeList.getIds()) {
      IUpdateSchemaOnLangaugeCreateModel updateSchemaOnLangaugeCreateTaxonomyModel = new UpdateSchemaOnLanguageCreateModel(code);
      updateSchemaOnCreateLanguageStrategy.execute(updateSchemaOnLangaugeCreateTaxonomyModel);
    }
  }
}
