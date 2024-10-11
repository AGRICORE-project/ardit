from vocextractor.voc_extractor import VocExtractor
from vocextractor.core import VocabularyRegister
from vocextractor.parsers import DETAuthorityParserHTML


def extractor_with_parser(cls) -> VocExtractor:
    register = VocabularyRegister()
    register.parser(cls)
    extractor = VocExtractor()
    extractor.register(register)
    return extractor


def test_iso_639_parser():
    extractor: VocExtractor = extractor_with_parser(DETAuthorityParserHTML)
    extractor.run()
