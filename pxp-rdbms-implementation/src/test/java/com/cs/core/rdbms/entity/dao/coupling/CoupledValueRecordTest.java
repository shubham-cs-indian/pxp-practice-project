package com.cs.core.rdbms.entity.dao.coupling;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.CoupledRecordTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CoupledValueRecordTest extends CoupledRecordTests {
  
  private static final String EN_US                 = "en_US";
  private static final String VALUE_TO_BE_INHERITED = "Value to be inherited";
  private static final String VALUE_TO_BE_DISCARDED = "Value to be discarded";
  private static final String VALUE_TO_BE_CLONED    = "Value to be cloned";
  private static final String NEW_VALUE             = "New value";
  private static final String UPDATED_VALUE         = "Updated value";
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private void createInheritingRecord(IBaseEntityDAO parentEntityDAO, IBaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws RDBMSException, CSFormatException
  {
    parentEntityDAO.createPropertyRecords(
        parentEntityDAO.newValueRecordDTOBuilder(property, VALUE_TO_BE_INHERITED)
            .localeID(EN_US)
            .build());
    ;
    IValueRecordDTO valueRecordDTO = childEntityDAO
        .newValueRecordDTOBuilder(property, VALUE_TO_BE_DISCARDED)
        .localeID(EN_US)
        .build();
    valueRecordDTO.addInheritanceCoupling(property, dynamic);
    childEntityDAO.createPropertyRecords(valueRecordDTO);
  }
  
  private void createRelationalRecord(IBaseEntityDAO sourceEntityDAO,
      IBaseEntityDAO targetEntityDAO, IPropertyDTO property, IPropertyDTO relationship,
      boolean dynamic) throws RDBMSException, CSFormatException
  {
    sourceEntityDAO.createPropertyRecords(
        sourceEntityDAO.newValueRecordDTOBuilder(property, VALUE_TO_BE_INHERITED)
            .localeID(EN_US)
            .build());
    IValueRecordDTO valueRecordDTO = targetEntityDAO
        .newValueRecordDTOBuilder(property, VALUE_TO_BE_DISCARDED)
        .localeID(EN_US)
        .build();
    valueRecordDTO.addRelationshipCoupling((IPropertyDTO) relationship, 1, property, dynamic);
    targetEntityDAO.createPropertyRecords(valueRecordDTO);
  }
  
  private void createDefaultRecord(BaseEntityDAO entityDAO, IClassifierDTO classifierDTO,
      IPropertyDTO propertyDTO, boolean dynamic) throws RDBMSException, CSFormatException
  {
    IValueRecordDTO targetRecord = entityDAO
        .newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_INHERITED)
        .build();
    targetRecord.addDefaultValueCoupling(classifierDTO, propertyDTO, dynamic);
    entityDAO.createPropertyRecords(targetRecord);
  }
  
  private void createClonedRecord(IBaseEntityDAO cloneEntityDAO, IPropertyDTO property)
      throws RDBMSException, CSFormatException
  {
    IValueRecordDTO clonedValueRecordDTO = cloneEntityDAO
        .newValueRecordDTOBuilder(property, VALUE_TO_BE_DISCARDED)
        .localeID(EN_US)
        .build();
    cloneEntityDAO.createPropertyRecords(clonedValueRecordDTO);
  }
  
  private IBaseEntityDAO createSourceEntity(IPropertyDTO property) throws RDBMSException
  {
    BaseEntityDTO newBaseEntity = DataTestUtils.newBaseEntity("SOURCE", false);
    IBaseEntityDAO baseEntityDAO = localeCatalogDao.openBaseEntity(newBaseEntity);
    baseEntityDAO.createPropertyRecords(
        baseEntityDAO.newValueRecordDTOBuilder(property, VALUE_TO_BE_CLONED)
            .localeID(EN_US)
            .build());
    return baseEntityDAO;
  }
  
  private IBaseEntityDAO createClonedEntity(long originBaseEntityIID) throws RDBMSException
  {
    BaseEntityDTO newBaseEntity = DataTestUtils.newBaseEntity("CLONE", false);
    newBaseEntity.setOriginBaseEntityIID(originBaseEntityIID);
    return localeCatalogDao.openBaseEntity(newBaseEntity);
  }
  
  private IBaseEntityDAO createClonedEntityWithProp(long originBaseEntityIID, IPropertyDTO property)
      throws RDBMSException, CSFormatException
  {
    IBaseEntityDAO clonedEntity = this.createClonedEntity(originBaseEntityIID);
    createClonedRecord(clonedEntity, property);
    return clonedEntity;
  }
  
  private void assertInitialTightCoupling(IPropertyRecordDTO propertyRecord,
      CouplingType couplingType) throws CSFormatException
  {
    assert (RecordStatus.COUPLED == propertyRecord.getRecordStatus());
    assert (CouplingBehavior.TIGHTLY == propertyRecord.getCouplingBehavior());
    assert (couplingType == propertyRecord.getCouplingType());
    assert (!propertyRecord.getCouplingExpressions()
        .isEmpty());
    assert (!propertyRecord.getMasterNodeID()
        .isEmpty());
  }
  
  @Ignore
  @Test
  public void createCloneOfTightyInheritedRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createCloneOfTightyInheritedRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IBaseEntityDAO clonedEntityDAO = createClonedEntity(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    createClonedRecord(clonedEntityDAO, propertyDTO);
    
    IValueRecordDTO clonedValueRecordDTO = getProperty(clonedEntityDAO, propertyDTO);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    printJSON("Cloned Enitity ", clonedEntityDAO.getBaseEntityDTO());
    assert (VALUE_TO_BE_INHERITED.equals(clonedValueRecordDTO.getValue()));
    assert (RecordStatus.CLONED == clonedValueRecordDTO.getRecordStatus());
  }
  
  @Test
  public void createInhertanceRecordFromEmptyValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("createInhertanceRecordFromEmptyValue");
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    IValueRecordDTO valueRecordDTO = childEntityDAO
        .newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_DISCARDED)
        .localeID(EN_US)
        .build();
    valueRecordDTO.addInheritanceCoupling(propertyDTO, false);
    childEntityDAO.createPropertyRecords(valueRecordDTO);
    assertNull(loadProperty(childEntityDAO, propertyDTO));
  }
  
  @Test
  public void updateDynamiclyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO inheritedValue = loadProperty(childEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(inheritedValue.getValue()));
    inheritedValue.setValue(VALUE_TO_BE_DISCARDED);
    childEntityDAO.updatePropertyRecords(inheritedValue);
    inheritedValue = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (VALUE_TO_BE_INHERITED.equals(inheritedValue.getValue()));
    assertDynamicCoupling(inheritedValue, CouplingType.DYN_INHERITANCE);
  }
  
  @Test
  public void updateDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, true);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    IValueRecordDTO coupledValue = loadProperty(targetEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(coupledValue.getValue()));
    coupledValue.setValue(VALUE_TO_BE_DISCARDED);
    targetEntityDAO.updatePropertyRecords(coupledValue);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    coupledValue = loadProperty(targetEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(coupledValue.getValue()));
    assertDynamicCoupling(coupledValue, CouplingType.DYN_RELATIONSHIP);
  }
  
  @Test
  public void updateTightlyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    targetRecord.setValue(NEW_VALUE);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
  }
  
  @Test
  public void updateTightlyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    IValueRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    targetRecord.setValue(NEW_VALUE);
    targetEntityDAO.updatePropertyRecords(targetRecord);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    IValueRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(sourceRecord.getValue()));
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  @Test
  public void updateDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledDefaultValue");
    IClassifierDTO electronicsTaxo = localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY);
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO targetRecord = entityDAO
        .newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_INHERITED)
        .build();
    targetRecord.addDefaultValueCoupling(electronicsTaxo, propertyDTO, true);
    entityDAO.createPropertyRecords(targetRecord); // default value is
    printJSON("Created", entityDAO.getBaseEntityDTO());
    targetRecord = loadProperty(entityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    targetRecord.setValue(NEW_VALUE);
    entityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(entityDAO, propertyDTO);
    printJSON("Updated", entityDAO.getBaseEntityDTO());
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    // dynamic default update behaves similar tight coupling
    assertUpdatedTightCoupling(targetRecord, CouplingType.DYN_CLASSIFICATION, entityDAO); 
  }
  
  @Test
  public void updateTightlyCoupedDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupedDefaultValue");
    IClassifierDTO electronicsTaxo = localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IValueRecordDTO modelVal = entityDAO
        .newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_INHERITED)
        .build();
    modelVal.addDefaultValueCoupling(electronicsTaxo, propertyDTO, false);
    entityDAO.createPropertyRecords(modelVal);
    printJSON("Created", entityDAO.getBaseEntityDTO());
    modelVal = loadProperty(entityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(modelVal.getValue()));
    modelVal.setValue(NEW_VALUE);
    entityDAO.updatePropertyRecords(modelVal);
    printJSON("Updated", entityDAO.getBaseEntityDTO());
    modelVal = loadProperty(entityDAO, propertyDTO);
    assert (NEW_VALUE.equals(modelVal.getValue()));
    assertDirectRecord(modelVal);
  }
  
  @Ignore
  @Test
  public void updateClonedRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateClonedRecord");
    IBaseEntityDAO clonedEntityDAO = createClonedEntity(100005);
    IPropertyDTO propertyDTO = ConfigTestUtils.getTextProperty("ShortDescription");
    createClonedRecord(clonedEntityDAO, propertyDTO);
    printJSON("Cloned ", clonedEntityDAO.getBaseEntityDTO());
    IValueRecordDTO clonedRecord = getProperty(clonedEntityDAO, propertyDTO);
    // long oldClonedRecordIID = clonedRecord.getPropertyRecordIID();
    clonedRecord.setValue(NEW_VALUE);
    clonedEntityDAO.updatePropertyRecords(clonedRecord);
    clonedRecord = getProperty(clonedEntityDAO, propertyDTO);
    printJSON("Updated ", clonedEntityDAO.getBaseEntityDTO());
    // assertUniqueRecord(clonedRecord, oldClonedRecordIID);
  }
  
  // Source Update cases
  @Test
  public void updateSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    printJSON("Parent Enitity (before source update)", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity (before source update)", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    sourceRecord.setValue(NEW_VALUE);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Parent Record (after source update)", sourceRecord);
    printJSON("Child Record (after source update)", targetRecord);
    assert (NEW_VALUE.equals(targetRecord.getValue())); // updated value is
                                                        // reflected on
                                                        // inheriting records
    assertDynamicCoupling(targetRecord, CouplingType.DYN_INHERITANCE); // coupling
                                                                       // is
                                                                       // intact
  }
  
  @Test
  public void updateSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    printJSON("source entity (before source update)", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity (before source update)", targetEntityDAO.getBaseEntityDTO());
    IValueRecordDTO sourceRecord = getProperty(sourceEntityDAO, propertyDTO);
    sourceRecord.setValue(NEW_VALUE);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = getProperty(sourceEntityDAO, propertyDTO);
    IValueRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("source Record (after source update)", sourceRecord);
    printJSON("target Record (after source update)", targetRecord);
    assert (NEW_VALUE.equals(targetRecord.getValue())); // updated value is
                                                        // reflected on
                                                        // inheriting records
    assertDynamicCoupling(targetRecord, CouplingType.DYN_RELATIONSHIP); // coupling
                                                                        // is
                                                                        // intact
  }
  
  @Test
  public void updateSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    printJSON("Parent Enitity (before source update)", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity (before source update)", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO sourceRecord = getProperty(parentEntityDAO, propertyDTO);
    sourceRecord.setValue(NEW_VALUE);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = getProperty(parentEntityDAO, propertyDTO);
    printJSON("Parent Record (after source update)", sourceRecord);
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Child Record (after source update)", targetRecord);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue())); // value
                                                                    // updated
                                                                    // on source
                                                                    // does not
                                                                    // effect
                                                                    // inherited
                                                                    // value
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
  }
  
  @Test
  public void updateSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    printJSON("Source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("Target entity ", targetEntityDAO.getBaseEntityDTO());
    IValueRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    sourceRecord.setValue(NEW_VALUE);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("Source entity  Record (after source update)", sourceRecord);
    IValueRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("Target entity Record (after source update)", targetRecord);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue())); // value
                                                                    // updated
                                                                    // on source
                                                                    // does not
                                                                    // effect
                                                                    // inherited
                                                                    // value
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  // delete cases
  @Test
  public void deleteTightlyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    targetEntityDAO.deletePropertyRecords(getProperty(targetEntityDAO, propertyDTO));
    assertNotNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  public void deleteTightlyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    childEntityDAO.deletePropertyRecords(getProperty(childEntityDAO, propertyDTO));
    assertNotNull(loadProperty(parentEntityDAO, propertyDTO));
    assertNull(loadProperty(childEntityDAO, propertyDTO));
  }
  
  @Test
  public void deleteDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    targetEntityDAO.deletePropertyRecords(getProperty(targetEntityDAO, propertyDTO));
    assertNotNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  public void deleteDynamiclyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    childEntityDAO.deletePropertyRecords(getProperty(childEntityDAO, propertyDTO));
    assertNotNull(loadProperty(parentEntityDAO, propertyDTO));
    assertNull(loadProperty(childEntityDAO, propertyDTO));
  }
  
  @Test
  public void deleteDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledDefaultValue");
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    createDefaultRecord(entityDAO, localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY), propertyDTO, true);
    entityDAO.deletePropertyRecords(getProperty(entityDAO, propertyDTO));
    assertNull(loadProperty(entityDAO, propertyDTO));
  }
  
  @Test
  public void deleteTightlyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledDefaultValue");
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    createDefaultRecord(entityDAO, localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY), propertyDTO, false);
    entityDAO.deletePropertyRecords(getProperty(entityDAO, propertyDTO));
    assertNull(loadProperty(entityDAO, propertyDTO));
  }
  
  // delete Source Cases
  @Test
  public void deleteSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    sourceEntityDAO.deletePropertyRecords(loadProperty(sourceEntityDAO, propertyDTO));
    IValueRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    assert (targetRecord.getValue()
        .equals(VALUE_TO_BE_INHERITED));
    assertDirectRecord(targetRecord);
  }
  
  @Test
  public void deleteSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    parentEntityDAO.deletePropertyRecords(getProperty(parentEntityDAO, propertyDTO));
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (targetRecord.getValue()
        .equals(VALUE_TO_BE_INHERITED));
    assertDirectRecord(targetRecord);
  }
  
  @Test
  public void deleteSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    sourceEntityDAO.deletePropertyRecords(getProperty(sourceEntityDAO, propertyDTO));
    IValueRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    assert (targetRecord.getValue()
        .equals(VALUE_TO_BE_INHERITED));
    assertDirectRecord(targetRecord);
  }
  
  @Test
  public void deleteSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    parentEntityDAO.deletePropertyRecords(getProperty(parentEntityDAO, propertyDTO));
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (targetRecord.getValue()
        .equals(VALUE_TO_BE_INHERITED));
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void resolveAsDirectRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsDirectRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    targetRecord.setValue(NEW_VALUE);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    targetRecord.resolveCoupling();
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void resolveAsCoupledRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsCoupledRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("MODEL", PropertyType.TEXT);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    IValueRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    targetRecord.setValue(NEW_VALUE);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (NEW_VALUE.equals(targetRecord.getValue()));
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (VALUE_TO_BE_INHERITED.equals(targetRecord.getValue()));
    assertTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE);
  }
}
