INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (1, 'Vocabulary to describe the genre or nature of a resource', 'DCMI resource type vocabulary', 'RESOURCE_TYPE', 'https://dublincore.org/specifications/dublin-core/dcmi-terms/#section-7');

INSERT INTO public.vocabulary_value (code,extra_data,label,url,vocabulary_id) VALUES 
("COL", NULL, "Collection", "http://purl.org/dc/dcmitype/Collection", 1)
,("DAT", NULL, "Dataset", "http://purl.org/dc/dcmitype/Dataset", 1)
,("EVE", NULL, "Event", "http://purl.org/dc/dcmitype/Event", 1)
,("IMG", NULL, "Image", "http://purl.org/dc/dcmitype/Image", 1)
,("INR", NULL, "Interactive resource", "http://purl.org/dc/dcmitype/InteractiveResource", 1)
,("MIM", NULL, "Moving image", "http://purl.org/dc/dcmitype/MovingImage", 1)
,("SRV", NULL, "Service", "http://purl.org/dc/dcmitype/Service", 1)
,("SOF", NULL, "Software", "http://purl.org/dc/dcmitype/Software", 1)
,("SOU", NULL, "Sound", "http://purl.org/dc/dcmitype/Sound", 1)
,("STI", NULL, "Still image", "http://purl.org/dc/dcmitype/StillImage", 1)
,("TEX", NULL, "Text", "http://purl.org/dc/dcmitype/Text", 1);