import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ConfigPropertyTypeDictionary from './config-module-data-model-property-group-type-dictionary';
import AttributeBaseTypeDictionary from './../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import TagTypeDictionary from './../../../../../../commonmodule/tack/tag-type-constants';
import oConfigEntityTypeDictionary from "../../../../../../commonmodule/tack/config-entity-type-dictionary";

let aDataModelPropertyGroupingList = function () {
  return [
    {
      id: ConfigPropertyTypeDictionary.HTML,
      label: getTranslations().HTML,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.HTML],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.TEXT,
      label: getTranslations().TEXT,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.TEXT],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.NUMBER,
      label: getTranslations().NUMBER,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.NUMBER],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.DATE,
      label: getTranslations().DATE,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.DATE],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.CALCULATED,
      label: getTranslations().CALCULATED,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.CALCULATED],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.CONCATENATED,
      label: getTranslations().CONCATENATED,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.CONCATENATED],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.PRICE,
      label: getTranslations().PRICE,
      entityType: "attribute",
      types: [AttributeBaseTypeDictionary.PRICE],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.MEASUREMENT,
      label: getTranslations().MEASUREMENT,
      entityType: "attribute",
      types: [
        AttributeBaseTypeDictionary.LENGTH,
        AttributeBaseTypeDictionary.CURRENT,
        AttributeBaseTypeDictionary.POTENTIAL,
        AttributeBaseTypeDictionary.TIME,
        AttributeBaseTypeDictionary.TEMPERATURE,
        AttributeBaseTypeDictionary.VOLUME,
        AttributeBaseTypeDictionary.AREA,
        AttributeBaseTypeDictionary.MASS,
        AttributeBaseTypeDictionary.FREQUENCY,
        AttributeBaseTypeDictionary.CURRENCY,
        AttributeBaseTypeDictionary.DIGITAL_STORAGE,
        AttributeBaseTypeDictionary.ENERGY,
        AttributeBaseTypeDictionary.PLANE_ANGLE,
        AttributeBaseTypeDictionary.PRESSURE,
        AttributeBaseTypeDictionary.SPEED,
        AttributeBaseTypeDictionary.POWER,
        AttributeBaseTypeDictionary.LUMINOSITY,
        AttributeBaseTypeDictionary.RADIATION,
        AttributeBaseTypeDictionary.ILLUMINANCE,
        AttributeBaseTypeDictionary.FORCE,
        AttributeBaseTypeDictionary.ACCELERATION,
        AttributeBaseTypeDictionary.CAPACITANCE,
        AttributeBaseTypeDictionary.VISCOCITY,
        AttributeBaseTypeDictionary.INDUCTANCE,
        AttributeBaseTypeDictionary.RESISTANCE,
        AttributeBaseTypeDictionary.MAGNETISM,
        AttributeBaseTypeDictionary.CHARGE,
        AttributeBaseTypeDictionary.CONDUCTANCE,
        AttributeBaseTypeDictionary.SUBSTANCE,
        AttributeBaseTypeDictionary.WEIGHT_PER_AREA,
        AttributeBaseTypeDictionary.PROPORTION,
        AttributeBaseTypeDictionary.THERMAL_INSULATION,
        AttributeBaseTypeDictionary.HEATING_RATE,
        AttributeBaseTypeDictionary.DENSITY,
        AttributeBaseTypeDictionary.WEIGHT_PER_TIME,
        AttributeBaseTypeDictionary.VOLUME_FLOW_RATE,
        AttributeBaseTypeDictionary.AREA_PER_VOLUME,
        AttributeBaseTypeDictionary.ROTATION_FREQUENCY,
        AttributeBaseTypeDictionary.CUSTOM_UNIT
      ],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.STANDARD,
      label: getTranslations().STANDARD_ATTRIBUTES,
      entityType: "attribute",
      types: [],
      children: [/**No Children*/],
      className: "actionItemAttribute",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.MASTER,
      label: getTranslations().MASTER,
      entityType: "tag",
      types: [
          TagTypeDictionary.TAG_TYPE_MASTER
      ],
      children: [/**No Children*/],
      className: "actionItemTag",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    /*{
      id: ConfigPropertyTypeDictionary.LANGUAGE,
      label: getTranslations().LANGUAGE,
      entityType: "tag",
      types: [
        TagTypeDictionary.TAG_TYPE_LANGUAGE
      ],
      children: [/!**No Children*!/],
      className: "actionItemTag"
    },*/
    {
      id: ConfigPropertyTypeDictionary.LOV,
      label: getTranslations().LOV,
      entityType: "tag",
      types: [
        TagTypeDictionary.YES_NEUTRAL_TAG_TYPE,
        TagTypeDictionary.YES_NEUTRAL_NO_TAG_TYPE,
        TagTypeDictionary.RANGE_TAG_TYPE,
        TagTypeDictionary.RULER_TAG_TYPE
      ],
      children: [/**No Children*/],
      className: "actionItemTag",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.STATUS,
      label: getTranslations().STATUS,
      entityType: "tag",
      types: [
          TagTypeDictionary.LIFECYCLE_STATUS_TAG_TYPE,
          TagTypeDictionary.STATUS_TAG_TYPE,
          TagTypeDictionary.LISTING_STATUS_TAG_TYPE
      ],
      children: [/**No Children*/],
      className: "actionItemTag",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    },
    {
      id: ConfigPropertyTypeDictionary.BOOLEAN,
      label: getTranslations().BOOLEAN,
      entityType: "tag",
      types: [TagTypeDictionary.TAG_TYPE_BOOLEAN],
      children: [/**No Children*/],
      className: "actionItemTag",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES
    }
  ];
};

export default aDataModelPropertyGroupingList;
