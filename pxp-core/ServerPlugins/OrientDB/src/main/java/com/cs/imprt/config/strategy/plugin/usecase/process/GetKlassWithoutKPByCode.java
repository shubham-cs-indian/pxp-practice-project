package com.cs.imprt.config.strategy.plugin.usecase.process;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityConfigDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetKlassWithoutKPByCode extends AbstractOrientPlugin {
  
  public GetKlassWithoutKPByCode(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String code = requestMap.get(IIdParameterModel.ID)
        .toString();
    String entityType = requestMap.get("Entity_type")
        .toString();
    Boolean getTaxonomyIds = (Boolean) requestMap.get("getTaxonomyIds");
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select * from " + entityType + " where code='" + code + "'"))
        .execute();
    
    if (resultIterable.iterator()
        .hasNext()) {
      Vertex klassVertex = resultIterable.iterator()
          .next();
      returnMap = KlassGetUtils.getKlassEntityReferencesMap(klassVertex, false);
      // I have update this things
      Iterable<Edge> edges = klassVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      if (edges.iterator()
          .hasNext()) {
        Map<String, String> referencedContexts = new HashMap<String, String>();
        Vertex variantContext = edges.iterator()
            .next()
            .getVertex(Direction.IN);
        String variantContextCode = (String) variantContext
            .getProperty(CommonConstants.CODE_PROPERTY);
        String variantContextLabel = (String) variantContext
            .getProperty("label__" + UtilClass.getLanguage());
        referencedContexts.put(variantContextCode, variantContextLabel);
        Map<String, Object> configDetails = new HashMap<>();
        configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_CONTEXTS,
            referencedContexts);
        returnMap.put(IGetKlassEntityWithoutKPModel.CONFIG_DETAILS, configDetails);
      } // last is here
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassWithoutKPByCode/*" };
  }
}
