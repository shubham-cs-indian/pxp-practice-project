package com.cs.core.rdbms.coupling.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.services.records.IRecordDAS;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CouplingDAS extends RDBMSDataAccessService{
  
  
  private static final String INSERT_COUPLED_RECORD = "INSERT INTO pxp.coupledrecord( propertyiid, entityiid, recordstatus, "
      + "couplingbehavior, couplingtype, masternodeid, coupling,masterentityiid, masterpropertyiid, localeiid) "
      + "VALUES (?,?,?,?,?,?,?,?,?,?)";
  
  private static final String INSERT_CONFLICTING_VALUE_RECORD = "INSERT INTO pxp.conflictingvalues( targetentityiid, propertyiid,"
      + " sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid) "
      + "VALUES (?,?,?,?,?,?,?,?)";
  
     
  public CouplingDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  

  public void createCoupledRecord(ICouplingDTO couplingDTO) throws SQLException, RDBMSException
  {
    
    PreparedStatement prepareStatement = currentConnection.prepareStatement(INSERT_COUPLED_RECORD);
    prepareStatement.setLong(1, couplingDTO.getPropertyIID());
    prepareStatement.setLong(2, couplingDTO.getTargetEntityIID());
    prepareStatement.setInt(3, RecordStatus.COUPLED.ordinal());
    prepareStatement.setInt(4, couplingDTO.getCouplingType().ordinal());
    prepareStatement.setInt(5, couplingDTO.getCouplingSourceType().ordinal());
    prepareStatement.setString(6, String.format("%d:%d", couplingDTO.getSourceEntityIID(), couplingDTO.getPropertyIID()));
    prepareStatement.setString(7, "");
    prepareStatement.setLong(8, couplingDTO.getSourceEntityIID());
    prepareStatement.setLong(9, couplingDTO.getPropertyIID());
    prepareStatement.setLong(10, couplingDTO.getLocaleIID());
    prepareStatement.executeUpdate();
    
  }
  
  public void createConflictingValueRecord(ICouplingDTO couplingDTO) throws SQLException, RDBMSException
  {
    
    PreparedStatement prepareStatement = currentConnection.prepareStatement(INSERT_CONFLICTING_VALUE_RECORD);
    prepareStatement.setLong(1, couplingDTO.getTargetEntityIID());
    prepareStatement.setLong(2, couplingDTO.getPropertyIID());
    prepareStatement.setLong(3, couplingDTO.getSourceEntityIID());
    prepareStatement.setInt(4, couplingDTO.getCouplingType().ordinal());
    prepareStatement.setLong(5, couplingDTO.getCouplingSourceIID());
    prepareStatement.setInt(6, couplingDTO.getRecordStatus().ordinal());
    prepareStatement.setInt(7, couplingDTO.getCouplingSourceType().ordinal());
    prepareStatement.setLong(8, couplingDTO.getLocaleIID());
    prepareStatement.executeUpdate();
    
  }
  
  public void resolveConflict(ICouplingDTO couplingDTO) throws SQLException, RDBMSException {
    
     driver.getProcedure(currentConnection, "pxp.sp_resolvedconflicts" )
        .setInput(ParameterType.IID, couplingDTO.getSourceEntityIID())
        .setInput(ParameterType.IID, couplingDTO.getTargetEntityIID())
        .setInput(ParameterType.INT, couplingDTO.getCouplingType().ordinal())
        .setInput(ParameterType.INT, couplingDTO.getCouplingSourceType().ordinal())
        .setInput(ParameterType.IID, couplingDTO.getCouplingSourceIID())
        .setInput(ParameterType.IID, couplingDTO.getPropertyIID())
        .setInput(ParameterType.STRING, String.format("%d:%d", couplingDTO.getSourceEntityIID(), couplingDTO.getPropertyIID()))
        .setInput(ParameterType.IID, couplingDTO.getLocaleIID())
        .execute();
        
  }
  
  public void updateSourceCoupledRecord(Long targetEntityIID, Long sourceEntityIID, Long propertyIID, Long couplingSourceIID, Long localeIID) throws SQLException, RDBMSException {
    
    driver.getProcedure(currentConnection, "pxp.sp_updateSourceCoupledRecord" )
    .setInput(ParameterType.IID, targetEntityIID)   
    .setInput(ParameterType.IID, sourceEntityIID)
    .setInput(ParameterType.IID, propertyIID)
    .setInput(ParameterType.IID, couplingSourceIID)
    .setInput(ParameterType.IID, localeIID)
    .execute();
       
 }
  public void updateSourceCoupledRecordForDynamicCoupling(Long targetEntityIID, Long sourceEntityIID, Long propertyIID, 
      Long couplingSourceIID, Long localeIID) throws SQLException, RDBMSException {
    
    driver.getProcedure(currentConnection, "pxp.sp_updateSourceCoupledRecordForDynamicCoupling" )
       .setInput(ParameterType.IID, targetEntityIID)   
       .setInput(ParameterType.IID, sourceEntityIID)
       .setInput(ParameterType.IID, propertyIID)
       .setInput(ParameterType.IID, couplingSourceIID)
       .setInput(ParameterType.IID, localeIID)
       .execute();
       
 }
  
  public void insertValueRecordAtTarget(Long sourceEntityIID, Long propertyIID) throws RDBMSException, SQLException {
    
    driver.getProcedure(currentConnection, "pxp.sp_CreateSeparatedValueTargets")
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, propertyIID)
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, sourceEntityIID)
    .execute();
    
  }
  
  public Boolean createCoupledRecord( IRecordDAS recordService, IPropertyRecordDTO record, ICouplingDTO couplinDTO) throws RDBMSException, SQLException, CSFormatException {
    
    ICSECoupling couplingDef = record.getCouplingExpressions().get(0);
    Boolean isCoupledCreated = false;
    switch (record.getCouplingType()) {

      case DYN_RELATIONSHIP:
        /*master = nodeResolver.getRelationCouplingMaster(couplingDef.getSource()
            .toRelation(), baseEntityIID, record.getProperty());*/
        
        IResultSetParser result = driver.getFunction(currentConnection, ResultType.INT,
            "pxp.fn_createConflictingValuesForDynamicCoupling")
        .setInput(ParameterType.LONG, couplinDTO.getTargetEntityIID())
        .setInput(ParameterType.LONG, record.getProperty().getIID())
        .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
        .setInput(ParameterType.INT, record.getCouplingType().ordinal())
        .setInput(ParameterType.STRING, String.format("%d:%d", couplinDTO.getSourceEntityIID(), couplinDTO.getPropertyIID()))
        .setInput(ParameterType.STRING, "")
        .setInput(ParameterType.IID, couplinDTO.getSourceEntityIID())
        .setInput(ParameterType.IID, record.getProperty().getIID())
        .setInput(ParameterType.IID, couplingDef.getSource().toRelation().getIID())
        .setInput(ParameterType.IID, couplinDTO.getLocaleIID())
        .execute();
        
        if(result.getInt() == 1) {
          isCoupledCreated = true;
        }
        break;
      case TIGHT_RELATIONSHIP: 
          
        driver.getProcedure(currentConnection, "pxp.sp_createconflictingvaluesfortightlycoupling")
        .setInput(ParameterType.LONG, couplinDTO.getTargetEntityIID())
        .setInput(ParameterType.LONG, record.getProperty().getIID())
        .setInput(ParameterType.INT, couplinDTO.getRecordStatus() != null && !couplinDTO.getRecordStatus().equals(RecordStatus.UNDEFINED) ? couplinDTO.getRecordStatus().ordinal() : RecordStatus.NOTIFIED.ordinal())
        .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
        .setInput(ParameterType.INT, record.getCouplingType().ordinal())
        .setInput(ParameterType.STRING, String.format("%d:%d", couplinDTO.getSourceEntityIID(), couplinDTO.getPropertyIID()))
        .setInput(ParameterType.STRING, "")
        .setInput(ParameterType.IID, couplinDTO.getSourceEntityIID())
        .setInput(ParameterType.IID, record.getProperty().getIID())
        .setInput(ParameterType.IID, couplingDef.getSource().toRelation().getIID())
        .setInput(ParameterType.IID, couplinDTO.getLocaleIID())
        .execute();
        
        break;
      
      default:
        throw new RDBMSException(0, "Program ERROR", "Try to resolve a non-coupling case"); // should
                                                                                            // never
    }
    return isCoupledCreated;
  }
  
  public Boolean createCoupledRecordForContextual(ICouplingDTO couplinDTO)
      throws RDBMSException, SQLException, CSFormatException
  {
    Boolean isCoupledCreated = false;
    switch (couplinDTO.getCouplingType()) {
      
      case DYNAMIC:
        
        IResultSetParser result = driver.getFunction(currentConnection, ResultType.INT, "pxp.fn_createConflictingValuesForDynamicCoupling")
            .setInput(ParameterType.LONG, couplinDTO.getTargetEntityIID())
            .setInput(ParameterType.LONG, couplinDTO.getPropertyIID())
            .setInput(ParameterType.INT, CouplingBehavior.DYNAMIC.ordinal())
            .setInput(ParameterType.INT, CouplingType.DYN_CONTEXTUAL.ordinal())
            .setInput(ParameterType.STRING, String.format("%d:%d", couplinDTO.getSourceEntityIID(), couplinDTO.getPropertyIID()))
            .setInput(ParameterType.STRING, "")
            .setInput(ParameterType.IID, couplinDTO.getSourceEntityIID())
            .setInput(ParameterType.IID, couplinDTO.getPropertyIID())
            .setInput(ParameterType.IID, couplinDTO.getCouplingSourceIID())
            .setInput(ParameterType.IID, couplinDTO.getLocaleIID())
            .execute();
        
        if(result.getInt() == 1) {
          isCoupledCreated =  true;
        }
        break;
      case TIGHTLY:
        
        driver.getProcedure(currentConnection, "pxp.sp_createconflictingvaluesfortightlycoupling")
            .setInput(ParameterType.LONG, couplinDTO.getTargetEntityIID())
            .setInput(ParameterType.LONG, couplinDTO.getPropertyIID())
            .setInput(ParameterType.INT, couplinDTO.getRecordStatus() != null && !couplinDTO.getRecordStatus().equals(RecordStatus.UNDEFINED) ? couplinDTO.getRecordStatus().ordinal() : RecordStatus.NOTIFIED.ordinal())
            .setInput(ParameterType.INT, CouplingBehavior.TIGHTLY.ordinal())
            .setInput(ParameterType.INT, couplinDTO.getCouplingSourceType().ordinal())
            .setInput(ParameterType.STRING, String.format("%d:%d", couplinDTO.getSourceEntityIID(), couplinDTO.getPropertyIID()))
            .setInput(ParameterType.STRING, "")
            .setInput(ParameterType.IID, couplinDTO.getSourceEntityIID())
            .setInput(ParameterType.IID, couplinDTO.getPropertyIID())
            .setInput(ParameterType.IID, couplinDTO.getCouplingSourceIID())
            .setInput(ParameterType.IID, couplinDTO.getLocaleIID())
            .execute();
        
        break;
        
      default:
        throw new RDBMSException(0, "Program ERROR", "Try to resolve a non-coupling case");                                                                                             // never
    }
    return isCoupledCreated;
  }
  
  public Boolean createCoupledRecordForClassification(IPropertyRecordDTO record, ICouplingDTO couplingDTO) throws RDBMSException, SQLException, CSFormatException
  {
    Boolean isCoupledCreated = false;
    switch (record.getCouplingType()) {

      case DYN_CLASSIFICATION:
        
       IResultSetParser result = driver.getFunction(currentConnection, ResultType.INT,
            "pxp.fn_createConflictingValuesForDynamicCoupling")
      .setInput(ParameterType.LONG, couplingDTO.getTargetEntityIID())
      .setInput(ParameterType.LONG, record.getProperty().getIID())
      .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
      .setInput(ParameterType.INT, record.getCouplingType().ordinal())
      .setInput(ParameterType.STRING, String.format("%d:%d", couplingDTO.getSourceEntityIID(), record.getProperty().getPropertyIID()))
      .setInput(ParameterType.STRING, "")
      .setInput(ParameterType.IID, couplingDTO.getSourceEntityIID())
      .setInput(ParameterType.IID, record.getProperty().getPropertyIID())
      .setInput(ParameterType.IID, couplingDTO.getCouplingSourceIID())
      .setInput(ParameterType.IID, couplingDTO.getLocaleIID())
      .execute();
       
       if(result.getInt() ==  1) {
         isCoupledCreated = true;
       }
      break;
      
    case TIGHT_CLASSIFICATION: 
        
      driver.getProcedure(currentConnection, "pxp.sp_createconflictingvaluesfortightlycoupling")
      .setInput(ParameterType.LONG, couplingDTO.getTargetEntityIID())
      .setInput(ParameterType.LONG, record.getProperty().getIID())
      .setInput(ParameterType.INT, couplingDTO.getRecordStatus() != null && !couplingDTO.getRecordStatus().equals(RecordStatus.UNDEFINED) ? couplingDTO.getRecordStatus().ordinal() : RecordStatus.NOTIFIED.ordinal())
      .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
      .setInput(ParameterType.INT, record.getCouplingType().ordinal())
      .setInput(ParameterType.STRING, String.format("%d:%d", couplingDTO.getSourceEntityIID(), record.getProperty().getPropertyIID()))
      .setInput(ParameterType.STRING, "")
      .setInput(ParameterType.IID, couplingDTO.getSourceEntityIID())
      .setInput(ParameterType.IID, record.getProperty().getIID())
      .setInput(ParameterType.IID, couplingDTO.getCouplingSourceIID())
      .setInput(ParameterType.IID, couplingDTO.getLocaleIID())
      .execute();
      
      break;
      
      default:
        throw new RDBMSException(0, "Program ERROR", "Try to resolve a non-coupling case"); // should
                                                                                            // never
    }
    
    return isCoupledCreated;
  }
  
}
