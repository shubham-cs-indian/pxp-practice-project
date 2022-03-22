package com.cs.core.rdbms.process.idao;

import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;
import com.cs.core.rdbms.process.idto.IKPIResultDTO;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Services to manage rules
 *
 * @author vallee
 */
public interface IRuleCatalogDAO {

  /**
   * @return the catalog that is represented by this DAO
   */
  public ICatalogDTO getCatalog();
  
  /**
   * 
   * @param ruleCode ruleCode of KPI.
   * @param entityCode 
   * @param localeID 
   * @param type 
   * @return result for dashboard of KPI.
   * @throws RDBMSException
   */
  public IRuleResultDTO getKPIRuleResult(String ruleCode, String entityCode, String localeID, String type, List<String> taxonomyInfo, List<String> tagInfo) throws RDBMSException;
 
  /**
   * Refresh a rule for a base entity
   *
   * @param ruleCode identifies the rule to be reevaluated
   * @param baseEntityDAO identifies the entity DAO on which to reevaluate the rule
   */
  public IChangedPropertiesDTO refreshDataQualityResult(String ruleCode, IBaseEntityDAO baseEntityDAO, Boolean isAllLocaleIdsApplicable) throws RDBMSException;

  /**
   * Evaluate the given property as product identifier.
   *
   * @param propertyIID propertyiid of product identifier
   * @param baseEntityIId basentityiid of entity.
   * @param classifier classifier in which the product identifier is attached
   * @throws RDBMSException
   */
  public void evaluateProductIdentifier(long propertyIID, long baseEntityIId, long classifier) throws RDBMSException;

  /**
   * 
   * @param baseEntityIID iid of base entity
   * @param localeID 
   * @return result of KPI's related to current base entity.
   * @throws RDBMSException
   */
  public List<IKPIResultDTO> loadKPI(Long baseEntityIID, String localeID) throws RDBMSException;

  /**
   * 
   * @param baseEntityIID IID of base entity of which violations need to be fetched.
   * @return All the violations related to current base entity.
   * @throws RDBMSException
   */
  public Set<IRuleViolationDTO> loadViolations(Long baseEntityIID) throws RDBMSException;
  
  
  /**
   * 
   * @param ruleCodes of Uniquness block in Kpi.
   * @param baseEntityIID of which violations need to be fetched.
   * @return All the violation related to the unqiueness block in kpi with current baseEntity.
   * @throws RDBMSException
   */
  public List<IKPIResultDTO> loadKPIForUniqunessBlock(List<String> ruleCodes, Long baseEntityIID) throws RDBMSException;
  
  /**
   * 
   * @param targetBaseEntityIIDs
   * @param ruleExpressionIID
   * @param result
   * @throws RDBMSException
   */
  public void updateTargetBaseEntityforUniquenessBlock(List<Long> targetBaseEntityIIDs, Long ruleExpressionIID, double result) throws RDBMSException;
  
  /**
   * 
   * @param baseEntityiid
   * @throws RDBMSException
   */
  public void deleteKpiOnBaseEntityDelete(long baseEntityiid) throws RDBMSException;
  
  /**
   * 
   * @param sourceIID
   * @param targetIIDs
   * @param ruleExpressionIID
   * @throws RDBMSException
   */
  public void deleteKpiFromKpiUniquenessViolation(long sourceIID, List<Long> targetIIDs, long ruleExpressionIID) throws RDBMSException;
  
  /**
   * 
   * @param sourceIID
   * @param targetIIDs
   * @param ruleExpressionIID
   * @throws RDBMSException
   */
  public void insertKpiUniqunessViolation(long sourceIID, List<Long> targetIIDs, long ruleExpressionIID) throws RDBMSException;
  
  
  /**
   * 
   * @param sourceIIDs
   * @param targetIID
   * @param ruleExpressionIID
   * @throws RDBMSException
   */
  public void insertKpiUniqunessViolationForSourceEntities(List<Long> sourceIIDs, long targetIID, long ruleExpressionIID) throws RDBMSException;
  
  /**
   * 
   * @param sourceEntityIID
   * @return
   * @throws RDBMSException
   */
  public Map<Long, List<Long>> getAllTargetEntitiesForKpiUniqueness(long sourceEntityIID) throws RDBMSException;
  
  /**
   * 
   * @param entityIID
   * @throws RDBMSException
   */
  public void deleteFromKpiUniquenessViolation(long entityIID) throws RDBMSException;
  
  /**
   * 
   * @param ruleCode
   * @param entityCode
   * @param tagInfo 
   * @param taxonomyInfo 
   * @param string 
   * @return
   * @throws RDBMSException
   */
  public IRuleResultDTO loadKpiForUniqueness(String ruleCode, String entityCode, String type, List<String> taxonomyInfo, List<String> tagInfo) throws RDBMSException;
  
  /** Returns List of Base Entity IIDs who are violated for the given Rule Codes.
   * @param ruleCodes codes of data rule.
   * @return List of BaseEntity IIDs.
   * @throws RDBMSException
   */
  public List<Long> getViolatedEntitiesOfRuleCodes(Set<String> ruleCodes) throws RDBMSException;
  
  /**
   * @param baseEntityIIDs IIDs of base entity of which violations need to be fetched.
   * @return All the violations related to base entities.
   * @throws RDBMSException
   */
  public Map<Long,Set<IRuleViolationDTO>> loadViolations(Set<Long> baseEntityIIds) throws RDBMSException;
  
  /**
   * 
   * @param cteQuery
   * @return kpi results per rulecode and category
   * @throws RDBMSException
   */
  public Map<String,IRuleResultDTO> getKPIRuleResultsForGetAll(String cteQuery) throws RDBMSException;
  
  /**
   * 
   * @param baseEntityIid
   * @return last evaluated for kpi for baseentity
   * @throws RDBMSException
   */
  public Long getLastEvaluatedKpiForBaseEntity(Long baseEntityIid) throws RDBMSException;
}
