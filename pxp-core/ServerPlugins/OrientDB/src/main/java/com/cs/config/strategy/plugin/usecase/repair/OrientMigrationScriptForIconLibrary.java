package com.cs.config.strategy.plugin.usecase.repair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.ILanguageModel;
import com.cs.config.strategy.plugin.model.LanguageModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.assetserver.AssetObjectNotFoundException;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * Orient Plugin:-
 * Description:- Migration to remove icon property from entity and create a new icon node linked to entity as has_icon edge. 
 * @author rahul.sehrawat
 *
 */
public class OrientMigrationScriptForIconLibrary extends AbstractOrientMigration {
  
  public OrientMigrationScriptForIconLibrary(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationToIconLibraryFromEntityIconProperty/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    long count = 0;
    long maxCountToItrate = 1;
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ICON, CommonConstants.CODE_PROPERTY);
    
    //Set language for filling icon label in us language
    ILanguageModel languageModel = new LanguageModel();
    languageModel.setUiLanguage("en_US");
    languageModel.setDataLanguage("en_US");
    UtilClass.setLanguage(languageModel);
    
    String countQuery = getIconCountQuery();
    Iterable<Vertex> countVertexies = graph.command(new OCommandSQL(countQuery))
        .execute();
    
    for (Vertex countVertex : countVertexies) {
      count = countVertex.getProperty("count");
    }
    
    if (count > 1000) {
      maxCountToItrate = count / 1000;
    }
    
    for (int iteration = 0; iteration < maxCountToItrate; iteration++) {
      String entitiesQuery = getEntitiesQuery(iteration * 1000);
      Iterable<Vertex> entityVertexies = graph.command(new OCommandSQL(entitiesQuery))
          .execute();
      for (Vertex entity : entityVertexies) {
        String icon = entity.getProperty(CommonConstants.ICON_PROPERTY);
        String entityType = entity.getProperty(CommonConstants.TYPE);
        
        if (!icon.isEmpty() && entityType != null
            && !entityType.equals(CommonConstants.USER_TYPE)) {
          requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, icon);
          
          String fileNameWithPath= getFileNameOfAsset(requestMap);
          String fileName = trimFileName(fileNameWithPath);
          Map<String, Object> iconMap = new HashMap<>();
          iconMap.put(IIconModel.ICON_KEY, icon);
          iconMap.put(IIconModel.LABEL, fileName);
          iconMap.put(IIconModel.MODIFIED_ON, System.currentTimeMillis());
          iconMap.put(IIconModel.CREATED_ON, System.currentTimeMillis());
          iconMap.put(IIconModel.CODE, RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ICON.getPrefix()));
          
          Vertex iconNode = UtilClass.createNode(iconMap, vertexType, new ArrayList<String>());
          entity.addEdge(RelationshipLabelConstants.HAS_ICON, iconNode);
        }
        if (entityType != null && !entityType.equals(CommonConstants.USER_TYPE)) {
          entity.removeProperty(CommonConstants.ICON_PROPERTY);
        }
      }
    }
    
    graph.commit();
    
    return null;
  }

  
  @SuppressWarnings("unchecked")
  protected String getFileNameOfAsset(Map<String, Object> requestMap) throws IOException, PluginException {
    String assetKey = (String) requestMap.get(IGetAssetDetailsRequestModel.ASSET_KEY);
    Map<String, Object> assetServerDetails = (Map<String, Object>) requestMap.get("assetServerDetails");
    URL uri = new URL(assetServerDetails.get("storageURL") + "/" + CommonConstants.SWIFT_CONTAINER_ICONS + "/" + assetKey);
    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
    connection.setRequestMethod("HEAD");
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("X-Auth-Token", (String) assetServerDetails.get("authToken"));
    
    connection.connect();
    
    int responseCode = connection.getResponseCode();
    
    if (responseCode == 200 || responseCode == 206) {
      Map<String, List<String>> headerFields = connection.getHeaderFields();
      List<String> fileName = headerFields.get("X-Object-Meta-Name");
      
      return fileName.get(0);
    }
    else if (responseCode == 401) {
      return "";
    }
    else if (responseCode == 404) {
      throw new AssetObjectNotFoundException();
    }
    else {
      PluginException exception = new PluginException();
      exception.getDevExceptionDetails()
          .get(0)
          .setDetailMessage("Get Asset Failed with Response Code: " + responseCode + ", Message: "
              + connection.getResponseMessage());
      throw exception;
    }
  }
  
  protected String trimFileName(String fileNameWithPath) {
    String[] fileNameArray = fileNameWithPath.split("\\\\");
    String fileNameWithExtension = fileNameArray[fileNameArray.length-1];
    
    return fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
  }
  
  
  protected String getIconCountQuery()
  {
    String query = "select count (*) from v where icon is NOT NULL";
    
    return query;
  }
  
  protected String getEntitiesQuery(int skip)
  {
    String query = "select from v where icon is NOT NULL SKIP " + skip + " LIMIT 1000";
    
    return query;
  }
}
