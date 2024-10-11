INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (12, 'Math representation values for data frequency', 'Math representation vocabulary', 'MATH_REPRESENTATION', '');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(14475, "AVG", NULL, "Average", "", 12)
,(14476, "INV", NULL, "Instant value", "", 12)
,(14477, "MAX", NULL, "Max", "", 12)
,(14478, "MED", NULL, "Median", "", 12)
,(14479, "MIN", NULL, "Min", "", 12)
,(14480, "MOD", NULL, "Mode", "", 12)
,(14481, "SUM", NULL, "Sum", "", 12)
,(14482, "VAR", NULL, "Variance", "", 12)
,(14483, "STD", NULL, "Standard deviation", "", 12);