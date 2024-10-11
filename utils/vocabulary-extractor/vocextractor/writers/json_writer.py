import json

from vocextractor.core import VocabularyWriter
from vocextractor.model import Vocabulary


class JSONWriter(VocabularyWriter):
    def __init__(self, output: str = 'output'):
        super().__init__(output=output)

    def write_all(self, vocabularies):
        pass

    def write(self, vocabulary: Vocabulary, id: int = 0):
        path: str = f"{self.output}/json/{vocabulary.name}.json"
        self.create_subfolders(path)
        with open(path, "w") as write_file:
            json.dump(vocabulary.to_json(), write_file, indent=4)
