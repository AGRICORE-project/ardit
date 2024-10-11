# -*- coding: utf-8 -*-
import json
from pathlib import Path
from collections.abc import Iterable

from vocextractor.core import VocabularyParser, register
from vocextractor.model import Vocabulary


@register.parser
class ValuesNatureParser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        return Vocabulary.create(name='Value nature vocabulary',
                                 description='Value nature for dataset analysis data',
                                 url='',
                                 topic='VALUE_NATURE')

    def load(self) -> any:
        path = Path(__file__).parent / "vocabulary.json"
        with open(path) as f:
            return json.load(f)

    def rows(self, data: any) -> Iterable:
        return data['values']

    def code(self, row: any) -> str:
        return row['code']

    def url(self, row: any) -> str:
        return ""

    def label(self, row: any) -> str:
        return row['label']

    def extra_data(self, row) -> str:
        pass
