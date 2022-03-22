import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
};

const oPropTypes = {
  configRuleMappings: ReactPropTypes.array,
};
/**
 * @class MappingSummaryView
 * @memberOf Views
 * @property {array} [configRuleMappings] - Contains array of mappings data.
 */

// @CS.SafeComponent
class MappingSummaryView extends React.Component {

  constructor(props) {
    super(props);
  }

  getMappingSummaryView =()=> {
    var aConfigRuleMappings = this.props.configRuleMappings;
    var iTotal = aConfigRuleMappings.length;
    var iIgnored = 0;
    var iMapped = 0;
    CS.forEach(aConfigRuleMappings, function (oRow) {
      if (oRow.mappedElementId) {
        iMapped++;
      }
      if (oRow.isIgnored) {
        iIgnored++;
      }
    });
    return (
        <div className="mappingSummaryBlock">
          <div className="summaryBlock">
            <div className="blockLabel">{getTranslation().TOTAL}</div>
            <div className="blockValue">{iTotal}</div>
          </div>
          <div className="summaryBlock">
            <div className="blockLabel">{getTranslation().MAPPED}</div>
            <div className="blockValue">{iMapped}</div>
          </div>
          <div className="summaryBlock">
            <div className="blockLabel">{getTranslation().UNMAPPED}</div>
            <div className="blockValue">{iTotal - iMapped}</div>
          </div>
          <div className="summaryBlock">
            <div className="blockLabel">{getTranslation().IGNORED}</div>
            <div className="blockValue">{iIgnored}</div>
          </div>
        </div>
    )
  }

  render() {
    var oMappingSummaryView = this.getMappingSummaryView();
    return (
        <div className="mappingSummaryView">
          {oMappingSummaryView}
        </div>);
  }
}

MappingSummaryView.propTypes = oPropTypes;

export const view = MappingSummaryView;
export const events = oEvents;
