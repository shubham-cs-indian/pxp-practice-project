/*
 * (FlatFieldUpdateSectionElementDisabled) : This migration script dated
 * 02-09-2016 is to update all flat field attributes and sections which they
 * contained in Orient so that isDisabled property will be set true.
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_02_09_2016 extends AbstractOrientPlugin {
  
  public static final List<String> disabledAttributes = new ArrayList<>(
      Arrays.asList(IStandardConfig.StandardProperty.createdonattribute.toString(),
          "createdbyattribute", "lastmodifiedattribute", "lastmodifiedbyattribute", "exifattribute",
          "iptcattribute", "xmpattribute", "otherattribute"));
  
  public Orient_Migration_Script_02_09_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    // For Attributes
    Iterator<Vertex> attributeIterator = UtilClass.getGraph()
        .getVerticesOfClass(CommonConstants.ATTRIBUTE_PROPERTY)
        .iterator();
    while (attributeIterator.hasNext()) {
      Vertex attribute = attributeIterator.next();
      if (disabledAttributes.contains(attribute.getProperty(CommonConstants.CODE_PROPERTY))) {
        attribute.setProperty(CommonConstants.IS_DISABLED_PROPERTY, true);
      }
    }
    
    // For sections
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT)
        .iterator();
    while (iterator.hasNext()) {
      Vertex sectionVertex = iterator.next();
      Iterator<Vertex> attributeVertexIterator = sectionVertex
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
          .iterator();
      if (attributeVertexIterator.hasNext()) {
        Vertex attributeVertex = attributeVertexIterator.next();
        HashMap<String, Object> attributeMap = UtilClass.getMapFromNode(attributeVertex);
        if (disabledAttributes.contains(attributeMap.get(CommonConstants.ID_PROPERTY))) {
          sectionVertex.setProperty(CommonConstants.IS_DISABLED_PROPERTY, true);
        }
      }
    }
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_02_09_2016/*" };
  }
}
