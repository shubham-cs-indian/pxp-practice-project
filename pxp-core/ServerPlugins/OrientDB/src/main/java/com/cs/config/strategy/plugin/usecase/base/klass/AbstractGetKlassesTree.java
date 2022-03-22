package com.cs.config.strategy.plugin.usecase.base.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

  
public abstract class AbstractGetKlassesTree extends AbstractOrientPlugin {
  
  public AbstractGetKlassesTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getTypeKlass();
  public abstract List<String> getFieldsToFetch();
  @SuppressWarnings("unchecked")
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    getFieldsToFetch();
    List<String> parentIds = getParentIds(requestMap);
    List<Map<String, Object>> klassList = new ArrayList<>();
    List<String> klassesIds = new ArrayList<>();
    for (String parentId : parentIds) {
      Map<String, Object> klassMap = new HashMap<>();
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(parentId, getTypeKlass());
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      klassMap = UtilClass.getMapFromVertex(getFieldsToFetch(), klassNode);
      klassesIds.add((String) klassMap.get(CommonConstants.ID_PROPERTY));
      fillKlassesTree(klassNode, klassMap, klassesIds);
      klassList.add(klassMap);
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(ICategoryTreeInformationModel.CATEGORY_INFO, klassList);
    returnMap.put(ICategoryTreeInformationModel.KLASSES_IDS, klassesIds);
    return returnMap;
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = executeInternal(requestMap);
    return responseMap;
  }
  
  private void fillKlassesTree(Vertex klassNode, Map<String, Object> klassMap,
      List<String> klassesIds) throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> childNodes = graph
        .command(new OCommandSQL(
            "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
                + "')) from " + klassNode.getId() + " order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    for (Vertex childNode : childNodes) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(getFieldsToFetch(), childNode);
      klassesIds.add((String) childMap.get(CommonConstants.ID_PROPERTY));
      fillKlassesTree(childNode, childMap, klassesIds);
      klassesList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }
  
  public List<String> getParentIds(Map<String, Object> requestMap)
  {
    return (List<String>) requestMap.get(IIdsListParameterModel.IDS);
  }
}
