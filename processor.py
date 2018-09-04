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

def mapping(sequence):
    indexes = list(set(sequence))
    words = {k:i for i, k in enumerate(indexes)}
    return indexes, words

_lemma = None
def lemmatize(indexes):
    global _lemma
    idxLemma = []
    dctLemma = {}
    if _lemma is None:
        _lemma = nltk.stem.WordNetLemmatizer()
    for word in indexes:
        try:
            w = _lemma.lemmatize(word)
        except Exception:
            nltk.download('wordnet')
            w = _lemma.lemmatize(word)
        i = len(idxLemma)
        dctLemma.setdefault(w, []).append(i)
    return idxLemma, dctLemma

def thesaurus(word):
    word = word.lower()
    synonyms, antonyms = [word], []
    for syn in nltk.corpus.wordnet.synsets(word):
        for l in syn.lemmas():
            synonyms.append(l.name().lower())
            if l.antonyms():
                antonyms.append(l.antonyms()[0].name().lower())
    synonyms = [s for s in set(synonyms) if '_' not in s and '-' not in s]
    antonyms = [a for a in set(antonyms) if '_' not in a and '-' not in a]
    return synonyms, antonyms
