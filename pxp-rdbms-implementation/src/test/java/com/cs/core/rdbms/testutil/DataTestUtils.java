package com.cs.core.rdbms.testutil;

import static com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests.localeCatalogDao;

import java.util.Random;
import java.util.UUID;

import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Quick access to data objects
 *
 * @author vallee
 */
public class DataTestUtils {
  
  public static BaseEntityDTO getBaseEntityDTO(long entityIID) throws RDBMSException
  {
    return (BaseEntityDTO) localeCatalogDao
        .newBaseEntityDTOBuilder("B07CVL2D2S", IBaseEntityIDDTO.BaseType.ARTICLE,
            localeCatalogDao.newClassifierDTO(4000, "Article", IClassifierDTO.ClassifierType.CLASS))
        .build();
  }
  
  public static BaseEntityDAO openBaseEntityDAO(long entityIID) throws RDBMSException
  {
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.getEntityByIID(entityIID);
    if (entity == null)
      return null;
    return (BaseEntityDAO) localeCatalogDao.openBaseEntity(entity);
  }
  
  public static BaseEntityDTO newBaseEntity(String idPrefix, boolean createInDB)
      throws RDBMSException
  {
    String baseEntityID = idPrefix + "#" + (new Random()).nextInt(10000);
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.newBaseEntityDTOBuilder(baseEntityID,
        IBaseEntityIDDTO.BaseType.ARTICLE,
        localeCatalogDao.newClassifierDTO(4000, "Article", IClassifierDTO.ClassifierType.CLASS)
      ).build();
    entity.setHashCode(UUID.randomUUID()
        .toString());
    if (createInDB) {
      IBaseEntityDAO openBaseEntity = AbstractRDBMSDriverTests.localeCatalogDao
          .openBaseEntity(entity);
      IPropertyDTO nameProperty = AbstractRDBMSDriverTests.localeCatalogDao.newPropertyDTO(200,
          "nameattribute", IPropertyDTO.PropertyType.TEXT);
      IValueRecordDTO articleName = openBaseEntity.newValueRecordDTOBuilder(nameProperty, baseEntityID+"name")
          .localeID(localeCatalogDao.getLocaleCatalogDTO()
              .getLocaleID())
          .build();
      
      return (BaseEntityDTO) localeCatalogDao.openBaseEntity(entity)
          .createPropertyRecords(articleName);
    }
    return new BaseEntityDTO(entity);
  }
  
  public static BaseEntityDTO createRandomBaseEntityWithName(String idPrefix) throws RDBMSException
  {
    String baseEntityID = idPrefix + (new Random()).nextInt(10000);
    BaseEntityIDDTO entity = (BaseEntityIDDTO) AbstractRDBMSDriverTests.localeCatalogDao
        .newBaseEntityDTOBuilder(baseEntityID, IBaseEntityIDDTO.BaseType.ARTICLE,
            AbstractRDBMSDriverTests.localeCatalogDao.newClassifierDTO(4000, "Article",
                IClassifierDTO.ClassifierType.CLASS)).build();
    IBaseEntityDAO openBaseEntity = AbstractRDBMSDriverTests.localeCatalogDao
        .openBaseEntity(entity);
    IBaseEntityDTO createPropertyRecords = createNamePropertyRecord(baseEntityID, openBaseEntity);
    return (BaseEntityDTO) createPropertyRecords;
  }
  
  private static IBaseEntityDTO createNamePropertyRecord(String baseEntityID,
      IBaseEntityDAO openBaseEntity) throws RDBMSException
  {
    IPropertyDTO childArticleNameProp = AbstractRDBMSDriverTests.localeCatalogDao
        .newPropertyDTO(200, "nameattribute", PropertyType.TEXT);
    IValueRecordDTO articleNameDto = openBaseEntity
        .newValueRecordDTOBuilder(childArticleNameProp, "")
        .localeID(localeCatalogDao.getLocaleCatalogDTO().getLocaleID())
        .build();
    
    IBaseEntityDTO createPropertyRecords = openBaseEntity.createPropertyRecords(articleNameDto);
    return createPropertyRecords;
  }
  
  public static IPropertyDTO createRandomProperty(String idPrefix, PropertyType type)
      throws RDBMSException
  {
    String propID = idPrefix + "#" + (new Random()).nextInt(10000);
    return AbstractRDBMSDriverTests.localeCatalogDao.newPropertyDTO(0L, propID, type);
  }
  
  public static IValueRecordDTO createPriceValueRecord(IBaseEntityDAO baseEntityDao,
      IPropertyDTO priceProp) throws RDBMSException, CSFormatException
  {
    IValueRecordDTO marketPriceDto = baseEntityDao.newValueRecordDTOBuilder(priceProp, "100 €")
        .localeID("fr_FR")
        .build();
    
    marketPriceDto.setAsNumber(100);
    marketPriceDto.setUnitSymbol("€");
    return marketPriceDto;
  }
  
  public static ITagsRecordDTO createTagRecord(IBaseEntityDAO baseEntityDao,
      IPropertyDTO materialsProp) throws RDBMSException, CSFormatException
  {
    ITagsRecordDTO materialDto = baseEntityDao.newTagsRecordDTOBuilder(materialsProp).build();
    ConfigTestUtils.createTagValue("PCBx#" + materialsProp.getIID(), materialsProp.getIID());
    ConfigTestUtils.createTagValue("PCBy" + materialsProp.getIID(), materialsProp.getIID());
    ITagDTO tagRecordX = baseEntityDao.newTagDTO(25, "PCBx#");
    ITagDTO tagRecordY = baseEntityDao.newTagDTO(25, "PCBy");
    materialDto.setTags(tagRecordX, tagRecordY);
    return materialDto;
  }
  
  public static BaseEntityDTO createRandomBaseEntityWithContextAndName(String idPrefix,
      ContextDTO context) throws RDBMSException
  {
    String baseEntityID = idPrefix + (new Random()).nextInt(10000);
    BaseEntityIDDTO entity = (BaseEntityIDDTO) AbstractRDBMSDriverTests.localeCatalogDao
        .newBaseEntityDTOBuilder(baseEntityID, IBaseEntityIDDTO.BaseType.ARTICLE,
            AbstractRDBMSDriverTests.localeCatalogDao.newClassifierDTO(4000, "Article",
                IClassifierDTO.ClassifierType.CLASS))
        .contextDTO(context)
        .build();
    entity.getContextualObject().setContextStartTime(System.currentTimeMillis());
    entity.getContextualObject().setContextEndTime(System.currentTimeMillis());
    IBaseEntityDAO openBaseEntity = AbstractRDBMSDriverTests.localeCatalogDao
        .openBaseEntity(entity);
    IBaseEntityDTO createPropertyRecords = createNamePropertyRecord(baseEntityID, openBaseEntity);
    return (BaseEntityDTO) createPropertyRecords;
  }
  
  public static IBaseEntityDTO loadPropertyRecord(IBaseEntityDAO baseEntityDao,
      IPropertyDTO propertyDTO) throws RDBMSException, CSFormatException
  {
    IBaseEntityDTO record = baseEntityDao.loadPropertyRecords(propertyDTO);
    return record;
  }
  
  public static IValueRecordDTO createRandomValueRecordText(IBaseEntityDAO entityDAO)
      throws RDBMSException
  {
    IPropertyDTO modelProp = DataTestUtils.createRandomProperty("MODEL",
        IPropertyDTO.PropertyType.TEXT);
    return entityDAO.newValueRecordDTOBuilder(modelProp, "Random Model Value")
        .localeID(localeCatalogDao.getLocaleCatalogDTO().getLocaleID())
        .build();
  }
  
  public static ITagsRecordDTO createRandomTagRecord(IBaseEntityDAO baseEntityDao)
      throws RDBMSException, CSFormatException
  {
    return createTagRecord(baseEntityDao,
        DataTestUtils.createRandomProperty("TAG", IPropertyDTO.PropertyType.TAG));
  }
  
  public static IRelationsSetDTO createRelationsSetRecord(IBaseEntityDAO baseEntityDao)
      throws RDBMSException
  {
    IRelationsSetDTO relationsSetDTO = baseEntityDao.newEntityRelationsSetDTOBuilder(
        DataTestUtils.createRandomProperty("Relation", PropertyType.RELATIONSHIP),
        RelationSide.SIDE_1).build();
    relationsSetDTO.setRelations(100010L, 100013L, 100014L);
    return relationsSetDTO;
  }
  
  public static IValueRecordDTO createInheritingValueRecord(IBaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws CSFormatException
  {
    IValueRecordDTO valueRecordDTO = childEntityDAO.newValueRecordDTOBuilder(property, "")
        .localeID("en_US")
        .build();
    valueRecordDTO.addInheritanceCoupling(property, dynamic);
    return valueRecordDTO;
  }
  
  public static ITagsRecordDTO createInheritingTagRecord(IBaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws CSFormatException
  {
    ITagsRecordDTO tagsRecord = childEntityDAO.newTagsRecordDTOBuilder(property).build();
    tagsRecord.addInheritanceCoupling(property, dynamic);
    return tagsRecord;
  }
  
  public static IRelationsSetDTO createInheritingRelationsSetRecord(IBaseEntityDAO childEntityDAO,
      IPropertyDTO property, boolean dynamic) throws CSFormatException
  {
    IRelationsSetDTO relationsSetRecord = childEntityDAO.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    relationsSetRecord.addInheritanceCoupling(property, dynamic);
    return relationsSetRecord;
  }
  
  public static IValueRecordDTO createRandomLangDepValueRecord(IBaseEntityDAO entityDAO, String localeID)
      throws RDBMSException
  {
    IPropertyDTO langProp = DataTestUtils.createRandomProperty("LANG",
        IPropertyDTO.PropertyType.HTML);
    return entityDAO.newValueRecordDTOBuilder(langProp, "language dependant Value"+localeID)
        .localeID(localeID)
        .build();
  }
}
