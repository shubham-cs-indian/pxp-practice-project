import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ColgroupView } from './colgroup-view';
import { view as TableHeaderView } from './table-header-view';
import { view as TableBodyView } from './table-body-view';
import { view as PaginationView } from './../paginationview/pagination-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  TABLE_VIEW_PAGINATION_CHANGED: "table_view_pagination_changed"
};

const oPropTypes = {
  tableContextId:ReactPropTypes.string,

  headerData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        value: ReactPropTypes.string,
        width: ReactPropTypes.number
      })
  ),

  data: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        properties: ReactPropTypes.shape({
          id: ReactPropTypes.string,
          value: ReactPropTypes.string,
          rowSpan: ReactPropTypes.number,
          colSpan: ReactPropTypes.number
        })
      })
  ),

  settings: ReactPropTypes.shape({
    showCheckbox: ReactPropTypes.bool,
    showEditButton: ReactPropTypes.bool,
    showDeleteButton: ReactPropTypes.bool,
    showOpenButton: ReactPropTypes.bool,
    context: ReactPropTypes.string,
    toolbar: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          tooltip: ReactPropTypes.string,
          className: ReactPropTypes.string
        })
    ),
    paginationData: ReactPropTypes.shape({
      from: ReactPropTypes.number,
      pageSize: ReactPropTypes.number,
      totalItems: ReactPropTypes.number
    }),
    showPagination: ReactPropTypes.bool,
    sortData: ReactPropTypes.shape({
      sortBy: ReactPropTypes.string,
      sortOrder: ReactPropTypes.string
    })
  }),

  onSortChange: ReactPropTypes.func,
  onPaginationChange: ReactPropTypes.func,

  referencedAttributes: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  isDisableTableView: ReactPropTypes.bool,
  isRowSelectionMode: ReactPropTypes.bool,
  rowSelectionData: ReactPropTypes.object,
  context: ReactPropTypes.string
};
/**
 * @class TableView - Use to dispaly Tbale view.
 * @memberOf Views
 * @property {string} [tableContextId] - Pass id of table.
 * @property {array} [headerData] - Pass data of Header in Table.
 * @property {array} [data] - Pass data of table like id, mdmKlassInstanceId, originalInstanceId, Properties, versionId.
 * @property {custom} [settings]  - Pass model contain showCheckbox, showEditButton, showDeleteButton, showOpenButton, toolbar, id, tooltip, className, sortData, sortBy, sortOrder.
 * @property {func} [onSortChange] - Pass sorted data function on base of text, calculated, date, price etc.
 * @property {func} [onPaginationChange] - Pass function for change on pagination click.
 * @property {object} [referencedAttributes] - Pass object of reference attributes in table like attributeId, createdBy,lastModifiedBy etc.
 * @property {object} [referencedTags] - Pass object of reference tags in article like attributeId, createdBy,lastModifiedBy etc.
 * @property {bool} [isDisableTableView] - Pass boolean for true and false table view.
 */

// @CS.SafeComponent
class TableView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleTablePaginationChanged =(oNewPaginationData)=> {
    EventBus.dispatch(oEvents.TABLE_VIEW_PAGINATION_CHANGED, oNewPaginationData);
  }

  getPaginationViewDOM =()=> {
    var __props = this.props;
    var oSettings = __props.settings;
    var oPaginationData = oSettings.paginationData;
    var aData = this.props.data;
    let aHeaderData = __props.headerData;

    if(CS.isEmpty(oPaginationData) || oPaginationData.totalItems == 0){
      return null;
    }

    let fOnPaginationChange = CS.isFunction(this.props.onPaginationChange) ? this.props.onPaginationChange : this.handleTablePaginationChanged;

    let iLength = 0;
    if(oSettings && oSettings.isTranspose){
      CS.forEach(aHeaderData,function (oHeaderData) {
        if(!oHeaderData.hideColumn){
          iLength++;
        }
      });
      iLength--; // For a transposed table, total count in pagination will number of columns minus 1, i.e., the first column which contains labels of properties (for language variants)
    } else {
      iLength = aData.length;
    }

    return (
        <div className="tableViewPagination">
          <div className="paginationSection">
          <PaginationView onChange={fOnPaginationChange}
                          from={oPaginationData.from}
                          pageSize={oPaginationData.pageSize}
                          currentPageItems={iLength}
                          totalItems={oPaginationData.totalItems}/>
          </div>
        </div>
    )
  }

  getTableView =()=> {
    var __props = this.props;
    var aHeaderData = __props.headerData;
    var oSettings = __props.settings;

    return (
        <table className="tableView">
          {this.props.isRowSelectionMode ? null : <ColgroupView headerData={aHeaderData} settings={oSettings}/>}
          <TableHeaderView tableContextId={__props.tableContextId} headerData={aHeaderData} settings={oSettings} onSortChange={this.props.onSortChange} isRowSelectionMode={this.props.isRowSelectionMode} filterContext={this.props.filterContext}/>
          <TableBodyView tableContextId={__props.tableContextId}
                         headerData={__props.headerData}
                         data={__props.data}
                         settings={__props.settings}
                         referencedAttributes={__props.referencedAttributes}
                         referencedTags={__props.referencedTags}
                         isRowSelectionMode={this.props.isRowSelectionMode}
                         rowSelectionData={this.props.rowSelectionData}
                         context={this.props.context}
                         filterContext={this.props.filterContext}/>
        </table>
    )
  }

  getNothingFoundView =()=> {
    return (
        <div className="nothingFound">{getTranslation().NOTHING_FOUND}</div>
    )
  }

  render() {
    var __props = this.props;
    var oSettings = __props.settings;
    var bShowPagination = oSettings.showPagination;
    var oPaginationView = (bShowPagination && !CS.isEmpty(__props.data)) ? this.getPaginationViewDOM() : null;
    //let bDisableTableView = oSettings.isTranspose && (__props.headerData).length <= 1;
    var oTableView = (CS.isEmpty(__props.data) || __props.isDisableTableView) ? this.getNothingFoundView() : this.getTableView();

    return (
        <div className="tableViewWrapper">
          <div className="tableViewContainer">
            {oTableView}
          </div>
          {oPaginationView}
        </div>
    );
  }

}

TableView.propTypes = oPropTypes;

export const view = TableView;
export const events = oEvents;
