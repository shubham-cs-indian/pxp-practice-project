import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ThumbnailInformationView } from '../../thumbnailview2/templates/thumbnail-information-view';
import { view as ThumbnailAddToCollectionView } from '../../thumbnailview2/templates/thumbnail-add-to-collection-view';
import RequestMapping from '../../../libraries/requestmappingparser/request-mapping-parser.js';
import { UploadRequestMapping as oUploadRequestMapping } from '../../tack/view-library-request-mapping';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager';
import TooltipView from '../../tooltipview/tooltip-view';
import ListItemModel from './../model/detailed-list-item-model';

const oEvents = {
  THUMB_SINGLE_CLICKED: "thumb_single_clicked",
  THUMB_CHECKBOX_CLICKED: "thumb_checkbox_clicked",
  THUMB_DELETE_CLICKED: "thumb_delete_clicked",
  THUMB_CLONE_CLICKED: "thumb_clone_clicked",
  THUMB_EDIT_RELATIONSHIP_VARIANT_CLICKED: "thumb_edit_relationship_variant_clicked"
};

const oPropTypes = {
  getSelectButton: ReactPropTypes.bool,
  isSelected: ReactPropTypes.bool,
  listItemModel: ReactPropTypes.instanceOf(ListItemModel).isRequired
};
/**
 * @class ListButtonsView - use to Display List Buttons View in Application.
 * @memberOf Views
 * @property {bool} [getSelectButton] -  boolean which is used for getSelectButton or not.
 * @property {bool} [isSelected] -  boolean which is used for isSelected or not.
 * @property {custom} listItemModel - pass List Item Model.
 */

// @CS.SafeComponent
class ListButtonsView extends React.Component {

  constructor(props) {
    super(props);

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }

  _handleSingleClicked =(oModel, oEvent)=> {
    if(!oEvent.nativeEvent.dontRaise) {
      oEvent.nativeEvent.dontRaise = true;
      EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, oEvent, oModel);
    }
  }

  _handleThumbnailCheckboxClicked =(oModel, oEvent)=>{
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_CHECKBOX_CLICKED, this, oModel);
  }

  _handleDeleteThumbClicked =(oModel, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_DELETE_CLICKED, this, oModel);
  }

  _handleCloneThumbClicked  =(oModel, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_CLONE_CLICKED, this, oModel);
  }

  handleDownloadButtonClicked (sRefKey, oEvent) {
    oEvent.nativeEvent.dontRaise = true;
    this[sRefKey].click();
  }

  _handleEditRelationshipVariantThumbClicked =(oModel, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_EDIT_RELATIONSHIP_VARIANT_CLICKED, this, oModel);
  }

  render() {

    var oModel = this.props.listItemModel;

    var bDisableCheck = oModel.properties['disableCheck'];
    var bDisableAddToCollection = oModel.properties['disableAddToCollection'];
    var bEnableEditRelationshipVariantContext = oModel.properties['enableEditRelationshipVariantContext'];
    var oProperties = oModel.properties;
    var oRuleViolationDetails = oProperties.entity.messages;
    var iMandatoryViolationCount = oProperties.entity.mandatoryViolationCount;
    let iShouldViolationCount = oProperties.entity.shouldViolationCount;
    let iUniqueViolationCount = oProperties.entity.isUniqueViolationCount;
    var sCloneOfLabel = oProperties.entity.branchOfLabel || "";
    let bIsGoldenArticle = CS.includes(oProperties.entity.types, "golden_article_klass");
    let sVariantOfLabel = oProperties.entity.variantOfLabel || "";
    let sVariantId = oProperties.entity.variantOf || "";

    var oThumbnailDetailView = <ThumbnailInformationView ruleViolation={oRuleViolationDetails}
                                                         cloneOfLabel={sCloneOfLabel}
                                                         variantOfLabel={sVariantOfLabel}
                                                         variantId={sVariantId}
                                                         mandatoryViolationCount={iMandatoryViolationCount}
                                                         shouldViolationCount={iShouldViolationCount}
                                                         uniqueViolationCount={iUniqueViolationCount}
                                                         contentId={oModel.id}
                                                         isMdmInstanceDeleted={oProperties.entity.isMdmInstanceDeleted}
                                                         isGoldenArticle ={bIsGoldenArticle}/>;

    var oAddToCollectionView = !bDisableAddToCollection ? <ThumbnailAddToCollectionView contentId={oModel.id} contentModel={oModel}/> : null;

   var oEditVariantTagButtonDOM = bEnableEditRelationshipVariantContext ? (
        <TooltipView label={getTranslation().EDIT_VARIANTS} placement="top">
          <div className="thumbnailButton editVariantButton" onClick={this._handleEditRelationshipVariantThumbClicked.bind(this, oModel)}></div>
        </TooltipView>) : null;

    if (this.props.getSelectButton) {
      // GET ONLY THE SELECT BUTTON -
      var sCheckButtonClass = "checkButton ";
      sCheckButtonClass += bDisableCheck ? "visibilityHidden " : "";
      var bIsSelected = this.props.isSelected;
      var sSelectLabel = getTranslation().SELECT;
      if (bIsSelected) {
        sCheckButtonClass += "isSelected";
        sSelectLabel = getTranslation().DESELECT;
      }

      return (
          <TooltipView label={sSelectLabel}>
            <div className={sCheckButtonClass}
                 onClick={this._handleThumbnailCheckboxClicked.bind(this, oModel)}>
              <div className="checkButtonIcon"></div>
            </div>
          </TooltipView>
      );
    }

    else {
      // GET ALL BUTTONS -
      var bDisableView = oModel.properties['disableView'];
      var bDisableDelete = oModel.properties['disableDelete'];
      var bDisableClone = oModel.properties['disableClone'];
      var bDisableDownload = oModel.properties['disableDownload'];
      var bIsEntityRemovable = oModel.properties['isEntityRemovable'];

      var oCheckButton = bDisableCheck ? null :
          (<div className="thumbnailButton checkButton"
                onClick={this._handleThumbnailCheckboxClicked.bind(this, oModel)}></div>);
      /*var oViewButton = bDisableView ? null :
          (<div className="thumbnailButton viewButton"
                onClick={this._handleSingleClicked.bind(this, oModel)}></div>);
      var oDeleteButton = bDisableDelete ? null :
          (<div className="thumbnailButton deleteButton"
                onClick={this._handleDeleteThumbClicked.bind(this, oModel)}></div>);*/
      var oViewButton = bDisableView ? null :
          (<TooltipView label={getTranslation().VIEW}>
            <div className="thumbnailButton viewButton"
                 onClick={this._handleSingleClicked.bind(this, oModel)}></div>
          </TooltipView>);

      var oDeleteButton = null;
      if (!bDisableDelete) {
        if (bIsEntityRemovable) {
          oDeleteButton = (
              <TooltipView label={getTranslation().REMOVE} placement="top">
                <div className="thumbnailButton removeButton" onClick={this._handleDeleteThumbClicked.bind(this, oModel)}></div>
              </TooltipView>
          );
        } else {
          oDeleteButton = (
              <TooltipView label={getTranslation().DELETE} placement="top">
                <div className="thumbnailButton deleteButton"
                     onClick={this._handleDeleteThumbClicked.bind(this, oModel)}></div>
              </TooltipView>);
        }
      }

      var oCloneButtonDOM = null;
      if (!bDisableClone) {
        oCloneButtonDOM = (<TooltipView label={getTranslation().CREATE_CLONE} placement="top">
          <div className="thumbnailButton cloneButton" onClick={this._handleCloneThumbClicked.bind(this, oModel)}></div>
        </TooltipView>);
      }

      var oDownloadButtonDOM = null;
      if (!bDisableDownload) {
        let sRefKey = "downloadAnchor_inbound";
        let sAssetInstanceId = oModel.id;
        let sDownloadUrl = sAssetInstanceId ? RequestMapping.getRequestUrl(oUploadRequestMapping.DownloadFile, {id: sAssetInstanceId}) : "";
        oDownloadButtonDOM = (<TooltipView label={getTranslation().DOWNLOAD} placement="top">
          <div className="thumbnailButton downloadButton" onClick={this.handleDownloadButtonClicked.bind(this, sRefKey)}>
            <a ref={this.setRef.bind(this,sRefKey)} href={sDownloadUrl}></a>
          </div>
        </TooltipView>);
      }


      return (
          <div className="listViewButtonContainer">
            {/*{oCheckButton}*/}
            {/*{oViewButton}*/}
            {oDeleteButton}
            {oAddToCollectionView}
            {oThumbnailDetailView}
            {oEditVariantTagButtonDOM}
            {oCloneButtonDOM}
            {oDownloadButtonDOM}
          </div>
      );
    }
  }
}

ListButtonsView.propTypes = oPropTypes;

export const view = ListButtonsView;
export const events = oEvents;
