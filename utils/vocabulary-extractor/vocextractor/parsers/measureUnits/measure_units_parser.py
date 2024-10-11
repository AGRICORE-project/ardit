# -*- coding: utf-8 -*-
from pathlib import Path
from collections.abc import Iterable
from bs4 import BeautifulSoup

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


# Not used due to several calls must be done to the endpoint for each language
@register.parser
class MeasureUnitsParser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        """
        Return the vocabulary information such as name, description and url
        Returns:
            Vocabulary data
        """
        return Vocabulary.create(name='Measurement unit vocabulary',
                                 description='Measurement units for dataset size unit values',
                                 url='http://publications.europa.eu/resource/dataset/measurement-unit',
                                 topic='MEASURE')

    def load(self) -> any:
        """
        Download the vocabulary data from the origin and return it in any datatype

        Returns:
            Vocabulary data extracted
        """
        path = Path(__file__).parent / "vocabulary.xml"
        return BeautifulSoup(open(path, "rb"), "html.parser")

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
        return data.findChildren('tr')

    def code(self, row: any) -> str:
        """
        Return the code of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the code of the value
        """
        code_a = row.findChild('a')
        code = code_a.getText()
        return code

    def url(self, row: any) -> str:
        """
        Return the url of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the url of the value
        """
        uid_a = row.findChild('a')
        url_prefix = 'http://publications.europa.eu/'
        url = url_prefix + uid_a['href'].split('/-/')[1]
        url = url[:-1]
        return url

    def label(self, row) -> str:
        """
        Return the label of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the label of the value
        """
        labels_list = row.findAll('td', {"class": "yui3-datatable-col-label"})
        assert len(labels_list) == 1
        label = labels_list[0].getText().strip()
        return label

    def extra_data(self, row) -> str:
        """
        Return extra information about the value

        Args:
            row (any): row of the vocabulary

        Returns:
            extra information of the value
        """
        return
