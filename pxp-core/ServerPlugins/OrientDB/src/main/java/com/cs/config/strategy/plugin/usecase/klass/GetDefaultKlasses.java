package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDefaultKlasses extends AbstractOrientPlugin {
  
  public GetDefaultKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String mode = (String) map.get(IGetAllowedTypesModel.MODE);
    String standardKlassId = (String) map.get(IGetAllowedTypesModel.STANDARD_KLASS_ID);
    
    String entityLabel = getEntityLabelForMode(mode);
    
    List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
    if (entityLabel != null) {
      String[] childrenKeyValues = new String[] { "isDefaultChild", "true" };
      childrenList = KlassGetUtils.getNonAbstractKlassesList(standardKlassId, entityLabel,
          childrenKeyValues, childrenList);
    }
    
    List<String> abstractKlassesIds = new ArrayList<>();
    Vertex rootNode = UtilClass.getVertexById(standardKlassId, entityLabel);
    String rid = (String) rootNode.getId()
        .toString();
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where (isAbstract = \"true\") ";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : resultIterable) {
      abstractKlassesIds.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Map<String, Object> returnModel = new HashMap<>();
    returnModel.put(IGetDefaultKlassesModel.CHILDREN, childrenList);
    returnModel.put(IGetDefaultKlassesModel.ABSTRACT_KLASSES_IDS, abstractKlassesIds);
    return returnModel;
  }
  
  protected String getEntityLabelForMode(String mode)
  {
    String entityLabel = null;
    if (mode.equals("klass")) {
      entityLabel = VertexLabelConstants.ENTITY_TYPE_KLASS;
    }
    else if (mode.equals("asset")) {
      entityLabel = VertexLabelConstants.ENTITY_TYPE_ASSET;
    }
    else if (mode.equals("target")) {
      entityLabel = VertexLabelConstants.ENTITY_TYPE_TARGET;
    }
    else if (mode.equals("textasset")) {
      entityLabel = VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
    }
    else if (mode.equals("supplier")) {
      entityLabel = VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
    }
    return entityLabel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDefaultKlasses/*" };
  }
}
