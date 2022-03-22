package com.cs.core.rdbms.uniqueViolation.idao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface IUniquenessViolationDAO {
  
  /**
   * 
   * @param propertyIID
   * @param baseEntityIId
   * @param classifierIID
   * @param string
   * @return List<long> violatedEntites
   * @throws RDBMSException
   */
  public List<Long> evaluateProductIdentifier(long propertyIID, long baseEntityIId, long classifierIID,
      String string) throws RDBMSException;
  
  /**
   * 
   * @param uniquenessViolationDTO
   * @throws RDBMSException
   */
  public void insertViolatedEntity(List<IUniquenessViolationDTO> uniquenessViolationDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param baseEntityIID
   * @param propertyIID
   * @param classifierIID
   * @throws RDBMSException
   */
  public void deleteBeforeEvaluateIdentifier(long baseEntityIID, List<Long> propertyIIDs, long classifierIID) throws RDBMSException;
  
  
  /**
   * 
   * @param uniquenessViolationDTO
   * @return Boolean
   * @throws RDBMSException
   */
  public List<Long> isUniqueRecord(IUniquenessViolationDTO uniquenessViolationDTO, Set<IClassifierDTO> classifierIIDs) throws RDBMSException;
  
  
  /**
   * 
   * @param sourceIID
   * @return count 
   * @throws RDBMSException
   */
  public Integer isUniqueRecordForTileView(long sourceIID) throws RDBMSException;
  
  
  public Map<Long,Integer> getUniquenessViolationCount(Set<Long> baseEntityIIds) throws RDBMSException;
  
  public List<Long> evaluateProductIdentifierForMigration(long propertyIID, long classifierIID, String catalogCode, String value)
      throws RDBMSException;
}
