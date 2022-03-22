package com.cs.core.bgprocess.services.di;

import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;

public class PXONImportClassifierTest extends AbstractPXONImporterTest{

  private static final String TEST_FOLDER = "/classTestPxon";

  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private static final String IMPORT_CLASS_WITH_CHILDREN = TEST_FOLDER + "/classWithChildren.pxon";
  @Test
  public void importClassWithParentChildrenRelation() throws Exception
  {
    printTestTitle("importClassWithParentChildrenRelation");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASS_WITH_CHILDREN));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_CLASSES_WITH_EMB_VARIANT = TEST_FOLDER + "/classWithEmbeddedClass.pxon";
  @Test
  public void importClassWithEmbeddedVariantWithCoupling() throws Exception
  {
    printTestTitle("importClassWithEmbeddedVariant");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASSES_WITH_EMB_VARIANT));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_ASSET_ClASS = TEST_FOLDER + "/assetKlass.pxon";
  @Test
  public void importAssetClass() throws Exception
  {
    printTestTitle("importAssetClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_ASSET_ClASS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_MARKET_CLASS = TEST_FOLDER + "/market.pxon";
  @Test
  public void importMarketClass() throws Exception
  {
    printTestTitle("importMarketClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MARKET_CLASS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_SUPPLIER_CLASS = TEST_FOLDER + "/supplier.pxon";
  //Get error in Log as supplier can't be created
  @Test
  public void importSupplierClass() throws Exception
  {
    printTestTitle("importSupplierClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_SUPPLIER_CLASS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_TEXT_ASSET_CLASS = TEST_FOLDER + "/textAsset.pxon";
  @Test
  public void importTextAssetClass() throws Exception
  {
    printTestTitle("importTextAssetClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_TEXT_ASSET_CLASS));
    submitImportProcess(fileName);
  }

  //TODO: PXPFDEV-21454: Deprecate Virtual Catalog 
  /*private static final String IMPORT_VIRTUAL_CATALOG_CLASS = TEST_FOLDER + "/virtualCatalog.pxon";
  @Test
  public void importVirtualCatalogClass() throws Exception
  {
    printTestTitle("importVirtualCatalogClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_VIRTUAL_CATALOG_CLASS));
    submitImportProcess(fileName);
  }*/

  private static final String IMPORT_LINKED_VARIANT = TEST_FOLDER + "/linkedVariant.pxon";
  @Test
  public void importClassWithLinkedVariant() throws Exception
  {
    printTestTitle("linkedVariant");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LINKED_VARIANT));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_LINKED_VARIANT_UPDATE = TEST_FOLDER + "/linkedVariantUpdate.pxon";
  @Test
  public void importClassWithLinkedVariantUpdate() throws Exception
  {
    printTestTitle("linkedVariant");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LINKED_VARIANT));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_LINKED_VARIANT_UPDATE));
    submitImportProcess(fileName);
  }

  private static final String INHERITANCE_BREAK = TEST_FOLDER + "/inheritanceBreak.pxon";
  @Test
  public void importClassWithInheritanceBreak() throws Exception
  {
    printTestTitle("inheritanceBreak");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(INHERITANCE_BREAK));
    submitImportProcess(fileName);

  }

  //Prerequisite: Create status tag with R_TAG code
  private static final String IMPORT_CLASS_WITH_STATUS_TAG_DELETE_AND_UPDATE = TEST_FOLDER + "/classWithStatusTagDeleteAndUpdate.pxon";
  @Test
  public void importClassWithStatusTagDeleteAndUpdate() throws Exception
  {
    printTestTitle("importClassWithParentChildrenRelation");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASS_WITH_CHILDREN));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASS_WITH_STATUS_TAG_DELETE_AND_UPDATE));
    submitImportProcess(fileName);
  }

  //Prerequisite: Create TASK with R_TASK1,R_TASK2,R_TASK3 code
  private static final String IMPORT_CLASS_WITH_TASK_DELETE_AND_UPDATE = TEST_FOLDER + "/NN_Updated.pxon";
  @Test
  public void importClassWithTaskDeleteAndUpdate() throws Exception
  {
    printTestTitle("importClassWithParentChildrenRelation");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASS_WITH_STATUS_TAG_DELETE_AND_UPDATE));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_CLASS_WITH_TASK_DELETE_AND_UPDATE));
    submitImportProcess(fileName);
  }

  private static final String UPDATE_NN_WITH_EMBEDDED = TEST_FOLDER + "/NN_create.pxon";
  private static final String UPDATE_NN_REMOVE_EMBEDDED = TEST_FOLDER + "/NN_updated.pxon";
  //test case added after bug https://contentserv.atlassian.net/browse/PXPFDEV-19653
  @Test
  public void updateNNWithEmbedded() throws Exception
  {
    printTestTitle("updateNNWithEmbedded");
    println("runSamples " + SERVICE);

    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(UPDATE_NN_WITH_EMBEDDED));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(UPDATE_NN_REMOVE_EMBEDDED));
    submitImportProcess(fileName);
  }


  private static final String UPDATE_NN_WITH_STATUS_TAGS = TEST_FOLDER + "/statusTagUpdate.pxon";
  //test case added after bug https://contentserv.atlassian.net/browse/PXPFDEV-19631
  @Test
  public void updateNNWithStatusTags() throws Exception
  {
    printTestTitle("updateNNWithEmbedded");
    println("runSamples " + SERVICE);

    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(UPDATE_NN_WITH_STATUS_TAGS));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(UPDATE_NN_WITH_STATUS_TAGS));
    submitImportProcess(fileName);
  }
}
