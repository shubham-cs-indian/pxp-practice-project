import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ContextMenuViewModel from '../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as ContextMenuItemView } from '../../contextmenuwithsearchview/context-menu-item-view.js';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../tooltipview/tooltip-view';
const oEvents = {
  THUMBNAIL_INFORMATION_STATIC_COLLECTION_BUTTON_CLICKED: "static_collection_button_clicked",
  THUMBNAIL_INFORMATION_MODIFY_STATIC_COLLECTION_CLICKED: "modify_static_collection_clicked",
  THUMBNAIL_INFORMATION_SET_VIOLATION_EVENT_DATA: "thumbnail_information_set_violation_event_data",
  THUMBNAIL_INFORMATION_VIEW_ENTITY_INFO_ICON_CLICKED: "thumbnail_information_view_entity_info_icon_clicked_"
};

const oPropTypes = {
  ruleViolation: ReactPropTypes.object,
  promotionalVersionOf: ReactPropTypes.string,
  cloneOfLabel: ReactPropTypes.string,
  mandatoryViolationCount: ReactPropTypes.number,
  shouldViolationCount: ReactPropTypes.number,
  uniqueViolationCount: ReactPropTypes.number,
  handleSingleClick: ReactPropTypes.func,
  contentId: ReactPropTypes.string,
  isGoldenArticle: ReactPropTypes.bool,
  isMdmInstanceDeleted: ReactPropTypes.bool,
  variantOfLabel: ReactPropTypes.string,
  assetExtraData: ReactPropTypes.object,
  variantId: ReactPropTypes.string,
  hideVariantOfIcon: ReactPropTypes.bool,
  disableView: ReactPropTypes.bool
};
/**
 * @class ThumbnailInformationView - Used for Display thumbnail information view.
 * @memberOf Views
 * @property {object} [ruleViolation] -  object for all the rules list.
 * @property {string} [promotionalVersionOf] string of promotionalVersionOf article.
 * @property {string} [cloneOfLabel] string of clone Label.
 * @property {number} [mandatoryViolationCount] number count of mandatoryViolationCount.
 * @property {number} [shouldViolationCount] number count of shouldViolationCount.
 * @property {number} [uniqueViolationCount] number count of uniqueViolationCount.
 * @property {func} [handleSingleClick] function of handleSingleClick.
 * @property {string} [contentId] -  string of contentID.
 * @property {bool} [isGoldenArticle] -  boolean of isGoldenArticle or not.
 * @property {bool} [isMdmInstanceDeleted] -  boolean of isMdmInstanceDeleted or not.
 * @property {string} [variantOfLabel] string of variant Label.
 */

// @CS.SafeComponent
class ThumbnailInformationView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showStaticCollectionsPopover: false
    }
  }

  handleStaticCollectionsPopoverVisibility =(oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMBNAIL_INFORMATION_STATIC_COLLECTION_BUTTON_CLICKED, this);
    this.setState({
      showStaticCollectionsPopover: true,
      staticCollection: oEvent.currentTarget
    });
  }

  handleStaticCollectionsPopoverClose =()=> {
    this.setState({
      showStaticCollectionsPopover: false
    });
  }

  handleAddToStaticCollectionClicked =(oModel)=> {
    var sCollectionId = oModel.id;
    var sContentId = this.props.contentId;
    EventBus.dispatch(oEvents.THUMBNAIL_INFORMATION_MODIFY_STATIC_COLLECTION_CLICKED, this, sCollectionId, sContentId);
    this.setState({
      showStaticCollectionsPopover: false
    });
  }

  getGenericCountCircle = (sClassName, iCount)=> {

    let sClass = "ruleViolationCircle " + (iCount ? sClassName : "");

    return (
        <div className="ruleViolationIndicator">
          <div className={sClass}>{iCount || ""}</div>
        </div>
    )
  };

  getExpiredAssetIndicator = () => {
    let bIsAssetExpired = this.props.assetExtraData.isAssetExpired;
    let oExpiredAssetIconDOM = bIsAssetExpired ? (
        <TooltipView placement="top" label={getTranslation().EXPIRED}>
          <div className={"expiredAssetsIndicator"}></div>
        </TooltipView>
    ) : null;
    return (
        <div className={"expiredAssetsIndicatorWrapper"}>
          {oExpiredAssetIconDOM}
        </div>
    )
  };

  getDuplicateAssetIndicator = () => {
    let bIsDuplicateAsset = this.props.assetExtraData.isDuplicateAsset;
    let oDuplicateAssetIconView = bIsDuplicateAsset ? (
        <TooltipView placement="top" label={getTranslation().DUPLICATE}>
          <div className={"duplicateAssetIndicator"}></div>
        </TooltipView>
    ) : null;

    return (
        <div className={"duplicateAssetIndicatorWrapper"}>
          {oDuplicateAssetIconView}
        </div>
    )
  };

  getGenericViolationCountView =(sLabel, iRedCount, iOrangeCount, iYellowCount)=> {

    let oRedView = this.getGenericCountCircle("ruleViolationRedColor", iRedCount);
    let oOrangeView = this.getGenericCountCircle("ruleViolationOrangeColor", iOrangeCount);
    let oYellowView = this.getGenericCountCircle("ruleViolationYellowColor", iYellowCount);

    return(
        <div className="ruleViolationIndicatorContainer">
          <div className="ruleViolationTooltipHeader">{sLabel}</div>
          <div className="individualRuleViolationCount">
            {oRedView}
            {oOrangeView}
            {oYellowView}
          </div>
        </div>
    );
  };

  getTotalRuleViolationCountView =()=> {
    var oRuleViolation = this.props.ruleViolation;
    let iRedCount = oRuleViolation.redCount + this.props.mandatoryViolationCount + this.props.uniqueViolationCount;
    let iOrangeCount = oRuleViolation.orangeCount + this.props.shouldViolationCount;
    let iYellowCount = oRuleViolation.yellowCount;

    var iTotalRuleViolationCount = iRedCount + iOrangeCount + iYellowCount;

    /**Fixed from Backend **/
    // if (oRuleViolation.validityMessage) {
    //   iTotalRuleViolationCount++;
    // }

    var sColor = (this.props.isAssetExpired || iRedCount > 0) ? "red" : ((iOrangeCount > 0) ? "orange" : "yellow");
    var sTotalRuleViolationClassName = "totalRuleViolationColor ";
    var sTotalRuleViolationCountClassName = "totalRuleViolationCount ";
    sTotalRuleViolationClassName += sColor;
    sTotalRuleViolationCountClassName += sColor;
    let oTotalView = (<div className="totalRuleViolation">
      <div className={sTotalRuleViolationClassName}>
      </div>
      <div className={sTotalRuleViolationCountClassName}>
        {iTotalRuleViolationCount}
      </div>
    </div>);

    return {
      view: oTotalView,
      totalViolationCount: iTotalRuleViolationCount
    }
  };

  getRuleViolationView =()=> {
    let oRuleViolation = this.props.ruleViolation;
    if(CS.isEmpty(oRuleViolation)) {
      return null;
    }
    let oTotalViolation = this.getTotalRuleViolationCountView();
    let iRedCount = oRuleViolation.redCount + this.props.mandatoryViolationCount + this.props.uniqueViolationCount;
    let iOrangeCount = oRuleViolation.orangeCount + this.props.shouldViolationCount;
    let iYellowCount = oRuleViolation.yellowCount;

    if (oTotalViolation.totalViolationCount > 0) {
      let oIndividualSummeryViolationView = this.getGenericViolationCountView(getTranslation().TOTAL, iRedCount, iOrangeCount, iYellowCount);
      var oIndividualRuleViolationCountView = this.getGenericViolationCountView(getTranslation().RULE_VIOLATIONS, oRuleViolation.redCount, oRuleViolation.orangeCount, oRuleViolation.yellowCount);
      let oMandatoryViolationView = this.getGenericViolationCountView(getTranslation().MANDATORY_VIOLATIONS, this.props.mandatoryViolationCount, this.props.shouldViolationCount);
      let oUniqueViolationView = this.getGenericViolationCountView(getTranslation().UNIQUE_VIOLATIONS, this.props.uniqueViolationCount);

      let oTotalSummeryView = (<div>
        {oIndividualSummeryViolationView}
        {oIndividualRuleViolationCountView}
        {oMandatoryViolationView}
        {oUniqueViolationView}
      </div>);
      var oTotalRuleViolationCountView = oTotalViolation.view;

      return (<div className="thumbInfo ruleViolationCount" onClick={this.handleViolationClicked}>
        <TooltipView placement="top" label={oTotalSummeryView}>
          {oTotalRuleViolationCountView}
        </TooltipView>
      </div>);
    } else {
      return null;
    }
  }

  handleViolationClicked = (oEvent) => {
    // oEvent.extraData = {isViolationClicked: true}; is Assigned as per checks in the store
    // only for calendar view (other parent views were already handling it)
    EventBus.dispatch(oEvents.THUMBNAIL_INFORMATION_SET_VIOLATION_EVENT_DATA, oEvent);
    // only for calendar view (other parent views were already handling it)
    if (CS.isFunction(this.props.handleSingleClick)) {
      this.props.handleSingleClick(oEvent);
    }
  };

  thumbnailEntityInfoIconClicked = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMBNAIL_INFORMATION_VIEW_ENTITY_INFO_ICON_CLICKED, this.props.contentId, sId);
  };

  getEntityInfoIconView (sLabel, sValue, sClass, sId) {
    if(CS.isEmpty(sValue)) {
      return null;
    }

    let oToolTipView = (
        <div className="entityInformationIconTooltip">
          <div className="entityInformationIconLabel">{sLabel}</div>
          <div className="parentEntityInformationLabel">{sValue}</div>
        </div>);

    let fOnClickHandler = null;
    if(sClass === "variantContextIcon" && CS.isNotEmpty(sId) && !this.props.disableView) {
      fOnClickHandler = this.thumbnailEntityInfoIconClicked.bind(this, sId)
    }

    let oView = (<div className={"entityInformationIcon " + sClass}/>);

    return (
        <div className="entityInformationIconIndicator" onClick={fOnClickHandler}>
          <TooltipView placement="top" label={oToolTipView}>
            {oView}
          </TooltipView>
        </div>
    );
  };

  getStaticCollectionModelsList =(aStaticCollectionList)=> {
    var aStaticCollectionModels = [];
    CS.forEach(aStaticCollectionList, function (oStaticCollection) {
      aStaticCollectionModels.push(new ContextMenuViewModel(
          oStaticCollection.id,
          CS.getLabelOrCode(oStaticCollection),
          false,
          "",
          {context: 'StaticCollection'}
      ));
    });

    return aStaticCollectionModels;
  }

  /*getStaticCollectionView =()=> {
    var aStaticCollectionList = this.context.staticCollectionList;
    var aStaticCollectionContextMenuModelList = this.getStaticCollectionModelsList(aStaticCollectionList);

    return (
        <TooltipView placement="top" label={getTranslation().ADD_TO_COLLECTION}>
        <div className="thumbInfo staticCollectionListContainer"
                 onClick={this.handleStaticCollectionsPopoverVisibility}>
          <Popover
              className="popover-root"
              open={this.state.showStaticCollectionsPopover}
              anchorEl={this.state.staticCollection}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'right', vertical: 'top'}}
              onClose={this.handleStaticCollectionsPopoverClose}
          >
            <ContextMenuItemView contextMenuViewModel={aStaticCollectionContextMenuModelList}
                                 onClickHandler={this.handleAddToStaticCollectionClicked}/>
          </Popover>getIsMdmInstanceDeleted
        </div>
        </TooltipView>
    );
  };*/

  getToolTipView = (sLabel, sClass, oChildren) => {
    let sClassName = "thumbInfo " + sClass;
    return (
        <TooltipView placement="top" label={sLabel}>
          {oChildren ? oChildren : <div className={sClassName}></div>}
        </TooltipView>
    );
  };

  render() {
    let oCloneView = this.getEntityInfoIconView(getTranslation().CLONE_OF, this.props.cloneOfLabel, "cloneContextIcon");
    let oVariantView = this.props.hideVariantOfIcon ? null :
                       this.getEntityInfoIconView(getTranslation().VARIANT_OF, this.props.variantOfLabel, "variantContextIcon", this.props.variantId);
    let oGoldenRecordIndicatorView = this.props.isGoldenArticle ? this.getToolTipView(getTranslation().GOLDEN_RECORD, "goldenRecordIndicator") : null;
    let oMdmInstanceDeletedView = this.props.isMdmInstanceDeleted ? this.getToolTipView(getTranslation().LINKED_MDM_INSTANCE_DELETED, "mdmInstanceDeleted") : null;
    return (<div className="thumbnailInformationContainer">
      {oCloneView}
      {oVariantView}
      {oGoldenRecordIndicatorView}
      {oMdmInstanceDeletedView}
      {this.getDuplicateAssetIndicator()}
      {this.getExpiredAssetIndicator()}
      {this.getRuleViolationView()}
      {/*this.getStaticCollectionView()*/}
    </div>);
  }

}

ThumbnailInformationView.propTypes = oPropTypes;
export const view = ThumbnailInformationView;
export const events = oEvents;
