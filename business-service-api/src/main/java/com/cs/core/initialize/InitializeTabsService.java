package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.CreateTabModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.tabs.IGetOrCreateTabsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeTabsService implements IInitializeTabsService {
  
  @Autowired
  protected IGetOrCreateTabsStrategy                    getOrCreateTabsStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeTabs();
    initializeTabTranslations();
  }
  
  private void initializeTabTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.TAB_TRANSLATIONS_JSON,
            CommonConstants.TAB);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeTabs() throws Exception
  {
    IListModel<ICreateTabModel> tabList = new ListModel<ICreateTabModel>();
    List<ICreateTabModel> tabs = getTabs();
    tabList.setList(tabs);
    getOrCreateTabsStrategy.execute(tabList);
  }
  
  private List<ICreateTabModel> getTabs() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.TABS_JSON);
    List<ICreateTabModel> tabs = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<CreateTabModel>>()
        {
        });
    stream.close();
    
    for (ICreateTabModel tab : tabs) {
      ValidationUtils.validateTab(tab);
    }
    
    return tabs;
  }
}
