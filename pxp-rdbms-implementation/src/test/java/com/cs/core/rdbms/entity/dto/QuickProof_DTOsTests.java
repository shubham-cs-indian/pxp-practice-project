package com.cs.core.rdbms.entity.dto;

import org.junit.Test;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.rdbms.tracking.dto.TrackingDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO.TrackingEvent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Stress test behavior of a simple DTO
 *
 * @author vallee
 */
// @Ignore
public class QuickProof_DTOsTests extends AbstractRDBMSDriverTests {
  
  @Test
  public void test() throws CSFormatException, RDBMSException
  {
    UserSessionDTO dtoUs = new UserSessionDTO();
    dtoUs.setRemarks("a stupid inclusion of \"quotes\" and 'quotes'");
    printJSON(dtoUs);
    
    UserDTO dtoU = new UserDTO();
    printJSON(dtoU);
    dtoU.setIID(3000);
    printJSON(dtoU);
    
    CatalogDTO dtoC = new CatalogDTO();
    printJSON(dtoC);
    
    ClassifierDTO dtoCl = new ClassifierDTO();
    dtoCl.fromPXON(""); // check the behavior when loading an empty string
    printJSON(dtoCl);
    dtoCl.fromPXON("{\"csid\":\"[c>TV-Sets]\"}");
    printJSON(dtoCl);
    
    ContextDTO dtoCo = new ContextDTO("Packaging", ContextType.EMBEDDED_VARIANT);
    printJSON(dtoCo);
    
    PropertyDTO dtoP = new PropertyDTO(6000, "ShortDescription", PropertyType.TEXT);
    printJSON(dtoP);
    
    TrackingDTO doTrC = new TrackingDTO(dtoU, System.currentTimeMillis(), TrackingEvent.MODIFY);
    doTrC.setIID(23);
    printJSON(doTrC);
    
    TagDTO dtoTR1 = new TagDTO("TAG-1", 10);
    TagDTO dtoTR2 = new TagDTO("TAG-2", 20);
    TagsRecordDTO dtoTR = new TagsRecordDTO();
    dtoTR.setTags(dtoTR1, dtoTR2);
    dtoTR.fromPXON(dtoTR.toPXON());
    printJSON("dtoR", dtoTR);
    
    BaseEntityIDDTO dtoBe = new BaseEntityIDDTO("PC#SCREEN#123", IBaseEntityIDDTO.BaseType.ARTICLE, "en_US",
        new CatalogDTO(dtoC.getCatalogCode(), IStandardConfig.STANDARD_ORGANIZATION_CODE), ConfigTestUtils.createRandomClass());
    
    dtoBe.setIID(100001);
    ContextualDataDTO contextualObject = (ContextualDataDTO) dtoBe.getContextualObject();
    contextualObject.setIID(100001);
    contextualObject.setContextCode(dtoCo.getCode());
    printJSON("dtoBe", dtoBe);
    
    BaseEntityDTO dtoFBe0 = new BaseEntityDTO(dtoBe, "PC Screen Dell 123");
    dtoFBe0.setCreatedTrack(new SimpleTrackingDTO(doTrC.getUserName(), doTrC.getWhen()));
    dtoFBe0.setLastModifiedTrack(new SimpleTrackingDTO("A. B", System.currentTimeMillis()));
    dtoFBe0.setEntityExtension("{\"coupling\":[\"a\",\"b\",\"c\"]}");
    dtoFBe0.setSourceCatalogCode(dtoC.getCatalogCode());
    printJSON("dtoFBe0", dtoFBe0);
    
    ClassifierDTO dtoCl11 = new ClassifierDTO(111, "C111", IClassifierDTO.ClassifierType.TAXONOMY);
    ClassifierDTO dtoC222 = new ClassifierDTO(222, "C222", IClassifierDTO.ClassifierType.TAXONOMY);
    ClassifierDTO dtoC333 = new ClassifierDTO(333, "C333", IClassifierDTO.ClassifierType.TAXONOMY);
    dtoFBe0.setOtherClassifierIIDs(dtoCl11, dtoC222, dtoC333);
    dtoFBe0.fromPXON(dtoFBe0.toPXON()); // round-trip test
    printJSON("dtoFBe0 with classifiers", dtoFBe0);
    
    BaseEntityIDDTO child1 = new BaseEntityIDDTO();
    child1.setBaseEntityID("C001");
    child1.setIID(8000001L);
    BaseEntityIDDTO child2 = new BaseEntityIDDTO();
    child2.setBaseEntityID("C002");
    child2.setIID(8000002L);
    
    dtoFBe0.setChildren(EmbeddedType.CONTEXTUAL_CLASS, child1, child2);
    dtoFBe0.fromPXON(dtoFBe0.toPXON()); // round-trip test
    printJSON("dtoFBe0 with children", dtoFBe0);
    
    PropertyDTO relationship = new PropertyDTO(7000, "Cross-sell", PropertyType.RELATIONSHIP);
    EntityRelationDTO dtoER = new EntityRelationDTO(100001);
    dtoER.fromJSON(dtoER.toJSON()); // round-trip test
    printJSON(dtoER);
    
    RelationsSetDTO dtoERS = new RelationsSetDTO(2000345L, relationship,
        IPropertyDTO.RelationSide.SIDE_1);
    dtoERS.fromPXON(dtoERS.toPXON()); // round-trip test
    printJSON(dtoERS);
  }
}
