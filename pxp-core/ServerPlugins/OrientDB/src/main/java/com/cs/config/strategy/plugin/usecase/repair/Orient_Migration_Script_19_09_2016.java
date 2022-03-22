/*
 * (UpdateBaseTypeForAttributes) : This migration script dated 19-09-2016 is to
 * update all attribute in Orient configuration to use type of Entity instead of
 * model.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_19_09_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_19_09_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    
    // For data rules
    Iterator<Vertex> attributeIterator = UtilClass.getGraph()
        .getVertices(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, new String[] {}, new String[] {})
        .iterator();
    while (attributeIterator.hasNext()) {
      Vertex attribute = attributeIterator.next();
      String attributeType = attribute.getProperty("type");
      if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.TextAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.TextAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.HTMLAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.HTMLAttribute");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.ImageAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.ImageAttribute");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.CoverflowAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.CoverflowAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.DateAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.DateAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.NumberAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.NumberAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.LengthAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.LengthAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType.equals(
          "com.cs.config.interactor.model.concrete.attribute.DigitalStorageAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.DigitalStorageAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.EnergyAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.EnergyAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.PlaneAngleAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.PlaneAngleAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.PressureAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.PressureAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.SpeedAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.SpeedAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.CurrentAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.CurrentAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.PotentialAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.PotentialAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.FrequencyAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.FrequencyAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.TimeAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.TimeAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.TemperatureAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.TemperatureAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.VolumeAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.VolumeAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.AreaAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.AreaAttribute");
        attribute.removeProperty("validator");
      }
      else if (attributeType
          .equals("com.cs.config.interactor.model.concrete.attribute.MassAttributeModel")) {
        attribute.setProperty("type",
            "com.cs.config.interactor.entity.concrete.attribute.MassAttribute");
        attribute.removeProperty("validator");
      }
    }
    
    UtilClass.getGraph()
        .commit();
    returnMap.put("status", "success");
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_19_09_2016/*" };
  }
}
