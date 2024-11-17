CC = gcc
OUTPUT = trabalho1
SRCS = trabalho1.c
OBJS = $(SRCS:.c=.o)
HDRS = 

all: $(OUTPUT)

$(OUTPUT): $(OBJS)
	$(CC) -o $(OUTPUT) $(OBJS)

%.o: %.c $(HDRS)
	$(CC) -c -o $@ $
