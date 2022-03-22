package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetKlassTree extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IConfigEntityTreeInformationModel.LABEL);
  
  public AbstractGetKlassTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getTypeKlass();
  
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> klassMap = new HashMap<>();
    
    Vertex klassNode = null;
    try {
      klassNode = UtilClass.getVertexById(parentId, getTypeKlass());
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    
    klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
    fillKlassesTree(klassNode, klassMap);
    return klassMap;
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = executeInternal(requestMap);
    return responseMap;
  }
  
  private void fillKlassesTree(Vertex klassNode, Map<String, Object> klassMap) throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterable<Vertex> ChildNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : ChildNodes) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch, childNode);
      fillKlassesTree(childNode, childMap);
      klassesList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }
}
