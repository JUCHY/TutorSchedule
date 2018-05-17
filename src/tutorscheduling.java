import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class tutorscheduling {
public static int[][] studentschedule = new int[5][13];
	public static void main(String[] args) throws InvalidExpressionException {
		String reflink = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("Type 0 to open custom file (uses filepath). Type a number from 1-8 to open preset files: ");
		String filenum = sc.next();
		if(filenum.equals("0")) {
			System.out.println("Enter reference link: ");
			filenum = sc.next();
			reflink = filenum;
			sc.close();
		}
		else {
			reflink = getfile(filenum);
			sc.close();
		}
		String studentdata = "";
		try {
			Scanner df = new Scanner(new File(reflink));
			while(df.hasNextLine()) {
				studentdata = studentdata+df.nextLine();
			}
			df.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(studentdata.length()>0) {
			SinglyLinkedList<String> data = isMatched2(studentdata);
			while(!data.isEmpty()) {
				String checkdata = data.removeFirst();
				if(isMatched(checkdata)) {
					returnData(checkdata);
				}
			}
			int[] datapoint = findMax(studentschedule);
			String day = getDay(datapoint[0]);
			String time = getTime(datapoint[1]);
			System.out.println("Based off the schedule below, the best time to hold a tutoring session would be: "+
			day + " @ "+ time );
			String day2 = getDay(datapoint[2]);
			String time2 = getTime(datapoint[3]);
			System.out.println("The second best time would be "+day2 + " @ " + time2);
			printschedule(studentschedule);
			System.out.println("*Note: Schedule shows number of students who can attend a specific scheduling time*");
		}
		
	}
	//Provides link to file
	public static String getfile(String x) {
		String fileref = "";
		try {
			int file = Integer.parseInt(x);
			switch(file) {
			case 1: fileref = "project3_inputs\\proj3_set1.txt";
			break;
			case 2: fileref = "project3_inputs\\proj3_set2.txt";
			break;
			case 3: fileref = "project3_inputs\\proj3_set3.txt";
			break;
			case 4: fileref = "project3_inputs\\proj3_set4.txt";
			break;
			case 5: fileref = "project3_inputs\\proj3_set5.txt";
			break;
			case 6: fileref = "project3_inputs\\custom_set1.txt";
			break;
			case 7: fileref = "project3_inputs\\custom_set2.txt";
			break;
			case 8: fileref = "project3_inputs\\custom_set3.txt";
			break;
			
			default: System.out.println("File does not exist");
			break;
			}
			return fileref;
		}
		catch(Exception e) {
			System.out.println("Please enter a number from 0-8");
			return "Invalid filenum";
		}
		
		
	}
	//gets data from string, and places it into schedulearray
	public static void returnData(String x) {
		String data1 = "";
		String data2 = "";
		final String opening = "<[";
		final String closing = ">]";
		char[] iterabletext = x.toCharArray();
		LinkedStack<Character> databuffer2 = new LinkedStack<Character>();
		SinglyLinkedList<String> data = new SinglyLinkedList<String>();
	
		for(int i = 0; i < iterabletext.length; i++) {
			char curr = iterabletext[i];
			if(opening.indexOf(curr)==0) {
				databuffer2.push(curr);
				data1 = data1 + iterabletext[i+1];
				i = i +1;
				data.addLast(data1);
				data1 = "";
			}
			if(opening.indexOf(curr)==1) {
				databuffer2.push(curr);
				boolean goon = true;
				while(goon) {
					curr = iterabletext[i+1];
					i = i+1;
					if(opening.indexOf(curr)!=-1 || closing.indexOf(curr)!= -1) {
						goon = false;
				}
					else {
					data2 = data2+curr;
					}
			}
			}
			if(closing.indexOf(curr)!=-1) {
				if(databuffer2.isEmpty()) {
					data2 = "";
				}
				else if (closing.indexOf(curr)!= opening.indexOf(databuffer2.pop())) {
					System.out.println("This is an ill formed expression: "+ data2);
					data2 = "";
				}
				else {
					if(data2.length()!=0){
						data.addLast(data2);
					}
					data2 = "";
					//if stack is empty after pop, and index of curr is equal to pop
					if (databuffer2.isEmpty()){
						if(closing.indexOf(curr)==0) {
							placedata(data);
						}
				}	
			}
		}
	}
	
}
		
	//places data in schedule array
	static void placedata(SinglyLinkedList<String> data){
		String currentdata = data.toString();
		try {
			String value1 = data.removeFirst();
			String value2 = data.removeFirst();
			int num1 = datacase(value1);
			int num2 = datacase(value2);
			if(num1 == -1 || num2 == -1) {
				throw new InvalidExpressionException(value1 + " or " + value2 + " contains inalid data");
			}
			int num3 = -1;
			while(!data.isEmpty()) {
				String value3 = data.removeFirst();
				try {
					int currentnum = datacase(value3);
					if(currentnum == -1) {
						throw new InvalidExpressionException(value3 + " contains invalid data");
					}
					if(currentnum>num3){
						num3 = currentnum;			
					}
					if(num3<num2) {
						num3 = num2;
						num2 = currentnum;
					}
				}
				catch(InvalidExpressionException e){
					e.printStackTrace();
					
				}
				
			}
			if(num3 == -1) {
				studentschedule[num1][num2] = studentschedule[num1][num2]+1;
			
			}
			else {
			while(num2<num3) {
				studentschedule[num1][num2] = studentschedule[num1][num2]+1;
				num2 = num2+1;
			}
			}
		}
		catch(InvalidExpressionException e){
			
			System.out.println(currentdata + " contains invalid data");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	static int datacase(String x) {
		int toreturn = 0;
		String check = x;
		switch(check) {
		case "M": toreturn = 0;
		break;
		case "T": toreturn = 1;
		break;
		case "W": toreturn = 2;
		break;
		case "H": toreturn = 3;
		break;
		case "F": toreturn = 4;
		break;
		case "0900": toreturn = 0;
		break;
		case "1000": toreturn = 1;
		break;
		case "1100": toreturn = 2;
		break;
		case "1200": toreturn = 3;
		break;
		case "1300": toreturn = 4;
		break;
		case "1400": toreturn = 5;
		break;
		case "1500": toreturn = 6;
		break;
		case "1600": toreturn = 7;
		break;
		case "1700": toreturn = 8;
		break;
		case "1800": toreturn = 9;
		break;
		case "1900": toreturn = 10;
		break;
		case "2000": toreturn = 11;
		break;
		case "2100": toreturn = 12;
		break;	
		default:  toreturn = -1;
		}
		return toreturn;
		
	}
	//Helps determine if sets of statement using )( are valid for set of multiple students
	public static SinglyLinkedList<String> isMatched2(String expression) throws InvalidExpressionException{
		final String opening = "(";
		final String closing = ")";
		String data = "";
		Stack<Character> buffer2 = new LinkedStack<>();
		SinglyLinkedList<String> sectioneddata = new SinglyLinkedList<String>();
		for(char c : expression.toCharArray()) {
			data =  data + c;
			if(opening.indexOf(c)!=-1) {
				while(!buffer2.isEmpty()) {
					buffer2.pop();
					System.out.println("This is an ill formed expression: "+ data);
					data = "" +c;
				}
				buffer2.push(c);
		}
			else if(closing.indexOf(c)!=-1) {
				if(buffer2.isEmpty()) {
					System.out.println("This is an ill formed expression "+ data);
					data = "" +c;
				}
				else if (closing.indexOf(c)!= opening.indexOf(buffer2.pop())) {
					System.out.println("This is an ill formed expression: "+ data);
					data = "" +c;
				}
				else {
					//if stack is empty after pop, and index of c is equal to pop
					if (buffer2.isEmpty()){
						sectioneddata.addLast(data);
						data = "";
					}	
				}
			}
		
	}
		if (buffer2.isEmpty()){
			return sectioneddata;
		}
		else {
			System.out.println("This is an ill formed expression: " +expression);
			throw new InvalidExpressionException("Bad File");
		}
	}
	//checks if current statement is valid for single student
	public static boolean isMatched(String expression){
		final String opening = "(<[";
		final String closing = ")>]";
		Stack<Character> buffer = new LinkedStack<>();
		for(char c : expression.toCharArray()) {
			if(opening.indexOf(c)!=-1) {
				buffer.push(c);
		}
			else if(closing.indexOf(c)!=-1) {
				if(buffer.isEmpty()) {
					System.out.println("This is an ill formed expression "+expression);
					return false;
				}
				if (closing.indexOf(c)!= opening.indexOf(buffer.pop())) {
					System.out.println("This is an ill formed expression: "+expression);
					return false;
				}
			}
		
	}
		if (buffer.isEmpty()){
			return true;
		}
		else {
			System.out.println("This an ill formed expression" +expression);
			return false;
		}
	}
	public static SinglyLinkedList<String> section(String x) {
		String currentString = "";
		SinglyLinkedList<String> sectioneddata = new SinglyLinkedList<String>();
		for(char character : x.toCharArray()) {
			currentString = currentString + character;
			if(character == ')') {
				sectioneddata.addLast(currentString);
				currentString = "";	
			}
		}
		return sectioneddata;
		
	}
	//finds the first and second best time for tutoring
	public static int[] findMax(int[][] studentschedule) {
		int currmax = 0;
		int prevmax1 = 0;
		int prevmax2 = 0;
		int index1 = 0;
		int index2 = 0;
		for(int i = 0; i < studentschedule.length; i ++) {
			for(int k = 0; k<studentschedule[i].length; k++) {
				if(studentschedule[i][k]>currmax) {
					prevmax1 = index1;
					prevmax2 = index2;
					currmax = studentschedule[i][k];
					index1 = i;
					index2 = k;
				}
				else if(studentschedule[i][k]==currmax && (prevmax1 == index1) && (prevmax2 == index2)) {
					prevmax1 = i;
					prevmax2 = k;
				}
				else if(studentschedule[i][k]==currmax && studentschedule[i][k]>studentschedule[prevmax1][prevmax2]) {
					prevmax1 = i;
					prevmax2 = k;
				}
			}
		}
		int[] results = new int[]{index1, index2, prevmax1, prevmax2};
		return results;
		
	}
	//gets day value from array index
	public static String getDay(int num){
		String toreturn = "";
		int check = num;
		switch(check) {
		case 0: toreturn = "Monday";
		break;
		case 1: toreturn = "Tuesday";
		break;
		case 2 : toreturn = "Wednesday";
		break;
		case 3 : toreturn = "Thursday";
		break;
		case 4 : toreturn = "Friday";
		break;
		}
		return toreturn;
		
	}
	//gets time value from array index
	public static String getTime(int num) {
		String toreturn = "";
		int check = num;
		switch(check) {
			case 0: toreturn = "0900";
			break;
			case 1: toreturn = "1000";
			break;
			case 2: toreturn = "1100";
			break;
			case 3: toreturn = "1200";
			break;
			case 4: toreturn = "1300";
			break;
			case 5: toreturn = "1400";
			break;
			case 6: toreturn = "1500";
			break;
			case 7: toreturn = "1600";
			break;
			case 8: toreturn = "1700";
			break;
			case 9: toreturn = "1800";
			break;
			case 10: toreturn = "1900";
			break;
			case 11: toreturn = "2000";
			break;
			case 12: toreturn = "2100";
			break;	
		}
		return toreturn;
	
	}
	//prints out schedule
	public static void printschedule(int[][] studentschedule) {
		System.out.print("Time");
		for(int i = 0; i<5; i++) {
			System.out.printf("%10s", getDay(i));
		}
		System.out.println();
		int num = 0;
		for(int k = 0; k < studentschedule[num].length; k++) {
			System.out.printf("%-10s", getTime(k));
			while(num<5) {
				System.out.printf("%-10s",studentschedule[num][k]);
				num++;
			}
			num = 0;
			System.out.println();
		}
		
	}
}