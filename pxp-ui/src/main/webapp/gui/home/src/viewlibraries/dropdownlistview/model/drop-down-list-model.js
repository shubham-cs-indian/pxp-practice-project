var ListModel = function (sId, sName, bIsSelected, oProperties) {

  this.id = sId;
  this.label = sName;
  this.isSelected = bIsSelected;
  this.properties = oProperties;

};

export default ListModel;