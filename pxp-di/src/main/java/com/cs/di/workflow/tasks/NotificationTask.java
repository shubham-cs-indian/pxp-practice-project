package com.cs.di.workflow.tasks;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.email.IEmailNotificationService;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.mail.IMailNotificationModel;
import com.cs.core.runtime.interactor.model.mail.MailNotificationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.di.config.interactor.users.IGetUserEmailId;
import com.cs.di.runtime.entity.dao.INotificationDAO;
import com.cs.di.runtime.entity.dto.NotificationDTO;
import com.cs.di.runtime.entity.idto.INotificationDTO;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("notificationTask")
public class NotificationTask extends AbstractTask {
  
  public static final String             NOTIFICATION_TASK    = "NOTIFICATION_TASK";
  public static final String             NOTIFICATION_SUBJECT = "NOTIFICATION_SUBJECT";
  public static final String             NOTIFICATION_BODY    = "NOTIFICATION_BODY";
  public static final String             NOTIFICATION         = "NOTIFICATION";
  public static final String             TO_DEFAULT_USER      = "TO_DEFAULT_USER";
  public static final String             RECIPIENTS_TO        = "RECIPIENTS_TO";
  public static final String             RECIPIENTS_CC        = "RECIPIENTS_CC";
  public static final String             RECIPIENTS_BCC       = "RECIPIENTS_BCC";


  public static final List<String>       INPUT_LIST           = Arrays.asList(NOTIFICATION_SUBJECT,
      NOTIFICATION_BODY, NOTIFICATION, TO_DEFAULT_USER, RECIPIENTS_TO, RECIPIENTS_CC, RECIPIENTS_BCC);
  
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(NOTIFICATION_TASK, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES          = Arrays.asList(EventType.BUSINESS_PROCESS);
  
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
    List<String> returnList = new ArrayList<>();
    String notificationSubject = (String) inputFields.get(NOTIFICATION_SUBJECT);
    if (StringUtils.isBlank(notificationSubject)) {
      returnList.add(NOTIFICATION_SUBJECT);
    }
    String notificationBody = (String) inputFields.get(NOTIFICATION_BODY);
    if (StringUtils.isBlank(notificationBody)) {
      returnList.add(NOTIFICATION_BODY);
    }
    @SuppressWarnings("unchecked")
    List<String> notificationOpt = (List<String>) inputFields.get(NOTIFICATION);
    if (notificationOpt.isEmpty()) {
      returnList.add(NOTIFICATION);
    }
    else if (notificationOpt.contains("email")) {
      List<String> recipientsTO = (List<String>) inputFields.get(RECIPIENTS_TO);
      String toDefaultUser = (String) inputFields.get(TO_DEFAULT_USER);
      if (validateEmails(recipientsTO, RECIPIENTS_TO, returnList) &&
              (toDefaultUser == null || !Boolean.parseBoolean((String) inputFields.get(TO_DEFAULT_USER)) && recipientsTO.isEmpty())) {
        returnList.add(RECIPIENTS_TO);
      }
      validateEmails((List<String>) inputFields.get(RECIPIENTS_CC), RECIPIENTS_CC, returnList);
      validateEmails((List<String>) inputFields.get(RECIPIENTS_BCC), RECIPIENTS_BCC, returnList);
    }
    return returnList;
  }

  private boolean validateEmails(List<String> emails, String fieldName, List<String> returnList) {
    if(emails == null){
      returnList.add(fieldName);
      return false;
    }
    for(String email: emails) {
      if(!validateEmailTypeInput(email)){
        returnList.add(fieldName);
        return false;
      }
    }
    return true;
  }

  private boolean validateEmailTypeInput(String email){
    if (!isRuntimeValue(email)) {
      return DiValidationUtil.emailValidator(email);
    }
    return true;
  }

  @Autowired
  IGetUserEmailId                 getUserEmailID;
  
  @Autowired
  IEmailNotificationService       emailNotificationService;
  
  @Autowired
  RDBMSComponentUtils             rdbmsComponentUtils;
  
  @Autowired
  protected TransactionThreadData transactionThreadData;
  
  IUserModel                      usermodel = new UserModel();
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    
    String notificationBody = (String) DiValidationUtil.validateAndGetRequiredString(model, NOTIFICATION_BODY);
    String notificationSubject = (String) DiValidationUtil.validateAndGetRequiredString(model, NOTIFICATION_SUBJECT);
    ArrayList<String> notificationOpt = (ArrayList<String>) DiValidationUtil.validateAndGetRequiredCollection(model, NOTIFICATION);
    
    if (executionStatusTable.isErrorOccurred()) {
      return;
    }
    
    usermodel = getUserDetailsByUserName(model, executionStatusTable);
    
    if (notificationOpt.contains("email")) {
      sendEmailNotification(model, notificationBody, notificationSubject, executionStatusTable);
    }
    if (notificationOpt.contains("user")) {
      try {
        TransactionData transactionData = DiUtils.getTransactionData(model);
        transactionThreadData.setTransactionData(transactionData);
        IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
        ILocaleCatalogDTO localeCatalogDTO = userSession.newLocaleCatalogDTO(transactionData.getDataLanguage(),
            transactionData.getPhysicalCatalogId(), transactionData.getOrganizationId());
        ILocaleCatalogDAO localeCatalogDAO = userSession.openLocaleCatalog((IUserSessionDTO) model.getWorkflowModel().getUserSessionDto(),
            localeCatalogDTO);
        INotificationDAO notificationDAO = localeCatalogDAO.openNotificationDAO();
        notificationDAO.createNotificationInstance(getnotifcationDTO(model, notificationBody));
      }
      catch (Exception e) {
        RDBMSLogger.instance().exception(e);
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN501, new String[] {});
      }
    }
  }
  
  /**
   * @param model
   * @param notificationBody
   * @return
   */
  private INotificationDTO getnotifcationDTO(WorkflowTaskModel model, String notificationBody)
  {
    INotificationDTO notificationDTO = new NotificationDTO();
    // long userIID = model.getWorkflowModel().getUserSessionDto().getUserIID();
    notificationDTO.setActedFor(usermodel.getUserIID());
    notificationDTO.setActedBy(usermodel.getUserIID());
    notificationDTO.setDescription("<pre style='white-space: pre-wrap;word-break: break-word'>" + notificationBody + "</pre>");
    notificationDTO.setAction("comment");
    notificationDTO.setStatus("unread");
    notificationDTO.setCreatedOn(System.currentTimeMillis());
    return notificationDTO;
  }
  
  /***
   * This method will return usermodel based on username
   * @param model
   * @param executionStatusTable
   * @return
   */
  private IUserModel getUserDetailsByUserName(WorkflowTaskModel model,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    String userName = model.getWorkflowModel().getTransactionData().getUserName();
    UserModel userModel = new UserModel();
    userModel.setUserName(userName);
    IUserModel userInfo = new UserModel();
    try {
      userInfo = getUserEmailID.execute(userModel);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN500, new String[] {});
    }
    return userInfo;
  }
  
  /***
   * This method will send email notification to logged in user when export of
   * any entity on UI is successfully completed.
   * 
   * @param model
   * @param notificationBody
   * @param notificationSubject
   * @param executionStatusTable
   */
  private void sendEmailNotification(WorkflowTaskModel model, String notificationBody, String notificationSubject,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {

    List<String> recipientsTO = (List<String>) validateAndGetRequiredCollectionAllowEmptyAndNull(model, RECIPIENTS_TO);
    List<String> recipientsCC = (List<String>) validateAndGetRequiredCollectionAllowEmptyAndNull(model, RECIPIENTS_CC);
    List<String> recipientsBCC = (List<String>) validateAndGetRequiredCollectionAllowEmptyAndNull(model, RECIPIENTS_BCC);
    Boolean toDefaultUser = DiValidationUtil.validateAndGetOptionalBoolean(model,TO_DEFAULT_USER);

    if (toDefaultUser == null || toDefaultUser) {
      recipientsTO.add(usermodel.getEmail());
    }
    IMailNotificationModel mailNotificationModel = new MailNotificationModel();
    mailNotificationModel.setRecipients(recipientsTO);
    mailNotificationModel.setRecipientsCC(recipientsCC);
    mailNotificationModel.setRecipientsBCC(recipientsBCC);
    
    StringBuilder MAIL_BODY_TO_SEND = new StringBuilder("");
    MAIL_BODY_TO_SEND.append("<pre>");
    MAIL_BODY_TO_SEND.append(notificationBody);
    MAIL_BODY_TO_SEND.append("</pre>");
    
    mailNotificationModel.setBody(MAIL_BODY_TO_SEND.toString());
    mailNotificationModel.setContentType("text/html");
    mailNotificationModel.setSubject(notificationSubject);
    try {
      emailNotificationService.execute(mailNotificationModel);
      executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN036, new String[] {});
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN501, new String[] {});
    }
    
  }

  /**
   * Method Validates all mandatory Collection Input and set error back to model
   * Empty collection and Null is allowed in this case
   *
   * @param model
   * @param paramName
   * @return
   */
  private Collection<?> validateAndGetRequiredCollectionAllowEmptyAndNull(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      param = new ArrayList<>();
      return (Collection<?>) param;
    }
    else if (!(param instanceof Collection<?>)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return (Collection<?>) param;
  }
}
