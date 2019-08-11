SELECT count(*)                as flcnt,
       max(
       CASE WHEN label like '%Botnet%'
         THEN 1
         ELSE 0 END) as infectedFrame,
       count(distinct proto)   as dprot,
       count(distinct sPort)   as dsport,
       count(distinct dstAddr) as ddadr,
       count(distinct dPort)   as ddport,
       sum(totPkts)            as totp,
       sum(totBytes)           as totbytes,
       srcAddr,
       label
FROM data
group by label, srcAddr