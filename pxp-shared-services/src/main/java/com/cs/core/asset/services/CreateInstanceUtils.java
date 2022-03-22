package com.cs.core.asset.services;

import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class CreateInstanceUtils {
  
  public static IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,
      Map<String, Object> configDetails, Map<String, Object> createInstanceModel) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT);
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = createAttributePropertyRecordInstance(
        propertyRecordBuilder, configDetails, createInstanceModel);
    // Create tag
    propertyRecords.addAll(
        createTagPropertyRecordInstance(propertyRecordBuilder, configDetails, createInstanceModel));
    return propertyRecords.toArray(new IPropertyRecordDTO[0]);
  }
  
  protected static List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, Map<String, Object> configDetails,
      Map<String, Object> createInstanceModel) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    Map<String, Object> object = (Map<String, Object>) configDetails.get("referencedAttributes");
    for (Object referencedAttribute : object.values()) {
      
      Map<String, Object> referencedAttributeMap = (Map<String, Object>) referencedAttribute;
      if (referencedAttributeMap.get("code")
          .equals(SystemLevelIds.ASSET_COVERFLOW_ATTRIBUTE)) {
        continue;
      }
      
      IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttributeMap);
      if (dto != null) {
        attributePropertyRecords.add(dto);
      }
    }
    addMandatoryAttribute(attributePropertyRecords, propertyRecordBuilder, configDetails,
        createInstanceModel);
    return attributePropertyRecords;
  }
  
  protected static List<IPropertyRecordDTO> createTagPropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, Map<String, Object> configDetails,
      Map<String, Object> createInstanceModel) throws Exception
  {
    Map<String, Object> map = (Map<String, Object>) configDetails.get("referencedElements");
    Set<String> referencedElementIds = map.keySet();
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) map.get("embeddedVariantContexts");
    List<String> contextTagIds = new ArrayList<>();
    if (embeddedVariantContexts != null) {
      
      Iterator<Object> iterator = embeddedVariantContexts.values()
          .iterator();
      if (iterator.hasNext()) {
        Map<String, Object> next = (Map<String, Object>) iterator.next();
        List<Map<String, Object>> tags = (List<Map<String, Object>>) next.get("tags");
        List<String> tagIds = tags.stream()
            .map(tag -> (String) tag.get("tagId"))
            .collect(Collectors.toList());
        contextTagIds.addAll(tagIds);
      }
    }
    
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    Map<String, Object> refrencedTags = (Map<String, Object>) configDetails.get("referencedTags");
    refrencedTags.values()
        .stream()
        .filter(referencedTag -> !isContextualTag((Map<String, Object>) referencedTag,
            referencedElementIds, contextTagIds))
        .forEach(referencedTag -> {
          try {
            IPropertyRecordDTO dto = propertyRecordBuilder
                .createTagsRecord((Map<String, Object>) referencedTag);
            if (dto != null) {
              tagPropertyRecords.add(dto);
            }
          }
          catch (Exception e) {
            new RuntimeException(e);
          }
        });
    
    return tagPropertyRecords;
  }
  
  protected static boolean isContextualTag(Map<String, Object> referencedTag,
      Set<String> referencedElementIds, List<String> contextTagIds)
  {
    String tagId = (String) referencedTag.get("id");
    return contextTagIds.contains(tagId) && !referencedElementIds.contains(tagId);
  }
  
  protected static void addMandatoryAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, Map<String, Object> configDetails,
      Map<String, Object> createInstanceModel) throws Exception
  {
    addNameAttribute(listOfPropertyRecordsDTO, propertyRecordBuilder,
        (String) createInstanceModel.get("name"));
  }
  
  protected static void addNameAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, String name) throws Exception
  {
    IPropertyRecordDTO nameAttribute = propertyRecordBuilder.createStandardNameAttribute(name);
    listOfPropertyRecordsDTO.add(nameAttribute);
  }
}
