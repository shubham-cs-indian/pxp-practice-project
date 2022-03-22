package com.cs.config.strategy.plugin.usecase.klass;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetChildTaxonomiesByParentIdWithPagination extends AbstractOrientPlugin {
  
  public GetChildTaxonomiesByParentIdWithPagination(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildTaxonomiesByParentIdWithPagination/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IIdPaginationModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IIdPaginationModel.SIZE).toString());
    String parentId = (String)requestMap.get(IIdPaginationModel.ID);
    
    Vertex parentTaxonomy = null;
    try {
      parentTaxonomy = UtilClass.getVertexByIndexedId(parentId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      throw new KlassTaxonomyNotFoundException(e);
    }
    
    Map<String, Object> taxonomyTree = getParentTaxonomyMap(parentTaxonomy);
    
    List<Map<String, Object>> children = new ArrayList<>();
    fillChildrenByParent(children, parentTaxonomy, from , size);
    
    taxonomyTree.put(ITaxonomyInformationModel.IS_LAST_NODE, children.isEmpty());
    taxonomyTree.put(ICategoryInformationModel.CHILDREN, children);
    taxonomyTree.put(ICategoryInformationModel.COUNT, taxonomyTree.remove(ITaxonomy.CHILD_COUNT));
    taxonomyTree.put(ICategoryInformationModel.TYPE, taxonomyTree.remove(ITaxonomy.BASE_TYPE));
    
    return taxonomyTree;
  }

  private Map<String, Object> getParentTaxonomyMap(Vertex parentTaxonomy)
  {
    List<String> fieldsToFetchForParentTaxonomy = Arrays.asList(ITaxonomy.LABEL, ITaxonomy.CHILD_COUNT, ITaxonomy.CODE, 
        ITaxonomy.BASE_TYPE, ITaxonomy.ICON, ITaxonomy.TAXONOMY_TYPE);
    Map<String, Object> taxonomyTree = UtilClass.getMapFromVertex(fieldsToFetchForParentTaxonomy, parentTaxonomy);
    return taxonomyTree;
  }

  private void fillChildrenByParent(List<Map<String, Object>> children, Vertex parentTaxonomy, Long from, Long size)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> childTaxonomies = graph.command(
        new OCommandSQL("select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
            + "')) from " + parentTaxonomy.getId() + " order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc skip " + from + " limit " + size))
        .execute();

    List<String> fieldsToFetchForChildTaxonomy = Arrays.asList(ITaxonomy.LABEL, ITaxonomy.CODE, ITaxonomy.ICON,
        ITaxonomy.TAXONOMY_TYPE,ITaxonomy.BASE_TYPE);
    for (Vertex child : childTaxonomies) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetchForChildTaxonomy, child);
      Iterable<Edge> edges = child.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      Boolean isChildPresent = edges.iterator().hasNext();
      childMap.put(ITaxonomyInformationModel.IS_LAST_NODE, !isChildPresent);
      childMap.put(ICategoryInformationModel.TYPE, childMap.remove(ITaxonomy.BASE_TYPE));
      children.add(childMap);
    }
  }
  
}