package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetAllowedTagValuesForTagTaxonomy extends AbstractOrientPlugin {
  
  public AbstractGetAllowedTagValuesForTagTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String tagGroupId = (String) requestMap.get(IIdParameterModel.ID);
    
    List<Map<String, Object>> attributionTaxonomies = new ArrayList<>();
    Vertex tagVertex = null;
    try {
      tagVertex = UtilClass.getVertexById(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    Iterable<Vertex> childVertices = tagVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childVertex : childVertices) {
      Vertex parentTaxonomyNode = AttributionTaxonomyUtil.getParentTaxonomy(childVertex);
      if (parentTaxonomyNode == null) {
        attributionTaxonomies.add(TagUtils.getTagMap(childVertex, false));
      }
    }
    /*    String query ="select from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY + " where out('child_of').code = " +
        tagGroupId + " and out('child_of').code != " + attributionTaxonomyId;
    
    Iterable<Vertex> unlinkedAttributionTaxonomies = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex attributionTaxonomy : unlinkedAttributionTaxonomies) {
      attributionTaxonomies.add(TagUtils.getTagMap(attributionTaxonomy,false));
    }*/
    
    Map<String, Object> result = new HashMap<>();
    result.put(IListModel.LIST, attributionTaxonomies);
    return result;
  }
}
