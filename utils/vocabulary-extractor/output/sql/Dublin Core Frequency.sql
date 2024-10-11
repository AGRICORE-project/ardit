INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (8, 'Dublin Core Frequency vocabulary', 'Dublin Core Frequency', 'PERIODICITY', 'https://www.dublincore.org/specifications/dublin-core/collection-description/frequency/');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(2054, "triennial", NULL, "Triennial", "http://purl.org/cld/freq/triennial", 8)
,(2055, "biennial", NULL, "Biennial", "http://purl.org/cld/freq/biennial", 8)
,(2056, "annual", NULL, "Annual", "http://purl.org/cld/freq/annual", 8)
,(2057, "semiannual", NULL, "Semiannual", "http://purl.org/cld/freq/semiannual", 8)
,(2058, "threeTimesAYear", NULL, "Three times a year", "http://purl.org/cld/freq/threeTimesAYear", 8)
,(2059, "quarterly", NULL, "Quarterly", "http://purl.org/cld/freq/quarterly", 8)
,(2060, "bimonthly", NULL, "Bimonthly", "http://purl.org/cld/freq/bimonthly", 8)
,(2061, "monthly", NULL, "Monthly", "http://purl.org/cld/freq/monthly", 8)
,(2062, "semimonthly", NULL, "Semimonthly", "http://purl.org/cld/freq/semimonthly", 8)
,(2063, "biweekly", NULL, "Biweekly", "http://purl.org/cld/freq/biweekly", 8)
,(2064, "threeTimesAMonth", NULL, "Three times a month", "http://purl.org/cld/freq/threeTimesAMonth", 8)
,(2065, "weekly", NULL, "Weekly", "http://purl.org/cld/freq/weekly", 8)
,(2066, "semiweekly", NULL, "Semiweekly", "http://purl.org/cld/freq/semiweekly", 8)
,(2067, "threeTimesAWeek", NULL, "Three times a week", "http://purl.org/cld/freq/threeTimesAWeek", 8)
,(2068, "daily", NULL, "Daily", "http://purl.org/cld/freq/daily", 8)
,(2069, "continuous", NULL, "Continuous", "http://purl.org/cld/freq/continuous", 8)
,(2070, "irregular", NULL, "Irregular", "http://purl.org/cld/freq/irregular", 8);