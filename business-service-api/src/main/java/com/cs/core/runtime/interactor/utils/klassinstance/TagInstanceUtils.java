package com.cs.core.runtime.interactor.utils.klassinstance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.datarule.ITagConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.duplicatecode.TagConflictingValuesModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class TagInstanceUtils {
  
  public static ITagInstance createTagInstance(IKlassInstance klassInstance,
      IReferencedSectionElementModel sectionElement, ITag referencedTag, String currentUser) throws RDBMSException
  {
    IReferencedSectionTagModel sectionTag = (ReferencedSectionTagModel) sectionElement;
    
    if (sectionTag.getIsSkipped()) {
      return null;
    }
    
    ITagInstance tagInstance = new TagInstance();
    setConflictingValue(tagInstance, sectionTag);
    setFlatProperty(klassInstance, referencedTag, tagInstance, currentUser);
    setConflictPropertyForTag(tagInstance);
    return tagInstance;
  }
  
  public static void setConflictPropertyForTag(ITagInstance tagInstance)
  {
    List<ITagConflictingValue> conflictingValues = tagInstance.getConflictingValues();
    if (conflictingValues.size() == 1) {
      ITagConflictingValue conflictingValue = conflictingValues.get(0);
      
      List newValues = conflictingValue.getTagValues();
      tagInstance.setTagValues(newValues);
    }
  }
  
  public static Boolean shouldCreateTagInstance(ITagInstance tagInstance,
      IReferencedSectionTagModel sectionTag)
  {
    
    // if dynamic coupled then create
    if (sectionTag.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)) {
      return true;
    }
    
    // if isMandatory or isShould set to true
    if (sectionTag.getIsMandatory() || sectionTag.getIsShould()) {
      return true;
    }
    
    // if loosely or tightly coupled with default values
    if (!sectionTag.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)
        && !tagInstance.getConflictingValues()
            .isEmpty()) {
      return true;
    }
    
    return false;
  }
  
  public static void setFlatProperty(IKlassInstance klassInstance, ITag referencedTag,
      ITagInstance tagInstance, String curentUser) throws RDBMSException
  {
    tagInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagInstance.setTagId(referencedTag.getId());
    tagInstance.setKlassInstanceId(klassInstance.getId());
    tagInstance.setLastModifiedBy(curentUser);
    tagInstance.setCreatedBy(curentUser);
    
    Long timestamp = System.currentTimeMillis();
    tagInstance.setCreatedOn(timestamp);
    tagInstance.setLastModified(timestamp);
    tagInstance.setVersionTimestamp(timestamp);
    tagInstance.setVersionId(0l);
  }
  
  public static void setConflictingValue(ITagInstance tagInstance,
      IReferencedSectionTagModel sectionTag) throws RDBMSException
  {
    String couplingType = sectionTag.getCouplingType();
    List<IElementConflictingValuesModel> tagConflictingValues = sectionTag.getConflictingSources();
    
    for (IElementConflictingValuesModel tagConflictingValue : tagConflictingValues) {
      
      List<IIdRelevance> tagValues = ((TagConflictingValuesModel) tagConflictingValue).getValue();
      if (tagValues.isEmpty()) {
        continue;
      }
      
      for (IIdRelevance tagValue : tagValues) {
        tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      }
      ITagConflictingValue conflictingValue = new TagConflictingValue();
      conflictingValue.setCouplingType(couplingType);
      conflictingValue.setTagValues(tagValues);
      
      String sourceType = (String) tagConflictingValue.getSourceType();
      IConflictingValueSource conflictingValueSource;
      if (sourceType.equals(CommonConstants.ENTITY_KLASS_TYPE)) {
        conflictingValueSource = new KlassConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      }
      else {
        conflictingValueSource = new TaxonomyConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
      }
      conflictingValueSource.setId(tagConflictingValue.getId());
      conflictingValue.setSource(conflictingValueSource);
      
      List<ITagConflictingValue> listOfConflictingValues = tagInstance.getConflictingValues();
      listOfConflictingValues.add(conflictingValue);
    }
  }
  
  public static ITagConflictingValue getTagConflictValueFromConflictingValueModel(String propertyId,
      IReferencedSectionElementModel referencedElement,
      ITagConflictingValuesModel conflictingValuesModel) throws RDBMSException
  {
    ITagConflictingValue conflictingValue = new TagConflictingValue();
    conflictingValue.setId(propertyId);
    conflictingValue.setCouplingType(referencedElement.getCouplingType());
    conflictingValue.setIsMandatory(referencedElement.getIsMandatory());
    conflictingValue.setIsShould(referencedElement.getIsShould());
    
    List<IIdRelevance> tagValues = conflictingValuesModel.getValue();
    for (IIdRelevance tagValue : tagValues) {
      tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    }
    conflictingValue.setTagValues(tagValues);
    IConflictingValueSource source = null;
    String sourceType = conflictingValuesModel.getSourceType();
    if (sourceType.equals("klass")) {
      source = new KlassConflictingValueSource();
      source.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
    }
    else if (sourceType.equals("taxonomy")) {
      source = new TaxonomyConflictingValueSource();
      source.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
    }
    source.setId(conflictingValuesModel.getId());
    conflictingValue.setSource(source);
    return conflictingValue;
  }
  
  public static IContentTagInstance getTagInstanceFromTagsRecordDTO(ITagsRecordDTO tagsRecordDTO)
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(tagsRecordDTO.getNodeID());
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setTagId(tagsRecordDTO.getProperty()
        .getPropertyCode());
    
    // Tag values
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    tagsRecordDTO.getTags()
        .forEach(tagRecordDTO -> {
          ITagInstanceValue tagInstanceValue = new TagInstanceValue();
          tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
          tagInstanceValue.setId(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
          tagInstanceValue.setCode(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
          tagInstanceValue.setRelevance(tagRecordDTO.getRange());
          tagInstanceValues.add(tagInstanceValue);
        });
    tagInstance.setTagValues(tagInstanceValues);
    return tagInstance;
  }
  

  public static List<IContentTagInstance> getTagInstance(IEntityRelationDTO relation, BaseEntityDAO baseEntityDAO) throws RDBMSException
  {
    Set<ITagDTO> tagDTO = relation.getContextualObject().getContextTagValues();
    List<IContentTagInstance> contentTagInstance = new ArrayList<>();
    if (!relation.getContextualObject().getContextTagValues().isEmpty()) {
      Map<String,Long> propertyIIDMap = ConfigurationDAO.instance().getTagPropertyIdByCode(relation);
        for(ITagDTO tag:tagDTO) {
        IPropertyDTO propertyDto = ConfigurationDAO.instance().getPropertyByIID(propertyIIDMap.get(tag.getTagValueCode()));
        ITagsRecordDTO tagRecord = baseEntityDAO.newTagsRecordDTOBuilder(propertyDto).build();
        tagRecord.setTags(tag);
        contentTagInstance.add(getTagInstanceFromTagsRecordDTO(tagRecord));
      }
    }
    
    return contentTagInstance;
  }

}
