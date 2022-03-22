var MenuViewModel = function (sId, sName, sClassName, sTitle, bIsSelected, oProperties) {

  this.id = sId;
  this.label = sName;
  this.className = sClassName;
  this.title = sTitle;
  this.isSelected = bIsSelected;
  this.properties = oProperties;

};

export default MenuViewModel;

