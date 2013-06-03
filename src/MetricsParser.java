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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeSet;
import net.htmlparser.jericho.Config;
//import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

/**
 * Gets all the metric's data
 * 
 * @author Víctor Apellániz
 */
public class MetricsParser {

    /**
     * Constructor
     */
    public MetricsParser() {
    }

    /**
     * Gets the source code
     * @param location
     * @return Source object of the location
     */
    public Source getSource(String location) {
        Source source = null;
        MicrosoftTagTypes.register(); //Recognition of Microsoft special Tags
        PHPTagTypes.register(); //Recognition of PHP special Tags
        MasonTagTypes.register(); //Recognition of Mason special Tags
        try {
            URL url = new URL(location);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(10000); //20 secs timeout
            urlConnection.setReadTimeout(10000); //20 secs timeout
            urlConnection.connect();
            source = new Source(urlConnection);
        } catch (ConnectException ce) {
            System.out.println("Connection error: " + ce.getMessage());
        } catch (FileNotFoundException fe) {
//            System.out.println(fe.getMessage());
        } catch (MalformedURLException ue) {
//            System.out.println(ue.getMessage());
        } catch (UnknownHostException he) {
//            System.out.println("Unknown Host Exception: " + he.getMessage());
            source = null;
        } catch (IllegalArgumentException iae) {
//            System.out.println("illegal argument exception " + iae.getLocalizedMessage());
//            source = null;
        } catch (Exception e) {
//            System.out.println("SourceException " + e.getLocalizedMessage());
            source = null;
        }
        return source;
    }

    /**
     * Gets the tree size of a tree
     * @param tree
     * @return int tree size
     * @throws SQLException
     */
    public int getTreeSize(TreeSet tree) throws SQLException {
        int nLinks = 0;
        try {
            nLinks = tree.size();
//            System.out.println("Number of imagesTree of " + locations.size());
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return nLinks;
    }

    /**
     * Get a link list of a specific location
     * @param location
     * @return linked list of links in location
     * @throws SQLException
     */
    public LinkedList<String> getLinkList(String location) throws SQLException {
        Config.LoggerProvider = LoggerProvider.DISABLED;
        LinkedList<String> links = new LinkedList<String>();
        try {
            
            //System.out.println("location="+location);
            Source source = this.getSource(location);
            if (source != null) {
                List<net.htmlparser.jericho.Element> linkList = source.getAllElements("a");
//            System.out.println("Numero imagesTree: " + imagesList.size());
                for (net.htmlparser.jericho.Element link : linkList) {
                    String path = link.toString().substring(link.toString().indexOf("\"") + 1, link.toString().length());
                    path = path.substring(0, path.indexOf("\""));
                    String fixedLink = this.fixLink(location, path);
                    links.add(fixedLink);
                }
            }
        } catch (Exception e) {
//            System.out.println("getLinkList" + e.getMessage());
        }
        return links;
    }
    
    /**
     * Gets the treeSet of a location
     * @param location
     * @return location's treeSet
     * @throws SQLException
     */
    public TreeSet<String> getTreeSite(String location) throws SQLException {
        Config.LoggerProvider = LoggerProvider.DISABLED;
        TreeSet<String> linksTree = new TreeSet();
        String path = null;
        try {
//            System.out.println("LOCATION " + locationPages);
            Source source = this.getSource(location);
            if (source != null) {
                List<net.htmlparser.jericho.Element> linkList = source.getAllElements("a");
//            System.out.println("TreeSize " + imagesList.size());
                linksTree.add(location);
                if (!linkList.isEmpty()) {
                    for (net.htmlparser.jericho.Element link : linkList) {
//                    System.out.println("link " + link.toString());
                        if (link.toString().contains("\"")) {
                            path = link.toString().substring(link.toString().indexOf("\"") + 1, link.toString().length());
                            path = path.substring(0, path.indexOf("\""));
                        } else {
                            path = link.toString().substring(link.toString().indexOf("<a href=") + 8, link.toString().length());
                            path = path.substring(0, path.indexOf(">"));
                        }
                        String fixedLink = fixLink(location, path);
                        //If depends on the parent web, we get all the links inside
                        if (fixedLink.startsWith(location.substring(0, location.lastIndexOf("/")))) {
                            linksTree.addAll(getLinkList(fixedLink));
                        }
                        linksTree.add(fixedLink);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("getUniqueLinksList Exception " + e.getMessage());
        }
        return linksTree;
    }

    /**
     * Gets the unique links tree
     * @param pagesTree
     * @return Treeset unique links tree of pagesTree
     * @throws SQLException
     */
    public TreeSet<String> getUniqueLinksTree(TreeSet pagesTree) throws SQLException {
        TreeSet<String> linksTree = new TreeSet();
        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
//                System.out.print("linked " + linked);
                linksTree.addAll(this.getLinkList(linked));
                System.out.print(">");
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return linksTree;
    }

    /**
     * Gets the links tree
     * @param pagesTree
     * @return LinkedList of links from pagesTree
     * @throws SQLException
     */
    public LinkedList<String> getLinksTree(TreeSet pagesTree) throws SQLException {
        LinkedList<String> linksList = new LinkedList();
        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
//                System.out.print("linked " + linked);
                linksList.addAll(getLinkList(linked));
                System.out.print(">");
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return linksList;
    }

    /**
     * Checks if the link is internal
     * @param location
     * @param link
     * @return is internal or not
     */
    public boolean isInternalLink(String location, String link) {
        boolean isInternal = false;
        try {
            String locationTemp = location.substring(location.indexOf("/") + 2); //Delete http://
            String locationRoot = "";
            if (locationTemp.contains("/")) {
                locationRoot = locationTemp.substring(0, locationTemp.indexOf("/"));
            } else {
                locationRoot = locationTemp;
            }
            locationRoot = "http://" + locationRoot;
            if (link.startsWith(locationRoot) || !link.startsWith("http://")) { //Consider mail address and ftp like internal
                isInternal = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isInternal;
    }

    /**
     * Cleans a link
     * @param link
     * @return String link cleaned
     */
    public String cleanLink(String link) {
        String cleanedLink = null;
        String auxLink;
        try {
//            System.out.println("link "+link);
            auxLink = link.substring(link.indexOf("\"") + 1);
//            System.out.println("auxlink "+auxLink);
            cleanedLink = auxLink.substring(0, auxLink.indexOf("\""));
//            System.out.println("cleaned " + cleanedLink);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            cleanedLink = link;
        }
        return cleanedLink;
    }

    /**
     * Counts the number of unique internal links
     * @param location
     * @param uniqueLinksList
     * @return int number of unique internal links
     * @throws SQLException
     */
    public int getUniqueIntenalLinksNumber(String location, TreeSet<String> uniqueLinksList) throws SQLException {
        int nLinks = 0;
        Config.LoggerProvider = LoggerProvider.DISABLED;
        try {
            for (String link : uniqueLinksList) {
//                System.out.println("link " + link + " location " + location);
                if (this.isInternalLink(location, link)) {
                    nLinks++;
//                    System.out.println("Interno");
                }
            }
        } catch (Exception e) {
            System.out.println("unique internal " + e.getMessage());
        }
        return nLinks;
    }

    /**
     * Count the number of unique external links
     * @param location
     * @param uniqueLinksList
     * @return number of unique external links
     * @throws SQLException
     */
    public int getUniqueExternalLinksNumber(String location, TreeSet<String> uniqueLinksList) throws SQLException {
        Config.LoggerProvider = LoggerProvider.DISABLED;
        int nLinks = 0;
        try {
            for (String link : uniqueLinksList) {
//                System.out.println("link " + link + " location " + location);
                if (!this.isInternalLink(location, link)) {
                    nLinks++;
//                    System.out.println("Externo");
                }
            }
        } catch (Exception e) {
            System.out.println("unique external " + e.getMessage());
        }
        return nLinks;
    }

    /**
     * Count the internal links number
     * @param location
     * @param LinksList
     * @return internal links number
     * @throws SQLException
     */
    public int getIntenalLinksNumber(String location, List<String> LinksList) throws SQLException {
        Config.LoggerProvider = LoggerProvider.DISABLED;
        int nLinks = 0;
        try {
            for (String link : LinksList) {
//                System.out.println("link " + link + " location " + location);
                if (this.isInternalLink(location, link)) {
                    nLinks++;
//                    System.out.println("Interno");
                }
            }
        } catch (Exception e) {
            System.out.println("internal " + e.getMessage());
        }
        return nLinks;
    }

    /**
     * Counts the external links number
     * @param location
     * @param LinksList
     * @return external links number
     * @throws SQLException
     */
    public int getExternalLinksNumber(String location, List<String> LinksList) throws SQLException {
        Config.LoggerProvider = LoggerProvider.DISABLED;
        int nLinks = 0;
        try {
            for (String link : LinksList) {
//                System.out.println("link " + link + " location " + location);
                if (!this.isInternalLink(location, link)) {
                    nLinks++;
//                    System.out.println("Externo");
                }
            }
        } catch (Exception e) {
            System.out.println("external " + e.getMessage());
        }
        return nLinks;
    }

    /**
     * Gets a tree of the location pages
     * @param location
     * @param locations
     * @return TreeSet of the location pages
     * @throws SQLException
     */
    public TreeSet getLocationPages(String location, TreeSet locations) throws SQLException {
        TreeSet pages = new TreeSet();
        if (!location.equals("")) {
            String locationPath = location.substring(0, location.lastIndexOf("/")); //Gets only the pàth of the locationPages, not the file
//            System.out.println("locationPath " + locationPath);
            if (locationPath.equals("http:/") || locationPath.equals("http://")) { //If location is a first domain site
                locationPath = location;
            }
            String locationTemp = location.substring(location.indexOf("/") + 2); //Delete http://
            String locationRoot = "";
            try {
                if (locationTemp.contains("/")) {
                    locationRoot = locationTemp.substring(0, locationTemp.indexOf("/"));
                } else {
                    locationRoot = locationTemp;
                }
                locationRoot = "http://" + locationRoot;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                pages.add(location);
                for (Iterator iter = locations.iterator(); iter.hasNext();) {
                    String linked = (String) iter.next();
                    if (linked.startsWith(locationPath) && (!linked.contains("#")) && (!linked.contains("id=")) && (!linked.contains("../")) && (!linked.contains("./"))) {
                        if (linked.endsWith(".html") || (linked.endsWith(".htm")) || (linked.endsWith(".asp")) || (linked.contains(".php")) || (linked.endsWith(".aspx")) || (linked.endsWith(".anim")) || (linked.endsWith(".pl"))) {
                            pages.add(linked);
                            System.out.println("Pages of " + location + " : " + linked);
                        }
                    }
                }
//            System.out.println("Pages number " + pages.size());
            } catch (Exception e) {
//            System.out.println(e.getMessage());
            }
        }
        return pages;
    }

    /**
     * Fix a specific link format
     * @param location
     * @param imgSrc
     * @return String link fixed
     * @throws SQLException
     */
    public String fixLink(String location, String imgSrc) throws SQLException {
        String link = null;
        try {
            if (imgSrc.startsWith("http://") || imgSrc.startsWith("ftp://") || imgSrc.startsWith("mailto:")) {
                link = imgSrc;
            } else {
                location = location.substring(0, location.lastIndexOf("/"));
                if (imgSrc.startsWith("../../")) {
                    imgSrc = imgSrc.substring(6);
                }
                if (imgSrc.startsWith("/")) {
                    imgSrc = imgSrc.substring(1);
                }
                if (imgSrc.startsWith("../")) {
                    imgSrc = imgSrc.substring(3);
                }
                if (imgSrc.startsWith("./")) {
                    imgSrc = imgSrc.substring(2);
                }
                link = location + "/" + imgSrc;
            }
//            System.out.println("LINK " + image);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return link;
    }

    /**
     * Counts the number of images from a source
     * @param source
     * @return number of images
     */
    public int getImgNumber(Source source) {
        int nImgs = 0;
        try {
            List imgList = source.getAllElements("img");
            nImgs = imgList.size();
//            System.out.println("Number of images " + imgList.size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nImgs;
    }


    /**
     * Counts the number of images from a location TreeSet
     * @param locationTree
     * @return number of images
     * @throws SQLException
     */
    public int getImageNumber(TreeSet locationTree) throws SQLException {
        int totalImages = 0;
        int elementImages = 0;
        try {
            for (Iterator iter = locationTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementImages = getImgNumber(getSource(linked));
                totalImages += elementImages;
                System.out.print(">");
            }
//            System.out.println("Number of imagesTree of " + imagesList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalImages;
    }

    /**
     * Gets the size of the objects from a source
     * @param source
     * @return objects size
     */
    public int getObjectSize(Source source) {
        int objectSize = 0;

        try {
            objectSize = source.length();
//            System.out.println("object: "+objectSize);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
            objectSize = 0;
        }
        return objectSize;
    }

    /**
     * Gets the HTML bytes from a location's tree
     * @param locationTree
     * @return HTMLByte of the location's tree
     * @throws SQLException
     */
    public int getIHtmlBytes(TreeSet locationTree) throws SQLException {
        int totalBytes = 0;
        int elementbytes = 0;

        try {
            for (Iterator iter = locationTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementbytes = getObjectSize(getSource(linked));
                totalBytes += elementbytes;
                System.out.print(">");
            }
//            System.out.println("Number of imagesTree of " + imagesList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
            elementbytes = 0;
        }
        return totalBytes;
    }
    
    /**
     * Gets the images tree of a location's tree
     * @param locationPages
     * @return images tree of the location
     * @throws SQLException
     */
    public TreeSet getImagesTree(TreeSet locationPages) throws SQLException {
        TreeSet imagesTree = new TreeSet();
        try {
            for (Iterator iter = locationPages.iterator(); iter.hasNext();) {
                String location = (String) iter.next();
//                System.out.println("location " + location);
                Source source = getSource(location);
                if (source != null) {
                    List<net.htmlparser.jericho.Element> imgList = source.getAllElements("img");
                    for (net.htmlparser.jericho.Element img : imgList) {
//                    System.out.println("imglist " + imgList.size());
//                    System.out.println("img: " + img.toString());
                        String imagen = img.getAttributeValue("src");
//                    System.out.println("imagenscr: " + imagen);
                        imagesTree.add(imagen);
                    }
                }
            }

        } catch (Exception e) {
//            System.out.println("getimagestree " + e.getMessage());
        }
        return imagesTree;
    }

    /**
     * Gets the images size
     * @param location
     * @param imagesTree
     * @return images size
     * @throws SQLException
     */
    public int getImagesSizeN(String location, TreeSet imagesTree) throws SQLException {
        int totalSize = 0;
        int elementSize = 0;
        String src;
        String path = null;
        String tempLocation = null;
        try {
            for (Iterator iter = imagesTree.iterator(); iter.hasNext();) {
                src = iter.next().toString();
//                System.out.println("src " + src);
                path = this.fixLink(location, src);
//                System.out.println("path " + path);
                elementSize = getObjectSize(getSource(path));
//                System.out.println("tamaño " + elementSize);
                if (elementSize == 0) {
                    tempLocation = location.substring(0, location.lastIndexOf("/"));
                    path = fixLink(tempLocation, src);
//                    System.out.println("path " + path);
                    elementSize = this.getObjectSize(getSource(path));
//                    System.out.println("tamaño " + elementSize);
                }
                if (elementSize == 0) {
                    tempLocation = tempLocation.substring(0, tempLocation.lastIndexOf("/"));
                    path = fixLink(tempLocation, src);
//                    System.out.println("path " + path);
                    elementSize = this.getObjectSize(getSource(path));
//                    System.out.println("tamaño " + elementSize);
                }
                if (elementSize == 0) {
                    String tempLocation2 = tempLocation.substring(0, tempLocation.lastIndexOf("/"));
                    path = fixLink(tempLocation2, src);
//                    System.out.println("path " + path);
                    elementSize = this.getObjectSize(getSource(path));
//                    System.out.println("tamaño " + elementSize);
                }
                System.out.print(">");
                totalSize = totalSize + elementSize;
//                System.out.println("total: "+ totalSize);
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            elementSize = 0;
        }
        return totalSize;
    }

    /**
     * Gets the source of an image
     * @param dirtyLink
     * @return String image's source
     */
    public String getImageSrc(String dirtyLink) {
        String imageSrc = null, aux;
        try {
            if (dirtyLink.startsWith("<media xmlns")) {
                aux = dirtyLink.substring(dirtyLink.indexOf("src=\"") + 5);
                imageSrc = aux.substring(0, aux.indexOf("\""));
            }
            if (dirtyLink.startsWith("<media id=")) {
                aux = dirtyLink.substring(dirtyLink.indexOf("image src=\"") + 11);
                imageSrc = aux.substring(0, aux.indexOf("\""));
            }
//            System.out.println("imageSrc " + imageSrc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imageSrc;
    }

    /**
     * 
     * @param location
     * @return object Size
     */
    public int getObjectSizeAlternative(String location) {
        int objectSize = 0;
        MicrosoftTagTypes.register(); //Recognition of Microsoft special Tags
        PHPTagTypes.register(); //Recognition of PHP special Tags
        MasonTagTypes.register(); //Recognition of Mason special Tags
        try {
            URL url = new URL(location);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(10000); //10 secs timeout
            urlConnection.setReadTimeout(10000); //10 secs timeout
            urlConnection.connect();
            objectSize = urlConnection.getContentLength();
//            System.out.println(objectSize);
        } catch (ConnectException ce) {
            System.out.println("Connection error: " + ce.getMessage());
        } catch (FileNotFoundException fe) {
//            System.out.println(fe.getMessage());
        } catch (MalformedURLException ue) {
//            System.out.println(ue.getMessage());
        } catch (UnknownHostException he) {
//            System.out.println("Unknown Host Exception: " + he.getMessage());
        } catch (IOException ioe) {
//            System.out.println(ioe.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            objectSize = 0;
        }
        return objectSize;
    }

    /**
     * Counts the number of scripts
     * @param source
     * @return number of scripts
     */
    public int getScriptNumber(Source source) {
        int nScripts = 0;

        try {
            List scriptList = source.getAllElements("script");
            nScripts = scriptList.size();
//            System.out.println("Number of scripts " + scriptList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return nScripts;
    }

    /**
     * Counts the number of scripts
     * @param pagesTree
     * @return number of scripts
     * @throws SQLException
     */
    public int getScriptNumber(TreeSet pagesTree) throws SQLException {
        int totalScriptsNumber = 0;
        int elementScriptNumber = 0;

        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementScriptNumber = getScriptNumber(getSource(linked));
                totalScriptsNumber += elementScriptNumber;
                System.out.print(">");
            }
//            System.out.println("Number of imagesTree of " + imagesList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalScriptsNumber;
    }


    /**
     * Counts the number of applets
     * @param source
     * @return number of applets
     */
    public int getAppletNumber(Source source) {
        int nApplets = 0;

        try {
            List appletList = source.getAllElements("applet");
            nApplets = appletList.size();
//            System.out.println("Number of applets " + appletList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return nApplets;
    }

    /**
     * Counts the number of applets
     * @param pagesTree
     * @return number of applets
     * @throws SQLException
     */
    public int getAppletNumber(TreeSet pagesTree) throws SQLException {
        int totalAppletsNumber = 0;
        int elementAppletsNumber = 0;

        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementAppletsNumber = getAppletNumber(getSource(linked));
                totalAppletsNumber += elementAppletsNumber;
                System.out.print(">");
            }
//            System.out.println("Number of applets of " + totalAppletNumber);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalAppletsNumber;
    }

    /**
     * Counts displayed words
     * @param source
     * @return number of displayed words
     */
    public int displayedWordCount(Source source) {
        int nWords = 0;

        try {
            TextExtractor text = source.getTextExtractor();
            StringTokenizer words = new StringTokenizer(text.toString());
            nWords = words.countTokens();
//            System.out.println("Number of words: " + nWords);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return nWords;
    }


    /**
     * Counts the displayed words
     * @param locationTree
     * @return number of displayed words
     * @throws SQLException
     */
    public int getDisplayedWords(TreeSet locationTree) throws SQLException {
        int totalWords = 0;
        int elementWords = 0;

        try {
            for (Iterator iter = locationTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementWords = displayedWordCount(getSource(linked));
                totalWords += elementWords;
                System.out.print(">");
            }
//            System.out.println("Number of imagesTree of " + imagesList.size());

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalWords;
    }

    /**
     * Count links words
     * @param source
     * @return number of link words
     */
    public int linkWordsCount(Source source) {
        int nWords = 0;
        int totalWords = 0;

        try {
            List linkList = source.getAllElements("a");
            for (Iterator i = linkList.iterator(); i.hasNext();) {
                net.htmlparser.jericho.Element linkElement = (net.htmlparser.jericho.Element) i.next();
                String href = linkElement.getTextExtractor().toString();
//                System.out.println(href);
                StringTokenizer words = new StringTokenizer(href);
                nWords = words.countTokens();
//                System.out.println(nWords);
                totalWords += nWords;
//                System.out.println(totalWords);
            }

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalWords;
    }

     /**
     * count links words
     * @param locationTree
     * @return number of links words
     * @throws SQLException
     */
    public int getLinkWords(TreeSet locationTree) throws SQLException {
        int totalWords = 0;
        int elementWords = 0;
        try {
            for (Iterator iter = locationTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementWords = this.linkWordsCount(getSource(linked));
                totalWords += elementWords;
                System.out.print(">");
            }

        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalWords;
    }

    /**
     * Counts files for download
     * @param uniqueLinksList
     * @return number of files for download
     * @throws SQLException
     */
    public int getFilesForDownload(TreeSet uniqueLinksList) throws SQLException {
        int totalFiles = 0;
        try {
            for (Iterator iter = uniqueLinksList.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                if (linked.endsWith(".pdf") || linked.endsWith(".exe") || linked.endsWith(".doc") || linked.endsWith(".xls") || linked.endsWith(".odt") || linked.endsWith(".zip") || linked.endsWith(".rar") || linked.endsWith(".ppt") || linked.endsWith(".tar") || linked.endsWith(".tar.gz") || linked.endsWith(".tar.bz2") || linked.endsWith(".txt") || linked.endsWith(".java") || linked.endsWith(".pl")) {
                    totalFiles++;
                }
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalFiles;
    }

      /**
     * Counts the audio files
     * @param uniqueLinksList
     * @return number of audio files
     * @throws SQLException
     */
    public int getAudioFiles(TreeSet uniqueLinksList) throws SQLException {
        int totalFiles = 0;
        try {
            for (Iterator iter = uniqueLinksList.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                if (linked.endsWith(".mp3") || linked.endsWith(".wav") || linked.endsWith(".midi") || linked.endsWith(".ra")) {
                    totalFiles++;
                }
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalFiles;
    }

    /**
     * Counts the video files
     * @param uniqueLinksList
     * @return video files number
     * @throws SQLException
     */
    public int getVideoFiles(TreeSet uniqueLinksList) throws SQLException {
        int totalFiles = 0;
        try {
            for (Iterator iter = uniqueLinksList.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                if (linked.endsWith(".mp4") || linked.endsWith(".avi") || linked.endsWith(".mov") || linked.endsWith(".swf") || linked.endsWith(".flv") || linked.endsWith(".wmv") || linked.endsWith(".mpg") || linked.endsWith(".mpeg") || linked.endsWith(".movie")) {
                    totalFiles++;
                }
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalFiles;
    }

    /**
     * Reduces the decimals of a float
     * @param entrada
     * @return float with only 1 decimal
     */
    public float reducirDecimales(float entrada) {
        
        float tmpd = entrada * 100;
        
        int tmpi = (int) tmpd;
        
        tmpd = tmpd - tmpi;
        
        if (tmpd >= 0.5) {
            tmpi++;
        }
        
        tmpd = (float) tmpi;
        
        tmpd = tmpd / 100;
        return tmpd;
    }
    
    /**
     * Count the number of links errors
     * @param links
     * @return number of links errors
     */
    public int getLinkErrors(TreeSet links)
    {
        int linkErrors=0;
        //Source source=null;
        Iterator it=links.iterator();
        while(it.hasNext())
        {
            String path=(String) it.next();
            //System.out.println("Path="+path);
            if(!path.contains("mailto:"))
            {
                try {
                    URL url = new URL(path);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setConnectTimeout(10000); //20 secs timeout
                    urlConnection.setReadTimeout(10000); //20 secs timeout
                    urlConnection.connect();
                    //source = new Source(urlConnection);
                    } catch (ConnectException ce) {
                        //System.out.println("ConnectException");
                        linkErrors++;
                    } catch (FileNotFoundException fe) {
                        //System.out.println("FileNotFoundException");
                        linkErrors++;
                    } catch (UnknownHostException he) {
                        //System.out.println("UnknownHostException");
                        linkErrors++;
                    } catch (Exception e) {
                        //System.out.println("Error!!!");
                    }
            }
        }
        //source = null;
        return linkErrors;
    }
    /**
     * Get only the text from a location
     * @param location
     * @return only the text from location
     */
    private String getTextFromWeb(String location)
    {
        String sourceUrlString=location;
        String text="";
        if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
	PHPTagTypes.register();
	PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
	MasonTagTypes.register();
        try{
            Source source=new Source(new URL(sourceUrlString));
            // Call fullSequentialParse manually as most of the source will be parsed.
            source.fullSequentialParse();
            TextExtractor textExtractor=new TextExtractor(source) {
            };
            text=textExtractor.setIncludeAttributes(true).toString();
            
        }catch(Exception e)
        {
            //System.out.println("(Web doesn't respond) " + e.getMessage());
        }
        return text;
    }
    
    /**
     * Counts words,sentences,words per sentence,large words
     * and the the sum of words per sentece (for future calculations)
     * @param input
     * @return array[0]:number of words;array[1]:number of large words
     * @return array[2]:number of sentences;array[3]:words per sentence sum
     */
    private int[] countWordsSentences(String input)
    {
        int wordCount = 0;
        int sentenceCount=0;
        int wordsPerSentence=0;
        int sumWordsPerSentenceCount=0;
        int countLargeWords=0;
        input=input.substring(0, input.lastIndexOf("."))+".";
        
        StringTokenizer t = new StringTokenizer(input,".");

	while(t.hasMoreTokens())
	{
            sentenceCount++;
            wordsPerSentence=0;
            String sentence = t.nextToken();
            StringTokenizer t2 = new StringTokenizer(sentence);
            while(t2.hasMoreTokens())
            {
                wordsPerSentence++;
                wordCount++;
                String word = t2.nextToken();
                if(word.length()>6)
                {
                    countLargeWords++;
                }
                //System.out.println("Word: "+word);
            }
            //System.out.println("wordsPerSentence="+wordsPerSentence);
            sumWordsPerSentenceCount+=wordsPerSentence;
            //System.out.println("Sentence: " + sentence);
	}
        int[] returnArray = new int[4];
        returnArray[0]=wordCount;
        returnArray[1]=countLargeWords;
        returnArray[2]=sentenceCount;
        returnArray[3]=sumWordsPerSentenceCount;

        return returnArray;
    }

    /**
     * Calculates the gunning fog index of a website
     * @param locationTree
     * @return float gunning fog index
     * @throws SQLException
     */
    public float getGunningFog(TreeSet locationTree) throws SQLException {
        float averageWordsPerSencence = 0;
        float averageLargeWords = 0;
        int totalWords=0;
        int totalLargeWords=0;
        int totalSentences=0;
        int totalSumWordsPerSentence=0;

        try {
            for (Iterator iter = locationTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                
                String webText=getTextFromWeb(linked);
                if(!webText.equals(""))
                {
                    int[] wordsSentences=countWordsSentences(webText);
                    totalWords+=wordsSentences[0];
                    totalLargeWords+=wordsSentences[1];
                    totalSentences+=wordsSentences[2];
                    totalSumWordsPerSentence+=wordsSentences[3];
                }
            }
        } catch (Exception e) {
        }
        averageWordsPerSencence=totalSumWordsPerSentence/totalSentences;
        averageLargeWords=totalLargeWords/totalWords;
        
        return reducirDecimales((float)((averageWordsPerSencence + averageLargeWords)*0.4));
    }

    /**
     * Counts the number of PopUps
     * @param source
     * @return number of popUps
     */
    public int getPopUps(Source source) {
        int nPopUps = 0;
        try {
            List scriptList = source.getAllElements("script");
            Iterator it = scriptList.iterator();
            while(it.hasNext())
            {
                String script=it.next().toString();
                if(script.toLowerCase().contains("window.open"))
                {
                    nPopUps++;
                }
            }
        } catch (Exception e) {
        }
        return nPopUps;
    }

    /**
     * Counts the number of PopUps
     * @param pagesTree
     * @return number of PopUps
     * @throws SQLException
     */
    public int getPopUps(TreeSet pagesTree) throws SQLException {
        int totalPopUpsNumber = 0;
        int elementPopUpNumber = 0;

        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementPopUpNumber = getPopUps(getSource(linked));
                totalPopUpsNumber += elementPopUpNumber;
                System.out.print(">");
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalPopUpsNumber;
    }

    /**
     * Counts the italian words
     * @param source
     * @return number of italian words
     */
    public int getItalicWords(Source source) {
        int italicWords = 0;
        try {
            List scriptList = source.getAllElements("i");
            Iterator it = scriptList.iterator();
            while(it.hasNext())
            {
                String sentence=it.next().toString();
                sentence=sentence.replaceAll("<i>", "");
                sentence=sentence.replaceAll("</i>", "");
                StringTokenizer t = new StringTokenizer(sentence);
                while(t.hasMoreTokens())
                {
                    t.nextToken();
                    italicWords++;
                }
            }
        } catch (Exception e) {
        }
        return italicWords;
    }

    /**
     * Counts the number of italian words
     * @param pagesTree
     * @return number of italian words
     * @throws SQLException
     */
    public int getItalicWords(TreeSet pagesTree) throws SQLException {
        int totalItalicWords = 0;
        int elementItalicWords = 0;

        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementItalicWords = getItalicWords(getSource(linked));
                totalItalicWords += elementItalicWords;
                System.out.print(">");
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalItalicWords;
    }

    /**
     * Count the number of colors
     * @param source
     * @return number of colors
     */
    public int getColors(Source source) {
        int colorCount = 1;
        try {
            //List scriptList = source.getElementById("color");
            //System.out.println(source.getParseText());
            String text = source.getParseText().toString().toLowerCase();
            List<String> colorList = new ArrayList<String>();
            String color="color";
            int position=0;
            boolean finished=false;
            while(!finished)
            {
                position = text.indexOf(color,position);
                if(position!=-1)
                {
                    int colorPosition=position+color.length();
                    String temp=text.substring(colorPosition,colorPosition+20);
                    if(temp.startsWith(":")||temp.startsWith("="))
                    {
                        temp=temp.replace("\"", "");
                        temp=temp.replace("=", "");
                        temp=temp.replace(":", "");
                        if(temp.startsWith(" "))
                        {
                            temp=temp.substring(1,temp.length());
                        }
                        if(temp.startsWith("#"))
                        {
                            temp=temp.substring(0, 7);
                        }
                        else
                        {
                            if(temp.contains(";"))
                            {
                                temp=temp.substring(0,temp.indexOf(";"));
                            }
                            else
                            {
                                temp=temp.substring(0,temp.indexOf(" "));
                            }
                        }
                        //System.out.println("Color="+temp);
                        if (colorList.isEmpty())
                        {
                            colorList.add(temp);
                            //System.out.println(temp);
                        }
                        else
                        {
                            Iterator it=colorList.iterator();
                            boolean exists=false;
                            while(it.hasNext())
                            {
                                if(it.next().toString().equals(temp))
                                {
                                    exists=true;
                                }
                            }
                            if(!exists)
                            {
                                colorList.add(temp);
                                //System.out.println(temp);
                            }
                        }
                    }
                    position=colorPosition+20;
                }
                else
                {
                    finished=true;
                }
            }
            if(!colorList.isEmpty())
            {
                colorCount=colorList.size();
            }
        } catch (Exception e) {
        }
        return colorCount;
    }

    /**
     * Count the number of colors
     * @param pagesTree
     * @return number of colors
     * @throws SQLException
     */
    public int getColors(TreeSet pagesTree) throws SQLException {
        int totalColors = 0;
        int elementColors = 0;

        try {
            for (Iterator iter = pagesTree.iterator(); iter.hasNext();) {
                String linked = (String) iter.next();
                elementColors = getColors(getSource(linked));
                totalColors += elementColors;
                System.out.print(">");
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
        return totalColors;
    }



}
