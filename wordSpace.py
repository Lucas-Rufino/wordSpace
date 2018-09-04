from scipy import sparse as sp
import decomposer as dc
import processor as pc

def read(filepath):
    with open(filepath) as fl:
        text = fl.read()
    return text

def init(words):
    size = len(words)
    return sp.lil_matrix((size, size))

def applyMutual(matrix, sequence, words, window):
    size = len(sequence)
    for i in range(window, size - window):
        wi = words[sequence[i]]
        for j in range(i - window, i + window + 1):
            sj = words[sequence[j]]
            matrix[wi,sj] += 1
    for i in range(matrix.shape[0]):
        for j in range(matrix.shape[1]):
            if i != j and matrix[i,j] != 0:
                matrix[i,j] = (matrix[i,j]**2) / (matrix[i,i] * matrix[j,j])
    for i in range(min(matrix.shape[0], matrix.shape[1])):
        matrix[i,i] = 1
    return matrix

def applySemantic(matrix, words):
    for word in words:
        syn, ant = pc.thesaurus(word)
        syn = [words[w] for w in syn if w in words]
        ant = [words[w] for w in ant if w in words]
        for i in range(len(syn)):
            wi = syn[i]
            for j in range(i+1, len(syn)):
                wj = syn[j]
                matrix[wi, wj] = 1
                matrix[wj, wi] = 1
        for i in range(len(ant)):
            wi = ant[i]
            for j in range(i+1, len(ant)):
                wj = ant[j]
                matrix[wi, wj] = 1
                matrix[wj, wi] = 1
        for i in range(len(syn)):
            wi = syn[i]
            for j in range(len(ant)):
                wj = ant[j]
                matrix[wi, wj] = -1
                matrix[wj, wi] = -1
    return matrix

def main():
    text = read('data/raw.txt')
    text = pc.normalize(text, lower=True)
    sequence = pc.tokenize(text)
    indexes, words = pc.mapping(sequence)

    matrix = init(words)
    matrix = applyMutual(matrix, sequence, words, 2)
    matrix = applySemantic(matrix, words)

    dc.fit(matrix)
    data = dc.transform(matrix)
    acc = dc.evaluate(data)
    print("Acurácia: " + str(round(acc * 100, 2)) + "%")

if __name__ == '__main__':
    main()