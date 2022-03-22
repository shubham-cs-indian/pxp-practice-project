package com.cs.core.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.cs.core.exception.base.smartdocument.UnableToConnectToServerException;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.realobjects.pdfreactor.webservice.client.Configuration;
import com.realobjects.pdfreactor.webservice.client.PDFreactor;

/**
 * Singleton to access PDF reactor server.
 * @author pranav.huchche
 */
@SuppressWarnings("unchecked")
public class CSPDFReactorServer {
  
  private static final String       PDF_REACTOR_BASE_URL         = "/service/rest";
  private static final String       PDF_REACTOR_CHECK_STATUS_URL = "/status";
  public static final String        GET_SMART_DOCUMENT           = "GetSmartDocument";
  
  private static CSPDFReactorServer instance;
  private PDFreactor                pdfReactor;
  
  /**
   * Private constructor for singleton implementation
   * @throws CSInitializationException
   * @throws UnableToConnectToServerException 
   * @throws CSFormatException 
   */
  private CSPDFReactorServer() throws CSInitializationException, UnableToConnectToServerException, CSFormatException {
    // Private constructor
    getPDFReactorInstance();
  }
  
  /**
   * Private constructor with pdf reactor configuration for singleton implementation
   * @param redererHostIp
   * @param redererPort
   * @throws CSInitializationException
   * @throws UnableToConnectToServerException
   * @throws CSFormatException
   */
  private CSPDFReactorServer(String redererHostIp, String redererPort) throws CSInitializationException, UnableToConnectToServerException, CSFormatException {
    connect(redererHostIp, redererPort);
  }

  private void getPDFReactorInstance() throws CSFormatException, CSInitializationException, UnableToConnectToServerException
  {
    Map<String, Object> smartDocument = CSConfigServer.instance().request(new HashMap<>(), GET_SMART_DOCUMENT, null);
    connect((String)smartDocument.get("rendererHostIp"), (String)smartDocument.get("rendererPort"));
  }

  /**
   * Call this method to get instance of this class.
   * @return void
   * @throws CSInitializationException
   * @throws UnableToConnectToServerException 
   * @throws CSFormatException 
   */
  public static CSPDFReactorServer createInstance() throws CSInitializationException, UnableToConnectToServerException, CSFormatException {
    if (instance == null) {
      instance = new CSPDFReactorServer();
    }
    return instance;
  }
  
  /**
   * Call this method to connect and then get instance of this class
   * @param redererHostIp
   * @param redererPort
   * @return
   * @throws CSFormatException 
   * @throws CSInitializationException 
   * @throws UnableToConnectToServerException 
   */
  public static CSPDFReactorServer createInstance(String redererHostIp, String redererPort) throws UnableToConnectToServerException, CSInitializationException, CSFormatException
  {
    instance = new CSPDFReactorServer(redererHostIp, redererPort);
    return instance;
  }
  
  /**
   * THis method is called to connect to PDF reactor server
   * @param rendererIP
   * @param rendererPort
   * @throws CSInitializationException
   * @throws UnableToConnectToServerException 
   */
  private void connect(String rendererIP, String rendererPort)
      throws UnableToConnectToServerException
  {
    HttpURLConnection connection = null;
    
    try {
      URL url = new URL(
          rendererIP + ":" + rendererPort + PDF_REACTOR_BASE_URL + PDF_REACTOR_CHECK_STATUS_URL);
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();
      if (connection.getResponseCode() == 200) {
        url = new URL(rendererIP + ":" + rendererPort + PDF_REACTOR_BASE_URL);
        this.pdfReactor = new PDFreactor(url.toString());
      }
    }
    catch (Exception e) {
      throw new UnableToConnectToServerException();
    }
    finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
  
  /**
   * This method is used to covert to binary to write pdf
   * @param configuration
   * @return
   * @throws Exception
   */
  public byte[] convertAsBinary(Configuration configuration) throws Exception
  {
    if (pdfReactor == null) {
      throw new UnableToConnectToServerException();
    }
    
    return pdfReactor.convertAsBinary(configuration);
  }
  
  /**
   * This method is to get status of pdf reactor server
   * @throws Exception
   */
  public void getStatus() throws Exception
  {
    if (pdfReactor == null) {
      throw new UnableToConnectToServerException();
    }
    
    pdfReactor.getStatus();
  }

}
