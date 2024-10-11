from collections.abc import Iterable
from vocextractor.core import VocabularyParser


class VocabularyRegister:
    """
    Class used to register the parsers to be executed
    """
    def __init__(self):
        self.parsers_cls: Iterable[VocabularyParser] = []

    def parser(self, cls):
        """
        Decorator that register the parser class

        Args:
            cls (class of VocabularyParser): parser class to be registered

        Returns:
            cls
        """
        self.parsers_cls.append(cls)
        return cls
