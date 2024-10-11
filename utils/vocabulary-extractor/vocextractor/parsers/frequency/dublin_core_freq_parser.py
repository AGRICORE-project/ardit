# -*- coding: utf-8 -*-
from pathlib import Path
from collections.abc import Iterable
import xml.etree.ElementTree as ET

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


@register.parser
class DublinCoreFrequencyParser(VocabularyParser):

    def __init__(self):
        super().__init__()
        self.ns = {
            'skos': 'http://www.w3.org/2004/02/skos/core#'
        }

    def vocabulary(self) -> Vocabulary:
        """
        Return the vocabulary information such as name, description and url
        Returns:
            Vocabulary data
        """
        return Vocabulary.create(name='Dublin Core Frequency',
                                 description='Dublin Core Frequency vocabulary',
                                 url='https://www.dublincore.org/specifications/dublin-core/collection-description/frequency/',
                                 topic='PERIODICITY')

    def load(self) -> any:
        """
        Download the vocabulary data from the origin and return it in any datatype

        Returns:
            Vocabulary data extracted
        """
        path = Path(__file__).parent / "dublin_core_freq.rdf"
        s = open(path, "rb").read()
        return ET.fromstring(s)

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
        # Get frequencies
        frequencies_tags = data.findall(f'skos:Concept', self.ns)
        # Frequency tag to dictionary
        rows = list(map(lambda frequency_tag: self.map_frequency(frequency_tag), frequencies_tags))
        return rows

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

    def map_frequency(self, frequency_tag):
        url = frequency_tag.attrib['{http://www.w3.org/1999/02/22-rdf-syntax-ns#}about']
        code = url.split('/')[-1]
        label = frequency_tag.findall(f'skos:prefLabel', self.ns)[0].text

        res = {
                'url': url,
                'code': code,
                'label': label,
                'extra_data': None,
            }
        return res
