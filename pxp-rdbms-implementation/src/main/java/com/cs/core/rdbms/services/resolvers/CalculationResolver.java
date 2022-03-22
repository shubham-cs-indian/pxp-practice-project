package com.cs.core.rdbms.services.resolvers;

import com.cs.core.csexpress.CSERegex;
import com.cs.core.csexpress.calculation.CSELiteralOperand;
import com.cs.core.csexpress.calculation.CSERecordOperand;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author vallee
 */
public class CalculationResolver extends RDBMSDataAccessService {
  
  protected final RecordResolver recordResolver;
  protected final LocaleCatalogDAO catalog;
  
  /**
   * Create a new service interface for calculation resolution
   *
   * @param driver
   * @param connection current connection
   * @param catalog the local catalog of execution
   */
  public CalculationResolver(RDBMSConnection connection, ILocaleCatalogDAO catalog)
  {
    super(connection);
    recordResolver = new RecordResolver(connection, catalog);
    this.catalog = (LocaleCatalogDAO) catalog;
  }
  
  /**
   * Transform an operand node into result
   *
   * @param node
   * @return the corresponding result
   */
  private ICSELiteralOperand getRecordResult( long baseEntityIID, ICSERecordOperand icseRecordOperand) 
          throws CSFormatException, RDBMSException, SQLException
  {
    return recordResolver.getResult( baseEntityIID, icseRecordOperand);
  }
  
  /**
   * @param content any text to be evaluated
   * @param lexeme the lexeme of evaluation
   * @return true if the content relates to the lexeme
   * @throws SQLException
   * @throws RDBMSException
   */
  private boolean stringRelates(String content, String lexeme) throws SQLException, RDBMSException
  {
    if ((content == null || content.isEmpty()) || (lexeme == null || lexeme.isEmpty()))
      return false;
    IResultSetParser result = driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.INT, "pxp.relates")
        .setInput(ParameterType.STRING, LocaleCatalogDAO.getDefaultRootLocaleCatalog().getLocaleID())
        .setInput(ParameterType.STRING, content)
        .setInput(ParameterType.STRING, lexeme)
        .execute();
    return (result.getInt() == 1);
  }
  
  /**
   * Evaluate an expression through a conditional operator (not used in first
   * version, to be reviewed to compare String)
   *
   * @param operator
   * @param leftResult
   * @param rightResult
   * @return the result of condition evaluation
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  protected boolean evaluateConditional( ICSECalculation.Operator operator, 
          ICSELiteralOperand leftResult, ICSELiteralOperand rightResult) throws CSFormatException, SQLException, RDBMSException
  {
	boolean listEvaluation =   leftResult.getLiteralType() == LiteralType.List 
            && rightResult.getLiteralType() == LiteralType.List;
    boolean numericEvaluation = leftResult.getLiteralType() == LiteralType.Number 
            && rightResult.getLiteralType() == LiteralType.Number;
    
    switch (operator) {
      case Equals:
    	  if(listEvaluation) {
    		  List<ICSEElement> leftTagValues = leftResult.asTagValueList().getSubElements();
    		  List<ICSEElement> rightTagValues = rightResult.asTagValueList().getSubElements();
    		  boolean  evaluation = leftTagValues.size() == leftTagValues.size();
    		  return evaluation ? listContainment(leftTagValues, rightTagValues) : evaluation;
    	  }else {
    		  return numericEvaluation ? 
    				  (leftResult.asDouble() == rightResult.asDouble()) : leftResult.asString().equals(rightResult.asString());
    	  }
      case Differs:
        return numericEvaluation ? 
          (leftResult.asDouble() != rightResult.asDouble()) : !leftResult.asString().equals(rightResult.asString());
      case GT:
       return numericEvaluation ? 
          (leftResult.asDouble() > rightResult.asDouble()) : leftResult.asString().compareTo(rightResult.asString()) > 0;
      case GTE:
       return numericEvaluation ? 
          (leftResult.asDouble() >= rightResult.asDouble()) : leftResult.asString().compareTo(rightResult.asString()) >= 0;
      case LT:
        return numericEvaluation ? 
          (leftResult.asDouble() < rightResult.asDouble()) : leftResult.asString().compareTo(rightResult.asString()) < 0;
      case LTE:
         return numericEvaluation ? 
          (leftResult.asDouble() <= rightResult.asDouble()) : leftResult.asString().compareTo(rightResult.asString()) <= 0;
      case Lxor:
        return leftResult.asBoolean() ^ rightResult.asBoolean();
      case Lor:
        return leftResult.asBoolean() || rightResult.asBoolean();
      case Land:
        return leftResult.asBoolean() && rightResult.asBoolean();
      case Like:
        Pattern regexLike = CSERegex.CSELikeToJavaRegex(rightResult.asString());
        return regexLike.matcher(leftResult.asString()).matches();
      case Contains:
    	  if(listEvaluation) {
    		  List<ICSEElement> leftTagValues = leftResult.asTagValueList().getSubElements();
    		  List<ICSEElement> rightTagValues = rightResult.asTagValueList().getSubElements();
    		  return listContainment(rightTagValues, leftTagValues);
    	  }  
        return leftResult.asString().contains(rightResult.asString());
      case Regex:
        Pattern regex = CSERegex.CSERegexToJavaRegex(rightResult.asString());
        return regex.matcher(leftResult.asString()).matches();
      case Relates:
        return stringRelates(leftResult.asString(), rightResult.asString());
      case Notcontains:
        if(listEvaluation) {
          List<ICSEElement> leftTagValues = leftResult.asTagValueList().getSubElements();
          List<ICSEElement> rightTagValues = rightResult.asTagValueList().getSubElements();
          return !listContainment(rightTagValues, leftTagValues);
        }
        break;
    }
    return false;
  }

private boolean listContainment( List<ICSEElement> containee, List<ICSEElement> container) {
	boolean evaluation = true;
		for(ICSEElement leftTagValue : containee) {
			if(!container.contains(leftTagValue)){
				evaluation = false;
				break;
			}
		}
	return evaluation;
}
  
  protected double evaluateMath(ICSECalculation.Operator operator, ICSELiteralOperand leftResult,
      ICSELiteralOperand rightResult) throws CSFormatException
  {
    double left = leftResult.asDouble();
    double right = rightResult.asDouble();
    switch (operator) {
      case Plus:
        return left + right;
      case Minus:
        return left - right;
      case Multiply:
        if(StringUtils.isEmpty(rightResult.asString())
            || StringUtils.isEmpty(rightResult.asString())) {
          return .0;
        }
        
        return left * right;
      case Divide:
        if(StringUtils.isEmpty(rightResult.asString())) {
          return left;
        }
        if (right == .0)
          throw new CSFormatException("ZERO Division");
        return left / right;
      case Modulus:
        return (long) left % (long) right;
    }
    return .0;
  }
  
  /**
   * @param baseEntityIID the base entity of reference
   * @param node the calculation node to be resolved (recursively)
   * @return the retrieved value of the record
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  public ICSELiteralOperand getResult( long baseEntityIID, ICSECalculationNode node) 
          throws CSFormatException, RDBMSException, SQLException
  {
    switch (node.getType()) {
      case Literal:
        return (ICSELiteralOperand) node;
      case Record:
        return recordResolver.getResult( baseEntityIID, (CSERecordOperand) node);
    }
    // in case of calculation
    ICSECalculation calculation = (ICSECalculation) node;
    ICSELiteralOperand leftResult = getResult( baseEntityIID, calculation.getOperands()[0]);
    ICSELiteralOperand rightResult = getResult( baseEntityIID, calculation.getOperands()[1]);
    CSELiteralOperand result = new CSELiteralOperand();
    boolean isEmptyComparison = Arrays.asList(Operator.Equals, Operator.Differs).contains(calculation.getOperator());
    if (!isEmptyComparison && (leftResult.isUndefined() || rightResult.isUndefined())) {
      return result; // empty and undefined itself
    }
    switch (calculation.getOperator().getType()) {
      case Logical:
      case Conditional:
        boolean condition = evaluateConditional(calculation.getOperator(), leftResult, rightResult);
        result.setValueAndType(condition, LiteralType.Boolean);
        break;
      case Math:
        double value = evaluateMath(calculation.getOperator(), leftResult, rightResult);
        result.setValueAndType(value, LiteralType.Number);
        break;
      case Text:
        result.setValueAndType(leftResult.asString() + rightResult.asString(), LiteralType.Text);
        break;
    }
    return result;
  }
}
