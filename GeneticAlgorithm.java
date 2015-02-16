import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {

	static String targetString="I am a Monkey".toUpperCase();
	static final int MUTATIONRATE=50;
	static final int CATACLYSMRATE=80;

	public static void main(String[] args) throws Exception {

		int numberOfGenerations=0;
		String[] initialPopulation=generatePopulation(1000, targetString.length());
		String[] newPopulation=new String[initialPopulation.length];
		String bestFitString="";
		int bestFitValue=100000;
		while(numberOfGenerations<5000 && bestFitValue!=0)
		{
			for(int i=0;i<initialPopulation.length;i++)
			{
				String[] parents=randomSelection(initialPopulation);

				String child=reproduce(parents[0], parents[1]);
				Random rand=new Random();
				int a=rand.nextInt(100);
				if(a>MUTATIONRATE)
				{

					Random rand1=new Random();
					int loc = rand1.nextInt((child.length()));
					Random r1=new Random();
					int mutation=r1.nextInt(91-65+1)+65;
					if(mutation==91)
					{
						child=child.replace(child.charAt(loc), (char)32);
					}
					else
					{
						child=child.replace(child.charAt(loc), (char)mutation);
					}
				}
				int evaluationOfChild=levenshteinDistance(child,targetString);
				if(evaluationOfChild<bestFitValue)
				{
					bestFitValue=evaluationOfChild;
					bestFitString=child;
				}
				newPopulation[i]=child;
				
			}
			Random r=new Random();
			int cata=r.nextInt(100);
			if(cata>CATACLYSMRATE)
			{
				String[] cataclysm=generatePopulation(newPopulation.length-100, targetString.length());
				for(int k=100;k<newPopulation.length;k++)
				{
					newPopulation[k]=cataclysm[k-100];
				}
			}
			System.out.println("Summary of Generation Number : " +numberOfGenerations+ " \n\n"+" Best Fit String in this Generation : " + bestFitString+ "\t with cost : "+ bestFitValue + " \n\n\n\n");

			initialPopulation=newPopulation;

			numberOfGenerations++;
		}







	}

	public static int levenshteinDistance(String str1, String str2)
	{
		int [][] cost= new int[str1.length()+1][str2.length()+1];

		//inititalization for the base case, when either of string is of length 0
		for(int i=0;i<=str1.length();i++)
		{
			cost[i][0]=i;
		}
		for(int j=0;j<=str2.length();j++)
		{
			cost[0][j]=j;
		}

		//calculation using memoization
		for(int i=1;i<=str1.length();i++)
		{
			for(int j=1;j<=str2.length();j++)
			{	
				int c=0;
				if (str1.charAt(i-1)==str2.charAt(j-1))
				{
					c=0;
				}
				else
				{
					c=1;
				}


				cost[i][j]=Math.min(cost[i-1][j]+1,Math.min(cost[i][j-1]+1, cost[i-1][j-1]+c));

			}
		}

		//		for(int i=0;i<=str1.length();i++)
		//		{
		//			for(int j=0;j<=str2.length();j++)
		//			{
		//				System.out.print(cost[i][j]+" ");
		//			}
		//			System.out.println();
		//		}
		return cost[str1.length()][str2.length()];
	}

	//function to reproduce a child
	public static String reproduce(String parent1, String parent2) throws Exception
	{

		Random rand=new Random();
		String child="";
		int a = rand.nextInt((parent1.length())-1);
		if(a==0)
		{
			child=parent2;
		}
		else if(a==parent2.length())
		{
			child=parent1;
		}
		else
		{
			child=parent1.substring(0, a)+parent2.substring(a,parent2.length());
		}
		if(child.length()!=parent1.length())
		{
			throw new Exception();
		}
		return child;
	}
	//Function to generate a random population of given Population Size and  Fixed size of individuals
	public static String[] generatePopulation(int sizeOfPopulation, int sizeOfIndividual)
	{
		String [] population=new String[sizeOfPopulation];
		for(int i=0;i<sizeOfPopulation;i++)
		{
			Random rand=new Random();
			char[] individual=new char[sizeOfIndividual];
			for(int j=0;j<sizeOfIndividual;j++)
			{
				int a=rand.nextInt(91-65+1)+65;
				if(a==91)
				{
					individual[j]=(char)32;
				}
				else
				{
					individual[j]=(char)a;
				}
			}
			population[i]=new String(individual);
		}
		return population;
	}

	public static String[] randomSelection(String[] population)
	{

		Random newrand=new Random();
		Arrays.sort(population, new costSort());
		int chance=newrand.nextInt(100);
		String[] parents=new String[2];
		int index=newrand.nextInt(population.length-20);
		if(chance>50)
		{
			parents[0]=population[index];	
			parents[1]=population[index+1];
		}
		else
		{
			parents[0]=population[index];	
			parents[1]=population[index+20];
		}
		return parents;
	}

	public static class costSort implements Comparator<String>
	{
		public int compare(String c1, String c2)
		{
			int a=levenshteinDistance(c1, targetString);
			int b=levenshteinDistance(c2, targetString);
			if(a<b)
				return -1;
			else if(a>b)
				return 1;
			else
				return 0;
		}
	}
}


