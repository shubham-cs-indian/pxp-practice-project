package com.cs.core.bgprocess.assets;

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
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Template to test BGP outside the scheduler -> it is required here to
 * initialize the bgp table with waiting job to make it running -> this example
 * just test the framework empty, when no job is declared in DB
 *
 * @author vallee
 */
public class AssetUploadTest extends AbstractBGProcessTests {

  private static final String SERVICE      = "ASSET_UPLOAD";
  private static final int    NB_BATCHES   = 2;

  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private JSONContent newJsonEntryForAssetUpload() throws CSFormatException
  {
    JSONContent entryData = new JSONContent("");
    entryData.setField("detectDuplicate", false);
    entryData.setField("assets", Arrays.asList("src/test/data/0514392_PE639462_S5a.jpg",
        "src/test/data/0992cc2a43863c111f708f265deec0cc.jpg"));
    return entryData;
  }

  @Test
  public void runSamples()
      throws CSInitializationException, RDBMSException, CSFormatException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    println("Posting 1 job in database...");
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    JSONContent entryData = newJsonEntryForAssetUpload();
    long jobIID = BGPDriverDAO.instance().submitBGPProcess(
        "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, entryData);
    println(String.format("job IID: %d posted and waiting...", jobIID));

    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID);
  }
}
