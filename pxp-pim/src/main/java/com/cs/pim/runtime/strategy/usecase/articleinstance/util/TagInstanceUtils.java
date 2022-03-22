package com.cs.pim.runtime.strategy.usecase.articleinstance.util;

import java.util.List;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.datarule.ITagConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
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
    return tagInstance;
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
    List<IIdRelevance> defaultValue = sectionTag.getDefaultValue();
    for (IIdRelevance iIdRelevance : defaultValue) {
      ITagInstanceValue value = new TagInstanceValue();
      value.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      value.setTagId(iIdRelevance.getTagId());
      value.setRelevance(iIdRelevance.getRelevance());
      value.setVersionId(0l);
      tagInstance.getTagValues()
          .add(value);
    }
    for (IElementConflictingValuesModel elementConflictingValue : tagConflictingValues) {
      
      ITagConflictingValuesModel tagConflictingValue = (ITagConflictingValuesModel) elementConflictingValue;
      List<IIdRelevance> tagValues = tagConflictingValue.getValue();
      if (tagValues.isEmpty() || !couplingType.equals(tagConflictingValue.getCouplingType())) {
        continue;
      }
      
      for (IIdRelevance tagValue : tagValues) {
        tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      }
      ITagConflictingValue conflictingValue = new TagConflictingValue();
      conflictingValue.setCouplingType(couplingType);
      conflictingValue.setTagValues(tagValues);
      
      String sourceType = (String) tagConflictingValue.getSourceType();
      IConflictingValueSource conflictingValueSource = PropertyInstanceUtils
          .getConflictingValueSourceBySourceType(sourceType);
      if (conflictingValueSource == null) {
        continue;
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
    String sourceType = conflictingValuesModel.getSourceType();
    IConflictingValueSource conflictingValueSource = PropertyInstanceUtils
        .getConflictingValueSourceBySourceType(sourceType);
    if (conflictingValueSource == null) {
      return null;
    }
    conflictingValueSource.setId(conflictingValuesModel.getId());
    conflictingValue.setSource(conflictingValueSource);
    return conflictingValue;
  }
  
}
