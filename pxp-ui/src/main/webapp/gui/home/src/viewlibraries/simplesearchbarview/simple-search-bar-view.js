import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../tooltipview/tooltip-view';
import ViewUtils from './../utils/view-library-utils';

const oEvents = {
};

const oPropTypes = {
  searchText: ReactPropTypes.string,
  onChange: ReactPropTypes.func,
  onBlur: ReactPropTypes.func,
  isDisabled: ReactPropTypes.bool,
  inputType: ReactPropTypes.string,
  placeHolder: ReactPropTypes.string
};
/**
 * @class SimpleSearchBarView - use to display simple searchbar view in Application.
 * @memberOf Views
 * @property {string} [searchText] -  string of searchText.
 * @property {func} [onChange] - pass function for onChange searchBar text.
 * @property {func} [onBlur] -  a function which used for onBlur event.
 * @property {string} [placeHolder] -  string of placeHolder.
 */

// @CS.SafeComponent
class SimpleSearchBarView extends React.Component{

  constructor(props) {
    super(props);

    this.state = {
      searchText: this.props.searchText ? this.props.searchText : "",
      typing: false
    }

    this.raiseOnBlur = true;
  }

  /*componentWillMount() {
    this.raiseOnBlur = true;
  }*/

  componentDidUpdate() {
    this.raiseOnBlur = true;
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if (oNextProps.hasOwnProperty("searchText")) {
      if(oState.typing){
        return {
          typing: false
        };
      } else if(oNextProps.searchText !== oState.searchText) {
        return {
          searchText: oNextProps.searchText || "",
          typing: false
        };
      }
    }
    return null;
  }

 /* componentWillReceiveProps(oNewProps) {
    if (oNewProps.hasOwnProperty("searchText")) {
      this.setState({
        searchText: oNewProps.searchText
      });
    }
  }*/

  testNumber =(sNumber)=>{
    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();
    let sRegex = oNumberFormat.positiveNumberRegex;

    return sRegex.test(sNumber);
  };

  handleOnChange =(oEvent)=> {
    var sSearchText = oEvent.target.value;
    if (this.props.inputType === "number" && CS.isNotEmpty(sSearchText)) {
      if (!this.testNumber(sSearchText)) {
        return;
      }
    }
    if (CS.isFunction(this.props.onChange)) {
      this.props.onChange(sSearchText);
    }
    /** Need to set state immediately to update search text in state, While entering characters continuously and
     *  trigger change was not done by store**/
    this.setState({
      searchText: sSearchText,
      typing: true
    });
  }

  handleOnBlur =(oEvent)=> {
    if (this.raiseOnBlur) {
      var sSearchText = oEvent.target.value;
      if (CS.isFunction(this.props.onBlur)) {
        this.props.onBlur(sSearchText);
      }
    } else {
      this.raiseOnBlur = true;
    }
  }

  handleClearClicked =()=> {
    if (CS.isFunction(this.props.onChange)) {
      this.props.onChange("");
    } if (CS.isFunction(this.props.onBlur) && this.state.searchText) {
      this.props.onBlur("");
      this.setState({
        searchText: ""
      });
    } else {
      this.setState({
        searchText: ""
      });
    }
  }

  handleClearButtonMouseDown =()=> {
    this.raiseOnBlur = false;
  }

  watchText =(oEvent)=> {
    if (oEvent.key == 'Enter') {
      oEvent.target.blur();
    }
  }

  render() {

    var oClearButtonDOM = null;

    if (this.state.searchText) {
      oClearButtonDOM = (
          <TooltipView label={getTranslation().CLEAR}>
            <div className="simpleSearchBarClearButton" onClick={this.handleClearClicked} onMouseDown={this.handleClearButtonMouseDown}></div>
          </TooltipView>
      );
    }

    let sPlaceHolder = this.props.placeHolder ? this.props.placeHolder : getTranslation().SEARCH;

    let sClassName = this.props.isDisabled ? "simpleSearchBarView disabled" : "simpleSearchBarView";
    return (
        <div className={sClassName}>

          {/*search icon*/}
          <div className="simpleSearchBarSearchIcon"></div>

          {/*search input*/}
          <input className="simpleSearchBarInput"
                 type="text"
                 value={this.state.searchText}
                 placeholder={sPlaceHolder}
                 onKeyPress={this.watchText}
                 onChange={this.handleOnChange}
                 onBlur={this.handleOnBlur}/>

          {/*clear icon*/}
          {oClearButtonDOM}

        </div>
    );
  }

}

SimpleSearchBarView.propTypes = oPropTypes;

export const view = SimpleSearchBarView;
export const events = oEvents;
export const propTypes = SimpleSearchBarView.propTypes;
