package com.cs.config.strategy.plugin.usecase.asset.iconlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class BulkCreateAndSaveIcons extends AbstractOrientPlugin {
  
  public BulkCreateAndSaveIcons(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateAndSaveIcons/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = new ArrayList<String>();
    List<Map<String, Object>> iconList = (List<Map<String, Object>>) requestMap.get("icons");
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_ICON, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> icon : iconList) {
      icon.put(IIconModel.MODIFIED_ON, System.currentTimeMillis());
      icon.put(IIconModel.CREATED_ON, System.currentTimeMillis());
      Vertex iconNode = UtilClass.createNode(icon, vertexType, new ArrayList<String>());
      String property = iconNode.getProperty(CommonConstants.CODE_PROPERTY);
      ids.add(property);
    }
    UtilClass.getGraph().commit();
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("ids", ids);
    return responseMap;
  }
}
