package com.cs.core.bgprocess.services.purging;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;

public class OrphansCleaning extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                   batchSize;
  private int                   currentBatchNo  = 0;
  private final Set<Long>       propertyIidList = new HashSet<>();
  private static final String[] STEP_TITLES     = { "Tags cleaning", "Values cleaning",
      "Relations side 1 cleaning", "Relations side 2 cleaning", "Collection cleaning", "Property cleaning"};
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    // Get the parameters from the properties file
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    setCurrentBatchNo(currentBatchNo);
    // get the runtime data if property delete
    getRuntimeData(jobData.getRuntimeData());
    jobData.getProgress().initSteps(STEP_TITLES); // The service must declare its steps
    jobData.getLog().progress("Job started with iid=%d", jobData.getJobIID());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d / batchSize=%d ", getService(), getIID(), batchSize);
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      return jobData.getStatus();
    }
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    switch (currentStepNo) {
      case 1:
        orphanBatchDelete("fn_deleteOrphanTagRecords");
        break;
      case 2:
        orphanBatchDelete("pxp.fn_deleteOrphanValueRecords");
        break;
      case 3:
        orphanBatchDelete("pxp.fn_deleteOrphanSide1Records");
        break;
      case 4:
        orphanBatchDelete("pxp.fn_deleteOrphanSide2Records");
        break;
      case 5:
        orphanBatchDelete("pxp.fn_deleteOrphanCollectionBaseEntityLink");
        break;
      case 6:
        orphanPropertiesDelete();
        break;
      case 7:
        // completion must be achieved at this level
        break;
      default:
        throw new RDBMSException( 100, "Programm Error", "Unexpected step-no: " + currentStepNo);
    }
    setCurrentBatchNo(++currentBatchNo);
    RDBMSLogger.instance()
        .info("\t->job %s / iid=%d step=%s purging batch %d of %d lines, step completion: %d",
            getService(), getIID(), jobData.getProgress().getCurrentStepName(),
            currentBatchNo, batchSize, jobData.getProgress().getPercentageCompletion());
    // here since it is automatically updated at the beginning of any batch
    if (jobData.getProgress().getPercentageCompletion() == 100)
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
    else
      jobData.setStatus(BGPStatus.RUNNING);
    return jobData.getStatus();
  }
  
  /**
   * Complete the step progress on the current operation
   *
   * @param deletionCount the number of deleted rows
   * @param step the current step to be updated
   */
  private void setCompletion(int deletionCount)
  {
    jobData.getLog().progress("Step %s, %d lines proceeded", 
            jobData.getProgress().getCurrentStepName(), deletionCount);
    if (deletionCount >= batchSize || deletionCount == 0) {
      jobData.getLog().progress("Step %s ENDED.", jobData.getProgress().getCurrentStepName());
      jobData.getProgress().incrStepNo();
    }
  }
  
  /**
   * @param psqlDeleteFunction
   *          the name of the PSQL function to call
   * @throws RDBMSException
   */
  public synchronized void orphanBatchDelete(String psqlDeleteFunction) throws RDBMSException
  {
    int[] completion = { 0 };
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction(ResultType.INT, psqlDeleteFunction)
              .setInput(ParameterType.INT, batchSize)
              .execute();
          setCompletion(result.getInt());
        });
  }
  
  /**
   * delete the line of records from event queue
   *
   * @throws RDBMSException
   */
  public synchronized void orphanPropertiesDelete() throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          currentConn.getProcedure( "pxp.sp_deleteProperty")
              .setInput(ParameterType.INT_ARRAY, propertyIidList)
              .execute();
        });
    setCompletion(batchSize); // force completion
  }
  
  /**
   * Collect the runtime property iids to be deleted from json
   */
  private void getRuntimeData(IJSONContent runTimeData) throws CSFormatException
  {
    JSONContentParser obj = new JSONContentParser(((JSONContent) runTimeData).toJSONObject());
    JSONArray numberArr = obj.getJSONArray("runtimeData");
    propertyIidList.clear();
    numberArr.forEach((propertyIID) -> {
      propertyIidList.add((Long) propertyIID);
    });
  }
}
