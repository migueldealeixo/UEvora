.globl main
.data
in:    .asciz "/home/pedro/Desktop/AC1/cat_noisy.gray"   # caminho da imagem original
final: .asciz "/home/pedro/Desktop/AC1/cat_final.gray"   # caminho da imagem final
final2: .asciz "/home/pedro/Desktop/AC1/cat_final_final.gray"   # caminho da imagem final

buffer: .space 239600  # espaço adequado para a imagem 599x400 com profundidade de cor de 8 bits
buffer1: .space 239600
buffer2: .space 239600

.text
######################################################
# Funcao: write_gray_image
# Descricao: Esta funcao escreve uma imagem em um ficheiro a partir de um buffer
# Argumentos:
# a0 - endereco do buffer da imagem
# a1 - tamanho do buffer
# a2 - caminho do ficheiro para escrita
# Retorna:
# (nenhum retorno direto, efeito colateral de escrever no ficheiro)
######################################################
write_gray_image:
	li   a7, 1024     
        la   a0, final   
  	li   a1, 1      
  	ecall             
  	mv   s6, a0 
  
  	li a7, 64
  	mv a0, s6
  	la a1, buffer1
  	li a2, 239600
  	ecall
  
   	li a7, 57
   	mv a0, s6
   	ecall   
   ret
   
   
######################################################
# Funcao: write_final_gray
# Descricao: Esta funcao escreve uma imagem em um ficheiro a partir de um buffer
# Argumentos:
# a0 - endereco do buffer da imagem
# a1 - tamanho do buffer
# a2 - caminho do ficheiro para escrita
# Retorna:
# (nenhum retorno direto, efeito colateral de escrever no ficheiro)
######################################################  
write_final_gray:
#cria um novo ficheiro
  li   a7, 1024     
  la   a0, final2 
  li   a1, 1      
  ecall             
  mv   s6, a0
  
  li a7, 64
  mv a0, s6
  la a1, buffer2
  li a2, 239600
  ecall
  
   li a7, 57
   mv a0, s6
   ecall   
   ret
    
######################################################
# Funcao: read_gray_image
# Descricao: Esta funcao le um ficheiro para um buffer
# Argumentos:
# a0 - caminho do ficheiro para leitura
# a1 - buffer onde a imagem será carregada
# a2 - tamanho máximo do buffer
# Retorna:
# (nenhum retorno direto, efeito colateral de carregar o buffer)
######################################################
read_gray_image:
    # Abre o arquivo para leitura
   	li a7, 1024
	la a0, in
	li a1, 0
	ecall
	mv s0, a0         # salva o descritor de arquivo

    # Ler o arquivo
	li a7, 63
	mv a0, s0
	la a1, buffer
	li a2, 239600
	ecall
               # realizar syscall

    # Fechar o arquivo
	li a7, 57
	mv a0, s0
	ecall
 ret


# Função principal que pode chamar write_gray_image ou read_gray_image
main:
    addi sp, sp, -4
    sw ra, 0(sp)
    # Exemplo de como carregar argumentos e chamar read_gray_image
    la   a0, in          # carrega o caminho da imagem original em a0
    la   a1, buffer      # carrega o endereço do buffer em a1
    jal  read_gray_image # chama read_gray_image

    # Outras operações, como processamento de imagem, podem seguir aqui
    la a0, buffer
    la a1, buffer1
    
    jal media
    	
 

    la a1, buffer1
    jal write_gray_image
    
    la a1, buffer
    la a2, buffer2
    jal mediana

    la a2, buffer2
    jal write_final_gray

    # Encerra o programa
    li   a7, 93          # syscall para sair
    li   a0, 0           # status de saída
    ecall                # realizar syscall

    lw ra, 0(sp)
    addi sp, sp, 4
ret





######################################

#   Função: find_centro_De_massa

#   Descrição: A função precorre a linhas e as colunas da imagem 

#   para encontrar o pixel correspondente ao centro de massa

#   a0 - endereço da imagem

#   a1 - array 

######################################



media:
	li s1, 0 #contador
	li s2, 9 #valor para divisao normal
	li s3, 239600
	li s4, 598
	li s5, 399
	
	
	li s6, 0 #indice linha
	li s7, 0 #indice coluna
	
	loop:
	beqz s6, media_aresta_up
	beqz s7, media_aresta_left
	beq s6, s5, media_aresta_down
	beq s7, s4, media_aresta_right 
	 
    	lbu t1, 0(a0)
    	add t2, zero, t1       #soma para a media

    	lbu t1, -600(a0)
    	add t2, t2, t1         #soma para a media 
    
    	lbu t1, -599(a0)
    	add t2, t2, t1   	   #soma para a média
    
    	lbu t1, -598(a0)
    	add t2, t2, t1         #soma para a média
    
    	lbu t1, -1(a0)
    	add t2, t2, t1         #soma para a média
    
    	lbu t1, 1(a0)
    	add t2, t2, t1         #soma para a média
    
    	lbu t1, 598(a0)
    	add t2, t2, t1	   #soma para a média
    
    	lbu t1, 599(a0)
    	add t2, t2, t1 	   #soma para a média
    
    	lbu t1, 600(a0)
    	add t2, t2, t1	   #soma para a média

    	div t2, t2, s2         #valor da media no t2
    	
    	j jumpy
    	
    	jumpy:
    
    	sb t2, 0(a1)		   #valor da media no pixel central
    	
    	
    	addi a0, a0, 1         #avança um pixel
    	addi a1, a1, 1         #avança um pixel no buffer
    	addi s1, s1, 1         #avança um no contador
    	addi s7, s7, 1
    	bne s7, s4, skippy
    	addi s6, s6, 1
    	skippy:
    	bne s3, s1, loop
    	
    	ret
		    
	    
	    



	

	
	
	
	


	media_aresta_up:
	
	bne s7, zero, salta
	j media_canto_1
	salta:
	bne s7, s4, salta1
	jal media_canto_2
	salta1:
	
	
	lbu t1, 0(a0)
    	add t2, zero, t1       #soma para a media

    	lbu t1, -1(a0)
    	add t2, t2, t1         #soma para a media 
    
    	lbu t1, 1(a0)
    	add t2, t2, t1   	   #soma para a média
    
    	lbu t1, 598(a0)
    	add t2, t2, t1         #soma para a média
    
    	lbu t1, 599(a0)
    	add t2, t2, t1         #soma para a média
    
    	lbu t1, 600(a0)
    	add t2, t2, t1         #soma para a média
    	
    	div t2, t2, s2
    	
    	j jumpy
	
	
	ret 
	
	
		media_canto_1:
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, 1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, 599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, 600(a0)
    		add t2, t2, t1         #soma para a média
    		
    		div t2, t2, s2
		j jumpy
		
		media_canto_2:
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, -1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, 599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, 598(a0)
    		add t2, t2, t1         #soma para a média
    		
    		div t2, t2, s2
		j jumpy

	media_aresta_down:
		
		bne s7, zero, salta2
		j media_canto_3
		salta2:
		bne s7, s4, salta3
		jal media_canto_4
		salta3:
		
		lbu t1, 0(a0)
	    	add t2, zero, t1       #soma para a media
	
	    	lbu t1, -1(a0)
	    	add t2, t2, t1         #soma para a media 
    
	    	lbu t1, 1(a0)
	    	add t2, t2, t1   	   #soma para a média
    
	    	lbu t1, -598(a0)
	    	add t2, t2, t1         #soma para a média
    
	    	lbu t1, -599(a0)
	    	add t2, t2, t1         #soma para a média
    
	    	lbu t1, -600(a0)
    		add t2, t2, t1         #soma para a média
	    	
    		div t2, t2, s2	
    		
    		j jumpy
	
		
			
		media_canto_3:
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, 1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, -599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, -598(a0)
    		add t2, t2, t1         #soma para a média
    		
    		div t2, t2, s2
		j jumpy
		
		media_canto_4:
		
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, -1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, -599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, -600(a0)
    		add t2, t2, t1         #soma para a média
    		
    		div t2, t2, s2
		j jumpy
		
		media_aresta_left:
		
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, 1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, 599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, 600(a0)
    		add t2, t2, t1         #soma para a média
    
    		lbu t1, -599(a0)
    		add t2, t2, t1         #soma para a média
    
    		lbu t1, -598(a0)
    		add t2, t2, t1         #soma para a média
    	
    		div t2, t2, s2
		
		j jumpy
		
		
		
		
		
		
		media_aresta_right:
		lbu t1, 0(a0)
    		add t2, zero, t1       #soma para a media

    		lbu t1, -1(a0)
    		add t2, t2, t1         #soma para a media 
    
    		lbu t1, 599(a0)
    		add t2, t2, t1   	   #soma para a média
    
    		lbu t1, -600(a0)
    		add t2, t2, t1         #soma para a média
    
    		lbu t1, 599(a0)
    		add t2, t2, t1         #soma para a média
    
    		lbu t1, 598(a0)
    		add t2, t2, t1         #soma para a média
    	
    		div t2, t2, s2
		
		j jumpy
		
		ret
		

mediana:
	li s1, 0 #contador
	li s2, 9 #valor para divisao normal
	li s3, 239600
	li s4, 598
	li s5, 399
	
	
	
	li s6, 0 #indice linha
	li s7, 0 #indice coluna
	
	while:
	beqz s6, mediana_aresta_up
	beqz s7, mediana_aresta_left
	beq s6, s5, mediana_aresta_down
	beq s7, s4, mediana_aresta_right
	 
	
    	lbu t1, 0(a1)
    	lbu t2, -600(a1)
    	lbu t3, -599(a1)
    	lbu t4, -598(a1)
    	lbu t5, -1(a1)
    	lbu t6, 1(a1)
    	lbu a5, 598(a1)
    	lbu a6, 599(a1)
    	lbu a7, 600(a1)
    	
    	
    	
    	
    	whily:
    	bltu t2, t1, movy
    	whily1:
    	bltu t3, t2, movy1
    	whily2:
    	bltu t4, t3, movy2
    	whily3:
    	bltu t5, t4, movy3
    	whily4:
    	bltu t6, t5, movy4
    	whily5:
    	bltu a5, t6, movy5
    	whily6:
    	bltu a6, a5, movy6
    	whily7:
    	bltu a7, a6, movy7
    	
    	j retest
    	
    	
    	continue:
    	

    
    	sb t5, 0(a2)		   #valor da mediana no pixel central
    	
    	jumpy1:
    	
    	
    	addi a1, a1, 1         #avança um pixel
    	addi a2, a2, 1         #avança um pixel no buffer
    	addi s1, s1, 1         #avança um no contador
    	addi s7, s7, 1
    	bne s7, s4, skippo
    	addi s6, s6, 1
    	skippo:
    	bne s3, s1, while
    	
ret
		
	movy:
	mv t0, t2
	mv t2, t1
	mv t1, t0
	j whily1
	movy1:
	mv t0, t3
	mv t3, t2
	mv t2, t0
	j whily2
	movy2:
	mv t0, t4
	mv t4, t3
	mv t3, t0
	j whily3
	movy3:
	mv t0, t5
	mv t5, t4
	mv t4, t0
	j whily4
	movy4:
	mv t0, t6
	mv t6, t5
	mv t5, t0
	j whily5
	movy5:
	mv t0, a5
	mv a5, t6
	mv t6, t0
	j whily6
	movy6:
	mv t0, a6
	mv a6, a5
	mv a5, t0
	j whily7
	movy7:
	mv t0, a7
	mv a7, a6
	mv a6, t0
	j whily
	
	
ret


retest:
	bltu t2, t1, whily
	bltu t3, t2, whily
	bltu t4, t3, whily
	bltu t5, t4, whily
	bltu t6, t5, whily
	bltu a5, t6, whily
	bltu a6, a5, whily
	bltu a7, a6, whily
	
	j continue
	
retest_aresta:

	bltu t2, t1, whilya
	bltu t3, t2, whilya
	bltu t4, t3, whilya
	bltu t5, t4, whilya
	bltu t6, t5, whilya
	
	
	beqz s6, continue_aresta_up
	beqz s7, continue_aresta_left
	beq s6, s5, continue_aresta_down
	beq s7, s4, continue_aresta_right


mediana_aresta_up:

        beqz s7, mediana_canto_1
        beq  s7, s4, mediana_canto_2
	

	lbu t1, 0(a1)
    	lbu t2, -1(a1)
    	lbu t3, 1(a1)
    	lbu t4, 598(a1)
    	lbu t5, 599(a1)
    	lbu t6, 600(a1)
    	j whiles_arestas
    	
    	
    	
    	
    	continue_aresta_up:
    	
    	li a7, 0
    	li t0, 2
    	
    	add a7, t3, t4
    	div a7, a7, t0
    	sb a7, 0(a2)
    	
    	
    	
    	
    	j jumpy1
    	
    	
    	
ret   	

mediana_aresta_down:

        beqz s7, mediana_canto_3
        beq  s7, s4, mediana_canto_4
	

	lbu t1, 0(a1)
    	lbu t2, -1(a1)
    	lbu t3, 1(a1)
    	lbu t4, -598(a1)
    	lbu t5, -599(a1)
    	lbu t6, -600(a1)
    	
    	j whiles_arestas
    	
    	continue_aresta_down:
    	
    	li a7, 0
    	li t0, 2
    	
    	add a7, t3, t4
    	div a7, a7, t0
    	sb a7, 0(a2)
    	
    	
    	
    	
    	j jumpy1
    	
    	
    	
ret   	
    	
    	
        movya1:
	mv t0, t2
	mv t2, t1
	mv t1, t0
	j whilya1
	movya2:
	mv t0, t3
	mv t3, t2
	mv t2, t0
	j whilya2
	movya3:
	mv t0, t4
	mv t4, t3
	mv t3, t0
	j whilya3
	movya4:
	mv t0, t5
	mv t5, t4
	mv t4, t0
	j whilya4
	movya5:
	mv t0, t6
	mv t6, t5
	mv t5, t0
	j whilya
	
	
	


mediana_aresta_left:


	

	lbu t1, 0(a1)
    	lbu t2, 1(a1)
    	lbu t3, 599(a1)
    	lbu t4, 600(a1)
    	lbu t5, -599(a1)
    	lbu t6, -598(a1)
    	
    	j whiles_arestas
    	
    	continue_aresta_left:
    	
    	li a7, 0
    	li t0, 2
    	
    	add a7, t3, t4
    	div a7, a7, t0
    	sb a7, 0(a2)
    	
    	
    	
    	
    	j jumpy1
    	
    	
    	
ret

mediana_aresta_right:

	

	lbu t1, 0(a1)
    	lbu t2, -1(a1)
    	lbu t3, 599(a1)
    	lbu t4, -600(a1)
    	lbu t5, -599(a1)
    	lbu t6, -598(a1)
    	
    	j whiles_arestas
    	
    	
    	
    	continue_aresta_right:
    	
    	li a7, 0
    	li t0, 2
    	
    	add a7, t3, t4
    	div a7, a7, t0
    	sb a7, 0(a2)
    	
    	
    	
    	
    	j jumpy1
    	
    	
    	
ret
whiles_arestas:

	whilya:
    	bltu t2, t1, movya1
    	whilya1:
    	bltu t3, t2, movya2
    	whilya2:
    	bltu t4, t3, movya3
    	whilya3:
    	bltu t5, t4, movya4
    	whilya4:
    	bltu t6, t5, movya5
    	
    	j retest_aresta



mediana_canto_1:

	lbu t1, 0(a1)
    	lbu t2, 1(a1)
    	lbu t3, 599(a1)
    	lbu t4, 600(a1)
    	
    	j whiles_cantos
    	
    	
mediana_canto_2:

	lbu t1, 0(a1)
    	lbu t2, -1(a1)
    	lbu t3, 598(a1)
    	lbu t4, 599(a1)
    	j whiles_cantos
    	
    	
mediana_canto_3:

	lbu t1, 0(a1)
    	lbu t2, 1(a1)
    	lbu t3, -599(a1)
    	lbu t4, -598(a1)
    	j whiles_cantos
    	
    	
mediana_canto_4:

	lbu t1, 0(a1)
    	lbu t2, -1(a1)
    	lbu t3, -599(a1)
    	lbu t4, -600(a1) 
    	j whiles_cantos   	
    	
continue_canto:
    	
    	li a7, 0
    	li t0, 2
    	
    	add a7, t2, t3
    	div a7, a7, t0
    	sb a7, 0(a2)
    	
    	j jumpy1
    	
 
    	

    	
    	
    	
    	
	
		


whiles_cantos:

	whilyc:
    	bltu t2, t1, movyc1
    	whilyc1:
    	bltu t3, t2, movyc2
    	whilyc2:
    	bltu t4, t3, movyc3
    	
    	
    	j retest_cantos
    	
    	
    	
    	
    	
    	
	movyc1:
	mv t0, t2
	mv t2, t1
	mv t1, t0
	j whilyc
	movyc2:
	mv t0, t3
	mv t3, t2
	mv t2, t0
	j whilyc1
	movyc3:
	mv t0, t4
	mv t4, t3
	mv t3, t0
	j whilyc2



retest_cantos:
	
	bltu t2, t1, whilyc
	bltu t3, t2, whilyc
	bltu t4, t3, whilyc
	
	j continue_canto









