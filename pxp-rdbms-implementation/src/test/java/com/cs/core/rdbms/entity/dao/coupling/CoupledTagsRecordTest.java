package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.testutil.CoupledRecordTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

public class CoupledTagsRecordTest extends CoupledRecordTests {
  
  private static final ITagDTO RED_TAG   = new TagDTO("red", 1);
  private static final ITagDTO BLUE_TAG  = new TagDTO("blue", 100);
  private static final ITagDTO BLACK_TAG = new TagDTO("black", 100);
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private void createInheritingRecord(BaseEntityDAO parentEntityDAO, BaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws RDBMSException, CSFormatException
  {
    ITagsRecordDTO sourcetagsRecord = parentEntityDAO.newTagsRecordDTOBuilder(property).build();
    sourcetagsRecord.setTags(parentEntityDAO.newTagDTO(50, "red"),
        parentEntityDAO.newTagDTO(51, "blue"), parentEntityDAO.newTagDTO(52, "black"));
    parentEntityDAO.createPropertyRecords(sourcetagsRecord);
    ITagsRecordDTO targetTagsRecord = childEntityDAO.newTagsRecordDTOBuilder(property).build();
    targetTagsRecord.addInheritanceCoupling(property, dynamic);
    childEntityDAO.createPropertyRecords(targetTagsRecord);
  }
  
  private void createRelationalRecord(BaseEntityDAO parentEntityDAO, BaseEntityDAO childEntityDAO,
      IPropertyDTO property, IPropertyDTO relationship, boolean dynamic)
      throws RDBMSException, CSFormatException
  {
    ITagsRecordDTO sourcetagsRecord = parentEntityDAO.newTagsRecordDTOBuilder(property).build();
    sourcetagsRecord.setTags(parentEntityDAO.newTagDTO(50, "red"),
        parentEntityDAO.newTagDTO(51, "blue"), parentEntityDAO.newTagDTO(52, "black"));
    parentEntityDAO.createPropertyRecords(sourcetagsRecord);
    ITagsRecordDTO targetTagsRecord = childEntityDAO.newTagsRecordDTOBuilder(property).build();
    targetTagsRecord.addRelationshipCoupling(relationship, 1, property, dynamic);
    childEntityDAO.createPropertyRecords(targetTagsRecord);
  }
  
  private void createDefaultRecord(BaseEntityDAO entityDAO, IClassifierDTO classifierDTO,
      IPropertyDTO property, boolean dynamic) throws CSFormatException, RDBMSException
  {
    ITagsRecordDTO targetRecord = entityDAO.newTagsRecordDTOBuilder(property).build();
    targetRecord.setTags(RED_TAG, BLUE_TAG, BLACK_TAG);
    targetRecord.addDefaultValueCoupling(classifierDTO, property, dynamic);
    entityDAO.createPropertyRecords(targetRecord);
  }
  
  private void assertEquals(ITagsRecordDTO sourceRecord, ITagsRecordDTO targetRecord)
  {
    for (ITagDTO targetTag : targetRecord.getTags()) {
      ITagDTO sourceTag = sourceRecord.getTagByCode(targetTag.getTagValueCode());
      assertNotNull(sourceTag);
      assert (sourceTag.getRange() == targetTag.getRange());
    }
  }
  
  @Test
  @Override
  public void updateDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, true);
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    ITagsRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(targetRecord, sourceRecord);
    targetRecord.setTags(targetEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    targetEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(targetRecord, sourceRecord);
    assertDynamicCoupling(targetRecord, CouplingType.DYN_RELATIONSHIP);
  }
  
  @Test
  @Override
  public void updateDynamiclyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, true);
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, property);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setTags(childEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    assertDynamicCoupling(targetRecord, CouplingType.DYN_INHERITANCE);
  }
  
  @Test
  @Override
  public void updateTightlyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, property);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setTags(childEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getTags()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
  }
  
  @Test
  @Override
  public void updateTightlyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, false);
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    ITagsRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setTags(targetEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    targetEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getTags()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  @Test
  @Override
  public void updateDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledDefaultValue");
    IClassifierDTO electronicsTaxo = localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY);
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color",
        IPropertyDTO.PropertyType.TAG);
    ITagsRecordDTO targetRecord = entityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
    targetRecord.setTags(RED_TAG, BLUE_TAG, BLACK_TAG);
    targetRecord.addDefaultValueCoupling(electronicsTaxo, propertyDTO, true);
    entityDAO.createPropertyRecords(targetRecord); // default value is
    printJSON("Created", entityDAO.getBaseEntityDTO());
    targetRecord = loadProperty(entityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    targetRecord.setTags(entityDAO.newTagDTO(50, "silver"));
    entityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(entityDAO, propertyDTO);
    printJSON("Updated", entityDAO.getBaseEntityDTO());
    assert (targetRecord.getTags()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.DYN_CLASSIFICATION, entityDAO); // dynamic
                                                                                   // default
                                                                                   // update
                                                                                   // behaves
                                                                                   // similar
                                                                                   // tight
                                                                                   // coupling
  }
  
  @Test
  @Override
  public void updateTightlyCoupedDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTightlyCoupedDefaultValue");
    IClassifierDTO electronicsTaxo = localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY);
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color",
        IPropertyDTO.PropertyType.TAG);
    ITagsRecordDTO targetRecord = entityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
    targetRecord.setTags(RED_TAG, BLUE_TAG, BLACK_TAG);
    targetRecord.addDefaultValueCoupling(electronicsTaxo, propertyDTO, false);
    entityDAO.createPropertyRecords(targetRecord); // default value is
    printJSON("Created", entityDAO.getBaseEntityDTO());
    targetRecord = loadProperty(entityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    targetRecord.setTags(entityDAO.newTagDTO(50, "silver"));
    entityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(entityDAO, propertyDTO);
    printJSON("Updated", entityDAO.getBaseEntityDTO());
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void updateSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Parent Enitity (before source update)", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity (before source update)", childEntityDAO.getBaseEntityDTO());
    sourceRecord.setTags(RED_TAG);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Parent Record (after source update)", sourceRecord);
    printJSON("Child Record (after source update)", targetRecord);
    assert (targetRecord.getTags()
        .size() == 1); // updated value is reflected on inheriting records
    assertDynamicCoupling(targetRecord, CouplingType.DYN_INHERITANCE); // coupling
                                                                       // is
                                                                       // intact
  }
  
  @Test
  @Override
  public void updateSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    ITagsRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("source entity (before source update)", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity (before source update)", targetEntityDAO.getBaseEntityDTO());
    sourceRecord.setTags(RED_TAG);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("source Record (after source update)", sourceRecord);
    printJSON("target Record (after source update)", targetRecord);
    assert (targetRecord.getTags()
        .size() == 1); // updated value is reflected on inheriting records
    assertDynamicCoupling(targetRecord, CouplingType.DYN_RELATIONSHIP); // coupling
                                                                        // is
                                                                        // intact
  }
  
  @Test
  @Override
  public void updateSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    printJSON("Parent Enitity (before source update)", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity (before source update)", childEntityDAO.getBaseEntityDTO());
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    sourceRecord.setTags(RED_TAG);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(parentEntityDAO, propertyDTO);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    printJSON("Parent Record (after source update)", sourceRecord);
    printJSON("Child Record (after source update)", targetRecord);
    assert (targetRecord.getTags()
        .size() == 3); // value updated on source does not effect inherited
                       // value
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
  }
  
  @Test
  @Override
  public void updateSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    ITagsRecordDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("Source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("Target entity ", targetEntityDAO.getBaseEntityDTO());
    sourceRecord.setTags(RED_TAG);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("Source entity  Record (after source update)", sourceRecord);
    printJSON("Target entity Record (after source update)", targetRecord);
    assert (targetRecord.getTags()
        .size() == 3); // value updated on source does not effect inherited
                       // value
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  @Test
  @Override
  public void deleteTightlyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    targetEntityDAO.deletePropertyRecords(getProperty(targetEntityDAO, propertyDTO));
    assertNotNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteTightlyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    childEntityDAO.deletePropertyRecords(getProperty(childEntityDAO, propertyDTO));
    assertNotNull(loadProperty(parentEntityDAO, propertyDTO));
    assertNull(loadProperty(childEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    targetEntityDAO.deletePropertyRecords(getProperty(targetEntityDAO, propertyDTO));
    assertNotNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteDynamiclyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    childEntityDAO.deletePropertyRecords(getProperty(childEntityDAO, propertyDTO));
    assertNotNull(loadProperty(parentEntityDAO, propertyDTO));
    assertNull(loadProperty(childEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledDefaultValue");
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createDefaultRecord(entityDAO, localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY), propertyDTO, true);
    entityDAO.deletePropertyRecords(getProperty(entityDAO, propertyDTO));
    assertNull(loadProperty(entityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteTightlyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledDefaultValue");
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color",
        IPropertyDTO.PropertyType.TAG);
    createDefaultRecord(entityDAO, localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY), propertyDTO, false);
    entityDAO.deletePropertyRecords(getProperty(entityDAO, propertyDTO));
    assertNull(loadProperty(entityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    sourceEntityDAO.deletePropertyRecords(loadProperty(sourceEntityDAO, propertyDTO));
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void deleteSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, false);
    parentEntityDAO.deletePropertyRecords(loadProperty(parentEntityDAO, propertyDTO));
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void deleteSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    sourceEntityDAO.deletePropertyRecords(loadProperty(sourceEntityDAO, propertyDTO));
    ITagsRecordDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void deleteSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, propertyDTO, true);
    parentEntityDAO.deletePropertyRecords(loadProperty(parentEntityDAO, propertyDTO));
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, propertyDTO);
    assert (targetRecord.getTags()
        .size() == 3);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  public void resolveAsDirectRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsDirectRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, property);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setTags(childEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getTags()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    targetRecord.resolveCoupling();
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  public void resolveAsCoupledRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsCoupledRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Color", PropertyType.TAG);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    ITagsRecordDTO sourceRecord = loadProperty(parentEntityDAO, property);
    ITagsRecordDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setTags(childEntityDAO.newTagDTO(RED_TAG.getRange(), RED_TAG.getTagValueCode()));
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getTags()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    assert (targetRecord.getTags()
        .size() == 3);
    assertTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE);
  }
}
