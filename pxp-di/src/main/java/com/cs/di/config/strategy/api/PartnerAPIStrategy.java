package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.config.interactor.model.organization.OrganizationModel;
import com.cs.core.config.interactor.model.organization.SaveOrganizationModel;
import com.cs.core.config.interactor.model.role.CreateRoleModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.interactor.model.role.RoleSaveModel;
import com.cs.core.config.interactor.usecase.organization.ICreateOrganization;
import com.cs.core.config.interactor.usecase.organization.IGetOrganization;
import com.cs.core.config.interactor.usecase.organization.ISaveOrganization;
import com.cs.core.config.interactor.usecase.role.ICreateRole;
import com.cs.core.config.interactor.usecase.role.IGetRole;
import com.cs.core.config.interactor.usecase.role.ISaveRole;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PartnerAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  IGetOrganization           getOrganization;
  
  @Autowired
  IGetRole                   getRole;
  
  @Autowired
  ICreateOrganization        createOrganization;
  
  @Autowired
  ISaveOrganization          saveOrganization;
  
  @Autowired
  ICreateRole                createRole;
  
  @Autowired
  ISaveRole                  saveRole;
  
  public static final String EMPTY_STRING   = "";
  public static final String ENTITY_MAP     = "entity";
  public static final String ENTITY_PARTNER = "Partner";
  public static final String ADDED_USERS    = "addedUsers";
  public static final String DELETED_USERS  = "deletedUsers";
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    Map<String, Object> inputData = mapper.readValue(configModel.getData(), new TypeReference<Map<String, Object>>()
    {
    });
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    if (((String) inputData.get(ENTITY_MAP)).equalsIgnoreCase(ENTITY_PARTNER)) {
      return getOrganization.execute(getEntityModel);
    }
    else {
      return getRole.execute(getEntityModel);
    }
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    if (!inputData.containsKey(CommonConstants.CODE_PROPERTY)) {
      inputData.put(CommonConstants.CODE_PROPERTY, EMPTY_STRING);
    }
    
    if (((String) inputData.get(ENTITY_MAP)).equalsIgnoreCase(ENTITY_PARTNER)) {
      IOrganizationModel organizationModel = mapper.convertValue(inputData, OrganizationModel.class);
      return createOrganization.execute(organizationModel);
    }
    else {
      // role creation
      ICreateRoleModel roleModel = new CreateRoleModel();
      roleModel.setCode((String) inputData.get(CommonConstants.CODE_PROPERTY));
      roleModel.setLabel((String) inputData.get(CommonConstants.LABEL_PROPERTY));
      roleModel.setEntities(new ArrayList<String>());
      roleModel.setOrganizationId((String) inputData.get(CreateRoleModel.ORGANIZATION_ID));
      roleModel.setIsMultiselect(true);
      roleModel.setPlaceholder(EMPTY_STRING);
      roleModel.setTooltip(EMPTY_STRING);
      roleModel.setDescription(EMPTY_STRING);
      roleModel.setIsStandard(false);
      return createRole.execute(roleModel);
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    Map<String, Object> entityMap;
    if (((String) inputData.get(ENTITY_MAP)).equalsIgnoreCase(ENTITY_PARTNER)) {
      entityMap = (Map<String, Object>) getData.get(CommonConstants.ORGANIZATION);
      ISaveOrganizationModel saveOrganizationModel = new SaveOrganizationModel();
      saveOrganizationModel.setLabel((String) updateEntity(inputData, entityMap, CommonConstants.LABEL_PROPERTY));
      saveOrganizationModel.setIcon((String) updateEntity(inputData, entityMap, CommonConstants.ICON_PROPERTY));
      saveOrganizationModel.setPhysicalCatalogs((List<String>) updateEntity(inputData, entityMap, IOrganizationModel.PHYSICAL_CATALOGS));
      saveOrganizationModel.setPortals((List<String>) updateEntity(inputData, entityMap, IOrganizationModel.PORTALS));
      if (inputData.containsKey(SaveOrganizationModel.ADDED_TAXONOMY_IDS)) {
        saveOrganizationModel.setAddedTaxonomyIds(
            validateUserInputs(inputData, SaveOrganizationModel.ADDED_TAXONOMY_IDS, entityMap, SaveOrganizationModel.TAXONOMY_IDS));
      }
      if (inputData.containsKey(SaveOrganizationModel.DELETED_TAXONOMY_IDS)) {
        saveOrganizationModel.setDeletedTaxonomyIds((List<String>) inputData.get((SaveOrganizationModel.DELETED_TAXONOMY_IDS)));
      }
      if (inputData.containsKey(SaveOrganizationModel.ADDED_KLASS_IDS)) {
        saveOrganizationModel.setAddedKlassIds(
            validateUserInputs(inputData, SaveOrganizationModel.ADDED_KLASS_IDS, entityMap, SaveOrganizationModel.KLASS_IDS));
      }
      if (inputData.containsKey(SaveOrganizationModel.DELETED_KLASS_IDS)) {
        saveOrganizationModel.setDeletedKlassIds((List<String>) inputData.get((SaveOrganizationModel.DELETED_KLASS_IDS)));
      }
      if (inputData.containsKey(SaveOrganizationModel.ADDED_SYSTEM_IDS)) {
        saveOrganizationModel.setaddedSystemIds((List<String>) inputData.get((SaveOrganizationModel.ADDED_SYSTEM_IDS)));
      }
      if (inputData.containsKey(SaveOrganizationModel.DELETED_SYSTEM_IDS)) {
        saveOrganizationModel.setDeletedSystemIds((List<String>) inputData.get((SaveOrganizationModel.DELETED_SYSTEM_IDS)));
      }
      if (inputData.containsKey(SaveOrganizationModel.ADDED_ENDPOINT_IDS)) {
        saveOrganizationModel.getAddedEndpointIds(((List<String>) inputData.get((SaveOrganizationModel.ADDED_ENDPOINT_IDS))));
      }
      if (inputData.containsKey(SaveOrganizationModel.DELETED_ENDPOINT_IDS)) {
        saveOrganizationModel.setDeletedEndpointIds((List<String>) inputData.get((SaveOrganizationModel.DELETED_ENDPOINT_IDS)));
      }
      saveOrganizationModel.setType((String) entityMap.get(CommonConstants.TYPE_PROPERTY));
      saveOrganizationModel.setCode((String) entityMap.get(CommonConstants.CODE_PROPERTY));
      saveOrganizationModel.setId((String) entityMap.get(CommonConstants.ID_PROPERTY));
      return saveOrganization.execute(saveOrganizationModel);
    }
    else {
      entityMap = (Map<String, Object>) getData.get(CommonConstants.ROLE);
      Map<String, Object> getOrganizationDetails = convertToMap(
          fetchOrganizationDetails((String) inputData.get(CreateRoleModel.ORGANIZATION_ID), configModel));
      Map<String, Object> organizationDetails = (Map<String, Object>) getOrganizationDetails.get(CommonConstants.ORGANIZATION);
      IRoleSaveModel roleSaveModel = new RoleSaveModel();
      roleSaveModel.setLabel((String) updateEntity(inputData, entityMap, CommonConstants.LABEL_PROPERTY));
      roleSaveModel.setPhysicalCatalogs((List<String>) updateEntity(inputData, entityMap, IOrganizationModel.PHYSICAL_CATALOGS));
      roleSaveModel.setPortals((List<String>) updateEntity(inputData, entityMap, IOrganizationModel.PORTALS));
      roleSaveModel.setIsDashboardEnable((Boolean) updateEntity(inputData, entityMap, IRole.IS_DASHBOARD_ENABLE));
      roleSaveModel.setEntities((List<String>) updateEntity(inputData, entityMap, IRole.ENTITIES));
      if (inputData.containsKey(RoleSaveModel.ADDED_TARGET_TAXONOMIES)) {
        roleSaveModel.setAddedTargetTaxonomies(
            validateUserInputsForRole(checkForAllowedInputsForRole(inputData, RoleSaveModel.ADDED_TARGET_TAXONOMIES, organizationDetails,
                SaveOrganizationModel.TAXONOMY_IDS), entityMap, RoleSaveModel.TARGET_TAXONOMIES));
      }
      if (inputData.containsKey(RoleSaveModel.DELETED_TARGET_TAXONOMIES)) {
        roleSaveModel.setDeletedTargetTaxonomies((List<String>) inputData.get((RoleSaveModel.DELETED_TARGET_TAXONOMIES)));
      }
      if (inputData.containsKey(RoleSaveModel.ADDED_TARGET_KLASSES)) {
        roleSaveModel
            .setAddedTargetKlasses(validateUserInputsForRole(checkForAllowedInputsForRole(inputData, RoleSaveModel.ADDED_TARGET_KLASSES,
                organizationDetails, SaveOrganizationModel.KLASS_IDS), entityMap, RoleSaveModel.TARGET_KLASSES));
      }
      if (inputData.containsKey(RoleSaveModel.DELETED_TARGET_KLASSES)) {
        roleSaveModel.setDeletedTargetKlasses((List<String>) inputData.get((RoleSaveModel.DELETED_TARGET_KLASSES)));
      }
      if (inputData.containsKey(RoleSaveModel.ADDED_ENDPOINTS)) {
        roleSaveModel.setAddedEndpoints(((List<String>) inputData.get((RoleSaveModel.ADDED_ENDPOINTS))));
      }
      else {
        roleSaveModel.setAddedEndpoints(new ArrayList<String>());
      }
      if (inputData.containsKey(RoleSaveModel.DELETED_ENDPOINTS)) {
        roleSaveModel.setDeletedEndpoints((List<String>) inputData.get((RoleSaveModel.DELETED_ENDPOINTS)));
      }
      else {
        roleSaveModel.setDeletedEndpoints(new ArrayList<String>());
      }
      if (inputData.containsKey(RoleSaveModel.ADDED_SYSTEM_IDS)) {
        roleSaveModel.setaddedSystemIds((List<String>) inputData.get((RoleSaveModel.ADDED_SYSTEM_IDS)));
      }
      if (inputData.containsKey(RoleSaveModel.DELETED_SYSTEM_IDS)) {
        roleSaveModel.setDeletedSystemIds((List<String>) inputData.get((RoleSaveModel.DELETED_SYSTEM_IDS)));
      }
      if (inputData.containsKey(ADDED_USERS)) {
        roleSaveModel.setAddedUsers((List<String>) inputData.get(ADDED_USERS));
      }
      if (inputData.containsKey(DELETED_USERS)) {
        roleSaveModel.setDeletedUsers((List<String>) inputData.get(DELETED_USERS));
      }
      if (inputData.containsKey(RoleSaveModel.ADDED_KPIS)) {
        roleSaveModel.setddedKPIs(validateUserInputs(inputData, RoleSaveModel.ADDED_KPIS, entityMap, RoleSaveModel.KPIS));
      }
      if (inputData.containsKey(RoleSaveModel.DELETED_KPIS)) {
        roleSaveModel.setDeletedKPIs((List<String>) inputData.get(RoleSaveModel.DELETED_KPIS));
      }
      roleSaveModel.setType((String) entityMap.get(CommonConstants.TYPE_PROPERTY));
      roleSaveModel.setCode((String) entityMap.get(CommonConstants.CODE_PROPERTY));
      roleSaveModel.setId((String) entityMap.get(CommonConstants.ID_PROPERTY));
      roleSaveModel.setDescription((String) entityMap.get(CommonConstants.DESCRIPTION_PROPERTY));
      roleSaveModel.setTooltip((String) entityMap.get(IConfigMasterPropertyEntity.TOOLTIP));
      roleSaveModel.setIsMandatory((Boolean) entityMap.get(IConfigMasterPropertyEntity.IS_MANDATORY));
      roleSaveModel.setPlaceholder((String) entityMap.get(IConfigMasterPropertyEntity.PLACEHOLDER));
      roleSaveModel.setIsStandard((Boolean) entityMap.get(IConfigMasterPropertyEntity.IS_STANDARD));
      roleSaveModel.setIsMultiselect((Boolean) entityMap.get(IRole.IS_MULTISELECT));
      roleSaveModel.setGlobalPermission((Map<String, IGlobalPermission>) entityMap.get(IRole.GLOBAL_PERMISSION));
      roleSaveModel.setIsBackgroundRole((Boolean) entityMap.get(IRole.IS_BACKGROUND_ROLE));
      roleSaveModel.setRoleType((String) entityMap.get(IRole.ROLE_TYPE));
      return saveRole.execute(roleSaveModel);
    }
  }
  
  /**
   * updating fields
   * 
   * @param inputData
   * @param getData
   * @param key
   * @return
   */
  Object updateEntity(Map<String, Object> inputData, Map<String, Object> getData, String key)
  {
    if (inputData.containsKey(key)) {
      return inputData.get(key);
    }
    else {
      return getData.get(key);
    }
  }
  
  /**
   * This method checks if user input (to be added) is not already present in system
   * 
   * @param inputData
   * @param inputDataKey
   * @param dataFromGETCall
   * @param keyForGETCall
   * @return
   */
  private List<String> validateUserInputs(Map<String, Object> inputData, String inputDataKey, Map<String, Object> dataFromGETCall,
      String keyForGETCall)
  {
    List<String> existingData = (List<String>) dataFromGETCall.get(keyForGETCall);
    List<String> userInputs = (List<String>) inputData.get(inputDataKey);
    List<String> validatedData = new ArrayList<String>();
    if (existingData != null && !existingData.isEmpty()) {
      for (String param : userInputs) {
        if (!existingData.contains(param)) {
          validatedData.add(param);
        }
      }
      return validatedData;
    }
    return userInputs;
  }
  
  /**Fetch Organization details to validate user details provided for a specific Role
   * 
   * @param organizationCode
   * @param configModel
   * @return
   * @throws Exception
   */
  protected IModel fetchOrganizationDetails(String organizationCode, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(organizationCode);
    return getOrganization.execute(getEntityModel);
  }
  
  /**This method checks if user input (to be added) to a Role is not already present in the system
   * 
   * @param allowedUserInputs
   * @param dataFromGETCall
   * @param keyForGETCall
   * @return
   */
  private List<String> validateUserInputsForRole(List<String> allowedUserInputs, Map<String, Object> dataFromGETCall, String keyForGETCall)
  {
    List<String> existingData = (List<String>) dataFromGETCall.get(keyForGETCall);
    List<String> validatedData = new ArrayList<String>();
    if (existingData != null && !existingData.isEmpty()) {
      for (String param : allowedUserInputs) {
        if (!existingData.contains(param)) {
          validatedData.add(param);
        }
      }
      return validatedData;
    }
    return allowedUserInputs;
  }
  
  /**This method checks if the user provided values for a Role are subset of the values of the Organization to which it belongs.
   * 
   * @param inputData
   * @param inputDataKey
   * @param organizationDetails
   * @param keyForOrg
   * @return
   */
  private List<String> checkForAllowedInputsForRole(Map<String, Object> inputData, String inputDataKey,
      Map<String, Object> organizationDetails, String keyForOrg)
  {
    List<String> organizationData = (List<String>) organizationDetails.get(keyForOrg);
    List<String> userProvidedData = (List<String>) inputData.get(inputDataKey);
    List<String> allowedValues = new ArrayList<String>();
    if (organizationData != null && !organizationData.isEmpty()) {
      for (String value : userProvidedData) {
        if (organizationData.contains(value)) {
          allowedValues.add(value);
        }
      }
      return allowedValues;
    }
    return userProvidedData;
  }
}