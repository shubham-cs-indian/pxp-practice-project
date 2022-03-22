package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.EntityRelationDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class RelationsContextualRecordTest extends AbstractRDBMSDriverTests {
  
  private IBaseEntityDAO curEntityDao;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void createRelationsSideContextualValues() throws RDBMSException, CSFormatException
  {
    
    printTestTitle("createRelationsSideContextualValues");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100020);
    IContextDTO context = ConfigTestUtils.createRandomValueContext();
    IPropertyDTO relation1 = curEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IPropertyDTO relation2 = curEntityDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO[] values = {
        createNewContextualRelationSet(relation1, context, System.currentTimeMillis(),
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)),
        createNewContextualRelationSet(relation2, context, System.currentTimeMillis(),
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)) };
    
    curEntityDao.createPropertyRecords(values);
    
    printJSON("After property creation", curEntityDao.getBaseEntityDTO());
    relation1.setRelationSide(RelationSide.SIDE_1);
    relation2.setRelationSide(RelationSide.SIDE_1);
    curEntityDao.loadPropertyRecords(relation1);
    
    printJSON("After property reloading", curEntityDao.getBaseEntityDTO());
  }
  
  @Test
  public void updateContextualValues() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateContextualValues");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    IContextDTO context = new ContextDTO("Country", ContextType.ATTRIBUTE_CONTEXT);
    IPropertyDTO relation = curEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO[] values = { createNewContextualRelationSet(relation, null,
        System.currentTimeMillis(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) };
    curEntityDao.createPropertyRecords(values);
    
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO entity = curEntityDao.loadPropertyRecords(relation);
    IRelationsSetDTO relationEntity = (IRelationsSetDTO) entity
        .getPropertyRecord(relation.getPropertyIID());
    for (IEntityRelationDTO entityRelationDTO : relationEntity.getRelations()) {
      if (entityRelationDTO.getContextualObject()
          .isNull()) {
        entityRelationDTO.getContextualObject()
            .setContextStartTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
        entityRelationDTO.getContextualObject()
            .setContextEndTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
      }
    }
    
    curEntityDao.updatePropertyRecords(relationEntity);
    printJSON("Updated with context", curEntityDao.getBaseEntityDTO());
  }
  
  private IRelationsSetDTO createNewContextualRelationSet(IPropertyDTO property,
      IContextDTO context, long startTime, long endTime) throws RDBMSException, CSFormatException
  {
    IRelationsSetDTO newEntityRelationsSetDTO = curEntityDao.newEntityRelationsSetDTOBuilder(property,
        RelationSide.SIDE_1).build();
    IBaseEntityDTO newEntity = DataTestUtils.newBaseEntity("ContextualTest", true);
    localeCatalogDao.openBaseEntity(newEntity)
        .createPropertyRecords();
    IEntityRelationDTO entityRelationDTO = new EntityRelationDTO(newEntity.getBaseEntityIID(),
        context != null ? context.getContextCode() : "");
    entityRelationDTO.getContextualObject()
        .setContextEndTime(endTime);
    entityRelationDTO.getContextualObject()
        .setContextStartTime(startTime);
    int key = (new Random()).nextInt(100000);
    IPropertyDTO materialsProp = localeCatalogDao.newPropertyDTO(0L, "Mat#" + key,
        PropertyType.TAG);
    ITagDTO tagRecordX = curEntityDao.newTagDTO(25, "PCBx#" + key);
    
    entityRelationDTO.getContextualObject()
        .setContextTagValues(tagRecordX);
    
    newEntityRelationsSetDTO.getRelations()
        .add(entityRelationDTO);
    return newEntityRelationsSetDTO;
  }
  
  @Test
  public void loadContextualValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadContextualValue");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100020);
    IPropertyDTO relationship = curEntityDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.RELATIONSHIP);
    relationship.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO entity = curEntityDao.loadPropertyRecords(relationship);
    
    printJSON("Entity with contextual property", entity);
    Set<IEntityRelationDTO> relations = ((IRelationsSetDTO) (entity.getPropertyRecords()
        .iterator()
        .next())).getRelations();
    int contextualRelationsCount = 0;
    for (IEntityRelationDTO relation : relations) {
      contextualRelationsCount++;
    }
    assertTrue(contextualRelationsCount > 0);
  }
}
