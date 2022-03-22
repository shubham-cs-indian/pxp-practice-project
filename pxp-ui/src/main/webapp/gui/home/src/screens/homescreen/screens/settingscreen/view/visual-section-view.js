import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DropView } from '../../../../../viewlibraries/dragndropview/drop-view.js';
import DropViewModel from '../../../../../viewlibraries/dragndropview/model/drop-view-model';
import { view as SectionGridElementView } from './section-grid-element-view';
import { view as RelationshipSectionView } from './relationship-section-view';
import SectionSettings from './../tack/class-config-settings';
import ViewUtils from './utils/view-utils';

const oEvents = {
  SECTION_CLICKED: "section_clicked",
  SECTION_NAME_CHANGED: "section_name_changed",
  SECTION_ICON_CHANGED: "section_icon_changed",
  SECTION_ICON_REMOVED: "section_icon_removed",
  SECTION_COL_COUNT_CHANGED: "section_col_count_changed",
  SECTION_ROW_COUNT_CHANGED: "section_row_count_changed",
  SECTION_DELETE_CLICKED: "section_delete_clicked",
  SECTION_MOVE_UP_CLICKED: "section_move_up_clicked",
  SECTION_BLOCKER_CLICKED: "section_blocker_clicked",
  SECTION_MOVE_DOWN_CLICKED: "section_move_down_clicked"
};

const oPropTypes = {
  section: ReactPropTypes.object,
  visualAttributeDragStatus: ReactPropTypes.object,
  activeClass: ReactPropTypes.object,
  attributeListModel: ReactPropTypes.array,
  addedTagList: ReactPropTypes.array,
  selectedSectionId: ReactPropTypes.string,
  masterEntitiesForSection: ReactPropTypes.object,
  tagList: ReactPropTypes.array

};

// @CS.SafeComponent
class VisualSectionView extends React.Component {
  constructor(props) {
    super(props);

    this.visualSectionIconUpload = React.createRef();
    this.sectionLabel = React.createRef();
    this.colCountInput = React.createRef();
    this.rowCountInput = React.createRef();
  }

  static propTypes = oPropTypes;

  componentDidUpdate() {
    this.updateTextFieldValues();
  }

  componentDidMount() {
    this.updateTextFieldValues();
  }

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

  updateTextFieldValues = () => {

    if (this.visualSectionIconUpload.current) {
      this.visualSectionIconUpload.current.value = '';
    }

    var oSectionLabel = this.sectionLabel.current;
    var oColumnInput = this.colCountInput.current;
    var oRowInput = this.rowCountInput.current;
    var oSection = this.props.section;

    if (oSectionLabel) {
      oSectionLabel.value = oSection.label;
    }

    if (oColumnInput) {
      oColumnInput.value = oSection.columns;
    }

    if (oRowInput) {
      oRowInput.value = oSection.rows;
    }
    };

  getGridElementHashMap = () => {
    var sSplitter = ViewUtils.getSplitter();
    var oSection = this.props.section;
    var oRes = {};

    CS.forEach(oSection.elements, function (oEl) {
      var iStartX = oEl.startPosition.x;
      var iStartY = oEl.startPosition.y;
      var sKey = iStartX + sSplitter + iStartY;

      oRes[sKey] = oEl;
    });

    return oRes;
  };

  getSectionGridView = () => {
    var __props = this.props;
    var sSplitter = ViewUtils.getSplitter();
    var oSection = this.props.section;
    var iRowCount = oSection.rows || 2;
    var iColCount = oSection.columns || 2;
    var aRowDOM = [];
    var oElMap = this.getGridElementHashMap();

    var iElWidth = 100 / iColCount;

    var visualSectionDragStatus = this.props.visualAttributeDragStatus;
    var sAvailableElementContext = "dragFromAvailableList";
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

        oProperties.dragStatus = oElMap[sId] ? false : visualSectionDragStatus[sWithinSectionContext] ||
        visualSectionDragStatus[sAvailableElementContext];
        oProperties.style = style;
        oProperties.sectionId = oSection.id;

        var oDropModel = new DropViewModel(sId, true, aValidItems, oProperties);

        if (oElMap[sId]) {
          oElDOM = (<SectionGridElementView
              element={oElMap[sId]}
              activeClass={__props.activeClass}
              attributeListModel={__props.attributeListModel}
              visualAttributeDragStatus={__props.visualAttributeDragStatus}
              addedTagList={__props.addedTagList}
              sectionId={oSection.id}
              tagList={__props.tagList}
              masterEntitiesForSection={__props.masterEntitiesForSection}/>)

        }
        var oElementDOM = (
            <DropView model={oDropModel} key={sId}>
              {oElDOM}
            </DropView>
        );

        aElementDom.push(oElementDOM);
      }
      aRowDOM.push(<div className="visualSectionElementsWrapper">{aElementDom}</div>);
    }

    return (<div className="visualSectionRowsWrapper">{aRowDOM}</div>)
  };

  getSectionGridViewAsPerTypeOfSection = (typeOfSection) => {

    if (typeOfSection == "com.cs.config.interactor.entity.RelationshipSection") {
      return this.getSectionGridViewForRelationship();
    } else {
      return this.getSectionGridView();
    }
  };

  getSectionGridViewForRelationship = () => {
    var oSection = this.props.section;
    var oKlassesForRelationship = this.props.klassesForRelationship;
    var oSectionElement = oSection.elements[0].relationshipSide;

    var oBothSides = oSection.elements[0].relationship;
    oSectionElement.sideNo = (oBothSides.side2.id == oSectionElement.id) ? "2" : "1";

    return (<RelationshipSectionView relationshipSideElement={oSectionElement}
                                     klassesForRelationship={oKlassesForRelationship}/> )

  };

  handleOnSectionClick = () => {
    var oSection = this.props.section;

    if (oSection.id != this.props.selectedSectionId) {
      EventBus.dispatch(oEvents.SECTION_CLICKED, this, oSection.id);
    }
  };

  handleSectionNameChanged = (oEvent) => {
    var oSection = this.props.section;
    var sNewValue = oEvent.target.value;
    var sOldValue = oSection.label;

    if (sOldValue != sNewValue) {
      EventBus.dispatch(oEvents.SECTION_NAME_CHANGED, this, sNewValue);
    }
  };

  uploadIcon = () => {
    this.visualSectionIconUpload.current.click();
  };

  handleAddIconClicked = (oEvent) => {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.SECTION_ICON_CHANGED, this, aFiles);
  };

  removeTagIcon = () => {
    EventBus.dispatch(oEvents.SECTION_ICON_REMOVED, this, []);
  };

  handleColCountChange = (oEvent) => {
    var oSection = this.props.section;
    var iColCount = oEvent.target.value || "";
    var iMinAllowed = SectionSettings.minAllowedColumns;
    var iMaxAllowed = SectionSettings.maxAllowedColumns;

    if (oSection.columns != iColCount) {

      iColCount = CS.trim(iColCount) ? iColCount : 1;

      if (iColCount < iMinAllowed) {
        iColCount = iMinAllowed;
      }

      if (iColCount > iMaxAllowed) {
        iColCount = iMaxAllowed;
      }

      EventBus.dispatch(oEvents.SECTION_COL_COUNT_CHANGED, this, oSection, iColCount);
    }
  };

  handleRowCountChange = (oEvent) => {
    var oSection = this.props.section;

    var iRowCount = oEvent.target.value || "";
    var iMinAllowed = SectionSettings.minAllowedRows;
    var iMaxAllowed = SectionSettings.maxAllowedRows;

    if (oSection.rows != iRowCount) {

      iRowCount = CS.trim(iRowCount) ? iRowCount : 1;

      if (iRowCount < iMinAllowed) {
        iRowCount = iMinAllowed;
      }

      if (iRowCount > iMaxAllowed) {
        iRowCount = iMaxAllowed;
      }

      EventBus.dispatch(oEvents.SECTION_ROW_COUNT_CHANGED, this, oSection, iRowCount);
    }
  };

  handleSectionDeleteClicked = () => {
    var oSection = this.props.section;

    EventBus.dispatch(oEvents.SECTION_DELETE_CLICKED, this, oSection.id);
  };

  handleSectionMoveDownClicked = () => {
    var oSection = this.props.section;

    EventBus.dispatch(oEvents.SECTION_MOVE_DOWN_CLICKED, this, oSection.id);
  };

  handleSectionMoveUpClicked = () => {
    var oSection = this.props.section;

    EventBus.dispatch(oEvents.SECTION_MOVE_UP_CLICKED, this, oSection.id);
  };

  handleBlockerClicked = () => {
    var oSection = this.props.section;

    EventBus.dispatch(oEvents.SECTION_BLOCKER_CLICKED, this, oSection.id);
  };

  render() {

    var sClassName = "visualSectionContainer";
    var oSection = this.props.section;
    var sIcon = "";

    var sSectionBlocker = "sectionBlocker ";
    sSectionBlocker += oSection.isInheritedUI ? "sectionBlockerActive " : "";
    // sSectionBlocker += "sectionBlockerActive ";

    var bIsInherited = oSection.isInheritedUI;
    var bIsCutOff = oSection.isCutoffUI;

    var sSelectedSectionId = this.props.selectedSectionId;
    if (sSelectedSectionId == oSection.id) {
      sClassName += " selected";
    }

    var sUploadIconClassName = "visualSectionUploadImageIcon";
    var sRemoveTagIconClass = 'iconRemoveImage';
    var sIconView = "";
    if (!CS.isEmpty(oSection.icon)) {
      sUploadIconClassName = "visualSectionUploadImageNoIcon";
      sIcon = ViewUtils.getIconUrl(oSection.icon);
      sIconView = (<img className="classIcon" src={sIcon}/>);
    } else {
      sRemoveTagIconClass += " dispN"
      sIconView = (<span className="classIcon"></span>);
    }

    var sSectionBaseType = oSection.type;
    var bIsNormalSection = sSectionBaseType == "com.cs.core.config.interactor.entity.propertycollection.Section";

    var oSectionGridView = this.getSectionGridViewAsPerTypeOfSection(sSectionBaseType);

    var oSectionDeleteIcon = (
        <div className="visualSectionDeleteIcon visualSectionToolbarIcon"
             title={getTranslation().REMOVE} onClick={this.handleSectionDeleteClicked}></div>
    );

    var oColumnCounterDom = (
        <div className="visualSectionColCountWrapper">
          <div className="visualSectionDrDnLabel columns" title={getTranslation().COLUMNS}></div>
          <input type="number"
                 ref={this.colCountInput}
                 className="visualSectionColCountInput"
                 min={SectionSettings.minAllowedColumns}
                 max={SectionSettings.maxAllowedColumns}
                 onChange={this.handleColCountChange}
          />
        </div>
    );

    var oRowCounterDom = (
        <div className="visualSectionRowCountWrapper">
          <div className="visualSectionDrDnLabel rows" title={getTranslation().ROWS}></div>
          <input type="number"
                 ref={this.rowCountInput}
                 className="visualSectionRowCountInput"
                 min={SectionSettings.minAllowedRows}
                 max={SectionSettings.maxAllowedRows}
                 onChange={this.handleRowCountChange}
          />
        </div>
    );

    if (!bIsNormalSection) {
      oSectionDeleteIcon = null;
      if (sSectionBaseType != "com.cs.config.interactor.entity.RelationshipSection") {
        oSectionGridView = null;
      }
      oColumnCounterDom = null;
      oRowCounterDom = null;
    }


    var bIsSectionLabelDisabled = false;
    var sLockViewClassName = "visualSectionLock visualSectionToolbarIcon ";
    sLockViewClassName += bIsInherited ? "locked " : "";
    sLockViewClassName += bIsCutOff ? "unlocked " : "";
    var oLockView = (<div className={sLockViewClassName}></div>);

    return (
        <div className={sClassName} onClick={this.handleOnSectionClick}>
          <div className="visualSectionHeader">
            <div className={sSectionBlocker} onClick={this.handleBlockerClicked}></div>
            <div key="icon-upload" className="visualSectionUploadImageIconContainer">
              <div className={sUploadIconClassName}
                   title={getTranslation().UPLOAD_SECTION}
                   onClick={this.uploadIcon}>
              </div>
              <div className={sRemoveTagIconClass}
                   onClick={this.removeTagIcon}
                   title={getTranslation().REMOVE_ICON}>

              </div>
              <div className="visualSectionIconContainer">
                {sIconView}
              </div>
              <input style={{"visibility": "hidden", height: "0", width: "0"}}
                     ref={this.visualSectionIconUpload}
                     onChange={this.handleAddIconClicked}
                     type="file"
                     accept="image/*"/>
            </div>
            <div className="visualSectionTitle">
              <input type="text" ref={this.sectionLabel} onBlur={this.handleSectionNameChanged}
                     disabled={bIsSectionLabelDisabled}/>
            </div>
            <div className="visualSectionToolbar">
              {oSectionDeleteIcon}
              <div className="visualSectionMoveDownIcon visualSectionToolbarIcon" title={getTranslation().MOVE_DOWN}
                   onClick={this.handleSectionMoveDownClicked}></div>
              <div className="visualSectionMoveUpIcon visualSectionToolbarIcon" title={getTranslation().MOVE_UP}
                   onClick={this.handleSectionMoveUpClicked}></div>
              {oLockView}
            </div>
            {oColumnCounterDom}
            {oRowCounterDom}
          </div>
          <div className="visualSectionBody">
            {oSectionGridView}
          </div>
        </div>
    );
  }
}

export const view = VisualSectionView;
export const events = oEvents;
