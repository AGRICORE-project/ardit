# -*- coding: utf-8 -*-
from pathlib import Path
from collections.abc import Iterable

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


# Not used due to several calls must be done to the endpoint for each language
@register.parser
class EionetMeasureUnitsParser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        """
        Return the vocabulary information such as name, description and url
        Returns:
            Vocabulary data
        """
        return Vocabulary.create(name='Eionet size unit vocabulary',
                                 description='Measurement units for dataset variable size unit values',
                                 url='http://dd.eionet.europa.eu/vocabulary/eurostat/unit/',
                                 topic='SIZE_UNIT')

    def load(self) -> any:
        """
        Download the vocabulary data from the origin and return it in any datatype

        Returns:
            Vocabulary data extracted
        """
        path = Path(__file__).parent / "vocabulary.txt"
        with open(path) as f:
            lines = f.readlines()

            valid_lines = []
            for row in lines:
                if row.split(',')[4] == '"valid"':
                    valid_lines.append(row)
            return valid_lines

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
        return data

    def code(self, row: any) -> str:
        """
        Return the code of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the code of the value
        """
        code = row.split(',')[3]
        code = code.replace('"', '')
        return code

    def url(self, row: any) -> str:
        """
        Return the url of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the url of the value
        """
        url = row.split(',')[0]
        url = url.replace('"', '')
        return url

    def label(self, row) -> str:
        """
        Return the label of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the label of the value
        """
        label = row.split(',')[1]
        label = label.replace('"', '')
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
