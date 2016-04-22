package scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class rr {
	//------------------------------------------------------------------------------------------------------
	//-----------------------------------ROUND ROBIN FUNCTION, Q=2------------------------------------------------------
	//------------------------------------------------------------------------------------------------------
		
		static void RR(Scanner random, int[][] process_list,int verbose_flag)throws IOException
		{
			
			
			
			int[][] new_process_list=new int[process_list.length][];
			int[] process_lengths=new int[process_list.length];
			int cnt=0;
			
		//------------------------------SORTING INPUT PROCESSES BY ARRIVAL TIME -------------------------------------------
			int temptime=0;
			while(cnt<process_list.length)
			{
				for(int j=0;j<process_list.length;j++)
				{
					if(process_list[j][1]==temptime)
					{
						//System.out.println(Arrays.toString(process_list[j]));
						new_process_list[cnt]=process_list[j];
						cnt++;
					}
				}
				temptime++;
			}
			for(int i=0;i<process_list.length;i++)
			{
				process_lengths[i]=new_process_list[i][3];
				new_process_list[i][0]=i;
				//System.out.println("printing sorted"+Arrays.toString(new_process_list[i]));
			}
			
			
			process_list=new_process_list;
			System.out.println("the sorted input");
			System.out.print(process_list.length);
			for(int i=0;i<process_list.length;i++)
			{
				System.out.print("  "+process_list[i][1]+" "+ process_list[i][2]+" "+ process_lengths[i]+" "+process_list[i][4]+"  ");
			}
				
			System.out.println();
			System.out.println();
			System.out.println();
		


			int time=0;
			int CPU_util=0;
			int IO_util=0;
			double avg_tt=0.0;
			double avg_wt=0.0;
			ArrayList<int[]> completed=new ArrayList<int[]>();	
			ArrayList<int[]> ready=new ArrayList<int[]>();
			ArrayList<int[]> blocked=new ArrayList<int[]>();
			ArrayList<int[]> toReady=new ArrayList<int[]>();
			String[] process_status=new String[process_list.length];
			int[] remaining_runtime=new int[process_list.length];
 			int[] wait_time=new int[process_list.length];
			int[] complete_time=new int[process_list.length];
			int[] io_time=new int[process_list.length];
			int[] running=new int[5];
			int[] wasrunning=new int[5];
			wasrunning=null;
			running=null;
			int runtime=0;
			int q=0;
			
			Hashtable<Integer,Integer> iotime=new Hashtable<Integer,Integer>();
			
			if(verbose_flag==1)
			{
				System.out.print("before cycle 0: ");
				for(int i=0;i<process_list.length;i++)
				{
					System.out.print(" unstarted  0  ");
				}
			}
			System.out.println();
			
			for(int i=0;i<process_list.length;i++)
			{
				remaining_runtime[i]=-1;
				process_status[i]="unstarted";
				wait_time[i]=0;
				io_time[i]=0;
				
			}
			
			
			while(completed.size()!=process_list.length)
			{
				for(int i=0;i<process_list.length;i++)
				{
					if(process_list[i][1]==time)
					{
						toReady.add(process_list[i]);
						process_status[i]="ready";
					}
				}
			
			
			
			//--------------------------HANDLING BLOCKED PROCESSES--------------------------------------------------
				
				if(!blocked.isEmpty())
				{	
					
				
					int i=0;
					//size changes when you remove process, FIX
					while(i<blocked.size())
					{
						
						int g=iotime.get(blocked.get(i)[0]);
						g--;
						if(g==0)
						{	
							toReady.add(blocked.get(i));
							//ready.add(blocked.get(i));
							process_status[blocked.get(i)[0]]="ready";
							iotime.remove(blocked.get(i)[0]);
							blocked.remove(i);
				
						}
						else
						{
							iotime.replace(blocked.get(i)[0], g);
							i++;
						}
						
					}
					
					
					
				}
				
	//---------------------------ADDING TO READY LIST------------------------
				if(!ready.isEmpty()&&wasrunning!=null)
				{
					toReady.add(wasrunning);
					wasrunning=null;
				}
				while(!toReady.isEmpty())
				{	
					int min=Integer.MAX_VALUE;
					int[] minproc=new int[5];
					
					for(int[] proc:toReady)
					{
						if(proc[1]<min)
						{
							min=proc[1];
							minproc=proc;
						}
						if(proc[1]==min)
						{
							if(proc[0]<minproc[0])
							{
								minproc=proc;
							}
						}

					}
					ready.add(minproc);
					toReady.remove(minproc);
				}

			if(wasrunning!=null)
			{
				ready.add(wasrunning);
				wasrunning=null;
			}
			//---------------------------------------END OF BLOCKED-----------------------------------------------		
				
				
				
				
			//---------------------------------------HANDLE RUNNING PROCESS-------------------------------------------------
				if(running==null&&!ready.isEmpty())
				{
					running=ready.remove(0); 
					
					process_status[running[0]]="running";
					
					if(remaining_runtime[running[0]]==-1)
					{
					 runtime=scheduler.randomOS(random,running[2]);
					 remaining_runtime[running[0]]=runtime;
					}
					else
					{
						runtime=remaining_runtime[running[0]];
					}
					q=0;
				}
			
				if(running!=null)
				{
					running[3]--;
					CPU_util++;
				}
				
				
				
				
			//--------------------------------CALCULATING WAIT TIME-------------------------------------------
				if(!ready.isEmpty())
				{
					for(int[] proc:ready)
					{
						wait_time[proc[0]]++;
					}
				}
				
				
				
			//---------------------------------------PRINT DETAILS OF CYLCLE-------------------------------------------------
						
				if(verbose_flag==1)
				{
					System.out.print("before cycle "+(time+1)+": ");
					for(int i=0;i<process_list.length;i++)
					{

						System.out.print("   "+process_status[i]+" ");
						if(process_status[i].equals("running"))
						{
							System.out.print(runtime);
						}
						else if(process_status[i].equals("blocked"))
						{	

							System.out.print(iotime.get(i));
						}
						else
						{
							System.out.print(0);
						}
					}
					System.out.println();
				}
				
				//-------------------------------------CALCULATING IO UTILIZATION-------------------------------------------------
				int ioflag=0;
				for(int i=0;i<process_list.length;i++)
				{
					if(process_status[i].equals("blocked"))
					{
						ioflag=1;
					}
				}

				if(ioflag==1)
				{
					IO_util++;	
				}
				

				
				
			//-------------------------------------------HANDLE COMPLETED PROCESSES---------------------------------------------
						
				if(running!=null&&running[3]<=0)
				{
				//	System.out.println("process "+running[0]+" completed");
					completed.add(running);
					process_status[running[0]]="completed";
					complete_time[running[0]]=time+1;
					runtime=0;
					running=null;
					q=0;
					
				}
				else
				{	
					q++;
					runtime--;
				}
				
			//----------------------------INCREMENTING TIME COUNTER------------------------------------------------
				time++;
				
				
				
			//----------------------------HANDLE RUNNING PROCESSES (ADDING TO BLOCKED)--------------------------------------------
				
				if(running!=null&&running[3]>0&&runtime==0)
				{
					blocked.add(running);
					int wait=scheduler.randomOS(random,running[4])+1;
					io_time[running[0]]+=(wait-1);
					
					remaining_runtime[running[0]]=-1;
					iotime.put(running[0],wait );
					process_status[running[0]]="blocked";
					q=0;
					running=null;
					
				}
				
	//--------------------------------PRE EMPTING PROCESS WHEN QUANTUM IS OVER----------------------
				if(running!=null&&q==2)
				{
					wasrunning=running;
					remaining_runtime[running[0]]=runtime;
					process_status[running[0]]="ready";
					running=null;
					q=0;
				}
				
				
			}
			
			
			
			
		//-------------------------------------------PRINT FINAL DETAILS-----------------------------------------------
				
			System.out.println();
			System.out.println();
			for(int i=0;i<process_list.length;i++)
			{
				System.out.println("PROCESS "+i);
				System.out.println();
				System.out.println("(A,B,C,IO) = ("+ process_list[i][1]+","+ process_list[i][2]+","+ process_lengths[i]+","+process_list[i][4]+")");
				System.out.println("Finish time = "+complete_time[i]);
				System.out.println("Turnaround Time = "+(complete_time[i]-process_list[i][1]));
				System.out.println("I/O time = "+io_time[i]);
				System.out.println("Waiting time = "+wait_time[i]);
				System.out.println();
				System.out.println();
				
			}
		
			for(int i=0;i<process_list.length;i++)
			{
				avg_wt+=wait_time[i];
				avg_tt+=(complete_time[i]-process_list[i][1]);
				
			}
			avg_wt=avg_wt/process_list.length;
			avg_tt=avg_tt/process_list.length;
			System.out.println("SUMMARY DATA ");
			System.out.println();
			System.out.println("Finishing Time =" + time);
			System.out.println("CPU Utilization ="+ (double)CPU_util/time);
			System.out.println("I/O Utilization = "+ (double)IO_util/time);
			System.out.println("Throughput = "+ ((double)process_list.length/time)*100);
			System.out.println("Average Turnaround Time = "+avg_tt);
			System.out.println("Average Waiting Time = "+avg_wt);
			System.out.println();
			System.out.println();

		}
		

}
