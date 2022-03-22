package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IAttributeCalculationUpdateDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BGPAttributeUpdateListDTO extends InitializeBGProcessDTO implements IBGPAttributeUpdateListDTO {
  
  public static final String                   CALCULATION_UPDATED_ATTRIBUTES = "calculationUpdatedAttributes";
  public static final String                   TYPE                           = "type";
  
  private List<IAttributeCalculationUpdateDTO> calculationUpdatedAttributes   = new ArrayList<>();
  private String                               type                           = "";
  
  @Override
  public List<IAttributeCalculationUpdateDTO> getCalculationUpdatedAttributes()
  {
    return calculationUpdatedAttributes;
  }
  
  @Override
  public void setCalculationUpdatedAttributes(List<IAttributeCalculationUpdateDTO> calculationUpdatedAttributes)
  {
    this.calculationUpdatedAttributes = calculationUpdatedAttributes;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONArray(CALCULATION_UPDATED_ATTRIBUTES, calculationUpdatedAttributes), JSONBuilder.newJSONField(TYPE, type));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    calculationUpdatedAttributes.clear();
    type = json.getString(TYPE);
    JSONArray jsonArray = json.getJSONArray(CALCULATION_UPDATED_ATTRIBUTES);
    for (Object jsonV : jsonArray) {
      IAttributeCalculationUpdateDTO attributeCalculationUpdateDTO = new AttributeCalculationUpdateDTO();
      attributeCalculationUpdateDTO.fromJSON(jsonV.toString());
      calculationUpdatedAttributes.add(attributeCalculationUpdateDTO);
    }
  }
  
}
