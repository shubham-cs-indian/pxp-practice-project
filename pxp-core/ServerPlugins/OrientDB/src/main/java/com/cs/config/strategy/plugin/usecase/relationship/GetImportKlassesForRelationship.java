package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.constants.SystemLevelIds;
import com.cs.core.runtime.interactor.constants.ImportSystemLevelIds;
import com.cs.core.runtime.interactor.constants.application.ImportVertexLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetImportKlassesForRelationship extends AbstractOrientPlugin {
  
  protected final String[] PIM_KLASSES   = new String[] { ImportSystemLevelIds.MASTER_ARTICLE_CLASS,
      ImportSystemLevelIds.SYSTEM_ARTICLE_CLASS };
  
  protected final String[] ASSET_KLASSES = new String[] { SystemLevelIds.ASSET, };
  
  public GetImportKlassesForRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnModel = new HashMap<>();
    LinkedHashMap<String, Object> listModel = new LinkedHashMap<>();
    for (String parentId : PIM_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, ImportVertexLabelConstants.ENTITY_TYPE_IMPORT_KLASS));
    }
    
    for (String parentId : ASSET_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_ASSET));
    }
    
    returnModel.put("klasses", listModel);
    
    return returnModel;
  }
  
  private List<Map<String, Object>> getKlassInfo(String parentId, String entityType)
      throws Exception
  {
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(parentId, entityType);
    List<Map<String, Object>> klasses = new ArrayList<>();
    for (Map<String, Object> klassMap : klassesList) {
      Map<String, Object> klassInfo = new HashMap<String, Object>();
      klassInfo.put("id", klassMap.get("id"));
      klassInfo.put("label", klassMap.get("label"));
      klasses.add(klassInfo);
    }
    
    return klasses;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetImportKlassesForRelationship/*" };
  }
}
