# -*- coding: utf-8 -*-
import requests
from collections.abc import Iterable

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


@register.parser
class ISO6392Parser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        return Vocabulary.create(name='ISO 639-2 Languages',
                                 description='Codes for the Representation of Names of Languages',
                                 url='https://id.loc.gov/vocabulary/iso639-2.json',
                                 topic='LANGUAGE')

    def load(self) -> any:
        response = requests.get('https://id.loc.gov/vocabulary/iso639-2.json')
        data = response.json()
        return data

    def rows(self, data: any) -> Iterable:
        return filter(lambda row: 'http://www.w3.org/2004/02/skos/core#prefLabel' in row, data)

    def code(self, row: any) -> str:
        return row['@id'].split('/')[-1]

    def url(self, row: any) -> str:
        return row['@id']

    def label(self, row: any) -> str:
        prefs = row['http://www.w3.org/2004/02/skos/core#prefLabel']
        en = filter(lambda x: x['@language'] == 'en', prefs)
        en = list(en)
        return en[0]['@value']

    def extra_data(self, row) -> str:
        pass
