import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

const oEvents = {

};
const oPropTypes = {

  headerData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        value: ReactPropTypes.string,
        width: ReactPropTypes.number
      })
  ),

  settings: ReactPropTypes.shape({
    showCheckbox: ReactPropTypes.bool,
    showEditButton: ReactPropTypes.bool,
    showOpenButton: ReactPropTypes.bool,
    toolbar: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          tooltip: ReactPropTypes.string,
          className: ReactPropTypes.string
        })
    )
  })
};
/**
 * @class ColgroupView - Use to Display and count column in Table view.
 * @memberOf Views
 * @property {array} [headerData] - Pass data of Header in Table.
 * @property {custom} [settings] - Pass model contain showCheckbox, showEditButton, showDeleteButton, showOpenButton, toolbar, id, tooltip, className, sortData, sortBy, sortOrder.
 */

// @CS.SafeComponent
class ColgroupView extends React.Component {

  constructor (props) {
    super(props);
    this.defaultWidth = 200;
  }

  shouldComponentUpdate =(oNextProps)=> {
    var oOldProps = this.props;
    return !CS.isEqual(oNextProps.headerData, oOldProps.headerData);
  }

  getCols =()=> {
    var _this = this;
    var __props = _this.props;
    var aCols = [];
    var iDefaultWidth = this.defaultWidth;
    var iIndex = 0;

    CS.forEach(__props.headerData, function (oHeader) {
      if (oHeader.hideColumn) {
        return;
      }
      var iWidth = oHeader.width || iDefaultWidth;
      var oStyle = {width: iWidth + "px"};

      aCols.push(
          <col key={iIndex++} style={oStyle}/>
      );
    });

    var oSettings = __props.settings;
    var iSettingWidth = 0;

    if(oSettings && !oSettings.isTranspose) {
      oSettings.showEditButton && (iSettingWidth++);
      oSettings.showDeleteButton && (iSettingWidth++);
      oSettings.showOpenButton && (iSettingWidth++);
    }

    if(iSettingWidth) {
      iSettingWidth  = ((iSettingWidth * 30) + 5) + "px";
      aCols.push(
          <col className="settingsCell" key={iIndex} style={{width: iSettingWidth}}/>
      );
    }

    return aCols;
  }

  render() {
    return (
        <colgroup className="colgroupView">
          {this.getCols()}
        </colgroup>
    );
  }

}

ColgroupView.propTypes = oPropTypes;

export const view = ColgroupView;
export const events = oEvents;
