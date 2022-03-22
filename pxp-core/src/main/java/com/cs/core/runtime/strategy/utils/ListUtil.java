package com.cs.core.runtime.strategy.utils;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {
  
  public static <T extends IEntity> Map<String, T> convertListToIdInstanceMap(List<T> instances)
  {
    Map<String, T> idInstanceMap = new HashMap<String, T>();
    for (T instance : instances) {
      idInstanceMap.put(instance.getId(), instance);
    }
    return idInstanceMap;
  }
}
