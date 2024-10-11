INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (6, 'Values that make the datset useful for', 'Dataset purpose vocabulary', 'PURPOSE', '');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(2034, "envp", NULL, "Environmental policy", "", 6)
,(2035, "gge", NULL, "Greenhouse gas emissions", "", 6)
,(2036, "euia", NULL, "Energy use in agriculture", "", 6)
,(2037, "clch", NULL, "Climate change", "", 6)
,(2038, "iadp", NULL, "Income and distributional policies", "", 6)
,(2039, "ipol", NULL, "Insurance policy", "", 6)
,(2040, "teffa", NULL, "Technical efficiency analysis", "", 6);