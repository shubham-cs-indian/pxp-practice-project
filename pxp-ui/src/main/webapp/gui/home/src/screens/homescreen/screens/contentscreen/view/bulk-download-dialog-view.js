import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../../../../libraries/cs';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
const  getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  BULK_DOWNLOAD_DIALOG_CHECKBOX_CLICKED: "bulk_download_dialog_checkbox_clicked",
  BULK_DOWNLOAD_DIALOG_EXPAND_BUTTON_CLICKED: "bulk_download_dialog_expand_clicked",
  BULK_DOWNLOAD_DIALOG_ACTION_BUTTON_CLICKED: "bulk_download_dialog_action_button_clicked",
  BULK_DOWNLOAD_DIALOG_FIXED_SECTION_VALUE_CHANGED: "bulk_download_dialog_fixed_section_value_changed",
  BULK_DOWNLOAD_DIALOG_CHILD_ELEMENT_VALUE_CHANGED: "bulk_download_dialog_child_element_value_changed",
};

const oPropTypes = {
  dialogData: ReactPropTypes.array,
  toggleButtonData: ReactPropTypes.object,
  isFolderByAsset: ReactPropTypes.bool,
  fixedSectionData: ReactPropTypes.object,
  downloadAsExtraData: ReactPropTypes.object,
  buttonExtraData: ReactPropTypes.object,
  invalidRowIds: ReactPropTypes.array,
};

// @CS.SafeComponent
class BulkDownloadDialogView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      id: "",
      textValue: "",
    }
  }

  static propTypes = oPropTypes;

  componentWillReceiveProps (oNextProps) {
    if (CS.isNotEmpty(this.state.textValue)) {
      this.setState({id: "", textValue: ""});
    }
  }

  handleButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_ACTION_BUTTON_CLICKED, sButtonId);
  };

  closeDownloadDialog = () => {
    this.handleButtonClicked("cancel");
  };

  handleCheckboxClicked = (sId, iIndex, sClassId) => {
    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_CHECKBOX_CLICKED, sId, iIndex, sClassId);
  };

  handleExpandButtonClicked = (sId, iIndex) => {
    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_EXPAND_BUTTON_CLICKED, sId, iIndex);
  };

  handleChildElementTextChanged = (sId, oEvent) => {
    let sValue = oEvent.target.value;
    this.setState({id: sId, textValue: sValue});
  };

  handleChildElementTextValueUpdated = (sClassId, iParentIndex) => {
    let sId = this.state.id;
    let sValue = this.state.textValue;

    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_CHILD_ELEMENT_VALUE_CHANGED, sId, sValue, sClassId, iParentIndex);
  };


  handleTextValueUpdated = (sId) => {
    let oAssetDownloadData = {
      id: sId,
      downloadFileName: this.props.fixedSectionData.downloadFileName,
      downloadComments: this.props.fixedSectionData.downloadComment
    };
    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_FIXED_SECTION_VALUE_CHANGED, "text" ,oAssetDownloadData);
  };

  handleDownloadAsTextChanged = (oEvent) => {
    this.props.fixedSectionData.downloadFileName = oEvent.target.value;
    this.setState({id: ""});
  };

  handleAddComment = (oEvent) => {
    this.props.fixedSectionData.downloadComment = oEvent.target.value;
    this.setState({id: ""});
  };

  handleToggleButtonValueChanged = () => {
    EventBus.dispatch(oEvents.BULK_DOWNLOAD_DIALOG_FIXED_SECTION_VALUE_CHANGED, "toggle", {});
  };

  getToggleButton = () => {
    let fHandleValueChanged = null;
    let oToggleButtonData = this.props.toggleButtonData;
    let bIsToggleButtonOn = oToggleButtonData.isToggleButtonOn;
    let sButtonClass = "downloadDialogYesNoSwitchButton ";
    if (bIsToggleButtonOn) {
      sButtonClass += "yes";
    } else {
      sButtonClass += "no";
    }
    if (oToggleButtonData.disabled) {
      sButtonClass += " disabled";
    } else {
      fHandleValueChanged = this.handleToggleButtonValueChanged;
    }

    return (
        <div className="downloadDialogToggleButtonContainer">
          <div className="downloadDialogYesNoView">
            <div className="downloadDialogYesNoSwitch" onClick={fHandleValueChanged}>
              <div className={sButtonClass}>
                <div className="textSection yes"></div>
                <div className={"circleSection"}></div>
                <div className="textSection no "></div>
              </div>
            </div>
          </div>
           <div className={"downloadDialogToggleButtonLabel"}>{oToggleButtonData.toggleButtonLabel}</div>
         </div>
    );
  };


  getDownloadDialog = () => {
    let oExtraButtonData = this.props.buttonExtraData;
    let bIsDownloadButtonDisabled = oExtraButtonData.isDownloadButtonDisabled;
    //Prepare Dialog Button Data.
    let aButtonData = [
      {
        id: "cancel",
        className: "downloadDialogCancelButton",
        label: getTranslation().CANCEL,
        isFlat: true,
      },
      {
        id: "download",
        className: "downloadDialogDownloadButton",
        label: getTranslation().DOWNLOAD,
        isFlat: false,
        isNotActive: bIsDownloadButtonDisabled,
      }
    ];

    let sClassName = "bulkDownloadAssetDialog";
    let sButtonLabel = getTranslation().DOWNLOAD;

    let oDialogView = (
        <div className={sClassName}>
          <div className="downloadDialogScrollableSection">
            {this.getParentListView()}
          </div>
          {this.getFixedSectionView()}
        </div>
    );

    let oContentStyle = {height: "80%", width: '100%', maxWidth: "1080px", minWidth: "800px"};

    return (
        <CustomDialogView
            modal={true}
            open={true}
            onRequestClose={this.closeDownloadDialog}
            title={sButtonLabel}
            buttonData={aButtonData}
            buttonClickHandler={this.handleButtonClicked}
            contentStyle={oContentStyle}
            contentClassName={"bulkCustomDownloadDialogView"}>
          {oDialogView}
        </CustomDialogView>
    );
  };

  getFixedSectionView = () => {
    let oFixedSectionData = this.props.fixedSectionData;
    let oDownloadAsExtraData = this.props.downloadAsExtraData;
    let bIsDownloadAsDisabled = oFixedSectionData.isDownloadAsDisabled;
    let sDownloadFileNameErrorMsg = oDownloadAsExtraData.downloadFileNameErrorMsg;
    let bIsDownloadAsError = oDownloadAsExtraData.isDownloadAsError;
    let bIsAssetByFolderDisabled = oFixedSectionData.isFolderByAssetDisabled;
    let bIsCommentSectionDisabled = oFixedSectionData.isCommentSectionDisabled;
    let bisHideDownloadAsExtension = oFixedSectionData.isHideDownloadAsExtension;
    let sDownloadAsInputClass = "downloadAsInput";
    let sDownloadAsLeftPanelClassName = "downloadAsSectionLeftPanel";
    let sCommentSectionLeftPanelClassName = "commentSectionLeftPanel";
    sDownloadAsLeftPanelClassName += bIsDownloadAsDisabled ? " disabled" : "";
    sCommentSectionLeftPanelClassName += bIsCommentSectionDisabled ? " disabled": "";
    if (bIsDownloadAsError) {
      sDownloadAsInputClass = sDownloadAsInputClass + " invalid";
    }
    return (
        <div className="downloadDialogFixedSection">
          <div className="downloadAsSectionContainer">
            <div className={sDownloadAsLeftPanelClassName}>
              <div className="downloadAsLabel">{getTranslation().DOWNLOAD_AS}</div>
              <input className={sDownloadAsInputClass} type={"text"} value={oFixedSectionData.downloadFileName}
                     onChange={this.handleDownloadAsTextChanged}
                     onBlur={this.handleTextValueUpdated.bind(this, "download")}/>
              <div className="downloadAsExtension">{bisHideDownloadAsExtension ? "" : ".zip"}</div>
              {bIsDownloadAsError && this.getInvalidClassView(sDownloadFileNameErrorMsg)}
            </div>
            <div className="downloadAsSectionRightPanel">
              {this.getSeparateFolderCheckboxView(bIsAssetByFolderDisabled)}
              {this.getToggleButton()}
            </div>
          </div>
          <div className="commentSection">
            <div className={sCommentSectionLeftPanelClassName}>
              <div className="commentSectionLabel">{getTranslation().COMMENT}</div>
              <input className={"commentSectionInput"} type={"text"} maxLength={999}
                     value={oFixedSectionData.downloadComment}
                     onChange={this.handleAddComment}
                     onBlur={this.handleTextValueUpdated.bind(this, "comment")}/>
            </div>
            <div className="commentSectionRightPanel">
              {this.getTotalAssetsSizeView(oFixedSectionData.totalSize, oFixedSectionData.totalCount)}
            </div>
          </div>
        </div>);
  };

  getTotalAssetsSizeView = (iSize, iTotalAssets) => {
    let oSizeUnitModel = this.covertFileSizeFromBytes(iSize);
    let sSizeWithUnit = oSizeUnitModel.size + " " + oSizeUnitModel.unit;

    return (
        <div className="totalAssetsSizeSection">
          <div className="totalAssetsLabel">{getTranslation().TOTAL}:</div>
          <div className="totalSelectedAssetsLabel">{getTranslation().ASSETS} - </div>
          <div className="totalSelectedAssetsValue">{iTotalAssets}</div>
          <div className="totalSelectedAssetsSizeLabel">{getTranslation().SIZE} - </div>
          <div className="totalSelectedAssetsSizeValue">{sSizeWithUnit}</div>
        </div>
    );
  };

  getSeparateFolderCheckboxView = (bIsAssetByFolderDisabled = false) => {
    let bIsChecked = this.props.isFolderByAsset;
    let sAssetByFolderContainerClassName = "separateFolderContainer";
    sAssetByFolderContainerClassName += bIsAssetByFolderDisabled ? " disabled" : "";
    let sCheckboxClassName = "separateFolderCheckbox";
    sCheckboxClassName += bIsChecked ? " isChecked" : "";

    return (
        <div className={sAssetByFolderContainerClassName}>
          <div className={sCheckboxClassName} onClick={this.handleCheckboxClicked.bind(this, "folderByAssetCheckbox")}></div>
          <div className="separateFolderViewLabel">{getTranslation().CREATE_SEPARATE_FOLDER_FOR_EACH_ASSET}</div>
        </div>
    );
  };

  getParentListView = () => {
    let _this = this;
    let aListItemView = [];
    let aListViewModel = _this.props.downloadData;

    CS.forEach(aListViewModel, function (oModel, iIndex) {
      aListItemView.push(_this.getParentElementView(oModel, iIndex));
    });

    return aListItemView;
  };

  //Preparing view for selected asset instance.
  getParentElementView = (oListModel, iParentIndex) => {
    let _this = this;
    let oInvalidClassView = null;
    let oListViewContainerStyle = null;
    let aExpandedChildView = [];
    let oProperties = oListModel.properties;
    let sIconClassName = "downloadDialogListViewIcon";
    let sContainerClassName = "downloadDialogListViewContainer";
    let sId = oProperties.id;
    let oDownloadDialogListNameView = _this.getDownloadDialogListNameView(oListModel);
    let bIsExpanded = oProperties.isExpanded;
    let bIsPartialChecked = oProperties.isPartialChecked;
    let bIsInvalid = oProperties.hasOwnProperty('isInvalid') ? oProperties.isInvalid : false;
    let sInvalidMessage = bIsInvalid ? oProperties.invalidMessage : "";
    let bIsExpandButtonDisabled = oProperties.disableExpandButton;
    let bIsChecked = oListModel.isChecked;
    let bIsCheckboxDisabled = oProperties.isCheckboxDisabled;
    let aInvalidRowIds = this.props.invalidRowIds;
    let bIsInvalidIdPresent = CS.includes(aInvalidRowIds, oProperties.id);
    let oCheckboxStyle = {
      "height": "45px",
      "paddingTop": "18px",
      "paddingBottom": "18px",
    };

    let oIcon = (
        <div className={sIconClassName}>
          <ImageFitToContainerView imageSrc={oListModel.icon}/>
        </div>
    );

    if (bIsExpanded) {
      sContainerClassName += " expanded";
      let aClassChildren = oProperties.children;
      let iLength = aClassChildren.length;
      CS.forEach(aClassChildren, function (oChild, iChildIndex) {
        aExpandedChildView.push(_this.getClassChildView(oChild, iParentIndex, iChildIndex, iLength));
      });
    } else {
      if (bIsInvalid && bIsInvalidIdPresent) {
        oInvalidClassView = _this.getInvalidClassView(sInvalidMessage, {"height": "17px", "backgroundSize": "18px"});
        oListViewContainerStyle = {
          "background": "#FFE8E8 0 0 no-repeat padding-box",
          "border": "1px solid #D53C3C",
          "borderRadius": "3px",
        };
      }
    }

    let oRightPanelView = (
        <div className="downloadDialogParentListRightPanelWrapper">
          {_this.getExpandButtonView(bIsExpanded, sId, iParentIndex, bIsExpandButtonDisabled)}
          {_this.getSizeWithUnitView(oProperties.selectedContentSize, oProperties.totalSize)}
          {_this.getSelectedAssetsCountView(oProperties.selectedContentCount, oProperties.totalCount)}
          {oInvalidClassView}
        </div>
    );

    return (
        <div className={sContainerClassName} style={oListViewContainerStyle}>
          <div className="downloadDialogListViewWrapper">
            {_this.getCheckboxView(bIsCheckboxDisabled, bIsChecked, oCheckboxStyle, sId, iParentIndex ,bIsPartialChecked)}
            {oIcon}
            <div className="downloadDialogListWrapper">
              {oDownloadDialogListNameView}
              {oRightPanelView}
            </div>
          </div>
          {aExpandedChildView}
        </div>
    );
  };

  getExpandButtonView = (bIsExpanded, sId, iIndex, bIsExpandButtonDisabled = false) => {
    let sClassName = "expandIcon";
    sClassName += bIsExpanded ? " expanded" : bIsExpandButtonDisabled ? " disabled" : "";

    return (<div className={sClassName} onClick={this.handleExpandButtonClicked.bind(this, sId, iIndex)}></div>);
  };

  getCheckboxView = (bIsDisabled = false, bIsChecked = true, oStyle, sId, iIndex, bIsHalfChecked = false, sClassId) => {
    let sCheckboxClassName = "downloadDialogCheckbox ";
    sCheckboxClassName += bIsDisabled ? " disabled" : bIsChecked ? "isChecked" : bIsHalfChecked ? "halfChecked" : "";


    return (
        <div className={sCheckboxClassName} style={oStyle} onClick={this.handleCheckboxClicked.bind(this, sId, iIndex, sClassId)}></div>
    );
  };

  //Return:- Name and class view of asset instance.
  getDownloadDialogListNameView = (oListModel) => {
    let sLabel = CS.getLabelOrCode(oListModel);
    let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;

    return (
        <div className="downloadDialogListViewName">
          <div className="downloadDialogListLabel" title={sLabel}>{sLabel}</div>
          <div className="downloadDialogListId" key={sId}>{sId}</div>
        </div>
    );
  };

  getSizeWithUnitView = (iSelectedContentSize, iTotalSize) => {
    let oSelectedContentSizeUnitModel = this.covertFileSizeFromBytes(iSelectedContentSize);
    let oTotalContentSizeUnitModel = this.covertFileSizeFromBytes(iTotalSize);
    let sSizeValueWithUnit = this.getSlashSeparatedValues(oSelectedContentSizeUnitModel.size, oTotalContentSizeUnitModel.size
         ,oSelectedContentSizeUnitModel.unit, oTotalContentSizeUnitModel.unit);
    let sSelectedAssetsSizeClassName = "selectedAssetsSize";

    return (
        <div className={sSelectedAssetsSizeClassName}>
          <span className="selectedAssetsSizeKey">{getTranslation().SIZE} : </span>
          <span className="selectedAssetsSizeValue">{sSizeValueWithUnit}</span>
        </div>
    );
  };

  getSelectedAssetsCountView = (iSelectedContentCount, iTotalCount) => {
    let sSelectedAssetsCountClassName = "selectedAssetsCount";
    let sCountValue = this.getSlashSeparatedValues(iSelectedContentCount, iTotalCount);

    return (
        <div className={sSelectedAssetsCountClassName}>
          <span className="selectedAssetsCountKey">{getTranslation().SELECTED_ASSETS} : </span>
          <span className="selectedAssetsCountValue">{sCountValue}</span>
        </div>
    );
  };

  //Return:- TIV class view when parent is expanded.
  getClassChildView = (oChild, iParentIndex, iChildIndex, iLength) => {
    let _this = this;
    let sId = oChild.id;
    let sLabel = oChild.label;
    let bIsChecked = oChild.isChecked;
    let bIsPartialChecked = oChild.isPartialChecked;
    let bIsExpanded = oChild.isExpanded;
    let aChildrenModel = oChild.children;
    let bShowBorderBottom = oChild.hasOwnProperty('showBorderBottom') ? oChild.showBorderBottom : false;
    let aChildElementView = [];
    let oChildElementView = null;
    let oStyle = {"height": "25px", "marginLeft": "10px"};
    let iChildModelLength = aChildrenModel.length;
    let bIsCheckboxDisabled = oChild.isCheckboxDisabled;
    bIsExpanded && CS.forEach(aChildrenModel, function (oChild, iIndex) {
      let oChildElementStyle = (iIndex <= iChildModelLength - 1) ? {
        "borderBottom": "1px solid #D9D9D9",
        "borderRadius": "2px"
      } : {};
      aChildElementView.push(_this.getChildElementView(oChild, oChildElementStyle, iParentIndex, sId));
    });

    if (bIsExpanded && aChildElementView) {

      oChildElementView = (
          <div className="childElementView">
            {aChildElementView}
          </div>
      );
    }
    let oParentClassStyle = (iLength - 1 == iChildIndex) && bIsExpanded ? {paddingBottom: "10px"} : {};
    if (bShowBorderBottom) {
      oParentClassStyle.borderBottom = "1px solid #D9D9D9";
    }

    return (
        <div className="downloadDialogClassChildContainer" style={oParentClassStyle}>
          <div className="downloadDialogClassChildWrapper">
            {this.getCheckboxView(bIsCheckboxDisabled, bIsChecked, oStyle, sId, iParentIndex, bIsPartialChecked)}
            <div className="downloadDialogClassChildLabel">{sLabel}</div>
            {this.getExpandButtonView(bIsExpanded, sId, iParentIndex)}
          </div>
          {oChildElementView}
        </div>
    );
  };

  //Return:- TIV Class instance view.
  getChildElementView = (oChildData, oChildElementStyle, iParentIndex, sClassId) => {
    let oInvalidClassView = null;
    let oInvalidClassStyle = null;
    let oRightSideElementsView = null;
    let sId = oChildData.id;
    let sExtension = oChildData.extension;
    let bIsChecked = oChildData.isChecked;
    let sChildElementContainerClassName = "downloadDialogChildElementContainer";
    sChildElementContainerClassName += oChildData.isDisabled ? " disabled" : "";
    let sLabel = (this.state.id == sId) ? this.state.textValue : oChildData.label;
    let bIsInvalid = oChildData.hasOwnProperty('isInvalid') ? oChildData.isInvalid : false;
    let sInvalidMessage = bIsInvalid ? oChildData.invalidMessage : "";
    let sInputViewClassName = "downloadDialogChildElementInput";
    sInputViewClassName += !bIsChecked ? " unChecked" : "";
    let oCheckboxStyle = {"height": "24px"};

    if (bIsChecked && bIsInvalid) {
      sInputViewClassName += " invalid";
      oInvalidClassView = this.getInvalidClassView(sInvalidMessage, {"backgroundSize": "18px"});
      oInvalidClassStyle = {
        "background": "#FFE8E8 0 0 no-repeat padding-box",
        "border": "1px solid #D53C3C",
        "borderRadius": "1px",
      };
    } else if(oChildData.size !== 0){
      let oSizeUnitModel = this.covertFileSizeFromBytes(oChildData.size);

      oRightSideElementsView = (
          <div className="downloadDialogChildRightSideElementsWrapper">
            <span className="childExtension">.{sExtension}</span>
            <span className="childSizeWithUnit">{oSizeUnitModel.size + oSizeUnitModel.unit}</span>
          </div>
      );
    }

    return (
        <div className={sChildElementContainerClassName} style={(bIsChecked && bIsInvalid) ? oInvalidClassStyle : oChildElementStyle}>
          {this.getCheckboxView(false, bIsChecked, oCheckboxStyle, sId, iParentIndex, false, sClassId)}
          <input className={sInputViewClassName} type={"text"} value={sLabel}
                 onChange={this.handleChildElementTextChanged.bind(this, sId)}
                 onBlur={this.handleChildElementTextValueUpdated.bind(this, sClassId, iParentIndex)}/>
          {(bIsChecked && bIsInvalid) ? oInvalidClassView : oRightSideElementsView}
        </div>
    );
  };

  getSlashSeparatedValues = (iSelectedSize, iTotalSize, sSelectedSizeUnit, sTotalSizeUnit) => {
    if (CS.isNotEmpty(sSelectedSizeUnit)) {
      iTotalSize += " " + sTotalSizeUnit;
      iSelectedSize += " " + sSelectedSizeUnit;
    }

    return iSelectedSize + "/" + iTotalSize;
  };

  //Invalid icon with style view;
  getInvalidClassView = (sMessage, oStyle = {}) => {
    oStyle.float = "right";
    return (
        <TooltipView placement={"top"} label={sMessage}>
          <div className="downloadDialogInvalidClassViewContainer" style={oStyle}>
          </div>
        </TooltipView>
    );
  };

  //This method converts bytes to any unit based on its value divisible by 1024.
  covertFileSizeFromBytes = (iSizeInBytes) => {
    let iIndex = 0;
    const aUnits = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    while (iSizeInBytes >= 1024 && ++iIndex) {
      iSizeInBytes = iSizeInBytes / 1024;
    }

    let oSizeUnitModel = {
      size: (iSizeInBytes.toFixed(iSizeInBytes < 100 && iIndex > 0 ? 2 : 0)) * 1,
      unit: aUnits[iIndex],
    };

    return oSizeUnitModel;
  };

  render () {
    let oDownloadDialog = this.getDownloadDialog();

    return (
        <div className="bulkDownloadContainer">
          {oDownloadDialog}
        </div>
    );
  }
}

export const view = BulkDownloadDialogView;
export const events = oEvents;
