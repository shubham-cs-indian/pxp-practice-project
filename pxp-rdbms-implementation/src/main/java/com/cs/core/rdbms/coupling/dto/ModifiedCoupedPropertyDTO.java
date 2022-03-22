package com.cs.core.rdbms.coupling.dto;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;

public class ModifiedCoupedPropertyDTO extends SimpleDTO implements IModifiedCoupedPropertyDTO {
  
  private static final long serialVersionUID      = 1L;
  private CouplingBehavior  couplingBehavior      = CouplingBehavior.UNDEFINED;
  private CouplingType      couplingType          = CouplingType.UNDEFINED;
  private IPropertyDTO      propertyDTO           = new PropertyDTO();
  private Set<String>       classifierCodes       = new HashSet<>();
  private String            value                 = "";
  private String            valueAsHtml           = "";
  private double            valueAsNumber         = 0;
  private String            unitSymbol            = "";
  private Boolean           isDependent           = false;
  private Boolean           isValueChanged        = false;
  private Boolean           isCouplingTypeChanged = false;
  protected Set<ITagDTO>    tagValues             = new HashSet<>();
  
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    couplingBehavior = parser.getEnum(CouplingBehavior.class, COUPLING_BEHAVIOUR);
    couplingType = parser.getEnum(CouplingType.class, COUPLING_TYPE);
    value = parser.getString(VALUE);
    valueAsHtml = parser.getString(VALUE_AS_HTML);
    valueAsNumber = parser.getDouble(VALUE_AS_NUMBER);
    unitSymbol = parser.getString(UNIT_SYMBOL);
    isDependent = parser.getBoolean(IS_DEPENDENT);
    isValueChanged = parser.getBoolean(IS_VALUE_CHANGED);
    isCouplingTypeChanged = parser.getBoolean(IS_COUPLING_TYPE_CHANGED);
    
    String propertyCSE = parser.getString(PROPERTY);
    ICSEElement propertyDTOCSE = (new CSEParser()).parseDefinition(propertyCSE);
    this.propertyDTO.fromCSExpressID(propertyDTOCSE);
    parser.getJSONArray(CLASSIFIER_CODES).forEach((code) -> {
      classifierCodes.add((String) code);
    });
    
    tagValues.clear();
    for (Object recordJSON : parser.getJSONArray(TAG_VALUES)) {
      ITagDTO tagDTO = new TagDTO();
      tagDTO.fromJSON(recordJSON.toString());
      tagValues.add(tagDTO);
    }
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(COUPLING_BEHAVIOUR, couplingBehavior),
        JSONBuilder.newJSONField(COUPLING_TYPE, couplingType),
        !value.isEmpty() ? JSONBuilder.newJSONField(VALUE, value) : JSONBuilder.VOID_FIELD,
        !valueAsHtml.isEmpty() ? JSONBuilder.newJSONField(VALUE_AS_HTML, valueAsHtml) : JSONBuilder.VOID_FIELD,
        valueAsNumber != 0 ? JSONBuilder.newJSONField(VALUE_AS_NUMBER, valueAsNumber) : JSONBuilder.VOID_FIELD,
        !unitSymbol.isEmpty() ? JSONBuilder.newJSONField(UNIT_SYMBOL, unitSymbol) : JSONBuilder.VOID_FIELD,
        isDependent ? JSONBuilder.newJSONField(IS_DEPENDENT, isDependent) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONStringArray(CLASSIFIER_CODES, classifierCodes), JSONBuilder.newJSONField(PROPERTY, propertyDTO.toCSExpressID()),
        isValueChanged ? JSONBuilder.newJSONField(IS_VALUE_CHANGED, isValueChanged) : JSONBuilder.VOID_FIELD,
        isCouplingTypeChanged ? JSONBuilder.newJSONField(IS_COUPLING_TYPE_CHANGED, isCouplingTypeChanged) : JSONBuilder.VOID_FIELD,
        !tagValues.isEmpty() ? JSONBuilder.newJSONArray(TAG_VALUES, tagValues) : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public void setCouplingBehavior(CouplingBehavior couplingBehavior)
  {
    this.couplingBehavior = couplingBehavior;
  }
  
  @Override
  public CouplingBehavior getCouplingBehavior()
  {
    return couplingBehavior;
  }
  
  @Override
  public void setCouplingType(CouplingType couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public CouplingType getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setProperty(IPropertyDTO property)
  {
    this.propertyDTO = property;
  }
  
  @Override
  public IPropertyDTO getProperty()
  {
    return propertyDTO;
  }
  
  @Override
  public void setClassifierCodes(Set<String> classifierCodes)
  {
    this.classifierCodes = classifierCodes;
  }
  
  @Override
  public Set<String> getClassifierCodes()
  {
    return classifierCodes;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsNumber(double valueAsNumber)
  {
    this.valueAsNumber = valueAsNumber;
  }
  
  @Override
  public double getValueAsNumber()
  {
    return valueAsNumber;
  }
  
  @Override
  public void setUnitSymbol(String unitSymbol)
  {
    this.unitSymbol = unitSymbol;
  }
  
  @Override
  public String getUnitSymbol()
  {
    return unitSymbol;
  }
  
  @Override
  public void setIsDependent(Boolean isDependent)
  {
    this.isDependent = isDependent;
  }
  
  @Override
  public Boolean isDependent()
  {
    return isDependent;
  }

  @Override
  public void setIsValueChanged(Boolean isValueChanged)
  {
    this.isValueChanged = isValueChanged;
  }
  
  @Override
  public Boolean isValueChanged()
  {
    return isValueChanged;
  }
  
  @Override
  public void setIsCouplingTypeChanged(Boolean isCouplingTypeChanged)
  {
    this.isCouplingTypeChanged = isCouplingTypeChanged;
  }
  
  @Override
  public Boolean isCouplingTypeChanged()
  {
    return isCouplingTypeChanged;
  }

  @Override
  public void setTagValues(Set<ITagDTO> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public Set<ITagDTO> getTagValues()
  {
    return tagValues;
  }

}
