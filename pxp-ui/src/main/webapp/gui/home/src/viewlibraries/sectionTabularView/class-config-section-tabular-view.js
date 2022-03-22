import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomDialogView } from '../../viewlibraries/customdialogview/custom-dialog-view';
import TooltipView from '../tooltipview/tooltip-view';
import { view as ContentAttributeElementView } from '../attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from '../attributeelementview/model/content-attribute-element-view-model';
import { view as TagGroupView } from '../taggroupview/tag-group-view.js';
import MockDataForElementProperties from '../../commonmodule/tack/mock-data-for-element-properties';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import DragViewModel from './../dragndropview/model/drag-view-model';
import { view as DragView } from './../dragndropview/drag-view.js';
import { view as DropView } from './../dragndropview/drop-view.js';
import DropViewModel from './../dragndropview/model/drop-view-model';
import { view as GridYesNoPropertyView } from './../gridview/grid-yes-no-property-view';
import { view as MultiSelectView } from './../multiselectview/multi-select-search-view';
import AttributeTypeDictionary from '../../commonmodule/tack/attribute-type-dictionary-new';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import TagTypeConstants from '../../commonmodule/tack/tag-type-constants';

const oEvents = {
  /**Warning : Plz dont add event in here, instead pass the events from calling view */
};

const oPropTypes = {
  fields: ReactPropTypes.array,
  sectionId: ReactPropTypes.string,
  sectionLabel: ReactPropTypes.string,
  context: ReactPropTypes.string,
  isInherited: ReactPropTypes.bool,
  isSkipped: ReactPropTypes.bool,
  isCollapsed: ReactPropTypes.bool,

  sectionElementCheckboxToggleHandler: ReactPropTypes.func,
  sectionElementInputChangeHandler: ReactPropTypes.func,
  sectionToolbarIconChangeHandler: ReactPropTypes.func,
  visualElementBlockerClickHandler: ReactPropTypes.func,
  tagDialogCloseClickHandler: ReactPropTypes.func,
  sectionElementMSSValueChanged: ReactPropTypes.func,
  sectionToggleButtonClicked: ReactPropTypes.func,

  masterAttributeList: ReactPropTypes.object,
  isTagDialogVisible: ReactPropTypes.bool,
  masterTagList: ReactPropTypes.array
};
/**
 * @class ClassConfigSectionTabularView - Use for Display tabular view in setting screen - data model section view.
 * @memberOf Views
 * @property {array} [fields] - Pass fileds of section like id, label, type, isCutoffUI, isInheritedUI,
 * isMultiselect, isSkipped, masterEntity.
 * @property {string} [sectionId] - Pass Id of section.
 * @property {string} [sectionLabel] - Pass label of section.
 * @property {string} [context] - Pass context.
 * @property {bool} [isInherited] - when true then can not delete that section.
 * @property {bool} [isSkipped] - for skip the section.
 * @property {bool} [isCollapsed] - when true section collapsed.
 * @property {func} [sectionElementCheckboxToggleHandler] - Pass function use for toggle handler when click on checkbox.
 * @property {func} [sectionElementInputChangeHandler] - Pass func for Input change handler.
 * @property {func} [sectionToolbarIconChangeHandler] - Pass func when toolbar icon change.
 * @property {func} [visualElementBlockerClickHandler] - Pass fun for visual element blocker clicked.
 * @property {func} [tagDialogCloseClickHandler] - func for tag dialog close handler.
 * @property {func} [sectionElementMSSValueChanged] - fun for section value changed.
 * @property {func} [sectionToggleButtonClicked] - fun for section toggle button clicked.
 * @property {object} [masterAttributeList] - Pass attribute list.
 * @property {bool} [isTagDialogVisible] - when true tag dialog visible.
 * @property {array} [masterTagList] - Pass data like allowedTags, code, childre, label, klass, type, tooltip etc.
 */

// @CS.SafeComponent
class ClassConfigSectionTabularView extends React.Component{

  constructor(props) {
    super(props);
  }

  handleInputCheckboxChanged =(sElementId, sProperty)=> {
    var sSectionId = this.props.sectionId;
    this.props.sectionElementCheckboxToggleHandler(sSectionId, sElementId, sProperty);
  }

/*
  handleInputTextChanged =(sElementId, sProperty, oEvent)=> {
    var sSectionId = this.props.sectionId;
    var sNewValue = oEvent[0] ;
    this.props.sectionElementInputChangeHandler(sSectionId, sElementId, sProperty, sNewValue);
  }
*/

  handleAttributeInputTextChanged =(sElementId, sProperty, oData)=> {
    var sSectionId = this.props.sectionId;
    var sNewValue = !CS.isEmpty(oData.valueAsHtml) ? oData : oData.value;
    this.props.sectionElementInputChangeHandler(sSectionId, sElementId, sProperty, sNewValue);
  }

  handleSectionToolbarIconClicked =(sIconType)=> {
    var sSectionId = this.props.sectionId;
    var sContext= this.props.context;
    this.props.sectionToolbarIconChangeHandler(sContext, sSectionId, sIconType)
  }

  handleSectionMSSChanged =(sElementId,sProperty,aSelectedItems)=> {
    var sSectionId = this.props.sectionId;
    this.props.sectionElementMSSValueChanged(sSectionId, sElementId,sProperty, aSelectedItems);
  }

  getIconClassName =(sType)=> {
    var sClassName = "tileIcon ";
    switch (sType) {
      case "attribute":
        sClassName += "tileIconAttribute";
        break;
      case "tag":
        sClassName += "tileIconTag";
        break;
      case "role":
        sClassName += "tileIconRole";
        break;
      case "relationship":
        sClassName += "tileIconRelationship";
        break;
      case "taxonomy":
        sClassName += "tileIconTaxonomy";
        break;
    }
    return sClassName
  }

  getContentAttributeElementViewModel =(oField, oColumn)=> {
    var oSectionElementDetails = {
      sectionId: this.props.sectionId,
      elementId: oField.id
    };
    var oProperties = {
      hideDescription: true,
      sectionElementDetails: oSectionElementDetails
    };
    if (!CS.isEmpty(oField.defaultUnit)) {
      oProperties.defaultUnit = oField.defaultUnit;
    }
    if (oField.precision && (CS.isNumber(oField.precision.selectedItems[0]))) {
      oProperties.precision = oField.precision.selectedItems[0];
    }
    var sId = oField.id + "%$#@!" + oColumn.id;
    var sRef = sId;
    var sLabel = "";
    var sNewValue = oField[oColumn.id];
    var bIsDisabled = false;
    var sErrorText = "";

    var oMasterAttribute = {};
    if(!CS.isEmpty(oField.masterEntity)){
      oMasterAttribute = oField.masterEntity;
    }else{
      var oMasterAttributeList = this.props.masterAttributeList;
      oMasterAttribute = oMasterAttributeList[oField.attributeId] || {};
    }

    if(oMasterAttribute.calculatedAttributeUnit){
      bIsDisabled = true;
    }

    /**Add Condition To Set Input Field To Accept Numeric Value Only*/
    if (oColumn.type === "number") {
      oProperties.viewAttributeType = AttributeTypeDictionary.NUMBER;
    }

    return (new ContentAttributeElementViewModel(sId, sLabel, sNewValue, bIsDisabled, sErrorText, sRef, oMasterAttribute, oProperties));
  }

  closeDialog =(oTagGroupModel)=> {
    this.props.tagDialogCloseClickHandler(oTagGroupModel);
  }

  getPropertyCellView =(oField, oColumn)=> {
    if (oField[oColumn.id] === undefined || (oField.masterEntity.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN && oColumn.id === "selectedTagValues")) {
      return null;
    }
    var _this = this;
    var oInputView = null;
    var sType = oColumn.type;
    if (sType == "userDefined") {
      sType = oField[oColumn.id + "Type"];
    }
    let sColumnId = oColumn.id;
    switch (sType) {
      case "checkbox":
        oInputView = (
            <input type="checkbox" key={oField.id + "_" + oColumn.id}
                   checked={oField[oColumn.id]}
                   className="sectionCellCheckbox"
                   onChange={_this.handleInputCheckboxChanged.bind(_this, oField.id, oColumn.id)}/>
        );
        break;
      case "yesno":
        oInputView = (<GridYesNoPropertyView propertyId={oField.id} value={oField[oColumn.id].value}
                                             isDisabled={oField[oColumn.id].isDisabled}
                                             onChange={_this.handleInputCheckboxChanged.bind(_this, oField.id, oColumn.id)} key={oField.id + "_" + oColumn.id}/>);
        break;
      case "number":
        var oElementViewModel = _this.getContentAttributeElementViewModel(oField, oColumn);
        oInputView = (<ContentAttributeElementView model={oElementViewModel} key={oField.id + "_" + oColumn.id}
                                                   onBlurHandler={_this.handleAttributeInputTextChanged.bind(_this, oField.id, oColumn.id)}/>);
        break;
      case "text":
        var oElementViewModel = _this.getContentAttributeElementViewModel(oField, oColumn);
        oInputView = (<ContentAttributeElementView model={oElementViewModel} key={oField.id + "_" + oColumn.id}
                                                   onBlurHandler={_this.handleAttributeInputTextChanged.bind(_this, oField.id, oColumn.id)}/>);
      {/*<input type="text" value={oField[oColumn.id]}
       className="sectionCellText"
       onChange={_this.handleInputTextChanged.bind(_this, oField.id, oColumn.id)}/>*/}
        break;
      /*case "select":
        var aSelectViews = [];
        var bIsDisabled = oField[oColumn.id].isDisabled;
        var aSelectedValue = [oField[oColumn.id].value];
        oInputView = (<MultiSelectView
            items={oField[oColumn.id].selectValues}
            selectedItems={aSelectedValue}
            isMultiSelect={false}
            isLoadMoreEnabled={oField[oColumn.id].isLoadMoreEnabled}
            context={oColumn.id}
            searchText={oField[oColumn.id].searchText}
            onApply={_this.handleInputTextChanged.bind(_this, oField.id, oColumn.id)}
            disabled={bIsDisabled}
            cannotRemove = {true}
        />);
      /!*  CS.forEach(oField[oColumn.id].selectValues, function (oOption) {
          aSelectViews.push(<option value={oOption.id}>{oOption.label || oOption.name}</option>);
        });

        var oClassStyle = bIsDisabled ? "sectionCellSelect isDisabled" : "sectionCellSelect";
        oInputView = (
            <select value={oField[oColumn.id].value}
                    className={oClassStyle}
                    disabled={!!bIsDisabled}
                    onChange={_this.handleInputTextChanged.bind(_this, oField.id, oColumn.id)}>
              {aSelectViews}
            </select>
        );*!/
        break;*/
      case "tagGroup":
        var oModel = oField[oColumn.id];
        var bIsTagDialogVisible = _this.props.isTagDialogVisible;
        var sPlaceholder = (oColumn.id === "selectedTagValues") ? getTranslation().ALL : null;
        let oProperties = oModel.tagGroupModel.properties;
        if (bIsTagDialogVisible && !oModel.tagGroupList[oModel.tagGroupModel.id].collapsed) {
          oInputView = (<CustomDialogView key={oField.id + "_" + oColumn.id}
              open={bIsTagDialogVisible}
              className="tagDialogContainer"
              contentClassName="tagDialogContent"
              onRequestClose={this.closeDialog.bind(this, oModel.tagGroupModel)}
              bodyClassName="tagDialogBody">
            <div className="tagDialogWrapper">
              <div className="tagDialogWrapperHeader">
                <TooltipView label={getTranslation().CLOSE}>
                  <div className="tagDialogWrapperHeaderCloseIcon" onClick={this.closeDialog.bind(this, oModel.tagGroupModel)}></div>
                </TooltipView>
              </div>
              <div className="tagDialogWrapperBody">
                <TagGroupView
                    tagGroupModel={oModel.tagGroupModel}
                    tagRanges={oProperties.tagRanges}
                    tagValues={oModel.tagValues}
                    disabled={oModel.disabled}
                    singleSelect={oProperties.singleSelect}
                    hideTooltip= {true}
                    masterTagList={_this.props.masterTagList}
                    customRequestObject={oModel.customRequestObject}
                />
              </div>
            </div>
          </CustomDialogView>);
        } else {
          oInputView = (
              <TagGroupView
                  tagGroupModel={oModel.tagGroupModel}
                  tagRanges={oProperties.tagRanges}
                  tagValues={oModel.tagValues}
                  disabled={oModel.disabled}
                  singleSelect={oProperties.singleSelect}
                  hideTooltip= {true}
                  masterTagList={_this.props.masterTagList}
                  customPlaceholder={sPlaceholder}
                  customRequestObject={oModel.customRequestObject}
                  key={oField.id + "_" + oColumn.id}
              />
          );
        }
        break;

      case "mss":
        let bCannotRemove = true;
        let bIsMultiSelect = false;
        let sCustomPlaceholder = null;
        let aSelectedItems = oField[oColumn.id].selectedItems;
        if (oColumn.id === "selectedTagValues") {
          bIsMultiSelect = oField[oColumn.id].isMultiSelect;
          bCannotRemove = false;
          sCustomPlaceholder = (CS.isEmpty(aSelectedItems) && oColumn.id === "selectedTagValues") ? getTranslation().ALL : null;
        }
        oInputView = (<MultiSelectView
            items={oField[oColumn.id].items}
            selectedItems={aSelectedItems}
            isMultiSelect={bIsMultiSelect}
            isLoadMoreEnabled={oField[oColumn.id].isLoadMoreEnabled}
            context={oColumn.id}
            searchText={oField[oColumn.id].searchText}
            onApply={_this.handleSectionMSSChanged.bind(_this, oField.id, oColumn.id)}
            disabled={oField[oColumn.id].isDisabled}
            cannotRemove={bCannotRemove}
            customPlaceholder={sCustomPlaceholder}
            key={oField.id + "_" + oColumn.id}
        />);
        break;

      case "lazyMSS":
        const oMSSData = oField[sColumnId] ;
        const oStyle = {
          maxWidth: '90%',
          maxHeight: '350px'
        };
        oInputView = (
            <LazyMSSView
                {...oMSSData}
                context={sColumnId}
                showColor={true}
                onApply={_this.handleSectionMSSChanged.bind(_this, oField.id, sColumnId)}
                popoverStyle={oStyle}
            />
        );
        break;
    }
    return oInputView;
  };

  handleBlockerClicked =(sElementId)=> {
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: sElementId
    };
    this.props.visualElementBlockerClickHandler(oInfo);
  }

  getElementBlockerView =(oElement)=> {
    var sElementBlocker = "permissionRowMask ";

    sElementBlocker += oElement.isInheritedUI ? "permissionRowMaskActive " : "";
    return (<div className={sElementBlocker} onClick={this.handleBlockerClicked.bind(this, oElement.id)}></div>);
  }

  getDragViewForSection =(oDragViewModel)=> {
    if(!CS.isEmpty(oDragViewModel)){
      var sSectionId = this.props.sectionId;
      return (
          <div className="sectionToolbarDragIcon">
            <DragView model={oDragViewModel} key={sSectionId}>
              <div className="sectionDragIcon dragIcon"></div>
            </DragView>
          </div>
      );
    }
  }

  handleSectionExpandToggle = () => {
    if(this.props.sectionToggleButtonClicked) {
      var sSectionId = this.props.sectionId;
      var sContextId = this.props.context;
      this.props.sectionToggleButtonClicked(sContextId, sSectionId);
    }
  }

  render() {
    var _props = this.props;
    var _this = this;
    var oContextButton = {};

    var aColumns = _props.context == "runtimeTaxonomySections" ?
        MockDataForElementProperties.runtimeTaxonomyElementProperties() : MockDataForElementProperties.configElementProperties();

    var iColumnWidth = 0;
    CS.forEach(aColumns, function (oColumn) {
      iColumnWidth += oColumn.width || 100;
    });
    let iRowWidth = 200 + iColumnWidth;
    let oRowStyle = {
      width: (iRowWidth) + "px"
    };
    let oFieldNameStyle= {
      flex: "1 0 200px"
    };

    let oSectionHeaderViews = null;
    if(!CS.isEmpty(_props.fields) && !_props.isCollapsed) {
      var aHeaderFields = [<div className="permissionRowItem permissionFieldName " style={oFieldNameStyle}></div>];
      CS.forEach(aColumns, function (oColumn, iColumnIndex) {
        let oRowItemStyle = {
          flex: "0 0 "+ oColumn.width + "px"
        };
        var sClassName = "permissionRowItem permissionColumnLabel ";
        sClassName += oColumn.className || "";
        aHeaderFields.push(<TooltipView placement="bottom" label={CS.getLabelOrCode(oColumn)} key={iColumnIndex}>
          <div className={sClassName} style={oRowItemStyle}>{CS.getLabelOrCode(oColumn)}</div>
        </TooltipView>);
      });
      oSectionHeaderViews = (<div className="permissionHeaderRow" style={oRowStyle}>{aHeaderFields}</div>);
    }

    var aSectionRows = [];
    if (!_props.isCollapsed) {
      CS.forEach(_props.fields, function (oField, iFieldIndex) {
        var sLabel = CS.getLabelOrCode(oField);
        if (oField.type === "attribute" || oField.type === "tag") {
          let oMasterEntity = oField.masterEntity;
          sLabel += " - " + (oMasterEntity.code);
        }
        var aRowItems = [
          <TooltipView label={sLabel} key={sLabel+iFieldIndex}>
            <div className="permissionRowItem permissionFieldName " style={oFieldNameStyle}>{sLabel}</div>
          </TooltipView>];
        CS.forEach(aColumns, function (oColumn, iColumnIndex) {
          var oInputView = _this.getPropertyCellView(oField, oColumn);
          var sClassName = "permissionRowItem permissionFieldEnabled ";
          sClassName += oColumn.className || "";
          if (oColumn.id === "isVariating" && oField.isVariating) {
            oContextButton = (<div className="contextButton"></div>);
          } else {
            oContextButton = null;
          }
          let oFieldStyle = {
            flex: "0 0 " + oColumn.width + "px"
          };
          aRowItems.push(<div className={sClassName} style={oFieldStyle} key={iColumnIndex}>
            {oInputView}{oContextButton}
          </div>);
        });

        var sRowClassName = _this.getIconClassName(oField.type);

        var oElementBlockerView = _this.getElementBlockerView(oField);

        aSectionRows.push(
            <div className="permissionRow" style={oRowStyle} key={iFieldIndex}>
              {oElementBlockerView}
              <div className={sRowClassName}></div>
              {aRowItems}
            </div>
        );
      });
    }

    var oSectionsView = (aSectionRows);

    var oDragDetails = {};
    var sDraggedId = "";
    var bDragStatus = false;

    if (!CS.isEmpty(_props.dragDetails)) {
      oDragDetails = _props.dragDetails.dragFromWithinClassSections;
      sDraggedId = oDragDetails.draggedSectionId;
      bDragStatus = (sDraggedId == _props.sectionId) ? false : oDragDetails.dragStatus;
    }

    var sDragHereSectionClassName = bDragStatus ? "classSectionDragHere isVisible" : "classSectionDragHere";
    var sDraggedSectionClassName = (sDraggedId == _props.sectionId) ? "classConfigTabularPermissionContainer isDragged" : "classConfigTabularPermissionContainer";

    var bIsDroppable = !_props.isInherited;
    var oDragViewModel = new DragViewModel(_props.sectionId, _props.sectionLabel, true, "dragFromWithinClassSections", {});
    var oDropModel = new DropViewModel(_props.sectionId, bIsDroppable, "dragFromWithinClassSections", {});

    var oDragView = null;
    var oDragHereView = null;
    if(!_props.isInherited){
      oDragView = this.getDragViewForSection(oDragViewModel);
      oDragHereView = <div className={sDragHereSectionClassName}></div>;
    }

    /*[
      <div className="sectionToolbarItemLabel">{getTranslation().SKIP}</div>,
      <input type="checkbox" checked={_props.isSkipped}
             onChange={this.handleSectionToolbarIconClicked.bind(this, 'isSkipped')}/>,
    ]*/

    var oSectionToolbarIcons = null;
    if(_props.context != "runtimeTaxonomySections"){
      oSectionToolbarIcons = _props.isInherited ?
          []
           :
          [
            <div className="sectionToolbarIcon removeIcon" key="remove"
                 onClick={this.handleSectionToolbarIconClicked.bind(this, 'remove')}></div>,
            /*(<div className="sectionToolbarIcon downIcon"
                 onClick={this.handleSectionToolbarIconClicked.bind(this, 'down')}></div>),
            (<div className="sectionToolbarIcon upIcon"
                 onClick={this.handleSectionToolbarIconClicked.bind(this, 'up')}></div>)*/
          ];
    }

    let sSectionToggleClass = "sectionToggle sectionExpanded";
    if (_props.isCollapsed) {
      sSectionToggleClass = "sectionToggle sectionCollapsed";
    }

    return (
        <DropView model={oDropModel} key={_props.sectionId}>
          {/*{oDragHereView}*/}
          <div className={sDraggedSectionClassName}>
            <div className="classConfigPermissionContainerHeader">
              {/*{oDragView}*/}
              <div className={sSectionToggleClass} onClick={this.handleSectionExpandToggle}></div>
              <span className="sectionName">{_props.sectionLabel}</span>
              <div className="sectionToolbar">{oSectionToolbarIcons}</div>
            </div>
            <div className="classConfigPermissionContainerBody">
              <div className="permissionSectionListContainer">
                {oSectionHeaderViews}
                {oSectionsView}
              </div>
            </div>
          </div>
        </DropView>
    );
  }
}

ClassConfigSectionTabularView.propTypes = oPropTypes;

export const view = ClassConfigSectionTabularView;
export const events = oEvents;
