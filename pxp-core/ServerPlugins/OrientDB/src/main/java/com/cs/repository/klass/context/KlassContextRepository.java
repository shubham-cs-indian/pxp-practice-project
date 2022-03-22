package com.cs.repository.klass.context;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;

public class KlassContextRepository {
  
  /**
   * @author Kushal.Nanglia
   * @param klassOrTaxonomyIds
   * @return
   */
  public static Iterable<Vertex> getKlassContextPropagablePropertyVerticesTransfer(
      List<String> klassOrTaxonomyIds)
  {
    String query = "SELECT FROM (SELECT EXPAND (out('"
        + RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK
        + "')) FROM (SELECT EXPAND(rid) FROM INDEX:RootKlass.code WHERE key in"
        + EntityUtil.quoteIt(klassOrTaxonomyIds) + "))";
    
    Iterable<Vertex> klassContextualPropagablePropertyVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return klassContextualPropagablePropertyVertices;
  }
  
  /**
   * @author Kushal.Nanglia
   * @param klassOrTaxonomyIds
   * @param parentKlassOrTaxonomyIds
   * @return
   */
  public static Iterable<Vertex> klassContextPropagablePropertyVerticesInherit(
      List<String> klassOrTaxonomyIds, List<String> parentKlassOrTaxonomyIds)
  {
    String query = "select from (select expand (in('"
        + RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK
        + "')) from (SELECT EXPAND(rid) FROM INDEX:RootKlass.code WHERE key in "
        + EntityUtil.quoteIt(klassOrTaxonomyIds) + ")) where IN('"
        + RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK + "').code in "
        + EntityUtil.quoteIt(parentKlassOrTaxonomyIds);
    
    Iterable<Vertex> klassContextualPropagablePropertyVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return klassContextualPropagablePropertyVertices;
  }
}
