package com.cs.config.strategy.plugin.usecase.base.taxonomy;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetAllKlassTaxonomiesTree extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IKlassTaxonomyTreeModel.LABEL, IKlassTaxonomyTreeModel.ICON);
  
  public AbstractGetAllKlassTaxonomiesTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getType();
  
  public List<Map<String, Object>> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> taxonomyMapList = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    
    try {
      Iterable<Vertex> iterable = graph
          .command(new OCommandSQL(
              "select from " + getType() + " where outE('Child_Of').size() = 0 order by "
                  + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex taxonomyNode : iterable) {
        Map<String, Object> taxonomyMap = new HashMap<>();
        taxonomyMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, taxonomyNode));
        fillKlassTaxonomyTree(taxonomyNode, taxonomyMap);
        taxonomyMapList.add(taxonomyMap);
      }
    }
    catch (Exception e) {
      throw new PluginException();
    }
    
    return taxonomyMapList;
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> responseList = executeInternal(requestMap);
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("list", responseList);
    return returnMap;
  }
  
  private void fillKlassTaxonomyTree(Vertex klassNode, Map<String, Object> klassMap)
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterable<Vertex> ChildNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : ChildNodes) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch, childNode);
      fillKlassTaxonomyTree(childNode, childMap);
      klassesList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }
}
