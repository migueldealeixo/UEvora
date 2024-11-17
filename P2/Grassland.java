package trabalho1;

public class Grassland {


    public final static int EMPTY = 0;
    public final static int RABBIT = 1;
    public final static int CARROT = 2;


    private int starveTime;
    public Coelho[][] coelho;
    public int[][] grassland;

    //Construtor Principal
    public Grassland (int i, int j, int starveTime){
        this.starveTime = starveTime;
        int x,y;
        this.coelho = new Coelho[i][j];
        this.grassland = new int[i][j];

        for(x = 0; x < i; x++ ){
            for(y = 0 ; y < j ; y++){
                grassland[x][y] = EMPTY;
            }
        }
    }
    
    public int width(){
        return grassland.length;
    }
    
    public int height(){
        return grassland[0].length;
    }

    public int starveTime(){
        return starveTime;
    }

    public void addCarrot(int x, int y){
      if(grassland[x][y] == EMPTY){
         grassland[x][y] = CARROT;
        }
      
    }

    public void addRabbit(int x, int y){
        if(grassland[x][y] == EMPTY){
                grassland[x][y] = RABBIT;
                coelho[x][y] = new Coelho();
            }
        }
      
    public int cellContents(int x, int y){
        return grassland[x][y];
        }      

  
    private int vizinhos(int x, int y, int type){
    int count = 0;
    for(int i = -1; i <= 1; i++){
        for(int j = -1; j <=1; j++){
            if(i != 0 || j != 0){
                int newX = (x + i + width()) % width();
                int newY = (y + j + height()) % height();
                if(grassland[newX][newY] == type){
                    count++;
                }
            }
        }
    }
    return count;
}

    private void moverCoelhos(Grassland grass) {
        for (int x = 0; x < height(); x++) {
            for (int y = 0; y < width(); y++) {
                if (grass.coelho[x][y] != null) {
                    this.coelho[x][y] = new Coelho();
                }
            }
        }
    }

    public int regrasJogo(int x, int y){
        int celulaAtual = grassland[x][y];
        int vizinhosCenoura = vizinhos(x, y, CARROT);
        int vizinhosCoelho = vizinhos(x, y, RABBIT);
        int novaCelula = celulaAtual;
        
            switch (celulaAtual) {
                case RABBIT:
                    if (vizinhosCenoura != 0) {
                        novaCelula = RABBIT; 
                        coelho[x][y].eat(CARROT);
                    } else {
                        if (coelho[x][y].isHungry()) {
                            novaCelula = EMPTY;
                            coelho[x][y] = null;
                        } else{
                            coelho[x][y].aumentarStarveTime();
                            novaCelula = RABBIT;
                        }
                    }
                
                break;
                case CARROT:
                    if(vizinhosCoelho == 0){
                        novaCelula = CARROT;
                    }else if(vizinhosCoelho == 1){
                        novaCelula = RABBIT;
                        coelho[x][y] = new Coelho();
                    }
                    else{
                        novaCelula = EMPTY;
                    }
                    break;
                case EMPTY:
                    if (vizinhosCenoura < 2) {
                        novaCelula = EMPTY;
                    } else if (vizinhosCenoura >= 2 && vizinhosCoelho == 0) {
                        novaCelula = CARROT;
                    } else if (vizinhosCenoura >= 2 && vizinhosCoelho >= 2) {
                        novaCelula = RABBIT;
                        coelho[x][y] = new Coelho();
                    break;
            }
        
            
        }
        return novaCelula;
    }
        
    public Grassland timeStep(){
        Grassland newGrassland =  new Grassland(width(), height(), starveTime);
        for(int x = 0; x < height(); x ++){
            for(int y = 0; y < width(); y++){
                int newBoard = regrasJogo(x, y);
                newGrassland.grassland[x][y] = newBoard;
            }
        }
        newGrassland.moverCoelhos(this);
        return newGrassland;    
    }
}