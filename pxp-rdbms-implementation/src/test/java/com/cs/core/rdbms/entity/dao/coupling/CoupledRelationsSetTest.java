package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.testutil.CoupledRecordTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

public class CoupledRelationsSetTest extends CoupledRecordTests {
  
  private void createInheritingRecord(BaseEntityDAO parentEntityDAO, BaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws RDBMSException, CSFormatException
  {
    IRelationsSetDTO sourceRelationRecord = parentEntityDAO.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    sourceRelationRecord.setRelations(100010L, 100013L, 100014L);
    parentEntityDAO.createPropertyRecords(sourceRelationRecord);
    IRelationsSetDTO targetRelationRecord = childEntityDAO.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    targetRelationRecord.addInheritanceCoupling(property, dynamic);
    childEntityDAO.createPropertyRecords(targetRelationRecord);
  }
  
  private void createRelationalRecord(BaseEntityDAO parentEntityDAO, BaseEntityDAO childEntityDAO,
      IPropertyDTO property, IPropertyDTO relationship, boolean dynamic)
      throws RDBMSException, CSFormatException
  {
    IRelationsSetDTO sourceRelationRecord = parentEntityDAO.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    sourceRelationRecord.setRelations(100010L, 100013L, 100014L);
    parentEntityDAO.createPropertyRecords(sourceRelationRecord);
    IRelationsSetDTO targetRelationRecord = childEntityDAO.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    targetRelationRecord.addRelationshipCoupling(relationship, 1, property, dynamic);
    childEntityDAO.createPropertyRecords(targetRelationRecord);
  }
  
  private void assertEquals(IRelationsSetDTO sourceRecord, IRelationsSetDTO targetRecord)
  {
    assert (sourceRecord.getRelations()
        .size() == targetRecord.getRelations()
            .size());
    for (IEntityRelationDTO entityRelation : sourceRecord.getRelations()) {
      IEntityRelationDTO entityRelationDTO = targetRecord
          .getRelationByIID(entityRelation.getOtherSideEntityIID());
      assertNotNull(entityRelationDTO);
      IContextualDataDTO contextualObject = entityRelation.getContextualObject();
      if (contextualObject == null) {
        assert (entityRelationDTO.getContextualObject() == null);
      }
      else {
        assert (contextualObject.getContextualObjectIID() == entityRelationDTO.getContextualObject()
            .getContextualObjectIID());
      }
    }
  }
  
  @Test
  @Override
  public void updateDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, true);
    IRelationsSetDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    IRelationsSetDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
    targetEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    assertDynamicCoupling(targetRecord, CouplingType.DYN_RELATIONSHIP);
  }
  
  @Test
  @Override
  public void updateDynamiclyCoupledByInheritanceRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, true);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
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
    printTestTitle("updateDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelationByIID(100010L) != null);
    assert (targetRecord.getRelations()
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
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, false);
    IRelationsSetDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    IRelationsSetDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
    targetEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelationByIID(100010L) != null);
    assert (targetRecord.getRelations()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  @Override
  public void updateDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void updateTightlyCoupedDefaultValue() throws RDBMSException, CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Test
  @Override
  public void updateSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, true);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    sourceRecord.setRelations(100010L);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(parentEntityDAO, property);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    assertDynamicCoupling(targetRecord, CouplingType.DYN_INHERITANCE);
  }
  
  @Test
  @Override
  public void updateSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, true);
    IRelationsSetDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    IRelationsSetDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    sourceRecord.setRelations(100010L);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    assertDynamicCoupling(targetRecord, CouplingType.DYN_RELATIONSHIP);
  }
  
  @Test
  @Override
  public void updateSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    sourceRecord.setRelations(100010L);
    parentEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(parentEntityDAO, property);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelations()
        .size() == 3);
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
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    IPropertyDTO relationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, relationship, false);
    IRelationsSetDTO targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    IRelationsSetDTO sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    printJSON("source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("target entity ", targetEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    sourceRecord.setRelations(100010L);
    sourceEntityDAO.updatePropertyRecords(sourceRecord);
    sourceRecord = loadProperty(sourceEntityDAO, propertyDTO);
    targetRecord = loadProperty(targetEntityDAO, propertyDTO);
    printJSON("updated source entity ", sourceEntityDAO.getBaseEntityDTO());
    printJSON("updated target entity ", targetEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelations()
        .size() == 3);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_RELATIONSHIP, targetEntityDAO);
  }
  
  @Test
  @Override
  public void deleteTightlyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    targetEntityDAO.deletePropertyRecords(loadProperty(targetEntityDAO, propertyDTO));
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
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    childEntityDAO.deletePropertyRecords(loadProperty(childEntityDAO, property));
    assertNotNull(loadProperty(parentEntityDAO, property));
    assertNull(loadProperty(childEntityDAO, property));
  }
  
  @Test
  @Override
  public void deleteDynamiclyCoupledByRelationshipRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    targetEntityDAO.deletePropertyRecords(loadProperty(targetEntityDAO, propertyDTO));
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
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, true);
    childEntityDAO.deletePropertyRecords(loadProperty(childEntityDAO, property));
    assertNotNull(loadProperty(parentEntityDAO, property));
    assertNull(loadProperty(childEntityDAO, property));
  }
  
  @Override
  public void deleteDynamiclyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void deleteTightlyCoupledDefaultValue() throws RDBMSException, CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Test
  @Override
  public void deleteSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), false);
    sourceEntityDAO.deletePropertyRecords(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNotNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfTightlyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    parentEntityDAO.deletePropertyRecords(loadProperty(parentEntityDAO, property));
    assertNull(loadProperty(parentEntityDAO, property));
    assertNotNull(loadProperty(childEntityDAO, property));
  }
  
  @Test
  @Override
  public void deleteSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByRelationshipRecord");
    BaseEntityDAO sourceEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO targetEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO propertyDTO = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    propertyDTO.setRelationSide(RelationSide.SIDE_1);
    createRelationalRecord(sourceEntityDAO, targetEntityDAO, propertyDTO, localeCatalogDao
        .newPropertyDTO(7003, "Similar-items", IPropertyDTO.PropertyType.RELATIONSHIP), true);
    sourceEntityDAO.deletePropertyRecords(loadProperty(sourceEntityDAO, propertyDTO));
    assertNull(loadProperty(sourceEntityDAO, propertyDTO));
    assertNotNull(loadProperty(targetEntityDAO, propertyDTO));
  }
  
  @Test
  @Override
  public void deleteSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteSourceOfDynamiclyCoupledByInheritanceRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, true);
    parentEntityDAO.deletePropertyRecords(loadProperty(parentEntityDAO, property));
    assertNull(loadProperty(parentEntityDAO, property));
    assertNotNull(loadProperty(childEntityDAO, property));
  }
  
  @Test
  @Override
  public void resolveAsDirectRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsDirectRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelationByIID(100010L) != null);
    assert (targetRecord.getRelations()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    targetRecord.resolveCoupling();
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    assert (targetRecord.getRelations()
        .size() == 1);
    assertDirectRecord(targetRecord);
  }
  
  @Test
  @Override
  public void resolveAsCoupledRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveAsCoupledRecord");
    BaseEntityDAO parentEntityDAO = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDAO childEntityDAO = DataTestUtils.openBaseEntityDAO(100010);
    IPropertyDTO property = DataTestUtils.createRandomProperty("Relation",
        PropertyType.RELATIONSHIP);
    property.setRelationSide(RelationSide.SIDE_1);
    createInheritingRecord(parentEntityDAO, childEntityDAO, property, false);
    IRelationsSetDTO sourceRecord = loadProperty(parentEntityDAO, property);
    IRelationsSetDTO targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Parent Enitity ", parentEntityDAO.getBaseEntityDTO());
    printJSON("Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assertEquals(sourceRecord, targetRecord);
    targetRecord.setRelations(100010L);
    childEntityDAO.updatePropertyRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    printJSON("Updated Child Enitity ", childEntityDAO.getBaseEntityDTO());
    assert (targetRecord.getRelationByIID(100010L) != null);
    assert (targetRecord.getRelations()
        .size() == 1);
    assertUpdatedTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE, childEntityDAO);
    childEntityDAO.resolveTightlyCoupledRecords(targetRecord);
    targetRecord = loadProperty(childEntityDAO, property);
    assertEquals(sourceRecord, targetRecord);
    assertTightCoupling(targetRecord, CouplingType.TIGHT_INHERITANCE);
  }
}
