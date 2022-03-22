package com.cs.config.strategy.plugin.usecase.ssoconfiguration.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.sso.DomainAlreadyExistsException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class SSOConfigurationUtils {
  
  public static void checkDomainExistanceInSSOConfiguration(String domain, String organizationId)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String edgeLabel = RelationshipLabelConstants.ORGANIZATION_SSO_LINK;
    String getOrganizationQuery = "select from " + VertexLabelConstants.ORGANIZATION
        + " where code = '" + organizationId + "'";
    
    String query = "select from (select expand(out('" + edgeLabel + "')) from ("
        + getOrganizationQuery + ")) where domain = '" + domain + "'";
    Iterable<Vertex> result = graph.command(new OCommandSQL(query))
        .execute();
    if (result.iterator()
        .hasNext())
      throw new DomainAlreadyExistsException();
  }
}
