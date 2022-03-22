package com.cs.core.elastic.idto;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPopulatedPropertiesDTO {

  Map<String, Object> getIndependentAttribute();

  Map<String, String> getDependentAttribute();

  Map<String, List<String>> getTags();

  void addTags(List<String> tagValues, String tagId);

  void addDependentAttribute(String key, String value);

  void addIndependentAttribute(String key, Object value);
}
