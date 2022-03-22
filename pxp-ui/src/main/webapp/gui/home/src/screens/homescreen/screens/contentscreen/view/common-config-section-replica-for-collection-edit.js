import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as GridYesNoPropertyView } from '../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import { view as CustomTextFieldView } from '../../../../../viewlibraries/customtextfieldview/custom-text-field-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

const aDisableViaOverlayFieldTypes = [];

const oEvents = {
  COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED: "common_config_section_single_text_changed",
  COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED: "common_config_section_yes_no_button_changed",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  sectionLayout: ReactPropTypes.array,
  disabledFields: ReactPropTypes.array,
  data: ReactPropTypes.object,
  oCalculatedAttributeMapping: ReactPropTypes.object,
  errorTextForCodeEntity: ReactPropTypes.string,
  mappingType: ReactPropTypes.string
};

class CommonConfigSectionView extends React.Component {

  constructor(props) {
    super(props);

    this.setRef =( sContext, element) => {
      this["iconUpload" + sContext] = element;
    };
  }

  componentDidUpdate(){}

  getIsDisabledField = (sKey) => {
    var aDisabledKeys = this.props.disabledFields || [];
    return CS.includes(aDisabledKeys, sKey);
  };

  handleSingleTextChanged =(sKey, sValue)=> {
    var sNewValue = sValue;
    var sContext = this.props.context;

    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED, this, sContext, sKey, sNewValue);
  }

  getSingleTextView=(sValue, sKey, sHintText)=> {
    var sErrorText ="";
    var fOnBlur = this.handleSingleTextChanged.bind(this, sKey);
    var fOnChange = null;
    let bShowCopyToClipboardButton = false;

    if(sKey === "code") {

      let sContext = this.props.context;
      if(sContext === "languageTree") {
        sErrorText = this.props.errorTextForCodeEntity;
      } else {
        sHintText = CS.isEmpty(this.props.errorTextForCodeEntity) ? "" : getTranslation().AUTO_GENERATED_CODE;
      }
      bShowCopyToClipboardButton = true;
    }

    let bIsDisabled = this.getIsDisabledField(sKey);
    return (<div className="singleTextWrapper elementWrapper">
      <CustomTextFieldView className="singleText"
                           value={sValue}
                           onBlur={fOnBlur}
                           onChange={fOnChange}
                           errorText={sErrorText}
                           shouldShowErrorText={true}
                           showCopyToClipboardButton={bShowCopyToClipboardButton}
                           isDisabled={bIsDisabled}
                           hintText={sHintText}/>
    </div>);
  }

  handleGridYesNoChanged=(sKey, sContext, sConfigContext)=> {
    var bValue = !sContext;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED, this, sKey, bValue, sConfigContext);
  }

  getYesNoView=(oElementData, sKey)=> {
    let bIsDisabled = this.getIsDisabledField(sKey);
    var sConfigContext = oElementData.context;
    return (
      <div className="yesNoViewWrapper elementWrapper">
        <div className="yesNoContainer">
          <GridYesNoPropertyView propertyId={oElementData.context} value={oElementData.isSelected}
                                 isDisabled={oElementData.isDisabled || bIsDisabled} onChange={this.handleGridYesNoChanged.bind(this, sKey, oElementData.isSelected, sConfigContext)}/>
        </div>
      </div>
    );
  }

  getElementView=(oElement)=> {
    var oData = this.props.data;
    var sKey = oElement.key;
    var sType = oElement.type;
    var oElementData = oData[sKey];
    let sHintText = (sKey === "label") ? oData["code"] : (oElement.hintText) ? oElement.hintText : "";
    var bDataExists = CS.isString(oElementData) ? true : oElementData;
    var oElView = null;

    switch (sType){

      case "singleText":
        oElView = bDataExists && this.getSingleTextView(oElementData, sKey, sHintText);
        break;

      case "yesNo":
        oElView = bDataExists && this.getYesNoView(oElementData, sKey);
        break;
    }

    if(oElView){
      return  (<div className="commonConfigSectionElement">
        <div className="commonConfigSectionElementHeader">{oElement.label}</div>
        <div className="commonConfigSectionElementBody">{oElView}</div>
      </div>);
    }else {
      return null;
    }
  };

  getView =()=>{
    var _this = this;
    var aSections = this.props.sectionLayout;
    var aDisabledKeys = this.props.disabledFields || [];
    var aSectionView = [];

    let iElementKeyCounter = 1;
    let iRowKeyCounter = 1;
    let iSectionKeyCounter = 1;

    CS.forEach(aSections, function(oSection){
      var aElView = [];
      var iPercentSum = 0;
      var aRowView = [];

      CS.forEach(oSection.elements, function(oElement){
        var oElView = _this.getElementView(oElement);

        if(oElView) {
          if (iPercentSum + oElement.width > 100) {
            aElView.length && aRowView.push(<div className="commonConfigSectionRow" key={iRowKeyCounter++}>{aElView}</div>);
            aElView = [];
            iPercentSum = oElement.width;
          } else {
            iPercentSum += oElement.width;
          }
          var oOverlayView = null;
          if (CS.includes(aDisabledKeys, oElement.key) && CS.includes(aDisableViaOverlayFieldTypes, oElement.type)) {
            oOverlayView = (<div className="commonConfigOverlay"></div>);
          }
          aElView.push(<div className="commonConfigSectionElementWrapper" key={iElementKeyCounter++}>
            {oOverlayView}
            {oElView}
          </div>);
        }
      });

      aElView.length && aRowView.push(<div className="commonConfigSectionRow" key={iRowKeyCounter++}>{aElView}</div>);

      aRowView.length && aSectionView.push(<div className="commonConfigSection" key={iSectionKeyCounter++}>
        <div className="commonConfigSectionHeader"></div>
        <div className="commonConfigSectionBody">{aRowView}</div>
      </div>);
    });

    return aSectionView;
  };

  render() {

    return (
      <div className="commonConfigSectionViewContainer">
        {this.props.data && this.getView()}
      </div>
    );
  }
}

CommonConfigSectionView.propTypes = oPropTypes;


export const view = CommonConfigSectionView;
export const events = oEvents;
