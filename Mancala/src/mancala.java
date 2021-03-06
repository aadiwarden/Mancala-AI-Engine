import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.Collections;

class theGame
{
	ArrayList<Integer> theState;
			
	public theGame()
	{
		theState = new ArrayList<Integer>();
	}
}

public class mancala 	
{
	private int ourPlayer;
	private int theCut;
	private int eachCount;
	private int modVal;
	private int maxPos;
	private int type;
	private PrintWriter out2;
	private int ansMax = Integer.MIN_VALUE;
	private theGame rootNode = new theGame();
	private theGame finalGreed = new theGame();
	
	public void display(theGame temp)
	{
		for(int i = 0; i <(eachCount*2 + 2); i++)
		{
			System.out.print(temp.theState.get(i));
		}
		System.out.println("");
	}
	
	public int comparePos(int a, int b, int turn)
	{
		int ans = 0;
		
		if(turn == 0)
		{
			if(a <= b)
			{
				ans = 1;
			}
			else
			{
				ans = 2;
			}
		}
		else
		{
			if(a >= b)
			{
				ans = 1;
			}
			else
			{
				ans =  2;
			}
		}
		return ans;
	}
	
	public int evalState(theGame temp)
	{
		int ans = 0;
		
		if(ourPlayer == 1)
		{
			ans = temp.theState.get(eachCount) - temp.theState.get((eachCount*2) + 1);
		}
		else
		{
			ans = temp.theState.get((eachCount*2) + 1) - temp.theState.get(eachCount);
		}
		
		return ans;
			
	}
	
	public int endGame(theGame temp, int turn)
	{
		int i, j, ans = 0, notEndGame1 = 0, notEndGame2 = 0, nextStart;
		
		for(i=turn; i < (turn + eachCount); i++)
		{
			if(temp.theState.get(i) != 0)
			{
				notEndGame1 = 1;
				break;
			}
		}
		
		nextStart = (turn + eachCount + 1) % modVal;
		
		for(j= nextStart; j < (nextStart + eachCount); j++)
		{
			if(temp.theState.get(j) != 0)
			{
				notEndGame2 = 1;
				break;
			}
		}
		
		if(notEndGame1 == 1 && notEndGame2 == 1)
		{
			ans = 0;
		}
		else
		{
			ans = 1;
		}
		
		return ans;
	}
	
	public void removeAllPits(theGame temp, int turn)
	{
		int i, start, sum = 0;
		for(i = turn; i < (turn + eachCount) % modVal; i++)
		{
			sum = sum + temp.theState.get(i);
			temp.theState.set(i, 0);
		}

		temp.theState.set((turn + eachCount) % modVal, temp.theState.get((turn + eachCount) % modVal) + sum);
		
		sum = 0;
		start = (turn + eachCount + 1) % modVal;
		for(i = start; i < (start + eachCount) % modVal; i++)
		{
			sum = sum + temp.theState.get(i);
			temp.theState.set(i, 0);
		}
		
		temp.theState.set((start + eachCount) % modVal, temp.theState.get((start + eachCount) % modVal) + sum);
	}
	
	public String posToString(int pos)
	{
		String temp = "";
		int tempPos = pos; 
				
		if(tempPos > eachCount)
		{
			temp = temp + "A";
			temp = temp + Integer.toString(modVal - tempPos); 
		}
		else
		{
			temp = temp + "B";
			temp = temp + Integer.toString(tempPos + 2);
		}
		
		return temp;
	}
	
	public String intToInfinity(int x)
	{
		if(x == Integer.MIN_VALUE)
			return "-Infinity";
	
		else if(x == Integer.MAX_VALUE)
			return "Infinity";
		
		else
			return Integer.toString(x);
				
	}
	
	// --------------------- Alpha Beta Starts
	
	public int abMaxMove(theGame temp, int dep,int turn, int prevPos, int prevDep, int alpha1, int beta1)
	{
		int maxVal = Integer.MIN_VALUE, valRet, maxEvalPos = 0, calledItself = 0, calledMin = 0;
		int pos ,pleaseCutIt = 0;
		theGame temp1 = new theGame();
		temp1.theState.addAll(temp.theState);

		if(turn == 0)
			pos = 0;
		
		else
			pos = maxPos - 1;
		
		if(dep > theCut)
		{
			System.out.print("THIS SHOULD NEVER GET PRINTED!");
			maxVal = evalState(temp);
		}
		else
		{
			for(int i = 0;i < eachCount; i++)
			{
				if(temp.theState.get(pos) != 0)
				{
					out2.append(posToString(pos) + ",");
					out2.append(Integer.toString(dep) + ",");
				
					calledItself = recGreed(temp,pos,turn,1,dep);
					
					if(endGame(temp,turn) == 1)
					{
			
						valRet = evalState(temp);
						if(calledItself == 0 && dep+1 > theCut)
						{
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
						
						else if(calledItself == 1)
						{
							out2.append("-Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
						
						else
						{
							out2.append("Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
						calledMin = -1;
					}
					else
					{
						if(calledItself == 1)
						{
						
							out2.append("-Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							valRet = abMaxMove(temp,dep,turn,pos,dep,alpha1,beta1);
							calledItself = 0; calledMin = 0;
						}
							
						else
						{		
							if(dep+1 <= theCut)
							{
								out2.append("Infinity,");
								out2.append(intToInfinity(alpha1) + ",");
								out2.append(intToInfinity(beta1) + "\n");
								valRet = abMinMove(temp,dep + 1, (turn + eachCount + 1) % modVal, pos, dep, alpha1, beta1);
							}
							
							else
							{
								valRet = evalState(temp);		
								out2.append(intToInfinity(valRet) + ",");
								out2.append(intToInfinity(alpha1) + ",");
								out2.append(intToInfinity(beta1) + "\n");
							}
				
							calledMin = 1;
						}
					}
					
					if(valRet >= beta1)
						pleaseCutIt = 1;
					else if(valRet > alpha1)
						alpha1 = valRet;
						
					if((valRet == maxVal && comparePos(pos,maxEvalPos,turn) == 1) || valRet > maxVal)
					{
						if(dep == 1 && calledMin == 1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						else if(dep == 1 && calledMin == -1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						
						maxVal = valRet;
						maxEvalPos = pos;
					}
					
					if(prevPos == -1)
					{
						out2.append("root,");
						out2.append("0,");
					}
					else
					{
						out2.append(posToString(prevPos) + ",");
						out2.append(Integer.toString(prevDep) + ",");
					}
					
					out2.append(intToInfinity(maxVal) + ",");
					out2.append(intToInfinity(alpha1) + ",");
					out2.append(intToInfinity(beta1) + "\n");
					
					if(pleaseCutIt == 1)
						break;
				}
					
				if(turn == 0)
					pos++;
				
				else
					pos--;
					
				temp.theState.clear();
				temp.theState.addAll(temp1.theState);				
			}
		}
		return maxVal;
	}
	
	public int abMinMove(theGame temp, int dep, int turn, int prevPos, int prevDep, int alpha1, int beta1)
	{
		int calledItself = 0, minVal = Integer.MAX_VALUE, valRet, minEvalPos =  0;
		int pos, pleaseCutIt = 0;
		theGame temp1 = new theGame();
		temp1.theState.addAll(temp.theState);
		
		if(turn == 0)
			pos = 0;
		
		else
			pos = maxPos - 1;
		
		if(dep > theCut)
		{
			System.out.print("THIS SHOULD NEVER GET PRINTED!");
			minVal = evalState(temp);
		}
		else
		{
			for(int i = 0; i < eachCount; i++)
			{
				if(temp.theState.get(pos) != 0)
				{
					calledItself = recGreed(temp,pos,turn,2,dep);
					
					out2.append(posToString(pos) + ",");
					out2.append(Integer.toString(dep) + ",");
					
					if(endGame(temp,turn) == 1)
					{
			
						valRet = evalState(temp);
						if(calledItself == 0 && dep+1 > theCut)
						{
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
						
						else if(calledItself == 1)
						{
							out2.append("Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
						
						else
						{
							out2.append("-Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + ",");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
						}
					}
					
					
					else
					{
						if(calledItself == 1)
						{
							out2.append("Infinity,");
							out2.append(intToInfinity(alpha1) + ",");
							out2.append(intToInfinity(beta1) + "\n");
							valRet = abMinMove(temp,dep,turn,pos,dep,alpha1,beta1);
							calledItself = 0;
						}
						
						else
						{
							if(dep+1 <= theCut)
							{
								out2.append("-Infinity,");
								out2.append(intToInfinity(alpha1) + ",");
								out2.append(intToInfinity(beta1) + "\n");
								valRet = abMaxMove(temp,dep + 1, (turn + eachCount + 1) % modVal,pos,dep,alpha1,beta1);
								
							}
							
							else
							{
								valRet = evalState(temp);
								out2.append(intToInfinity(valRet) + ",");
								out2.append(intToInfinity(alpha1) + ",");
								out2.append(intToInfinity(beta1) + "\n");
							}
						}
					}
						
					if(valRet <= alpha1)
						pleaseCutIt = 1;
					else if(valRet < beta1)
						beta1 = valRet;
						
					if(valRet < minVal || (valRet == minVal && comparePos(pos,minEvalPos,turn) == 1))
					{
						minVal = valRet;
						minEvalPos = pos;
					}	
					
					out2.append(posToString(prevPos) + ",");	
					out2.append(Integer.toString(prevDep) + ",");
					out2.append(intToInfinity(minVal) + ",");
					out2.append(intToInfinity(alpha1) + ",");
					out2.append(intToInfinity(beta1) + "\n");
					
					if(pleaseCutIt == 1)
						break;
				}
				
				if(turn == 0)
					pos++;
				
				else
					pos--;
				
				temp.theState.clear();
				temp.theState.addAll(temp1.theState);
				
				
				
			}
		}
		return minVal;
	}
	
	public int theABMinimax(theGame temp)
	{
		int turn = 0, ans;
		
		if(ourPlayer == 1)
		{
			turn = 0;
		}
		else if(ourPlayer == 2)
		{
			turn = eachCount + 1;
		}

		ans = abMaxMove(temp,1,turn,-1,1,Integer.MIN_VALUE, Integer.MAX_VALUE);
		return ans;
	}
	
	//---------------------------- Alpha Beta Ends
	
	//----------------- MinMax Starts
	
	public int maxMove(theGame temp, int dep,int turn, int prevPos, int prevDep)
	{
		int maxVal = Integer.MIN_VALUE, valRet, maxEvalPos = 0, calledItself = 0, calledMin = 0;
		int pos;
		theGame temp1 = new theGame();
		temp1.theState.addAll(temp.theState);

		if(turn == 0)
			pos = 0;
		
		else
			pos = maxPos - 1;
		
		if(dep > theCut)
		{
			System.out.print("THIS SHOULD NEVER GET PRINTED!");
			maxVal = evalState(temp);
		}
		else
		{
			for(int i = 0;i < eachCount; i++)
			{
				if(temp.theState.get(pos) != 0)
				{
					out2.append(posToString(pos) + ",");
					out2.append(Integer.toString(dep) + ",");
				
					calledItself = recGreed(temp,pos,turn,1,dep);
					
					if(endGame(temp,turn) == 1)
					{
			
						valRet = evalState(temp);
						if(calledItself == 0 && dep+1 > theCut)
						{
							out2.append(intToInfinity(valRet) + "\n");
						}
						
						else if(calledItself == 1)
						{
							out2.append("-Infinity\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + "\n");
						}
						
						else
						{
							out2.append("Infinity\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + "\n");
						}
						calledMin = -1;
					}
					
					else
					{
						if(calledItself == 1)
						{
							out2.append("-Infinity\n");
							
							valRet = maxMove(temp,dep,turn,pos,dep);
							calledItself = 0; calledMin = 0;
						}
							
						else
						{		
							if(dep+1 <= theCut)
							{
								out2.append("Infinity\n");
								valRet = minMove(temp,dep + 1, (turn + eachCount + 1) % modVal, pos, dep);
							}
							
							else
							{
								valRet = evalState(temp);
								out2.append(intToInfinity(valRet) + "\n");
							}
							calledMin = 1;
						}
					}
						
					if((valRet == maxVal && comparePos(pos,maxEvalPos,turn) == 1) || valRet > maxVal)
					{
						if(dep == 1 && calledMin == 1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						else if(dep == 1 && calledMin == -1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						
						maxVal = valRet;
						maxEvalPos = pos;
					}
					
					if(prevPos == -1)
					{
						out2.append("root,");
						out2.append("0,");
					}
					else
					{
						out2.append(posToString(prevPos) +",");
						out2.append(Integer.toString(prevDep) + ",");
					}
					
					out2.append(intToInfinity(maxVal) + "\n");
				}
					
				if(turn == 0)
					pos++;
				
				else
					pos--;
					
				temp.theState.clear();
				temp.theState.addAll(temp1.theState);
				
			}
		}
		return maxVal;
	}
	
	public int minMove(theGame temp, int dep, int turn, int prevPos, int prevDep)
	{
		int calledItself = 0, minVal = Integer.MAX_VALUE, valRet, minEvalPos =  0;
		int pos;
		theGame temp1 = new theGame();
		temp1.theState.addAll(temp.theState);
		
		if(turn == 0)
			pos = 0;
		
		else
			pos = maxPos - 1;
		
		if(dep > theCut)
		{
			System.out.print("THIS SHOULD NEVER GET PRINTED!");
			minVal = evalState(temp);
		}
		else
		{
			for(int i = 0; i < eachCount; i++)
			{
				if(temp.theState.get(pos) != 0)
				{
					calledItself = recGreed(temp,pos,turn,2,dep);
					
					out2.append(posToString(pos) +",");
					out2.append(Integer.toString(dep) + ",");
					
					if(endGame(temp,turn) == 1)
					{
			
						valRet = evalState(temp);
						if(calledItself == 0 && dep+1 > theCut)
						{
							out2.append(intToInfinity(valRet) + "\n");
						}
						
						else if(calledItself == 1)
						{
							out2.append("Infinity\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + "\n");
						}
						
						else
						{
							out2.append("-Infinity\n");
							out2.append(posToString(pos) + ",");
							out2.append(Integer.toString(dep) + ",");
							out2.append(intToInfinity(valRet) + "\n");
						}

					}
					
					else
					{
						if(calledItself == 1)
						{
							out2.append("Infinity\n");
							
							valRet = minMove(temp,dep,turn,pos,dep);
							calledItself = 0;
						}
						
						else
						{
							if(dep+1 <= theCut)
							{
								out2.append("-Infinity\n");
								valRet = maxMove(temp,dep + 1, (turn + eachCount + 1) % modVal,pos,dep);
								
							}
							
							else
							{
								valRet = evalState(temp);
								out2.append(intToInfinity(valRet) + "\n");
							}
						}
					}
						
					if(valRet < minVal || (valRet == minVal && comparePos(pos,minEvalPos,turn) == 1))
					{
						minVal = valRet;
						minEvalPos = pos;
					}	
					
					out2.append(posToString(prevPos) + ",");	
					out2.append(Integer.toString(prevDep) + ",");
					out2.append(intToInfinity(minVal) + "\n");
				}
				
				if(turn == 0)
					pos++;
				
				else
					pos--;
				
				temp.theState.clear();
				temp.theState.addAll(temp1.theState);
				
				
				
			}
		}
		return minVal;
	}
	
	
	public int theMinimax(theGame temp)
	{
		int turn = 0, ans;
		
		if(ourPlayer == 1)
		{
			turn = 0;
		}
		else if(ourPlayer == 2)
		{
			turn = eachCount + 1;
		}
		
		ans = maxMove(temp,1,turn,-1,1);
		return ans;
	}
	
	//------------------ MiniMax Ends
	
	// -------------------Greedy Starts
	
	public int theGreed(theGame temp, int dep, int turn)
	{
		int maxVal = Integer.MIN_VALUE, valRet, maxEvalPos = 0, calledItself = 0, calledMin = 0;
		int pos = turn;
		theGame temp1 = new theGame();
		temp1.theState.addAll(temp.theState);

		if(turn == 0)
			pos = 0;
		
		else
			pos = maxPos - 1;
		
		if(dep > theCut)
		{
			System.out.print("THIS SHOULD NEVER GET PRINTED!");
			maxVal = evalState(temp);
		}
		else
		{
			for(int i = 0;i < eachCount; i++)
			{
				if(temp.theState.get(pos) != 0)
				{
					calledItself = recGreed(temp,pos,turn,1,dep);
					
					if(calledItself == -1)
					{
						valRet = evalState(temp);
						calledMin = -1;
					}
					
					else
					{
						if(calledItself == 1)
						{
							valRet = maxMove(temp,dep,turn,pos,dep);
							calledItself = 0; calledMin = 0;
						}
							
						else
						{		
							valRet = evalState(temp);
							calledMin = 1;
						}
					}
						
					if((valRet == maxVal && comparePos(pos,maxEvalPos,turn) == 1) || valRet > maxVal)
					{
						if(dep == 1 && calledMin == 1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						else if(dep == 1 && calledMin == -1 && valRet > ansMax)
						{
							ansMax = valRet;
							finalGreed.theState.clear();
							finalGreed.theState.addAll(temp.theState);
						}
						
						maxVal = valRet;
						maxEvalPos = pos;
					}
				}
					
				if(turn == 0)
					pos++;
				
				else
					pos--;
					
				temp.theState.clear();
				temp.theState.addAll(temp1.theState);
				
			}
		}
		return maxVal;
	}
	
	//------------------- Greedy Ends
	
	public int recGreed(theGame temp,int pos,int turn, int minMax, int dep)
	{
		int j, j1, nof, lastEmptyPit = 0, toAdd = 0, calledItself = 0;
		int directlyAcross = 0;
		nof = temp.theState.get(pos);
		temp.theState.set(pos, 0);
		
		j1 = 0;
		for(j = 1; j <= nof ; j++)
		{
			j1++;
			if((pos + j1) % modVal == (turn + maxPos) % modVal)
			{
				j--;
			}
			else
			{
				if(temp.theState.get((pos + j1) % modVal) == 0 && j == nof)
				{
					if(((pos + j1) % modVal < (turn + eachCount)) && (pos + j1) % modVal >= turn)
					{
						lastEmptyPit = 1;
						break;
					}
				}
				
				temp.theState.set((pos + j1) % modVal , temp.theState.get((pos + j1) % modVal) + 1);
			}
		}
		
		
		if(lastEmptyPit == 1) //----------------------Snatch module
		{
			if(ourPlayer == 1)
			{
				directlyAcross = maxPos - ((pos + j1) % modVal) - 1;
			}
			else if(ourPlayer == 2)
			{
				directlyAcross = maxPos - ((pos + j1) % modVal) - 1;
			}
			toAdd = temp.theState.get(directlyAcross) + 1;
			
			temp.theState.set(turn + eachCount, temp.theState.get((turn + eachCount) % modVal) + toAdd);
			temp.theState.set((pos + j1) % modVal, 0);
			temp.theState.set(directlyAcross, 0);
		}
		
		else if((pos + j1) % modVal == (turn + eachCount))   // -------------Extra Chance
		{
			calledItself = 1;
		}
		

		if(endGame(temp,turn) == 1) //---------------------End Game
		{
			removeAllPits(temp, turn);
		}
		
		return calledItself;
	}
	
	public ArrayList<Integer> divideIt(String temp)
	{
		int t1;
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		String temparr[];
		
		temparr = temp.split(" ");
		eachCount = temparr.length;
		
		for(int i = 0; i < eachCount; i++)
		{
			t1 = Integer.parseInt(temparr[i]);
			list1.add(i,t1);
		}
		
		return list1;
		
	}
	
	public void displayOutput(int type) throws FileNotFoundException
	{
		String temp = "";
		int i;
		PrintWriter out1 = new PrintWriter(new FileOutputStream("next_state.txt",true));
		
		if(type == 1 || type == 2 || type == 3)
		{
			for(i = (maxPos-1); i > eachCount; i--)
			{
				temp = temp + finalGreed.theState.get(i);
				if(i != eachCount + 1)
				{
					temp = temp + " ";
				}
			}
			
			out1.append(temp + "\n");
			
			temp = "";
			for(i = 0;i< eachCount; i++)
			{
				temp = temp + finalGreed.theState.get(i);
				if(i != (eachCount - 1))
				{
					temp = temp + " ";
				}
			}
			out1.append(temp + "\n");
			out1.append(Integer.toString(finalGreed.theState.get(maxPos)) + "\n");
			out1.append(Integer.toString(finalGreed.theState.get(eachCount)));
			
		}
		
		out1.close();
	}
	
	
	public void readInput(String theFile) throws NumberFormatException, FileNotFoundException
	{
		int ans = 0, m1, m2;
		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		
		File file1 = new File(theFile);
		PrintWriter out12 = new PrintWriter("next_state.txt");
		out12.close();
		
		PrintWriter out13 = new PrintWriter("traverse_log.txt");
		out13.close();
		
		Scanner ob = new Scanner(file1);
		theGame board = new theGame();
		
		type = Integer.parseInt(ob.nextLine());
		ourPlayer = Integer.parseInt(ob.nextLine());
		theCut = Integer.parseInt(ob.nextLine());
		arr1 = divideIt(ob.nextLine());
		Collections.reverse(arr1);
		arr2 = divideIt(ob.nextLine());
		m1 = Integer.parseInt(ob.nextLine());
		arr1.add(m1);
		m2 = Integer.parseInt(ob.nextLine());
		arr2.add(m2);
		
		modVal = (eachCount*2) + 2;
		maxPos = (eachCount*2) + 1;
		
		board.theState.addAll(arr2);
		board.theState.addAll(arr1);
		
		rootNode.theState.addAll(board.theState);
		
		if(type == 1)
		{
			try 
			{
				theCut = 1;
				out2 = new PrintWriter(new FileOutputStream("traverse_log.txt",true));
				ans = theMinimax(board);
				out2.close();
				PrintWriter out14 = new PrintWriter("traverse_log.txt");
				out14.close();
				displayOutput(type); 
			}
			catch(Exception e)
			{
				ans = 999;
			}
		}
		else if(type == 2)
		{
			try 
			{
				out2 = new PrintWriter(new FileOutputStream("traverse_log.txt",true));
				out2.append("Node,Depth,Value\n");
				out2.append("root,0,-Infinity\n");
				ans = theMinimax(board);
				out2.close();
				displayOutput(type);
			}
			catch(Exception e)
			{
				ans = 999;
			}
		}
		else if(type == 3)
		{
			try 
			{
				out2 = new PrintWriter(new FileOutputStream("traverse_log.txt",true));
				out2.append("Node,Depth,Value,Alpha,Beta\n");
				out2.append("root,0,-Infinity,-Infinity,Infinity\n");
				ans = theABMinimax(board);
				out2.close();
				displayOutput(type); 
			}
			catch(Exception e)
			{
				ans = 999;
			}
		}
		//System.out.println("Answer : " + ans);
		ob.close();
	}
	
	
	public static void main(String args[]) throws FileNotFoundException
	{
		mancala mancalaOb = new mancala();
		mancalaOb.readInput(args[1]);
	}
}