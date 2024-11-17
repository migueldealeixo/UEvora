package trabalho1;

import java.util.Random;

public class SimText  {

private static void adicionarEntidades(Grassland gra, int x, int y){
    Random random = new Random();
    int numCarrots = random.nextInt(2);
    for(int c = 0; c < numCarrots; c++){
        gra.addCarrot(x, y);
    }
    int numRabbits = random.nextInt(2);
    for(int r = 0; r < numRabbits; r++){
        gra.addRabbit(x, y);
    }
}

private static void inicializarPrado(Grassland meadow, int width, int height){

    int grupo = 4;
    int espaço = 4;

    for(int x = 0; x < width; x+= grupo + espaço){
        for(int y = 0; y < height; y += grupo + espaço){
            for(int i = 0; i < grupo; i++){
                for(int j = 0; j < grupo; j++){
                    int cx = x +i;
                    int cy = y +j;
                    if(cx < width && cy < height){
                        adicionarEntidades(meadow, cx, cy);                      
                            }
                        }
                    }
                }
            }
      }
private static void printarPrado(Grassland meadow){
    for(int y = 0; y < meadow.height(); y++){
        for(int x = 0; x < meadow.width(); x++){
            switch(meadow.cellContents(x, y)){
                case Grassland.EMPTY:
                System.out.print("E");
                break;

                case Grassland.CARROT:
                System.out.print("C");
                break;

                case Grassland.RABBIT:
                System.out.print("R");
                break;

            }
            System.out.print(" ");
              
        }
        System.out.println();
    }
    System.out.println();
}


public static void main(String[] args) {
    int width = 20;
    int height = 20;
    int starveTime = 4;

    Grassland meadow = new Grassland(width, height, starveTime);
    inicializarPrado(meadow, width, height);
    int timeStep = 20;

    for(int i = 0; i < timeStep; i++){
        printarPrado(meadow);

        meadow = meadow.timeStep();


        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }


}




}
