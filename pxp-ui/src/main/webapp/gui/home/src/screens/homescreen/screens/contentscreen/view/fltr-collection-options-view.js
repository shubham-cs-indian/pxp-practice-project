import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as CollectionCreationView } from './collection-creation-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_CREATE_DYNAMIC_COLLECTION_BUTTON_CLICKED: "handle_create_dynamic_collection_button_clicked",
  HANDLE_CREATE_STATIC_COLLECTION_BUTTON_CLICKED: "handle_create_static_collection_button_clicked",
  HANDLE_COLLECTION_BREADCRUMB_RESET: "handle_static_collection_breadcrumb_reset"
};

const oPropTypes = {
  collectionData: ReactPropTypes.object,
  isFilterAndSearchViewDisabled: ReactPropTypes.bool,
  viewMasterData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  collectionPopOverWidth: ReactPropTypes.number
};

// @CS.SafeComponent
class CollectionOptionsView extends React.Component {
  constructor(props) {
    super(props);
    this.popOverAnchor = React.createRef();
  }

  state = {
    showDynamicCollectionsPopover: false,
    showStaticCollectionsPopover: false,
    showAllCollectionsPopover: false
  };

  closePopups = () => {
    this.setState({
      showDynamicCollectionsPopover: false,
      showStaticCollectionsPopover: false,
      showAllCollectionsPopover: false
    });
  };

  handleDynamicCollectionsButtonClicked = (oEvent) => {
    var oCollectionData = this.props.collectionData;
    var bShowPopOverStatus = !!oCollectionData.allHierarchySafe;
    this.setState({
      showDynamicCollectionsPopover: bShowPopOverStatus,
    });
    EventBus.dispatch(oEvents.HANDLE_CREATE_DYNAMIC_COLLECTION_BUTTON_CLICKED, this);
  };

  handleRequestDynamicCollectionsPopoverClose = () => {
    this.setState({
      showDynamicCollectionsPopover: false
    });
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_BREADCRUMB_RESET);
  };

  handleStaticCollectionsButtonClicked = (oEvent) => {
    var oCollectionData = this.props.collectionData;
    var bShowPopOverStatus = !!oCollectionData.allHierarchySafe;
    var oDOM = oEvent.target;
    this.setState({
      showStaticCollectionsPopover: bShowPopOverStatus,
      anchorElement: oDOM
    });
    EventBus.dispatch(oEvents.HANDLE_CREATE_STATIC_COLLECTION_BUTTON_CLICKED, this);
  };

  handleRequestStaticCollectionsPopoverClose = () => {
    this.setState({
      showStaticCollectionsPopover: false
    });
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_BREADCRUMB_RESET);
  };

  getBookmarkDom = () => {
    var sDynamicCollectionButtonClass = "dynamicCollectionsButton ";
    if (this.state.showDynamicCollectionsPopover) {
      sDynamicCollectionButtonClass += "isSelected";
    }

    if(!this.props.isFilterAndSearchViewDisabled) {
      return (<TooltipView placement="bottom" label={getTranslation().BOOKMARK}>
          <div className={sDynamicCollectionButtonClass}
                   onClick={this.handleDynamicCollectionsButtonClicked}></div></TooltipView>);
    }
    return null;
  };

  getDynamicCollectionsSectionView = () => {

    var oCollectionData = this.props.collectionData;

    if (oCollectionData.showDynamicCollectionOption) {
      return (
          <div className="dynamicCollectionsSection">
            {this.getBookmarkDom()}
            {this.getCurrentPopOverView(this.state.showDynamicCollectionsPopover)}
          </div>
      );

    } else {
      return null;
    }
  };

  getStaticCollectionsSectionView = () => {
    var oCollectionData = this.props.collectionData;
    if (oCollectionData.showStaticCollectionOption) {
      var sStaticCollectionButtonClass = "staticCollectionsButton ";
      if (this.state.showStaticCollectionsPopover) {
        sStaticCollectionButtonClass += "isSelected";
      }
      return (
          <div className="staticCollectionsSection">
            <div className="staticCollectionSectionContainer">
            <TooltipView placement="bottom" label={getTranslation().COLLECTION_LABEL}>
              <div className={sStaticCollectionButtonClass}
                 onClick={this.handleStaticCollectionsButtonClicked}></div>
            </TooltipView>
            {this.getCurrentPopOverView(this.state.showStaticCollectionsPopover)}
            </div>
          </div>
      );

    } else {
      return null;
    }
  };

  getCurrentPopOverView (bShowPopOver) {
    let oState = this.state;

    var oCollectionData = this.props.collectionData;
    let oStyle = {
      width: `${this.props.collectionPopOverWidth}px`,
      maxWidth: '420px',
      minWidth: '420px',
      maxHeight: '330px',
      overflowY: 'hidden'
    };

    let oCollectionCreationViewProps = {
      onClose: this.closePopups,
      collectionPopOverWidth: this.props.collectionPopOverWidth,
      filterContext: this.props.filterContext,
      collectionData: oCollectionData
    };
    let fPopOverCloseHandler;

    if (oState.showStaticCollectionsPopover) {
      oCollectionCreationViewProps.isStatic = true;
      fPopOverCloseHandler = this.handleRequestStaticCollectionsPopoverClose;
    } else if (oState.showDynamicCollectionsPopover) {
      fPopOverCloseHandler = this.handleRequestDynamicCollectionsPopoverClose;
      oCollectionCreationViewProps.isDynamic = true;
    }

    return (<div className={"currentPopOver"}>
      <CustomPopoverView
        className="popover-root"
        open={bShowPopOver}
        anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
        transformOrigin={{horizontal: 'right', vertical: 'top'}}
        onClose={fPopOverCloseHandler}
        anchorEl={this.popOverAnchor.current}
        style={ oStyle}
    >
      <CollectionCreationView {...oCollectionCreationViewProps}/>
    </CustomPopoverView></div>);
  }

  render() {
    var oViewMasterData = this.props.viewMasterData;
    var oDynamicCollectionsSectionView = oViewMasterData.isBookmarkEnabled ? this.getDynamicCollectionsSectionView() : null;
    var oStaticCollectionsSectionView = oViewMasterData.isStaicCollectionEnabled ? this.getStaticCollectionsSectionView() : null;

    return (
        <div className="collectionOptionsContainer">
          {oDynamicCollectionsSectionView}
          {oStaticCollectionsSectionView}
          <div className={"popOverAnchor"} ref={this.popOverAnchor}></div>

        </div>
    );
  }
}

export const view = CollectionOptionsView;
export const events = oEvents;
