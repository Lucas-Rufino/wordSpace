from sklearn.metrics.pairwise import cosine_similarity
from scipy import sparse as sp
from math import sqrt, ceil
import numpy as np
import warnings
import sys

if not sys.warnoptions:
    warnings.simplefilter("ignore")

_vectors = None
_values = None
_cosine = None
_k = None

def fit(X, k=None):
    global _vectors, _values, _cosine, _k
    if k is None and _k is None:
        _k = int(ceil(sqrt(X.shape[1])))
    elif _k is None:
        _k = k
    _cosine = cosine_similarity(X, dense_output=False)
    w, v = sp.linalg.eigs(_cosine, k=_k, which='LM')
    order = np.argsort(w)[::-1]
    _vectors = v[:,order].astype(np.float)
    _values = w[order].astype(np.float)

def transform(X, k=None):
    if k is None:
        k = _k
    data = X.dot(_vectors[:,:k])
    return data

def evaluate(X):
    X = sp.csr_matrix(X)
    delta = _cosine - cosine_similarity(X, dense_output=False)
    total = delta.shape[0] * delta.shape[1]
    error = abs(delta).sum() / (2 * total)
    return 1 - error
