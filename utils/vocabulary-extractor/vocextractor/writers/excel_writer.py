import json
from collections.abc import Iterable
import xlsxwriter

from vocextractor.core import VocabularyWriter
from vocextractor.model import Vocabulary
from vocextractor.model import VocValue
from vocextractor.writers import SQLWriter



class ExcelWriter(VocabularyWriter):
    def __init__(self, output: str = 'output'):
        super().__init__(output=output)

    def write_all(self, vocabularies):
        path: str = f"{self.output}/excel/vocabularies.xlsx"
        self.create_subfolders(path)

        workbook = xlsxwriter.Workbook(path)

        # List of vocabularies IDs
        vocabulary_ids = [i + 1 for i in range(len(vocabularies))]

        # Vocabularies list worksheet
        vocabulary_worksheet = self.create_vocabulary_worksheet(workbook, vocabularies, vocabulary_ids)
        
        # Individual vocabulary worksheet
        for vocabulary, vocabulary_id in zip(vocabularies, vocabulary_ids):
            vocabulary_worksheet = self.create_values_worksheet(workbook, vocabulary, vocabulary_id)

        workbook.close()

    def write(self, vocabulary: Vocabulary, id: int = 0):
        pass

    def create_vocabulary_worksheet(self, workbook, vocabularies, ids):
        vocabularies_worksheet = workbook.add_worksheet('Vocabularies')

        caption = 'Vocabularies'

        vocabulary_rows = self.vocabulary_rows(vocabularies, ids)

        # Set the columns widths.
        vocabularies_worksheet.set_column('B:G', 10)
        vocabularies_worksheet.set_column('F:H', 100)

        # Write the caption.
        vocabularies_worksheet.write('B1', caption)

        # Add a table to the worksheet.
        row_init = 3
        row_end = len(vocabulary_rows) + row_init

        vocabularies_worksheet.add_table(f'B{row_init}:H{row_end}',
                                         {'data': vocabulary_rows,
                                          'columns': [{'header': 'Vocabulary ID'},
                                                      {'header': 'Name'},
                                                      {'header': 'Topic'},
                                                      {'header': 'Description'},
                                                      {'header': 'URL'},
                                                      {'header': 'INSERT SQL'}
                                                      ]})
        return vocabularies_worksheet

    def create_values_worksheet(self, workbook, vocabulary, vocabulary_id):
        values_worksheet = workbook.add_worksheet(vocabulary.name)

        caption = vocabulary.name

        values_rows = self.values_rows(vocabulary, vocabulary_id)

        # Set the columns widths.
        values_worksheet.set_column('B:E', 10)
        values_worksheet.set_column('E:G', 100)

        # Write the caption.
        values_worksheet.write('B1', caption)

        # Add a table to the worksheet.
        row_init = 3
        row_end = len(values_rows) + row_init

        """
                    row = [[vocabulary_id,
                     value.code,
                     value.label,
                     value.url,
                     value.extra_data if value.extra_data else 'NULL',
                     SQLWriter.value_sql(value, id)
                     ] for value in values]
        """
        values_worksheet.add_table(f'B{row_init}:G{row_end}',
                                   {'data': values_rows,
                                    'columns': [{'header': 'Vocabulary ID'},
                                                {'header': 'Code'},
                                                {'header': 'Label'},
                                                {'header': 'URL'},
                                                {'header': 'Extra data'},
                                                {'header': 'INSERT SQL'}
                                                ]})
        return values_worksheet

    def values_rows(self, vocabulary, vocabulary_id):
        res = []
        # INSERT INTO public.vocabulary_value (code,extra_data,"label",url,vocabulary_id)
        '''for vocabulary, vocabulary_id in zip(vocabularies, ids):
            values = vocabulary.values
            row = [[vocabulary_id,
                     value.code,
                     value.label,
                     value.url,
                     value.extra_data if value.extra_data else 'NULL',
                     SQLWriter.value_sql(value, vocabulary_id)
                    ] for value in values]
            res += row'''
        values = vocabulary.values
        row = [[vocabulary_id,
                     value.code,
                     value.label,
                     value.url,
                     value.extra_data if value.extra_data else 'NULL',
                     SQLWriter.value_sql(value, vocabulary_id)
                    ] for value in values]
        res += row
        return res

    def vocabulary_rows(self, vocabularies, ids):
        return [[id,
                 vocabulary.name,
                 vocabulary.topic,
                 vocabulary.description,
                 vocabulary.url,
                 SQLWriter.vocabulary_sql(vocabulary, id)
                 ]
                for vocabulary, id in zip(vocabularies, ids)]
