package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.common.test.DiTestUtil;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

@SuppressWarnings("unchecked")
public class ConfigPXONToExcelTaskTest extends DiIntegrationTestConfig {
  
  public static final String ARTICLE_PATH             = "src/test/resources/pxon/Config_CLASSIFIER.pxon";
  public static final String SUPPLIER_PATH            = "src/test/resources/pxon/Config_SUPPLIER.pxon";
  public static final String VALID_SCOPE_RELATIONSHIP = "{\"csid\": \"[standardArticleAssetRelationship $iid=281 $type=RELATIONSHIP]\"}";
  public static final String INVALID_SCOPE            = "{\"csid\": \"[standardArticleAssetRelationship $iid=281 $type=RELAT]\"}";
  public static final String ATTRIBUTE_PATH           = "src/test/resources/pxon/Config_ATTRIBUTE.pxon";
  public static final String PROPERTY_COLLECTION_PATH = "src/test/resources/pxon/Config_PROPERTY_COLLECTION.pxon";
  public static final String CONTEXT_PATH             = "src/test/resources/pxon/Config_CONTEXT.pxon";
  public static final String RELATIONSHIP_PATH        = "src/test/resources/pxon/Config_RELATIONSHIP.pxon";
  public static final String INVALID_PATH             = "src/test/resources/pxon/Config_INVALID.pxon";
  public static final String HIERARCHY_PATH           = "src/test/resources/pxon/Config_HIERARCHY.pxon";
  public static final String MASTER_TAXONOMY_PATH     = "src/test/resources/pxon/Config_TAXONOMY.pxon";
  public static final String INVALID_CONFIG_TYPE      = "src/test/resources/pxon/Config_TYPE_INV.pxon";
  public static final String TAG_PATH                 = "src/test/resources/pxon/Config_TAG.pxon";
  public static final String USER_PATH                = "src/test/resources/pxon/Config_USER.pxon";
  public static final String TASK_PATH                = "src/test/resources/pxon/Config_TASK.pxon";
  public static final String REFERENCE_PATH           = "src/test/resources/pxon/Config_REFERENCE.pxon";
  public static final String GOLDEN_RECORD_RULE_PATH  = "src/test/resources/pxon/Config_GOLDEN_RECORD_RULE.pxon";
  public static final String RULE_PATH                = "src/test/resources/pxon/Config_RULE.pxon";
  public static final String PARTNER_PATH             = "src/test/resources/pxon/Config_PARTNER.pxon";
  public static final String TAB_PATH                 = "src/test/resources/pxon/Config_TAB.pxon";
  public static final String TRANSLATION_PATH         = "src/test/resources/pxon/Config_TRANSLATION.pxon";
  
  @Autowired
  ConfigPXONToExcelTask      configPXONToExcelTask;
  
  @Test
  public void getScope() throws CSFormatException
  {
    ICSEObject cseObject = configPXONToExcelTask.parseCSEElement(VALID_SCOPE_RELATIONSHIP);
    String specificationType = cseObject.getSpecification(ICSEElement.Keyword.$type);
    ICSEElement.CSEObjectType cseObjectType = cseObject.getObjectType();
    IPropertyDTO.SuperType propertySuperType = IPropertyDTO.PropertyType.valueOf(specificationType).getSuperType();
    System.out.println("ConfigPXONToExcelTaskTest :: getScope :: output  ObjectType:" + cseObjectType.name() + "; Specification Type:"
        + propertySuperType.name());
    System.out.println("******************************TEST COMPLETED******************************");
    assertTrue(cseObject != null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidScope() throws CSFormatException
  {
    ICSEObject cseObject = configPXONToExcelTask.parseCSEElement(INVALID_SCOPE);
    String specificationType = cseObject.getSpecification(ICSEElement.Keyword.$type);
    ICSEElement.CSEObjectType cseObjectType = cseObject.getObjectType();
    IPropertyDTO.SuperType propertySuperType = IPropertyDTO.PropertyType.valueOf(specificationType).getSuperType();
    System.out.println("ConfigPXONToExcelTaskTest :: getScope :: output  ObjectType:" + cseObjectType.toString() + "; Specification Type:" + propertySuperType.name());
    System.out.println("******************************TEST COMPLETED******************************");
   } 
  
  @Test
  public void testClassifierArticle()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { ARTICLE_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> enrtySet = filesToExport.entrySet();
    enrtySet.forEach(p -> writeToExcel("ARTICLE_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testClassifierArticle :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void testClassifierSupplier()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { SUPPLIER_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> enrtySet = filesToExport.entrySet();
    enrtySet.forEach(p -> writeToExcel("SUPPLIER_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testClassifierSupplier :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * Creates Excel for the test case output TRANSFORMED_DATA
   * 
   * @param key
   * @param object
   */
  public void writeToExcel(String key, Entry<String, String> object)
  {
    File destinationFile = null;
    InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(object.getValue().getBytes()));
    try {
      String path = CSProperties.instance().getString("nfs.file.path") + "/JUNIT/";// D:/NFS
      destinationFile = new File(path + key + DateTime.now().toString("yyyy-MM-dd_HH.mm.ss.mmm") + ".xlsx");
      FileUtils.copyInputStreamToFile(inputStream, destinationFile);
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    catch (CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  @Test
  public void testAttribute()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { ATTRIBUTE_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> enrtySet = filesToExport.entrySet();
    enrtySet.forEach(p -> writeToExcel("ATTRIBUTE_PATH_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testAttribute :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void testPropertyCollection()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { PROPERTY_COLLECTION_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
    entrySet.forEach(p -> writeToExcel("PROPERTY_COLLECTION_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testPropertyCollection :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * Transform config PXON to Excel.
   * 
   */
  @Test
  public void testContext()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { CONTEXT_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
    entrySet.forEach(p -> writeToExcel("CONTEXT_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testContext :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void testRelationship()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { RELATIONSHIP_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
    entrySet.forEach(p -> writeToExcel("RELATIONSHIP_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testRelationship :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * Invalid PXON Path tested
   */
  @Test
  public void testInvalidPath()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { INVALID_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR).get(0).toString()
        .equals("Input parameter PXON_FILE_PATH is Invalid."));
    System.out.println("ConfigPXONToExcelTaskTest :: testRelationship :: output  "
        + workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  /**
   * Invalid PropertyType given in PXON 
   */
  @Test
  public void testInvalidConfigType()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { INVALID_CONFIG_TYPE });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    assertTrue(workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR).get(0).toString()
        .equals("PXON has Invalid Config Type : No enum constant com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType.RELATIONSHIP1"));
    
    System.out.println("ConfigPXONToExcelTaskTest :: testRelationship :: output  "
        + workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR).get(0));
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void testHierarchy()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { HIERARCHY_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    System.out.println(workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
    entrySet.forEach(p -> writeToExcel("HIERARCHY_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testHierarchy :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }
  
  @Test
  public void testMasterTaxonomy()
  {
    WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
        new String[] { MASTER_TAXONOMY_PATH });
    configPXONToExcelTask.generateFromPXON(workflowTaskModel);
    System.out.println(workflowTaskModel.getExecutionStatusTable().getExecutionStatusTableMap().get(MessageType.ERROR));
    // to verify Excel Data
    Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
        .get("TRANSFORMED_DATA");
    Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
    entrySet.forEach(p -> writeToExcel("TAXONOMY_", p.getValue().entrySet().iterator().next()));
    System.out.println("ConfigPXONToExcelTaskTest :: testMasterTaxonomy :: output  " + filesToExport);
    System.out.println("******************************TEST COMPLETED******************************");
  }

 @Test
 public void testTag()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { TAG_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   //to verify Excel Data
   Map<String, Map<String,String>> filesToExport =  (Map<String, Map<String,String>>)workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String,String>>> enrtySet = filesToExport.entrySet();
   enrtySet.forEach(p->writeToExcel("TAG_PATH_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testTag :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testUser()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { USER_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   //to verify Excel Data
   Map<String, Map<String,String>> filesToExport =  (Map<String, Map<String,String>>)workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String,String>>> enrtySet = filesToExport.entrySet();
   enrtySet.forEach(p->writeToExcel("USER_PATH_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testUSER :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 } 
 
 @Test
 public void testTask()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask",
       new String[] { "PXON_FILE_PATH" }, new String[] { TASK_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel
       .getOutputParameters().get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> enrtySet = filesToExport.entrySet();
   enrtySet.forEach(p -> writeToExcel("TASK_PATH_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testTASK :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testReference()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { REFERENCE_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   entrySet.forEach(p -> writeToExcel("REEFERENCE_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testReference :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testGoldenRecordRule()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { GOLDEN_RECORD_RULE_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   entrySet.forEach(p -> writeToExcel("GOLDEN_RECORD_RULE_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testGoldenRecordRule :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testRule()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask",
       new String[] { "PXON_FILE_PATH" }, new String[] { RULE_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   entrySet.forEach(p -> writeToExcel("RULE_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testRule :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }

 @Test
 public void testPartner()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { PARTNER_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   entrySet.forEach(p -> writeToExcel("PARTNER_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testPartner :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testTab()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask",
       new String[] { "PXON_FILE_PATH" }, new String[] { TAB_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   entrySet.forEach(p -> writeToExcel("Tab_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testTab :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
 
 @Test
 public void testTranslations()
 {
   WorkflowTaskModel workflowTaskModel = DiTestUtil.createTaskModel("configPXONToExcelTask", new String[] { "PXON_FILE_PATH" },
       new String[] { TRANSLATION_PATH });
   configPXONToExcelTask.generateFromPXON(workflowTaskModel);
   assertTrue(!workflowTaskModel.getOutputParameters().get("TRANSFORMED_DATA").equals(null));
   // to verify Excel Data
   Map<String, Map<String, String>> filesToExport = (Map<String, Map<String, String>>) workflowTaskModel.getOutputParameters()
       .get("TRANSFORMED_DATA");
   Set<Entry<String, Map<String, String>>> entrySet = filesToExport.entrySet();
   
   entrySet.forEach(p -> writeToExcel("TRANSLATION_", p.getValue().entrySet().iterator().next()));
   System.out.println("ConfigPXONToExcelTaskTest :: testTranslation :: output  " + filesToExport);
   System.out.println("******************************TEST COMPLETED******************************");
 }
}
