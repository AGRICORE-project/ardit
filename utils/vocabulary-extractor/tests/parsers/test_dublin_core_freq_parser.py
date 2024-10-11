from vocextractor.voc_extractor import VocExtractor
from vocextractor.core import VocabularyRegister
from vocextractor.parsers import DublinCoreFrequencyParser


def extractor_with_parser(cls) -> VocExtractor:
    register = VocabularyRegister()
    register.parser(cls)
    extractor = VocExtractor()
    extractor.register(register)
    return extractor


def test_dublin_core_frequency_parser():
    extractor: VocExtractor = extractor_with_parser(DublinCoreFrequencyParser)
    extractor.run()
