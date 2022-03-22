package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.role.IGetOrCreateRolesStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class InitializeRolesService implements IInitializeRolesService {
  
  @Autowired
  protected IGetOrCreateRolesStrategy                   getOrCreateRolesStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeRoles();
    initializeRoleTranslations();
  }
  
  private void initializeRoleTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.ROLE_TRANSLATIONS_JSON,
            CommonConstants.ROLE);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeRoles()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.ROLES_JSON);
    List<IRole> roleList = ObjectMapperUtil.readValue(stream, new TypeReference<List<IRole>>()
    {
    });
    stream.close();
    
    for (IRole role : roleList) {
      ValidationUtils.validateRole(role);
    }
    
    IListModel<IRole> roles = new ListModel<>();
    roles.setList(roleList);
    
    getOrCreateRolesStrategy.execute(roles);
  }
}
