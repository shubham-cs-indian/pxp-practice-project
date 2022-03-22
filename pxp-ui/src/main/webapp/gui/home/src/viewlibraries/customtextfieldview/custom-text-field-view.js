import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import AutoSizeInputView from 'react-input-autosize';
import TooltipView from './../tooltipview/tooltip-view';

import copy from 'copy-to-clipboard';
import alertify from '../../commonmodule/store/custom-alertify-store';
import { getTranslations } from '../../commonmodule/store/helper/translation-manager';

const autoSize = require('../../libraries/autosize/autosize').default;

const oEvents = {};

const oPropTypes = {
  label: ReactPropTypes.string,
  value: ReactPropTypes.string,
  onChange: ReactPropTypes.func,
  onBlur: ReactPropTypes.func,
  onLoseFocus: ReactPropTypes.func,
  onKeyDown: ReactPropTypes.func,
  shouldFocus: ReactPropTypes.bool,
  onForceFocus: ReactPropTypes.func,
  onFocus: ReactPropTypes.func,
  forceBlur: ReactPropTypes.bool,
  hintText: ReactPropTypes.string,
  errorText: ReactPropTypes.string,
  shouldShowErrorText: ReactPropTypes.bool,
  isDisabled: ReactPropTypes.bool,
  isMultiLine: ReactPropTypes.bool,
  multiLineNumberOfRows: ReactPropTypes.string,
  className: ReactPropTypes.string,
  autosizeInput : ReactPropTypes.bool,
  autoSizeWrapperStyle: ReactPropTypes.object,
  showTooltip: ReactPropTypes.bool,
  showSelection: ReactPropTypes.bool
};
/**
 * @class CustomTextFieldView - use for edit textfield view.
 * @memberOf Views
 * @property {string} [label] -  label of textField.
 * @property {string} [value] -  string of textField value.
 * @property {func} [onChange] -  function which is used for onChange textField event.
 * @property {func} [onBlur] -  function which is used for onBlur textField event.
 * @property {func} [onLoseFocus] -  function which is used for onLoseFocus textField event.
 * @property {func} [onKeyDown] -  function which is used for onKeyDown textField event.
 * @property {bool} [shouldFocus] -  boolean value for shouldFocus true or not for selecting textField input.
 * @property {func} [onForceFocus] -  function which is used for onForceFocus textField event.
 * @property {func} [onFocus] -  function which is used for onFocus textField event.
 * @property {bool} [forceBlur] -  boolean value for forceBlur true or not textField input.
 * @property {string} [hintText] -  hintText of textField.
 * @property {string} [errorText] -  label of errorText.
 * @property {bool} [shouldShowErrorText] -  boolean value for shouldShowErrorText true or not in textField input.
 * @property {bool} [isDisabled] -  boolean value for isDisabled textField input or not.
 * @property {bool} [isMultiLine] -  boolean value for isMultiLine textField input or not.
 * @property {string} [multiLineNumberOfRows] -  string of multiLineNumberOfRows.
 * @property {string} [className] -  string of className.
 * @property {bool} [autosizeInput] -  boolean value for autosizeInput textField input or not.
 * @property {object} [autoSizeWrapperStyle]
 * @property {bool} [showTooltip] -  boolean value for showTooltip  or not.
 * @property {bool} [showSelection] -  boolean value for showSelection in textField input or not.
 */

// @CS.SafeComponent
class CustomTextFieldView extends React.Component {

  static typing = false;

  constructor(props) {
    super(props);
    this.state = {
      value: this.validatedValue(this.props.value)
    }
  }

  validatedValue = (sValue) => {
    if(CS.isNull(sValue)) {
      return "";
    }

    return sValue;
  };

  componentDidMount =()=> {
    this.resizeTextField();
    if (this.props.shouldFocus) {
      this.textField.focus();
      if(this.props.showSelection) {
        this.textField.select();
      } else {
        let sText = this.state.value;
        let iCursorPosition = sText.length;
        this.textField.setSelectionRange(iCursorPosition, iCursorPosition);
      }
      this.props.onForceFocus && this.props.onForceFocus();
    }
  }

  componentDidUpdate =()=> {
    this.resizeTextField(true);
    if (this.props.shouldFocus || CustomTextFieldView.typing) {
      this.textField && this.textField.focus();
      this.props.onForceFocus && this.props.onForceFocus();
    }
  }

  resizeTextField = (bForceUpdate) => {
    let oTextField = this.textField;
    if (oTextField) {
      autoSize(oTextField, bForceUpdate);
    }
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    //Note : if value comes as null, then the old value in the input field is not overridden. Hence, if null, "" is set. (Probably a react bug!)
    /**
     * On every update this method called
     * There fore we maintaining typing state on change of value typing state changed with true,
     * after state changed this method called and according to this we making decision value need to pick from prop or state.
     */
    if(CustomTextFieldView.typing){
      return {
        value: oState.value
      };
    } else if(oNextProps.value !== oState.value) {
      return {
        value: oNextProps.value || "",
      };
    }
    return null;
  }

  shouldComponentUpdate =(oNextProps, oNextState)=> {
    var oProps = this.props;
    return !(CS.isEqual(oNextProps, oProps) && CS.isEqual(this.state, oNextState));
  }

  domMounted =(oDOM)=> {
    this.textField = oDOM;
  }

  handleOnChange =(oEvent)=> {
    var sNewVal = oEvent.target.value;
    if(this.props.onChange && !this.props.autosizeInput ) {
      this.props.onChange.call(this, sNewVal);
    } else {
      this.setState({value: sNewVal});
      CustomTextFieldView.typing = true;
    }

  }

  handleCopyToClipboardClick = (sValue) => {
    copy(sValue) ? alertify.success(getTranslations().CODE_COPIED) : alertify.error(getTranslations().COPY_TO_CLIPBOARD_FAILED);
  }

  handleAutoSizeInputBlur =(oEvent)=> {
    var sNewVal = this.state.value;
    this.props.onChange.call(this, sNewVal);
    CustomTextFieldView.typing = false;
  }

  handleOnFocus =()=>{
    if (this.props.onFocus) {
      this.props.onFocus();
    }
  }

  callOnBlur =()=> {
    let sValue =this.validatedValue(this.props.value);
    if(this.state.value != sValue || this.props.forceBlur) {
      if (this.props.onBlur) {
        this.props.onBlur(this.state.value);
      }
    }
  }

  handleOnBlur =()=> {
    this.callOnBlur();
    if (this.props.onLoseFocus) {
      this.props.onLoseFocus();
    }
    CustomTextFieldView.typing = false;
  }

  handleOnKeyDown =(oEvent)=> {
    if (oEvent.keyCode == 13) {
      this.textField.selectionEnd = this.textField.selectionStart;
    }

    if (oEvent.keyCode == 9 || (oEvent.keyCode == 13 && !this.props.isMultiLine)) {
      this.callOnBlur();
    }

    if (this.props.onKeyDown) {
      this.props.onKeyDown(oEvent);
    }
  }

  getTooltip = (oDom, sValue) => {
    return (
        this.props.showTooltip ?
        <TooltipView placement="bottom" label={sValue || this.props.value}>
          {oDom}
        </TooltipView>  : oDom
    )
  };

  getView =()=> {
    var __props = this.props;
    var sValue = this.state.value;
    var sPlaceholder = __props.hintText || "";
    var sDescription = __props.errorText || null;
    var bDisabled = __props.isDisabled;
    var bIsMultiLine = __props.isMultiLine;
    var sRows = __props.multiLineNumberOfRows || 1;
    var sClassName = __props.className || "";
    var isAutoSize = __props.autosizeInput;
    var sInputClass = "customTextField ";
    if (bDisabled) {
      sInputClass += "isDisabled ";
    }
    sInputClass += sClassName;

    var oAutoResizeStyle = __props.autoSizeWrapperStyle;
    var oInputStyle = {minWidth: "120px"};
    var oTextDOM = null;
    if (isAutoSize && !bDisabled) {
      oTextDOM = (
          this.getTooltip(
              <div className="autoSizeInputWrapper" onBlur={this.handleAutoSizeInputBlur}>
                <AutoSizeInputView
                    value={sValue}
                    className={sInputClass}
                    inputClassName={"labelInput"}
                    onChange={this.handleOnChange}
                    inputStyle={oInputStyle}
                    style={oAutoResizeStyle}
                />
              </div>)
      );
    } else if (bDisabled) {
      let oCopyToClipboardDOM = (this.props.showCopyToClipboardButton) ?
        (<TooltipView placement={"top"} label={getTranslations().COPY_TO_CLIPBOARD_TOOLTIP}>
          <div
          className={"copyToClipboard"}
          onClick={this.handleCopyToClipboardClick.bind(this, sValue)}></div>
        </TooltipView>) : null;

      oTextDOM =
          (<div className={sInputClass}
                 disabled={bDisabled}
                 ref={this.domMounted}
                 placeholder={sPlaceholder}>
              {this.getTooltip(<span>{sValue}</span>)}
              {oCopyToClipboardDOM}
           </div>)
    } else if (bIsMultiLine) {
      oTextDOM = this.getTooltip(<textarea readOnly={bDisabled}
                           disabled={bDisabled}
                           ref={this.domMounted}
                           rows={sRows}
                           className={sInputClass}
                           placeholder={sPlaceholder}
                           value={sValue}
                           onKeyDown={this.handleOnKeyDown}
                           onChange={this.handleOnChange}
                           onFocus={this.handleOnFocus}
                           onBlur={this.handleOnBlur}/>)
    } else {
      oTextDOM = (
          this.getTooltip(
              <input readOnly={bDisabled}
               disabled={bDisabled}
               ref={this.domMounted}
               className={sInputClass}
               placeholder={sPlaceholder}
               value={sValue}
               onKeyDown={this.handleOnKeyDown}
               onChange={this.handleOnChange}
               onFocus={this.handleOnFocus}
               onBlur={this.handleOnBlur}/>, sValue)
      )
    }

    let oDescriptionDOM = __props.shouldShowErrorText && sDescription ?
        <div className="customTextFieldDescription">{sDescription}</div> : null;

    return (<React.Fragment>
      {/*{oTooltipDOM}*/}
      {oTextDOM}
      {oDescriptionDOM}
    </React.Fragment>)
  }

  render () {
    return  <div className="customTextFieldViewContainer">{this.getView()}</div>;
 }
}

CustomTextFieldView.propTypes = oPropTypes;

export const view = CustomTextFieldView;
export const events = oEvents;
