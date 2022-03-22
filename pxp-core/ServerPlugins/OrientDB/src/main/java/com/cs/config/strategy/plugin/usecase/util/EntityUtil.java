package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class EntityUtil {
  
  public static String quoteIt(Object unquotedString)
  {
    return "\"" + unquotedString + "\"";
  }
  
  public static String quoteIt(Collection<String> listOfStrings)
  {
    // String unQuotedArrayOfString = listOfStrings.toString();
    StringBuilder classesArrayInString = new StringBuilder();
    classesArrayInString.append("[");
    int arraySize = listOfStrings.size();
    for (String listItem : listOfStrings) {
      arraySize--;
      classesArrayInString.append(quoteIt(listItem));
      if (arraySize != 0) {
        classesArrayInString.append(", ");
      }
    }
    classesArrayInString.append("]");
    
    return classesArrayInString.toString();
  }
  
  public static List<String> getKlassTypes(List<String> entities)
  {
    List<String> returnList = new ArrayList<>();
    for (String entity : entities) {
      switch (entity) {
        case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
          returnList.add(Constants.PROJECT_KLASS_TYPE);
          break;
        case Constants.ASSET_INSTANCE_MODULE_ENTITY:
          returnList.add(Constants.ASSET_KLASS_TYPE);
          break;
        case Constants.MARKET_INSTANCE_MODULE_ENTITY:
          returnList.add(Constants.MARKET_KLASS_TYPE);
          break;
        case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
          returnList.add(Constants.TEXT_ASSET_KLASS_TYPE);
          break;
        case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
          returnList.add(Constants.SUPPLIER_KLASS_TYPE);
          break;
      }
    }
    
    return returnList;
  }
  
  public static String getKlassTypeByBaseType(String baseType)
  {
    String klassType = null;
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
        klassType = Constants.PROJECT_KLASS_TYPE;
        break;
      case Constants.ASSET_INSTANCE_MODULE_ENTITY:
        klassType = Constants.ASSET_KLASS_TYPE;
        break;
      case Constants.MARKET_INSTANCE_MODULE_ENTITY:
        klassType = Constants.MARKET_KLASS_TYPE;
        break;
      case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
        klassType = Constants.TEXT_ASSET_KLASS_TYPE;
        break;
      
      case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
        klassType = Constants.SUPPLIER_KLASS_TYPE;
        break;
    }
    
    return klassType;
  }
  
  
  public static String getKlassType(String baseType)
  {
    String klassType = null;
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        klassType = Constants.PROJECT_KLASS_TYPE;
        break;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        klassType = Constants.ASSET_KLASS_TYPE;
        break;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        klassType = Constants.MARKET_KLASS_TYPE;
        break;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        klassType = Constants.TEXT_ASSET_KLASS_TYPE;
        break;
      
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        klassType = Constants.SUPPLIER_KLASS_TYPE;
        break;
    }
    
    return klassType;
  }
  
  public static String getModuleEntityFromKlassType(String klassType)
  {
    if (klassType == null) {
      return null;
    }
    switch (klassType) {
      case CommonConstants.PROJECT_KLASS_TYPE:
        return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
      case CommonConstants.ASSET_KLASS_TYPE:
        return CommonConstants.ASSET_INSTANCE_MODULE_ENTITY;
      case CommonConstants.MARKET_KLASS_TYPE:
        return CommonConstants.MARKET_INSTANCE_MODULE_ENTITY;
      case CommonConstants.SUPPLIER_KLASS_TYPE:
        return CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY;
      
      case CommonConstants.TEXT_ASSET_KLASS_TYPE:
        return CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;
    }
    return null;
  }
  
  /**
   * Osho
   *
   * @param countQuery
   * @return totalCount
   */
  public static Long executeCountQueryToGetTotalCount(String countQuery)
  {
    Long count;
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(countQuery))
        .execute();
    Iterator<Vertex> iterator = countResult.iterator();
    count = iterator.next()
        .getProperty("count");
    return count;
  }
  
  public static String getStandardKlassIdForModuleEntity(String moduleEntity)
  {
    switch (moduleEntity) {
      case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.ARTICLE;
      case Constants.ASSET_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.ASSET;
      case Constants.MARKET_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.MARKET;
      case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.TEXT_ASSET;
      
      case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.SUPPLIER;
      case Constants.FILE_INSTANCE_MODULE_ENTITY:
        return SystemLevelIds.FILE;
    }
    return null;
  }
  
  public static String getVertexLabelByEntityType(String entityType)
  {
    switch (entityType) {
      case CommonConstants.TAG:
        return VertexLabelConstants.ENTITY_TAG;
      case CommonConstants.TAG_VALUES:
        return VertexLabelConstants.ENTITY_TAG;
      case CommonConstants.ATTRIBUTE:
        return VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
      case CommonConstants.PROPERTY:
        return VertexLabelConstants.ENTITY_TYPE_PROPERTY;
      case CommonConstants.CONTEXT:
        return VertexLabelConstants.VARIANT_CONTEXT;
      case CommonConstants.TASK:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      case CommonConstants.PROPERTY_COLLECTION:
        return VertexLabelConstants.PROPERTY_COLLECTION;
      case CommonConstants.ARTICLE_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case CommonConstants.ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      
      case CommonConstants.TARGET:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case CommonConstants.TEXT_ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case CommonConstants.SUPPLIER_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      case CommonConstants.KLASS_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
      case CommonConstants.TEMPLATE:
        return VertexLabelConstants.TEMPLATE;
      case CommonConstants.TAXONOMY:
        return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
      case CommonConstants.ROLE:
        return VertexLabelConstants.ENTITY_TYPE_ROLE;
      case CommonConstants.RULE:
        return VertexLabelConstants.DATA_RULE;
      case CommonConstants.TAXONOMY_MASTER_LIST:
        return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
      case CommonConstants.USER:
        return VertexLabelConstants.ENTITY_TYPE_USER;
      case CommonConstants.PROFILE:
        return VertexLabelConstants.ENDPOINT;
      case CommonConstants.MAPPING:
        return VertexLabelConstants.PROPERTY_MAPPING;
      case CommonConstants.PROCESS:
        return VertexLabelConstants.PROCESS_EVENT;
      case CommonConstants.ATTRIBUTION_TAXONOMY:
      case CommonConstants.MASTER_TAXONOMY:  
        return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
      case CommonConstants.RULE_LIST:
        return VertexLabelConstants.RULE_LIST;
      case CommonConstants.RELATIONSHIP:
        return VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP;
      case CommonConstants.ORGANIZATION:
        return VertexLabelConstants.ORGANIZATION;
      case CommonConstants.SYSTEM:
        return VertexLabelConstants.SYSTEM;
      case CommonConstants.DATA_GOVERNANCE:
        return VertexLabelConstants.GOVERNANCE_RULE_TASK;
      case CommonConstants.KEYPERFORMANCEINDEX:
        return VertexLabelConstants.GOVERNANCE_RULE_KPI;
      case CommonConstants.TAB:
        return VertexLabelConstants.TAB;
      case CommonConstants.ENTITY_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
      case CommonConstants.DASHBOARD_TAB:
        return VertexLabelConstants.DASHBOARD_TAB;
      case CommonConstants.GOLDEN_RECORDS:
        return VertexLabelConstants.GOLDEN_RECORD_RULE;
      case CommonConstants.LANGUAGE:
        return VertexLabelConstants.LANGUAGE;
      case CommonConstants.SMART_DOCUMENT_ENTITY:
        return VertexLabelConstants.SMART_DOCUMENT;
      case CommonConstants.SMART_DOCUMENT_TEMPLATE:
        return VertexLabelConstants.SMART_DOCUMENT_TEMPLATE;
      case CommonConstants.SMART_DOCUMENT_PRESET:
        return VertexLabelConstants.SMART_DOCUMENT_PRESET;
      case CommonConstants.PROCESS_EVENT:
        return VertexLabelConstants.PROCESS_EVENT;
      case CommonConstants.SSO_SETTING:
        return VertexLabelConstants.SSO_CONFIGURATION;
      case CommonConstants.AUTHORIZATION_MAPPING:
        return VertexLabelConstants.AUTHORIZATION_MAPPING;
      case CommonConstants.STATIC_TRANSLATION:
        return VertexLabelConstants.UI_TRANSLATIONS;
      case CommonConstants.ENDPOINT:
        return VertexLabelConstants.ENDPOINT;
    }
    return null;
  }
  
  
  public static String getVertexLabelByEntityTypeForIcon(String entityType)
  {
    switch (entityType) {
      case EntityConfigurationConstants.TAG:
        return VertexLabelConstants.ENTITY_TAG;
      case EntityConfigurationConstants.KLASS:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case EntityConfigurationConstants.CONTEXT:
        return VertexLabelConstants.VARIANT_CONTEXT;
      case EntityConfigurationConstants.TASK:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      case EntityConfigurationConstants.PROPERTY_COLLECTION:
        return VertexLabelConstants.PROPERTY_COLLECTION;
      case EntityConfigurationConstants.TAXONOMY:
        return VertexLabelConstants.KLASS_TAXONOMY_ENTITY;
      case EntityConfigurationConstants.MAPPING:
        return VertexLabelConstants.PROPERTY_MAPPING;
      case EntityConfigurationConstants.RELATIONSHIP:
        return VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP;
      case EntityConfigurationConstants.ORGANISATION:
        return VertexLabelConstants.ORGANIZATION;
      case EntityConfigurationConstants.LANGUAGE:
        return VertexLabelConstants.LANGUAGE;
      case EntityConfigurationConstants.STANDARD_ATTRIBUTE:
        return VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE;
      case EntityConfigurationConstants.ATTRIBUTE:
        return VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
      case EntityConfigurationConstants.TAB:
        return VertexLabelConstants.TAB;
      case EntityConfigurationConstants.ATTRIBUTION_TAXONOMY:
        return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
      case EntityConfigurationConstants.ASSET:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case EntityConfigurationConstants.TARGET:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case EntityConfigurationConstants.SUPPLIER:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      case EntityConfigurationConstants.KLASS_TEXT_ASSET:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case EntityConfigurationConstants.ENDPOINT:
        return VertexLabelConstants.ENDPOINT;
      
    }
    return null;
  }
  
  public static String getLanguageConvertedField(String field)
  {
    if (!IStandardTranslationModel.TRNASLATION_FIELDS.contains(field)) {
      return field;
    }
    String userLang = UtilClass.getLanguage()
        .getUiLanguage();
    return field + Seperators.FIELD_LANG_SEPERATOR + userLang;
  }
  
  public static String getDataLanguageConvertedField(String field)
  {
    if (!IStandardTranslationModel.TRNASLATION_FIELDS.contains(field)) {
      return field;
    }
    String userLang = UtilClass.getLanguage()
        .getDataLanguage();
    return field + Seperators.FIELD_LANG_SEPERATOR + userLang;
  }
  
  public static String getVertexType(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      default:
        return null;
    }
  }
  
  public static String getVertexTypeByKlassType(String klassType, String klassId)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case Constants.ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case Constants.MARKET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case Constants.SUPPLIER_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      default:
        return null;
    }
  }
  
  public static String getEntityTypeByOrientClassType(String classType)
  {
    // expand method as per use
    switch (classType) {
      case VertexLabelConstants.PROPERTY_COLLECTION:
        return CommonConstants.PROPERTY_COLLECTION;
      case VertexLabelConstants.VARIANT_CONTEXT:
        return CommonConstants.CONTEXT;
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
      case VertexLabelConstants.NATURE_RELATIONSHIP:
        return CommonConstants.RELATIONSHIP;
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
        return CommonConstants.ENTITY_KLASS_TYPE;
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
        return CommonConstants.TAXONOMY;
      case VertexLabelConstants.COLLECTION:
        return CommonConstants.COLLECTION;
    }
    return null;
  }
  
  public static StringBuilder getSearchQuery(String searchText, String searchColumn)
  {
    StringBuilder query = new StringBuilder();
    if (searchText != null && !searchText.isEmpty()) {
      
      /*We need to replace single Quote because Orientdb Query already has single quote surrounding LIKE %% query.
       * Query will give error if encountered single quote in search text
       */
      searchText = searchText.replace("'", "\\'");
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      query.append(searchColumn + " like '%" + searchText + "%' ");
    }
    return query;
  }
  
  public static StringBuilder getConditionQuery(StringBuilder... queries)
  {
    StringBuilder finalQuery = new StringBuilder();
    if (queries.length == 0) {
      return finalQuery;
    }
    for (StringBuilder query : queries) {
      if (query.length() == 0) {
        continue;
      }
      if (finalQuery.length() == 0) {
        finalQuery.append(" where " + query);
      }
      else {
        finalQuery.append(" and " + query);
      }
    }
    return finalQuery;
  }
  
  /**
   * This function checks if newCouplingType is higher than oldCouplingType
   * if(oldCouplingType = newCouplingType) then 0 if(oldCouplingType <
   * newCouplingType) then 1 if(oldCouplingType > newCouplingType) then -1
   *
   * @author Lokesh
   * @param oldCouplingType
   * @param newCouplingType
   * @return
   */
  public static Integer compareCoupling(String oldCouplingType, String newCouplingType)
  {
    if (oldCouplingType.equals(newCouplingType)) {
      return 0;
    }
    if (oldCouplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
      return 1;
    }
    if (oldCouplingType.equals(CommonConstants.READ_ONLY_COUPLED)
        && !newCouplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
      return 1;
    }
    if (!oldCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)
        && newCouplingType.equals(CommonConstants.TIGHTLY_COUPLED)) {
      return 1;
    }
    if (newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
      return 1;
    }
    return -1;
  }
  
  public static String getSystemLevelIdByModuleId(String moduleId)
  {
    switch (moduleId) {
      case Constants.ALL_MODULE:
        return "";
      case Constants.PIM_MODULE:
        return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
      
      case Constants.MAM_MODULE:
        return CommonConstants.ASSET_INSTANCE_MODULE_ENTITY;
      
      case Constants.TARGET_MODULE:
        return CommonConstants.MARKET_INSTANCE_MODULE_ENTITY;
      
      case Constants.TEXT_ASSET_MODULE:
        return CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;
      
      case Constants.SUPPLIER_MODULE:
        return CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY;
      
      case Constants.FILES_MODUlE:
        return CommonConstants.FILE_INSTANCE_MODULE_ENTITY;
    }
    return null;
  }
  
  public static String getVertexLabelConstantsByKlassType(String klassType)
  {
    if (klassType == null) {
      return null;
    }
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      
      case Constants.ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      
      case Constants.MARKET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      
      case Constants.SUPPLIER_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
    }
    return null;
  }
  
  public static List<String> getEntityTypesByKlassTypes(Collection<String> entities)
  {
    List<String> returnList = new ArrayList<>();
    for (String entity : entities) {
      switch (entity) {
        case Constants.PROJECT_KLASS_TYPE:
          returnList.add(Constants.ARTICLE_INSTANCE_MODULE_ENTITY);
          break;
        case Constants.ASSET_KLASS_TYPE:
          returnList.add(Constants.ASSET_INSTANCE_MODULE_ENTITY);
          break;
        case Constants.MARKET_KLASS_TYPE :
          returnList.add(Constants.MARKET_INSTANCE_MODULE_ENTITY);
          break;
        case Constants.TEXT_ASSET_KLASS_TYPE :
          returnList.add(Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY);
          break;
        case Constants.SUPPLIER_KLASS_TYPE :
          returnList.add(Constants.SUPPLIER_INSTANCE_MODULE_ENTITY);
          break;
      }
    }
    
    return returnList;
  }
  
  public static List<String> getStandardKlassIds(List<String> entities)
  {
    List<String> returnList = new ArrayList<>();
    for (String entity : entities) {
      returnList.add(getStandardKlassIdForModuleEntity(entity));
    }
    return returnList;
  }
}
