package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAssetWithGlobalPermission extends AbstractOrientPlugin {
  
  public GetAssetWithGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> rootNode = new HashMap<>();
    String id = (String) map.get(IIdParameterModel.ID);
    
    OrientGraph graph = UtilClass.getGraph();
    if (id.equals("-1")) {
      
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.ASSET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ASSET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex assetNode : i) {
        childKlasses.add(AssetUtils.getAssetEntityMap(assetNode, null));
      }
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
    }
    else {
      Vertex assetNode = null;
      try {
        assetNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_ASSET);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      rootNode = AssetUtils.getAssetEntityMap(assetNode, null);
      KlassUtils.addGlobalPermission(assetNode, returnMap);
      KlassUtils.fillReferencedAttributesAndTags(returnMap, rootNode);
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
      KlassUtils.fillReferencedContextDetails(returnMap);
      KlassUtils.fillContextKlassesDetails(returnMap, rootNode, assetNode);
      
      KlassUtils.fillReferencedTaskDetails(returnMap);
      KlassUtils.fillReferencedDataRuleDetails(returnMap);
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssetWithGlobalPermission/*" };
  }
}
