package com.cs.core.rdbms.process.dao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class RuleCatalogDAS extends RDBMSDataAccessService {
  
  public RuleCatalogDAS( RDBMSConnection connection)
  {
    super(connection);
  }
  
  
  /**
   * @param ruleExpressionIID ruleExpressionIID of the rule that needs to be deleted .
   * @throws RDBMSException
   * @throws SQLException
   */
  private static final String DELETE_EXPRESSION = "delete from pxp.ruleexpression where ruleexpressioniid = ? ";
  private static final String DELETE_VIOLATION = "delete from pxp.baseentityqualityrulelink where ruleexpressioniid = ? ";
  private static final String DELETE_KPI = "delete from pxp.baseentitykpirulelink where ruleexpressioniid = ? ";
  
  public void deleteExpression(long ruleExpressionIID, RuleType ruleType) throws RDBMSException, SQLException
  {
    StringBuilder deleteExpressions = new StringBuilder(DELETE_EXPRESSION);
    PreparedStatement stm = currentConnection.prepareStatement(deleteExpressions.toString());
    stm.setLong(1, ruleExpressionIID);
    stm.execute();
    if(ruleType.equals(RuleType.dataquality)) {
      StringBuilder deleteViolations = new StringBuilder(DELETE_VIOLATION);
      PreparedStatement stmt = currentConnection.prepareStatement(deleteViolations.toString());
      stmt.setLong(1, ruleExpressionIID);
      stmt.execute();
    }else if(ruleType.equals(RuleType.kpi)) {
      StringBuilder deleteViolations = new StringBuilder(DELETE_KPI);
      PreparedStatement stmt = currentConnection.prepareStatement(deleteViolations.toString());
      stmt.setLong(1, ruleExpressionIID);
      stmt.execute();
    }
  }
  
  public static final String GET_VIOLATION = "select qualityflag from pxp.baseentityqualityrulelink "
      + "where ruleexpressioniid = ? and baseentityiid = ? and propertyiid = ? and localeid %s";
  /**
   * Get violation for a specific property in a baseentity.
   * 
   * @param ruleExpressionIId: expression IID of the rule of which violations need to fetched.
   * @param localeId: localeID for which violations are fetched.
   * @param baseEntityIID: IID of base entity to check for violations.
   * @param propertyIID: IID of property to check for violations.
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public QualityFlag getViolation(Long ruleExpressionIId, String localeId, Long baseEntityIID, long propertyIID)
      throws RDBMSException, SQLException
  {
    String getViolationQuery = String.format( GET_VIOLATION, localeId.isEmpty() ? " = 'NAL'" : " = '" + localeId + "' or localeid = 'NAL'");
    PreparedStatement stmt = currentConnection.prepareStatement(getViolationQuery);
    stmt.setLong(1, ruleExpressionIId);
    stmt.setLong(2, baseEntityIID);
    stmt.setLong(3, propertyIID);
    stmt.execute();
    IResultSetParser ruleResult = driver.getResultSetParser(stmt.getResultSet());
    if ( ruleResult.next() )
      return QualityFlag.valueOf(ruleResult.getInt("qualityflag"));
    else
      return QualityFlag.$green; // return green by default
  }
  
  /**
   * Insert violation for Data Quality Rule.
   * @param ruleExpressionIId: expression IID of the rule of which violations need to fetched.
   * @param localeId The localeId on which the violation needs to applied.
   * @param baseEntityIID Base entity on which violations need to be inserted.
   * @param propertyIID propertyiid of the product identifier attribute.
   * @param qualityFlagAction quality flag which needs to inserted.
   * @throws RDBMSException
   * @throws SQLException
   */
  public void insertViolation(Long ruleExpressionIId, String localeId, Long baseEntityIID,
      ICSEPropertyQualityFlag qualityFlagAction, long propertyIID)
      throws RDBMSException, SQLException
  {
    driver.getProcedure(currentConnection, "pxp.sp_violationUpsert")
        .setInput(ParameterType.IID, ruleExpressionIId)
        .setInput(ParameterType.IID, baseEntityIID)
        .setInput(ParameterType.IID, propertyIID)
        .setInput(ParameterType.STRING, localeId==null? "NAL": localeId)
        .setInput(ParameterType.INT, qualityFlagAction.getFlag().ordinal())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, qualityFlagAction.getMessage())
        .execute();
  }
  
  /**
   * Insert result of KPI Evaluations.
   * @param result result of the KPI that is evaluated. 
   * @param ruleExpressionIId: expression IID of the rule of which violations need to fetched.
   * @param baseEntityIID Base entity on which violations need to be inserted.
   * @param localeID 
   * @throws RDBMSException
   * @throws SQLException
   */
  public void insertKPIevaluation(Double result, long baseEntityIID, long ruleExpressionIId, String localeID)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_kpiUpsert")
        .setInput(ParameterType.IID, ruleExpressionIId)
        .setInput(ParameterType.IID, baseEntityIID)
        .setInput(ParameterType.FLOAT, result)
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, localeID)
        .execute();
  }
  
  public static final String UNIQUENESS_VIOLATION_QUERY_FOR_ATTRIBUTE = "select distinct avr.entityiid from pxp.allvaluerecord avr "
      + "join pxp.baseentity be ON be.baseentityiid = avr.entityiid and be.ismerged != true "
      + "left join pxp.baseentityclassifierlink becl on becl.baseentityiid = be.baseentityiid "
      + "join pxp.classifierconfig cc ON cc.classifieriid = be.classifieriid OR cc.classifieriid = becl.otherclassifieriid where ";
  
  public static final String UNIQUNESS_VIOLATION_QUERY_FOR_TAG = "select atg.entityiid from pxp.alltagsrecord atg "
      + "join pxp.baseentity be ON be.baseentityiid = atg.entityiid and be.ismerged != true "
      + "left join pxp.baseentityclassifierlink becl on becl.baseentityiid = be.baseentityiid "
      + "join pxp.classifierconfig cc ON cc.classifieriid = be.classifieriid OR cc.classifieriid = becl.otherclassifieriid where ";
  
  /**
   * Check whether the given property record is unique.
   * 
   * @param propertyRecord property record that needs to be checked for uniqueness.
   * @param catalogCodes codes of catalog that qualify for check across uniqueness.
   * @param organizationCodes codes of organization that qualify for check across uniqueness.
   * @param localeIds localeIds of locale that are to be considered for uniqueness evaluation.
   * @param includingClassifiers 
   * @return boolean to check if the given value is unique or not.
   * @throws RDBMSException
   * @throws SQLException
   */
  public String isUnique(IPropertyRecordDTO propertyRecord, Collection<String> catalogCodes,
      Collection<String> organizationCodes, Collection<String> localeIds,
      Collection<String> includingClassifiers) throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder();
    String catalogQueryString = " catalogCode in (" + Text.join(",", catalogCodes, "'%s'")
        + ") and ";
    String organizationQueryString = " organizationCode in ("
        + Text.join(",", organizationCodes, "'%s'") + ") and ";
    
    if(propertyRecord instanceof ValueRecordDTO) {
      
      ValueRecordDTO valueRecordDTO = (ValueRecordDTO) propertyRecord;
      query.append(UNIQUENESS_VIOLATION_QUERY_FOR_ATTRIBUTE);
      
      if(!catalogCodes.isEmpty()) {
        query.append(catalogQueryString);
      }
      
      if (!organizationCodes.isEmpty()) {
        query.append(organizationQueryString);
      }
      
      query.append(String.format("avr.value = \'%s\' and avr.propertyiid = %d ", valueRecordDTO.getValue(), valueRecordDTO.getProperty().getPropertyIID()));
      
    }else if(propertyRecord instanceof TagsRecordDTO) {
      
      query.append(UNIQUNESS_VIOLATION_QUERY_FOR_TAG);
      
      if(!catalogCodes.isEmpty()) {
        query.append(catalogQueryString);
      }
      
      if (!organizationCodes.isEmpty()) {
        query.append(organizationQueryString);
      }
      
      TagsRecordDTO tagsRecordDTO = (TagsRecordDTO) propertyRecord;
      Set<ITagDTO> tagsDTO = tagsRecordDTO.getTags();
      
      for(ITagDTO tagDTO : tagsDTO) {
        query.append(String.format("atg.usrtags -> \'%s\' = \'%d\' and ", tagDTO.getTagValueCode(), tagDTO.getRange()));
      }
      
      if(tagsDTO.isEmpty()) {
        query.append("atg.usrtags = \'\' and ");
      }else {
        query.append(String.format("array_length(avals(atg.usrtags), 1) = %d and ", tagsDTO.size()));
      }
      query.append(String.format("atg.propertyiid = %d ", tagsRecordDTO.getProperty().getPropertyIID()));
      
    }
    if(query.length() != 0) {
      query.append(getQueryForClassifier(includingClassifiers));
    }
    return query.toString();
  }
  
  public StringBuilder getQueryForClassifier(Collection<String> includingClassifiers) throws RDBMSException {
    
    StringBuilder queryForKlass = new StringBuilder();
    StringBuilder queryForTaxonomy = new StringBuilder();
    
    for(String classifier : includingClassifiers) {
      
      IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(classifier);
      if(classifierDTO.getClassifierType() == ClassifierType.CLASS) {
        if(queryForKlass.length() == 0) {
          queryForKlass.append(" and be.classifierIID IN (" +classifierDTO.getClassifierIID());
        }else {
          queryForKlass.append("," + classifierDTO.getClassifierIID());
        }
      }else {
        if(queryForTaxonomy.length() == 0) {
          queryForTaxonomy.append("  and cc.hierarchyiids && '{" +classifierDTO.getClassifierIID());
        }else {
          queryForTaxonomy.append(", \"" +classifierDTO.getClassifierIID()+ "\"");
        }
      }
    }
    
    if(queryForTaxonomy.length() > 0) {
      queryForTaxonomy.append("}'");
    }
    
    if(queryForKlass.length() > 0) {
      queryForKlass.append(")");
    }
    
    return queryForKlass.append(queryForTaxonomy);
  }

  public static final String RULE_SELECTOR_QUERY = "select re.ruleCode from pxp.ruleConfig rc join pxp.ruleExpression re  "
      +" on rc.ruleCode = re.ruleCode where (re.catalogCodes @> ? or array_length(re.catalogCodes , 1) = 0 ) "  
      +" and (re.organizationCodes @> ? or array_length(re.organizationCodes , 1) = 0 or re.organizationcodes = \'{}\')";
  /**s
   * Filtering rules according to catalog code.
   * @param catalogDAO DAO that represents locale and catalog.
   * @return List of rules to evaluate
   * @throws RDBMSException
   * @throws SQLException
   */
  public List<String> getRulesToEvaluate(ILocaleCatalogDAO catalogDAO, RuleType ruleType) throws RDBMSException, SQLException
  {
    StringBuffer query = new StringBuffer();
    query.append(RULE_SELECTOR_QUERY);
    if(ruleType != null) {
      query.append(String.format(" and rc.ruletype = %d", ruleType.ordinal()));
    }
    PreparedStatement stmt = currentConnection.prepareStatement(query.toString());

    stmt.setArray(1, currentConnection.newStringArray( Arrays.asList(catalogDAO.getLocaleCatalogDTO().getCatalogCode())));
    stmt.setArray(2,currentConnection.newStringArray( Arrays.asList(catalogDAO.getLocaleCatalogDTO().getOrganizationCode())));
    stmt.execute();
    IResultSetParser ruleResult = driver.getResultSetParser(stmt.getResultSet());
    List<String> ruleIds = new ArrayList<>();
    while (ruleResult.next()) {
      ruleIds.add(ruleResult.getString("ruleCode"));
    }
    return ruleIds;
  }
  
  /**
   * Insert violation for identifier attribute.
   * @param classifierId The classifierIID on which product identifier is applied.
   * @param localeId The localeId on which the violation needs to applied.
   * @param baseEntityIID Base entity on which violations need to be inserted.
   * @param propertyIID propertyiid of the product identifier attribute.
   * @param message
   * @throws RDBMSException
   * @throws SQLException
   */
  public void insertIdentifierViolation(long classifierId, String localeId, long baseEntityIID, long propertyIID, String message)
      throws RDBMSException, SQLException
  {
    driver.getProcedure(currentConnection, "pxp.sp_violationUpsert")
        .setInput(ParameterType.IID, classifierId)
        .setInput(ParameterType.IID, baseEntityIID)
        .setInput(ParameterType.IID, propertyIID)
        .setInput(ParameterType.STRING, localeId)
        .setInput(ParameterType.INT, ICSEPropertyQualityFlag.QualityFlag.$red.ordinal())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, message)
        .execute();
  }
  
  public void insertMandatoryViolation(String localeId, long baseEntityIID, long propertyIID, int flag, String message)
	      throws RDBMSException, SQLException
	  {
	    driver.getProcedure(currentConnection, "pxp.sp_violationUpsert")
	        .setInput(ParameterType.IID, -1)
	        .setInput(ParameterType.IID, baseEntityIID)
	        .setInput(ParameterType.IID, propertyIID)
	        .setInput(ParameterType.STRING, localeId==null? "NAL": localeId)
	        .setInput(ParameterType.INT, flag)
	        .setInput(ParameterType.LONG, System.currentTimeMillis())
	        .setInput(ParameterType.STRING, message)
	        .execute();
	  }
  
  public static final String VALUE_QUERY = "select value from pxp.valuerecord where entityiid=? and propertyiid = ?";
  public static final String PRODUCT_IDENTIFIER = " select be.baseentityiid "
      + "from pxp.allvaluerecord avr join pxp.baseentity be on be.baseentityiid = avr.entityiid and be.ismerged != true left join pxp.baseentityclassifierlink becl on "
      + "becl.baseentityiid = be.baseentityiid " 
      + "where avr.propertyiid = ? and (becl.otherclassifieriid = ? or be.classifieriid = ?) " 
      + "and be.catalogcode = ? and avr.value = ?";
  /**
   * @param propertyIID PropertyIID of the property that needs to be checked for uniqueness.
   * @param baseEntityIID baseEntityIID of which the value is to be checked is unique.
   * @param classifierIID classifierIID of the product identifier.
   * @param catalogDTO catalog on which uniqueness need to be checked.
   * @return If there are any duplicate, return the list of duplicate baseentityIIDs.
   * @throws RDBMSException
   * @throws SQLException
   */
  public List<Long> isProductIdentifierUnique(long propertyIID, long baseEntityIID, long classifierIID, ICatalogDTO catalogDTO) throws RDBMSException, SQLException
  {
    List<Long> violatedEntities = new ArrayList<>();
    PreparedStatement stmt = currentConnection.prepareStatement(VALUE_QUERY);
    stmt.setLong(1, baseEntityIID);
    stmt.setLong(2, propertyIID);
    stmt.execute();
    IResultSetParser resultSetParser = driver.getResultSetParser(stmt.getResultSet());
    if(!resultSetParser.next()) {
      return violatedEntities;
    }   
    String value = resultSetParser.getString("value");
    
    stmt = currentConnection.prepareStatement(PRODUCT_IDENTIFIER);
    stmt.setLong(1, propertyIID);
    stmt.setLong(2, classifierIID);
    stmt.setLong(3, classifierIID);
    stmt.setString(4, catalogDTO.getCatalogCode());
    stmt.setString(5, value);
    stmt.execute();
    resultSetParser = driver.getResultSetParser(stmt.getResultSet());

    while (resultSetParser.next()) {
      violatedEntities.add(resultSetParser.getLong("baseentityiid"));
    }
    return violatedEntities;
  }
  
  
  /**
   * @param currentConn Current Database connection.
   * @param localeIDs Locales in which violation needs to be deleted
   * @param ruleExpressionIID ruleexpressionIID of the given rule.
   * @param baseEntityIID base entity of which violations need to be deleted
   * @throws RDBMSException
   * @throws SQLException
   */
  private static final String DELETE_VIOLATIONS = "delete from pxp.baseentityqualityrulelink where "
      + "ruleexpressioniid = ? and baseentityiid = ? ";
  public void deleteViolation(RDBMSConnection currentConn, String localeID,
      Long ruleExpressionIID, long baseEntityIID) throws RDBMSException, SQLException
  {
    StringBuilder deleteViolations = new StringBuilder(DELETE_VIOLATIONS);
    if (!localeID.isEmpty()) {
      deleteViolations
          .append(String.format(" and localeid = '%s'", localeID));
    }
    PreparedStatement stm = currentConn.prepareStatement(deleteViolations.toString());
    stm.setLong(1, ruleExpressionIID);
    stm.setLong(2, baseEntityIID);
    stm.execute();
  }
  
  private static final String DELETE_KPI_VIOLATIONS = "delete from pxp.baseentitykpirulelink where "
      + "ruleexpressioniid = ? and baseentityiid = ? ";
  public void deleteKpiViolation(RDBMSConnection currentConn, Collection<String> localeIDs,
      Long ruleExpressionIID, long baseEntityIID) throws RDBMSException, SQLException
  {
    StringBuilder deleteViolations = new StringBuilder(DELETE_KPI_VIOLATIONS);
    PreparedStatement stm = currentConn.prepareStatement(deleteViolations.toString());
    stm.setLong(1, ruleExpressionIID);
    stm.setLong(2, baseEntityIID);
    stm.execute();
  }
  
  private static final String DELETE_MANDATORY_VIOLATIONS = "delete from pxp.baseentityqualityrulelink where "
	      + "ruleexpressioniid = -1 and baseentityiid = %s and propertyiid in (%s) and (localeid = '%s' or localeid = 'NAL') ";
	  public void deleteNonEmptyMandatoryViolations(RDBMSConnection currentConn,
			  long baseEntityIID, List<Long> propertyiids, String localeID) throws RDBMSException, SQLException
	  {
	    String finalQuery = String.format(DELETE_MANDATORY_VIOLATIONS, baseEntityIID, Text.join(",", propertyiids), localeID);
	    PreparedStatement stm = currentConn.prepareStatement(finalQuery.toString());
		
	    stm.execute();
	  }

  private static final String DELETE_DATA_RULES_AND_VIOLATIONS = "with re as ( delete from pxp.ruleexpression where "
      + "rulecode in (%s) returning ruleexpressioniid, rulecode), rc as ( delete from pxp.ruleconfig where rulecode in (select rulecode from re)), "
      + "beqrl as ( delete from pxp.baseentityqualityrulelink where ruleexpressioniid in (select ruleexpressioniid from re)) select * from re ";
  public void deleteDataRulesAndViolations(Set<String> dataRuleCodes) throws RDBMSException, SQLException
  {
    String finalQuery = String.format(DELETE_DATA_RULES_AND_VIOLATIONS, Text.join(",", dataRuleCodes, "'%s'"));
    PreparedStatement stm = currentConnection.prepareStatement(finalQuery.toString());
    stm.execute();
  }

  private static final String DELETE_RULE_VIOLATION = "delete from pxp.baseentityqualityrulelink where "
      + "ruleexpressioniid = ? and baseentityiid = ? ";
  public void deleteExpressionForSaveRule(long ruleExpressionIId, long baseEntityIID, RuleType dataquality) throws SQLException, RDBMSException
  {
    StringBuilder deleteViolations = new StringBuilder(DELETE_RULE_VIOLATION);
    PreparedStatement stm = currentConnection.prepareStatement(deleteViolations.toString());
    stm.setLong(1, ruleExpressionIId);
    stm.setLong(2, baseEntityIID);
    stm.execute();
  }
  
  private static final String DELETE_RULE_VIOLATIONS = "delete from pxp.baseentityqualityrulelink where "
      + "ruleexpressioniid in (%s) and baseentityiid = ? ";
  public void deleteExpressionsForSaveRule(List<Long> ruleExpressionIIds, long baseEntityIID) throws SQLException, RDBMSException
  {
    String finalQuery = String.format(DELETE_RULE_VIOLATIONS, Text.join(",",ruleExpressionIIds));
    PreparedStatement stm = currentConnection.prepareStatement(finalQuery);
    stm.setLong(1, baseEntityIID);
    stm.execute();
  }
  
}