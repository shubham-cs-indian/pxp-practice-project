var DragViewModel = function (sId, sLabel, bIsDraggable, sContext, oProperties) {

  this.id = sId;
  this.label = sLabel;
  this.isDraggable = bIsDraggable;
  this.context = sContext;
  this.properties = oProperties;

};

export default DragViewModel;
