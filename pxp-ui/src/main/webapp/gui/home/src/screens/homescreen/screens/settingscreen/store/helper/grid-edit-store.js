import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import SettingUtils from './../helper/setting-utils';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import {GridEditMapping} from '../../tack/setting-screen-request-mapping';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import GridEditViewProps from "./../model/grid-edit-view-props";

let GridEditStore = (function () {
  let _triggerChange = function () {
    GridEditStore.trigger('grid-edit-changed');
  };

  let _handleSaveAction = function (oCallbackData) {
    let aPropertySequenceList = GridEditViewProps.getPropertySequenceList();
    aPropertySequenceList = aPropertySequenceList.clonedObject;
    let aPropertySequenceIdsList = CS.map(aPropertySequenceList, "id");
    let oPropertySequenceListModel = {sequenceList: aPropertySequenceIdsList};
    SettingUtils.csPostRequest(GridEditMapping.saveGridEditProperty, {}, oPropertySequenceListModel, successSaveGridEdit.bind(this, oCallbackData), failureSaveGridEdit);
  };

  let _handleDiscardAction = function (oCallback) {
    let oPropertySequenceListModel = GridEditViewProps.getPropertySequenceList();
    delete oPropertySequenceListModel.clonedObject;
    delete oPropertySequenceListModel.isDirty;

    GridEditViewProps.setGridEditScreenLockStatus(false);
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    _fetchGridEditProperties(false, false, oCallback);
    _triggerChange();
  };

  let successSaveGridEdit = function (oCallback, oResponse) {
    let oGridEditModel = oResponse.success.propertySequenceList;
    GridEditViewProps.setPropertySequenceList(oGridEditModel);
    GridEditViewProps.setGridEditScreenLockStatus(false);
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  let failureSaveGridEdit = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveGridEditCallback", getTranslation());
  };

  let successFetchGridEdit = function (oCallback, oResponse) {
    oResponse = oResponse.success;
    let aPropertyList = oResponse.propertyList;
    let aPropertySequenceList = oResponse.propertySequenceList;
    CS.map(aPropertyList,(oProperty) => {
      oProperty.type && (oProperty.iconClassName = oProperty.type);
    });

    CS.map(aPropertySequenceList,(oProperty) => {
      oProperty.type && (oProperty.iconClassName = oProperty.type);
    });

    GridEditViewProps.setPropertyList(aPropertyList);
    GridEditViewProps.setPropertySequenceList(aPropertySequenceList);
    GridEditViewProps.setSequenceListLimit(25);

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }

    _triggerChange();
  };

  let failureFetchGridEdit = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchGridEditCallback", getTranslation());
  };

  let _handleSearchTextChanged = (sSearchText) => {
    GridEditViewProps.setSearchText(sSearchText);
    _fetchGridEditProperties(false, false);
  };

  let _handleLoadMoreClicked = () => {
    let oPaginationData = GridEditViewProps.getPaginationData();
    let aPropertyList = GridEditViewProps.getPropertyList();
    oPaginationData.from = CS.size(aPropertyList);
    _fetchGridEditProperties(true, false);
  };

  let _makeGridEditDirty = function () {
    let oPropertySequenceModel = GridEditViewProps.getPropertySequenceList();

    SettingUtils.makeObjectDirty(oPropertySequenceModel);
    return oPropertySequenceModel.clonedObject;
  };

  let _fetchGridEditData = function (oCallback) {
    let oPaginationData = GridEditViewProps.getPaginationData();
    let iSize = oPaginationData.pageSize;

    let oRequestData = {
      from: 0,
      searchColumn: "label",
      searchText: GridEditViewProps.getSearchText(),
      size: iSize,
      sortBy: GridEditViewProps.getSortBy(),
      sortOrder: GridEditViewProps.getSortOrder(),
      isRuntimeRequest: false
    };
    SettingUtils.csPostRequest(GridEditMapping.getGridEditProperty, {}, oRequestData, successFetchGridEdit.bind(this, oCallback), failureFetchGridEdit);
  };

  let _handleColumnOrganizerSaveButtonClicked = function (aSelectedOrganizerColumns, oCallbackData) {
    let aPropertySequenceList = aSelectedOrganizerColumns;
    let aPropertySequenceIdsList = CS.map(aPropertySequenceList, "id");
    let oPropertySequenceListModel = {sequenceList: aPropertySequenceIdsList};
    SettingUtils.csPostRequest(GridEditMapping.saveGridEditProperty, {}, oPropertySequenceListModel, successSaveGridEdit.bind(this, oCallbackData), failureSaveGridEdit);
  };

  let _fetchGridEditProperties = (bIsLoadMore, bDisableLoader, oCallback) => {
    let oPaginationData = GridEditViewProps.getPaginationData();
    let iSize = !bIsLoadMore ? oPaginationData.from + oPaginationData.pageSize : oPaginationData.pageSize;

    let aPropertySequenceList = GridEditViewProps.getPropertySequenceList();
    aPropertySequenceList = aPropertySequenceList.clonedObject && aPropertySequenceList.clonedObject || aPropertySequenceList;
    let aPropertySequenceIdsList = CS.map(aPropertySequenceList, "id");

    let oRequestData = {
      from: bIsLoadMore ? oPaginationData.from : 0,
      searchColumn: "label",
      searchText: GridEditViewProps.getSearchText(),
      size: iSize,
      sortBy: GridEditViewProps.getSortBy(),
      sortOrder: GridEditViewProps.getSortOrder(),
      idsToExclude: aPropertySequenceIdsList
    };

    SettingUtils.csPostRequest(GridEditMapping.getGridEditProperties, {}, oRequestData, successFetchGridEditProperties.bind(this, bIsLoadMore, oCallback), failureFetchGridEdit, bDisableLoader);
  };

  let successFetchGridEditProperties = function (bIsLoadMore, oCallback, oResponse) {
    oResponse = oResponse.success;
    let aGridEditProperties = oResponse.gridEditProperties;

    if (!!bIsLoadMore) {
      let aCurrentPropertyList = GridEditViewProps.getPropertyList();
      aGridEditProperties = CS.concat(aCurrentPropertyList, aGridEditProperties);
    }
    GridEditViewProps.setPropertyList(aGridEditProperties);

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  let _handlePropertySequenceShuffled = function (oSource, oDestination, aDraggableIds) {
    _makeGridEditDirty();
    let aPropertiesSequenceList = GridEditViewProps.getPropertySequenceList().clonedObject;
    let aPropertiesList = GridEditViewProps.getPropertyList();
    let iDestinationIndex = oDestination.index;
    let iSourceIndex = oSource.index;
    let aShuffledProperties = [];
    GridEditViewProps.setGridEditScreenLockStatus(true);

    CS.forEach(aDraggableIds, function (sId) {
      let oProperty = CS.find(aPropertiesList, {id: sId}) || CS.find(aPropertiesSequenceList, {id: sId});
      oProperty && aShuffledProperties.push(oProperty)
    });

    if (oSource.droppableId === "propertyList" && oDestination.droppableId === "propertySequenceList") {
      if (CS.size(aPropertiesSequenceList) === GridEditViewProps.getSequenceListLimit()) {
        alertify.error(getTranslation().LIMIT_OF_TWENTY_FIVE_PROPERTIES_EXCEEDED);
        return;
      }
      aPropertiesSequenceList.splice(iDestinationIndex, 0, ...aShuffledProperties);
      _fetchGridEditProperties(false);
    }
    else if (oSource.droppableId === "propertySequenceList") {
      CS.remove(aPropertiesSequenceList, function (oProperty) {
        return CS.includes(aDraggableIds, oProperty.id);
      });
      oDestination.droppableId === "propertyList" && _fetchGridEditProperties(false);

      if (oDestination.droppableId === "propertySequenceList") {
        if(iSourceIndex < iDestinationIndex) {
          iDestinationIndex = iDestinationIndex - (aDraggableIds.length - 1);
        }
        aPropertiesSequenceList.splice(iDestinationIndex, 0, ...aShuffledProperties);
        _triggerChange();
      }
    }

  };

  let _getGridEditScreenLockStatus = function () {
    return GridEditViewProps.getGridEditScreenLockStatus();
  };

  let _resetGridEditProps = function () {
    return GridEditViewProps.reset();
  };

  return {
    handleSaveAction: function (oCallbackData) {
      _handleSaveAction(oCallbackData);
    },

    handleDiscardAction: function (oCallbackData) {
      _handleDiscardAction(oCallbackData);
    },

    handlePropertySequenceShuffled: function (oSource, oDestination, aDraggableIds) {
      _handlePropertySequenceShuffled(oSource, oDestination, aDraggableIds);
    },

    handleSearchTextChanged: function (sSearchText) {
      _handleSearchTextChanged(sSearchText);
    },

    handleLoadMoreClicked: function () {
      _handleLoadMoreClicked();
    },

    fetchGridEditData: function () {
      _fetchGridEditData();
    },

    handleColumnOrganizerSaveButtonClicked(aSelectedOrganizerColumns) {
      _handleColumnOrganizerSaveButtonClicked(aSelectedOrganizerColumns);
    },

    getGridEditScreenLockStatus: function () {
      return _getGridEditScreenLockStatus();
    },

    resetGridEditProps: function () {
      return _resetGridEditProps();
    }
  }
})();

MicroEvent.mixin(GridEditStore);

export default GridEditStore;
