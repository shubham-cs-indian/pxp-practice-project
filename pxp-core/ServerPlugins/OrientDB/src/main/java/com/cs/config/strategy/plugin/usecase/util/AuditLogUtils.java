package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.tinkerpop.blueprints.Vertex;


public class AuditLogUtils {
  
  public static String getAttributeTypeCodeByAttributteType(String attributeType)
  {
    
    if (attributeType.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
      return "ATTRIBUTE_TYPES_CALCULATED_ATTRIBUTE";
    }
    else if (attributeType.equals(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE)) {
      return "ATTRIBUTE_TYPES_CONCATENATED_ATTRIBUTE";
    }
    else if (attributeType.equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
      return "DATE";
    }
    else if (attributeType.equals(CommonConstants.HTML_TYPE_ATTRIBUTE)) {
      return "ATTRIBUTE_TYPES_HTML";
    }
    else if (attributeType.equals(CommonConstants.NUMBER_ATTRIBUTE_TYPE)) {
      return "ATTRIBUTE_TYPES_NUMBER";
    }
    else if (attributeType.equals(CommonConstants.PRICE_ATTRIBUTE_TYPE)) {
      return "ATTRIBUTE_TYPES_PRICE";
    }
    else if (attributeType.equals(CommonConstants.TEXT_ATTRIBUTE_TYPE)) {
      return "ATTRIBUTE_TYPES_TEXT";
    }
    else if (CommonConstants.UNIT_ATTRIBUTE_TYPES.contains(attributeType)) {
      return "ATTRIBUTE_TYPES_MEASUREMENT_ATTRIBUTE";
    }
    else if (CommonConstants.STANDARD_ATTRIBUTE_TYPES.contains(attributeType)) {
      return "STANDARD_ATTRIBUTES";
    }
    else
      return "";
  }

  
  public static String getElementTypeCodeByElementType(String elementType)
  {
    
    switch(elementType)
    {
      case CommonConstants.BOOLEAN_TAG_TYPE_ID:
        return "TAG_TYPE_BOOLEAN";
        
      case CommonConstants.YES_NEUTRAL_TAG_TYPE_ID:
        return "TAG_TYPE_YES_NEUTRAL";
     
      case CommonConstants.YES_NEUTRAL_NO_TAG_TYPE_ID:
        return "TAG_TYPE_YES_NEUTRAL_NO";
        
      case CommonConstants.MASTER_TAG_TYPE_ID:
        return "MASTER";
      
      case CommonConstants.STATUS_TYPE_ID:
        return "TAG_TYPE_STATUS";
        
      case CommonConstants.STATUS_TAG_TYPE_ID:
        return "TAG_TYPE_LIFECYCLE_STATUS";
      
      case CommonConstants.LISTING_TYPE_ID:
        return "TAG_TYPE_LISTING_STATUS";
        
      case CommonConstants.CLASSIFICATION:
        return "CLASSIFICATION";
      
      case CommonConstants.RANGE_TAG_TYPE_ID:
        return "RANGE";  
      
      case CommonConstants.RULER_TAG_TYPE_ID:
        return "TAG_TYPE_RULER";   
        
      case Constants.STANDARDIZATION_AND_NORMALIZATION:
      return "STANDARDIZATION_AND_NORMALIZATION";
      
      case Constants.VIOLATION:
        return "VIOLATION";
        
      case Constants.FIXED_BUNDLE:
        return "FIXED_BUNDLE";
        
      case Constants.SET_OF_PRODUCTS:
        return "SET_OF_PRODUCTS";
        
      case Constants.PID:
        return "BASE_ARTICLE";
        
      case Constants.INDIVIDUAL_ARTICLE:
        return "SINGLE_ARTICLE";
        
      case CommonConstants.MAJOR_TAXONOMY:
        return "MAJOR";
        
      case CommonConstants.MINOR_TAXONOMY:
        return "MINOR";
        
      case Constants.EMBEDDED:
        return "EMBEDDED";
        
      case Constants.STANDARD_IDENTIFIER:
        return "GTIN";
      
      case CommonConstants.NON_NATURE_TYPE:
        return "NON_NATURE";
        
      case CommonConstants.NATURE_TYPE:
        return "NATURE";
       
      case CommonConstants.MAM_NATURE_TYPE_IMAGE:
        return "IMAGE";
        
      case CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE:
        return "TECHNICAL_IMAGE";
        
      case CommonConstants.MAM_NATURE_TYPE_DOCUMENT:
        return "DOCUMENT";

      case CommonConstants.MAM_NATURE_TYPE_FILE:
        return "FILE";
        
      case CommonConstants.MAM_NATURE_TYPE_VIDEO:
        return "VIDEO";
        
      case CommonConstants.TEXT_ASSET:
        return "TEXT_ASSET";
        
      case EntityConfigurationConstants.PRODUCT_CATALOG:
        return "PRODUCT";
        
      case EntityConfigurationConstants.MEDIA_CATALOG: //
        return "MEDIA";
        
      case CommonConstants.ATTRIBUTE_VARIANT_CONTEXT:
        return "ATTRIBUTE_CONTEXT_VARIANT";
        
      case CommonConstants.PRODUCT_VARIANT:
        return "CONTEXT_TYPES_PRODUCT_VARIANT";
        
      case CommonConstants.RELATIONSHIP_VARIANT:
        return "CONTEXT_TYPES_RELATIONSHIP_VARIANT";
        
      case CommonConstants.PROPERTY_COLLECTION:
       return "PROPERTY_COLLECTION";
       
      case CommonConstants.X_RAY:
        return "X-RAY";
        
      case CommonConstants.MARKET_ENTITY:
        return "MARKET";
        
      case CommonConstants.TEXT_ASSET_ENTITY:
        return "TEXT_ASSET";
        
      case CommonConstants.SSO_LDAP:
        return "LDAP";

      case CommonConstants.SSO_SAML:
        return "SAML";

    }
    
    return getAttributeTypeCodeByAttributteType(elementType);
  }
  
  public static void fillAuditLoginfo(Map<String, Object> returnKlassMap, Vertex node, Entities entity, Elements element)
  {
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    fillAuditLoginfo(auditLogInfoList, node, entity, element);
    returnKlassMap.put(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO, auditLogInfoList);
  }
  
  public static void fillAuditLoginfo(Map<String, Object> returnKlassMap, Vertex node, Entities entity, Elements element, String elementName)
  {
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    fillAuditLoginfo(auditLogInfoList, node, entity, element, elementName);
    returnKlassMap.put(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO, auditLogInfoList);
  }
  
  public static void fillAuditLoginfo(List<Map<String, Object>> auditLogInfoList,Vertex node, Entities entity, Elements element)
  {
    String elementName = (String) node.getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY));
    Map<String, Object> auditLogInfo = prepareAndGetAuditLogInfoModel(node, entity, element, elementName);
    auditLogInfoList.add(auditLogInfo);
  }
  
  public static void fillAuditLoginfo(List<Map<String, Object>> auditLogInfoList,Vertex node, Entities entity, Elements element, String elementName)
  {
    Map<String, Object> auditLogInfo = prepareAndGetAuditLogInfoModel(node, entity, element, elementName);
    auditLogInfoList.add(auditLogInfo);
  }
  
  public static Map<String, Object> prepareAndGetAuditLogInfoModel(Vertex node, Entities entity, Elements element, String elementName)
  {
    
    String code = node.getProperty(IConfigEntity.CODE);
    String elementType = getElementTypeByVertexType(node);
    Map<String, Object> auditLogInfo = new HashMap<>();
    auditLogInfo.put(IAuditLogModel.ELEMENT_CODE, code);
    auditLogInfo.put(IAuditLogModel.ELEMENT_NAME, elementName);
    auditLogInfo.put(IAuditLogModel.ELEMENT_TYPE, AuditLogUtils.getElementTypeCodeByElementType(elementType));
    auditLogInfo.put(IAuditLogModel.ELEMENT, element);
    auditLogInfo.put(IAuditLogModel.ENTITY_TYPE, entity);
    
    return auditLogInfo;
  }

  private static String getElementTypeByVertexType(Vertex node)
  {
    String vertexType = (String) node.getProperty(CommonConstants.KLASS_PROPERTY_OREINT);
    String natureType = "";
    
    if (vertexType.equals(CommonConstants.DATA_RULES)) {
      natureType = node.getProperty(IDataRule.TYPE);
    }
    else if (CommonConstants.AUDIT_KLASS_LIST.contains(vertexType)) {
      natureType = getNatureTypeOfKlassesByVertex(node); 
    }
    else if (vertexType.equals(CommonConstants.VARIANT_CONTEXT)) {
      natureType = node.getProperty(IVariantContext.TYPE);
    }
    else if (vertexType.equals(CommonConstants.RELATIONSHIP_KLASS)) {
      Boolean isNature = (Boolean) node.getProperty(IRelationship.IS_NATURE);
      natureType = getNatureOrNonNatureType(isNature);
    }
    else if (vertexType.equals(CommonConstants.ATTRIBUTION_TAXONOMY_KLASS)
        || vertexType.equals(CommonConstants.KLASS_TAXONOMY_KLASS)) {
      String natureProperty = node.getProperty(ITaxonomy.TAXONOMY_TYPE);
      natureType = natureProperty == null ? "" : natureProperty;
      
    }
    else if (vertexType.equals(CommonConstants.ATTRIBUTE_KLASS_TYPE)) {
      natureType = node.getProperty(IAttribute.TYPE);
    }
    else if (vertexType.equals(CommonConstants.STANDARD_ATTRIBUTE_KLASS_TYPE)) {
      natureType = node.getProperty(IAttribute.TYPE);
    }
    else if (vertexType.equals(CommonConstants.TAG_KLASS_TYPE)) {
      natureType = node.getProperty(ITag.TAG_TYPE);
    }
    else if (vertexType.equals(CommonConstants.PROPERTY_COLLECTION_TYPE)) {
      Boolean isForXRay = node.getProperty(IPropertyCollection.IS_FOR_X_RAY);
      natureType = isForXRay ? CommonConstants.X_RAY : CommonConstants.PROPERTY_COLLECTION ;
    }
    else {
      
    }
    
    return natureType;
  }

  private static String getNatureOrNonNatureType(Boolean property)
  {
    return property ? CommonConstants.NATURE_TYPE : CommonConstants.NON_NATURE_TYPE;
  }
  
  private static String getNatureTypeOfKlassesByVertex(Vertex node)
  {
    String natureType = node.getProperty(IKlass.NATURE_TYPE);
    natureType = (natureType == null || natureType.equals("")) ? CommonConstants.NON_NATURE_TYPE
        : natureType;
    return natureType;
  }
 }

