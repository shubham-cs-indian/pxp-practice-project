package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.tag.IPropertyTag;
import com.cs.core.config.interactor.entity.tag.PropertyTag;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AccelerationAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AreaAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AreaPerVolumeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AssetMetadataAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CalculatedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CapacitanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ChargeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ConcatenatedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ConductanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CoverflowAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CreatedByAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CreatedOnAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CurrentAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.CustomUnitAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.DateAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.DensityAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.DigitalStorageAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.EnergyAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FileMappedToKlassAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FileNameAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FileSizeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FileSupplierAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FileTypeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ForceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.FrequencyAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.HTMLAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.HeatingRateAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.IlluminanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ImageAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ImageCoverflowAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.InductanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.LastModifiedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.LastModifiedByAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.LengthAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.LuminosityAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.MagnetismAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.MassAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.NameAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.NumberAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.PlaneAngleAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.PotentialAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.PowerAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.PressureAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.PriceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ProportionAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.RadiationAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ResistanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.RotationFrequencyAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.SpeedAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.SubstanceAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.TemperatureAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.TextAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ThermalInsulationAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.TimeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.ViscocityAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.VolumeAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.VolumeFlowRateAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.WeightPerAreaAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.WeightPerTimeAttributeModel;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
//import com.cs.core.runtime.interactor.constants.application.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @Type(value = ImageCoverflowAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.standard.attribute.ImageCoverflowAttribute"),
    @Type(value = CreatedOnAttributeModel.class, name = "com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute"),
    @Type(value = NameAttributeModel.class, name = "com.cs.core.config.interactor.entity.standard.attribute.NameAttribute"),
    @Type(value = HTMLAttributeModel.class, name = CoreConstant.HTML_TYPE_ATTRIBUTE),
    @Type(value = TextAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.TextAttribute"),
    @Type(value = ImageAttributeModel.class, name = "com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance"),
    @Type(value = CoverflowAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.CoverflowAttribute"),
    @Type(value = DateAttributeModel.class, name =  "com.cs.core.config.interactor.entity.attribute.DateAttribute"),
    @Type(value = CalculatedAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.CalculatedAttribute"),
    @Type(value = ConcatenatedAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute"),
    @Type(value = NumberAttributeModel.class, name = CoreConstant.NUMBER_ATTRIBUTE_TYPE),
    @Type(value = LengthAttributeModel.class, name = CoreConstant.LENGTH_ATTRIBUTE_TYPE),
    @Type(value = DigitalStorageAttributeModel.class,
        name = CoreConstant.DIGITAL_STORAGE_ATTRIBUTE_TYPE),
    @Type(value = EnergyAttributeModel.class, name = CoreConstant.ENERGY_ATTRIBUTE_TYPE),
    @Type(value = PlaneAngleAttributeModel.class,
        name = CoreConstant.PLANE_ANGLE_ATTRIBUTE_TYPE),
    @Type(value = PowerAttributeModel.class, name = CoreConstant.POWER_ATTRIBUTE_TYPE),
    @Type(value = LuminosityAttributeModel.class, name = CoreConstant.LUMINOSITY_ATTRIBUTE_TYPE),
    @Type(value = RadiationAttributeModel.class, name = CoreConstant.RADIATION_ATTRIBUTE_TYPE),
    @Type(value = IlluminanceAttributeModel.class,
        name = CoreConstant.ILLUMINANCE_ATTRIBUTE_TYPE),
    @Type(value = ForceAttributeModel.class, name = CoreConstant.FORCE_ATTRIBUTE_TYPE),
    @Type(value = AccelerationAttributeModel.class,
        name = CoreConstant.ACCELERATION_ATTRIBUTE_TYPE),
    @Type(value = CapacitanceAttributeModel.class,
        name = CoreConstant.CAPACITANCE_ATTRIBUTE_TYPE),
    @Type(value = ViscocityAttributeModel.class, name = CoreConstant.VISCOCITY_ATTRIBUTE_TYPE),
    @Type(value = InductanceAttributeModel.class, name = CoreConstant.INDUCTANCE_ATTRIBUTE_TYPE),
    @Type(value = ResistanceAttributeModel.class, name = CoreConstant.RESISTANCE_ATTRIBUTE_TYPE),
    @Type(value = MagnetismAttributeModel.class, name = CoreConstant.MAGNETISM_ATTRIBUTE_TYPE),
    @Type(value = ChargeAttributeModel.class, name = CoreConstant.CHARGE_ATTRIBUTE_TYPE),
    @Type(value = ConductanceAttributeModel.class,
        name = CoreConstant.CONDUCTANCE_ATTRIBUTE_TYPE),
    @Type(value = SubstanceAttributeModel.class, name = CoreConstant.SUBSTANCE_ATTRIBUTE_TYPE),
    @Type(value = PressureAttributeModel.class, name = "com.cs.core.config.interactor.entity.attribute.PressureAttribute"),
    @Type(value = SpeedAttributeModel.class, name = CoreConstant.SPEED_ATTRIBUTE_TYPE),
    @Type(value = CurrentAttributeModel.class, name = CoreConstant.CURRENT_ATTRIBUTE_TYPE),
    @Type(value = PotentialAttributeModel.class, name = CoreConstant.POTENTIAL_ATTRIBUTE_TYPE),
    @Type(value = FrequencyAttributeModel.class, name = CoreConstant.FREQUENCY_ATTRIBUTE_TYPE),
    @Type(value = TimeAttributeModel.class, name = CoreConstant.TIME_ATTRIBUTE_TYPE),
    @Type(value = TemperatureAttributeModel.class,
        name = CoreConstant.TEMPERATURE_ATTRIBUTE_TYPE),
    @Type(value = VolumeAttributeModel.class, name = CoreConstant.VOLUME_ATTRIBUTE_TYPE),
    @Type(value = AreaAttributeModel.class, name = CoreConstant.AREA_ATTRIBUTE_TYPE),
    @Type(value = MassAttributeModel.class, name = CoreConstant.MASS_ATTRIBUTE_TYPE),
    @Type(value = WeightPerAreaAttributeModel.class,
        name = CoreConstant.WEIGHT_PER_AREA_ATTRIBUTE_TYPE),
    @Type(value = ProportionAttributeModel.class, name = CoreConstant.PROPORTION_ATTRIBUTE_TYPE),
    @Type(value = ThermalInsulationAttributeModel.class,
        name = CoreConstant.THERMAL_INSULATION_ATTRIBUTE_TYPE),
    
    /*   @Type(value = CurrencyAttributeModel.class,
    name = "com.cs.config.interactor.entity.concrete.attribute.CurrencyAttribute"),*/
    @Type(value = PriceAttributeModel.class, name = CoreConstant.PRICE_ATTRIBUTE_TYPE),
    @Type(value = CustomUnitAttributeModel.class,
        name = CoreConstant.CUSTOM_UNIT_ATTRIBUTE_TYPE),
    @Type(value = LastModifiedByAttributeModel.class,
        name = CoreConstant.LAST_MODIFIED_BY_ATTRIBUTE_TYPE),
    @Type(value = LastModifiedAttributeModel.class,
        name = "com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute"),
    @Type(value = CreatedByAttributeModel.class, name = CoreConstant.CREATED_BY_ATTRIBUTE_TYPE),
    @Type(value = AssetMetadataAttributeModel.class,
        name = CoreConstant.ASSET_METADATA_ATTRIBUTE_TYPE),
    @Type(value = FileNameAttributeModel.class, name = CoreConstant.FILE_NAME_ATTRIBUTE_TYPE),
    @Type(value = FileSizeAttributeModel.class, name = CoreConstant.FILE_SIZE_ATTRIBUTE_TYPE),
    @Type(value = FileTypeAttributeModel.class, name = CoreConstant.FILE_TYPE_ATTRIBUTE_TYPE),
    @Type(value = FileSupplierAttributeModel.class,
        name = CoreConstant.FILE_SUPPLIER_ATTRIBUTE_TYPE),
    @Type(value = FileMappedToKlassAttributeModel.class,
        name = CoreConstant.FILE_MAPPED_TO_KLASS_ATTRIBUTE_TYPE),
    @Type(value = HeatingRateAttributeModel.class,
        name = CoreConstant.HEATING_RATE_ATTRIBUTE_TYPE),
    @Type(value = DensityAttributeModel.class, name = CoreConstant.DENSITY_ATTRIBUTE_TYPE),
    @Type(value = WeightPerTimeAttributeModel.class,
        name = CoreConstant.WEIGHT_PER_TIME_ATTRIBUTE_TYPE),
    @Type(value = VolumeFlowRateAttributeModel.class,
        name = CoreConstant.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE),
    @Type(value = AreaPerVolumeAttributeModel.class,
        name = CoreConstant.AREA_PER_VOLUME_ATTRIBUTE_TYPE),
    @Type(value = RotationFrequencyAttributeModel.class,
        name = CoreConstant.ROTATION_FREQUENCY_ATTRIBUTE_TYPE),
 })
@JsonIgnoreProperties("rendererType")
public abstract class AbstractAttributeModel implements IAttributeModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected IAttribute         attribute;
  protected String             rendererType;
  protected Boolean            isTranslatable   = false;
  protected List<IPropertyTag> attributeTags;
  protected String             code;
  
  public AbstractAttributeModel(IAttribute attribute, String renderer)
  {
    this.attribute = attribute;
    this.rendererType = renderer;
  }
  
  @Override
  public String getCode()
  {
    return attribute.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    attribute.setCode(code);
  }
  
  @JsonIgnore
  @Override
  public IEntity getEntity()
  {
    return attribute;
  }
  
  @Override
  public String getId()
  {
    return attribute.getId();
  }
  
  @Override
  public void setId(String id)
  {
    attribute.setId(id);
  }
  
  @Override
  public String getValueAsHtml()
  {
    return attribute.getValueAsHtml();
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    attribute.setValueAsHtml(valueAsHtml);
  }
  
  @Override
  public String getDefaultValue()
  {
    return attribute.getDefaultValue();
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
  {
    attribute.setDefaultValue(defaultValue);
  }
  
  @Override
  public String getDescription()
  {
    return attribute.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    attribute.setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return attribute.getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    attribute.setTooltip(tooltip);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return attribute.getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    attribute.setIsMandatory(isMandatory);
  }
  
  @Override
  public String getPlaceholder()
  {
    return attribute.getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    attribute.setPlaceholder(placeholder);
  }
  
  @Override
  public String getLabel()
  {
    return attribute.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    attribute.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return attribute.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    attribute.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return attribute.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    attribute.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return this.attribute.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.attribute.setType(type);
  }
  
  @Override
  public Long getVersionId()
  {
    return attribute.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    attribute.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return attribute.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    attribute.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return attribute.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    attribute.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return attribute.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    attribute.setIsStandard(isStandard);
  }
  
  @Override
  public String getMappedTo()
  {
    return this.attribute.getMappedTo();
  }
  
  @Override
  public void setMappedTo(String mappedToId)
  {
    this.attribute.setMappedTo(mappedToId);
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return this.attribute.getIsDisabled();
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.attribute.setIsDisabled(isDisabled);
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return this.attribute.getIsSearchable();
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    this.attribute.setIsSearchable(isSearchable);
  }
  
  @Override
  public String getRendererType()
  {
    return this.rendererType;
  }
  
  @Override
  public String toString()
  {
    return "\nID : " + getId() + " --- Name : " + getLabel();
  }
  
  @Override
  public List<IPropertyTag> getAttributeTags()
  {
    return attributeTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyTag.class)
  public void setAttributeTags(List<IPropertyTag> attributeTags)
  {
    this.attributeTags = attributeTags;
  }
  
  @Override
  public Boolean getIsGridEditable()
  {
    return attribute.getIsGridEditable();
  }
  
  @Override
  public void setIsGridEditable(Boolean isGridEditable)
  {
    attribute.setIsGridEditable(isGridEditable);
  }
  
  @Override
  public Boolean getIsFilterable()
  {
    return attribute.getIsFilterable();
  }
  
  @Override
  public void setIsFilterable(Boolean isFilterable)
  {
    this.attribute.setIsFilterable(isFilterable);
  }
  
  @Override
  public Boolean getIsSortable()
  {
    return this.attribute.getIsSortable();
  }
  
  @Override
  public void setIsSortable(Boolean isSortable)
  {
    this.attribute.setIsSortable(isSortable);
  }
  
  @Override
  public List<String> getAvailability()
  {
    return this.attribute.getAvailability();
  }
  
  @Override
  public void setAvailability(List<String> availability)
  {
    this.attribute.setAvailability(availability);
  }
  
  @Override
  public Boolean getIsTranslatable()
  {
    // TODO Auto-generated method stub
    return attribute.getIsTranslatable();
  }
  
  @Override
  public void setIsTranslatable(Boolean isTranslatable)
  {
    this.attribute.setIsTranslatable(isTranslatable);
  }
  
  @Override
  public Boolean getIsVersionable()
  {
    return attribute.getIsVersionable();
  }
  
  @Override
  public void setIsVersionable(Boolean isVersionable)
  {
    attribute.setIsVersionable(isVersionable);
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.attribute.getPropertyIID();
  }
  
  @Override
  public void setPropertyIID(long propertyIID)
  {
    this.attribute.setPropertyIID(propertyIID);
  }
}
