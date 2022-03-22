import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ContentAttributeElementView } from './../../viewlibraries/attributeelementview/content-attribute-element-view';
import { view as TagGroupView } from './../../viewlibraries/taggroupview/tag-group-view.js';
import { view as ContentComparisonMatchAndMergeRelationshipView } from './content-comparison-mnm-relationship-view';
import { view as ChipsView } from '../../viewlibraries/chipsView/chips-view';


const oEvents = {
};

const oPropTypes = {
  oElement: ReactPropTypes.object,
  sContext: ReactPropTypes.string,
  fOnChangeHandler: ReactPropTypes.func,
  sTableId: ReactPropTypes.string,
  sViewContext: ReactPropTypes.string
};

/** Functional Component **/
/**
 * @class ContentComparisonPropertyItemView - Use to Display properties in comparison view.
 * @memberOf Views
 * @property {object} [oMenuItem] - Pass Object contain containerClassName, id, label, menuClassName.
 */


function ContentComparisonPropertyItemView ({oElement, sContext, fOnChangeHandler, sViewContext, sTableId}) {

  let getViewAccordingToRenderingType = (sRendererType, aValues) => {
    let aViews = [];
    switch (sRendererType) {
      case "chip":
        CS.forEach(aValues, function (aValue, iIndex) {
          aViews.push(
              <div className={"chipsWrapper"} key={iIndex}>
                <ChipsView items={aValue}/>
              </div>
          );
        });
        break;
    }

    return aViews;
  };

  let getTagGroupView = (oModel) => {
    let oTagGroupModel = oModel.tagGroupModel;
    let oExtraData = oTagGroupModel.properties["extraData"];
    let oReferencedTags = oModel.masterTagList;
    let oProperties = oModel.tagGroupModel.properties;
    return (
        <TagGroupView
            tagGroupModel={oTagGroupModel}
            tagRanges={oProperties.tagRanges}
            tagValues={oModel.tagValues}
            disabled={oModel.disabled}
            singleSelect={oModel.singleSelect}
            hideTooltip={true}
            showLabel={false}
            extraData={oExtraData}
            masterTagList={oReferencedTags}
            customRequestObject={oModel.customRequestObject}
            onApply={fOnChangeHandler}
            showDefaultIcon={true}
        />
    );
  }


  switch (oElement.propertyType) {
    case "attribute":
      return <ContentAttributeElementView
          model={oElement}
          onBlurHandler={fOnChangeHandler}/>;

    case "tag":
      return getTagGroupView(oElement.tagGroupModel);

    case "basicInfo":
      return getViewAccordingToRenderingType(oElement.rendererType, oElement.values);

    case "relationship":
    case "natureRelationship":
      return <ContentComparisonMatchAndMergeRelationshipView
          property={oElement}
          viewContext={sViewContext}
          tableId={sTableId}/>;
  }
}

ContentComparisonPropertyItemView.propTypes = oPropTypes;

export default {
  view: CS.SafeComponent(ContentComparisonPropertyItemView),
  events: oEvents
}
