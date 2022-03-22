import CS from '../../libraries/cs';
import React from 'react';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import ReactPropTypes from 'prop-types';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import TagTypeConstants from './../../commonmodule/tack/tag-type-constants';
import ViewLibraryUtils from './../utils/view-library-utils';
import { view as DisconnectedHTMLView } from './../disconnectedhtmlview/disconnected-html-view';
import { view as CustomDatePickerView } from '../customdatepickerview/customdatepickerview.js';
import { view as TagGroupView } from './../taggroupview/tag-group-view';
import { view as ContentAttributeElementView } from './../attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from './../attributeelementview/model/content-attribute-element-view-model';
import { view as VariantLinkedEntitiesView } from '../variantlinkedentitiesview/variant-linked-entities-view';
import oFilterPropType from './../../screens/homescreen/screens/contentscreen/tack/filter-prop-type-constants';


var oRawTheme = {
  zIndex: {
    layer: 1500
  }
};

const oEvents = {
  TABLE_CELL_VIEW_CELL_VALUE_CHANGED: "table_cell_view_cell_value_changed",
  TABLE_CELL_ADD_LINKED_INSTANCE: "table_cell_add_linked_instance",
  TABLE_CELL_SET_ACTIVE_POP_OVER_VARIANT: "table_cell_set_active_pop_over_variant",
  TABLE_CELL_CLEAR_ACTIVE_POP_OVER_VARIANT: "table_cell_clear_active_pop_over_variant",
  TABLE_CELL_REMOVE_LINKED_INSTANCE: "table_cell_remove_linked_instance"
};

const oPropTypes = {
  isCellEditable: ReactPropTypes.bool,
  isCellDisabled: ReactPropTypes.bool,
  rowId: ReactPropTypes.string,
  cellProperty: ReactPropTypes.object,
  headerData: ReactPropTypes.object,
  masterEntity: ReactPropTypes.object,
  context: ReactPropTypes.string,
  tableContextId: ReactPropTypes.string,
  entityType: ReactPropTypes.string,
  isTranspose: ReactPropTypes.bool
};
/**
 * @class TableCellView - Use to Display table cell view.
 * @memberOf Views
 * @property {bool} [isCellEditable] - Pass a boolean for editable cell for true or not for false.
 * @property {bool} [isCellDisabled] -  Pass a boolean for disable cell for true or not for false.
 * @property {string} [rowId] - Pass a cell row id.
 * @property {object} [cellProperty] - Pass properties like id, icon, createdBy, color, jobId, tags, value etc.
 * @property {object} [headerData] - Pass data of Header in Table.
 * @property {object} [masterEntity] - Pass properties like allowedTags, availability, code, children, klass etc.
 * @property {string} [context] - Pass context for view (ex."uomVariants").
 * @property {string} [tableContextId] - Pass Table context Id.
 * @property {string} [entityType] - Pass entity type like tag , attribute etc.
 * @property {bool} [isTranspose] -  Pass a boolean for isTranspose cell for true or not for false.
 */


// @CS.SafeComponent
class TableCellView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showPopover: false
    }

    this.wrapperDivStyle = {
      overflow: "hidden",
      border: "2px solid #1976D2"
    }
  }

  openPopover =(oEvent)=> {
    let _props = this.props;
    let oMasterAttribute = _props.masterEntity;
    let oHeaderData = _props.headerData;

    if (!_props.isCellEditable || ViewLibraryUtils.isAttributeTypeCalculated(oMasterAttribute.type) || _props.isCellDisabled) {
      return;
    }

    let oDOM = oEvent.currentTarget;
    this.wrapperDivStyle.width = oDOM.clientWidth + "px";
    this.wrapperDivStyle.minHeight = oDOM.clientHeight + "px";
    if (_props.entityType == "linkedInstances") {
      let [sContextInstanceId, sEntity] = _props.isTranspose ? [_props.headerData.id, _props.rowId] : [_props.rowId, _props.headerData.id];
      EventBus.dispatch(oEvents.TABLE_CELL_SET_ACTIVE_POP_OVER_VARIANT, _props.context, sContextInstanceId, _props.tableContextId, sEntity, _props.isTranspose);
    }

    this.setState({
      showPopover: true,
    });
  }

  handleDOMMount =(oView)=> {
    this.anchorElement = oView;
  }

  closePopover =()=> {
    if (this.props.entityType == "linkedInstances") {
      EventBus.dispatch(oEvents.TABLE_CELL_CLEAR_ACTIVE_POP_OVER_VARIANT);
    }
      this.setState({
      showPopover: false
    });
  };

  handleCellValueChanged =(oData)=> {
    let _props = this.props;
    let sContext = _props.context;
    let oHeaderData = _props.headerData;
    let sValue = oData.value;
    let oCellData = {
      rowId: _props.rowId,
      columnId: oHeaderData.id,
      value: sValue,
      type: _props.entityType
    };

    if(CS.has(oData, "valueAsHtml")) {
      oCellData.valueAsHtml = oData.valueAsHtml;
      oCellData.expressionId = oData.expressionId;
    }

    EventBus.dispatch(oEvents.TABLE_CELL_VIEW_CELL_VALUE_CHANGED, oCellData, sContext, _props.tableContextId);

    if(this.getShouldUseLayerForClickAway()){ //if "useLayerForClickAway" is true in popover then close it
      this.closePopover();
    }
  }

  handleDateChanged =(oNull, oDate)=>{
    var oData = {
      value: oDate ? oDate.getTime() : ""
    };
    this.handleCellValueChanged(oData);
  }

  getTooltipValueDom =(oValue)=> {
    let aDom = [];
    let aValues =!CS.isArray(oValue) ? oValue.split(',') : oValue;

    let oTooltipValueDom = {
      "text-align": "center"
    };

    if (CS.isEmpty(oValue)) {
      return null;
    }
    CS.forEach(aValues, function (sVal) {
      aDom.push(<span style={oTooltipValueDom}>{sVal}</span>);
      aDom.push(<br/>);
    });

    return aDom
  }

  getDatePickerView =()=> {
    let _props = this.props;
    let oProperty = _props.cellProperty;
    let oDate = null;

    if (!!oProperty.originalValue) {
      oDate = new Date(oProperty.originalValue * 1); //convert oProperty.originalValue to number
    }

    return (<CustomDatePickerView
        value={oDate}
        minDate={oProperty.minValue ? new Date(oProperty.minValue) : undefined}
        maxDate={oProperty.maxValue ? new Date(oProperty.maxValue) : undefined}
        className="datePickerCustom"
        disableClearDate={oProperty.isMandatory}
        onChange={this.handleDateChanged}
    />);
  }

  getAttributeElementView =()=> {
    let _props = this.props;
    let oProperty = _props.cellProperty;
    let oHeaderData = _props.headerData;
    let sValue = oProperty.value;
    let sAttributeId = oHeaderData.id;
    let oMasterAttribute = this.props.masterEntity;
    let oProperties = {};

    if(ViewLibraryUtils.isAttributeTypeDate(oMasterAttribute.type) ||
        ViewLibraryUtils.isAttributeTypeMeasurement(oMasterAttribute.type) ||
        ViewLibraryUtils.isAttributeTypeNumber(oMasterAttribute.type)){
      sValue = oProperty.originalValue;
    }

    if (ViewLibraryUtils.isAttributeTypeConcatenated(oMasterAttribute.type)) {
      oProperties.expressionList = oProperty.expressionList;
    }

    let oAttributeElementModel = new ContentAttributeElementViewModel(
        sAttributeId,
        "",
        sValue,
        false,
        "",
        "",
        oMasterAttribute,
        oProperties
    );

    return (<ContentAttributeElementView key={sAttributeId}
                                         data-id={sAttributeId}
                                         model={oAttributeElementModel}
                                         onBlurHandler={this.handleCellValueChanged}/>);
  };

  handleTableLinkedInstanceAdd = (sViewContext) => {
    EventBus.dispatch(oEvents.TABLE_CELL_ADD_LINKED_INSTANCE, sViewContext, {filterType: oFilterPropType.QUICKLIST, screenContext: oFilterPropType.QUICKLIST});
  };

  handleTableLinkedInstanceRemove = (sLinkedInstanceId) => {
    EventBus.dispatch(oEvents.TABLE_CELL_REMOVE_LINKED_INSTANCE, sLinkedInstanceId);
  };

  getVariantLinkedEntitiesViewData = () => {
    let _props = this.props;
    let oCellProperty =_props.cellProperty;
    let aLinkedInstances = oCellProperty.referencedLinkedEntites;

    return {
      isInstanceRemovable: true,
      canEdit: true,
      variantSectionViewData: {
        selectedVisibleContext: {
          entities: [_props.headerData.id],
        },
        editableVariant: {
          linkedInstances: {[_props.headerData.id]: aLinkedInstances},
        },
        assetRelationshipEntities: oCellProperty.assetRelationshipEntities,
        contextId: _props.tableContextId
      }
    };
  };

  getLinkedInstancesView = () => {
    let _props = this.props;
    let sContext = _props.context;
    let oProperty = _props.cellProperty;
    if (_props.masterEntity) {
      let oVariantLinkedEntitiesData = this.getVariantLinkedEntitiesViewData();
      let fAddHandler = this.handleTableLinkedInstanceAdd.bind(this, sContext);
      return (<VariantLinkedEntitiesView {...oVariantLinkedEntitiesData} addedLinkedInstancesHandler={fAddHandler}
                                         removeLinkedInstanceHandler={this.handleTableLinkedInstanceRemove}/>);
    } else {
      return null;
    }
  };

  getTagElementView =()=> {
    let _props = this.props;
    let sContext = _props.context;
    let oHeaderData = _props.headerData;
    let oProperty = _props.cellProperty;
    let oMasterTag = this.props.masterEntity; // For a transposed table, type is available in masterEntity (for language variants)
    let oTags = {
      [oProperty.id]: oProperty
    };
    let oTagGroupModel = ViewLibraryUtils.getTagGroupModels(oMasterTag, {tags: oTags}, {}, sContext);
    let oExtraData = {
      outerContext: sContext,
      rowId: _props.rowId,
      type: _props.entityType,
      columnId: oHeaderData.id,
      tableContextId: _props.tableContextId
    };
    let oProperties = oTagGroupModel.tagGroupModel.properties;

    return (<TagGroupView
        tagGroupModel={oTagGroupModel.tagGroupModel}
        tagRanges={oProperties.tagRanges}
        tagValues={oTagGroupModel.tagValues}
        disabled={oTagGroupModel.disabled}
        singleSelect={oProperties.singleSelect}
        hideTooltip={false}
        showLabel={false}
        extraData={oExtraData}
        masterTagList={[oMasterTag]}
    />);
  }

  getElementView =()=> {
    let _props = this.props;
    let oProperty = _props.cellProperty;
    let sType = _props.entityType;

    switch (sType) {
      case "tag":
        return this.getTagElementView();

      case "date":
        return this.getDatePickerView();

      case "attribute":
        return this.getAttributeElementView();

      case "linkedInstances":
        return this.getLinkedInstancesView();
    }

    return oProperty.value;
  }

  //TODO: find a better solution
  getShouldUseLayerForClickAway =()=>{
    let _props = this.props;
    let oMasterEntity = _props.masterEntity;

    let sType = _props.entityType;
    if(sType === "attribute"){
      return ViewLibraryUtils.isAttributeTypeDate(oMasterEntity.type);

    } else if (sType === "date" || sType === "tag" || sType === "linkedInstances") {
      return true;
    }

    return false;
  }

  getPopoverView =()=> {
    let oPopoverStyle = {
      width: "auto",
      height: "auto"
    };

    let bShouldUseLayerForClickAway = this.getShouldUseLayerForClickAway();
    let oClonedWrapperDivStyle = CS.cloneDeep(this.wrapperDivStyle);

    return (<CustomPopoverView className="popover-root tableCellPopover"
                     open={this.state.showPopover}
                     style={ oPopoverStyle}
                     animated={false}
                     anchorEl={this.anchorElement}
                     anchorOrigin={{horizontal: 'left', vertical: 'top'}}
                     transformOrigin={{horizontal: 'left', vertical: 'top'}}
                     useLayerForClickAway={bShouldUseLayerForClickAway}
                     onClose={this.closePopover}>
      <div style={oClonedWrapperDivStyle}>
        {this.getElementView()}
      </div>
    </CustomPopoverView>);
  }

  getLinkedInstancesCellView =()=> {
    let oProperty = this.props.cellProperty;
    let sValue = oProperty.value;
    let oTooltipValueDOM = this.getTooltipValueDom(sValue);
    let oCellView = (
        <div className="cellValueContainer">
          {sValue}
        </div>
    );

    return (
        <div className="tableCell"  ref={this.handleDOMMount}>
          <TooltipView placement="bottom" label={oTooltipValueDOM} delay={400}>
            <div className="cellValueContainerWrapper" onClick={this.openPopover}>
              {oCellView}
            </div>
          </TooltipView>
          {this.getPopoverView()}
        </div>
    );
  }

  getTagCellView =()=> {
    let _props = this.props;
    let oCellView = null;
    let oTooltipValueDOM = null;
    let oProperty = _props.cellProperty;
    let oHeaderData = _props.headerData;
    let oMasterEntity = _props.masterEntity;
    let oValue = oProperty.value;
    let sTagType = oHeaderData.tagType || oMasterEntity.tagType; // For a transposed table, type is available in masterEntity (for language variants)

    if (sTagType === TagTypeConstants.TAG_TYPE_BOOLEAN) {
      if (oValue) {
        //When Boolean tag value is true
        oCellView = (
            <div className="checkMark">
              {oValue}
            </div>
        );
      }
      else {
        //When Boolean tag value is false
        oCellView = <div className="cellValueContainer"/>
      }
    } else {
      oTooltipValueDOM = this.getTooltipValueDom(oValue);
      if(CS.isArray(oValue)) {
        oValue = oValue.join(", ");
      }
      oCellView = (
          <div className="cellValueContainer">
            {oValue}
          </div>
      );
    }

    let sTableCellClassName = "tableCell";
    if (_props.isCellDisabled) {
      sTableCellClassName += " disabled";
    }

    return (
        <div className={sTableCellClassName} ref={this.handleDOMMount}>
          <TooltipView placement="bottom" label={oTooltipValueDOM} delay={400}>
            <div className="cellValueContainerWrapper" onClick={this.openPopover}>
              {oCellView}
            </div>
          </TooltipView>
          {this.getPopoverView()}
        </div>
    );
  }

  getDisabledString = (aExpressionList) => {
    var sString = "";
    CS.forEach(aExpressionList, function (oExpression) {
      if(oExpression.type === "html") {
        sString += oExpression.valueAsHtml;
      } else {
        sString += oExpression.value;
      }
    });
    return sString;
  };

  getAttributeCellView =()=> {
    let oCellView = null;
    let _props = this.props;
    let oHeaderData = _props.headerData;
    let oMasterEntity = _props.masterEntity;
    let oProperty = _props.cellProperty;
    let oValue = oProperty.value;
    let sBaseType = oHeaderData.baseType || oMasterEntity.type; // For a transposed table, base type is available in masterEntity (for language variants)
    let bIsConcatenatedAttribute = ViewLibraryUtils.isAttributeTypeConcatenated(sBaseType);

    if (ViewLibraryUtils.isAttributeTypeHtml(sBaseType) || bIsConcatenatedAttribute) {
      let sValue = bIsConcatenatedAttribute && !CS.isEmpty(oProperty.expressionList) ? this.getDisabledString(oProperty.expressionList) : oProperty.value;
      oCellView = (
          <div className="cellHTMLValue">
            <DisconnectedHTMLView content={sValue}/>
          </div>
      );
      oValue = oCellView;
    } else {
      oCellView = (
          <div className="cellValueContainer">
            {oValue}
          </div>
      );
    }

    let sTableCellClassName = "tableCell";
    if (_props.isCellDisabled) {
      sTableCellClassName += " disabled";
    }

    return (
        <div className={sTableCellClassName} ref={this.handleDOMMount}>
          <TooltipView placement="bottom" label={oValue} delay={400}>
            <div className="cellValueContainerWrapper"  onClick={this.openPopover}>
              {oCellView}
            </div>
          </TooltipView>
          {this.getPopoverView()}
        </div>
    );
  }

  render() {
    let _props = this.props;
    let sType = _props.entityType;

    switch (sType) {
      case "tag":
        return this.getTagCellView();

      case "linkedInstances":
        return this.getLinkedInstancesCellView();

      case "attribute":
      case "custom":
        return this.getAttributeCellView();
    }

    let oProperty = _props.cellProperty;

    return (
        <div className="tableCell"
             ref={this.handleDOMMount}>
          <div className="cellValueContainerWrapper"
               onClick={this.openPopover}>
            <div className="cellValueContainer">
              {oProperty.value}
            </div>
          </div>
          {this.getPopoverView()}
        </div>
    );
  }

}

TableCellView.propTypes = oPropTypes;



export const view = TableCellView;
export const events = oEvents;
