 /*
Waad Alharbi
Sarah Aljohani
Ghada Alsulami
Reema Almalki
 */
package AutoMemory;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
 
public class MemoryManagementSystem { 
 
        public static int OFFSET=128;//2^8 bytes page and Frame size (frameSize=pageSize)
        public static int  page_Table_Size=256; 
        public static int framesSize=256;
        public static int Physical_Memory_Size=OFFSET*framesSize;
        
         
    public static void main(String[] args) throws FileNotFoundException {
        
          System.out.println("\n\n\t\t\t\t\t Welcome to our translator System ");
         int recentlyUsedPageNum=0;
         int frameNumber=0;
         int randomFrameNumber=0;
         int translateToPhyAdd=0;
         Queue<Integer> addRecentlyUsedFrameNum = new LinkedList<>() ;
         Queue<Integer> addRecentlyUsedPageNum = new LinkedList<>() ;
         int recentlyUsedFrameNum;
          
        //Create File for reading adresses 
        String FileName="addresses.txt";
        File logicalAddressFile=new File(FileName);
        
        //Create File to read value to be store in memory
        String FileName2="correct.txt";
        File correctFile=new File(FileName2);
        
        //File Found checking
        if (!logicalAddressFile.exists()) {
            System.out.println("We couldn't find file with name "+logicalAddressFile.getName());
            System.exit(0);
        }
        
         if (!correctFile.exists()) {
            System.out.println("We couldn't find file with name "+correctFile.getName());
            System.exit(0);
        }
        

        Scanner inputAddress= new Scanner(logicalAddressFile); 
        Scanner inputValue= new Scanner(correctFile);
        
        

        //Create page table and initilized with -1
        int pageTable[]=new int[page_Table_Size]; //0-255
        for (int i = 0; i < pageTable.length; i++) {
           pageTable[i]=-1; 
        }
        
        //My Test array: work as mapping  between page number and value
        int [] myTestDataKey=new int[100];
        int [] myTestDataValaue=new int[100];
        
        for (int i = 0; i < 100; i++) {
            myTestDataKey[i]=inputAddress.nextInt();
            myTestDataValaue[i]=inputValue.nextByte();
        }
        
   
        
        //Create Physical memory
        int [] PhysicalMem=new int[Physical_Memory_Size];
        for (int i = 0; i < PhysicalMem.length; i++) {
            PhysicalMem[i]=-1;
        }
       
        //--------------------------Part 1--------------------------------------- 
        
        //R1:Storing a value (signed byte) in physical memory given its logical address
        
        //System.out.println("Here we do five cases from a to e: ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        System.out.println("R1: Storing a value (signed byte) in physical memory given its logical address\n");
        for (int i = 0; i < 100; i++) {  
         //int= 4 bytes= 0000 0000 |0000 0000|0000 0000|0000 0000
         //                byte3   |   byte2 |   byte1 |   byte0
         //i.e int=5 0000 0000 |0000 0000|0100 0100|0000 0101
         //Case 1:            
         int logicalAddress=myTestDataKey[i]; 
         int offset=0xff&logicalAddress;
         int pageNumber=(0xff00&logicalAddress)>>8;
         
         /*We have two conditions to assign the value into Physical memory, Check
         if the page table hasn't the desired page number, so we need to generate new frame number randomly 
         and culculate the appropriate place in memory, else we can extract the frame number from page table*/
         if (pageTable[pageNumber]==-1) {
                  randomFrameNumber=GenerteRandomFrameNumber(); 
                  translateToPhyAdd=offset+(randomFrameNumber*OFFSET);   
                  while(PhysicalMem[translateToPhyAdd]!=-1) {
                  randomFrameNumber=GenerteRandomFrameNumber(); 
                  translateToPhyAdd=offset+(randomFrameNumber*OFFSET); 
                  }
                   pageTable[pageNumber]=randomFrameNumber;
                   addRecentlyUsedFrameNum.add(randomFrameNumber);
                   addRecentlyUsedPageNum.add(pageNumber);
            }else{
        
                   frameNumber= pageTable[pageNumber];
                   translateToPhyAdd=offset+(frameNumber*OFFSET);           
               
         }                  PhysicalMem[translateToPhyAdd]=myTestDataValaue[i];
   
                
            }
        
              
                 
                 // we are make a model to test the previous step to make sure that
                 //we correctly store value in the physical address corresponding to its logical address 
                 System.out.println("The model checks the stored physsical memory value that corresponding to the logical addresses: ");
                 System.out.println("Logical Address\t\t Page #\t\t offset\t\tframe #\t\t value  (Model answer)");
                 for (int i = 0; i < 100; i++) {
            
                 int logicalAddress=myTestDataKey[i];
                 int offset=0xff&logicalAddress;
                 int pageNumber=(0xff00&logicalAddress)>>8;
                 int frameNumber2=pageTable[pageNumber];
                 int translateToPhyAdd2=offset+(frameNumber2*OFFSET);  
                 int storedvalue = PhysicalMem[translateToPhyAdd2];
          
                     if (storedvalue==myTestDataValaue[i]) {
 
                     System.out.println(logicalAddress+"\t\t\t "+pageNumber+"\t\t  "+offset+"\t\t "+frameNumber2+"\t\t  "+storedvalue);
                         
                     }else{
                         
                     System.out.println("Error is Occured.");break;
                     }     
                 }
                 
                 System.out.println();
                 System.out.println("\n");
               //  System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
                 System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                 //R 2: Retrieving a value (signed byte) from physical memory given its logical address
                 System.out.println("The result of retrieving the values of 5 logical addresses:");
                 System.out.println("Logical Address\t\t Page #\t\t offset\t\tframe #\t\tvalue   same as model answer");
                 for (int i = 0; i < 5; i++) {
            
                 int RandomIndex=0+(int)(Math.random()*((99-0)+1));
                 int logicalAddress=myTestDataKey[RandomIndex];
                 int offset=0xff&logicalAddress;
                 int pageNumber=(0xff00&logicalAddress)>>8;
                 int frameNumber2=pageTable[pageNumber];
                 int translateToPhyAdd2=offset+(frameNumber2*OFFSET);  
                 int storedvalue = PhysicalMem[translateToPhyAdd2];
                 System.out.println(logicalAddress+"\t\t\t "+pageNumber+"\t\t  "+offset+"\t\t "+frameNumber2+"\t\t  "+storedvalue+"\t\tYes");
                 
                 
                 }
                 System.out.println();
                // System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
                 System.out.println("-----------------------------------------------------------------------------------------------------------------------");
       
                 //Reset random page to be -1
                 for (int i = 0; i < 30; i++) {
                 int RandomIndex=0+(int)(Math.random()*((99-0)+1));
                 int logicalAddress=myTestDataKey[RandomIndex];
                 int pageNumber=(0xff00&logicalAddress)>>8;
                 pageTable[pageNumber]=-1;
     
                 }
                  
                 
                System.out.println("Counting number of page faults for a string of 80 randomly chosen logical addresses: ");
                int pageFaultNum=0;//paage fault counter
                int pageHitCount=0;//page hit counter
                
                //We choose 80 random logical addresses to see if there are any page fault occures
                //when request some frame number from pages
                for (int i = 0; i <80; i++) {
               int RandomIndex=0+(int)(Math.random()*((99-0)+1));
                 int logicalAddress=myTestDataKey[RandomIndex];
                 System.out.print(logicalAddress+",");
                 int offset=0xff&logicalAddress;
                 int pageNumber=(0xff00&logicalAddress)>>8;
                 int frameNumberCompar=pageTable[pageNumber]; 
                    if (frameNumberCompar==-1) {
                       ++pageFaultNum; 
                    
                    }else{
                      ++pageHitCount;
                    
                    }
                
                }
                System.out.println();
                System.out.println("Number of page faults: "+pageFaultNum);
                System.out.println("Number of page Hits:"+pageHitCount);
                System.out.println();
              //  System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
               System.out.println("-----------------------------------------------------------------------------------------------------------------------");
                
                
                
    //----------------------Part 2(Page replacement(bonus))-------------------------------     
          //Generate new 133 logical address range from 0-132     
          int [] newLogicalAdressesP=new int[133];
          int [] newLogicalAdressesD=new int[133];
         
         for (int i = 0; i < newLogicalAdressesP.length; i++) {    
         newLogicalAdressesP[i]=i;//i.e 0,1,2,3,4,5,6..
         newLogicalAdressesD[i]=0+(int)(Math.random()*(256-0));// Frame size =256;
         }
       
      
              //My new Test array
        int [] NewMyTestDataKeyP=new int[133];
        int [] NewMyTestDataKeyD=new int[133];
        int [] NewMyTestDataValaue=new int[133];
        
        for (int i = 0; i < NewMyTestDataKeyP.length; i++) {
            int randomIndex=0+(int)(Math.random()*(132-0));
            NewMyTestDataKeyP[i]= newLogicalAdressesP[randomIndex];
            NewMyTestDataKeyD[i]= newLogicalAdressesD[randomIndex];
            NewMyTestDataValaue[i]=-128+(int)(Math.random()*(127-(-128)));
            
            
           
        }
          
        
        //Store the value in corresponding to its new logical address
        for (int i = 0; i < 128; i++) {
 
                 int pageNum=NewMyTestDataKeyP[i];
                 int offset=NewMyTestDataKeyD[i];
                 int val= NewMyTestDataValaue[i];
                  
                 int translateToPhyAdd2=offset+(pageNum*OFFSET);  
                 PhysicalMem[translateToPhyAdd2]=val;
                 
   
        
        }
                
        System.out.println("Running page replacement routine:");
       // System.out.println("Logical\t\tAddress new Page #\t\t# victim page\t\treused frame #\t\tStored Value"); 
        System.out.println("Logical\t\tAddress new Page #\t\t# victim page\t\treused frame #\t\t"); 
        for (int i = 128; i < 133; i++) {
         
                 recentlyUsedFrameNum=addRecentlyUsedFrameNum.peek();
                 recentlyUsedPageNum=addRecentlyUsedPageNum.peek();
                 addRecentlyUsedPageNum.poll();
                 addRecentlyUsedFrameNum.poll();
                 pageTable[recentlyUsedPageNum]=-1;
               
                  
                 int pageNum=NewMyTestDataKeyP[i];
                 pageTable[pageNum]=recentlyUsedFrameNum;
                 int offset=NewMyTestDataKeyD[i];
                 int val= NewMyTestDataValaue[i];
                  
                 int translateToPhyAdd2=offset+(recentlyUsedFrameNum*OFFSET);  
                 PhysicalMem[translateToPhyAdd2]=val;
  
               //  System.out.println(pageNum+""+offset+"\t\t\t"+pageNum+"\t\t\t\t"+recentlyUsedPageNum+"\t\t "+recentlyUsedFrameNum+"\t\t\t "+val); 
                 System.out.println(pageNum+""+offset+"\t\t\t"+pageNum+"\t\t\t\t"+recentlyUsedPageNum+"\t\t "+recentlyUsedFrameNum+"\t\t\t "); 
         
         }
         System.out.println();
        // System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
          System.out.println("-----------------------------------------------------------------------------------------------------------------------");           
          System.out.println("The result of retrieving the values of 5 logical addresses:");       
          System.out.println("Logical Address\t\tPage #\t\toffset\t\tframe #\t\tvalue \t\tsame as model answer");   
            for (int i = 128; i < 133; i++) {

                     int pageNum=NewMyTestDataKeyP[i];
                     int offset=NewMyTestDataKeyD[i];
                     int val= NewMyTestDataValaue[i];
                     int frame= pageTable[pageNum]; 
                 
                     int translateToPhyAdd2=offset+(frame*OFFSET);  
                     int physicalMemVal=PhysicalMem[translateToPhyAdd2];

                     if (val==physicalMemVal) {

                      System.out.println(pageNum+""+offset+"\t\t\t "+pageNum+"\t \t"+offset+"\t\t"+frame+"\t\t "+val+"\t\t\t"+"Yes");

                     }else{
                         System.out.println("Erroris occured. ");break;
                     }



            } 
        
        
            System.out.println("Page Replacement Sucessfully Done. ");
            //System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
     
        System.out.println("\n\n\t\t\t\t\tThanks for using our translator");
    }
    
   
    
    
    
    public static int GenerteRandomFrameNumber(){
    return 0+(int)(Math.random()*(255-0));
    
    }
    
    
    public static int GenerteRandomLogicalAddress(){
    return 0+(int)(Math.random()*((79-0)+1));
    
    }
        
    
}
