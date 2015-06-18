
import java.util.ArrayList;
import java.util.Random;

/*   
 * Runs the simulation
 */
public class main
{

    private static ArrayList<host> hosts;
    private static Random randomGenerator;
    private static int firstWaitTime;
    private static int secondWaitTime;
    private static int lastWaitTime;
    private static int elementDone;
    private static boolean justSent;

    public static void main(String[] args)
    {
        int numOfHosts = 5;
        initializeARun(numOfHosts);
        for(numOfHosts=20;numOfHosts<=100;numOfHosts=numOfHosts+20)
            initializeARun(numOfHosts);
    }

    public static void initializeARun(int numOfHosts)
    {
        System.out.println("Number of hosts = " + numOfHosts);
        firstWaitTime = 0;
        secondWaitTime = 0;
        lastWaitTime = 0;
        for (int i = 0; i < 100; i++)
        {
            elementDone = 0;
            simulateOneRun(numOfHosts);
        }
        System.out.println();
        System.out.println("First Element Average delay:" + firstWaitTime
                / 100.0);
        System.out.println("Second Element Average delay:" + secondWaitTime
                / 100.0);
        System.out.println(" Element Average delay:" + lastWaitTime / 100.0);

    }

    public static void simulateOneRun(int numOfElements)
    {
        //System.out.print("Time  ");
        //System.out.format("%32s", "Hosts who try      ");
        //System.out.print("Coin flip result(Binary),");
        //System.out.print("Next trasmit time(Integer)");
        randomGenerator = new Random();
        hosts = new ArrayList<host>(numOfElements);
        //Initialize the array
        for (int i = 0; i < numOfElements; i++)
        {
            hosts.add(new host(i + 65));

        }
        //While no one has sent data, continue this process
        int time = 0;
        ArrayList<host> runners = new ArrayList<host>();
        while (!allSent())
        {
            //System.out.println();
            //System.out.format("%s", time);
            int sent = anyoneSent();
            if (sent != -1) //if anyone was just sent, don't send anything in the current block.
            {
                switch (elementDone)
                {
                    case 1: firstWaitTime += time;
                        break;
                    case 2: secondWaitTime += time;
                        break;
                    default: elementDone += 0;
                }
                hosts.remove(sent);
                runners = findNumOfRunningHosts(time);
                //System.out.format("%32s", printAllRunners(runners));
                //System.out.print("      ");
                for (host a : runners)
                {
                    a.run(time, runners.size() + 1, randomGenerator);
                }
                //System.out.print("Channels recognize no one can be sent");
            } else
            {
                runners = findNumOfRunningHosts(time);
                //System.out.format("%32s", printAllRunners(runners));
                //System.out.print("      ");
                for (host a : runners)
                {
                    a.run(time, runners.size(), randomGenerator);
                }
            }
            time++;
        }
        lastWaitTime += time;
        //System.out.println();
        //System.out.print(time + "     ");
        //System.out.println("Channels sense that a host has sent packet.");
    }

    public static String printAllRunners(ArrayList<host> runners)
    {
        String temp = "";
        for (int i = 0; i < runners.size(); i++)
        {
            temp = temp + (runners.get(i).getID());
        }
        return temp;
    }

    public static int anyoneSent()
    {
        for (int i = 0; i < hosts.size(); i++)
        {
            if (hosts.get(i).getPacketSentStatus())
            {
                elementDone++;
                return i;
            }
        }
        return -1;
    }

    public static boolean allSent()
    {
        for (int i = 0; i < hosts.size(); i++)
        {
            if (!hosts.get(i).getPacketSentStatus())
            {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<host> findNumOfRunningHosts(int time)
    {
        ArrayList<host> temp = new ArrayList<host>();
        for (host a : hosts)
        {
            if (time == a.getNextTransTime())
            {
                temp.add(a);
            }
        }
        return temp;
    }

}
