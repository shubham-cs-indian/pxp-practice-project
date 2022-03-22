package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.hidden.IGetPropertyTranslationsHiddenModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class Orient_Migration_Script_for_Linked_Variant_Relationships extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_for_Linked_Variant_Relationships(
      OServerCommandConfiguration iConfiguration)
  {
    
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_for_Linked_Variant_Relationships/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Iterable<Vertex> natureRelationshipVertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.NATURE_RELATIONSHIP);
    
    for (Vertex natureRelationshipVertex : natureRelationshipVertices) {
      
      Map<String, Object> side1 = natureRelationshipVertex.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2 = natureRelationshipVertex.getProperty(IRelationship.SIDE2);
      String side1Label = (String) side1.get(IRelationshipSide.LABEL);
      String side2Label = (String) side2.get(IRelationshipSide.LABEL);
      
      if (side1Label == null) {
        String klassId = (String) side1.get(IRelationshipSide.KLASS_ID);
        Vertex klass = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
        side1Label = klass.getProperty(IGetPropertyTranslationsHiddenModel.LABEL__EN_US);
        side1.put(IRelationshipSide.LABEL, side1Label);
        natureRelationshipVertex.setProperty(IRelationship.SIDE1, side1);
      }
      
      if (side2Label == null) {
        String klassId = (String) side2.get(IRelationshipSide.KLASS_ID);
        Vertex klass = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
        side2Label = klass.getProperty(IGetPropertyTranslationsHiddenModel.LABEL__EN_US);
        side2.put(IRelationshipSide.LABEL, side2Label);
        natureRelationshipVertex.setProperty(IRelationship.SIDE2, side2);
      }
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
