package com.cs.ui.runtime.controller.usecase.workflow.callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.workflow.trigger.standard.BusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.ActionSubTypes;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerService;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * 
 * This Controller triggers all AFTER CREATE/SAVE 
 * Business process WF when BGP import/transfer
 * Task is executed  
 *
 */
@RestController
@RequestMapping(value = "/bgp")
@SuppressWarnings("unchecked")
public class TriggerBusinessProcessWFController extends BaseController implements IRuntimeController {
  
  private static final String    BASE_TYPE         = "baseType";
  private static final String    ACTION            = "businessProcessActionType";
  private static final String    KLASSIDS          = "klassIds";
  private static final String    TAXONOMYIDS       = "taxonomyIds";
  private static final String    USERNAME          = "userName";
  private static final String    TRANSACTIONID     = "transactionID";
  private static final String    SESSIONID         = "sessionID";
  private static final String    LOGINTIME         = "loginTime";
  private static final String    USERIID           = "userIID";
  private static final String    TAGIDS            = "tagIds";
  private static final String    ATTRIBUTEIDS      = "attributeIds";
  private static final String    ACTIONSUBTYPE     = "actionSubType";
  private static final String    KLASS_INSTANCE_ID = "klassInstanceId";
  private static final String    USERID            = "userID";
  private static final String    USECASE            = "usecase";
  private static final String ACTIONSUBTYPE_ARRAY     = "actionSubTypeArray";
  @Autowired
  IBusinessProcessTriggerService businessProcessEvent;
  
  @Autowired
  TransactionThreadData transactionThreadData;
  
  @RequestMapping(value = "/businessprocess", method = RequestMethod.POST)
  public void execute(@RequestParam String organizationId,
      @RequestParam String physicalCatalogId, @RequestParam String dataLanguage, @RequestBody HashMap<String, Object> requestBody)
      throws Exception
  {
    List<String> subtypes = (List<String>)requestBody.get(ACTIONSUBTYPE_ARRAY);
    if(subtypes==null || subtypes.size()<2) {
      IBusinessProcessTriggerModel businessProcessEventModel = prepareBusinessProcessModel(organizationId, physicalCatalogId, dataLanguage,
              requestBody);
      businessProcessEvent.triggerQualifyingWorkflows(businessProcessEventModel);
    } else {
      List<String> executedProcessIds = new ArrayList<>();
      for(String actionSubtype :subtypes) {
        requestBody.put(ACTIONSUBTYPE, actionSubtype);
        businessProcessEvent.triggerQualifyingWorkflows(prepareBusinessProcessModel(organizationId, physicalCatalogId, dataLanguage,
            requestBody), executedProcessIds);
      }
    }
  }

 
  private IBusinessProcessTriggerModel prepareBusinessProcessModel(String organizationId, String physicalCatalogId, String dataLanguage,
      HashMap<String, Object> requestBody)
  {
    String userId = (String) requestBody.get(USERID);
    IBusinessProcessTriggerModel businessProcessEventModel = new BusinessProcessTriggerModel();
    IUserSessionDTO userSessionDTO = createUserSessionData(requestBody);
    TransactionData transactionData = createTransactionData(organizationId, physicalCatalogId, dataLanguage, userSessionDTO, userId);
    businessProcessEventModel.setUserSession(userSessionDTO);
    businessProcessEventModel.setTransactionData(transactionData);
    if (BusinessProcessActionType.AFTER_SAVE.toString().equals((String) requestBody.get(ACTION))) {
      businessProcessEventModel.setBusinessProcessActionType(BusinessProcessActionType.AFTER_SAVE);
      if (ActionSubTypes.AFTER_PROPERTIES_SAVE.toString().equals((String) requestBody.get(ACTIONSUBTYPE)))
        businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_PROPERTIES_SAVE);
      if (ActionSubTypes.AFTER_CLASSIFICATION_SAVE.toString().equals((String) requestBody.get(ACTIONSUBTYPE)))
        businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_CLASSIFICATION_SAVE);
      if (ActionSubTypes.AFTER_RELATIONSHIP_SAVE.toString().equals((String) requestBody.get(ACTIONSUBTYPE)))
        businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_RELATIONSHIP_SAVE);
    }
    if (BusinessProcessActionType.AFTER_CREATE.toString().equals((String) requestBody.get(ACTION)))
      businessProcessEventModel.setBusinessProcessActionType(BusinessProcessActionType.AFTER_CREATE);
    
    // common for both After Save and Create
    businessProcessEventModel.setBaseType((String) requestBody.get(BASE_TYPE));
    businessProcessEventModel.setKlassIds((List<String>) requestBody.get(KLASSIDS));
    businessProcessEventModel.setTaxonomyIds((List<String>) requestBody.get(TAXONOMYIDS));
    businessProcessEventModel.setAttributeIds((List<String>) requestBody.get(ATTRIBUTEIDS));
    businessProcessEventModel.setTagIds((List<String>) requestBody.get(TAGIDS));
    businessProcessEventModel.setKlassInstanceId(String.valueOf(requestBody.get(KLASS_INSTANCE_ID)));
    if (Usecase.IMPORT.toString().equals((String) requestBody.get(USECASE)))
      businessProcessEventModel.setUsecase(Usecase.IMPORT);
    else if (Usecase.TRANSFER.toString().equals((String) requestBody.get(USECASE)))
      businessProcessEventModel.setUsecase(Usecase.TRANSFER);
    else if (Usecase.OTHERS.toString().equals((String) requestBody.get(USECASE)))
      businessProcessEventModel.setUsecase(Usecase.OTHERS);
    // Trigger Business Process WF using request Model
    return businessProcessEventModel;
  }
  
  /**
   * Create transaction data 
   * @param organizationId
   * @param physicalCatalogId
   * @param dataLanguage
   * @param userSessionDTO
   * @return
   */
  private TransactionData createTransactionData(String organizationId, String physicalCatalogId, String dataLanguage,
      IUserSessionDTO userSessionDTO, String userId)
  {
    
    TransactionData transactionData = new TransactionData();
    transactionData.setDataLanguage(dataLanguage);
    transactionData.setPhysicalCatalogId(physicalCatalogId);
    transactionData.setOrganizationId(organizationId);
    transactionData.setUserId(userId);
    transactionData.setUserName(userSessionDTO.getUserName());
    transactionThreadData.setTransactionData(transactionData);
    return transactionData;
  }

  /**
   * Create user session
   * @param requestBody
   * @return
   */
  private IUserSessionDTO createUserSessionData(HashMap<String, Object> requestBody)
  {
    UserDTO user = new UserDTO();
    user.setUserName((String) requestBody.get(USERNAME));
    user.setIID((Integer) requestBody.get(USERIID));
    SessionDTO session = new SessionDTO((String) requestBody.get(SESSIONID), (long) requestBody.get(LOGINTIME));
    IUserSessionDTO userSessionDTO = new UserSessionDTO(session, user, LogoutType.UNDEFINED, 0L, null);
    userSessionDTO.setTransactionId(TRANSACTIONID);
    return userSessionDTO;
  }
}


