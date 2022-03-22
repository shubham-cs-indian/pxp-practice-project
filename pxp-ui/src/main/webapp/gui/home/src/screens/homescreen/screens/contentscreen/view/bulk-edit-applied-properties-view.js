import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ViewUtils from '../view/utils/view-utils';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContentAttributeElementView } from '../../../../../viewlibraries/attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from '../../../../../viewlibraries/attributeelementview/model/content-attribute-element-view-model';
import { view as ContentSectionElementViewNew } from './content-section-element-view-new';
import { view as TagGroupView } from '../../../../../viewlibraries/taggroupview/tag-group-view.js';
import MockDataForEntityBaseTypes from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';

const oEvents = {
  BULK_EDIT_APPLIED_PROPERTIES_VALUE_CHANGED: "bulk_edit_applied_properties_value_changed",
};

const oPropTypes = {
  referencedAttributes: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  attributes: ReactPropTypes.array,
  tags: ReactPropTypes.array
};

// @CS.SafeComponent
class BulkEditAppliedPropertiesView extends React.Component{

  constructor(props) {
    super(props);
  }

  handleAttributeValueChanged =(sAttributeId, valueData)=> {
    EventBus.dispatch(oEvents.BULK_EDIT_APPLIED_PROPERTIES_VALUE_CHANGED, sAttributeId, valueData);
  };

  getAppliedAttributesView = () => {
    let _this = this;
    let oProps = _this.props;
    let aViews = [];
    let aAppliedAttributes = oProps.attributes;
    let oReferencedAttributes = oProps.referencedAttributes;

    if(CS.isEmpty(aAppliedAttributes)) {
      return null;
    }

    CS.forEach(aAppliedAttributes, function (oProperty, iIndex) {
      let oReferencedAttribute = oReferencedAttributes[oProperty.attributeId];

      let oProperties = {
        hideDescription: true,
        isMultiLine: false
      };

      if (ViewUtils.isAttributeTypeNumber(oReferencedAttribute.type) || ViewUtils.isAttributeTypeText(oReferencedAttribute.type)) {
        oProperties.isMultiLine = true;
      }

      let value = oProperty.valueAsHtml || oProperty.value;
      if (CS.isEmpty(value) && !CS.isNumber(value)) {
        value = "";
      }
      let oElement = {
        id: oReferencedAttribute.id,
        attribute: oReferencedAttribute,
        type: "attribute",
        contentAttributes: []
      };

      let oContentAttributeElementViewModel = new ContentAttributeElementViewModel(oReferencedAttribute.id, CS.getLabelOrCode(oReferencedAttribute), value, false, "", "", oReferencedAttribute, oProperties);
      let oView =
          <div className="sectionElementContainerNew">
            <ContentAttributeElementView
                model={oContentAttributeElementViewModel}
                onBlurHandler={_this.handleAttributeValueChanged.bind(_this, oReferencedAttribute.id)}/>
          </div>;

      aViews.push(
          <ContentSectionElementViewNew
              key={iIndex}
              sectionElement={oElement}
              masterEntityId={oReferencedAttribute.id}
              label={CS.getLabelOrCode(oReferencedAttribute)}
              tooltip={CS.getLabelOrCode(oReferencedAttribute)}
              selectedElementInformation={oElement}
              referencedAttributes={oReferencedAttributes}
              showCrossIcon={true}>
            {oView}
          </ContentSectionElementViewNew>
      );
    });

    return (
        <div className="appliedAttributes">
          <div className="appliedElementsGroupHeader">{getTranslation().ATTRIBUTES}</div>
          {aViews}
        </div>);
  };

  getAppliedTagsView = () => {
    let _this = this;
    let oProps = _this.props;
    let aViews = [];
    let aAppliedTags = oProps.tags;
    let oReferencedTags = oProps.referencedTags;

    if(CS.isEmpty(aAppliedTags)) {
      return null;
    }

    CS.forEach(aAppliedTags, function (oTagInstance, iIndex) {
      let oReferencedTag = oReferencedTags[oTagInstance.tagId];
      let oElement = {
        id: oReferencedTag.id,
        tag: oReferencedTag,
        type: "tag",
        contentTags: []
      };

      aViews.push(
          <ContentSectionElementViewNew
              key={iIndex}
              sectionElement={oElement}
              masterEntityId={oReferencedTag.id}
              label={CS.getLabelOrCode(oReferencedTag)}
              selectedElementInformation={oElement}
              referencedTags={oReferencedTags}
              showCrossIcon={true}
              showDefaultIcon={true}>
            {_this.getTagGroupView(oTagInstance, oReferencedTag)}
          </ContentSectionElementViewNew>
      )
    });

    return (
        <div className="appliedTags">
          <div className="appliedElementsGroupHeader">{getTranslation().TAGS}</div>
          {aViews}
        </div>
    );
  };

  getTagGroupView = (oTagInstance, oMasterTag) => {
    let sContext = "bulkEditTag";
    let oDummyEntityTag = {
      id: oTagInstance.tagId,
      baseType: MockDataForEntityBaseTypes.tagInstanceBaseType,
      tagId: oMasterTag.id,
      tagValues: CS.cloneDeep(oTagInstance.tagValues)
    };
    let oTags = {
      [oDummyEntityTag['id']]: oDummyEntityTag
    };
    let oExtraData = {
      outerContext: sContext,
      entityId: oTagInstance.tagId
    };

    let oTagGroupModel = ViewUtils.getTagGroupModels(oMasterTag, {tags: oTags}, {}, sContext);
    let oProperties = oTagGroupModel.tagGroupModel.properties;
    if(CS.isEmpty(oTagGroupModel.tagGroupModel.tooltip)) {
      oTagGroupModel.tagGroupModel.tooltip = CS.getLabelOrCode(oMasterTag);
    }
    return (
        <div className="sectionElementContainerNew">
          <TagGroupView
              tagGroupModel={oTagGroupModel.tagGroupModel}
              tagRanges={oProperties.tagRanges}
              tagValues={oTagGroupModel.tagValues}
              disabled={oTagGroupModel.disabled}
              singleSelect={oProperties.singleSelect}
              hideTooltip={false}
              masterTagList={this.props.referencedTags}
              showLabel={false}
              extraData={oExtraData}
              customPlaceholder={getTranslation().PLEASE_CLICK_TO_SELECT}
              showDefaultIcon={true}
          />
        </div>
    );
  }

  render() {

    return (
        <div className="appliedElementViewContainer">
          {this.getAppliedAttributesView()}
          {this.getAppliedTagsView()}
        </div>
    );
  }
}

BulkEditAppliedPropertiesView.propTypes = oPropTypes;

export const view = BulkEditAppliedPropertiesView;
export const events = oEvents;
