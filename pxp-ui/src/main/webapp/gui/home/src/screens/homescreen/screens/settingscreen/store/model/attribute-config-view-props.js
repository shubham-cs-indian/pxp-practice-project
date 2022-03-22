/**
 * Created by DEV on 29-07-2015.
 */
var AttributeConfigViewProps = (function () {

  let Props = function () {
    return {
      oSelectedAttribute: {},
      oMultiValuedListObject: {},
      oMSSProp:  {
        attributeDetailAllowedFrame: {
          list: [],
          filteredList: [],
          isActive: false
        },
        attributeDefaultStyles: {
          list: [],
          filteredList: [],
          isActive: false
        },
        attributeRTEIcon: {
          list: [],
          filteredList: [],
          isActive: false
        }
      },
      aAttributeGridData: []
    }
  };

  let oProperties = new Props();

  return {

    setMultiValuedListObject: function (_oMultiValuedListObject) {
      oProperties.oMultiValuedListObject = _oMultiValuedListObject;
    },

    getMultiValuedListObject: function () {
      return oProperties.oMultiValuedListObject;
    },

    getMSSProps: function () {
      return oProperties.oMSSProp;
    },

    getSelectedAttribute: function () {
      return oProperties.oSelectedAttribute;
    },

    setSelectedAttribute: function (_oAttribute) {
      oProperties.oSelectedAttribute = _oAttribute;
    },

    getAttributeGridData: function () {
      return oProperties.aAttributeGridData;
    },

    setAttributeGridData: function (_aAttributeGridData) {
      oProperties.aAttributeGridData = _aAttributeGridData;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
        oSelectedAttribute: oProperties.oSelectedAttribute,
        oMultiValuedListObject: oProperties.oMultiValuedListObject,
        oMSSProp:  oProperties.oMSSProp,
        aAttributeGridData: oProperties.aAttributeGridData
      };
    }
  }
})();

export default AttributeConfigViewProps;