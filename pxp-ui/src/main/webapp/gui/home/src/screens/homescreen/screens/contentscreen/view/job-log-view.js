import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
};

const oPropTypes = {
  failedComponents:ReactPropTypes.array
};

// @CS.SafeComponent
class JobLogView extends React.Component {
  static propTypes = oPropTypes;


  getComponent =(oFailedComponents) => {
    let aFailedList = [];
    let oHeaderDOM = null;
    let sErrorMessage = getTranslation().ERROR_LOG_MESSAGE;
    aFailedList.push(<div className="jobLabel">{oFailedComponents.componentId} : {oFailedComponents.componentLabel}</div>);
    CS.forEach(oFailedComponents.status.failedIds,(sFailedComponent,iIndex) => {
      let aSplitSting = sFailedComponent.split("class");
      if(iIndex == 0){
        oHeaderDOM = ( <tr>
          <th>Id</th>
          <th>Error Log</th>
        </tr>);
      }else{
        oHeaderDOM = null;
      }
      aFailedList.push(<table>
        {oHeaderDOM}
        <tr>
          <td>{aSplitSting[0]}</td>
          <td>{sErrorMessage}</td>
        </tr></table>);
    });
    return (<div className="componentContainer">{aFailedList}</div>);
  };

  render() {

    let aFailedComponents = this.props.failedComponents;
    let aLabelDOM = [];
    CS.forEach(aFailedComponents, (oFailedComponents) => {
      let oComponent = this.getComponent(oFailedComponents);
      aLabelDOM.push(oComponent);
    });
    return (
        <div className="jobLogContainer">{aLabelDOM}</div>
    );
  }
}

export const view = JobLogView;
export const events = oEvents;
