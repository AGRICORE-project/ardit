# -*- coding: utf-8 -*-
import json
from pathlib import Path
from collections.abc import Iterable

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


@register.parser
class ContinentParser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        return Vocabulary.create(name='Geonames ADM2 vocabulary',
                                 description='ADM2 values for dataset geoname coverage',
                                 url='',
                                 topic='ADM2')

    def load(self) -> any:
        path = Path(__file__).parent / "adm2.txt"
        with open(path) as f:
            lines = f.readlines()
            return lines

    def rows(self, data: any) -> Iterable:
        return data

    def code(self, row: any) -> str:
        code = row.split('\t')[0]
        return code

    def url(self, row: any) -> str:
        url_prefix = 'http://geonames.org/'
        code = row.split('\t')[0]
        url = url_prefix + code
        return url

    def label(self, row: any) -> str:
        label = row.split('\t')[1]
        return label

    def extra_data(self, row) -> str:
        pass
