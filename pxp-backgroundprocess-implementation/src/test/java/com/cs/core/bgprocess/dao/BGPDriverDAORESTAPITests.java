package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.BGProcessRESTServer;
import static com.cs.core.bgprocess.idto.IBGProcessDTO.JOB_PLACEHOLDER;
import static com.cs.core.bgprocess.idto.IBGProcessDTO.STATUS_PLACEHOLDER;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class BGPDriverDAORESTAPITests extends AbstractBGProcessTests {
  
  public static Client        client = null;
  private static final String SUBMIT_BGP_PROCESS = "http://localhost:5080/REST/bgp/submit";
  private static final String GET_BGP_PROCESS    = "http://localhost:5080/REST/bgp/info";
  private static final String LOAD_JOB_FILE      = "http://localhost:5080/REST/bgp/log";
  private static final String EXPORT_PLAN        = "http://localhost:5080/REST/bgp/exportplan";

  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    if ( client == null ) {
      super.init();
      BGProcessRESTServer.start();
      client = ClientBuilder.newClient();
    }
  }
  
  public String getTestCallbackTemplateURL() throws CSInitializationException {
    return CSProperties.instance().getString("testCallbackURL");
  }
  
  @Test
  public void submit() throws RDBMSException, CSInitializationException
  {
    printTestTitle("submit");
    JSONContent entryParameters = new JSONContent();
    entryParameters.setField( IPXON.PXONTag.callback.toReadOnlyTag(), getTestCallbackTemplateURL());
    Response response = client.target(SUBMIT_BGP_PROCESS)
        .queryParam("service", "ORPHANS_CLEANING")
        .queryParam("user", "Rahul Duldul")
        .queryParam("priority", "HIGH")
        .request()
        .post(Entity.entity( entryParameters.toString(), MediaType.APPLICATION_JSON));
    String returned = response.readEntity(String.class);
    assertNotNull( returned);
    println( "Returned job iid: " + returned);
  }
  
  @Test
  public void info() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("info");
    String jobIID = submitNewJob();
    Response response = client.target(GET_BGP_PROCESS)
        .queryParam("job", jobIID)
        .request()
        .get();
    println( "Returned info: " + response.readEntity(String.class));
  }
  
  @Test
  public void log() throws RDBMSException, CSInitializationException
  {
    printTestTitle("log");
    String jobIID = submitNewJob();
    Response response = client.target(LOAD_JOB_FILE)
        .queryParam("job", jobIID)
        .request()
        .get();
    println( "Returned log: " + response.readEntity(String.class));
  }
  
  @Test
  public void exportplan() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("exportplan");
    String inputJson = "{\"csidFormat\":\"SYSTEM\",\"catalog\":{\"csid\":\"[C>onboarding $locale=en_CA $type=DI]\"},"
        + "\"localeInheritance\":[\"en_WW\",\"en_US\",\"en_CA\"],\"allEntities\":false,\"includeEmbeddedEntities\":true,"
        + "\"baseEntityIDs\":[\"B07CVL2D2S\",\"B0148NNKTC\"],\"entityPropertyIIDs\":[200,201,202,203,204,285],"
        + "\"itemTypes\":[\"CLASSIFIER\",\"PROPERTY\",\"CONTEXT\"],\"hierarchyCodes\":[\"single_article\",\"Electricity\",\"Electronics\"],"
        + "\"propertyTypes\":[\"HTML\",\"TEXT\",\"TAG\"],\"CLASSIFIER\":{\"itemCodes\":[\"Shirt\",\"Textile\"]},"
        + "\"PROPERTY\":{\"itemCodes\":[\"createdonattribute\",\"createdbyattribute\",\"lastmodifiedbyattribute\","
        + "\"standardArticleAssetRelationship\",\"nameattribute\",\"lastmodifiedattribute\"]},"
        + "\"classifierTypes\": [\"CLASS\", \"TAXONOMY\", \"MINOR_TAXONOMY\"],"
        + "\"CONTEXT\":{\"itemCodes\":[\"Country\",\"Color\",\"Packaging\",\"MarketEntryDate\"]}}";
    JSONContent entryParameters = new JSONContent(inputJson);
    entryParameters.setField( IPXON.PXONTag.callback.toReadOnlyTag(), getTestCallbackTemplateURL());
    Response response = client.target(EXPORT_PLAN)
        .request()
        .post(Entity.entity(entryParameters.toString(), MediaType.APPLICATION_JSON));
    println( "Returned plan " + response.readEntity(String.class));
  }
  
  private String submitNewJob() throws CSInitializationException
  {
    JSONContent entryParameters = new JSONContent();
    entryParameters.setField( IPXON.PXONTag.callback.toReadOnlyTag(), getTestCallbackTemplateURL());
    Response response = client.target(SUBMIT_BGP_PROCESS)
        .queryParam("service", "ORPHANS_CLEANING")
        .queryParam("user", "Rahul Duldul")
        .queryParam("priority", "HIGH")
        .request()
        .post(Entity.entity( entryParameters.toString(), MediaType.APPLICATION_JSON));
    String jobIID = response.readEntity(String.class);
    return jobIID;
  }
  
  @Test
  public void uploadAsset() throws RDBMSException, IOException, CSInitializationException
  {
    printTestTitle("uploadAsset");
    String inputFile = "src/test/data/PUNE-Holidays-2019.jpg";
    JSONContent entryParameters = new JSONContent();
    entryParameters.setField( IPXON.PXONTag.callback.toReadOnlyTag(), getTestCallbackTemplateURL());
    entryParameters.setField("fileName", inputFile);
    
    Response response = client.target(SUBMIT_BGP_PROCESS)
        .queryParam("service", "ASSET_UPLOAD")
        .queryParam("user", "Rahul Duldul")
        .queryParam("priority", "HIGH")
        .request()
        .post(Entity.entity( entryParameters.toString(), MediaType.APPLICATION_JSON));
    
    println( "Returned job IID: " + response.readEntity(String.class));
  }
  
   @Test
   public void callbackSimulation() throws RDBMSException, IOException, CSInitializationException
  {
    printTestTitle("callbackSimulation");
    String callbackURL = getTestCallbackTemplateURL();
    callbackURL = callbackURL.replaceAll( JOB_PLACEHOLDER, "111000");
    callbackURL = callbackURL.replaceAll( STATUS_PLACEHOLDER, "SUCCESS");
    Response response = client.target(callbackURL).request().put( Entity.entity( "{}", MediaType.APPLICATION_JSON));
    println( "return status: " + response.readEntity(String.class));
  }
}
