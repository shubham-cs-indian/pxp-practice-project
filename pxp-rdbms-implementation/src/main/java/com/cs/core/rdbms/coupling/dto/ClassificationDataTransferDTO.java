package com.cs.core.rdbms.coupling.dto;

import java.util.Set;
import java.util.TreeSet;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

public class ClassificationDataTransferDTO extends InitializeBGProcessDTO implements IClassificationDataTransferDTO {
  
  private static final long   serialVersionUID       = 1L;
  private Long                baseEntityIID;
  private Set<IClassifierDTO> addedOtherCLassifier   = new TreeSet<>();
  private Set<IClassifierDTO> removedOtherCLassifier = new TreeSet<>();
  private Set<String>         addedTranslations      = new TreeSet<>();
  private Boolean             translationChanged     = false;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    CSEList addedClassifierList = new CSEList();
    for (IClassifierDTO classifier : addedOtherCLassifier) {
      addedClassifierList.getSubElements().add(classifier.toCSExpressID());
    }
    
    CSEList removedClassifierList = new CSEList();
    for (IClassifierDTO classifier : removedOtherCLassifier) {
      removedClassifierList.getSubElements().add(classifier.toCSExpressID());
    }
    
    CSEList addedTranslationList = new CSEList();
    for (String locale : addedTranslations) {
      CSEObject childCse = new CSEObject(CSEObjectType.Translation);
      childCse.setCode(locale);
      addedTranslationList.addElement(childCse);
    }
    
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        baseEntityIID != null ? JSONBuilder.newJSONField(BASE_ENTITY_IID, baseEntityIID) : JSONBuilder.VOID_FIELD,
        addedOtherCLassifier.size() > 0 ? JSONBuilder.newJSONField(ADDED_OTHER_CLASSIFIERS, addedClassifierList) : JSONBuilder.VOID_FIELD,
        removedOtherCLassifier.size() > 0 ? JSONBuilder.newJSONField(REMOVED_OTHER_CLASSIFIERS, removedClassifierList) : JSONBuilder.VOID_FIELD,
        addedTranslations.size() > 0 ? JSONBuilder.newJSONField(ADDED_TRANSLATIONS, addedTranslationList) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(TRANSLATION_CHANGED, translationChanged));
    
  }
  /* !otherClassifiers.isEmpty()
  ? JSONBuilder.newJSONField(OTHER_CLASSIFIERS, classifierList)*/
  
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    super.fromJSON(parser);
    baseEntityIID = parser.getLong(BASE_ENTITY_IID);
    translationChanged = parser.getBoolean(TRANSLATION_CHANGED);
    
    addedOtherCLassifier.clear();
    String classCse = parser.getString(ADDED_OTHER_CLASSIFIERS);
    if (!classCse.isEmpty()) {
      CSEList classList = (CSEList) parser.getCSEElement(ADDED_OTHER_CLASSIFIERS);
      for (ICSEElement CSEObject : classList.getSubElements()) {
        ClassifierDTO classifier = new ClassifierDTO();
        classifier.fromCSExpressID(CSEObject);
        addedOtherCLassifier.add(classifier);
      }
    }
    removedOtherCLassifier.clear();
    String removedClassCse = parser.getString(REMOVED_OTHER_CLASSIFIERS);
    if (!removedClassCse.isEmpty()) {
      CSEList classList = (CSEList) parser.getCSEElement(REMOVED_OTHER_CLASSIFIERS);
      for (ICSEElement CSEObject : classList.getSubElements()) {
        ClassifierDTO classifier = new ClassifierDTO();
        classifier.fromCSExpressID(CSEObject);
        removedOtherCLassifier.add(classifier);
      }
    }
    
    String addedTransCse = parser.getString(ADDED_TRANSLATIONS);
    if (!addedTransCse.isEmpty()) {
      CSEList childrenCse = (CSEList) parser.getCSEElement(ADDED_TRANSLATIONS);
      for (ICSEElement childCse : childrenCse.getSubElements()) {
        addedTranslations.add(((CSEObject) childCse).getCode());
      }
    }
  }
  
  @Override
  public void setBaseEntityIID(Long baseEntityIID)
  {
    this.baseEntityIID = baseEntityIID;
  }
  
  @Override
  public Long getBaseEntityIID()
  {
    return baseEntityIID;
  }
  
  @Override
  public void setAddedOtherClassifiers(Set<IClassifierDTO> addedOtherCLassifier)
  {
    this.addedOtherCLassifier = addedOtherCLassifier;
  }
  
  @Override
  public Set<IClassifierDTO> getAddedOtherClassifiers()
  {
    return addedOtherCLassifier;
  }
  
  @Override
  public void setRemovedOtherClassifiers(Set<IClassifierDTO> removedCLassifier)
  {
    this.removedOtherCLassifier = removedCLassifier;
  }
  
  @Override
  public Set<IClassifierDTO> getRemovedOtherClassifiers()
  {
    return removedOtherCLassifier;
  }
  
  @Override
  public void setAddedTranslations(Set<String> addedTranslations)
  {
    this.addedTranslations = addedTranslations;
  }
  
  @Override
  public Set<String> getAddedTranslations()
  {
    return addedTranslations;
  }
  
  @Override
  public void setTranslationChanged(Boolean translationChanged)
  {
    this.translationChanged = translationChanged;
  }
  
  @Override
  public Boolean isTranslationChanged()
  {
    return translationChanged;
  }
  
}
