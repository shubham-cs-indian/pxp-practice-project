import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import { view as MSSView } from '../multiselectview/multi-select-search-view';
import ConfigDataEntitiesDictionary from '../../commonmodule/tack/config-data-entities-dictionary';

const oPropTypes = {
  context: ReactPropTypes.string,

  //type list props
  isTypeListDisabled: ReactPropTypes.bool,
  selectedType: ReactPropTypes.string,
  onTypeListChanged: ReactPropTypes.func,

  //entity list props
  isPropertyListDisabled: ReactPropTypes.bool,
  isPropertyListMultiSelect: ReactPropTypes.bool,
  referencedProperties: ReactPropTypes.object,
  selectedProperties: ReactPropTypes.array,
  excludedProperties: ReactPropTypes.array,
  onPropertyListChanged: ReactPropTypes.func,
};
/**
 * @class AttributeAndTagSelectorView
 * @memberOf Views
 * @property {string} [context]
 * @property {bool} [isTypeListDisabled]
 * @property {string} [selectedType]
 * @property {func} [onTypeListChanged]
 * @property {bool} [isPropertyListDisabled]
 * @property {bool} [isPropertyListMultiSelect]
 * @property {object} [referencedProperties]
 * @property {array} [selectedProperties]
 * @property {array} [excludedProperties]
 * @property {func} [onPropertyListChanged]
 */


// @CS.SafeComponent
class AttributeAndTagSelectorView extends React.Component {

  constructor (props) {
    super(props);
  }

  handlePropertyTypeChanged = (aSelectedItems) => {
    if (CS.isFunction(this.props.onTypeListChanged)) {
      this.props.onTypeListChanged(aSelectedItems)
    }
  };

  handlePropertyListChanged = (aSelectedItems, oReferencedData) => {
    if (CS.isFunction(this.props.onPropertyListChanged)) {
      this.props.onPropertyListChanged(aSelectedItems, oReferencedData);
    }
  };

  //attribute is set as default type if no type is provided
  getSelectedType = () => {
    let sSelectedType = !CS.isEmpty(this.props.selectedType) ? this.props.selectedType : "attribute";
    return sSelectedType;
  };

  getRequestResponseInfo = () => {
    let oRequestResponseInfo = {};
    let sSelectedType = this.getSelectedType();

    switch (sSelectedType) {
      case "attribute":
        oRequestResponseInfo = {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
        };
        break;

      case "tag":
        oRequestResponseInfo = {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.TAGS,
        };
        break;
    }

    return oRequestResponseInfo;
  };

  getPropertyTypeData = () => {
    let aTypeData = [
      {
        id: "attribute",
        label: getTranslation().ATTRIBUTES
      },
      {
        id: "tag",
        label: getTranslation().TAGS
      }
    ];

    return aTypeData;
  };

  getTypeView = () => {
    let oProps = this.props;

    return (
        <div className="attributeAndTagSelectorMSSContainer">
          <MSSView
              disabled={oProps.isTypeListDisabled}
              items={this.getPropertyTypeData()}
              selectedItems={[this.getSelectedType()]}
              isMultiSelect={false}
              onApply={this.handlePropertyTypeChanged.bind(this)}
              cannotRemove={true}
              context={oProps.context}
          />
        </div>
    )
  };

  getListView = () => {
    let oProps = this.props;
    let sContext = oProps.context + this.getSelectedType();

    return (
        <div className="attributeAndTagSelectorMSSContainer">
          <LazyMSSView
              isMultiSelect={oProps.isPropertyListMultiSelect}
              disabled={oProps.isPropertyListDisabled}
              selectedItems={oProps.selectedProperties}
              excludedItems={oProps.excludedProperties}
              context={sContext}
              referencedData={oProps.referencedProperties}
              requestResponseInfo={this.getRequestResponseInfo()}
              onApply={this.handlePropertyListChanged}
              cannotRemove={true}
          />
        </div>
    )
  };

  render () {

    return (<div className="attributeAndTagSelectorContainer">
      <div className="attributeAndTagSelectorBoxContainer">
        {this.getTypeView()}
      </div>
      <div className="attributeAndTagSelectorBoxContainer attributeAndTagSelectorList">
        {this.getListView()}
      </div>
    </div>);
  }

}

AttributeAndTagSelectorView.propTypes = oPropTypes;

export const view = AttributeAndTagSelectorView;
