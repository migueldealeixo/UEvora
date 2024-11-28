import pandas as pd
import sys
import os
import numpy as np
from sklearn.model_selection import train_test_split

# Verifica se o número correto de argumentos foi fornecido
if len(sys.argv) != 2:
    sys.exit("Use: python3 <Trabalho.py> <caminhoDoFicheiroCSV>")

# Obtém o caminho do ficheiro CSV a partir do argumento da linha de comando
caminhoDoFicheiro = sys.argv[1]

# Verifica se o ficheiro existe
if not os.path.isfile(caminhoDoFicheiro):
    sys.exit(f"O ficheiro {caminhoDoFicheiro} não existe.")

# Carrega os dados do ficheiro CSV
data = pd.read_csv(caminhoDoFicheiro)

# Separar atributos da classe
X = data.iloc[:, :-1].values
y = data.iloc[:, -1].values

# Criar conjuntos de treino e teste
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=3)

class KNeighborsClassUE:
    def __init__(self, k, p):
        self.k = k
        self.p = p
        
    def fit(self, X, Y):
        self.X_train = X
        self.y_train = Y

    def predict(self, X):
        etiquetasPrevistas = []

        for x in X:
            distancia = []

            for y in self.X_train:
                soma = 0
                
                for i in range(len(x)):
                  
                    soma += abs(x[i] - y[i]) ** self.p
                    
                distanciaComPotencia = soma ** (1 / self.p)
                distancia.append(distanciaComPotencia)
                
            indicesOrdenados = np.argsort(distancia)

            classes = []
            contagemClasses = []

            for i in range(self.k):
                indiceAtual = indicesOrdenados[i]
                classeAtual = self.y_train[indiceAtual]

                if classeAtual not in classes:
                    classes.append(classeAtual)
                    contagemClasses.append(1)
                else:
                    indiceClasse = classes.index(classeAtual)
                    contagemClasses[indiceClasse] += 1

            indiceClassePredominante = np.argmax(contagemClasses)
            etiquetasPrevistas.append(classes[indiceClassePredominante])
            
        return etiquetasPrevistas

    def score(self, X, y):
        previsoesCorretas = 0

        X = np.atleast_2d(X)
        y = np.atleast_2d(y).reshape(-1, 1)

        previsao = np.array(self.predict(X)).reshape(-1, 1)

        for i in range(len(y)):
            if previsao[i] == y[i]:
                previsoesCorretas += 1

        return float(previsoesCorretas / len(X))


class NBayesUE:
    def __init__(self, suave=1e-9):
        self.suave = suave
        self.classes = None
        self.media = None
        self.var = None
        self.classes_priors = None

    def fit(self, X, y, peso_amostra=None):
        n_amostras, n_features = X.shape
        self.classes = np.unique(y)
        n_classes = len(self.classes)

        self.media = np.zeros((n_classes, n_features))
        self.var = np.zeros((n_classes, n_features))
        self.classes_priors = np.zeros(n_classes)

        for idx, classe in enumerate(self.classes):
            X_classe = X[y == classe]
            self.media[idx, :] = np.mean(X_classe, axis=0)
            self.var[idx, :] = np.var(X_classe, axis=0) + self.suave
            self.classes_priors[idx] = X_classe.shape[0] / n_amostras

        return self

    def predict(self, X):
        previsoes = []
        for x in X:
            probabilidade_classe = []
            for idx, classe in enumerate(self.classes):
                probabilidade = np.log(self.classes_priors[idx])
                probabilidade += np.sum(-0.5 * np.log(2 * np.pi * self.var[idx])
                                        - 0.5 * ((x - self.media[idx]) ** 2) / self.var[idx])
                probabilidade_classe.append(probabilidade)
            previsoes.append(self.classes[np.argmax(probabilidade_classe)])
        return previsoes

    def score(self, X, y):
        previsoes = self.predict(X)
        return np.mean(previsoes == y)

print("Escolha o seu classificador: ")
print("1. KNN")
print("2.NBayes")

escolha = input("Digite o numero do classificador escolhido: ")

if escolha == "1":
    vizinhos = 3
    potencia = 2.0

    vizinhosLinha = input("Digite o valor de número de vizinhos mais próximos a considerar: ")
    
    if len(vizinhosLinha) != 0:
        vizinhos = int(vizinhosLinha)
        
    potenciaLinha = input("Digite 1 para distância de Manhattan ou 2 para distância euclidiana: ")
    
    if len(potenciaLinha) != 0:
        potencia = float(potenciaLinha)
    
    classificador = KNeighborsClassUE(vizinhos, potencia)

    classificador.fit(X_train, y_train)
    previsoes = classificador.predict(X_test)
    exatidao = classificador.score(X_test, y_test)
    print(f"K = {vizinhos}   p = {potencia} => Exatidão = {exatidao}")

elif escolha == "2":
    suaveLinha = input("Digite o valor de suavização: ")
    
    if len(suaveLinha) != 0:
        suaveLinha = float(suaveLinha)
    else:
        suaveLinha = 1e-9
    
    classificador = NBayesUE(suave=suaveLinha)
    classificador.fit(X_train, y_train)
    exatidao = classificador.score(X_test, y_test)
    print(f"Suave = {suaveLinha} => Exatidão = {exatidao}")
