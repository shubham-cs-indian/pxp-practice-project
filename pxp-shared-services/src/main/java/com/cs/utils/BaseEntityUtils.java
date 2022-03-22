package com.cs.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.SetUtils;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BaseEntityUtils {
  
  public static List<String> getReferenceTypeFromClassifierDTO(Collection<IClassifierDTO> classifiers)
      throws Exception
  {
    List<String> typeIds = classifiers.stream()
        .filter(classifier -> classifier.getClassifierType()
            .equals(ClassifierType.CLASS))
        .map(classifier -> classifier.getClassifierCode())
        .collect(Collectors.toList());
    return typeIds;
  }
  
  public static List<String> getAllReferenceTypeFromBaseEntity(IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    List<String> classifiers = getReferenceTypeFromClassifierDTO(baseEntityDAO.getClassifiers());
    classifiers.add(baseEntityDAO.getBaseEntityDTO()
        .getNatureClassifier()
        .getClassifierCode());
    return classifiers;
  }
  
  public static List<String> getReferenceTaxonomyIdsFromBaseEntity(Collection<IClassifierDTO> classifiers)
  {
    List<String> selectedTaxonomyIds = classifiers.stream()
        .filter(classifier -> classifier.getClassifierType()
            .equals(ClassifierType.TAXONOMY)
            || classifier.getClassifierType()
                .equals(ClassifierType.MINOR_TAXONOMY))
        .map(classifier -> classifier.getClassifierCode())
        .collect(Collectors.toList());
    return selectedTaxonomyIds;
  }

  public static Map<Boolean, List<String>> seggregateTaxonomyAndClasses(Collection<IClassifierDTO> classifiers)
  {
    return classifiers.stream()
        .collect(Collectors.partitioningBy(classifier -> classifier.getClassifierType().equals(ClassifierType.CLASS),
            Collectors.mapping(IClassifierDTO::getClassifierCode, Collectors.toList())));
  }


  public static List<IPropertyDTO> getReferenceAttributesTagsProperties(
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags, IBaseEntityDAO baseEntityDAO)
  {
    List<IPropertyDTO> referenceAttributesTags = new ArrayList<IPropertyDTO>();

    referenceAttributesTags.addAll(getReferenceAttributesProperties(referencedAttributes, baseEntityDAO));
    referenceAttributesTags.addAll(getReferenceTagsProperties(referencedTags, baseEntityDAO));
    return referenceAttributesTags;
  }

  public static List<IPropertyDTO> getReferenceAttributesTags(
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags, IBaseEntityDAO baseEntityDAO)
      throws RDBMSException, SQLException
  {
    HashSet<String> union = new HashSet<>(SetUtils.union(referencedAttributes.keySet(), referencedTags.keySet()));
    return baseEntityDAO.getLocaleCatalog().getReferencedPropertiesByCodes(union);
  }
  public static Set<IPropertyDTO> getReferenceAttributesProperties(
      Map<String, IAttribute> referencedAttributes, IBaseEntityDAO baseEntityDAO)
  {
    if (referencedAttributes == null || referencedAttributes.isEmpty()) {
      return new HashSet<IPropertyDTO>();
    }
    Set<IPropertyDTO> propertyAttributes = referencedAttributes.values()
        .stream()
        .map(referencedAttribute -> {
          IPropertyDTO propertyDTO = null;
          try {
            PropertyType propertyType = IConfigMap.getPropertyType(referencedAttribute.getType());
            if (propertyType != null) {
              propertyDTO = baseEntityDAO.newPropertyDTO(referencedAttribute.getPropertyIID(),
                  referencedAttribute.getCode(), propertyType);
            }
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
          return propertyDTO;
        })
        .filter(propertyDTO -> propertyDTO != null) // TODO Filter added for
                                                    // remove null property like
                                                    // createby,
        // createon etc.
        .collect(Collectors.toSet());
    return propertyAttributes;
  }
  
  public static Set<IPropertyDTO> getReferenceTagsProperties(Map<String, ITag> referencedTags, IBaseEntityDAO baseEntityDAO)
  {
    if (referencedTags == null || referencedTags.isEmpty()) {
      return new HashSet<IPropertyDTO>();
    }
    Set<IPropertyDTO> propertyTags = referencedTags.values()
        .stream()
        .map(referencedTag -> {
          IPropertyDTO propertyDTO = null;
          try {
            PropertyType propertyType = IConfigMap.getPropertyType(referencedTag.getTagType());
            if (propertyType != null) {
              propertyDTO = baseEntityDAO.newPropertyDTO(referencedTag.getPropertyIID(), referencedTag.getCode(),
                  propertyType);
            }
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
          return propertyDTO;
        })
        .filter(propertyDTO -> propertyDTO != null)
        .collect(Collectors.toSet());
    return propertyTags;
  }
  
  public static String getBaseTypeString(BaseType baseType)
  {
    switch (baseType) {
      case ARTICLE:
        return Constants.ARTICLE_INSTANCE_BASE_TYPE;
      case ASSET:
        return Constants.ASSET_INSTANCE_BASE_TYPE;
      case TARGET:
        return Constants.MARKET_INSTANCE_BASE_TYPE;  
      case TEXT_ASSET:
        return Constants.TEXTASSET_INSTANCE_BASE_TYPE;    
      case SUPPLIER:
        return Constants.SUPPLIER_INSTANCE_BASE_TYPE; 
      default:
        break; 
    }
    return null;
  }
  
  public static String getKlassByBaseType(BaseType baseType)
  {
    switch (baseType) {
      case ARTICLE:
        return CommonConstants.ARTICLE;
      case ASSET:
        return CommonConstants.ASSET;
      case TARGET:
        return CommonConstants.MARKET; 
      case TEXT_ASSET:
        return CommonConstants.TEXT_ASSET;  
      case SUPPLIER:
        return CommonConstants.SUPPLIER;
      default:
        break;
    }
    return null;
  }
  
  public static BaseType getBaseTypeByKlass(String klass)
  {
    switch (klass) {
      case CommonConstants.ARTICLE:
        return BaseType.ARTICLE;
      case CommonConstants.ASSET:
        return BaseType.ASSET;
      case CommonConstants.TEXT_ASSET:
        return BaseType.TEXT_ASSET;
    }
    return null;
  }
  
  public static String getBaseTypeByKlassType(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return Constants.ARTICLE_INSTANCE_BASE_TYPE;
        
      case Constants.ASSET_KLASS_TYPE:
        return Constants.ASSET_INSTANCE_BASE_TYPE;
      
      case Constants.SUPPLIER_KLASS_TYPE:
        return Constants.SUPPLIER_INSTANCE_BASE_TYPE;
        
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
    }
    return null;
  }
  
  public static BaseType getBaseTypeAsBaseTypeByKlassType(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return BaseType.ARTICLE;
        
      case Constants.ASSET_KLASS_TYPE:
        return BaseType.ASSET;
      
      case Constants.SUPPLIER_KLASS_TYPE:
        return BaseType.SUPPLIER;
        
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return BaseType.TEXT_ASSET;
        
      case Constants.MARKET_KLASS_TYPE:
        return BaseType.TARGET;
    }
    return null;
  }
  
  public static List<BaseType> getBaseTypesByKlassTypes(Collection<String> klassTypes)
  {
    List<BaseType> baseTypes = new ArrayList<>();
    for(String klassType : klassTypes) {
      baseTypes.add(getBaseTypeAsBaseTypeByKlassType(klassType));
    }
    return baseTypes;
  }
  
  public static List<IAssetAttributeInstanceInformationModel> fillAssetInfoModel(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    IAssetAttributeInstanceInformationModel assetInfoModel = fillAssetInformationModel(baseEntityDTO);
    
    List<IAssetAttributeInstanceInformationModel> referencedAssets = new ArrayList<>();
    referencedAssets.add(assetInfoModel);
    return referencedAssets;
  }

  public static IAssetAttributeInstanceInformationModel fillAssetInformationModel(IBaseEntityDTO baseEntityDTO)
  {
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    IAssetAttributeInstanceInformationModel assetInfoModel = new AssetAttributeInstanceInformationModel();
    assetInfoModel.setAssetInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    HashMap<String, String> assetProperties = new HashMap<>();
    IJSONContent jsonProperties = entityExtension.getInitField("properties", new JSONContent());
   
    assetProperties.put("status", jsonProperties.getInitField("status", "0"));
    assetProperties.put("width", jsonProperties.getInitField("width", "0"));
    assetProperties.put("extension", jsonProperties.getInitField("extension", "jpg"));
    assetProperties.put("height", jsonProperties.getInitField("height", "0"));
    
    assetInfoModel.setLabel(baseEntityDTO.getBaseEntityName());
    assetInfoModel.setProperties(assetProperties);
    assetInfoModel.setThumbKey(entityExtension.getInitField("thumbKey", ""));
    assetInfoModel.setType(entityExtension.getInitField("type", ""));
    return assetInfoModel;
  }
  
  public static IAssetAttributeInstanceInformationModel fillAssetInformationModel(Long entityIId,IJSONContent entityExtension, String label)
  {
    IAssetAttributeInstanceInformationModel assetInfoModel = new AssetAttributeInstanceInformationModel();
    assetInfoModel.setAssetInstanceId(String.valueOf(entityIId));
    HashMap<String, String> assetProperties = new HashMap<>();
    IJSONContent jsonProperties = entityExtension.getInitField("properties", new JSONContent());
   
    assetProperties.put("status", jsonProperties.getInitField("status", "0"));
    assetProperties.put("width", jsonProperties.getInitField("width", "0"));
    assetProperties.put("extension", jsonProperties.getInitField("extension", "jpg"));
    assetProperties.put("height", jsonProperties.getInitField("height", "0"));
    
    assetInfoModel.setProperties(assetProperties);
    assetInfoModel.setThumbKey(entityExtension.getInitField("thumbKey", ""));
    assetInfoModel.setType(entityExtension.getInitField("type", ""));
    assetInfoModel.setLabel(label);
    return assetInfoModel;
  }

  public static String getSupplierClass(String partnerType)
  {
    switch (partnerType) {
      case Constants.SUPPLIERS_ORGANIZATION:
        return SystemLevelIds.SUPPLIERS;
      case Constants.MARKETPLACES_ORGANIZATION:
        return SystemLevelIds.MARKETPLACES;
      case Constants.DISTRIBUTORS_ORGANIZATION:
        return SystemLevelIds.DISTRIBUTORS;
      case Constants.WHOLESALERS_ORGANIZATION:
        return SystemLevelIds.WHOLESALERS;
      case Constants.TRANSLATION_AGENCY_ORGANIZATION:
        return SystemLevelIds.TRANSLATION_AGENCY;
      case Constants.CONTENT_ENRICHMENT_AGENCY_ORGANIZATION:
        return SystemLevelIds.CONTENT_ENRICHMENT_AGENCY;
      case Constants.DIGITAL_ASSET_AGENCY_ORGANIZATION:
        return SystemLevelIds.DIGITAL_ASSET_AGENCY;
    }
    return null;
  }
}
