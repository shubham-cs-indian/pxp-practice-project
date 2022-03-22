/**
 * Author:  vallee
 * Created: Apr 25, 2019
 */
/* Paritions management 
   WITH_PARTITIONS triggers or dismisses the partitioning system
   _NB_PARTITIONS defines case by case the number of partitions to set (must be locally declared)
*/
/**
#define WITH_PARTITIONS  /* just comment/uncomment this line to skip/activate partitioning */
#define _NB_PARTITIONS 4 /* default number of partitions to start on */

#define FOREIGN_KEY_WITHOUT_PARTITION   ( !defined(PGSQL11) || !defined(WITH_PARTITIONS) )
#define FOREIGN_KEY_WITH_PARTITION      ( defined(PGSQL11) && defined(WITH_PARTITIONS) )

#if FOREIGN_KEY_WITH_PARTITION
    #define _TRIGGER_ACTION  after insert /* partitioned table cannot have a before insert trigger */
#else
    #define _TRIGGER_ACTION  before insert
#endif
