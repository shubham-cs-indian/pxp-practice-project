import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../contextmenuwithsearchview/model/context-menu-view-model';

const oEvents = {
  CALCULATED_ATTRIBUTE_GET_ALLOWED_ATTRIBUTES: "calculated_attribute_get_allowed_attributes"
};

const oPropTypes = {
  calculatedAttribute: ReactPropTypes.object,
  attributeOperatorList: ReactPropTypes.array,
  addOperatorHandler: ReactPropTypes.func,
  operatorAttributeValueChangedHandler: ReactPropTypes.func,
  deleteOperatorAttributeValueHandler: ReactPropTypes.func,
  paginationData: ReactPropTypes.object,
  extraData: ReactPropTypes.object,
  allowedAttributes: ReactPropTypes.array,
};
/**
 * @class CalculatedAttributeFormulaView - use for calculate the attribute in Application.
 * @memberOf Views
 * @property {object} [calculatedAttribute] -  object of calculatedAttribute.
 * @property {array} [attributeOperatorList] -  array of attributeOperatorList.
 * @property {func} [addOperatorHandler] function for addOperatorHandler event.
 * @property {func} [operatorAttributeValueChangedHandler] function for operatorAttributeValueChangedHandler event.
 * @property {func} [deleteOperatorAttributeValueHandler] function for deleteOperatorAttributeValueHandler event.
 * @property {object} [paginationData] -  a object which contain pagination data.
 * @property {object} [extraData] -  object of referencedAttributes.
 * @property {array} [allowedAttributes] -  array of allowedAttributes
 */

// @CS.SafeComponent
class CalculatedAttributeFormulaView extends React.Component {

  constructor(props) {
    super(props);
  }

  addOperatorHandler=(sOperatorType)=> {
    this.props.addOperatorHandler(sOperatorType);
  }

  operatorAttributeValueChangedHandler =(oAttributeOperator, oEvent)=> {
    this.props.operatorAttributeValueChangedHandler(oAttributeOperator, oEvent.target.value);
  }

  deleteOperatorAttributeValueHandler =(sAttributeOperatorId)=> {
    this.props.deleteOperatorAttributeValueHandler(sAttributeOperatorId);
  }

  getFormulaView =(aAttributeOperatorList)=> {

    var oOperatorView = this.getExpressionView(aAttributeOperatorList);

    return (
        <div>
          <div className="inputButtonWrapper elementWrapper">
            <input className="inputButton" type="button" value="+"
                   onClick={this.addOperatorHandler.bind(this, "ADD")}/>
            <input className="inputButton" type="button" value="-"
                   onClick={this.addOperatorHandler.bind(this, "SUBTRACT")}/>
            <input className="inputButton" type="button" value="*"
                   onClick={this.addOperatorHandler.bind(this, "MULTIPLY")}/>
            <input className="inputButton" type="button" value="/"
                   onClick={this.addOperatorHandler.bind(this, "DIVIDE")}/>
            <input className="inputButton" type="button" value="("
                   onClick={this.addOperatorHandler.bind(this, "OPENING_BRACKET")}/>
            <input className="inputButton" type="button" value=")"
                   onClick={this.addOperatorHandler.bind(this, "CLOSING_BRACKET")}/>
            <input className="inputButton" type="button" value="A"
                   onClick={this.addOperatorHandler.bind(this, "ATTRIBUTE")}/>
            <input className="inputButton" type="button" value="V"
                   onClick={this.addOperatorHandler.bind(this, "VALUE")}/>
          </div>
          <div className="expressionWrapper elementWrapper">
            {oOperatorView}
          </div>
        </div>
    );
  }

  getExpressionView =(aAttributeOperatorList)=> {
    var aOperatorView = [];
    var _this = this;
    CS.forEach(aAttributeOperatorList, function (oAttributeOperator) {
      var sType = oAttributeOperator.type;
      var oDom = null;
      if (sType != "ATTRIBUTE" && sType != "VALUE") {
        oDom = _this.getOperatorView(oAttributeOperator);
      } else {
        oDom = _this.getAttributeOrValueView(oAttributeOperator);
      }
      aOperatorView.push(oDom);
    });
    return aOperatorView;
  }

  getOperatorView =(oAttributeOperator)=> {
    var sOperatorValue = oAttributeOperator.operator;
    var sId = oAttributeOperator.id;
    var aDom = [];
    if (sOperatorValue == "OPENING_BRACKET") {
      aDom.push("(");
    } else if (sOperatorValue == "CLOSING_BRACKET") {
      aDom.push(")");
    } else {
      aDom.push(<select value={sOperatorValue}
                        onChange={this.operatorAttributeValueChangedHandler.bind(this, oAttributeOperator)}>
        <option value="ADD">+</option>
        <option value="SUBTRACT">-</option>
        <option value="MULTIPLY">*</option>
        <option value="DIVIDE">/</option>
      </select>);
    }
    return (<div className="operatorWrapper elementWrapper">
      <div className="operatorButtonWrapper">
        <input type="button" value="X" onClick={this.deleteOperatorAttributeValueHandler.bind(this, sId)}/>
      </div>
      <div  className="formulaGeneratorButtons">
        {aDom}
      </div>
    </div>);
  }

  getAttributeOrValueView =(oAttributeOperator)=> {
    var sType = oAttributeOperator.type;
    var sId = oAttributeOperator.id;

    if (sType == "ATTRIBUTE") {
      return this.getAttributeDropdownView(oAttributeOperator);
    } else if (sType == "VALUE") {
      return (
          <div className="operatorWrapper elementWrapper">
            <div className="operatorButtonWrapper">
              <input type="button" value="X" onClick={this.deleteOperatorAttributeValueHandler.bind(this, sId)}/>
            </div>
            <div className="formulaGeneratorButtons">
              <input value={oAttributeOperator.value} type="number"
                     onChange={this.operatorAttributeValueChangedHandler.bind(this, oAttributeOperator)}/>
            </div>
          </div>
      );
    }

  }

  getAttributeDropdownView =(oAttributeOperator)=> {
    let aAllowedAttributes = CS.cloneDeep(this.props.allowedAttributes);
    let oPaginationData = this.props.paginationData;
    if (CS.isEmpty(aAllowedAttributes) && oPaginationData.isFirstCall && oPaginationData.from !== 0) {
      let oPaginationDataClone = CS.cloneDeep(oPaginationData);
      oPaginationDataClone.from = 0;
      this.getAllowedAttributes(oPaginationDataClone);
      return null;
    }
    let oReferencedAttributes = this.props.extraData.referencedAttributes;
    let sAttributeId = oAttributeOperator.attributeId;
    let aSelectedAttributes = CS.remove(aAllowedAttributes, {"id": sAttributeId});
    let oSelectedAttribute = !CS.isEmpty(aSelectedAttributes) ? aSelectedAttributes[0] : oReferencedAttributes[sAttributeId];

    let oMSSData = {
      items: aAllowedAttributes,
      context: "CalculatedAttribute",
      selectedItems: []
    };

    let oAttributeView = (
        <div className="elementWithType" onClick={this.attributeOperatorClickHandler.bind(this, oPaginationData)}>
          {oSelectedAttribute ? CS.getLabelOrCode(oSelectedAttribute) : ""}
          <div className="typeIcon attribute"/>
        </div>
    );

    return (
        <div className="operatorWrapper elementWrapper">
          <div className="operatorButtonWrapper">
            <input type="button" value="X"
                   onClick={this.deleteOperatorAttributeValueHandler.bind(this, oAttributeOperator.id)}/>
          </div>
          <div className="formulaGeneratorButtons">
            {this.getMSSView(oMSSData, oAttributeOperator, oAttributeView)}
          </div>
        </div>
    );
  }

  attributeOperatorClickHandler =(oPaginationData)=> {
    let oPaginationDataClone = CS.cloneDeep(oPaginationData);
    oPaginationDataClone.from = 0;
    this.getAllowedAttributes(oPaginationDataClone, true);
  }

  getContextMenuModelList =(aItems, sContext)=> {
    var aItemModels = [];

    CS.forEach(aItems, function (oItem) {

      let sIcon = oItem.icon || "";
      let sColor = oItem.color || "";
      let oProperties = {
        context: sContext,
        color: sColor
      };

      aItemModels.push(new ContextMenuViewModel(
          oItem.id,
          CS.getLabelOrCode(oItem),
          false, //bIsActive
          sIcon, //sIcon
          oProperties
      ));

    });
    return aItemModels;
  }

  getMSSView =(oMSSData, oAttributeOperator, oAttributeView)=> {

    var oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
    var oTargetOrigin = {horizontal: 'left', vertical: 'top'};

    return (
        <ContextMenuViewNew
            contextMenuViewModel={this.getContextMenuModelList(oMSSData.items, oMSSData.context)}
            context={oMSSData.context}
            selectedItems={oMSSData.selectedItems}
            isMultiselect={false}
            onApplyHandler={this.onApply.bind(this, oAttributeOperator)}
            onClickHandler={this.onApply.bind(this, oAttributeOperator)}
            showSelectedItems={false}
            showColor={false}
            anchorOrigin={oAnchorOrigin}
            targetOrigin={oTargetOrigin}
            useAnchorElementWidth={true}
            searchHandler={this.onSearch}
            searchText={this.props.paginationData.searchText}
            loadMoreHandler={this.onLoadMore}
            disabled={false}>
          {oAttributeView}
        </ContextMenuViewNew>
    );
  }

  onApply =(oAttributeOperator, oSelectedItem)=> {
    this.props.operatorAttributeValueChangedHandler(oAttributeOperator, oSelectedItem.id);
  }

  onSearch=(sSearchText)=> {
    var oPaginationData = CS.cloneDeep(this.props.paginationData);
    oPaginationData.searchText = sSearchText;
    oPaginationData.from = 0;
    this.getAllowedAttributes(oPaginationData);
  }

  onLoadMore =()=> {
    var oPaginationData = CS.cloneDeep(this.props.paginationData);
    var aAllowedAttributes = this.props.allowedAttributes || [];
    oPaginationData.from = aAllowedAttributes.length;
    this.getAllowedAttributes(oPaginationData);
  }

  getAllowedAttributes =(oPaginationData, bIsForReload)=> {
    var oCalculatedAttr = this.props.calculatedAttribute;
    var sCalculatedAttributeId = oCalculatedAttr.id;
    var sCalculatedAttributeType = oCalculatedAttr.calculatedAttributeType;
    var sCalculatedAttributeUnit = oCalculatedAttr.calculatedAttributeUnit;
    EventBus.dispatch(oEvents.CALCULATED_ATTRIBUTE_GET_ALLOWED_ATTRIBUTES, oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, !!bIsForReload);
  }

  render() {

    var oFormulaView = this.getFormulaView(this.props.attributeOperatorList);
    return (
        <div>
          {oFormulaView}
        </div>
    );
  }

}

CalculatedAttributeFormulaView.propTypes = oPropTypes;

export const view = CalculatedAttributeFormulaView;
export const events = oEvents;
