package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.textasset.util.TextAssetUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTextAssetWithGlobalPermission extends AbstractOrientPlugin {
  
  public GetTextAssetWithGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String id = (String) requestMap.get(IIdParameterModel.ID);
    ;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> rootNode = new HashMap<>();
    
    if (id.equals("-1")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.TEXT_ASSET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex klassNode : i) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
        childKlasses.add(klassEntityMap);
      }
      returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
    }
    else {
      try {
        Vertex textAssetNode = UtilClass.getVertexByIndexedId(id,
            VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
        rootNode = TextAssetUtils.getTextAssetEntityMap(textAssetNode, null);
        KlassUtils.addGlobalPermission(textAssetNode, returnMap);
        returnMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNode);
        KlassUtils.fillReferencedAttributesAndTags(returnMap, rootNode);
        KlassUtils.fillReferencedContextDetails(returnMap);
        KlassUtils.fillContextKlassesDetails(returnMap, rootNode, textAssetNode);
        
        KlassUtils.fillReferencedTaskDetails(returnMap);
        KlassUtils.fillReferencedDataRuleDetails(returnMap);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTextAssetWithGlobalPermission/*" };
  }
}
