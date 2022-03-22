import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

// @CS.SafeComponent
class DashboardEndpointSummaryTileView extends React.Component {

  static oPropTypes = {
    tileData: ReactPropTypes.array
  };

  constructor (props) {
    super(props);
  };

  render () {

    let aData = CS.map(this.props.tileData, function (oData, iIndex) {
      let oIconView = oData.iconClass ? <div className={"elementIcon " + oData.iconClass}/> : null;

      return (
          <div className="informationElement" key={iIndex}>
            <div className='element'>
              <div className="elementLabel" title={oData.label}>
                {oData.label}
              </div>
              {oIconView}
            </div>
            <div className="elementValue">{oData.value}</div>
          </div>
      );
    });

    return (
        <div className="inboundEndpointBody">
          {aData}
        </div>
    );
  };
}

export const view = DashboardEndpointSummaryTileView;
