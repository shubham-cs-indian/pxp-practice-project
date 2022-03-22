package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.config.interactor.model.language.UpdateSchemaOnLanguageCreateModel;
import com.cs.core.config.strategy.usecase.language.ICreateLanguageStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateLanguageService extends AbstractCreateConfigService<ICreateLanguageModel, IGetLanguageModel>
    implements ICreateLanguageService {
  
  @Autowired
  protected ICreateLanguageStrategy createLanguageStrategy;

  @Autowired
  protected LanguageValidations languageValidations;

  @Override
  public IGetLanguageModel executeInternal(ICreateLanguageModel dataModel) throws Exception
  {
    String id = dataModel.getId();
    if (id == null || id.isEmpty()) {
      id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.LOCALE.getPrefix());
      dataModel.setId(id);
    }
    languageValidations.validateLanguageCreation(dataModel);
    ConfigurationDAO.instance().createLanguageConfig(dataModel.getCode(), dataModel.getParentId());
    IGetLanguageModel returnModel = createLanguageStrategy.execute(dataModel);
    
    updateConfigurationSchema(returnModel);
    return returnModel;
  }
  
  private void updateConfigurationSchema(IGetLanguageModel returnModel) throws Exception
  {
    String code = returnModel.getEntity().getCode();
    IUpdateSchemaOnLangaugeCreateModel updateSchemaOnLangaugeCreateModel = new UpdateSchemaOnLanguageCreateModel(code);
    
    // TODO: BGP
  }
}
