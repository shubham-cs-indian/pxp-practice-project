import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';

var CustomSearchDialogStore = (function () {

  var _triggerChange = function () {
    CustomSearchDialogStore.trigger('custom-search-changed');
  };

  let createMockForSearch = function (aItems,sPath,sParentId,aMockData) {
    CS.forEach(aItems, function (item) {
      let oTempObject = {};
      oTempObject.id = item.id;
      oTempObject.label = item.label;
      oTempObject.path = CS.cloneDeep(sPath);;
      let oPathObject = {};
      oPathObject.id = item.id;
      oPathObject.label = item.label;
      oTempObject.path.push(oPathObject);
      oTempObject.parentId =(oTempObject.path[0].id == sParentId)? null : sParentId;

      if(CS.isEmpty(item.children)){
        aMockData.push(oTempObject);
      }
      if(item.children){
        createMockForSearch(item.children,oTempObject.path,item.id,aMockData);
      }
    })
  }

  return{
    triggerChange: function () {
      _triggerChange();
    },
  }

})();

MicroEvent.mixin(CustomSearchDialogStore);

export default CustomSearchDialogStore;
