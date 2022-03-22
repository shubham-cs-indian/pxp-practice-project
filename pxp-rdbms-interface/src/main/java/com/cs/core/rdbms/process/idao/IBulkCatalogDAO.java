package com.cs.core.rdbms.process.idao;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idto.IBulkReportDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.Collection;
import java.util.List;

/**
 * Execute operations on a batch of base entities and records The concept of this interface is to run from a batch of defined base entities.
 * Tracking events and revisions are managed separately and are under the responsibility of the caller of these services Various services
 * must be run separately to manage the base entity data, included base entity references (children) /!\ For creation, these operations
 * don't assume all properties are created with their default values when specified. (no querying the configuration database for first
 * version) /!\ Frozen for version 1...
 *
 * @author vallee
 */
public interface IBulkCatalogDAO {

  public enum UpdateLinkMode {
    notapplicable, // default value when no links are updated
    overwrite, // all links are deleted and replaced by the update
    add; // links provided by the update are added to the existng ones
  }

  /**
   * Create all base entities (without records and name) provided they have no defined IID - create new base entities create attached
   * classifiers - create contextual data when specified.
   *
   * @param entities the list of entities with IID = 0 (entities with IID > 0 generates an exception)
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO createBaseEntities(List<IBaseEntityDTO> entities) throws RDBMSException, CSFormatException;

  /**
   * Update all base entities provided they have a defined IID - update base entities with modified time and user - update attached
   * classifiers (nature class cannot be updated) - update contextual data when specified.
   *
   * @param classifiersMode update mode for non-nature classifiers
   * @param childrenMode update mode for children
   * @param contextTagsMode update mode for context tags in contextual data if specified
   * @param entities the list of entities with defined IID (entities with IID == 0 generates an exception)
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO updateBaseEntities(UpdateLinkMode classifiersMode,
          UpdateLinkMode childrenMode, UpdateLinkMode contextTagsMode,
          Collection<IBaseEntityDTO> entities) throws RDBMSException, CSFormatException;

  /**
   * Create all value records provided they have no defined value IID - entityIID and propertyIID must be defined - create contextual data
   * when specified - coupled records and calculated records are supported
   *
   * @param entities the set of entities to which to attach the created value records (required for revision data)
   * @param records the list of value records without value IID
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO createValueRecords(Collection<IBaseEntityDTO> entities,
          Collection<IValueRecordDTO> records) throws RDBMSException, CSFormatException;

  /**
   * Update all value records provided they have a defined entityIID, propertyIID and value IID - entityIID and propertyIID must be defined
   * - only value, number, html, unit symbol and calculation can be updated - update contextual data when specified - coupling formula of
   * coupled records cannot be updated - calculated records are refreshed after update of their calculation
   *
   * @param entities the set of entities to which to attach the updated value records (required for revision data)
   * @param contextTagsMode update mode for context tags in contextual data if specified
   * @param records the list of value records with IID
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO updateValueRecords(Collection<IBaseEntityDTO> entities,
          UpdateLinkMode contextTagsMode, Collection<IValueRecordDTO> records)
          throws RDBMSException, CSFormatException;

  /**
   * Create all Tags records provided they are not existing in RDBMS - entityIID and propertyIID must be defined - coupled records are
   * supported
   *
   * @param entities the set of entities to which to attach the updated value records (required for revision data)
   * @param records the list of tags records to be created
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO createTagsRecords(Collection<IBaseEntityDTO> entities,
          Collection<ITagsRecordDTO> records) throws RDBMSException, CSFormatException;

  /**
   * Update all Tags records provided they are already existing in RDBMS - entityIID and propertyIID must be defined - Tags and ranges can
   * be updated - coupled records are supported
   *
   * @param entities the set of entities to which to attach the updated value records (required for revision data)
   * @param tagsMode update mode for tags (required for revision data)
   * @param records the list of value records with IID
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO updateTagsRecords(Collection<IBaseEntityDTO> entities,
          UpdateLinkMode tagsMode, Collection<ITagsRecordDTO> records)
          throws RDBMSException, CSFormatException;

  /**
   * Create all relations set provided they are not existing in RDBMS - entityIID and propertyIID must be defined - create contextual data
   * when specified - create extension when specified - coupled records are supported
   *
   * @param entities the set of entities to which to attach the updated value records (required for revision data)
   * @param records the list of tags records to be created
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO createRelationsSets(Collection<IBaseEntityDTO> entities,
          Collection<IRelationsSetDTO> records) throws RDBMSException, CSFormatException;

  /**
   * Update all Relation Sets provided they are already existing in RDBMS - entityIID and propertyIID must be defined - Relations are
   * updated - update contextual data when specified - coupled records are supported
   *
   * @param entities the set of entities to which to attach the updated value records (required for revision data)
   * @param tagsMode update mode for tags (required for revision data)
   * @param records the list of value records with IID
   * @return the bulk operations report
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBulkReportDTO updateRelationsSets(Collection<IBaseEntityDTO> entities,
          UpdateLinkMode tagsMode, Collection<ITagsRecordDTO> records)
          throws RDBMSException, CSFormatException;
}
