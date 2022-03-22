import CS from '../../libraries/cs';
import React from 'react';
import alertify from '../../commonmodule/store/custom-alertify-store';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import MockDataIdsForFroalaView from '../../commonmodule/tack/mock-data-ids-for-froala-view';
import { view as CalculatedAttributeFormulaView } from '../calculatedattributeformulaview/calculated-attribute-formula-view';
import { view as MultiSelectSearchView } from './../multiselectview/multi-select-search-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import ViewLibraryUtils from '../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

var oEvents = {
  GRID_CALCULATED_ATTRIBUTE_POPOVER_OPENED: "grid_calculated_attribute_popover_opened"
};

const oContextTypes = {
  masterAttributeList: ReactPropTypes.object,
  calculatedAttributeMapping: ReactPropTypes.object
};

const oPropTypes = {
  calculatedAttributeId: ReactPropTypes.string,
  attributeOperatorList: ReactPropTypes.array,
  calculatedAttributeType: ReactPropTypes.string,
  calculatedAttributeUnit: ReactPropTypes.string,
  calculatedAttributeUnitAsHTML: ReactPropTypes.string,
  unitAttributesList: ReactPropTypes.array,
  measurementUnitsData: ReactPropTypes.object,
  paginationData: ReactPropTypes.object,
  extraData: ReactPropTypes.object,
  allowedAttributes: ReactPropTypes.array,
  onChange: ReactPropTypes.func,
  isGridDataDirty: ReactPropTypes.bool,
};
/**
 * @class GridCalculatedAttributePropertyView
 * @memberOf Views
 * @property {string} [calculatedAttributeId] -
 * @property {array} [attributeOperatorList]
 * @property {string} [calculatedAttributeType] -
 * @property {string} [calculatedAttributeUnit] -
 * @property {string} [calculatedAttributeUnitAsHTML]
 * @property {array} [unitAttributesList]
 * @property {object} [measurementUnitsData]
 * @property {object} [paginationData]
 * @property {object} [extraData]
 * @property {array} [allowedAttributes]
 * @property {func} [onChange]
 * @property {bool} [isGridDataDirty]
 */

// @CS.SafeComponent
class GridCalculatedAttributePropertyView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showPopover: false,
      attributeOperatorList: this.props.attributeOperatorList,
      calculatedAttributeType: this.props.calculatedAttributeType,
      calculatedAttributeUnit: this.props.calculatedAttributeUnit,
      calculatedAttributeUnitAsHTML: this.props.calculatedAttributeUnitAsHTML,
      isGridDataDirty: this.props.isGridDataDirty
    }

    this.gridCalculatedAttributePropertyView = React.createRef();
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    /**To handle discard click, below check is added.*/
    if(oState.showPopover){
      // if (oState.isGridDataDirty && !oNextProps.isGridDataDirty) {
        return ({
          attributeOperatorList: oState.attributeOperatorList,
          calculatedAttributeType: oState.calculatedAttributeType,
          calculatedAttributeUnit: oState.calculatedAttributeUnit,
          calculatedAttributeUnitAsHTML: oState.calculatedAttributeUnitAsHTML,
          isGridDataDirty: oState.isGridDataDirty
        });
      // }
    }else{
      return ({
        attributeOperatorList: oNextProps.attributeOperatorList,
        calculatedAttributeType: oNextProps.calculatedAttributeType,
        calculatedAttributeUnit: oNextProps.calculatedAttributeUnit,
        calculatedAttributeUnitAsHTML: oNextProps.calculatedAttributeUnitAsHTML,
        isGridDataDirty: oNextProps.isGridDataDirty
      });
    }
  }

  openPopover =()=> {
    try {
      var oDOM = this.gridCalculatedAttributePropertyView.current;
      this.setState({
        showPopover: true,
        anchorElement: oDOM
      });
      this.fetchAllowedAttributes(this.state.calculatedAttributeType, this.state.calculatedAttributeUnit);

    } catch (oException) {
      ExceptionLogger.error(oException);
    }
  }

  closePopover =()=> {
    if (this.isExpressionValid()) {
      this.setState({
        showPopover: false
      });
      this.handleValueChanged();
    }
  }

  handleValueChanged =()=> {
    var oValue = {
      attributeOperatorList: this.state.attributeOperatorList,
      calculatedAttributeType: this.state.calculatedAttributeType,
      calculatedAttributeUnit: this.state.calculatedAttributeUnit,
      calculatedAttributeUnitAsHTML: this.state.calculatedAttributeUnitAsHTML,
      calculatedAttributeId: this.props.calculatedAttributeId
    };
    this.props.onChange(oValue);
  }

  isExpressionValid =()=> {
    var bIsExpressionValid = this.checkFormulaValidity(this.state.attributeOperatorList);
    if (!bIsExpressionValid) {
      alertify.error(getTranslation().INVALID_REGEX);
    }
    return bIsExpressionValid;
  }

  operatorAttributeValueChangedHandler =(oAttributeOperator, sValue)=> {
    var aAttributeOperatorList = CS.cloneDeep(this.state.attributeOperatorList);
    var sType = oAttributeOperator.type;
    var oModifiedOperator = CS.find(aAttributeOperatorList, {id: oAttributeOperator.id});
    if (oModifiedOperator) {
      var sOperator = null;
      var sVal = null;
      var sAttributeId = null;

      if (sType == "ATTRIBUTE") {
        sAttributeId = sValue;
      } else if (sType == "VALUE") {
        sVal = sValue;
      } else {
        sOperator = sValue;
        sType = sValue;
      }

      oModifiedOperator.attributeId = sAttributeId;
      oModifiedOperator.operator = sOperator;
      oModifiedOperator.value = sVal;
      oModifiedOperator.type = sType;
    }

    this.setState({
      attributeOperatorList: aAttributeOperatorList
    });
  }

  addOperatorHandler =(sOperatorType)=> {
    var aAttributeOperatorList = this.state.attributeOperatorList;
    var iAttributeOperatorListLength = aAttributeOperatorList.length;
    var sOperator = null;
    var uuid = UniqueIdentifierGenerator.generateUUID();

    if (sOperatorType != "ATTRIBUTE" && sOperatorType != "VALUE") {
      sOperator = sOperatorType;
    }

    var oAttributeOperator = {
      id: uuid,
      attributeId: null,
      operator: sOperator,
      type: sOperatorType,
      value: null,
      order: iAttributeOperatorListLength
    };

    var aNewOperatorList = aAttributeOperatorList.concat([oAttributeOperator]);

    this.setState({
      attributeOperatorList: aNewOperatorList
    });
  }

  deleteOperatorAttributeValueHandler =(sAttributeOperatorId)=> {
    var aAttributeOperatorList = CS.cloneDeep(this.state.attributeOperatorList);
    var iCount = 0;
    CS.remove(aAttributeOperatorList, function (oAttributeOperator) {
      if (oAttributeOperator.id == sAttributeOperatorId) {
        return true;
      }
      oAttributeOperator.order = iCount++;
    });

    this.setState({
      attributeOperatorList: aAttributeOperatorList
    });
  }

  getMeasurementTypeMSSView =()=> {
    var aSelectedList = [];
    var aMeasurementTypeMSSList = [];
    var _this = this;

    CS.forEach(this.props.unitAttributesList, function (oUnitAttribute) {
      if (oUnitAttribute.value == _this.state.calculatedAttributeType) {
        aSelectedList.push(_this.state.calculatedAttributeType);
      }
      aMeasurementTypeMSSList.push({
        id: oUnitAttribute.value,
        label: oUnitAttribute.name
      });
    });

    //Sorting is done in view and not in store due to some issues.
    aMeasurementTypeMSSList = CS.sortBy(aMeasurementTypeMSSList, 'label');

    return (
        <div className="measurementTypeMSSViewWrapper">
          <div className="measurementTypeTitle">
            {getTranslation().CALCULATED_ATTRIBUTE_TYPE}
          </div>
          <MultiSelectSearchView model={null}
                                 onApply={this.handleMeasurementTypeChanged}
                                 items={aMeasurementTypeMSSList}
                                 selectedItems={aSelectedList}
                                 isMultiSelect={false}
                                 checkbox={false}
                                 disabled={false}
                                 disableCross={false}/>
        </div>
    );
  }

  handleMeasurementTypeChanged =(aValues)=> {
    var sUnitType = aValues[0] || "";
    var sUnitValue = "";
    if (sUnitType && !ViewLibraryUtils.isMeasurementAttributeTypeCustom(sUnitType)) {
      sUnitValue = this.props.measurementUnitsData[sUnitType][0].unit;
    }
    this.setState({
      calculatedAttributeType: sUnitType,
      calculatedAttributeUnit: sUnitValue,
      calculatedAttributeUnitAsHTML: "",
      attributeOperatorList: []
    });
    this.fetchAllowedAttributes(sUnitType, sUnitValue);
  }

  getMeasurementUnitMSSView =()=> {

    var oUnitViewToRender = null;

    if (ViewLibraryUtils.isMeasurementAttributeTypeCustom(this.state.calculatedAttributeType)) {

      var uuid = UniqueIdentifierGenerator.generateUUID();

      oUnitViewToRender = (
          <div className="froalaViewWrapper">
            <FroalaWrapperView dataId={uuid}
                               activeFroalaId={uuid}
                               content={this.state.calculatedAttributeUnitAsHTML}
                               className={""}
                               toolbarIcons={MockDataIdsForFroalaView}
                               showPlaceHolder={false}
                               handler={this.handleFroalaUnitChanged}/>
          </div>
      );

    } else {

      var aSelectedList = [];
      var aMeasurementUnitMSSList = [];
      var _this = this;

      CS.forEach(this.props.measurementUnitsData[this.state.calculatedAttributeType], function (oUnit) {
        if (oUnit.unit == _this.state.calculatedAttributeUnit) {
          aSelectedList.push(_this.state.calculatedAttributeUnit);
        }
        var sLabel = CS.getLabelOrCode(oUnit) + "(" + oUnit.unitToDisplay + ")";
        aMeasurementUnitMSSList.push({
          id: oUnit.unit,
          label: sLabel
        });
      });

      //Sorting is done in view and not in store due to some issues.
      aMeasurementUnitMSSList = CS.sortBy(aMeasurementUnitMSSList, 'label');

      oUnitViewToRender = (<MultiSelectSearchView model={null}
                                                  onApply={this.handleMeasurementUnitChanged}
                                                  items={aMeasurementUnitMSSList}
                                                  selectedItems={aSelectedList}
                                                  isMultiSelect={false}
                                                  checkbox={false}
                                                  disabled={false}
                                                  disableCross={false}/>);
    }

    return (
        <div className="measurementUnitMSSViewWrapper">
          <div className="measurementUnitTitle">
            {getTranslation().CALCULATED_ATTRIBUTE_UNIT}
          </div>
          {oUnitViewToRender}
        </div>
    );
  }

  handleFroalaUnitChanged =(oEvent)=> {
    let sNewUnit = oEvent.textValue;
    this.setState({
      calculatedAttributeUnit: sNewUnit,
      calculatedAttributeUnitAsHTML: oEvent.htmlValue,
      attributeOperatorList: []
    });
    this.fetchAllowedAttributes(this.state.calculatedAttributeType, sNewUnit);
  }

  handleMeasurementUnitChanged =(aValues)=> {
    let sNewUnit = aValues[0];
    this.setState({
      calculatedAttributeUnit: sNewUnit,
      calculatedAttributeUnitAsHTML: "",
      attributeOperatorList: []
    });
    this.fetchAllowedAttributes(this.state.calculatedAttributeType, sNewUnit);
  }

  fetchAllowedAttributes =(calculatedAttributeType, calculatedAttributeUnit)=> {
    let oPaginationData = CS.cloneDeep(this.props.paginationData);
    oPaginationData.from = 0;
    EventBus.dispatch(oEvents.GRID_CALCULATED_ATTRIBUTE_POPOVER_OPENED, oPaginationData, calculatedAttributeType, calculatedAttributeUnit, this.props.calculatedAttributeId);
  }

  getCalculatedFormulaView =()=> {

    var oCalculatedAttr = {
      id: this.props.calculatedAttributeId,
      calculatedAttributeType: this.state.calculatedAttributeType,
      calculatedAttributeUnit: this.state.calculatedAttributeUnit
    };

    return (
        <div className="calculatedFormulaViewWrapper">
          {getTranslation().FORMULA}
          <CalculatedAttributeFormulaView calculatedAttribute={oCalculatedAttr}
                                          attributeOperatorList={this.state.attributeOperatorList}
                                          operatorAttributeValueChangedHandler={this.operatorAttributeValueChangedHandler}
                                          addOperatorHandler={this.addOperatorHandler}
                                          deleteOperatorAttributeValueHandler={this.deleteOperatorAttributeValueHandler}
                                          paginationData={this.props.paginationData}
                                          extraData={this.props.extraData}
                                          allowedAttributes={this.props.allowedAttributes}
                                          />
        </div>
    );

  }

  getReadOnlyCalculatedFormula =()=> {
    var aFormula = [];
    var aAttributeOperatorList = this.state.attributeOperatorList;
    var oAllowedAttributes = this.props.allowedAttributes;
    var oReferencedAttributes = this.props.extraData.referencedAttributes;

    CS.forEach(aAttributeOperatorList, function (oAttributeOperator, iIndex) {
      var sType = oAttributeOperator.type;
      if (sType == "ATTRIBUTE") {
        if (oAttributeOperator.attributeId) {
          var oMasterAttribute = oReferencedAttributes[oAttributeOperator.attributeId] ||
              (oAllowedAttributes && oAllowedAttributes[oAttributeOperator.attributeId]);
          if (oMasterAttribute) {
            aFormula.push(
                <div className="elementWithType" key={iIndex}>
                  {CS.getLabelOrCode(oMasterAttribute)}
                  <div className="typeIcon attribute"></div>
                </div>
            );
          }
        }
      } else if (sType == "VALUE") {
        aFormula.push(
            <div className="element" key={iIndex}>
              {oAttributeOperator.value}
            </div>
        );
      } else {
        var sOperatorValue = oAttributeOperator.operator;
        var sValueToPush = "";
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
            <div className="element" key={iIndex}>
              {sValueToPush}
            </div>
        );
      }
    });
    return aFormula;
  };

  checkFormulaValidity = (aAttributeOperatorList) => {
    if (CS.isEmpty(aAttributeOperatorList)) {
      return true;
    }
    var oExpression = this.getExpression(aAttributeOperatorList);
    if (!oExpression.validExpression) {
      return false;
    }
    var sExpression = oExpression.expression;
    try {
      var iResult = eval(sExpression);
      return !CS.isNaN(iResult) && CS.isNumber(iResult);

    } catch (err) {
      return false;
    }
  }

  getExpression = (aAttributeOperatorList) => {
    var _this = this;
    var sExpression = "";
    var sPrevType = "";
    var bValidExpression = true;
    CS.forEach(aAttributeOperatorList, function (oAttributeOperator, iIndex) {
      if (iIndex > 0) {
        sPrevType = aAttributeOperatorList[iIndex - 1].type;
      }
      var sNewType = oAttributeOperator.type;
      bValidExpression = _this.checkCalculatedExpressionManualCases(sPrevType, sNewType);
      if (!bValidExpression) {
        return false;
      }

      switch (sNewType) {
        case "ADD":
          sExpression = sExpression + "+";
          break;
        case "SUBTRACT":
          sExpression = sExpression + "-";
          break;
        case "MULTIPLY":
          sExpression = sExpression + "*";
          break;
        case "DIVIDE":
          sExpression = sExpression + "/";
          break;
        case "OPENING_BRACKET":
          sExpression = sExpression + "(";
          break;
        case "CLOSING_BRACKET":
          sExpression = sExpression + ")";
          break;
        case "ATTRIBUTE":
          if (oAttributeOperator.attributeId == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(2)";
          break;
        case "VALUE":
          if (oAttributeOperator.value == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(" + oAttributeOperator.value + ")";
          break;
      }

      sPrevType = oAttributeOperator.type

    });

    return {
      expression: sExpression,
      validExpression: bValidExpression
    }
  };

  checkCalculatedExpressionManualCases = (sPrevType, sNewType) => {
    var bValidExpression = true;

    var aSignOperators = ["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"];

    if (sPrevType != "") {
      if ((sPrevType == "ATTRIBUTE" && sNewType == "ATTRIBUTE") || (sPrevType == "VALUE" && sNewType == "VALUE")) {
        bValidExpression = false;
      }

      if (
          (sPrevType != "ATTRIBUTE" && sPrevType != "VALUE" && sPrevType != "OPENING_BRACKET" && sPrevType != "CLOSING_BRACKET") &&
          (sNewType != "ATTRIBUTE" && sNewType != "VALUE" && sNewType != "OPENING_BRACKET" && sNewType != "CLOSING_BRACKET")) {
        bValidExpression = false;
      }

      if (CS.includes(aSignOperators, sPrevType) && CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    } else {
      if (CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    }
    return bValidExpression;
  };

  render() {

    var oPopoverStyle = {
      width: "800px",
      padding: "20px"
    };

    var aCalculatedFormula = this.getReadOnlyCalculatedFormula();

    var sCalculatedAttributeType = "";
    var sCalculatedAttributeUnit = "";

    if (!CS.isEmpty(this.state.calculatedAttributeType)) {
      sCalculatedAttributeType = CS.find(this.props.unitAttributesList, {"value": this.state.calculatedAttributeType})["name"];
      if (ViewLibraryUtils.isMeasurementAttributeTypeCustom(this.state.calculatedAttributeType)) {
        sCalculatedAttributeUnit = this.state.calculatedAttributeUnit ? this.state.calculatedAttributeUnit : "";
      }
      else if (!CS.isEmpty(this.state.calculatedAttributeUnit)) {
        sCalculatedAttributeUnit = CS.find(this.props.measurementUnitsData[this.state.calculatedAttributeType], {"unit": this.state.calculatedAttributeUnit})["unitToDisplay"];
      }
    }

    return (
        <div className="gridCalculatedAttributePropertyView" ref={this.gridCalculatedAttributePropertyView}>
          <div className="gridCalculatedAttributeProperties" onClick={this.openPopover}>
            <div className="calculatedAttributeType">{getTranslation().CALCULATED_ATTRIBUTE_TYPE + " : " + sCalculatedAttributeType}</div>
            <div className="calculatedAttributeUnit">{getTranslation().CALCULATED_ATTRIBUTE_UNIT + " : " + sCalculatedAttributeUnit}</div>
            <div className="calculatedFormula">
              <div className="calculatedFormulaHeader">
                {getTranslation().FORMULA + " : "}
              </div>
              {aCalculatedFormula}
            </div>
          </div>
          <CustomPopoverView className="popover-root"
                   open={this.state.showPopover}
                   style={ oPopoverStyle}
                   anchorEl={this.state.anchorElement}
                   anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
                   transformOrigin={{horizontal: 'right', vertical: 'top'}}
                   onClose={this.closePopover}>
            <div className="calculatedFormulaWrapper">
              {this.getMeasurementTypeMSSView()}
              {this.getMeasurementUnitMSSView()}
              {this.getCalculatedFormulaView()}
            </div>
          </CustomPopoverView>
        </div>
    );
  }
}

GridCalculatedAttributePropertyView.propTyepes = oPropTypes;

export const view = GridCalculatedAttributePropertyView;
export const events = oEvents;

