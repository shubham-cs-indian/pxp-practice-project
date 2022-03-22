package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the various cases of coupled records creation
 *
 * @author vallee
 */
public class BaseEntityDAOCreateCoupledRecTest extends AbstractRDBMSDriverTests {
  
  private static final String NEW_MODEL_VALUE = "A New Model value for en_US";
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private IPropertyDTO createRandomValueRecordText(IBaseEntityDAO entityDAO0) throws RDBMSException
  {
    IPropertyDTO modelProp = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO modelVal = entityDAO0.newValueRecordDTOBuilder(modelProp, NEW_MODEL_VALUE)
        .localeID("en_US")
        .build();
    
    
    entityDAO0.createPropertyRecords(modelVal);
    return modelProp;
  }
  
  @Test
  public void createTransferedValueRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createTransferedValueRecord");
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
    IPropertyDTO shortDescProp = ConfigTestUtils.getTextProperty("ShortDescription");
    IValueRecordDTO shortDescVal = entityDAO.newValueRecordDTOBuilder(shortDescProp, "")
        .build();
    
    //shortDescVal.addTransferCoupling(ICSECouplingSource.Predefined.$origin, shortDescProp);
    entityDAO.createPropertyRecords(shortDescVal);
    printJSON("Created ", entityDAO.getBaseEntityDTO());
    IValueRecordDTO clonedPropertyRecord = (IValueRecordDTO) entityDAO
        .loadPropertyRecords(shortDescProp)
        .getPropertyRecord(2008);
    assert (clonedPropertyRecord != null && clonedPropertyRecord.getValueIID() != 0L);
    assert (!clonedPropertyRecord.getMasterNodeID()
        .isEmpty());
    assertPropertyRecordStatusCloned(clonedPropertyRecord);
  }
  
  @Test
  public void createTransferedTagsRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createTransferedTagsRecord");
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
    assert (entityDAO.getBaseEntityDTO()
        .getPropertyRecord(2011) != null);
    assertPropertyRecordStatusCloned(entityDAO.getBaseEntityDTO()
        .getPropertyRecord(2011));
  }
  
  @Test
  public void createTransferedRelationsSetRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createTransferedRelationsSetRecord");
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
    assert (entityDAO.getBaseEntityDTO()
        .getPropertyRecord(7003) != null);
    assertPropertyRecordStatusCloned(entityDAO.getBaseEntityDTO()
        .getPropertyRecord(7003));
  }
  
  @Test
  public void createDefaultValueRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("createDefaultValueRecords");
    IBaseEntityDAO entityDAO0 = DataTestUtils.openBaseEntityDAO(100005);
    IClassifierDTO electronicsTaxo = localeCatalogDao.newClassifierDTO(4007, "Electronics",
        IClassifierDTO.ClassifierType.TAXONOMY);
    IPropertyDTO modelProp = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO modelVal = entityDAO0.newValueRecordDTOBuilder(modelProp, "MODEL default value")
        .build();

    modelVal.addDefaultValueCoupling(electronicsTaxo, modelProp, true);
    entityDAO0.createPropertyRecords(modelVal);
    printJSON("Created new ", entityDAO0.getBaseEntityDTO());
    
    assert (entityDAO0.getBaseEntityDTO()
        .getPropertyRecord(modelProp.getIID()) != null);
    
    BaseEntityDTO entity1 = DataTestUtils.getBaseEntityDTO(100006);
    IBaseEntityDAO entityDAO1 = localeCatalogDao.openBaseEntity(entity1);
    IValueRecordDTO modelVal2 = entityDAO1
        .newValueRecordDTOBuilder(modelProp, "Value that must be discarded")
        .build();
    modelVal2.addDefaultValueCoupling(electronicsTaxo, modelProp, true);
    entityDAO1.createPropertyRecords(modelVal2);
    printJSON("Created from existing ", entityDAO1.getBaseEntityDTO());
    assert (((IValueRecordDTO) entityDAO1.getBaseEntityDTO()
        .getPropertyRecord(modelProp.getIID())).getValueIID() != 0l);
    String str0 = ((IValueRecordDTO) entityDAO0.getBaseEntityDTO()
        .getPropertyRecord(modelProp.getIID())).getValue();
    String str1 = ((IValueRecordDTO) entityDAO1.loadPropertyRecords(modelProp)
        .getPropertyRecord(modelProp.getIID())).getValue();
    assert (str0.equals(str1));
  }
  
  @Test
  public void createInheritedValueRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("createInheritedValueRecords");
    IBaseEntityDAO entityDAO0 = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO modelProp = createRandomValueRecordText(entityDAO0);
    
    IBaseEntityDAO entityDAOChild = DataTestUtils.openBaseEntityDAO(100010);
    IValueRecordDTO modelValchild = entityDAOChild
        .newValueRecordDTOBuilder(modelProp, "Discarded value")
        .localeID("en_US")
        .build();
    modelValchild.addInheritanceCoupling(modelProp, true);
    entityDAOChild.createPropertyRecords(modelValchild);
    
    printJSON("Created child ", entityDAOChild.getBaseEntityDTO());
    IValueRecordDTO valueRecordDTO = (IValueRecordDTO) entityDAOChild.loadPropertyRecords(modelProp)
        .getPropertyRecord(modelProp.getIID());
    assert (valueRecordDTO != null && valueRecordDTO.getValueIID() != 0l);
    assert (valueRecordDTO.getValue()
        .equals(NEW_MODEL_VALUE));
    assert (!valueRecordDTO.getMasterNodeID()
        .isEmpty());
  }
  
  @Test
  public void createSharedByRelationRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createSharedByRelationRecord");
    IBaseEntityDAO entityDAO0 = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO similarRelationship = localeCatalogDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    IPropertyDTO modelProp = createRandomValueRecordText(entityDAO0);
    
    IBaseEntityDAO entityLinkedDAO = DataTestUtils.openBaseEntityDAO(100010);
    IValueRecordDTO modelLinkedVal = entityLinkedDAO
        .newValueRecordDTOBuilder(modelProp, "Discarded value")
        .localeID("en_US")
        .build();
    modelLinkedVal.addRelationshipCoupling(similarRelationship, 1, modelProp, true);
    entityLinkedDAO.createPropertyRecords(modelLinkedVal);
    
    printJSON("Created at other side ", entityLinkedDAO.getBaseEntityDTO());
    assert (entityLinkedDAO.getBaseEntityDTO()
        .getPropertyRecord(modelProp.getIID()) != null);
  }
  
  private void assertPropertyRecordStatusCloned(IPropertyRecordDTO propertyRecord)
  {
    assert (propertyRecord.getRecordStatus() == RecordStatus.CLONED);
  }
}
