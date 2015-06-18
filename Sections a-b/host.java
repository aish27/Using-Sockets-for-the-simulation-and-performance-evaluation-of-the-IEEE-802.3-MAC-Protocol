import java.util.Random;

/*
 * Represents a host.
 * Each host stores its next transmission time and the number of collisions it has had.
 * 
 */
public class host
{
    private int nextTransTime;
    private int collisionsCompleted;
    private boolean packetSent;
    private final char id;
    
    public host(int id)
    {
        nextTransTime=0;
        collisionsCompleted = 0;
        packetSent = false;
        this.id = (char)(id);
        
    }
    
    public boolean getPacketSentStatus()
    {
        return packetSent;
    }
    
    public void run(int time, int numOfRunningHosts,Random randomGenerator)
    {
            if(numOfRunningHosts == 1)
            {
                packetSent =  true;
            }
            else //I collided
            {
                collisionsCompleted++;
                nextTransTime = time+ 1 + findNextTransTime(randomGenerator);
                packetSent =  false;
            }
    }
    
    public int findNextTransTime(Random randomGenerator)
    {
        //printHost();
        //System.out.print(":");
        int total = 0;
        String totalS = "";
        for(int i=0;i<getCollisionsCompleted();i++)
        {  
            int coinFlipResult = 0;
            //do a coin flip simulation [0 or 1]
            int randomInt = randomGenerator.nextInt(2);
            //append the value to total
            //System.out.print(randomInt);
            totalS = randomInt + totalS;
            total = (int) (Math.pow(2,i)*randomInt)+total;
        }
        //System.out.print("     ");
        //System.out.print(totalS+","+total+"| ");
        return total;
    }

    /**
     * @return the nextTransTime
     */
    public int getNextTransTime()
    {
        return nextTransTime;
    }

    /**
     * @return the collisionsCompleted
     */
    public int getCollisionsCompleted()
    {
        return collisionsCompleted;
    }
    
    public void printHost()
    {
        System.out.print(this.id);
    }
    

    public char getID()
    {
        return id;
    }
}