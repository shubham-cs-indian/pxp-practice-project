package com.cs.core.csexpress.calculation;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vallee
 */
public class CSERecordOperand extends CSECalculationNode implements ICSERecordOperand {

  private CSECouplingSource source;
  private CSEProperty property = null;
  private PropertyField propertyField = PropertyField.NONE;

  public CSERecordOperand() {
    super(OperandType.Record);
    source = new CSECouplingSource(Predefined.$entity); // default source
  }

  @Override
  public String toString() {
    return String.format("%s.%s%s", source.toString(), property.toString(),
            propertyField != PropertyField.NONE ? "." + propertyField.toString() : "");
  }

  @Override
  public Set<ICSERecordOperand> getRecords() {
    Set<ICSERecordOperand> records = new HashSet<>();
    records.add(this);
    return records;
  }

  @Override
  public ICSECouplingSource getSource() {
    return source;
  }

  @Override
  public ICSEProperty getProperty() {
    return property;
  }

  /**
   * Set the property of the record (can be either source coupling property or direct property)
   *
   * @param property
   */
  public void setProperty(CSEProperty property) {
    // Take as property on first call
    if (this.property == null) {
      this.property = property;
    } else {
      if (this.source.isPredefined() && this.source.toPredefined() == Predefined.$entity) {
        // The property first declared is a relationship source
        this.source = new CSECouplingSource(this.property);
        this.property = property;
      }
    }
  }

  @Override
  public PropertyField getPropertyField() {
    return propertyField;
  }

  /**
   * Complete the operand with a property field
   *
   * @param field
   */
  public void setPropertyField(PropertyField field) {
    this.propertyField = field;
  }

  /**
   * Set the entity of the source
   *
   * @param entity
   */
  public void setSourceEntity(CSEObject entity) {
    this.source = new CSECouplingSource(entity);
  }

  /**
   * Set the predefined entity of the source
   *
   * @param entity
   */
  public void setPredefinedSource(Predefined entity) {
    this.source = new CSECouplingSource(entity);
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    String json = JSONBuilder.assembleJSON(getUnaryOperator() != null
            ? JSONBuilder.newJSONField("unary", getUnaryOperator().toString())
            : JSONBuilder.VOID_FIELD, JSONBuilder.newJSONField("record", this.toString(), true));
    return new JSONContent(json);
  }

  @Override
  public boolean isResolved() {
    return source.isResolved() && property.containsSpecification(Keyword.$iid);
  }

  @Override
  public String getDependencyNodeID(boolean required) throws CSFormatException {
    if (required && !isResolved()) {
      throw new CSFormatException(
              "Cannot get the signature of unresolved record: " + this.toString());
    } else if (!property.containsSpecification(Keyword.$iid)) {
      throw new CSFormatException(
              "Cannot get the signature of non-existing property code: " + property.getCode());
    }
    return String.format("%s:%s", source.toEntity()
            .getSpecification(Keyword.$iid), property.getSpecification(Keyword.$iid));
  }

  @Override
  public void checkConsistency() throws CSFormatException {
    if (source.getSource() != null
            && source.getSource().getCode().isEmpty()) {
      throw new CSFormatException("Undefined code for record " + source);
    }
  }
}
