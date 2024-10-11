class VocValue:

    code: str
    url: str
    label: str
    extra_data: str

    @staticmethod
    def create(code: str = '', url: str = '', label: str = '', extra_data: str = ''):
        return VocValue(code=code, url=url, label=label, extra_data=extra_data)

    def __init__(self, code: str = '', url: str = '', label: str = '', extra_data: str = ''):
        self.code = code
        self.url = url
        self.label = label
        self.extra_data = extra_data

    def to_json(self):
        return {
            'code': self.code,
            'url': self.url,
            'label': self.label,
            'extraData': self.extra_data,
        }
