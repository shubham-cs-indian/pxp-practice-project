import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as StartAndEndDatePickerView } from './start-and-end-date-picker-view';
import { view as CircledManyTagGroupView } from '../../../../../viewlibraries/circledmanytaggroupview/circled-many-tag-group-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
let getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {};

const oPropTypes = {
  eventData: ReactPropTypes.object,
  tagData: ReactPropTypes.object,
  tabPermission: ReactPropTypes.object,
  validityMessage: ReactPropTypes.string,
  isDisabled: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  isExpired: ReactPropTypes.bool
};

// @CS.SafeComponent
class AssetSchedulerSectionView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    showValidityMessage: false
  };

  getRuleViolationBodyView = () => {

    let aRedRules = [];

    let sColor = "red";
    let sClassName = "ruleColor " + sColor;

    let sMessage = "";

    if (this.props.validityMessage && this.props.validityMessage.toLowerCase() === 'expired') {
      sMessage = getTranslation().MAM_VALIDITY_EXPIRED;
    }

    let oDOM = (
        <div className="ruleContainer">
          <div className={sClassName}/>
          <div className="ruleDescription">{sMessage}</div>
        </div>
    );

    aRedRules.push(oDOM);

    return (
        <div className="ruleViolationsBodyContainer">
          <div className="ruleViolationLabel">{getTranslation().RULE_VIOLATION_LABEL}</div>
          {aRedRules}
        </div>
    );
  };

  getExpiredAssetIndicator = () => {
    let bIsAssetExpired = this.props.isExpired;
    let oExpiredAssetIconDOM = bIsAssetExpired ? (
            <TooltipView placement="top" label={getTranslation().EXPIRED}>
              <div className={"expiredAssetsIndicator"}></div>
            </TooltipView>
        ) : null;
    return (
        <div className={"expiredAssetsIndicatorWrapper"}>
          {oExpiredAssetIconDOM}
        </div>
    )
  };

  render() {
    var oTagData = this.props.tagData || {};

    let bShouldShowValidityRuleViolation = !CS.isEmpty(this.props.validityMessage);

    return (
        <div className="assetSchedulerSectionView">
          {this.getExpiredAssetIndicator()}
          <div className="clickableIconsContainer">
            {bShouldShowValidityRuleViolation ? this.getExpiredAssetIndicator() : null}
          </div>
          {bShouldShowValidityRuleViolation && this.state.showValidityMessage ? this.getRuleViolationBodyView() : null}
          <StartAndEndDatePickerView eventModel={this.props.eventData}
                                     tabPermission={this.props.tabPermission}
                                     isDisabled={this.props.isDisabled}
                                     context={this.props.context}/>
          <CircledManyTagGroupView masterTagGroups={oTagData.masterTagGroups}
                                   tagInstances={oTagData.tagInstances}
                                   disabled={oTagData.disabled}
                                   context={this.props.context}
          />
        </div>
    );
  }
}

export const view = AssetSchedulerSectionView;
export const events = oEvents;
