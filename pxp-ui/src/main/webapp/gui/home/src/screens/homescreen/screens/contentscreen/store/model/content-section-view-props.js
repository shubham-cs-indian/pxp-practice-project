import CS from '../../../../../../libraries/cs';

var ContentSectionViewProps = (function () {

  var Props = function () {
    return {
      oSectionVisualProps: {},
      aHiddenClasses: [],
      aViolatingMandatoryElements: [],
      oContentSectionGridMap: {},
      aSectionsToUpdate: [],
      aOldSections: [],
      oContentSectionElementVariantVersionMap: {},
      sVisibleClassId: ""
    }
  };

  var oProperties = new Props();

  return {

    getSectionVisualProps: function () {
      return oProperties.oSectionVisualProps;
    },

    setSectionVisualProps: function (oSectionVisualProps) {
      oProperties.oSectionVisualProps = oSectionVisualProps;
    },

    getVisibleClassId: function () {
      return oProperties.sVisibleClassId;
    },

    setVisibleClassId: function (_sVisibleClassId) {
      return oProperties.sVisibleClassId = _sVisibleClassId;
    },

    getViolatingMandatoryElements: function () {
      return oProperties.aViolatingMandatoryElements;
    },

    setViolatingMandatoryElements: function (_aViolatingMandatoryElements) {
      return oProperties.aViolatingMandatoryElements = _aViolatingMandatoryElements;
    },

    getSectionElementVariantVersionMap: function () {
      return oProperties.oContentSectionElementVariantVersionMap;
    },

    getSectionsToUpdate: function () {
      return oProperties.aSectionsToUpdate;
    },

    setSectionsToUpdate: function (sSectionsToUpdate) {
      let _aSectionsToUpdate = sSectionsToUpdate;
      if(!CS.isArray(sSectionsToUpdate)){
        _aSectionsToUpdate = [sSectionsToUpdate];
      }

      oProperties.aSectionsToUpdate = CS.union(_aSectionsToUpdate, oProperties.aOldSections);
      oProperties.aOldSections = _aSectionsToUpdate;
    },

    emptySectionsToUpdate: function () {
      oProperties.aSectionsToUpdate = [];
      CS.remove(oProperties.aOldSections, function (sSectionsId) {return sSectionsId == "all"});
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {}
    }
  };

})();

export default ContentSectionViewProps;
