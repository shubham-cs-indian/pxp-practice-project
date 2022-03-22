package com.cs.core.elastic.dto;

import com.cs.core.elastic.idto.IPopulatedPropertiesDTO;

import java.util.*;

public class PopulatedPropertiesDTO implements IPopulatedPropertiesDTO {

  private Map<String, Object>       independentAttribute = new HashMap<>();
  private Map<String, String>       dependentAttribute   = new HashMap<>();
  private Map<String, List<String>> tags                 = new HashMap<>();

  @Override
  public Map<String, Object> getIndependentAttribute()
  {
    return independentAttribute;
  }

  @Override
  public Map<String, String> getDependentAttribute()
  {
    return dependentAttribute;
  }

  @Override
  public Map<String, List<String>> getTags()
  {
    return tags;
  }


  @Override
  public void addTags(List<String> tagValues, String tagId)
  {
    tags.put(tagId, tagValues);
  }

  @Override
  public void addDependentAttribute(String key, String value)
  {
    dependentAttribute.put(key, value);
  }
  @Override
  public void addIndependentAttribute(String key, Object value)
  {
    independentAttribute.put(key, value);
  }
}
