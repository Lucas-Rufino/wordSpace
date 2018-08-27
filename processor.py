import decomposer as dc
import nltk

def normalize(text, lower=False):
    if lower:
        text = text.lower()
    for symbol in '!"#$%&()*+,-./:;<=>?@[\\]^_`{|}~':
        text = text.replace(symbol, ' ' + symbol + ' ')
    return text

def tokenize(text):
    return text.split()

_lemma = None
def lemmatize(word):
    global _lemma
    if type(word) == list:
        return list(map(lemmatize, word))
    if _lemma is None:
        _lemma = nltk.stem.WordNetLemmatizer()
    try:
        return _lemma.lemmatize(word)
    except Exception:
        nltk.download('wordnet')
        return _lemma.lemmatize(word)

def thesaurus(word):
    synonyms, antonyms = set(), set()
    for syn in nltk.corpus.wordnet.synsets(word):
        for l in syn.lemmas():
            synonyms.add(l.name())
            if l.antonyms():
                antonyms.add(l.antonyms()[0].name())
    return synonyms, antonyms