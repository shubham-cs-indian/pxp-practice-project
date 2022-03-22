package com.cs.core.rdbms.services.records;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Singleton method that creates the correct record service according to its in
 *
 * @author vallee
 */
public class RecordDASFactory {
  
  // Singleton implementation
  private static final RecordDASFactory INSTANCE = new RecordDASFactory();
  
  private RecordDASFactory()
  {
  }
  
  ;
  
  public static RecordDASFactory instance()
  {
    return INSTANCE;
  }
  
  /**
   * Load records attached to a base entity
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param catalog
   *          the catalog in which to load the records
   * @param baseEntityIID
   *          the concerned base entity IID
   * @param properties
   *          the related set of properties
   * @return a set of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Set<IPropertyRecordDTO> loadRecords(
      RDBMSConnection connection, LocaleCatalogDAO catalog, long baseEntityIID,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {
    
    // Load value records
    Set<IPropertyRecordDTO> records = ValueRecordDAS.loadRecords( connection, catalog, baseEntityIID, properties);
    // Load tags records
    Set<IPropertyRecordDTO> tagRecords = TagsRecordDAS.loadRecords( connection, catalog, baseEntityIID, properties);
    records.addAll(tagRecords);
    // Load relations set records
    Set<IPropertyRecordDTO> relationsSets = RelationsSetDAS.loadRecords( connection, catalog, baseEntityIID, properties);
    records.addAll(relationsSets);
    return records;
  }
  
  
  /**
   * Load records attached to a base entity
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param catalog
   *          the catalog in which to load the records
   * @param baseEntityIIDs
   *          the concerned base entity IIDs
   * @param properties
   *          the related set of properties
   * @return a set of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Map<Long,Set<IPropertyRecordDTO>> loadRecordsForEntities(
      RDBMSConnection connection, LocaleCatalogDAO catalog, Set<Long> baseEntityIIDs,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {
    
    Map<Long,Set<IPropertyRecordDTO>> recordsMap = ValueRecordDAS.loadRecordsForEntities( connection, catalog, baseEntityIIDs, properties);
    
    Map<Long,Set<IPropertyRecordDTO>> allRecordMaps = TagsRecordDAS.loadRecordsForEntities( connection, catalog, baseEntityIIDs, properties);
    
    recordsMap.forEach(
        (key, value) -> allRecordMaps.merge( key, value, (v1, v2) -> new HashSet<>(SetUtils.union(v1, v2)))
    );
    return allRecordMaps;
  }
  
  /**
   * Load the records that are dependent on a source of coupling
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param sourceRecord
   *          the record that may be source of coupling
   * @return a set of dependent property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public static Set<IPropertyRecordDTO> loadCouplingDependentRecords(RDBMSAbstractDriver driver,
      RDBMSConnection connection, IPropertyRecordDTO sourceRecord)
      throws SQLException, RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  /**
   * Load the records that are dependent by calculation rule
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param operandRecord
   *          the record that may be operand in calculation rules
   * @return a set of dependent property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public static Set<IPropertyRecordDTO> loadCalculatedDependentRecords(RDBMSAbstractDriver driver,
      RDBMSConnection connection, IPropertyRecordDTO operandRecord)
      throws SQLException, RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  /**
   * Build a new service according to the specificity of the record
   *
   * @param driver
   *          the current RDBMS driver
   * @param connection
   *          the current connection
   * @param record
   *          the concerned record
   * @param baseEntityIID
   *          IID of the owner entity
   * @return the applicable set of services
   */
  public IRecordDAS newService( RDBMSConnection connection,
      LocaleCatalogDAO catalog, IPropertyRecordDTO record, long baseEntityIID)
  {
    switch (record.getProperty()
        .getSuperType()) {
      case ATTRIBUTE:
        return new ValueRecordDAS( connection, catalog, record, baseEntityIID);
      case TAGS:
        return new TagsRecordDAS( connection, catalog, record, baseEntityIID);
      case RELATION_SIDE:
        return new RelationsSetDAS( connection, catalog, record, baseEntityIID);
    }
    return null; // should never happen
  }
}
