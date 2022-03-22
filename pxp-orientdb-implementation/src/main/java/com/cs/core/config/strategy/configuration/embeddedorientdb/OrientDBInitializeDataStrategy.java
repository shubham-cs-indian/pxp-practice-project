package com.cs.core.config.strategy.configuration.embeddedorientdb;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.ApplicationStatusForMigration;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.client.http.HttpResponse;

@Component("initializeConfigDataStrategy")
public class OrientDBInitializeDataStrategy extends OrientDBBaseStrategy
    implements IInitializeDataStrategy {
  
  public static final String              useCase = "CreateVertexTypes";
  
  @Autowired
  protected String                        orientDBPassword;
  
  @Autowired
  protected String                        orientDBUser;
  @Autowired
  protected ApplicationStatusForMigration applicationStatus;
  @Autowired
  boolean                                 executeWithLoadBalancer;
  
  @Override
  public IModel execute(IModel model) throws Exception
  {
    try {
      getOrCreateDatabase();
      /*
       * OServerAdmin orientServer = new OServerAdmin( "remote:" + orientDBHost + ":"
       * + orientDBBinaryPort + "/" + orientDBdatabase).connect(orientDBUser,
       * orientDBPassword); if (!orientServer.existsDatabase()) {
       * orientServer.createDatabase(orientDBdatabase, "graph", "plocal").close(); }
       */
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    execute(useCase, new HashMap<>());
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public void getOrCreateDatabase() throws Exception
  {
    String uri = "/" + "listDatabases";
    String response;
    if (executeWithLoadBalancer) {
      HttpResponse httpResponse = executeWithLoadBalancer(uri, null, "GET", null);
      response = getResponseString(httpResponse.getInputStream());
    }
    else {
      uri = "http://" + orientDBHost + ":" + orientDBPort + uri;
      HttpURLConnection connection = executeWithoutLoadBalancer(new URI(uri), null, "GET");
      Authenticator.setDefault(getAuthenticator);
      response = getResponseString(connection.getInputStream());
    }
    
    Map<String, Object> postData = ObjectMapperUtil.readValue(response,
        new TypeReference<Map<String, Object>>()
        {
          
        });
    List<String> databases = (List<String>) postData.get("databases");
    if (!databases.contains(orientDBdatabase)) {
      createDatabase();
      applicationStatus.setCleanInstallation(true);
    }
  }
  
  @SuppressWarnings("unused")
  public void createDatabase() throws Exception
  {
    String uri = "/database/" + orientDBdatabase + "/plocal";
    int statusCode;
    String contentJson = new HashMap<>().toString();
    if (executeWithLoadBalancer) {
      HttpResponse httpResponse = executeWithLoadBalancer(uri.toString(), contentJson, "POST",
          null);
      String response = getResponseString(httpResponse.getInputStream());
      statusCode = httpResponse.getStatus();
    }
    else {
      uri = "http://" + orientDBHost + ":" + orientDBPort + uri;
      HttpURLConnection connection = executeWithoutLoadBalancer(new URI(uri), contentJson, "POST");
      Authenticator.setDefault(getAuthenticator);
      String response = getResponseString(connection.getInputStream());
      statusCode = connection.getResponseCode();
    }
    if (statusCode == HttpURLConnection.HTTP_OK) {
      RDBMSLogger.instance().info("Database " + orientDBdatabase + " created successfully");
    }
  }
}
