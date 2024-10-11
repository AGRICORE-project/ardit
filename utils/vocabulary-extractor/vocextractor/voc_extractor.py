import logging
import json
from collections.abc import Iterable

from vocextractor.core import VocabularyRegister
from vocextractor.core import VocabularyParser
from vocextractor.core import VocabularyWriter
from vocextractor.model import Vocabulary, VocValue

from vocextractor.writers import JSONWriter
from vocextractor.writers import SQLWriter
from vocextractor.writers import ExcelWriter

logger = logging.getLogger(__name__)


class VocExtractor:
    def __init__(self, output_path: str = 'output', log_level: int = logging.WARNING):
        """
        Utility tool to extract vocabularies for ARDIT
        Args:
            output_path ('output'): Output folder
            log_level (logging.LOG_LEVEL): logging level
        """
        logger.setLevel(log_level)
        self.log_level: int = log_level
        self.output_path: str = output_path
        self.parsers_cls: Iterable = []

    def register(self, register: VocabularyRegister):
        """
        Store all the parsers tagged by the register with @register.parse
        Args:
            register:

        Returns:

        """
        self.parsers_cls: Iterable = register.parsers_cls
        return self

    def run(self):
        """
        Main instruction that launch the parsers and the store operation
        Returns:

        """
        vocabularies: Iterable[Vocabulary] = []
        for parser_cls in self.parsers_cls:
            # Parser instantiation
            parser: VocabularyParser = parser_cls()
            # Vocabulary data recovered
            vocabulary: Vocabulary = parser.vocabulary()

            print(f'Parsing vocabulary: {vocabulary.name}')

            # Data load and rows
            data: any = parser.load()
            rows: Iterable[any] = parser.rows(data)

            # Rows to VocValue
            values: Iterable[VocValue] = map(lambda row: self.to_voc_value(parser, row), rows)
            values: Iterable[VocValue] = list(values)

            # Assign values to the vocabulary object
            vocabulary.values = values

            vocabularies.append(vocabulary)
            # TODO: This operation must be performed by an abstract class to output the data from a vocabulary
            # E.g. Json output, csv output, DDL sql file output
        self.write_vocabularies(vocabularies)
        return vocabularies

    def write_vocabularies(self, vocabularies: Iterable):
        id: int = 1 # Counter for vocabulary ID
        value_id: int = 1 # Counter for vocabularies values IDs
        vocabularies.sort(key=lambda voc: voc.name)
        for vocabulary in vocabularies:
            JSONWriter(output=self.output_path).write(vocabulary=vocabulary, id=id)
            SQLWriter(output=self.output_path).write(vocabulary=vocabulary, id=id, value_id=value_id)
            id += 1
            value_id += len(vocabulary.values)
        ExcelWriter(output=self.output_path).write_all(vocabularies=vocabularies)

    def to_voc_value(self, parser: VocabularyParser, row: any) -> VocValue:
        return VocValue.create(code=parser.code(row),
                               url=parser.url(row),
                               label=parser.label(row),
                               extra_data=parser.extra_data(row))
