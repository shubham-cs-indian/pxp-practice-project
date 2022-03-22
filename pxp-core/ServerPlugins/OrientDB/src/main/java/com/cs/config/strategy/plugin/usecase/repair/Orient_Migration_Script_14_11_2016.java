package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_14_11_2016 extends AbstractOrientPlugin {
  
  /*
  Fix related to addition of "precision" in number & unit attributes.
  This script adds "precision" property in existing Number, Unit,
  List Price, Selling Price, Min. & Max. Allowed Price Attribute Nodes.
  Date: 14-11-2016
   */
  
  public Orient_Migration_Script_14_11_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> numberOrMeasurementTypes = Arrays.asList(
        "com.cs.config.interactor.model.concrete.attribute.NumberAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.AreaAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.CurrencyAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.CurrentAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.DigitalStorageAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.EnergyAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.FrequencyAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.LengthAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.MassAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.PlaneAngleAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.PotentialAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.PressureAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.SpeedAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.TemperatureAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.TimeAttributeModel",
        "com.cs.config.interactor.model.concrete.attribute.VolumeAttributeModel");
    
    List<String> standardAttributeTypesToChange = Arrays.asList(SystemLevelIds.LIST_PRICE_ATTRIBUTE,
        SystemLevelIds.SELLING_PRICE_ATTRIBUTE, SystemLevelIds.MAXIMUM_PRICE_ATTRIBUTE,
        SystemLevelIds.MINIMUM_PRICE_ATTRIBUTE);
    
    Map<String, Object> response = new HashMap<>();
    
    // for standard attributes
    for (String type : standardAttributeTypesToChange) {
      Vertex attribute = UtilClass.getVertexById(type, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      attribute.setProperty(INumberAttribute.PRECISION, 5);
    }
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE))
        .execute();
    // attribute node
    for (Vertex attributeNode : iterable) {
      String type = attributeNode.getProperty(CommonConstants.TYPE_PROPERTY);
      if (numberOrMeasurementTypes.contains(type)) {
        attributeNode.setProperty(INumberAttribute.PRECISION, 5);
      }
    }
    UtilClass.getGraph()
        .commit();
    response.put("status", "SUCCESS");
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_14_11_2016/*" };
  }
}
