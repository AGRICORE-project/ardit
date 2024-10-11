# Vocabulary extractor: Utility tool to extract vocabularies for ARDIT
---

## How to run
```
> pip install -r requirements.txt
> python voc_extractor.py --help
```

Example:
```
> python voc_extractor.py
```

## Install vocextract package
Run the following command from the `vocabulary-extractor` directory:

`> pip install -e .`

## Extracting vocabularies

```python
from vocextractor import VocExtractor
from vocextractor.core import register

extractor = VocExtractor()
extractor.register(register)
extractor.run()
```

## Creating a new extractor

To create a new extractor, a class that inherit from VocabularyRegister must be created and decorated with the 'register'
An example is provided bellow:
```python

import requests
from collections.abc import Iterable

from vocextractor.model import Vocabulary
from vocextractor.core import VocabularyParser
from vocextractor import VocExtractor, register

@register.parser
class ExampleParser(VocabularyParser):

    def __init__(self):
        super().__init__()

    def vocabulary(self) -> Vocabulary:
        """
        Return the vocabulary information such as name, description and url
        Returns:
            Vocabulary data
        """
        return Vocabulary.create(name='Example vocabulary',
                                 description='My example vocabulary',
                                 url='https://jsonplaceholder.typicode.com/todos')

    def load(self) -> any:
        """
        Download the vocabulary data from the origin and return it in any datatype

        Returns:
            Vocabulary data extracted
        """
        return requests.get('https://jsonplaceholder.typicode.com/todos').json()

    def rows(self, data: any) -> Iterable:
        """
        Extract the rows in an Iterable from the data loaded. The data could be returned as a generator or as an Iterable.
        The politic of how the data is parsed to different rows is delegated to this method. So, multiprocessing or any
        different way to extract the information must be performed here due to the VocabularyExtractor will not
        apply any mechanism to increase the extraction operation.

        Args:
            data (any): Vocabulary data downloaded

        Returns:
            Iterable of data
        """
        return data

    def code(self, row: any) -> str:
        """
        Return the code of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the code of the value
        """
        return row['id']

    def url(self, row: any) -> str:
        """
        Return the url of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the url of the value
        """
        return f'https://jsonplaceholder.typicode.com/todos/{row["id"]}'

    def label(self, row) -> str:
        """
        Return the label of the value by the row

        Args:
            row (any): row of the vocabulary

        Returns:
            the label of the value
        """
        return row['title']

    def extra_data(self, row) -> str:
        """
        Return extra information about the value

        Args:
            row (any): row of the vocabulary

        Returns:
            extra information of the value
        """
        return {'completed': row['completed']}

extractor = VocExtractor()
extractor.register(register)
extractor.run()
```

## Launch specific extractors

```python
from vocextractor.core import VocabularyRegister
from vocextractor.core import VocabularyParser
from vocextractor import VocExtractor, register

custom_register = VocabularyRegister()

@custom_register.parser
class ExampleParser(VocabularyParser):
    # Implementation ...
    pass

extractor = VocExtractor()
extractor.register(custom_register)
extractor.run()
```