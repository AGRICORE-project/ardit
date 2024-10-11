INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (1, 'Access right vocabulary for dataset type of access', 'Access Rights Vocabulary', 'ACCESS_RIGHT', 'http://publications.europa.eu/resource/authority/access-right');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(1, "NON_PUBLIC", NULL, "Non-public", "http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC", 1)
,(2, "OP_DATPRO", NULL, "Provisional data", "http://publications.europa.eu/resource/authority/access-right/OP_DATPRO", 1)
,(3, "PUBLIC", NULL, "Public", "http://publications.europa.eu/resource/authority/access-right/PUBLIC", 1)
,(4, "RESTRICTED", NULL, "Restricted", "http://publications.europa.eu/resource/authority/access-right/RESTRICTED", 1)
,(5, "SENSITIVE", NULL, "Sensitive", "http://publications.europa.eu/resource/authority/access-right/SENSITIVE", 1);