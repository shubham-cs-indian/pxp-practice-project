package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * DTO representation of a value record
 *
 * @author vallee
 */
public final class ValueRecordDTO extends PropertyRecordDTO implements IValueRecordDTO {
  
  private static final String HTML               = PXONTag.html.toTag();
  private static final String NUMBER             = PXONTag.number.toTag();
  private static final String UNIT_SYMBOL        = PXONTag.unit.toTag();
  private static final String VALUE              = PXONTag.value.toTag();
  private static final String CONTEXTUAL_OBJECT  = PXONTag.cxtual.toJSONContentTag();
  private static final String CALCULATION_FIELD  = PXONTag.calc.toPrivateTag();
  
  private long                valueIID           = 0L;
  private String              value              = "";
  private String              html               = "";
  private double              number             = 0;
  private String              unitSymbol         = "";
  private String              localeID           = "";
  private ContextualDataDTO   contextualData     = new ContextualDataDTO();
  private String              calculation        = "";
  private boolean             changedCalculation = false; // transient information used to identify changed calculation


  /**
   * Enabled default constructor
   */
  public ValueRecordDTO()
  {
  }
  
  /**
   * Value constructor with minimal information
   *
   * @param entityIID
   * @param property
   * @param value
   */
  public ValueRecordDTO(long entityIID, IPropertyDTO property, String value)
  {
    super(entityIID, property);
    setChanged(value.isEmpty() || !this.value.equals(value));
    this.value = value;
  }
  
  /**
   * Value Constructor
   *
   * @param entityIID
   * @param property
   * @param coupling
   * @param recordStatus
   * @param html
   * @param valueIID
   * @param number
   * @param value
   * @param unitSymbol
   * @param localeID
   * @param contextualData
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ValueRecordDTO(long entityIID, long valueIID, IPropertyDTO property, CouplingType coupling,
      RecordStatus recordStatus, String value, String html, double number, String unitSymbol,
      String localeID, IContextualDataDTO contextualData) throws CSFormatException
  {
    super(entityIID, property, coupling, recordStatus);
    setChanged(value.isEmpty() || !this.value.equals(value) || !this.html.equals(html) || !this.localeID.equals(localeID));
    this.valueIID = valueIID;
    this.html = html;
    this.number = number;
    this.unitSymbol = unitSymbol;
    this.localeID = localeID;
    this.value = value;
    if (contextualData != null)
      this.contextualData = (ContextualDataDTO) contextualData;
  }
  
  /**
   * Constructor from a query result containing value record information
   *
   * @param parser
   * @param property
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ValueRecordDTO(IResultSetParser parser, IPropertyDTO property)
      throws SQLException, CSFormatException
  {
    super(parser, property);
    this.valueIID = parser.getLong("valueIID");
    this.value = parser.getString("value");
    this.html = parser.getString("asHtml");
    this.number = parser.getDouble("asNumber");
    this.localeID = parser.getString("localeid");
    this.unitSymbol = parser.getString("unitSymbol");
    this.calculation = parser.getString("calculation");
    this.changedCalculation = false;
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject gcse = initCSExpressID(ICSEElement.CSEObjectType.ValueRecord);
    if ( exportIID )
      gcse.setIID( valueIID);
    if (localeID != null && !localeID.isEmpty())
      gcse.setSpecification(Keyword.$locale, localeID);
    if (isVersionable)
      gcse.setSpecification(Keyword.$isver, Keyword.$true);
    
    return gcse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject vcse = (CSEObject)cse;
    super.fromCSExpressID(cse);
    localeID = ((CSEObject) cse).getSpecification(Keyword.$locale);
    valueIID = vcse.getIID();
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    value = parser.getString(VALUE);
    html = parser.getString(HTML);
    number = parser.getDouble(NUMBER);
    unitSymbol = parser.getString(UNIT_SYMBOL);
    if ( parser.toJSONObject().containsKey(CONTEXTUAL_OBJECT) ) {
      CSEObject cxtual = (CSEObject)parser.getCSEElement(CONTEXTUAL_OBJECT);
      contextualData.fromCSExpressID(cxtual);
    }
    else {
      contextualData = new ContextualDataDTO();
    }
    calculation = parser.getString(CALCULATION_FIELD);
    this.changedCalculation = false;
  }
  
  private StringBuffer valueToJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(VALUE, value, true),
        !html.isEmpty() ? JSONBuilder.newJSONField(HTML, html, true) : JSONBuilder.VOID_FIELD,
        number != 0 ? JSONBuilder.newJSONField(NUMBER, number) : JSONBuilder.VOID_FIELD,
        !unitSymbol.isEmpty() ? JSONBuilder.newJSONField(UNIT_SYMBOL, unitSymbol)
            : JSONBuilder.VOID_FIELD,
        !contextualData.isNull()
            ? JSONBuilder.newJSONField(CONTEXTUAL_OBJECT, contextualData.toCSExpressID())
            : JSONBuilder.VOID_FIELD,
        !calculation.isEmpty() ? JSONBuilder.newJSONField(CALCULATION_FIELD, calculation)
            : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(), valueToJSONBuffer());
  }
  
  @Override
  public long getEntityIID()
  {
    return getIID(); // the owner of the value record
  }
  
  @Override
  public long getValueIID()
  {
    return valueIID;
  }
  
  /**
   * @param valueIID
   *          overwritten value IID (/!\ NOT the propertyIID record IID as
   *          managed by inherited setIID())
   */
  public void setValueIID(long valueIID)
  {
    this.valueIID = valueIID;
  }
  
  @Override
  public boolean hasChangedCalculation()
  {
    return changedCalculation;
  }
  
  /**
   * @param cxtObject
   *          overwritten contextual data
   */
  public void setContextualData(ContextualDataDTO cxtObject)
  {
    this.contextualData = cxtObject;
  }
  
  @Override
  public boolean isEmpty()
  {
    return (this.value == null || this.value.isEmpty());
  }
  
  @Override
  public IContextualDataDTO getContextualObject()
  {
    return contextualData;
  }
  
  @Override
  public String getValue()
  {
    return this.value;
  }
  
  @Override
  public void setValue(String value)
  {
    setChanged(!this.value.equals(value) ? true : isChanged());
    this.value = value;
  }
  
  @Override
  public double getAsNumber()
  {
    return number;
  }
  
  @Override
  public void setAsNumber(double number)
  {
    setChanged(this.number != number ? true : isChanged());
    this.number = number;
  }
  
  @Override
  public String getAsHTML()
  {
    return html;
  }
  
  @Override
  public void setAsHTML(String html)
  {
    setChanged(!this.html.equals(html) ? true : isChanged());
    this.html = html;
  }
  
  @Override
  public String getLocaleID()
  {
    return localeID;
  }
  
  /**
   * @param localeID
   *          overwritten locale ID
   */
  public void setLocaleID(String localeID)
  {
    setChanged(!this.localeID.equals(localeID) ? true : isChanged());
    this.localeID = localeID;
  }
  
  @Override
  public String getUnitSymbol()
  {
    return unitSymbol;
  }
  
  @Override
  public void setUnitSymbol(String unitSymbol)
  {
    setChanged(!this.unitSymbol.equals(unitSymbol) ? true : isChanged());
    this.unitSymbol = unitSymbol;
  }
  
  @Override
  public void setContentFrom(IPropertyRecordDTO source) throws CSFormatException
  {
    ValueRecordDTO valSource = (ValueRecordDTO) source;
    this.setIID(valSource.getIID());
    this.setValueIID(valSource.getValueIID());
    this.setLocaleID(valSource.getLocaleID());
    this.contextualData = valSource.contextualData;
    this.setValue(valSource.getValue());
    this.setAsHTML(valSource.getAsHTML());
    this.setAsNumber(valSource.getAsNumber());
    this.setUnitSymbol(valSource.getUnitSymbol());
    this.addCalculation(valSource.calculation);
    setChanged(!this.value.equals(valSource.getValue()) ? true : isChanged());
    setChanged(!this.html.equals(valSource.getAsHTML()) ? true : isChanged());
    setChanged(this.number != valSource.getAsNumber() ? true : isChanged());
    setChanged(!this.unitSymbol.equals(valSource.getUnitSymbol()) ? true : isChanged());
    setChanged(!this.localeID.equals(valSource.getLocaleID()) ? true : isChanged());
  }
  
  @Override
  public boolean isCalculated()
  {
    return !calculation.isEmpty();
  }
  
  @Override
  public void addCalculation(String calculation) throws CSFormatException
  {
    if (calculation.isEmpty()) {
      this.calculation = calculation;
    }
    else {
      (new CSEParser())
          .parseCalculation(calculation);
      this.calculation = calculation;
    }
  }
  
  @Override
  public String getCalculation()
  {
    return calculation;
  }
  
  /**
   * Check the consistency rules concerning this value record
   *
   * @throws RDBMSException
   *           for found inconsistencies
   */
  public void checkContentConsistency() throws RDBMSException
  {
    value = value.trim();
    if (value.isEmpty())
      return;
    IPropertyDTO.PropertyType propertyType = property.getPropertyType();
    switch (propertyType) {
      case AUTO:
      case CALCULATED:
      case CONCATENATED:
      case ASSET_ATTRIBUTE:
        return; // No Control for std attribute/auto/calculated/concatenated
        
      case DATE:
        if (number <= 0) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              "Value =" + value + ",number=" + number + " must be defined as positive integer");
        }
        else if (!(html.isEmpty() && unitSymbol.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " html=" + html + ",unitSymbol=" + unitSymbol + " must be empty");
        }
        break;
      
      case HTML:
        if (!(number == 0 && unitSymbol.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " number=" + number + ",unitSymbol=" + unitSymbol + " must be empty");
        }
        break;
      
      case MEASUREMENT:
        if (unitSymbol.isEmpty() || !value.matches("-?\\d+(\\.\\d+)?.*")) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              "Value =" + value + ",unitSymbol=" + unitSymbol + " must be defined and consistent");
        }
        else if (!(html.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " html=" + html + " must be empty");
        }
        break;
      
      case NUMBER:
        if (!value.matches("-?\\d+(\\.\\d+)?")) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              "Value " + value + " must be a number");
        }
        else if (!(html.isEmpty() && unitSymbol.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " html=" + html + ",unitSymbol=" + unitSymbol + " must be empty");
        }
        break;
      
      case PRICE:
        if (number < 0 || unitSymbol.isEmpty() || !value.matches("\\d+(\\.\\d+)?.*")) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              "Value =" + value + ",number=" + number + ",unitSymbol=" + unitSymbol
                  + " must be defined with a positive and consistent price");
        }
        else if (!(html.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " html=" + html + " must be empty");
        }
        break;
      
      case TEXT:
        if (!(html.isEmpty() && number == 0 && unitSymbol.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType, " html=" + html
              + ",number=" + number + ",unitSymbol=" + unitSymbol + " must be empty");
        }
        break;
      
      case BOOLEAN:
        if (!((value.equals(IStandardConfig.TRUE) && number == 1)
            || (number == 0 && value.equals(IStandardConfig.FALSE)))) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              "Value =" + value + ",number=" + number + " must have consistent boolean value");
        }
        else if (!(html.isEmpty() && unitSymbol.isEmpty())) {
          throw new RDBMSException(10005, "Consistency check of " + propertyType,
              " html=" + html + ",unitSymbol=" + unitSymbol + " must be empty");
        }
        break;
      
      default:
        throw new RDBMSException(10005, "Consistency check",
            "Incorrect property type: " + propertyType);
    }
  }
  
  @Override
  public int compareTo(Object t)
  {
    int compare = super.compareTo(t); // comparison by propertyIID
    if (compare != 0) {
      return compare;
    }
    ValueRecordDTO that = (ValueRecordDTO) t;
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    if (valueIID != 0 && that.valueIID != 0)
      return compareToBuilder.append(valueIID, that.valueIID).toComparison();
    compare = compareToBuilder.append(this.localeID, that.localeID).toComparison();
    if (compare != 0)
      return compare;
    compare = this.contextualData.compareTo(that.contextualData);
    if (compare != 0)
      return compare;
    return 0;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    boolean test = super.equals(obj);
    if (!test)
      return false;
    ValueRecordDTO that = (ValueRecordDTO) obj;
    if (valueIID != 0 && that.valueIID != 0)
      return (valueIID == that.valueIID);
    if (!new EqualsBuilder().append(this.localeID,that.localeID).isEquals())
      return false;
    return this.contextualData.equals(that.contextualData);
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(7, 13).append(super.hashCode())
        .append(valueIID)
        .append(localeID)
        .append(contextualData)
        .build();
  }
  
  /**
   * implementation of IValueRecordDTOBuilder
   * @author Janak.Gurme
   *
   */
  public static class ValueRecordDTOBuilder implements IValueRecordDTOBuilder {

    private final ValueRecordDTO valueRecordDTO;
    
    /**
     * minimal fields to prepare ValueRecordDTO
     * @param entityIID
     * @param property
     * @param value
     */
    public ValueRecordDTOBuilder(long entityIID, IPropertyDTO property, String value)
    {
      valueRecordDTO = new ValueRecordDTO(entityIID, property, value);
    }
    
    @Override
    public IValueRecordDTOBuilder valueIID(long valueIID)
    {
      valueRecordDTO.valueIID = valueIID;
      return this;
    }
    
    @Override
    public IValueRecordDTOBuilder contextDTO(IContextDTO contextDTO)
    {
      valueRecordDTO.contextualData.setContextCode(contextDTO.getCode());
      return this;
    }

    @Override
    public IValueRecordDTOBuilder asNumber(double number)
    {
      valueRecordDTO.setAsNumber(number);
      return this;
    }

    @Override
    public IValueRecordDTOBuilder asHTML(String html)
    {
      valueRecordDTO.setAsHTML(html);
      return this;
    }

    @Override
    public IValueRecordDTOBuilder localeID(String localeID)
    {
      valueRecordDTO.setLocaleID(localeID);
      return this;
    }

    @Override
    public IValueRecordDTOBuilder unitSymbol(String unitSymbol)
    {
      valueRecordDTO.setUnitSymbol(unitSymbol);
      return this;
    }

    @Override
    public IValueRecordDTOBuilder addCalculation(String calculation) throws CSFormatException
    {
      valueRecordDTO.addCalculation(calculation);
      return this;
    }
    

    @Override
    public IValueRecordDTOBuilder recordStatus(RecordStatus recordStatus)
    {
      valueRecordDTO.recordStatus = recordStatus;
      return this;
    }

    @Override
    public IValueRecordDTOBuilder couplingType(CouplingType couplingType)
    {
      valueRecordDTO.couplingType = couplingType;
      return this;
    }

    @Override
    public IValueRecordDTOBuilder isVersionable(boolean isVersionable)
    {
      valueRecordDTO.isVersionable = isVersionable;
      return this;
    }

    @Override
    public IValueRecordDTO build() {
      return valueRecordDTO; 
    }

  }

}
