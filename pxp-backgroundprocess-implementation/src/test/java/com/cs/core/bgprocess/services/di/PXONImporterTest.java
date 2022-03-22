package com.cs.core.bgprocess.services.di;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.dataintegration.dto.PXONImporterPlanDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;

public class PXONImporterTest extends AbstractBGProcessTests {

  private static final String SERVICE    = "PXON_IMPORT";
  private static final int    NB_BATCHES = 15;
  
  private static final String IMPORT_USERS = "[{\"firstname\":\"Shubhangi132\",\"$username\":\"Shubhangi132\","
      + "\"csid\":\"[U>Shubhangi132 $iid=0]\",\"$code\":\"Shubhangi132\",\"$isbackgrounduser\":false,"
      + "\"email\":\"Shubhangi132waghmare@gmail.com\",\"lastname\":\"\",\"gender\":\"female\",\"contact\":35453445487,"
      + "\"password\":\"hsdhsg\"},{\"firstname\":\"Shubhangi132 Organization\",\"$username\":\"BackgroundUser_Shubhangi132Organization\","
      + "\"csid\":\"[U>BackgroundUser_Shubhangi132Organization $iid=0]\",\"$code\":\"BackgroundUserShubhangi132Organization\","
      + "\"$isstandard\":true,\"$isbackgrounduser\":true,\"email\":\"Shubhangi132waghmare123@gmail.com\",\"lastname\":\"waghmare\","
      + "\"gender\":\"\",\"contact\":34245237436,\"password\":\"ddghddhfg\"}]";

  private static final String IMPORT_TASK = "[{\"prioritytag\":\"availabilitytag\",\"color\":\"#A07400\",\"csid\":\"[K>R_Task $type=SHARED]\","
      + "\"icon\":\"\",\"$statustag\":\"taskstatustag\",\"label\":\"R_Task\"},{\"csid\":\"[K>N_Task $type=PERSONAL]\",\"icon\":\"\","
      + "\"$statustag\":\"taskstatustag\",\"label\":\"N_Task\"}]";

  private static final String IMPORT_TAB = "[{\"sequence\":3,\"LpropertySequenceList\":[],\"csid\":\"[W>context_tab]\",\"icon\":null,\"label\":\"context_tab\"},\r\n" 
      + "{\"sequence\":8,\"LpropertySequenceList\":[],\"csid\":\"[W>duplicate_tab]\",\"icon\":null,\"label\":\"duplicates_tab\"},\r\n"
      + "{\"sequence\":7,\"LpropertySequenceList\":[],\"csid\":\"[W>mdm_tab]\",\"icon\":null,\"label\":\"mdm_tab\"},\r\n"
      + "{\"sequence\":10,\"LpropertySequenceList\":[\"targetGeneralInformationPropertyCollection\"],\"csid\":\"[W>Tab100097]\",\"icon\":null,\"label\":\"New 88 tab\"},\r\n" 
      + "{\"sequence\":11,\"LpropertySequenceList\":[\"supplierGeneralInformationPropertyCollection\",\"UNIT_Default\",\"metadataCollection\"],\"csid\":\"[W>Tab100098]\",\"icon\":null,\"label\":\"New 89 tab\"},\r\n" 
      + "{\"sequence\":-1,\"LpropertySequenceList\":[],\"csid\":\"[W>overview_tab]\",\"icon\":null,\"label\":\"overview_tab\"},\r\n"
      + "{\"sequence\":1,\"LpropertySequenceList\":[\"articlegeneralInformationPropertyCollection\",\"pricingPropertyCollection\",\"assetInformationPropertyCollection\",\"textassetGeneralInformationPropertyCollection\",\"defaultXRayPropertyCollection\"],\"csid\":\"[W>property_collection_tab]\",\"icon\":null,\"label\":\"property_collection_tab\"},\r\n" 
      + "{\"sequence\":2,\"LpropertySequenceList\":[\"standardArticleAssetRelationship\",\"standardMarketAssetRelationship\",\"standardSupplierAssetRelationship\",\"standardTextAsset-AssetRelationship\",\"standardArticleMarketRelationship\",\"standardVirtualCatalogAssetRelationship\",\"standardArticleGoldenArticleRelationship\"],\"csid\":\"[W>relationship_tab]\",\"icon\":null,\"label\":\"relationship_tab\"},\r\n" 
      + "{\"sequence\":6,\"LpropertySequenceList\":[],\"csid\":\"[W>rendition_tab]\",\"icon\":null,\"label\":\"renditions_tab\"},\r\n"
      + "{\"sequence\":4,\"LpropertySequenceList\":[],\"csid\":\"[W>task_tab]\",\"icon\":null,\"label\":\"task_tab\"},\r\n"
      + "{\"sequence\":5,\"LpropertySequenceList\":[],\"csid\":\"[W>timeline_tab]\",\"icon\":null,\"label\":\"timeline_tab\"},\r\n" 
      + "{\"sequence\":9,\"LpropertySequenceList\":[],\"csid\":\"[W>usage_tab]\",\"icon\":null,\"label\":\"Usages\"}]";

  private static final String IMPORT_GOLDEN_RECORD_RULE = "[{\"isautocreate\":false,\"Ltags\":[\"availabilitytag\"],"
      + "\"mergeeffect\":{\"Ltags\":[{\"Lsupplierids\":[\"-1\"],\"entitytype\":\"tags\",\"entityid\":\"availabilitytag\",\"type\":"
      + "\"supplierPriority\"},{\"entitytype\":\"tags\",\"entityid\":\"listingstatustag\",\"type\":\"latest\"}],"
      + "\"Lattributes\":[{\"Lsupplierids\":[\"-1\"],\"entitytype\":\"attributes\",\"entityid\":\"createdbyattribute\",\"type\":"
      + "\"supplierPriority\"},{\"Lsupplierids\":[\"-1\"],\"entitytype\":\"attributes\",\"entityid\":\"discountattribute\",\"type\":"
      + "\"supplierPriority\"},{\"entitytype\":\"attributes\",\"entityid\":\"scheduleendattribute\",\"type\":\"latest\"}],"
      + "\"Lnaturerelationships\":[],\"Lrelationships\":[{\"Lsupplierids\":[],\"entitytype\":\"relationships\",\"entityid\":"
      + "\"standardArticleAssetRelationship\",\"type\":\"supplierPriority\"}]},\"Lattributes\":[\"descriptionattribute\","
      + "\"duedateattribute\",\"scheduleendattribute\"],\"Ltaxonomyids\":[],\"Lklassids\":[\"single_Article\",\"article\"],"
      + "\"Lorganizations\":[\"-1\"],\"csid\":\"[S>Golden_Record_rule]\",\"label\":\"Golden_Record_rule\",\"Lphysicalcatalogids\":"
      + "[\"pim\"]}]";
  
  private static final String IMPORT_REFERANCE = "[{\"tab\":\"context_tab\",\"csid\":\"[xxLutz_referance1 $type=REFERENCE]\","
      + "\"$isstandard\":false,\"icon\":\"\",\"label\":\"xxLutz_referance\",\"Jside1\":{\"$cardinality\":\"Many\",\"$classcode\":"
      + "\"single_article\",\"label\":\"R_side1\",\"type\":\"PROJECT_KLASS_TYPE\"},\"Jside2\":{\"$cardinality\":\"One\","
      + "\"$classcode\":\"image_asset\",\"label\":\"R_side2\",\"type\":\"ASSET_KLASS_TYPE\"},\"$isnature\":false}]";

  private static final String IMPORT_ORGANIZATION = "[{\"Lportals\":[],\"Lsystems\":[\"Sys100577\",\"Sys100578\"],\"Ltaxonomyids\":[\"tax1\",\"tax2\",\"tax3\"]" +
      ",\"Lklassids\":[\"asset_asset\",\"content_enrichment_agency\",\"digital_asset_agency\"],\"csid\":\"[O>Org1000112]\",\"LphysicalCatalogs\":" +
      "[\"pim\",\"onboarding\",\"offboarding\"],\"Lroles\":[{\"isDashboardEnable\":true,\"Lportals\":[],\"LtargetTaxonomies\":[\"tax1\"],\"csid\":" +
      "\"[Q>Role12121]\",\"LphysicalCatalogs\":[\"onboarding\"],\"Lentities\":[\"ArticleInstance\",\"SupplierInstance\",\"TextAssetInstance\"," +
      "\"VirtualCatalogInstance\"],\"Lusers\":[\"Use100552\",\"Use100553\"],\"label\":\"Niraj\",\"roleType\":\"user\",\"$type\":" +
      "\"com.cs.core.config.interactor.entity.role.Role\",\"LtargetKlasses\":[\"content_enrichment_agency\"],\"Lsystems\":[\"Sys100577\",\"Sys100578\"]," +
      "\"Lendpoints\":[\"ep1\",\"ep6\"],\"Lkpis\":[\"kpi4\",\"kpi5\",\"kpi3\"],\"isStandard\":false,\"isBackgroundRole\":false},{\"isDashboardEnable\":" +
      "true,\"Lportals\":[],\"LtargetTaxonomies\":[\"tax2\",\"tax3\"],\"csid\":\"[Q>Role12321]\",\"LphysicalCatalogs\":[\"onboarding\",\"offboarding\"]," +
      "\"Lentities\":[],\"Lusers\":[\"Use100554\"],\"label\":\"Nirja\",\"roleType\":\"user\",\"$type\":\"com.cs.core.config.interactor.entity.role.Role\"," +
      "\"LtargetKlasses\":[\"asset_asset\",\"digital_asset_agency\"],\"Lsystems\":[\"Sys100577\",\"Sys100578\"],\"Lendpoints\":[\"ep2\",\"ep5\"],\"Lkpis\":" +
      "[\"kpi2\",\"kpi1\",\"kpi3\"],\"isStandard\":false,\"isBackgroundRole\":false}],\"label\":\"Niraj's Org\",\"LendpointIds\":[\"ep1\",\"ep2\",\"ep5\",\"ep6\"]" +
      ",\"$type\":\"content_enrichment_agency\"}]";
  
  private static final String IMPORT_PROPERTY_COLLECTION = "[{\"isdefaultforxray\":false,\"Lelements\":[{\"code\":\"nameattribute\","
      + "\"id\":\"nameattribute\",\"index\":1,\"type\":\"attribute\"},{\"code\":\"longdescriptionattribute\","
      + "\"id\":\"longdescriptionattribute\",\"index\":2,\"type\":\"attribute\"}],\"tab\":null,"
      + "\"csid\":\"[G>xxxLutz_PropertyCollection3]\",\"$isstandard\":false,\"icon\":\"\","
      + "\"label\":\"xxxLutz_PropertyCollection3\",\"isforxray\":false}]";
  

  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  @Test
  public void importTestData() throws Exception
  {
    printTestTitle("importTestData");
    println("runSamples " + SERVICE);
    
    PXONImporterPlanDTO importerPlanDTO = new PXONImporterPlanDTO();
    importerPlanDTO.setRelativeAfterImportFile("rosh\\importEntities1.txt");
    importerPlanDTO.setRelativeImportFile("importEntities.txt");
    importerPlanDTO.setLocaleID(localeCatalogDto.getLocaleID());
    JSONContent entryData = new JSONContent(importerPlanDTO.toJSON());
    
    long jobIID = BGPDriverDAO.instance().submitBGPProcess("admin", SERVICE, getTestCallbackTemplateURL(),
            IBGProcessDTO.BGPPriority.LOW, entryData);
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent(jobIID);
  }
  
  @Ignore
  @Test
  public void importUsers()throws Exception
  {
    printTestTitle("importUsers");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_USERS);
    submitImportProcess(fileName);
    deleteFile(directory, fileName);
  }

  @Ignore
  @Test
  public void importTasks()throws Exception
  {
    printTestTitle("importTasks");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_TASK);
    submitImportProcess(fileName);
    deleteFile(directory, fileName);
  }

  @Test
  public void importTabs()throws Exception
  {
    printTestTitle("importTabs");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_TAB);
    submitImportProcess(fileName);
  }
  
  @Test
  public void importGoldenRecord() throws Exception {
    printTestTitle("importGoldenRecord");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_GOLDEN_RECORD_RULE);
    submitImportProcess(fileName);
  }
  
  @Test
  public void importReference() throws Exception {
    printTestTitle("importReference");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_REFERANCE);
    submitImportProcess(fileName);
  }
  
  @Test
  public void importOrganization() throws Exception {
    printTestTitle("importReference");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_ORGANIZATION);
    submitImportProcess(fileName);
  }
  
  @Test
  public void importPropertyCollection() throws Exception {
    printTestTitle("importPropertyCollection");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, IMPORT_PROPERTY_COLLECTION);
    submitImportProcess(fileName);
  }

  private void deleteFile(String directory, String fileName)
  {
    File file = new File(directory+"/"+fileName);
    if(file.delete()) {
      System.out.println("file deleted");
    }
  }
  
  protected String prepareImportData(String directory, String importData) throws CSInitializationException
  {
    String fileName = "import#MasterTaxonoy.pxon";
    Path path = FileSystems.getDefault()
            .getPath(directory, fileName);
    File dir = new File(directory);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(path.toFile());
      outputStream.write(importData.getBytes());
      outputStream.close();
    } catch (IOException exc) {
      throw new CSInitializationException("Can't open export file", exc);
    }
    return fileName;
  }

  private void submitImportProcess(String fileName) throws Exception
  {
    PXONImporterPlanDTO importerPlanDTO = new PXONImporterPlanDTO();
    importerPlanDTO.setRelativeAfterImportFile("rosh\\importEntities1.txt");
    importerPlanDTO.setRelativeImportFile(fileName);
    importerPlanDTO.setLocaleID(localeCatalogDto.getLocaleID());
    //importerPlanDTO.setSessionID(""); set this when you want to create supplier instance. Also please start the main jetty server
    //prior to setting this. This field should contain JsessionID.
    JSONContent entryData = new JSONContent(importerPlanDTO.toJSON());
    
    long jobIID = BGPDriverDAO.instance().submitBGPProcess("admin", SERVICE, getTestCallbackTemplateURL(),
            IBGProcessDTO.BGPPriority.LOW, entryData);
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent(jobIID);
  }
}