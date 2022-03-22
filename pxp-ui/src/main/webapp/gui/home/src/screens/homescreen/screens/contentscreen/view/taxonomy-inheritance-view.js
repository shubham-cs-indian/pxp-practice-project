import CS from '../../../../../libraries/cs';
import React from 'react'
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as CustomDialogView } from './../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ChipsView } from "./../../../../../viewlibraries/chipsView/chips-view";
import GenericTableView from '../../../../../viewlibraries/generictableview/generic-table-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
    TAXONOMY_INHERITANCE_DIALOG_CLOSE: "taxonomy_inheritance_dialog_close",
    TAXONOMY_INHERITANCE_ADAPT_TAXONOMY: "taxonomy_inheritance_adapt_taxonomy",
    TAXONOMY_INHERITANCE_ADAPTALL_TAXONOMY: "taxonomy_inheritance_adaptall_taxonomy",
    TAXONOMY_INHERITANCE_REVERT_TAXONOMY: "taxonomy_inheritance_revert_taxonomy",
    TAXONOMY_INHERITANCE_REVERTALL_TAXONOMY: "taxonomy_inheritance_revertall_taxonomy",
};

const TaxonomyInheritanceView = (oProps) => {

    const handleDialogButtonAction = (sButtonId) => {
        EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_DIALOG_CLOSE, sButtonId, {isResolved: true});
    };

    const handleAdaptTaxonomy = (oBaseArticleTaxonomy, iIndex, aTaxonomies) => {
        let sArticleTaxonomy  = aTaxonomies[iIndex].id;
        EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_ADAPT_TAXONOMY, oBaseArticleTaxonomy, sArticleTaxonomy, iIndex, oProps.articleId, oProps.parentContentId);
    };

    const handleAdaptAllTaxonomy = () => {
        EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_ADAPTALL_TAXONOMY, oProps.articleId, oProps.parentContentId);
    };

    const handleRevertTaxonomy = (oBaseArticleTaxonomy, iIndex, aTaxonomies) => {
        let sArticleTaxonomy  = aTaxonomies[iIndex].id;
        EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_REVERT_TAXONOMY, oBaseArticleTaxonomy, sArticleTaxonomy, oProps.articleId, oProps.parentContentId);
    };

    const handleRevertAllTaxonomy = () => {
        EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_REVERTALL_TAXONOMY, oProps.articleId, oProps.parentContentId);
    };

    const getBodyData = () => {
        let _oProps = oProps;
        let oContentIdVsTypesTaxonomies = _oProps.contentIdVsTypesTaxonomies;

        let oBaseArticleContentIdVsTypesTaxonomies = oContentIdVsTypesTaxonomies[_oProps.parentContentId];
        let oBaseArticleTaxonomies = oContentIdVsTypesTaxonomies[_oProps.parentContentId].taxonomyIds;

        let oArticleContentIdVsTypesTaxonomies = oContentIdVsTypesTaxonomies[_oProps.articleId];
        let oArticleTaxonomies = oContentIdVsTypesTaxonomies[_oProps.articleId].taxonomyIds;

        let aBaseArticleTaxonomyStructure = getTaxonomiesStructure(oBaseArticleContentIdVsTypesTaxonomies.taxonomyIds, oArticleTaxonomies, true);
        let aArticleTaxonomyStructure = getTaxonomiesStructure(oArticleContentIdVsTypesTaxonomies.taxonomyIds, oBaseArticleTaxonomies, false);

        let aBaseArticleChipView = getChipsViewData(aBaseArticleTaxonomyStructure.baseArticleTaxonomies, aBaseArticleTaxonomyStructure.articleTaxonomies, true);
        let aArticleChipView = getChipsViewData(aArticleTaxonomyStructure.articleTaxonomies, aArticleTaxonomyStructure.baseArticleTaxonomies, false);

        let rows = [];

        aBaseArticleChipView.forEach((viewData, index) => {
            rows.push({
                id: index,
                cells: [viewData, aArticleChipView[index] || {cellView: null, actionView: null}]
            })
        })

        return rows;
    }

    const getTaxonomiesStructure = (aArticleIds, aBaseArticleIds, bIsBaseArticle) => {
        let articleContentIdVsTypesTaxonomies = CS.sortBy(aArticleIds, "id");
        let baseArticleContentIdVsTypesTaxonomies = CS.sortBy(aBaseArticleIds, "id");
        let aBaseArticleTaxonomies = articleContentIdVsTypesTaxonomies;
        let aUniqueTaxonomiesInBaseArticle = CS.differenceBy(baseArticleContentIdVsTypesTaxonomies, articleContentIdVsTypesTaxonomies,'id');
        let aUniqueTaxonomiesInArticle = CS.differenceBy(articleContentIdVsTypesTaxonomies,baseArticleContentIdVsTypesTaxonomies, "id");
        let aAllTaxonomies = baseArticleContentIdVsTypesTaxonomies.concat(aUniqueTaxonomiesInArticle);
        let aUniqueTaxonomies = aUniqueTaxonomiesInBaseArticle;
        if (bIsBaseArticle) {
            aUniqueTaxonomies = aUniqueTaxonomiesInArticle;
            aAllTaxonomies = articleContentIdVsTypesTaxonomies.concat(aUniqueTaxonomiesInBaseArticle);
            CS.forEach(aUniqueTaxonomiesInBaseArticle, (oArticleTaxonomy, iIndex) => {
                let oEmptyTaxonomy = {id: ""};
                oEmptyTaxonomy.hideAdaptIcon = oArticleTaxonomy.isDisabled ? true :  false;
                aBaseArticleTaxonomies.push(oEmptyTaxonomy);
            })
            var aCommonTaxonomies = baseArticleContentIdVsTypesTaxonomies.filter(function(obj) { return CS.find(articleContentIdVsTypesTaxonomies, {id: obj.id})});
            CS.forEach(aCommonTaxonomies, oCommonTaxonomy => {
                let iIndex = CS.findIndex(aBaseArticleTaxonomies, {id: oCommonTaxonomy.id});
                if (iIndex != -1 && oCommonTaxonomy.isAvailable) aBaseArticleTaxonomies[iIndex].hideIcon = true;
                if (oCommonTaxonomy.isDisabled) aBaseArticleTaxonomies[iIndex].hideAdaptIcon = true;
            })
        }
        let aArticleTaxonomies = aAllTaxonomies.map(key => {
            let bIsTaxonomyPresent = aUniqueTaxonomies.find(k => key.id == k.id);
            return (bIsTaxonomyPresent ? {id: ""} : key);
        });
        return {
            articleTaxonomies: aArticleTaxonomies,
            baseArticleTaxonomies: aBaseArticleTaxonomies
        }
    };

    const getActionIcons = (oTaxonomy, iIndex, aTaxonomies) => {
        if (!oTaxonomy.hideIcon) {
            if (oTaxonomy.hideAdaptIcon) {
                return (<div className="fixedIcon">
                    <div className="inheritedIcon"></div>
                  <TooltipView placement="top" label={getTranslation().REVERT}>
                    <div className="revertIcon" onClick={handleRevertTaxonomy.bind(this, oTaxonomy, iIndex, aTaxonomies)}></div>
                  </TooltipView>
                </div>);
            } else {
                return (
                    <TooltipView placement="top" label={getTranslation().ADAPT}>
                    <div className="adaptIcon" onClick={handleAdaptTaxonomy.bind(this, oTaxonomy, iIndex, aTaxonomies)}></div>
                    </TooltipView>
                );
            }
        }
    };

    const getChipsViewData = (aArticleTaxonomies, aBaseArticleTaxonomies, showIcons) => {
        let aTaxonomyChipsView = [];
        let oReferencedTaxonomy = oProps.referencedTaxonomies;
        CS.forEach(aArticleTaxonomies, (oTaxonomy, iIndex) => {
            let aTaxonomyLevels = oReferencedTaxonomy[oTaxonomy.id];
            let sClassName = "icons ";
            oTaxonomy.isDisabled && (sClassName += "isDisabled");
            aTaxonomyChipsView.push({
                cellView: <div className={sClassName}><ChipsView items={aTaxonomyLevels} /></div>,
                actionView: showIcons && <div className={sClassName}>
                    {getActionIcons(oTaxonomy, iIndex, aBaseArticleTaxonomies)}
                </div>
            });
        });
        return aTaxonomyChipsView;
    };

    const getButtonData = () => {
        const aButtonData = [{
            id: "cancel",
            label: getTranslation().CANCEL,
            isFlat: true,
            isDisabled: false
        }
        ];
        let _oProps = oProps;
        let oArticleTypeAndTaxonomies = _oProps.contentIdVsTypesTaxonomies[_oProps.articleId];
        let isDirty = oArticleTypeAndTaxonomies.isDirty;

        isDirty && aButtonData.push({
            id: "apply",
            label: getTranslation().APPLY,
            isFlat: false,
            isDisabled: false
        });

        return aButtonData;
    }

    const getHeaderData = () => {
        let _oProps = oProps;
        let oArticleTypeAndTaxonomies = _oProps.contentIdVsTypesTaxonomies[_oProps.articleId];
        let oBaseArticleTypeAndTaxonomies = _oProps.contentIdVsTypesTaxonomies[_oProps.parentContentId];
        let bIsDisable = false;
        let isDirty = oArticleTypeAndTaxonomies.isDirty;

        if (oArticleTypeAndTaxonomies.clonedData) {
            let aDifferenceInArticleTaxonomies = CS.differenceBy(oArticleTypeAndTaxonomies.taxonomyIds, oBaseArticleTypeAndTaxonomies.taxonomyIds, 'id');
            let aDifferenceInBaseArticleTaxonomies = CS.differenceBy(oBaseArticleTypeAndTaxonomies.taxonomyIds, oArticleTypeAndTaxonomies.taxonomyIds, 'id');
            aDifferenceInBaseArticleTaxonomies = CS.filter(aDifferenceInBaseArticleTaxonomies, {hideAdaptIcon:false });
            aDifferenceInArticleTaxonomies = CS.filter(aDifferenceInArticleTaxonomies, {isDisabled:false });
            let aDifferenceInTaxonomies = aDifferenceInArticleTaxonomies.concat(aDifferenceInBaseArticleTaxonomies);
            bIsDisable = aDifferenceInTaxonomies.length == 0 ? true : false;
        }

        let fAdaptTaxonomyHandler = bIsDisable ? CS.noop : handleAdaptAllTaxonomy;
        let sAdaptAllClassName = bIsDisable ? "adaptAllIcon disabled" : "adaptAllIcon";
        return [{
            id: "BASE_ARTICLE_TAXONOMIES",
            label: getTranslation().BASE_ARTICLE_TAXONOMIES,
            actionView: <div className="icons">
                <TooltipView placement="top" label={getTranslation().ADAPT_ALL}><div className={sAdaptAllClassName} onClick={fAdaptTaxonomyHandler}></div></TooltipView>
                {isDirty && <TooltipView placement="top" label={getTranslation().REVERT_ALL_CHANGES}><div className="revertAllIcon" onClick={handleRevertAllTaxonomy}></div></TooltipView>}
            </div>
        }, {
            id: "ARTICLE_TAXONOMIES",
            label: getTranslation().ARTICLE_TAXONOMIES
        }]
    }

    let oContentStyle = {
        overflow: 'hidden',
        width: '80%',
        maxWidth: '80%',
        height: '617px'
    };

    let headerData = getHeaderData();
    let bodyData = getBodyData();

    return (<CustomDialogView
        open={oProps.bOpenDialog}
        title={getTranslation().ADAPT_CHANGES_FROM_BASE_ARTICLE}
        className={"taxonomyInheritanceDialog"}
        onRequestClose={handleDialogButtonAction}
        buttonData={getButtonData()}
        contentStyle={oContentStyle}
        buttonClickHandler={handleDialogButtonAction}
        contentClassName="taxonomyInheritanceDialogBody"
    >
        <div className="taxonomyInheritanceHeader">{getTranslation().TAXONOMY_INHERITANCE_HEADER}</div>
        <GenericTableView headerData={headerData} bodyData={bodyData} />
    </CustomDialogView>)
}

export default TaxonomyInheritanceView;
export const events = oEvents;
