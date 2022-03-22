import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import alertify from '../../commonmodule/store/custom-alertify-store';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import AutoSize from 'autosize';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import TooltipView from './../tooltipview/tooltip-view';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import MockDataIdsForFroalaView from '../../commonmodule/tack/mock-data-ids-for-froala-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import ConfigDataEntitiesDictionary from '../../commonmodule/tack/config-data-entities-dictionary';
import ConcatenatedEntityTypes from '../../commonmodule/tack/concatenated-entity-types';
import { view as CustomTextFieldView } from './../customtextfieldview/custom-text-field-view';

const oEvents = {

};

const oPropTypes = {
  attributeConcatenatedList: ReactPropTypes.array,
  referencedData: ReactPropTypes.object,
  onChange: ReactPropTypes.func,
  allowedEntities: ReactPropTypes.bool,
  requestResponseInfoForAttributes: ReactPropTypes.object,
  attributesToExclude: ReactPropTypes.array
};
/**
 * @class GridConcatenatedAttributePropertyView
 * @memberOf Views
 * @property {array} [attributeConcatenatedList] - Concatenated attribute list.
 * @property {object} [referencedData] - Contains referenced attributes and referenced tags.
 * @property {func} [onChange] -Executes after value is changed.
 * @property {bool} [allowedEntities] - (html, attribute, tag).
 * @property {object} [requestResponseInfoForAttributes] - Contains entity name, request type, types to exclude etc(for example:
 * entity name = "attributes", requestType = "configData").
 * @property {array} [attributesToExclude] - Contains attributes which are excluded from attribute list.
 */

// @CS.SafeComponent
class GridConcatenatedAttributePropertyView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showPopover: false,
      attributeConcatenatedList: this.props.attributeConcatenatedList,
      referencedData: this.props.referencedData,
    }

    this.GridConcatenatedAttributePropertyView = React.createRef();
  }
  static getDerivedStateFromProps (oNextProps, oState) {
    if(oState.showPopover){
      return {
        attributeConcatenatedList: oState.attributeConcatenatedList,
        referencedData: oState.referencedData,
        showPopover: oState.showPopover
      };
    }
    else{
      return {
        attributeConcatenatedList: oNextProps.attributeConcatenatedList,
        referencedData: oNextProps.referencedData,
        showPopover: oNextProps.showPopover
      };
    }
  }

 /* componentWillReceiveProps (oNextProps) {
    this.setState({
      attributeConcatenatedList: oNextProps.attributeConcatenatedList,
      referencedData: oNextProps.referencedData
    });
  }*/


  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  openPopover =()=> {
    var oDOM = this.GridConcatenatedAttributePropertyView.current;
    this.setState({
      showPopover: true,
      anchorElement: oDOM
    });
  }

  closePopover =()=> {
    if (this.isExpressionValid()) {
      this.setState({
        showPopover: false
      });
      if (!CS.isEqual(this.state.attributeConcatenatedList, this.props.attributeConcatenatedList)) {
        this.handleValueChanged();
      }
    }
  }

  handleValueChanged =()=> {
    this.props.onChange(this.state.attributeConcatenatedList, this.state.referencedData);
  }

  isExpressionValid =()=> {
    var aAttributeConcatenatedList = this.state.attributeConcatenatedList;

    var bIsExpressionValid = CS.isEmpty(aAttributeConcatenatedList) || this._checkConcatExpressionValidity(aAttributeConcatenatedList);
    if(!bIsExpressionValid){
      alertify.error(getTranslation().INVALID_REGEX);
    }

    return bIsExpressionValid;
  }


  /******************* functions from attribute store *********************/

  _addConcatObject =(sType)=> {
    var uuid = UniqueIdentifierGenerator.generateUUID();
    var aAttributeConcatenatedList = CS.cloneDeep(this.state.attributeConcatenatedList || []);
    var iAttributeConcatenatedListLength = aAttributeConcatenatedList.length;
    var sValue = "";
    var sEntityId = null;

    var oEntityConcat = {
      id: uuid,
      type: sType,
      order: iAttributeConcatenatedListLength
    };

    if(sType === "attribute") {
      oEntityConcat.attributeId = sEntityId;
    } else if (sType === "tag") {
      oEntityConcat.tagId = sEntityId;
    }

    if(sType === "html") {
      oEntityConcat.value = sValue;
      oEntityConcat.valueAsHtml = "";
    }

    aAttributeConcatenatedList.push(oEntityConcat);

    this.setState({
      attributeConcatenatedList: aAttributeConcatenatedList
    });
  }

  _handleCommonConfigSectionConcatObjectRemoved =(sConcatId)=> {
    var aAttributeConcatenatedList = CS.cloneDeep(this.state.attributeConcatenatedList);

    var iCount = 0;
    CS.remove(aAttributeConcatenatedList, function (oConcat) {
      if(oConcat.id == sConcatId) {
        return true;
      }
      oConcat.order = iCount++;
    });

    this.setState({
      attributeConcatenatedList: aAttributeConcatenatedList
    });
  }

  _handleCommonConfigSectionConcatInputChanged =(sValue, oConcat, oReferencedData)=> {
    var aAttributeConcatenatedList = CS.cloneDeep(this.state.attributeConcatenatedList);
    var oCurrentConcatString = CS.find(aAttributeConcatenatedList, {id: oConcat.id});
    if(oConcat.type === "string"){
      oCurrentConcatString.value = sValue;
    } else if (oConcat.type === ConcatenatedEntityTypes.html){
      if(CS.has(sValue, "valueAsHtml")) {
        oCurrentConcatString.value = sValue.value;
        oCurrentConcatString.valueAsHtml = sValue.valueAsHtml;
      } else {
        oCurrentConcatString.value = sValue;
      }
    } else if (oConcat.type === ConcatenatedEntityTypes.tag){
      let oReferencedTags = oReferencedData.referencedTags;
      let oReferencedTag = CS.find(oReferencedTags, {id: sValue});
      oCurrentConcatString.tagId = sValue;
      oCurrentConcatString.value = CS.getLabelOrCode(oReferencedTag);
    } else {
      let oReferencedAttributes = oReferencedData.referencedAttributes;
      let oReferencedAttribute = CS.find(oReferencedAttributes, {id: sValue});
      oCurrentConcatString.attributeId = sValue;
      oCurrentConcatString.value = CS.getLabelOrCode(oReferencedAttribute);
    }

    let oReferencedStateData = CS.assign(this.state.referencedData, oReferencedData);

    this.setState({
      attributeConcatenatedList: aAttributeConcatenatedList,
      referencedData: oReferencedStateData
    });
  }

  _checkConcatExpressionValidity =(aExpression)=> {
    var bIsExpressionValid = true;
    CS.forEach(aExpression, function (oExpression) {
      if(oExpression.type === ConcatenatedEntityTypes.attribute || oExpression.type === ConcatenatedEntityTypes.tag){
        bIsExpressionValid = !CS.isEmpty(oExpression.attributeId) || !CS.isEmpty(oExpression.tagId);
        if(!bIsExpressionValid){
          return false;
        }
      }
    });

    return bIsExpressionValid;
  }

  /** ********************************************************************** **/


  handleOperatorTagValueChanged =(oAttributeOperator, bForConcatenation, aSelectedValue, oReferencedTags)=>{
    var sValue = aSelectedValue[0];
    let oExistingReferencedData = this.state.referencedData;
    let oReferencedData = {
      referencedTags: oExistingReferencedData && oExistingReferencedData.referencedTags ?
        CS.combine(oExistingReferencedData.referencedTags, oReferencedTags) : oReferencedTags
    };
    if(bForConcatenation){
      this._handleCommonConfigSectionConcatInputChanged(sValue, oAttributeOperator, oReferencedData);
    }
  };


  handleOperatorAttributeValueChanged =(oAttributeOperator, bForConcatenation, aSelectedValue, oReferencedAttributes)=>{
    var sValue = aSelectedValue[0];
    let oExistingReferencedData = this.state.referencedData;
    let oReferencedData = {
      referencedAttributes: oExistingReferencedData && oExistingReferencedData.referencedAttributes ?
        CS.combine(oExistingReferencedData.referencedAttributes, oReferencedAttributes) : oReferencedAttributes
    };
    if(bForConcatenation){
      this._handleCommonConfigSectionConcatInputChanged(sValue, oAttributeOperator, oReferencedData);
    }
  };

  handleConcatInputChanged =(oConcat, oEvent)=> {
    var sValue = "";
    if(oConcat.type === ConcatenatedEntityTypes.html){
      sValue = {
        valueAsHtml: oEvent.htmlValue,
        value: oEvent.textValue
      }
    }else {
      sValue = oEvent.target.value;
    }

    this._handleCommonConfigSectionConcatInputChanged(sValue, oConcat);
  }

  handleConcatTextValueChanged = (oConcat, sValue) => {
    this._handleCommonConfigSectionConcatInputChanged(sValue, oConcat);
  };

  handleConcatObjectRemoveClicked =(sId)=> {
    this._handleCommonConfigSectionConcatObjectRemoved(sId);
  }

  addConcatObject =(sObjectType)=> {
    this._addConcatObject(sObjectType);
  }






  getFroalaView =(sId, sValue, aStyle, fHandler)=>{

    return (
        AutoSize(<FroalaWrapperView dataId={sId}
                                    activeFroalaId={sId}
                                    content={sValue}
                                    toolbarIcons={MockDataIdsForFroalaView}
                                    showPlaceHolder={false}
                                    handler={fHandler}/>)
    )
  }

/*
  isFromAttributesToExcludeForConcatenated =(sBaseType)=> {
    var sVisualType = ViewLibraryUtils.getAttributeTypeForVisual(sBaseType);
    var aTypesToExclude = ["concatenated", "image", "coverflow"];

    return (
        CS.includes(aTypesToExclude, sVisualType) ||
        ViewLibraryUtils.isAttributeTypeFile(sBaseType) ||
        ViewLibraryUtils.isAttributeTypeType(sBaseType) ||
        ViewLibraryUtils.isAttributeTypeTaxonomy(sBaseType)
    );
  }
*/

  /*getAttributeDropdownView =(oAttributeOperator, sContext, bShowId)=> {
    var _this = this;
    var bForConcatenation = sContext == "concatenated";
    var sAttributeId = oAttributeOperator.attributeId;
    var oAttrDropDownMap = this.context.masterAttributeList;
    var aOptionDom = [];

    aOptionDom.push(<option disabled selected value="defaultOption" key="blank">{""}</option>);

    CS.forEach(oAttrDropDownMap, function (oAttribute) {
      var sLabel = bShowId ? CS.getLabelOrCode(oAttribute) + "  (" + oAttribute.id + ") " : CS.getLabelOrCode(oAttribute);
      var sVisualType = ViewLibraryUtils.getAttributeTypeForVisual(oAttribute.type);
      if (!bForConcatenation && (sVisualType == "number" || sVisualType == "measurementMetrics")) {
        aOptionDom.push(<option value={oAttribute.id} title={sLabel} key={oAttribute.id}>{sLabel}</option>);
      } else if (bForConcatenation) {
        if (!_this.isFromAttributesToExcludeForConcatenated(oAttribute.type)) {
          aOptionDom.push(<option value={oAttribute.id} title={sLabel} key={oAttribute.id}>{sLabel}</option>);
        }
      }
    });

    return (
        <select defaultValue="defaultOption"
                value={sAttributeId}
                onChange={this.handleOperatorAttributeValueChanged.bind(this, oAttributeOperator, bForConcatenation)}>
          {aOptionDom}
        </select>
    );
  }*/

  getCustomTextFieldView = (oConcat) => {
    let fHandler = this.handleConcatTextValueChanged.bind(this, oConcat);
    return (
        <CustomTextFieldView className={"userDefinedText"}
                             value={oConcat.value}
                             isMultiLine={true}
                             onChange={fHandler}/>
    );
  };

  getConcatenatedBodyDOM =(aConcatData)=> {
    var aDom = [];
    var _this = this;
    let aAllowedEntities = this.props.allowedEntities;

    CS.forEach(aConcatData, function (oConcat, iIndex) {
      var oCrossIconDom = (
          <div className="crossIconWrapper">
            <TooltipView placement="bottom" label={getTranslation().REMOVE}>
              <div className="crossIcon" onClick={_this.handleConcatObjectRemoveClicked.bind(_this, oConcat.id)}></div>
            </TooltipView>
          </div>);

      var sConcatType = oConcat.type;
      var fHandler = _this.handleConcatInputChanged.bind(_this, oConcat);

      switch (sConcatType) {
        case ConcatenatedEntityTypes.html:
          if(CS.includes(aAllowedEntities, ConcatenatedEntityTypes.text)) {
            aDom.push(<div className="userDefinedTextContainer">
              {_this.getCustomTextFieldView(oConcat)}
              {oCrossIconDom}
            </div>);
          } else {
            aDom.push(
                <div className="froalaDomWrapper" key={iIndex}>
                  {_this.getFroalaView(oConcat.id, oConcat.valueAsHtml, [], fHandler)}
                  {oCrossIconDom}
                </div>);
          }
          break;

        case ConcatenatedEntityTypes.attribute:
          aDom.push(
              <div className="selectDomWrapper" key={iIndex}>
                {_this.getLazyMSSView(oConcat, "concatenated", true)}
                {oCrossIconDom}
              </div>);
          break;

        case ConcatenatedEntityTypes.tag:
          aDom.push(
              <div className="selectDomWrapper">
                {_this.getLazyMSSViewForTag(oConcat, "concatenated", true)}
                {oCrossIconDom}
              </div>);
          break;
      }
    });

    return aDom
  }

  getLazyMSSViewForTag = (oTag) => {
    let oReqResObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.TAGS,
    };
    let oReferencedData = this.props.referencedData;
    let oReferencedTags = oReferencedData.referencedTags;

    return (<LazyMSSView isMultiSelect={false}
                         context={'concatenatedAttribute'}
                         disabled={false}
                         selectedItems={[oTag.tagId]}
                         cannotRemove={true}
                         showSelectedInDropdown={true}
                         referencedData={oReferencedTags}
                         requestResponseInfo={oReqResObj}
                         onApply={this.handleOperatorTagValueChanged.bind(this, oTag, true)}
    />)
  };

  getLazyMSSView = (oAttributeOperator, sContext, bShowId) => {
    let oReqResObj = this.props.requestResponseInfoForAttributes;
    let oReferencedData = this.props.referencedData;
    let oReferencedAttributes = oReferencedData.referencedAttributes;
    let aSelectedItems = oAttributeOperator.attributeId && [oAttributeOperator.attributeId] || [];
    let aAttributesToExclude = this.props.attributesToExclude || [];

    return (<LazyMSSView isMultiSelect={false}
                         context={'concatenatedAttribute'}
                         disabled={false}
                         selectedItems={aSelectedItems}
                         excludedItems={aAttributesToExclude}
                         cannotRemove={true}
                         showSelectedInDropdown={true}
                         referencedData={oReferencedAttributes}
                         requestResponseInfo={oReqResObj}
                         onApply={this.handleOperatorAttributeValueChanged.bind(this, oAttributeOperator, true)}
    />)

  };

  getConcatenatedHeaderView = () => {
    let _this = this;
    let aAllowedEntities = this.props.allowedEntities;
    let aHeaderEntitiesView = [];
    let sTooltip = null;
    let sLabel = null;
    let fHandler = null;

    CS.forEach(aAllowedEntities, function (sEntity) {
      switch (sEntity){
        case ConcatenatedEntityTypes.html:
          sTooltip = getTranslation().USER_DEFINED_TEXT;
          sLabel = getTranslation().USER_DEFINED_TEXT;
          fHandler = _this.addConcatObject.bind(_this, ConcatenatedEntityTypes.html);
          break;

        case ConcatenatedEntityTypes.tag:
          sTooltip = getTranslation().ADD_TAG;
          sLabel = getTranslation().TAG;
          fHandler = _this.addConcatObject.bind(_this, ConcatenatedEntityTypes.tag);
          break;

        case ConcatenatedEntityTypes.attribute:
          sTooltip = getTranslation().ADD_ATTRIBUTE;
          sLabel = getTranslation().ATTRIBUTE;
          fHandler = _this.addConcatObject.bind(_this, ConcatenatedEntityTypes.attribute);
          break;

        case ConcatenatedEntityTypes.text:
          sLabel = sTooltip = getTranslation().USER_DEFINED_TEXT;
          sTooltip = sTooltip = getTranslation().USER_DEFINED_TEXT;
          fHandler = _this.addConcatObject.bind(_this, ConcatenatedEntityTypes.html);
          break;
      }

      aHeaderEntitiesView.push(
          <TooltipView placement="bottom" label={sTooltip}>
            <div className="concatButton" key={"concatButton" + sEntity}
                 onClick={fHandler}>{sLabel}</div>
          </TooltipView>
      );
    });

    return aHeaderEntitiesView;
  };

  getConcatenatedFormulaView = () => {
    var aAttributeConcatenatedList = this.state.attributeConcatenatedList;
    var oConcatenatedBodyDOM = this.getConcatenatedBodyDOM(aAttributeConcatenatedList);
    let aHeaderView = this.getConcatenatedHeaderView();

    return(
        <div className="concatenatedFormulaWrapper elementWrapper">
          <div className="concatenatedFormulaButtons">
            {aHeaderView}
          </div>
          <div className="concatenatedFormulaBody">
            {oConcatenatedBodyDOM}
          </div>
        </div>
    );
  };

  getReadOnlyConcatenatedFormula =()=> {
    var aFormula = [];
    var aAttributeConcatenatedList = this.state.attributeConcatenatedList;
    let oReferencedData = this.state.referencedData;
    let oReferencedAttributes = oReferencedData.referencedAttributes;
    let oReferencedTags = oReferencedData.referencedTags;
    let sLabel = "";

    CS.forEach(aAttributeConcatenatedList, function (oData, iIndex) {
      let sClassName = "typeIcon ";
      if ((oData.type === ConcatenatedEntityTypes.html)) {
        sClassName += "string";
        sLabel = oData.value;
      } else if (oData.type === ConcatenatedEntityTypes.tag) {
        let oReferencedTag = oData.tagId && oReferencedTags[oData.tagId] || {};
        sLabel = !CS.isEmpty(oReferencedTag) && CS.getLabelOrCode(oReferencedTag);
        sClassName += "tag";
      } else {
        let oReferencedAttribute = oData.attributeId && oReferencedAttributes[oData.attributeId] || {};
        sLabel = !CS.isEmpty(oReferencedAttribute) && CS.getLabelOrCode(oReferencedAttribute);
      }
      aFormula.push(
          <TooltipView placement="bottom" label={sLabel} key={iIndex}>
            <div className="element">
              {sLabel}
              <div className={sClassName}></div>
            </div>
          </TooltipView>
      );
    });

    return aFormula;
  }

  render() {

    var oPopoverStyle = {
      width : "800px",
      padding: "20px"
    };

    var aConcatenationFormula = this.getReadOnlyConcatenatedFormula();

    return (
        <div className="gridConcatenatedAttributePropertyView" ref={this.GridConcatenatedAttributePropertyView}>
          <div className="concatenationFormula" onClick={this.openPopover}>{aConcatenationFormula}</div>
          <CustomPopoverView className="popover-root"
                   open={this.state.showPopover}
                   style={ oPopoverStyle}
                   anchorEl={this.state.anchorElement}
                   anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
                   transformOrigin={{horizontal: 'right', vertical: 'top'}}
                   onClose={this.closePopover}>
            {this.getConcatenatedFormulaView()}
          </CustomPopoverView>
        </div>
    );
  }

}



GridConcatenatedAttributePropertyView.propTypes = oPropTypes;


export const view = GridConcatenatedAttributePropertyView;
export const events = oEvents;
