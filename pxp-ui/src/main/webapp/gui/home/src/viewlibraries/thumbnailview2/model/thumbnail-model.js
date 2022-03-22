var ThumbnailItemModel = function (sId, sName, sImageSrc, oTags, sType, sClassName, oProperties, sEndpointId, sOrganizationId) {

  this.id = sId;
  this.label = sName;
  this.imageSrc = sImageSrc;
  this.tags = oTags;
  this.type = sType;
  this.className = sClassName;
  this.properties = oProperties;
  this.endpointId = sEndpointId;
  this.organizationId = sOrganizationId;
};

export default ThumbnailItemModel;
