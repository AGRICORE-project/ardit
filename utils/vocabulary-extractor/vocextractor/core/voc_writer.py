import os
from abc import ABC, abstractmethod


from vocextractor.model import Vocabulary


class VocabularyWriter(ABC):
    """
    Abstract class to write a vocabulary in a file
    """
    def __init__(self, output: str = 'output'):
        if output[-1] in ['/', '\\']:
            output = output[0:-1]
        self.output: str = output

    @abstractmethod
    def write(self, vocabulary: Vocabulary, id: int = 0):
        """
        Write the vocabulary in the implemented output

        Args:
            vocabulary (Vocabulary): Vocabulary to write in the output format
            id (int): ID if its required
        Returns:

        """
    @abstractmethod
    def write_all(self, vocabularies):
        """
        Write all the vocabularies in a file

        Args:
            vocabularies (list of Vocabulary): Vocabularies to write in the output format

        Returns:

        """

    def create_subfolders(self, path: str):
        """
        Create the subfolders from a path
        Args:
            path (str): Path indicating the subfolders to create if they not exist

        Returns:

        """
        if not os.path.exists(os.path.dirname(path)):
            os.makedirs(os.path.dirname(path))
