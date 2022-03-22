import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { EndpointRequestMapping } from '../../tack/setting-screen-request-mapping';
import ProfileProps from './../model/profile-config-view-props';
import SettingUtils from './../helper/setting-utils';
import ProfileStore from './profile-store';
import SettingScreenProps from './../model/setting-screen-props';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';

var EndpointStore = (function () {

  var _triggerChange = function () {
    EndpointStore.trigger('endpoint-changed');
  };

  var _makeEndpointDirty = function (oEndpoint) {
    if (!oEndpoint.clonedObject) {
      SettingUtils.makeObjectDirty(oEndpoint);
    }
    return oEndpoint.clonedObject;
  };

  var _processAndSetGridSkeleton = function (aEndpoints) {
    var oSkeleton = {
      fixedColumns: [
        {
          id: "label",
          label: getTranslation().ELEMENT,
          type: "text",
          width: 200,
          isSortable: true,
          showTooltip: true
        }
      ],
      scrollableColumns: CS.map(aEndpoints, function (oEndpoint) {
        return {
          id: oEndpoint.id,
          label: oEndpoint.label,
          type: "text",
          isVisible: true,
          width: 150,
          actionItems: [
            {
              id: "delete",
              label: getTranslation().DELETE,
              class: "deleteIcon"
            }
          ]
        };
      }),
      actionItems: [],
      selectedContentIds: []
    };

    SettingScreenProps.screen.setGridViewSkeleton(oSkeleton);
  };

  var _createGridRowDataFromEndpoints = function (oElementMap, aEndpoints) {
    var aGridData = [];
    CS.forEach(oElementMap, function (oElement) {
      var oData = {};
      oData.id = oElement.id;
      oData.actionItemsToShow = [];
      oData.isExpanded = true;
      oData.children = _createGridRowDataFromEndpoints(oElement.children, aEndpoints);
      oData.properties = {};
      oData.properties["label"] = {
        value: oElement.label,
        isDisabled: true
      };
      CS.forEach(aEndpoints, function (oEndpoint) {
        var oFoundMapping = CS.find(oEndpoint.configRuleMappings, {mappedElementId: oElement.id});
        if (oFoundMapping) {
          oData.properties[oEndpoint.id] = {value: CS.join(oFoundMapping.columnNames, " ")};
        } else {
          oData.properties[oEndpoint.id] = {value: ""};
        }
      });
      aGridData.push(oData);
    });
    return aGridData;
  };

  var _processAndSetGridData = function (aEndpoints) {
    var oAttributeMap = SettingUtils.getAppData().getAttributeList();
    var oTagMap = SettingUtils.getAppData().getTagMap();
    var oElementMap = CS.assign({}, oAttributeMap, oTagMap);
    var aGridData = _createGridRowDataFromEndpoints(oElementMap, aEndpoints);

    SettingScreenProps.screen.setGridViewData(aGridData);
    SettingScreenProps.screen.setGridViewTotalItems(CS.size(oElementMap));
    SettingScreenProps.screen.setIsGridDataDirty(false);
  };

  var failureEndpointsCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureEndpointsCallback", getTranslation());
  };

  var _createEndpoint = function () {
    var oNewEndpointToCreate = {};
    oNewEndpointToCreate.id = UniqueIdentifierGenerator.generateUUID();
    oNewEndpointToCreate.label = UniqueIdentifierGenerator.generateUntitledName();
    oNewEndpointToCreate.isDefault = false;

    SettingUtils.csPutRequest(EndpointRequestMapping.CreateEndpoint, {}, oNewEndpointToCreate, successCreateEndpoint, failureEndpointsCallback);
  };

  var successCreateEndpoint = function (oResponse) {
    var oEndpoint = oResponse.success;
    var aEndpointsList = ProfileProps.getEndpointsList();
    aEndpointsList.push(oEndpoint);
    _processAndSetGridSkeleton(aEndpointsList);
    _processAndSetGridData(aEndpointsList);
    alertify.success(getTranslation().ENDPOINT_CREATED_SUCCESSFULLY);
    _triggerChange();
  };

  var _saveBulkEndpoints = function () {
    var aADMList = [];
    var aEndpointsList = ProfileProps.getEndpointsList();
    aEndpointsList = CS.filter(aEndpointsList, {isDirty: true});
    if (CS.isEmpty(aEndpointsList)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }
    CS.forEach(aEndpointsList, function (oEndpoint) {
      var oADMForEndpoint = ProfileStore.generateADMForEndpoint(oEndpoint, oEndpoint.clonedObject);
      aADMList.push(oADMForEndpoint);
    });
    var oDataToSave = {
      modifiedEndpointsMappings: aADMList
    };
    SettingUtils.csPostRequest(EndpointRequestMapping.SaveBulkEndpoints, {}, oDataToSave, successSaveBulkEndpoints, failureEndpointsCallback);
  };

  var successSaveBulkEndpoints = function (oResponse) {
    var aSavedEndpoints = oResponse.success;
    var aEndpoints = ProfileProps.getEndpointsList();

    CS.forEach(aSavedEndpoints, function (oSavedEndpoint) {
      var oEndpoint = CS.find(aEndpoints, {id: oSavedEndpoint.id});
      if (oEndpoint) {
        delete oEndpoint.clonedObject;
        delete oEndpoint.isDirty;
        CS.assign(oEndpoint, oSavedEndpoint);
      }
    });
    _processAndSetGridData(aEndpoints);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().ENDPOINTS}));
    _triggerChange();
  };

  var _discardEndpointsChanges = function () {
    var aEndpointsList = ProfileProps.getEndpointsList();
    CS.forEach(aEndpointsList, function (oEndpoint) {
      if (oEndpoint.isDirty) {
        delete oEndpoint.clonedObject;
        delete oEndpoint.isDirty;
      }
    });
    _processAndSetGridData(aEndpointsList);
    _triggerChange();
  };

  var _handleGridPropertyValueChangeDependencies = function (oContent, sEndpointId, value) {
    var sMappedElementId = oContent.id;
    var aEndpointsList = ProfileProps.getEndpointsList();
    var oEndpoint = CS.find(aEndpointsList, {id: sEndpointId});
    if (oEndpoint) {
      oEndpoint = _makeEndpointDirty(oEndpoint);
      var aMappings = oEndpoint.configRuleMappings || [];
      if (value) {
        var oRow = CS.find(aMappings, {mappedElementId: sMappedElementId});
        if (oRow) {
          oRow.columnNames = [value];
        } else {
          aMappings.push({
            id: UniqueIdentifierGenerator.generateUUID(),
            columnNames: [value],
            mappedElementId: sMappedElementId,
            isIgnored: false
          });
        }
      } else {
        CS.remove(aMappings, {mappedElementId: sMappedElementId});
      }
    }
    _triggerChange();
  };

  return {
    handleGridPropertyValueChangeDependencies: function (oContent, sPropertyId, value) {
      _handleGridPropertyValueChangeDependencies(oContent, sPropertyId, value)
    },

    saveBulkEndpoints: function () {
      _saveBulkEndpoints();
    },

    createEndpoint: function () {
      _createEndpoint();
    },

    discardEndpointGridViewChanges: function () {
      _discardEndpointsChanges();
    },

  }
})();

MicroEvent.mixin(EndpointStore);
export default EndpointStore;
