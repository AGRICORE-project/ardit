INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (2, 'Continents values for dataset continental coverage', 'Continents vocabulary', 'CONTINENT', 'http://publications.europa.eu/resource/authority/continent');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(6, "AFRICA", NULL, "Africa", "http://publications.europa.eu/resource/authority/continent/AFRICA", 2)
,(7, "AMERICA", NULL, "America", "http://publications.europa.eu/resource/authority/continent/AMERICA", 2)
,(8, "ANTARCTICA", NULL, "Antarctica", "http://publications.europa.eu/resource/authority/continent/ANTARCTICA", 2)
,(9, "ASIA", NULL, "Asia", "http://publications.europa.eu/resource/authority/continent/ASIA", 2)
,(10, "EUROPE", NULL, "Europe", "http://publications.europa.eu/resource/authority/continent/EUROPE", 2)
,(11, "OCEANIA", NULL, "Oceania", "http://publications.europa.eu/resource/authority/continent/OCEANIA", 2);