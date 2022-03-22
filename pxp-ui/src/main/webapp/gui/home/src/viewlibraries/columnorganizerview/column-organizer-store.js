import ColumnOrganizerProps from './column-organizer-props';
import CS from "../../libraries/cs";
import alertify from "../../commonmodule/store/custom-alertify-store";
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import ContentUtils from "../../screens/homescreen/screens/contentscreen/store/helper/content-utils";
import CommonUtils from '../../commonmodule/util/common-utils';
import ContextualGridViewProps from "../contextualgridview/store/model/contextual-grid-view-props"
import MenuDictionary from "../../screens/homescreen/tack/menu-dictionary";
import GlobalStore from "../../screens/homescreen/store/global-store";
import SettingScreenProps from "../../screens/homescreen/screens/settingscreen/store/model/setting-screen-props";

let ColumnOrganizerStore = function (oProps, fTriggerChangeHandler) {



  let _getColumnOrganizerProps = function (sTableContextId) {
    if(CS.isEmpty(sTableContextId)){
      return ColumnOrganizerProps;
    }

    let aMenuList = _getMenuList();
    let oSelectedMenu = CS.find(aMenuList, function (oMenu) {
      return oMenu.isSelected;
    });
    //TODO: Remove the dependency
    if (oSelectedMenu.type === MenuDictionary['setting']) {
      return ContextualGridViewProps.getGridViewPropsByContext(sTableContextId).columnOrganizerProps;
    }

    //TODO: temporary fix, should not require ContentUtils.
    let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContextId);
    return ContentUtils.getComponentProps().tableViewProps.getColumnOrganizerPropsByContext(sIdForProps, oProps.klassInstanceId);
  };

  let _getGridViewProps = function (sTableContextId) {
    let aMenuList = _getMenuList();
    let oSelectedMenu = CS.find(aMenuList, function (oMenu) {
      return oMenu.isSelected;
    });
    //TODO: Remove the dependency
    if (oSelectedMenu.type === MenuDictionary['setting']) {
      return ContextualGridViewProps.getGridViewPropsByContext(sTableContextId).gridViewProps;
    }

    //TODO: temporary fix, should not require ContentUtils.
    let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContextId);
    return ContentUtils.getComponentProps().tableViewProps.getTableViewPropsByContext(sIdForProps, oProps.klassInstanceId);
  };

  let _getMenuList = () => {
    let oGlobalModulesData = GlobalStore.getGlobalModulesData();
    return oGlobalModulesData.screens;
  };

  let _initializeProps = function (oProps) {
    let oColumnOrganizerProps = null;
    let aColumns = [];

    let aAvailableColumns = oProps.totalColumns;
    let aSelectedColumns = (oProps.selectedColumns && oProps.selectedColumns.clonedObject) || oProps.selectedColumns;
    CS.forEach(aAvailableColumns, function (oColumn) {
      if (!CS.find(aSelectedColumns, {id: oColumn.id}) && !CS.find(aColumns, {id: oColumn.id})) {
        aColumns.push(oColumn);
      }
    });
    if(CS.isEmpty(oProps.tableContextId)){
      oColumnOrganizerProps = ColumnOrganizerProps;
    }
    else {
      oColumnOrganizerProps = _getColumnOrganizerProps(oProps.tableContextId);
    }

    if(CS.isEmpty(oColumnOrganizerProps.getTotalColumns())){
      oColumnOrganizerProps.setHiddenColumns(aColumns);
      oColumnOrganizerProps.setTotalColumns(aAvailableColumns);
      oColumnOrganizerProps.setSelectedOrganizedColumns(oProps.selectedColumns);
      oColumnOrganizerProps.setColumnOrganizerContext(oProps.context);
      oColumnOrganizerProps.setIsLoadMore(oProps.bIsLoadMore);
      oColumnOrganizerProps.setSelectedColumnsLimit(oProps.selectedColumnsLimit);
      CS.isNotEmpty(oProps.customRequestResponseInfo) && oColumnOrganizerProps.setCustomRequestResponseInfo(oProps.customRequestResponseInfo);
    }
  };

  _initializeProps(oProps);

  let _triggerChange = function () {
    fTriggerChangeHandler && fTriggerChangeHandler();
  };

  let _handleColumnOrganizerDialogButtonClicked = function (sButtonId, sTableContextId) {
    if (sButtonId === "save") {
      _saveActiveColumnOrganizerConfig(sTableContextId);
    } else if (sButtonId === "cancel") {
      _discardActiveColumnOrganizerConfig(sTableContextId);
      _triggerChange();
    } else {
      _closeColumnOrganizerDialog(sTableContextId);
    }
  };

  let _handleSearchTextChanged = function (sSearchText, sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    oColumnOrganizerProps.setSearchText(sSearchText);
    let aColumns = [];
    let aAvailableColumns = oColumnOrganizerProps.getTotalColumns();
    let aSelectedOrganizerColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();
    aSelectedOrganizerColumns = aSelectedOrganizerColumns.clonedObject || aSelectedOrganizerColumns;
    CS.forEach(aAvailableColumns, function (oColumn) {
      if (!CS.find(aSelectedOrganizerColumns, {id: oColumn.id}) && !CS.find(aColumns, {id: oColumn.id})) {
        aColumns.push(oColumn);
      }
    });

    let oCustomRequestResponseInfo = oColumnOrganizerProps.getCustomRequestResponseInfo();

    if(CS.isNotEmpty(oCustomRequestResponseInfo)){
      _handleCustomSearchRequest(oCustomRequestResponseInfo.searchRequestResponseInfo);
    } else {
      CS.remove(aColumns, (oColumn) => {
        return !CS.includes(oColumn.label.toLocaleLowerCase(), sSearchText.toLocaleLowerCase());
      });
      oColumnOrganizerProps.setHiddenColumns(aColumns);
    }
  };

  let _handleLoadMoreClicked = function (sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    let oCustomRequestResponseInfo = oColumnOrganizerProps.getCustomRequestResponseInfo();
    if(CS.isEmpty(oCustomRequestResponseInfo)){
      return;
    } else {
      let oPaginationData = oColumnOrganizerProps.getPaginationData();
      let aHiddenColumns = oColumnOrganizerProps.getHiddenColumns();
      oPaginationData.from = CS.size(aHiddenColumns);
      _handleCustomSearchRequest(oCustomRequestResponseInfo.searchRequestResponseInfo, true);
    }
  };

  let _resetColumnOrganizerPaginationData = (sTableContextId) => {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    oColumnOrganizerProps.setSearchText("");
    oColumnOrganizerProps.setPaginationData({
      from: 0,
      pageSize: 20
    });
  };

  let _saveActiveColumnOrganizerConfig = function (sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    oColumnOrganizerProps.setSelectedOrganizedColumns(oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject);
    oColumnOrganizerProps.getIsDialogOpen() && _closeColumnOrganizerDialog(sTableContextId);
    if(CS.isNotEmpty(sTableContextId)) {
      let oGridViewProps = _getGridViewProps(sTableContextId);
      oGridViewProps.setResizedColumnId("");
    }
    else {
      SettingScreenProps.screen.setResizedColumnId("");
    }
  };

  let _discardActiveColumnOrganizerConfig = function (sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    delete oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject;
    delete oColumnOrganizerProps.getSelectedOrganizedColumns().isDirty;

    let oCustomRequestResponseInfo = oColumnOrganizerProps.getCustomRequestResponseInfo();
    if(CS.isEmpty(oCustomRequestResponseInfo)){
      let aColumns = [];
      let aAvailableColumns = oColumnOrganizerProps.getTotalColumns();
      CS.forEach(aAvailableColumns, function (oColumn) {
        if (!CS.find(oColumnOrganizerProps.getSelectedOrganizedColumns(), {id: oColumn.id}) && !CS.find(aColumns, {id: oColumn.id})) {
          aColumns.push(oColumn);
        }
      });
      oColumnOrganizerProps.setHiddenColumns(aColumns);
    } else {
      _handleCustomSearchRequest(oCustomRequestResponseInfo.searchRequestResponseInfo);
    }
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
  };

  let _closeColumnOrganizerDialog = function (sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    _resetColumnOrganizerPaginationData(sTableContextId);
    oColumnOrganizerProps.setTotalColumns([]);
    oColumnOrganizerProps.setIsDialogOpen(false);
  };

  let _getCustomSearchRequestData = function (bIsLoadMore, sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    let oPaginationData = oColumnOrganizerProps.getPaginationData();
    let iSize = !bIsLoadMore ? oPaginationData.from + oPaginationData.pageSize : oPaginationData.pageSize;

    let aSelectedOrganizerColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();
    aSelectedOrganizerColumns = aSelectedOrganizerColumns.clonedObject && aSelectedOrganizerColumns.clonedObject || aSelectedOrganizerColumns;
    let aSelectedOrganizerColumnIdsList = CS.map(aSelectedOrganizerColumns, "id");

    return {
      from: bIsLoadMore ? oPaginationData.from : 0,
      searchColumn: "label",
      searchText: oColumnOrganizerProps.getSearchText(),
      size: iSize,
      sortBy: "label",
      sortOrder: "asc",
      idsToExclude: aSelectedOrganizerColumnIdsList
    };
  };

  let _handleCustomSearchRequest = function (oSearchRequestResponseInfo, bIsLoadMore, sTableContextId, aColumn) {
    let oRequestData = _getCustomSearchRequestData(bIsLoadMore, sTableContextId);
    CS.postRequest(oSearchRequestResponseInfo.url, {}, oRequestData, _successCustomSearchRequest.bind(this, oSearchRequestResponseInfo, bIsLoadMore, sTableContextId, aColumn), _failureCustomSearchRequest);

  };

  let _successCustomSearchRequest = function (oSearchRequestResponseInfo, bIsLoadMore, sTableContextId, aColumn, oResponse) {
    oResponse = oResponse.success;
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    let aHiddenColumns = oResponse[oSearchRequestResponseInfo.hiddenColumnsResponsePath];

    if (!!bIsLoadMore) {
      let aCurrentHiddenColumnList = oColumnOrganizerProps.getHiddenColumns();
      aHiddenColumns = CS.concat(aCurrentHiddenColumnList, aHiddenColumns);
    }

    CS.map(aHiddenColumns, (oColumn) => {
      oColumn.type && (oColumn.iconClassName = oColumn.type);
    });

    CS.forEach(aColumn, function (oColumn) {
      let oHiddenColumn = CS.find(aHiddenColumns, {id:oColumn.id});
      oHiddenColumn.iconKey = oColumn.iconKey;
      oHiddenColumn.showDefaultIcon = true;
    });
    bIsLoadMore = aHiddenColumns.length !== oResponse.totalCount;
    oColumnOrganizerProps.setIsLoadMore(bIsLoadMore);
    oColumnOrganizerProps.setHiddenColumns(aHiddenColumns);
    _triggerChange();
  };

  let _failureCustomSearchRequest = (oResponse) => {
    CommonUtils.failureCallback(oResponse, "failureCustomSearchRequest", getTranslation);
  };

  let _handleColumnsShuffled = function (oSource, oDestination, aDraggableIds, oCallbackData, sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    let aSelectedColumns = _makeColumnOrganizerDirty(sTableContextId);
    let aColumns = oColumnOrganizerProps.getHiddenColumns();
    let iDestinationIndex = oDestination.index;
    let aShuffledColumns = [];
    let oCustomRequestResponseInfo = oColumnOrganizerProps.getCustomRequestResponseInfo();

    CS.forEach(aDraggableIds, function (sId) {
      let oProperty = CS.find(aColumns, {id: sId}) || CS.find(aSelectedColumns, {id: sId});
      oProperty && aShuffledColumns.push(oProperty)
    });

    if (oSource.droppableId === "propertyList" && oDestination.droppableId === "propertySequenceList") {
      let iSelectedColumnsLimit = oColumnOrganizerProps.getSelectedColumnsLimit();
      if (CS.isNotEmpty(CS.toString(iSelectedColumnsLimit)) && (CS.size(aSelectedColumns) + CS.size(aShuffledColumns) > iSelectedColumnsLimit)) {
        alertify.error(getTranslation().LIMIT_OF_TWENTY_FIVE_PROPERTIES_EXCEEDED);
        return;
      }
      aSelectedColumns.splice(iDestinationIndex, 0, ...aShuffledColumns);
      CS.remove(aColumns, function (oProperty) {
        return CS.find(aShuffledColumns, {id:oProperty.id});
      });
      CS.isNotEmpty(oCustomRequestResponseInfo) && _handleCustomSearchRequest(oCustomRequestResponseInfo.searchRequestResponseInfo, sTableContextId);
    }
    else if (oSource.droppableId === "propertySequenceList") {
      CS.remove(aSelectedColumns, function (oProperty) {
        return CS.includes(aDraggableIds, oProperty.id);
      });

      if (oDestination.droppableId === "propertySequenceList") {
        iDestinationIndex = (oSource.index < iDestinationIndex) ? iDestinationIndex - (aDraggableIds.length - 1) : iDestinationIndex;
        aSelectedColumns.splice(iDestinationIndex, 0, ...aShuffledColumns);
      } else {
        aColumns = CS.concat(aColumns, aShuffledColumns);
        aColumns = CS.sortBy(aColumns,"label");
        oColumnOrganizerProps.setHiddenColumns(aColumns);
        CS.isNotEmpty(oCustomRequestResponseInfo) && _handleCustomSearchRequest(oCustomRequestResponseInfo.searchRequestResponseInfo,"","", aColumns);
      }
    }
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let _makeColumnOrganizerDirty = function (sTableContextId) {
    let oColumnOrganizerProps = _getColumnOrganizerProps(sTableContextId);
    let aSelectedColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();

    if (!aSelectedColumns.clonedObject) {
      aSelectedColumns.clonedObject = CS.cloneDeep(aSelectedColumns);
      aSelectedColumns.isDirty = true;
    }
    return aSelectedColumns.clonedObject;
  };

 /* let _resetColumnOrganizerProps = function () {
    ColumnOrganizerProps.reset();
  };
*/
  return {

    handleColumnOrganizerDialogButtonClicked: function (sButtonId, sTableContextId) {
      _handleColumnOrganizerDialogButtonClicked(sButtonId, sTableContextId);
    },

    handleSearchTextChanged: function (sSearchText, sTableContextId) {
      _handleSearchTextChanged(sSearchText, sTableContextId);
      _triggerChange();
    },

    handleLoadMoreClicked: function (sTableContextId) {
      _handleLoadMoreClicked(sTableContextId);
    },

    handlePropertySequenceShuffled: function (oSource, oDestination, aDraggableIds, oCallbackData, sTableContextId) {
      _handleColumnsShuffled(oSource, oDestination, aDraggableIds, oCallbackData, sTableContextId);
      _triggerChange();
    },

    saveActiveColumnOrganizerConfig: function(sTableContextId) {
      _saveActiveColumnOrganizerConfig(sTableContextId);
      _triggerChange();
    },

    discardActiveColumnOrganizerConfig: function(sTableContextId) {
      _discardActiveColumnOrganizerConfig(sTableContextId);
      _triggerChange();
    },

    getColumnOrganizerProps: function (sTableContextId) {
      return _getColumnOrganizerProps(sTableContextId);
    },

    initializeStore: function (oProps) {
      _initializeProps(oProps);
    },

 /*   resetColumnOrganizerProps: function () {
      _resetColumnOrganizerProps();
    },*/
  }
};

export default ColumnOrganizerStore;
