package com.cs.core.rdbms.coupling.idao;

import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICouplingDAO {
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void createConflictingValueRecord(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void resolvedConflicts(ICouplingDTO couplingDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param targetEntityIID
   * @param sourceEntityIID
   * @param propertyIID
   * @param couplingSourceIID
   * @param localeIID
   * @throws RDBMSException
   */
  public void updateSourceCoupledRecord(Long targetEntityIID, Long sourceEntityIID, Long propertyIID, 
      Long couplingSourceIID, Long localeIID) throws RDBMSException;

  /**
   * 
   * @param targetEntityIID
   * @param sourceEntityIID
   * @param propertyIID
   * @param couplingSourceIID
   * @param localeIID 
   * @throws RDBMSException
   */
  public void updateSourceCoupledRecordForDynamicCoupling(Long targetEntityIID, Long sourceEntityIID, 
      Long propertyIID, Long couplingSourceIID, Long localeIID) throws RDBMSException;
  
  
  /**
   * 
   * @param sourceEntityIID
   * @param targetEntityIID
   * @param sourceCouplingIID
   * @throws RDBMSException
   */
  public void deleteCouplingRecord(Long sourceEntityIID, Long targetEntityIID, Long sourceCouplingIID, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  
  /**
   * 
   * 
   * @param couplingDTOs, list of coupling DTO's to be deleted
   * 
   * @throws RDBMSException
   */
  //public void deleteCouplingRecords(Set<ICouplingDTO> couplingDTOs) throws RDBMSException;  
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getCoupledConflictingValue(ICouplingDTO couplingDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param sourceEntityIID
   * @param propertyIID
   * @throws RDBMSException
   */
  public void insertValueRecordAtTarget(Long sourceEntityIID, Long propertyIID) throws RDBMSException;
  
  
  /**
   * 
   * @param masterEntityIID
   * @param targetEnitityIID 
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getCoupledRecords(Long masterEntityIID, Long targetEnitityIID) throws RDBMSException;
  
  /**
   * 
   * @param masterEntityIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingRecord(Long masterEntityIID) throws RDBMSException;
  
  /**
   * get conflicting values for specific coupling source
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public ICouplingDTO getConflictingValueForCouplingSource(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param entityIID
   * @throws RDBMSException
   */
  public void deleteCoupledRecord(Long entityIID, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  


  /**
   * 
   * @param entityIID
   * @param relationshipIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingValuesFromRelationship(Long entityIID, Long relationshipIID) throws RDBMSException;

  /**
   * 
   * @param targetEntityIID
   * @param sourceEntityIID
   * @param propertyIID
   * @param couplingSourceIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getCoupledRecordForDynamicCoupled(Long targetEntityIID, Long sourceEntityIID, 
      Long propertyIID, Long couplingSourceIID, Long localeIID) throws RDBMSException;
  
  /**
   * 
   * @param propertyIID
   * @param targetEntityIID
   * @throws RDBMSException
   */
  public void updateTargetConflictingValues(Long propertyIID, Long targetEntityIID, Long languageIID) throws RDBMSException;
  
  /**
   * 
   * @param entityIID
   * @param propertyIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getCoupledRecord(Long entityIID, Long propertyIID, Long languageIID) throws RDBMSException;
  
  /**
   * 
   * @param propertyRecords
   * @param couplingDTO
   * @param catalogDao
   * @throws RDBMSException
   */
  public void createCoupledRecord(List<IPropertyRecordDTO> propertyRecords, ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao, IBaseEntityDTO baseEntityDTO, Map<Long, RecordStatus> recordStatusInfo) throws RDBMSException;
 
  /**
   * 
   * @param sourceCouplingIID
   * @param propertyIId
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictsCoupledRecord(Long sourceCouplingIID, Long propertyIId) throws RDBMSException;
 
  /**
   * 
   * @param sourceEntityIID
   * @param targetEntityIID
   * @param propertyIID
   * @throws RDBMSException
   */
  public void deleteCoupledRecord(Long sourceEntityIID, Long targetEntityIID, Long propertyIID, Long localeiid, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  
  /**
   * 
   * @param propertyIID
   * @param sourceCouplingIID
   * @throws RDBMSException
   */
  public void deleteConflictingValues(Long propertyIID, Long sourceCouplingIID) throws RDBMSException;
  
  /**
   * 
   * @param sourceCouplingIID
   * @param propertyIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingValuesByCouplingSourceIIDAndPropertyIID(Long sourceCouplingIID, Long propertyIID) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getSourceConflictingValues(ICouplingDTO couplingDTO, CouplingBehavior couplingType) throws RDBMSException;
  
  /**
   * get notified record from conflicting values without considering coupling
   * source
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getNotifiedConflictingValues(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void updateRecordStatusForConflictingValues(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @param recordStatus
   * @throws RDBMSException
   */
  public void updateCouplingTypeForConflictingValues(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @param recordStatus
   * @throws RDBMSException
   */
  public void updateCouplingTypeForCoupledRecord(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getSourceConflictingValuesForRelationship(ICouplingDTO couplingDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param couplingDTO
   * @param couplingBehavior
   * @param recordStatus
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getSourceConflictingValuesForRecordStatus(ICouplingDTO couplingDTO, 
      CouplingBehavior couplingBehavior, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void createCoupledRecordForContextual(ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO)
      throws RDBMSException;
  
 
  /**
   * create coupling for classification.
   * 
   * @Param propertyrecords
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void createCoupledRecordsForCLassification(List<IPropertyRecordDTO> propertyRecords, ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO, Map<Long, RecordStatus> recordStatus) throws RDBMSException;

  /**
   *
   * @param deletedContextEntity
   * @param deletedEntities
   * @return
   * @throws RDBMSException
   */
  public List<Long> getEntitiesForDelete(Long deletedContextEntity, List<Long> deletedEntities) throws RDBMSException;
  /**
   * 
   * @param couplingSourceIID
   * @param propertyIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingValuesByCouplingSourceIID(Long couplingSourceIID, Long propertyIID) throws RDBMSException;
  
  
  /**
   * 
   * @param couplingSourceIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getSourceAndTargetEntitiesByCouplingSourceIID(Long couplingSourceIID) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void deleteCouplingRecordFromConflictingValues(ICouplingDTO couplingDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param sourceBaseEntityIIDs
   * @param targetBaseEntityIIDs
   * @return
   * @throws RDBMSException
   */
  public List<String> getlanguageCodesByBaseEntityIIDs(Long sourceBaseEntityIIDs, Long targetBaseEntityIIDs) throws RDBMSException; 
  
  /**
   * 
   * @param baseEntityIID
   * @param sourceLocaleIID
   * @param recordStatus
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getAllTargetLocaleIIDs(Long baseEntityIID, Long sourceLocaleIID, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void deleteConflictingValuesByLocaleIIds(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void deleteCoupledRecordByLocaleIIDAndPropertyIID(ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void deleteCoupledRecordByLocaleIID(ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getCoupledRecordForLocaleIID(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @throws RDBMSException
   */
  public void updateConflictingValuesByLocaleIID(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException;
  
  /**
   * 
   * @param parentIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingValues(Long parentIID) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getConflictingValuesByLocaleIID(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param sourceEntityIID
   * @return
   * @throws RDBMSException
   */
  public Map<String, IBGPCouplingDTO> getOtherSideEntitesFromRelationship(Long sourceEntityIID) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist(ICouplingDTO couplingDTO) throws RDBMSException;
  
  /**
   * 
   * @param couplingDTO
   * @return
   * @throws RDBMSException
   */
  public Boolean checkWheatherConflictingValuesAlreadyExistForCoupingSource(ICouplingDTO couplingDTO) throws RDBMSException;
  
  
  /**
   * 
   * @param entityIID
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getOtherSideEntitesForContextual(Long entityIID) throws RDBMSException;
  
  /**
   * 
   * @param sourceEntityIID
   * @param recordStatus
   * @return
   * @throws RDBMSException
   */
  public List<ICouplingDTO> getTargetConflictingValues(Long sourceEntityIID, CouplingType couplingType, 
      RecordStatus recordStatus, Long localeiid) throws RDBMSException;
  
  /**
   * 
   * @param sourceEntityiid
   * @param localeiid
   * @throws RDBMSException
   */
  public void deleteCouplingRecordsForTranslationDelete(Long sourceEntityiid, Long localeiid) throws RDBMSException;
  
  /**
   * 
   * @param propertyRecord
   * @throws RDBMSException
   */
  public void deletePropertyRecord(IPropertyRecordDTO propertyRecord) throws RDBMSException;
  
  /**
   * return all base entity IIDs for specified classifier iid
   * 
   * @param classifierIID
   * @param classifierType
   * @return
   * @throws RDBMSException
   */
  public Set<Long> getAllEntityIIDsForCLassifierIID(Long classifierIID, ClassifierType classifierType) throws RDBMSException;

  /**
   *
   * @param couplingDTO
   * @param localeCatalogDAO
   * @throws RDBMSException
   */
  public void createCoupledRecord(ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException;
  
  /**
   * 
   * @param propertyRecords
   * @param couplingDTO
   * @param catalogDao
   * @param baseEntity
   * @throws RDBMSException
   */
  public void raiseEventsForCoupling(IPropertyRecordDTO propertyRecords, ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao, IBaseEntityDTO baseEntity) throws RDBMSException;
  
  /**
   * 
   * @param topParentIID
   * @return
   * @throws RDBMSException
   */
  public List<IContextualDataTransferGranularDTO> triggerContextualDataTransferFromCloning(Long topParentIID) throws RDBMSException;
}
