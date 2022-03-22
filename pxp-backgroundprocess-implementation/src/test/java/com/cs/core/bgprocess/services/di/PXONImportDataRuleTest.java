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

public class PXONImportDataRuleTest extends AbstractPXONImporterTest {

  private static final String TEST_FOLDER = "/dataRulePxon";
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  // prerequisite : R_Taxonomy1, R_taxonomy2 taxonomy must be created with these
  // code in orientDB
  private static final String IMPORT_RULE_VIOLATION = TEST_FOLDER + "/ruleViolation.pxon";
  @Ignore
  @Test
  public void importDataRuleViolation() throws Exception
  {
    printTestTitle("importDataRuleViolation");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_RULE_VIOLATION));
    submitImportProcess(fileName);
  }
  
  // prerequisite : R_Taxonomy1, R_taxonomy2 taxonomy and R_non_nature class
  // must be created with these code in orientDB
  private static final String IMPORT_RULE_CLASSIFICATION = TEST_FOLDER + "/classification.pxon";
  @Ignore
  @Test
  public void importDataRuleClassification() throws Exception
  {
    printTestTitle("importDataRuleClassification");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_RULE_CLASSIFICATION));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_RULE_STANDARDIZATION = TEST_FOLDER + "/standardization.pxon";
  @Ignore
  @Test
  public void importStandardization() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_RULE_STANDARDIZATION));
    submitImportProcess(fileName);
  }

  // prerequisite : R_Taxonomy1, R_taxonomy2 taxonomy and R_non_nature class
  // must be created with these code in orientDB
  private static final String IMPORT_LANG_RULE_VIOLATION = TEST_FOLDER + "/lang_dependent_2.pxon";
  @Ignore
  @Test
  public void importLanguageDependentViolation() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LANG_RULE_VIOLATION));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_LANG_RULE_STANDARDIZATION = TEST_FOLDER + "/languageDependent.pxon";
  @Ignore
  @Test
  public void importLanguageDependentStandardization() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LANG_RULE_STANDARDIZATION));
    submitImportProcess(fileName);
  }

  private static final String UPDATE_FLAT_FIELDS = TEST_FOLDER + "/flatFieldsUpdate.pxon";
  @Ignore
  @Test
  public void updateFlatFields() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LANG_RULE_VIOLATION));
    submitImportProcess(fileName);
     fileName = prepareImportData(directory, getPxonAsString(UPDATE_FLAT_FIELDS));
    submitImportProcess(fileName);
  }

  private static final String UPDATE_VIOLATION = TEST_FOLDER + "/updateRuleViolations.pxon";
  @Ignore
  @Test
  public void updateViolation() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_RULE_VIOLATION));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(UPDATE_VIOLATION));
    submitImportProcess(fileName);
  }

  // prerequisite : R_Taxonomy1, R_taxonomy3 taxonomy and R_non_nature3, R_non_nature3 class
  // must be created with these code in orientDB
  private static final String UPDATE_NORMALIZATION_AND_RULES = TEST_FOLDER + "/updateRuleAndNormalizations.pxon";
  @Ignore
  @Test
  public void updateNormalization() throws Exception
  {
    printTestTitle("importDataRuleSTDNORM");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_RULE_STANDARDIZATION));
    submitImportProcess(fileName);
    fileName = prepareImportData(directory, getPxonAsString(UPDATE_NORMALIZATION_AND_RULES));
    submitImportProcess(fileName);
  }

}
