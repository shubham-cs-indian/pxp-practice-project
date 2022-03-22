package com.cs.core.rdbms.entity.dto;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author vallee
 */
public class PXONSampleTests extends AbstractRDBMSDriverTests {
  
  private RelationsSetDTO buildOneRelationSet() throws RDBMSException
  {
    PropertyDTO property = ConfigTestUtils.createRandomRelationship();
    RelationsSetDTO relationSet = new RelationsSetDTO(300001, property, RelationSide.SIDE_1);
    relationSet.getRelations()
        .add(new EntityRelationDTO(20004));
    relationSet.getRelations()
        .add(new EntityRelationDTO(20005));
    return relationSet;
  }
  
  @Test
  public void pxonRelationsSet() throws CSFormatException, RDBMSException
  {
    printTestTitle("pxonRelationsSet");
    RelationsSetDTO relationSet = buildOneRelationSet();
    printJSON(relationSet);
    relationSet.fromPXON(relationSet.toPXON()); // reversibility test
    PropertyDTO property = ConfigTestUtils.createRandomRelationship();
    RelationsSetDTO relationSet2 = new RelationsSetDTO(300002, property, RelationSide.SIDE_1);
    EntityRelationDTO cxtRel = new EntityRelationDTO(2004, "Persona");
    cxtRel.getContextualObject().getContextTagValues().add(new TagDTO("Age20-35", 100));
    relationSet2.getRelations().add(cxtRel);
    printJSON(relationSet2);
    relationSet2.fromPXON(relationSet2.toPXON()); // reversibility test
  }
  
  private ValueRecordDTO buildOneValueRecord() throws RDBMSException
  {
    PropertyDTO property = ConfigTestUtils.createRandomTextProperty();
    ValueRecordDTO value = new ValueRecordDTO(20001, property, "My Value Content");
    value.setValueIID(34001);
    value.setLocaleID("en_US");
    
    return value;
  }
  
  @Test
  public void pxonValueRecord() throws CSFormatException, RDBMSException
  {
    printTestTitle("pxonValueRecord");
    ValueRecordDTO value = buildOneValueRecord();
    printJSON(value);
    value.fromPXON(value.toPXON());
    PropertyDTO property = ConfigTestUtils.createRandomTextProperty();
    ValueRecordDTO value2 = new ValueRecordDTO(20002, property, "25 cm");
    value2.setValueIID(34002);
    value2.setLocaleID("en_US");
    
    ContextualDataDTO cxtualVal2 = (ContextualDataDTO) value2.getContextualObject();
    cxtualVal2.setContextCode("Country");
    cxtualVal2.getContextTagValues()
        .add(new TagDTO("USA", 100));
    cxtualVal2.getContextTagValues()
        .add(new TagDTO("CANADA", 50));
    cxtualVal2.getContextTagValues()
        .add(new TagDTO("MEXICO", 0));
    value2.setAsNumber(25);
    value2.setUnitSymbol("cm");
    value2.addCalculation("=[PackageLength] - 10");
    printJSON(value2);
    value2.fromPXON(value2.toPXON()); // reversibility test
  }
  
  private TagsRecordDTO buildOneTagsRecord() throws CSFormatException, RDBMSException
  {
    PropertyDTO lov = ConfigTestUtils.createRandomTagProperty();
    TagsRecordDTO tags = new TagsRecordDTO(3000, lov);
    tags.addPredefinedCoupling(ICSECouplingSource.Predefined.$parent, lov, true);
    tags.addPredefinedCoupling(ICSECouplingSource.Predefined.$parent, lov, false);
    tags.getTags()
        .add(new TagDTO("RED", 0));
    tags.getTags()
        .add(new TagDTO("BLUE", 50));
    tags.getTags()
        .add(new TagDTO("WHITE", -50));
    return tags;
  }
  
  @Test
  public void pxonTagsRecord() throws CSFormatException, RDBMSException
  {
    printTestTitle("pxonTagsRecord");
    TagsRecordDTO tags = buildOneTagsRecord();
    printJSON(tags);
    tags.fromPXON(tags.toPXON()); // reversibility test
  }
  
  private void buildOneContextualData(ContextualDataDTO cxtual)
  {
    cxtual.setIID(1000);
    cxtual.setContextCode("Color");
    cxtual.setContextStartTime(System.currentTimeMillis());
    cxtual.setContextEndTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10));
    cxtual.getContextTagValues().add(new TagDTO("RED", 0));
    cxtual.getContextTagValues().add(new TagDTO("BLUE", 50));
    cxtual.getContextTagValues().add(new TagDTO("WHITE", -50));
  }
  
  @Test
  public void pxonContextualData() throws CSFormatException
  {
    printTestTitle("pxonContextualData");
    ContextualDataDTO cxtual = new ContextualDataDTO();
    buildOneContextualData(cxtual);
    Long[] entityIIDs = { 100001L, 100002L, 100003L };
    cxtual.getLinkedBaseEntityIIDs().addAll(Arrays.asList(entityIIDs));
    printJSON(cxtual);
    cxtual.fromPXON(cxtual.toPXON()); // reversibility test
  }
  
  @Test
  public void pxonBaseEntity() throws CSFormatException, RDBMSException
  {
    printTestTitle("pxonBaseEntity");
    ClassifierDTO natureClass = ConfigTestUtils.createRandomClass();
    BaseEntityDTO entity = new BaseEntityDTO("MYBaseEntityObject",
        IBaseEntityIDDTO.BaseType.ARTICLE, "en_US", new CatalogDTO("pim", IStandardConfig.STANDARD_ORGANIZATION_CODE), natureClass);
    entity.setIID(100055);
    entity.addPropertyRecord(buildOneValueRecord());
    entity.addPropertyRecord(buildOneTagsRecord());
    entity.addPropertyRecord(buildOneRelationSet());
    entity.setDefaultImageIID(50003256);
    //entity.setChildrenIIDs(IBaseEntityIDDTO.EmbeddedType.VERSION_CONTEXT, 100056L, 100057L, 100058L);
    printJSON(entity);
    entity.fromPXON(entity.toPXON()); // reversibility test
    BaseEntityDTO entity2 = new BaseEntityDTO("MYBaseEntityObject#1",
        IBaseEntityIDDTO.BaseType.ARTICLE, "en_US", new CatalogDTO("pim", IStandardConfig.STANDARD_ORGANIZATION_CODE), natureClass);
    entity2.setIID(100056);
    ContextualDataDTO contextualObject = (ContextualDataDTO) entity2.getContextualObject();
    buildOneContextualData(contextualObject);
    entity2.setSourceCatalogCode("onboarding");
    entity2.setChildLevel(2);
    entity2.setOriginBaseEntityIID(100057);
    printJSON(entity2);
    entity.fromPXON(entity2.toPXON()); // reversibility test
  }
  
  @Test
  public void pxonBaseEntityWithOrgnization() throws CSFormatException, RDBMSException
  {
    printTestTitle("pxonBaseEntityWithOrgnization");
    ClassifierDTO natureClass = ConfigTestUtils.createRandomClass();
    BaseEntityDTO entity = new BaseEntityDTO("MYBaseEntityObject",
        IBaseEntityIDDTO.BaseType.ARTICLE, "en_US", new CatalogDTO("pim", "Organization_PXP"), natureClass);
    entity.setIID(100055);
    entity.addPropertyRecord(buildOneValueRecord());
    entity.addPropertyRecord(buildOneTagsRecord());
    entity.addPropertyRecord(buildOneRelationSet());
    entity.setDefaultImageIID(50003256);
    /*entity.setChildrenIIDs(IBaseEntityIDDTO.EmbeddedType.VERSION_CONTEXT, 100056L, 100057L,
        100058L);*/
    printJSON(entity);
    entity.fromPXON(entity.toPXON());
    printJSON(entity);
    assert(entity.getCatalog().getOrganizationCode().equals("Organization_PXP"));
  }
}