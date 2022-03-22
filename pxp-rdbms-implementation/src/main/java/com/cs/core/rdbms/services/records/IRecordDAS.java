package com.cs.core.rdbms.services.records;

import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.Set;

/**
 * The setContentFrom of applicable services
 *
 * @author vallee
 */
public interface IRecordDAS {
  
  /**
   * @return the record concerned by this setContentFrom of services
   */
  public PropertyRecordDTO getRecord();
  
  /**
   * @return true if the attached record is coupled
   */
  public default boolean isCalculated()
  {
    return getRecord().isCalculated();
  }
  
  /**
   * @param onlyDirect
   *          true means only direct dependencies are fetched
   * @return the set of record signatures which are dependent by calculation
   *         rule from the current record
   * @throws SQLException
   * @throws RDBMSException
   */
  public Set<String> getTargetNodesOfCalculation(boolean onlyDirect)
      throws SQLException, RDBMSException;
  
  /**
   * @return true if there exists records dependent by calculation on the
   *         current record
   * @throws SQLException
   * @throws RDBMSException
   */
  public boolean isSourceOfCalculation() throws SQLException, RDBMSException;
  
  /**
   * @return true if the record is coupled
   */
  public default boolean isCoupled()
  {
    return getRecord().isCoupled();
  }
  
  /**
   * @return true if the record participates in cloning. Record can either be
   *         source or target of cloning
   * @throws SQLException
   * @throws RDBMSException
   */
  public boolean isClonedRecord() throws RDBMSException, SQLException;
  
  /**
   * @param excludeDynamicBehavior
   *          true to exclude dynamic coupling targets, false to include all
   *          types of coupling
   * @return the set of record signatures which are coupled to the current
   *         record
   * @throws SQLException
   * @throws RDBMSException
   */
  public Set<String> getCouplingTargetNodeIDs(boolean excludeDynamicBehavior)
      throws SQLException, RDBMSException;
  
  /**
   * Note: same as getCouplingTargetNodeIDs(false) returning non empty targets
   *
   * @return true if there exists coupling records dependent on the current
   *         record
   * @throws SQLException
   * @throws RDBMSException
   */
  public boolean isSourceOfCoupling() throws SQLException, RDBMSException;
  
  /**
   * Note: same as getCouplingTargetNodeIDs(true) returning non empty targets
   *
   * @return true it there exists dependent coupling records by tightly coupling
   *         or cloning behavior
   * @throws SQLException
   * @throws RDBMSException
   */
  public boolean hasSeparableCouplingDependency() throws SQLException, RDBMSException;
  
  /**
   * Create a simple record in RDBMS (excluding coupled and calculated record)
   *
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void createSimpleRecord() throws SQLException, RDBMSException, CSFormatException;
  
  /**
   * Insert a coupled property record if a propertyRecordIID or a
   * masterSignature is determined (excluding simple and calculated record)
   *
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void createCoupledRecord() throws SQLException, RDBMSException, CSFormatException;
  
  /**
   * Create a calculated record in RDBMS (excluding simple and coupled record)
   *
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void createCalculatedRecord() throws SQLException, RDBMSException;
  
  /**
   * Update the record in RDBMS
   *
   * @throws SQLException
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void updateRecord() throws SQLException, RDBMSException, CSFormatException;
  
  /**
   * Update the coupled record in RDBMS
   *
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void updateCoupledRecord() throws RDBMSException, SQLException, CSFormatException;
  
  /**
   * Refresh the calculation and update the result
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public void updateCalculatedRecord() throws RDBMSException, SQLException;
  
  /**
   * for a source of coupling, prepare the dependent records before updating any
   * value => for Transfer rules, clone target records before update => for
   * tightly coupled rules, clone target records and notify
   *
   * @param targetSignatures
   *          are the identified targets of coupling
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public void notifyCouplingChange(Set<String> targetSignatures)
      throws RDBMSException, SQLException;
  
  /**
   * Resolves notification on basis of Record Status
   *
   * @throws RDBMSException
   * @throws SQLException
   */
  public void resolveNotification() throws RDBMSException, SQLException;
  
  /**
   * For coupling case that requires to clone the targets before any update,
   * this service identify target records and clone them into separated
   * instances -> for tight-linked rules, the status is passed to notification
   * -> for transfer rules, the trace of coupling is removed
   *
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void separateCoupledTargets(Long propertyIID, Long masterEntityIID) throws SQLException, RDBMSException;
  
  /**
   * delete the record when identified as source of coupling
   *
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public void deleteAsSourceOfCoupling() throws RDBMSException, SQLException, CSFormatException;
  
  /**
   * delete a record from RDBMS when it is not source of coupling or calculation
   *
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void deleteRecord() throws RDBMSException, SQLException, CSFormatException;
}
