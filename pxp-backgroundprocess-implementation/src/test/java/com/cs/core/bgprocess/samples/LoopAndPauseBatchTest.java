package com.cs.core.bgprocess.samples;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test of the fake BGP service LOOP&PAUSE
 *
 * @author vallee
 */
public class LoopAndPauseBatchTest extends AbstractBGProcessTests {
  
  private static final String SERVICE      = "LOOP&PAUSE";
  private static final int    NB_BATCHES   = 2;
  
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
   
  @Test
  public void runSamples()
      throws CSInitializationException, RDBMSException, CSFormatException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    println("Posting 2 jobs in database...");
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    long[] jobIID = { 0L, 0L };
    String callbackURL = getTestCallbackTemplateURL();
    jobIID[0] = BGPDriverDAO.instance().submitBGPProcess("Admin", SERVICE, callbackURL, userPriority, new JSONContent(""));
    jobIID[1] = BGPDriverDAO.instance().submitBGPProcess("Admin", SERVICE, callbackURL, userPriority, new JSONContent(""));
    println(String.format("job IIDs: %d, %d posted and waiting...", jobIID[0], jobIID[1]));

    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID[0]);
  }
}
