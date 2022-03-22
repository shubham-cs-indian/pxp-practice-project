package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IAttributeCalculationUpdateDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class AttributeCalculationUpdateDTO extends SimpleDTO implements IAttributeCalculationUpdateDTO {
  
  public static final String PROPERTY_IID         = "propertyIID";
  public static final String EXISTING_CALCULATION = "existingCalculation";
  public static final String ATTRIBUTE_ID         = "attributeId";
  
  private Long               propertyIID;
  private String             attributeId;
  private String             existingCalculation;
  
  public Long getPropertyIID()
  {
    return propertyIID;
  }
  
  public void setPropertyIID(Long propertyIID)
  {
    this.propertyIID = propertyIID;
  }
  
  public String getAttributeId()
  {
    return attributeId;
  }
  
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  public String getExistingCalculation()
  {
    return existingCalculation;
  }
  
  public void setExistingCalculation(String existingCalculation)
  {
    this.existingCalculation = existingCalculation;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(PROPERTY_IID, propertyIID),
        JSONBuilder.newJSONField(ATTRIBUTE_ID, attributeId), JSONBuilder.newJSONField(EXISTING_CALCULATION, existingCalculation));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    propertyIID = json.getLong(PROPERTY_IID);
    attributeId = json.getString(ATTRIBUTE_ID);
    existingCalculation = json.getString(EXISTING_CALCULATION);
  }
  
}
