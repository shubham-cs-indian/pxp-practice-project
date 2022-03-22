import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as NothingFoundView } from './../../../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import ViewUtils from './utils/view-utils';

const oPropTypes = {
  tileData: ReactPropTypes.array
};

// @CS.SafeComponent
class DashboardEndpointAllUploadsTileView extends React.Component {

  constructor (props) {
    super(props);

  };

  getIndivisualRowView (oRowData) {
    let aRowData = [];
    let sClassName = "";
    CS.forEach(oRowData, function (sValue, sKey) {
      if (sKey == "totalFileUploads") {
        return;
      }
      switch (sKey) {
        case "label":
          sClassName = sKey + "Column";
          aRowData.push(
              <td className={sClassName}>
                <TooltipView label={sValue}>
                  <div className="cellValueContainer">
                    {sValue}
                  </div>
                </TooltipView>
              </td>);
          break;
        case "timeStamp":
          sValue = sValue ? ViewUtils.getDateAttributeInTimeFormat(sValue) : null;
          sClassName = sKey + "Column";
          aRowData.push(
              <td className={sClassName}>
                <TooltipView label={sValue}>
                  <div className="cellValueContainer">
                    {sValue}
                  </div>
                </TooltipView>
              </td>);
          break;

        case "totalRedViolations":
        case "totalOrangeViolations":
        case "totalYellowViolations":
          sClassName = "violationColumn";
          aRowData.push(<td className={sClassName}>{sValue}</td>);
          break;

        case "success":
          sValue = sValue + '/' + (oRowData.success + oRowData.failure);
          sClassName = sKey + "Column";
          aRowData.push(<td className={sClassName}>{sValue}</td>);
          break;
      }
    });

    return (<tr>{aRowData}</tr>);
  };

  getHeaderRow (oRowData) {
    let aHeaderRowData = [];
    let sClassName = "";
    CS.forEach(oRowData, function (sValue, sKey) {
      if (sKey == "totalFileUploads") {
        return;
      }
      switch (sKey) {
        case "label":
        case "success":
        case "timeStamp":
          sClassName = sKey + "Column header";
          aHeaderRowData.push(
              <th className={sClassName}>
                <TooltipView label={getTranslation()[sKey.toUpperCase()]}>
                  <div className={sClassName}>
                    {getTranslation()[sKey.toUpperCase()]}
                  </div>
                </TooltipView>
              </th>);
          break;
        case "totalRedViolations":
        case "totalOrangeViolations":
        case "totalYellowViolations":
          sClassName = "violationColumn header";
          aHeaderRowData.push(
              <th className={sClassName}>
                <TooltipView label={getTranslation()[sKey.toUpperCase()]}>
                  <div className={sKey}>
                  </div>
                </TooltipView>
              </th>);
          break;
      }
    });

    return (<tr>{aHeaderRowData}</tr>)
  };

  render () {
    let __this = this;
    let aTileData = this.props.tileData;
    let oView = <NothingFoundView />;

    if (!CS.isEmpty(aTileData)) {
      let aRowView = [];
      let oHeaderData = null;
      CS.forEach(aTileData, function (oRowData, iIndex) {
        aRowView.push(__this.getIndivisualRowView(oRowData));
        if (iIndex == 0) {
          oHeaderData = __this.getHeaderRow(oRowData);
        }
      });
      oView = (
          <table className="endpointAllUploadsTable">
            <thead>
            {oHeaderData}
            </thead>
            <tbody className="endpointAllUploadsTableBody">
            {aRowView}
            </tbody>
          </table>
      );
    }

    return (
      <div className="inboundEndpointBody">
        {oView}
      </div>
    );
  };

}

DashboardEndpointAllUploadsTileView.propTypes = oPropTypes;

export const view = DashboardEndpointAllUploadsTileView;
