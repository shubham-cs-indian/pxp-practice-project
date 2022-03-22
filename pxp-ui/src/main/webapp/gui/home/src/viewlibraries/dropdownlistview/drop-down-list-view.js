import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewLibraryConstants from '../../commonmodule/tack/mss-view-constants';
import ListViewModel from './model/drop-down-list-model';

const oPropTypes = {
  dropDownListModel: ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ListViewModel)).isRequired,
  checkedItems: ReactPropTypes.array,
  checkbox: ReactPropTypes.bool,
  handleOnClick: ReactPropTypes.func,
  handleHideDropDown: ReactPropTypes.func,
  maxHeight: ReactPropTypes.number
};
/**
 * @class DropDownList
 * @memberOf Views
 * @property {array} dropDownListModel - Contains items.
 * @property {array} [checkedItems] - Contains selected items list.
 * @property {bool} [checkbox] - To show checkbox for items selection.
 * @property {func} [handleOnClick] - Execute when item is clicked.
 * @property {func} [handleHideDropDown] - To hide dropdown list.
 * @property {number} [maxHeight] - Height for dropdown.
 */

// @CS.SafeComponent
class DropDownList extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      activeIndex: 0
    }
  }

  componentDidMount() {
    document.addEventListener("click", this.props.handleHideDropDown);
  }

  componentWillUnmount() {
    document.removeEventListener("click", this.props.handleHideDropDown);
  }

  componentDidUpdate(){
    if(this.state.activeIndex >= this.props.dropDownListModel.length){
      this.resetState();
    }
  }

  resetState =()=>{
    this.setState({
      activeIndex: 0
    })
  }

  changeStateByProperty =(sKey, sValue)=>{
    var oCurrentState = {};
    oCurrentState[sKey] = sValue;

    this.setState(oCurrentState);
  }

  onClickHandler =(oModel)=> {
    this.props.handleOnClick(oModel);
  }

  onHoverHandler =(iIndex)=> {
    this.changeStateByProperty('activeIndex', iIndex);
  }

  checkIfValidColor =(sColorHex)=>{
    return sColorHex ? sColorHex.match(/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i) !== null : false;
  }

  //for tag config
  getColorBoxView =(sValue)=> {
    var oStyle = {backgroundColor: sValue};

    return this.checkIfValidColor(sValue) ? <div className="dropDownNodeColorBox" style={oStyle}></div> : null;
  }

  getNodeView =()=> {

    var aDropDownListModels = this.props.dropDownListModel;
    var aCheckedItems = this.props.checkedItems;
    var bUseCheckbox = this.props.checkbox;

    return CS.map(aDropDownListModels, function (oModel, iIndex) {
      var iActiveIndex = this.state.activeIndex;
      var sName = CS.getLabelOrCode(oModel);
      var oColorView = this.getColorBoxView(sName);
      var sNodeContainerClassName = "dropDownNodeContainer ";
      sNodeContainerClassName += (iIndex == iActiveIndex) ? "activeNode " : "";
      var oCheckboxView = null;
      if (bUseCheckbox && oModel.id != ViewLibraryConstants.MSS_NOTHING_FOUND_ID) {
        var bIsChecked = CS.includes(aCheckedItems, oModel.id);
        oCheckboxView =  <input type="checkbox" checked={bIsChecked} />
      }

      return (<div className={sNodeContainerClassName}
                   onClick={this.onClickHandler.bind(this, oModel)}
                   onMouseOver={this.onHoverHandler.bind(this, iIndex)}
                   title={sName}
                   key={sName + "_" + iIndex}>
        {oCheckboxView}
        {sName}
        {oColorView}
      </div>);
    }.bind(this));
  }

  incrementActiveIndex =()=> {
    var aDropDownListModels = this.props.dropDownListModel;
    var iNewActiveIndex = this.state.activeIndex + 1;

    if(iNewActiveIndex < aDropDownListModels.length)
    this.changeStateByProperty('activeIndex', iNewActiveIndex);
  }

  decrementActiveIndex =()=> {
    var iNewActiveIndex = this.state.activeIndex - 1;

    if(iNewActiveIndex >= 0)
    this.changeStateByProperty('activeIndex', iNewActiveIndex);
  }

/*
  getActiveIndex =()=> {
    return this.state.activeIndex;
  }
*/

  getActiveModel =()=> {
    var iActiveIndex = this.state.activeIndex;
    var aDropDownListModels = this.props.dropDownListModel;

    return aDropDownListModels[iActiveIndex];
  }

  render() {

    var aNodeView = this.getNodeView();
    var sClassHide = aNodeView.length === 0 ? 'dropDownItemHidden' : '';
    var iMaxHeight = this.props.maxHeight;
    var iHeight = iMaxHeight ? iMaxHeight : 100;

    var oStyle = {
      maxHeight: iHeight
    };

    return (
        <div className={"dropDownListView " + sClassHide} style={oStyle}>
          {aNodeView}
        </div>
    );
  }

}

DropDownList.propTypes = oPropTypes;

export default DropDownList;
