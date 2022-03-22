package com.cs.core.bgprocess.services.di;

import org.junit.Before;
import org.junit.Test;

import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class PXONImportTranslationsTest extends AbstractPXONImporterTest{
  
  public static final String NFS_PATH = "nfs.file.path";
  
  public static final String IMPORT_ATTRIBUTE_TRANSLATION = "[{\"csid\":\"[N>createdbyattribute]\",\"Jlanguagetranslation\":"
      + "{\"en_US\":{\"tooltip\":\"Tooltip_createdbyattribute_en_US1\",\"description\":\"Description_createdbyattribute_en_US1\","
      + "\"label\":\"Imp_createdbyattribute_en_US1\",\"placeholder\":\"Placeholder_createdbyattribute_en_US1\"},\"es_ES\":"
      + "{\"tooltip\":\"Tooltip_createdbyattribute_es_ES1\",\"description\":\"Description_createdbyattribute_es_ES1\",\"label\":"
      + "\"Imp_createdbyattribute_es_ES1\",\"placeholder\":\"Placeholder_createdbyattribute_es_ES1\"}},\"$type\":\"attribute\"}]";
  
  public static final String IMPORT_TAG_TRANSLATION = "[{\"csid\":\"[N>statustag]\",\"Jlanguagetranslation\":{\"en_US\":{\"tooltip\":"
      + "null,\"description\":\"description\",\"label\":\"statustag1\"},\"es_ES\":{\"tooltip\":\"tooltip ES\","
      + "\"description\":null,\"label\":\"statustag2\"}},\"$type\":\"tag\"}]";
  
  public static final String IMPORT_TAG_VALUE_TRANSLATION = "[{\"csid\":\"[N>enrichmenttag]\",\"Jlanguagetranslation\":{\"en_US\":"
      + "{\"label\":\"enrichmenttag1\"},\"es_ES\":{\"label\":\"enrichmenttag2\"}},\"$type\":\"tag\"}]";
  
  public static final String IMPORT_TASK_TRANSLATION = "[{\"csid\":\"[N>R_Task]\",\"Jlanguagetranslation\":{\"en_US\":"
      + "{\"label\":\"R_Task_US\"},\"es_ES\":{\"label\":\"R_Task_ES\"}},\"$type\":\"task\"}]";
  
  private static final String IMPORT_TASK = "[{\"prioritytag\":\"availabilitytag\",\"color\":\"#A07400\",\"csid\":\"[K>R_Task $type=PUBLIC]\","
      + "\"icon\":\"\",\"$statustag\":\"taskstatustag\",\"label\":\"R_Task\"}]";
  
  private static final String IMPORT_TAB_TRANSLATION = "[{\"csid\":\"[N>task_Tab]\",\"Jlanguagetranslation\":{\"en_US\":{\"label\":"
      + "\"task_tab_US\"},\"es_ES\":{\"label\":\"task_tab_ES\"}},\"$type\":\"tab\"}]";
  
  private static final String IMPORT_DASHBOARD_TAB_TRANSLATION = "[{\"csid\":\"[N>dataIntegrationTab]\",\"Jlanguagetranslation\":"
      + "{\"en_US\":{\"label\":\"DefaultDataIntegrationTab_en_US\"},\"fr_FR\":{\"label\":\"DefaultDataIntegrationTab__fr_ES\"}},"
      + "\"$type\":\"dashboardTab\"}]";
  //TODO: PXPFDEV-21454: Deprecate Virtual Catalog 
  private static final String IMPORT_CLASSES_TRANSLATION = "[{\"csid\":\"[N>single_article]\",\"Jlanguagetranslation\":{\"en_US\":"
      + "{\"label\":\"Single Article_en_US\"}},\"$type\":\"article\"},{\"csid\":\"[N>image_asset]\",\"Jlanguagetranslation\":"
      + "{\"en_US\":{\"label\":\"Image_en_US\"}},\"$type\":\"asset\"},{\"csid\":\"[N>market]\",\"Jlanguagetranslation\":{\"en_US\":"
      + "{\"label\":\"Market_en_US\"}},\"$type\":\"target\"},{\"csid\":\"[N>text_asset]\",\"Jlanguagetranslation\":{\"en_US\":"
      + "{\"label\":\"TextAsset_en_US\"}},\"$type\":\"textAsset\"},{\"csid\":\"[N>virtual_catalog]\",\"Jlanguagetranslation\":"
      + "{\"en_US\":{\"label\":\"VirtualCatalog_en_US\"}},\"$type\":\"virtualCatalog\"}]";
  
  private static final String IMPORT_RELATIONSHIP_TRANSLATION = "[{\"csid\":\"[N>standardArticleAssetRelationship]\","
      + "\"Jlanguagetranslation\":{\"en_US\":{\"side2Label\":\"Images English\",\"label\":\"Article-Asset Relationship English\","
      + "\"side1Label\":\"Articles English\"}},\"$type\":\"relationship\"}]";
  
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void imortAttributeTranslation() throws Exception {
    importTranslations("imortAttributeTranslation", IMPORT_ATTRIBUTE_TRANSLATION);
  }
  
  @Test
  public void importTagTranslation() throws Exception {
    importTranslations("importTagTranslation", IMPORT_TAG_TRANSLATION);
  }
  
  @Test
  public void importTagValueTranslation() throws Exception {
    importTranslations("importTagValueTranslation", IMPORT_TAG_VALUE_TRANSLATION);
  }
  
  @Test
  public void importTaskTranslation() throws Exception {
    importTranslations("importTask", IMPORT_TASK);
    importTranslations("importTaskTranslation", IMPORT_TASK_TRANSLATION);
  }
  
  @Test
  public void importTabTranslation() throws Exception {
    importTranslations("importTabTranslation", IMPORT_TAB_TRANSLATION);
  }
  
  @Test
  public void importDashboardTabTranslation() throws Exception {
    importTranslations("importDashboardTabTranslation", IMPORT_DASHBOARD_TAB_TRANSLATION);
  }
  
  @Test
  public void importClassesTranslation() throws Exception {
    importTranslations("importClassesTranslation", IMPORT_CLASSES_TRANSLATION);
  }
  
  @Test
  public void importRelationshipTranslation() throws Exception {
    importTranslations("importClassesTranslation", IMPORT_RELATIONSHIP_TRANSLATION);
  }
  
  private void importTranslations(String testName, String importData) throws CSInitializationException, Exception
  {
    System.out.println("Test "+testName);
    String directory = CSProperties.instance().getString(NFS_PATH);
    String fileName = prepareImportData(directory, importData);
    submitImportProcess(fileName);
  }
  
  
}
