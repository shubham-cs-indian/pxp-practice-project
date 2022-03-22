package com.cs.core.rdbms.coupling.dao;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.BGPCouplingDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.dto.CouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.EntityEventDAS;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.records.IRecordDAS;
import com.cs.core.rdbms.services.records.RecordDASFactory;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unused")
public class CouplingDAO implements ICouplingDAO {

  private final long    userIID;
  private final String  catalogCode;

  public CouplingDAO(long userIID, String catalogCode)
  {
    this.userIID = userIID;
    this.catalogCode = catalogCode;
  }

  @Override
  public void createCoupledRecord(ICouplingDTO couplingDTO, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.createCoupledRecord(couplingDTO);

    });

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IBaseEntityDTO entityByIID = localeCatalogDAO.getEntityByIID(couplingDTO.getTargetEntityIID());
      IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(entityByIID);
      IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
      baseEntityDAO.loadPropertyRecords(propertyByIID);
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, localeCatalogDAO.getUserSessionDTO(), localeCatalogDAO.getLocaleCatalogDTO(),
          entityByIID);
      eventDAS.registerChange(ITimelineDTO.ChangeCategory.CouplingSource, baseEntityDAO.getBaseEntityDTO().getPropertyRecord(couplingDTO.getPropertyIID()));
      eventDAS.postRegisteredChanges();
    });
  }

  @Override
  public void createConflictingValueRecord(ICouplingDTO couplingDTO) throws RDBMSException{
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.createConflictingValueRecord(couplingDTO);
    });
  }


  @Override
  public void resolvedConflicts(ICouplingDTO couplingDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.resolveConflict(couplingDTO);
    });
  }

  @Override
  public void updateSourceCoupledRecord(Long targetEntityIID, Long sourceEntityIID, Long propertyIID,
      Long couplingSourceIID, Long localeIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.updateSourceCoupledRecord(targetEntityIID, sourceEntityIID, propertyIID,
          couplingSourceIID, localeIID);
    });
  }

  @Override
  public void updateSourceCoupledRecordForDynamicCoupling(Long targetEntityIID, Long sourceEntityIID, Long propertyIID, 
      Long couplingSourceIID, Long localeIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.updateSourceCoupledRecordForDynamicCoupling(targetEntityIID, sourceEntityIID, propertyIID, 
          couplingSourceIID, localeIID);
    });
  }

  private static final String DELETE_RECORD_FROM_CONFLICTING = "delete from pxp.conflictingvalues cv where "
      + "cv.targetentityiid = ? and cv.sourceentityiid = ? and cv.couplingsourceiid = ? ";

  private static final String SELECT_CONFLICTING_COUPLED_RECORD = "select * from pxp.conflictingvalues where sourceentityiid = ? "
      + "and targetentityiid = ? and couplingsourceiid = ? and recordstatus = ?";
  
  private static final String UPDATE_TIGHT_CONFLICTING_RECORD = "Update pxp.conflictingvalues set recordStatus = ? where "
      + "targetentityiid = ? and couplingsourceiid = ? and propertyIID = ?";

  @Override
  public void deleteCouplingRecord(Long sourceEntityIID, Long targetEntityIID, Long couplingSourceIID, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      deleteCoupledRecord(sourceEntityIID, targetEntityIID, couplingSourceIID, currentConn, localeCatalogDAO);
    });
  }

  private void deleteCoupledRecord(Long sourceEntityIID, Long targetEntityIID, Long couplingSourceIID, RDBMSConnection currentConn, ILocaleCatalogDAO localeCatalogDAO)
      throws RDBMSException, SQLException, CSFormatException
  {
    List<ICouplingDTO> coupledRecordToDelete = new ArrayList<>();

    PreparedStatement stmt2 = currentConn.prepareStatement(SELECT_CONFLICTING_COUPLED_RECORD);
    stmt2.setLong(1, sourceEntityIID);
    stmt2.setLong(2, targetEntityIID);
    stmt2.setLong(3, couplingSourceIID);
    stmt2.setInt(4, RecordStatus.COUPLED.ordinal());
    stmt2.execute();


    IResultSetParser resultSet = currentConn.getResultSetParser(stmt2.getResultSet());;
    while(resultSet.next()) {
      coupledRecordToDelete.add(new CouplingDTO(resultSet));
    }


    PreparedStatement stmt = currentConn.prepareStatement(DELETE_RECORD_FROM_CONFLICTING);
    stmt.setLong(1, targetEntityIID);
    stmt.setLong(2, sourceEntityIID);
    stmt.setLong(3, couplingSourceIID);
    stmt.execute();

    for(ICouplingDTO coupledDTO : coupledRecordToDelete) {
     deleteCoupledRecord(coupledDTO.getSourceEntityIID(), coupledDTO.getTargetEntityIID(), coupledDTO.getPropertyIID(), coupledDTO.getLocaleIID(), localeCatalogDAO);
    }
    
    //update the record status for tight coupled records
    if(coupledRecordToDelete.size() != 0) {
      ICouplingDTO couplingDTO = coupledRecordToDelete.get(0);
      if(couplingDTO.getCouplingType().equals(CouplingBehavior.TIGHTLY)) {
        PreparedStatement updateConflictingValueForTightCoupling = currentConn.prepareStatement(UPDATE_TIGHT_CONFLICTING_RECORD);
        updateConflictingValueForTightCoupling.setInt(1, RecordStatus.NOTIFIED.ordinal());
        updateConflictingValueForTightCoupling.setLong(2, targetEntityIID);
        updateConflictingValueForTightCoupling.setLong(3, couplingSourceIID);
        updateConflictingValueForTightCoupling.setLong(4, couplingDTO.getPropertyIID());
        updateConflictingValueForTightCoupling.execute();
      }
    }
  }


  /*  @Override
  public void deleteCouplingRecords(Set<ICouplingDTO> couplingDTOs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      for(ICouplingDTO couplingDTO: couplingDTOs) {
        deleteCoupledRecord(couplingDTO.getSourceEntityIID(),couplingDTO.getTargetEntityIID(), couplingDTO.getCouplingSourceIID(), currentConn);
      }
    });
  }*/

  private static final String GET_CONFLICTING_VALUE = "select * from pxp.conflictingvalues where sourceentityiid = ? and targetentityiid = ? " +
      "and propertyiid = ? and couplingsourceiid = ? and recordstatus = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getCoupledConflictingValue(ICouplingDTO couplingDTO) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUE);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getCouplingSourceIID());
      stmt.setInt(5, couplingDTO.getRecordStatus().ordinal());
      stmt.setLong(6, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_CONFLICTING_VALUE_FOR_COUPLING_SOURCE = "select * from pxp.conflictingvalues where sourceentityiid = ? and targetentityiid = ? "
      + "and propertyiid = ? and couplingsourceiid = ? and localeiid = ?";
  
  @Override
  public ICouplingDTO getConflictingValueForCouplingSource(ICouplingDTO couplingDTO) throws RDBMSException
  {
    ICouplingDTO[] couplingDTOs = new ICouplingDTO[1];
    couplingDTOs[0] = null;
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUE_FOR_COUPLING_SOURCE);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getCouplingSourceIID());
      stmt.setLong(5, couplingDTO.getLocaleIID());
      stmt.execute();
      
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      if (resultSet.next()) {
        couplingDTOs[0] = new CouplingDTO(resultSet);
      }
    });
    return couplingDTOs[0];
  }
  
  private static final String GET_NOTIFIED_CONFLICTING_VALUE_ = "select * from pxp.conflictingvalues where propertyiid = ? "
      + "and couplingsourceiid <> ? and targetentityiid = ? and recordstatus = ? and localeiid = ?";
  
  @Override
  public List<ICouplingDTO> getNotifiedConflictingValues(ICouplingDTO couplingDTO) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(GET_NOTIFIED_CONFLICTING_VALUE_);
      stmt.setLong(1, couplingDTO.getPropertyIID());
      stmt.setLong(2, couplingDTO.getCouplingSourceIID());
      stmt.setLong(3, couplingDTO.getTargetEntityIID());
      stmt.setInt(4, couplingDTO.getRecordStatus().ordinal());
      stmt.setLong(5, couplingDTO.getLocaleIID());
      
      stmt.execute();
      
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      if (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }
  
  @Override
  public void insertValueRecordAtTarget(Long sourceEntityIID, Long propertyIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      entityDAS.insertValueRecordAtTarget(sourceEntityIID, propertyIID);
    });
  }

  private static final String GET_COUPLED_RECORD = "select * from pxp.coupledrecord where masterentityiid = ? and entityiid= ?";

  @Override
  public List<ICouplingDTO> getCoupledRecords(Long masterEntityIID,Long targetEntityIID) throws RDBMSException
  {
     List<ICouplingDTO> coupledRecord = new ArrayList<>();

     RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_COUPLED_RECORD);
      stmt.setLong(1, masterEntityIID);
      stmt.setLong(2, targetEntityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());;
      while(resultSet.next()) {
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setPropertyIID(resultSet.getLong("propertyiid"));
        couplingDTO.setLocaleIID(resultSet.getLong("localeiid"));
        coupledRecord.add(couplingDTO);
      }

    });

    return coupledRecord;
  }

  private static final String GET_CONFLICTING_VALUE_RECORD = "select * from pxp.conflictingvalues where sourceentityiid = ?";

  @Override
  public List<ICouplingDTO> getConflictingRecord(Long masterEntityIID) throws RDBMSException
  {
    List<ICouplingDTO> coupledRecord = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUE_RECORD);
      stmt.setLong(1, masterEntityIID);
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      ;
      while (resultSet.next()) {
        coupledRecord.add(new CouplingDTO(resultSet));
      }
    });
    return coupledRecord;
  }

  private static final String DELETE_FROM_CONFLICTING = "delete from pxp.conflictingvalues where sourceentityiid = ? OR targetentityiid = ? ";

  private static final String DELETE_FROM_COUPLED = "delete from pxp.coupledrecord where entityiid = ? or masterentityiid = ?" ;

  @Override
  public void deleteCoupledRecord(Long entityIID, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, localeCatalogDAO.getUserSessionDTO(), localeCatalogDAO.getLocaleCatalogDTO(),
          localeCatalogDAO.getEntityByIID(entityIID));

      PreparedStatement stmt = currentConn.prepareStatement(DELETE_FROM_CONFLICTING);
      stmt.setLong(1, entityIID);
      stmt.setLong(2, entityIID);
      stmt.execute();

      PreparedStatement stmt1 = currentConn.prepareStatement(DELETE_FROM_COUPLED);
      stmt1.setLong(1, entityIID);
      stmt1.setLong(2, entityIID);
      stmt1.execute();

    });
  }

  private static final String GET_CONFLICTING_VALUES_FROM_RELATIONSHIP = "Select * from pxp.conflictingvalues where couplingsourceiid = ?" ;


  @Override
  public List<ICouplingDTO> getConflictingValuesFromRelationship(Long entityIID, Long relationshipIID) throws RDBMSException
  {
    List<ICouplingDTO> dtos = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuilder query = new StringBuilder();
      query.append(GET_CONFLICTING_VALUES_FROM_RELATIONSHIP);
      query.append("and sourceentityiid = "+ entityIID);

      PreparedStatement stmt = currentConn.prepareStatement(query);
      stmt.setLong(1, relationshipIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());;
      while(resultSet.next()) {
        dtos.add(new CouplingDTO(resultSet));
      }

      if(dtos.isEmpty()) {
        query = new StringBuilder();
        query.append(GET_CONFLICTING_VALUES_FROM_RELATIONSHIP);
        query.append("and targetentityiid = "+ entityIID);

        stmt = currentConn.prepareStatement(query);
        stmt.setLong(1, relationshipIID);
        stmt.execute();

        resultSet = currentConn.getResultSetParser(stmt.getResultSet());;
        while(resultSet.next()) {
          dtos.add(new CouplingDTO(resultSet));
        }
      }
    });
    return dtos;
  }
  private static final String GET_COUPLED_RECORD_FOR_DYNAMIC = "Select* from pxp.coupledrecord where entityiid = ? and masterentityiid = ? and "
      + "propertyiid = ? and localeiid = ?";


  @Override
  public List<ICouplingDTO> getCoupledRecordForDynamicCoupled(Long targetEntityIID, Long sourceEntityIID, Long propertyIID,
      Long couplingSourceIID, Long localeIID) throws RDBMSException
  {
      List<ICouplingDTO> couplingDTOs = new ArrayList<>();
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_COUPLED_RECORD_FOR_DYNAMIC);
      stmt.setLong(1, targetEntityIID);
      stmt.setLong(2, sourceEntityIID);
      stmt.setLong(3, propertyIID);
      stmt.setLong(4, localeIID);
      stmt.execute();


      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());;
      while(resultSet.next()) {
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setTargetEntityIID(resultSet.getLong("entityiid"));
        couplingDTO.setPropertyIID(resultSet.getLong("propertyiid"));
        couplingDTO.setSourceEntityIID(resultSet.getLong("masterentityiid"));
        couplingDTOs.add(couplingDTO);
      }

    });
    return couplingDTOs;
  }

  private static final String UPDATE_CONFLICTING_VALUES = "update pxp.conflictingvalues set recordstatus = 2 where targetentityiid = ? and propertyiid = ? and "
      + "recordstatus in (6, 4) and localeiid = ?";

  @Override
  public void updateTargetConflictingValues(Long propertyIID, Long targetEntityIID, Long languageIID) throws RDBMSException
  {

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(UPDATE_CONFLICTING_VALUES);
      stmt.setLong(1, targetEntityIID);
      stmt.setLong(2, propertyIID);
      stmt.setLong(3, languageIID);
      stmt.execute();

    });

  }

  private static final String GET_COUPLED_RECORD1 = "select * from pxp.coupledrecord where entityiid = ? and "
      + "propertyiid = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getCoupledRecord(Long entityIID, Long propertyIID, Long languageIID) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
     RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_COUPLED_RECORD1);
      stmt.setLong(1, entityIID);
      stmt.setLong(2, propertyIID);
      stmt.setLong(3, languageIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setTargetEntityIID(resultSet.getLong("entityiid"));
        couplingDTO.setPropertyIID(resultSet.getLong("propertyiid"));
        couplingDTO.setSourceEntityIID(resultSet.getLong("masterentityiid"));
        couplingDTO.setLocaleIID(resultSet.getLong("localeiid"));
        couplingDTO.setCouplingSourceType(CouplingType.valueOf(resultSet.getInt("couplingtype")));
        couplingDTOs.add(couplingDTO);
      }
     });
     return couplingDTOs;
  }

  @Override
  public void createCoupledRecord(List<IPropertyRecordDTO> propertyRecords, ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao, IBaseEntityDTO baseEntity, Map<Long, RecordStatus> recordStatusInfo) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(),
          baseEntity);
      for(IPropertyRecordDTO record : propertyRecords) {
        long entityIID = record.getEntityIID();
        IRecordDAS recordService = RecordDASFactory.instance()
            .newService(currentConn, (LocaleCatalogDAO) catalogDao, record, entityIID);
        CouplingDAS entityDAS = new CouplingDAS(currentConn);
        RecordStatus recordStatus = recordStatusInfo.get(record.getProperty().getPropertyIID());
        RecordStatus coupledRecord = recordStatus == null ? RecordStatus.UNDEFINED : recordStatus;
        couplingDTO.setRecordStatus(coupledRecord);
        Boolean isCoupledRecord = entityDAS.createCoupledRecord(recordService, record, couplingDTO);
        
        if(isCoupledRecord) {
          eventDAS.registerCreatedCouplingRecord(recordService);
        }
      }
      eventDAS.postRegisteredChanges();
     });

  }

  private static final String GET_CONFLICTING_RECORD = "select * from pxp.conflictingvalues where "
      + "propertyiid = ? and couplingsourceiid = ? and recordstatus = ?";

  @Override
  public List<ICouplingDTO> getConflictsCoupledRecord(Long sourceCouplingIID, Long propertyIID) throws RDBMSException
  {

    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_RECORD);
      stmt.setLong(1, propertyIID);
      stmt.setLong(2, sourceCouplingIID);
      stmt.setLong(3, RecordStatus.COUPLED.ordinal());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String DELETE_RECORD_FROM_COUPLED = "delete from pxp.coupledrecord cr "
      + "where cr.entityiid = ? and cr.masterentityiid = ? and cr.propertyiid = ? and cr.localeiid = ?";

  @Override
  public void deleteCoupledRecord(Long sourceEntityIID, Long targetEntityIID, Long propertyIID, Long localeiid, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      /*
      IBaseEntityDTO entityByIID = catalogDao.getEntityByIID(targetEntityIID);
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(), entityByIID);
      */
      PreparedStatement stmt1 = currentConn.prepareStatement(DELETE_RECORD_FROM_COUPLED);
      stmt1.setLong(1, targetEntityIID);
      stmt1.setLong(2, sourceEntityIID);
      stmt1.setLong(3, propertyIID);
      stmt1.setLong(4, localeiid);
      stmt1.execute();
      /*
      IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
      IBaseEntityDTO baseEntityDTO = catalogDao.openBaseEntity(entityByIID).loadPropertyRecords(propertyByIID);
      eventDAS.registerChange(ITimelineDTO.ChangeCategory.RemovedCouplingSource, baseEntityDTO.getPropertyRecord(propertyIID));
      eventDAS.postRegisteredChanges();
      */
    });
  }

  private static final String DELETE_CONFLICTING = "delete from pxp.conflictingvalues where couplingsourceiid = ? " + "and propertyiid = ?";

  @Override
  public void deleteConflictingValues(Long propertyIID, Long sourceCouplingIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_CONFLICTING);
      stmt.setLong(1, sourceCouplingIID);
      stmt.setLong(2, propertyIID);
      stmt.execute();
    });
  }

  private static final String GET_CONFLICTING_VALUES = "select * from pxp.conflictingvalues where " +
      "propertyiid = ? and couplingsourceiid = ?";

  @Override
  public List<ICouplingDTO> getConflictingValuesByCouplingSourceIIDAndPropertyIID(Long sourceCouplingIID, Long propertyIID) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUES);
      stmt.setLong(1, propertyIID);
      stmt.setLong(2, sourceCouplingIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_SOURCE_CONFLICTING = "select * from pxp.conflictingvalues where propertyiid = ? "
      + "and couplingsourceiid <> ? and targetentityiid = ? and couplingtype = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getSourceConflictingValues(ICouplingDTO couplingDTO, CouplingBehavior couplingType) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_SOURCE_CONFLICTING);
      stmt.setLong(1, couplingDTO.getPropertyIID());
      stmt.setLong(2, couplingDTO.getCouplingSourceIID());
      stmt.setLong(3, couplingDTO.getTargetEntityIID());
      stmt.setInt(4, couplingType.ordinal());
      stmt.setLong(5, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_SOURCE_CONFLICTING_FOR_RELATIONSHIP = "select * from pxp.conflictingvalues where propertyiid = ? "
      + "and couplingsourceiid = ? and targetentityiid = ? and localeiid = ? ";

  @Override
  public List<ICouplingDTO> getSourceConflictingValuesForRelationship(ICouplingDTO couplingDTO)
      throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_SOURCE_CONFLICTING_FOR_RELATIONSHIP);
      stmt.setLong(1, couplingDTO.getPropertyIID());
      stmt.setLong(2, couplingDTO.getCouplingSourceIID());
      stmt.setLong(3, couplingDTO.getTargetEntityIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String UPDATE_RECORDSTATUS = "update pxp.conflictingvalues set recordstatus = ? where "
      + "sourceentityiid = ? and targetentityiid = ? and propertyiid = ? and couplingsourceiid = ? and localeiid = ?";

  @Override
  public void updateRecordStatusForConflictingValues(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(UPDATE_RECORDSTATUS);
      stmt.setInt(1, recordStatus.ordinal());
      stmt.setLong(2, couplingDTO.getSourceEntityIID());
      stmt.setLong(3, couplingDTO.getTargetEntityIID());
      stmt.setLong(4, couplingDTO.getPropertyIID());
      stmt.setLong(5, couplingDTO.getCouplingSourceIID());
      stmt.setLong(6, couplingDTO.getLocaleIID());
      stmt.execute();

    });
  }

  private static final String UPDATE_COUPLING_TYPE_CONFLICTING = "update pxp.conflictingvalues set recordstatus = ? , "
      + "couplingtype = ? , couplingsourcetype = ? where sourceentityiid = ? and targetentityiid = ? "
      + "and propertyiid = ? and couplingsourceiid = ? and localeiid = ?";


  @Override
  public void updateCouplingTypeForConflictingValues(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(UPDATE_COUPLING_TYPE_CONFLICTING);
      stmt.setInt(1, recordStatus.ordinal());
      stmt.setInt(2, couplingDTO.getCouplingType().ordinal());
      stmt.setInt(3, couplingDTO.getCouplingSourceType().ordinal());
      stmt.setLong(4, couplingDTO.getSourceEntityIID());
      stmt.setLong(5, couplingDTO.getTargetEntityIID());
      stmt.setLong(6, couplingDTO.getPropertyIID());
      stmt.setLong(7, couplingDTO.getCouplingSourceIID());
      stmt.setLong(8, couplingDTO.getLocaleIID());
      stmt.execute();

    });
  }

  private static final String UPDATE_COUPLING_TYPE_COUPLED = "update pxp.coupledrecord set recordstatus = ? , "
      + "couplingbehavior = ? , couplingtype = ? where masterentityiid = ? and entityiid = ? "
      + "and propertyiid = ? and localeiid = ?";

  @Override
  public void updateCouplingTypeForCoupledRecord(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(UPDATE_COUPLING_TYPE_COUPLED);
      stmt.setInt(1, recordStatus.ordinal());
      stmt.setInt(2, couplingDTO.getCouplingType().ordinal());
      stmt.setInt(3, couplingDTO.getCouplingSourceType().ordinal());
      stmt.setLong(4, couplingDTO.getSourceEntityIID());
      stmt.setLong(5, couplingDTO.getTargetEntityIID());
      stmt.setLong(6, couplingDTO.getPropertyIID());
      stmt.setLong(7, couplingDTO.getLocaleIID());
      stmt.execute();

    });
  }


  private static final String GET_SOURCE_CONFLICTING_FOR_RECORD_STATUS = "select * from pxp.conflictingvalues where propertyiid = ? "
      + "and couplingsourceiid <> ? and targetentityiid = ? and couplingtype = ? and recordstatus = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getSourceConflictingValuesForRecordStatus(ICouplingDTO couplingDTO, CouplingBehavior couplingType,
      RecordStatus recordStatus) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_SOURCE_CONFLICTING_FOR_RECORD_STATUS);
      stmt.setLong(1, couplingDTO.getPropertyIID());
      stmt.setLong(2, couplingDTO.getCouplingSourceIID());
      stmt.setLong(3, couplingDTO.getTargetEntityIID());
      stmt.setInt(4, couplingType.ordinal());
      stmt.setInt(5, recordStatus.ordinal());
      stmt.setLong(6, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  @Override
  public void createCoupledRecordForContextual(ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IBaseEntityDAO baseEntityDAO = catalogDao.openBaseEntity(catalogDao.getEntityByIID(couplingDTO.getTargetEntityIID()));
      IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
      baseEntityDAO.loadPropertyRecords(propertyByIID);
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(),
          baseEntityDAO.getBaseEntityDTO());
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      
      Boolean isCoupledCreated = entityDAS.createCoupledRecordForContextual(couplingDTO);
      
      if(isCoupledCreated) {
        eventDAS.registerChange(ITimelineDTO.ChangeCategory.CouplingSource, baseEntityDAO.getBaseEntityDTO().getPropertyRecord(couplingDTO.getPropertyIID()));
        eventDAS.postRegisteredChanges();
      }

    });
  }

  @Override
  public void createCoupledRecordsForCLassification(List<IPropertyRecordDTO> propertyRecords, ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao, Map<Long, RecordStatus> recordStatus) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      IBaseEntityDTO entityByIID = catalogDao.getEntityByIID(couplingDTO.getTargetEntityIID());
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(),entityByIID);

      for (IPropertyRecordDTO propertyRecordDTO : propertyRecords) {
        if(propertyRecordDTO.getCouplingBehavior().equals(CouplingBehavior.TIGHTLY)) {
          RecordStatus newRecordStatus = recordStatus.get(propertyRecordDTO.getProperty().getPropertyIID());
          RecordStatus coupledRecord = newRecordStatus == null ? RecordStatus.UNDEFINED : newRecordStatus;
          couplingDTO.setRecordStatus(coupledRecord);
        }
        Boolean isCoupledCreated = entityDAS.createCoupledRecordForClassification(propertyRecordDTO, couplingDTO);
        
        if(isCoupledCreated) {
          eventDAS.registerChange(ITimelineDTO.ChangeCategory.CouplingSource, propertyRecordDTO);
        }
      }
      eventDAS.postRegisteredChanges();
    });
  }



  private static final String GET_CONFLICTING_VALUES_BY_COUPLING_SOURCE_IID = "select * from pxp.conflictingvalues where "
      + "couplingsourceiid = ? and  propertyiid = ?";

  @Override
  public List<ICouplingDTO> getConflictingValuesByCouplingSourceIID(Long couplingSourceIID, Long propertyIID) throws RDBMSException
  {

    List<ICouplingDTO> couplingDTOs =  new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUES_BY_COUPLING_SOURCE_IID);
      stmt.setLong(1, couplingSourceIID);
      stmt.setLong(2, propertyIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_ENTITIES = "select targetentityiid from pxp.conflictingvalues where "
      + "sourceentityiid = ? and ( couplingsourcetype = " +CouplingType.TIGHT_CONTEXTUAL.ordinal()+ " OR "
          + "couplingsourcetype = " + CouplingType.DYN_CONTEXTUAL.ordinal() + " )";
  
  @Override
  public List<Long> getEntitiesForDelete(Long deletedContextEntity, List<Long> deletedEntities) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_ENTITIES);
      stmt.setLong(1, deletedContextEntity);
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());

      while (resultSet.next()) {
        deletedEntities.add(resultSet.getLong("targetentityiid"));
        getEntitiesForDelete(resultSet.getLong("targetentityiid"), deletedEntities);
      }
    });
    return deletedEntities;
  }

  private static final String GET_SOURCE_AND_TARGET_ENTITIES = "select * from pxp.baseentity where classifieriid = ? and ismerged != true";

  @Override
  public List<ICouplingDTO> getSourceAndTargetEntitiesByCouplingSourceIID(Long couplingSourceIID) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_SOURCE_AND_TARGET_ENTITIES);
      stmt.setLong(1, couplingSourceIID);
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());

      while (resultSet.next()) {
        if(resultSet.getLong("parentiid") != 0) {
          
          ICouplingDTO couplingDTO = new CouplingDTO();
          couplingDTO.setSourceEntityIID(resultSet.getLong("parentiid"));
          couplingDTO.setTargetEntityIID(resultSet.getLong("baseentityiid"));
          couplingDTOs.add(couplingDTO);
        }
      }
    });
    return couplingDTOs;
  }

private static final String DELETE_COUPLING_RECORDS = "delete from pxp.conflictingvalues where sourceentityiid = ? and "
      + "targetentityiid = ? and propertyiid = ? and couplingsourceiid = ? and localeiid = ?";

  @Override
  public void deleteCouplingRecordFromConflictingValues(ICouplingDTO couplingDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_COUPLING_RECORDS);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getCouplingSourceIID());
      stmt.setLong(5, couplingDTO.getLocaleIID());
      stmt.execute();

    });
  }

  private static final String GET_LANGUAGE_CODES = "select localeid from pxp.baseentitylocaleidlink where baseentityiid = ? "
      + "intersect "
      + "select localeid from pxp.baseentitylocaleidlink where baseentityiid = ?";

  @Override
  public List<String> getlanguageCodesByBaseEntityIIDs(Long sourceBaseEntityIID, Long targetBaseEntityIID) throws RDBMSException
  {
    List<String> languageCodes = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_LANGUAGE_CODES);
      stmt.setLong(1, sourceBaseEntityIID);
      stmt.setLong(2, targetBaseEntityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        languageCodes.add(resultSet.getString("localeid"));
      }
    });
    return languageCodes;
  }

  private static final String GET_ALL_TARGET_LOCALE_IIDS = "select * from pxp.conflictingvalues where sourceentityiid = ? "
      + "and targetentityiid = ? and couplingsourceiid = ? and couplingsourcetype = ? and recordstatus = ?";

  @Override
  public List<ICouplingDTO> getAllTargetLocaleIIDs(Long baseEntityIID, Long sourceLocaleIID, RecordStatus recordStatus)
      throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_ALL_TARGET_LOCALE_IIDS);
      stmt.setLong(1, baseEntityIID);
      stmt.setLong(2, baseEntityIID);
      stmt.setLong(3, sourceLocaleIID);
      stmt.setInt(4, CouplingType.LANG_INHERITANCE.ordinal());
      stmt.setInt(5, recordStatus.ordinal());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String DELETE_CONFLICTING_VALUES_BY_LOCALE_IID = "delete from pxp.conflictingvalues where "
      + "sourceentityiid = ?  and targetentityiid = ? and couplingsourceiid = ? OR localeiid = ? "
      + "and couplingsourcetype = ?";

  @Override
  public void deleteConflictingValuesByLocaleIIds(ICouplingDTO couplingDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_CONFLICTING_VALUES_BY_LOCALE_IID);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getCouplingSourceIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.setInt(5, couplingDTO.getCouplingSourceType().ordinal());
      stmt.execute();

    });
  }

  private static final String DELETE_COUPLED_RECORD_BY_LOCALE_IID_AND_PROPERTY_IID = "delete from pxp.coupledrecord where entityiid = ? "
      + "and masterentityiid = ?  and propertyiid = ? and localeiid = ? and couplingtype = ?";

  @Override
  public void deleteCoupledRecordByLocaleIIDAndPropertyIID(ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IBaseEntityDTO entityByIID = catalogDao.getEntityByIID(couplingDTO.getTargetEntityIID());
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(), entityByIID);

      PreparedStatement stmt = currentConn.prepareStatement(DELETE_COUPLED_RECORD_BY_LOCALE_IID_AND_PROPERTY_IID);
      stmt.setLong(1, couplingDTO.getTargetEntityIID());
      stmt.setLong(2, couplingDTO.getSourceEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.setInt(5, couplingDTO.getCouplingSourceType().ordinal());
      stmt.execute();
      IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
      IBaseEntityDTO baseEntityDTO = catalogDao.openBaseEntity(entityByIID).loadPropertyRecords(propertyByIID);
      eventDAS.registerChange(ITimelineDTO.ChangeCategory.RemovedCouplingSource, baseEntityDTO.getPropertyRecord(couplingDTO.getPropertyIID()));
      eventDAS.postRegisteredChanges();
    });
  }

  private static final String GET_COUPLED_RECORD_FOR_LOCALE_IID = "select * from pxp.coupledrecord where entityiid = ? "
      + "and masterentityiid = ? and propertyiid = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getCoupledRecordForLocaleIID(ICouplingDTO couplingDTO) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_COUPLED_RECORD_FOR_LOCALE_IID);
      stmt.setLong(1, couplingDTO.getTargetEntityIID());
      stmt.setLong(2, couplingDTO.getSourceEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {

        ICouplingDTO newCouplingDTO = new CouplingDTO();
        newCouplingDTO.setTargetEntityIID(resultSet.getLong("entityiid"));
        newCouplingDTO.setPropertyIID(resultSet.getLong("propertyiid"));
        newCouplingDTO.setSourceEntityIID(resultSet.getLong("masterentityiid"));
        newCouplingDTO.setLocaleIID(resultSet.getLong("localeiid"));
        newCouplingDTO.setCouplingSourceType(CouplingType.getTypeByPrecedence(resultSet.getInt("couplingtype")));
        newCouplingDTO.setRecordStatus(RecordStatus.valueOf(resultSet.getInt("recordstatus")));
        couplingDTOs.add(newCouplingDTO);
      }
    });
    return couplingDTOs;
  }

  private static final String UPDATE_CONFLICTING_VALUS_BY_LOCALE = "update pxp.conflictingvalues set recordstatus = ? where "
      + "targetentityiid = ? and propertyiid = ? and localeiid = ? and recordstatus = ?";

  @Override
  public void updateConflictingValuesByLocaleIID(ICouplingDTO couplingDTO, RecordStatus recordStatus) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(UPDATE_CONFLICTING_VALUS_BY_LOCALE);
      stmt.setInt(1, recordStatus.ordinal());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getPropertyIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.setInt(5, couplingDTO.getRecordStatus().ordinal());
      stmt.execute();

    });
  }

  private static final String DELETE_COUPLED_RECORD_BY_LOCALE_IID = "delete from pxp.coupledrecord where entityiid = ? "
      + "and masterentityiid = ?  and localeiid = ? and couplingtype = ?";

  @Override
  public void deleteCoupledRecordByLocaleIID(ICouplingDTO couplingDTO, ILocaleCatalogDAO catalogDao) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IBaseEntityDTO entityByIID = catalogDao.getEntityByIID(couplingDTO.getTargetEntityIID());
      EntityEventDAS eventDAS = new EntityEventDAS( currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(), entityByIID);

      PreparedStatement stmt = currentConn.prepareStatement(DELETE_COUPLED_RECORD_BY_LOCALE_IID);
      stmt.setLong(1, couplingDTO.getTargetEntityIID());
      stmt.setLong(2, couplingDTO.getSourceEntityIID());
      stmt.setLong(3, couplingDTO.getLocaleIID());
      stmt.setInt(4, couplingDTO.getCouplingSourceType().ordinal());
      stmt.execute();

    });
  }

  private static final String GET_ALL_CONFLICTING_VALUES = "select * from pxp.conflictingvalues where "
      + "sourceentityiid = ?  OR targetentityiid = ?";

  @Override
  public List<ICouplingDTO> getConflictingValues(Long entityIID) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_ALL_CONFLICTING_VALUES);
      stmt.setLong(1, entityIID);
      stmt.setLong(2, entityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_CONFLICTING_VALUES_BY_LOCALE_IID = "select * from pxp.conflictingvalues where "
      + "sourceentityiid = ?  and propertyiid = ? and couplingsourcetype = ? and couplingsourceiid = ?";

  @Override
  public List<ICouplingDTO> getConflictingValuesByLocaleIID(ICouplingDTO couplingDTO) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_CONFLICTING_VALUES_BY_LOCALE_IID);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getPropertyIID());
      stmt.setInt(3, couplingDTO.getCouplingSourceType().ordinal());
      stmt.setLong(4, couplingDTO.getCouplingSourceIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String GET_SIDE2ENTITY_BY_SIDE1 = "select * from pxp.relation where side1entityiid = ?";
  private static final String GET_SIDE1ENTITY_BY_SIDE2 = "select * from pxp.relation where side2entityiid = ?";

  @Override
  public Map<String, IBGPCouplingDTO> getOtherSideEntitesFromRelationship(Long sourceEntityIID) throws RDBMSException
  {
    Map<String, IBGPCouplingDTO> bgpCouplingDTOs = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_SIDE2ENTITY_BY_SIDE1);
      stmt.setLong(1, sourceEntityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {

        long propertyIID = resultSet.getLong("propertyiid");
        String propertyCode = ConfigurationDAO.instance().getPropertyByIID(propertyIID).getPropertyCode();

        if(bgpCouplingDTOs.containsKey(propertyCode)) {
          bgpCouplingDTOs.get(propertyCode).getAddedEntityIIDs().add(resultSet.getLong("side2entityiid"));
        }else {
          IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
          bgpCouplingDTO.getAddedEntityIIDs().add(resultSet.getLong("side2entityiid"));
          bgpCouplingDTO.setRelationshipId(propertyCode);
          bgpCouplingDTO.getSourceBaseEntityIIDs().add(sourceEntityIID);
          bgpCouplingDTO.setSideId(null);
          bgpCouplingDTOs.put(propertyCode, bgpCouplingDTO);
        }
      }
    });

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_SIDE1ENTITY_BY_SIDE2);
      stmt.setLong(1, sourceEntityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {

        long propertyIID = resultSet.getLong("propertyiid");
        String propertyCode = ConfigurationDAO.instance().getPropertyByIID(propertyIID).getPropertyCode();

        if(bgpCouplingDTOs.containsKey(propertyCode)) {
          bgpCouplingDTOs.get(propertyCode).getAddedEntityIIDs().add(resultSet.getLong("side1entityiid"));
        }else {
          IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
          bgpCouplingDTO.getAddedEntityIIDs().add(resultSet.getLong("side1entityiid"));
          bgpCouplingDTO.setRelationshipId(propertyCode);
          bgpCouplingDTO.getSourceBaseEntityIIDs().add(sourceEntityIID);
          bgpCouplingDTO.setSideId(null);
          bgpCouplingDTOs.put(propertyCode, bgpCouplingDTO);
        }
      }
    });
    return bgpCouplingDTOs;
  }

  private static final String CHECK_WHEATHER_CONFLICTING_VALUES = "select * from pxp.conflictingvalues where "
      + "sourceentityiid = ? and targetentityiid = ? and couplingsourceiid = ? and localeiid = ? and propertyiid = ?";

  @Override
  public List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist(ICouplingDTO couplingDTO) throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(CHECK_WHEATHER_CONFLICTING_VALUES);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getCouplingSourceIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.setLong(5, couplingDTO.getPropertyIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String CHECK_WHEATHER_CONFLICTING_VALUE_EXIST = "select * from pxp.conflictingvalues where "
      + "sourceentityiid = ? and targetentityiid = ? and couplingsourceiid = ? and localeiid = ?";


  @Override
  public Boolean checkWheatherConflictingValuesAlreadyExistForCoupingSource(ICouplingDTO couplingDTO) throws RDBMSException
  {
    Boolean[] flag =  new Boolean[1];
    flag[0]= false;
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(CHECK_WHEATHER_CONFLICTING_VALUE_EXIST);
      stmt.setLong(1, couplingDTO.getSourceEntityIID());
      stmt.setLong(2, couplingDTO.getTargetEntityIID());
      stmt.setLong(3, couplingDTO.getCouplingSourceIID());
      stmt.setLong(4, couplingDTO.getLocaleIID());
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        flag[0] = true;
        break;
      }
    });
    return flag[0];
  }

  private static final String GET_CONTEXTUAL_INSTANCE = "select parentiid,baseentityiid,classifieriid  from pxp.baseentity where parentiid = ? and ismerged != true";

  private static final String GET_PARENT_OF_CONTEXTUAL = "select parentiid,baseentityiid,classifieriid from pxp.baseentity where baseentityiid = ? and ismerged != true";

  @Override
  public List<ICouplingDTO> getOtherSideEntitesForContextual(Long entityIID) throws RDBMSException
  {

    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_CONTEXTUAL_INSTANCE);
      stmt.setLong(1, entityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        ICouplingDTO couplingDTO = new CouplingDTO();
        couplingDTO.setSourceEntityIID(resultSet.getLong("parentiid"));
        couplingDTO.setTargetEntityIID(resultSet.getLong("baseentityiid"));
        couplingDTO.setCouplingSourceIID(resultSet.getLong("classifieriid"));
        couplingDTOs.add(couplingDTO);
      }
    });

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_PARENT_OF_CONTEXTUAL);
      stmt.setLong(1, entityIID);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        Long sourceEntityiid = resultSet.getLong("parentiid");
        if(sourceEntityiid != null && sourceEntityiid != 0) {

          ICouplingDTO couplingDTO = new CouplingDTO();
          couplingDTO.setSourceEntityIID(sourceEntityiid);
          couplingDTO.setTargetEntityIID(entityIID);
          couplingDTO.setCouplingSourceIID(resultSet.getLong("classifieriid"));
          couplingDTOs.add(couplingDTO);
        }
      }
    });
    return couplingDTOs;
  }

  private static final String GET_TARGET_ENTITIES = "select * from pxp.conflictingvalues where sourceentityiid = ? "
      + "and couplingsourcetype <> ? and recordstatus = ? and localeiid = ?";

  @Override
  public List<ICouplingDTO> getTargetConflictingValues(Long sourceEntityIID, CouplingType couplingType,
      RecordStatus recordStatus, Long localeiid)
      throws RDBMSException
  {
    List<ICouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_TARGET_ENTITIES);
      stmt.setLong(1, sourceEntityIID);
      stmt.setLong(2, couplingType.ordinal());
      stmt.setLong(3, recordStatus.ordinal());
      stmt.setLong(4, localeiid);
      stmt.execute();

      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        couplingDTOs.add(new CouplingDTO(resultSet));
      }
    });
    return couplingDTOs;
  }

  private static final String DELETE_CONFLICTING_RECORDS_FOR_TRANSLATION = "delete from pxp.conflictingvalues where "
      + "(sourceentityiid = ?  OR targetentityiid = ?) and localeiid = ?";

  private static final String DELETE_COUPLED_RECORDS_FOR_TRANSLATION = "delete from pxp.coupledrecord where (entityiid = ? "
      + "OR masterentityiid = ? )  and localeiid = ?";

  @Override
  public void deleteCouplingRecordsForTranslationDelete(Long sourceEntityiid, Long localeiid) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_CONFLICTING_RECORDS_FOR_TRANSLATION);
      stmt.setLong(1, sourceEntityiid);
      stmt.setLong(2, sourceEntityiid);
      stmt.setLong(3, localeiid);
      stmt.execute();

    });

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_COUPLED_RECORDS_FOR_TRANSLATION);
      stmt.setLong(1, sourceEntityiid);
      stmt.setLong(2, sourceEntityiid);
      stmt.setLong(3, localeiid);
      stmt.execute();

    });
  }

  public static final String DELETE_VALUE_RECORD_BY_LOCALE = "delete from pxp.ValueRecord where propertyIID = ? "
      + "and entityIID = ? and localeid = ?";

  public static final String DELETE_VALUE_RECORD = "delete from pxp.ValueRecord where propertyIID = ? "
      + "and entityIID = ? and localeid is null";

  public static final String DELETE_TAGS_RECORD = "delete from pxp.tagsrecord where propertyiid = ? and entityiid = ?";

  @Override
  public void deletePropertyRecord(IPropertyRecordDTO propertyRecord) throws RDBMSException
  {

    if(propertyRecord instanceof IValueRecordDTO) {

      IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
      if(valueRecordDTO.getLocaleID().equals("")) {
        RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(DELETE_VALUE_RECORD);
          stmt.setLong(1, valueRecordDTO.getProperty().getPropertyIID());
          stmt.setLong(2, valueRecordDTO.getEntityIID());
          stmt.execute();
        });
      }else {

        RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(DELETE_VALUE_RECORD_BY_LOCALE);
          stmt.setLong(1, valueRecordDTO.getProperty().getPropertyIID());
          stmt.setLong(2, valueRecordDTO.getEntityIID());
          stmt.setString(3, valueRecordDTO.getLocaleID());
          stmt.execute();
        });

      }
    }else {

      ITagsRecordDTO tagRecordDTO = (ITagsRecordDTO) propertyRecord;

      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement stmt = currentConn.prepareStatement(DELETE_TAGS_RECORD);
        stmt.setLong(1, tagRecordDTO.getProperty().getPropertyIID());
        stmt.setLong(2, tagRecordDTO.getEntityIID());
        stmt.execute();
      });
    }
  }

  private static final String Q_FOR_ENTITY_IID      = "select baseentityiid from pxp.baseentity where classifieriid = ? and ismerged != true";
  private static final String Q_FOR_ENTITY_IID_LINK = "select becl.baseentityiid from pxp.baseentityclassifierlink becl join pxp.baseentity be on becl.baseentityiid = be.baseentityiid and be.ismerged != true where becl.otherclassifieriid = ? ";

  @Override
  public Set<Long> getAllEntityIIDsForCLassifierIID(Long classifierIID, ClassifierType classifierType) throws RDBMSException
  {
    Set<Long> baseEntityIIDs = new HashSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = "";

      if (classifierType.equals(ClassifierType.CLASS)) {
        query = Q_FOR_ENTITY_IID;
      }
      else {
        query = Q_FOR_ENTITY_IID_LINK;
      }
      PreparedStatement stmt = currentConn.prepareStatement(query);
      stmt.setLong(1, classifierIID);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        baseEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });

    return baseEntityIIDs;
  } 

  @Override
  public void raiseEventsForCoupling(IPropertyRecordDTO record, ICouplingDTO couplingDTO, ILocaleCatalogDAO 
      catalogDao, IBaseEntityDTO baseEntity) throws RDBMSException
  {
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      EntityEventDAS eventDAS = new EntityEventDAS(currentConn, catalogDao.getUserSessionDTO(), catalogDao.getLocaleCatalogDTO(),
          baseEntity);
      
      CouplingDAS entityDAS = new CouplingDAS(currentConn);
      
      IRecordDAS recordService = RecordDASFactory.instance()
          .newService(currentConn, (LocaleCatalogDAO) catalogDao, record, record.getEntityIID());
      
      eventDAS.registerCreatedCouplingRecord(recordService);
      eventDAS.postRegisteredChanges();
    });
    
  }
  
  private static final String Q_FOR_CHILD_ENTITIES = "select be.baseentityiid, be.parentiid, cc.classifiercode from "
      + "pxp.baseentity be join pxp.classifierconfig cc ON cc.classifieriid = be.classifieriid where topparentiid = ?";
  
  @Override
  public List<IContextualDataTransferGranularDTO> triggerContextualDataTransferFromCloning(Long topParentIID) throws RDBMSException
  {
    
    List<IContextualDataTransferGranularDTO> contextualDataTranserDTO = new ArrayList<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(Q_FOR_CHILD_ENTITIES);
      stmt.setLong(1, topParentIID);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        IContextualDataTransferGranularDTO bgpCouplingDTO = new ContextualDataTransferGranularDTO();
        bgpCouplingDTO.setParentBaseEntityIID(result.getLong("parentiid"));
        bgpCouplingDTO.setVariantBaseEntityIID(result.getLong("baseentityiid"));
        bgpCouplingDTO.setContextID(result.getString("classifiercode"));
        contextualDataTranserDTO.add(bgpCouplingDTO);
      }
    });

    return contextualDataTranserDTO;
  }

}
