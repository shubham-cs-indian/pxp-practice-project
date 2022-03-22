import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as TableRowView} from "./table-row-view.js"
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";

const oEvents = {
  RESET_TABLE_VIEW_PROPS: "reset_table_view_props"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  sectionData: ReactPropTypes.object,
  sectionBodyData: ReactPropTypes.object,
  screenContext: ReactPropTypes.object,
  showCode: ReactPropTypes.bool,
};

// @CS.SafeComponent
class TableViewNew extends React.Component {

  constructor (props) {
    super(props);
  }

  componentWillUnmount = () => {
    EventBus.dispatch(oEvents.RESET_TABLE_VIEW_PROPS, this.props.context);
  };

  getTableRowView = (oContent, sRowId) => {
    return (
        <TableRowView
            rowData={oContent}
            context={this.props.context}
            rowId={sRowId}
            screenContext={this.props.screenContext}
            showCode={this.props.showCode}
        />
    )
  };

  getTableView = (oRowData) => {
    var _this = this;
    let aViews = [];
      CS.forEach(oRowData, function (oData) {
        aViews.push(_this.getTableRowView(oData, oData.rowId));
    });

    return aViews
  };

  render () {
    let bHasIsCollapsed = this.props.sectionHeaderData[0].hasOwnProperty("isCollapsed");
    let bIsCollapsed = bHasIsCollapsed && this.props.sectionHeaderData[0].isCollapsed;
    let oTableHeaderView = this.getTableView([this.props.sectionHeaderData]);
    let oTableBodyData = bHasIsCollapsed && bIsCollapsed ?
                         null : this.getTableView(this.props.sectionBodyData);

    return (
        <React.Fragment>
          <div className="tableHeaderData">
            {oTableHeaderView}
          </div>
          <div className="tableRowData">
            {oTableBodyData}
          </div>
        </React.Fragment>
    );
  }

}

TableViewNew.propTypes = oPropTypes;

export const view = TableViewNew;
export const events = oEvents;
