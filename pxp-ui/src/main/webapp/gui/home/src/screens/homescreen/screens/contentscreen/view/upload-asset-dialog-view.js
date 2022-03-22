import CS from '../../../../../libraries/cs';
import React from 'react';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as MSSView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import AssetUploadContextDictionary from '../../../../../commonmodule/tack/asset-upload-context-dictionary';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import ReactPropTypes from 'prop-types';

const oEvents = {
  UPLOAD_ASSET_DIALOG_BUTTON_CLICKED: "upload_asset_dialog_button_clicked",
  UPLOAD_ASSET_BUTTON_CLICKED: "Upload_Asset_Button_Clicked",

};

const oPropTypes = {
  assetClassList: ReactPropTypes.array,
  assetExtensions: ReactPropTypes.object,
  relationshipSideId: ReactPropTypes.string,
  context: ReactPropTypes.string,
  filterContext: ReactPropTypes.object,
  showUploadButton: ReactPropTypes.bool,
  showBulkUploadDialog: ReactPropTypes.bool,
  requestURL: ReactPropTypes.string,
};

// @CS.SafeComponent
class UploadAssetDialogView extends React.Component {

  constructor(props) {
    super(props);
    this.assetBulkUpload = React.createRef();
  }

  state = {
    selectedKlassId: "",
  };

  static propTypes = oPropTypes;

  handleKlassSelected = (sId) => {
    this.setState({
      selectedKlassId: sId[0]
    });
  };

  handleButtonClick = (sButtonId) => {
    let sSelectedKlassId = this.state.selectedKlassId;
    let {context: sContext} = this.props;
    let sRelationshipSideId = this.props.relationshipSideId;

    if (sButtonId === "ok" && (sContext == AssetUploadContextDictionary.RELATIONSHIP_SECTION || sContext == AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST)) {
      CS.isNotEmpty(this.assetBulkUpload.current) && this.assetBulkUpload.current.click();
    }
    else {
      this.setState({
        selectedKlassId: "",
      });
      EventBus.dispatch(oEvents.UPLOAD_ASSET_DIALOG_BUTTON_CLICKED, sButtonId, sContext, sSelectedKlassId, sRelationshipSideId);
    }

  };

  // Icon clicked
  handleAssetUploadClicked = () => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.UPLOAD_ASSET_BUTTON_CLICKED, sContext, this.props.filterContext);
  };

  handleBulkUploadControlClicked = (oEvent) => {
    let aFiles = oEvent.target.files;
    let oFiles = {files: aFiles};
    let sSelectedKlassId = this.state.selectedKlassId;
    let sContext = this.props.context;

    EventBus.dispatch(oEvents.UPLOAD_ASSET_DIALOG_BUTTON_CLICKED, "ok", sContext, sSelectedKlassId, this.props.relationshipSideId, oFiles);
  };

  getAllowedKlassesMSSView = () => {
    if (!CS.isEmpty(this.props.requestURL)) {
      let oRequestResponseData = {
        responsePath: ["success"],
        requestType: "customType",
        requestURL: this.props.requestURL,
        customRequestModel: {
          moduleId: "mammodule"
        }
      };

      let aSelectedKlassIds = this.state.selectedKlassId ? [this.state.selectedKlassId] : [];
      return (<div className={"bulkUploadSelectClassContainer"}>
        <LazyMSSView
            selectedItems={aSelectedKlassIds}
            context={""}
            referencedData={{}}
            requestResponseInfo={oRequestResponseData}
            onApply={this.handleKlassSelected}
            disabled={false}
            cannotRemove={true}
            isMultiselect={false}
        />
      </div>);
    }
    else {
      let aAllowedKlasses = this.props.assetClassList;
      let aListForMSS = [];
      let aSelectedKlassIds = this.state.selectedKlassId ? [this.state.selectedKlassId] : [];
      CS.forEach(aAllowedKlasses, function (oItem) {
        aListForMSS.push({
          id: oItem.id,
          label: CS.getLabelOrCode(oItem)
        })
      });

      return (
          <div className="klassSelectorMSSContainer">
            <MSSView
                disabled={false}
                items={aListForMSS}
                selectedItems={aSelectedKlassIds}
                isMultiSelect={false}
                cannotRemove={true}
                onApply={this.handleKlassSelected}
            />
          </div>);
    }
    ;
  }

  getAssetUploadDialog = () => {
    let oKlassMSSView = this.getAllowedKlassesMSSView();
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      },
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false
      }
    ];
    let fButtonHandler = this.handleButtonClick;
    let oChildren = (<div className="bulkUploadAssetDialog">

      <div className="selectKlassContainer">
        <div className="selectKlassLabel">{getTranslation().SELECT_CLASS}</div>
        <div className="selectKlassBody">{oKlassMSSView}</div>
      </div>
    </div>);

    return (
        <CustomDialogView
            modal={true}
            open={true}
            onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
            title={getTranslation().UPLOAD_ASSETS}
            buttonData={aButtonData}
            buttonClickHandler={fButtonHandler}>
          {oChildren}
        </CustomDialogView>
    );
  };

  getAllowedTypes = () => {
    let oAssetExtensions = this.props.assetExtensions;
    let aAllowedKlasses = this.props.assetClassList;
    let sSelectedKlassId = this.state.selectedKlassId;
    let aNativeKlassNatureTypes = ["imageAsset", "videoAsset", "documentAsset"];
    let oSelectedKlass = CS.find(aAllowedKlasses, {'id': sSelectedKlassId});

    return CS.includes(aNativeKlassNatureTypes, oSelectedKlass.natureType) ? oAssetExtensions[oSelectedKlass.natureType].join(", ") : oAssetExtensions.allAssets.join(", ");
  };

  render () {

    let oDialogView = this.props.showBulkUploadDialog ? this.getAssetUploadDialog() : null;
    let oBulkUploadButtonView = null;
    let oSelectFileView = null;

    if (this.props.showUploadButton) {
      oBulkUploadButtonView = (<TooltipView placement={"top"}
                                            label={getTranslation().BULK_UPLOAD_CONTENT_MENU_ITEM_TITLE}>
            <div className="toolbarItemNew assetBulkUpload"
                 onClick={this.handleAssetUploadClicked}>
            </div>
          </TooltipView>
      );

      let sAllowedTypes = CS.isNotEmpty(this.state.selectedKlassId) ? this.getAllowedTypes() : "";

      oSelectFileView =
          <input style={{"visibility": "hidden"}}
                 ref={this.assetBulkUpload}
                 onChange={this.handleBulkUploadControlClicked}
                 type="file"
                 accept={sAllowedTypes}
                 multiple={true}/>
    }

    return (
        <div className="uploadAssetButtonContainer">
          {oBulkUploadButtonView}
          {oDialogView}
          {oSelectFileView}
        </div>
    );
  }
}

export const view = UploadAssetDialogView;
export const events = oEvents;
