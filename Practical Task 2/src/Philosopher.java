
import java.util.Random;


public class Philosopher implements Runnable {
	
	private int id;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	
	private Random randomGenerator = new Random();
	
	private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;

	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;

	protected Boolean hasBeenFed = false;


	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;


		
		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution 
		 
		 * NOTE
		 * In order to get the same average values use the seed 100, and set the id of the philosopher starting from 0 to 4 (0,1,2,3,4). 
		 * Each philosopher sets the seed to the random number generator as seed+id. 
		 * The seed for each philosopher is as follows:
		 * 	 	P0.seed = 100 + P0.id = 100 + 0 = 100
		 * 		P1.seed = 100 + P1.id = 100 + 1 = 101
		 * 		P2.seed = 100 + P2.id = 100 + 2 = 102
		 * 		P3.seed = 100 + P3.id = 100 + 3 = 103
		 * 		P4.seed = 100 + P4.id = 100 + 4 = 104
		 * Therefore, if the ids of the philosophers are not 0,1,2,3,4 then different random numbers will be generated.
		 */
		
		randomGenerator.setSeed(id+seed);
	}
	public int getId() {
		return id;
	}

	public double getAverageThinkingTime() {
		//to get the averages we divide the time by how many repetitions
		return avg(thinkingTime,numberOfThinkingTurns);


		/* TODO
		 * Return the average thinking time
		 * Add comprehensive comments to explain your implementation
		 */

	}

	public double getAverageEatingTime() {
		return avg(eatingTime,numberOfEatingTurns);



		/* TODO
		 * Return the average eating time
		 * Add comp rehensive comments to explain your implementation
		 */

	}

	public double getAverageHungryTime() {
		return avg(hungryTime,numberOfHungryTurns);

		/* TODO
		 * Return the average hungry time
		 * Add comprehensive comments to explain your implementation
		 */
		
	}
	
	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}
	
	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}
	
	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}

	public double getTotalThinkingTime() {
		return thinkingTime;
	}

	public double getTotalEatingTime() {
		return eatingTime;
	}

	public double getTotalHungryTime() {
		return hungryTime;
	}


	@Override
	public void run() {


		while(!hasBeenFed) {

			//run while the thread of this philosopher has not been interrupted
			long beganThinking = System.currentTimeMillis();
			think();
			thinkingTime = thinkingTime + (System.currentTimeMillis() - beganThinking);

			long beganBeingHungry = System.currentTimeMillis();
			hungry();

			//attempt the procedure of picking up the chopsticks and eating
			boolean hasEaten = false;
			while (!hasEaten) {
				if (leftChopStick.myLock.tryLock()) { //so if we manage to lock the left one
					if (DiningPhilosopher.getDebugStatus()) {
						System.out.println("Philosopher " + id + " as picked up the left chopstick");
					}
					//we try locking the second one
					if (rightChopStick.myLock.tryLock()) {
						//once we have achieved locking the second one
						if (DiningPhilosopher.getDebugStatus()) {
							System.out.println("Philosopher " + id + " as picked up the right chopstick");
						}
						//the hunger ends as sons as they have both chopsticks
						hungryTime = hungryTime + (System.currentTimeMillis() - beganBeingHungry); //pretty much the total hungry time is the addition of it and being hungery this time
						//now that we know how much he was hungry for he can finally eatiginal mix
						long beganEating = System.currentTimeMillis();
						eating();
						eatingTime = eatingTime + (System.currentTimeMillis() - beganEating); //again, its the total eating time, plus the eaten time this turn
						hasEaten = true; //meaning that it has eaten and doesnt have to try picking up the left chopstick again
						//now we can unlock these cause we are done eatin
						rightChopStick.myLock.unlock();
						if (DiningPhilosopher.getDebugStatus()) {
							System.out.println("Philosopher " + id + " has put down the right chopstick");
						}
					}
					leftChopStick.myLock.unlock();
					if (DiningPhilosopher.getDebugStatus()) {
						System.out.println("Philosopher " + id + " has put down the left chopstick");
					}
				}
			}
		}


	}


	public void think() {
		//I had to use randomGenerator because if not it wouldnt work for whatever reason
		try {
			if(DiningPhilosopher.getDebugStatus()) {
				System.out.println("Philosopher " + id + " Is thinking.");
			}
			numberOfThinkingTurns++;
			int randNumber = randomGenerator.nextInt(1000);
			//just adding the number thing to the time thought
			Thread.sleep(randNumber);

		} catch(InterruptedException e) {
			hasBeenFed = true; //this is set so the while loop can be stopped as soon as it is interrupted
		}

	}

	public void hungry(){

		if(DiningPhilosopher.getDebugStatus()) {
			System.out.println("Philosopher " + id + " Is hungry.");
		}
			numberOfHungryTurns++;

	}

	public void eating() {

		try {
			if (DiningPhilosopher.getDebugStatus()) {
				System.out.println("Philosopher " + id + " Is eating.");
			}
			numberOfEatingTurns++;
			int randNumber = randomGenerator.nextInt(1000);
			Thread.sleep(randNumber);
		} catch(InterruptedException e) {
			hasBeenFed = true;
		}

	}

	private double avg(double x , double y) {
		if(y == 0) { //cause if not you get infinite values (cant divide by 0)
			return 0;
		} else {
			return  x/y;
		}
	}


}
