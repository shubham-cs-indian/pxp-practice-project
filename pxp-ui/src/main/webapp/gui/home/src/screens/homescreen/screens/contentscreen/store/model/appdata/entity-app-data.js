/**
 * Created by CS56 on 9/27/2016.
 */
import IAppData from './i-app-data';

var EntityAppData = function () {
  this.inheritsFrom(IAppData);
  this.className = "EntityAppData";

  /**
   * Private properties and methods
   */
  var aEntityList = [];

  /**
   * Public methods
   */
  this.getEntityList = function () {
    return aEntityList;
  };

  this.setEntityList = function (_aEntityList) {
    aEntityList = _aEntityList;
  };

};


export default new EntityAppData();