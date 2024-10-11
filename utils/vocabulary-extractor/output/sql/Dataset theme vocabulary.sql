INSERT INTO public.vocabulary (id,description,name,topic,url) VALUES (7, 'Data Theme Authority tables for dataset themes', 'Dataset theme vocabulary', 'THEME', 'http://publications.europa.eu/resource/authority/data-theme');

INSERT INTO public.vocabulary_value (id,code,extra_data,label,url,vocabulary_id) VALUES 
(2041, "AGRI", NULL, "Agriculture, fisheries, forestry and food", "http://publications.europa.eu/resource/authority/data-theme/AGRI", 7)
,(2042, "ECON", NULL, "Economy and finance", "http://publications.europa.eu/resource/authority/data-theme/ECON", 7)
,(2043, "EDUC", NULL, "Education, culture and sport", "http://publications.europa.eu/resource/authority/data-theme/EDUC", 7)
,(2044, "ENER", NULL, "Energy", "http://publications.europa.eu/resource/authority/data-theme/ENER", 7)
,(2045, "ENVI", NULL, "Environment", "http://publications.europa.eu/resource/authority/data-theme/ENVI", 7)
,(2046, "GOVE", NULL, "Government and public sector", "http://publications.europa.eu/resource/authority/data-theme/GOVE", 7)
,(2047, "HEAL", NULL, "Health", "http://publications.europa.eu/resource/authority/data-theme/HEAL", 7)
,(2048, "INTR", NULL, "International issues", "http://publications.europa.eu/resource/authority/data-theme/INTR", 7)
,(2049, "JUST", NULL, "Justice, legal system and public safety", "http://publications.europa.eu/resource/authority/data-theme/JUST", 7)
,(2050, "REGI", NULL, "Regions and cities", "http://publications.europa.eu/resource/authority/data-theme/REGI", 7)
,(2051, "SOCI", NULL, "Population and society", "http://publications.europa.eu/resource/authority/data-theme/SOCI", 7)
,(2052, "TECH", NULL, "Science and technology", "http://publications.europa.eu/resource/authority/data-theme/TECH", 7)
,(2053, "TRAN", NULL, "Transport", "http://publications.europa.eu/resource/authority/data-theme/TRAN", 7);