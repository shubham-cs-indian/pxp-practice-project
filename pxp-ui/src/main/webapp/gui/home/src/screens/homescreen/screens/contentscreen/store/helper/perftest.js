import CS from '../../../../../../libraries/cs';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
var aTree = [];
var oIdLabelMap = {};
var oIdOtherInfoMap = {};
var oPropsForTree = {};
var oPropsForMap = {};
var sRandomNodeId = "some_random_ID_string_9_3_9_9";
var sRandomTextForSearch = "string_9_9_9";
var aSearchesFromTree = [];
var aSearchesFromMap = [];

function makeTreeNode(sSuffix, iCnt, iPwr, iCurrentPwr, sParentId){

  var aChildren = [];
  var sId = "some_random_ID_string" + sSuffix;
  var sLabel = "some_random_LABEL_string" + sSuffix;

  if(iPwr > iCurrentPwr){

    for(var i=0; i<iCnt; i++){
      aChildren.push(makeTreeNode(sSuffix+"_"+i, iCnt, iPwr, iCurrentPwr+1, sId))
    }
  }

  oIdLabelMap[sId] = sLabel;
  oIdOtherInfoMap[sId] = {
    id: sId,
    children: CS.map(aChildren, "id"),
    parentId: sParentId
  };

  return {
    id: sId,
    label: sLabel,
    children: aChildren
  }
}

function makeTreeData(){
  var iCnt = 10;
  var iPwr = 4;

  for(var i=0; i<iCnt; i++){
    aTree.push(makeTreeNode("_" + i, iCnt, iPwr, 1, "-1"))
  }
}

var _getToggledNodeState = function (iState) {
  if (iState == 2) {
    return 0;
  } else {
    return 2;
  }
};

var _assignToAllNodesBelow = function (oObjToAssign, oNode, oVisualProps) {
  var oNodeVisualProp = oVisualProps[oNode.id] || {};
  CS.assign(oNodeVisualProp, oObjToAssign);

  CS.forEach(oNode.children, function (oChild) {
    _assignToAllNodesBelow(oObjToAssign, oChild, oVisualProps)
  });
};

var _toggleNodeStateRecursively = function (aNodes, sNodeId, oVisualProps) {

  var bNodeFound = false;
  var bAllChildChecked = true;
  var bAnyChildChecked = false;

  CS.forEach(aNodes, function (oNode) {
    oVisualProps[oNode.id] = oVisualProps[oNode.id] || {};
    var oNodeVisualProp = oVisualProps[oNode.id];
    if (oNode.id == sNodeId) {
      bNodeFound = true;
      var iNewState = _getToggledNodeState(oNodeVisualProp.isChecked);
      _assignToAllNodesBelow({isChecked: iNewState, isHidden: !iNewState}, oNode, oVisualProps);
    }
    else if (!bNodeFound && !CS.isEmpty(oNode.children)) {
      var oChildInfo = _toggleNodeStateRecursively(oNode.children, sNodeId, oVisualProps);
      if (oChildInfo.isAllChildChecked) {
        oNodeVisualProp.isChecked = 2;
      }
      else if (oChildInfo.isAnyChildChecked) {
        oNodeVisualProp.isChecked = 1;
      }
      else {
        oNodeVisualProp.isChecked = 0
      }
    }

    bAllChildChecked = bAllChildChecked && oNodeVisualProp.isChecked == 2;
    bAnyChildChecked = bAnyChildChecked || !!oNodeVisualProp.isChecked;
  });

  return {
    isNodeFound: bNodeFound,
    isAnyChildChecked: bAnyChildChecked,
    isAllChildChecked: bAllChildChecked
  }

};

function randomNodeCheckedThroughTree(){
  _toggleNodeStateRecursively(aTree, sRandomNodeId, oPropsForTree);
}

function resetNodeStateAccordingToChildren(oNode){
  oPropsForMap[oNode.id] = oPropsForMap[oNode.id] || {};
  var oNodeVisualProp = oPropsForMap[oNode.id];
  var aChildrenIds = oNode.children;

  var bAllChildChecked = true;
  var bAnyChildChecked = false;

  for(var i=0; i<aChildrenIds.length; i++){
    var sChildId = aChildrenIds[i];
    oPropsForMap[sChildId] = oPropsForMap[sChildId] || {};
    var oChildProps = oPropsForMap[sChildId];

    bAllChildChecked = bAllChildChecked && oChildProps.isChecked == 2;
    bAnyChildChecked = bAnyChildChecked || !!oChildProps.isChecked;
  }

  if (bAllChildChecked) {
    oNodeVisualProp.isChecked = 2;
  }
  else if (bAnyChildChecked) {
    oNodeVisualProp.isChecked = 1;
  }
  else {
    oNodeVisualProp.isChecked = 0
  }

}

function randomNodeCheckedThroughMap(){
  var oNode = oIdOtherInfoMap[sRandomNodeId];
  var sParentId = oNode.parentId;
  oPropsForMap[sRandomNodeId] = oPropsForMap[sRandomNodeId] || {};
  var oNodeVisualProp = oPropsForMap[sRandomNodeId];

  var iNewState = _getToggledNodeState(oNodeVisualProp.isChecked);
  _assignToAllNodesBelow({isChecked: iNewState, isHidden: !iNewState}, oNode, oPropsForMap);

  while(sParentId != "-1"){
    var oParentNode = oIdOtherInfoMap[sParentId];
    resetNodeStateAccordingToChildren(oParentNode);
    sParentId = oParentNode.parentId;
  }

}

var _matchAndAddTaxonomyIds = function(aTaxonomyTree, aMatchingTaxonomyIds, sSearchText){
  CS.forEach(aTaxonomyTree, function (oTaxonomyObject) {
    var sLabel = oTaxonomyObject.label;
    var sLabelToLowerCase = sLabel.toLowerCase();
    var sSearchTextToLowerCase = sSearchText.toLowerCase();
    if(sLabelToLowerCase.includes(sSearchTextToLowerCase)){
      if(oTaxonomyObject.id != -1) {
        aMatchingTaxonomyIds.push(oTaxonomyObject.id);
      }
    }
    _matchAndAddTaxonomyIds(oTaxonomyObject.children, aMatchingTaxonomyIds, sSearchText);
  })
};

var _addMatchingTaxonomyIds = function(sSearchText){
  _matchAndAddTaxonomyIds(aTree, aSearchesFromTree, sSearchText);
};

function randomTextSearchedFromTree(){
  _addMatchingTaxonomyIds(sRandomTextForSearch);
}

function randomTextSearchedFromMap(){

  for(var key in oIdLabelMap){
    var sVal = oIdLabelMap[key];
    if(sVal.indexOf(sRandomTextForSearch) != -1){
      aSearchesFromMap.push(key);
    }
  }
}

var perform = function (fFunctionToExecute) {
  var startTime = new Date().getTime();
  fFunctionToExecute();
  var endTime = new Date().getTime();

  ExceptionLogger.log(fFunctionToExecute.name + ": " + (endTime - startTime) + " ms");
};

var run = function () {

  perform(makeTreeData);
  perform(randomNodeCheckedThroughTree);
  perform(randomNodeCheckedThroughMap);
  perform(randomTextSearchedFromTree);
  perform(randomTextSearchedFromMap);

  // ExceptionLogger.log(JSON.stringify(oPropsForTree));
  // ExceptionLogger.log(JSON.stringify(oPropsForMap));

  // ExceptionLogger.log(JSON.stringify(aSearchesFromTree));
  // ExceptionLogger.log(JSON.stringify(aSearchesFromMap));
  //
  // ExceptionLogger.log(aSearchesFromTree.length);
  // ExceptionLogger.log(aSearchesFromMap.length);

}();

