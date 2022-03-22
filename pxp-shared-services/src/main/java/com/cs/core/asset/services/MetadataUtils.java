package com.cs.core.asset.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
