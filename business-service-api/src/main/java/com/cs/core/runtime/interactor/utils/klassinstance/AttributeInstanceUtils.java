package com.cs.core.runtime.interactor.utils.klassinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IAttributeConflictingValuesModel;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionAttributeModel;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class AttributeInstanceUtils {
  
  public static final List<String> mandatoryAttributes = new ArrayList<>(
      Arrays.asList(IStandardConfig.StandardProperty.createdonattribute.toString(),
          IStandardConfig.StandardProperty.createdbyattribute.toString(),
          IStandardConfig.StandardProperty.lastmodifiedattribute.toString(),
          IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString(),
          IStandardConfig.StandardProperty.nameattribute.toString()));
  
  public static IContentAttributeInstance createNewAttributeInstance(String klassInstanceId,
      IReferencedSectionElementModel sectionElement, IAttribute referencedAttribute,
      String language, String currentUser) throws RDBMSException
  {
    
    IReferencedSectionAttributeModel sectionAttribute = (ReferencedSectionAttributeModel) sectionElement;
    // if attribute is ImageCoverflowAttribute or skipped attribute or attribute
    // variant do nothing.
    String type = referencedAttribute.getType();
    List<String> attributesToExclude = Arrays.asList(ImageCoverflowAttribute.class.getName());
    if (attributesToExclude.contains(type) || sectionAttribute.getIsSkipped()
        || sectionAttribute.getAttributeVariantContext() != null) {
      return null;
    }
    
    IAttributeInstance newAttributeInstance = new AttributeInstance();
    setConflictingValue(newAttributeInstance, sectionAttribute);
    setFlatPropertyForAttributeInstance(klassInstanceId, newAttributeInstance, referencedAttribute,
        language, currentUser);
    setConflictPropertyForAttributeInstance(newAttributeInstance);
    return newAttributeInstance;
  }
  
  public static Boolean shouldCreateAttributeInstance(IAttributeInstance newAttributeInstance,
      IReferencedSectionAttributeModel sectionElement)
  {
    
    // if dynamic coupled then create
    if (sectionElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)) {
      return true;
    }
    
    // if isMandatory or isShould set to true
    if (sectionElement.getIsMandatory() || sectionElement.getIsShould()) {
      return true;
    }
    
    // if loosely or tightly coupled with default values
    if (!sectionElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)
        && !newAttributeInstance.getConflictingValues()
            .isEmpty()) {
      return true;
    }
    
    return false;
  }
  
  public static void setConflictingValue(IAttributeInstance newAttributeInstance,
      IReferencedSectionAttributeModel sectionAttribute)
  {
    String couplingType = sectionAttribute.getCouplingType();
    List<IElementConflictingValuesModel> attributeConflictingValues = sectionAttribute
        .getConflictingSources();
    
    for (IElementConflictingValuesModel elementConflictingValue : attributeConflictingValues) {
      
      IAttributeConflictingValuesModel attributeConflictingValue = (IAttributeConflictingValuesModel) elementConflictingValue;
      String value = attributeConflictingValue.getValue();
      String valueAsHtml = attributeConflictingValue.getValueAsHtml();
      if (value == null && valueAsHtml == null) {
        continue;
      }
      IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
      conflictingValue.setCouplingType(couplingType);
      conflictingValue.setValue(value);
      conflictingValue.setValueAsHtml(valueAsHtml);
      
      String sourceType = attributeConflictingValue.getSourceType();
      IConflictingValueSource conflictingValueSource;
      if (sourceType.equals(CommonConstants.ENTITY_KLASS_TYPE)) {
        conflictingValueSource = new KlassConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      }
      else {
        conflictingValueSource = new TaxonomyConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
      }
      conflictingValueSource.setId(attributeConflictingValue.getId());
      conflictingValue.setSource(conflictingValueSource);
      
      newAttributeInstance.getConflictingValues()
          .add(conflictingValue);
    }
  }
  
  public static void setFlatPropertyForAttributeInstance(String klassInstanceId,
      IAttributeInstance newAttributeInstance, IAttribute referencedAttribute, String language,
      String currentUser) throws RDBMSException
  {
    newAttributeInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
    newAttributeInstance.setAttributeId(referencedAttribute.getId());
    newAttributeInstance.setCode(referencedAttribute.getCode());
    newAttributeInstance.setKlassInstanceId(klassInstanceId);
    
    newAttributeInstance.setLastModifiedBy(currentUser);
    newAttributeInstance.setCreatedBy(currentUser);
    
    Long currentTimeMillis = System.currentTimeMillis();
    newAttributeInstance.setCreatedOn(currentTimeMillis);
    newAttributeInstance.setLastModified(currentTimeMillis);
    newAttributeInstance.setVersionTimestamp(currentTimeMillis);
    newAttributeInstance.setVersionId(0L);
    
    if (referencedAttribute.getIsTranslatable()) {
      newAttributeInstance.setLanguage(language);
    }
  }
  
  public static void setConflictPropertyForAttributeInstance(IAttributeInstance attributeInstance)
  {
    List<IAttributeConflictingValue> conflictingValues = attributeInstance.getConflictingValues();
    if (conflictingValues.size() == 1) {
      IAttributeConflictingValue conflictingValue = conflictingValues.get(0);
      String newValue = (String) conflictingValue.getValue();
      String newValueAsHtml = (String) conflictingValue.getValueAsHtml();
      
      updateAttributeInstanceValueProperty(attributeInstance, newValue, newValueAsHtml);
    }
  }
  
  public static void updateAttributeInstanceValueProperty(IAttributeInstance attributeInstance,
      String newValue, String newValueAsHtml)
  {
    attributeInstance.setValue(newValue);
    attributeInstance.setValueAsHtml(newValueAsHtml);
    Double valueAsNumber = null;
    try {
      valueAsNumber = Double.parseDouble(newValue);
    }
    catch (Exception e) {
      valueAsNumber = null;
    }
    attributeInstance.setValueAsNumber(valueAsNumber);
  }
  
  public static Map<String, IAttributeConflictingValuesModel> getMergedSourceIdConflictingValueMap(
      IReferencedSectionElementModel referencedElement)
  {
    Map<String, IAttributeConflictingValuesModel> mergedSourceIdConflictingValueMap = new HashMap<>();
    List<IElementConflictingValuesModel> conflictingSources = referencedElement
        .getConflictingSources();
    for (IElementConflictingValuesModel conflictingValue : conflictingSources) {
      IAttributeConflictingValuesModel attributeConflictingValue = (IAttributeConflictingValuesModel) conflictingValue;
      if (attributeConflictingValue.getSourceType()
          .equals("klass")
          || attributeConflictingValue.getSourceType()
              .equals("taxonomy")) {
        mergedSourceIdConflictingValueMap.put(attributeConflictingValue.getId(),
            attributeConflictingValue);
      }
    }
    return mergedSourceIdConflictingValueMap;
  }
  
  public static IAttributeConflictingValue getAttributeConflictValueFromConflictingValueModel(
      String propertyId, IReferencedSectionElementModel referencedElement,
      IAttributeConflictingValuesModel conflictingValuesModel)
  {
    IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
    conflictingValue.setId(propertyId);
    conflictingValue.setCouplingType(referencedElement.getCouplingType());
    conflictingValue.setIsMandatory(referencedElement.getIsMandatory());
    conflictingValue.setIsShould(referencedElement.getIsShould());
    conflictingValue.setValue(conflictingValuesModel.getValue());
    conflictingValue.setValueAsHtml(conflictingValuesModel.getValueAsHtml());
    IConflictingValueSource source = null;
    String sourceType = conflictingValuesModel.getSourceType();
    if (sourceType.equals("klass")) {
      source = new KlassConflictingValueSource();
      source.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      source.setId(conflictingValuesModel.getId());
    }
    else if (sourceType.equals("taxonomy")) {
      source = new TaxonomyConflictingValueSource();
      source.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
      source.setId(conflictingValuesModel.getId());
    }
    conflictingValue.setSource(source);
    return conflictingValue;
  }
  
  public static void checkAndAddMissingMandatoryAttributes(IKlassInstance klassInstance,
      List<String> attributeIdsList, Map<String, IAttribute> referencedAttributes,
      String currentUser, String language) throws RDBMSException
  {
    List<IContentAttributeInstance> attributeInstances = (List<IContentAttributeInstance>) klassInstance
        .getAttributes();
    Long currentTime = System.currentTimeMillis();
    for (String mandatoryAttributeId : mandatoryAttributes) {
      if (!attributeIdsList.contains(mandatoryAttributeId)) {
        IAttributeInstance newAttributeInstance = new AttributeInstance();
        AttributeInstanceUtils.setFlatPropertyForAttributeInstance(klassInstance.getId(),
            newAttributeInstance, referencedAttributes.get(mandatoryAttributeId), language,
            currentUser);
        attributeInstances.add(newAttributeInstance);
        
        String defaultValue = "";
        String entityName = klassInstance.getName();
        if (mandatoryAttributeId.equals(IStandardConfig.StandardProperty.nameattribute.toString())
            && entityName != null) {
          defaultValue = entityName;
        }
        else if (mandatoryAttributeId
            .equals(IStandardConfig.StandardProperty.createdonattribute.toString())) {
          defaultValue = String.valueOf(currentTime);
          klassInstance.setCreatedOn(currentTime);
        }
        else if (mandatoryAttributeId
            .equals(IStandardConfig.StandardProperty.lastmodifiedattribute.toString())) {
          defaultValue = String.valueOf(currentTime);
          klassInstance.setLastModified(currentTime);
        }
        else if (mandatoryAttributeId
            .equals(IStandardConfig.StandardProperty.createdbyattribute.toString())) {
          defaultValue = currentUser;
          klassInstance.setCreatedBy(defaultValue);
        }
        else if (mandatoryAttributeId
            .equals(IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString())) {
          defaultValue = currentUser;
          klassInstance.setLastModifiedBy(defaultValue);
        }
        newAttributeInstance.setValue(defaultValue);
      }
    }
  }
  
  public static Boolean isSpecialAttribute(String type)
  {
    Boolean isSpecialAttribute = false;
    if (type.equals(CreatedOnAttribute.class.getName())
        || type.equals(LastModifiedAttribute.class.getName())
        || type.equals(CreatedByAttribute.class.getName())
        || type.equals(LastModifiedByAttribute.class.getName())
        || type.equals(NameAttribute.class.getName())) {
      isSpecialAttribute = true;
    }
    
    return isSpecialAttribute;
  }
  
  public static Boolean isSpecialAttributeExceptName(String type)
  {
    Boolean isSpecialAttribute = false;
    if (type.equals(CreatedOnAttribute.class.getName())
        || type.equals(LastModifiedAttribute.class.getName())
        || type.equals(CreatedByAttribute.class.getName())
        || type.equals(LastModifiedByAttribute.class.getName())) {
      isSpecialAttribute = true;
    }
    
    return isSpecialAttribute;
  }
  
  public static IContentAttributeInstance createSpecialAttributeInstance(
      IKlassInstance klassInstance, IAttribute referencedAttribute, String currentUser,
      String language) throws RDBMSException
  {
    String type = referencedAttribute.getType();
    IAttributeInstance newAttributeInstance = new AttributeInstance();
    if (type.equals(CreatedOnAttribute.class.getName())) {
      newAttributeInstance.setValue(String.valueOf(klassInstance.getCreatedOn()));
    }
    else if (type.equals(LastModifiedAttribute.class.getName())) {
      newAttributeInstance.setValue(String.valueOf(klassInstance.getLastModified()));
    }
    else if (type.equals(CreatedByAttribute.class.getName())) {
      newAttributeInstance.setValue(klassInstance.getCreatedBy());
    }
    else if (type.equals(LastModifiedByAttribute.class.getName())) {
      newAttributeInstance.setValue(currentUser);
    }
    else if (type.equals(NameAttribute.class.getName())) {
      newAttributeInstance.setValue(klassInstance.getName());
    }
    
    AttributeInstanceUtils.setFlatPropertyForAttributeInstance(klassInstance.getId(),
        newAttributeInstance, referencedAttribute, language, currentUser);
    return newAttributeInstance;
  }
  
  public static IContentAttributeInstance createSpecialAttributeInstanceExcludingNameAttribute(
      IKlassInstance klassInstance, IAttribute referencedAttribute, String currentUser,
      String language) throws RDBMSException
  {
    String type = referencedAttribute.getType();
    IAttributeInstance newAttributeInstance = new AttributeInstance();
    if (type.equals(CreatedOnAttribute.class.getName())) {
      newAttributeInstance.setValue(String.valueOf(klassInstance.getCreatedOn()));
    }
    else if (type.equals(LastModifiedAttribute.class.getName())) {
      newAttributeInstance.setValue(String.valueOf(klassInstance.getLastModified()));
    }
    else if (type.equals(CreatedByAttribute.class.getName())) {
      newAttributeInstance.setValue(klassInstance.getCreatedBy());
    }
    else if (type.equals(LastModifiedByAttribute.class.getName())) {
      newAttributeInstance.setValue(currentUser);
    }
    
    AttributeInstanceUtils.setFlatPropertyForAttributeInstance(klassInstance.getId(),
        newAttributeInstance, referencedAttribute, language, currentUser);
    return newAttributeInstance;
  }
}
