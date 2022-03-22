import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as TableCellView} from "./table-cell-view.js"

const oPropTypes = {
  context: ReactPropTypes.string,
  rowData: ReactPropTypes.object,
  screenContext: ReactPropTypes.object,
  showCode: ReactPropTypes.bool,
};

// @CS.SafeComponent
class TableRowView extends React.Component {

  constructor(props) {
    super(props);
  }

  getCellView = (obj) => {
    return (
        <TableCellView
            cellData={obj}
            context={this.props.context}
            rowId={this.props.rowId}
            screenContext={this.props.screenContext}
            showCode={this.props.showCode}
        />
    )
  };

  getTableCellView = () => {
    let rowData = this.props.rowData;
    let _this = this;
    let aViews = [];
    CS.forEach(rowData, function (obj) {
      aViews.push(_this.getCellView(obj));
    });

    return(
      <div className="rowView">
        {aViews}
      </div>
    );

  };

  render() {

    var oRowView = this.getTableCellView();
    return (
        <React.Fragment>
          {oRowView}
        </React.Fragment>
    );
  }

}

TableRowView.propTypes = oPropTypes;

export const view = TableRowView;
