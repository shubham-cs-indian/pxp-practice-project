package com.cs.core.bgprocess.testutil;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

/**
 * Template to test BGP outside the scheduler -> it is required here to
 * initialize the bgp table with waiting job to make it running -> this example
 * just test the framework empty, when no job is declared in DB
 *
 * @author vallee
 */
public class BGProcessApplicationTest extends AbstractBGProcessTests {
  
  private static final int TEST_LOOP_NB = 2;
  private static final int NB_BATCHES   = 2;
  
  @Test
  public void testMultipleLoops()
      throws CSInitializationException, RDBMSException, CSFormatException
  {
    printTestTitle("testMultipleLoops");
    int i = 0;
    while (++i <= TEST_LOOP_NB) {
      println("MAIN APPLICATION: Scheduler loop " + i);
      RDBMSLogger.instance().info("MAIN APPLICATION: Scheduler loop %d/%d", i, TEST_LOOP_NB);
      this.runDispatcher();
      this.pause();
    }
    this.shutdown();
  }
  
  @Test
  public void runSamples()
      throws CSInitializationException, RDBMSException, CSFormatException, Exception
  {
    printTestTitle("runSamples");
    this.runJobSample(NB_BATCHES);
  }
}
