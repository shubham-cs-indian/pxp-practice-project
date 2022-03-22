package com.cs.core.config.interactor.model.attribute.standard;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.config.interactor.model.variantcontext.AddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.config.interactor.model.variantcontext.VariantContextModifiedTagsModel;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @Type(value = SaveImageCoverflowAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute"),
    @Type(value = SaveCreatedOnAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute"),
    @Type(value = SaveNameAttributeModel.class, name = "com.cs.core.config.interactor.entity.standard.attribute.NameAttribute"),
    @Type(value = SaveHTMLAttributeModel.class, name = CoreConstant.HTML_TYPE_ATTRIBUTE),
    @Type(value = SaveTextAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.TextAttribute"),
    @Type(value = SaveImageAttributeModel.class, name = CoreConstant.IMAGE_TYPE_ATTRIBUTE),
    @Type(value = SaveCoverflowAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.attribute.CoverflowAttribute"),
    @Type(value = SaveDateAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.DateAttribute"),
    @Type(value = SaveCalculatedAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.CalculatedAttribute"),
    @Type(value = SaveConcatenatedAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute"),
    @Type(value = SaveNumberAttributeModel.class, name = CoreConstant.NUMBER_ATTRIBUTE_TYPE),
    @Type(value = SaveLengthAttributeModel.class, name = CoreConstant.LENGTH_ATTRIBUTE_TYPE),
    @Type(value = SaveCustomUnitAttributeModel.class,
        name = CoreConstant.CUSTOM_UNIT_ATTRIBUTE_TYPE),
    @Type(value = SaveDigitalStorageAttributeModel.class,
        name = CoreConstant.DIGITAL_STORAGE_ATTRIBUTE_TYPE),
    @Type(value = SaveEnergyAttributeModel.class, name = CoreConstant.ENERGY_ATTRIBUTE_TYPE),
    @Type(value = SavePlaneAngleAttributeModel.class,
        name = CoreConstant.PLANE_ANGLE_ATTRIBUTE_TYPE),
    @Type(value = SavePowerAttributeModel.class, name = CoreConstant.POWER_ATTRIBUTE_TYPE),
    @Type(value = SaveLuminosityAttributeModel.class,
        name = CoreConstant.LUMINOSITY_ATTRIBUTE_TYPE),
    @Type(value = SaveRadiationAttributeModel.class,
        name = CoreConstant.RADIATION_ATTRIBUTE_TYPE),
    @Type(value = SaveIlluminanceAttributeModel.class,
        name = CoreConstant.ILLUMINANCE_ATTRIBUTE_TYPE),
    @Type(value = SaveForceAttributeModel.class, name = CoreConstant.FORCE_ATTRIBUTE_TYPE),
    @Type(value = SaveAccelerationAttributeModel.class,
        name = CoreConstant.ACCELERATION_ATTRIBUTE_TYPE),
    @Type(value = SaveCapacitanceAttributeModel.class,
        name = CoreConstant.CAPACITANCE_ATTRIBUTE_TYPE),
    @Type(value = SaveViscocityAttributeModel.class,
        name = CoreConstant.VISCOCITY_ATTRIBUTE_TYPE),
    @Type(value = SaveInductanceAttributeModel.class,
        name = CoreConstant.INDUCTANCE_ATTRIBUTE_TYPE),
    @Type(value = SaveResistanceAttributeModel.class,
        name = CoreConstant.RESISTANCE_ATTRIBUTE_TYPE),
    @Type(value = SaveMagnetismAttributeModel.class,
        name = CoreConstant.MAGNETISM_ATTRIBUTE_TYPE),
    @Type(value = SaveChargeAttributeModel.class, name = CoreConstant.CHARGE_ATTRIBUTE_TYPE),
    @Type(value = SaveConductanceAttributeModel.class,
        name = CoreConstant.CONDUCTANCE_ATTRIBUTE_TYPE),
    @Type(value = SaveSubstanceAttributeModel.class,
        name = CoreConstant.SUBSTANCE_ATTRIBUTE_TYPE),
    @Type(value = SavePressureAttributeModel.class, name = CoreConstant.PRESSURE_ATTRIBUTE_TYPE),
    @Type(value = SaveSpeedAttributeModel.class, name = CoreConstant.SPEED_ATTRIBUTE_TYPE),
    @Type(value = SaveCurrentAttributeModel.class, name = CoreConstant.CURRENT_ATTRIBUTE_TYPE),
    @Type(value = SavePotentialAttributeModel.class,
        name = CoreConstant.POTENTIAL_ATTRIBUTE_TYPE),
    @Type(value = SaveFrequencyAttributeModel.class,
        name = CoreConstant.FREQUENCY_ATTRIBUTE_TYPE),
    @Type(value = SaveTimeAttributeModel.class, name = CoreConstant.TIME_ATTRIBUTE_TYPE),
    @Type(value = SaveTemperatureAttributeModel.class,
        name = CoreConstant.TEMPERATURE_ATTRIBUTE_TYPE),
    @Type(value = SaveVolumeAttributeModel.class, name = CoreConstant.VOLUME_ATTRIBUTE_TYPE),
    @Type(value = SaveAreaAttributeModel.class, name = CoreConstant.AREA_ATTRIBUTE_TYPE),
    @Type(value = SaveMassAttributeModel.class, name = CoreConstant.MASS_ATTRIBUTE_TYPE),
    @Type(value = SaveWeightPerAreaAttributeModel.class,
        name = CoreConstant.WEIGHT_PER_AREA_ATTRIBUTE_TYPE),
    @Type(value = SaveProportionAttributeModel.class,
        name = CoreConstant.PROPORTION_ATTRIBUTE_TYPE),
    @Type(value = SaveThermalInsulationAttributeModel.class,
        name = CoreConstant.THERMAL_INSULATION_ATTRIBUTE_TYPE),
    
    /* @Type(value = SaveCurrencyAttributeModel.class,
    name = "com.cs.config.interactor.entity.concrete.attribute.CurrencyAttribute"),*/
    @Type(value = SavePriceAttributeModel.class, name = CoreConstant.PRICE_ATTRIBUTE_TYPE),
    @Type(value = SaveLastModifiedByAttributeModel.class,
        name = CoreConstant.LAST_MODIFIED_BY_ATTRIBUTE_TYPE),
    @Type(value = SaveLastModifiedAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute"),
    @Type(value = SaveCreatedByAttributeModel.class,
        name = CoreConstant.CREATED_BY_ATTRIBUTE_TYPE),
    @Type(value = SaveAssetMetadataAttributeModel.class,
        name = CoreConstant.ASSET_METADATA_ATTRIBUTE_TYPE),
    @Type(value = SaveFileNameAttributeModel.class,
        name = CoreConstant.FILE_NAME_ATTRIBUTE_TYPE),
    @Type(value = SaveFileSizeAttributeModel.class,
        name = CoreConstant.FILE_SIZE_ATTRIBUTE_TYPE),
    @Type(value = SaveFileTypeAttributeModel.class,
        name = CoreConstant.FILE_TYPE_ATTRIBUTE_TYPE),
    @Type(value = SaveFileSupplierAttributeModel.class,
        name = CoreConstant.FILE_SUPPLIER_ATTRIBUTE_TYPE),
    @Type(value = SaveFileMappedToKlassAttributeModel.class,
        name = CoreConstant.FILE_MAPPED_TO_KLASS_ATTRIBUTE_TYPE),
    @Type(value = SaveHeatingRateAttributeModel.class,
        name = CoreConstant.HEATING_RATE_ATTRIBUTE_TYPE),
    @Type(value = SaveDensityAttributeModel.class, name = CoreConstant.DENSITY_ATTRIBUTE_TYPE),
    @Type(value = SaveWeightPerTimeAttributeModel.class,
        name = CoreConstant.WEIGHT_PER_TIME_ATTRIBUTE_TYPE),
    @Type(value = SaveVolumeFlowRateAttributeModel.class,
        name = CoreConstant.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE),
    @Type(value = SaveAreaPerVolumeAttributeModel.class,
        name = CoreConstant.AREA_PER_VOLUME_ATTRIBUTE_TYPE),
    @Type(value = SaveRotationFrequencyAttributeModel.class,
        name = CoreConstant.ROTATION_FREQUENCY_ATTRIBUTE_TYPE),
     })
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractSaveAttributeModel extends AbstractAttributeModel
    implements ISaveAttributeModel {
  
  private static final long                        serialVersionUID = 1L;
  
  protected List<IAddedVariantContextTagsModel>    addedTags;
  protected List<IVariantContextModifiedTagsModel> modifiedTags;
  protected List<String>                           deletedTags;
  
  public AbstractSaveAttributeModel(IAttribute attribute, String renderer)
  {
    super(attribute, renderer);
  }
  
  @Override
  public List<IAddedVariantContextTagsModel> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<IAddedVariantContextTagsModel>();
    }
    return addedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = AddedVariantContextTagsModel.class)
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<IVariantContextModifiedTagsModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<IVariantContextModifiedTagsModel>();
    }
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantContextModifiedTagsModel.class)
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<String>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
}
