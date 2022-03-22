/**
 * Created by CS80 on 20/3/2017.
 */

var TemplateConfigViewProps = (function () {

  let Props = function () {
    return {
      oActiveTemplate: {},
      oReferencedData: {},
      bTemplateScreenLockStatus: false,
      sCurrentTabType: "",/*TemplateTabsDictionary.HOME_TAB,*/
      oTemplateSectionDragStatus: {},
      aSectionMap: [],
      aTemplatesGridData:[],
    };
  };

  let oProperties = new Props();

  return {
    getTemplateSectionMap: function () {
      return oProperties.aSectionMap;
    },

    setTemplateSectionMap: function (_aSectionMap) {
      oProperties.aSectionMap = _aSectionMap;
    },

    getActiveTemplate: function () {
      return oProperties.oActiveTemplate;
    },

    getTemplateReferencedData: function () {
      return oProperties.oReferencedData;
    },

    setReferencedTemplateData: function (_oReferencedData) {
      oProperties.oReferencedData = _oReferencedData;
    },

    getCurrentTemplate: function () {
      return oProperties.oActiveTemplate.clonedObject ? oProperties.oActiveTemplate.clonedObject : oProperties.oActiveTemplate;
    },

    setActiveTemplate: function (_sActiveRuleId) {
      oProperties.oActiveTemplate = _sActiveRuleId;
    },

    getCurrentTabType: function () {
      return oProperties.sCurrentTabType;
    },

    setCurrentTabType: function (_sCurrentTabType) {
      oProperties.sCurrentTabType = _sCurrentTabType;
    },

    getTemplateSectionDragStatus: function () {
      return oProperties.oTemplateSectionDragStatus;
    },

    setTemplateSectionDragStatus: function (_oTemplateSectionDragStatus) {
      oProperties.oTemplateSectionDragStatus = _oTemplateSectionDragStatus;
    },

    setTemplateGridData: function (_aTaskGridData) {
      oProperties.aTemplatesGridData = _aTaskGridData;
    },

    getTemplateGridData: function () {
      return oProperties.aTemplatesGridData;
    },

    reset: function (bIsSave) {
      if (!bIsSave) {
        oProperties.oActiveTemplate = {}
      }
      delete oProperties.oActiveTemplate.clonedObject;
    }
  };
})();

export default TemplateConfigViewProps;