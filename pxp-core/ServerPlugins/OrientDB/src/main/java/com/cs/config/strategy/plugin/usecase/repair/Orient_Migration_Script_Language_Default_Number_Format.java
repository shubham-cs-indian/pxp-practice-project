package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class Orient_Migration_Script_Language_Default_Number_Format
    extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Language_Default_Number_Format(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Language_Default_Number_Format/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Map<String, String> numberFormats = fillNumberFormats();
    
    String query = "select from " + VertexLabelConstants.LANGUAGE;
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : vertices) {
      String numberFormat = vertex.getProperty(ILanguage.NUMBER_FORMAT);
      vertex.setProperty(ILanguage.NUMBER_FORMAT, numberFormats.get(numberFormat));
    }
    graph.commit();
    return "sucess";
  }
  
  private Map<String, String> fillNumberFormats()
  {
    Map<String, String> numberFormats = new HashMap<String, String>();
    numberFormats.put("# ###,##", "### ### ###,##");
    numberFormats.put("# ###.##", "### ### ###.##");
    numberFormats.put("####.##", "#########.##");
    numberFormats.put("#,###.##", "###,###,###.##");
    numberFormats.put("#.###,##", "###.###.###,##");
    numberFormats.put("#'###.##", "###�###�###.##");
    numberFormats.put("#,###.##", "#,###,###,##.##");
    return numberFormats;
  }
}

// { "id": "7",
// "pluginName":"Orient_Migration_Script_Language_Default_Number_Format",
// "createdOnAsString":"21/11/2018",
// "description":"This migration script will update the default values of
// numberFormat in Languages.
// }
