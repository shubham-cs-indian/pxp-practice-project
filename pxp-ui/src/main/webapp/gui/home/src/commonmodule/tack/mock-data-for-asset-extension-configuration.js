


var oTranslations = require('../store/helper/translation-manager.js')
    .getTranslations;

let aAssetExtensionConfigurationProperties = function () {
  return [
    {
      id: "extension",
      label: oTranslations().EXTENSION,
      className: "extension",
      type: "text",
      width: 28
    },
    {
      id: "extractMetadata",
      label: oTranslations().EXTRACT_METADATA,
      className: "extractMetadata",
      type: "yesno",
      width: 28
    },
    {
      id: "extractRendition",
      label: oTranslations().EXTRACT_RENDITIONS,
      className: "extractRendition",
      type: "yesno",
      width: 28
    }
  ];
}

module.exports = aAssetExtensionConfigurationProperties();



