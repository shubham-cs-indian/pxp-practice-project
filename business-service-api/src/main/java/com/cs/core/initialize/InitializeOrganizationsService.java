package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.organization.OrganizationModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.organization.IGetOrCreateOrganizationsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeOrganizationsService implements IInitializeOrganizationsService {
  
  @Autowired
  protected IGetOrCreateOrganizationsStrategy           getOrCreateOrganizationsStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeOrganizations();
    initializeOrganizationTranslations();
  }
  
  private void initializeOrganizationTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.ORGANIZATION_TRANSLATIONS_JSON,
            CommonConstants.ORGANIZATION);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeOrganizations() throws Exception
  {
    IListModel<IOrganizationModel> organizationList = new ListModel<IOrganizationModel>();
    List<IOrganizationModel> organizations = getOrganizations();
    organizationList.setList(organizations);
    
    getOrCreateOrganizationsStrategy.execute(organizationList);
  }
  
  private List<IOrganizationModel> getOrganizations() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.ORGANISATIONS_JSON);
    List<IOrganizationModel> organizations = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<OrganizationModel>>()
        {
        });
    stream.close();
    
    for (IOrganizationModel organization : organizations) {
      ValidationUtils.validateOrganization(organization);
    }
    
    return organizations;
  }
}
