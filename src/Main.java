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
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        String executionMethod = "";
        String databaseName = "";
        String databaseUser = "";
        String databasePass = "";

        if (args.length > 0)
        {
            executionMethod = args[0];
            if(!executionMethod.equals("-total") && !executionMethod.equals("-onlyMaterials") && !executionMethod.equals("-onlyMetrics") && !executionMethod.equals("-updateUsers"))
            {
                System.out.println("Error! Incorrect execution method");
                System.out.println("Execution methods available:");
                System.out.println("-total: obtain materials + obtain metrics");
                System.out.println("-onlyMaterials: obtain materials");
                System.out.println("-onlyMetrics: obtain materials");
                System.out.println("-updateUsers: obtain materials");
                System.exit(1);
            }
        }
        else
        {
            System.out.println("Error! You must indicate an execution method by parameter");
            System.out.println("Execution methods available:");
            System.out.println("-total: obtain materials + obtain metrics");
            System.out.println("-onlyMaterials: obtain materials");
            System.out.println("-onlyMetrics: obtain materials");
            System.out.println("-updateUsers: update users' database");
            System.exit(1);
        }

        //We read the config file
        try{
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("./config/config.xml"));
            doc.getDocumentElement().normalize();
            NodeList databaseConfig = doc.getElementsByTagName("database");
            Node databaseNode = databaseConfig.item(0);
            Element databaseElement = (Element)databaseNode;
            NodeList nameList = databaseElement.getElementsByTagName("Name");
            Element nameElement = (Element)nameList.item(0);
            NodeList textNameList = nameElement.getChildNodes();
            databaseName=(((Node)textNameList.item(0)).getNodeValue().trim());

            NodeList userList = databaseElement.getElementsByTagName("User");
            Element userElement = (Element)userList.item(0);
            NodeList textUserList = userElement.getChildNodes();
            databaseUser=(((Node)textUserList.item(0)).getNodeValue().trim());

            NodeList passList = databaseElement.getElementsByTagName("Pass");
            Element passElement = (Element)passList.item(0);
            NodeList textPassList = passElement.getChildNodes();
            databasePass=(((Node)textPassList.item(0)).getNodeValue().trim());
        }
        catch(Exception e)
        {
            System.out.println("Error reading config.xml file: " + e.getMessage());
            System.exit(1);
        }

        if(executionMethod.equals("-total"))
        {
            System.out.println("Complete Execution");
            Crawler crawler = new Crawler(databaseName,databaseUser,databasePass);
            crawler.CrawlMaterials();
            crawler.CrawlMetrics();
            System.exit(0);
        }else if(executionMethod.equals("-onlyMaterials"))
        {
            System.out.println("Obtain Materials");
            Crawler crawler = new Crawler(databaseName,databaseUser,databasePass);
            crawler.CrawlMaterials();
            System.exit(0);
        }else if(executionMethod.equals("-onlyMetrics"))
        {
            System.out.println("Obtain Metrics");
            Crawler crawler = new Crawler(databaseName,databaseUser,databasePass);
            crawler.CrawlMetrics();
            System.exit(0);
        }else if(executionMethod.equals("-updateUsers"))
        {
            System.out.println("Update Users");
            Crawler crawler = new Crawler(databaseName,databaseUser,databasePass);
            crawler.UpdateUsers();
            System.exit(0);
        }
    }//main void's end
}//main class' end
