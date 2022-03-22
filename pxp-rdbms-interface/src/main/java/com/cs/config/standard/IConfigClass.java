package com.cs.config.standard;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of all types and class names from the configuration database
 *
 * @author vallee
 */
public interface IConfigClass {

  public enum PropertyClass {

    UNKNOWN(""), TAG_TYPE("com.cs.core.config.interactor.entity.tag.Tag"),
    CALCULATED_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.CalculatedAttribute"),
    CONCATENATED_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute"),
    DATE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.DateAttribute"),
    TEXT_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.TextAttribute"),
    CUSTOM_UNIT_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.CustomUnitAttribute"),
    HTML_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.HTMLAttribute"),
    IMAGE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ImageAttribute"),
    FILE_MAPPED_TO_KLASS_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.FileMappedToKlassAttribute"),
    FILE_SUPPLIER_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.FileSupplier"),
    FILE_TYPE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.FileTypeAttribute"),
    FILE_SIZE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.FileSizeAttribute"),
    FILE_NAME_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.FileNameAttribute"),
    CREATED_BY_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute"),
    LAST_MODIFIED_BY_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute"),
    AREA_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.AreaAttribute"),
    MASS_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.MassAttribute"),
    FREQUENCY_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.FrequencyAttribute"),
    CURRENT_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.CurrentAttribute"),
    PRICE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.PriceAttribute"),
    CURRENCY_ATTRIBUTE_TYPE("com.cs.config.interactor.entity.concrete.attribute.CurrencyAttribute"),
    MINIMUM_PRICE_ATTRIBUTE_TYPE(
            "com.cs.config.interactor.entity.concrete.attribute.standard.MinimumPriceAttribute"),
    MAXIMUM_PRICE_ATTRIBUTE_TYPE(
            "com.cs.config.interactor.entity.concrete.attribute.standard.MaximumPriceAttribute"),
    LIST_PRICE_ATTRIBUTE_TYPE(
            "com.cs.config.interactor.entity.concrete.attribute.standard.ListPriceAttribute"),
    SELLING_PRICE_ATTRIBUTE_TYPE(
            "com.cs.config.interactor.entity.concrete.attribute.standard.SellingPriceAttribute"),
    VOLUME_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.VolumeAttribute"),
    TIME_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.TimeAttribute"),
    TEMPERATURE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.TemperatureAttribute"),
    POTENTIAL_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.PotentialAttribute"),
    SPEED_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.SpeedAttribute"),
    PRESSURE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.PressureAttribute"),
    ROTATION_FREQUENCY_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.RotationFrequencyAttribute"),
    AREA_PER_VOLUME_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.AreaPerVolumeAttribute"),
    VOLUME_FLOW_RATE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.VolumeFlowRateAttribute"),
    WEIGHT_PER_TIME_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.WeightPerTimeAttribute"),
    DENSITY_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.DensityAttribute"),
    HEATING_RATE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.HeatingRateAttribute"),
    PROPORTION_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ProportionAttribute"),
    THERMAL_INSULATION_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.ThermalInsulationAttribute"),
    WEIGHT_PER_AREA_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.WeightPerAreaAttribute"),
    SUBSTANCE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.SubstanceAttribute"),
    CONDUCTANCE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.ConductanceAttribute"),
    CHARGE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ChargeAttribute"),
    MAGNETISM_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.MagnetismAttribute"),
    RESISTANCE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ResistanceAttribute"),
    INDUCTANCE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.InductanceAttribute"),
    VISCOCITY_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ViscocityAttribute"),
    CAPACITANCE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.CapacitanceAttribute"),
    ACCELERATION_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.AccelerationAttribute"),
    FORCE_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.ForceAttribute"),
    ILLUMINANCE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.IlluminanceAttribute"),
    RADIATION_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.RadiationAttribute"),
    LUMINOSITY_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.LuminosityAttribute"),
    POWER_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.PowerAttribute"),
    PLANE_ANGLE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.PlaneAngleAttribute"),
    ENERGY_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.EnergyAttribute"),
    DIGITAL_STORAGE_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.attribute.DigitalStorageAttribute"),
    LENGTH_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.LengthAttribute"),
    ASSET_METADATA_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.AssetMetadataAttribute"),
    NUMBER_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.NumberAttribute"),
    RELATIONSHIP_TYPE("com.cs.core.config.interactor.entity.relationship.Relationship"),
    // issued
    // from
    // former
    // constants.java
    // (instead
    // of
    // coreConstnats.java:
    NAME_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.standard.attribute.NameAttribute"),
    CREATED_ON_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute"),
    DUE_DATE_ATTRIBUTE_TYPE(
            "com.cs.config.interactor.entity.concrete.attribute.standard.DueDateAttribute"),
    LAST_MODIFIED_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute"),
    IMAGE_COVER_FLOW_ATTRIBUTE_TYPE(
            "com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute"),
    COVERFLOW_ATTRIBUTE_TYPE("com.cs.core.config.interactor.entity.attribute.CoverflowAttribute");

    private final String classPath;

    private PropertyClass(String classPath) {
      this.classPath = classPath;
    }

    public static PropertyClass valueOfClassPath(String propertyClassPath) {
      Optional<PropertyClass> firstResult = Arrays.stream(values())
              .filter(x -> x.toString()
              .equals(propertyClassPath))
              .findFirst();

      return firstResult.orElse(UNKNOWN);
    }

    @Override
    public String toString() {
      return classPath;
    }
  }

  public enum ClassifierClass {

    UNKNOWN(""),
    HIERARCHY_KLASS_TYPE(
            "com.cs.core.config.interactor.entity.attributiontaxonomy.HierarchyTaxonomy"),
    TAXONOMY_KLASS_TYPE("com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy"),
    PROJECT_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.ProjectKlass"),
    PROJECT_SET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.SetKlass"),
    ASSET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.Asset"),
    MARKET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.Market"),
    TEXT_ASSET_KLASS_TYPE("com.cs.core.config.interactor.entity.textasset.TextAsset"),
    SUPPLIER_KLASS_TYPE("com.cs.core.config.interactor.entity.supplier.Supplier"),
    VIRTUAL_CATALOG_KLASS_TYPE(
            "com.cs.core.config.interactor.entity.virtualcatalog.VirtualCatalog");

    private final String classPath;

    private ClassifierClass(String classPath) {
      this.classPath = classPath;
    }

    public static ClassifierClass valueOfClassPath(String classifierClassPath) {
      Optional<ClassifierClass> firstResult = Arrays.stream(values())
          .filter(x -> x.toString()
              .equals(classifierClassPath))
          .findFirst();

      return firstResult.orElse(UNKNOWN);
    }

    @Override
    public String toString() {
      return classPath;
    }

  }

  public enum EntityClass {

    EVENT_INSTANCE_BASE_TYPE("com.cs.core.runtime.interactor.entity.eventinstance.EventInstance"),
    ARTICLE_INSTANCE_BASE_TYPE(
            "com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance"),
    SET_INSTANCE_BASE_TYPE("com.cs.runtime.interactor.entity.KlassInstanceSet"),
    ASSET_INSTANCE_BASE_TYPE("com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance"),
    MARKET_INSTANCE_BASE_TYPE("com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance"),
    TEXTASSET_INSTANCE_BASE_TYPE(
            "com.cs.core.runtime.interactor.entity.textassetinstance.TextAssetInstance"),
    SUPPLIER_INSTANCE_BASE_TYPE(
            "com.cs.core.runtime.interactor.entity.supplierinstance.SupplierInstance"),
    VIRTUAL_CATALOG_INSTANCE_BASE_TYPE(
            "com.cs.core.runtime.interactor.entity.virtualcataloginstance.VirtualCatalogInstance"),
    FILE_INSTANCE_BASE_TYPE(
            "com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance"),
    PROJECT_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.ProjectKlass"),
    PROJECT_SET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.SetKlass"),
    ASSET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.Asset"),
    MARKET_KLASS_TYPE("com.cs.core.config.interactor.entity.klass.Market"),
    TEXT_ASSET_KLASS_TYPE("com.cs.core.config.interactor.entity.textasset.TextAsset"),
    SUPPLIER_KLASS_TYPE("com.cs.core.config.interactor.entity.supplier.Supplier"),
    VIRTUAL_CATALOG_KLASS_TYPE(
            "com.cs.core.config.interactor.entity.virtualcatalog.VirtualCatalog"),
    TARGET_KLASS_TYPE(
            "com.cs.core.config.interactor.entity.klass.Target");

    private final String classPath;

    private EntityClass(String classPath) {
      this.classPath = classPath;
    }

    @Override
    public String toString() {
      return classPath;
    }
  }
}
