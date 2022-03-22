import CS from "../../../../../libraries/cs";
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {view as ConfigHeaderView} from "../../../../../viewlibraries/configheaderview/config-header-view";
import {view as AdministrationSummaryItemDataView} from "./administration-summary-item-data-view";

const oEvents = {
  EXPAND_ARROW_BUTTON_CLICKED: "expand_arrow_button_clicked",
};

const oPropTypes = {
  administrationSummaryData: ReactPropTypes.array,
};


// @CS.SafeComponent
class AdministrationSummaryView extends React.Component {
  static propTypes = oPropTypes;

  handleTileArrowClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.EXPAND_ARROW_BUTTON_CLICKED, sButtonId);
  };

  getConfigHeaderView = () => {
    let oRefreshHeaderAction = {
      id: 'refresh_administration_summary',
      className: 'actionItemRefresh',
      title: 'REFRESH'
    };
    return (<ConfigHeaderView
        actionButtonList={[oRefreshHeaderAction]}
        hideSearchBar={true}
        context={"administrationSummary"}/>)
  };

  getAdministrationsTileView = () => {
    let aAdministrationSummaryData = this.props.administrationSummaryData;
    let aTileDOMColumnFirst = [];
    CS.forEach(aAdministrationSummaryData, (oData) => {
      let sClassName = oData.isExpanded ? "expandIcon expanded" : "expandIcon";
      let oDOM = (
          <div className={"administrationTileView"}>
            <div className={"administrationTileHeaderView"}>
              <div className={"tileLabel"}>{oData.label}</div>
              <div className={sClassName} onClick={this.handleTileArrowClicked.bind(this, oData.id)}></div>
            </div>
            {oData.isExpanded ?
             <AdministrationSummaryItemDataView administrationSummaryData={oData}/> :
             null}
          </div>
      );
      if (!aTileDOMColumnFirst[oData.Column - 1]) {
        aTileDOMColumnFirst[oData.Column - 1] = [];
      }
      aTileDOMColumnFirst[oData.Column - 1].push(oDOM);
    });
    return (
        <div className={"administrationSummaryTabView"}>
          <div className={"administrationSummaryColumnOne"}>
            {aTileDOMColumnFirst[0]}
          </div>
          <div className={"administrationSummaryColumnTwo"}>
            {aTileDOMColumnFirst[1]}
          </div>
          <div className={"administrationSummaryColumnThree"}>
            {aTileDOMColumnFirst[2]}
          </div>
        </div>
    )
  };

  render () {
    let oHeaderView = this.getConfigHeaderView();

    return (
        <div className={"administrationSummaryViewWrapper"}>
          {oHeaderView}
          {this.getAdministrationsTileView()}
        </div>

    );
  }
}


export {AdministrationSummaryView as view} ;
export {oEvents as events} ;

