var ContentDetailsViewProps = (function () {

  var Props = function () {
    return {
      oContentRelationshipDragStatus: {},
      oContentSelectionVersionProps: {},
      oContentVariantVersionProps: {},
      oSectionVersionToggleProps: {}
    }
  };

  var oProperties = new Props();

  return {

    setContentRelationshipDragDetails: function (_oDragStatus) {
      oProperties.oContentRelationshipDragStatus = _oDragStatus;
    },

    resetContentSelectionVersionProps: function(oVersionProps){
      oProperties.oContentSelectionVersionProps = {};
    },

    emptyContentVariantVersionProps: function(){
      oProperties.oContentVariantVersionProps = {};
    },

    getContentVariantVersionProps: function(){
      return oProperties.oContentVariantVersionProps;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }

  };

})();

export default ContentDetailsViewProps;
