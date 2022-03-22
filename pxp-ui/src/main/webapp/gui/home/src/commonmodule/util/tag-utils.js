/**
 * Created by CS31 on 23-11-2016.
 */
import CS from '../../libraries/cs';
import TagTypeConstants from './../tack/tag-type-constants';
import ClassNameDictionary from './../tack/dictionary-for-class-name';

var TagUtils = (function () {

  var oTagTypeValues = {};

  oTagTypeValues[TagTypeConstants.YES_NEUTRAL_TAG_TYPE] = [
    {
      "label": "Neutral",
      "relevance": 0
    },
    {
      "label": "Yes",
      "relevance": 100
    }
  ];

  oTagTypeValues[TagTypeConstants.RULER_TAG_TYPE] = [
    {
      "label": "Neutral",
      "relevance": 0
    },
    {
      "label": "Yes",
      "relevance": 100
    }
  ];

  oTagTypeValues[TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE] = [
    {
      "label": "No",
      "relevance": -100
    },
    {
      "label": "Neutral",
      "relevance": 0
    },
    {
      "label": "Yes",
      "relevance": 100
    }
  ];
  oTagTypeValues[TagTypeConstants.RANGE_TAG_TYPE] = [
    {
      "label": "From",
      "relevance": -100
    },
    {
      "label": "To",
      "relevance": 100
    }
  ];
  oTagTypeValues[TagTypeConstants.TAG_TYPE_BOOLEAN] = [
    {
      "label": "No",
      "relevance": -100
    },
    {
      "label": "Yes",
      "relevance": 100
    }
  ];
  oTagTypeValues[TagTypeConstants.TAG_TYPE_MASTER] = [
    {
      "label": "Neutral",
      "relevance": 0
    },
    {
      "label": "Yes",
      "relevance": 100
    }
  ];

  var _getTagValuesForTagType = function (sTagType) {
    return oTagTypeValues[sTagType];
  };

  var _isTagYesNeutral = function (sType) {
    return sType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE;
  };

  var _isTagRuler = function (sType) {
    return sType == TagTypeConstants.RULER_TAG_TYPE;
  };

  var _isTagYesNeutralNo = function (sType) {
    return sType == TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE;
  };

  var _isTagRange = function (sType) {
    return sType == TagTypeConstants.RANGE_TAG_TYPE;
  };

  var _isTagBoolean = function (sType) {
    return sType == TagTypeConstants.TAG_TYPE_BOOLEAN;
  };

  var _getRoundedTagRelevanceValueByTagType = function (iRelevance, sTagType) {
    var iNewRelevance = 0;
    if (iRelevance) {
      if(_isTagYesNeutral(sTagType) || _isTagRuler(sTagType)) {
        if(iRelevance > 0) {
          iNewRelevance = 100;
        }
      } else if (_isTagYesNeutralNo(sTagType) || _isTagBoolean(sTagType)) {
        if(iRelevance > 0) {
          iNewRelevance = 100;
        } else {
          iNewRelevance = -100;
        }
      } else  {
        iNewRelevance = iRelevance;
      }
    }

    return iNewRelevance;
  };

  var _getSplitter = function(){
    return "#$%$#";
  };

  var _getLeafNodeWithSubbedWithParentInfo = function (aTreeNode, sParentPropertyName, sChildPropName) {
    var sSplitter = _getSplitter();
    var aChildNodes = [];
    var sCustomField = 'custom' + sSplitter + sParentPropertyName;
    CS.forEach(aTreeNode, function (oNode) {
      var aChildren = oNode[sChildPropName];
      if(CS.isEmpty(aChildren)) {
        oNode[sCustomField] = oNode[sParentPropertyName];
        aChildNodes.push(oNode);
      } else {
        var aLeafNodes = _getLeafNodeWithSubbedWithParentInfo(aChildren, sParentPropertyName, sChildPropName, aChildNodes);
        CS.forEach(aLeafNodes, function (oLeafNode) {
          var aCustomFieldValues = oLeafNode[sCustomField].split('/');
          aCustomFieldValues.unshift(oNode[sParentPropertyName]);
          oLeafNode[sCustomField] = aCustomFieldValues.join('/');
          aChildNodes.push(oLeafNode);
        });
      }
    });

    return aChildNodes;
  };

  var _getMinValueFromListByPropertyName = function (aList, sPropertyName) {
    var oMin = CS.minBy(aList, sPropertyName);
    return oMin ? oMin[sPropertyName] : 0;
  };

  var _getMaxValueFromListByPropertyName = function (aList, sPropertyName) {
    var oMax = CS.maxBy(aList, sPropertyName);
    return oMax ? oMax[sPropertyName] : 0;
  };

  var _isTag = function (sBaseType) {
    return CS.includes(CS.values(TagTypeConstants), sBaseType) || ClassNameDictionary.ENTITY_TAG === sBaseType;
  };

  var _updateTagValueRelevanceOrRange = function (oTagValue, oRelevanceData, bUpdateRange) {
    if (CS.isEmpty(oRelevanceData)) {
      oRelevanceData = {};
      oRelevanceData.to = 0;
      oRelevanceData.from = 0;
      oRelevanceData.relevance = 0;
    }

    if (bUpdateRange) {
      if (oRelevanceData.to || oRelevanceData.from) {

        if (oRelevanceData.to) {
          oTagValue.to = oRelevanceData.to;
        }

        if (oRelevanceData.from) {
          oTagValue.from = oRelevanceData.from;
        }

      } else {
        oTagValue.to = oTagValue.from = oRelevanceData.relevance;
      }
    } else {
      oTagValue.relevance = oRelevanceData.relevance;
    }
  };

  /****************** PUBLIC API's ****************/
  return {

    getTagValuesForTagType: function (sTagType) {
      return _getTagValuesForTagType(sTagType);
    },

    getRoundedTagRelevanceValueByTagType: function (iRelevance, sTagType) {
      return _getRoundedTagRelevanceValueByTagType(iRelevance, sTagType);
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentPropertyName, sChildPropName) {
      return _getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentPropertyName, sChildPropName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    updateTagValueRelevanceOrRange: function (oTagValue, oRelevanceData, bUpdateRange) {
      _updateTagValueRelevanceOrRange(oTagValue, oRelevanceData, bUpdateRange);
    },

    isTag: function (sBaseType) {
      return _isTag(sBaseType);
    },

  }
})();

export default TagUtils;