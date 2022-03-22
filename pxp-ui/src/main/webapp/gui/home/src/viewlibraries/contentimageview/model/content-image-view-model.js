/*****
 *
 * @param sId
 * @param sImageSrc
 * @param sImageType
 * @param sClassName
 * @param oImageStyle
 * @param oProperties
 * @constructor
 */
var ImageViewModel = function (sId, sImageSrc, sImageType, sClassName, oImageStyle, oProperties) {
  this.id = sId;
  this.imageSrc = sImageSrc;
  this.imageType = sImageType;
  this.className = sClassName;
  this.imageStyle = oImageStyle;
  this.properties = oProperties;
};

export default ImageViewModel;