package com.cs.core.bgprocess.services.data;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author vallee
 */
public class OrphansCleaningTest extends AbstractBGProcessTests {
  
  private static final String SERVICE       = "ORPHANS_CLEANING";
  
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void cleaningBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, new JSONContent("{}"));
    
    this.runJobSample(10);
  }  
}
