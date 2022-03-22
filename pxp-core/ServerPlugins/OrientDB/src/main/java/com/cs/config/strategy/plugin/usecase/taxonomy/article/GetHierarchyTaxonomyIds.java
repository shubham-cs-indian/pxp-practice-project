package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetHierarchyTaxonomyIds extends AbstractOrientPlugin {

  public GetHierarchyTaxonomyIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetHierarchyTaxonomyIds/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String code = (String) requestMap.get(IIdLabelCodeModel.CODE);
    Vertex vertexByCode = UtilClass.getVertexByCode(code, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    String queryFormat = "TRAVERSE out('%s') from %s";

    String query = String.format(queryFormat, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, vertexByCode.getId());
    Iterable<Vertex> taxonomies  = UtilClass.getGraph().command(new OCommandSQL(query)).execute();

    List<Long> idParameterModel = new ArrayList<>();

    for(Vertex taxonomy : taxonomies){
      if(taxonomy.getProperty("@class").equals(VertexLabelConstants.ENTITY_TAG)){
        continue;
      }
      Number property = (Number) taxonomy.getProperty(ITaxonomy.CLASSIFIER_IID);
      idParameterModel.add(property.longValue());
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IIIDsListModel.IIDS, idParameterModel);
    return returnMap;
  }

}
