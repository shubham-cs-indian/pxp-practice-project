
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import {blue} from '@material-ui/core/colors';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';

const oEvents = {
  VISUAL_ELEMENT_DELETE_ICON_CLICKED: "property_collection_visual_element_delete_icon_clicked",
  VISUAL_ELEMENT_BLOCKER_CLICKED: "property_collection_visual_element_blocker_clicked",
};

const oPropTypes = {
  element: ReactPropTypes.object,
  style: ReactPropTypes.object,
  sectionId: ReactPropTypes.string,
  onRemovePlaceHolderHandler: ReactPropTypes.func,
  disableDrag: ReactPropTypes.bool
};

// @CS.SafeComponent
class PropertyCollectionGridElementView extends React.Component {
  handleRemovePlaceHolder = (oEvent) => {
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: __props.element.id,
      context: __props.context
    };

    if (CS.isFunction(__props.onRemovePlaceHolderHandler)) {
      __props.onRemovePlaceHolderHandler(oInfo);
    } else {
      EventBus.dispatch(oEvents.VISUAL_ELEMENT_DELETE_ICON_CLICKED, this, oEvent, oInfo);
    }

  };

  getHeaderView = (oDragViewModel, sTileIconClass, sLabel) => {

    var oElement = this.props.element;
    var bIsInherited = oElement.isInheritedUI;
    var bIsCutOff = oElement.isCutoffUI;

    var sLockViewClassName = "visualElementLock ";
    sLockViewClassName += bIsInherited ? "locked " : "";
    sLockViewClassName += bIsCutOff ? "unlocked " : "";
    var oLockView = (<div className={sLockViewClassName}></div>);

    return (
        <DragView model={oDragViewModel}>
          <div className="tileHeader propertyCollectionGridHeader">
            <div className={sTileIconClass}></div>
            <div className="tileValue" title={sLabel}>{sLabel}</div>
            <div className="removePlaceholder" title={getTranslation().DELETE}
                 onClick={this.handleRemovePlaceHolder}>
            </div>
            {oLockView}
          </div>
        </DragView>
    );
  };

  handleBlockerClicked = (oEvent) => {
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: __props.element.id,
      context: __props.context
    };

    EventBus.dispatch(oEvents.VISUAL_ELEMENT_BLOCKER_CLICKED, this, oEvent, oInfo);
  };

  getElementBlockerView = () => {
    var oElement = this.props.element;
    var sElementBlocker = "elementBlocker ";

    sElementBlocker += oElement.isInheritedUI ? "elementBlockerActive " : "";

    return (<div className={sElementBlocker} onClick={this.handleBlockerClicked}></div>);
  };

  getPlaceHolderView = (sType, oDragViewModel, oElement) => {
    var sKey = '';
    var sLabel = oElement.label;
    var sTileIconClass = "tileIcon ";
    sTileIconClass += sType ? "tileIcon" + sType : "";
    if (sType === "Attribute" || sType === "Tag" || sType === "Taxonomy") {
      sLabel = !CS.isEmpty(sLabel) ? (sLabel + " - " + oElement.code) : CS.getLabelOrCode(oElement);
    }
    var oTileHeaderDOM = this.getHeaderView(oDragViewModel, sTileIconClass, sLabel);
    var oBlockerDom = this.getElementBlockerView();

    return (
        <div className="sectionTileContainer" key={sKey}>
          {oTileHeaderDOM}
          <div className="sectionTileForm">
          </div>
          {oBlockerDom}
        </div>
    );

  };

  getVisualElement = () => {
    var oVisualElement = this.props.element;
    var oVisualElementName = oVisualElement.data ? oVisualElement.data.label : "";
    let sVisualElementCode = oVisualElement.data && oVisualElement.data.code || "";
    var oVisualAttributeNode = null;

    var oProperties = {};
    var oVisualAttributeData = {
      id: oVisualElement.type,
      dataId: oVisualElement.id,
      sectionId: this.props.sectionId,
      code: sVisualElementCode
    };

    oProperties.data = {visualAttributeData: oVisualAttributeData};
    let bIsDraggable = !this.props.disableDrag;
    var oDragViewModel = new DragViewModel(oVisualElement.id, oVisualElementName, bIsDraggable, "dragFromWithinSection", oProperties);

    var oVisualData = oVisualElement.data || {};
    if (oVisualElement.type === 'attribute') {
        oVisualAttributeNode = this.getPlaceHolderView('Attribute', oDragViewModel, oVisualData);
    } else if (oVisualElement.type === 'tag') {
      oVisualAttributeNode = this.getPlaceHolderView('Tag', oDragViewModel, oVisualData);
    } else if (oVisualElement.type === 'role') {
      oVisualAttributeNode = this.getPlaceHolderView('Role', oDragViewModel, oVisualData);
    } else if (oVisualElement.type === 'relationship') {
      oVisualAttributeNode = this.getPlaceHolderView('Relationship', oDragViewModel, oVisualData);
    } else if (oVisualElement.type === 'taxonomy') {
      oVisualAttributeNode = this.getPlaceHolderView('Taxonomy', oDragViewModel, oVisualData);
    }

    return oVisualAttributeNode;
  };

  render() {

    var oStyle = this.props.style;

    return (
        <div className="visualSectionElementContainer" style={oStyle}>
          {this.getVisualElement()}
        </div>
    );
  }
}

export const view = PropertyCollectionGridElementView;
export const events = oEvents;
