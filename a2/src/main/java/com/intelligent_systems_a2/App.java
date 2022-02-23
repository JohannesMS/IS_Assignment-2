package com.intelligent_systems_a2;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.print.event.PrintJobListener;


/**
 * Hello world!
 *
 */
public class App 
{
    static String filePath = new File("").getAbsolutePath() + "\\a2\\src\\csv\\";
    static File folder = new File(filePath);
    static File[] files = new File[folder.listFiles().length];
    
    short keyBulb = 10;
    short keyEmptyField = 20;
    short keyEmptyFieldIlluminated = 21;
    short keyEmptyFieldImplaceable = 22;
    short keyEmptyFieldIlluminatedAndImplaceable = 23;
    short keyWallBlank = -1;
    short keyNumberedWall_0 = 0;
    short keyNumberedWall_1 = 1;
    short keyNumberedWall_2 = 2;
    short keyNumberedWall_3 = 3;
    short keyNumberedWall_4 = 4;
    
    short xBoundary = 14;
    short yBoundary = 14;
    short[][] gameboard = new short[xBoundary][yBoundary];
    short[][] checkpoint = new short[xBoundary][yBoundary]; 

    short numberedWallLocations[][];
    short emptyFieldLocations[][];
    
    public static void main( String[] args )
    {
        
        /*
        Check for placeable fields
            if(!isOutOfBound(tempX+1, tempY) && isEmptyField(tempX+1, tempY) && isIlluminated(tempX+1, tempY)){}
            if(!isOutOfBound(tempX-1, tempY) && isEmptyField(tempX-1, tempY) && isIlluminated(tempX-1, tempY)){}
            if(!isOutOfBound(tempX, tempY+1) && isEmptyField(tempX, tempY+1) && isIlluminated(tempX, tempY+1)){}
            if(!isOutOfBound(tempX, tempY-1) && isEmptyField(tempX, tempY-1) && isIlluminated(tempX, tempY-1)){}

        */
        //Spielbrett als zweidimensionales Array
        //Birne = 10
        //Leeres Feld = 20, Leeres Feld beleuchtet = 21, Leeres Feld implaceable = 22
        //Mauer blank = -1, Mauer Nummeriert = 0-4
        
        App test = new App();
        test.setFilepaths();
        test.setGameboard(1);
        test.printGameboard();
        
        

        //TODO
        //Es gibt nur einfach und zweifach beleuchtet, dreifach beleuchten ist technisch gar nicht möglich wegen der Prüfung beim platzieren
        //Wenn ich also eine Birne entferne muss die Zahl der beleuchtungen geprüft werden, die Beleuchtung darf nicht kleiner als 20 werden, die Range ist also 20-22
        //
        //Beleuchtungsfunktion erstellen
        //Trivial Solver
        //Birne entfernen
        
        
    }

    public boolean setBulb(short x, short y){
        gameboard[x][y] = 10;
        return false;
    }

    public short numNotIlluminated(){
        short illuminatedCounter = 0;
        for(short x = 0; x < gameboard.length; x++){
            for(short y = 0; y < gameboard[x].length; y++){
                if(isIlluminated(x, y)){illuminatedCounter++;}
            }
        }
        return illuminatedCounter;
    }

    public boolean wallConstraintValidation(){
        boolean violation = false;
        int tempX;
        int tempY;
        int bulbCounter = 0;
        for(int i = 0; i < numberedWallLocations.length; i++){
            tempX = numberedWallLocations[i][0];
            tempY = numberedWallLocations[i][1];
            if(!isOutOfBound(tempX+1, tempY) && isBulb(tempX+1, tempY)){bulbCounter++;}
            if(!isOutOfBound(tempX-1, tempY) && isBulb(tempX-1, tempY)){bulbCounter++;}
            if(!isOutOfBound(tempX, tempY+1) && isBulb(tempX, tempY+1)){bulbCounter++;}
            if(!isOutOfBound(tempX, tempY-1) && isBulb(tempX, tempY-1)){bulbCounter++;}
            if(gameboard[tempX][tempY] != bulbCounter){violation = true;}
        }
        return violation;
    }

    public boolean validateSolution(){
        if(numNotIlluminated() == 0 || wallConstraintValidation() == false){
            return true;
        }
        else{return false;}
    }

    public boolean isOutOfBound(int x, int y){
        if(x<0 || y<0 || x>xBoundary || y>yBoundary){return true;}
        else{return false;}
    }
    public boolean isBulb(int x, int y){
        if(gameboard[x][y] == keyBulb){return true;}
        else{return false;}
    }
    public boolean isEmptyField(int x, int y){
        if(gameboard[x][y] >= keyEmptyField && gameboard[x][y] < 30){return true;}
        else{return false;}
    }
    public boolean isIlluminated(int x, int y){
        if(gameboard[x][y] == keyEmptyFieldIlluminated || gameboard[x][y] == keyEmptyFieldIlluminatedAndImplaceable){return true;}
        else{return false;}
    }
    public boolean isImplaceable(int x, int y){
        if(gameboard[x][y] == keyEmptyFieldImplaceable || gameboard[x][y] == keyEmptyFieldIlluminatedAndImplaceable){return true;}
        else{return false;}
    }   
    public boolean isWall(int x, int y){
        if(gameboard[x][y] >= keyWallBlank && gameboard[x][y] < keyNumberedWall_4){return true;}
        else{return false;}
    }
    public boolean isNumberedWall(int x, int y){
        if(gameboard[x][y] >= keyNumberedWall_0 && gameboard[x][y] < keyNumberedWall_4){return true;}
        else{return false;}
    }

    public void setFilepaths(){
        for(int i = 0; i<files.length; i++){
            files[i] = folder.listFiles()[i];
        }
    }
    public void setGameboard(int csv_number){
        xBoundary = (short) getGameboardDimension(csv_number);
        yBoundary = (short) getGameboardDimension(csv_number);
    
        try {
            Scanner scanner = new Scanner(files[csv_number]);
            String[] rows = new String[xBoundary];
            int column = 0;
            //rows = sc.nextLine().split("\\;", -1);
            while(scanner.hasNextLine()){
                rows = scanner.nextLine().split("\\;",-1);
                for(int i = 0; i<rows.length; i++){
                    if(rows[i].equals("")){
                        gameboard[i][column] = 20;
                    }
                    else if(rows[i].equals("x")){
                        gameboard[i][column] = -1;
                    }
                    else if(rows[i].equals("0")){
                        gameboard[i][column] = 0;
                    }
                    else if(rows[i].equals("1")){
                        gameboard[i][column] = 1;
                    }
                    else if(rows[i].equals("2")){
                        gameboard[i][column] = 2;
                    }
                    else if(rows[i].equals("3")){
                        gameboard[i][column] = 3;
                    }
                    else if(rows[i].equals("4")){
                        gameboard[i][column] = 4;
                    }
                }
                System.out.println("");
                column++;
            }
            scanner.close();
        }
            catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getGameboardDimension(int csv_number){
        int dimension = 0;
        try {
            Scanner scanner = new Scanner(files[csv_number]);
            String[] rows;
            rows = scanner.nextLine().split("\\;",-1);
            dimension = rows.length;
            System.out.println(dimension);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dimension;
    }
    
    public void printGameboard(){
        for(int y = 0; y<gameboard.length; y++){
            for(int x = 0; x<gameboard[y].length; x++){
                System.out.print(gameboard[x][y] + " ");
            }
            System.out.println("");
        }
    }

    public short setCandidateLocations(){
        List<short[]> tempList = new ArrayList<>();
        short[] tempArray = new short[2];
        short candidateCounter = 0;
        return 1;
    }

    public short setEmptyFieldLocations(){
        List<short[]> tempList = new ArrayList<>();
        short[] tempArray = new short[2];
        short emptyFieldCounter = 0;

        for(short x = 0; x < gameboard.length; x++){
            for(short y = 0; y < gameboard[x].length; y++){
                if(isEmptyField(x,y) && !isIlluminated(x, y) && !isImplaceable(x, y)){
                    emptyFieldCounter++;
                    tempArray[0] = x;
                    tempArray[1] = y;
                    tempList.add(tempArray);
                }
            }
        }
        emptyFieldLocations = new short[emptyFieldCounter][2];
        for(int i = 0; i < tempList.size(); i++){
            emptyFieldLocations[i][0] = tempList.get(i)[0];
            emptyFieldLocations[i][1] = tempList.get(i)[1];
        }
        return emptyFieldCounter;
        
    }

    public short setNumberedWallLocations(){
        List<short[]> tempList = new ArrayList<>();
        short[] tempArray = new short[2];
        short numberedWallCounter = 0;
        for(short x = 0; x < gameboard.length; x++){
            for(short y = 0; y < gameboard[x].length; y++){
                if(isNumberedWall(x, y)){
                    numberedWallCounter++;
                    tempArray[0] = x;
                    tempArray[1] = y;
                    tempList.add(tempArray);
                }
            }
        }
        numberedWallLocations = new short[numberedWallCounter][2];
        for(int i = 0; i < tempList.size(); i++){
            numberedWallLocations[i][0] = tempList.get(i)[0];
            numberedWallLocations[i][1] = tempList.get(i)[1];
        }
        return numberedWallCounter;
    }

    public void setCheckpoint(){checkpoint = gameboard;}
    public void getCheckpoint(){gameboard = checkpoint;}
}
