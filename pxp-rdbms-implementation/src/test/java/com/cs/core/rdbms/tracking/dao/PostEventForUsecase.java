package com.cs.core.rdbms.tracking.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cs.constants.Constants;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class PostEventForUsecase extends AbstractRDBMSDriverTests {
  
  IBaseEntityDAO baseEntityDao = null;
  IPropertyDTO   tagProperty   = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    tagProperty = ConfigTestUtils.createRandomTagProperty();
  }
  
  @Test
  public void createRecordsWithEntity() throws RDBMSException, CSFormatException
  {
    printTestTitle("createEntity");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    // Value Record
    IValueRecordDTO valueRecord1 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord2 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord3 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord4 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    
    // tag records
    ITagsRecordDTO tagRecord1 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    ITagsRecordDTO tagRecord2 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    ITagsRecordDTO tagRecord3 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    ITagsRecordDTO tagRecord4 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    
    // three entity attach to each relationship at time
    BaseEntityDTO asset1 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    BaseEntityDTO asset2 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    BaseEntityDTO asset3 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    IRelationsSetDTO relationsSetDTO1 = createRelationship(asset1, asset2, asset3);
    IRelationsSetDTO relationsSetDTO2 = createRelationship(asset1, asset2, asset3);
    
    // Contextual ValueRecord with multi language
    IContextDTO context = ConfigTestUtils.createRandomValueContext();
    IPropertyDTO property = ConfigTestUtils.getTextProperty("ShortDescription");
    
    IValueRecordDTO contextValueRecord1 = createNewContextualValue(property, "C1 data in time", context, 0L, System.currentTimeMillis(),
        null, Constants.ENGLISH_US);
    IValueRecordDTO contextValueRecord2 = createNewContextualValue(property, "C2 data in time", context, System.currentTimeMillis(),
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1), null, Constants.FRENCH);
    IValueRecordDTO contextValueRecord3 = createNewContextualValue(property, "RED data with Tag", context, 0, 0,
        ConfigTestUtils.newRandomCode("RED"), Constants.ENGLISH_US);
    IValueRecordDTO contextValueRecord4 = createNewContextualValue(property, "BLUE data with Tag", context, 0, 0,
        ConfigTestUtils.newRandomCode("BLUE"), Constants.FRENCH);
    
    // create value record in multiple language
    IPropertyDTO vR1Property = valueRecord1.getProperty();
    String value = "language dependant Value_";
    IValueRecordDTO valueRecord1_UK = baseEntityDao.newValueRecordDTOBuilder(vR1Property, value + Constants.ENGLISH_UK)
        .localeID(Constants.ENGLISH_UK).build();
    IValueRecordDTO valueRecord1_FR = baseEntityDao.newValueRecordDTOBuilder(vR1Property, value + Constants.FRENCH)
        .localeID(Constants.FRENCH).build();
    
    IPropertyDTO vR2Property = valueRecord2.getProperty();
    IValueRecordDTO valueRecord2_UK = baseEntityDao.newValueRecordDTOBuilder(vR2Property, value + Constants.ENGLISH_UK)
        .localeID(Constants.ENGLISH_UK).build();
    IValueRecordDTO valueRecord2_FR = baseEntityDao.newValueRecordDTOBuilder(vR2Property, value + Constants.FRENCH)
        .localeID(Constants.FRENCH).build();
    
    IPropertyRecordDTO records[] = { valueRecord1, valueRecord2, valueRecord3, valueRecord4, tagRecord1, tagRecord2, tagRecord3, tagRecord4,
        relationsSetDTO1, relationsSetDTO2, contextValueRecord1, contextValueRecord2, contextValueRecord3, contextValueRecord4,
        valueRecord1_UK, valueRecord1_FR, valueRecord2_UK, valueRecord2_FR };
    baseEntityDao.createPropertyRecords(records);
    
    // Update value record and tag
    value = "new Value updated_";
    valueRecord1.setValue(value+" 1");
    valueRecord2.setValue(value+" 2");
    
    Set<ITagDTO> tags1 = tagRecord1.getTags();
    tags1.forEach(t -> t.setRange(100));
    Set<ITagDTO> tags2 = tagRecord2.getTags();
    tags2.forEach(t -> t.setRange(0));
    
    valueRecord1_UK.setValue(value + Constants.ENGLISH_UK);
    valueRecord1_FR.setValue(value + Constants.FRENCH);
    valueRecord2_UK.setValue(value + Constants.ENGLISH_UK);
    valueRecord2_FR.setValue(value + Constants.FRENCH);
    
    // Update contextual tag
    addTagValue(ConfigTestUtils.newRandomCode("GREEN"), contextValueRecord3);
    addTagValue(ConfigTestUtils.newRandomCode("BLACK"), contextValueRecord3);
    addTagValue(ConfigTestUtils.newRandomCode("GREEN"), contextValueRecord4);
    
    contextValueRecord1.setValue(value + "1");
    contextValueRecord2.setValue(value + "2");
    
    BaseEntityDTO asset4 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    IEntityRelationDTO entity1 = baseEntityDao.newEntityRelationDTOBuilder().otherSideEntityIID(asset4.getIID())
        .OtherSideEntityID(asset4.getBaseEntityID()).build();
    relationsSetDTO1.getProperty().setRelationSide(RelationSide.SIDE_1);
    relationsSetDTO1.getRelations().add(entity1);
    relationsSetDTO1.removeRelations(asset1.getIID());
    
    BaseEntityDTO asset5 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    IEntityRelationDTO entity2 = baseEntityDao.newEntityRelationDTOBuilder().otherSideEntityIID(asset5.getIID())
        .OtherSideEntityID(asset5.getBaseEntityID()).build();
    relationsSetDTO2.getProperty().setRelationSide(RelationSide.SIDE_1);
    relationsSetDTO2.getRelations().add(entity2);
    relationsSetDTO2.removeRelations(asset2.getIID());
    
    baseEntityDao.updatePropertyRecords(valueRecord1, valueRecord2, tagRecord1, tagRecord2, valueRecord1_UK, valueRecord1_FR,
        valueRecord2_UK, valueRecord2_FR, contextValueRecord3, contextValueRecord4, contextValueRecord1, contextValueRecord2,
        relationsSetDTO1, relationsSetDTO2);
    
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void addAndRemoveClassifier() throws RDBMSException
  {
    printTestTitle("addAndRemoveClassifier");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    // add and remove classifier and taxonomy
    IClassifierDTO newTaxo1 = ConfigTestUtils.createRandomTaxonomy();
    IClassifierDTO newTaxo2 = ConfigTestUtils.createRandomTaxonomy();
    ClassifierDTO class1 = ConfigTestUtils.createRandomClass();
    ClassifierDTO class2 = ConfigTestUtils.createRandomClass();
    baseEntityDao.addClassifiers(newTaxo1, newTaxo2, class1, class2);
    baseEntityDao.removeClassifiers(newTaxo1, class1);
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void addAndRemoveChildrens() throws RDBMSException
  {
    printTestTitle("addAndRemoveClassifier");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    // add children and remove children
    BaseEntityDTO newChild1 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    BaseEntityDTO newChild2 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    BaseEntityDTO newChild3 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao.addChildren(EmbeddedType.CONTEXTUAL_CLASS, newChild1, newChild2, newChild3);
    baseEntityDao.removeChildren(EmbeddedType.CONTEXTUAL_CLASS, newChild3, newChild2);
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void createCoupledByRelationRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createCoupledByRelationRecord");
    BaseEntityDTO srcEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(srcEntity);
    
    BaseEntityDTO targetEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    IBaseEntityDAO entityLinkedDAO = localeCatalogDao.openBaseEntity(targetEntity);
    
    IEntityRelationDTO entity1 = entityDAO.newEntityRelationDTOBuilder().otherSideEntityIID(targetEntity.getIID())
        .OtherSideEntityID(targetEntity.getBaseEntityID()).build();
    IPropertyDTO relationshipProp = DataTestUtils.createRandomProperty("Relation", PropertyType.RELATIONSHIP);
    IRelationsSetDTO relationsSetDTO = entityDAO.newEntityRelationsSetDTOBuilder(relationshipProp, RelationSide.SIDE_1).build();
    relationsSetDTO.getRelations().addAll(Arrays.asList(entity1));
    entityDAO.updatePropertyRecords(relationsSetDTO);
    
    IPropertyDTO property = ConfigTestUtils.getTextProperty("ShortDescription");
    IValueRecordDTO modelLinkedVal = entityLinkedDAO.newValueRecordDTOBuilder(property, "Discarded value").localeID("en_US").build();
    modelLinkedVal.addRelationshipCoupling(relationshipProp, 1, property, true);
    entityLinkedDAO.createPropertyRecords(modelLinkedVal);
    
    printJSON("Created at other side ", entityLinkedDAO.getBaseEntityDTO());
    println("Source entity event info");
    printEventQueueData(srcEntity);
    println("Target entity event info");
    printEventQueueData(targetEntity);
  }
  
  //use this method for UI created entity event info
  @Ignore
  @Test
  public void getEvenetQueueInfo() throws RDBMSException
  {
    IBaseEntityDTO entityByIID = localeCatalogDao.getEntityByIID(1000054);
    printEventQueueData((BaseEntityDTO) entityByIID);
  }
  
  @Test
  public void createAndUpdateValueRecords() throws RDBMSException
  {
    printTestTitle("createAndUpdateValueRecords");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    // Value Record
    IValueRecordDTO valueRecord1 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord2 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord3 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    IValueRecordDTO valueRecord4 = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    baseEntityDao.createPropertyRecords(valueRecord1, valueRecord2, valueRecord3, valueRecord4);
    
    String value = "updated Value_";
    valueRecord1.setValue(value + 1);
    valueRecord2.setValue(value + 2);
    valueRecord3.setValue(value + 3);
    valueRecord4.setValue(value + 4);
    baseEntityDao.updatePropertyRecords(valueRecord1, valueRecord2, valueRecord3, valueRecord4);
    
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void createAndUpdateTagRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("createAndUpdateTagRecords");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    ITagsRecordDTO tagRecord1 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    ITagsRecordDTO tagRecord2 = DataTestUtils.createRandomTagRecord(baseEntityDao);
    baseEntityDao.createPropertyRecords(tagRecord1, tagRecord2);
    
    Set<ITagDTO> tags1 = tagRecord1.getTags();
    tags1.forEach(t -> t.setRange(100));
    Set<ITagDTO> tags2 = tagRecord2.getTags();
    tags2.forEach(t -> t.setRange(0));
    baseEntityDao.updatePropertyRecords(tagRecord1, tagRecord2);
    
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void cretaeAndUpdateRelationshipRecord() throws RDBMSException
  {
    printTestTitle("cretaeAndUpdateRelationshipRecord");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    
    BaseEntityDTO asset1 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    BaseEntityDTO asset2 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    BaseEntityDTO asset3 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    IRelationsSetDTO relationsSetDTO1 = createRelationship(asset1, asset2, asset3);
    IRelationsSetDTO relationsSetDTO2 = createRelationship(asset1, asset2, asset3);
    baseEntityDao.createPropertyRecords(relationsSetDTO1, relationsSetDTO2);
    
    BaseEntityDTO asset4 = DataTestUtils.createRandomBaseEntityWithName(BaseType.ASSET.getPrefix());
    IEntityRelationDTO entityRelation = buildEntityRelationDTO(asset4);
    relationsSetDTO1.getRelations().add(entityRelation);
    relationsSetDTO1.removeRelations(asset1.getBaseEntityIID());
    relationsSetDTO2.removeRelations(asset2.getBaseEntityIID(), asset3.getBaseEntityIID());
    relationsSetDTO1.getProperty().setRelationSide(RelationSide.SIDE_1);
    relationsSetDTO2.getProperty().setRelationSide(RelationSide.SIDE_1);
    baseEntityDao.updatePropertyRecords(relationsSetDTO1, relationsSetDTO2);
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void createAndUpdateLanguageDependantAttribute() throws RDBMSException
  {
    printTestTitle("createAndUpdateLanguageDependantAttribute");
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    baseEntityDao = localeCatalogDao.openBaseEntity(newBaseEntity);
    
    IValueRecordDTO valueRecord1_US = DataTestUtils.createRandomValueRecordText(baseEntityDao);
    // create value record in multiple language
    IPropertyDTO vR1Property = valueRecord1_US.getProperty();
    String value = "language dependant Value_";
    IValueRecordDTO valueRecord1_UK = baseEntityDao.newValueRecordDTOBuilder(vR1Property, value + Constants.ENGLISH_UK)
        .localeID(Constants.ENGLISH_UK).build();
    IValueRecordDTO valueRecord1_FR = baseEntityDao.newValueRecordDTOBuilder(vR1Property, value + Constants.FRENCH)
        .localeID(Constants.FRENCH).build();
    baseEntityDao.createPropertyRecords(valueRecord1_US, valueRecord1_UK, valueRecord1_FR);
    
    value = "Updated Value_";
    valueRecord1_US.setValue(value + Constants.ENGLISH_US);
    valueRecord1_UK.setValue(value + Constants.ENGLISH_UK);
    valueRecord1_FR.setValue(value + Constants.FRENCH);
    baseEntityDao.updatePropertyRecords(valueRecord1_US, valueRecord1_UK, valueRecord1_FR);
    printEventQueueData(newBaseEntity);
  }
  
  @Test
  public void createUndefinedCalculatedRecord()
      throws RDBMSException, CSFormatException, InterruptedException
  {
    printTestTitle("createUndefinedCalculatedRecord");
    IPropertyDTO sourceProp = ConfigTestUtils.createRandomTextProperty();
    IPropertyDTO calcProp = ConfigTestUtils.createRandomCalculatedProperty();
    String calcExpression = String.format("= 'New Content ' || [%s]", sourceProp.getCode());
    BaseEntityDTO newBaseEntity = DataTestUtils.createRandomBaseEntityWithName(BaseType.ARTICLE.getPrefix());
    IBaseEntityDAO entityDAO = localeCatalogDao.openBaseEntity(newBaseEntity);
    IValueRecordDTO calcRecord = entityDAO.newValueRecordDTOBuilder(calcProp, "")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    
    calcRecord.addCalculation(calcExpression);
    entityDAO.createPropertyRecords(calcRecord);
    printJSON(entityDAO.getBaseEntityDTO());
    
    IValueRecordDTO srcRecord = entityDAO.newValueRecordDTOBuilder(sourceProp, "JUST-CREATED")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    entityDAO.createPropertyRecords(srcRecord);
    printJSON("Source Record created:", entityDAO.getBaseEntityDTO());
    Thread.sleep(350); // Wait to allow the event handler running
    entityDAO.loadPropertyRecords(calcProp);
    printJSON("After propagation:", entityDAO.getBaseEntityDTO());
    
    printEventQueueData(newBaseEntity);
  }
  
  private IRelationsSetDTO createRelationship(BaseEntityDTO asset1, BaseEntityDTO asset2, BaseEntityDTO asset3) throws RDBMSException
  {
    IEntityRelationDTO entityRelation1 = buildEntityRelationDTO(asset1);
    IEntityRelationDTO entityRelation2 = buildEntityRelationDTO(asset2);
    IEntityRelationDTO entityRelation3 = buildEntityRelationDTO(asset3);
    IRelationsSetDTO relationsSetDTO = baseEntityDao
        .newEntityRelationsSetDTOBuilder(DataTestUtils.createRandomProperty("Relation", PropertyType.RELATIONSHIP), RelationSide.SIDE_1)
        .build();
    relationsSetDTO.getRelations().addAll(Arrays.asList(entityRelation1, entityRelation2, entityRelation3));
    return relationsSetDTO;
  }
  
  private IEntityRelationDTO buildEntityRelationDTO(BaseEntityDTO asset)
  {
    IEntityRelationDTO entityRelation = baseEntityDao.newEntityRelationDTOBuilder().otherSideEntityIID(asset.getIID())
        .OtherSideEntityID(asset.getBaseEntityID()).build();
    return entityRelation;
  }
  
  private void printEventQueueData(BaseEntityDTO newBaseEntity) throws RDBMSException
  {
    List<ObjectTrackingDTO> lastChanges = getEventQueueInfo(newBaseEntity);
    lastChanges.forEach(o -> {
      try {
        printJSON(o.getTimelineData().getCategories().toString(), o);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
      }
    });
  }
  
  //there is always Redirection event for rule Calculation which is duplicate info into event table
  private static final String Q_GET_Event_QUEUE_DATA = "select * from pxp.eventqueuewithdata ot where "
      + "ot.objectIID = ? and ot.eventType != 15 order by posted asc";
  
  private List<ObjectTrackingDTO> getEventQueueInfo(IBaseEntityDTO entity)
      throws RDBMSException
  {
    List<ObjectTrackingDTO> lastChanges = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currConnection) -> {
      PreparedStatement query = currConnection.prepareStatement(Q_GET_Event_QUEUE_DATA);
      query.setLong(1, entity.getBaseEntityIID());
      IResultSetParser rs = currConnection.getDriver().getResultSetParser(query.executeQuery());
      while (rs.next()) {
        ObjectTrackingDTO change = new ObjectTrackingDTO();
        try {
          change.set(rs);
        }
        catch (CSFormatException ex) {
          throw new RDBMSException(0, "FORMAT", ex);
        }
        lastChanges.add(change);
      }
    });
    return lastChanges;
  }
  
  private IValueRecordDTO createNewContextualValue(IPropertyDTO property, String value, IContextDTO context, long startDate, long endDate,
      String tagValueID, String localeID) throws RDBMSException
  {
    IValueRecordDTO vr = baseEntityDao.newValueRecordDTOBuilder(property, value).localeID(localeID).contextDTO(context).build();
    vr.getContextualObject().setContextStartTime(startDate);
    vr.getContextualObject().setContextEndTime(endDate);
    vr.getContextualObject().setAllowDuplicate(true);
    if (tagValueID != null) {
      addTagValue(tagValueID, vr);
    }
    return vr;
  }
  
  private void addTagValue(String tagValueID, IValueRecordDTO vr) throws RDBMSException
  {
    ITagValueDTO tag = ConfigurationDAO.instance().createTagValue(tagValueID, tagProperty.getCode());
    ITagDTO tagRec = baseEntityDao.newTagDTO(100, tag.getTagValueCode());
    vr.getContextualObject().getContextTagValues().add(tagRec);
  }
}
