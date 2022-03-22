/**
 * @param sId
 * @param sLabel
 * @param sImageSrc
 * @param oTags
 * @param sType
 * @param sClassName
 * @param oProperties
 * @constructor
 */

var DetailedListItemModel = function (sId, sLabel, sImageSrc, oTags, sType, sClassName, oProperties, sEndpointId, sOrganizationId) {

  this.id = sId;
  this.label = sLabel;
  this.type = sType;
  this.imageSrc = sImageSrc;
  this.className = sClassName;
  this.tags = oTags;
  this.properties = oProperties;
  this.endpointId = sEndpointId;
  this.organizationId = sOrganizationId;
};

export default DetailedListItemModel;