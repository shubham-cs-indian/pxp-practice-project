package com.cs.core.rdbms.services.resolvers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.calculation.CSECalculation;
import com.cs.core.csexpress.calculation.CSECalculationNode;
import com.cs.core.csexpress.calculation.CSELiteralOperand;
import com.cs.core.csexpress.calculation.CSERangeLiteralOperand;
import com.cs.core.csexpress.calculation.CSERecordOperand;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperandType;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.UnaryOperator;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand.PropertyField;
import com.cs.core.technical.icsexpress.definition.ICSETagValue;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 *
 * @author frva
 */
class CalculatedConditionResolver extends AbstractSearchEntityResolver {
  
  private final List<String> inheritanceSchema;
  private Boolean isContextSearch;

  public CalculatedConditionResolver(RDBMSConnection connection, Set<String> catalogCodes, Set<BaseType> baseTypes, List<String> localeIDs,
      Boolean isContextSearch, Set<String> organizationCodes, Set<Long> baseEntityIIDs, Set<String> endpointCodes)
  {
    super( connection, catalogCodes, baseTypes, "tmpCal", organizationCodes, baseEntityIIDs, endpointCodes);
    inheritanceSchema = localeIDs;
    this.isContextSearch = isContextSearch;
  }
  
  /**
   * @param condition
   *          with logical operator
   * @return the result table with the compound
   */
  private String resolveCompoundCondition(CSECalculation condition) throws RDBMSException, SQLException, CSFormatException
  {
    CSECalculation leftOperand = (CSECalculation) condition.getOperands()[0];
    String leftResultTable = resolveCalculatedCondition(leftOperand);

    CSECalculation rightOperand = (CSECalculation) condition.getOperands()[1];
    String rightResultTable = resolveCalculatedCondition(rightOperand);

    String resultTable = resolveLogicalOperator(condition.getOperator(), leftResultTable, rightResultTable);
    if (condition.getUnaryOperator() == UnaryOperator.not)
      resultTable = tempTableDas.excludeResults(getBaseQueryResult(), resultTable);
    return resultTable;
  }
  
  /**
   * @param operand
   * @return the property of a record operand
   * @throws RDBMSException
   * @throws SQLException
   */
  private PropertyDTO getProperty(CSERecordOperand operand) throws RDBMSException, SQLException
  {
    String code = operand.getProperty().getCode();
    if (code.equals("search")) {
     return new PropertyDTO(0l, code, PropertyType.AUTO);
    }
    return ConfigurationDAO.instance().getPropertyByCode( currentConnection, code);
  }
  
  /**
   * @param condition
   * @param leftProperty
   * @param leftRecord
   * @param rightProperty
   * @param rightRecord
   * @return the result of comparing two attributes
   */
  private String resolveTwoAttributesCondition(CSECalculation condition, IPropertyDTO leftProperty, CSERecordOperand leftRecord,
      IPropertyDTO rightProperty, CSERecordOperand rightRecord) throws RDBMSException, SQLException
  {
    PropertyField leftPropertyField = leftRecord.getPropertyField();
    PropertyField rightPropertyField = rightRecord.getPropertyField();

    DynamicViewDAS viewDas = new DynamicViewDAS( currentConnection, inheritanceSchema);
    String valueRecordView = viewDas.createDynamicValueView();
    LiteralType rightLiteralType = rightProperty.getPropertyType()
        .getLiteralType();
    LiteralType leftLiteralType = leftProperty.getPropertyType()
        .getLiteralType();
    
    String format = String.format("a.%s", Arrays.asList(LiteralType.Date, LiteralType.Number)
        .contains(rightLiteralType) || leftPropertyField.equals(PropertyField.number) ? "asNumber" : "value");
    String format2 = String.format("b.%s", Arrays.asList(LiteralType.Date, LiteralType.Number)
        .contains(leftLiteralType) || rightPropertyField.equals(PropertyField.number)? "asNumber" : "value");
    String comparisonCondition = getComparisonQuery(condition, format, format2);
    
    String searchQuery = String.format(
        "select a.entityiid as baseentityiid from %s a"
            + " join %s b on (a.entityIID = b.entityIID and a.propertyIID = %d and b.propertyIID = %d)",
         valueRecordView, valueRecordView, leftProperty.getIID(), rightProperty.getIID());
    
    String searchWithCondition = comparisonCondition != null ? String.format("%s where %s", searchQuery,comparisonCondition) : searchQuery;
    return tempTableDas.createTable(searchWithCondition);
  }
  
  private String getComparisonQuery(CSECalculation condition, String leftParameter, String rightParameter) throws RDBMSException
  {

    switch (condition.getOperator()) {
      case Equals:
        boolean isNullQuery = StringUtils.strip(rightParameter, "'").isEmpty();
        String format = isNullQuery ? "" : "%s = %s";
        return String.format(format, leftParameter, rightParameter);
      
      case Differs:
        isNullQuery = StringUtils.strip(rightParameter, "'").isEmpty();
        format = isNullQuery ? "" : "%s <> %s";
        return String.format(format, leftParameter, rightParameter);
        
      case GT:
        return String.format("%s > %s", leftParameter, rightParameter);
      case LT:
        return String.format("%s < %s", leftParameter, rightParameter);
      case LTE:
        return String.format("%s <= %s", leftParameter, rightParameter);
      case GTE:
        return String.format("%s >= %s", leftParameter, rightParameter);
      case Like:
        return String.format("%s like %s", leftParameter, rightParameter);
      case Contains:
        rightParameter = "'%" + Text.unescapeQuotedString(rightParameter) + "%'";
        return String.format("%s ilike %s", leftParameter, rightParameter);
      default:
        throw new RDBMSException(100, "Program", "Unsupported operator in " + condition);
    }
  }
  
  private String getTagComparisonQuery(CSECalculation conditionOnTag, ICSECalculationNode tagValueConditionObj, String tagColumnName)
      throws RDBMSException, CSFormatException
  {
    String tagValueFormat =  "(" + tagColumnName + "-> '%s')::int";
    
    switch (tagValueConditionObj.getType()) {
      case Calculation:
        // this usecase is for range tag
        CSECalculation tagValueCalculation = (CSECalculation) tagValueConditionObj;
        if (conditionOnTag.getOperator()
            .equals(Operator.Equals)) { // if the comparison is for single tag.
          
          Operator operator = tagValueCalculation.getOperator();
          if (operator.equals(Operator.Between)) {
            // if the comparison is for the tag values.
            ICSECalculationNode[] rangeConditions = tagValueCalculation.getOperands();
            CSERecordOperand leftOperand = (CSERecordOperand) rangeConditions[0];
            CSERangeLiteralOperand rightOperand = (CSERangeLiteralOperand) rangeConditions[1];
            
            String tagValueCode = leftOperand.getProperty()
                .getCode();
            String tagValueCondition = String.format(tagValueFormat, tagValueCode);
            
            long from = Math.round(rightOperand.getLowerBound());
            long to = Math.round(rightOperand.getUpperBound());
            String rangeConditionFormat = String.format(" %s >= %d and %s <= %d ", tagValueCondition, from, tagValueCondition, to);
            return String.format("%s ?& ARRAY['%s'] and %s",tagColumnName, tagValueCode, rangeConditionFormat);
          }
        }
        return "";
      case Literal:
        CSELiteralOperand literalValue = (CSELiteralOperand) tagValueConditionObj;
        if (literalValue.isUndefined()) {
          return literalValue.asString();
        }
        String tagValueCodes = Text.join(",", literalValue.asList(), "'%s'");
        
        List<ICSEElement> subElements = literalValue.asTagValueList()
            .getSubElements();
        Map<String, String> codeVsRange = subElements.stream()
            .collect(Collectors.toMap(x -> ((ICSETagValue) x).getCode(), x -> x.getSpecification(Keyword.$range)));
        StringBuilder tagValueConditions = new StringBuilder();
        String sep = " and ";
        
        for (Entry<String, String> element : codeVsRange.entrySet()) {
          String tagValueCondition = String.format(tagValueFormat, element.getKey());
          tagValueConditions.append(getComparisonQuery(conditionOnTag, tagValueCondition, element.getValue()))
              .append(sep);
        }
        tagValueConditions.setLength(tagValueConditions.length() - sep.length());
        
        return String.format("%s ?& ARRAY[%s] and %s", tagColumnName,tagValueCodes, tagValueConditions);
      
      default:
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
  }
  
  /**
   * @param condition
   * @param leftProperty
   * @param leftRecord
   * @param rightProperty
   * @param rightRecord
   * @return the result of comparing two tags
   */
  private String resolveTwoTagsCondition(CSECalculation condition, IPropertyDTO leftProperty, CSERecordOperand leftRecord, IPropertyDTO rightProperty,
      CSERecordOperand rightRecord)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  /**
   * @param condition
   * @param leftRecord
   * @param rightRecord
   * @return the result of comparing two records
   * @throws RDBMSException
   * @throws SQLException
   */
  private String resolveTwoRecordsCondition(CSECalculation condition, CSERecordOperand leftRecord, CSERecordOperand rightRecord)
      throws RDBMSException, SQLException, CSFormatException
  {
    IPropertyDTO leftProperty = getProperty(leftRecord);
    IPropertyDTO rightProperty = getProperty(rightRecord);
    if (leftProperty.getSuperType() != rightProperty.getSuperType())
      throw new RDBMSException(100, "Program", "Cannot execute records comparison on different types");
    if (leftProperty.getSuperType() == SuperType.ATTRIBUTE)
      return resolveTwoAttributesCondition(condition, leftProperty, leftRecord, rightProperty, rightRecord);
    if (leftProperty.getSuperType() == SuperType.TAGS)
      return resolveTwoTagsCondition(condition, leftProperty, leftRecord, rightProperty, rightRecord);
    throw new RDBMSException(100, "Program", "Cannot execute records comparison from property: " + leftProperty.toPXON());
  }
  
  private String resolveTagCondition(CSECalculation condition, IPropertyDTO property, ICSECalculationNode value) throws SQLException, RDBMSException, CSFormatException
  {
    String comparisonCondition = getTagComparisonQuery(condition, value, "usrTags");
    String searchQuery = String.format(" select entityiid as baseentityiid from pxp.alltagsrecord a where a.propertyIID = %d ", property.getIID());
    String searchWithCondition = !StringUtils.isEmpty(comparisonCondition) ? String.format("%s and %s", searchQuery,comparisonCondition) : searchQuery;
    String tagTempTable = tempTableDas.createTable(searchWithCondition);
   // if context search true then we have to search  on contextualobject table tags;
    if (isContextSearch) {
      
      String contextComparisonCondition = getTagComparisonQuery(condition, value, "cxtTags");
      String searchContextWithCondition =  String.format( 
          "%s join pxp.contextualobject co on a.contextualobjectiid = co.contextualobjectiid "
                  + " where %s and %s", 
          Text.getBefore(baseQuery, "where"), 
          Text.getAfter(baseQuery, "where"), contextComparisonCondition);
      
      String contextTagTempTable = tempTableDas.createTable(searchContextWithCondition);
      return resolveLogicalOperator(Operator.Lor, tagTempTable, contextTagTempTable);
    }
    
    return tagTempTable;
  }    
  
  /**
   * 
   * @param condition condition to be evaluated on attribute
   * @param property propertyDTO for given attribute
   * @param value value that needs to be evaluated on attribute
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  private String resolveAttributeCondition(CSECalculation condition, IPropertyDTO property, CSELiteralOperand value, PropertyField propField)
      throws RDBMSException, SQLException
  {
    
    LiteralType literalType = property.getPropertyType().getLiteralType();
    boolean isAttributeNumeric = Arrays.asList(LiteralType.Date, LiteralType.Number).contains(literalType) 
        && propField.equals(PropertyField.number);
    boolean isTrackingProperty = IStandardConfig.isTrackingProperty(property.getIID(), property.getCode());
    boolean isStringEvaluation = Arrays.asList(Operator.Like, Operator.Contains, Operator.Regex).contains(condition.getOperator());
    
    String valueToFilter = (isAttributeNumeric && !isStringEvaluation) || isTrackingProperty || propField.equals(PropertyField.length)
        ? value.asString()
        : Text.escapeStringWithQuotes(value.asString());
    
    String valueRecordView = "pxp.allvaluerecord";
    if(!isAttributeNumeric && !isTrackingProperty) {
      DynamicViewDAS viewDas = new DynamicViewDAS(currentConnection, inheritanceSchema);
      valueRecordView = viewDas.createDynamicValueView();
    }

    String searchQuery = "";
    if(isTrackingProperty) {
      String propertyColumn = SearchRenderer.getPropertyColumn(property);
      String comparisonQuery = getComparisonQuery(condition, propertyColumn, valueToFilter);
      searchQuery = String.format("select baseentityiid from %s a where %s and a.ismerged != true ","pxp.baseentity", comparisonQuery);
    }
    else if(property.getCode().equals("search")) { // condition for search
      String mainQuery = String.format("select coalesce(b.parentiid,b.baseentityiid) as baseentityiid from %s a join %s b on b.baseentityiid = a.entityiid and b.ismerged != true ", valueRecordView, "pxp.baseentity");
      String comparisonCondition = getComparisonQuery(condition, "a.value", valueToFilter);
      searchQuery = !StringUtils.isEmpty(comparisonCondition) ? String.format("%s where %s", mainQuery, comparisonCondition) : mainQuery;
    }
  else {
      String comparisonFormat = String.format("a.%s",isAttributeNumeric && !propField.equals(PropertyField.length) && !isStringEvaluation
              ? "asNumber" : "value");
      String mainQuery = String.format("select entityiid as baseentityiid from %s a where a.propertyIID = %d",
        valueRecordView, property.getIID());
    comparisonFormat = evaluateOnPropertyField(comparisonFormat, propField);
    String comparisonCondition = getComparisonQuery(condition, comparisonFormat, valueToFilter);
    
    searchQuery = !StringUtils.isEmpty(comparisonCondition) ? String.format("%s and %s", mainQuery, comparisonCondition) : mainQuery;
    }

    return tempTableDas.createTable(searchQuery);
  }

  private String evaluateOnPropertyField(String comparisonFormat, PropertyField propField)
  {
    if(propField.equals(PropertyField.length)) {
      return String.format("length(%s)", comparisonFormat) ;
    }
      return comparisonFormat;
  }

  
  /**
   * @param condition condition that needs to be resolved
   * @param record property on which the given condition needs to be evaluated
   * @param value the result table corresponding to the condition
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  private String resolveRecordCondition(CSECalculation condition, CSERecordOperand record, ICSECalculationNode value)
      throws RDBMSException, SQLException, CSFormatException
  {
    //TODO: TEMPORARY FIX, since cannot be determined whether the standard attribute is numeric or not.
    PropertyDTO property = getProperty(record);
    
    if (property.getSuperType().equals(SuperType.ATTRIBUTE))
      return resolveAttributeCondition(condition, property, (CSELiteralOperand)value, record.getPropertyField());
    if (property.getSuperType().equals(SuperType.TAGS))
      return resolveTagCondition(condition, property, value);
    throw new RDBMSException(100, "Program", "Cannot execute records comparison from property: " + property.toPXON());
    
  }
  
  /**
   * Main switch for simple condition
   * 
   * @param condition condition that needs to be resolved
   * @return the result table corresponding to the condition
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  private String resolveSimpleCondition(CSECalculation condition) throws RDBMSException, SQLException, CSFormatException
  {
    String table = "";
    if (condition.getOperands()[0].getType() == OperandType.Record && condition.getOperands()[1].getType() == OperandType.Record) {
      table = resolveTwoRecordsCondition(condition, (CSERecordOperand) (condition.getOperands()[0]), (CSERecordOperand) (condition.getOperands()[1]));
    }
    else if (condition.getOperands()[0].getType() == OperandType.Record && condition.getOperands()[1].getType() == OperandType.Calculation) {
      table = resolveRecordCondition(condition, (CSERecordOperand) (condition.getOperands()[0]), (CSECalculation) (condition.getOperands()[1]));
    }
    else if (condition.getOperands()[1].getType() == OperandType.Record && condition.getOperands()[0].getType() == OperandType.Calculation) {
      table = resolveRecordCondition(condition, (CSERecordOperand) (condition.getOperands()[1]), (CSECalculation) (condition.getOperands()[0]));
    }
    else if (condition.getOperands()[0].getType() == OperandType.Record) {
      table = resolveRecordCondition(condition, (CSERecordOperand) (condition.getOperands()[0]), (CSELiteralOperand) (condition.getOperands()[1]));
    }
    else {
      table = resolveRecordCondition(condition, (CSERecordOperand) (condition.getOperands()[1]), (CSELiteralOperand) (condition.getOperands()[0]));
    }
    if (condition.getUnaryOperator() == UnaryOperator.not)
      table = tempTableDas.excludeResults(getBaseQueryResult(), table);
    return table;
  }
  
  /**
   * recursive switch level between compound and simple conditions
   * 
   * @param calculatedCondition
   * @return
   * @throws RDBMSException
   */
  private String resolveCalculatedCondition(CSECalculation calculatedCondition) throws RDBMSException, SQLException, CSFormatException
  {
    switch (calculatedCondition.getOperator().getType()) {
      case Logical:
        return resolveCompoundCondition(calculatedCondition);
      case Conditional:
        return resolveSimpleCondition(calculatedCondition);
      default:
        throw new RDBMSException(100, "Programm", "unexpected for search: " + calculatedCondition);
    } // other cases cannot be supported
  }
  

  /**
   * Entry point to resolve a condition
   * 
   * @param condition
   * @return the temporary table that contains the result of the condition
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public String resolve(CSECalculationNode condition) throws RDBMSException, SQLException, CSFormatException
  {
    // A condition must follow strict rules that are checked first:
    if (condition.getType() != OperandType.Calculation) {
      throw new RDBMSException(100, "Programm", "Not supported for search: " + condition);
    }
    CSECalculation calculatedCondition = (CSECalculation) condition;
    if (!calculatedCondition.isSearchCondition())
      throw new RDBMSException(100, "Programm", "Not eligible condition for search: " + condition);
    return resolveCalculatedCondition(calculatedCondition);
  }
  
}
