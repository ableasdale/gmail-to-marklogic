xquery version "1.0-ml";

(: GMAIL date to XML Date :)
xdmp:parse-dateTime("[F], [d] [MN] [Y] [H]:[m]:[s] +0000 ([Z])", "Wed, 18 Sep 2013 10:04:53 +0000 (UTC)")