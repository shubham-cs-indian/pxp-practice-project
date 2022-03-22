package com.cs.core.runtime.interactor.constants.application;

import java.util.Arrays;
import java.util.List;

public class CoreConstant {
  
  public static final String TAG_TYPE                            = "com.cs.core.config.interactor.entity.tag.Tag";
  public static final String BOOLEAN_TAG_TYPE_ID                 = "tag_type_boolean";
  
  public static final String CALCULATED_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.attribute.CalculatedAttribute";
  public static final String CONCATENATED_ATTRIBUTE_TYPE         = "com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute";
  public static final String DATE_ATTRIBUTE_TYPE                 = "com.cs.core.config.interactor.entity.attribute.DateAttribute";
  public static final String TEXT_ATTRIBUTE_TYPE                 = "com.cs.core.config.interactor.entity.attribute.TextAttribute";
  public static final String CUSTOM_UNIT_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.CustomUnitAttribute";
  public static final String HTML_TYPE_ATTRIBUTE                 = "com.cs.core.config.interactor.entity.attribute.HTMLAttribute";
  public static final String IMAGE_TYPE_ATTRIBUTE                = "com.cs.core.config.interactor.entity.attribute.ImageAttribute";
  public static final String FILE_MAPPED_TO_KLASS_ATTRIBUTE_TYPE = "com.cs.core.config.interactor.entity.standard.attribute.FileMappedToKlassAttribute";
  public static final String FILE_SUPPLIER_ATTRIBUTE_TYPE        = "com.cs.core.config.interactor.entity.standard.attribute.FileSupplier";
  public static final String FILE_TYPE_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute";
  public static final String FILE_SIZE_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.standard.attribute.FileSizeAttribute";
  public static final String FILE_NAME_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.standard.attribute.FileNameAttribute";
  public static final String CREATED_BY_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute";
  public static final String LAST_MODIFIED_BY_ATTRIBUTE_TYPE     = "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute";
  public static final String AREA_ATTRIBUTE_TYPE                 = "com.cs.core.config.interactor.entity.attribute.AreaAttribute";
  public static final String MASS_ATTRIBUTE_TYPE                 = "com.cs.core.config.interactor.entity.attribute.MassAttribute";
  public static final String FREQUENCY_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.FrequencyAttribute";
  public static final String CURRENT_ATTRIBUTE_TYPE              = "com.cs.core.config.interactor.entity.attribute.CurrentAttribute";
  public static final String PRICE_ATTRIBUTE_TYPE                = "com.cs.core.config.interactor.entity.attribute.PriceAttribute";
  public static final String CURRENCY_ATTRIBUTE_TYPE             = "com.cs.config.interactor.entity.concrete.attribute.CurrencyAttribute";
  public static final String MINIMUM_PRICE_ATTRIBUTE_TYPE        = "com.cs.config.interactor.entity.concrete.attribute.standard.MinimumPriceAttribute";
  public static final String MAXIMUM_PRICE_ATTRIBUTE_TYPE        = "com.cs.config.interactor.entity.concrete.attribute.standard.MaximumPriceAttribute";
  public static final String LIST_PRICE_ATTRIBUTE_TYPE           = "com.cs.config.interactor.entity.concrete.attribute.standard.ListPriceAttribute";
  public static final String SELLING_PRICE_ATTRIBUTE_TYPE        = "com.cs.config.interactor.entity.concrete.attribute.standard.SellingPriceAttribute";
  public static final String VOLUME_ATTRIBUTE_TYPE               = "com.cs.core.config.interactor.entity.attribute.VolumeAttribute";
  public static final String TIME_ATTRIBUTE_TYPE                 = "com.cs.core.config.interactor.entity.attribute.TimeAttribute";
  public static final String TEMPERATURE_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.TemperatureAttribute";
  public static final String POTENTIAL_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.PotentialAttribute";
  public static final String SPEED_ATTRIBUTE_TYPE                = "com.cs.core.config.interactor.entity.attribute.SpeedAttribute";
  public static final String PRESSURE_ATTRIBUTE_TYPE             = "com.cs.core.config.interactor.entity.attribute.PressureAttribute";
  public static final String ROTATION_FREQUENCY_ATTRIBUTE_TYPE   = "com.cs.core.config.interactor.entity.attribute.RotationFrequencyAttribute";
  public static final String AREA_PER_VOLUME_ATTRIBUTE_TYPE      = "com.cs.core.config.interactor.entity.attribute.AreaPerVolumeAttribute";
  public static final String VOLUME_FLOW_RATE_ATTRIBUTE_TYPE     = "com.cs.core.config.interactor.entity.attribute.VolumeFlowRateAttribute";
  public static final String WEIGHT_PER_TIME_ATTRIBUTE_TYPE      = "com.cs.core.config.interactor.entity.attribute.WeightPerTimeAttribute";
  public static final String DENSITY_ATTRIBUTE_TYPE              = "com.cs.core.config.interactor.entity.attribute.DensityAttribute";
  public static final String HEATING_RATE_ATTRIBUTE_TYPE         = "com.cs.core.config.interactor.entity.attribute.HeatingRateAttribute";
  public static final String PROPORTION_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.attribute.ProportionAttribute";
  public static final String THERMAL_INSULATION_ATTRIBUTE_TYPE   = "com.cs.core.config.interactor.entity.attribute.ThermalInsulationAttribute";
  public static final String WEIGHT_PER_AREA_ATTRIBUTE_TYPE      = "com.cs.core.config.interactor.entity.attribute.WeightPerAreaAttribute";
  public static final String SUBSTANCE_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.SubstanceAttribute";
  public static final String CONDUCTANCE_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.ConductanceAttribute";
  public static final String CHARGE_ATTRIBUTE_TYPE               = "com.cs.core.config.interactor.entity.attribute.ChargeAttribute";
  public static final String MAGNETISM_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.MagnetismAttribute";
  public static final String RESISTANCE_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.attribute.ResistanceAttribute";
  public static final String INDUCTANCE_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.attribute.InductanceAttribute";
  public static final String VISCOCITY_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.ViscocityAttribute";
  public static final String CAPACITANCE_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.CapacitanceAttribute";
  public static final String ACCELERATION_ATTRIBUTE_TYPE         = "com.cs.core.config.interactor.entity.attribute.AccelerationAttribute";
  public static final String FORCE_ATTRIBUTE_TYPE                = "com.cs.core.config.interactor.entity.attribute.ForceAttribute";
  public static final String ILLUMINANCE_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.IlluminanceAttribute";
  public static final String RADIATION_ATTRIBUTE_TYPE            = "com.cs.core.config.interactor.entity.attribute.RadiationAttribute";
  public static final String LUMINOSITY_ATTRIBUTE_TYPE           = "com.cs.core.config.interactor.entity.attribute.LuminosityAttribute";
  public static final String POWER_ATTRIBUTE_TYPE                = "com.cs.core.config.interactor.entity.attribute.PowerAttribute";
  public static final String PLANE_ANGLE_ATTRIBUTE_TYPE          = "com.cs.core.config.interactor.entity.attribute.PlaneAngleAttribute";
  public static final String ENERGY_ATTRIBUTE_TYPE               = "com.cs.core.config.interactor.entity.attribute.EnergyAttribute";
  public static final String DIGITAL_STORAGE_ATTRIBUTE_TYPE      = "com.cs.core.config.interactor.entity.attribute.DigitalStorageAttribute";
  public static final String LENGTH_ATTRIBUTE_TYPE               = "com.cs.core.config.interactor.entity.attribute.LengthAttribute";
  public static final String NUMBER_ATTRIBUTE_TYPE               = "com.cs.core.config.interactor.entity.attribute.NumberAttribute";
  public static final String ASSET_METADATA_ATTRIBUTE_TYPE       = "com.cs.core.config.interactor.entity.standard.attribute.AssetMetadataAttribute";
  
  public static final String RELATIONSHIP_TYPE                   = "com.cs.core.config.interactor.entity.relationship.Relationship";
  
  public static final List<String>               NUMERIC_TYPE                         = Arrays.asList(CALCULATED_ATTRIBUTE_TYPE, DATE_ATTRIBUTE_TYPE,
      CUSTOM_UNIT_ATTRIBUTE_TYPE, AREA_ATTRIBUTE_TYPE, MASS_ATTRIBUTE_TYPE, FREQUENCY_ATTRIBUTE_TYPE, CURRENT_ATTRIBUTE_TYPE, PRICE_ATTRIBUTE_TYPE,
      CURRENCY_ATTRIBUTE_TYPE, MINIMUM_PRICE_ATTRIBUTE_TYPE, MAXIMUM_PRICE_ATTRIBUTE_TYPE, LIST_PRICE_ATTRIBUTE_TYPE,
      SELLING_PRICE_ATTRIBUTE_TYPE, VOLUME_ATTRIBUTE_TYPE, TIME_ATTRIBUTE_TYPE, TEMPERATURE_ATTRIBUTE_TYPE, POTENTIAL_ATTRIBUTE_TYPE,
      SPEED_ATTRIBUTE_TYPE, PRESSURE_ATTRIBUTE_TYPE, ROTATION_FREQUENCY_ATTRIBUTE_TYPE, AREA_PER_VOLUME_ATTRIBUTE_TYPE,
      VOLUME_FLOW_RATE_ATTRIBUTE_TYPE, WEIGHT_PER_TIME_ATTRIBUTE_TYPE, DENSITY_ATTRIBUTE_TYPE, HEATING_RATE_ATTRIBUTE_TYPE, PROPORTION_ATTRIBUTE_TYPE,
      THERMAL_INSULATION_ATTRIBUTE_TYPE, WEIGHT_PER_AREA_ATTRIBUTE_TYPE, SUBSTANCE_ATTRIBUTE_TYPE, CONDUCTANCE_ATTRIBUTE_TYPE, CHARGE_ATTRIBUTE_TYPE,
      MAGNETISM_ATTRIBUTE_TYPE, RESISTANCE_ATTRIBUTE_TYPE, INDUCTANCE_ATTRIBUTE_TYPE, VISCOCITY_ATTRIBUTE_TYPE, CAPACITANCE_ATTRIBUTE_TYPE,
      ACCELERATION_ATTRIBUTE_TYPE, FORCE_ATTRIBUTE_TYPE, ILLUMINANCE_ATTRIBUTE_TYPE, RADIATION_ATTRIBUTE_TYPE, LUMINOSITY_ATTRIBUTE_TYPE,
      POWER_ATTRIBUTE_TYPE, PLANE_ANGLE_ATTRIBUTE_TYPE, ENERGY_ATTRIBUTE_TYPE, DIGITAL_STORAGE_ATTRIBUTE_TYPE, LENGTH_ATTRIBUTE_TYPE,
      NUMBER_ATTRIBUTE_TYPE, Constants.LAST_MODIFIED_ATTRIBUTE_BASE_TYPE, Constants.CREATED_ON_ATTRIBUTE_BASE_TYPE);

  public static final List<String>               NUMERIC_TYPE_WITHOUT_DATE                         = Arrays.asList(CALCULATED_ATTRIBUTE_TYPE,
      CUSTOM_UNIT_ATTRIBUTE_TYPE, AREA_ATTRIBUTE_TYPE, MASS_ATTRIBUTE_TYPE, FREQUENCY_ATTRIBUTE_TYPE, CURRENT_ATTRIBUTE_TYPE, PRICE_ATTRIBUTE_TYPE,
      CURRENCY_ATTRIBUTE_TYPE, MINIMUM_PRICE_ATTRIBUTE_TYPE, MAXIMUM_PRICE_ATTRIBUTE_TYPE, LIST_PRICE_ATTRIBUTE_TYPE,
      SELLING_PRICE_ATTRIBUTE_TYPE, VOLUME_ATTRIBUTE_TYPE, TIME_ATTRIBUTE_TYPE, TEMPERATURE_ATTRIBUTE_TYPE, POTENTIAL_ATTRIBUTE_TYPE,
      SPEED_ATTRIBUTE_TYPE, PRESSURE_ATTRIBUTE_TYPE, ROTATION_FREQUENCY_ATTRIBUTE_TYPE, AREA_PER_VOLUME_ATTRIBUTE_TYPE,
      VOLUME_FLOW_RATE_ATTRIBUTE_TYPE, WEIGHT_PER_TIME_ATTRIBUTE_TYPE, DENSITY_ATTRIBUTE_TYPE, HEATING_RATE_ATTRIBUTE_TYPE, PROPORTION_ATTRIBUTE_TYPE,
      THERMAL_INSULATION_ATTRIBUTE_TYPE, WEIGHT_PER_AREA_ATTRIBUTE_TYPE, SUBSTANCE_ATTRIBUTE_TYPE, CONDUCTANCE_ATTRIBUTE_TYPE, CHARGE_ATTRIBUTE_TYPE,
      MAGNETISM_ATTRIBUTE_TYPE, RESISTANCE_ATTRIBUTE_TYPE, INDUCTANCE_ATTRIBUTE_TYPE, VISCOCITY_ATTRIBUTE_TYPE, CAPACITANCE_ATTRIBUTE_TYPE,
      ACCELERATION_ATTRIBUTE_TYPE, FORCE_ATTRIBUTE_TYPE, ILLUMINANCE_ATTRIBUTE_TYPE, RADIATION_ATTRIBUTE_TYPE, LUMINOSITY_ATTRIBUTE_TYPE,
      POWER_ATTRIBUTE_TYPE, PLANE_ANGLE_ATTRIBUTE_TYPE, ENERGY_ATTRIBUTE_TYPE, DIGITAL_STORAGE_ATTRIBUTE_TYPE, LENGTH_ATTRIBUTE_TYPE,
      NUMBER_ATTRIBUTE_TYPE, Constants.LAST_MODIFIED_ATTRIBUTE_BASE_TYPE, Constants.CREATED_ON_ATTRIBUTE_BASE_TYPE);
}
