import React from 'react';
import ReactPropTypes from 'prop-types';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import CS from '../../libraries/cs';

import {getTranslations as getTranslation} from '../../commonmodule/store/helper/translation-manager';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';

import TooltipView from '../tooltipview/tooltip-view';
import {view as EntityTagsSummaryView} from '../entitytagsummaryview/entity-tags-summary-view';
import MockDataForMeasurementMetricAndImperial
  from '../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import {view as CustomDialogView} from "./../customdialogview/custom-dialog-view";

const oPropTypes = {
  oSectionElement: ReactPropTypes.object,
  oConflictingSources: ReactPropTypes.object,
  oRuleViolationObject: ReactPropTypes.object,
  fOnDialogClose: ReactPropTypes.func,
  oAllowedAttributes: ReactPropTypes.object,
  oReferencedAttributes: ReactPropTypes.object,
  fConflictContainerAttributeRef: ReactPropTypes.func,
  fConflictContainerTagRef: ReactPropTypes.func,
  fRuleViolationBodyContainerRef: ReactPropTypes.func,
  fHandleDefaultValueChanged: ReactPropTypes.func,
  fHandleDefaultValueChangedForTags: ReactPropTypes.func,
  fHandleConflictValueResolved: ReactPropTypes.func,
  fHandleDialogOptionValueClicked: ReactPropTypes.func
};

function PropertyConflictingValueFooterView ({
                                               oSectionElement, oConflictingSources, oRuleViolationObject, fOnDialogClose, oAllowedAttributes, oReferencedAttributes,
                                               fConflictContainerAttributeRef, fConflictContainerTagRef, fRuleViolationBodyContainerRef,
                                               fHandleDefaultValueChanged, fHandleDefaultValueChangedForTags, fHandleConflictValueResolved, fHandleDialogOptionValueClicked
                                             }) {

  //Alerts block
  let getHTMLValue = function (sVal) {
    let oMarkupObj = {__html: sVal};
    return <div dangerouslySetInnerHTML={oMarkupObj}></div>;
  }

  let handleDefaultValueChanged = function (sValue, sNewValue, sId, sSource) {
    fHandleDefaultValueChanged(sValue, sNewValue, sId, sSource);
  };

  let getConflictingValuesDOM = function (oAttribute, oElement, bIsForNotification) {

    //let oConflictingValuesMap = this.props.conflictingValues;
    if (CS.isEmpty(oAttribute.conflictingValues)) {
      return null;
    }
    //let aConflictingValues = oConflictingValuesMap[sAttributeId].values;
    let aConflictingValues = oAttribute.conflictingValues;
    let sValue = oAttribute.value;
    let oAttributeSource = oAttribute.source;
    let aDefaultConflictRows = [];
    let sCurrentAttributeType = oElement.attribute.type;
    let sSplitter = ViewUtils.getSplitter();
    let bIsHtmlAttribute = ViewUtils.isAttributeTypeHtml(sCurrentAttributeType);

    CS.forEach(aConflictingValues, function (oValue) {
      let sValueToShow = oValue.value;
      let bIsEqualValue = (sValue === sValueToShow);

      if (ViewUtils.isAttributeTypeDate(sCurrentAttributeType)) {
        sValueToShow = ViewUtils.getDateAttributeInTimeFormat(sValueToShow);
      } else if (bIsHtmlAttribute) { //html type
        sValueToShow = getHTMLValue(oValue.valueAsHtml);
        bIsEqualValue = (oAttribute.valueAsHtml === oValue.value);
      } else if (ViewUtils.isAttributeTypeMeasurement(sCurrentAttributeType)) {
        let sDefaultUnit = oElement.attribute.defaultUnit;
        let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
        let aConverterList = oMeasurementMetricAndImperial[sCurrentAttributeType];
        let oBaseUnit = CS.find(aConverterList, {isBase: true});
        let sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
        let sPrecision = oElement.attribute.precision;
        let sCalculatedValue = ViewUtils.getMeasurementAttributeValueToShow(sValueToShow, sBaseUnit, sDefaultUnit, sPrecision);
        sCalculatedValue = ViewUtils.getTruncatedValue(sCalculatedValue, sPrecision);
        sValueToShow = sCalculatedValue + " " + ViewUtils.getDisplayUnitFromDefaultUnit(sDefaultUnit);
      }
      let oValueSource = oValue.source;
      let bIsConflictSelected = (
          oValue.isResolved &&
          bIsEqualValue &&
          oAttributeSource && oAttributeSource.id === oValueSource.id &&
          oAttributeSource.type === oValueSource.type &&
          (!CS.isEmpty(oAttributeSource.contentId) ? oAttributeSource.contentId === oValueSource.contentId : true)
      );
      let sSelectedClassName = bIsConflictSelected ? "defaultConflictSelected selected" : "defaultConflictSelected";

      // if (oElement.isDisabled && oElement.couplingType !== CouplingConstants.DYNAMIC_COUPLED && oElement.couplingType !== CouplingConstants.READ_ONLY_COUPLED) {
      let fDefaultValueChanged = CS.noop();
      if (!oElement.canResolveConflicts) {
        sSelectedClassName = "defaultConflictSelected hideVisibility";
      } else {
        let sNewValue = oValue.value;
        if (bIsHtmlAttribute) {
          sNewValue = {
            htmlValue: oValue.valueAsHtml,
            textValue: oValue.value
          }
        }
        fDefaultValueChanged = handleDefaultValueChanged.bind(this, sValue, sNewValue, oAttribute.id, oValue.source);
      }

      //TODO: Refactor below conditions to determine source id
      let sSourceId = CS.isNotEmpty(oValueSource.contentId) ? oValueSource.contentId + sSplitter + oValueSource.id : oValueSource.id;
      let oConflictingSource = oConflictingSources[sSourceId];
      let sClassLabel = "";
      let sConflictOccursInClassName = "";
      if (!CS.isEmpty(oConflictingSource)) {
        sClassLabel = CS.getLabelOrCode(oConflictingSource);
        sConflictOccursInClassName = oConflictingSource.conflictOccursIn;
      }

      aDefaultConflictRows.push(
          <div className="defaultConflictRow">
            <TooltipView label={sValueToShow}>
              <div className="defaultConflictValue">{sValueToShow}</div>
            </TooltipView>
            <TooltipView label={getTranslation().FROM + " " + sClassLabel}>
              <div className="defaultConflictClass">
                <span className={"conflictOccursIn " + sConflictOccursInClassName}></span>
                <div className="defaultConflictClassLabel">{getTranslation().FROM + " " + sClassLabel}</div>
              </div>
            </TooltipView>
            <div className={sSelectedClassName} onClick={fDefaultValueChanged}></div>
          </div>);
    });

    let oStyle = {};
    if (bIsForNotification) {
      oStyle.display = "none";
    }

    let sClassName = (oElement.violationExpanded) ? "defaultConflictContainer expanded" : "defaultConflictContainer collapsed";

    return (
        <div className={sClassName} ref={fConflictContainerAttributeRef}>
          <div className="defaultConflictHeader" style={oStyle}>{getTranslation().CONFLICTING_VALUES}</div>
          <div className="defaultConflictBody">
            {aDefaultConflictRows}
          </div>
        </div>
    );
  }

  let getRuleViolationBodyView = function (sMasterEntityId) {

    if (!oRuleViolationObject) {
      return null;
    }
    let aRuleViolation = oRuleViolationObject[sMasterEntityId];

    let aRedRules = [] || null;
    let aYellowRules = [] || null;
    let aOrangeRules = [] || null;

    CS.forEach(aRuleViolation, function (oRuleViolation) {
      let sColor = oRuleViolation.color;
      let oStyle = {"background-color": sColor};
      let sClassName = "ruleColor " + sColor;

      let oDOM = (
          <div className="ruleContainer">
            <div className={sClassName}></div>
            <div className="ruleDescription">{oRuleViolation.description}</div>
          </div>
      );

      if (sColor == "red") {
        aRedRules.push(oDOM);
      } else if (sColor == "orange") {
        aOrangeRules.push(oDOM);
      } else if (sColor == "yellow") {
        aYellowRules.push(oDOM);
      }

    });

    return (
        <div className="ruleViolationsBodyContainer collapsed" ref={fRuleViolationBodyContainerRef}>
          <div className="ruleViolationLabel">{getTranslation().RULE_VIOLATION_LABEL}</div>
          {aRedRules}
          {aOrangeRules}
          {aYellowRules}
        </div>
    );
  }

  let getUserDeletedAlertView = function () {
    return (<div className="userDeletedAlert">
      <div className="userDeletedIcon"></div>
      <div className="userDeletedMessage"> {getTranslation().USER_DELETED_ALERT} </div>
    </div>);
  }

  let getAttributeAlerts = function () {
    let oNotificationBodyView = null;
    let oDefaultConflictBody = null;
    let oRuleViolationBody = null;
    if (!CS.isEmpty(oSectionElement.contentAttributes)) {

      let oAttribute = oSectionElement.contentAttributes[0];
      let sAttributeId = oAttribute.attributeId;
      if (oAttribute.attributeId == "typeattribute") {
        sAttributeId = oAttribute.value;
      }
      if (!CS.isEmpty(oSectionElement.contentAttributes[0].notification)) {
        // let sNotificationBodyRef = "notificationBody_" + oSectionElement.contentAttributes[0].id;
        // oNotificationIconView = this.getNotificationIconView(sNotificationBodyRef, oSectionElement);
        // oNotificationBodyView = this.getAttributeNotificationBodyView(sNotificationBodyRef, oSectionElement);

        //TODO: Temporary Hack - Shashank : after resolving issue keep only else part.
        if (oAttribute.conflictingValues.length > 1) {
          oDefaultConflictBody = getConflictingValuesDOM(oAttribute, oSectionElement);
        } else {
          let bIsForNotification = true;
          oNotificationBodyView = getConflictingValuesDOM(oAttribute, oSectionElement, bIsForNotification);
        }
      }
      else if (oAttribute.conflictingValues && oAttribute.conflictingValues.length > 1) {
        oDefaultConflictBody = getConflictingValuesDOM(oAttribute, oSectionElement);
      }

      if (oRuleViolationObject) {
        let aRuleViolation = oRuleViolationObject[sAttributeId];
        if (!CS.isEmpty(aRuleViolation)) {
          oRuleViolationBody = getRuleViolationBodyView(sAttributeId)
        }
      }
    }

    let oUserDeletedAlertView = oSectionElement.isUserDeleted ? getUserDeletedAlertView() : null;


    return (<React.Fragment>
      {oDefaultConflictBody}
      {oNotificationBodyView}
      {oRuleViolationBody}
      {oUserDeletedAlertView}
    </React.Fragment>);
  }

  let getNotificationTagsView = function (aTags, oTagMaster) {
    return (<EntityTagsSummaryView tags={[{tagId: oTagMaster.id, tagValues: aTags}]}
                                   customPlaceholder={getTranslation().NO_VALUE}/>);
  }

  let isTagValueEqual = function (aContentTagValues, aConflictingTagValues) {
    let bFlag = false;
    let aTagValuesTagIds = [];
    let aConflictingTagValuesTagIds = [];
    let oTagValuesMap = {};
    let oConflictingTagValuesMap = {};

    CS.forEach(aContentTagValues, function (oTag) {
      // if (oTag.relevance != 0) {
      aTagValuesTagIds.push(oTag.tagId);
      oTagValuesMap[oTag.tagId] = oTag.relevance;
      // }
    });

    CS.forEach(aConflictingTagValues, function (oTag) {
      //if (oTag.relevance != 0) {
      aConflictingTagValuesTagIds.push(oTag.tagId);
      oConflictingTagValuesMap[oTag.tagId] = oTag.relevance;
      // }
    });

    /*
     *in oConflictingTagValuesMap only conflicting tags only conflicting selected tag values getting there fore
     * reversed tag value check from oConflictingTagValuesMap.
     */

    CS.forEach(oConflictingTagValuesMap, function (sValue, sTagId) {
      let sTagValue = oTagValuesMap[sTagId];
      bFlag = (sTagValue == sValue);
      if (!bFlag) {
        return false;
      }
    });
    /*let bIsArraySame = !!CS.xor(aTagValuesTagIds, aConflictingTagValuesTagIds).length;
    if (!bIsArraySame) {

    CS.forEach(oTagValuesMap, function (sValue, sTagId) {
      let sTagValue = oConflictingTagValuesMap[sTagId];
      bFlag = (sTagValue == sValue);
      if (!bFlag) {
        return false;
      }
    });
    }*/
    return bFlag;
  }

  let getConflictingValuesDOMForTags = function (oTag, oElement, bIsForNotification) {

    //let oConflictingValuesMap = this.props.conflictingValues;
    if (CS.isEmpty(oTag) || CS.isEmpty(oTag.conflictingValues)) {
      return null;
    }
    // let aConflictingValues = oConflictingValuesMap[sTagId].values;
    let aConflictingValues = oTag.conflictingValues;
    let oTagSource = oTag.source;
    let aContentTagValues = oTag.tagValues;
    let aDefaultConflictRows = [];
    let sSplitter = ViewUtils.getSplitter();

    CS.forEach(aConflictingValues, function (oValue) {
      let aConflictingTagValues = oValue.tagValues;
      let oTagValuesDOM = getNotificationTagsView(aConflictingTagValues, oElement.tag);
      let oValueSource = oValue.source;
      let sSourceId = CS.isNotEmpty(oValueSource.contentId) ? oValueSource.contentId + sSplitter + oValueSource.id : oValueSource.id;
      let oConflictingSource = oConflictingSources[sSourceId];
      let sClassLabel = "";
      let sConflictOccursInClassName = "";

      if (!CS.isEmpty(oConflictingSource)) {
        sClassLabel = CS.getLabelOrCode(oConflictingSource);
        sConflictOccursInClassName = oConflictingSource.conflictOccursIn;
      }

      let bIsConflictSelected = (
          oValue.isResolved && isTagValueEqual(aContentTagValues, aConflictingTagValues) &&
          oTagSource && oTagSource.id === oValueSource.id &&
          oTagSource.type === oValueSource.type &&
          (!CS.isEmpty(oTagSource.contentId) ? oTagSource.contentId === oValueSource.contentId : true)
      );
      let sSelectedClassName = bIsConflictSelected ? "defaultConflictSelected selected" : "defaultConflictSelected";

      if (!oElement.canResolveConflicts) {
        sSelectedClassName += " hideVisibility";
      }

      aDefaultConflictRows.push(<div className="defaultConflictRow">
        <div className="defaultConflictValue">{oTagValuesDOM}</div>
        <TooltipView label={getTranslation().FROM + " " + sClassLabel}>
          <div className="defaultConflictClass">
            <span className={"conflictOccursIn " + sConflictOccursInClassName}></span>
            <div className="defaultConflictClassLabel">{getTranslation().FROM + " " + sClassLabel}</div>
          </div>
        </TooltipView>
        <div className={sSelectedClassName}
             onClick={fHandleDefaultValueChangedForTags.bind(this, aContentTagValues, oValue.tagValues, oTag.tagId, oValue.source)}></div>
      </div>);
    });
    let oStyle = {};
    if (bIsForNotification) {
      oStyle.display = "none";
    }

    let sClassName = (oElement.violationExpanded) ? "defaultConflictContainer expanded" : "defaultConflictContainer collapsed";

    return (
        <div className={sClassName} ref={fConflictContainerTagRef}>
          <div className="defaultConflictHeader" style={oStyle}>{getTranslation().CONFLICTING_VALUES}</div>
          <div className="defaultConflictBody">
            {aDefaultConflictRows}
          </div>
        </div>
    );
  }

  let getTagAlerts = function () {
    let oElement = oSectionElement;
    let oNotificationBodyView = null;
    let oDefaultConflictBody = null;
    let oRuleViolationBody = null;
    if (!CS.isEmpty(oElement.contentTags)) {
      let oTag = oElement.contentTags[0];
      if (!CS.isEmpty(oTag.notification)) {
        // let sNotificationBodyRef = "notificationBody_" + oElement.contentTags[0].id;
        // oNotificationIconView = this.getNotificationIconView(sNotificationBodyRef, oElement);
        // oNotificationBodyView = this.getTagNotificationBodyView(sNotificationBodyRef, oElement);

        //TODO: Temporary Hack - Shashank : after resolving issue keep only else part.
        if (oTag.conflictingValues.length > 1) {
          oDefaultConflictBody = getConflictingValuesDOMForTags(oTag, oElement);
        } else {
          let bIsForNotification = true;
          oNotificationBodyView = getConflictingValuesDOMForTags(oTag, oElement, bIsForNotification);
        }
      }
      else if (oTag.conflictingValues && oTag.conflictingValues.length > 1) {
        oDefaultConflictBody = getConflictingValuesDOMForTags(oTag, oElement);
      }

      let sTagId = oTag.tagId;
      if (oRuleViolationObject) {
        let aRuleViolation = oRuleViolationObject[sTagId];
        if (!CS.isEmpty(aRuleViolation)) {
          oRuleViolationBody = getRuleViolationBodyView(sTagId)
        }
      }
    }

    return (<React.Fragment>
      {oDefaultConflictBody}
      {oNotificationBodyView}
      {oRuleViolationBody}
    </React.Fragment>);
  }

  let getCalculatedFormula = function (aAttributeOperatorList) {
    let aFormula = [];

    CS.forEach(aAttributeOperatorList, function (oAttributeOperator) {
      let sType = oAttributeOperator.type;
      if (sType == "ATTRIBUTE") {
        if (oAttributeOperator.attributeId) {
          let oMasterAttribute = oReferencedAttributes[oAttributeOperator.attributeId] ||
              (oAllowedAttributes && oAllowedAttributes[oAttributeOperator.attributeId]);
          if (oMasterAttribute) {
            aFormula.push(
                <div className="elementWithType">
                  {CS.getLabelOrCode(oMasterAttribute)}
                  <div className="typeIcon attribute"></div>
                </div>
            );
          }
        }
      } else if (sType == "VALUE") {

        aFormula.push(
            <div className="element">
              {oAttributeOperator.value}
            </div>
        );
      } else {
        let sOperatorValue = oAttributeOperator.operator;
        let sValueToPush = "";
        if (sOperatorValue == "OPENING_BRACKET") {
          sValueToPush = "(";
        } else if (sOperatorValue == "CLOSING_BRACKET") {
          sValueToPush = ")";
        } else if (sOperatorValue == "ADD") {
          sValueToPush = "+";
        } else if (sOperatorValue == "SUBTRACT") {
          sValueToPush = "-";
        } else if (sOperatorValue == "MULTIPLY") {
          sValueToPush = "*";
        } else if (sOperatorValue == "DIVIDE") {
          sValueToPush = "/";
        }

        aFormula.push(
            <div className="element">
              {sValueToPush}
            </div>
        );
      }
    });
    return (
        <div className="calculatedFormula">
          <div className="calculatedFormulaHeader">Formula :</div>
          {aFormula}
        </div>
    );
  };

  let handleDialogButtonClick = function (sButtonId) {
    switch (sButtonId) {
      case "save":
        let oContentRelationship;
        oContentRelationship = CS.filter(oSectionElement.contentRelationships, {
          sideId: oSectionElement.id,
          relationshipId: oSectionElement.propertyId
        });
        let oRelationship = oContentRelationship[0];
        fHandleConflictValueResolved(oRelationship);
        break;
      case "cancel":
        fOnDialogClose();
        break;
    }
  }

  let getConflictingValuesDOMForRelationship = function (oRelationship, oSectionElement, bIsForNotification) {
    let aView = [];
    let aConflictingValues = oRelationship.conflictingValues;
    CS.forEach(aConflictingValues, (oConflictingValue) => {
      let fOnChange = fHandleDialogOptionValueClicked.bind(this,oConflictingValue, oRelationship);
      let oRadioButtonView = <Radio checked={oConflictingValue.isChecked || false}
                                              color="primary"
                                              classes={{root: "ContentComparisonMnmPropertyDetailedView-radioRoot-237"}}
                                              onChange={fOnChange}/>

      let oValueSource = oConflictingValue.source;
      let sSplitter = ViewUtils.getSplitter();
      let sSourceId = CS.isNotEmpty(oValueSource.contentId) ? oValueSource.contentId + sSplitter + oValueSource.id : oValueSource.id;
      let oConflictingSource = oConflictingSources[sSourceId];
      let sClassLabel = "";

      if (!CS.isEmpty(oConflictingSource)) {
        sClassLabel = CS.getLabelOrCode(oConflictingSource);
      }

      aView.push(
          <div className={"propertyValueWrapper"}>
            {oRadioButtonView}
            {/* {_this.getPropertyValue(oElement)} */}
            <div className="propertyValue">{getTranslation().FROM + " " + sClassLabel}</div>
          </div>
      );
    })
    return aView;
  }

  let getRelationshipAlerts = function () {
    let oNotificationBodyView = null;
    if (!CS.isEmpty(oSectionElement.contentRelationships)) {
      let oContentRelationship = CS.filter(oSectionElement.contentRelationships, {
        sideId: oSectionElement.id,
        relationshipId: oSectionElement.propertyId
      });
      let oRelationship = oContentRelationship[0];
      oNotificationBodyView = getConflictingValuesDOMForRelationship(oRelationship, oSectionElement);
    }

    let oButtonData = [
      {
        id: "cancel",
        isDisabled: false,
        isFlat: false,
        label: "Cancel"
      },
      {
        id: "save",
        isDisabled: false,
        isFlat: false,
        label: "Save"
      }
    ];
    let oFooterDescriptionWrapper =
        (
            <CustomDialogView
                modal={true}
                className="relationshipConflictDialog"
                open={oSectionElement.violationExpanded}
                buttonData={oButtonData}
                buttonClickHandler={handleDialogButtonClick.bind(this)}
                bodyClassName={"relationshipConflictBody"}
                contentClassName={"relationshipConflictContent"}
                title={"Choose values"}
                onRequestClose={fOnDialogClose}>
              <div className="relationshipConflictValue">
                <RadioGroup
                    aria-label="contentComparisonDataSelection"
                    name="contentComparisonData"
                    value={""}>
                  {oNotificationBodyView}
                </RadioGroup>
              </div>
            </CustomDialogView>
        );

    return oFooterDescriptionWrapper;
  }

  let sElementType = oSectionElement.type;
  let oAlertsDOM = null;
  let sDescription = "";
  let aAttributeOperatorList = [];
  switch (sElementType) {
    case "attribute":
      oAlertsDOM = getAttributeAlerts();
      sDescription = oSectionElement.attribute.description;
      if (oSectionElement.attribute.attributeOperatorList) {
        aAttributeOperatorList = oSectionElement.attribute.attributeOperatorList;
      }
      break;
    case "tag":
      oAlertsDOM = getTagAlerts();
      sDescription = oSectionElement.tag.description;
      break;
    case "relationship":
      oAlertsDOM = getRelationshipAlerts();
      break;
  }

  let oCalculatedFormulaDOM = CS.isEmpty(aAttributeOperatorList) ? null : getCalculatedFormula(aAttributeOperatorList);
  let oDescriptionDOM = CS.isEmpty(sDescription) ? null : (<div className={"footerDescription"}>{sDescription}</div>);
  let oFooterDescriptionWrapper = (CS.isEmpty(oDescriptionDOM) && CS.isEmpty(oCalculatedFormulaDOM)) ? null :
                                  <div className={"footerDescriptionWrapper"}>
                                    {oDescriptionDOM}
                                    {oCalculatedFormulaDOM}
                                  </div>;

  return (
      <div className={"footerView"}>
        <div className={"alertsSection"}>
          {oAlertsDOM}
        </div>
        {oFooterDescriptionWrapper}
      </div>);
}

PropertyConflictingValueFooterView.propTypes = oPropTypes;


export default CS.SafeComponent(PropertyConflictingValueFooterView);