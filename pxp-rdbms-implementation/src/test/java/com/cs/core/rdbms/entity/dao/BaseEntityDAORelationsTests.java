package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
// @Ignore
public class BaseEntityDAORelationsTests extends AbstractRDBMSDriverTests {
  
  IBaseEntityDAO baseEntityDao = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void loadRelations() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadRelations");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100006);
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.REFERENCE);
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO loadedEntity = baseEntityDao.loadPropertyRecords(relation);
    printJSON("loadedRelations", loadedEntity);
    assert (baseEntityDao.getBaseEntityDTO()
        .getPropertyRecords()
        .size() >= 1);
  }
  
  @Test
  public void createRelations() throws RDBMSException, CSFormatException
  {
    printTestTitle("createRelations");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100006);
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO set = baseEntityDao.newEntityRelationsSetDTOBuilder(relation,
        IPropertyDTO.RelationSide.SIDE_1).build();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity2 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity3 = DataTestUtils.newBaseEntity("FAM", true);
    set.setRelations(entity1.getIID(), entity2.getIID(), entity3.getIID());
    baseEntityDao.createPropertyRecords(set);
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO loadedEntity = DataTestUtils.loadPropertyRecord(baseEntityDao, relation);
    printJSON("after create", loadedEntity);
  }
  
  @Test
  public void updateRelations() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateRelations");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100006);
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7003, "Similar-items",
        IPropertyDTO.PropertyType.REFERENCE);
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO loadedEntity = baseEntityDao.loadPropertyRecords(relation);
    printJSON("before update", loadedEntity);
    
    // added new Entity
    IRelationsSetDTO relationsSet = (IRelationsSetDTO) loadedEntity.getPropertyRecord(7003);
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    BaseEntityDTO entity2 = DataTestUtils.newBaseEntity("FAM", true);
    relationsSet.addRelations(entity1.getBaseEntityIID(), entity2.getBaseEntityIID());
    baseEntityDao.updatePropertyRecords(relationsSet);
    printJSON("after update", baseEntityDao.loadPropertyRecords(relation));
    
    // removed new RelationSet
    relationsSet.removeRelations(entity1.getBaseEntityIID());
    baseEntityDao.updatePropertyRecords(relationsSet);
    printJSON("after remove RelationEntity", baseEntityDao.loadPropertyRecords(relation));
  }
  
  @Test
  public void deleteRelationRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("deleteRelationSets");
    // First create a relation and then remove Relation
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100006);
    IPropertyDTO relation = baseEntityDao.newPropertyDTO(7002, "Family-product",
        IPropertyDTO.PropertyType.REFERENCE);
    IRelationsSetDTO set = baseEntityDao.newEntityRelationsSetDTOBuilder(relation,
        IPropertyDTO.RelationSide.SIDE_1).build();
    BaseEntityDTO entity1 = DataTestUtils.newBaseEntity("FAM", true);
    set.setRelations(entity1.getIID());
    baseEntityDao.createPropertyRecords(set);
    relation.setRelationSide(RelationSide.SIDE_1);
    IBaseEntityDTO loadedEntity = baseEntityDao.loadPropertyRecords(relation);
    IRelationsSetDTO relationsSet = (IRelationsSetDTO) loadedEntity.getPropertyRecord(7002);
    printJSON("before deleting", relationsSet);
    
    // To delete the record which has been created
    baseEntityDao.deletePropertyRecords(relationsSet);
    IBaseEntityDTO afterDeleteloadedEntity = baseEntityDao.loadPropertyRecords(relation);
    IRelationsSetDTO afterDeleterelationsSet = (IRelationsSetDTO) afterDeleteloadedEntity
        .getPropertyRecord(7002);
    assert (afterDeleterelationsSet == null);
  }
}
