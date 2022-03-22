package com.cs.api.estordbmsmigration.services;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSInitializationException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RequestHandler {
 
  @Autowired
  protected Authenticator             getAuthenticator;

  public static RestHighLevelClient restClient;
  static {
    try {
      restClient = RequestHandler.prepareRestClient();
    }
    catch (CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
    }
  }
 
  public static Map<String, Object> connectToEsServer(String useCase, Map<String, Object> requestMap, String requesttype)
      throws URISyntaxException, MalformedURLException, Exception, IOException
  {
    String host = MigrationProperties.instance().getString("elastic.server.host");
    String port = MigrationProperties.instance().getString("elastic.server.port");
    String protocol = MigrationProperties.instance().getString("elastic.server.protocol");
    String url = protocol + "://" + host + ":" + port + "/";
    URI uri = new URI(url + useCase);
    String contentJson = requestMap == null ? null : ObjectMapperUtil.writeValueAsString(requestMap);
    HttpURLConnection connection = createHttpConnection(uri, contentJson, requesttype);
    InputStream inputStream = connection.getInputStream();
    Map<String, Object> response = ObjectMapperUtil.readValue(inputStream, Map.class);
    return response;
  }

  public static RestHighLevelClient getRestClient() throws CSInitializationException
  {
      return restClient;
  }

  public static RestHighLevelClient prepareRestClient() throws CSInitializationException
  {
    String host = MigrationProperties.instance().getString("elastic.server.host");
    String port = MigrationProperties.instance().getString("elastic.server.port");
    String protocol = MigrationProperties.instance().getString("elastic.server.protocol");
    return new RestHighLevelClient(RestClient.builder(new HttpHost(host, Integer.parseInt(port), protocol))
        .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(30000000)
            .setConnectionRequestTimeout(90000000)
            .setSocketTimeout(90000000)));
  }

  public static HttpURLConnection createHttpConnection(URI uri, String contentJson, String methodType) throws MalformedURLException, Exception
  {

    HttpURLConnection connection = prepareConnection(uri.toURL(), methodType);
    if (contentJson != null) {
      DataOutputStream output = new DataOutputStream(connection.getOutputStream());
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
      writer.write(contentJson);
      writer.close();
    }
    return connection;
  }
  
  public static HttpURLConnection prepareConnection(URL URI, String methodType) throws Exception
  {
    HttpURLConnection connection = (HttpURLConnection) URI.openConnection();
    connection.setRequestMethod(methodType);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.connect();
    return connection;
  }
  
  public static List<Map<String, Object>> getDocumentsFromServer(String index, String docType, Long from, Long batchSize) throws MalformedURLException, URISyntaxException, IOException, Exception
  {
    String useCase = index+"/"+docType+"/_search";
    Map<String, Object> request = new HashMap<String, Object>();
    request.put("from", String.valueOf(from));
    request.put("size", String.valueOf(batchSize));
    Map<String, Object> response = RequestHandler.connectToEsServer(useCase, request, "POST");
    Map<String, Object> hits = (Map<String, Object>) response.get("hits");
    return (List<Map<String, Object>>) hits.get("hits");
  }
  
  public static Map<String, Object> getDocumentById(String index, String docType, String id) throws MalformedURLException, URISyntaxException, IOException, Exception
  {
    String useCase = index+"/"+docType+"/" + id;
    Map<String, Object> response = RequestHandler.connectToEsServer(useCase, null, "GET");
    return (Map<String, Object>) response.get("_source");
  }

  public static Integer getTotalCount(String index, String docType) throws MalformedURLException, URISyntaxException, IOException, Exception
  {
    String useCase = index+"/"+docType+"/_count";
    Map<String, Object> response = RequestHandler.connectToEsServer(useCase, null, "GET");
    return (Integer) response.get("count");
  }
}
