package com.cs.core.bgprocess.dto;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONObject;

import com.cs.core.bgprocess.idto.IBaseEntityBulkUpdateDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.exception.CSFormatException;

public class BaseEntityBulkUpdateDTO extends BaseEntityPlanDTO implements IBaseEntityBulkUpdateDTO {
  
  private final String                  PROPERTY_RECORDS  = "LRecord";
  private final String                  ADDED_CLASSIFIERS   = "addedClassifiers";
  private final String                  REMOVED_CLASSIFIERS = "removedClassifiers";
  private final Set<IPropertyRecordDTO> propertyRecords   = new TreeSet<>();
  
  private Set<IClassifierDTO> addedClassifiers = new TreeSet<>();
  private Set<IClassifierDTO> removedClassifiers = new TreeSet<>();
  
  @Override
  public Set<IPropertyRecordDTO> getPropertyRecords()
  {
    return propertyRecords;
  }
  
  @Override
  public void setPropertyRecords(IPropertyRecordDTO... records)
  {
    this.propertyRecords.clear();
    this.propertyRecords.addAll(Arrays.asList(records));
  }
  
  public Set<IClassifierDTO> getAddedClassifiers()
  {
    return addedClassifiers;
  }
  
  @Override
  public void setAddedClassifiers(Set<IClassifierDTO> addedClassifiers)
  {
    this.addedClassifiers = addedClassifiers;
  }
  
  @Override
  public Set<IClassifierDTO> getRemovedClassifiers()
  {
    return removedClassifiers;
  }
  
  @Override
  public void setRemovedClassifiers(Set<IClassifierDTO> removedClassifiers)
  {
    this.removedClassifiers = removedClassifiers;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONArray(PROPERTY_RECORDS, propertyRecords),
        JSONBuilder.newJSONArray(ADDED_CLASSIFIERS, addedClassifiers),
        JSONBuilder.newJSONArray(REMOVED_CLASSIFIERS, removedClassifiers));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    propertyRecords.clear();
    addedClassifiers.clear();
    removedClassifiers.clear();
    for (Object recordJSON : json.getJSONArray(PROPERTY_RECORDS)) {
      JSONContentParser recordParser = new JSONContentParser((JSONObject) recordJSON);
      // Determine if the record is a Tag or a value and construct the record
      // accordingly:
      PropertyRecordDTO record = newPropertyRecordByType(recordParser);
      propertyRecords.add(record);
    }
    
    for (Object addedClassifierJSON : json.getJSONArray(ADDED_CLASSIFIERS)) {
      JSONContentParser classifierParser = new JSONContentParser((JSONObject) addedClassifierJSON);
      ClassifierDTO classifier = new ClassifierDTO();
      classifier.fromPXON(classifierParser);
      addedClassifiers.add((IClassifierDTO) classifier);
    }
    
    for (Object removedClassifierJSON : json.getJSONArray(REMOVED_CLASSIFIERS)) {
      JSONContentParser classifierParser = new JSONContentParser((JSONObject) removedClassifierJSON);
      ClassifierDTO classifier = new ClassifierDTO();
      classifier.fromPXON(classifierParser);
      removedClassifiers.add((IClassifierDTO) classifier);
    }
  }
  
  private PropertyRecordDTO newPropertyRecordByType(JSONContentParser recordParser)
      throws CSFormatException
  {
    CSEObject recordCse = (CSEObject) recordParser.getCSEElement(PXONTag.csid.toTag());
    PropertyRecordDTO record;
    switch (recordCse.getObjectType()) {
      case ValueRecord:
        record = new ValueRecordDTO();
        break;
      case TagsRecord:
        record = new TagsRecordDTO();
        break;
      
      default:
        throw new CSFormatException("Program ERROR: undefined record type from PXON " + recordCse);
    }
    record.fromPXON(recordParser);
    return record;
  }
  
}
