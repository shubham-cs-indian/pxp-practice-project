package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.DashboardTabModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.dashboardtabs.IGetOrCreateDashboardTabsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeDashboardTabsService implements IInitializeDashboardTabsService {
  
  @Autowired
  protected IGetOrCreateDashboardTabsStrategy           getOrCreateDashboardTabsStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeDashboardTabs();
    initializeDashboardTabTranslations();
  }
  
  private void initializeDashboardTabTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.DASHBOARD_TAB_TRANSLATIONS_JSON,
            CommonConstants.DASHBOARD_TAB);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeDashboardTabs() throws Exception
  {
    IListModel<IDashboardTabModel> dashboardTabList = new ListModel<IDashboardTabModel>();
    List<IDashboardTabModel> dashboardTabs = getDashboardTabs();
    dashboardTabList.setList(dashboardTabs);
    
    getOrCreateDashboardTabsStrategy.execute(dashboardTabList);
  }
  
  private List<IDashboardTabModel> getDashboardTabs() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.DASHBOARD_TABS_JSON);
    List<IDashboardTabModel> dashboardTabs = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<DashboardTabModel>>()
        {
        });
    stream.close();
    
    for (IDashboardTabModel dashboardTab : dashboardTabs) {
      ValidationUtils.validateDashboardTab(dashboardTab);
    }
    
    return dashboardTabs;
  }
}
