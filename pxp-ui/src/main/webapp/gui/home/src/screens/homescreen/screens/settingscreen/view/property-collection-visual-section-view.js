
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DropView } from '../../../../../viewlibraries/dragndropview/drop-view.js';
import DropViewModel from '../../../../../viewlibraries/dragndropview/model/drop-view-model';
import { view as PropertyCollectionGridElementView } from './property-collection-grid-element-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  SECTION_CLICKED: "property_collection_section_clicked",
};

const oPropTypes = {
  section: ReactPropTypes.object,
  visualAttributeDragStatus: ReactPropTypes.object,
  selectedSectionId: ReactPropTypes.string
};

// @CS.SafeComponent
class PropertyCollectionVisualSectionView extends React.Component {
  static propTypes = oPropTypes;

  shouldComponentUpdate(oNextProps, oNextState) {
    var sPreviousSectionId = this.props.section.id;
    var sNextSextionId = oNextProps.section.id;
    var sPreviousSelectedSectionId = this.props.selectedSectionId;
    var sNextSelectedSectionId = oNextProps.selectedSectionId;

    if (sNextSelectedSectionId == "") {
      return true;
    }

    if (sPreviousSectionId == sPreviousSelectedSectionId || sNextSextionId == sNextSelectedSectionId) {
      return true;
    }

    return false;
  }

  getGridElementHashMap = () => {
    var sSplitter = ViewUtils.getSplitter();
    var oSection = this.props.section;
    var oRes = {};

    CS.forEach(oSection.elements, function (oEl) {
      var iStartX = oEl.position.x;
      var iStartY = oEl.position.y;
      var sKey = iStartX + sSplitter + iStartY;

      oRes[sKey] = oEl;
    });

    return oRes;
  };

  getSectionGridView = () => {
    var sSplitter = ViewUtils.getSplitter();
    var oSection = this.props.section;
    var iRowCount = oSection.rows || 2;
    var iColCount = oSection.columns || 2;
    var aRowDOM = [];
    var oElMap = this.getGridElementHashMap();
    var iElWidth = 100 / iColCount;
    var visualSectionDragStatus = this.props.visualAttributeDragStatus;
    var sAvailableElementContext = "dragFromPropertyCollectionAvailableList";
    var sWithinSectionContext = "dragFromWithinSection";

    for (var i = 0; i < iRowCount; i++) {
      var aElementDom = [];

      for (var j = 0; j < iColCount; j++) {

        var sId = i + sSplitter + j;
        var oProperties = {};
        var aValidItems = [sAvailableElementContext, sWithinSectionContext];
        var oElDOM = (<span></span>);
        var style = {
          width: iElWidth + "%",
          position: "relative"
        };

        let oElement = oElMap[sId];
        if (oElement && oElement.type === "taxonomy") {
          style.width = "100%";
        }

        oProperties.dragStatus = oElement ? false : visualSectionDragStatus[sWithinSectionContext] ||
        visualSectionDragStatus[sAvailableElementContext];
        oProperties.style = style;
        oProperties.sectionId = oSection.id;

        var oDropModel = new DropViewModel(sId, true, aValidItems, oProperties);

        if (oElement) {
          oElDOM = (<PropertyCollectionGridElementView
            element={oElement}
            sectionId={oSection.id}
            context="propertycollection"
          />)

        }

        var oElementDOM = (
            <DropView model={oDropModel} key={sId}>
              {oElDOM}
            </DropView>
        );

        if (oElement && oElement.type === "taxonomy") {
          aElementDom = [oElementDOM];
          break;
        } else {
          aElementDom.push(oElementDOM);
        }
      }
      aRowDOM.push(<div className="visualSectionElementsWrapper" key={i}>{aElementDom}</div>);
    }

    return (<div className="visualSectionRowsWrapper">{aRowDOM}</div>)
  };

  handleOnSectionClick = (oEvent) => {
    var oSection = this.props.section;

    if (oSection.id != this.props.selectedSectionId) {
      EventBus.dispatch(oEvents.SECTION_CLICKED, this, oSection.id);
    }
  };

  render() {

    var sClassName = "visualSectionContainer";
    var oSection = this.props.section;

    var sSelectedSectionId = this.props.selectedSectionId;
    if (sSelectedSectionId == oSection.id) {
      sClassName += " selected";
    }

    var sSectionBaseType = oSection.type;
    var bIsNormalSection = true;
    var oSectionGridView = this.getSectionGridView();

    var oSectionDeleteIcon = (
        <div className="visualSectionDeleteIcon visualSectionToolbarIcon"
             title={getTranslation().REMOVE}></div>
    );

    if (!bIsNormalSection) {
      oSectionDeleteIcon = null;
      if (sSectionBaseType != "com.cs.config.interactor.entity.RelationshipSection") {
        oSectionGridView = null;
      }
    }

    return (
        <div className={sClassName} onClick={this.handleOnSectionClick}>
          <div className="visualSectionBody">
            {oSectionGridView}
          </div>
        </div>
    );
  }
}

export const view = PropertyCollectionVisualSectionView;
export const events = oEvents;
