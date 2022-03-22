import AttributesScreenViewProps from './attributes-screen-view-props';
import ScreenProps from './screen-props';
import ImageCoverflowViewProps from './image-coverflow-view-props';
import RelationshipViewProps from './relationship-view-props';
import ContentDetailsViewProps from './content-details-view-props';
import ContentSectionViewProps from './content-section-view-props';
import RightBarViewProps from './right-bar-view-props';
import CollectionViewProps from './viewprops/collection-view-props';
import ArticleViewProps from './viewprops/article-view-props';
import AvailableEntityViewProps from './available-entity-view-props';
import FilterProps from './filter-props';
import InnerFilterProps from './inner-filter-props';
import VariantSectionViewProps from './variant-section-view-props';
import EndPointMappingViewProps from './endpoints-mapping-view-props';
import TaskProps from './content-task-props';
import JobProps from './job-screen-view-props';
import TimelineProps from './timeline-props';
import TableViewProps from './table-view-props';
import NotificationProps from './notifications-props';
import UOMProps from './uom-props';
import BulkEditProps from './bulk-edit-props';
import ContentGridProps from './content-grid-props';
import GoldenRecordProps from './golden-record-props';
import InformationTabProps from './information-tab-view-props';
import ContextualFilterProps from './contextual-filter-props';
import BreadCrumbProps from '../../../../../../commonmodule/props/breadcrumb-props';
import ColumnOrganizerProps from '../../../../../../viewlibraries/columnorganizerview/column-organizer-props';
import BulkAssetLinkSharingProps from './bulk-asset-link-sharing-props';
import DamInformationTabProps from './dam-information-tab-view-props';
import GridEditViewProps from './grid-edit-view-props';
import MultiClassificationViewProps from './multiclassification-view-props.js';
import ContextualAllCategoriesSelectorViewProps from './contextual-all-categories-selector-view-props';
import AssetDownloadTrackerProps from './asset-download-tracker-props';
import BulkDownLoadAssetProps from './content-bulk-download-asset-props';

export default {
  attributesScreenViewProps: AttributesScreenViewProps,
  screen: ScreenProps,
  imageCoverflowViewProps: ImageCoverflowViewProps,
  relationshipView: RelationshipViewProps,
  contentDetailsView: ContentDetailsViewProps,
  contentSectionViewProps: ContentSectionViewProps,
  rightBarViewProps: RightBarViewProps,
  collectionViewProps: CollectionViewProps,
  articleViewProps: ArticleViewProps,
  availableEntityViewProps: AvailableEntityViewProps,
  filterProps: FilterProps,
  innerFilterProps: InnerFilterProps,
  variantSectionViewProps: VariantSectionViewProps,
  endPointMappingViewProps: EndPointMappingViewProps,
  taskProps: TaskProps,
  jobProps: JobProps,
  timelineProps: TimelineProps,
  goldenRecordProps: GoldenRecordProps,
  tableViewProps: TableViewProps,
  notificationProps: NotificationProps,
  uomProps: UOMProps,
  bulkEditProps: BulkEditProps,
  contentGridProps: ContentGridProps,
  gridEditViewProps: GridEditViewProps,
  informationTabProps: InformationTabProps,
  damInformationTabProps: DamInformationTabProps,
  breadCrumbProps: BreadCrumbProps,
  ContextualFilterProps: ContextualFilterProps,
  bulkAssetLinkSharingProps: BulkAssetLinkSharingProps,
  multiClassificationViewProps: MultiClassificationViewProps,
  assetDownloadTrackerProps: AssetDownloadTrackerProps,
  ColumnOrganizerProps: ColumnOrganizerProps,
  bulkDownloadAssetProps: BulkDownLoadAssetProps,
  contextualAllCategoriesSelectorViewProps: ContextualAllCategoriesSelectorViewProps,


  reset: function () {
    AttributesScreenViewProps.reset();
    ScreenProps.reset();
    ImageCoverflowViewProps.reset();
    RelationshipViewProps.reset();
    ContentDetailsViewProps.reset();
    ContentSectionViewProps.reset();
    RightBarViewProps.reset();
    Object.resetDataProperties(CollectionViewProps);
    Object.resetDataProperties(ArticleViewProps);
    Object.resetDataProperties(FilterProps);
    Object.resetDataProperties(InnerFilterProps);
    AvailableEntityViewProps.reset();
    VariantSectionViewProps.reset();
    EndPointMappingViewProps.reset();
    TaskProps.reset();
    JobProps.reset();
    TimelineProps.reset();
    TableViewProps.reset();
    NotificationProps.reset();
    UOMProps.reset();
    BulkEditProps.reset();
    ContentGridProps.reset();
    GoldenRecordProps.reset();
    InformationTabProps.reset();
    ContextualFilterProps.reset();
    BulkAssetLinkSharingProps.reset();
    DamInformationTabProps.reset();
    GridEditViewProps.reset();
    MultiClassificationViewProps.reset();
    AssetDownloadTrackerProps.reset();
    ColumnOrganizerProps.reset();
    BulkDownLoadAssetProps.reset();
  }
};
