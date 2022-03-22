/**
 * Author:  vallee
 * Created: Jul 6, 2019
 */
/* test simple cases to use graph table */
-- Initial test - graphCase = -1:
delete from pxp.graph;
insert into pxp.graph values
    ( '100:az', array['10:az'], -1, 1); -- initial test case

call pxp.sp_insertGraph( '10:az', '1:az', -1);
select * from pxp.graph; -- path of 100 < 10 extended with 1 as root

call pxp.sp_insertGraph( '100:az', '11:aa', -1);
select * from pxp.graph; -- just a new path for 100 is added

call pxp.sp_insertGraph( '11:aa', '1:az', -1);
select * from pxp.graph; -- path of 100 < 11 extended with 1 as root

call pxp.sp_insertGraph( '1000:36', '100:az', -1);
select * from pxp.graph; -- 2 new paths added for 1000 root 1,10 and root 1,11

call pxp.sp_insertGraph( '10:az', '2:36', -1);
select * from pxp.graph; -- paths of 10 and 100 extended with root 2


                            