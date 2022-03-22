package com.cs.core.elastic.utils;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.dto.PopulatedPropertiesDTO;
import com.cs.core.elastic.idto.IPopulatedPropertiesDTO;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PropertyPopulationUtils {

  public static final String SELF                  = "self";

  @SuppressWarnings("unchecked")
  public static void mergeModifiedProperties(IPopulatedPropertiesDTO properties, Map<String, Object> document, String propertyObjectID,
      String localeID, Boolean isFullUpdate)
  {
    List<Map<String, Object>> propertyObjects = (List<Map<String, Object>>) document.get(
        ISearchBuilder.Fields.propertyObjects.name());
    if (propertyObjects == null) {
      propertyObjects = new ArrayList<>();
      document.put(ISearchBuilder.Fields.propertyObjects.name(), propertyObjects);
    }
    Map<String, Object> propertyObjectToUpdate = null;
    for (Map<String, Object> propertyObject : propertyObjects) {
      if (propertyObject.get(ISearchBuilder.Fields.identifier.name()).equals(propertyObjectID)) {
        propertyObjectToUpdate = propertyObject;
      }
    }
    if (propertyObjectToUpdate == null) {
      propertyObjectToUpdate = new HashMap<>();
      propertyObjectToUpdate.put(ISearchBuilder.Fields.identifier.name(), propertyObjectID);
      propertyObjects.add(propertyObjectToUpdate);
    }
    Map<String, Object> attributeInDocument = (Map<String, Object>) propertyObjectToUpdate.get(ISearchBuilder.Fields.attribute.name());

    if (attributeInDocument == null) {
      attributeInDocument = new HashMap<>();
      propertyObjectToUpdate.put(ISearchBuilder.Fields.attribute.name(), attributeInDocument);
    }
    if(isFullUpdate){
      attributeInDocument.put(localeID, properties.getDependentAttribute());
      attributeInDocument.put(ISearchBuilder.Fields.independent.name(), properties.getIndependentAttribute());
      propertyObjectToUpdate.put(ISearchBuilder.Fields.tag.name(), properties.getTags());
    }
    else{
      Map<String, Object> dependentAttributes = (Map<String, Object>) attributeInDocument.get(localeID);
      if(dependentAttributes == null){
        dependentAttributes = new HashMap<>();
        attributeInDocument.put(localeID, dependentAttributes);
      }
      dependentAttributes.putAll(properties.getDependentAttribute());

      Map<String, Object> independentAttributes = (Map<String, Object>) attributeInDocument.get(ISearchBuilder.Fields.independent.name());
      if(independentAttributes == null){
        independentAttributes = new HashMap<>();
        attributeInDocument.put(ISearchBuilder.Fields.independent.name(), independentAttributes);
      }
      independentAttributes.putAll(properties.getIndependentAttribute());

      Map<String, List<String>> tags = (Map<String, List<String>>) propertyObjectToUpdate.get(ISearchBuilder.Fields.tag.name());
      if(tags == null){
        tags = new HashMap<>();
        propertyObjectToUpdate.put(ISearchBuilder.Fields.tag.name(), tags);
      }
      tags.putAll(properties.getTags());
    }
  }

  public static IPopulatedPropertiesDTO fillAttributesAndTags(Set<IPropertyRecordDTO> propertyRecords)
  {
    IPopulatedPropertiesDTO properties = new PopulatedPropertiesDTO();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO record = (IValueRecordDTO) propertyRecord;
        IPropertyDTO property = record.getProperty();
        
        if (property.isNumeric()) {
          Double valueAsNumber = record.getAsNumber();
          if(IPropertyDTO.PropertyType.DATE == property.getPropertyType()) {
            if(valueAsNumber == 0.0) {
              properties.addIndependentAttribute(Index.number_mapping_prefix + property.getCode(), "");
            }
            else {
              properties.addIndependentAttribute(Index.number_mapping_prefix + property.getCode(), Long.valueOf(record.getValue()));
            }
          }
          else {
            properties.addIndependentAttribute(Index.number_mapping_prefix + property.getCode(), record.getValue());
          }
        }
        
        else {
          if (StringUtils.isEmpty(record.getLocaleID())) {
            properties.addIndependentAttribute(Index.text_mapping_prefix + property.getCode(), record.getValue());
          }
          else {
            properties.addDependentAttribute(Index.text_mapping_prefix + property.getCode(), record.getValue());
          }
        }
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        ITagsRecordDTO record = (ITagsRecordDTO) propertyRecord;
        List<String> codes = record.getTags().stream().map(ITagDTO::getTagValueCode).collect(Collectors.toList());
        properties.addTags(codes, record.getProperty().getCode());
      }
    }
    return properties;
  }
}
