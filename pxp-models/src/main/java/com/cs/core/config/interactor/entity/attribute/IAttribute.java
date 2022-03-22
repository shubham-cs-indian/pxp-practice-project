package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.tag.IPropertyTag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IAttribute extends IConfigMasterPropertyEntity {
  
  public static final String TYPE             = "type";
  public static final String DEFAULT_VALUE    = "defaultValue";
  public static final String IS_DISABLED      = "isDisabled";
  public static final String MAPPED_TO        = "mappedTo";
  public static final String IS_SEARCHABLE    = "isSearchable";
  public static final String RENDERER_TYPE    = "rendererType";
  public static final String ATTRIBUTE_TAGS   = "attributeTags";
  public static final String IS_GRID_EDITABLE = "isGridEditable";
  public static final String CODE             = "code";
  public static final String IS_FILTERABLE    = "isFilterable";
  public static final String IS_SORTABLE      = "isSortable";
  public static final String AVAILABILITY     = "availability";
  public static final String IS_TRANSLATABLE  = "isTranslatable";
  public static final String VALUE_AS_HTML    = "valueAsHtml";
  public static final String PROPERTY_IID     = "propertyIID";
  public static final String IS_VERSIONABLE   = "isVersionable";
  public static final String SUB_TYPE         = "subType";

  public String getType();
  
  public void setType(String type);
  
  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);
  
  public String getMappedTo();
  
  public void setMappedTo(String mappedToId);
  
  public Boolean getIsDisabled();
  
  public void setIsDisabled(Boolean isDisabled);
  
  public Boolean getIsSearchable();
  
  public void setIsSearchable(Boolean isSearchable);
  
  public String getRendererType();
  
  public List<IPropertyTag> getAttributeTags();
  
  public void setAttributeTags(List<IPropertyTag> attributeTags);
  
  public Boolean getIsGridEditable();
  
  public void setIsGridEditable(Boolean isGridEditable);
  
  public Boolean getIsFilterable();
  
  public void setIsFilterable(Boolean isFilterable);
  
  public Boolean getIsSortable();
  
  public void setIsSortable(Boolean isSortable);
  
  public Boolean getIsTranslatable();
  
  public void setIsTranslatable(Boolean isTranslatable);
  
  public List<String> getAvailability();
  
  public void setAvailability(List<String> availability);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long propertyIID);
  
  public Boolean getIsVersionable();
  
  public void setIsVersionable(Boolean isVersionable);
  
  public enum Renderer
  {
    HTML, TEXT, NUMBER, DATE, COVERFLOW, TYPE, IMAGE, LENGTH, CURRENT, POTENTIAL, FREQUENCY, TIME,
    TEMPERATURE, VOLUME, AREA, MASS, DIGITAL_STORAGE, ENERGY, PLANE_ANGLE, PRESSURE, SPEED,
    TAXONOMY, CURRENCY, POWER, WEIGHT_PER_AREA, PROPORTION, THERMAL_INSULATION, LUMINOSITY,
    RADIATION, ILLUMINANCE, FORCE, ACCELERATION, CAPACITANCE, VISCOCITY, INDUCTANCE, RESISTANCE,
    MAGNETISM, CHARGE, CONDUCTANCE, SUBSTANCE, CALCULATED, CONCATENATED, CUSTOM, HEATING_RATE,
    DENSITY, WEIGHT_PER_TIME, VOLUME_FLOW_RATE, AREA_PER_VOLUME, ROTATION_FREQUENCY, PRICE
  }
}
