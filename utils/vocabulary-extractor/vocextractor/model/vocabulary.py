from collections.abc import Iterable


class Vocabulary:

    name: str
    description: str
    url: str
    values: Iterable

    @staticmethod
    def create(name: str = '', description: str = '', url: str = '', topic: str = ''):
        return Vocabulary(name=name, description=description, url=url, topic=topic)

    def __init__(self, name: str = '', description: str = '', url: str = '', topic: str = '', values: Iterable = []):
        self.name = name
        self.description = description
        self.url = url
        self.topic = topic.upper()
        self.values = values

    def to_json(self):
        return {
            'name': self.name,
            'description': self.description,
            'url': self.url,
            'topic': self.topic.upper(),
            'values': list(map(lambda value: value.to_json(), self.values)),
        }
