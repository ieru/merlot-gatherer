/*
=============
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package merlotcrawler;

import java.io.*;

/**
 *
 * @author Victor Apellaniz
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //If a you need to use a proxy
        //System.setProperty("http.proxyHost", "proxy");
        //System.setProperty("http.proxyPort", "8080");

        InputStreamReader inp = new InputStreamReader(System.in) ;
        BufferedReader br = new BufferedReader(inp);

        System.out.println("Choose Execution : ");
        System.out.println("1. Complete (Obtain Materials + Obtain Metrics)");
        System.out.println("2. Obtain Materials");
        System.out.println("3. Obtain Metrics");
        System.out.println("4. Update Users");
        System.out.print(">");

        String str="";
        try{
            str = br.readLine();
            if(str.equals("1"))
            {
                System.out.println("Complete Execution");
                Crawler crawler = new Crawler();
                crawler.CrawlMaterials();
                crawler.CrawlMetrics();
                System.exit(0);
            }else if(str.equals("2"))
            {
                System.out.println("Obtain Materials");
                Crawler crawler = new Crawler();
                crawler.CrawlMaterials();
                System.exit(0);
            }else if(str.equals("3"))
            {
                System.out.println("Obtain Metrics");
                Crawler crawler = new Crawler();
                crawler.CrawlMetrics();
                System.exit(0);
            }else if(str.equals("4"))
            {
                System.out.println("Update Users");
                Crawler crawler = new Crawler();
                crawler.UpdateUsers();
                System.exit(0);
            }
        }catch(Exception e){
            System.out.println("Error! Exiting program + " + e.getMessage());
            //System.exit(0);
        }
        
    }//main void's end
}//main class' end
