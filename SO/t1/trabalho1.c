#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "input.c"

#define QUANTUM 3
#define MAX_PROCESSOS 10
#define MAX_PROGRAM_SIZE 10

typedef struct {
    int ciclos[MAX_PROGRAM_SIZE];
    int PC;
} Programa;

enum ESTADO { READY, EXECUTING, INTERRUPTIBLE, ZOMBIE, STOP };

typedef struct {
    Programa program;
    enum ESTADO estado;
    int CpuTime;
    int InterruptibleTime;
    int ZTime;
    int I;
} Processo;

typedef struct {
    Processo* data[MAX_PROCESSOS];
    int inicio;
    int size;
    int final;
} Queue;

Queue* createQueue() {
    Queue* q = malloc(sizeof(Queue));
    q->inicio = 0;
    q->size = 0;
    q->final = 0;
    return q;
}

void enqueue(Queue* queue, Processo* process) {
    if (queue->size < MAX_PROCESSOS) {
        queue->data[(queue->final) % MAX_PROCESSOS] = process;
        queue->final = (queue->final + 1) % MAX_PROCESSOS;
        queue->size++;
    }
}

Processo* dequeue(Queue* queue) {
    if (queue->size > 0) {
        Processo* process = queue->data[queue->inicio];
        queue->inicio = (queue->inicio + 1) % MAX_PROCESSOS;
        queue->size--;
        return process;
    }
    return NULL;
}

void print_header(int total_processes) {
    printf("time inst |");
    for (int i = 0; i < total_processes; i++) {
        printf(" th%d            |", i + 1);
    }
    printf("\n");
}

void print_state(Processo* processes[], int total_processes, int ciclo) {
    
}

void read_inputs(Processo **Processes) {
    // Assumindo que o array input01 está no formato correto e é acessível
        for (int i = 0; i < 4; i++) { // Número de processos
        Processo *novo_processo = malloc(sizeof(Processo));
        for (int j = 0; j < 11; j++) {
            novo_processo->program.ciclos[j] = input00[i][j];
        }
        novo_processo->program.PC = 2;
        novo_processo->estado = STOP;
        novo_processo->CpuTime = 0;
        novo_processo->InterruptibleTime = 0;
        novo_processo->I = 3;
        Processes[i]=novo_processo;
    }
}

void simulate(Queue* readyQueue, Processo **Processes, int Nprocesses) {
    int ciclo = 1;
    Processo* current_process = NULL;
    Processo* EXcurrent_process = NULL;
    Processo* Interruptible_process = NULL;
    int quantum_restante = QUANTUM;
    int waitTime=0;

    print_header(readyQueue->size);

    while (ciclo < 100) {
        if(ciclo==1){
            for (int i=0;i<Nprocesses;i++){
                if(ciclo==Processes[i]->program.ciclos[1]){
                    Processes[i]->estado = READY; 
                    enqueue(readyQueue, Processes[i]);
                }
            }
        }

        if (current_process == NULL) {
            current_process = dequeue(readyQueue);
            if(EXcurrent_process!=NULL && current_process!=NULL){
                if(EXcurrent_process->program.ciclos[0]==current_process->program.ciclos[0]){
                    if(EXcurrent_process==current_process){
                        waitTime=0;
                        quantum_restante=1;
                    }else{
                        waitTime=1;
                    }
                    
                }else{
                    waitTime=3;
                } 
            }
            if (current_process != NULL && waitTime==0) {
                current_process->estado = EXECUTING;
                if((current_process->program.PC==2 || current_process->program.PC<11)&&current_process->CpuTime==0){
                    current_process->CpuTime = current_process->program.ciclos[current_process->program.PC];
                    current_process->program.PC++;
                }
                current_process->CpuTime--;
                quantum_restante--;
                
            }
        } else if(current_process != NULL && waitTime==0){
            if(current_process->estado==READY){
                current_process->estado = EXECUTING;
                if((current_process->program.PC==2 || current_process->program.PC<11)&&current_process->CpuTime==0){
                    current_process->CpuTime = current_process->program.ciclos[current_process->program.PC];
                    current_process->program.PC++;
                }
            }
            current_process->CpuTime--;
            quantum_restante--;
        }
        

        // Imprimir o estado atual de todos os processos
        const char* estado_str[] = { "READY", "EXECUTING", "INTERRUPTIBLE", "ZOMBIE", "   " };
        printf("%-10d|", ciclo);  // A contagem do ciclo começa em 1
        for (int i = 0; i < Nprocesses; i++) {
            if (Processes[i] != NULL) {
                printf(" %-13s|", estado_str[Processes[i]->estado]);
                if(Processes[i]->estado==ZOMBIE){
                    Processes[i]->ZTime++;
                    if(Processes[i]->ZTime==2){
                        Processes[i]->estado=STOP;
                    }
                }
            } else {
                printf(" %-13s|", "NONE");
            }
        }
        printf("\n");

        
        if(Interruptible_process!=NULL){
            Interruptible_process->InterruptibleTime--;
            if(Interruptible_process->InterruptibleTime==0){
                if(Interruptible_process->program.PC+1<11){
                   if(Interruptible_process->program.ciclos[Interruptible_process->program.PC+1]!=0){
                    Interruptible_process->estado=READY;
                    enqueue(readyQueue,Interruptible_process);
                    Interruptible_process=NULL;
                    }else{
                        Interruptible_process->estado = ZOMBIE;
                        Interruptible_process->ZTime = 0;
                        Interruptible_process=NULL;
                    } 
                }
                
            }
        }

        for (int i=0;i<Nprocesses;i++){
            if(ciclo==Processes[i]->program.ciclos[1]-1){
                Processes[i]->estado = READY; 
                enqueue(readyQueue, Processes[i]);
            }
        }

        if(current_process!=NULL){
            if (current_process->CpuTime == 0 && quantum_restante>=0 && current_process->estado==EXECUTING) {
                if(current_process->program.ciclos[current_process->program.PC]!=0){
                    current_process->estado = INTERRUPTIBLE;
                    Interruptible_process=current_process;
                    EXcurrent_process=current_process;
                    if(current_process->program.ciclos[current_process->program.PC]!=0){
                        current_process->InterruptibleTime=current_process->program.ciclos[current_process->program.PC];
                        current_process->program.PC++;
                    }
                }else if(current_process->program.ciclos[current_process->program.PC]==0 && current_process->InterruptibleTime==0){
                    EXcurrent_process=current_process;
                    current_process->estado = ZOMBIE;
                    current_process->ZTime=0;
                }
                current_process = NULL;
                waitTime=0;
                quantum_restante=3;
            }else if(current_process->CpuTime > 0 && quantum_restante==0 && current_process->estado==EXECUTING){
                current_process->estado = READY;
                EXcurrent_process=current_process;
                enqueue(readyQueue, current_process);
                current_process = NULL;
                waitTime=0;
                quantum_restante=3;
            }
        }
        if(waitTime!=0){
           waitTime--; 
        }
        ciclo++;
    }
}

int main() {
    Queue* readyQueue = createQueue();
    int Nprocesses=4;
    Processo* processes[4];
    read_inputs(processes);
    simulate(readyQueue, processes, Nprocesses);
    return 0;
}