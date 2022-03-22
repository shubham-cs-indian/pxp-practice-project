import CS from '../../../../../libraries/cs';
import React from 'react';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {};

const oPropTypes = {};

// @CS.SafeComponent
class RuleViolationView extends React.Component {
  constructor (props) {
    super(props);

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }
  static propTypes = oPropTypes;

  toggleBodyDisplay = (sBodyRef, sIconRef) => {
    var oBodyDom = this[sBodyRef];
    var oIconDom = this[sIconRef];
    if(CS.includes(oBodyDom.classList, "collapsed")){
      oBodyDom.classList.remove("collapsed");
      oBodyDom.classList.add("expanded");
      oIconDom.classList.add("selected");
    }else {
      oBodyDom.classList.add("collapsed");
      oBodyDom.classList.remove("expanded");
      oIconDom.classList.remove("selected");
    }
  };

  getRelationshipRuleViolationIcon = (sBodyRef) => {
    var sIconRef = sBodyRef + "_violationIconRef";
    return (
        <div className="ruleViolationIconContainer">
          <div className="sectionElementRuleViolationIcon upperIcon" ref={this.setRef.bind(this,sIconRef)}
               title={getTranslation().RULE_VIOLATION_LABEL}
               onClick={this.toggleBodyDisplay.bind(this, sBodyRef, sIconRef)}></div>
        </div>);
  };

  getRelationshipRuleViolationBodyView = (sBodyRef, oElement) => {
    var oRelationship = oElement.relationship;
    var oRuleViolationObject = this.props.ruleViolation;
    var aRuleViolation =  oRuleViolationObject[oRelationship.id];

    var aRedRules = [] || null;
    var aYellowRules = [] || null;
    var aOrangeRules = [] || null;

    CS.forEach(aRuleViolation, function (oRuleViolation) {
      var sColor = oRuleViolation.color;
      var oStyle = {"background-color": sColor};
      var sClassName = "ruleColor " + sColor;

      var oDOM = (
          <div className="ruleContainer">
            <div className={sClassName}></div>
            <div className="ruleDescription">{oRuleViolation.description}</div>
          </div>
      );

      if(sColor == "red"){
        aRedRules.push(oDOM);
      }else if(sColor == "orange"){
        aOrangeRules.push(oDOM);
      }else if(sColor == "yellow"){
        aYellowRules.push(oDOM);
      }
    });

    return (
        <div className="ruleViolationsBodyContainer collapsed" ref={this.setRef.bind(this,sBodyRef)}>
          <div className="ruleViolationLabel">{getTranslation().RULE_VIOLATION_LABEL}</div>
          {aRedRules}
          {aOrangeRules}
          {aYellowRules}
        </div>
    );
  };

  render() {
    var oElements = this.props.elements;
    var sRuleViolationBodyRef = "ruleViolationBody_" + oElements.id;
    var oRuleViolationIcon = this.getRelationshipRuleViolationIcon(sRuleViolationBodyRef);
    var oRuleViolationBody = this.getRelationshipRuleViolationBodyView(sRuleViolationBodyRef, oElements);
    return (
        <div className="ruleViolationContainer">
          {oRuleViolationIcon}
          {oRuleViolationBody}
        </div>
    );
  }
}

export const view = RuleViolationView;
export const events = oEvents;
