# -*- coding: utf-8 -*-
import requests
from pathlib import Path
from multiprocessing import Pool
from collections.abc import Iterable
import xml.etree.ElementTree as ET

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


# Not used due to several calls must be done to the endpoint for each language
# @register.parser
class DETAuthorityParser(VocabularyParser):

    def __init__(self):
        super().__init__()
        self.rdf = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'
        self.ns = {
            'rdf': self.rdf
        }

    def vocabulary(self) -> Vocabulary:
        """
        Return the vocabulary information such as name, description and url
        Returns:
            Vocabulary data
        """
        return Vocabulary.create(name='DET Authority vocabulary',
                                 description='DET Authority tables for dataset subjects',
                                 url='http://publications.europa.eu/resource/authority/uxp/det',
                                 topic='SUBJECT')

    def load(self) -> any:
        """
        Download the vocabulary data from the origin and return it in any datatype

        Returns:
            Vocabulary data extracted
        """
        url = 'http://publications.europa.eu/resource/authority/uxp/det'
        global_tree = self.xml_by_url(url)
        return global_tree

    def rows(self, data: any) -> Iterable:
        """
        Extract the rows in an Iterable from the data loaded. The data could be returned as a generator or as an Iterable.
        The politic of how the data is parsed to different rows is delegated to this method. So, multiprocessing or any
        different way to extract the information must be performed here due to the VocabularyExtractor will not
        apply any mechanism to increase the extraction operation.

        Args:
            data (any): Vocabulary data downloaded

        Returns:
            Iterable of data
        """
        languages_urls = self.languages_urls_by_global_tree(data)
        with Pool(4) as p:
            languages_trees = p.map(self.xml_by_url, languages_urls)
        languages = map(lambda language_tree: self.extract_language_by_language_tree(language_tree), languages_trees)
        return languages

    def code(self, row: any) -> str:
        """
        Return the code of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the code of the value
        """
        return row['code']

    def url(self, row: any) -> str:
        """
        Return the url of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the url of the value
        """
        return row['url']


    def label(self, row) -> str:
        """
        Return the label of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the label of the value
        """
        return row['label']

    def extra_data(self, row) -> str:
        """
        Return extra information about the value

        Args:
            row (any): row of the vocabulary

        Returns:
            extra information of the value
        """
        return

    @staticmethod
    def xml_by_url(url: str):
        print('Downloading: ', url)
        response = requests.get(url)
        tree = ET.fromstring(response.content)
        return tree

    def rdf_attr(self, attr: str) -> str:
        return '{' + self.rdf + '}' + attr

    def languages_urls_by_global_tree(self, tree) -> Iterable:
        language_tags = tree.findall(f'rdf:Description', self.ns)
        language_urls = map(lambda row: row.attrib[self.rdf_attr('about')], language_tags)
        return language_urls

    def extract_language_by_language_tree(self, language_tree):
        descriptions = language_tree.getchildren()
        descriptions = filter(lambda x: x.findall('{http://www.w3.org/2004/02/skos/core#}prefLabel'), descriptions)
        language = list(descriptions)[0]
        prefLabels = language.findall('{http://www.w3.org/2004/02/skos/core#}prefLabel')
        prefLabel = filter(lambda x: x.attrib['{http://www.w3.org/XML/1998/namespace}lang'] == 'en', prefLabels)
        prefLabel = list(prefLabel)
        prefLabel = prefLabel[0]
        url = language.attrib['{http://www.w3.org/1999/02/22-rdf-syntax-ns#}about']
        code = url.split('/')[-1]
        label = prefLabel.text

        res = {
            'url': url,
            'code': code,
            'label': label,
            'extra_data': None,
        }
        return res
