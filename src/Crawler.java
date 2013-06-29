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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.Source;

/**
 *
 * @author Victor Apellaniz
 */

/**
     * Class for crawling from the webs, depending on the type of
     * execution selected.
     * It could be crawl the web of Materials, update the users Data
     * from the database, or crawl for getting the Metrics data
     */
public class Crawler {
    /**
     * Class' constructor
     */
    private String _databaseName;
    private String _databaseUser;
    private String _databasePass;

    public Crawler(String databaseName,String databaseUser,String databasePass)
    {
        _databaseName = databaseName;
        _databaseUser = databaseUser;
        _databasePass = databasePass;
    }

    /**
     * CrawlMaterials
     * It crawls the materials' webs, extracting all the info, and
     * inserting all into the database
     */
    public void CrawlMaterials()
    {
        DBConnection dbMerlot = new DBConnection(_databaseName,_databaseUser,_databasePass);
        System.out.println("Connected to database");
        //We connect to the principal web of materials
        Materials webMaterials = new Materials();
        int numberPages = webMaterials.getNumberPages();
        //Start from a different page
        //for(int pageNumber=3210;pageNumber<numberPages;pageNumber++)
        for(int pageNumber=1;pageNumber<numberPages;pageNumber++)
        {
            System.out.println("Material's page= "+pageNumber);
            //We get the IDs that the current page contains
            int[] materialsInPage=webMaterials.extractLinksInPage(pageNumber);
            for(int k=0;k<materialsInPage.length;k++)
            {
                ViewMaterial currentMaterial = new ViewMaterial(materialsInPage[k]);
                //Insert into loorganizations. We keep the organizationID
                System.out.println("Inserting into loorganizations: " + currentMaterial.getOrganizationName());
                int organizationID = dbMerlot.addOrganization(currentMaterial.getOrganizationName());
                //insert into loauthors. We keep the authorID
                System.out.println("Inserting into loaauthors: " + currentMaterial.getAuthorName());
                int idAuthorDatabase = dbMerlot.addAuthor(currentMaterial.getAuthorName(), organizationID, currentMaterial.getAuthorEmail(), currentMaterial.getAuthorID());
                //insert into lodata
                System.out.println("Inserting into lodata");
                dbMerlot.addLOData(materialsInPage[k], currentMaterial.getTitle(), currentMaterial.getMaterialType(), currentMaterial.getTechnicalFormat(),
                    currentMaterial.getLocation(), currentMaterial.getDateAdded(), currentMaterial.getDateModified(), idAuthorDatabase,
                    currentMaterial.getSubmitterID(), currentMaterial.getDescription(), currentMaterial.getTechnicalRequirements(),
                    currentMaterial.getLanguage(), currentMaterial.getMaterialVersion(), currentMaterial.getCopyright(),
                    currentMaterial.getSourceCodeAvailable(), currentMaterial.getAccessibilityInformationAvailable(),
                    currentMaterial.getCostInvolved(), currentMaterial.getCreativeCommons(), currentMaterial.getMobileCompatibility(),
                    currentMaterial.getEditorsChoice(), currentMaterial.getMerlotClassic());

                //insert into lovaloration
                System.out.println("Inserting into lovaloration");
                int valorationID = dbMerlot.addValoration(materialsInPage[k], currentMaterial.getPeerReviews(), currentMaterial.getComments(),
                        currentMaterial.getStarsReviews(), currentMaterial.getStarsComments(), currentMaterial.getPersonalCollections(),
                        currentMaterial.getLearningExercises());

                //If there are comments, we'll surf over the comments' web
                if(!currentMaterial.getComments().equals("none"))
                {
                    System.out.print("Inserting into locomments");
                    Comments webComments = new Comments(materialsInPage[k]);
                    //we get all the Comments' IDs
                    int[] commentsIDs = webComments.getCommentsIDs();
                    //Loop over all the comments
                    for(int i=0;i<commentsIDs.length;i++)
                    {
                        //we connect to the current comment webpage
                        ViewComment comment = new ViewComment(commentsIDs[i],currentMaterial.getTitle());
                        //inserting into Locomments
                        //System.out.print("." + commentsIDs[i]);
                        System.out.print(".");
                        dbMerlot.addComment(comment.getIdComment(), comment.getMaterial(), comment.getRating(),
                                comment.getClassroomUse(), comment.getAuthorID(), comment.getRemarks(),
                                comment.getTechnicalRemarks(), comment.getDateAdded());
                        //inserting into loval_com
                        //System.out.print("." + commentsIDs[i]);
                        System.out.print(".");
                        dbMerlot.addVal_Com(commentsIDs[i], valorationID);
                    }
                    System.out.print("\n");
                }
                //if there are reviews, we'll surf over the reviews' web
                if(!currentMaterial.getPeerReviews().equals("not reviewed"))
                {
                    System.out.print("Inserting into loreviews");
                    Reviews reviews = new Reviews(materialsInPage[k]);
                    //we obtain all the reviews' ids
                    int[] reviewsIDs = reviews.getReviewsIDs();
                    //we surfer over all the reviews' webpages
                    for(int i=0;i<reviewsIDs.length;i++)
                    {
                        ViewCompositeReview review = new ViewCompositeReview(reviewsIDs[i],currentMaterial.getTitle());
                        //inserting into en loreviews
                        //System.out.print("." + reviewsIDs[i]);
                        System.out.print(".");
                        dbMerlot.addReview(review.getIdReview(), review.getMaterial(), review.getOverview(),
                                review.getLearningGoals(), review.getAuthor(), review.getDateAdded(),
                                review.getTargetStudentPopulation(), review.getPrerequisiteKnowledgeOrSkills(),
                                review.getTypeOfMaterial(), review.getRecommendedUse(),
                                review.getTechnicalRequirements(), review.getContentQuality(),
                                review.getEfectiveness(), review.getEaseOfUse(),
                                review.getOtherIssuesAndComments(), review.getCommentsFromTheAuthor(),
                                review.getPeerReviewerID());
                        //inserting into loval_Rev
                        //System.out.println("." + reviewsIDs[i]);
                        System.out.println(".");
                        dbMerlot.addVal_Rev(reviewsIDs[i], valorationID);
                    }
                    System.out.print("\n");
                }
                //We insert the categories into the database
                System.out.print("Inserting into locategories");
                for(int i=0;i<currentMaterial.getCategoriesIDs().length;i++)
                {
                    //System.out.println("Inserting into locategories:" + currentMaterial.getCategoriesNames()[i]);
                    System.out.print(".");
                    dbMerlot.addCategories(materialsInPage[k], currentMaterial.getCategoriesNames()[i], currentMaterial.getCategoriesIDs()[i]);
                }
                //We insert the primaryAudience into the database
                System.out.print("\nInserting into primaryAud");
                for(int i=0;i<currentMaterial.getPrimaryAudience().length;i++)
                {
                    //System.out.println("Inserting into primaryAud:" + currentMaterial.getPrimaryAudience()[i]);
                    System.out.print(".");
                    dbMerlot.addPrimaryAudience(materialsInPage[k], currentMaterial.getPrimaryAudience()[i]);
                }
                //If the author is a Merlot member, we get his data
                System.out.print("\n");
                if(currentMaterial.getAuthorID()!=0)
                {
                    ViewMember webMember = new ViewMember(currentMaterial.getAuthorID());
                    //we add (if it not exists) the author's category into locagegories
                    //System.out.println("Inserting user's category into locatagories:" + webMember.getCategoryName());
                    dbMerlot.addCategories(materialsInPage[k], webMember.getCategoryName(), webMember.getIdCategory());
                    //inserting user
                    System.out.println("Inserting into users:" + webMember.getIdMember());
                    dbMerlot.addUsers(webMember.getIdMember(),webMember.getName(),webMember.getRibbon(),
                            webMember.getAuthor(),webMember.getVsb(),webMember.getPeerReviewer(),
                            webMember.getMerlotAward(),webMember.getIdCategory(),webMember.getMemberType(),
                            webMember.getLastLogin(),webMember.getMemberSince(),webMember.hasPersonalCollections());
                    //We get the portfolios from the author (if he has any)
                    if(webMember.hasPersonalCollections())
                    {
                        Portfolios webPortfolios = new Portfolios(webMember.getIdMember());
                        int[] userPortfolios = webPortfolios.getPortfoliosIDs();
                        //we surfer over all the portfolios
                        System.out.print("Inserting into users_lo");
                        for(int i=0;i<userPortfolios.length;i++)
                        {
                            ViewPortfolio portfolio = new ViewPortfolio(userPortfolios[i]);
                            int[] idsMaterialsPortfolio = portfolio.getIdsMaterials();
                            //we loop over all the Material's ids in the portfolio (if there are any)
                            if(idsMaterialsPortfolio!=null)
                            {
                                for(int j=0;j<idsMaterialsPortfolio.length;j++)
                                {
                                    //System.out.println("Inserting into users_lo: "+ idsMaterialsPortfolio[j]
                                    //        + ", "+ webMember.getIdMember() + ", "+ userPortfolios[i]);
                                    System.out.print(".");
                                    dbMerlot.addUsersLo(idsMaterialsPortfolio[j], webMember.getIdMember(),
                                            userPortfolios[i]);
                                }
                            }//End looping over materials in the portfolio
                        }//End looping over portfolios
                        System.out.print("\n");
                    }//End If user has portfolios
                }//End if user is a merlot member
            }//End looping over the materials in a page
        }//End looping over the materials pages
        dbMerlot.closeConnection();
    }

    /**
     * CrawlMetrics()
     * It connects to the merlot database, obtains all the locations, and surf over
     * them to obtain metrics and inserting them into the metrics' database
     */
    public void CrawlMetrics() {
        DBConnection dbMerlot = new DBConnection(_databaseName,_databaseUser,_databasePass);

        Config.LoggerProvider = LoggerProvider.DISABLED;
        Date fecha_hora_inicio = new Date(); //Show time
        System.out.println("Time : " + fecha_hora_inicio); //Show time
        //we create the metrics table if it doesn't exists
        dbMerlot.createTableMerlot(_databaseName,_databaseUser,_databasePass);
        //we count all the locations we have to check
        int top = dbMerlot.countUrlsMerlot();
        //we create a list of IDs
        dbMerlot.createListIdLoMerlot();
        //we create a list of locations
        dbMerlot.createListLocationMerlot();
        MetricsParser parser = new MetricsParser();

        //for (int i = 1; i < 1310 ; i++) { // TO START IN A DIFFERENT POINT, n+1 of the rows of the DB
        //    dbMerlot.getNextIdLo(); // TO START IN A DIFFERENT POINT, n+1 of the rows of the DB
        //    dbMerlot.getNextLocation(); // TO START IN A DIFFERENT POINT, n+1 of the rows of the DB
        //}
        
        for (int row = 1; row <= top; row++) {
            try {
                String idLo = dbMerlot.getNextIdLo();
                String location = dbMerlot.getNextLocation();
                //location="http://polymer.bu.edu/java";
                System.out.println("\nParsing LO: " + idLo);
                System.out.println("Number:"+row+" of "+top);
                
                TreeSet treeSite = parser.getTreeSite(location);
                TreeSet pagesTree = parser.getLocationPages(location, treeSite);
                TreeSet locationUniqueLinks = parser.getUniqueLinksTree(pagesTree);
                List locationLinks = parser.getLinksTree(pagesTree);
                TreeSet imagesTree = parser.getImagesTree(pagesTree);
                Source source = parser.getSource(location);
                if (!location.endsWith(".mhtml") && (source != null)) {
                    System.out.println("\nLocation: " + location);
                    int nLinks = locationLinks.size();
                    System.out.println("nLinks ok = " + nLinks);
                    int nUniqueLinks = locationUniqueLinks.size();
                    System.out.println("nUniqueLinks ok = " + nUniqueLinks);
                    int nWebLinksErrors = parser.getLinkErrors(locationUniqueLinks);
                    System.out.println("nWebLinksErrors ok= "+nWebLinksErrors);
                    int nRedundantLinks=nLinks-nUniqueLinks;
                    System.out.println("nRedundantLinks ok = " + nRedundantLinks);
                    int nInternalLinks = parser.getIntenalLinksNumber(location, locationLinks);
                    System.out.println("nInternalLinks ok = " + nInternalLinks);
                    int nUniqueInternalLinks = parser.getUniqueIntenalLinksNumber(location, locationUniqueLinks);
                    System.out.println("nUniqueInternalLinks ok = " + nUniqueInternalLinks);
                    int nExternalLinks = parser.getExternalLinksNumber(location, locationLinks);
                    System.out.println("nExternalLinks ok");
                    int nUniqueExternalLinks = parser.getUniqueExternalLinksNumber(location, locationUniqueLinks);
                    System.out.println("nExternalUniqueLinks ok");
                    int nImgs = parser.getTreeSize(imagesTree);
                    System.out.println("nImgs ok");
                    int htmlSize = parser.getIHtmlBytes(pagesTree);
                    System.out.println("htmlSize ok");
                    int imagesSize = parser.getImagesSizeN(location, imagesTree);
                    System.out.println("imagesSize ok");
                    int nScripts = parser.getScriptNumber(pagesTree);
                    System.out.println("nScripts ok");
                    int nApplets = parser.getAppletNumber(pagesTree);
                    System.out.println("nApplets ok");
                    int nPopUps = parser.getPopUps(pagesTree);
                    System.out.println("nPopUps ok");
                    int displayWordsCount = parser.getDisplayedWords(pagesTree);
                    System.out.println("displayWords ok");
                    int linkWordsCount = parser.getLinkWords(pagesTree);
                    System.out.println("linkWords ok");
                    int bodyWordsCount = displayWordsCount-linkWordsCount;
                    System.out.println("bodyWordsCount ok");
                    int italicWordsCount = parser.getItalicWords(pagesTree);
                    System.out.println("italicWordsCount ok");
                    int colorCount = parser.getColors(pagesTree);
                    System.out.println("colorCount ok");
                    int numberOfPages = pagesTree.size();
                    System.out.println("nPages ok");
                    int downloadFiles = parser.getFilesForDownload(locationUniqueLinks);
                    System.out.println("download files ok");
                    int audioFiles = parser.getAudioFiles(locationUniqueLinks);
                    System.out.println("audio files ok");
                    int videoFiles = parser.getVideoFiles(locationUniqueLinks);
                    System.out.println("video files ok");
                    int multimediaFiles = audioFiles + videoFiles;
                    System.out.println("multimedia files ok");
                    float averageNumberOfUniqueInternalLinks = parser.reducirDecimales((float) nUniqueInternalLinks / numberOfPages);
                    System.out.println("averageNumberOfUniqueInternalLinks ok");
                    float averageNumberOfInternalLinks = parser.reducirDecimales((float) nInternalLinks / numberOfPages);
                    System.out.println("averageNumberOfInternalLinks ok");
                    float averageNumberOfUniqueExternalLinks = parser.reducirDecimales((float) nUniqueExternalLinks / numberOfPages);
                    System.out.println("averageNumberOfUniqueExternalLinks ok");
                    float averageNumberOfExternalLinks = parser.reducirDecimales((float) nExternalLinks / numberOfPages);
                    System.out.println("averageNumberOfExternalLinks ok");
                    float averageNumberOfUniqueLinks = parser.reducirDecimales((float) nUniqueLinks / numberOfPages);
                    System.out.println("averageNumberOfUniqueLinks ok");
                    float averageNumberOfLinks = parser.reducirDecimales((float) nLinks / numberOfPages);
                    System.out.println("averageNumberOfLinks ok");
                    float averageWords = parser.reducirDecimales((float) displayWordsCount / numberOfPages);
                    System.out.println("averageWords ok");
                    float averageDownloadFiles = parser.reducirDecimales((float) downloadFiles / numberOfPages);
                    System.out.println("averageDownloadFiles ok");
                    float averageAudioFiles = parser.reducirDecimales((float) audioFiles / numberOfPages);
                    System.out.println("averageAudioFiles ok");
                    float averageVideoFiles = parser.reducirDecimales((float) videoFiles / numberOfPages);
                    System.out.println("averageVideoFiles ok");
                    float averageMultimediaFiles = parser.reducirDecimales((float) multimediaFiles / numberOfPages);
                    System.out.println("averageMultimediaFiles ok");
                    float averageApplets = parser.reducirDecimales((float) nApplets / numberOfPages);
                    System.out.println("averageApplets ok");
                    float averageImages = parser.reducirDecimales((float) nImgs / numberOfPages);
                    System.out.println("averageImages ok");
                    float averageHtmlBytes = parser.reducirDecimales((float) htmlSize / numberOfPages);
                    System.out.println("averageHtmlBytes ok");
                    float averageImagesSize = parser.reducirDecimales((float) imagesSize / numberOfPages);
                    System.out.println("averageImagesSize ok");
                    float averageScripts = parser.reducirDecimales((float) nScripts / numberOfPages);
                    System.out.println("averageScripts ok");
                    float gunningFogIndex = parser.getGunningFog(pagesTree);
                    System.out.println("gunningFogIndex ok= "+gunningFogIndex);
                    dbMerlot.setDataMerlot(idLo, nLinks, nUniqueLinks, nInternalLinks, nUniqueInternalLinks,
                            nExternalLinks, nUniqueExternalLinks, nImgs, htmlSize, imagesSize, nScripts, nApplets,
                            displayWordsCount, linkWordsCount, numberOfPages, downloadFiles, audioFiles, videoFiles,
                            multimediaFiles, averageNumberOfUniqueInternalLinks, averageNumberOfInternalLinks, averageNumberOfUniqueExternalLinks,
                            averageNumberOfExternalLinks, averageNumberOfUniqueLinks, averageNumberOfLinks, averageWords,
                            averageDownloadFiles, averageAudioFiles, averageVideoFiles, averageMultimediaFiles,
                            averageApplets, averageImages, averageHtmlBytes, averageImagesSize, averageScripts,
                            gunningFogIndex,nWebLinksErrors,nRedundantLinks,nPopUps,colorCount,
                            bodyWordsCount,italicWordsCount);
                    if (htmlSize == 0) {
                        dbMerlot.updateErrorFieldMerlot(idLo, 1);
                    }
                } else {
                    System.out.println("Location: " + location);
                    int nLinks = 0;
                    int nUniqueLinks = 0;
                    int nInternalLinks = 0;
                    int nUniqueInternalLinks = 0;
                    int nExternalLinks = 0;
                    int nUniqueExternalLinks = 0;
                    int nImgs = 0;
                    int htmlSize = 0;
                    int imagesSize = 0;
                    int nScripts = 0;
                    int nApplets = 0;
                    int displayWordsCount = 0;
                    int linkWordsCount = 0;
                    int numberOfPages = 0;
                    int downloadFiles = 0;
                    int audioFiles = 0;
                    int videoFiles = 0;
                    int multimediaFiles = 0;
                    int nWebLinksErrors = 0;
                    int nRedundantLinks = 0;
                    int nPopUps = 0;
                    int colorCount = 0;
                    int bodyWordsCount = 0;
                    int italicWordsCount = 0;
                    float averageNumberOfUniqueInternalLinks = 0;
                    float averageNumberOfInternalLinks = 0;
                    float averageNumberOfUniqueExternalLinks = 0;
                    float averageNumberOfExternalLinks = 0;
                    float averageNumberOfUniqueLinks = 0;
                    float averageNumberOfLinks = 0;
                    float averageWords = 0;
                    float averageDownloadFiles = 0;
                    float averageAudioFiles = 0;
                    float averageVideoFiles = 0;
                    float averageMultimediaFiles = 0;
                    float averageApplets = 0;
                    float averageImages = 0;
                    float averageHtmlBytes = 0;
                    float averageImagesSize = 0;
                    float averageScripts = 0;
                    float gunningFogIndex = 0;
                    dbMerlot.setDataMerlot(idLo, nLinks, nUniqueLinks, nInternalLinks, nUniqueInternalLinks,
                            nExternalLinks, nUniqueExternalLinks, nImgs, htmlSize, imagesSize, nScripts, nApplets,
                            displayWordsCount, linkWordsCount, numberOfPages, downloadFiles, audioFiles, videoFiles,
                            multimediaFiles, averageNumberOfUniqueInternalLinks, averageNumberOfInternalLinks, averageNumberOfUniqueExternalLinks,
                            averageNumberOfExternalLinks, averageNumberOfUniqueLinks, averageNumberOfLinks, averageWords,
                            averageDownloadFiles, averageAudioFiles, averageVideoFiles, averageMultimediaFiles,
                            averageApplets, averageImages, averageHtmlBytes, averageImagesSize, averageScripts,
                            gunningFogIndex,nWebLinksErrors,nRedundantLinks,nPopUps,colorCount,
                            bodyWordsCount,italicWordsCount);
                    dbMerlot.updateErrorFieldMerlot(idLo, 1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
//                dbMerlot.updateErrorFieldMerlot(idLo, 3);
            } catch (Exception e) {
                System.out.println("MerlotCrawlerException " + e.getMessage());
//                dbMerlot.updateErrorFieldMerlot(idLo, 3);
                }
        }
        Date fecha_hora_final = new Date(); //Show time
        System.out.println("Time : " + fecha_hora_final); //Show time
        dbMerlot.closeConnection();
    }

    /**
     * UpdateUsers()
     * It connects to the database, obtain the users' list and
     * surf over each user's web for obtaining data and update the database
     */
    public void UpdateUsers()
    {
        DBConnection dbMerlot = new DBConnection(_databaseName,_databaseUser,_databasePass);

        int top = dbMerlot.countUsersMerlot();
        dbMerlot.createListIdUserMerlot();
        for (int row = 1; row <= top; row++)
        {
            int idUser = dbMerlot.getNextUserId();

            ViewMember webMember = new ViewMember(idUser);
            
            System.out.println("Inserting into users:" + webMember.getIdMember());
            dbMerlot.addUsers(webMember.getIdMember(),webMember.getName(),webMember.getRibbon(),
                  webMember.getAuthor(),webMember.getVsb(),webMember.getPeerReviewer(),
                  webMember.getMerlotAward(),webMember.getIdCategory(),webMember.getMemberType(),
                  webMember.getLastLogin(),webMember.getMemberSince(),webMember.hasPersonalCollections());
            //We get the user's portfolios (if any)
            if(webMember.hasPersonalCollections())
            {
                Portfolios webPortfolios = new Portfolios(webMember.getIdMember());
                int[] userPortfolios = webPortfolios.getPortfoliosIDs();
                //we surf over the portfolios
                for(int i=0;i<userPortfolios.length;i++)
                {
                    ViewPortfolio portfolio = new ViewPortfolio(userPortfolios[i]);
                    int[] idsMaterialsPortfolio = portfolio.getIdsMaterials();
                    //we surf over the materials on the current portfolio
                    if(idsMaterialsPortfolio!=null)
                    {
                        for(int j=0;j<idsMaterialsPortfolio.length;j++)
                        {
                            
                            System.out.println("Inserting into users_lo: "+ idsMaterialsPortfolio[j]
                                + ", "+ webMember.getIdMember() + ", "+ userPortfolios[i]);
                            dbMerlot.addUsersLo(idsMaterialsPortfolio[j], webMember.getIdMember(),
                                                userPortfolios[i]);
                        }
                    }//end if there are materials in the portfolio
                }//end looping over portfolios
             }//end if author has portfolios
        }//end looping over the users' Ids
    }

}
