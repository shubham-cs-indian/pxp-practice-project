package com.cs.core.bgprocess.services.di;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import static org.junit.Assert.assertNotNull;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.dataintegration.dto.PXONExportScopeDTO;
import com.cs.core.dataintegration.idto.IPXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO.ItemType;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement.ExportFormat;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class PXONExporterTest extends AbstractBGProcessTests {

  private static final String SERVICE    = "PXON_EXPORT";
  private static final int    NB_BATCHES = 20;

  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private static final String EXPORT_SCOPE_FOR_ATTRIBUTES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\","
          + "\"localeInheritance\":[\"en_US\"],\"allEntities\":false,\"includeEmbeddedEntities\":false,"
          + "\"itemTypes\":[\"PROPERTY\"],\"propertyTypes\":[\"CALCULATED\", \"CONCATENATED\", "
          + "\"DATE\", \"MEASUREMENT\", \"HTML\", \"NUMBER\", \"PRICE\", "
          + "\"TEXT\", \"ASSET_ATTRIBUTE\"],\"PROPERTY\":{\"itemIDs\":[]}}";

  private static final String EXPORT_SCOPE_FOR_TAGS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"PROPERTY\"],\"propertyTypes\":[\"TAG\", \"BOOLEAN\"]}";

  private static final String EXPORT_SCOPE_FOR_RELATIONSHIPS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"PROPERTY\"],\"propertyTypes\":[\"RELATIONSHIP\"]}";

  private static final String EXPORT_SCOPE_FOR_PROPERTIES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"PROPERTY\"]}";

  private static final String EXPORT_SCOPE_FOR_PROPERTY_COLLECTIONS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"PROPERTY_COLLECTION\"]}";

  private static final String EXPORT_SCOPE_FOR_CONTEXTS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"CONTEXT\"]}";

  private static final String EXPORT_SCOPE_FOR_CLASSIFIER =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"MASTER_TAXONOMY\", \"CLASSIFIER\",\"HIERARCHY_TAXONOMY\"]}";

  private static final String EXPORT_SCOPE_FOR_ALL_BASE_ENTITIES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"catalog\":{\"csid\":\"[C>pim $locale=en_US $type=DI]\"},"
          + "\"localeInheritance\":[\"en_US\"],\"allEntities\":true,\"includeEmbeddedEntities\":true}";

  private static final String EXPORT_SCOPE_FOR_BASE_ENTITY_BY_IID =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"catalog\":{\"csid\":\"[C>pim $locale=en_US $type=DI]\"},"
          + "\"localeInheritance\":[\"en_US\"],\"allEntities\":false,\"includeEmbeddedEntities\":true,"
          + "\"baseEntityIIDs\": [100003,100004]}";

  private static final String EXPORT_SCOPE_FOR_BASE_ENTITY_BY_ID =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"catalog\":{\"csid\":\"[C>pim $locale=en_US $type=DI]\"},"
          + "\"localeInheritance\":[\"en_US\"],\"allEntities\":false,\"includeEmbeddedEntities\":true,"
          + "\"baseEntityIDs\": [\"PCScreen-W530XP#DELL\"]}";

  private static final String EXPORT_SCOPE =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"catalog\":{\"csid\":\"[C>pim $locale=en_US $type=DI]\"},"
          + "\"localeInheritance\":[\"en_US\"],\"allEntities\":true,\"includeEmbeddedEntities\":false,"
          + "\"entityPropertyIIDs\":[]," + "\"itemTypes\":[\"PROPERTY\"],"
          + "\"propertyTypes\":[],"
          + "\"PROPERTY\":{\"itemIDs\":[]}}";
  
  private static final String EXPORT_SCOPE_FOR_USERS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"USER\"]}";
  
  private static final String EXPORT_SCOPE_FOR_TASKS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"TASK\"]}";
  
  private static final String EXPORT_SCOPE_FOR_RULES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"RULE\"]}";
  
  private static final String EXPORT_SCOPE_FOR_GOLDEN_RECORD_RULES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"GOLDEN_RECORD_RULE\"]}";
  

  private static final String EXPORT_SCOPE_FOR_REFERENCES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"PROPERTY\"],\"propertyTypes\":[\"REFERENCE\"]}";
  
  private static final String EXPORT_SCOPE_FOR_ORGANIZATIONS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"ORGANIZATION\"]}";
  

  private static final String EXPORT_SCOPE_FOR_TRANSLATIONS =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"TRANSLATION\"], \"entityType\":\"USER\", \"languages\":[\"en_US\", \"es_ES\"], \"sortLanguage`\":\"en_US\""
          + "\"sortBy\":\"label\"}";

  private static final String EXPORT_SCOPE_FOR_LANGUAGES =
      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
          + "\"itemTypes\":[\"LANGUAGE\"]}";
  
  private static final String EXPORT_SCOPE_FOR_TABS =
	      "{\"csidFormat\":\"SYSTEM\",\"separator\":\"!$\",\"localeInheritance\":[\"en_US\"],"
	          + "\"itemTypes\":[\"TAB\"]}";
  
  private long exportSubmitBGProcess(String exportScope) throws Exception {
    printTestTitle("runSamples " + SERVICE);

    IPXONExportScopeDTO exportScopeDTO = new PXONExportScopeDTO();
    exportScopeDTO.fromJSON(exportScope);
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    IPXONExportPlanDTO prepareExport = bgpDriverDAO.prepareExport(exportScopeDTO);

    String json = prepareExport.toJSON();
    assertNotNull(json);
    JSONContent entryData = new JSONContent(json);
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    long jobIID = BGPDriverDAO.instance()
        .submitBGPProcess("Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, entryData);

    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent(jobIID);
    return jobIID;
  }

  @Test
  public void exportData() throws Exception {
    printTestTitle("exportData");
    exportSubmitBGProcess(EXPORT_SCOPE);
  }

  //@Ignore
  @Test
  public void exportAllBaseEntities() throws Exception {
    printTestTitle("exportAllBaseEntities");
    long jobIID = exportSubmitBGProcess(EXPORT_SCOPE_FOR_ALL_BASE_ENTITIES);
    
    TimeUnit.SECONDS.sleep(5);
    //parsing exported entities
    String directory = CSProperties.instance().getString("bgp.PXON_EXPORT.filePath");
    Path filePath = FileSystems.getDefault().getPath(directory, "export#"+jobIID+".pxon");
    PXONFileParser pxonFileParser = new PXONFileParser(filePath.toString());
    PXONFileParser.PXONBlock blockInfo = null;
    while ((blockInfo = pxonFileParser.getNextBlock()) != null) {
      BaseEntityDTO baseEntityDTO = new BaseEntityDTO();
      baseEntityDTO.fromPXON(blockInfo.getData());
      println("PXON :- "+baseEntityDTO.toPXON());
      println("JSON :- "+baseEntityDTO.toJSON());
    }
  }

  @Ignore
  @Test
  public void exportBaseEntityByIID() throws Exception {
    printTestTitle("exportBaseEntityByIID");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_BASE_ENTITY_BY_IID);
  }

  @Test
  public void exportBaseEntityByID() throws Exception {
    printTestTitle("exportBaseEntityByID");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_BASE_ENTITY_BY_ID);
  }

  @Ignore
  @Test
  public void exportProperties() throws Exception {
    printTestTitle("exportProperties");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_PROPERTIES);
  }

  @Ignore
  @Test
  public void exportTags() throws Exception {
    printTestTitle("exportTags");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_TAGS);
  }

  //@Ignore
  @Test
  public void exportAttributes() throws Exception {
    printTestTitle("exportAttributes");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_ATTRIBUTES);
  }


  //@Ignore
  @Test
  public void exportRelationships() throws Exception {
    printTestTitle("exportRelationships");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_RELATIONSHIPS);
  }

 // @Ignore
  @Test
  public void exportClassifier() throws Exception {
    printTestTitle("exportClassifier");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_CLASSIFIER);
  }

  @Ignore
  @Test
  public void exportPropertyCollections() throws Exception {
    printTestTitle("exportPropertyCollections");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_PROPERTY_COLLECTIONS);
  }

 // @Ignore
  @Test
  public void exportContexts() throws Exception {
    printTestTitle("exportContexts");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_CONTEXTS);
  }
  
  @Test
  public void exportUsers() throws Exception
  {
    printTestTitle("exportUsers");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_USERS);
  }
  
  @Test
  public void exportTasks() throws Exception
  {
    printTestTitle("exportTask");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_TASKS);
  }
  
  @Test
  public void exportRules() throws Exception
  {
    printTestTitle("exportRules");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_RULES);
  }
  
  @Test
  public void exportGoldenRecordRules() throws Exception
  {
    printTestTitle("export Golden record Rules");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_GOLDEN_RECORD_RULES);
  }
  
  @Test
  public void exportReferences() throws Exception {
    printTestTitle("exportReferences");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_REFERENCES);
  }
  
  @Test
  public void exportOrganizations() throws Exception {
    printTestTitle("exportOrganizations");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_ORGANIZATIONS);
  }
  
  @Test
  public void exportTranslations() throws Exception {
    printTestTitle("exportTranslations");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_TRANSLATIONS);
  }
  
  @Test
  public void exportAllKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportAllKlass");
    exportClasses(EntityType.UNDEFINED);
  }
  
  @Test
  public void exportArticleKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportArticleKlass");
    exportClasses(EntityType.ARTICLE);
  }
  
  @Test
  public void exportAssetKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportAssetKlass");
    exportClasses(EntityType.ASSET);
  }
  
  @Test
  public void exportSupplierKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportSupplierKlass");
    exportClasses(EntityType.SUPPLIER);
  }
  
  @Test
  public void exportTextAssetKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportTextAssetKlass");
    exportClasses(EntityType.TEXT_ASSET);
  }
  
  //TODO: PXPFDEV-21454: Deprecate Virtual Catalog 
  /*@Test
  public void exportVirtualCatalogKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportVirtualCatalogKlass");
    exportClasses(EntityType.VIRTUAL_CATALOG);
  }*/
  
  @Test
  public void exportMarketKlass() throws CSFormatException, Exception
  {
    printTestTitle("exportMarketKlass");
    exportClasses(EntityType.TARGET);
  }
  
  private void exportClasses(EntityType entityType) throws Exception, CSFormatException
  {
    IPXONExportScopeDTO scope = new PXONExportScopeDTO();
    List<IRootConfigDTO.ItemType> itemTypes = new ArrayList<>();
    itemTypes.add(ItemType.CLASSIFIER);
    scope.setConfigItemTypes(itemTypes);
    scope.setEnitytType(entityType);
    scope.setCSEFormat(ExportFormat.SYSTEM, "!$");
    scope.setLocaleCatalog(localeCatalogDto.getLocaleID(), localeCatalogDto.getCatalogCode(), localeCatalogDto.getOrganizationCode(),
        localeCatalogDto.getLocaleInheritanceSchema());
    exportSubmitBGProcess(scope.toJSON());
  }
  
  @Test
  public void exportLanguages() throws Exception {
    printTestTitle("exportLanguages");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_LANGUAGES);
  }
  
  @Test
  public void exportTabs() throws Exception {
    printTestTitle("exportTabs");
    exportSubmitBGProcess(EXPORT_SCOPE_FOR_TABS);
  }

}
