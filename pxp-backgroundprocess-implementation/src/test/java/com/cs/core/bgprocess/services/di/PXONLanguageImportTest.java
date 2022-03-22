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

public class PXONLanguageImportTest extends AbstractPXONImporterTest {

  private static final String TEST_FOLDER = "/languagePxon";


  private static final String IMPORT_LANGUAGES = TEST_FOLDER + "/language.pxon";
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  @Test
  public void importLanguage() throws Exception {
    printTestTitle("importLanguage");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LANGUAGES));
    submitImportProcess(fileName);
  }

  private static final String IMPORT_LANGUAGES_WITH_NO_DEFAULT = TEST_FOLDER + "/export.pxon";
  @Test
  public void importLanguageWithNoDefault() throws Exception {
    printTestTitle("importLanguageWithNoDefault");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_LANGUAGES_WITH_NO_DEFAULT));
    submitImportProcess(fileName);
  }
}
