/**
 * Author:  vallee
 * Created: Sep 4, 2019
 */
-- best way to query tag array
select * from (
    with codeselection as 
        (select * from pxp.tagsrecord where tagcodes @> '{HDMI}')
    select baseentityiid, propertyiid, 
        unnest(tagcodes) as code, unnest( tagranges) as range 
        from codeselection
) as a where a.code = 'HDMI' and a.range = 100;
