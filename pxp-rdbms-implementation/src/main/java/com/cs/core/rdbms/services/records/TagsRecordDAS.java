package com.cs.core.rdbms.services.records;

import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.NodeResolver;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.SQLException;
import java.util.*;

/**
 * @author vallee
 */
public class TagsRecordDAS extends AbstractRecordDAS implements IRecordDAS {
  
  private final TagsRecordDTO tagsRecord = (TagsRecordDTO) record;
  
  /**
   * Tags record service constructor
   *
   * @param driver
   * @param connection
   * @param catalog
   * @param record
   * @param baseentityIID
   *          to which the property belongs
   */
  TagsRecordDAS(RDBMSConnection connection, LocaleCatalogDAO catalog,
      IPropertyRecordDTO record, long baseentityIID)
  {
    super( connection, catalog, record, baseentityIID);
  }
  
  /**
   * Load tags records attached to a base entity
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param catalog
   *          the current catalog DAO
   * @param entityIID
   *          the concerned base entity / classifier IID
   * @param properties
   *          the related setContentFrom of properties
   * @return a setContentFrom of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Set<IPropertyRecordDTO> loadRecords(
      RDBMSConnection connection, LocaleCatalogDAO catalog, long entityIID,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {
    Set<Long> tagRecordPropertyIIDs = new HashSet<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyType()
          .getSuperType() == IPropertyDTO.SuperType.TAGS) {
        tagRecordPropertyIIDs.add(((PropertyDTO) property).getIID());
      }
    }
    Set<IPropertyRecordDTO> records = new HashSet<>();
    if (tagRecordPropertyIIDs.isEmpty()) {
      return records;
    }
    IResultSetParser tRecordResult = connection.getFunction( 
            RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_readTagsRecord")
        .setInput(ParameterType.IID, entityIID)
        .setInput(ParameterType.IID_ARRAY, tagRecordPropertyIIDs)
        .execute();
    while (tRecordResult.next()) {
      Long propertyIID = tRecordResult.getLong("propertyIID");
      IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
      records.add(new TagsRecordDTO(tRecordResult, property));
    }
    return records;
  }
  
  /**
   * Load tags records attached to a base entity
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param catalog
   *          the current catalog DAO
   * @param entityIId
   *          the concerned base entity iids
   * @param properties
   *          the related setContentFrom of properties
   * @return a map of loaded property records against entityIId
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Map<Long,Set<IPropertyRecordDTO>> loadRecordsForEntities(
      RDBMSConnection connection, LocaleCatalogDAO catalog, Set<Long> entityIIds,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {
    Set<Long> tagRecordPropertyIIDs = new HashSet<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyType()
          .getSuperType() == IPropertyDTO.SuperType.TAGS) {
        tagRecordPropertyIIDs.add(((PropertyDTO) property).getIID());
      }
    }
    Map<Long,Set<IPropertyRecordDTO>> recordsMap = new HashMap<>();
    if (tagRecordPropertyIIDs.isEmpty()) {
      return recordsMap;
    }
    IResultSetParser tRecordResult = connection.getFunction( 
            RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_getselectivetagrecords")
        .setInput(ParameterType.IID_ARRAY, entityIIds)
        .setInput(ParameterType.IID_ARRAY, tagRecordPropertyIIDs)
        .execute();
    while (tRecordResult.next()) {
      Long entityIID = tRecordResult.getLong("entityIID");
      Set<IPropertyRecordDTO> records; 
      if(recordsMap.containsKey(entityIID)) {
        records = recordsMap.get(entityIID);
      }
      else {
        records = new HashSet<>();
        recordsMap.put(entityIID, records);
      }
      Long propertyIID = tRecordResult.getLong("propertyIID");
      IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
      records.add(new TagsRecordDTO(tRecordResult, property));
    }
    return recordsMap;
  }
  
  @Override
  public void createSimpleRecord() throws SQLException, RDBMSException
  {
    if (record.isCalculated() || record.isCoupled()) {
      return;
    }
    TagsRecordDTO tagRecord = (TagsRecordDTO) record;
    driver.getProcedure(currentConnection, "pxp.sp_createTagsRecord")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, tagRecord.getProperty()
            .getIID())
        .setInput(ParameterType.INT, tagRecord.getRecordStatus()
            .ordinal())
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, tagRecord.getHStoreFormat())
        .execute();
  }
  
  @Override
  public void updateRecord() throws SQLException, RDBMSException
  {
    TagsRecordDTO tagRecord = (TagsRecordDTO) record;
    driver.getProcedure(currentConnection, "pxp.sp_updateTagsRecord")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, tagRecord.getProperty()
            .getIID())
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, tagRecord.getHStoreFormat())
        .execute();
  }
  
  @Override
  public void separateCoupledTargets(Long propertyIID, Long masterEntityIID) throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_CreateSeparatedValueTargets")
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, propertyIID)
    .setInput(RDBMSAbstractFunction.ParameterType.LONG, masterEntityIID)
    .setInput(ParameterType.STRING, null)
    .execute();
  }
  
  @Override
  protected void createNewDefaultRecord(NodeResolver.CouplingMaster master)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_createDefaultTagsRecord")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, tagsRecord.getProperty()
            .getPropertyIID())
        .setInput(ParameterType.INT, record.getCouplingBehavior()
            .ordinal())
        .setInput(ParameterType.INT, record.getCouplingType()
            .ordinal())
        .setInput(ParameterType.STRING, record.getCouplingExpression())
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, tagsRecord.getHStoreFormat())
        .setInput(ParameterType.IID, master.getEntityIID())
        .setInput(ParameterType.IID, master.getPropertyIID())
        .execute();
  }
  
  @Override
  public void createCalculatedRecord() throws SQLException, RDBMSException
  {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    throw new RDBMSException(0, "Program ERROR", "Cannot create calculated tags.");
  }
  
  @Override
  public void updateCalculatedRecord() throws RDBMSException, SQLException
  {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    throw new RDBMSException(0, "Program ERROR", "Cannot update calculated tags.");
  }
  
  @Override
  protected void updateAsTightlyCoupledRecord() throws SQLException, RDBMSException
  {
    if (!tagsRecord.isChanged())
      return;
    //tagsRecord.setDecouplingStatus();
    
    long masterEntiyIID = tagsRecord.getMasterEntityIID();
    /*driver.getProcedure(currentConnection, "pxp.sp_updateCoupledTagsRecord")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, tagsRecord.getProperty()
            .getIID())
        .setInput(RDBMSAbstractFunction.ParameterType.INT, tagsRecord.getRecordStatus()
            .ordinal())
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, tagsRecord.getHStoreFormat())
        .execute();*/
    
    driver
    .getProcedure(currentConnection, "pxp.sp_updateTagCoupledRecord")
    .setInput(ParameterType.LONG, baseEntityIID)
    .setInput(ParameterType.LONG, tagsRecord.getProperty().getIID())
    .setInput(ParameterType.LONG, masterEntiyIID)
    .setInput(ParameterType.STRING, tagsRecord.getHStoreFormat())
    .execute();
    
  }
  
  @Override
  protected void updateAsDecoupledRecord() throws SQLException, RDBMSException, CSFormatException
  {
    updateAsTightlyCoupledRecord();
  }
  
  @Override
  public void deleteAsSourceOfCoupling() throws RDBMSException, SQLException, CSFormatException
  {
    driver.getProcedure(currentConnection, "pxp.sp_CreateTagsFromAllTargetsAndDelete")
        .setInput(RDBMSAbstractFunction.ParameterType.STRING, record.getNodeID())
        .execute();
  }
  
  @Override
  public void deleteRecord() throws RDBMSException, SQLException, CSFormatException
  {
    driver.getProcedure(currentConnection, "pxp.sp_deleteTagsRecord")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, record.getProperty().getIID())
        .execute();
  }
  
  @Override
  public void resolveNotification() throws RDBMSException, SQLException
  {
    if (record.isNotifiedByCoupling()) {
      driver.getProcedure(currentConnection, "pxp.sp_resolveTagsRecordNotification")
          .setInput(RDBMSAbstractFunction.ParameterType.IID, baseEntityIID)
          .setInput(RDBMSAbstractFunction.ParameterType.IID, record.getProperty()
              .getIID())
          .setInput(RDBMSAbstractFunction.ParameterType.INT, record.getRecordStatus()
              .ordinal())
          .execute();
    }
  }
  
  public static Map<Long,List<ITagsRecordDTO>> getAllTagRecordsForEntities(
      RDBMSConnection connection, IConfigurationDAO configDAO ,Set<Long> entityIIds) throws SQLException, RDBMSException, CSFormatException
  {
    
    IResultSetParser result = connection
        .getFunction(ResultType.CURSOR, "fn_getalltagsrecord")
        .setInput(ParameterType.IID_ARRAY, entityIIds)
        .execute();
    
    Map<Long, List<ITagsRecordDTO>> iidVStagsRecords = new HashMap<>();
    
    while (result.next()) {
      IPropertyDTO property = configDAO.getPropertyByIID(result.getLong("propertyiid"));
      TagsRecordDTO tagsRecordDto = new TagsRecordDTO(result, property);
      Long entityIID = result.getLong("entityiid");
      List<ITagsRecordDTO> tagsRecords = iidVStagsRecords.get(entityIID);
      if(tagsRecords == null) {
        tagsRecords = new ArrayList<>();
        iidVStagsRecords.put(entityIID, tagsRecords);
      }
      tagsRecords.add(tagsRecordDto);
    }
    return iidVStagsRecords;
  }
}
