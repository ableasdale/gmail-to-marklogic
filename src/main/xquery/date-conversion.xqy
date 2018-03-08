xquery version "1.0-ml";

(: GMAIL / system dates to XML Date notes :)

(:

   Symbol         Description
  -----------------------------------
     'Y'        year(absolute value)
     'M'        month in year
     'D'        day in month
     'd'        day in year
     'F'        day of week
     'W'        week in year
     'w'        week in month
     'H'        hour in day
     'h'        hour in half-day
     'P'        am/pm marker
     'm'        minute in hour
     's'        second in minute
     'f'        fractional seconds
     'Z'        timezone as a time offset from UTC
                for example PST
     'z'        timezone as an offset using GMT,
                for example GMT+1
"[Y0001]-[M01]-[D01]T[h01]:[m01]:[s01].[f1][Z]"
:)

xdmp:parse-dateTime("[F], [D] [MN] [Y] [H]:[m]:[s] +0000 ([Z])", "Wed, 18 Sep 2013 10:04:53 +0000 (UTC)"),
xdmp:parse-dateTime("[F], [D] [MN] [Y] [H]:[m]:[s] [z] ([Z])", "Wed, 18 Sep 2013 10:04:53 +0000 (UTC)"),
xdmp:parse-dateTime("[F], [D] [MN] [Y] [H]:[m]:[s] [z]", "Tue, 7 Oct 2008 14:59:17 -0500"),
xdmp:parse-dateTime("[D] [MN] [Y] [H]:[m]:[s] [z]", "06 Mar 2017 15:05:29 +0000")