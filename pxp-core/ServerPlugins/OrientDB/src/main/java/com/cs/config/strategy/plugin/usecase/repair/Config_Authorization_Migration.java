/*
 * (Mapped Columns are getting displayed empty in red box if UI language is
 * changed) Replacing the label property with columnName property and first
 * priority for copying the value is en_US.
 */
package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Config_Authorization_Migration extends AbstractOrientPlugin {
  
  public Config_Authorization_Migration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Config_Authorization_Migration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String query = "select * from " + VertexLabelConstants.UI_TRANSLATIONS + " where code='UPDATE_AUTHORIZATION'";
    String authMappingQuery = "select * from " + VertexLabelConstants.UI_TRANSLATIONS + " where code='AUTHORIZATION_MAPPING_DETAILS'";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();    
    Iterator<Vertex> iterator = vertices.iterator();   
    Iterable<Vertex> mappingVertices = UtilClass.getGraph()
        .command(new OCommandSQL(authMappingQuery))
        .execute();    
    Iterator<Vertex> mappingIterator = mappingVertices.iterator(); 
    if(iterator.hasNext()) {
      Vertex auth = iterator.next();
      auth.setProperty(CommonConstants.CODE_PROPERTY, "AUTHORIZATION");
      auth.setProperty("label__en_US", "Authorization");
      auth.setProperty("label__es_ES", "Autorizaci�n");
      auth.setProperty("label__de_DE", "Genehmigung");
      auth.setProperty("label__en_UK", "Authorization");      
    }        
    if(mappingIterator.hasNext()) {
      Vertex authMapping = mappingIterator.next();
      authMapping.setProperty(CommonConstants.CODE_PROPERTY, "DATA_INTEGRATION_AUTHORIZATION");
      authMapping.setProperty("label__en_US", "Data Integration Authorization");
      authMapping.setProperty("label__es_ES", "Autorización de integración de datos");
      authMapping.setProperty("label__de_DE", "Datenintegrationsautorisierung");
      authMapping.setProperty("label__en_UK", "Data Integration Authorization");      
    }    
    UtilClass.getGraph().commit();    
    return null;
  }
  
}
