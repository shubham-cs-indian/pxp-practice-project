package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_For_Hiding_Local_Of_Number_Type extends AbstractOrientMigration {
  
  public Orient_Migration_For_Hiding_Local_Of_Number_Type(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_Hiding_Local_Of_Number_Type/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    List<String> numberTypes = Arrays.asList(CoreConstant.NUMBER_ATTRIBUTE_TYPE,
        CoreConstant.AREA_ATTRIBUTE_TYPE, CoreConstant.CURRENCY_ATTRIBUTE_TYPE,
        CoreConstant.CURRENT_ATTRIBUTE_TYPE, CoreConstant.DIGITAL_STORAGE_ATTRIBUTE_TYPE,
        CoreConstant.ENERGY_ATTRIBUTE_TYPE, CoreConstant.FREQUENCY_ATTRIBUTE_TYPE,
        CoreConstant.LENGTH_ATTRIBUTE_TYPE, CoreConstant.MASS_ATTRIBUTE_TYPE,
        CoreConstant.PLANE_ANGLE_ATTRIBUTE_TYPE, CoreConstant.POTENTIAL_ATTRIBUTE_TYPE,
        CoreConstant.PRESSURE_ATTRIBUTE_TYPE, CoreConstant.SPEED_ATTRIBUTE_TYPE,
        CoreConstant.TEMPERATURE_ATTRIBUTE_TYPE, CoreConstant.TIME_ATTRIBUTE_TYPE,
        CoreConstant.VOLUME_ATTRIBUTE_TYPE, CoreConstant.POWER_ATTRIBUTE_TYPE,
        CoreConstant.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE, CoreConstant.FORCE_ATTRIBUTE_TYPE,
        CoreConstant.PROPORTION_ATTRIBUTE_TYPE, CoreConstant.WEIGHT_PER_TIME_ATTRIBUTE_TYPE,
        CoreConstant.ACCELERATION_ATTRIBUTE_TYPE, CoreConstant.RADIATION_ATTRIBUTE_TYPE,
        CoreConstant.WEIGHT_PER_AREA_ATTRIBUTE_TYPE, CoreConstant.HEATING_RATE_ATTRIBUTE_TYPE,
        CoreConstant.RESISTANCE_ATTRIBUTE_TYPE, CoreConstant.AREA_PER_VOLUME_ATTRIBUTE_TYPE,
        CoreConstant.ILLUMINANCE_ATTRIBUTE_TYPE, CoreConstant.ROTATION_FREQUENCY_ATTRIBUTE_TYPE,
        CoreConstant.CAPACITANCE_ATTRIBUTE_TYPE, CoreConstant.INDUCTANCE_ATTRIBUTE_TYPE,
        CoreConstant.SPEED_ATTRIBUTE_TYPE, CoreConstant.CHARGE_ATTRIBUTE_TYPE,
        CoreConstant.LUMINOSITY_ATTRIBUTE_TYPE, CoreConstant.SUBSTANCE_ATTRIBUTE_TYPE,
        CoreConstant.CONDUCTANCE_ATTRIBUTE_TYPE, CoreConstant.MAGNETISM_ATTRIBUTE_TYPE,
        CoreConstant.THERMAL_INSULATION_ATTRIBUTE_TYPE, CoreConstant.CUSTOM_UNIT_ATTRIBUTE_TYPE,
        CoreConstant.DENSITY_ATTRIBUTE_TYPE, CoreConstant.VISCOCITY_ATTRIBUTE_TYPE,
        CoreConstant.PRICE_ATTRIBUTE_TYPE, CoreConstant.CALCULATED_ATTRIBUTE_TYPE);
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE))
        .execute();
    // attribute node
    for (Vertex attributeNode : iterable) {
      String type = attributeNode.getProperty(CommonConstants.TYPE_PROPERTY);
      if (numberTypes.contains(type)) {
        attributeNode.setProperty(INumberAttribute.HIDE_SEPARATOR, false);
      }
    }
    
    UtilClass.getGraph()
        .commit();
    
    return null;
  }
}
