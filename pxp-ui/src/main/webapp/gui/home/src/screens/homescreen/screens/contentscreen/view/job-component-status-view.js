
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import BarStatusComponent from './../../../../../viewlibraries/barstatuscomponentview/bar-status-component';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {};

const oPropTypes = {
  job: ReactPropTypes.object
};

// @CS.SafeComponent
class JobComponentStatusView extends React.Component {
  static propTypes = oPropTypes;

  getAllComponentsStatus = () => {
    var oJob = this.props.job;
    var aComponent = oJob.components;

    var aComponentStatusDoms = [];
    CS.forEach(aComponent, function (oComponent) {
      var oStatus = oComponent.status;
      if (CS.isEmpty(oStatus)) {
        return;
      }
      var iSuccessCount = oStatus.completedCount;
      var iFailedCount = oStatus.failedCount;
      var iNotApplicableCount = 0; //oStatus.notApplicableCount;
      var iInProgressCount = /*oStatus.inProgressCount*/0;
      var iTotalCount = oStatus.totalCount;
      var sLabel = oComponent.componentLabel || oComponent.componentId;
      aComponentStatusDoms.push(
        <BarStatusComponent
            barLabel={sLabel}
            totalCount={iTotalCount}
            failedCount={iFailedCount}
            successCount={iSuccessCount}
            inProgressCount={iInProgressCount}
            notApplicableCount={iNotApplicableCount}
        />
      );
    });

    return aComponentStatusDoms;
  };

  render() {

    return (
        <div className="jobComponentStatusPanelContainer">
          <div className="panelHeader">
            <div className="panelLabel">{getTranslation().COMPONENT_STATUS}</div>
          </div>
          <div className="panelBody">
            {this.getAllComponentsStatus()}
          </div>
        </div>
    );
  }
}

export const view = JobComponentStatusView;
export const events = oEvents;
