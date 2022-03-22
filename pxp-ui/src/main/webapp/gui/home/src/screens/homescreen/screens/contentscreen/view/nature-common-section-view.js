import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as PaginationView } from './../../../../../viewlibraries/paginationview/pagination-view';
import { view as NatureRelationshipView } from './nature-relationship-view';
import NatureTypeDictionary from './../../../../../commonmodule/tack/nature-type-dictionary';
import NatureRelationshipDictionary from './../../../../../commonmodule/tack/relationship-type-dictionary';
import Constants from '../../../../../commonmodule/tack/constants';
import { view as ToolbarView } from '../../../../../viewlibraries/toolbarview/toolbar-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_BUNDLE_TOOLBAR_ITEM_CLICKED: "handle_bundle_toolbar_item_clicked",
  NATURE_COMMON_SECTION_NAVIGATION_ITEM_CLICKED: "nature_common_section_navigation_item_clicked"
};

const oPropTypes = {
  natureSectionViewData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired
};

// @CS.SafeComponent
class NatureCommonSectionView extends React.Component {
  static propTypes = oPropTypes;

  handleBundleToolbarButtonClicked = (sButtonContext) => {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oRelationship = oNatureSectionViewData.natureRelationship;
    var sSideId = oRelationship.sideId;
    EventBus.dispatch(oEvents.HANDLE_BUNDLE_TOOLBAR_ITEM_CLICKED, sButtonContext, sSideId, oNatureSectionViewData.viewContext, oRelationship.id);
  };

  handlePaginationChanged = (sSideId, oPaginationData, oFilterContext) => {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oRelationship = oNatureSectionViewData.natureRelationship;
    EventBus.dispatch(oEvents.NATURE_COMMON_SECTION_NAVIGATION_ITEM_CLICKED, sSideId, oPaginationData, oRelationship.id);
  };

  getHeaderView = (oReferencedNatureRelationship) => {
    var oRelationshipToolbarProps = this.props.natureSectionViewData.relationshipToolbarProps;
    var iNoOfElements = oRelationshipToolbarProps.totalCount;
    var sNatureType = oReferencedNatureRelationship.natureType;

    let sLabel = "";
    let sCountInformation = "";

    switch (oReferencedNatureRelationship.relationshipType) {
      case NatureRelationshipDictionary.PRODUCT_NATURE:
        sLabel = getTranslation().PRODUCTS + " (" + iNoOfElements + ")";
        break;
      case NatureRelationshipDictionary.MEDIA_ASSET:
        sLabel = getTranslation().MEDIA + " (" + iNoOfElements + ")";
        break;
      default:
        sLabel = getTranslation()[sNatureType];
        sCountInformation = "(" + iNoOfElements + " " + getTranslation().PRODUCTS + ")";
    }

    return (
        <div className="bundleSectionHeader">
          <div className="bundle">{sLabel}</div>
          <div className="productCount">{sCountInformation}</div>
        </div>
    );
  };

  getCornerRibbonView = () => {
    return (
        <div className="cornerRibbon"></div>
    );
  };

  getBundleToolbarIcons = () => {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var aNatureRelationshipInstanceElements = oNatureSectionViewData.natureRelationshipInstanceElements;
    let _this = this;

    if(!oNatureSectionViewData.showToolbar){
      return null;
    }


    if(CS.isEmpty(aNatureRelationshipInstanceElements)) {
      return null;
    }

    let oItemList =  oNatureSectionViewData.bundleSectionToolbarData;
    CS.forEach(oItemList, function (oItem) {
      oItem[0].onChange = _this.handleBundleToolbarButtonClicked;
    });

    return (
        <div className="bundleToolbarIconContainer">
          <ToolbarView  toolbarData={oItemList}/>
        </div>
    )

  };

  getNatureNavigationBarView = () => {
    var __props = this.props;
    var oNatureSectionViewData = __props.natureSectionViewData;
    var oNavigationData = oNatureSectionViewData.relationshipToolbarProps;
    if(CS.isEmpty(oNavigationData) || oNavigationData.to == 0){
      return null;
    }

    var oRelationship = oNatureSectionViewData.natureRelationship;
    var sRelationshipId = oRelationship.sideId;

    var iCurrentPageItems = oNavigationData.to - oNavigationData.startIndex;

    return (
        <div className="relationshipLoadMoreContainer">
          <div className="paginationSection">
            <PaginationView
                currentPageItems={iCurrentPageItems}
                from={oNavigationData.startIndex}
                pageSize={oNavigationData.paginationLimit}
                totalItems={oNavigationData.totalContents}
                onChange={this.handlePaginationChanged.bind(this, sRelationshipId)}
                filterContext={this.props.filterContext}
                displayTheme={Constants.DARK_THEME}
            />
          </div>
        </div>
    )
  };

  render() {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oReferencedNatureRelationship = oNatureSectionViewData.referencedNatureRelationship;
    var oHeaderView = this.getHeaderView(oReferencedNatureRelationship);
    let oCornerRibbonView = null;
    let bShowCornerRibbon = oReferencedNatureRelationship.natureType == NatureTypeDictionary.FIXED_BUNDLE;
    bShowCornerRibbon && (oCornerRibbonView = this.getCornerRibbonView());
    var oBundleToolBarIcons = this.getBundleToolbarIcons();
    var oNavigationView = this.getNatureNavigationBarView();

    return (
        <div className="natureCommonSectionView">
          {oHeaderView}
          {oBundleToolBarIcons}
          <NatureRelationshipView natureSectionViewData={oNatureSectionViewData}/>
          {oCornerRibbonView}
          {oNavigationView}
        </div>
    );
  }
}

export const view = NatureCommonSectionView;
export const events = oEvents;
