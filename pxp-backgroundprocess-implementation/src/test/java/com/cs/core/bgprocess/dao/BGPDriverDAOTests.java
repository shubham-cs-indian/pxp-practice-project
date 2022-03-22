package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.idao.IBGPCursor;
import com.cs.core.bgprocess.idao.IBGPCursor.Characteristic;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BGPDriverDAOTests extends AbstractBGProcessTests {

  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }

  private long submitBGProcess(String entries)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    IJSONContent jsonContent = new JSONContent(entries);
    long jobIID = BGPDriverDAO.instance()
        .submitBGPProcess("Rahul Duldul", "ORPHANS_CLEANING", getTestCallbackTemplateURL(),
            IBGProcessDTO.BGPPriority.HIGH, jsonContent);
    return jobIID;
  }

  @Test
  public void submitBGPProcess() throws RDBMSException, CSInitializationException
  {
    printTestTitle("submitBGPProcess");
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    IJSONContent jsonContent = new JSONContent();
    bgpDriverDAO.submitBGPProcess("Rahul Duldul", "LOOP&PAUSE", getTestCallbackTemplateURL(),
        IBGProcessDTO.BGPPriority.HIGH, jsonContent);
  }

  @Test
  public void getBGPProcess() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("getBGPProcess");
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    long jobIID = submitBGProcess("{\"entry\":450,\"test\":\"" + "getBGPProcess" + "\"}");
    IBGProcessDTO bgpProcess = bgpDriverDAO.getBGPProcess(jobIID);
    println(bgpProcess.toPXON());
    assertEquals(bgpProcess.getJobIID(), jobIID);
  }

  @Test
  public void loadJobLogFile() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("loadJobLogFile");
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    long jobIID = submitBGProcess("{\"test\":\"" + "getBGPProcess" + "\"}");
    File loadJobLogFile = bgpDriverDAO.loadJobLogFile(jobIID);
    println(loadJobLogFile.getName());
  }

  @Test
  public void getBGPProceses() throws RDBMSException, CSFormatException
  {
    printTestTitle("getBGPProcesses");
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    IBGPCursor bgpCursor = bgpDriverDAO.getBGProcesses();
    println("count "+bgpCursor.getCount());
    List<IBGProcessDTO> processes = bgpCursor.getNext(0, 10);
    printBGPProcess(processes);

    // Changing order and direction only
    println("Changing Order and Direction Only");
    bgpCursor.setOrdering(OrderDirection.ASC, Characteristic.created);
    println("count "+bgpCursor.getCount());
    List<IBGProcessDTO> orderedProcesses = bgpCursor.getNext(0, 10);
    printBGPProcess(orderedProcesses);

    //Users, Status and Service filter applied
    Set<Long> userIIDs = new HashSet<>();
    userIIDs.add(6001L);
    bgpCursor.setUserFilter(userIIDs);
    bgpCursor.setServiceFilter("LOOP&PAUSE");
    bgpCursor.setStatusFilter(BGPStatus.PENDING);
    List<IBGProcessDTO> filterOutPut = bgpCursor.getNext(0, 10);
    println( "count LOOP&PAUSE/PENDING = " + bgpCursor.getCount());
    printBGPProcess(filterOutPut);

    //Filter by service and status
    IBGPCursor bgpCursor1 = bgpDriverDAO.getBGProcesses();
    bgpCursor1.setServiceFilter("LOOP&PAUSE");
    bgpCursor1.setStatusFilter(BGPStatus.CANCELED);
    filterOutPut = bgpCursor1.getNext(0, 10);
    println( "count LOOP&PAUSE/CANCELED = " + bgpCursor1.getCount());
    printBGPProcess(filterOutPut);

    //filter by user and service
    IBGPCursor bgpCursor2 = bgpDriverDAO.getBGProcesses();
    bgpCursor2.setServiceFilter("ORPHANS_CLEANING");
    HashSet<String> userNames = new HashSet<>(Arrays.asList("Rahul Duldul"));
    bgpCursor2.setUserNameFilter( userNames);
    filterOutPut = bgpCursor2.getNext(0, 10);
    println( "count ORPHANS_CLEANING = " + bgpCursor2.getCount());
    printBGPProcess(filterOutPut);

    //filter by status and user
    IBGPCursor bgpCursor3 = bgpDriverDAO.getBGProcesses();
    bgpCursor3.setStatusFilter(BGPStatus.ENDED_ERRORS);
    bgpCursor3.setUserFilter(userIIDs);
    filterOutPut = bgpCursor3.getNext(0, 10);
    println( "count ALL/ERRORS = " + bgpCursor3.getCount());
    printBGPProcess(filterOutPut);

  }

  @Test
  public void getServices() throws RDBMSException {
    printTestTitle("getServices");
    BGPDriverDAO bgpDriverDAO = BGPDriverDAO.instance();
    List<String> services = bgpDriverDAO.getServices();
    println( Text.join( ",", services));
    assert(services.size()>0);
  }

  private void printBGPProcess(List<IBGProcessDTO> processes) throws CSFormatException
  {
    for (IBGProcessDTO bgp : processes) {
      println(bgp.toPXON());
    }
  }
}
