package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test the various cases of coupled records update
 *
 * @author rohan.shelar
 */
@Ignore
public class BaseEntityDAOUpdateCoupledRecTest extends AbstractRDBMSDriverTests {
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private void assertCheckOnCoupledUpdateRecord(long clonedRecordIID,
      IPropertyRecordDTO updatedPropertyRecord)
  {
    assert (CouplingBehavior.NONE == updatedPropertyRecord.getCouplingBehavior());
    assert (RecordStatus.DIRECT == updatedPropertyRecord.getRecordStatus());
    assert (CouplingType.NONE == updatedPropertyRecord.getCouplingType());
    // assert(clonedRecordIID != updatedPropertyRecord.getPropertyRecordIID());
  }
  
  @Test
  public void updateTransferedCoupledValueReocrd() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTransferedCoupledValueReocrd");
    BaseEntityDTO newEntity = DataTestUtils.newBaseEntity("Trans", false); // build
                                                                           // the
                                                                           // new
                                                                           // DTO
                                                                           // without
                                                                           // create
                                                                           // in
                                                                           // DB
    newEntity.setOriginBaseEntityIID(100005); // Important => fix the origin
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newEntity);
    IPropertyDTO shortDescProp = localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO shortDescVal = 
    entityDAO.newValueRecordDTOBuilder(shortDescProp, "")
        .build();
    
    //shortDescVal.addTransferCoupling(ICSECouplingSource.Predefined.$origin, shortDescProp);
    entityDAO.createPropertyRecords(shortDescVal);
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    
    // Load Cloned Property from RBDBMS
    IBaseEntityDTO clonedEntityDTO = entityDAO.loadPropertyRecords(shortDescProp);
    IValueRecordDTO clonedPropertyRecord = (IValueRecordDTO) clonedEntityDTO
        .getPropertyRecord(2008);
    // long clonedRecordIID = clonedPropertyRecord.getPropertyRecordIID();
    
    // assign new value
    clonedPropertyRecord.setValue("New value");
    entityDAO.updatePropertyRecords(clonedPropertyRecord);
    IBaseEntityDTO updatedEntityDTO = entityDAO.loadPropertyRecords(shortDescProp);
    IValueRecordDTO updatedPropertyRecord = (IValueRecordDTO) updatedEntityDTO
        .getPropertyRecord(2008);
    printJSON("Updated ", entityDAO.getBaseEntityDTO());
    
    assert ("New value".equals(updatedPropertyRecord.getValue()));
    // assertCheckOnCoupledUpdateRecord(clonedRecordIID, updatedPropertyRecord);
    
    // TODO add asserts for locale values and for context clone
  }
  
  @Test
  public void updateTransferedCoupledTagRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTransferedCoupledTagReocrd");
    BaseEntityDTO newEntity = DataTestUtils.newBaseEntity("Trans", false); // build
                                                                           // the
                                                                           // new
                                                                           // DTO
                                                                           // without
                                                                           // create
                                                                           // in
                                                                           // DB
    newEntity.setOriginBaseEntityIID(100005); // Important => fix the origin
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newEntity);
    IPropertyDTO colorsProp = localeCatalogDao.newPropertyDTO(2011, "Colors",
        IPropertyDTO.PropertyType.TAG);
    ITagsRecordDTO colorTags = entityDAO.newTagsRecordDTOBuilder(colorsProp).build();
    //colorTags.addTransferCoupling(ICSECouplingSource.Predefined.$origin, colorsProp);
    entityDAO.createPropertyRecords(colorTags);
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    
    // Load Cloned Property from RBDBMS and assign new value
    IBaseEntityDTO clonedEntityDTO = entityDAO.loadPropertyRecords(colorsProp);
    ITagsRecordDTO clonedPropertyRecord = (ITagsRecordDTO) clonedEntityDTO.getPropertyRecord(2011);
    // long clonedRecordIID = clonedPropertyRecord.getPropertyRecordIID();
    
    // Add new tag
    ITagDTO newTagValue = entityDAO.newTagDTO(55, "Colors");
    clonedPropertyRecord.mergeTags(newTagValue);
    entityDAO.updatePropertyRecords(clonedPropertyRecord);
    printJSON("Updated ", entityDAO.getBaseEntityDTO());
    
    IBaseEntityDTO updatedPropertyRecords = entityDAO.loadPropertyRecords(colorsProp);
    ITagsRecordDTO updatedPropertyRecord = (ITagsRecordDTO) updatedPropertyRecords
        .getPropertyRecord(2011);
    assert (4 == updatedPropertyRecord.getTags()
        .size());
    // assertCheckOnCoupledUpdateRecord(clonedRecordIID, updatedPropertyRecord);
  }
  
  @Test
  public void updateTransferedCoupledRelationSetReocrd() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTransferedCoupledRelationSetReocrd");
    BaseEntityDTO newEntity = DataTestUtils.newBaseEntity("Trans", false); // build
                                                                           // the
                                                                           // new
                                                                           // DTO
                                                                           // without
                                                                           // create
                                                                           // in
                                                                           // DB
    newEntity.setOriginBaseEntityIID(100005); // Important => fix the origin
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newEntity);
    IPropertyDTO similarProp = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    IRelationsSetDTO similarItems = entityDAO.newEntityRelationsSetDTOBuilder(similarProp,
        RelationSide.SIDE_1).build();
    similarProp.setRelationSide(RelationSide.SIDE_1);
    //similarItems.addTransferCoupling(ICSECouplingSource.Predefined.$origin, similarProp);
    entityDAO.createPropertyRecords(similarItems);
    
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    
    IRelationsSetDTO clonedPropertyRecord = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    // long clonedRecordIID = clonedPropertyRecord.getPropertyRecordIID();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    clonedPropertyRecord.removeRelations(100010L);
    clonedPropertyRecord.addRelations(entity1.getBaseEntityIID());
    
    // Update
    clonedPropertyRecord.getProperty()
        .setRelationSide(RelationSide.SIDE_1);
    entityDAO.updatePropertyRecords(clonedPropertyRecord);
    IRelationsSetDTO relationPropertyRecords = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    IEntityRelationDTO addedRelation = relationPropertyRecords
        .getRelationByIID(entity1.getBaseEntityIID());
    
    assert (entity1.getBaseEntityIID() == addedRelation.getOtherSideEntityIID());
    assert (null == relationPropertyRecords.getRelationByIID(100010L));
    assert (3 == relationPropertyRecords.getReferencedBaseEntityIIDs().length);
    // assertCheckOnCoupledUpdateRecord(clonedRecordIID,
    // relationPropertyRecords);
  }
  
  public void updateTransferedCoupledRelationSetReocrdContextualTime()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTransferedCoupledRelationSetReocrdContextualTime");
    BaseEntityDTO newEntity = DataTestUtils.newBaseEntity("Trans", false); // build
                                                                           // the
                                                                           // new
                                                                           // DTO
                                                                           // without
                                                                           // create
                                                                           // in
                                                                           // DB
    newEntity.setOriginBaseEntityIID(100005); // Important => fix the origin
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newEntity);
    IPropertyDTO similarProp = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    IRelationsSetDTO similarItems = entityDAO.newEntityRelationsSetDTOBuilder(similarProp,
        RelationSide.SIDE_1).build();
    similarProp.setRelationSide(RelationSide.SIDE_1);
    //similarItems.addTransferCoupling(ICSECouplingSource.Predefined.$origin, similarProp);
    entityDAO.createPropertyRecords(similarItems);
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    IRelationsSetDTO clonedPropertyRecord = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    // long clonedRecordIID = clonedPropertyRecord.getPropertyRecordIID();
    
    IEntityRelationDTO entityRelationDTO = clonedPropertyRecord.getRelationByIID(100013);
    IContextualDataDTO sideContextualObject = entityRelationDTO.getContextualObject();
    long oldEndTime = sideContextualObject.getContextEndTime();
    sideContextualObject.setContextEndTime(123);
    
    // Update
    clonedPropertyRecord.getProperty()
        .setRelationSide(RelationSide.SIDE_1);
    entityDAO.updatePropertyRecords(clonedPropertyRecord);
    printJSON("Updated ", entityDAO.getBaseEntityDTO());
    
    IRelationsSetDTO relationPropertyRecords = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    
    assert (123 == relationPropertyRecords.getRelationByIID(100013)
        .getContextualObject()
        .getContextEndTime());
    assert (3 == relationPropertyRecords.getReferencedBaseEntityIIDs().length);
    // assertCheckOnCoupledUpdateRecord(clonedRecordIID,
    // relationPropertyRecords);
  }
  
  public void updateTransferedCoupledRelationSetReocrdWithExtension()
      throws RDBMSException, CSFormatException
  {
    printTestTitle("updateTransferedCoupledRelationSetReocrdWithExtension");
    BaseEntityDTO newEntity = DataTestUtils.newBaseEntity("Trans", false); // build
                                                                           // the
                                                                           // new
                                                                           // DTO
                                                                           // without
                                                                           // create
                                                                           // in
                                                                           // DB
    newEntity.setOriginBaseEntityIID(100020); // Important => fix the origin
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newEntity);
    IPropertyDTO similarProp = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    IRelationsSetDTO similarItems = entityDAO.newEntityRelationsSetDTOBuilder(similarProp,
        RelationSide.SIDE_1).build();
    similarProp.setRelationSide(RelationSide.SIDE_1);
    //similarItems.addTransferCoupling(ICSECouplingSource.Predefined.$origin, similarProp);
    entityDAO.createPropertyRecords(similarItems);
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    
    IRelationsSetDTO clonedPropertyRecord = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    clonedPropertyRecord.getProperty()
        .setRelationSide(RelationSide.SIDE_1);
    // long clonedRecordIID = clonedPropertyRecord.getPropertyRecordIID();
    IEntityRelationDTO entityRelationDTO = clonedPropertyRecord.getRelationByIID(100030);
    
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    clonedPropertyRecord.addRelations(entity1.getBaseEntityIID());
    // Update
    entityDAO.updatePropertyRecords(clonedPropertyRecord);
    printJSON("Updated ", entityDAO.getBaseEntityDTO());
    
    IRelationsSetDTO relationPropertyRecords = (IRelationsSetDTO) entityDAO
        .loadPropertyRecords(similarProp)
        .getPropertyRecord(7003);
    
    assert (CouplingBehavior.NONE == relationPropertyRecords.getCouplingBehavior());
    assert (RecordStatus.DIRECT == relationPropertyRecords.getRecordStatus());
    assert (CouplingType.NONE == relationPropertyRecords.getCouplingType());
    // assert(clonedRecordIID !=
    // relationPropertyRecords.getPropertyRecordIID());
    // assert extension iid
    // assert cloned property count
    // assertEquals(expected, actual);
  }
  
  public void updateSourceOfCouplingValueRecod() throws RDBMSException, CSFormatException
  {
    // Create source
    printTestTitle("updateSourceOfCouplingValueRecod");
    BaseEntityDTO sourceEntity = DataTestUtils.newBaseEntity("TransSource", true);
    IBaseEntityDAO sourceEntityDAO = localeCatalogDao.openBaseEntity(sourceEntity);
    IPropertyDTO shortDescProp = localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO shortDescVal = sourceEntityDAO.newValueRecordDTOBuilder(shortDescProp, "")
        .build();
    shortDescVal.setValue("Short Description Value");
    sourceEntityDAO.createPropertyRecords(shortDescVal);
    printJSON("Created Source ", sourceEntityDAO.getBaseEntityDTO());
    
    BaseEntityDTO coupledEntity = DataTestUtils.newBaseEntity("TransCoupled", false);
    IBaseEntityDAO coupledEntityDAO = localeCatalogDao.openBaseEntity(coupledEntity);
    coupledEntity.setOriginBaseEntityIID(sourceEntity.getBaseEntityIID());
    IValueRecordDTO coupledShortDescVal = coupledEntityDAO.newValueRecordDTOBuilder(shortDescProp, "")
        .build();
    //coupledShortDescVal.addTransferCoupling(ICSECouplingSource.Predefined.$origin, shortDescProp);
    coupledEntityDAO.createPropertyRecords(coupledShortDescVal);
    printJSON("Created Cloned", coupledEntityDAO.getBaseEntityDTO());
    
    shortDescVal.setValue("Changed Source Value");
    shortDescVal.setChanged(true);
    sourceEntityDAO.updatePropertyRecords(shortDescVal);
    // assertEquals("New value", shortDescVal.getValue());
    // assertCheckOnCoupledUpdateRecord(clonedRecordIID, updatedPropertyRecord);
  }
}
