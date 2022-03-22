package com.cs.core.rdbms.revision.dao;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.cs.core.rdbms.driver.RDBMSLogger;
import org.junit.Before;
import org.junit.Test;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.revision.dto.ObjectRevisionDTO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.google.common.collect.Sets;

public class RevisionDAOTests extends AbstractRDBMSDriverTests {
  
  IBaseEntityDAO baseEntityDao = null;
  RevisionDAO    revisionDAO   = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    revisionDAO = new RevisionDAO((UserSessionDTO) session);
  }
  
  private void printLastObjectRevisionInformationForBaseEntity(long iid) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          RevisionDAS das = new RevisionDAS( currentConn);
          ObjectRevisionDTO lastObjectRevision = das.getLastObjectRevision(iid);
          IBaseEntityDTO baseEntity = lastObjectRevision.getBaseEntityDTOArchive();
          TimelineDTO timelineData = lastObjectRevision.getTimelineData();
          System.out.println(timelineData.toJSON());
          System.out.println(baseEntity.toPXON());
          System.out.println(baseEntity.getPropertyRecords().size());
        });
  }
  
  @Test
  public void createFirstRevision() throws RDBMSException, CSFormatException
  {
    printTestTitle("createFirstRevision");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("REVTEST", true);
    IBaseEntityDAO baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    baseEntityDao.createPropertyRecords();
    println("select * from pxp.objectrevision where objectiid=" + entity.getBaseEntityIID());
    println("Use above qery to chcek revision is cretaed or not");
  }
  
  @Test
  public void getObjectRevisions() throws RDBMSException, CSFormatException
  {
    printTestTitle("ObjectRevisions get revision timeline test Case");
    
    // create data to ftech by objectREvision
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    IPropertyDTO webPresProp = localeCatalogDao.newPropertyDTO(2005, "", PropertyType.HTML);
    IValueRecordDTO webPresRec = baseEntityDao.newValueRecordDTOBuilder(webPresProp, "Hello World")
        .localeID("en_US")
        .build();
    webPresRec.setAsHTML("<p>Hello World</p>");
    
    IPropertyDTO webPresProp2 = localeCatalogDao.newPropertyDTO(2008, "", PropertyType.TEXT);
    IValueRecordDTO webPresRec2 = baseEntityDao
        .newValueRecordDTOBuilder(webPresProp2, "Hello World2")
        .localeID("en_US")
        .build();
    
    baseEntityDao.createPropertyRecords(webPresRec, webPresRec2);
    
    // revisionDAO.createNewRevision(entity, "Test ObjectRevisions");
    
    List<IObjectRevisionDTO> objectRevisions = revisionDAO
        .getObjectRevisions(baseEntityDao.getBaseEntityDTO()
            .getBaseEntityIID(), 1, 0);
    objectRevisions.forEach(dto -> {
      try {
        dto.getTimelines()
            .toString();
      }
      catch (CSFormatException e) {
        RDBMSLogger.instance().exception(e);
      }
    });
    
    assert (objectRevisions.size() == 1);
    System.out.println(objectRevisions.get(0)
        .toJSONBuffer());
  }
  
  @Test
  public void createNewRevisionForAttribute() throws RDBMSException, CSFormatException
  {
    printTestTitle("createNewRevision for 2 attribute created and one tag cretaed");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    System.out.println("baseEntityIID  " + entity.getBaseEntityIID());
    
    IPropertyDTO webPresProp = localeCatalogDao.newPropertyDTO(2005, "", PropertyType.HTML);
    IValueRecordDTO webPresRec = baseEntityDao.newValueRecordDTOBuilder(webPresProp, "Hello World")
        .localeID("en_US")
        .build();
    
    webPresRec.setAsHTML("<p>Hello World</p>");
    IPropertyDTO webPresProp2 = localeCatalogDao.newPropertyDTO(2008, "", PropertyType.TEXT);
    IValueRecordDTO webPresRec2 = baseEntityDao
        .newValueRecordDTOBuilder(webPresProp2, "Hello World2")
        .localeID("en_US")
        .build();
    
    int key = (new Random()).nextInt(100000);
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(0L, "Mat#" + key,
        PropertyType.TAG);
    ITagsRecordDTO MaterialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    driver.newConfigurationDAO()
        .createTagValue("PCB1", materialsProp.getIID());
    driver.newConfigurationDAO()
        .createTagValue("PCB2", materialsProp.getIID());
    ITagDTO tagRecordX = baseEntityDao.newTagDTO(25, "PCB1");
    ITagDTO tagRecordY = baseEntityDao.newTagDTO(25, "PCB2");
    MaterialDto.setTags(tagRecordX, tagRecordY);
    
    baseEntityDao.createPropertyRecords(webPresRec, webPresRec2, MaterialDto);
    
    // create 1st revision with 2 attribute created, 1 tag created
    int createNewRevision = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    entity = (BaseEntityDTO) baseEntityDao.loadPropertyRecords(webPresProp, webPresProp2);
    
    IPropertyRecordDTO propertyRecord = entity.getPropertyRecord(2005);
    
    IValueRecordDTO webPresRec3 = baseEntityDao
        .newValueRecordDTOBuilder(webPresProp, "Update Hello World")
        .localeID("en_US")
        .asHTML("<p>Update Hello World</p>")
        .build();
    
    baseEntityDao.updatePropertyRecords(webPresRec3);
    // create 2nd revision with one attribute modify
    int createNewRevision1 = revisionDAO.createNewRevision(entity, "createNewRevision");
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    assert (createNewRevision == 1);
    assert (createNewRevision1 == 2);
    
  }
  
  @Test
  public void createNewRevisionForParentChildren() throws RDBMSException, CSFormatException
  {
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    System.out.println("baseEntityIID  " + entity.getBaseEntityIID());
    
    BaseEntityDTO newChild = DataTestUtils.createRandomBaseEntityWithName("Child");
    BaseEntityDTO newChild2 = DataTestUtils.createRandomBaseEntityWithName("Child");
    baseEntityDao.addChildren(EmbeddedType.CONTEXTUAL_CLASS, newChild, newChild2);
    
    int createNewRevision1 = revisionDAO.createNewRevision(entity, "createNewRevision");
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    
    BaseEntityIDDTO oldChild = DataTestUtils.getBaseEntityDTO(newChild.getBaseEntityIID());
    baseEntityDao.removeChildren(EmbeddedType.CONTEXTUAL_CLASS, oldChild);
    
    int createNewRevision2 = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    assert (createNewRevision1 == 1);
    assert (createNewRevision2 == 2);
  }
  
  @Test
  public void createNewRevisionForClassifier() throws RDBMSException, CSFormatException
  {
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    IClassifierDTO newTaxo = ConfigTestUtils.createRandomTaxonomy();
    baseEntityDao.addClassifiers(newTaxo);
    int createNewRevision1 = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    
    baseEntityDao.removeClassifiers(newTaxo);
    
    int createNewRevision2 = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    
    assert (createNewRevision1 == 1);
    assert (createNewRevision2 == 2);
  }
  
  @Test
  public void createNewRevisionForRelation() throws RDBMSException, CSFormatException
  {
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    relation.setRelationSide(RelationSide.SIDE_1);
    IRelationsSetDTO set = baseEntityDao.newEntityRelationsSetDTOBuilder(relation,
        IPropertyDTO.RelationSide.SIDE_1).build();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity2 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity3 = DataTestUtils.newBaseEntity("FAM", true);
    set.setRelations(entity1.getIID(), entity2.getIID(), entity3.getIID());
    baseEntityDao.createPropertyRecords(set);
    
    entity = (BaseEntityDTO) baseEntityDao.loadPropertyRecords(relation);
    int revision1 = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    System.out.println("removed baseentity iid =====  " + entity3.getBaseEntityIID());
    set.removeRelations(entity3.getBaseEntityIID());
    baseEntityDao.updatePropertyRecords(set);
    int revision2 = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    printLastObjectRevisionInformationForBaseEntity(entity.getBaseEntityIID());
    assert (revision1 == 1);
    assert (revision2 == 2);
  }
  
  @Test
  public void compareRevison() throws RDBMSException, CSFormatException
  {
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    // 1st revision with one txonomy
    IClassifierDTO newTaxo = ConfigTestUtils.createRandomTaxonomy();
    baseEntityDao.addClassifiers(newTaxo);
    revisionDAO.createNewRevision(entity, "createNewRevision");
    
    // 2nd revision with relation and removed taxonomy
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO set = baseEntityDao.newEntityRelationsSetDTOBuilder(relation,
        IPropertyDTO.RelationSide.SIDE_1).build();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity2 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity3 = DataTestUtils.newBaseEntity("FAM", true);
    set.setRelations(entity1.getIID(), entity2.getIID(), entity3.getIID());
    baseEntityDao.createPropertyRecords(set);
    baseEntityDao.removeClassifiers(newTaxo);
    revisionDAO.createNewRevision(entity, "createNewRevision");
    
    List<IObjectRevisionDTO> revisions = getRevision(entity, Sets.newHashSet(1,2));
  }
  
  private List<IObjectRevisionDTO> getRevision(BaseEntityDTO entity, Set<Integer> revisionNos)
      throws RDBMSException, CSFormatException
  {
    List<IObjectRevisionDTO> revisions = revisionDAO.getRevisions(entity.getBaseEntityIID(),
        revisionNos);
    for (IObjectRevisionDTO dto : revisions)
      printJSON(dto);
    return revisions;
  }
  
  // TODO : impact of delete relation
  // @Test
  public void restoreRelationsFromRevision() throws RDBMSException, CSFormatException
  {
    printTestTitle("restore relations from revision");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO set = baseEntityDao.newEntityRelationsSetDTOBuilder(relation,
        IPropertyDTO.RelationSide.SIDE_1).build();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity2 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity3 = DataTestUtils.newBaseEntity("FAM", true);
    set.setRelations(entity1.getIID(), entity2.getIID(), entity3.getIID());
    baseEntityDao.createPropertyRecords(set);
    int createRelationRevision = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    set.removeRelations(entity1.getIID());
    baseEntityDao.updatePropertyRecords(set);
    int updateRelationRevision = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    revisionDAO.restoreFromRevision(entity, createRelationRevision);
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO loadPropertyRecords = baseEntityDao.loadPropertyRecords(relation);
    IRelationsSetDTO relationRecord = (IRelationsSetDTO) loadPropertyRecords
        .getPropertyRecord(relation.getPropertyIID());
    
    assert (relationRecord.getRelations()
        .size() == 3);
    
    revisionDAO.restoreFromRevision(entity, updateRelationRevision);
    IBaseEntityDTO loadPropertyRecords1 = baseEntityDao.loadPropertyRecords(relation);
    IRelationsSetDTO relationRecord1 = (IRelationsSetDTO) loadPropertyRecords1
        .getPropertyRecord(relation.getPropertyIID());
    
    assert (relationRecord1.getRelations()
        .size() == 2);
  }
  
  // @Test
  public void restorePropertyRecordsFromRevision() throws RDBMSException, CSFormatException
  {
    printTestTitle("restore property records from revision");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    System.out.println("baseEntityIID  " + entity.getBaseEntityIID());
    
    IPropertyDTO webPresProp = localeCatalogDao.newPropertyDTO(2005, "", PropertyType.HTML);
    IValueRecordDTO webPresRec = baseEntityDao.newValueRecordDTOBuilder(webPresProp, "Hello World")
        .localeID("en_US")
        .build();
    webPresRec.setAsHTML("<p>Hello World</p>");
    IPropertyDTO webPresProp2 = localeCatalogDao.newPropertyDTO(2008, "", PropertyType.TEXT);
    IValueRecordDTO webPresRec2 = baseEntityDao
        .newValueRecordDTOBuilder(webPresProp2, "Hello World2")
        .localeID("en_US")
        .build();
    
    int key = (new Random()).nextInt(100000);
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(0L, "Mat#" + key,
        PropertyType.TAG);
    ITagsRecordDTO MaterialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    driver.newConfigurationDAO()
        .createTagValue("PCB1", materialsProp.getIID());
    driver.newConfigurationDAO()
        .createTagValue("PCB2", materialsProp.getIID());
    ITagDTO tagRecordX = baseEntityDao.newTagDTO(25, "PCB1");
    ITagDTO tagRecordY = baseEntityDao.newTagDTO(25, "PCB2");
    MaterialDto.setTags(tagRecordX, tagRecordY);
    
    baseEntityDao.createPropertyRecords(webPresRec, webPresRec2, MaterialDto);
    int revision1 = revisionDAO.createNewRevision(entity, "createNewRevision");
    // update record 2005
    IPropertyRecordDTO propertyRecord = entity.getPropertyRecord(2005);
    IValueRecordDTO webPresRec3 = baseEntityDao
        .newValueRecordDTOBuilder(webPresProp2, "Update Hello World")
        .localeID("en_US")
        .build();
    webPresRec3.setAsHTML("<p>Update Hello World</p>");
    
    baseEntityDao.updatePropertyRecords(webPresRec3);
    revisionDAO.createNewRevision(entity, "createNewRevision");
    
    revisionDAO.restoreFromRevision(entity, revision1);
    IBaseEntityDTO restoredDTO = baseEntityDao.loadPropertyRecords(webPresProp);
    IPropertyRecordDTO restoredPropertyRecord = restoredDTO
        .getPropertyRecord(webPresProp.getPropertyIID());
    
    assert (((IValueRecordDTO) restoredPropertyRecord).getValue()
        .equals(webPresRec.getValue()));
  }
  
  //@Test
  public void restoreCLassifierFromRevision() throws RDBMSException, CSFormatException
  {
    printTestTitle("restore classifier from revision");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("createNewRevision", true);
    
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    System.out.println("baseEntityIID  " + entity.getBaseEntityIID());
    
    IClassifierDTO newTaxo = ConfigTestUtils.createRandomTaxonomy();
    baseEntityDao.addClassifiers(newTaxo);
    int classifierAddedRevision = revisionDAO.createNewRevision(entity, "createNewRevision");
    
    baseEntityDao.removeClassifiers(newTaxo);
    revisionDAO.createNewRevision(entity, "createNewRevision");
    
    revisionDAO.restoreFromRevision(entity, classifierAddedRevision);
    List<IClassifierDTO> classifiers = baseEntityDao.getClassifiers();
    IClassifierDTO classifierDTO = classifiers.get(0);
    
    assert (newTaxo.getClassifierCode() == classifierDTO.getClassifierCode());
  }
}
