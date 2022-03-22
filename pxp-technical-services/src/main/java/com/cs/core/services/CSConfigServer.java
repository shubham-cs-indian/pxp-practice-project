package com.cs.core.services;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CSConfigServer {

  public static final String DEFAULT_LOCALEID = "en_US";
  private static final String CSDATABASE = "cs";
  private static CSConfigServer Instance = null;
  private final HttpClient orientClient;
  private final String orientConnectionString;
  private final String orientUser;
  private final String orientPassword;

  private CSConfigServer() throws CSInitializationException {
    
	String connectionStr = CSProperties.instance().getString("orientdb.connectionString");
    orientConnectionString = connectionStr.replaceAll("/$", "");
    orientUser = CSProperties.instance().getString("orientdb.user");
    orientPassword = CSProperties.instance().getString("orientdb.password");
    orientClient = HttpClient.newBuilder()
            .authenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(orientUser, orientPassword.toCharArray());
                }
            })
            .build();

		/*
		 * ClientConfig clientConfig = new ClientConfig(); HttpAuthenticationFeature
		 * feature = HttpAuthenticationFeature.basic(orientUser, orientPassword);
		 * clientConfig.register(feature);
		 */
  }

  public static CSConfigServer instance() throws CSInitializationException {
    if (Instance == null) {
      synchronized (CSConfigServer.class) {
        if (Instance == null) {
          Instance = new CSConfigServer();
        }
      }
    }
    return Instance;
  }

  // TODO: This Temporary Method Until Transaction Data for this is implemented
  /*
   * Override this method and add any extra headers you need to Query ODB Plugin.
   * @return
   */
  protected HashMap<String, String> fillHeadersForODB(String localeId) {
    HashMap<String, String> headers = new HashMap<>();
    // TODO: HARDCODED FOR NOW GET FROM TRANSACTION DATA.
    localeId = localeId == null ? DEFAULT_LOCALEID : localeId;
    headers.put("uiLanguage", localeId);
    headers.put("dataLanguage", localeId);
    headers.put("Content-Type", "application/json");
    return headers;
  }

  /**
   * Internal request with added header information
   *
   * @param requestModel
   * @param usecase
   * @param requestType
   * @param headers
   * @return
   * @throws CSFormatException
 * @throws CSInitializationException 
 * @throws RDBMSException 
 * @throws InterruptedException 
 * @throws IOException 
 * @throws ParseException 
   */
  private JSONObject executeRequest(Map<String, Object> requestModel, String usecase,  Map<String, String> headers) throws CSFormatException, CSInitializationException {
    //Builder request = orientClient.target(usecase).request();
    List<String> headersKeyAndValue = new ArrayList<>();
    headers.entrySet().forEach((header) -> {
    	headersKeyAndValue.add(header.getKey());
    	headersKeyAndValue.add(header.getValue());
    });
    
    JSONObject reqJSONObject = new JSONObject(requestModel);

    HttpRequest request1 = HttpRequest.newBuilder(URI.create(usecase))
    .headers(headersKeyAndValue.toArray(new String[0]))
    .POST(BodyPublishers.ofString(reqJSONObject.toJSONString()))
    .build();
    HttpResponse<String> response = null;
		try {
			response = orientClient.send(request1, BodyHandlers.ofString(Charset.forName("UTF-8")));
		} catch (IOException e1) {
			    RDBMSLogger.instance().exception(e1);
				throw new CSInitializationException("Failed to connect to OrientDB. Reason: " + e1.getMessage()) ;
		} catch (InterruptedException e1) {
			throw new CSInitializationException("Connection to OrientDB Interrupted. Reason: " + e1.getMessage()) ;	
		}
	
		/*
		 * Response response =
		 * request.post(Entity.entity(JSONObject.toJSONString(requestModel),
		 * requestType)); JSONContent responseContent = new
		 * JSONContent(response.readEntity(String.class)); // TODO: manage exception
		 * content to raise a CSFormatException
		    return responseContent.toJSONObject();*/
		JSONObject json = null; 
   try { 
   JSONParser parser = new JSONParser();
		json = (JSONObject) parser.parse(response.body());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		throw new CSFormatException("Failed to Parse Json Response From ORient Server");
	}
    return json;
  }

  /**
   * Specific request to the configuration database
   *
   * @param requestModel Model required by Plugin
   * @param usecase The Url of the specific Plugin
   * @param localeId
   * @return Response of the specific request.
   * @throws CSFormatException
   * @throws CSInitializationException
 * @throws ParseException 
 * @throws InterruptedException 
 * @throws IOException 
 * @throws RDBMSException 
   */
  public JSONObject request(Map<String, Object> requestModel, String usecase, String localeId)
          throws CSFormatException, CSInitializationException {
    String orientURL = String.format("%s/%s/%s/", orientConnectionString, usecase, CSDATABASE);
    return executeRequest(requestModel, orientURL,  fillHeadersForODB(localeId));
  }
}
