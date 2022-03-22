import CS from '../../../../../libraries/cs';
import MasterUserListContext from '../../../../../commonmodule/HOC/master-user-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from '../view/utils/view-utils';
import ImageGalleryDialogViewModel from '../../../../../viewlibraries/imagegallerydialogview/model/image-gallery-dialog-view-model';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ImageGalleryDialogView } from '../../../../../viewlibraries/imagegallerydialogview/image-gallery-dialog-view';
import { view as ContentImageWrapperView } from './content-image-wrapper-view';
import { view as KpiSummaryTileView } from '../../../../../viewlibraries/kpisummaryview/kpi-summary-tile-view';
import { view as ImageSliderView } from '../../../../../viewlibraries/imagesliderview/image-slider-view';
import CommonUtils from '../../../../../commonmodule/util/common-utils';
import { view as CustomTextFieldView } from '../../../../../viewlibraries/customtextfieldview/custom-text-field-view';
import {getTranslations} from "../../../../../commonmodule/store/helper/translation-manager";
import copy from "copy-to-clipboard";
import alertify from "../../../../../commonmodule/store/custom-alertify-store";

const oEvents = {
  CONTENT_HEADER_CONTENT_NAME_BLURRED: "content_header_content_label_blurred",
  CONTENT_HEADER_CONTENT_NAME_EDIT_CLICKED: "content_header_entity_name_edit_clicked",
  IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED: "image_gallery_dialog_view_visibility_status_changed",
  CONTENT_INFORMATION_SIDEBAR_VIEW_HANDLE_SLIDER_IMAGE_CLICKED: "content_information_sidebar_view_handle_slider_image_clicked",
  CONTENT_INFORMATION_SIDEBAR_VIEW_SET_DEFAULT_ASSET_BUTTON_CLICKED: "content_information_sidebar_view_set_default_asset_button_clicked",
  CONTENT_INFORMATION_SIDEBAR_VIEW_DELETE_GENERATED_ASSET_LINK: "content_information_sidebar_view_delete_generated_asset_link",
};

const oPropTypes = {
  contentInfoData: ReactPropTypes.object,
  selectedImageId: ReactPropTypes.string,
  isDefaultIconVisible: ReactPropTypes.bool,
  masterUserList: ReactPropTypes.array
};

// @MasterUserListContext
// @CS.SafeComponent
class ContentInformationSidebarView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      showEventsView: false,
      imageGalleryDialogViewVisibilityStatus: false,
      imageCollapsed: false,
      sNameText:this.props.contentInfoData.entityName,
      sEntityId: this.props.contentInfoData.entityId
    };

    this.imageCollapseHandler = this.imageCollapseHandler.bind(this);
    this.handleEntityLabelBlur = this.handleEntityLabelBlur.bind(this);
    this.getCreationDetailsView = this.getCreationDetailsView.bind(this);
    this.handleEntityLabelKeyDown = this.handleEntityLabelKeyDown.bind(this);
    this.getImageGalleryDialogView = this.getImageGalleryDialogView.bind(this);
    this.handleImageGalleryDialogViewClose = this.handleImageGalleryDialogViewClose.bind(this);
    this.handleEntityNameEditButtonClicked = this.handleEntityNameEditButtonClicked.bind(this);
    this.handleSliderImageClicked = this.handleSliderImageClicked.bind(this);

    this.contentNameInput = React.createRef();
  };

  componentDidUpdate () {
    /** Update only if entity id changed.  **/
    if(this.props.contentInfoData.entityId != this.state.sEntityId){
      this.setState({
        sNameText: this.props.contentInfoData.entityName,
        sEntityId: this.props.contentInfoData.entityId
      });
    }

    if (this.props.contentInfoData.entityLabelEditable) {
      var oLabelInputDom = this.contentNameInput.current;

      if (oLabelInputDom) {
        oLabelInputDom.value = CommonUtils.getFilteredName(this.state.sNameText);
        oLabelInputDom.focus();
      }
    }
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    var oImageGalleryDialogViewData = oNextProps.contentInfoData.imageGalleryDialogViewData;
    var bIsImageDialogVisible = oImageGalleryDialogViewData.variantImageUploadDialogStatus;
    return {
      sNameText: oNextProps.contentInfoData.entityName,
      imageGalleryDialogViewVisibilityStatus: bIsImageDialogVisible
    };
  }

  handleEntityLabelKeyDown (oEvent) {
    if (oEvent.key == 'Enter' || oEvent.keyCode == 9 || oEvent.keyCode == 27) {
      oEvent.target.blur();
    }
  };


  handleEntityLabelBlur (sValue) {
    EventBus.dispatch(oEvents.CONTENT_HEADER_CONTENT_NAME_BLURRED, this, sValue);
  };

  handleEntityNameEditButtonClicked () {
    EventBus.dispatch(oEvents.CONTENT_HEADER_CONTENT_NAME_EDIT_CLICKED)
  };

  handleImageGalleryDialogViewClose (oEvent) {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED);
    this.setState({
      imageGalleryDialogViewVisibilityStatus: false
    });
  };

  imageCollapseHandler (bImageCollapsed) {
    this.setState({
      imageCollapsed: bImageCollapsed
    });
  };

  getCreationDetailsView () {
    let __props = this.props;
    let oEntityDetailData = __props.contentInfoData;
    var aMasterUserList = this.props.masterUserList;
    var oCreationDetails = oEntityDetailData.creationDetails;
    var oModificationDetails = oEntityDetailData.modificationDetails;

    var oCreatedBy = ViewUtils.getUserByUserName(oCreationDetails.createdBy);
    var sCreatedBy = ViewUtils.getUserDisplayName(oCreatedBy);
    var oCreatedOn = ViewUtils.getDateAttributeInDateTimeFormat(oCreationDetails.createdOn);

    var oLastModifiedBy = ViewUtils.getUserByUserName(oModificationDetails.lastModifiedBy);
    let sLastModifiedBy = ViewUtils.getUserDisplayName(oLastModifiedBy);
    var oLastModifiedOn = ViewUtils.getDateAttributeInDateTimeFormat(oModificationDetails.lastModifiedOn);

    return (
        <div className="contentInformationSectionWrapper">
          {getTranslation().INFO}
          <div className="contentInformationSection">
            <div className="createdByContainer">
              <span className="normal">{getTranslation().CREATED_BY}</span>
              <span className="highlighted">{sCreatedBy}</span>
              <span className="normal on">{getTranslation().ON}</span>
              <span className="highlighted">{oCreatedOn.date}</span>
              <span className="normal">{getTranslation().AT}</span>
              <span className="highlighted">{oCreatedOn.time}</span>
            </div>
            <div className="createdOnContainer">
              <span className="normal">{getTranslation().LAST_MODIFIED_BY}</span>
              <span className="highlighted">{sLastModifiedBy}</span>
              <span className="normal on">{getTranslation().ON}</span>
              <span className="highlighted">{oLastModifiedOn.date}</span>
              <span className="normal">{getTranslation().AT}</span>
              <span className="highlighted">{oLastModifiedOn.time}</span>
            </div>
          </div>
        </div>
    );
  };

  getImageGalleryDialogView () {
    let __props = this.props;
    let oEntityDetailData = __props.contentInfoData;
    var oImageGalleryDialogViewData = oEntityDetailData.imageGalleryDialogViewData;
    var bDialogOpenStatus = this.state.imageGalleryDialogViewVisibilityStatus;
    var oActiveEntityDetails = {};
    oActiveEntityDetails.id = oImageGalleryDialogViewData.entityId;
    oActiveEntityDetails.defaultAssetInstanceId = oImageGalleryDialogViewData.defaultAssetInstanceId;

    var sEntityId = '';
    if (!CS.isEmpty(oActiveEntityDetails)) {
      sEntityId = oActiveEntityDetails.id;
    }
    var aAssetList = oImageGalleryDialogViewData.allAssetList || [];
    var oProperties = {
      context: "EntityHeaderView",
      bodyClassName: "setDefaultAssetDetailsViewModel",
      title: getTranslation().SET_DEFAULT_IMAGE,
    };
    var bHasCloseIcon = true;
    var oImageGalleryDialogViewModel = new ImageGalleryDialogViewModel(sEntityId, oActiveEntityDetails,
        aAssetList, bDialogOpenStatus, bHasCloseIcon, oProperties);
    return (<ImageGalleryDialogView model={oImageGalleryDialogViewModel}
                                    onClose={CS.noop}/>);
  };

  getIconView () {
    let __props = this.props;
    let oEntityDetailData = __props.contentInfoData;
    let bCanDownload = __props.isDownloadIconVisible;
    let bCanShare = __props.isShareIconVisible;
    return (
        <ContentImageWrapperView
          headerPermission={__props.headerPermission}
          contentInfoData={oEntityDetailData}
          imageCollapseHandler={this.imageCollapseHandler}
          isDownloadIconVisible={bCanDownload}
          isShareIconVisible={bCanShare}
          assetDownloadData={oEntityDetailData.assetDownloadData}
        />
    );
  };

  getEntityInfoIconView (sLabel, sValue, sClass) {
    if(CS.isEmpty(sValue)) {
      return null;
    }

    let oToolTipView = (
        <div className="entityInformationIconTooltip">
          <div className="entityInformationIconLabel">{sLabel}</div>
          <div className="parentEntityInformationLabel">{sValue}</div>
        </div>);

    let oView = (
        <div className="entityInformationIconIndicator">
          <div className={"entityInformationIcon " + sClass}/>
        </div>);

    return (
        <TooltipView label={oToolTipView} placement="right">
          {oView}
        </TooltipView>
    );
  };

  handleSliderImageClicked (sSelectedImageId) {
    EventBus.dispatch(oEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_HANDLE_SLIDER_IMAGE_CLICKED, sSelectedImageId);
  }

  handleSetDefaultAssetButtonClicked () {
    let sAssetId = this.props.selectedImageId;
    let sDefaultAssetId = this.props.contentInfoData.imageGalleryDialogViewData.defaultAssetInstanceId;
    if (sAssetId != sDefaultAssetId) {
      EventBus.dispatch(oEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_SET_DEFAULT_ASSET_BUTTON_CLICKED, "", sAssetId, "EntityHeaderView");
    }
  }

  handleDeleteGeneratedLinkClick () {
    EventBus.dispatch(oEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_DELETE_GENERATED_ASSET_LINK);
  }

  handleCopyToClipboardClick (sAssetLink) {
    copy(sAssetLink) ? alertify.success(getTranslations().LINK_COPIED_SUCCESSFULLY) : alertify.error(getTranslation().COPY_TO_CLIPBOARD_FAILED);
  }

  render () {
    let __props = this.props;
    let oEntityDetailData = __props.contentInfoData;
    let oHeaderPermission = __props.headerPermission;
    let oEntityLabelView = null;
    let oEntityLabelEditButtonView = null;
    if (CS.isEmpty(oHeaderPermission) || oHeaderPermission.viewName) {
      if (oEntityDetailData.entityLabelEditable) {
          oEntityLabelView = (
              <CustomTextFieldView
                  value={this.state.sNameText}
                  onBlur={this.handleEntityLabelBlur}
                  forceBlur={true}
                  shouldFocus={true}
                  className={"contentNameInput"}
              >
              </CustomTextFieldView>);
      } else {
        let oEntityLabelTooltipDom = (<div style={{maxWidth: 235}} className="maxWidthTooltip">{oEntityDetailData.entityName}</div>);
        oEntityLabelView = (<TooltipView label={oEntityLabelTooltipDom} placement="bottom"><div className="contentName">{oEntityDetailData.entityName}</div></TooltipView>);
        oEntityLabelEditButtonView = ((CS.isEmpty(oHeaderPermission) || oHeaderPermission.canEditName) && !oEntityDetailData.isDisableHeader) ?
            (<div className="contentNameEditButton" onClick={this.handleEntityNameEditButtonClicked}></div>) : null;
      }
    }
    let bIsDetailedUnitView = oEntityDetailData.isDetailedUnitView;
    let oLifeCycleTagsView = oEntityDetailData.lifeCycleTagsView;
    let oTemplateListView =
        (<div className="contentInformationSectionWrapper">{getTranslation().TEMPLATES}
          <div className="contentInformationSection">{oEntityDetailData.templateListView}</div>
        </div>);

    let oCreationDetailsView = /*!bIsDetailedUnitView && (*/CS.isEmpty(oHeaderPermission) || oHeaderPermission.viewCreatedOn/*)*/ ? this.getCreationDetailsView() : null;

    var oCloneView = !bIsDetailedUnitView ? this.getEntityInfoIconView(getTranslation().CLONE_OF, oEntityDetailData.branchOfLabel, "cloneContextIcon") : null;
    var oLinkedVariantView = !bIsDetailedUnitView ? this.getEntityInfoIconView(getTranslation().VARIANT_OF, oEntityDetailData.variantOfLabel, "variantContextIcon") : null;

    var versionInfoClassName = this.state.imageCollapsed ? "versionInfoContainer collapsed" : "versionInfoContainer";
    var oRightSideIndicatorsViewContainer = !bIsDetailedUnitView ? (
        <div className={versionInfoClassName}>
          {oCloneView}
          {oLinkedVariantView}
        </div>
    ) : null;

    let oMultiClassificationDOM = oEntityDetailData.multiClassificationView;
    let oKpiSummaryViewData = oEntityDetailData.kpiSummaryViewData;
    let oKPISummaryView = oKpiSummaryViewData.showSummaryDataInHeader ? (<KpiSummaryTileView {...oKpiSummaryViewData}/>) : null;
    let oImageGalleryDialogViewData = this.props.contentInfoData.imageGalleryDialogViewData;

    let sSliderImageWrapperDimension = "50px";
    let sScrollButtonWidth = "20px";
    let iCountOfShiftingImagesInSlider = 4;

    let oImageSliderView = ((oImageGalleryDialogViewData.allAssetList.length <= 0) || !oHeaderPermission.viewIcon) ? null :
        <ImageSliderView assetList={oImageGalleryDialogViewData.allAssetList}
                         selectedImageObject={this.props.contentInfoData.imageObject}
                         handler={this.handleSliderImageClicked}
                         sliderImageWrapperDimension={sSliderImageWrapperDimension}
                         scrollButtonWidth={sScrollButtonWidth}
                         countOfShiftingImagesInSlider={iCountOfShiftingImagesInSlider}
        />;

    let sDefaultAssetId = oImageGalleryDialogViewData.defaultAssetInstanceId;
    let sCurrentVisibleAssetId = this.props.selectedImageId;
    let oDefaultImageIcon = null;
    let sDefaultImageIconClassName = "defaultImageIconContainer";
    if (this.props.isDefaultIconVisible && sCurrentVisibleAssetId && oHeaderPermission.canChangeIcon && oHeaderPermission.viewIcon) {
      if (sDefaultAssetId == sCurrentVisibleAssetId) {
        sDefaultImageIconClassName += oEntityDetailData.isDisableHeader ? " disabled" : "";
        sDefaultImageIconClassName += " default";
        oDefaultImageIcon = (
            <TooltipView label={getTranslation().DEFAULT_IMAGE}>
              <div className={sDefaultImageIconClassName}
                   onClick={this.handleSetDefaultAssetButtonClicked.bind(this)}>
              </div>
            </TooltipView>
        );
      }
      else {
        oDefaultImageIcon = !oEntityDetailData.isDisableHeader ?(
            <TooltipView label={getTranslation().SET_AS_DEFAULT}>
              <div className={sDefaultImageIconClassName} onClick={this.handleSetDefaultAssetButtonClicked.bind(this)}>
              </div>
            </TooltipView>
        ) : null
      }
    }

    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
    let oShareAssetData = oEntityDetailData.shareAssetData;
    let oLinkSharingView = CS.isNotEmpty(oShareAssetData.assetSharedUrl) && !bIsCurrentUserReadOnly ? (<div className={"linkSharingContainer"}>
      <a className={"assetLink"} href={oShareAssetData.assetSharedUrl}
         target={"_blank"}>{oShareAssetData.assetSharedUrl}</a>
      <TooltipView placement={"top"} label={getTranslations().COPY_TO_CLIPBOARD_TOOLTIP}>
        <div className={"copyToClipboard"}
             onClick={this.handleCopyToClipboardClick.bind(this, oShareAssetData.assetSharedUrl)}></div>
      </TooltipView>
      <TooltipView placement={"top"} label={getTranslations().DELETE}>
        <div className={"deleteGeneratedLink"} onClick={this.handleDeleteGeneratedLinkClick}></div>
      </TooltipView>
    </div>) : null;

    return (
        <div className="contentInformationSidebarViewContainer">
          <div className="contentNameContainer">
            {oRightSideIndicatorsViewContainer}
            {oEntityLabelView}
            {oEntityLabelEditButtonView}
          </div>
          <div className="imageAndVersionContainer">
            {oDefaultImageIcon}
            {CS.isEmpty(oHeaderPermission) || oHeaderPermission.viewIcon ? this.getIconView() : null}
            {oImageSliderView}
          </div>
          {oLinkSharingView}
          <div className="contentInformation">
            {oKPISummaryView}
            {oTemplateListView}
            {oMultiClassificationDOM}
            {oLifeCycleTagsView}
            {oCreationDetailsView}
          </div>

          {this.getImageGalleryDialogView()}
        </div>
    );
  }

}

ContentInformationSidebarView.propTypes = oPropTypes;

export const view = MasterUserListContext(ContentInformationSidebarView);
export const events = oEvents;
