package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.usecase.auditlog.IDeleteAuditLogExport;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("HouseKeepingTask")
public class HouseKeepingTask extends AbstractTask{
  
  public static final String             HOUSEKEEPING_API              = "HOUSE_KEEPING_API";
  public static final String             CLEAR_LOG_OFFSET              = "CLEAR_LOG_OFFSET";
  public static final String             AUDIT_LOG_EXPORT_HOUSEKEEPING = "auditLogExportHousekeeping";
  public static final List<WorkflowType> WORKFLOW_TYPES                = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                   = Arrays.asList(EventType.BUSINESS_PROCESS);
  public static final List<String>       INPUT_LIST                    = Arrays.asList(HOUSEKEEPING_API, CLEAR_LOG_OFFSET);
  public static final List<String>       OUTPUT_LIST                   = Arrays.asList(EXECUTION_STATUS);

  @Autowired
  IDeleteAuditLogExport deleteAuditLogExport;

  @Override
  public void executeTask(WorkflowTaskModel model)
  {
     String houseKeepingAPI = (String) model.getInputParameters().get(HOUSEKEEPING_API);
     String clearLogOffset = (String) model.getInputParameters().get(CLEAR_LOG_OFFSET);
     IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();

     if(StringUtils.isBlank(clearLogOffset)) {
       executionStatusTable.addError(new ObjectCode[] {ObjectCode.INPUT_PARAM_VALUE}, MessageCode.GEN004, new String[] {clearLogOffset});
       return;
     }

     if(!StringUtils.isNumeric(clearLogOffset)) {
       executionStatusTable.addError(new ObjectCode[] {ObjectCode.INPUT_PARAM_VALUE}, MessageCode.GEN005, new String[] {clearLogOffset});
       return;
     }
     
     int offset = Integer.parseInt(clearLogOffset);
     
     if (offset <= 0) {
       executionStatusTable.addError(new ObjectCode[] {ObjectCode.INPUT_PARAM_VALUE}, MessageCode.GEN032, new String[] {clearLogOffset});
       return;
     }
     
     switch(houseKeepingAPI) {
       case AUDIT_LOG_EXPORT_HOUSEKEEPING:
        try {
          housekeepingForAuditLogExport(offset);
        }
        catch (Exception e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
        }
         executionStatusTable.addSuccess(new ObjectCode[] {ObjectCode.MESSAGE_TYPE}, MessageCode.GEN033, new String[] {AUDIT_LOG_EXPORT_HOUSEKEEPING});
         break;
         
      default:
        break;
     }
     
  }
  
  private void housekeepingForAuditLogExport(int clearLogOffset) throws Exception
  {
    IAuditLogExportDAO auditLogExportDAO = RDBMSUtils.newUserSessionDAO().newAuditLogExportDAO();
    List<String> assetIds = auditLogExportDAO
        .getAuditLogExportTrackerAssetIdsForHouseKeeping(getHouseKeepingTime(new Date(), -clearLogOffset));    
    
    if (!assetIds.isEmpty()) {
      IIdsListParameterModel requestModel = new IdsListParameterModel();
      requestModel.setIds(assetIds);
      deleteAuditLogExport.execute(requestModel);
    }
  }

  private long getHouseKeepingTime(Date date, int days)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days);
    return cal.getTime().getTime();
  }

  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }

  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }

  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }

  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }

  @Override
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }

}
