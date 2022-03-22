package com.cs.config.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigContextDTO;
import com.cs.config.dto.ConfigDataRuleDTO;
import com.cs.config.dto.ConfigGoldenRecordRuleDTO;
import com.cs.config.dto.ConfigLanguageDTO;
import com.cs.config.dto.ConfigOrganizationDTO;
import com.cs.config.dto.ConfigPermissionDTO;
import com.cs.config.dto.ConfigPropertyCollectionDTO;
import com.cs.config.dto.ConfigRelationshipDTO;
import com.cs.config.dto.ConfigTabDTO;
import com.cs.config.dto.ConfigTagDTO;
import com.cs.config.dto.ConfigTagValueDTO;
import com.cs.config.dto.ConfigTaskDTO;
import com.cs.config.dto.ConfigTranslationDTO;
import com.cs.config.dto.ConfigUserDTO;
import com.cs.config.idao.IRefConfigurationDAO;
import com.cs.config.idto.IConfigAttributeDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.idto.IConfigDataRuleDTO;
import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.config.idto.IConfigPropertyCollectionDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigRoleDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.idto.IConfigTaskDTO;
import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;

// import com.cs.core.constants.ConfigRequestMappingConstants;

@SuppressWarnings("unchecked")
public class RefConfigurationDAO implements IRefConfigurationDAO {
  
  /**
   * ************************* EXPORT constants *********************
   */
  // specific service
  private static final String EXPORT_ATTRIBUTE_LIST           = "ExportAttributeList";
  private static final String EXPORT_TAG_LIST                 = "ExportTagList";
  private static final String EXPORT_RELATIONSHIP_LIST        = "ExportRelationshipList";
  private static final String EXPORT_PROPERTY_COLLECTION_LIST = "ExportPropertyCollectionList";
  private static final String EXPORT_CLASS_LIST               = "ExportClassList";
  private static final String EXPORT_TAXONOMY_LIST            = "ExportTaxonomyList";
  private static final String EXPORT_CONTEXT_LIST             = "ExportContextList";
  private static final String EXPORT_USER_LIST                = "ExportUserList";
  private static final String EXPORT_TASK_LIST                = "ExportTaskList";
  private static final String EXPORT_RULE_LIST                = "ExportRuleList";
  private static final String EXPORT_GOLDEN_RECORD_RULE_LIST  = "ExportGoldenRecordRuleList";
  private static final String EXPORT_ORGANIZATION_LIST        = "ExportOrganizationList";
  private static final String EXPORT_TRANSLATION_LIST         = "ExportTranslationList";
 //private static final String EXPORT_HIERARCHY_TAXONOMY_LIST = "ExportHierarchyTaxonomyList"; PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
  private static final String EXPORT_LANGUAGE_LIST            = "ExportLanguageList";
  private static final String EXPORT_TAB_LIST                 = "ExportTabList";
  private static final String EXPORT_PERMISSION_LIST                 = "ExportPermissionList";
  
  private static final String GET_TAG_VALUES_BY_CODE          = "GetTagValueByCode";

  // specific keys
  private static final String ITEM_CODES                      = "itemCodes";
  private static final String LIST                            = "list";
  private static final String LANGUAGES                       = "languages";
  private static final String ENTITY_TYPE                     = "entityType";
  private static final String SORT_LANGUAGE                   = "sortLanguage";
  private static final String SORT_BY                         = "sortBy";
  private static final String INCLUDE_CHILDREN                 = "includeChildren";
  
  @Override
  public Collection<IConfigPropertyCollectionDTO> getPropertyCollections(String localeID,
      String... collectionCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigPropertyCollectionDTO> propertyCollectionDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(collectionCodes);
    requestModel.put(ITEM_CODES, codes);
    
    JSONObject propertyCollections = CSConfigServer.instance()
        .request(requestModel, EXPORT_PROPERTY_COLLECTION_LIST, localeID);
    
    JSONArray propertyCollectionList = (JSONArray) propertyCollections.get(LIST);
    for (Object propertyCollection : propertyCollectionList) {
      ConfigPropertyCollectionDTO collectionDTO = new ConfigPropertyCollectionDTO();
      collectionDTO.fromJSON(propertyCollection.toString());
      propertyCollectionDTOList.add(collectionDTO);
    }
    return propertyCollectionDTOList;
  }
  
  @Override
  public Collection<IConfigAttributeDTO> getAttributes(String localeID, String... attributeCodes)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigAttributeDTO> attributeDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(attributeCodes);
    requestModel.put(ITEM_CODES, codes);
    
    JSONObject attributeObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_ATTRIBUTE_LIST, localeID);
    
    JSONArray propertyList = (JSONArray) attributeObj.get(LIST);
    
    for (Object attribute : propertyList) {
      ConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();
      attributeDTO.fromJSON(attribute.toString());
      attributeDTOList.add(attributeDTO);
    }
    
    return attributeDTOList;
  }
  
  @Override
  public Collection<IConfigAttributeDTO> getAttributesByType(String localeID,
      PropertyType... attributeCodes)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public Collection<IConfigTagDTO> getTags(String localeID, String... tagCodes)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigTagDTO> tagDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(tagCodes);
    requestModel.put(ITEM_CODES, codes);
    
    JSONObject tagObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_TAG_LIST, localeID);
    
    JSONArray propertyList = (JSONArray) tagObj.get(LIST);
    for (Object tag : propertyList) {
      ConfigTagDTO tagDTO = new ConfigTagDTO();
      tagDTO.fromJSON(tag.toString());
      tagDTOList.add(tagDTO);
    }
    
    return tagDTOList;
  }
  
  @Override
  public IConfigTagValueDTO getTagValueByCode(String localeID, String tagCode)
      throws CSFormatException, CSInitializationException
  {
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = new ArrayList<>();
    codes.add(tagCode);
    requestModel.put("ids", codes);
    
    JSONObject tagValueObj = CSConfigServer.instance()
        .request(requestModel, GET_TAG_VALUES_BY_CODE, localeID);
    
    JSONArray propertyList = (JSONArray) tagValueObj.get(LIST);
    
    ConfigTagValueDTO tagValueDTO = new ConfigTagValueDTO();
    for (Object tag : propertyList) {
      tagValueDTO.fromJSON(tag.toString());
      break;
    }
    
    return tagValueDTO;
  }
  
  @Override
  public Collection<IConfigRelationshipDTO> getRelationships(String localeID,
      String... relationCodes) throws CSFormatException, CSInitializationException
  {
    
    Collection<IConfigRelationshipDTO> realtionshipDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(relationCodes);
    requestModel.put(ITEM_CODES, codes);
    
    JSONObject relationshipObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_RELATIONSHIP_LIST, localeID);
    
    JSONArray propertyList = (JSONArray) relationshipObj.get(LIST);
    
    for (Object relationship : propertyList) {
      ConfigRelationshipDTO relationshipDTO = new ConfigRelationshipDTO();
      relationshipDTO.fromJSON( relationship.toString());
      realtionshipDTOList.add(relationshipDTO);
    }
    
    return realtionshipDTOList;
  }

  @Override
  public Collection<IConfigContextDTO> getContexts(String localeID, String... contextCodes)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigContextDTO> contextDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(contextCodes);
    requestModel.put(ITEM_CODES, codes);
    
    JSONObject contextObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_CONTEXT_LIST, localeID);
    
    JSONArray contextList = (JSONArray) contextObj.get(LIST);
    for (Object context : contextList) {
      ConfigContextDTO contextDTO = new ConfigContextDTO();
      contextDTO.fromJSON( context.toString());
      contextDTOList.add(contextDTO);
    }
    return contextDTOList;
  }
  
  @Override
  public Collection<IConfigClassifierDTO> getClasses(boolean includeChildren, String localeID,
      EntityType klassType, String... classifierCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigClassifierDTO> classifierDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(classifierCodes);
    requestModel.put(ITEM_CODES, codes);
    requestModel.put(INCLUDE_CHILDREN, includeChildren);
    requestModel.put(ConfigTag.entityType.name(), klassType.getEntityType());
    
    JSONObject classifierObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_CLASS_LIST, localeID);
    
    JSONArray classiferList = (JSONArray) classifierObj.get(LIST);
    for (Object classifier : classiferList) {
      ConfigClassifierDTO classifierDTO = new ConfigClassifierDTO();
      classifierDTO.fromJSON( classifier.toString());
      classifierDTOList.add(classifierDTO);
    }
    return classifierDTOList;
  }
  
  @Override
  public Collection<IConfigClassifierDTO> getTaxonomies(boolean includeChildren, String localeID,
      String... classifierCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigClassifierDTO> taxonomyDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(classifierCodes);
    requestModel.put(ITEM_CODES, codes);
    requestModel.put(INCLUDE_CHILDREN, includeChildren);
    requestModel.put(ConfigTag.entityType, EntityType.ATTRIBUTION_TAXONOMY.getEntityType());
    
    JSONObject taxonomyObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_TAXONOMY_LIST, localeID);
    
    JSONArray taxonomyList = (JSONArray) taxonomyObj.get(LIST);
    for (Object taxonomy : taxonomyList) {
      ConfigClassifierDTO classifierDTO = new ConfigClassifierDTO();
      classifierDTO.fromJSON( taxonomy.toString());
      taxonomyDTOList.add(classifierDTO);
    }
    return taxonomyDTOList;
  }

  @Override
  public Collection<IConfigUserDTO> getUsers(String localeID,
      String... userNames) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigUserDTO> userDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> names = Arrays.asList(userNames);
    requestModel.put(ITEM_CODES, names);
    
    JSONObject users = CSConfigServer.instance()
        .request(requestModel, EXPORT_USER_LIST, localeID);
    
    JSONArray userList = (JSONArray) users.get(LIST);
    for (Object user : userList) {
      ConfigUserDTO userDTO = new ConfigUserDTO();
      userDTO.fromJSON(user.toString());
      userDTOList.add(userDTO);
    }
    return userDTOList;
  }


  @Override
  public Collection<IConfigTaskDTO> getTasks(String localeID,
      String... taskCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigTaskDTO> taskDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(taskCodes));

    JSONObject tasks = CSConfigServer.instance()
        .request(requestModel, EXPORT_TASK_LIST, localeID);

    JSONArray taskList = (JSONArray) tasks.get(LIST);
    for (Object task : taskList) {
      ConfigTaskDTO taskDTO = new ConfigTaskDTO();
      taskDTO.fromJSON(task.toString());
      taskDTOs.add(taskDTO);
    }
    return taskDTOs;
  }
  
  @Override
  public Collection<IConfigOrganizationDTO> getOrganizations(String localeID,
      String... organizationCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigOrganizationDTO> organizationDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(organizationCodes));
    
    JSONObject organizations = CSConfigServer.instance().request(requestModel, EXPORT_ORGANIZATION_LIST, localeID);
    
    JSONArray organizationList = (JSONArray) organizations.get(LIST);

    for (Object organization : organizationList) {
      ConfigOrganizationDTO organizationDTO = new ConfigOrganizationDTO();
      organizationDTO.fromJSON(organization.toString());
      
      List<String> organizationPhysicalCatalogs = organizationDTO.getPhysicalCatalogs();

      if (organizationPhysicalCatalogs.size() == 0) {
        organizationPhysicalCatalogs = Constants.PHYSICAL_CATALOG_IDS;
        organizationDTO.setPhysicalCatalogs(organizationPhysicalCatalogs);
      }

      if (organizationDTO.getPortals().size() == 0) {
        organizationDTO.setPortals(Constants.PORTALS_IDS);
      }

      for (IConfigRoleDTO roleDto : organizationDTO.getRoles()) {
        if (roleDto.getPhysicalCatalogs().size() == 0) {
          roleDto.setPhysicalCatalogs(organizationPhysicalCatalogs);
        }

        if (roleDto.getPortals().size() == 0) {
          roleDto.setPortals(Constants.PORTALS_IDS);
        }

        if (roleDto.getEntities().size() == 0) {
          roleDto.setEntities(CommonConstants.MODULE_ENTITIES);
        }
      }
      
      organizationDTOs.add(organizationDTO);
    }
    return organizationDTOs;
  }
  
  @Override
  public Collection<IConfigLanguageDTO> getLanguages(String localeID,
      String... languageCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigLanguageDTO> languageDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(languageCodes));
    JSONObject languages = CSConfigServer.instance().request(requestModel, EXPORT_LANGUAGE_LIST, localeID);
    JSONArray languageList = (JSONArray) languages.get(LIST);
    
    for (Object language : languageList) {
    	ConfigLanguageDTO languageDTO = new ConfigLanguageDTO();
    	languageDTO.fromJSON(language.toString());
    	languageDTOs.add(languageDTO);
      }
    
    return languageDTOs;
  }
  
  @Override
  public Collection<IConfigTabDTO> getTabs(String localeID,
      String... tabCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigTabDTO> tabDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(tabCodes));
    JSONObject tabs = CSConfigServer.instance().request(requestModel, EXPORT_TAB_LIST, localeID);
    JSONArray tabList = (JSONArray) tabs.get(LIST);
    
    for (Object tab : tabList) {
      ConfigTabDTO tabDTO = new ConfigTabDTO();
      tabDTO.fromJSON(tab.toString());
      tabDTOs.add(tabDTO);
    }
    
    return tabDTOs;
  }
  
  @Override
  public Collection<IConfigDataRuleDTO> getRules(String localeID, String... rulesCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigDataRuleDTO> ruleDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(rulesCodes));
    
    JSONObject tasks = CSConfigServer.instance().request(requestModel, EXPORT_RULE_LIST, localeID);
    
    JSONArray ruleList = (JSONArray) tasks.get(LIST);
    for (Object rule : ruleList) {
      ConfigDataRuleDTO ruleDTO = new ConfigDataRuleDTO();
      ruleDTO.fromJSON(rule.toString());
      ruleDTOs.add(ruleDTO);
    }
    return ruleDTOs;
  }
  
  @Override
  public Collection<IConfigGoldenRecordRuleDTO> getGoldenRecordRules(String localeID, String... rulesCodes)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigGoldenRecordRuleDTO> goldenRuleDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(rulesCodes));
    
    JSONObject goldenRules = CSConfigServer.instance().request(requestModel, EXPORT_GOLDEN_RECORD_RULE_LIST, localeID);
    
    JSONArray ruleList = (JSONArray) goldenRules.get(LIST);
    for (Object rule : ruleList) {
      ConfigGoldenRecordRuleDTO goldenRuleDTO = new ConfigGoldenRecordRuleDTO();
      goldenRuleDTO.fromJSON(rule.toString());
      goldenRuleDTOs.add(goldenRuleDTO);
    }
    return goldenRuleDTOs;
  }
  
  @Override
  public Collection<IConfigTranslationDTO> getTranslations(String entityType, String sortLanguage, String sortBy, String... languages)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigTranslationDTO> translationDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ENTITY_TYPE, entityType);
    requestModel.put(LANGUAGES, Arrays.asList(languages));
    requestModel.put(SORT_LANGUAGE, sortLanguage);
    requestModel.put(SORT_BY, sortBy);
    
    JSONObject translations = CSConfigServer.instance().request(requestModel, EXPORT_TRANSLATION_LIST, "");
    JSONArray translationList = (JSONArray) translations.get(LIST);
    for (Object translation : translationList) {
      ConfigTranslationDTO translationDTO = new ConfigTranslationDTO();
      translationDTO.fromJSON(translation.toString());
      translationDTOs.add(translationDTO);
    }
    return translationDTOs;
  }
  
  @Override
  @Deprecated // PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
  public Collection<IConfigClassifierDTO> getHierarchyTaxonomies(boolean includeChildren, String localeID,
      String... taxonomyCodes) throws CSFormatException, CSInitializationException
  {
    Collection<IConfigClassifierDTO> taxonomyDTOList = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    
    List<String> codes = Arrays.asList(taxonomyCodes);
    requestModel.put(ITEM_CODES, codes);
    requestModel.put(INCLUDE_CHILDREN, includeChildren);
    // PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
    /*JSONObject taxonomyObj = CSConfigServer.instance()
        .request(requestModel, EXPORT_HIERARCHY_TAXONOMY_LIST, localeID);
    
    JSONArray taxonomyList = (JSONArray) taxonomyObj.get(LIST);
    for (Object taxonomy : taxonomyList) {
      ConfigClassifierDTO classifierDTO = new ConfigClassifierDTO();
      classifierDTO.fromJSON( taxonomy.toString());
      taxonomyDTOList.add(classifierDTO);
    }*/
    return taxonomyDTOList;
  }

  @Override
  public Collection<IConfigPermissionDTO> getPermissions(String localeID, String... permissionCodes)
      throws CSFormatException, CSInitializationException
  {
    Collection<IConfigPermissionDTO> permissionDTOs = new ArrayList<>();
    JSONObject requestModel = new JSONObject();
    requestModel.put(ITEM_CODES, Arrays.asList(permissionCodes));
    JSONObject permissions = CSConfigServer.instance()
        .request(requestModel, EXPORT_PERMISSION_LIST, localeID);
    
    JSONArray permissionList = (JSONArray) permissions.get(LIST);
    
    for (Object permission : permissionList) {
      IConfigPermissionDTO permissionDTO = new ConfigPermissionDTO();
      permissionDTO.fromJSON(permission.toString());
      permissionDTOs.add(permissionDTO);
    }
    
    return permissionDTOs;
  }
  
}
