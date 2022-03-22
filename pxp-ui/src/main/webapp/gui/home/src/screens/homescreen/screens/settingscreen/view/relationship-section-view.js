import React from 'react';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

const oEvents = {};

// @CS.SafeComponent
class RelationshipSectionView extends React.Component {
  getSide2ClassName = () => {
    return '';
  };

  render() {

    var oRelationshipElement = this.props.relationshipSideElement;
    var sSideId = oRelationshipElement.klassId;
    var sCardinality = oRelationshipElement.cardinality;
    var sSideNo = oRelationshipElement.sideNo;
    var sSide2ClassName = this.getSide2ClassName(sSideId);

    return (
        <div className="relationshipSectionContainer">
          <div className="relationshipSectionLabelContainer blockContainer">
            <div className="relationshipSide2Label blockLabel">{getTranslation().SIDE} {sSideNo}</div>
            <div className="relationshipSide2InputContainer inputContainer">
              <input className="relationshipSide2Input input"
                     value={sSide2ClassName}
                     disabled={true}/>
            </div>
          </div>
          <div className="relationshipSectionCardinalityContainer blockContainer">
            <div className="relationshipSide2CardinalityLabel blockLabel">{getTranslation().CARDINALITY}</div>
            <div className="relationshipSide2CardinalityInputContainer inputContainer">
              <input className="relationshipSide2CardinalityInput input"
                     value={sCardinality}
                     disabled={true}/>
            </div>
          </div>
        </div>
    );
  }
}

export const view = RelationshipSectionView;
export const events = oEvents;
