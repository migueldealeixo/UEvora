package trabalho1;
public class Coelho {    
    private int rabbitStarveTime;
    private int fome;  
      
    public Coelho() {
        rabbitStarveTime = 0;
        fome = 0;
    }
    public int getRabbitStarveTime() {
        return rabbitStarveTime;
    }
    public int aumentarStarveTime() {
        return rabbitStarveTime++;
    }
    public boolean isHungry(){
        return fome >= rabbitStarveTime;
    }
    public int eat(int cenoura){
        rabbitStarveTime = 0;
        fome = Math.max(0, fome - cenoura);
        return rabbitStarveTime;
    }
}
