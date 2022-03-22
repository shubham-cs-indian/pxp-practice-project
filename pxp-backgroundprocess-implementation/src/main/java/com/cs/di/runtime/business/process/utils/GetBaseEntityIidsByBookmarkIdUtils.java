package com.cs.di.runtime.business.process.utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetBaseEntityIidsByBookmarkIdUtils {
  
  private static final String USERNAME      = "userName";
  private static final String TRANSACTIONID = "transactionID";
  private static final String SESSIONID     = "sessionID";
  private static final String LOGINTIME     = "loginTime";
  private static final String USERIID       = "userIID";
  private static final String USERID        = "userID";
  private static final String BOOKMARKID    = "bookmarkId";
  
  public IIdsListParameterModel triggerGetBaseEntityIidsByBookmarkId(IUserSessionDTO userSession,
      String organizationCode, String catalogCode, String baseLocaleID, String bookmarkId)
  {
    
    // setting transaction data
    Map<String, Object> map = new HashMap<String, Object>();
    // Setting userSession in map
    map.put(USERNAME, userSession.getUserName());
    map.put(TRANSACTIONID, userSession.getTransactionID());
    map.put(SESSIONID, userSession.getSessionID());
    map.put(LOGINTIME, userSession.getLoginTime());
    map.put(USERIID, userSession.getUserIID());
    map.put(USERID, "admin"); // Need to set usercode
    map.put(BOOKMARKID, bookmarkId);
    
    // REST call using URL
    try {
     return triggerBookmarkGetURL(prepareBusinessProcessWFURL(organizationCode, catalogCode, baseLocaleID),
          new ObjectMapper().writeValueAsString(map));
    }
    catch (CSInitializationException | JsonProcessingException e) {
      RDBMSLogger.instance()
          .exception(e);
    }
    return null;
    
  }
  
  /**
   * triggerBusinessProcess using URL and request provided
   * 
   * @param businessProcessURL
   * @param requestBody
   */
  private IIdsListParameterModel triggerBookmarkGetURL(String bookmarkGetURL, String requestBody)
  {
    IIdsListParameterModel parameterModel= null; 
    if (!bookmarkGetURL.isEmpty()) {
      WebTarget pxpServer = ClientBuilder.newClient()
          .target(bookmarkGetURL);
      RDBMSLogger.instance()
          .info("triggerBookmarkGetURL is %s", bookmarkGetURL);
      try {
        Response response = pxpServer.request()
            .post(Entity.entity(requestBody, MediaType.APPLICATION_JSON));
       String entity = response.readEntity(String.class);
       parameterModel = new ObjectMapper().readValue(entity, IdsListParameterModel.class);  
      }
      catch (Exception ex) {
        RDBMSLogger.instance()
            .exception(ex);
      }
    }
    else {
      RDBMSLogger.instance()
          .warn("triggerBookmarkGetURL is Empty ");
    }
    return parameterModel;
    
  }
  
  /**
   * Prepare URL to call
   * 
   * @return
   * @throws CSInitializationException
   */
  private String prepareBusinessProcessWFURL(String organizationCode, String catalogCode,
      String baseLocaleID) throws CSInitializationException
  {
    String tomcatURL = CSProperties.instance()
        .getString("tomcat.server.url");
    String warName = CSProperties.instance()
        .getString("tomcat.war.name");
    return String.format(
        "%s/%s/bgp/bookmarkbaseentityiids?organizationId=%s&physicalCatalogId=%s&dataLanguage=%s",
        tomcatURL, warName, organizationCode, catalogCode, baseLocaleID);
  }
  
}
