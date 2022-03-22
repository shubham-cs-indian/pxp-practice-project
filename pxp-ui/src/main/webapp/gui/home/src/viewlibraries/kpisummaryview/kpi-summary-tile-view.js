import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as ContextMenuView } from './../contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from './../contextmenuwithsearchview/model/context-menu-view-model';

const oEvents = {
  KPI_SUMMARY_VIEW_KPI_SELECTED: "kpi_summary_view_kpi_selected"
};

const oPropTypes = {
  selectedKPIId: ReactPropTypes.string,
  kpiList: ReactPropTypes.array,
  showSummaryDataInHeader: ReactPropTypes.bool
};
/**
 * @class KpiSummaryTileView Used to show KPI summary, KPI are used to show Completeness, Accuracy, Conformity, Uniqueness of tags and attributes through chart.
 * @memberOf Views
 * @property {string} [selectedKPIId] - Contains selected KPI Id.
 * @property {array} [kpiList] - Contains all KPIs information
 * @property {bool} [showSummaryDataInHeader] - To show summary data on header bar of tile(if true show summary data).
 * (Showing a dropdown list which used to change selected KPI in content)
 */

// @CS.SafeComponent
class KpiSummaryTileView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleKpiSelected = (oModel) => {
    let sKpiId = oModel.id;
    EventBus.dispatch(oEvents.KPI_SUMMARY_VIEW_KPI_SELECTED, sKpiId);
  };

  getColorRGBFromRelevance = (iRelevance) => {

    if (iRelevance > 0) {
      return "#152078";
    } else if (iRelevance < 0) {
      return "#FF0000";
    } else {
      return "#FFBC00";
    }

  };

  getClockPercentView = (iPercent) => {
    //todo: remove negative values code
    //todo: change class names
    if (!CS.isNumber(iPercent)) {
      iPercent = 0;
    }
    let iRelevance = iPercent;
    let sOuterCircleColor = "";
    let sDegree1 = "";
    let sShadowColor = "";
    let sDegree2 = "";
    let iFactor = 180 / 50;

    if (iRelevance > 50 || iRelevance < -50) {
      sShadowColor = this.getColorRGBFromRelevance(iRelevance);
      sOuterCircleColor = "#fff";
      sDegree1 = "90deg";
      sDegree2 = 90 + iFactor * (Math.abs(iRelevance) - 50) + "deg";
    } else {
      sOuterCircleColor = this.getColorRGBFromRelevance(iRelevance); //#CE4B0C
      sShadowColor = "#eee";
      sDegree1 = "270deg";
      sDegree2 = 90 + iFactor * Math.abs(iRelevance) + "deg";
    }

    let sLinearGradient = "linear-gradient(" + sDegree1 + ", transparent 50%, " + sShadowColor +
        " 50%), linear-gradient(" + sDegree2 + ", transparent 50%, " + sShadowColor + " 50%)";

    let oStyle = {
      "backgroundColor": sOuterCircleColor,
      "backgroundImage": sLinearGradient
    };

    let sSectionTagLetterClassName = "mnmtSectionTagLetter ";

    return (
        <div className="mnmtSectionTagWrapper" style={oStyle}>
          <div className="mnmtSectionTagInnerCircle">
            <div className={sSectionTagLetterClassName}>{iPercent}</div>
          </div>
        </div>
    );
  };

  getContextMenuModel = (aList) => {
    let aListModels = [];
    let sSelectedKPIId = this.props.selectedKPIId;

    CS.forEach(aList, function (oListItem) {
      if(sSelectedKPIId !== oListItem.id) {
        aListModels.push(new ContextMenuViewModel(
            oListItem.id,
            CS.getLabelOrCode(oListItem),
            false,
            "",
            {context: ""}
        ));
      }
    });
    return aListModels;
  };

  getSummaryView = () => {
    let sSelectedKPIId = this.props.selectedKPIId;
    let aKpiList = this.props.kpiList;
    let oSelectedKpiData = CS.find(aKpiList, {id: sSelectedKPIId});
    let oStatistics = oSelectedKpiData.statistics;
    let aContextMenuViewModels = this.getContextMenuModel(aKpiList);
    let aSelectedItems = [oSelectedKpiData.id];
    let sCompletenessLabel = getTranslation().COMPLETENESS;
    let sAccuracyLabel = getTranslation().ACCURACY;
    let sConformityLabel = getTranslation().CONFORMITY;
    let sUniquenessLabel = getTranslation().UNIQUENESS;
    let oLastModifiedOn = oSelectedKpiData.lastModifiedOn;
    let bShowContextMenuView = this.props.showSummaryDataInHeader;
    let oContextMenuView = null;
    if (bShowContextMenuView) {
      oContextMenuView = (<ContextMenuView contextMenuViewModel={aContextMenuViewModels}
                                           showSearch={false}
                                           onClickHandler={this.handleKpiSelected}
                                           showSelectedItems={false}
                                           selectedItems={aSelectedItems}>
        <div className="kpiSelectorSection">
          <div className="kpiKey">{getTranslation().KPI + ": "}</div>
          <div className="selectedKpiLabel">{CS.getLabelOrCode(oSelectedKpiData)}</div>
          <div className="dropDownIcon"></div>
        </div>
      </ContextMenuView>);
    }
    else {
      oContextMenuView = (<div className="kpiLabel">{CS.getLabelOrCode(oSelectedKpiData)}</div>)
    }

    return (
        <div className="kpiSummaryView">
          {oContextMenuView}
          <div className="kpiSummarySectionContainer">
            <div className="kpiSummarySection">

              <div className="dimensionContainer">
                {this.getClockPercentView(oStatistics.completeness)}
                <TooltipView label={sCompletenessLabel}>
                  <div className="dimensionLabel">{sCompletenessLabel}</div>
                </TooltipView>
              </div>

              <div className="dimensionContainer">
                {this.getClockPercentView(oStatistics.accuracy)}
                <TooltipView label={sAccuracyLabel}>
                  <div className="dimensionLabel">{sAccuracyLabel}</div>
                </TooltipView>
              </div>

              <div className="dimensionContainer">
                {this.getClockPercentView(oStatistics.conformity)}
                <TooltipView label={sConformityLabel}>
                  <div className="dimensionLabel">{sConformityLabel}</div>
                </TooltipView>
              </div>

              <div className="dimensionContainer">
                {this.getClockPercentView(oStatistics.uniqueness)}
                <TooltipView label={sUniquenessLabel}>
                  <div className="dimensionLabel">{sUniquenessLabel}</div>
                </TooltipView>
              </div>

            </div>

            <div className="lastModifiedSection">
              <span className="lastModifiedOnText normal">{getTranslation().KPI_LAST_CALCULATED_ON}</span>
              <span className="lastModifiedOnDate highlighted">{oLastModifiedOn.date}</span>
              <span className="normal">{getTranslation().AT}</span>
              <span className="lastModifiedOnTime highlighted">{oLastModifiedOn.time}</span>
            </div>
          </div>
        </div>
    );
  };

  render () {
    let oSelectedKPIId = this.props.selectedKPIId;
    let aKpiList = this.props.kpiList;
    if (CS.isEmpty(oSelectedKPIId) || CS.isEmpty(aKpiList)) {
      return null;
    }
    return this.getSummaryView();
  }
}

KpiSummaryTileView.propTypes = oPropTypes;

export const view = KpiSummaryTileView;
export const events = oEvents;
