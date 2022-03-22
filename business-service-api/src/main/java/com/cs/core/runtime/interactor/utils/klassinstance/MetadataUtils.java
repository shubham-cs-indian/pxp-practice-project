package com.cs.core.runtime.interactor.utils.klassinstance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

public class MetadataUtils {
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> convertMetadataIntoMap(Map<String, Object> metadata,
      List<String> priorityList)
  {
    Map<String, Object> convertedMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : metadata.entrySet()) {
      String categoryKey = entry.getKey();
      Object value = entry.getValue();
      if (value != null) {
        value = value.toString();
      }
      String[] categoryKeyArray = categoryKey.split(":");
      String category = null;
      String key = null;
      if (categoryKeyArray.length == 1) {
        category = CommonConstants.OTHER;
        key = categoryKeyArray[0];
      }
      else {
        category = categoryKeyArray[0].toLowerCase()
            .trim();
        if (!priorityList.contains(category)) {
          category = CommonConstants.OTHER;
        }
        key = categoryKeyArray[1];
      }
      Map<String, Object> keyValueMap = new HashMap<>();
      keyValueMap.put(key, value);
      Map<String, Object> existingCategoryMap = (Map<String, Object>) convertedMap.get(category);
      if (existingCategoryMap == null) {
        convertedMap.put(category, keyValueMap);
      }
      else {
        existingCategoryMap.putAll(keyValueMap);
      }
    }
    return convertedMap;
  }
  
  public static List<IPropertyRecordDTO> addMetadataAttributesToAssetInstanceAttributes(
      Map<String, Object> metadataMap, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails, ILocaleCatalogDAO localeCatalogDAO) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, localeCatalogDAO);
    List<String> metadataAttributeIds = IStandardConfig.StandardProperty.AssetMetaAttributeCodes;
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Set<String> referencedAttributeIds = referencedAttributes.keySet();
    
    if (metadataMap == null) {
      return new ArrayList<IPropertyRecordDTO>();
    }
    
    InputStream stream = MetadataUtils.class.getClassLoader()
        .getResourceAsStream("metadataPropertyMapping.json");
    Map<String, Object> metadataPropertyMapping = ObjectMapperUtil.readValue(stream,
        new TypeReference<Map<String, Object>>()
        {
          
        });
    
    Map<String, Object> propertyMap = (Map<String, Object>) metadataPropertyMapping
        .get(CommonConstants.PROPERTY_MAP);
    List<String> priorityList = (List<String>) metadataPropertyMapping
        .get(CommonConstants.PRIORITY);
    String localeID = baseEntityDAO.getBaseEntityDTO()
        .getBaseLocaleID();
    
    Map<String, Object> convertedMap = MetadataUtils.convertMetadataIntoMap(metadataMap,
        priorityList);
    
    List<IPropertyRecordDTO> metaDataAttributes = new ArrayList<IPropertyRecordDTO>();
    for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
      String attributeId = entry.getKey();
      Map<String, Object> mapping = (Map<String, Object>) entry.getValue();
      String finalValue = null;
      Double finalValueNumber = null;
      boolean valueFoundInPriority = false;
      for (String metadataKey : priorityList) {
        List<String> keyList = (List<String>) mapping.get(metadataKey);
        Map<String, Object> metadataKeyMap = (Map<String, Object>) convertedMap.get(metadataKey);
        boolean valueFoundInKeyList = false;
        if (metadataKeyMap != null) {
          if (keyList != null) {
            for (String key : keyList) {
              finalValue = (String) metadataKeyMap.get(key);
              if (finalValue != null && !finalValue.equals("")) {
                valueFoundInKeyList = true;
                try {
                  finalValueNumber = Double.parseDouble(finalValue);
                }
                catch (Exception e) {
                  finalValueNumber = null;
                }
                break;
              }
            }
          }
        }
        if (valueFoundInKeyList) {
          valueFoundInPriority = true;
          break;
        }
      }
      
      if (valueFoundInPriority) {
        if (metadataAttributeIds.contains(attributeId)
            && referencedAttributeIds.contains(attributeId)) {
          IAttribute attributeConfig = referencedAttributes.get(attributeId);
          IValueRecordDTO attributeInstance = propertyRecordBuilder.buildValueRecord(0l, 0l,
              finalValue, "", null, null, attributeConfig, PropertyType.ASSET_ATTRIBUTE);
          attributeInstance.setValue(finalValue);
          if (finalValueNumber != null) {
            attributeInstance.setAsNumber(finalValueNumber);
          }
          metaDataAttributes.add(attributeInstance);
        }
      }
    }
    return metaDataAttributes;
  }
}
