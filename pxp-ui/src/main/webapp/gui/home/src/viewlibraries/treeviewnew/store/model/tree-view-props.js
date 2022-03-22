

let TreeViewProps = (function () {

  let fProps = function () {
    return {
      treeData: [],
      oTreeLoadMoreMap: {}
    }
  };

  let oProperties = new fProps();

  return {
    getTreeData: function () {
      return oProperties.treeData;
    },

    setTreeData: function (aTree) {
      oProperties.treeData = aTree;
    },

    getTreeLoadMoreMap: function () {
      return oProperties.oTreeLoadMoreMap;
    },

    setTreeLoadMoreMap: function (_oTreeLevelLoadMoreMap) {
      oProperties.oTreeLoadMoreMap = _oTreeLevelLoadMoreMap;
    },

    //Add functions above reset function
    reset: function () {
      oProperties = new fProps();
    }
  };


})();

export default TreeViewProps;
