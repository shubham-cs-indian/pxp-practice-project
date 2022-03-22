package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IBGPProgressDTO;
import com.cs.core.bgprocess.idto.IBGPSummaryDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.TextArchive;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for PXP background process information
 *
 * @author vallee
 */
public class BGProcessDTO extends RDBMSRootDTO implements IBGProcessDTO {
  
  private String              service       = "";
  private BGPPriority         userPriority  = BGPPriority.UNDEFINED;
  private String              callbackURI   = "";
  private long                runningTime   = 0;
  private long                startTime     = 0;
  private long                endTime       = 0;
  private long                createdTime   = System.currentTimeMillis();
  private UserDTO             user          = new UserDTO();
  private BGPStatus           bgpStatus     = BGPStatus.UNDEFINED;
  private JSONContent         entryData     = new JSONContent();
  private JSONContent         runtimeData   = new JSONContent();
  private BGPProgressDTO      progressData  = null;
  private final BGPSummaryDTO summaryData   = new BGPSummaryDTO();
  private BGPLog              log           = new BGPLog(summaryData);
  private List<String>        successIds    = new ArrayList<>();
  private List<String>        failedIds     = new ArrayList<>();
  
  private static final String USER_PRIORITY = PXONTag.priority.toReadOnlyTag();
  private static final String USER          = PXONTag.user.toReadOnlyCSETag();
  private static final String CALLBACK      = PXONTag.callback.toReadOnlyTag();
  private static final String BGP_STATUS    = PXONTag.status.toReadOnlyTag();
  private static final String ENTRY         = PXONTag.entry.toJSONContentTag();
  private static final String RUNTIME_DATA  = PXONTag.runtime.toJSONContentTag();
  private static final String PROGRESS_DATA = PXONTag.progress.toJSONContentTag();
  private static final String SUMMARY_DATA  = PXONTag.report.toJSONContentTag();
  private static final String RUN_TIME      = PXONTag.runtime.toReadOnlyTag();
  private static final String CREATED_TIME  = PXONTag.created.toReadOnlyTag();
  private static final String START_TIME    = PXONTag.start.toReadOnlyTag();
  private static final String END_TIME      = PXONTag.end.toReadOnlyTag();
  
  /**
   * Enable default constructor
   */
  public BGProcessDTO()
  {
  }
  
  /**
   * Constructor with minimal entry properties
   *
   * @param service
   * @param user
   * @param priority
   * @param callback
   */
  public BGProcessDTO(String service, UserDTO user, BGPPriority priority, String callback)
  {
    this.service = service;
    this.user = user;
    this.userPriority = priority;
    this.callbackURI = callback;
    this.progressData = new BGPProgressDTO(service); // default
  }
  
  /**
   * Constructor from reading the DB
   *
   * @param result
   * @throws SQLException
   * @throws CSFormatException
   */
  public BGProcessDTO(IResultSetParser result) throws SQLException, CSFormatException
  {
    super(result.getLong("jobIID"));
    this.user = new UserDTO(result);
    this.service = result.getString("service");
    this.bgpStatus = BGPStatus.valueOf(result.getInt("status"));
    this.callbackURI = result.getString("callbackuri");
    this.userPriority = BGPPriority.valueOf(result.getInt("userpriority"));
    this.entryData = new JSONContent(result.getStringFromJSON("entrydata"));
    this.runtimeData = new JSONContent(result.getStringFromJSON("runtimedata"));
    if (this.progressData == null)
      this.progressData = new BGPProgressDTO(service);
    this.progressData.fromJSON(result.getStringFromJSON("progressdata"));
    if (this.progressData.getTotalStepNb() == 0) // initialization case
      this.progressData.initSteps(this.service); // default initialization
    this.summaryData.fromJSON(result.getStringFromJSON("summarydata"));
    this.runningTime = result.getLong("runningtime");
    this.createdTime = result.getLong("created");
    this.startTime = result.getLong("started");
    this.endTime = result.getLong("ended");
    byte[] logBlob = result.getBinaryBlob("logdata");
    if (logBlob != null && logBlob.length > 0)
      this.log = new BGPLog(summaryData, TextArchive.unzip(logBlob));
    else
      this.log = new BGPLog(summaryData);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Tracking);
    cse.setIID(getIID());
    cse.setSpecification(Keyword.$type, service);
    return cse; // returns meta information
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    setIID(gcse.getIID());
    service = gcse.getSpecification(Keyword.$type);
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    user.fromCSExpressID(parser.getCSEElement(USER));
    bgpStatus = parser.getEnum(BGPStatus.class, BGP_STATUS);
    callbackURI = parser.getString(CALLBACK);
    userPriority = parser.getEnum(BGPPriority.class, USER_PRIORITY);
    entryData = parser.getJSONContent(ENTRY);
    runtimeData = parser.getJSONContent(RUNTIME_DATA);
    progressData.fromJSON(parser.getJSONParser(PROGRESS_DATA));
    summaryData.fromJSON(parser.getJSONParser(SUMMARY_DATA));
    runningTime = parser.getLong(RUN_TIME);
    createdTime = parser.getLong(CREATED_TIME);
    startTime = parser.getLong(START_TIME);
    endTime = parser.getLong(END_TIME);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(), user.toPXONBuffer(),
        JSONBuilder.newJSONField(BGP_STATUS, bgpStatus),
        JSONBuilder.newJSONField(CALLBACK, callbackURI),
        JSONBuilder.newJSONField(USER_PRIORITY, userPriority),
        JSONBuilder.newJSONField(ENTRY, entryData),
        JSONBuilder.newJSONField(RUNTIME_DATA, runtimeData),
        JSONBuilder.newJSONField(PROGRESS_DATA, progressData.toJSONBuffer()),
        JSONBuilder.newJSONField(SUMMARY_DATA, summaryData.toJSONBuffer()),
        JSONBuilder.newJSONField(RUN_TIME, runningTime),
        JSONBuilder.newJSONField(CREATED_TIME, createdTime),
        JSONBuilder.newJSONField(START_TIME, startTime),
        JSONBuilder.newJSONField(END_TIME, endTime));
  }
  
  @Override
  public long getJobIID()
  {
    return getIID();
  }
  
  @Override
  public String getService()
  {
    return service;
  }
  
  @Override
  public BGPStatus getStatus()
  {
    return bgpStatus;
  }
  
  /**
   * if at least one step returns failed, then status is FAILED
   *
   * @param status
   *          overwritten status
   */
  public void setStatus(BGPStatus status)
  {
    bgpStatus = status;
  }
  
  /**
   * @return job runtime data
   */
  public IJSONContent getRuntimeData()
  {
    return runtimeData;
  }
  
  /**
   * @param jsonContent
   *          overwritten runtime data
   * @throws CSFormatException
   */
  public void setRuntimeData(String jsonContent) throws CSFormatException
  {
    runtimeData.fromString(jsonContent);
  }
  
  @Override
  public IJSONContent getEntryData()
  {
    return entryData;
  }
  
  public void setEntryData(String jsonContent) throws CSFormatException
  {
    entryData.fromString(jsonContent);
  }
  
  @Override
  public IBGPProgressDTO getProgress()
  {
    return progressData;
  }
  
  /**
   * Shortcut to increment the batch no
   */
  public void incrBatchNo()
  {
    progressData.incrBatchNo();
  }
  
  @Override
  public IBGPSummaryDTO getSummary()
  {
    return summaryData;
  }
  
  /**
   * @return the process log object
   */
  public BGPLog getLog()
  {
    return this.log;
  }
  
  @Override
  public synchronized long getCreatedTime()
  {
    return createdTime;
  }
  
  @Override
  public synchronized long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public synchronized long getEndTime()
  {
    return endTime;
  }
  
  /**
   * set up created time at first insert in RDBMS
   */
  public synchronized void setCreatedTimeNow()
  {
    createdTime = System.currentTimeMillis();
  }
  
  /**
   * set up started time now only if not updated
   */
  public synchronized void initStartTimeNow()
  {
    if (this.startTime == 0) {
      startTime = System.currentTimeMillis();
    }
  }
  
  /**
   * set up ended time now
   */
  public synchronized void setEndTimeNow()
  {
    if (this.endTime == 0) {
      endTime = System.currentTimeMillis();
    }
  }
  
  @Override
  public long getRunningTime()
  {
    if (runningTime == 0 && startTime > 0)
      return (endTime > 0 ? endTime - startTime : System.currentTimeMillis() - startTime);
    return runningTime;
  }
  
  /**
   * @param moreTime
   *          incremented time to be added to running time
   */
  public void addRunningTime(long moreTime)
  {
    runningTime += moreTime;
  }
  
  @Override
  public IUserDTO getUser()
  {
    return this.user;
  }
  
  @Override
  public BGPPriority getUserPriority()
  {
    return this.userPriority;
  }
  
  @Override
  public String getCallbackURLTemplate() {
    return this.callbackURI;
  }

  @Override
  public List<String> getSuccessIds()
  {
    return successIds;
  }

  @Override
  public void setSuccessIds(List<String> successIds)
  {
    this.successIds = successIds;
  }

  @Override
  public List<String> getFailedIds()
  {
    return failedIds;
  }

  @Override
  public void setFailedIds(List<String> failedIds)
  {
    this.failedIds = failedIds;
  }
}
