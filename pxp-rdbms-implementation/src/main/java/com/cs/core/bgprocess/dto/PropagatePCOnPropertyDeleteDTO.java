package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.bgprocess.idto.IPropagatePCOnPropertyDeleteDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

@SuppressWarnings("unchecked")
public class PropagatePCOnPropertyDeleteDTO extends SimpleDTO implements IPropagatePCOnPropertyDeleteDTO {
  
  private static final long  serialVersionUID       = 1L;
  
  public static final String DELETED_PROPERTY_CODES = "deletedPropertyCodes";
  public static final String CLASSIFIER_CODES       = "classifierCodes";
  public static final String ADDED_PROPERTY_CODES   = "addedPropertyCodes";
  
  private List<String>       deletedPropertyCodes   = new ArrayList<>();
  private Set<String>        classifierCodes        = new HashSet<>();
  private Set<String>        addedPropertyCodes     = new HashSet<String>();
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONStringArray(DELETED_PROPERTY_CODES, deletedPropertyCodes),
        JSONBuilder.newJSONStringArray(CLASSIFIER_CODES, classifierCodes),
        JSONBuilder.newJSONStringArray(ADDED_PROPERTY_CODES, addedPropertyCodes));
  }
  
  @Override
  public void setDeletedPropertyCodes(List<String> propertyCodes)
  {
    this.deletedPropertyCodes.clear();
    this.deletedPropertyCodes.addAll(propertyCodes);
  }
  
  @Override
  public List<String> getDeletedPropertyCodes()
  {
    return deletedPropertyCodes;
  }
  
  @Override
  public void setClassifierCodes(Set<String> classifierCodes)
  {
    this.classifierCodes.clear();
    this.classifierCodes.addAll(classifierCodes);
  }
  
  @Override
  public Set<String> getClassifierCodes()
  {
    return classifierCodes;
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    deletedPropertyCodes.clear();
    json.getJSONArray(DELETED_PROPERTY_CODES).forEach((propertyCode) -> {
      deletedPropertyCodes.add(propertyCode.toString());
    });
    classifierCodes.clear();
    json.getJSONArray(CLASSIFIER_CODES).forEach((iid) -> {
      classifierCodes.add((String) iid);
    });
    
    addedPropertyCodes.clear();
    json.getJSONArray(ADDED_PROPERTY_CODES).forEach((addedPropertyCode) -> {
      addedPropertyCodes.add((String) addedPropertyCode);
    });
  }

  @Override
  public void setAddedPropertyCodes(Set<String> addedPropertyCodes)
  {
    this.addedPropertyCodes = addedPropertyCodes;
  }

  @Override
  public Set<String> getAddedPropertyCodes()
  {
    return addedPropertyCodes;
  }
  
}