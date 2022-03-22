
import React from 'react';
import ReactDOM from 'react-dom';
import MicroEvent from '../../../../../../../libraries/microevent/MicroEvent.js';
import { view as BPMNCustomProperties } from '../../bpmn-custom-components-properties-view';

class BPMNCustomPropertiesStore {

  constructor (_props){
    this.props = _props;
  }

  get props() {
    return this._props;
  }

  set props(_props) {
    this._props = _props;
    this.triggerChange();
  }

   triggerChange () {
    this.trigger('bpmn-custom-properties-change');
  }
}

MicroEvent.mixin(BPMNCustomPropertiesStore);



let BPMPropertiesViewProvider = (function () {

  let oBPMNPropertiesViewMountPoint = null;
  const oDOM = document.createElement('div');
  let oStore = null;

  let _getViewDOM = function (element, referencedData, customPropertiesData, activeProcess, taxonomy, validationInfo) {
    if (!oBPMNPropertiesViewMountPoint) {
      oStore = new BPMNCustomPropertiesStore({element, referencedData, customPropertiesData, activeProcess, taxonomy, validationInfo});
      oBPMNPropertiesViewMountPoint = ReactDOM.render(<BPMNCustomProperties store={oStore}/>, oDOM);
    }
    else {
      oStore.props = {element, referencedData, customPropertiesData, activeProcess, taxonomy, validationInfo}
    }

    return oDOM;
  };


  return {
    getViewDOM: function (element, referencedData, oCustomPropertiesData, oActiveProcess, oTaxonomyData, oValidationInfo) {
      return _getViewDOM(element, referencedData, oCustomPropertiesData, oActiveProcess, oTaxonomyData, oValidationInfo);
    }
  }
})();


let fCustomPropertyGroupProvider = function (group, element, bpmnFactory, modeling, oConfigDetails, oCustomPropertiesData, bIsAdvancedTabEnabled, oActiveProcess, oTaxonomyData, oValidationInfo) {

  let oDOM = BPMPropertiesViewProvider.getViewDOM(element, oConfigDetails, oCustomPropertiesData, oActiveProcess, oTaxonomyData, oValidationInfo);

    group.entries.push({
      id: 'configView' + bIsAdvancedTabEnabled,
      description: '',
      label: 'configView',
      modelProperty: 'configView',
      get: function () {
      },
      set: function () {
      },
      validate: function () {
      },
      html: oDOM
    });
};


export default fCustomPropertyGroupProvider;