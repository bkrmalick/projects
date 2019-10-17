import java.io.IOException;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import java.util.Scanner;

import java.util.Arrays; 

public class Counter
{
    public static void main (String [] args)
    {
       Scanner s=new Scanner(System.in); 
        
       System.out.println("Please enter the URL of the caplin blog you want the letters counter for:");
       String URL=s.nextLine().trim();
      
        
       try //try to connect to given URL
       {
           Document blog=Jsoup.connect(URL).get();
           
           Elements data=blog.select("div.entry-content"); //select <div class="entry-content"> from the webpage
           
           Elements heading=blog.select(".entry-title"); //select  <h1 class="entry-title"> from webpage
           
           
           String blogText="";
           for(Element h:heading) //loop through selected tags
           {
  
               blogText=blogText.trim() + h.wholeText(); //select all text from the tag and its children
               
           }
           for(Element tag:data) //loop through selected tags
           {
               printHeading("Data fetched from URL:");
               blogText=blogText.trim() + tag.wholeText(); //select all text from the tag and its children
               
           }
           System.out.println(blogText=blogText.replace("\n"," ").replace(" "," ").replace("."," ").replace(","," ").trim()); 
           
           
           printHeading("Data Statistics: ");
           
           printStats(blogText);
           
       }
       catch (IOException e)
       {
           System.out.println("Error connecting to URL. See stack trace below."); 
           e.printStackTrace(); 
       }

    }
    
    private static void printHeading(String s)
    {
       System.out.println("\n#######################");
       System.out.println(s);
       System.out.println("#######################");
    }
    
    private static void printStats(String s)
    { 
        double total=0;
        int n=0;
        
        
        String[] split=s.split(" ");
        
        for(String word:split)
        {
            if(word.trim().length() >= 1 && !word.trim().isEmpty())
            {
                if(word.trim().charAt(0) == 's' || word.trim().charAt(0) == 'S')
                {   
                    n++; 
                }

                total++; 
            }
        }
        
        
        
        System.out.println("No. of words beginning with s: " + n);
        System.out.println("Total no. of words           : " + (int) total);
        System.out.println("% of words beginning with s  : " + ((n/total )*100) + "%" );
    }
}