package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;


public class GetKlassesAndTaxonomyByIds extends AbstractOrientPlugin {
  
  public GetKlassesAndTaxonomyByIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesAndTaxonomyByIds/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> selectedTypes = (List<String>)requestMap.get("selectedTypes");
    List<String> selectedTaxonomies = (List<String>)requestMap.get("selectedTaxonomyIds");
    
    //Nature and Non_nature
    Map<String, Object> klassMap = new HashMap<>();
    
    for (String klassId : selectedTypes) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(
          Arrays.asList(IKlass.LABEL, IKlass.CODE, IKlass.ID, IKlass.NATURE_TYPE, IKlass.ICON),
          klassNode);
      String code = (String) mapFromVertex.get("code");
      klassMap.put(code, mapFromVertex);
    }
    
    //Taxonomy
    Map<String, Object> referencedTaxonomies = new HashMap<String, Object>();
    for (String taxonomyId : selectedTaxonomies) {
      Vertex taxonomyNode = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillConfigDetailsRecursive(referencedTaxonomies, taxonomyNode);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("referencedKlasses", klassMap);
    response.put("referencedTaxonomies", referencedTaxonomies);
    return response;
  }
  
  private Vertex fillConfigDetailsRecursive(Map<String, Object> referencedTaxonomies, Vertex taxonmyNode) throws Exception
  {
    if(taxonmyNode == null) {
      return null;
    }
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(ITaxonomy.CODE, ITaxonomy.LABEL, ITaxonomy.ID, ITaxonomy.ICON), taxonmyNode);
    
    Vertex parentVertex = null;
    Iterator<Vertex> parentVertices = taxonmyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    
    while (parentVertices.hasNext()) {
      Vertex nextParentVertex = parentVertices.next();
      if (nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
          .equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)
          || nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        parentVertex = nextParentVertex;
        taxonomyMap.put(CommonConstants.PARENT_ID_PROPERTY,
            parentVertex.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
    
    if (parentVertex == null) {
      taxonomyMap.put(CommonConstants.PARENT_ID_PROPERTY, -1);
    }
    
    String taxonomyId = taxonmyNode.getProperty(CommonConstants.CODE_PROPERTY);
    referencedTaxonomies.put(taxonomyId, taxonomyMap);
    
    return fillConfigDetailsRecursive(referencedTaxonomies, parentVertex);
  }
  
}
