package com.cs.di.workflow.constants;

import java.util.Arrays;
import java.util.Optional;

public enum CalculatedAttributeTypes
{
  
  UNKNOWN(""), ACCELERATION_ATTRIBUTE_TYPE("acceleration"), AREA_ATTRIBUTE_TYPE("area"), AREA_PER_VOLUME_ATTRIBUTE_TYPE("areaPerVolume"),
  CAPACITANCE_ATTRIBUTE_TYPE("capacitance"), CHARGE_ATTRIBUTE_TYPE("charge"), CONDUCTANCE_ATTRIBUTE_TYPE("conductance"),
  CURRENT_ATTRIBUTE_TYPE("current"), CUSTOM_UNIT_ATTRIBUTE_TYPE("customUnit"), DENSITY_ATTRIBUTE_TYPE("density"),
  DIGITAL_STORAGE_ATTRIBUTE_TYPE("digitalStorage"), ENERGY_ATTRIBUTE_TYPE("energy"), FORCE_ATTRIBUTE_TYPE("force"),
  FREQUENCY_ATTRIBUTE_TYPE("frequency"), HEATING_RATE_ATTRIBUTE_TYPE("heatingRate"), ILLUMINANCE_ATTRIBUTE_TYPE("illuminance"),
  INDUCTANCE_ATTRIBUTE_TYPE("inductance"), LENGTH_ATTRIBUTE_TYPE("length"), LUMINOSITY_ATTRIBUTE_TYPE("luminosity"),
  MAGNETISM_ATTRIBUTE_TYPE("magnetism"), MASS_ATTRIBUTE_TYPE("mass"), PLANE_ANGLE_ATTRIBUTE_TYPE("planeAngle"),
  POTENTIAL_ATTRIBUTE_TYPE("potential"), POWER_ATTRIBUTE_TYPE("power"), PRESSURE_ATTRIBUTE_TYPE("pressure"), PRICE_ATTRIBUTE_TYPE("price"),
  PROPORTION_ATTRIBUTE_TYPE("proportion"), RADIATION_ATTRIBUTE_TYPE("radiation"), RESISTANCE_ATTRIBUTE_TYPE("resistance"),
  ROTATION_FREQUENCY_ATTRIBUTE_TYPE("rotationFrequency"), SUBSTANCE_ATTRIBUTE_TYPE("substance"), SPEED_ATTRIBUTE_TYPE("speed"),
  TEMPERATURE_ATTRIBUTE_TYPE("temperature"), THERMAL_INSULATION_ATTRIBUTE_TYPE("thermalInsulation"), TIME_ATTRIBUTE_TYPE("time"),
  VISCOCITY_ATTRIBUTE_TYPE("viscocity"), VOLUME_ATTRIBUTE_TYPE("volume"), VOLUME_FLOW_RATE_ATTRIBUTE_TYPE("volumeFlowRate"),
  WEIGHT_PER_TIME_ATTRIBUTE_TYPE("weightPerTime"), WEIGHT_PER_AREA_ATTRIBUTE_TYPE("weightPerArea");
  
  private final String abbreviation;
  
  private CalculatedAttributeTypes(String value)
  {
    this.abbreviation = value;
  }
  
  public static CalculatedAttributeTypes valueOfClassPath(String value)
  {
    Optional<CalculatedAttributeTypes> firstResult = Arrays.stream(values()).filter(x -> x.toString().equals(value)).findFirst();
    
    return firstResult.orElse(UNKNOWN);
  }
  
  @Override
  public String toString()
  {
    return abbreviation;
  }
}
