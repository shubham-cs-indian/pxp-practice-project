package com.cs.core.rdbms.services.resolvers;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.WordUtils;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.csexpress.calculation.CSEFunctionOperand;
import com.cs.core.csexpress.calculation.CSELiteralOperand;
import com.cs.core.csexpress.calculation.CSERecordOperand;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.dao.RuleCatalogDAS;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.actions.ICSEAction;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.actions.ICSEObjectClassification;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyAssignment;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSEFunctionOperand;
import com.cs.core.technical.icsexpress.calculation.ICSEFunctionOperand.Function;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSETagValue;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author Niraj
 */
public class RuleResolver extends CalculationResolver {

  protected final Collection<String> organizationCodes;
  protected final Collection<String> catalogCodes;
  protected final Collection<String> localeIds;
  protected final LocaleCatalogDAO catalog;
  public StringBuilder queryForUniqueness ;
  public Collection<String> includingClassifiers;
  private Map<String, IReferencedSectionElementModel> referencedElements = new HashMap<>();

  public RuleResolver(RDBMSConnection connection, ILocaleCatalogDAO catalog, Collection<String> catalogCodes, Collection<String> localeIDs,
      Collection<String> organizationCodes, StringBuilder stringBuilder, Collection<String> includingClassifiers,
      Map<String, IReferencedSectionElementModel> referencedElements)
  {
    super( connection, catalog);
    this.organizationCodes = organizationCodes;
    this.catalogCodes = catalogCodes;   
    this.localeIds = localeIDs;     
    this.catalog = (LocaleCatalogDAO) catalog;
    this.queryForUniqueness = stringBuilder;
    this.includingClassifiers = includingClassifiers;
    this.referencedElements = referencedElements;
  }
  
  @Override
  public ICSELiteralOperand getResult( long baseEntityIID, ICSECalculationNode calculationNode) throws CSFormatException, RDBMSException, SQLException
  {
    switch (calculationNode.getType()) {
      case Function:
        CSEFunctionOperand functionOperand = (CSEFunctionOperand) calculationNode;
        Function function = functionOperand.getFunction();
        switch (function.getType()) {
          case Logical:
            if (function.equals(Function.unique)) {
              return evaluateUniqueness( baseEntityIID, functionOperand);
            }
          case Text:
            return executeTextFunction( baseEntityIID, functionOperand);
          case Math:
            return executeMathFunction(baseEntityIID, functionOperand);
          default:
            return new CSELiteralOperand();
        }
      case Record:
    	  CSERecordOperand record = (CSERecordOperand) calculationNode;
    	  IPropertyDTO recordProperty = configDao.getPropertyByCode(record.getProperty().getCode());
			if (recordProperty.getSuperType().equals(SuperType.TAGS)) {
				return recordResolver.getTagResult(baseEntityIID, recordProperty, false);
			} 
			else {
				return super.getResult(baseEntityIID, calculationNode);
			}
      default:
        return super.getResult(baseEntityIID, calculationNode);
    }
  }
  
  /**
   * 
   * @param baseEntityIID IID of base entity on which this function needs to be applied.
   * @param function The Text function that needs to be applied.
   * @return Value after applying the given function
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  private CSELiteralOperand executeTextFunction( long baseEntityIID, ICSEFunctionOperand function)
      throws CSFormatException, RDBMSException, SQLException
  {
    Function functionType = function.getFunction();
    ICSECalculationNode[] parameters = function.getParameters();
    Set<ICSERecordOperand> records = parameters[0].getRecords();
    ICSERecordOperand record = records.iterator()
        .next();
    CSELiteralOperand result = recordResolver.getResult( baseEntityIID, record);
    
    switch (functionType) {
      
      case lower:
        result.setValueAndType(result.asString().toLowerCase(), LiteralType.Text);
        break;
      case upper:
        result.setValueAndType(result.asString().toUpperCase(), LiteralType.Text);
        break;
      case propercase:
        result.setValueAndType(WordUtils.capitalize(result.asString()), LiteralType.Text);
        break;
      case trim:
        result.setValueAndType(result.asString().trim(), LiteralType.Text);
        break;
      case replace:
        String find = ((ICSELiteralOperand) parameters[1]).asString();
        String replace = ((ICSELiteralOperand) parameters[2]).asString();
        result.setValueAndType(result.asString().replaceAll(find, replace), LiteralType.Text);
        break;
      
      case substring:
        Double startIndex = ((ICSELiteralOperand) parameters[1]).asDouble();
        Double endIndex = ((ICSELiteralOperand) parameters[2]).asDouble();
        
        String valueText = result.asString();
        if (startIndex >= valueText.length()) {
          valueText = "";
        }
        else if (startIndex.intValue() < endIndex.intValue()) {
          // endIndex + 1 because in substring() the endIndex is exclusive (refer docs)
          endIndex = endIndex >= valueText.length() ? valueText.length() : endIndex + 1;
          valueText = valueText.substring(startIndex.intValue(), endIndex.intValue());
        }
        result.setValueAndType(valueText, LiteralType.Text);
        break;
      default:
        break;
    }
    return result;
  }
  
  /**
   * Evaluate Uniqueness of given base entity
   * @param baseEntityIID IID of base entity who's parameter needs to be evaluated for uniqueness.
   * @param function parameters of Uniqueness function
   * @return Boolean Operand to denote whether the result is true or false.
   * @throws RDBMSException
   * @throws CSFormatException
   * @throws SQLException
   */
  private CSELiteralOperand evaluateUniqueness( long baseEntityIID, CSEFunctionOperand function)
      throws RDBMSException, CSFormatException, SQLException
  {
    ICSECalculationNode[] parameters = function.getParameters();
    Set<ICSERecordOperand> records = parameters[0].getRecords();
    ICSERecordOperand record = records.iterator().next();
    
    String code = record.getProperty().getCode();
    IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(code);
    
    IBaseEntityDTO entity = catalog.getEntityByIID(baseEntityIID);
    IBaseEntityDAO entityDAO = catalog.openBaseEntity(entity);
    
    entity = entityDAO.loadPropertyRecords(property);
    IPropertyRecordDTO propertyRecord = entity.getPropertyRecord(property.getPropertyIID());
    
    CSELiteralOperand result = new CSELiteralOperand();
    RuleCatalogDAS ruleCatalogDAS = new RuleCatalogDAS( currentConnection);
    
    if(queryForUniqueness.length() != 0) {
      queryForUniqueness.append("intersect ");
    }
    
    queryForUniqueness.append(ruleCatalogDAS.isUnique(propertyRecord, catalogCodes, organizationCodes, localeIds, includingClassifiers));
    
    //result.setValueAndType( ruleCatalogDAS.isUnique(propertyRecord, catalogCodes, localeIds), LiteralType.Boolean);
    return result;
  }
  
  
  /**
   * 
   * @param actions List of actions to be taken
   * @param ruleExpressionIId : expression IID of rule
   * @param baseEntityIID: IID of base entity on which rules need to be applied.
   * @return Return the properties if any Normalization Needs to be done.
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public Map<IRootConfigDTO, ICSELiteralOperand> evaluateRuleEffects(ICSEActionList actions,
      long ruleExpressionIId, long baseEntityIID)
      throws RDBMSException, SQLException, CSFormatException
  {
    Map<IRootConfigDTO, ICSELiteralOperand> result = new HashMap<>();
    for (ICSEAction action : actions) {

      switch (action.getType()) {
        case Assignment:
          ICSEPropertyAssignment assignment = (ICSEPropertyAssignment) action;
          String propertyCode = assignment.getProperty().getCode();
          
          ICSECalculationNode value = assignment.getValue();
          IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByCode(propertyCode);
          
          result.put(propertyDTO, getResult( baseEntityIID, value));
          break;
        case Classification:
          ICSEObjectClassification classificationAction = (ICSEObjectClassification) action;
          ICSECouplingSource destination = classificationAction.getDestination();
          if (destination.isClassifier()) {
            String classifierCode = destination.toClassifier().getCode();
            IClassifierDTO classifierByCode = ConfigurationDAO.instance().getClassifierByCode(classifierCode);
            result.put(classifierByCode, new CSELiteralOperand());
          }
          break;
        
        case QualityFlag:
          ICSEPropertyQualityFlag violation = (ICSEPropertyQualityFlag) action;
          applyViolation(ruleExpressionIId, baseEntityIID, violation);
          break;
        default:
          break;
      }
    }
    return result;
  }
  
  /**
   * Insert Required violations according to the applied locale.
   * @param ruleExpressionIId: IID of rule expression. 
   * @param baseEntityIID: IID of base entity on which rule is being evaluated
   * @param violation violations that are to be applied
   * 
   * @throws RDBMSException
   * @throws SQLException
   */
  private void applyViolation(Long ruleExpressionIId, Long baseEntityIID, ICSEAction violation)
      throws RDBMSException, SQLException
  {
    ICSEPropertyQualityFlag qualityFlagAction = (ICSEPropertyQualityFlag) violation;
    String code = qualityFlagAction.getProperty()
        .getCode();
    IPropertyDTO property = ConfigurationDAO.instance()
        .getPropertyByCode(code);
 
    if(!referencedElements.containsKey(code)) {
      return;
    }
    
    RuleCatalogDAS ruleCatalogDAS = new RuleCatalogDAS(currentConnection);
    String localeID = catalog.getLocaleCatalogDTO()
        .getLocaleID();
    if (localeIds.contains(localeID)) {
      ruleCatalogDAS.insertViolation(ruleExpressionIId, localeID, baseEntityIID, qualityFlagAction,
          property.getPropertyIID());
    }
    else if (localeIds.isEmpty()) {
      ruleCatalogDAS.insertViolation(ruleExpressionIId, null, baseEntityIID, qualityFlagAction,
          property.getPropertyIID());
    }
    else if (localeID.isEmpty()) {
      for (String localeId : localeIds) {
        ruleCatalogDAS.insertViolation(ruleExpressionIId, localeId, baseEntityIID,
            qualityFlagAction, property.getPropertyIID());
      }
    }
  }
  
  /**
   * 
   * @param baseEntityIID - IID of base entity on which this function needs to be applied.
   * @param function - The Math function that needs to be applied.
   * @return List after applying the given function
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  private ICSELiteralOperand executeMathFunction(long baseEntityIID, ICSEFunctionOperand function)
      throws CSFormatException, RDBMSException, SQLException
  {
    ICSECalculationNode[] parameters = function.getParameters();
    Set<ICSERecordOperand> records = parameters[0].getRecords();
    ICSERecordOperand record = records.iterator().next();
    IPropertyDTO recordProperty = configDao.getPropertyByCode(record.getProperty().getCode());
    CSELiteralOperand result = recordResolver.getTagResult(baseEntityIID, recordProperty, false);
    
    List<String> tagValues = result.getTags();
    List<Integer> ranges = result.getRanges();
    
    List<ICSEElement> subElements = ((ICSELiteralOperand) parameters[1]).asTagValueList().getSubElements();

    for (ICSEElement subElement : subElements) {
      ICSETagValue icseElement = (ICSETagValue) subElement;
      String code = icseElement.getCode();
      switch (function.getFunction()) {
        case add:
          if (!tagValues.contains(code)) {
            tagValues.add(code);
            ranges.add(icseElement.getRange());
          }
          break;
        case remove:
          if (tagValues.contains(code)) {
            ranges.remove(tagValues.indexOf(code));
            tagValues.remove(code);
          }
          break;
        default:
          break;
      }
    }
    
    result.setValueAndType("", LiteralType.List);
    result.addTags(tagValues, ranges);
    return result;
  }

}