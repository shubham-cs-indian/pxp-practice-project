package com.cs.core.rdbms.process.dao;

import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.IMigrationDAO;
import com.cs.core.rdbms.services.records.IRecordDAS;
import com.cs.core.rdbms.services.records.RecordDASFactory;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.collections.SetUtils;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType.IID;

public class MigrationDAO extends LocaleCatalogDAO implements IMigrationDAO {

  public MigrationDAO(IUserSessionDTO pUserSessionDTO, ILocaleCatalogDTO pLocalCatalog)
  {
    super(pUserSessionDTO, pLocalCatalog);
  }

  public static final String GET_SOURCE_RECORD = "select *,1 as couplingtype,1 as couplingbehaviour,null as coupling,null as masterentityIID,null as masterpropertyIID from %s where entityIID = %s and propertyIID = %s ";
  @Override
  public Optional<IPropertyRecordDTO> getSourceRecord(long masterEntityIID, IPropertyDTO propertyDTO, long localeIID)
      throws RDBMSException
  {
    boolean isAttribute = propertyDTO.isAttribute();
    String tableName = isAttribute ? "pxp.valueRecord" : "pxp.tagsRecord";
    String query = String.format(GET_SOURCE_RECORD, tableName, masterEntityIID, propertyDTO.getPropertyIID());

    if(isAttribute && localeIID != 0) {
      ILanguageConfigDTO language = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID);
      query += "and localeID = " + Text.escapeStringWithQuotes(language.getLanguageCode());
    }
    String finalQuery = query;
    List<IPropertyRecordDTO> property = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        if(isAttribute){
          property.add(new ValueRecordDTO(result, propertyDTO));
        }
        else{
          property.add(new TagsRecordDTO(result, propertyDTO));
        }
      }
    });
    if(property.isEmpty()){
      return Optional.empty();
    }
    return Optional.of(property.get(0));
  }

  @Override
  public void createRecord(IPropertyRecordDTO record) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IRecordDAS recordService = RecordDASFactory.instance().newService(currentConn, this, record, record.getEntityIID());
      recordService.createSimpleRecord();
    });
  }

  @Override
  public void deleteRecord(long entityIID, IPropertyDTO propertyDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      if( propertyDTO.isAttribute()){
        currentConn.getDriver().getProcedure(currentConn, "pxp.sp_deleteValueRecord")
            .setInput(IID, entityIID)
            .setInput(IID, propertyDTO.getIID())
            .setInput(IID, null)
            .execute();
      } else{
        currentConn.getDriver().getProcedure(currentConn, "pxp.sp_deleteTagsRecord")
            .setInput(IID, entityIID)
            .setInput(IID, propertyDTO.getIID())
            .execute();
      }
    });
  }

  public static final String GET_TIGHTLY_COUPLED = "select * from pxp.coupledrecord where couplingBehavior = 4 ";
  @Override
  public List<Map<String, Object>> getAllTightlyCoupledRecord() throws RDBMSException
  {
    List<Map<String, Object>> tightlyCoupledProperties = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_TIGHTLY_COUPLED);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        Map<String, Object>  property = new HashMap<>();
        property.put("entityIID" ,result.getLong("entityIID"));
        property.put("propertyIID" ,result.getLong("propertyIID"));
        property.put("masterEntityIID" ,result.getLong("masterEntityIID"));
        property.put("localeIID" ,result.getLong("localeIID"));
        tightlyCoupledProperties.add(property);
      }
    });
    return tightlyCoupledProperties;
  }

  public static final String GET_DYNAMIC_CONFLICTS = "select * from pxp.conflictingValues where couplingType = 3 and recordStatus = 6";
  @Override public Map<String, List<Map<String, Object>>> getDynamicConflictingValues() throws RDBMSException
  {
    Map<String, List<Map<String, Object>>> dynamicConflicts = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_DYNAMIC_CONFLICTS);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        Map<String, Object> property = new HashMap<>();
        long targetEntityIID = result.getLong("targetEntityIID");
        long propertyIID = result.getLong("propertyIID");

        property.put("entityIID", targetEntityIID);
        property.put("propertyIID", propertyIID);
        property.put("sourceEntityIID", result.getLong("sourceEntityIID"));
        property.put("localeIID", result.getLong("localeIID"));
        property.put("couplingSourceType", result.getInt("couplingSourceType"));

        String key = targetEntityIID + "__" + propertyIID;
        List<Map<String, Object>> properties = dynamicConflicts.computeIfAbsent(key, z -> new ArrayList<>());
        properties.add(property);
      }
    });
    return dynamicConflicts;
  }


  public static final String GET_SIMILIAR_RECORD = "select * from %s where propertyIID = %s and entityIID = %s";
  @Override
  public Long findSimilarValuedSourceEntity(IPropertyRecordDTO value, List<Long> sourceEntityIIDs) throws RDBMSException
  {
    String tableName = value.getProperty().isAttribute() ? "pxp.valueRecord" : "pxp.tagsRecord";
    String query = String.format(GET_SIMILIAR_RECORD, tableName, value.getProperty().getPropertyIID(), sourceEntityIIDs);

    if(value.getProperty().isAttribute() && ((IValueRecordDTO)value).getLocaleID() != null) {
      query += "and localeID = " + ((IValueRecordDTO)value).getLocaleID();
    }
    String finalQuery = query;
    AtomicLong sourceEntityIID = new AtomicLong(0L);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        if(value.getProperty().isAttribute()) {

          IValueRecordDTO targetDTO =  (ValueRecordDTO) value;
          IValueRecordDTO sourceDTO = new ValueRecordDTO(result, value.getProperty());

          Predicate<IValueRecordDTO> isSource = y -> y.getLocaleID().equals(targetDTO.getLocaleID())
              && y.getValue().equals(targetDTO.getValue());

          if(isSource.test(sourceDTO)) {
            sourceEntityIID.set(sourceDTO.getEntityIID());
          }
        }
        else{
          ITagsRecordDTO targetDTO =  (TagsRecordDTO) value;
          ITagsRecordDTO tagDTO = new TagsRecordDTO(result, value.getProperty());

          Predicate<ITagsRecordDTO> isSource = y -> SetUtils.isEqualSet(y.getTags(), targetDTO.getTags());

          if(isSource.test(tagDTO)) {
            sourceEntityIID.set(tagDTO.getEntityIID());
          }
        }
      }
    });
    return sourceEntityIID.get();
  }


  public static final String UPDATE_CONFLICTING_VALUE_FOR_COUPLED_RECORD = "update pxp.conflictingvalues set recordstatus = 4 where targetentityiid = %s and propertyiid = %s" +
      " and sourceEntityIID = %s";
  public static final String UPDATE_CONFLICTING_VALUE_FOR_CONFLICTS = "update pxp.conflictingvalues set recordstatus = 2 where targetentityiid = %s and propertyiid = %s" +
      " and sourceEntityIID in (%s) ";
  public static final String CREATE_COUPLED_RECORD = "  INSERT INTO pxp.coupledRecord"
      + "   VALUES (%d, %d, %d, %d, %d, %s,'', %d, %d, %d); ";
  @Override
  public void migrateToDynamicCoupling(long entityToCouple, IPropertyDTO property, long targetEntityIID, List<Long> sourceEntityIIDs, long localeIID, ICSECoupling.CouplingType couplingType) throws
      RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      if(!sourceEntityIIDs.isEmpty()){
        String initQuery = String.format(UPDATE_CONFLICTING_VALUE_FOR_CONFLICTS, targetEntityIID, property.getPropertyIID(), Text
            .join(",",sourceEntityIIDs));
        PreparedStatement stmt = currentConn.prepareStatement(initQuery);
        stmt.executeUpdate();
      }

      String initialQuery = String.format(UPDATE_CONFLICTING_VALUE_FOR_COUPLED_RECORD, targetEntityIID, property.getPropertyIID(), entityToCouple);
      PreparedStatement stmt = currentConn.prepareStatement(initialQuery);
      stmt.executeUpdate();

      String createCoupledQuery = String.format(CREATE_COUPLED_RECORD, property.getPropertyIID(), targetEntityIID, 4 ,
          CouplingBehavior.DYNAMIC.ordinal(), couplingType.ordinal(), String.format("'%d:%d'", entityToCouple, property.getPropertyIID()),
          entityToCouple, property.getPropertyIID(), localeIID);
      stmt = currentConn.prepareStatement(createCoupledQuery);
      stmt.executeUpdate();
    });
  }
}
