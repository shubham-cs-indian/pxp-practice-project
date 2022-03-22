import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  CLASS_CONFIG_RULE_ITEM_CLICKED: "class_config_rule_item_clicked"
};

const oPropTypes = {
  activeClass: ReactPropTypes.object.isRequired,
  ruleList: ReactPropTypes.object.isRequired
};

// @CS.SafeComponent
class ClassConfigRuleView extends React.Component {
  static propTypes = oPropTypes;

  handleClassConfigRuleItemClicked = (sRuleId, oEvent) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_RULE_ITEM_CLICKED, this, sRuleId);
  };

  getClassConfigRuleBody = () => {
    var _this = this;
    var oActiveClass = _this.props.activeClass;
    var aSelectedRules = oActiveClass.dataRules;
    var oRuleList = this.props.ruleList;
    var aRuleListDom = [];

    CS.forEach(oRuleList, function (oRule, sKey) {
      var sRuleId = oRule.id;
      var sRuleLabelClass = CS.includes(aSelectedRules, sRuleId) ? "ruleLabel selected" : "ruleLabel";

      aRuleListDom.push(
          <div className={sRuleLabelClass}
               key={sKey}
               onClick={_this.handleClassConfigRuleItemClicked.bind(_this, sRuleId)}>
            {CS.getLabelOrCode(oRule)}
          </div>
      );
    });

    return <div className="ruleListContainer">{aRuleListDom}</div>;
  };

  render() {
    var oClassConfigRuleBody = this.getClassConfigRuleBody();
    return (
        <div className="classConfigRuleContainer">
          <div className="classConfigRulesContainerHeader">{getTranslation().RULES}</div>
          <div className="classConfigRulesBody">
            {oClassConfigRuleBody}
          </div>
        </div>
    );
  }
}

export const view = ClassConfigRuleView;
export const events = oEvents;
