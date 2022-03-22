/*
 * Saving Icon Onboarding.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

// @SuppressWarnings("unchecked")
public class Orient_Linking_Icons extends AbstractOrientPlugin {
  
  public Orient_Linking_Icons(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> notFoundIds = new ArrayList<>();
    for (String entityId : requestMap.keySet()) {
      try {
        Vertex entityNode = getEntityNode(entityId);
        entityNode.setProperty(CommonConstants.ICON_PROPERTY, (String) requestMap.get(entityId));
        if (((OrientVertex) entityNode).getType()
            .getAllSuperClasses()
            .contains(UtilClass.getGraph()
                .getVertexType(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS))) {
          entityNode.setProperty(IKlass.PREVIEW_IMAGE, (String) requestMap.get(entityId));
        }
      }
      catch (NoSuchElementException e) {
        notFoundIds.add(entityId);
        continue;
      }
    }
    System.out.println("Ids not found for icon import:");
    System.out.println(notFoundIds);
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> returnMap = new HashMap<>();
    return returnMap;
  }
  
  private Vertex getEntityNode(String entityId)
  {
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVertices("V", new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { entityId })
        .iterator();
    
    return iterator.next();
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Linking_Icons/*" };
  }
}
