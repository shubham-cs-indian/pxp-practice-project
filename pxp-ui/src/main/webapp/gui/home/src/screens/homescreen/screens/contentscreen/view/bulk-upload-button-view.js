import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import ViewUtils from './utils/view-utils';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  CONTENT_ASSET_BULK_UPLOAD_EVENT: "asset_bulk_upload_event",
  BULK_UPLOAD_TO_COLLECTION_CLOSED: "bulk_upload_to_collection_closed",
  BULK_UPLOAD_TO_COLLECTION_CLICKED: "bulk_upload_to_collection_clicked"
};

const oPropTypes = {
  collectionList: ReactPropTypes.array,
  showCollectionDialog: ReactPropTypes.bool,
  activeCollectionId: ReactPropTypes.string,
  filterContext: ReactPropTypes.object.isRequired,
  assetExtensions: ReactPropTypes.object,
  requestResponseData: ReactPropTypes.object
};

// @CS.SafeComponent
class BulkUploadButtonView extends React.Component {
  static propTypes = oPropTypes;
  constructor(props) {
    super(props);

    this.newCollectionLabel = React.createRef();
    this.listValidationMessage = React.createRef();
    this.labelValidationMessage = React.createRef();
    this.assetBulkUpload = React.createRef();
  }

  state = {
    collectionIds: [],
    selectedKlassId: "",
    isStateInvalid: false,
    searchText: "",
    selectedClass: {}
  };


  changeStateByProperty = (sKey, sValue) => {
    var oCurrentState = {};
    oCurrentState[sKey] = sValue;

    this.setState(oCurrentState);
  };

  /**
  * Ideally componentDidUpdate should not use setState.
  * */
  componentDidUpdate(oPreProps, oPreState) {
    const bShowCollectionDialog = oPreProps.showCollectionDialog;
    if (!CS.isEmpty(oPreState.collectionIds) && (bShowCollectionDialog !== this.props.showCollectionDialog)) {
      this.changeStateByProperty("collectionIds", []);
    }
    if (oPreState.selectedKlassId && (bShowCollectionDialog !== this.props.showCollectionDialog)) {
      this.changeStateByProperty("selectedKlassId", "");
    }
    this.assetBulkUpload.current && (this.assetBulkUpload.current.value = null);
  }

  handleBulkUploadControlClicked = (oEvent) => {
    var aFiles = oEvent.target.files;
    var oFiles = {files: aFiles};
    var aCollectionIds = this.state.collectionIds;
    var sSelectedKlassId = this.state.selectedKlassId;

    aCollectionIds = this.props.activeCollectionId ? [this.props.activeCollectionId] : aCollectionIds;
    EventBus.dispatch(oEvents.CONTENT_ASSET_BULK_UPLOAD_EVENT, "assetBulkUpload", oFiles, aCollectionIds, sSelectedKlassId, this.props.filterContext);
  };

  closeCollectionsDialog = () => {
    this.setState({
      collectionIds: [],
      selectedKlassId: "",
      isStateInvalid: false
    });

    EventBus.dispatch(oEvents.BULK_UPLOAD_TO_COLLECTION_CLOSED);
  };

  handleButtonClick = (sButtonId) => {
    if(sButtonId === "next") {
      this.handleNextButtonClicked();
    }
    else{
      this.closeCollectionsDialog();
    }
  };

  handleAssetBulkUploadClicked = (sContext) => {
    var oNewCollectionLabel = this.newCollectionLabel.current || {};
    var oListValidationMessage = this.listValidationMessage.current || {};
    var oLabelValidationMessage = this.labelValidationMessage.current || {};
    if (sContext == "existing") {
      oLabelValidationMessage.innerHTML = "";
      if (CS.isEmpty(this.state.collectionIds)) {
        oListValidationMessage.innerHTML = getTranslation().NO_COLLECTIONS_SELECTED;
        return;
      } else {
        oListValidationMessage.innerHTML = "";
      }

      oNewCollectionLabel.value = "";
    } else if (sContext == "new") {
      oListValidationMessage.innerHTML = "";
      var sLabel = CS.trim(oNewCollectionLabel.value);
      if (!sLabel) {
        oLabelValidationMessage.innerHTML = getTranslation().NEW_COLLECTION_LABEL_CANNOT_BE_EMPTY;
        return;
      } else {
        oLabelValidationMessage.innerHTML = "";
      }

      this.changeStateByProperty("collectionIds", []);
    } else if (sContext == "currentCollection") {
      oNewCollectionLabel.value = "";
    }
    !CS.isEmpty(this.assetBulkUpload.current) && this.assetBulkUpload.current.click();
  };

  handleUploadToCollectionClicked = () => {
    if (this.props.activeCollectionId) {
      this.handleAssetBulkUploadClicked("currentCollection");
    } else {
      EventBus.dispatch(oEvents.BULK_UPLOAD_TO_COLLECTION_CLICKED);
    }
  };

  handleCollectionItemClicked = (sCollectionId) => {
    var aCheckedIds = this.state.collectionIds;
    if (CS.includes(aCheckedIds, sCollectionId)) {
      CS.remove(aCheckedIds, function (sId) {
        return sId == sCollectionId;
      });
    } else {
      aCheckedIds.push(sCollectionId);
    }

    this.changeStateByProperty("collectionIds", aCheckedIds);
  };

  handleKlassSelected = (sId, oReferencedData) => {
    this.setState({
      isStateInvalid: false,
      selectedKlassId: sId,
      selectedClass: oReferencedData[sId]
    });
  };

  getAllowedKlassesLazyMSSView = () => {
    var aSelectedKlassIds = this.state.selectedKlassId ? [this.state.selectedKlassId] : [];

    return (<div className={"bulkUploadSelectClassContainer"}>
      <LazyMSSView
          selectedItems={aSelectedKlassIds}
          context={""}
          referencedData={{}}
          requestResponseInfo={this.props.requestResponseData}
          onApply={this.handleKlassSelected}
          disabled={false}
          cannotRemove={true}
          isMultiselect={false}
      />
    </div>);
  };

  getCollectionTableView = () => {
    var _this = this;
    var aCollectionList = this.props.collectionList || [];
    var aCheckedIds = this.state.collectionIds;
    var aCollectionItemViews = [];
    var sSearchText = this.state.searchText;

    CS.forEach(aCollectionList, function (oCollection) {
      var bIsChecked = CS.includes(aCheckedIds, oCollection.id);
      var sLabel = CS.getLabelOrCode(oCollection);
      if(sLabel.toLowerCase().includes(sSearchText.toLowerCase())) {
        var oCollectionView = (
            <div className="collectionItemContainer"
                 onClick={_this.handleCollectionItemClicked.bind(_this, oCollection.id)}>
              <input type="checkbox" checked={bIsChecked}/>
              <span className="collectionItemLabel">{CS.getLabelOrCode(oCollection)}</span>
            </div>
        );
        aCollectionItemViews.push(oCollectionView);
      }
    });

    return aCollectionItemViews;
  };

  handleNextButtonClicked = () => {
    var sSelectedKlassId = this.state.selectedKlassId;

    if(CS.isEmpty(sSelectedKlassId)){
      this.changeStateByProperty("isStateInvalid", true);
    } else {
      !CS.isEmpty(this.assetBulkUpload.current) && this.assetBulkUpload.current.click();
    }
  };

  handleCollectionSearchTextChanged = (oEvent) => {
    var sNewSearchValue = oEvent.target.value;
    this.setState({
      searchText: sNewSearchValue
    });
  };

  getCollectionsDialog = () => {
    var aCollectionItemViews = this.getCollectionTableView();
    var oKlassView = this.getAllowedKlassesLazyMSSView();
    var oStateInvalidView = this.state.isStateInvalid ?
                            (<div className="selectClassInvalidState">{ViewUtils.getDecodedTranslation(getTranslation().PLEASE_SELECT_CONTEXT_TO_PROCEED,{context : getTranslation().CLASS})}</div>) : null;
    var aButtonData=[
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      },
      {
        id: "next",
        label: getTranslation().NEXT,
        isFlat: false
      }
    ];

    var oChildren = ( <div className="bulkUploadAssetDialog">

          <div className="selectKlassContainer">
            <div className="selectKlassLabel">{getTranslation().SELECT_CLASS}</div>
            <div className="selectKlassBody">{oKlassView}</div>
            {oStateInvalidView}
          </div>

          <div className="selectCollectionsContainer">
            <div className="selectCollectionsLabel">
              <div className="collectionLabelWrapper">{getTranslation().SELECT_COLLECTIONS_OPTIONAL}</div>
              <div className="collectionListSearchContainer">
                <div className="collectionSearchWrapper">
                  <div className="searchInputIcon"></div>
                  <input className="collectionSearchInput"
                         type="text"
                         onChange={this.handleCollectionSearchTextChanged}
                         placeholder={getTranslation().SEARCH}/>
                </div>
              </div>
            </div>
            <div className="selectCollectionsBody">{!CS.isEmpty(aCollectionItemViews) ? aCollectionItemViews :
                                                    <NothingFoundView />
            }
            </div>
          </div>
    </div>);

    return (
        <CustomDialogView
            modal = {true}
            open = {true}
            onRequestClose={this.closeCollectionsDialog}
            title = {getTranslation().UPLOAD_ASSETS}
            buttonData = {aButtonData}
            buttonClickHandler= {this.handleButtonClick}>
          {oChildren}
        </CustomDialogView>
    );
  };

  getAllowedTypes () {
    let oAssetExtensions = this.props.assetExtensions;
    let aNativeKlassNatureTypes = ["imageAsset", "videoAsset", "documentAsset"];
    let oSelectedKlass = this.state.selectedClass;
    return CS.includes(aNativeKlassNatureTypes, oSelectedKlass.natureType) ? oAssetExtensions[oSelectedKlass.natureType].join(", ") : oAssetExtensions.allAssets.join(", ");
  }

  render() {

    var bMultiple = true;
    var oCollectionsDialog = this.props.showCollectionDialog ? this.getCollectionsDialog() : null;
    var oBulkUploadView = (
        <TooltipView placement={"top"} label={getTranslation().BULK_UPLOAD_CONTENT_MENU_ITEM_TITLE}>
          <div className="toolbarItemNew assetBulkUpload" onClick={this.handleUploadToCollectionClicked}>
          </div>
        </TooltipView>
    );

   let sAllowedTypes = !CS.isEmpty(this.state.selectedKlassId) ? this.getAllowedTypes() : "";

    return (
        <div className="bulkUploadContainer">
          {oBulkUploadView}
          {oCollectionsDialog}
          <input style={{"visibility": "hidden"}}
                 ref={this.assetBulkUpload}
                 onChange={this.handleBulkUploadControlClicked}
                 type="file"
                 accept={sAllowedTypes}
                 multiple={bMultiple}/>
        </div>
    );
  }
}

export const view = BulkUploadButtonView;
export const events = oEvents;
