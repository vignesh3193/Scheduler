package scheduler;

import java.io.*;
import java.util.*;


public class scheduler {

	
	public static void main(String args[])throws IOException
	{
		//System.out.println("enter name of input file with .extension");
		//Scanner sc=new Scanner(System.in);
		//String file_name=sc.nextLine();
		int verbose_flag=0;
		String file_name;
		if(args[0].equals("--verbose"))
		{
			verbose_flag=1;
		}
		if(args.length==1)
		{
			file_name=args[0];
			
		}
		else
		{
			file_name=args[1];
			
		}
		File file=new File(file_name);
		File random_numbers=new File("random-numbers.txt");
		Scanner r_sc=new Scanner(random_numbers);
		
		int[][] process_list=process_details(file);
		int[][] process_list2=new int[process_list.length][5];
		int[][] process_list3=new int[process_list.length][5];
		int[][] process_list4=new int[process_list.length][5];
		
		for(int i=0;i<process_list.length;i++)
		{
			for(int j=0;j<5;j++)
			{
				process_list2[i][j]=process_list[i][j];
				process_list3[i][j]=process_list[i][j];
				process_list4[i][j]=process_list[i][j];
			}
		}
		
		
		 
		System.out.println("-------------------------SCHEDULE ALGORITHM USED: FCFS-----------------------------------------------");
		fcfs.FCFS(r_sc,process_list,verbose_flag);
		
		r_sc=new Scanner(random_numbers);
		System.out.println("-------------------------SCHEDULE ALGORITHM USED: LCFS-----------------------------------------------");
		lcfs.LCFS(r_sc,process_list2,verbose_flag);
		
		r_sc=new Scanner(random_numbers);
		System.out.println("-------------------------SCHEDULE ALGORITHM USED: RR-----------------------------------------------");
		rr.RR(r_sc,process_list3,verbose_flag);
	
		r_sc=new Scanner(random_numbers);
		System.out.println("-------------------------SCHEDULE ALGORITHM USED: HPRN-----------------------------------------------");
		hprn.HPRN(r_sc,process_list4,verbose_flag);
	
		//sc.close();
		r_sc.close();
		
	}	
	
	static int[][] process_details(File file)throws IOException
	{
		Scanner sc=new Scanner(file);
		int[][] processes=new int[sc.nextInt()][5];
		int count=0;
		while(sc.hasNextInt())
		{
			processes[count][0]=count;
			for(int i=1;i<5;i++)
			{
				processes[count][i]=sc.nextInt();
			}
			count++;
		}		
		sc.close();
		return processes;
	}
	
	static int randomOS(Scanner sc,int U)throws IOException
	{
		
		/*RandomAccessFile r=new RandomAccessFile(file,"r");
		r.seek(random_access);
		int X=r.readInt();*/
		
		
		int X=sc.nextInt();
		
		return (1+(X%U));
	}
	
	
		
	
	
	
	
	
	
}
