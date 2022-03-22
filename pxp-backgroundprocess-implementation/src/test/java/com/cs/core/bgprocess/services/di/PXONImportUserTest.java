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

public class PXONImportUserTest extends AbstractPXONImporterTest {

  private static final String TEST_FOLDER = "/UserPXON";

  @Before @Override public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private static final String IMPORT_USER = TEST_FOLDER + "/user.pxon";

  @Test public void importUser() throws Exception
  {
    printTestTitle("IMPORT USER");
    println("runSamples " + SERVICE);
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_USER));
    submitImportProcess(fileName);
  }
}
