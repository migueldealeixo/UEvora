#include "boggle.h"
int cont = 0; // contagem das palavras encontradas no boggle

int valido(int x, int y, int utilizado[4][4]) {
    if ((y >= 0 && y < 4) && (x >= 0 && x < 4) && !utilizado[x][y]) {
        return 1; 
    }
    return 0;
}

void ProcurarBoggle(HashTable words, HashTable prefix, char jogo[4][4], int utilizado[4][4], int x, int y, char palavra[17], int coordenada[2][17], List resultado) {
    utilizado[x][y] = 1; // Marca a posição (x, y) como utilizada
    int tamanho = strlen(palavra);
    palavra[tamanho] = jogo[x][y]; // Adiciona a letra da posição (x, y) à palavra
    coordenada[0][tamanho] = x; // Armazena a coordenada x da letra na palavra
    coordenada[1][tamanho] = y; // Armazena a coordenada y da letra na palavra

    if (Search(palavra, prefix)) { // Verifica se a palavra é um prefixo válido
        for (int l = -1; l < 2; l++) {
            for (int c = -1; c < 2; c++) {
                if (((l != 0) || (c != 0)) && valido(x + l, y + c, utilizado)) {
                    ProcurarBoggle(words, prefix, jogo, utilizado, x + l, y + c, palavra, coordenada, resultado); // Chamada recursiva para as posições adjacentes válidas
                }
            }
        }
    }

    if (Search(palavra, words)) { // Verifica se a palavra é uma palavra válida
        cont++; // Incrementa a contagem de palavras encontradas
        Position p = resultado;
        while (!IsLast(p)) {
            p = Advance(p); // Avança para a última posição da lista
        }
        InsertList(palavra, coordenada, resultado, p); // Insere a palavra na lista de resultados
    }
    palavra[tamanho] = '\0'; // Reinicia a palavra para o estado anterior
    utilizado[x][y] = 0; // Marca a posição (x, y) como não utilizada
}

void LerBoggle(char jogo[4][4], char *file) {
    char c;
    FILE *f = fopen(file, "r");
    if (f == NULL) {
        FatalError("ERRO AO ABRIR FICHEIRO :(");
    }
    for (int i = 0; i < 4; i++) {
        // Percorre o array e lê o boggle do arquivo
        for (int j = 0; j < 4; j++) {
            c = fgetc(f);
            jogo[i][j] = c;
            if (fgetc(f) == EOF) {
                break;
            }
        }
    }
    fclose(f);
}

void MostrarTAB(char jogo[4][4]) {
    for (int i = 0; i < 4; i++) {
        // Percorre o array e mostra o tabuleiro do boggle
        printf("\t");
        for (int j = 0; j < 4; j++) {
            printf("%c ", jogo[i][j]);
        }
        printf("\n");
    }
}

void CorrerBoggle(int utilizado[4][4], HashTable words, HashTable prefix, char jogo[4][4], char palavra[17], int coordenada[2][17], List resultado) {
    int x = 0, count = 0;
    while (x < 4) {
        int y = 0;
        while (y < 4) {
            ProcurarBoggle(words, prefix, jogo, utilizado, x, y, palavra, coordenada, resultado); // Chama a função para procurar palavras no boggle
            memset(utilizado, 0, 16); // Limpa a matriz 'utilizado'
            memset(coordenada, 0, 34); // Limpa a matriz 'coordenada'
            y++;
        }
        x++;
    }
}

int main(int argc, char const *argv[]) {
    int utilizado[4][4]; //matriz do tabuleiro
    int coordenada[2][17];
    int escolha = 0;
    char palavra[17] = "";
    char jogo[4][4];
    memset(utilizado, 0, sizeof(utilizado)); // Inici a matriz 'utilizado' com zeros
    List resultado = CreateList(); // Cria uma nova lista vazia chamada 'resultado'
    HashTable WordsTable = InitializeTable(SIZE_DIC), PrefixTable = InitializeTable(SIZE_DIC);
    WordsTable = loadDic("corncob_caps_2023.txt", WordsTable); // Carrega as palavras do arquivo 'corncob_caps_2023.txt' na tabela de hash 'WordsTable'
    PrefixTable = loadPrefix("corncob_caps_2023.txt", PrefixTable); // Carrega as palavras do arquivo 'corncob_caps_2023.txt' na tabela de hash 'PrefixTable'
    printf("boogle0 = 0\nboogle1 = 1\nboogle2 = 2\nEscolha o boggle que quer usar:");
    scanf("%d", &escolha); // Lê a escolha do utilizador para selecionar o boggle

    if (escolha == 0) {
        LerBoggle(jogo, "boggle0.txt"); 
    } else if (escolha == 1) {
        LerBoggle(jogo, "boggle1.txt"); 
    } else if (escolha == 2) {
        LerBoggle(jogo, "boggle2.txt"); 
    } else {
        printf("Como não escolheu um número válido, iremos utilizar o boggle0");
        LerBoggle(jogo, "boggle0.txt");
    }
    
    MostrarTAB(jogo); // Imprime o tabuleiro no terminal
    CorrerBoggle(utilizado, WordsTable, PrefixTable, jogo, palavra, coordenada, resultado); // Executa o jogo do boggle
    PrintList(resultado); // Imprime a lista de palavras encontradas
    printf("Todas as palavras encontradas no boggle: %d\n", cont); // Imprime o número de palavras encontradas

    return 0;
}
