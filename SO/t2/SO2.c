#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MEM_SIZE 20 
#define FRAME_SIZE 2 
#define NUM_FRAMES (MEM_SIZE / FRAME_SIZE) 
#define MAX_PROCESSES 5 

typedef struct {
    int process_id;
    int page_number;
} Frame;

typedef struct {
    int process_id;
    int memory_size;
    int num_pages;
    int pages_loaded;
} Process;

Frame memory[NUM_FRAMES];
Process processes[MAX_PROCESSES];
int fifo_queue[NUM_FRAMES];
int clock_hand = 0;
int use_bit[NUM_FRAMES];


//Inputs
int inputP1Mem00[] = {5, 6, 3, 6, 4};
int inputP1Exec00[] = {2, 3, 2, 1, 5, 2, 4, 5, 3, 2, 5, 2};

//Incializalção de Memoria e Processos
void initialize_memory() {
    for (int i = 0; i < NUM_FRAMES; i++) {
        memory[i].process_id = -1; // -1 indica frame livre
        fifo_queue[i] = -1; // FIFO queue inicialmente vazia
        use_bit[i] = 0; // Use bit inicializado a 0
    }
}

void initialize_processes() {
    for (int i = 0; i < MAX_PROCESSES; i++) {
        processes[i].process_id = i + 1;
        processes[i].memory_size = inputP1Mem00[i];
        processes[i].num_pages = (inputP1Mem00[i] + FRAME_SIZE - 1) / FRAME_SIZE;
        processes[i].pages_loaded = 0;
    }
}
int find_free_frame() {
    for (int i = 0; i < NUM_FRAMES; i++) {
        if (memory[i].process_id == -1) {
            return i;
        }
    }
    return -1;
}
int fifo_replace(int current_process) {
    int frame_to_replace;
    do {
        frame_to_replace = fifo_queue[0];
        for (int i = 0; i < NUM_FRAMES - 1; i++) {
            fifo_queue[i] = fifo_queue[i + 1];
        }
        fifo_queue[NUM_FRAMES - 1] = -1;
    } while (memory[frame_to_replace].process_id == current_process);
    return frame_to_replace;
}

int clock_replace(int current_process) {
    while (1) {
        if (use_bit[clock_hand] == 0 && memory[clock_hand].process_id != current_process) {
            int frame_to_replace = clock_hand;
            clock_hand = (clock_hand + 1) % NUM_FRAMES;
            return frame_to_replace;
        }
        use_bit[clock_hand] = 0;
        clock_hand = (clock_hand + 1) % NUM_FRAMES;
    }
}

void load_process(int process_id, int (*replace_algorithm)(int)) {
    Process *proc = &processes[process_id - 1];
    for (int i = 0; i < proc->num_pages; i++) {
        int frame_index = find_free_frame();
        if (frame_index == -1) {
            frame_index = replace_algorithm(process_id);
        }
        memory[frame_index].process_id = process_id;
        memory[frame_index].page_number = i;
        use_bit[frame_index] = 1;
        for (int j = 0; j < NUM_FRAMES; j++) {
            if (fifo_queue[j] == -1) {
                fifo_queue[j] = frame_index;
                break;
            }
        }
        proc->pages_loaded++;
    }
}

void print_memory_state(int time) {
    printf("%2d |", time);
    for (int i = 0; i < MAX_PROCESSES; i++) {
        int first = 1;
        for (int j = 0; j < NUM_FRAMES; j++) {
            if (memory[j].process_id == i + 1) {
                if (!first) printf(",");
                first = 0;
                printf(" F%d", j);
            }
        }
        printf(" |");
    }
    printf("\n");
}

void run_simulation(int (*replace_algorithm)(int), const char* output_file) {
    FILE *out = fopen(output_file, "w");
    for (int i = 0; i < sizeof(inputP1Exec00) / sizeof(inputP1Exec00[0]); i++) {
        int process_id = inputP1Exec00[i];
        load_process(process_id, replace_algorithm);
        print_memory_state(i + 1);
        fprintf(out, "%2d |", i + 1);
        for (int j = 0; j < MAX_PROCESSES; j++) {
            int first = 1;
            for (int k = 0; k < NUM_FRAMES; k++) {
                if (memory[k].process_id == j + 1) {
                    if (!first) fprintf(out, ",");
                    first = 0;
                    fprintf(out, " F%d", k);
                }
            }
            fprintf(out, " |");
        }
        fprintf(out, "\n");
    }
    fclose(out);
}
int main() {
    initialize_memory();
    initialize_processes();

    printf("Simulação FIFO:\n");
    run_simulation(fifo_replace, "fifo00.out");

    initialize_memory();
    printf("\nSimulação Clock:\n");
    run_simulation(clock_replace, "clock00.out");

    return 0;
}
