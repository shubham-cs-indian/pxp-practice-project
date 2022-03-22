import React from 'react';
import ReactPropTypes from 'prop-types';
import {oTableRenderTypeConstant} from '../tack/table-render-type';
import {view as GridYesNoPropertyView} from './../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {getTranslations as getTranslation} from "../../../../../commonmodule/store/helper/translation-manager";
import ViewUtils from "../../contentscreen/view/utils/view-utils";
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";

const oEvents = {
  TABLE_SECTION_EXPAND_COLLAPSED_CLICKED: "table_section_expand_collapsed_clicked",
  TABLE_BUTTON_CLICKED_CLICKED: "table_button_clicked_clicked",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  sectionHeaderData: ReactPropTypes.object,
  screenContext: ReactPropTypes.object,
};

// @CS.SafeComponent
class TableCellView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleSectionExpandCollapseClicked = () => {
    EventBus.dispatch(oEvents.TABLE_SECTION_EXPAND_COLLAPSED_CLICKED, this.props.context);
  };

  handleTableButtonClicked = (sCellId) => {
    EventBus.dispatch(oEvents.TABLE_BUTTON_CLICKED_CLICKED, this.props.context, this.props.rowId, sCellId, this.props.screenContext);
  };

  getHeaderLabelViewDOM = (oCellData) => {
    let sLabelClass = oCellData.labelClassName;
    sLabelClass += oCellData.hasOwnProperty("iconClass") ? oCellData.iconClass : "";
    return (<div className={sLabelClass}>{oCellData.label}</div>)
  };

  getTableCellRenderView = (oCellData) => {
    let sClassName = "";
    switch (oCellData.renderType) {
      case oTableRenderTypeConstant.LABEL:
        sClassName = "tableLabel " + oCellData.iconClass;
        let oCollapsedData = null;
        let oHeaderLabelDOM = null;
        if (oCellData.hasOwnProperty("isCollapsed")) {
          oCollapsedData = this.getExpandCollapseDOM(oCellData);
        }
        if(oCellData.hasOwnProperty("label")){
          oHeaderLabelDOM = this.getHeaderLabelViewDOM(oCellData);
        }
        let sLabel = oCellData.value;
        let aLabels = [sLabel];
        let oLabelDOM = (
            <TooltipView label={aLabels}>
              <div className={sClassName}>{oHeaderLabelDOM}{oCollapsedData}{sLabel}</div>
            </TooltipView>
        );

        let oCodeDOMWrapper = null;

        if (this.props.showCode && oCellData.code) {
          let sCode = oCellData.code;
          aLabels.push(<span className="tooltipNodeId" key={sCode}>{sCode}</span>);

          let sClassName = "tableCode ";
          sClassName+= oCellData.iconClass ? "addPadding" : "";
          let oCodeDOM = (
              <TooltipView label={aLabels}>
                <div className={sClassName} style={{width: oCellData.width - 20 + "px"}}>{sCode}</div>
              </TooltipView>
          );
          let oCopyCodeDOM = (<TooltipView placement={"top"} label={getTranslation().COPY_TO_CLIPBOARD_TOOLTIP}>
            <div className={"copyToClipboard"}
                 onClick={ViewUtils.copyToClipboard.bind(this, sCode)}></div>
          </TooltipView>);

          oCodeDOMWrapper = (<div className="tableCellShowCodeContainer">
            {oCodeDOM}
            {oCopyCodeDOM}
          </div>);
        }

        return (
          <div className="tableCellWrapper" style={{width: oCellData.width + "px"}}>
            {oLabelDOM}
            {oCodeDOMWrapper}
          </div>
       );

      case oTableRenderTypeConstant.BOOLEAN:
        sClassName = " tableBoolean " ;
        let oHeaderDOM = null;
        sClassName += oCellData.isPartiallySelected ? " partial" : "";
        sClassName += oCellData.hasOwnProperty("isDisabled") && oCellData.isDisabled ? " disabled " : "";
        if(oCellData.hasOwnProperty("label")){
          oHeaderDOM = this.getHeaderLabelViewDOM(oCellData);
        }
       return (
           <div className={sClassName} style={{width: oCellData.width + "px"}}>{oHeaderDOM}
             <GridYesNoPropertyView value={oCellData.value}
                                    onChange={this.handleTableButtonClicked.bind(this, oCellData.id)}/>
           </div>
       );

      case oTableRenderTypeConstant.CHECKBOX:
        sClassName = " tableCheckbox";
        sClassName += oCellData.value ? " checked" : " unchecked";
        sClassName += oCellData.hasOwnProperty("isDisabled") && oCellData.isDisabled ? " disabled " : "";
        return (
            <div className={sClassName}
                 onClick={this.handleTableButtonClicked.bind(this, oCellData.id)}></div>
        )
    }
  };

  getExpandCollapseDOM (cellData) {
    let sExpandClassName = cellData.isCollapsed ? "collapsed" : "expanded";
    return (<div className={sExpandClassName}
                 onClick={this.handleSectionExpandCollapseClicked}></div>);
  }

  render () {
    let cellData = this.props.cellData;
    var oCellView = this.getTableCellRenderView(cellData);
    return (
        <div className="gridCellWrapper">
          {oCellView}
        </div>
    );
  }

}

TableCellView.propTypes = oPropTypes;

export const view = TableCellView;
export const events = oEvents;
