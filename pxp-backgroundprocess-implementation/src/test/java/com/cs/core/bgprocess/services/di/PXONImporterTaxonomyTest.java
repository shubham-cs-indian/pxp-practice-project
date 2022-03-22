package com.cs.core.bgprocess.services.di;

import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;

public class PXONImporterTaxonomyTest extends AbstractPXONImporterTest {

  private static final String TEST_FOLDER = "/taxonomytestpxon";
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private static final String IMPORT_MASTER_TAXONOMY = TEST_FOLDER + "/masterTaxonomy.pxon";
  @Test
  public void importMasterTaxonomyWithPC()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithTaskAndPC");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY));
    submitImportProcess(fileName);
  }

  //prerequisite : create R_Task and R_EMB from UI
  private static final String IMPORT_MASTER_TAXONOMY_WITH_EMBCLASS = TEST_FOLDER + "/masterTaxonomyWithEmbeddedClasses.pxon";
  @Ignore
  @Test
  public void importMasterTaxonomyWithEMBClass()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithEMBClass");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_EMBCLASS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_MASTER_TAXONOMY_WITH_LEVELS = TEST_FOLDER + "/masterTaxonomyWithLevels.pxon";
  @Test
  public void importMasterTaxonomyWithLevels()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithLevels");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_LEVELS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_MASTER_TAXONOMY_WITH_CHILD = TEST_FOLDER + "/masterTaxonomyWithChildren.pxon";
  @Test
  public void importMasterTaxonomyWithChild()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithChild");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_CHILD));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_HIERARCHY_TAXONOMY_WITH_LEVELS = TEST_FOLDER + "/heirarchyTaxonomyWithLevels.pxon";
  @Test
  public void importHierarchyTaxonomyWithLevels()throws Exception
  {
    printTestTitle("importHierarchyTaxonomyWithLevels");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_HIERARCHY_TAXONOMY_WITH_LEVELS));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_HIERARCHY_TAXONOMY_WITH_CHILDREN = TEST_FOLDER + "/heirarchyTaxonomyWithChildren.pxon";
  @Test
  public void importHierarchyTaxonomyWitChildren()throws Exception
  {
    printTestTitle("importHierarchyTaxonomyWitChildren");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_HIERARCHY_TAXONOMY_WITH_CHILDREN));
    submitImportProcess(fileName);
  }


  //prerequisite : create R_Task2 and R_EMB2 from UI
  private static final String IMPORT_MASTER_TAXONOMY_WITH_REPLACE_EMBEDDED_AND_TASK = TEST_FOLDER + "/masterTaxonomyRemoveEmbeddedClassesAndTask.pxon";
  @Ignore
  @Test
  public void importMasterTaxonomyWithReplaceEmbeddedAndTask()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithDeleteEmbeddedAndTask");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");

    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_EMBCLASS));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_REPLACE_EMBEDDED_AND_TASK));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_MASTER_TAXONOMY_WITHOUT_PC = TEST_FOLDER + "/masterTaxonomyWithoutPC.pxon";
  @Test
  public void importMasterTaxonomyWithDeletePropertyCollection()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithDeletePropertyCollection");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITHOUT_PC));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_MASTER_TAXONOMY_WITH_COUPLING_MODIFICATION = TEST_FOLDER + "/masterTaxonomyModificationCoupling.pxon";
  @Test
  public void importMasterTaxonomyWithContextualCouplingModification()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithContextualCouplingModification");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_REPLACE_EMBEDDED_AND_TASK));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_COUPLING_MODIFICATION));
    submitImportProcess(fileName);
  }


  private static final String INHERITANCE_BREAK = TEST_FOLDER + "/breakInheritance.pxon";
  @Test
  public void importMasterTaxonomyWithInheritanceBreakInPropertyCollection()throws Exception
  {
    printTestTitle("importMasterTaxonomyWithInheritanceBreakInPropertyCollection");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_MASTER_TAXONOMY_WITH_CHILD));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(INHERITANCE_BREAK));
    submitImportProcess(fileName);
  }
}
