import re
from setuptools import setup

with open("vocextractor/__init__.py", encoding="utf8") as f:
    version = re.search(r'__version__ = "(.*?)"', f.read()).group(1)

setup(
    name='vocestractor',
    packages=['vocestractor'],
    install_requires=[
    ],
    description='Utility tool to extract vocabularies for ARDIT',
    version=version,
    url='https://gitlab.com/agricore-project/agricore-indexer',
    author='AGRICORE team',
    keywords=['ardit', 'vocabulary', 'extractor', 'rdf'],
    include_package_data=True,
    )
