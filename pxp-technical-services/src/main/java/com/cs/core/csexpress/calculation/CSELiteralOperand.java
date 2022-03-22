package com.cs.core.csexpress.calculation;

import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.definition.CSETagValue;
import com.cs.core.data.Text;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.ElementType;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.definition.ICSETagValue;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author vallee
 */
public class CSELiteralOperand extends CSECalculationNode implements ICSELiteralOperand {

  private LiteralType literalType;
  private String strValue;
  private final List<Integer> ranges = new ArrayList<>();
  private double numValue;
  private boolean isUndefined;

  /**
   * Default constructor
   */
  public CSELiteralOperand() {
    super(OperandType.Literal);
    literalType = LiteralType.Text;
    strValue = "";
    numValue = 0.;
    isUndefined = true;
  }

  /**
   * Default constructor with type
   *
   * @param type
   */
  public CSELiteralOperand(LiteralType type) {
    super(OperandType.Literal);
    literalType = type;
    strValue = "";
    numValue = 0.;
    isUndefined = true;
  }

  /**
   * Value constructor
   *
   * @param type
   * @param literalValue
   */
  public CSELiteralOperand(LiteralType type, String literalValue) {
    super(OperandType.Literal);
    literalType = type;
    strValue = literalValue;
    numValue = Text.str2Num(literalValue.trim());
    isUndefined = false;
  }

  /**
   * Value constructor
   *
   * @param type
   * @param literalValue
   */
  public CSELiteralOperand(LiteralType type, boolean literalValue) {
    super(OperandType.Literal);
    literalType = type;
    strValue = String.format("%b", literalValue);
    numValue = (literalValue ? 1 : 0);
    isUndefined = false;
  }

  /**
   * Value constructor
   *
   * @param type
   * @param literalValue
   */
  public CSELiteralOperand(LiteralType type, double literalValue) {
    super(OperandType.Literal);
    literalType = type;
    numValue = literalValue;
    strValue = Text.num2Str(literalValue);
    isUndefined = false;
  }

  @Override
  public LiteralType getLiteralType() {
    return literalType;
  }

  @Override
  public String toString() {
    if (ranges.isEmpty()) {
      return strValue;
    } else {
      try {
        return asTagValueList().toString();
      } catch (CSFormatException ex) {
        RDBMSLogger.instance().exception(ex);
      }
      return Text.join("&", Arrays.asList(strValue.split(",")));
    }
  }

  @Override
  public long asInteger() throws CSFormatException {
    if (numValue == .0 || strValue.isEmpty()) {
      return 0;
    }
    if (literalType == LiteralType.Any) {
      return (long) Text.str2Num(strValue);
    }
    if (literalType != LiteralType.Number && literalType != LiteralType.Date
            && literalType != LiteralType.Boolean) {
      throw new CSFormatException("Cannot convert " + this + " into integer");
    }
    return (long) numValue;
  }

  @Override
  public double asDouble() throws CSFormatException {
    if (numValue == .0 || strValue.isEmpty()) {
      return .0;
    }
    if (literalType == LiteralType.Any) {
      return Text.str2Num(strValue);
    }
    if (literalType != LiteralType.Number && literalType != LiteralType.Date
            && literalType != LiteralType.Boolean) {
      throw new CSFormatException("Cannot convert " + this + " into double");
    }
    return numValue;
  }

  @Override
  public boolean asBoolean() throws CSFormatException {
    if (isUndefined) {
      return false;
    }
    if (literalType != LiteralType.Boolean) {
      return !strValue.isEmpty();
    }
    return (numValue > 0);
  }

  @Override
  public List<String> asList() throws CSFormatException {
    return Arrays.asList(strValue.split(","));
  }

  @Override
  public CSEList asTagValueList() throws CSFormatException {
    CSEList tagValueList = new CSEList();
    if(strValue.length() != 0) {
      String[] tagValueCodes = strValue.split(",");
      IntStream.range(0, tagValueCodes.length)
      .forEach((int i) -> {
        ICSETagValue tagValue = new CSETagValue(tagValueCodes[i], ranges.get(i));
        tagValueList.addElement(tagValue);
      });
    }
    return tagValueList;
  }

  @Override
  public Set<ICSERecordOperand> getRecords() {
    return new HashSet<>(); // empty
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    String json = JSONBuilder.assembleJSON(
            getUnaryOperator() != null
                    ? JSONBuilder.newJSONField("unary", getUnaryOperator().toString()) : JSONBuilder.VOID_FIELD,
            JSONBuilder.newJSONField(literalType.toString(), this.toString(), true));
    return new JSONContent(json);
  }

  /**
   * @param value overwritten value
   * @param type overwritten type
   */
  public void setValueAndType(String value, LiteralType type) {
    strValue = value;
    numValue = Text.str2Num(value.trim());
    literalType = type;
    isUndefined = false;
  }

  /**
   * @param value overwritten value
   * @param type overwritten type
   */
  public void setValueAndType(boolean value, LiteralType type) {
    strValue = String.format("%b", value);
    numValue = value ? 1 : 0;
    literalType = type;
    isUndefined = false;
  }

  /**
   * @param value overwritten value
   * @param type overwritten type
   */
  public void setValueAndType(double value, LiteralType type) {
    numValue = value;
    strValue = Text.num2Str(value);
    literalType = type;
    isUndefined = false;
  }
  
  public void setValueAndType(double value,String valueAsString, LiteralType type) {
    numValue = value;
    strValue = valueAsString;
    literalType = type;
    isUndefined = false;
  }

  @Override
  public boolean isUndefined() {
    return isUndefined;
  }

  @Override
  public void checkConsistency() throws CSFormatException {
    // nothing to test here
  }

  /**
   * @param code a tag value code to be added to the list of values range by default is 100 in that case
   */
  public void addTag(String code) {
    if (strValue.isEmpty()) {
      strValue = code;
    } else {
      strValue = Text.join(",", strValue, code);
    }
    ranges.add(100);
    isUndefined = false;
  }

  /**
   * @param tagValue a full CSE tag definition to be added to the list of values
   */
  public void addTag(CSETagValue tagValue) {
    if (strValue.isEmpty()) {
      strValue = tagValue.getCode();
    } else {
      strValue = Text.join(",", strValue, tagValue.getCode());
    }
    ranges.add(tagValue.getRange());
    isUndefined = false;
  }

  /**
   * @param tagValueList a list of tag definition to be added to the list of values
   */
  void addTagList(CSEList tagValueList) {
    tagValueList.getSubElements().forEach(element -> {
      if (element.getType() == ElementType.TAGVALUE) {
        addTag((CSETagValue) element);
      }
    });
  }

  /**
   * @param tagValueCodes a list of tag definition to be added to the list of values
   * @param tagRanges the associated list of ranges
   */
  public void addTags(List<String> tagValueCodes, List<Integer> tagRanges) {
    if (strValue.isEmpty()) {
      strValue = Text.join(",", tagValueCodes);
    } else {
      strValue = Text.join(",", strValue, Text.join(",", tagValueCodes));
    }
    ranges.addAll(tagRanges);
  }
  

  /**
   * @return list of tags codes
   */
  public ArrayList<String> getTags() {
    return new ArrayList<String>(Arrays.asList(strValue.split(",")));
  }
  
  public List<Integer> getRanges() {
    return ranges;
  }
}
