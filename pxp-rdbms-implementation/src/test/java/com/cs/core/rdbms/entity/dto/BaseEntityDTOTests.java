package com.cs.core.rdbms.entity.dto;

import java.util.Date;

import org.junit.Test;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;

/**
 * @author farooq.kadri
 */
// @Ignore
public class BaseEntityDTOTests extends AbstractRDBMSDriverTests {
  
  // Variable for ExtensionDTO
  public static final String           EXTENSION      = "{\"myExtension\":\"the content of extension\", \"nb\":75343}";
  // Variables for USER DTO
  static final String                  USER_NAME      = "Xavier \"del\" Y'Antra";
  static final String                  USER_ID        = "12345";
  static final long                    USER_IID       = 12345;
  // Variables for LOCALE DTO
  static final String                  LOCALE_ID      = "en_US";
  
  // Variables for CATALOG DTO
  static final String                  CATALOG_ID     = "SAP-INBOUND";
  static final String                  NAME           = "";
  static final String                  SOURCE_CATALOG = "sourceCatalog";
  
  static final String                  OBJECT_ID      = "SAMPLE-ARTICLE";
  static final String                  OBJECT_NAME    = "Sample DTO example";
  
  public BaseEntityDTOTests()
  {
  }
  
  @Test
  public void test() throws CSFormatException
  {
    long WHEN = (new Date()).getTime();
    // long later = now + 3000;
    SimpleTrackingDTO lastModifiedTrack = new SimpleTrackingDTO(USER_NAME, WHEN);
    SimpleTrackingDTO createdTrack = new SimpleTrackingDTO(USER_NAME, WHEN);
    ClassifierDTO natureClass = new ClassifierDTO(4001, "Accessory",
        IClassifierDTO.ClassifierType.CLASS);
    
    BaseEntityIDDTO sample = new BaseEntityIDDTO( OBJECT_ID,
        IBaseEntityIDDTO.BaseType.ARTICLE, LOCALE_ID, new CatalogDTO(CATALOG_ID, IStandardConfig.STANDARD_ORGANIZATION_CODE), natureClass);
    sample.setIID(123456);
    ContextualDataDTO contextualObject = (ContextualDataDTO) sample.getContextualObject();
    contextualObject.setIID(123456);
    contextualObject.setContextCode("Country");
    contextualObject.setContextStartTime(54321);
    contextualObject.setContextEndTime(65432);
    
    
    String json = sample.toPXON();
    System.out.println("Sample: " + json);
    IBaseEntityIDDTO sampleRev = new BaseEntityIDDTO();
    sampleRev.fromPXON(json);
    assert (sampleRev.getBaseLocaleID()
        .equals(LOCALE_ID));
    String jsonRev = sampleRev.toPXON();
    System.out.println(jsonRev);
    // assertEquals(jsonRev, json); - requires additional tests but the 2
    // strings cannot be exactly
    // the same due to the JSON extesnion inside
    
    BaseEntityDTO sample2 = new BaseEntityDTO(sample, OBJECT_NAME);
    sample2.setLastModifiedTrack(lastModifiedTrack);
    sample2.setCreatedTrack(createdTrack);
    sample2.setSourceCatalogCode("onboarding");
    sample2.setEntityExtension(EXTENSION);
    String json2 = sample2.toPXON();
    System.out.println("Sample2: " + json2);
    sample2.fromPXON(json2);
  }
}
