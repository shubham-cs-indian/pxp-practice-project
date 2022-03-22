package com.cs.config.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;

/**
 * Declared mappings across various constant types and codes
 *
 * @author vallee
 */
public interface IConfigMap {

  /**
   * @param configType a type of property issued from the configuration DB
   * @return the corresponding type of RDBMS property
   */
  public static IPropertyDTO.PropertyType getPropertyType(String configType) {
    return Maps.PROPERTY_TYPE_MAP.get(configType);
  }

  /**
   * @param configType a type of classifier issued from the configuration DB
   * @return the corresponding type of RDBMS classifier
   */
  public static IClassifierDTO.ClassifierType getClassType(String configType) {
    return Maps.CLASSIFIER_TYPE_MAP.get(configType);
  }

  /**
   * @param configType a type of classifier issued from the configuration DB
   * @return the corresponding type of RDBMS entity
   */
  public static IBaseEntityDTO.BaseType getBaseType(String configType) {
    return Maps.BASE_TYPE_MAP.get(configType);
  }

  /**
   * @param baseType a type of entity issued from RDBMS
   * @return the corresponding type of configuration class path
   */
  public static String getBaseTypeClassPath(IBaseEntityDTO.BaseType baseType) {
    return Maps.CONFIG_CLASS_MAP.get(baseType)
            .toString();
  }

  /**
   * @param configType a type of context issued from the configuration DB
   * @return the corresponding type of RDBMS entity
   */
  public static IContextDTO.ContextType getContextType(String configType) {
    return Maps.CONTEXT_TYPE_MAP.get(configType);
  }

  /**
   * @param configType a type of context issued from the configuration DB
   * @return the corresponding type of RDBMS entity
   */
  public static String getContextTypeClass(IContextDTO.ContextType configType) {
    return Maps.CONTEXT_TYPE_CLASS_MAP.get(configType);
  }
  
  /**
   * @param taskType a type of task from the configuration DB
   * @return the corresponding type of RDBMS entity
   */
  public static ITaskDTO.TaskType getTaskType(String taskType)
  {
    return Maps.TASK_TYPE_MAP.get(taskType);
  }

  /**
   * 
   * @return all baseTypes
   */
  public static List<IBaseEntityDTO.BaseType> getAllBaseTypes() {
    return new ArrayList<IBaseEntityIDDTO.BaseType>(Maps.BASE_TYPE_MAP.values());
  }

  class Maps {

    private static final Map<String, IPropertyDTO.PropertyType> PROPERTY_TYPE_MAP = new HashMap<>();
    private static final Map<String, IClassifierDTO.ClassifierType> CLASSIFIER_TYPE_MAP = new HashMap<>();
    public static final Map<String, IBaseEntityDTO.BaseType> BASE_TYPE_MAP = new HashMap<>();
    private static final Map<IBaseEntityDTO.BaseType, IConfigClass.EntityClass> CONFIG_CLASS_MAP = new HashMap<>();
    private static final Map<String, IContextDTO.ContextType> CONTEXT_TYPE_MAP = new HashMap<>();
    private static final Map<IContextDTO.ContextType, String> CONTEXT_TYPE_CLASS_MAP = new HashMap<>();
    private static final Map<String, ITaskDTO.TaskType> TASK_TYPE_MAP = new HashMap<>();

    static {
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.TAG_TYPE.toString(),
              IPropertyDTO.PropertyType.TAG);
      IStandardConfig.TagType.AllTagTypes.forEach((key) -> {
        PROPERTY_TYPE_MAP.put(key, IPropertyDTO.PropertyType.TAG);
      });
      PROPERTY_TYPE_MAP.put(IStandardConfig.TagType.BooleanTagCode,
              IPropertyDTO.PropertyType.BOOLEAN);

      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CALCULATED_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.CALCULATED);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CONCATENATED_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.CONCATENATED);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.DATE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.DATE);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.HTML_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.HTML);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.LENGTH_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.AREA_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.AREA_PER_VOLUME_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CAPACITANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.ACCELERATION_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CHARGE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CONDUCTANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CURRENCY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CURRENT_ATTRIBUTE_TYPE.toString(),
          IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CUSTOM_UNIT_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.DIGITAL_STORAGE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.ENERGY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.FREQUENCY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.HEATING_RATE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.ILLUMINANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.INDUCTANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.FORCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.LUMINOSITY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.MAGNETISM_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.MASS_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.POTENTIAL_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.POWER_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.PROPORTION_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.RADIATION_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.ROTATION_FREQUENCY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.SPEED_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.SUBSTANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.TEMPERATURE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.THERMAL_INSULATION_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.VISCOCITY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.VOLUME_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.VOLUME_FLOW_RATE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.WEIGHT_PER_TIME_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.WEIGHT_PER_AREA_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.RESISTANCE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.PRESSURE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.PLANE_ANGLE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.DENSITY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.TIME_ATTRIBUTE_TYPE.toString(),
          IPropertyDTO.PropertyType.MEASUREMENT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.NUMBER_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.NUMBER);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.PRICE_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.PRICE);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.TEXT_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.TEXT);

      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.NAME_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.TEXT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CREATED_ON_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.DATE);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.CREATED_BY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.TEXT);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.LAST_MODIFIED_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.DATE);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.LAST_MODIFIED_BY_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.TEXT);

      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.ASSET_METADATA_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.ASSET_ATTRIBUTE);
      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.IMAGE_COVER_FLOW_ATTRIBUTE_TYPE.toString(),
              IPropertyDTO.PropertyType.ASSET_ATTRIBUTE);

      PROPERTY_TYPE_MAP.put(IConfigClass.PropertyClass.RELATIONSHIP_TYPE.toString(),
              IPropertyDTO.PropertyType.RELATIONSHIP);
      
    }

    static {
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.TAXONOMY_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.TAXONOMY);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.PROJECT_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.PROJECT_SET_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.ASSET_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.MARKET_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.TEXT_ASSET_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
      CLASSIFIER_TYPE_MAP.put(IConfigClass.ClassifierClass.SUPPLIER_KLASS_TYPE.toString(),
              IClassifierDTO.ClassifierType.CLASS);
    }

    static {
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.ARTICLE_INSTANCE_BASE_TYPE.toString(),
          IBaseEntityDTO.BaseType.ARTICLE);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.PROJECT_KLASS_TYPE.toString(),
          IBaseEntityDTO.BaseType.ARTICLE);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.ASSET_INSTANCE_BASE_TYPE.toString(),
          IBaseEntityDTO.BaseType.ASSET);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.ASSET_KLASS_TYPE.toString(),
          IBaseEntityDTO.BaseType.ASSET);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.MARKET_KLASS_TYPE.toString(),
          IBaseEntityDTO.BaseType.TARGET);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.TEXT_ASSET_KLASS_TYPE.toString(),
          IBaseEntityDTO.BaseType.TEXT_ASSET);
      BASE_TYPE_MAP.put(IConfigClass.EntityClass.SUPPLIER_KLASS_TYPE.toString(),
          IBaseEntityDTO.BaseType.SUPPLIER);
    }

    static {
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.ARTICLE,
              IConfigClass.EntityClass.ARTICLE_INSTANCE_BASE_TYPE);
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.ASSET,
              IConfigClass.EntityClass.ASSET_INSTANCE_BASE_TYPE);
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.TEXT_ASSET,
              IConfigClass.EntityClass.TEXTASSET_INSTANCE_BASE_TYPE);
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.TARGET,
              IConfigClass.EntityClass.TARGET_KLASS_TYPE);
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.SUPPLIER,
              IConfigClass.EntityClass.SUPPLIER_INSTANCE_BASE_TYPE);

      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.ATTACHMENT,
          IConfigClass.EntityClass.ASSET_INSTANCE_BASE_TYPE);
      /*TODO: Hack for relationship extension(RE). 
      Need to provide appropriate EntityClass for RE or atleast change 
      the basetype of extension that is stored in base entity.
       */
      CONFIG_CLASS_MAP.put(IBaseEntityDTO.BaseType.RELATIONSHIP_EXTENSION,
              IConfigClass.EntityClass.ARTICLE_INSTANCE_BASE_TYPE);
    }

    static {
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.productVariant.toString(),
              IContextDTO.ContextType.LINKED_VARIANT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.attributeVariantContext.toString(),
              IContextDTO.ContextType.ATTRIBUTE_CONTEXT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.relationshipVariant.toString(),
              IContextDTO.ContextType.RELATIONSHIP_VARIANT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.gtinVariant.toString(),
              IContextDTO.ContextType.GTIN_VARIANT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.contextualVariant.toString(),
              IContextDTO.ContextType.EMBEDDED_VARIANT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.imageVariant.toString(),
              IContextDTO.ContextType.IMAGE_VARIANT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.promotionContext.toString(),
              IContextDTO.ContextType.PROMOTION_CONTEXT);
      CONTEXT_TYPE_MAP.put(IStandardConfig.StandardContext.attributeVariant.toString(),
              IContextDTO.ContextType.UNDEFINED);
    }

    static {
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.LINKED_VARIANT,
              IStandardConfig.StandardContext.productVariant.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.ATTRIBUTE_CONTEXT,
              IStandardConfig.StandardContext.attributeVariantContext.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.RELATIONSHIP_VARIANT,
              IStandardConfig.StandardContext.relationshipVariant.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.GTIN_VARIANT,
              IStandardConfig.StandardContext.gtinVariant.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.EMBEDDED_VARIANT,
              IStandardConfig.StandardContext.contextualVariant.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.IMAGE_VARIANT,
              IStandardConfig.StandardContext.imageVariant.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.PROMOTION_CONTEXT,
              IStandardConfig.StandardContext.promotionContext.toString());
      CONTEXT_TYPE_CLASS_MAP.put(IContextDTO.ContextType.UNDEFINED,
              IStandardConfig.StandardContext.attributeVariant.toString());
    }
    
    static {
      TASK_TYPE_MAP.put(IStandardConfig.StandardTaskType.shared.toString(), ITaskDTO.TaskType.SHARED);
      TASK_TYPE_MAP.put(IStandardConfig.StandardTaskType.personal.toString(), ITaskDTO.TaskType.PERSONAL);
    }
  }

}
