package com.cs.core.rdbms.services.resolvers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.config.dao.RefConfigurationDAO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.csexpress.calculation.CSELiteralOperand;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.records.ValueRecordDAS;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand.PropertyField;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Data Access Services to select value contents from CSE definitions
 *
 * @author vallee
 */
public class RecordResolver extends RDBMSDataAccessService {
  
  private final NodeResolver  nodeResolver;
  private final LocaleCatalogDAO catalog;
  
  /**
   * Create a new service interface
   *
   * @param driver
   * @param connection current connection
   * @param catalog
   */
  public RecordResolver(RDBMSConnection connection, ILocaleCatalogDAO catalog)
  {
    super(connection);
    nodeResolver = new NodeResolver( connection, catalog);
    this.catalog = (LocaleCatalogDAO) catalog;
  }
  
  /**
   * @param sourceBaseEntityIID
   * @param recordProperty
   * @return the result when a tracking property has been identified
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public CSELiteralOperand getTrackingPropertyResult( long sourceBaseEntityIID, IPropertyDTO recordProperty) 
          throws RDBMSException {
    CSELiteralOperand result = new CSELiteralOperand();
    IBaseEntityDTO sourceEntity = catalog.getBaseEntitiesByIIDs(List.of(String.valueOf(sourceBaseEntityIID))).get(0);
    if (recordProperty.getCode().equals(StandardProperty.createdbyattribute.toString())) {
      result.setValueAndType(sourceEntity.getCreatedTrack().getWho(), LiteralType.Text);
    }
    else if (recordProperty.getCode().equals(StandardProperty.createdonattribute.toString())) {
      result.setValueAndType(sourceEntity.getCreatedTrack().getWhen(), LiteralType.Date);
    }
    else if (recordProperty.getCode().equals(StandardProperty.lastmodifiedbyattribute.toString())) {
      result.setValueAndType(sourceEntity.getLastModifiedTrack().getWho(), LiteralType.Text);
    }
    else if (recordProperty.getCode().equals(StandardProperty.lastmodifiedattribute.toString())) {
      result.setValueAndType(sourceEntity.getLastModifiedTrack().getWhen(), LiteralType.Date);
    }
    return result;
  }
  /**
   * @param sourceBaseEntityIID
   * @param recordProperty
   * @param getSingleTextValue return the best tag value when true, or the full tag content when false
   * @return the result operand from a tag
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException 
   */
  public CSELiteralOperand getTagResult( long sourceBaseEntityIID, IPropertyDTO recordProperty, boolean getSingleTextValue) 
          throws SQLException, RDBMSException, CSFormatException {
    CSELiteralOperand result = new CSELiteralOperand();
    IResultSetParser rs = driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_readTagsRecord")
        .setInput(ParameterType.IID, sourceBaseEntityIID)
        .setInput(ParameterType.IID_ARRAY, Arrays.asList(recordProperty.getPropertyIID()))
        .execute();
    if ( rs.next() ) {
      TagsRecordDTO tag = new TagsRecordDTO(rs, recordProperty);
      if(tag.getTagValueCodes().size() > 0) {
        if ( getSingleTextValue ) {
          String tagValueCode = tag.getBetterTagValueCode();
          String tagValue = "";
          if (!tagValueCode.isEmpty()) {
            try {
              IConfigTagValueDTO tagValueDTO = (new RefConfigurationDAO()).getTagValueByCode(catalog.getLocaleCatalogDTO().getLocaleID(),
                  tagValueCode);
              tagValue = tagValueDTO.getLabel();
            }
            catch (CSInitializationException ex) {
              RDBMSLogger.instance().exception(ex);
            }
          }
          result.setValueAndType(tagValue, LiteralType.Text);
        } else {
          result.setValueAndType( "", LiteralType.List);
          result.addTags( tag.getTagValueCodes(), tag.getTagRanges());
        }
      } else {
        result.setValueAndType( "", LiteralType.List);
        result.addTags( tag.getTagValueCodes(), tag.getTagRanges());
      }
    } else {
      result.setValueAndType( "", LiteralType.List);
      result.addTags(new ArrayList<String>(), new ArrayList<Integer>());
    }
    return result;
  }
  
  /**
   * @param sourceBaseEntityIID
   * @param recordProperty
   * @return the result operand from a contextual value 
   * @throws SQLException
   * @throws RDBMSException 
   */
  private CSELiteralOperand getValueResult( 
          long sourceBaseEntityIID, IPropertyDTO recordProperty, String contextCode, PropertyField field) 
          throws SQLException, RDBMSException, CSFormatException {
    // check consistency of catalog locale at minimum in locale inheritance schema and load records
    catalog.checkLocaleInheritance();    
    CSELiteralOperand result = new CSELiteralOperand();
    ValueRecordDTO valueRecord = new ValueRecordDTO(); // empty by default
    // load all corresponding records with possible locales and contexts
    Set<IPropertyRecordDTO> records = ValueRecordDAS.loadRecordsForEntities(
            currentConnection, catalog, Set.of(sourceBaseEntityIID), recordProperty).computeIfAbsent(sourceBaseEntityIID, k -> new HashSet<>());
    for ( IPropertyRecordDTO record : records ) {
      valueRecord = (ValueRecordDTO)record;
      if ( contextCode.isEmpty() )
        break; // for non contextual records, the first entry matches the search
      else if ( valueRecord.getContextualObject().getContextCode().equals(contextCode)) {
        break;
      }
    }
    switch (field) {
      case html:
        result.setValueAndType( valueRecord.getAsHTML(), LiteralType.Text);
        break;
      case number:
        result.setValueAndType( valueRecord.getAsNumber(), valueRecord.getValue(),LiteralType.Number);
        break;
      case unit:
        result.setValueAndType( valueRecord.getUnitSymbol(), LiteralType.Text);
        break;
    case length:
      result.setValueAndType( valueRecord.getValue().length(), LiteralType.Number);
      break;
      default:
        result.setValueAndType( valueRecord.getValue(), recordProperty.getPropertyType().getLiteralType());
    }
     return result;
  }
  /**
   * @param baseEntityIID the base entity of reference
   * @param record the record definition to be resolved
   * @return the retrieved value of the record and update the records with found IIDs
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  public CSELiteralOperand getResult(long baseEntityIID,
      ICSERecordOperand record) throws CSFormatException, RDBMSException, SQLException
  {
    long sourceBaseEntityIID = nodeResolver.getSourceBaseEntityIID(record.getSource(), baseEntityIID);
    if ( sourceBaseEntityIID == 0L ) {
      RDBMSLogger.instance().warn(
              "Unresolved source %s for base entity IID %d", record.getSource().toString(), baseEntityIID);
      return new CSELiteralOperand(); // empty and undefined
    }
    CSELiteralOperand result;
    // Query the record property from the cache
    IPropertyDTO recordProperty = configDao.getPropertyByCode(record.getProperty().getCode());
    ((CSEProperty)record.getProperty()).setIID(recordProperty.getIID());
   // Manage special case for tracking attribute
    if (recordProperty.isTrackingProperty()) {
      result = getTrackingPropertyResult( sourceBaseEntityIID, recordProperty);
    }
    else if (recordProperty.getPropertyType() == PropertyType.TAG) {
      result = getTagResult( sourceBaseEntityIID, recordProperty, true);
    }
    // Query the record value
    else if (recordProperty.getSuperType() == SuperType.ATTRIBUTE) {
      PropertyField field = record.getPropertyField();
      String contextCode = record.getProperty().getContext().getCode();
      result = getValueResult( sourceBaseEntityIID, recordProperty, contextCode, field);
    } else {
      result = new CSELiteralOperand();
    }
    if ( result.isUndefined() ) // No record that corresponds to the specification has been found
      RDBMSLogger.instance().warn("Unresolved record %s for base entity IID %d", record.toString(), baseEntityIID);
    return result; // empty and undefined if the query has returned not found
  }
}
