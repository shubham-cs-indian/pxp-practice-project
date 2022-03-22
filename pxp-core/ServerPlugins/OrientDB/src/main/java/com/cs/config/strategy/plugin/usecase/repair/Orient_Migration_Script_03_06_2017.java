package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Orient_Migration_Script_03_06_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_03_06_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Set<Vertex> templatesToDelete = new HashSet<>();
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.TEMPLATE))
        .execute();
    
    for (Vertex template : iterable) {
      Object type = template.getProperty(ITemplate.TYPE);
      if (type.equals(CommonConstants.KLASS_TEMPLATE)
          || type.equals(CommonConstants.CUSTOM_TEMPLATE)) {
        continue;
      }
      Iterator<Vertex> iterator = template
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION)
          .iterator();
      if (iterator.hasNext()) {
        continue;
      }
      templatesToDelete.add(template);
    }
    
    for (Vertex templateToDelete : templatesToDelete) {
      // DeleteTemplateUtils.deleteTemplateNode(templateToDelete);
      UtilClass.getGraph()
          .commit();
    }
    System.out.println("Completed!!!");
    
    return "Successful";
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_03_06_2017/*" };
  }
}
