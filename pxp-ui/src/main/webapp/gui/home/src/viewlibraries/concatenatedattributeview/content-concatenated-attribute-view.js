import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {blue} from '@material-ui/core/colors';
import ContentConcatenatedAttributeViewModel from './model/content-concatenated-attribute-view-model';
import ViewUtils from './../utils/view-library-utils';
import ToolTip from '../tooltipview/tooltip-view';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import aAlllowedRTEIcons from '../../commonmodule/tack/mock-data-ids-for-froala-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import {view as DisconnectedHTMLView} from "../disconnectedhtmlview/disconnected-html-view";

const oEvents = {
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ContentConcatenatedAttributeViewModel).isRequired,
  onChange: ReactPropTypes.func
};
/**
 * @class ContentConcatenatedAttributeView
 * @memberOf Views
 * @property {custom} model - InstanceOf ContentConcatenatedAttributeViewModel.
 * @property {func} [onChange] - Executes when concatenated attribute value is changed.
 */

// @CS.SafeComponent
class ContentConcatenatedAttributeView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      isEditMode: false
    }
  }

  handleOnChange =(oExpression, oEvent)=> {
    let bIsHTml = (oExpression.type === "html");
    let oData = bIsHTml ? oEvent : oEvent.target.value;
    var sNewValueToStore = bIsHTml ? oEvent.textValue : oEvent.target.value;
    var sOldValueToCompare = oExpression.value;

    if ((sOldValueToCompare !== sNewValueToStore) || (oEvent.htmlValue !== oExpression.valueAsHtml)) {
      this.props.onChange.call(this, oData, oExpression.id);
    }
  }

/*
  handleKeyDown =(oEvent)=> {
    if (oEvent.key == "Tab") {
      this.handleOnChange();
    }
  }
*/

  toggleEditIcon =()=> {
    var _this = this;
    this.setState({
      isEditMode: !_this.state.isEditMode
    });
  }

  getDisabledString =(aExpressionList)=> {
    var sString = "";
    CS.forEach(aExpressionList, function (oExpression) {
      if (ViewUtils.isAttributeTypeHtml(oExpression.baseType) || oExpression.type == "html"){
        sString = sString.concat(oExpression.valueAsHtml)
      }else{
        sString = sString.concat(oExpression.value);
      }
    });
    return sString;
  }

  getPreviewString =(aExpressionList)=> {
    var sString = "";
    CS.forEach(aExpressionList, function (oExpression) {
      var sExpressionValue = oExpression.value || "";
      if(oExpression.type === "html" || ViewUtils.isAttributeTypeHtml(oExpression.baseType)){
        sExpressionValue = oExpression.valueAsHtml || oExpression.value || "";
        sExpressionValue = sExpressionValue.replace("<p>", "<span>");
        sExpressionValue = sExpressionValue.replace("</p>", "</span>");
      }

      if(oExpression.attributeId || oExpression.tagId){
        // sExpressionValue = '<span style="color: rgb(191, 185, 185);font-weight: 500;font-style: italic;">' + sExpressionValue + "</span>";
      }
      sString = sString.concat(sExpressionValue);
    });
    return sString;
  }

/*
  getCustomMaterialStyles =()=>{

    return {
      inputStyles: {
        "fontSize": "14px"
      },

      floatingLabelStyles: {
        "fontSize": "14px",
        "color": "#555"
      },

      underlineStyle: {
        color: '#ccc',
        borderWidth: '1px'
      },

      underlineFocusStyle: {
        color: blue["300"],
        borderWidth: '1px'
      }
    }
  }
*/

  getPreviewView =()=> {
    var oModel = this.props.model;
    var aExpressionList = oModel.expressionList;
    var sDisabledString = this.getPreviewString(aExpressionList);

    return <span dangerouslySetInnerHTML={{__html: sDisabledString}}></span>
  }

  getFroalaView =(oExpression)=> {
    let _this = this;
    var fHandler = _this.handleOnChange.bind(_this, oExpression);
    var aAllowedRTEIcons = aAlllowedRTEIcons;
    let sValue = oExpression.valueAsHtml || oExpression.value;
    return (
        <FroalaWrapperView dataId={oExpression.type === "html" ? oExpression.id : ""}
                                    activeFroalaId={oExpression.id}
                                    content={sValue}
                                    toolbarIcons={aAllowedRTEIcons}
                                    showPlaceHolder={false}
                                    handler={fHandler}/>)

  }

  getEditableExpressionViewDOM =()=> {
    var oModel = this.props.model;
    var aExpressionList = oModel.expressionList;
    return CS.map(aExpressionList, this.getFroalaView);
  }

  getValueDOM =()=> {
    var oModel = this.props.model;
    // var sLabel = CS.getLabelOrCode(oModel);
    var bDisabled = oModel.isDisabled;
    // var sPlaceholder = oModel.placeholder;
    var aExpressionList = oModel.expressionList;


    var oInputView = {};
    if (bDisabled) {

      var oModel = this.props.model;
      var aExpressionList = oModel.expressionList;
      var sDisabledString = oModel.showDisconnected ? this.getDisabledString(aExpressionList) : this.getPreviewString(aExpressionList);
      oInputView = <DisconnectedHTMLView content={sDisabledString} className="disabledDisconnectedHTMLView"/>;
      /*
      var oErrorStyle = {color: '#555', borderWidth: '1px', fontStyle: 'italic'};
      var oUnderlineStyle = {color: '#ccc', borderWidth: '1px'};
      var oUnderlineFocusStyle = {color: blue["300"], borderWidth: '1px'};

      var sClassName = "textFieldCustom textFieldDisabled";
      var sInputType = "text";
      var oCustomStyles = this.getCustomMaterialStyles();
      var sRef = oModel.id;
      oInputView = (
            <TextField
                hintText={sPlaceholder}
                label={sLabel}
                floatingLabelText={sLabel}
                isDisabled={true}
                ref={sRef}
                key={sRef}
                className={sClassName}
                fullWidth={true}
                errorStyle={oErrorStyle}
                inputStyle={oCustomStyles.inputStyles}
                multiLine={true}
                type={sInputType}
                floatingLabelStyle={oCustomStyles.floatingLabelStyles}
                defaultValue={sDisabledString}
                value={sDisabledString}
                underlineStyle={oUnderlineStyle}
                underlineFocusStyle={oUnderlineFocusStyle}
            />);
      */
    }
    else {
      var oInnerValueDOM = this.state.isEditMode ? this.getEditableExpressionViewDOM() : this.getPreviewView();
      oInputView = (
          <div className="concatenatedAttributeEditableWrapper">
            {/*<div className="concatenatedAttributeInputLabel textFieldLabel">{sLabel}</div>*/}
            <div className="concatenatedAttributeElementWrapper">
              {oInnerValueDOM}
            </div>
            {/*<div className="concatenatedAttributeInputDescription textFieldDescription">{sDescription}</div>*/}
          </div>
      );
    }
    return oInputView;
  }

  getUpperIconContainer =()=> {
    var bIsEditMode = this.state.isEditMode;
    var sTooltipValue = getTranslation().ENABLE_EDIT_MODE;
    var sIconClassName = " editIcon";
    if(bIsEditMode){
      sIconClassName = " previewIcon";
      sTooltipValue = getTranslation().ENABLE_VIEW_MODE;
    }
    return (
        <div className="concatenatedUpperIconContainer">
          <ToolTip placement="bottom" label={sTooltipValue}>
            <div className={"modeIcon" + sIconClassName} onClick={this.toggleEditIcon}></div>
          </ToolTip>
        </div>
    );
  }

  render() {
    var oValueDom = this.getValueDOM();
    let oModel = this.props.model;
    let aExpressionList = oModel.expressionList;
    let bIsHTMLExpressionExist = CS.some(aExpressionList, {type: "html"});
    var oUpperIconContainer = this.props.model.isDisabled || !bIsHTMLExpressionExist ? null : this.getUpperIconContainer();

    /**
    NOTE: Adding fr-view in className so that html formatting is applied to the elements in disconnectedHtmlView same as in froala editor
      It handles following scenarios
      1. Paragraph style formatting
      2. Smileys
      3. Table formatting
    */
    return (
        <div className="contentConcatenatedAttributeContainer fr-view">
          {oUpperIconContainer}
          {oValueDom}
        </div>
    );

  }
}

ContentConcatenatedAttributeView.propTypes = oPropTypes;

export const view = ContentConcatenatedAttributeView;
export const events = oEvents;
