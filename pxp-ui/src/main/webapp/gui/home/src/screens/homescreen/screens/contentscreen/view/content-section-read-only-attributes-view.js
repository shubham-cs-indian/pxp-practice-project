import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from './utils/view-utils';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as FroalaWrapperView } from '../../../../../viewlibraries/customfroalaview/froala-wrapper-view';
import { view as ContentMeasurementMetricsView } from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view.js';


const oPropTypes = {
  attribute: ReactPropTypes.object,
  ruleViolation: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentSectionReadOnlyAttributesView extends React.Component {

  constructor (props) {
    super(props);
  }

  getBodyView () {
    let oAttribute = this.props.attribute;
    let sType = oAttribute.type;
    let oValueDOM = null;

    if(ViewUtils.isAttributeTypeHtml(sType)) {
      oValueDOM = (<FroalaWrapperView
                    dataId={""}
                    activeFroalaId={oAttribute.id}
                    content={oAttribute.value}
              />);
    } else if(ViewUtils.isAttributeTypeMeasurement(sType)) {
      oValueDOM = (<ContentMeasurementMetricsView
                    model={oAttribute}/>);
    }  else
     {
      let sValue = oAttribute.value //CS.isEmpty(oMaster.attributeTags) ? aAttributes[0].value : aAttributes[0].contextualCalculatedValue;
        oValueDOM = <div className={"sectionElementValue"}>{sValue}</div>
    }

    return (
        <div className={"sectionElementContainerNew"}>
          {oValueDOM}
        </div>
    )
  }

  getClassNameForRuleViolationIconByPriority = (aColors) => {
    let sClassName = "ruleViolationIcon ";
    if(CS.includes(aColors, "red")) {
      sClassName += "red"
    } else if(CS.includes(aColors, "orange")) {
      sClassName += "orange"
    } else {
      sClassName += "yellow"
    }

    return sClassName;
  };

  getRuleViolationIcon () {
    let oProps = this.props;
    let oAttribute = oProps.attribute;
    let sAttributeId = oAttribute.attributeId;
    let aRuleViolations = oProps.ruleViolation;
    let aRedRules = [] || null;
    let aYellowRules = [] || null;
    let aOrangeRules = [] || null;
    let aColors = [];

    let aAttributeRuleViolation = [];
     CS.find(aRuleViolations, {entityId: sAttributeId}) || []; // eslint-disable-line
    CS.forEach(aRuleViolations, function (oRuleViolation) {
      if (oRuleViolation.entityId == sAttributeId) {
        aAttributeRuleViolation.push(oRuleViolation);
      }
    });
    if (CS.isEmpty(aAttributeRuleViolation)) {
      return;
    }

    CS.forEach(aAttributeRuleViolation, function (oRuleViolation) {
      let sColor = oRuleViolation.color;

      let sRuleViolationTooltipIconClassName = "ruleColor " + sColor;

      let oDOM = (
          <div className="ruleContainer">
            <div className={sRuleViolationTooltipIconClassName}></div>
            <div className="ruleDescription">{oRuleViolation.description}</div>
          </div>
      );

      if (sColor === "red") {
        aRedRules.push(oDOM);
      } else if (sColor === "orange") {
        aOrangeRules.push(oDOM);
      } else if (sColor === "yellow") {
        aYellowRules.push(oDOM);
      }
      aColors.push(sColor);
    });

    let sClassName = this.getClassNameForRuleViolationIconByPriority(aColors);
    let oRuleViolationTooltipView = (
        <div className="ruleViolationsTooltip">
          {aRedRules}
          {aOrangeRules}
          {aYellowRules}
        </div>);

    return (
        <TooltipView placement="left" label={oRuleViolationTooltipView}>
          <div className={sClassName}></div>
        </TooltipView>
    );
  }

  getHeaderView () {
    let oProps = this.props;
    let oAttribute = oProps.attribute;
    let sLabel = CS.getLabelOrCode(oAttribute);
    let sLabelClass = "headerLabel ";

    return (
        <div className={"headerView"}>
          <div className={sLabelClass}>{sLabel}</div>
          {this.getRuleViolationIcon()}
        </div>
    );
  }

  render () {
    let sElementClass = "sectionElementNew readOnlySectionElement";

    return (
        <div className={sElementClass}>
          {this.getHeaderView()}
          {this.getBodyView()}
        </div>
    )
  };
}


ContentSectionReadOnlyAttributesView.propTypes = oPropTypes;


export const view = ContentSectionReadOnlyAttributesView;
