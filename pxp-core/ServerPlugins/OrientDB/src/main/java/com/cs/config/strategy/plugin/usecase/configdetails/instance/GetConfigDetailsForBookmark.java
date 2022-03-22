package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTreeInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GetConfigDetailsForBookmark extends AbstractOrientPlugin {
  
  public GetConfigDetailsForBookmark(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForBookmark/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForBookmarkRequestModel.SELECTED_TAXONOMYIDS);
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> taxonomyTree = fillTaxonomyTree(selectedTaxonomyIds);
    mapToReturn.put(IConfigDetailsForBookmarkModel.TAXONOMY_TREE, taxonomyTree);
    return mapToReturn;
  }
  
  private Map<String, Object> fillTaxonomyTree(List<String> selectedTaxonomyIds) throws Exception
  {
    Vertex rootTaxonomy = null;
    Set<Vertex> parentTaxonomyNodes = new HashSet();
    Set<Vertex> selectedTaxonomyNodes = new HashSet<>();
    for (String selectedTaxonomyId : selectedTaxonomyIds) {
      Vertex taxonomyNode = null;
      try {
        taxonomyNode = UtilClass.getVertexById(selectedTaxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
      rootTaxonomy = fillParentTaxonomyIdsAndGetRootNode(taxonomyNode, parentTaxonomyNodes);
      selectedTaxonomyNodes.add(taxonomyNode);
    }
    
    parentTaxonomyNodes.removeAll(selectedTaxonomyNodes);
    
    Map<String, Object> map = new HashMap<>();
    map.put(IIdLabelTreeInformationModel.ID, UtilClass.getCodeNew(rootTaxonomy));
    map.put(IIdLabelTreeInformationModel.LABEL,
        UtilClass.getValueByLanguage(rootTaxonomy, ITaxonomy.LABEL));
    map.put(IIdLabelTreeInformationModel.CHILDREN,
        getNestedTaxonomyTree(rootTaxonomy, parentTaxonomyNodes));
    return map;
  }
  
  protected List<Map<String, Object>> getNestedTaxonomyTree(Vertex rootTaxonomy,
      Set<Vertex> parentTaxonomyNodes)
  {
    
    // Now Start filling the taxonomy tree from root node..
    Iterable<Vertex> childTaxnomies = rootTaxonomy.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    List<Map<String, Object>> children = new ArrayList<>();
    
    for (Vertex childTaxonomy : childTaxnomies) {
      
      List<Map<String, Object>> nestedChildren = new ArrayList<>();
      String childTaxonomyId = UtilClass.getCodeNew(childTaxonomy);
      Map<String, Object> map = new HashMap<>();
      map.put(IIdLabelTreeInformationModel.ID, childTaxonomyId);
      map.put(IIdLabelTreeInformationModel.LABEL,
          UtilClass.getValueByLanguage(childTaxonomy, CommonConstants.LABEL_PROPERTY));
      map.put(IIdLabelTreeInformationModel.CHILDREN, nestedChildren);
      children.add(map);
      
      if (parentTaxonomyNodes.contains(childTaxonomy)) {
        // if its one of our parent taxonomies list, expand it.
        nestedChildren.addAll(getNestedTaxonomyTree(childTaxonomy, parentTaxonomyNodes));
      }
    }
    return children;
  }
  
  protected Vertex fillParentTaxonomyIdsAndGetRootNode(Vertex taxonomyNode,
      Set<Vertex> parentTaxonomyIds) throws Exception
  {
    
    Vertex rootTaxonomy = null;
    parentTaxonomyIds.add(taxonomyNode);
    Vertex parentNode = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyNode);
    if (parentNode == null) {
      // means this is root taxonomy..
      return taxonomyNode;
    }
    rootTaxonomy = fillParentTaxonomyIdsAndGetRootNode(parentNode, parentTaxonomyIds);
    return rootTaxonomy;
  }
}
