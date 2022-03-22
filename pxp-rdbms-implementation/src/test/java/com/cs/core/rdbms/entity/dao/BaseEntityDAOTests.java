package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.*;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * TODO To pass all the test case, please ready with sample data, you can find
 * the "init-sample-data.sql" for sample data please run the test case with same
 * data.
 *
 * @author PankajGajjar
 */
public class BaseEntityDAOTests extends AbstractRDBMSDriverTests {
  
  IBaseEntityDAO baseEntityDao = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void delete() throws RDBMSException, CSFormatException
  {
    printTestTitle("delete");
    
    // prepare the entityDTO - ideally available or to be prepare for deletion
    // a dummy nature class is uded there knowing it is not significant for the
    // deletion operation
    // operation
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100045);
    if (baseEntityDao == null) {
      println("/!\\ Object already deleted => test skipped");
      return;
    }
    // Execute phase
    printJSON("deleted object", baseEntityDao.getBaseEntityDTO());
    baseEntityDao.delete();
    System.out.println("--------------delete");
  }
  
  @Test
  public void addclassifier() throws RDBMSException
  {
    printTestTitle("addclassifier");
    IClassifierDTO newTaxo = ConfigTestUtils.createRandomTaxonomy();
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100003);
    baseEntityDao.addClassifiers(newTaxo);
  }
  
  @Test
  public void removeClassifiers() throws RDBMSException
  {
    printTestTitle("removeClassifiers");
    IClassifierDTO nonNatureClass = ConfigTestUtils.getNonNatureClass();
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100003);
    baseEntityDao.removeClassifiers(nonNatureClass);
  }
  
  @Test
  public void getClassifiers() throws RDBMSException, CSFormatException
  {
    printTestTitle("getClassifiers");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100003);
    List<IClassifierDTO> result = baseEntityDao.getClassifiers();
    for (IClassifierDTO classDTO : result)
      printJSON(classDTO);
    assert (result != null);
    // positive test case
    // assertEquals(result.size(), 3);
  }
  
  @Test
  public void loadPropertyRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadPropertyRecords");
    
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100003);
    IBaseEntityDTO baseEntity = baseEntityDao.loadPropertyRecords(
        ConfigTestUtils.getTextProperty("ShortDescription"),
        ConfigTestUtils.getTagsProperty("Materials"),
        ConfigTestUtils.getTextProperty("nameattribute"));
    assert (baseEntity.getPropertyRecords().size() == 3);
    
    for (IPropertyRecordDTO property : baseEntity.getPropertyRecords()) {
      if (property instanceof ValueRecordDTO) {
        printJSON("ValueRecord Property: ", property);
        assert (((ValueRecordDTO) property).getValueIID() != 0l);
      }
      else if (property instanceof TagsRecordDTO) {
        assert (((TagsRecordDTO) property).getIID() != 0l
            && ((TagsRecordDTO) property).getTagValueCodes().size() > 0);
        printJSON("Tags Property: ", property);
      }
    }
  }
  
  @Test
  public void createLoadUpdatePropertyRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("createLoadUpdatePropertyRecords");
    BaseEntityDTO entity = DataTestUtils.newBaseEntity("CreateLoadUpdate", true);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    // new object with web-presentation "2005" creation
    IPropertyDTO webPresProp = localeCatalogDao.newPropertyDTO(2005, "web-presentation",
        PropertyType.HTML);
    IValueRecordDTO webPresRec = baseEntityDao.newValueRecordDTOBuilder(webPresProp, "Hello World")
        .localeID("en_US").build();
    webPresRec.setAsHTML("<p>Hello World</p>");
    baseEntityDao.createPropertyRecords(webPresRec);
    printJSON("100005 afer create 2005", baseEntityDao.getBaseEntityDTO());
    
    // Load
    IPropertyDTO webPresProp2 = localeCatalogDao.newPropertyDTO(2005, "web-presentation",
        PropertyType.HTML);
    baseEntityDao.loadPropertyRecords(webPresProp2);
    printJSON("100005 afer load 2005", baseEntityDao.getBaseEntityDTO());
    assert (baseEntityDao.getBaseEntityDTO()
        .getPropertyRecords()
        .size() == 1);
  }
  
  @Test
  public void createPropertyRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("createPropertyRecords");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    
    // prepare Value Record
    int key = (new Random()).nextInt(100000);
    IPropertyDTO marketPriceProp = localeCatalogDao.newPropertyDTO(0L, "Price#" + key,
        PropertyType.PRICE);
    IValueRecordDTO marketPriceDto = baseEntityDao.newValueRecordDTOBuilder(marketPriceProp,
        "230 €").localeID("fr_FR").build();
    marketPriceDto.setAsNumber(230);
    marketPriceDto.setUnitSymbol("€");
    // Prepare Tag Record
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(0L, "Mat#" + key,
        PropertyType.TAG);
    ITagsRecordDTO MaterialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    ITagDTO tagRecordX = baseEntityDao.newTagDTO(25, "PCBx#" + key);
    ITagDTO tagRecordY = baseEntityDao.newTagDTO(25, "PCBy" + key);
    MaterialDto.setTags(tagRecordX, tagRecordY);
    IPropertyRecordDTO records[] = { marketPriceDto, MaterialDto };
    
    IBaseEntityDTO resultDTO = baseEntityDao.createPropertyRecords(records);
    printJSON("Created properties on entity: ", resultDTO);
    assert (resultDTO != null);
    assert (2 == resultDTO.getPropertyRecords()
        .size());
  }
  
  @Test
  public void updatePropertyRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("updatePropertyRecords");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    
    baseEntityDao.getBaseEntityDTO()
        .setDefaultImageIID(100002);
    baseEntityDao.getBaseEntityDTO()
        .setHashCode(UUID.randomUUID()
            .toString());
    ;
    // Market Price "2007" old value is 230 - current Value - set by updating
    // the DTO
    IPropertyDTO marketPriceProp = localeCatalogDao.newPropertyDTO(2007, "Price",
        PropertyType.PRICE);
    IValueRecordDTO marketPriceDto = baseEntityDao
        .newValueRecordDTOBuilder(marketPriceProp, "300 €")
        .valueIID(400022)
        .localeID("fr_FR")
        .build();
    marketPriceDto.setAsNumber(300);
    marketPriceDto.setUnitSymbol("Rs");
    // Materials "2012" (2012, "y3456-PCB"); - current Value - set by
    // createPropertyRecords() Apis call
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(2012, "Colors", PropertyType.TAG);
    ITagsRecordDTO materialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    ITagDTO tagsValueDto = baseEntityDao.newTagDTO(-50, "pcb");
    ITagDTO tagsValueDto2 = baseEntityDao.newTagDTO(-51, "flexoLED");
    materialDto.setTags(tagsValueDto, tagsValueDto2);
    
    IBaseEntityDTO resultDTO = baseEntityDao.updatePropertyRecords(marketPriceDto, materialDto);
    printJSON("Updated base entity", resultDTO);
    assert (resultDTO != null);
  }
  
  @Test
  public void deletePropertyRecords() throws RDBMSException, CSFormatException
  {
    BaseEntityDTO entity1 = DataTestUtils.createRandomBaseEntityWithName("NEWEntity");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(entity1.getBaseEntityIID());
    // Preparing data to delete the value record
    IPropertyDTO marketPriceProp = DataTestUtils.createRandomProperty("Attribute",
        PropertyType.PRICE);
    IValueRecordDTO marketPriceDto = DataTestUtils.createPriceValueRecord(baseEntityDao,
        marketPriceProp);
    // Preparing data to delete the Tag record
    IPropertyDTO materialsProp = DataTestUtils.createRandomProperty("Tag", PropertyType.TAG);
    ITagsRecordDTO materialsDto = DataTestUtils.createTagRecord(baseEntityDao, materialsProp);
    baseEntityDao.createPropertyRecords(marketPriceDto, materialsDto);
    
    // delete value record
    baseEntityDao.deletePropertyRecords(marketPriceDto);
    IBaseEntityDTO afterDeleteValueRecord = DataTestUtils.loadPropertyRecord(baseEntityDao,
        marketPriceProp);
    ;
    printJSON("after value Record Deleted  : ", afterDeleteValueRecord);
    assert (afterDeleteValueRecord.getPropertyRecords()
        .isEmpty());
    
    // delete the Tag Record
    baseEntityDao.deletePropertyRecords(materialsDto);
    IBaseEntityDTO afterDeleteTagRecord = DataTestUtils.loadPropertyRecord(baseEntityDao,
        materialsProp);
    printJSON("after Tag Record Deleted : ", afterDeleteTagRecord);
    assert (afterDeleteTagRecord.getPropertyRecords()
        .isEmpty());
  }
  
  @Test
  public void createBaseEntityWithContextualObject() throws RDBMSException, CSFormatException
  {
    printTestTitle("createBaseEntityWithContextualObject");
    IBaseEntityDTO newEntity = DataTestUtils.createRandomBaseEntityWithName("Article#");
    ContextDTO newContext = ConfigTestUtils.createRandomValueContext();
    
    IBaseEntityDTO contextualBaseEntity = DataTestUtils.createRandomBaseEntityWithContextAndName("Article", newContext);
    contextualBaseEntity.getContextualObject().setContextStartTime(System.currentTimeMillis());
    contextualBaseEntity.getContextualObject().setContextEndTime(System.currentTimeMillis());
    Set<Long> linkedBaseEntityIIDs = contextualBaseEntity.getContextualObject()
        .getLinkedBaseEntityIIDs();
    linkedBaseEntityIIDs.add(newEntity.getBaseEntityIID());
    contextualBaseEntity.getContextualObject()
        .setLinkedBaseEntityIIDs(linkedBaseEntityIIDs.toArray(new Long[0]));
    baseEntityDao = localeCatalogDao.openBaseEntity(contextualBaseEntity);
    
    // to update the BaseEntity
    baseEntityDao.updatePropertyRecords();
    
    IBaseEntityDTO result = localeCatalogDao
        .getEntityByIID(contextualBaseEntity.getBaseEntityIID());
    baseEntityDao = localeCatalogDao.openBaseEntity(result);
    System.out.println(baseEntityDao.getBaseEntityDTO()
        .toPXON());
    assert (baseEntityDao.getContextualLinkedEntities()
        .size() == 1);
  }
  
  @Test
  public void updateBaseEntityWithTag() throws RDBMSException {
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(2011, "Colors", PropertyType.TAG);
    ITagsRecordDTO materialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    ITagDTO tagsValueDto = baseEntityDao.newTagDTO(-50, "green");
    ITagDTO tagsValueDto2 = baseEntityDao.newTagDTO(100, "red");
    materialDto.setTags(tagsValueDto, tagsValueDto2);
    //update with all new values
    IBaseEntityDTO resultDTO = baseEntityDao.updatePropertyRecords( materialDto);
    assert (resultDTO != null);
    assert (1 == resultDTO.getPropertyRecords()
        .size());
  }
  
  @Test
  public void isPropertyRecordDuplicate() throws RDBMSException
  {
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    ContextualDataDTO cxtDTO = new ContextualDataDTO(100200, "Color", 1549219138, 1549220138);
    cxtDTO.setAllowDuplicate(false);
    IPropertyDTO prop = ConfigurationDAO.instance().getPropertyByIID(2009);
    ContextDTO contextDTO = new ContextDTO();
    contextDTO.setCode("Color");
    ValueRecordDTO propertyDTO = (ValueRecordDTO) baseEntityDao.newValueRecordDTOBuilder(prop,
        "xyz").contextDTO(contextDTO).build();
    propertyDTO.setContextualData(cxtDTO);
    Boolean recordDuplicate = baseEntityDao.isPropertyRecordDuplicate(propertyDTO);
    assert (recordDuplicate.equals(true));
    
    cxtDTO = new ContextualDataDTO(100200, "Color",1549219138, 1549219138);
    propertyDTO.setContextualData(cxtDTO);
    recordDuplicate = baseEntityDao.isPropertyRecordDuplicate(propertyDTO);
    assert (recordDuplicate.equals(true));

    cxtDTO = new ContextualDataDTO(100200, "Color", 0, 0);
    ITagDTO tags[] = { new TagDTO("blue", 100), new TagDTO("silver", 50) };
    cxtDTO.setContextTagValues(tags);
    propertyDTO.setContextualData(cxtDTO);
    recordDuplicate = baseEntityDao.isPropertyRecordDuplicate(propertyDTO);
    assert (recordDuplicate.equals(true));
    
    cxtDTO = new ContextualDataDTO(100202, "Color", 1549219138, 1549219138);
    propertyDTO.setContextualData(cxtDTO);
    ITagDTO tags2[] = { new TagDTO("blue", 100), new TagDTO("silver", 50), new TagDTO("red", 100) };
    cxtDTO.setContextTagValues(tags2);
    recordDuplicate = baseEntityDao.isPropertyRecordDuplicate(propertyDTO);
    assert (recordDuplicate.equals(true));

  }

  @Test
  public void isBaseEntityRecordDuplicate() throws RDBMSException
  {
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100003);
    ContextualDataDTO cxtDTO = new ContextualDataDTO("Color");
    cxtDTO.setAllowDuplicate(false);

    ContextDTO contextDTO = new ContextDTO();
    contextDTO.setCode("Color");
    BaseEntityDTO yolo = DataTestUtils.createRandomBaseEntityWithContextAndName("yolo", contextDTO);
    yolo.setParent(100003, IBaseEntityIDDTO.EmbeddedType.CONTEXTUAL_CLASS);
    yolo.setTopParent(100003);

    IContextualDataDTO contextualObject = yolo.getContextualObject();

    //required as the baseEntityDTO generated sets context times to current timestamp.
    contextualObject.setContextStartTime(0l);
    contextualObject.setContextEndTime(0l);

    IBaseEntityDAO baseEntityDAO = localeCatalogDao.openBaseEntity(yolo);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(true));

    //check without anything but tags if duplication works
    ITagDTO tags[] = { new TagDTO("green", 30), new TagDTO("red", 100) };
    contextualObject.setContextTagValues(tags);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(true));

    //check if the duplication works with tags and timestamp(exact time stamp)
    contextualObject.setContextStartTime(1580755138);
    contextualObject.setContextEndTime(1582484138);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(true));

    //check if the duplication works with tags and timestamp(subset time stamp)
    contextualObject.setContextStartTime(1580927938);
    contextualObject.setContextEndTime(1582311338);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(true));

    //check if the duplication works with no tags and timestamp(overlap time stamp)
    contextualObject.setContextTagValues(new TagDTO[]{});
    contextualObject.setContextStartTime(1582655938);
    contextualObject.setContextEndTime(1583780138);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(true));

    //Negative Test Case. Check whether nonDuplicate field return duplicate as well.
    contextualObject.setContextTagValues(new TagDTO("green", 30), new TagDTO("red", 100));
    contextualObject.setContextStartTime(1582655938);
    contextualObject.setContextEndTime(1583780138);
    assert (baseEntityDAO.isEmbeddedEntityDuplicate(100003).equals(false));

  }
}
