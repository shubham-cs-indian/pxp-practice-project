package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.system.CreateSystemModel;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.system.IGetOrCreateSystemsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeSystemsService implements IInitializeSystemsService {
  
  @Autowired
  protected IGetOrCreateSystemsStrategy                 getOrCreateSystemsStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeSystems();
    initializeSystemTranslations();
  }
  
  private void initializeSystemTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.SYSTEM_TRANSLATIONS_JSON,
            CommonConstants.SYSTEM);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeSystems() throws Exception
  {
    IListModel<ICreateSystemModel> systemList = new ListModel<ICreateSystemModel>();
    List<ICreateSystemModel> systems = getSystems();
    systemList.setList(systems);
    
    getOrCreateSystemsStrategy.execute(systemList);
  }
  
  private List<ICreateSystemModel> getSystems() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.SYSTEMS_JSON);
    
    List<ICreateSystemModel> systems = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<CreateSystemModel>>()
        {
        });
    stream.close();
    
    for (ICreateSystemModel system : systems) {
      ValidationUtils.validateSystem(system);
    }
    
    return systems;
  }
}
