package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.taxonomy.IParentChildTaxonomyIdModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetParentVsChildsTaxonomyIds extends AbstractOrientPlugin {
 
  public GetParentVsChildsTaxonomyIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetParentVsChildsTaxonomyIds/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> taxonomyIdList = (List<String>) requestMap.get(IListModel.LIST);
    Map<String, List<String>> patentVsChildMap = new HashMap<>();
    for (String taxonomyId : taxonomyIdList) {
      Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      if (taxonomyVertex != null) {
        Iterable<Vertex> parentItrator = taxonomyVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
        
        String parentId = getParent(taxonomyVertex, parentItrator);
        List<String> childIds = patentVsChildMap.get(parentId);
        if (childIds == null) {
          childIds = new ArrayList<>();
          childIds.add(taxonomyId);
          patentVsChildMap.put(parentId, childIds);
        }
        else {
          patentVsChildMap.get(parentId)
              .add(taxonomyId);
        }
      }
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IParentChildTaxonomyIdModel.TAXONOMY_MAP, patentVsChildMap);
    return returnMap;
  }
  
  private String getParent(Vertex childVertex, Iterable<Vertex> parents)
  {
    Iterator<Vertex> parentIterator = parents.iterator();
    if (!parentIterator.hasNext()) {
      return UtilClass.getCodeNew(childVertex);
    }
    
    while (parentIterator.hasNext()) {
      Vertex parentVertex = parentIterator.next();
      if (!parentVertex.getProperty("@class")
          .equals("Tag")) {
        Iterable<Vertex> parentItrator = parentVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
        return getParent(parentVertex, parentItrator);
      }
    }
    return null;
  }
}
