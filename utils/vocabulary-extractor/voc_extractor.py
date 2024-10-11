import argparse
import logging
import sys
from inspect import signature

from vocextractor import VocExtractor
from vocextractor.core import register


def default_init_param(param):
    """
    Get the default argument value from the constructor of the given parameter
    Args:
        param (string): argument name
    Returns:
        default value
    """
    return signature(VocExtractor.__init__).parameters[param].default


if __name__ == "__main__":
    """
    Vocabulary extractor: Utility tool to extract vocabularies for ARDIT

    Examples:
        python voc_extractor.py
    """

    parser = argparse.ArgumentParser(description='Vocabulary extractor: Utility tool to extract vocabularies for '
                                                 'ARDIT')

    output_default = default_init_param('output_path')

    parser.add_argument('-f', '--output_path', type=str, action='store', dest='output_path',
                        help=f'output folder path (default: {output_default})',
                        default=output_default)

    args = parser.parse_args()
    output_path = args.output_path
    try:
        # Output path definition
        extractor = VocExtractor(output_path=output_path)
        # Adding all the parsers tagged by @register.parser
        extractor.register(register)
        # Launching the tool
        extractor.run()
    except Exception as e:
        logging.error(e)
        sys.exit(-1)
