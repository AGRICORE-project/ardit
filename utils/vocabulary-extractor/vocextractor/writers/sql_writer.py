import json
from collections.abc import Iterable

from vocextractor.core import VocabularyWriter
from vocextractor.model import Vocabulary
from vocextractor.model import VocValue



class SQLWriter(VocabularyWriter):
    def __init__(self, output: str = 'output'):
        super().__init__(output=output)

    def write_all(self, vocabularies):
        pass

    def write(self, vocabulary: Vocabulary, id: int = 0, value_id: int = 0):
        path: str = f"{self.output}/sql/{vocabulary.name}.sql"
        self.create_subfolders(path)

        vocabulary_sql: str = self.vocabulary_sql(vocabulary, id)
        values_sql: str = self.values_sql(vocabulary, id, value_id)

        res_sql: str = f"{vocabulary_sql}\n\n{values_sql}"

        with open(path, "w") as write_file:
            write_file.write(res_sql)

    @staticmethod
    def vocabulary_sql(vocabulary: Vocabulary, id: int):
        insert: str = 'INSERT INTO public.vocabulary (id,description,name,topic,url)'
        register: str = (id, vocabulary.description, vocabulary.name, vocabulary.topic.upper(), vocabulary.url)

        res: str = f"{insert} VALUES {register};"
        return res

    @staticmethod
    def values_sql(vocabulary: Vocabulary, id: int, value_id: int):
        values: Iterable[VocValue] = vocabulary.values
        insert: str = 'INSERT INTO public.vocabulary_value (id,code,extra_data,"label",url,vocabulary_id)'
        values_sql: Iterable[str] = []
        for value in values:
            # NULL must be added without quotes
            extra_data = f"'value.extra_data'" if value.extra_data else 'NULL'
            register = f"({value_id}, '{value.code}', {extra_data}, '{value.label}', '{value.url}', {id})"
            values_sql.append(register)
            value_id += 1
        values_sql = '\n,'.join(values_sql)

        res: str = f"{insert} VALUES \n{values_sql};"
        return res

    @staticmethod
    def value_sql(value: VocValue, id: int):
        insert: str = 'INSERT INTO public.vocabulary_value (code,extra_data,"label",url,vocabulary_id)'

        # NULL must be added without quotes
        extra_data = f"'value.extra_data'" if value.extra_data else 'NULL'
        value_sql = f"('{value.code}', {extra_data}, '{value.label}', '{value.url}', {id})"

        res: str = f"{insert} VALUES \n{value_sql};"
        return res
