var ConfigRelationshipViewModel = function (sId, sName, sTooltip, oSide,
  oProperties) {

  this.id = sId;
  this.label = sName;
  this.tooltip = sTooltip;
  this.side = oSide;
  this.properties = oProperties;
};

export default ConfigRelationshipViewModel;

