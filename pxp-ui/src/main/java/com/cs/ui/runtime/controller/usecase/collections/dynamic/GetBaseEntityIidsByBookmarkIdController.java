package com.cs.ui.runtime.controller.usecase.collections.dynamic;

import java.util.HashMap;

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
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeForBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetInstanceTreeForBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.pim.runtime.dynamiccollection.IGetBaseEntityIidsByBookmarkIdService;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/bgp")
public class GetBaseEntityIidsByBookmarkIdController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  TransactionThreadData                transactionThreadData;
  
  @Autowired
  protected ISessionContext            context;
  
  @Autowired
  IGetBaseEntityIidsByBookmarkIdService getBaseEntityIIDsForBookmarkService;
  
  private static final String          USERNAME      = "userName";
  private static final String          TRANSACTIONID = "transactionID";
  private static final String          SESSIONID     = "sessionID";
  private static final String          LOGINTIME     = "loginTime";
  private static final String          USERIID       = "userIID";
  private static final String          USERID        = "userID";
  private static final String          BOOKMARKID    = "bookmarkId";
  
  @RequestMapping(value = "/bookmarkbaseentityiids", method = RequestMethod.POST)
  public IIdsListParameterModel execute(@RequestParam String organizationId,
      @RequestParam String physicalCatalogId, @RequestParam String dataLanguage,
      @RequestBody HashMap<String, Object> requestBody) throws Exception
  {
    IGetInstanceTreeForBookmarkRequestModel dataModel = prepareRequestModle(organizationId,
        physicalCatalogId, dataLanguage, requestBody);
    
    return getBaseEntityIIDsForBookmarkService.execute(dataModel);    
  }
  
  private IGetInstanceTreeForBookmarkRequestModel prepareRequestModle(String organizationId,
      String physicalCatalogId, String dataLanguage, HashMap<String, Object> requestBody)
  {
    String userId = (String) requestBody.get(USERID);
    IUserSessionDTO userSessionDTO = createUserSessionData(requestBody);
    createTransactionData(organizationId, physicalCatalogId, dataLanguage, userSessionDTO, userId);
    IGetInstanceTreeForBookmarkRequestModel dataModel = new GetInstanceTreeForBookmarkRequestModel();
    dataModel.setBookmarkId((String) requestBody.get(BOOKMARKID));
    
    return dataModel;
  }
  
  /**
      * Create transaction data
      * 
      * @param organizationId
      * @param physicalCatalogId
      * @param dataLanguage
      * @param userSessionDTO
      * @return
      */

  private TransactionData createTransactionData(String organizationId, String physicalCatalogId,
      String dataLanguage, IUserSessionDTO userSessionDTO, String userId)
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
    * 
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
    context.setUserSessionDTO(userSessionDTO);
    context.setUserId((String) requestBody.get(USERID));
    context.setUserName((String) requestBody.get(USERNAME));
    return userSessionDTO;
  }
}
      