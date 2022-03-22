package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GetKlassesForRelationship extends AbstractOrientPlugin {
  
  protected final String[] PIM_KLASSES             = new String[] { SystemLevelIds.ARTICLE_KLASS,
      SystemLevelIds.COLLECTION_KLASS, SystemLevelIds.SET_KLASS };
  
  protected final String[] ASSET_KLASSES           = new String[] { SystemLevelIds.ASSET };
  
  protected final String[] TARGET_KLASSES          = new String[] { SystemLevelIds.MARKET };
  
  protected final String[] TEXT_ASSET_KLASSES      = new String[] { SystemLevelIds.TEXT_ASSET };
  
  protected final String[] SUPPLIER_KLASSES        = new String[] { SystemLevelIds.SUPPLIER };
  
  public GetKlassesForRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> returnModel = new HashMap<>();
    LinkedHashMap<String, Object> listModel = new LinkedHashMap<>();
    for (String parentId : PIM_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_KLASS));
    }
    
    for (String parentId : ASSET_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_ASSET));
    }
    
    for (String parentId : TARGET_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_TARGET));
    }
    
    for (String parentId : SUPPLIER_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_SUPPLIER));
    }
    for (String parentId : TEXT_ASSET_KLASSES) {
      listModel.put(parentId, getKlassInfo(parentId, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET));
    }
    
    listModel.put(SystemLevelIds.TAXONOMY_ATTRIBUTION,
        getTaxonomyKlassInfo(VertexLabelConstants.ATTRIBUTION_TAXONOMY));
    
    returnModel.put("klasses", listModel);
    return returnModel;
  }
  
  private List<Map<String, String>> getKlassInfo(String parentId, String entityType)
      throws Exception
  {
    List<Map<String, Object>> klassesList = KlassGetUtils.getKlassesList(parentId, entityType);
    List<Map<String, String>> klasses = new ArrayList<>();
    for (Map<String, Object> klassMap : klassesList) {
      Map klassInfo = new HashMap();
      klassInfo.put(IKlass.ID, klassMap.get(IKlass.ID));
      klassInfo.put(IKlass.LABEL, klassMap.get(IKlass.LABEL));
      klassInfo.put(IKlass.ICON, klassMap.get(IKlass.ICON));
      klassInfo.put(IKlass.PREVIEW_IMAGE, klassMap.get(IKlass.PREVIEW_IMAGE));
      klassInfo.put(IKlass.IS_NATURE, klassMap.get(IKlass.IS_NATURE));
      klassInfo.put(IKlass.NATURE_TYPE, klassMap.get(IKlass.NATURE_TYPE));
      klassInfo.put(IKlass.IS_ABSTRACT, klassMap.get(IKlass.IS_ABSTRACT));
      klasses.add(klassInfo);
    }
    
    return klasses;
  }
  
  private List<Map<String, String>> getTaxonomyKlassInfo(String entityType)
  {
    List<Map<String, Object>> klassesList = AttributionTaxonomyUtil.getTaxonomiesList();
    List<Map<String, String>> klasses = new ArrayList<>();
    for (Map<String, Object> klassMap : klassesList) {
      Map klassInfo = new HashMap();
      klassInfo.put(IKlass.ID, klassMap.get(IKlass.ID));
      klassInfo.put(IKlass.LABEL, klassMap.get(IKlass.LABEL));
      klassInfo.put(IKlass.ICON, klassMap.get(IKlass.ICON));
      klassInfo.put(IKlass.PREVIEW_IMAGE, klassMap.get(IKlass.PREVIEW_IMAGE));
      klassInfo.put(IKlass.IS_NATURE, klassMap.get(IKlass.IS_NATURE));
      klassInfo.put(IKlass.NATURE_TYPE, klassMap.get(IKlass.NATURE_TYPE));
      klasses.add(klassInfo);
    }
    
    return klasses;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesForRelationship/*" };
  }
}
