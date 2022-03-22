package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetConfirmAddDeleteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class ConfirmImageStrategy implements IConfirmImageStrategy {
  
  @Resource(name = "assetStoreAuthenticationMap")
  protected Map<String, String> assetStoreAuthenticationMap;
  
  @Autowired
  protected String              assetStoreURL;
  
  @Resource(name = "cookies")
  protected List<String>        cookies;
  
  @Override
  public IAssetConfirmAddDeleteModel execute(IAssetConfirmAddDeleteModel model) throws Exception
  {
    /*
    AssetData strategyData = new AssetData();
    try {
      assetStrategy.addAssetData(strategyData);
    }
    catch (Exception e) {
      System.out.println("EXCPETION FOR ElasticStrategyData");
    }
    strategyData.setKlassName(this.getClass().getSimpleName());
    strategyData.setUseCase("Confirm Image");
    strategyData.setStartTime(System.currentTimeMillis());
    URL uri = new URL(assetStoreURL + "confirm/");
    strategyData.setRequestURL(uri.toString());
    strategyData.setRequestMethod("POST");
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("POST");
    ObjectMapper jsonMapper = new ObjectMapper();
    String imagesConfirmModel = jsonMapper.writeValueAsString(model);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Content-Type", "application/json");
    if (cookies.size() > 0) {
      connection.setRequestProperty("Cookie", cookies.get(0));
    }
    connection.connect();
    
    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
    output.writeBytes(imagesConfirmModel);
    output.flush();
    output.close();
    if(connection.getResponseMessage() != null) {
      strategyData.setEndTime(System.currentTimeMillis());
      strategyData.setTurnAroundTime(strategyData.getEndTime() - strategyData.getStartTime());
    }
    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
      strategyData.setRequest(imagesConfirmModel);
      strategyData.setResponse(connection.getResponseMessage());
      throw new Exception("Confirm/Delete Images Failed");
    } else {
      String setCookie = connection.getHeaderField("Set-Cookie");
      if(setCookie != null){
        List<HttpCookie> httpCookies = HttpCookie.parse(setCookie);
        cookies.removeAll(cookies);
        for (HttpCookie httpCookie : httpCookies) {
          cookies.add(httpCookie.toString());
        }
      }
    }
    */
    /*System.out.println("Content-------start-----");
    DataInputStream input = new DataInputStream(connection.getInputStream());
    BufferedReader d = new BufferedReader(new InputStreamReader(input));
    System.out.println(d.readLine());
    String inputLine;
    while ((inputLine = d.readLine()) != null) {
      System.out.println(inputLine);
    }
    System.out.println("Content-------end-----");*/
    return null;
  }
}
