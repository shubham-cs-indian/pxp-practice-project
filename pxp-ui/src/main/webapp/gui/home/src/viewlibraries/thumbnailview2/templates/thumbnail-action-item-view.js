import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ThumbnailAddToCollectionView } from './thumbnail-add-to-collection-view';
import RequestMapping from '../../../libraries/requestmappingparser/request-mapping-parser.js';
import { UploadRequestMapping as oUploadRequestMapping } from '../../tack/view-library-request-mapping';
import ThumbnailItemModel from './../model/thumbnail-model';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../tooltipview/tooltip-view';
import { view as ThumbnailInformationView } from './thumbnail-information-view';
import { view as MediaPreviewDialogView } from '../../mediapreviewdialogview/media-preview-dialog-view';
import ViewUtils from '../../utils/view-library-utils';

const oEvents = {
  ACTION_ITEM_DELETE_CLICKED: "action_item_delete_clicked",
  ACTION_ITEM_REMOVE_CLICKED: "action_item_remove_clicked",
  ACTION_ITEM_CLONE_CLICKED: "action_item_clone_clicked",
  ACTION_ITEM_EDIT_RELATIONSHIP_VARIANT_CLICKED: "action_item_edit_relationship_variant_clicked",
  ACTION_ITEM_RESTORE_CLICKED: "action_item_restore_clicked",
  ACTION_ITEM_CREATE_VARIANT_CLICKED: "action_item_create_variant_clicked"
};

const oPropTypes = {
  theme: ReactPropTypes.string,
  thumbnailViewModel: ReactPropTypes.instanceOf(ThumbnailItemModel).isRequired,
  filterContext: ReactPropTypes.object,
};
/**
 * @class ThumbnailActionItemView
 * @memberOf Views
 * @property {string} [theme] - Theme(ex. MAM and PIM).
 * @property {custom} thumbnailViewModel - Instance of ThumbnailItemModel.
 */

// @CS.SafeComponent
class ThumbnailActionItemView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      showPreview: false,
    }

    this.downloadAnchor_inbound = React.createRef();
  }

  closeMediaPreviewPopView = () => {
    this.setState({
      showPreview: false,
    });
  };

  handleDeleteThumbClicked = (oModel, oFilterContext, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ACTION_ITEM_DELETE_CLICKED, this, oModel, oFilterContext);
  };

  handleRemoveThumbClicked = (oModel, oFilterContext, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ACTION_ITEM_REMOVE_CLICKED, this, oModel, oFilterContext);
  };

  handleCreateVariantThumbClicked = (oModel, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oEntityModel = oModel.properties.entity;
    EventBus.dispatch(oEvents.ACTION_ITEM_CREATE_VARIANT_CLICKED, this, oEntityModel);
  };

  handleCloneThumbClicked = (oModel, oFilterContext, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ACTION_ITEM_CLONE_CLICKED, this, oModel, oFilterContext);
  };

  handleEditRelationshipVariantThumbClicked = (oModel, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ACTION_ITEM_EDIT_RELATIONSHIP_VARIANT_CLICKED, this, oModel);
  };

  handleThumbnailRestoreButtonClicked = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ACTION_ITEM_RESTORE_CLICKED, sId);
  };

  handleShowPreviewDialogClicked = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    this.setState({
      showPreview: true,
    });
  };

  handleDownloadButtonClicked = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    this.downloadAnchor_inbound.current.click();
  };

  preventEventBubbling = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
  };

  getMediaPreviewDialogViewData = () => {
    var oModel = this.props.thumbnailViewModel;
    var oElement = oModel.properties['entity'];
    return ViewUtils.getElementAssetData(oElement);
  };

  getMediaPreviewDialogView = () => {
    return (<MediaPreviewDialogView mediaData={this.getMediaPreviewDialogViewData()}
                                    onClose={this.closeMediaPreviewPopView}
                                    showPreview={this.state.showPreview}/>)

  };

  getButtonView = (sLabel, sClassName, fClickHandler) => {
    return (<TooltipView label={sLabel} placement="top">
      <div className={"thumbnailActionButton " + sClassName}
            onClick={fClickHandler}>
      </div>
    </TooltipView>)
  }


  getActionItemsView = () => {
    let oModel = this.props.thumbnailViewModel;
    let bDisableDelete = oModel.properties['disableDelete'];
    let bIsShowVariantCreation = oModel.properties['isShowVariantCreation'];
    let bIsEntityRemovable = oModel.properties['isEntityRemovable'];
    let bDisableAddToCollection = oModel.properties['disableAddToCollection'];
    let bDisableClone = oModel.properties['disableClone'];
    let bEnableEditRelationshipVariantContext = oModel.properties['enableEditRelationshipVariantContext'];
    let bShowPreview = oModel.properties['showPreview'];
    let bDisableDownload = oModel.properties['disableDownload'];
    let sTheme = this.props.theme;
    let bDisabledRestore = oModel.properties['disableRestore'];
    let bHideDeleteButton = oModel.properties['hideDeleteButton'];
    let bShowContextPreview = oModel.properties['showContextPreview'];

    let oPreviewDialogViewHandler = null;
    let oCloneButtonDOM = null;
    let oDownloadButton = null;
    let oDeleteButtonDOM = null;
    let oCreateVariantButtonDDOM = null;
    let oRestoreIconDOM = null;
    let oRemoveButton = null;
    let oPreviewContextButton = null;

    let fDeleteButtonHandler = this.handleDeleteThumbClicked.bind(this, oModel, this.props.filterContext);
    let fRemoveButtonHandler = this.handleRemoveThumbClicked.bind(this, oModel, this.props.filterContext);

    if (!bDisableDelete) {
      bIsEntityRemovable && (oRemoveButton = this.getButtonView(getTranslation().REMOVE, "thumbnailRemoveButton", fRemoveButtonHandler));
      !bHideDeleteButton && (oDeleteButtonDOM = this.getButtonView(getTranslation().DELETE, "thumbnailDeleteButton", fDeleteButtonHandler));
    }

    if(bIsShowVariantCreation){
      oCreateVariantButtonDDOM =  (<TooltipView label={getTranslation().CREATE_VARIANT} placement="top">
        <div className="thumbnailActionButton thumbnailCreateVariantButton"
             onClick={this.handleCreateVariantThumbClicked.bind(this, oModel)}>
        </div>
      </TooltipView>);
    }

    var oAddToCollectionView = !bDisableAddToCollection ?
        <ThumbnailAddToCollectionView filterContext={this.props.filterContext} contentId={oModel.id} contentModel={oModel}/> : null;


    if (!bDisableClone) {
      oCloneButtonDOM = (<TooltipView label={getTranslation().CREATE_CLONE} placement="top">
        <div className="thumbnailActionButton thumbnailCloneButton"
              onClick={this.handleCloneThumbClicked.bind(this, oModel, this.props.filterContext)}>
        </div>
      </TooltipView>);
    }

    let oEditVariantTagButtonDOM = bEnableEditRelationshipVariantContext ? (
        <TooltipView label={getTranslation().EDIT_VARIANTS} placement="top">
          <div className="thumbnailActionButton thumbnailEditRelationshipVariantButton"
               onClick={this.handleEditRelationshipVariantThumbClicked.bind(this, oModel)}>
          </div>
        </TooltipView>) : null;


    if (bShowPreview && sTheme === "MAM") {
      oPreviewDialogViewHandler = (
          <TooltipView label={getTranslation().VIEW} placement="top">
            <div className="thumbnailActionButton thumbnailPreviewButton"
                 onClick={this.handleShowPreviewDialogClicked}>
            </div>
          </TooltipView>
      );
    }

    if(bShowContextPreview) {
      oPreviewContextButton = (
          <TooltipView label={getTranslation().VIEW} placement="top">
            <div className="thumbnailActionButton thumbnailPreviewButton"
                 onClick={this.handleEditRelationshipVariantThumbClicked.bind(this, oModel)}>
            </div>
          </TooltipView>
      );
    }

    if (!bDisableDownload) {
      // let sRefKey = "downloadAnchor_inbound";
      let sAssetInstanceId = oModel.id;
      let sEndpointId = oModel.endpointId;
      let sOrganizationId = oModel.organizationId;
      let sDownloadUrl = sAssetInstanceId ? RequestMapping.getRequestUrl(oUploadRequestMapping.DownloadFile, {
        id: sAssetInstanceId,
        organizationId: sOrganizationId,
        endpointId: sEndpointId
      }) : "";
      oDownloadButton = (<TooltipView label={getTranslation().DOWNLOAD} placement="top">
        <div className="thumbnailActionButton thumbnailDownloadButton"
             onClick={this.handleDownloadButtonClicked}>
          <a ref={this.downloadAnchor_inbound} href={sDownloadUrl} target="_blank"></a>
        </div>
      </TooltipView>);
    }

    if (!bDisabledRestore) {
      oRestoreIconDOM = (
          <TooltipView label={getTranslation().RESTORE} placement="top">
            <div className="thumbnailActionButton thumbnailRestoreButton"
                 onClick={this.handleThumbnailRestoreButtonClicked.bind(this, oModel.id)}>
            </div>
          </TooltipView>
      );
    }
    let oMediaPreviewDialogView = null;
    if (this.state.showPreview) {
      oMediaPreviewDialogView = this.getMediaPreviewDialogView();
    }

    return (
        <div className="ThumbnailActionItemContainer" onClick={this.preventEventBubbling}>
          {oCreateVariantButtonDDOM}
          {oDeleteButtonDOM}
          {oRemoveButton}
          {oAddToCollectionView}
          {oCloneButtonDOM}
          {oEditVariantTagButtonDOM}
          {oPreviewDialogViewHandler}
          {oDownloadButton}
          {oRestoreIconDOM}
          {oMediaPreviewDialogView}
          {oPreviewContextButton}
        </div>
    )

  };

  getThumbnailInformationView = () => {
    let oModel = this.props.thumbnailViewModel;
    let oProperties = oModel.properties;
    let oRuleViolationDetails = oProperties.entity.messages;
    let iMandatoryViolationCount = oProperties.entity.mandatoryViolationCount;
    let iUniqueViolationCount = oProperties.entity.isUniqueViolationCount;
    let iShouldViolationCount = oProperties.entity.shouldViolationCount;
    let sCloneOfLabel = oProperties.entity.branchOfLabel || "";
    let sVariantOfLabel = oProperties.entity.variantOfLabel || "";
    let sVariantId = oProperties.entity.variantOf || "";
    let bHideVariantOfIcon = oProperties.hideVariantOfIcon;
    let bDisableView = oProperties.disableView;
    let oAssetExtraData = {
        isAssetExpired: oProperties.entity.isAssetExpired,
        isDuplicateAsset: oProperties.entity.isDuplicate
    };
    let oThumbnailDetailView = <ThumbnailInformationView ruleViolation={oRuleViolationDetails}
                                                         cloneOfLabel={sCloneOfLabel}
                                                         variantOfLabel={sVariantOfLabel}
                                                         variantId={sVariantId}
                                                         mandatoryViolationCount={iMandatoryViolationCount}
                                                         shouldViolationCount={iShouldViolationCount}
                                                         uniqueViolationCount={iUniqueViolationCount}
                                                         isGoldenArticle={oProperties.isGoldenArticle}
                                                         contentId={oModel.id}
                                                         assetExtraData={oAssetExtraData}
                                                         hideVariantOfIcon={bHideVariantOfIcon}
                                                         disableView={bDisableView}/>;

    return oThumbnailDetailView;
  };

  render () {
    let oActionItemsView = this.getActionItemsView();
    let oThumbnailInformationView = this.getThumbnailInformationView();
    return (
        <div className="thumbnailActionItemAndInformationView">
          <div className="thumbnailActionItemWrapper">
            {oActionItemsView}
          </div>
          <div className="thumbnailInformationView">
            {oThumbnailInformationView}
          </div>
        </div>);
  };
}

export const view = ThumbnailActionItemView;
export const events = oEvents;
