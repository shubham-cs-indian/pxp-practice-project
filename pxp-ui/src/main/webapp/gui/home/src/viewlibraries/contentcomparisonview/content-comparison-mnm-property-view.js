import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ContentComparisonMNMPropertyItemViewObj from './content-comparison-mnm-property-item-view';
import TooltipView from '../../viewlibraries/tooltipview/tooltip-view';
import GRConstants from '../../screens/homescreen/screens/contentscreen/tack/golden-record-view-constants';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';
const ContentComparisonMNMPropertyItemView = ContentComparisonMNMPropertyItemViewObj.view;

const oEvents = {};

const oPropTypes = {
  property: ReactPropTypes.object,
  context: ReactPropTypes.string,
  tableId: ReactPropTypes.string,
  selectedRowId: ReactPropTypes.string,
  editableRowId: ReactPropTypes.string,
  selectedLanguageIds: ReactPropTypes.array,
  hideEditRowButton: ReactPropTypes.bool,
  onChangeHandler: ReactPropTypes.func,
};

// @CS.SafeComponent
class ContentComparisonMnmPropertyView extends React.Component {
  constructor (props) {
    super(props);
  }

  static propTypes = oPropTypes;

  handleTagValueChanged = (sTagId, aTagValueRelevanceData, oExtraData) => {
    let oValue = {
      tagId: sTagId,
      tagValueRelevanceData : aTagValueRelevanceData
    };
    this.props.onChangeHandler(oValue);
  };

  handleAttributeValueChanged = (oProperty, oValue) => {
    let oPropertyToSave = {};
    let bIsHTMLAttribute = ViewUtils.isAttributeTypeHtml(oProperty.type);
    if (bIsHTMLAttribute) {
      oPropertyToSave.valueAsHtml = oValue.value;
      oPropertyToSave.value = oValue.valueAsHtml;
    } else {
      oPropertyToSave = oValue;
    }
    this.props.onChangeHandler(oPropertyToSave);
  };

  getIconsTobeShowOnRow = (oProperty) => {
    let aIcons = [];
   /* if (this.props.editableRowId !== oProperty.id && !this.props.hideEditRowButton && !oProperty.disabledRow) {
      aIcons.push(
          <div className="contentPropertyEditButton"
               onClick={this.handleContentComparisonMatchAndMergePropertyEditButtonClicked.bind(this, oProperty.id)}>
          </div>
      );
    }*/

   if(oProperty.isDisabled) {
     aIcons.push(<TooltipView label={getTranslation().DISABLED_SECTION} placement="bottom">
       <div className="notClickableIcon disabledIcon"></div>
     </TooltipView>);
   }

   if(oProperty.isDependent) {
     aIcons.push(<TooltipView label={getTranslation().LANGUAGE_DEPENDENT} placement="bottom">
           <div className="notClickableIcon translatableIcon"></div>
         </TooltipView>
     );
   }

    return (
        <div className="contentComparisonPropertyIconsWrapper">
          {aIcons}
        </div>
    );
  };

  getViewAccordingToType = () => {
    let oProperty = this.props.property;
    let fOnChangeHandler = CS.noop;

    switch (oProperty.propertyType) {
      case "attribute":
        fOnChangeHandler = this.handleAttributeValueChanged.bind(this, oProperty);
        break;

      case "tag":
        oProperty.tagGroupModel.disabled = oProperty.isDisabled;
        fOnChangeHandler = this.handleTagValueChanged;
        break;
    }

    return (
        <ContentComparisonMNMPropertyItemView
            oElement={oProperty}
            sContext={this.props.context}
            fOnChangeHandler={fOnChangeHandler}
            sViewContext={GRConstants.PROPERTY_VIEW}
            sTableId={this.props.tableId}
        />
    )
  };

  getPropertyView = () => {
    let oProperty = this.props.property;
    let sPropertyCellClassName = "contentComparisonPropertyValueWrapper ";
    if (this.props.editableRowId !== oProperty.id) {
      sPropertyCellClassName += "isDisabled";
    }

    let aPropertyView = [];
    aPropertyView.push(
        <div className={sPropertyCellClassName}>
          {this.getViewAccordingToType()}
        </div>);

    aPropertyView.push(this.getIconsTobeShowOnRow(oProperty));

    return aPropertyView;
  };

  render () {
    let sClassName = "contentComparisonMNMPropertyViewContainer";
    return (
        <div className={sClassName}>
          {this.getPropertyView()}
        </div>
    );
  }
}

export const view = ContentComparisonMnmPropertyView;
export const events = oEvents;
