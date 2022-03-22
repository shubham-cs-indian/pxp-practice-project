import CS from '../../libraries/cs';
import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import AttributeDictionary from './attribute-type-dictionary-new';

/*
module.exports = [
  {
    "id": 2,
    "name": oTranslations().ATTRIBUTE_TYPES_MULTI_LINE_TEXT,
    "regex": ".{0,n}",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_MULTI_LINE_TEXT_LIMIT
  },
  {
    "id": 1,
    "name": oTranslations().ATTRIBUTE_TYPES_SINGLE_LINE_TEXT,
    "regex": ".{0,n}",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_SINGLE_LINE_TEXT_LIMIT
  },
  {
    "id": 3,
    "name": oTranslations().DATE,
    //"regex": "^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$",
    "regex": "",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_DATE_VALIDATE
  },
  */
/*{
    "id": 4,
    "name": oTranslations().ATTRIBUTE_TYPES_TIME,
    "regex": "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_TIME_VALIDATE
  },*//*

*/
/*{
    "id": 5,
    "name": "Multi-valued",
    "regex": "",
    "errorMessage": ""
  },*//*

{
"id": 6,
"name": oTranslations().ATTRIBUTE_TYPES_NUMBER,
"regex": "^[0-9]+(?:[.,][0-9]+)*$",*/
/*"^[0-9]*$",*//*

"errorMessage": oTranslations().ATTRIBUTE_TYPES_NUMBER_VALIDATE
},
*/
/*{
    "id": 7,
    "name": oTranslations().USER_NAME,
    "regex": "^[A-Za-z0-9_]{3,20}$",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_USERNAME_VALIDATE
  },
  {
    "id": 8,
    "name": oTranslations().ATTRIBUTE_TYPES_PASSWORD,
    "regex": "^[A-Za-z0-9!@#$%^&*()_]{6,20}$",
    "errorMessage": oTranslations().ATTRIBUTE_TYPES_PASSWORD_VALIDATE
  },*//*

{
"id": 9,
"name": oTranslations().ATTRIBUTE_TYPES_EMAIL,
"regex": "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$",
"errorMessage": oTranslations().EMAIL_VALIDATE
}
];*/

const attributeTypes = function () {
  return CS.sortBy([
    {
      "id": 2,
      "name": oTranslations().ATTRIBUTE_TYPES_TEXT,
      "value": AttributeDictionary.TEXT,
      "isStandard" : false,
      "isUnitAttribute": false
    },{
      "id": 1,
      "name": oTranslations().ATTRIBUTE_TYPES_HTML,
      "value": AttributeDictionary.HTML,
      "isStandard" : false,
      "isUnitAttribute": false
    },

    {
      "id": 3,
      "name": oTranslations().DATE,
      "value": AttributeDictionary.DATE,
      "isStandard" : false,
      "isUnitAttribute": false
    },
    {
      "id": 4,
      "name": oTranslations().ATTRIBUTE_TYPES_NUMBER,
      "value": AttributeDictionary.NUMBER,
      "isStandard" : false,
      "isUnitAttribute": false
    },
    /*{
      "id": 14,
      "name": oTranslations().ATTRIBUTE_TYPES_COVERFLOW,
      "value": AttributeDictionary.COVERFLOW,
      "isStandard" : false,
      "isUnitAttribute": false
    }*/


    //Below are the standard attributes
    {
      "id": '59_1',
      "name": oTranslations().TAXONOMY,
      "value": AttributeDictionary.TAXONOMY,
      "isStandard" : true,
      "isUnitAttribute": false
    },
    {
      "id": 6,
      "name": oTranslations().TYPE,
      "value": AttributeDictionary.TYPE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 7,
      "name": oTranslations().ATTRIBUTE_TYPES_STANDARD_COVERFLOW,
      "value": AttributeDictionary.IMAGE_COVERFLOW,
      "isStandard" : true,
      "isUnitAttribute": false
    },

    {
      "id": 8,
      "name": oTranslations().OWNER,
      "value": AttributeDictionary.OWNER,
      "isStandard" : true,
      "isUnitAttribute": false
    },
    {
      "id": 9,
      "name": oTranslations().NAME,
      "value": AttributeDictionary.NAME,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 10,
      "name": oTranslations().ATTRIBUTE_TYPES_ASSIGNEE,
      "value": AttributeDictionary.ASSIGNEE,
      "isStandard" : true,
      "isUnitAttribute": false
    },
    {
      "id": 13,
      "name": oTranslations().ATTRIBUTE_TYPES_CREATED_ON,
      "value": AttributeDictionary.CREATED_ON,
      "isStandard" : true,
      "isUnitAttribute": false
    },

    {
      "id": 20,
      "name": oTranslations().ATTRIBUTE_TYPES_EXIF,
      "value": AttributeDictionary.STANDARD_EXIF,
      "isStandard" : true,
      "isUnitAttribute": false
    },
    {
      "id": 21,
      "name": oTranslations().ATTRIBUTE_TYPES_IPTC,
      "value": AttributeDictionary.STANDARD_IPTC,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 22,
      "name": oTranslations().ATTRIBUTE_TYPES_XMP,
      "value": AttributeDictionary.STANDARD_XMP,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 23,
      "name": oTranslations().OTHER,
      "value": AttributeDictionary.STANDARD_OTHER,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 29,
      "name": oTranslations().ATTRIBUTE_TYPES_LENGTH,
      "value": AttributeDictionary.LENGTH,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 30,
      "name": oTranslations().CURRENT,
      "value": AttributeDictionary.CURRENT,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 31,
      "name": oTranslations().ATTRIBUTE_TYPES_POTENTIAL,
      "value": AttributeDictionary.POTENTIAL,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 32,
      "name": oTranslations().ATTRIBUTE_TYPES_FREQUENCY,
      "value": AttributeDictionary.FREQUENCY,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 33,
      "name": oTranslations().ATTRIBUTE_TYPES_TIME,
      "value": AttributeDictionary.TIME,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 34,
      "name": oTranslations().ATTRIBUTE_TYPES_TEMPERATURE,
      "value": AttributeDictionary.TEMPERATURE,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 35,
      "name": oTranslations().ATTRIBUTE_TYPES_VOLUME,
      "value": AttributeDictionary.VOLUME,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 36,
      "name": oTranslations().ATTRIBUTE_TYPES_AREA,
      "value": AttributeDictionary.AREA,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 37,
      "name": oTranslations().ATTRIBUTE_TYPES_MASS,
      "value": AttributeDictionary.MASS,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 38,
      "name": oTranslations().ATTRIBUTE_TYPES_SIZE,
      "value": AttributeDictionary.SIZE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 39,
      "name": oTranslations().ATTRIBUTE_TYPES_FILE_TYPE,
      "value": AttributeDictionary.FILE_TYPE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 40,
      "name": oTranslations().STATUS,
      "value": AttributeDictionary.STATUS,
      "isStandard" : true,
      "isUnitAttribute": false
    },
    {
      "id": 46,
      "name": oTranslations().ATTRIBUTE_TYPES_LAST_MODIFIED,
      "value": AttributeDictionary.LAST_MODIFIED,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 47,
      "name": oTranslations().ATTRIBUTE_TYPES_LAST_MODIFIED_BY,
      "value": AttributeDictionary.LAST_MODIFIED_BY,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 51,
      "name": oTranslations().ATTRIBUTE_TYPES_CREATED_BY,
      "value": AttributeDictionary.CREATED_BY,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 54,
      "name": oTranslations().ATTRIBUTE_TYPES_DIGITAL_STORAGE,
      "value": AttributeDictionary.DIGITAL_STORAGE,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 55,
      "name": oTranslations().ATTRIBUTE_TYPES_ENERGY,
      "value": AttributeDictionary.ENERGY,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 56,
      "name":  oTranslations().ATTRIBUTE_TYPES_PLANE_ANGLE,
      "value": AttributeDictionary.PLANE_ANGLE,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 57,
      "name": oTranslations().ATTRIBUTE_TYPES_PRESSURE,
      "value": AttributeDictionary.PRESSURE,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 58,
      "name": oTranslations().ATTRIBUTE_TYPES_SPEED,
      "value": AttributeDictionary.SPEED,
      "isStandard" : false,
      "isUnitAttribute": true
    },{
      "id": 59,
      "name": oTranslations().ATTRIBUTE_TYPES_TAT_UPLOAD,
      "value": AttributeDictionary.TAT,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 60,
      "name": oTranslations().ATTRIBUTE_TYPES_MULTI_CLASSIFICATION,
      "value": AttributeDictionary.MULTI_CLASSIFICATION,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 61,
      "name": oTranslations().ATTRIBUTE_TYPES_APPLICATION_RECORD_VERSION,
      "value": AttributeDictionary.APPLICATION_RECORD_VERSION,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 62,
      "name": oTranslations().ATTRIBUTE_TYPES_CAPTION_ABSTRACT,
      "value": AttributeDictionary.CAPTION_ABSTRACT,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 63,
      "name": oTranslations().ATTRIBUTE_TYPES_COMPOSITE,
      "value": AttributeDictionary.COMPOSITE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 64,
      "name": oTranslations().ATTRIBUTE_TYPES_COMPRESSION,
      "value": AttributeDictionary.COMPRESSION,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 65,
      "name": oTranslations().ATTRIBUTE_TYPES_CREATOR_TOOL,
      "value": AttributeDictionary.CREATOR_TOOL,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 66,
      "name": oTranslations().ATTRIBUTE_TYPES_DOCUMENT_ID,
      "value": AttributeDictionary.DOCUMENT_ID,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 67,
      "name": oTranslations().ATTRIBUTE_TYPES_EXIF_TOOL,
      "value": AttributeDictionary.EXIF_TOOL,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 68,
      "name": oTranslations().FILE,
      "value": AttributeDictionary.FILE,
      "isStandard" : true,
      "isUnitAttribute": false }
    ,{
      "id": 69,
      "name": oTranslations().ATTRIBUTE_TYPES_KEYWORDS,
      "value": AttributeDictionary.KEYWORDS,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 70,
      "name": oTranslations().ATTRIBUTE_TYPES_METADATA_DATE,
      "value": AttributeDictionary.META_DATA_DATE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 71,
      "name": oTranslations().ATTRIBUTE_TYPES_OBJECT_NAME,
      "value": AttributeDictionary.OBJECT_NAME,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 72,
      "name": oTranslations().ATTRIBUTE_TYPES_THUMBNAIL_LENGTH,
      "value": AttributeDictionary.THUMBNAIL_LENGTH,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 73,
      "name": oTranslations().ATTRIBUTE_TYPES_THUMBNAIL_OFFSET,
      "value": AttributeDictionary.THUMBNAIL_OFFSET,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 74,
      "name": oTranslations().ATTRIBUTE_TYPES_XMP_TOOLKIT,
      "value": AttributeDictionary.XMP_TOOLKIT,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 75,
      "name": oTranslations().ATTRIBUTE_TYPES_X_RESOLUTION,
      "value": AttributeDictionary.X_RESOLUTION,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 76,
      "name": oTranslations().ATTRIBUTE_TYPES_Y_RESOLUTION,
      "value": AttributeDictionary.Y_RESOLUTION,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 77,
      "name": oTranslations().ATTRIBUTE_TYPES_SOURCE_FILE,
      "value": AttributeDictionary.SOURCE_FILE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 78,
      "name": oTranslations().ATTRIBUTE_TYPES_MODIFY_DATE,
      "value": AttributeDictionary.MODIFY_DATE,
      "isStandard" : true,
      "isUnitAttribute": false
    },{
      "id": 79,
      "name": oTranslations().ATTRIBUTE_TYPES_CREATE_DATE,
      "value": AttributeDictionary.CREATE_DATE,
      "isStandard" : true,
      "isUnitAttribute": false
    }, {
      "id": 80,
      "name": oTranslations().ATTRIBUTE_TYPES_CREATOR,
      "value": AttributeDictionary.CREATOR,
      "isStandard": true,
      "isUnitAttribute": false
    }, {
      "id": 81,
      "name": oTranslations().ATTRIBUTE_TYPES_ASSET_METADATA,
      "value": AttributeDictionary.ASSET_META_DATA,
      "isStandard": true,
      "isUnitAttribute": false

    },
    {
      "id": 82,
      "name":  oTranslations().ATTRIBUTE_TYPES_POWER,
      "value": AttributeDictionary.POWER,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 83,
      "name":  oTranslations().ATTRIBUTE_TYPES_LUMINOSITY,
      "value": AttributeDictionary.LUMINOSITY,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 84,
      "name":  oTranslations().ATTRIBUTE_TYPES_RADIATION,
      "value": AttributeDictionary.RADIATION,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 85,
      "name":  oTranslations().ATTRIBUTE_TYPES_ILLUMINANCE,
      "value": AttributeDictionary.ILLUMINANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 86,
      "name":  oTranslations().ATTRIBUTE_TYPES_FORCE,
      "value": AttributeDictionary.FORCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 87,
      "name":  oTranslations().ATTRIBUTE_TYPES_ACCELERATION,
      "value": AttributeDictionary.ACCELERATION,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 88,
      "name":  oTranslations().ATTRIBUTE_TYPES_CAPACITANCE,
      "value": AttributeDictionary.CAPACITANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 89,
      "name":  oTranslations().ATTRIBUTE_TYPES_VISCOCITY,
      "value": AttributeDictionary.VISCOCITY,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 90,
      "name":  oTranslations().ATTRIBUTE_TYPES_INDUCTANCE,
      "value": AttributeDictionary.INDUCTANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 91,
      "name":  oTranslations().ATTRIBUTE_TYPES_RESISTANCE,
      "value": AttributeDictionary.RESISTANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 92,
      "name":  oTranslations().ATTRIBUTE_TYPES_MAGNETISM,
      "value": AttributeDictionary.MAGNETISM,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 93,
      "name":  oTranslations().ATTRIBUTE_TYPES_CHARGE,
      "value": AttributeDictionary.CHARGE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 94,
      "name":  oTranslations().ATTRIBUTE_TYPES_CONDUCTANCE,
      "value": AttributeDictionary.CONDUCTANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    },
    {
      "id": 95,
      "name":  oTranslations().ATTRIBUTE_TYPES_SUBSTANCE,
      "value": AttributeDictionary.SUBSTANCE,
      "isStandard" : false,
      "isUnitAttribute": true
    }, {
      "id": 97,
      "name": oTranslations().ATTRIBUTE_TYPES_CALCULATED_ATTRIBUTE,
      "value": AttributeDictionary.CALCULATED,
      "isStandard": false,
      "isUnitAttribute": false
    }, {
      "id": 98,
      "name": oTranslations().ATTRIBUTE_TYPES_WEIGHT_PER_AREA_ATTRIBUTE,
      "value": AttributeDictionary.WEIGHT_PER_AREA,
      "isStandard": false,
      "isUnitAttribute": true
    }, {
      "id": 99,
      "name": oTranslations().UNIT_PROPORTION_x_y,
      "value": AttributeDictionary.PROPORTION,
      "isStandard": false,
      "isUnitAttribute": true
    }, {
      "id": 100,
      "name": oTranslations().ATTRIBUTE_TYPES_THERMAL_INSULATION_ATTRIBUTE,
      "value": AttributeDictionary.THERMAL_INSULATION,
      "isStandard": false,
      "isUnitAttribute": true
    }, {
      "id": 101,
      "name": oTranslations().ATTRIBUTE_TYPES_CONCATENATED_ATTRIBUTE,
      "value": AttributeDictionary.CONCATENATED,
      "isStandard": false,
      "isUnitAttribute": false
    },
    {
      "id": 102,
      "name": oTranslations().ATTRIBUTE_TYPES_MEASUREMENT_ATTRIBUTE,
      "value": AttributeDictionary.CUSTOM_MEASUREMENT,
      "isStandard": false,
      "isUnitAttribute": false
    },
    {
      "id": 103,
      "name": oTranslations().ATTRIBUTE_TYPES_CUSTOM_MEASUREMENT_ATTRIBUTE,
      "value": AttributeDictionary.CUSTOM_UNIT,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 104,
      "name": oTranslations().ATTRIBUTE_TYPES_HEATING_RATE,
      "value": AttributeDictionary.HEATING_RATE,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 105,
      "name": oTranslations().ATTRIBUTE_TYPES_DENSITY,
      "value": AttributeDictionary.DENSITY,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 106,
      "name": oTranslations().ATTRIBUTE_TYPES_WEIGHT_PER_TIME,
      "value": AttributeDictionary.WEIGHT_PER_TIME,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 107,
      "name": oTranslations().ATTRIBUTE_TYPES_VOLUME_FLOW_RATE,
      "value": AttributeDictionary.VOLUME_FLOW_RATE,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 108,
      "name": oTranslations().ATTRIBUTE_TYPES_AREA_PER_VOLUME,
      "value": AttributeDictionary.AREA_PER_VOLUME,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 109,
      "name": oTranslations().ATTRIBUTE_TYPES_ROTATION_FREQUENCY,
      "value": AttributeDictionary.ROTATION_FREQUENCY,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 110,
      "name": oTranslations().ATTRIBUTE_TYPES_PRICE,
      "value": AttributeDictionary.PRICE,
      "isStandard": false,
      "isUnitAttribute": true
    },
    {
      "id": 111,
      "name": oTranslations().ATTRIBUTE_TYPES_FILE_NAME,
      "value": AttributeDictionary.FILE_NAME,
      "isStandard": true,
      "isUnitAttribute": false
    }
  ], 'name');
};
export default attributeTypes;
