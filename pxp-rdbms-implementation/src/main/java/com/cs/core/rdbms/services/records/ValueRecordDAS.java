package com.cs.core.rdbms.services.records;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.CalculationResolver;
import com.cs.core.rdbms.services.resolvers.NodeResolver;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Manage simple (not-coupled, not-calculated) value records
 *
 * @author vallee
 */
public class ValueRecordDAS extends AbstractRecordDAS implements IRecordDAS {

  private final ValueRecordDTO valueRecord;

  /**
   * Value record service constructor
   *
   * @param driver
   * @param connection
   * @param catalog
   * @param record
   * @param baseentityIID to which the property belongs
   */
  public ValueRecordDAS( RDBMSConnection connection, LocaleCatalogDAO catalog,
          IPropertyRecordDTO record, long baseentityIID) {
    super( connection, catalog, record, baseentityIID);
    valueRecord = (ValueRecordDTO) record;
  }

  /**
   * Load value records attached to a base entity
   *
   * @param connection the current connection
   * @param catalog the catalog from where to consider value records
   * @param entityIID the concerned base entity IID
   * @param properties the setContentFrom of properties to load, only ATTRIBUTE type are considered
   * @return a setContentFrom of loaded property records
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Set<IPropertyRecordDTO> loadRecords(
      RDBMSConnection connection, LocaleCatalogDAO catalog, long entityIID,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {

    Set<Long> valueRecordPropertyIIDs = new HashSet<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyType()
              .getSuperType() == SuperType.ATTRIBUTE && !property.isTrackingProperty()) {
        valueRecordPropertyIIDs.add(((PropertyDTO) property).getIID());
      }
    }
    Set<IPropertyRecordDTO> records = new HashSet<>();
    if (valueRecordPropertyIIDs.isEmpty()) {
      return records;
    }
    Map<Long, String> registeredRecords = new HashMap<>(); // memorize loaded records per propertyIID / contextualObjectIID
    IResultSetParser vRecordResult = connection.getFunction( ResultType.CURSOR, "pxp.fn_readValueRecord")
            .setInput(ParameterType.IID, entityIID)
            .setInput(ParameterType.INT, valueRecordPropertyIIDs.contains( StandardProperty.nameattribute.getIID()) ? 1 : 0)
            .setInput(ParameterType.IID_ARRAY, valueRecordPropertyIIDs)
            .setInput(ParameterType.STRING_ARRAY, catalog.getLocaleInheritanceSchema())
            .execute();

    while (vRecordResult.next()) {
      Long propertyIID = vRecordResult.getLong("propertyIID");
      IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
      long cxtObjectIID = vRecordResult.getLong("contextualobjectIID");
      ContextualDataDTO cxtObject = new ContextualDataDTO();
      if (cxtObjectIID != 0l) {
        cxtObject = getContextualObjectByIID( connection, cxtObjectIID);
      }
      String localeID = vRecordResult.getString("localeID");
      if (!registeredRecords.containsKey(propertyIID)) {
        registeredRecords.put(propertyIID, localeID);
        ValueRecordDTO record = new ValueRecordDTO(vRecordResult, property);
        record.setContextualData(cxtObject);
        records.add(record);
      } else if (registeredRecords.get(propertyIID).equals(localeID) && cxtObjectIID > 0) {
        ValueRecordDTO record = new ValueRecordDTO(vRecordResult, property);
        record.setContextualData(cxtObject);
        records.add(record);
      }
    }
    return records;
  }
  
  
  /**
   * Load value records attached for given entities
   *
   * @param connection the current connection
   * @param catalog the catalog from where to consider value records
   * @param entityIIds the concerned base entity IIds
   * @param properties the setContentFrom of properties to load, only ATTRIBUTE type are considered
   * @return a map  of loaded property records against entityIId
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static Map<Long,Set<IPropertyRecordDTO>> loadRecordsForEntities(
      RDBMSConnection connection, LocaleCatalogDAO catalog, Set<Long> entityIIds,
      IPropertyDTO... properties) throws SQLException, RDBMSException, CSFormatException
  {

    Set<Long> valueRecordPropertyIIDs = new HashSet<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyType()
              .getSuperType() == SuperType.ATTRIBUTE && !property.isTrackingProperty()) {
        valueRecordPropertyIIDs.add(((PropertyDTO) property).getIID());
      }
    }
    Map<Long,Set<IPropertyRecordDTO>> returnMap = new HashMap<>();
    if (valueRecordPropertyIIDs.isEmpty()) {
      return returnMap;
    }
    List<String> localeInheritanceSchema = catalog.getLocaleInheritanceSchema();
    IResultSetParser vRecordResult = connection.getFunction( ResultType.CURSOR, "fn_getselectivevaluerecords")
            .setInput(ParameterType.IID_ARRAY, entityIIds)
            .setInput(ParameterType.IID_ARRAY, valueRecordPropertyIIDs)
            .setInput(ParameterType.STRING, localeInheritanceSchema.get(0))
            .execute();

    while (vRecordResult.next()) {
      Long entityIID = vRecordResult.getLong("entityIID");
      Set<IPropertyRecordDTO> records;
      if(returnMap.containsKey(entityIID)) {
        records = returnMap.get(entityIID);
      }
      else {
        records = new HashSet<>();
        returnMap.put(entityIID, records);
      }
      Long propertyIID = vRecordResult.getLong("propertyIID");
      IPropertyDTO property = catalog.getPropertyByIID(propertyIID);
      long cxtObjectIID = vRecordResult.getLong("contextualobjectIID");
      ContextualDataDTO cxtObject = new ContextualDataDTO();
      if (cxtObjectIID != 0l) {
        cxtObject = getContextualObjectByIID( connection, cxtObjectIID);
      }
      ValueRecordDTO record = new ValueRecordDTO(vRecordResult, property);
      record.setContextualData(cxtObject);
      records.add(record);
    }
    return returnMap;
  }

  /**
   * create the value record for simple and calculated case
   *
   * @throws SQLException
   * @throws RDBMSException
   */
  private void createRecord() throws SQLException, RDBMSException {
    valueRecord.checkContentConsistency();
    ContextualDataDTO valueContext = (ContextualDataDTO) valueRecord.getContextualObject();
    IResultSetParser result = driver
            .getFunction(currentConnection, ResultType.IID, "pxp.fn_createValueRecord")
            .setInput(ParameterType.IID, this.baseEntityIID)
            .setInput(ParameterType.IID, valueRecord.getProperty().getIID())
            .setInput(ParameterType.STRING, valueContext.getContextCode().isEmpty() ? null : valueContext.getContextCode())
            //.setInput(ParameterType.INT, valueContext.getAllowDuplicate() ? 1 : 0)
            // TODO : Have to handle for allow Duplicates
            .setInput(ParameterType.INT, 1)
            .setInput(ParameterType.INT, valueRecord.getRecordStatus().ordinal())
            .setInput(ParameterType.STRING, (valueRecord.getLocaleID().isEmpty() ? null : valueRecord.getLocaleID()))
            .setInput(ParameterType.STRING, valueRecord.getValue())
            .setInput(ParameterType.STRING, valueRecord.getAsHTML())
            .setInput(ParameterType.STRING, valueRecord.getCalculation())
            .setInput(ParameterType.FLOAT, valueRecord.getAsNumber())
            .setInput(ParameterType.STRING, valueRecord.getUnitSymbol())
            .execute();
    valueRecord.setValueIID(result.getIID());
    if (!valueContext.getContextCode().isEmpty()) {
      valueContext.setIID(valueRecord.getValueIID());
      updateContextualData(valueContext); // Update contextual contents here
    }
  }

  @Override
  public void createSimpleRecord() throws SQLException, RDBMSException {
    if (record.isCalculated() || record.isCoupled()) {
      return;
    }
    createRecord();
  }

  @Override
  public void separateCoupledTargets(Long propertyIID, Long masterEntityIID) throws SQLException, RDBMSException
  {
    
    driver.getProcedure(currentConnection, "pxp.sp_CreateSeparatedValueTargets")
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, propertyIID)
        .setInput(RDBMSAbstractFunction.ParameterType.LONG, masterEntityIID)
        .setInput(ParameterType.STRING, valueRecord.getLocaleID().equals("") ? null : valueRecord.getLocaleID())
        .execute();
  }
  
  @Override
  protected void createNewDefaultRecord(NodeResolver.CouplingMaster master)
          throws SQLException, RDBMSException {
    IResultSetParser resultSetParser = driver
            .getFunction(currentConnection, ResultType.IID, "pxp.fn_createDefaultValueRecord")
            .setInput(RDBMSAbstractFunction.ParameterType.LONG, baseEntityIID)
            .setInput(RDBMSAbstractFunction.ParameterType.LONG, valueRecord.getProperty().getPropertyIID())
            .setInput(ParameterType.INT, record.getCouplingBehavior().ordinal())
            .setInput(ParameterType.INT, record.getCouplingType().ordinal())
            .setInput(ParameterType.STRING, record.getCouplingExpression())
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, valueRecord.getValue())
            .setInput(RDBMSAbstractFunction.ParameterType.TEXT, valueRecord.getAsHTML()
                    .isEmpty() ? null : valueRecord.getAsHTML())
            .setInput(RDBMSAbstractFunction.ParameterType.FLOAT, valueRecord.getAsNumber())
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, valueRecord.getUnitSymbol()
                    .isEmpty() ? null : valueRecord.getUnitSymbol())
            .setInput(ParameterType.IID, master.getEntityIID())
            .setInput(ParameterType.IID, master.getPropertyIID())
            .execute();
    valueRecord.setValueIID(resultSetParser.getIID());
  }

  /**
   * Refresh the value record with calculation evaluation
   *
   * @param catalog the current locale catalog of application
   * @return the parser calculation object
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  public ICSECalculationNode refreshCaclulation(LocaleCatalogDAO catalog)
          throws CSFormatException, RDBMSException, SQLException {
    ValueRecordDTO valRecord = (ValueRecordDTO) record;
    String calculationText = valRecord.getCalculation();
    ICSECalculationNode calc = (new CSEParser())
            .parseCalculation(calculationText);
    CalculationResolver resolver = new CalculationResolver( currentConnection, catalog);
    ICSELiteralOperand result = resolver.getResult(baseEntityIID, calc);
    if (!result.isUndefined()) {
      String value = StringEscapeUtils.unescapeHtml4(result.asString());
      value = Jsoup.parse(value).text();
      valRecord.setValue(value);
      valRecord.setAsHTML(result.asString());// set the String value in any case
      switch (result.getLiteralType()) {
        case Number:
        case Boolean:
          valRecord.setAsNumber(result.asDouble());
      }
    } else {
      valRecord.setValue("");
      valRecord.setAsHTML("");
      valRecord.setAsNumber(.0);
    }
    return calc;
  }

  @Override
  public void createCalculatedRecord() throws SQLException, RDBMSException {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    Set<String> dependencies;
    try {
      ICSECalculationNode calc = refreshCaclulation(catalog);
      dependencies = calc.getRecordNodeIDs();
    } catch (CSFormatException ex) {
      throw new RDBMSException(1000, "Invalid Calculation", ex);
    }
    createRecord();
    createDependencies(dependencies);
  }

  @Override
  public void updateCalculatedRecord() throws SQLException, RDBMSException {
    if (!record.isCalculated()) {
      return; // exclude non calculated records
    }
    try {
      ICSECalculationNode calc = refreshCaclulation(catalog);
      updateRecord();
      if (valueRecord.hasChangedCalculation()) {
        Set<String> dependencies = calc.getRecordNodeIDs();
        createDependencies(dependencies);
      }
    } catch (CSFormatException ex) {
      throw new RDBMSException(1000, "Invalid Calculation", ex);
    }
  }

  @Override
  public void updateRecord() throws SQLException, RDBMSException {
    valueRecord.checkContentConsistency();
    ContextualDataDTO valueContext = (ContextualDataDTO) valueRecord.getContextualObject();
    if (valueRecord.isChanged()) {
      driver.getProcedure(currentConnection, "pxp.sp_updateValueRecord")
              .setInput(ParameterType.LONG, valueRecord.getValueIID())
              .setInput(ParameterType.STRING, valueRecord.getValue())
              .setInput(ParameterType.STRING, valueRecord.getAsHTML())
              .setInput(ParameterType.FLOAT, valueRecord.getAsNumber())
              .setInput(ParameterType.STRING, valueRecord.getUnitSymbol())
              .setInput(ParameterType.STRING, valueRecord.getCalculation())
              .execute();
    }
    if (!valueContext.getContextCode()
            .isEmpty() && valueContext.isChanged()) {
      updateContextualData(valueContext);
    }
  }

  @Override
  protected void updateAsTightlyCoupledRecord()
          throws SQLException, RDBMSException, CSFormatException {
    if (valueRecord.isChanged()) {
      /*valueRecord.checkContentConsistency();
      // Keep memory of the master references, before being reset by status
      // update
      long masterEntiyIID = valueRecord.getMasterEntityIID();
      long masterPropertyIID = valueRecord.getMasterPropertyIID();
      valueRecord.setDecouplingStatus();
      
      IResultSetParser result = driver
              .getFunction(currentConnection, ResultType.IID, "pxp.fn_updateCoupledValueRecord")
              .setInput(ParameterType.LONG, baseEntityIID)
              .setInput(ParameterType.LONG, valueRecord.getProperty()
                      .getIID())
              .setInput(ParameterType.LONG, valueRecord.getValueIID())
              .setInput(ParameterType.INT, valueRecord.getRecordStatus()
                      .ordinal())
              .setInput(ParameterType.STRING, valueRecord.getValue())
              .setInput(ParameterType.STRING, valueRecord.getAsHTML())
              .setInput(ParameterType.FLOAT, valueRecord.getAsNumber())
              .setInput(ParameterType.STRING, valueRecord.getUnitSymbol())
              .setInput(ParameterType.IID, masterEntiyIID)
              .setInput(ParameterType.IID, masterPropertyIID)
              .execute();
      valueRecord.setValueIID(result.getIID());*/
      
      long masterEntiyIID = valueRecord.getMasterEntityIID();
        driver
          .getProcedure(currentConnection, "pxp.sp_updateValueCoupledRecord")
          .setInput(ParameterType.LONG, baseEntityIID)
          .setInput(ParameterType.LONG, valueRecord.getProperty().getIID())
          .setInput(ParameterType.LONG, masterEntiyIID)
          .setInput(ParameterType.STRING, valueRecord.getValue())
          .setInput(ParameterType.STRING, valueRecord.getLocaleID().equals("") ? null : valueRecord.getLocaleID())
          .setInput(ParameterType.STRING, valueRecord.getAsHTML())
          .setInput(ParameterType.FLOAT, valueRecord.getAsNumber())
          .setInput(ParameterType.STRING, valueRecord.getUnitSymbol())
          .setInput(ParameterType.STRING, valueRecord.getCalculation())
          .execute();
    }
    // Update contextual information
    ContextualDataDTO valueContext = (ContextualDataDTO) valueRecord.getContextualObject();
    if (!valueContext.getContextCode()
            .isEmpty() && valueContext.isChanged()) {
      valueContext.setIID(valueRecord.getValueIID()); // per construction same
      // as valueIID
      updateContextualData(valueContext);
    }
  }

  @Override
  protected void updateAsDecoupledRecord() throws SQLException, RDBMSException, CSFormatException {
    updateAsTightlyCoupledRecord();
  }

  @Override
  public void deleteAsSourceOfCoupling() throws RDBMSException, SQLException, CSFormatException {
    driver.getProcedure(currentConnection, "pxp.sp_CreateValuesFromAllTargetsAndDelete")
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, record.getNodeID())
            .execute();
  }

  @Override
  public void deleteRecord() throws RDBMSException, SQLException, CSFormatException {
    driver.getProcedure(currentConnection, "pxp.sp_deleteValueRecord")
        .setInput(ParameterType.IID, baseEntityIID)
        .setInput(ParameterType.IID, valueRecord.getProperty().getIID())
        .setInput(ParameterType.IID, valueRecord.getContextualObject().isNull() ? null : valueRecord.getContextualObject().getContextualObjectIID())
        .execute();
  }
  
  /** Deletes record from value record when language translation of an entity is deleted.
   * @throws RDBMSException
   * @throws SQLException 
   */
  public void deleteLanguageTranslationRecord() throws RDBMSException, SQLException{
    driver.getProcedure(currentConnection, "pxp.sp_deleteLanguageTranslation")
          .setInput(ParameterType.IID, valueRecord.getValueIID())
          .execute();
  }
  
  @Override
  public void resolveNotification() throws RDBMSException, SQLException {
    if (record.isNotifiedByCoupling()) {
      driver.getProcedure(currentConnection, "pxp.sp_resolveValueRecordNotification")
              .setInput(RDBMSAbstractFunction.ParameterType.IID, baseEntityIID)
              .setInput(RDBMSAbstractFunction.ParameterType.IID, record.getProperty()
                      .getIID())
              .setInput(RDBMSAbstractFunction.ParameterType.INT, record.getRecordStatus()
                      .ordinal())
              .execute();
    }
  }
  
  /***
   * Update the record's value and asnumber field for the given propertyIId and entityIId
   * @param propertyIId
   * @param value
   * @param entityIIds
   * @throws SQLException 
   * @throws RDBMSException 
   */
  public static void updateRecordValueByPropertyIIdAndEntityIId(RDBMSConnection connection,
      double value, long propertyIId, long entityIId) throws SQLException, RDBMSException
  {
    StringBuilder query = new StringBuilder("Update pxp.valuerecord set \"value\" = asnumber+").append(value)
        .append(", asnumber = asnumber+").append(value)
        .append(" where propertyiid = ").append(propertyIId)
        .append(" and entityiid =").append(entityIId);
    PreparedStatement statement = connection.prepareStatement(query.toString());
    statement.executeUpdate();
  }
  
  /***
   * reduce the value and asnumber field of parent instance in valuerecord table for given propertyIId, entityIID
   * @param connection
   * @param value
   * @param propertyIId
   * @param entityIId
   * @throws SQLException
   * @throws RDBMSException
   */
  public static void decrementRecordValueByPropertyIIdAndEntityIId(RDBMSConnection connection,
      double value, long propertyIId, long entityIId) throws SQLException, RDBMSException
  {
    StringBuilder query = new StringBuilder("Update pxp.valuerecord vr set \"value\" = asnumber-").append(value)
        .append(", asnumber = asnumber-").append(value)
        .append(" from pxp.baseentity be where vr.propertyiid = ").append(propertyIId)
        .append(" and vr.entityiid = be.parentiid and be.baseentityiid = ").append(entityIId);
    PreparedStatement statement = connection.prepareStatement(query.toString());
    statement.executeUpdate();
  }
  
  public static Map<Long,List<IValueRecordDTO>> getAllValueRecordsForEntities(
      RDBMSConnection connection, IConfigurationDAO configDAO ,Set<Long> entityIIds) throws SQLException, RDBMSException, CSFormatException
  {
    
    IResultSetParser vRecordResult = connection
        .getFunction(ResultType.CURSOR, "pxp.fn_getallvaluerecord")
        .setInput(ParameterType.IID_ARRAY, entityIIds)
        .execute();
    
    Map<Long, List<IValueRecordDTO>> iidVSvalueRecords = new HashMap<>();
    
    while (vRecordResult.next()) {
      
      IPropertyDTO property = configDAO.getPropertyByIID(vRecordResult.getLong("propertyiid"));
      long cxtObjectIID = vRecordResult.getLong("contextualobjectIID");
      ContextualDataDTO cxtObject = new ContextualDataDTO();
      if (cxtObjectIID != 0l) {
        cxtObject = getContextualObjectByIID(connection, cxtObjectIID);
      }
      ValueRecordDTO valueRecordDto = new ValueRecordDTO(vRecordResult, property);
      if (cxtObjectIID > 0) {
        valueRecordDto.setContextualData(cxtObject);
      }
      
      long entityIID = vRecordResult.getLong("entityiid");
      List<IValueRecordDTO> valueRecords = iidVSvalueRecords.get(entityIID);
      if (valueRecords == null) {
        valueRecords = new ArrayList<>();
        iidVSvalueRecords.put(entityIID, valueRecords);
      }
      valueRecords.add(valueRecordDto);
    }
    return iidVSvalueRecords;
  }  

}
