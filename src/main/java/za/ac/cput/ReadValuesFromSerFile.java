/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 *
 * @author: Hilary Cassidy Nguepi Nangmo_220346887
 */
public class ReadValuesFromSerFile
{
    private final String str= "stakeholder.ser";
    FileWriter fW;
    PrintWriter pW;
    
    FileInputStream fim;
    ObjectInputStream osm;
    
    public void openFile(String fileName)
    {
        try
        {
            fW= new FileWriter(new File(fileName));
            pW= new PrintWriter(fW);
            System.out.println("The "+ fileName +" has been created");
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private ArrayList<Customer> custArray()
    {
        ArrayList<Customer> cust= new ArrayList<>();
        
        try
        {
            fim= new FileInputStream(new File(str));
            osm= new ObjectInputStream(fim);
            
            while (true)
            {
                Object obj= osm.readObject();
                if (obj instanceof Customer)
                {
                    cust.add((Customer)obj);
                }
            }
        }
        catch(EOFException eof)
        {
            
        }
        catch (IOException| ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
       
        finally
        {
            try
            {
                fim.close();
                osm.close();
            }
            catch(IOException e)
                    {
                         e.printStackTrace();
                         System.out.println("error: "+ e.getMessage());
                    }
        }
            
       if (!cust.isEmpty())
       {
           Collections.sort(cust, (Customer cu, Customer cus)-> cu.getStHolderId().compareTo(cus.getStHolderId()));
       }
       return cust;
    }
    
    
    private void writeCusFile()
    {
        String header= "*****************************CUSTOMERS*****************************\n";
        String holder= "%s\t%-20s\t%-10s\t%-10s\t%-10s\n";
        String space= "********************************************************************\n";
        
        try
        {
        pW.print(header);
        pW.printf(holder,"ID","Name","Surname","Date of birth","Age");
        pW.print(space);
        
        for (int i=0; i<custArray().size(); i++)
        {
            pW.printf(holder, custArray().get(i).getStHolderId(), custArray().get(i).getFirstName(),custArray().get(i).getSurName(),date(custArray().get(i).getDateOfBirth()),calAge(custArray().get(i).getDateOfBirth()) );
        }
        
        pW.printf("\nNumber of customers who can rent: %d", canRent());
        pW.printf("\nNumber of customers who cannot rent: %d", denyRent());
        }
        catch(Exception e)
        {
            System.out.println("Error: "+ e.getMessage());
        }
    }
    
    private String date(String dob)
    {
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate pDob= LocalDate.parse(dob);
       
        return pDob.format(formatter);
    }
    
    private int calAge(String dob)
    {
        LocalDate pDob= LocalDate.parse(dob);
        int doy= pDob.getYear();
        ZonedDateTime toDate= ZonedDateTime.now();
        int thisYear= toDate.getYear();
        return thisYear-doy;
    }
    
    private int canRent()
    {
        int canRent=0;
        for(int i=0; i<custArray().size(); i++)
        {
            if(custArray().get(i).getCanRent())
            {
                canRent += 1;
            }
        }
        
        return canRent;
    }
    
    private int denyRent()
    {
        int denyRent=0;
        for(int i=0; i<custArray().size(); i++)
        {
            if(!custArray().get(i).getCanRent())
            {
                denyRent +=1;
            }
        }
        
        return denyRent;
    }
    
    
    
    private ArrayList<Supplier>supArray()
    {
        ArrayList<Supplier>sup=new ArrayList<>();
        try
        {
            fim= new FileInputStream(new File(str));
            osm= new ObjectInputStream(fim);
            
            while (true)
            {
                Object obj= osm.readObject();
                if (obj instanceof Supplier)
                {
                    sup.add((Supplier)obj);
                }
            }
        }
        catch(EOFException eof)
        {
            
        }
        catch (IOException| ClassNotFoundException e)
        {
            e.printStackTrace();
           
        }
       
        finally
        {
            try
            {
                fim.close();
                osm.close();
            }
            catch(IOException e)
                    {
                         e.printStackTrace();
                    }
        }
            
       if (!sup.isEmpty())
       {
           Collections.sort(sup, (Supplier su, Supplier sr)-> su.getName().compareTo(sr.getName()));
       }
       return sup;
    }
    
    private void writeSupFile()
    {
        String header= "*************************SUPPLIERS*************************\n";
        String holder= "%s\t%-20s\t%-10s\t%-10s\n";
        String inLine= "***********************************************************\n";
        
        try
        {
        pW.print(header);
        pW.format(holder,"ID","Name","Prod Type","Description");
        pW.print(inLine);
        
        for (int i=0; i<supArray().size(); i++)
        {
            pW.printf(holder, supArray().get(i).getStHolderId(), supArray().get(i).getName(),supArray().get(i).getProductType(),supArray().get(i).getProductDescription());
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
           
        }
    }
                
    public void closeFile(String fileName)
    {
        try
        {
            fW.close();
            pW.close();
            System.out.println("The "+fileName+" has been closed.");
        }
        catch(IOException e)
                {
                    System.out.println("Error: "+ e.getMessage());
                }
    }
                
    
    
    public static void main(String[] args) 
    {
        ReadValuesFromSerFile val = new ReadValuesFromSerFile();
        
        val.openFile("cusFile.txt");
        val.writeCusFile();
        val.closeFile("cusFile.txt");
        
        val.openFile("supFile.txt");
        val.writeSupFile();
        val.closeFile("supFile.txt");
        
        
    }
    
}
