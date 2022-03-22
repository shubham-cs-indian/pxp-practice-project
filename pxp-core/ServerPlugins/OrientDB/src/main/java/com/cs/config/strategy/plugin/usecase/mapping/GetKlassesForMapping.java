package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

@SuppressWarnings("unchecked")
public class GetKlassesForMapping extends AbstractOrientPlugin {
  
  protected final String[] PIM_KLASSES             = new String[] { SystemLevelIds.ARTICLE_KLASS };
  
  protected final String[] ASSET_KLASSES           = new String[] { SystemLevelIds.ASSET };
  
  protected final String[] TARGET_KLASSES          = new String[] { SystemLevelIds.MARKET };
  
  protected final String[] TEXT_ASSET_KLASSES      = new String[] { SystemLevelIds.TEXT_ASSET };
  
  protected final String[] SUPPLIER_KLASSES        = new String[] { SystemLevelIds.SUPPLIER };
  
  public GetKlassesForMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnModel = new HashMap<>();
    List<String> selectedKlasses = new ArrayList<String>();
    LinkedHashMap<String, Object> listModel = new LinkedHashMap<>();
    for (String parentId : PIM_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_KLASS, selectedKlasses));
    }
    
    for (String parentId : ASSET_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_ASSET, selectedKlasses));
    }
    
    for (String parentId : TARGET_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_TARGET, selectedKlasses));
    }
    
    /*for (String parentId : SUPPLIER_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_SUPPLIER, selectedKlasses));
    }*/
    for (String parentId : TEXT_ASSET_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET, selectedKlasses));
    }
    for (String parentId : SUPPLIER_KLASSES) {
      listModel.put(parentId,
          getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_SUPPLIER, selectedKlasses));
    }
    returnModel.put("klasses", listModel);
    
    return returnModel;
  }
  
  @SuppressWarnings({ "rawtypes" })
  private List<Map<String, String>> getKlassInfo(String parentId, String entityType,
      List<String> selectedKlasses) throws Exception
  {
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(parentId, entityType);
    List<Map<String, String>> klasses = new ArrayList<>();
    for (Map<String, Object> klassMap : klassesList) {
      Map klassInfo = new HashMap();
      Object klassId = klassMap.get(IKlass.ID);
      if (!(Boolean) klassMap.get(IKlass.IS_ABSTRACT)) {
        klassInfo.put(IKlass.ID, klassId);
        klassInfo.put(IKlass.LABEL, klassMap.get(IKlass.LABEL));
        klassInfo.put(IKlass.ICON, klassMap.get(IKlass.ICON));
        klassInfo.put(IKlass.NATURE_TYPE, klassMap.get(IKlass.NATURE_TYPE));
        klasses.add(klassInfo);
      }
    }
    
    return klasses;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesForMapping/*" };
  }
}
