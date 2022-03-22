package com.cs.core.config.businessapi.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.attribute.IAttribute.Renderer;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AbstractSaveUnitAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AbstractUnitAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CalculatedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ICalculatedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IUnitAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.SaveCalculatedAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.InvalidDefaultUnitException;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;

@Component
public class AttributeValidations extends Validations {
  
  private final List<String> RENDERER_TYPE = Arrays.asList(Renderer.ACCELERATION.toString(),
      Renderer.AREA.toString(), Renderer.AREA_PER_VOLUME.toString(), Renderer.CAPACITANCE.toString(),
      Renderer.CHARGE.toString(), Renderer.CONDUCTANCE.toString(), Renderer.CURRENT.toString(),
      Renderer.DENSITY.toString(), Renderer.DIGITAL_STORAGE.toString(), Renderer.ENERGY.toString(),
      Renderer.FORCE.toString(), Renderer.FREQUENCY.toString(), Renderer.HEATING_RATE.toString(),
      Renderer.ILLUMINANCE.toString(), Renderer.INDUCTANCE.toString(),Renderer.LENGTH.toString(), 
      Renderer.LUMINOSITY.toString(), Renderer.MAGNETISM.toString(), Renderer.MASS.toString(),
      Renderer.PLANE_ANGLE.toString(), Renderer.POTENTIAL.toString(), Renderer.POWER.toString(),
      Renderer.PRESSURE.toString(), Renderer.PRICE.toString(), Renderer.PROPORTION.toString(),
      Renderer.RADIATION.toString(), Renderer.RESISTANCE.toString(), Renderer.ROTATION_FREQUENCY.toString(),
      Renderer.SPEED.toString(), Renderer.SUBSTANCE.toString(), Renderer.TEMPERATURE.toString(),
      Renderer.THERMAL_INSULATION.toString(), Renderer.TIME.toString(), Renderer.VISCOCITY.toString(),
      Renderer.VOLUME.toString(), Renderer.VOLUME_FLOW_RATE.toString(), Renderer.WEIGHT_PER_TIME.toString(),
      Renderer.WEIGHT_PER_AREA.toString());
  
  public void validateAttributeCreation(IAttributeModel attributeModel) 
      throws Exception 
  {
    validateAttribute(attributeModel, "CREATE");
  }
  
  public void validateAttributeUpdation(IListModel<ISaveAttributeModel> model) 
      throws Exception 
  {
    for(IAttributeModel attributeModel : model.getList()) 
    {
      validateAttribute(attributeModel, "SAVE");
    }
  }

  private void validateAttribute(IAttributeModel attributeModel, String usecase) 
      throws Exception
  {
    validate(attributeModel.getCode(), attributeModel.getLabel());
    
    String type = attributeModel.getType();
    validateType(type);
    
    if (type.equals(Constants.CALCULATED_ATTRIBUTE_TYPE)) 
    {
      ICalculatedAttributeModel calculatedAttributeModel;
      if(usecase == "CREATE")
      {
        calculatedAttributeModel = (CalculatedAttributeModel) attributeModel;
      }
      else
      {
        calculatedAttributeModel = (SaveCalculatedAttributeModel) attributeModel;
      }
      validateCalculatedAttribute(calculatedAttributeModel.getCalculatedAttributeUnit(),
          calculatedAttributeModel.getCalculatedAttributeType());
    }
    else if (RENDERER_TYPE.contains(attributeModel.getRendererType())
        && !type.equals(CoreConstant.CUSTOM_UNIT_ATTRIBUTE_TYPE)) 
    {
      IUnitAttributeModel attribute; 
      if(usecase == "CREATE")
      {
        attribute = (AbstractUnitAttributeModel)attributeModel;
      }
      else 
      {
        attribute = (AbstractSaveUnitAttributeModel)attributeModel;
      }
      validateDefaultUnit(attribute.getDefaultUnit(), attribute.getType());
    }
  }
  
  
  private void validateDefaultUnit(String defaultUnit, String measurmentType) 
      throws InvalidDefaultUnitException
  {
    if (defaultUnit == null || defaultUnit.isEmpty()) 
    {
      throw new InvalidDefaultUnitException();
    }
    else if (!getDefaultUnitsList(measurmentType).contains(defaultUnit)) 
    {
      throw new InvalidDefaultUnitException();
    }
  }
  
  private void validateType(String type) 
      throws InvalidTypeException
  {
    PropertyType propertyType = IConfigMap.getPropertyType(type);
    if (type == null || type.isEmpty() || propertyType == null) 
    {
      throw new InvalidTypeException();
    }
  }
  
  private List<String> getDefaultUnitsList(String measurmentType)
  {
    switch (measurmentType) 
    {
      case CommonConstants.ACCELERATION_ATTRIBUTE_TYPE:
        return Arrays.asList("gee");
      case CommonConstants.AREA_ATTRIBUTE_TYPE:
        return Arrays.asList("km2", "m2", "mi2", "cm2", "mm2", "yd2", "sqft", "in2", "acre", "hectare");
      case CommonConstants.AREA_PER_VOLUME_ATTRIBUTE_TYPE:
        return Arrays.asList("cm2/mL", "cm2/L", "m2/mL", "m2/L");
      case CommonConstants.CAPACITANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("F");
      case CommonConstants.CHARGE_ATTRIBUTE_TYPE:
        return Arrays.asList("C", "Ah");
      case CommonConstants.CONDUCTANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("S");
      case CommonConstants.CURRENT_ATTRIBUTE_TYPE:
        return Arrays.asList("A", "mA");
      case CommonConstants.DENSITY_ATTRIBUTE_TYPE:
        return Arrays.asList("kg/m3", "kg/cm3", "g/m3", "g/cm3");
      case CommonConstants.DIGITAL_STORAGE_ATTRIBUTE_TYPE:
        return Arrays.asList("b", "kb", "Kib", "Mb", "Mib", "Gb", "Gib", "Tb", "Tib", "Pb", "Pib", "B", "kB", "KiB", "MB", "MiB", "GB",
            "GiB", "TB", "TiB", "PB", "PiB");
      case CommonConstants.ENERGY_ATTRIBUTE_TYPE:
        return Arrays.asList("J", "kJ", "Cal", "Wh", "kWh", "BTU", "therm");
      case CommonConstants.FORCE_ATTRIBUTE_TYPE:
        return Arrays.asList("N", "dyn", "lbf");
      case CommonConstants.FREQUENCY_ATTRIBUTE_TYPE:
        return Arrays.asList("Hz", "kHz", "MHz", "GHz");
      case CommonConstants.HEATING_RATE_ATTRIBUTE_TYPE:
        return Arrays.asList("°C/sec", "°C/min", "°C/hr");
      case CommonConstants.ILLUMINANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("lux");
      case CommonConstants.INDUCTANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("H");
      case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
        return Arrays.asList("km", "m", "dm", "cm", "mm", "um", "nm", "mi", "yd", "ft", "in", "nmi");
      case CommonConstants.LUMINOSITY_ATTRIBUTE_TYPE:
        return Arrays.asList("cd", "mcd", "lm");
      case CommonConstants.MAGNETISM_ATTRIBUTE_TYPE:
        return Arrays.asList("T", "G");
      case CommonConstants.MASS_ATTRIBUTE_TYPE:
        return Arrays.asList("tonne", "kg", "g", "mg", "ug", "ton", "stone", "lbs", "oz");
      case CommonConstants.PLANE_ANGLE_ATTRIBUTE_TYPE:
        return Arrays.asList("deg", "gon", "mrad", "rad");
      case CommonConstants.POTENTIAL_ATTRIBUTE_TYPE:
        return Arrays.asList("V", "mV");
      case CommonConstants.POWER_ATTRIBUTE_TYPE:
        return Arrays.asList("W", "hp", "kW", "mW");
      case CommonConstants.PRESSURE_ATTRIBUTE_TYPE:
        return Arrays.asList("atm", "bar", "Pa", "psi", "torr", "kPa");
      case CommonConstants.PRICE_ATTRIBUTE_TYPE:
        return Arrays.asList("EUR", "USD", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS",
            "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "ZAR");
      case CommonConstants.PROPORTION_ATTRIBUTE_TYPE:
        return Arrays.asList("%", "x/y");
      case CommonConstants.RADIATION_ATTRIBUTE_TYPE:
        return Arrays.asList("Gy", "R", "Sv");
      case CommonConstants.RESISTANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("ohm");
      case CommonConstants.ROTATION_FREQUENCY_ATTRIBUTE_TYPE:
        return Arrays.asList("rev/sec", "rev/min", "rev/hr");
      case CommonConstants.SPEED_ATTRIBUTE_TYPE:
        return Arrays.asList("mph", "fps", "m/s", "kph", "kt");
      case CommonConstants.SUBSTANCE_ATTRIBUTE_TYPE:
        return Arrays.asList("mol");
      case CommonConstants.TEMPERATURE_ATTRIBUTE_TYPE:
        return Arrays.asList("tempC", "tempF", "tempK");
      case CommonConstants.THERMAL_INSULATION_ATTRIBUTE_TYPE:
        return Arrays.asList("m2K/W", "hr-ft2-F/BTU");
      case CommonConstants.TIME_ATTRIBUTE_TYPE:
        return Arrays.asList("ns", "us", "ms", "sec", "hr", "min", "days", "weeks", "years", "decades", "centuries");
      case CommonConstants.VISCOCITY_ATTRIBUTE_TYPE:
        return Arrays.asList("P");
      case CommonConstants.VOLUME_ATTRIBUTE_TYPE:
        return Arrays.asList("gal", "qt", "pt", "cu", "floz", "tbsp", "tsp", "m3", "L", "ml", "ft3", "in3", "cm3");
      case CommonConstants.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE:
        return Arrays.asList("m3/sec", "m3/min", "m3/hr", "L/sec", "L/min", "L/hr");
      case CommonConstants.WEIGHT_PER_TIME_ATTRIBUTE_TYPE:
        return Arrays.asList("gm/sec", "gm/min", "gm/hr", "gm/day", "kg/sec", "kg/min", "kg/hour", "kg/day");
      case CommonConstants.WEIGHT_PER_AREA_ATTRIBUTE_TYPE:
        return Arrays.asList("kg/m2", "gm/m2", "lb/yd2", "lb/ft2", "lb/in2", "oz/in2", "oz/ft2", "tn/mi2", "tn/ac1", "t/km2");
      default:
        return new ArrayList<>();
    }
  }
  
  private void validateCalculatedAttribute(String defaultUnit, String measurmentType) 
      throws InvalidDefaultUnitException
  {
    if (measurmentType != null && !measurmentType.isEmpty() && !measurmentType.equals(CoreConstant.CUSTOM_UNIT_ATTRIBUTE_TYPE)) 
    {
      validateDefaultUnit(defaultUnit, measurmentType);
    }
  }
  
}
