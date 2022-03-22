/**
 * Created by CS56 on 5/12/2016.
 */
import CS from '../../../../../../libraries/cs';

import LogFactory from '../../../../../../libraries/logger/log-factory';
var logger = LogFactory.getLogger('content-log-utils');

/**
 * @type {{info, infoAjaxRequest, error, errorAjaxRequest, debug, debugAjaxRequest, warn, warnAjaxRequest}}
 *
 */

var ContentLogUtils = (function () {

  var _contentADMDataList = function (oContent) {
    var aDataList = [];
    //Attributes
    aDataList.push({'1_addedAttributes': oContent.addedAttributes});
    aDataList.push({'2_modifiedAttributes': oContent.modifiedAttributes});
    aDataList.push({'3_deletedAttributes': oContent.deletedAttributes});

    //Roles
    aDataList.push({'4_addedRoles': oContent.addedRoles});
    aDataList.push({'5_modifiedRoles': oContent.modifiedRoles});
    aDataList.push({'6_deletedRoles': oContent.deletedRoles});

    //Tags
    aDataList.push({'7_addedTags': oContent.addedTags});
    aDataList.push({'8_modifiedTags': oContent.modifiedTags});
    aDataList.push({'9_deletedTags': oContent.deletedTags});

    //StructureAttributeMapping
    aDataList.push({'10_addedStructureAttributeMapping': oContent.addedStructureAttributeMapping});
    aDataList.push({'11_modifiedStructureAttributeMapping': oContent.modifiedStructureAttributeMapping});
    aDataList.push({'12_deletedStructureAttributeMapping': oContent.deletedStructureAttributeMapping});

    //StructureChildren
    aDataList.push({'13_addedStructureChildren': oContent.addedStructureChildren});
    aDataList.push({'14_modifiedStructureChildren': oContent.modifiedStructureChildren});
    aDataList.push({'15_deletedStructureChildren': oContent.deletedStructureChildren});

    //Assets
    aDataList.push({'16_assets': {'added': oContent.addedAssets, 'deleted': oContent.deletedAssets}});

    aDataList.push({'17_otherDetails': {
                    'id': oContent.id,
                    'name': oContent.name,
                    'type': oContent.type,
                    'owner': oContent.owner,
                    'createdBy': oContent.createdBy,
                    'createdOn': oContent.createdOn
                  }});

    return aDataList;
  };

  var _contentDataList = function (oContent) {
    var aDataList = [];
    aDataList.push({'1_attributes': oContent.attributes});
    aDataList.push({'2_tags': oContent.tags});
    aDataList.push({'3_roles': oContent.roles});
    aDataList.push({'4_structureAttributeMapping': oContent.structureAttributeMapping});
    aDataList.push({'5_structureChildren': oContent.structureChildren});
    aDataList.push({'6_otherDetails': {
      'id': oContent.id,
      'name': oContent.name,
      'type': oContent.type,
      'owner': oContent.owner,
      'createdBy': oContent.createdBy,
      'createdOn': oContent.createdOn
    }});
    return aDataList;
  };

  var _info = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.info(sUseCase, oData, sURL);
    });
  };

  var _infoAjaxRequest = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.infoAjaxRequest(sUseCase, oData, sURL);
    });
  };

  var _error = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.error(sUseCase, oData, sURL);
    });
  };

  var _errorAjaxRequest = function  (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.errorAjaxRequest(sUseCase, oData, sURL);
    });
  };

  var _debug = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.debug(sUseCase, oData, sURL);
    });
  };

  var _debugAjaxRequest = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.debugAjaxRequest(sUseCase, oData, sURL);
    });
  };

  var _warn = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.warn(sUseCase, oData, sURL);
    });
  };

  var _warnAjaxRequest = function (sUseCase, oContent, sURL, sContext) {
    var aDataList = CS.upperCase(sContext) == 'ADM' ? _contentADMDataList(oContent) : _contentDataList(oContent);
    CS.forEach(aDataList, function (oData) {
      logger.warnAjaxRequest(sUseCase, oData, sURL);
    });
  };

  /******************************** PUBLIC *****************************/

  return {

    info: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _info(sUseCase, oContent, sURL, sContext);
      }) : _info(sUseCase, contents, sURL, sContext);
    },

    infoAjaxRequest: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _infoAjaxRequest(sUseCase, oContent, sURL, sContext);
      }) : _infoAjaxRequest(sUseCase, contents, sURL, sContext);
    },

    error: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _error(sUseCase, oContent, sURL, sContext);
      }) : _error(sUseCase, contents, sURL, sContext);
    },

    errorAjaxRequest: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _errorAjaxRequest(sUseCase, oContent, sURL, sContext);
      }) : _errorAjaxRequest(sUseCase, contents, sURL, sContext);
    },

    debug: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _debug(sUseCase, oContent, sURL, sContext);
      }) : _debug(sUseCase, contents, sURL, sContext);
    },

    debugAjaxRequest: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _debugAjaxRequest(sUseCase, oContent, sURL, sContext);
      }) : _debugAjaxRequest(sUseCase, contents, sURL, sContext);
    },

    warn: function (sUseCase, contents, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) {
        _warn(sUseCase, oContent, sURL, sContext);
      }) : _warn(sUseCase, contents, sURL, sContext);
    },

    warnAjaxRequest: function (sUseCase, oContent, sURL, sContext) {
      CS.isArray(contents) ? CS.forEach(contents, function (oContent) { // eslint-disable-line
        _warnAjaxRequest(sUseCase, oContent, sURL, sContext);
      }) : _warnAjaxRequest(sUseCase, contents, sURL, sContext); // eslint-disable-line
    }

  }

})();

export default ContentLogUtils;
