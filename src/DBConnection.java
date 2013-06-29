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

import java.util.*;
import java.sql.*;
import java.sql.ResultSet;

/**
 *
 * @author Victor Apellaniz
 */

/**
* Class: DBConnection
* Used for connecting to the merlot database and inserting data
*/
public class DBConnection {
    private Statement _stmt = null;
    private Connection _con;

    private static LinkedList<String> _idLoList = new LinkedList<String>();
    private static LinkedList<String> _locationList = new LinkedList<String>();
    private static LinkedList<String> _idUserList = new LinkedList<String>();

    /**
     * DBConnection(database)
     * It connects to a database
     * @param database
     */
    public DBConnection(String database,String user,String password)
    {
          try
          {
           // _con = DriverManager.getConnection
           //         ("jdbc:mysql://localhost/" + database + "?" +
           //         "user=admin&password=admin");
               Properties props = new Properties();
               props.put("user", user);
               props.put("password",password);
               props.put("autoReconnect", "true");
               _con = DriverManager.getConnection("jdbc:mysql://localhost/"+database,props);
          }
          catch(SQLException e)
            {
                System.out.println("SQLException on DBConnection constructor" + e.getMessage());
            }
    }
    /**
     * convertDate(date)
     * Converts a java.util.Date to a java.sql.Date
     * @param java.util.Date
     * @return java.sql.Date
     */
    private static java.sql.Date convertDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * addLOData
     * 
     * @param idLO
     * @param title
     * @param materialType
     * @param technicalFormat
     * @param location
     * @param dateAdded
     * @param dateModified
     * @param idAuthor
     * @param idSubmitter
     * @param description
     * @param technicalRequirements
     * @param language
     * @param materialVersion
     * @param copyright
     * @param sourceCodeAvailable
     * @param accessibilityInformationAvailable
     * @param costInvolved
     * @param creativeCommons
     * @param mobileCompatibility
     * @param editorsChoice
     * @param merlotClassic
     * 
     * Inserts into table "lodata"
     */
    public synchronized void addLOData(int idLO,String title,String materialType,
            String technicalFormat,String location,java.util.Date dateAdded,java.util.Date dateModified,
            int idAuthor,int idSubmitter,String description,String technicalRequirements,
            String language,String materialVersion,boolean copyright,boolean sourceCodeAvailable,
            boolean accessibilityInformationAvailable,boolean costInvolved,String creativeCommons,
            String mobileCompatibility,boolean editorsChoice,boolean merlotClassic)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM lodata WHERE " +
                    "ID_LO="+"'"+idLO+"'");
            if (!(res.next())) //test if exist
                {   //If it doesn't exist, we add it
                    String sql="INSERT INTO lodata VALUES " +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement addNew = _con.prepareStatement(sql);
                    addNew.setInt(1,idLO);
                    addNew.setString(2,title);
                    addNew.setString(3,materialType);
                    addNew.setString(4,technicalFormat);
                    addNew.setString(5,location);
                    addNew.setDate(6,convertDate(dateAdded));
                    addNew.setDate(7,convertDate(dateModified));
                    addNew.setInt(8,idAuthor);
                    addNew.setInt(9,idSubmitter);
                    addNew.setString(10,description);
                    addNew.setString(11,technicalRequirements);
                    addNew.setString(12,language);
                    addNew.setString(13,materialVersion);
                    addNew.setBoolean(14,copyright);
                    addNew.setBoolean(15,sourceCodeAvailable);
                    addNew.setBoolean(16,accessibilityInformationAvailable);
                    addNew.setBoolean(17,costInvolved);
                    addNew.setString(18,creativeCommons);
                    addNew.setString(19,mobileCompatibility);
                    addNew.setBoolean(20,editorsChoice);
                    addNew.setBoolean(21,merlotClassic);

                    addNew.executeUpdate();
                    addNew.close();
                    System.out.print(idLO+" --> Added \n");
                    res.close();
                }
                else
                {   //If it exists, check that the new version is updated
                    ResultSet res2=_stmt.executeQuery("SELECT * FROM lodata " +
                            "WHERE ID_LO="+"'"+idLO+"' AND " +
                            "Date_Modified="+"'"+dateModified+"'");
                    if (!(res2.next())) //test if exist
                    {   //If the modification dates do not coincide, we assume
                        //that the object has been modified

                        //Delete the old row and add the new
                        String sql="DELETE FROM lodata WHERE " +
                                "ID_LO='"+idLO+"'";
                        PreparedStatement delete = _con.prepareStatement(sql);
                        delete.execute(sql);
                        delete.close();
                        sql="INSERT INTO lodata VALUES " +
                                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement addNew = _con.prepareStatement(sql);
                        addNew.setInt(1,idLO);
                        addNew.setString(2,title);
                        addNew.setString(3,materialType);
                        addNew.setString(4,technicalFormat);
                        addNew.setString(5,location);
                        addNew.setDate(6,convertDate(dateAdded));
                        addNew.setDate(7,convertDate(dateModified));
                        addNew.setInt(8,idAuthor);
                        addNew.setInt(9,idSubmitter);
                        addNew.setString(10,description);
                        addNew.setString(11,technicalRequirements);
                        addNew.setString(12,language);
                        addNew.setString(13,materialVersion);
                        addNew.setBoolean(14,copyright);
                        addNew.setBoolean(15,sourceCodeAvailable);
                        addNew.setBoolean(16,accessibilityInformationAvailable);
                        addNew.setBoolean(17,costInvolved);
                        addNew.setString(18,creativeCommons);
                        addNew.setString(19,mobileCompatibility);
                        addNew.setBoolean(20,editorsChoice);
                        addNew.setBoolean(21,merlotClassic);

                        addNew.executeUpdate();
                        addNew.close();
                        res2.close();
                        System.out.print(idLO+" --> The Learning " +
                                "Object has been updated\n");
                }
                    else
                    {
                          System.out.print(idLO+" --> The Learning " +
                                  "object exists\n");
                    }
                }
                _stmt.close();
        }
        catch(SQLException e)
         {
           System.out.println("SQLException on addLOData "+e.getMessage()+"Objeto "
                   +idLO);
          }

    }

    /**
     * addOrganization
     * 
     * Inserts into "loorganizations" table. Once the data is inserted,
     * it returns the id from the organization added.
     * 
     * @param organization
     * @return organizationID
     */
    public synchronized int addOrganization (String organization)
    {
        int ID_Organization=0;
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT ID_Organization " +
                    "FROM LOOrganizations " +
                    "WHERE Name="+"'"+organization+"'");
            if (!(res.next())) //test if exist
            {
                 String sql="INSERT INTO LOOrganizations (Name) " +
                         "VALUES (?)";
                  PreparedStatement addNewOrg = _con.prepareStatement(sql);
                  addNewOrg.setString(1,organization);
                  addNewOrg.executeUpdate();
                  addNewOrg.close();

                  ResultSet res2=_stmt.executeQuery("SELECT ID_Organization " +
                      "FROM LOOrganizations " +
                      "WHERE Name="+"'"+organization+"'");
                  if(res2.next())
                  {
                    ID_Organization=res2.getInt(1);
                  }
                  res2.close();
            }
             else
          {
             ID_Organization=res.getInt(1);
          }
             res.close();
            _stmt.close();
        }
        catch(SQLException e)
         {
           System.out.println("SQLException on addOrganization "+e.getMessage()+
                   "Objeto "+organization);
         }
        return ID_Organization;
    }

    /**
     * addAuthor
     * 
     * Inserts into "loauthors" table. Once inserted, it gets the author ID
     * 
     * @param name
     * @param ID_Organization
     * @param email
     * @param idAuthorMerlot
     * @return authorID
     */
    public synchronized int addAuthor(String name,int ID_Organization,String email,int idAuthorMerlot)
    {
        int ID_Author=0;
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT ID_Author " +
                    "FROM LOAuthors WHERE Name="+"'"+name+"'");
          if (!(res.next())) //test if exist
          {
              String sql="INSERT INTO LOAuthors " +
                      "(Name,ID_Organization,Email, ID_Author_Merlot) " +
                      "VALUES (?,?,?,?)";
              PreparedStatement addNewAuthor = _con.prepareStatement(sql);
              addNewAuthor.setString(1,name);
              addNewAuthor.setInt(2,ID_Organization);
              addNewAuthor.setString(3,email);
              addNewAuthor.setInt(4,idAuthorMerlot);
              addNewAuthor.executeUpdate();
              addNewAuthor.close();
              ResultSet res2=_stmt.executeQuery("SELECT ID_Author " +
                      "FROM LOAuthors WHERE Name="+"'"+name+"'");
              if(res2.next())
              {
                ID_Author=res2.getInt(1);
              }
              res2.close();
          }
          else
          {
             ID_Author=res.getInt(1);

          }
          res.close();
          _stmt.close();
        }
        catch(SQLException e)
         {
           System.out.println("SQLException on addAuthor "+e.getMessage()+
                   "Objeto "+name);
         }
       return ID_Author;
    }

    /**
     * Insert into "lovalorations" table. Once inserted, it gets the valoration ID
     * 
     * @param idLO
     * @param peerReviews
     * @param comments
     * @param starsReviews
     * @param starsComments
     * @param personalCollections
     * @return valorationID
     */
    public synchronized int addValoration(int idLO,String peerReviews,String comments,
            double starsReviews,double starsComments,String personalCollections,String learningExercises)
    {
        int idVal=0;
            try
            {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM LOValoration " +
                    "WHERE ID_LO="+"'"+idLO+"'");
            if (!(res.next())) //test if exist
                {
                    String sql="INSERT INTO LOValoration (ID_LO, " +
                            "Peer_Reviews, Comments, Stars_Reviews, " +
                            "Stars_Comments, Personal_Collections, Learning_Exercises)" +
                            "VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement addNew = _con.prepareStatement(sql);
                    addNew.setInt(1,idLO);
                    addNew.setString(2,peerReviews);
                    addNew.setString(3,comments);
                    addNew.setDouble(4,starsReviews);
                    addNew.setDouble(5,starsComments);
                    addNew.setString(6,personalCollections);
                    addNew.setString(7,learningExercises);

                    addNew.executeUpdate();
                    addNew.close();
                }
                else
                {
                    String sql="DELETE FROM LOValoration WHERE ID_LO='"+
                            idLO+"'";
                    PreparedStatement delete = _con.prepareStatement(sql);
                    delete.execute(sql);
                    delete.close();
                    sql="INSERT INTO LOValoration (ID_LO, Peer_Reviews, " +
                            "Comments, Stars_Reviews, Stars_Comments," +
                            "Personal_Collections,Learning_Exercises)VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement addNew = _con.prepareStatement(sql);
                    addNew.setInt(1,idLO);
                    addNew.setString(2,peerReviews);
                    addNew.setString(3,comments);
                    addNew.setDouble(4,starsReviews);
                    addNew.setDouble(5,starsComments);
                    addNew.setString(6,personalCollections);
                    addNew.setString(7,learningExercises);
                    addNew.executeUpdate();
                    addNew.close();
                }
                    ResultSet res2=_stmt.executeQuery
                            ("SELECT * FROM LOValoration " + "WHERE ID_LO="+"'"+
                            idLO+"'");
                    if(res2.next())
                    {
                        idVal=res2.getInt(6);
                    }
                    res2.close();
                    res.close();
                   _stmt.close();
            }
            catch(SQLException e)
         {
           System.out.println("SQLException on addValoration "+e.getMessage()+
                   "Objeto "+idLO);
         }
        return idVal;
    }

    /**
     * Insert into "loval_com" table.
     * 
     * @param IDComment
     * @param IDVal
     */
    public synchronized void addVal_Com(int IDComment, int IDVal) {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM LOVal_Com "
                + "WHERE ID_Comment="+"'"+IDComment+"' AND " +
                "ID_Valoration="+"'"+IDVal+"'");
                    if (!(res.next())) //test if exist
                    {
                      String sql="INSERT INTO LOVal_Com " +
                              "(ID_Valoration,ID_Comment) " + "VALUES (?,?)";
                      PreparedStatement addNew = _con.prepareStatement(sql);
                      addNew.setInt(1,IDVal);
                      addNew.setInt(2,IDComment);

                     addNew.executeUpdate();
                     addNew.close();

                    }
                     res.close();
                  _stmt.close();
        }
          catch(SQLException e)
         {
           System.out.println("SQLException on addVal_Com "+e.getMessage());
         }
    }

    /**
     * Insert into "locomments" table
     * 
     * @param IDComment
     * @param material
     * @param rating
     * @param classroomUse
     * @param author
     * @param remarks
     * @param technicalRemarks
     * @param dateAdded
     */
    public synchronized void addComment(int IDComment,String material,int rating,
            String classroomUse,int author,String remarks,String technicalRemarks,
            java.util.Date dateAdded)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery
                    ("SELECT * FROM LOComments " + "WHERE ID_Comment="+
                     "'"+IDComment+"'");
                    if (!(res.next())) //test if exist
                    {
                       String sql="INSERT INTO loComments VALUES "+
                               "(?,?,?,?,?,?,?,?)";
                        PreparedStatement addNew = _con.prepareStatement(sql);
                        addNew.setInt(1,IDComment);
                        addNew.setString(2,material);
                        addNew.setInt(3,rating);
                        addNew.setString(4,classroomUse);
                        addNew.setInt(5,author);
                        addNew.setString(6,remarks);
                        addNew.setString(7,technicalRemarks);
                        addNew.setDate(8,convertDate(dateAdded));

                        addNew.executeUpdate();
                        addNew.close();

                        res.close();
                    }
            _stmt.close();
            }
         catch(SQLException e)
         {
           System.out.println("SQLException on addComment "+e.getMessage());
         }
    }

    /**
     * Insert into loval_rev table.
     * 
     * @param idReview
     * @param idVal
     */
    public void addVal_Rev(int idReview, int idVal)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM LOVal_Rev "+
                            "WHERE ID_Review="+"'"+idReview+"' AND " +
                            "ID_Valoration="+"'"+idVal+"'");
            if (!(res.next())) //test if exist
            {
                     String sql="INSERT INTO LOVal_Rev(ID_Valoration,ID_Review)"
                             +"VALUES (?,?)";
                      PreparedStatement addNew = _con.prepareStatement(sql);
                      addNew.setInt(1,idVal);
                      addNew.setInt(2,idReview);

                     addNew.executeUpdate();
                     addNew.close();
             }
             res.close();
             _stmt.close();
        }
          catch(SQLException e)
         {
           System.out.println("SQLException on addVal_Rev "+e.getMessage());
         }
    }

    /**
     * Insert into loreviews table.
     * 
     * @param idReview
     * @param material
     * @param overview
     * @param learningGoals
     * @param author
     * @param dateAdded
     * @param targetStudentPopulation
     * @param prerequisiteKnowledge
     * @param typeOfMaterial
     * @param recommendedUse
     * @param technicalRequirements
     * @param contentQuality
     * @param efectiveness
     * @param easeOfUse
     * @param otherIssues
     * @param commentsFromAuthor
     * @param peerReviewerID
     */
    public synchronized void addReview(int idReview,String material,String overview,
           String learningGoals, String author, java.util.Date dateAdded,String targetStudentPopulation,
           String prerequisiteKnowledge,String typeOfMaterial,String recommendedUse,
           String technicalRequirements,String[] contentQuality,String[] efectiveness,
           String[] easeOfUse,String otherIssues,String commentsFromAuthor,int peerReviewerID)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery
                    ("SELECT * FROM LOReviews " + "WHERE id_review="+
                    "'"+idReview+"'");
            if (!(res.next())) //test if exist
            {
                String sql="INSERT INTO loReviews VALUES "+
                         "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement addNew = _con.prepareStatement(sql);
                addNew.setInt(1,idReview);
                addNew.setString(2,material);
                addNew.setString(3,overview);
                addNew.setString(4,learningGoals);
                addNew.setString(5,author);
                addNew.setDate(6,convertDate(dateAdded));
                addNew.setString(7,targetStudentPopulation);
                addNew.setString(8,prerequisiteKnowledge);
                addNew.setString(9,typeOfMaterial);
                addNew.setString(10,recommendedUse);
                addNew.setString(11,technicalRequirements);
                addNew.setDouble(12,Double.valueOf(contentQuality[0]));
                addNew.setString(13,contentQuality[1]);
                addNew.setString(14,contentQuality[2]);
                addNew.setDouble(15,Double.valueOf(efectiveness[0]));
                addNew.setString(16,efectiveness[1]);
                addNew.setString(17,efectiveness[2]);
                addNew.setDouble(18,Double.valueOf(easeOfUse[0]));
                addNew.setString(19,easeOfUse[1]);
                addNew.setString(20,easeOfUse[2]);
                addNew.setString(21,otherIssues);
                addNew.setString(22,commentsFromAuthor);
                addNew.setInt(23,peerReviewerID);
                addNew.executeUpdate();
                addNew.close();

                res.close();
            }
            _stmt.close();
         }
         catch(SQLException e)
         {
           System.out.println("SQLException on addReview "+e.getMessage());
         }
    }

    /**
     * Insert into locategories table
     * 
     * @param ID
     * @param Categorie
     * @param ID_Cat
     */
    public synchronized void addCategories
            (int ID, String Categorie, int ID_Cat)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM locategories" +
                    " WHERE ID_Cat="+"'"+ID_Cat+"'");
            if (!(res.next())) //test if exist
                {
                    String sql="INSERT INTO locategories VALUES (?,?)";
                    PreparedStatement addNew = _con.prepareStatement(sql);
                    addNew.setInt(1,ID_Cat);
                    addNew.setString(2,Categorie);

                   addNew.executeUpdate();
                   addNew.close();
                }
               res.close();

                ResultSet res2=_stmt.executeQuery("SELECT ID_CatDat " +
                        "FROM LOCat_Dat " +
                        "WHERE ID_LO="+"'"+ID+"'AND ID_Cat= '"+ID_Cat+"'");

                if (!(res2.next())) //test if exist
                     {

                  String sql="INSERT INTO LOCat_Dat (ID_LO,ID_Cat) " +
                          "VALUES (?,?)";
                  PreparedStatement addNew = _con.prepareStatement(sql);
                  addNew.setInt(1,ID);
                  addNew.setInt(2,ID_Cat);

                 addNew.executeUpdate();
                 addNew.close();
              }
                 res2.close();
                 _stmt.close();
        }
        catch(SQLException e)
         {
           System.out.println("SQLException on addCategories "+e.getMessage()+"Objeto "+ID);
         }
    }

    /**
     * Insert into "PrimaryAud" and "lodata_has_primaryAud" tables
     * 
     * @param idLO
     * @param audience
     */
    public synchronized void addPrimaryAudience(int idLO,String audience)
    {
        int idAudience=0;
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT idPrimary_Audience FROM PrimaryAud" +
                    " WHERE Audience="+"'"+audience+"'");
            
            if (!(res.next())) //test if exist
            {
                String sql="INSERT INTO PrimaryAud (Audience) " +
                          "VALUES (?)";
                PreparedStatement addNew = _con.prepareStatement(sql);
                addNew.setString(1,audience);

                addNew.executeUpdate();
                addNew.close();

                ResultSet res2=_stmt.executeQuery("SELECT idPrimary_Audience FROM PrimaryAud" +
                    " WHERE Audience="+"'"+audience+"'");
                
                if(res2.next()){
                    idAudience=res2.getInt(1);
                }
                res2.close();
            }
            else
            {
                    idAudience=res.getInt(1);
            }
            //isnert into lodata_has_primaryaud, checking if it's not inserted yet
            ResultSet res2=_stmt.executeQuery("SELECT * FROM lodata_has_PrimaryAud "+
                    " WHERE lodata_ID_LO='"+idLO+"' AND PrimaryAUd_idPrimary_Audience='"+
                    idAudience+"'");
            if(!res2.next())
            {
                String sql="INSERT INTO lodata_has_PrimaryAud (lodata_ID_LO,PrimaryAUd_idPrimary_Audience) " +
                      "VALUES (?,?)";
                //System.out.println("Insertando en lodata_has_primaryaud: " + idLO + ", " + idAudience);
                PreparedStatement addNew = _con.prepareStatement(sql);
                addNew.setInt(1,idLO);
                addNew.setInt(2,idAudience);

                addNew.executeUpdate();
                addNew.close();
                res.close();
            }
            res2.close();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException on addPrimaryAudience "+e.getMessage());
        }
    }

    /**
     * Insert into users table.
     * 
     * @param idUser
     * @param name
     * @param ribbon
     * @param author
     * @param vsb
     * @param peerReviewer
     * @param merlotAward
     * @param idCategory
     * @param memberType
     * @param lastLogin
     * @param memberSince
     * @param hasPersonalCol
     */
    public synchronized void addUsers(int idUser,String name,String ribbon,boolean author,
           boolean vsb,boolean peerReviewer,String merlotAward,int idCategory,
           String memberType,java.util.Date lastLogin,java.util.Date memberSince,boolean hasPersonalCol)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM users" +
                    " WHERE ID_User="+"'"+idUser+"'");
            if (!(res.next())) //test if exist
            {
                String sql="INSERT INTO users " +
                          "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement addNew = _con.prepareStatement(sql);
                addNew.setInt(1, idUser);
                addNew.setString(2, name);
                addNew.setString(3, ribbon);
                addNew.setBoolean(4, author);
                addNew.setBoolean(5, vsb);
                addNew.setBoolean(6, peerReviewer);
                addNew.setString(7,merlotAward);
                addNew.setInt(8, idCategory);
                addNew.setString(9,memberType);
                addNew.setDate(10, convertDate(lastLogin));
                addNew.setDate(11, convertDate(memberSince));
                addNew.setBoolean(12, hasPersonalCol);

                addNew.executeUpdate();
                addNew.close();
            }
            else
            {
                //The current user exists. We check if we have to update it
                if(res.next()){
                    java.util.Date lastLoginDB=res.getDate(10);
                    //if login date is not the same, we assume it has change so
                    //we update the users data
                    if(lastLoginDB!=lastLogin)
                    {
                        String updateSQL="UPDATE users SET "
                            + "ribbon=" + "'" + ribbon + "'"
                            + ", author=" + "'" + author + "'"
                            + ", vsb=" + "'" + vsb + "'"
                            + ", peerReviewer=" + "'" + peerReviewer + "'"
                            + ", merlotAward=" + "'" + merlotAward + "'"
                            + ", locategories_ID_Cat=" + "'" + idCategory + "'"
                            + ", memberType=" + "'" + memberType + "'"
                            + ", lastLogin=" + "'" + lastLogin + "'"
                            + ", memberSince=" + "'" + memberSince + "'"
                            + ", usersCol=" + "'" + hasPersonalCol + "'"
                            + " where ID_User=" + idUser;

                        PreparedStatement addData = (PreparedStatement) _con.prepareStatement(updateSQL);
                        addData.executeUpdate();
                        addData.close();
                    }
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("SQLException"+e.getMessage());
        }
    }
    /**
     * Insert into users_lo table. The l.o. has to exist in lodata table.
     * 
     * @param lodata_ID_LO
     * @param users_ID_User
     * @param ID_Coll
     */
    public synchronized void addUsersLo(int lodata_ID_LO,int users_ID_User,int ID_Coll)
    {
        try
        {
            _stmt = _con.createStatement();
            ResultSet res=_stmt.executeQuery("SELECT * FROM users_lo" +
                    " WHERE lodata_ID_LO="+"'"+lodata_ID_LO+"'" +
                    " AND users_ID_User="+"'"+users_ID_User+"'" +
                    " AND ID_Coll="+"'"+ID_Coll+"'");
            if (!(res.next())) //test if exist
            {
                String sql="INSERT INTO users_lo " +
                          "VALUES (?,?,?)";
                PreparedStatement addNew = _con.prepareStatement(sql);
                addNew.setInt(1, lodata_ID_LO);
                addNew.setInt(2, users_ID_User);
                addNew.setInt(3, ID_Coll);

                addNew.executeUpdate();
                addNew.close();
            }
        }
        catch(SQLException e)
        {
            if((!e.getMessage().contains("FOREIGN KEY"))&&(!e.getMessage().contains("PRIMARY")))
            {
                System.out.println("SQLException"+e.getMessage());
            }
        }
    }


    /**
     * Creates the merlot's metrics table if it doesn't exist
     */
    public void createTableMerlot(String databaseName,String databaseUser,String databasePass) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost/";
            Connection connection = DriverManager.getConnection(url, databaseUser, databasePass);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS `"+databaseName+"`.`Metrics` (" +
                    "`lodata_ID_LO` INT UNSIGNED NOT NULL ," +
                    " `Links_number` INT NULL DEFAULT 0 ," +
                    " `Links_unique_number` INT NULL DEFAULT 0 ," +
                    " `Internal_links` INT NULL DEFAULT 0 ," +
                    " `Unique_internal_links` INT NULL DEFAULT 0 ," +
                    " `External_links` INT NULL DEFAULT 0 ," +
                    " `Unique_external_links` INT NULL DEFAULT 0 ," +
                    " `Image_Number` INT NULL DEFAULT 0 ," +
                    " `Html_bytes` INT NULL DEFAULT 0 ," +
                    " `Images_Size` INT NULL DEFAULT 0 ," +
                    " `Script_number` INT NULL DEFAULT 0 ," +
                    " `Applets_Number` INT NULL DEFAULT 0 ," +
                    " `Display_word_count` INT NULL DEFAULT 0 , " +
                    "`Link_word_count` INT NULL DEFAULT 0, " +
                    "`Number_of_pages` INT NULL DEFAULT 0," +
                    "`download_files` INT NULL DEFAULT 0," +
                    "`audio_files` INT NULL DEFAULT 0," +
                    "`video_files` INT NULL DEFAULT 0," +
                    "`multimedia_files` INT NULL DEFAULT 0," +
                    "`average_number_unique_internal_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_internal_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_unique_external_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_external_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_unique_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_links` FLOAT NULL DEFAULT 0," +
                    "`average_number_words` FLOAT NULL DEFAULT 0," +
                    "`average_files_download` FLOAT NULL DEFAULT 0," +
                    "`average_audio_files` FLOAT NULL DEFAULT 0," +
                    "`average_video_files` FLOAT NULL DEFAULT 0," +
                    "`average_multimedia_files` FLOAT NULL DEFAULT 0," +
                    "`average_applets_number` FLOAT NULL DEFAULT 0," +
                    "`average_image_number` FLOAT NULL DEFAULT 0," +
                    "`average_html_bytes` FLOAT NULL DEFAULT 0," +
                    "`average_images_size` FLOAT NULL DEFAULT 0," +
                    "`average_scripts_number` FLOAT NULL DEFAULT 0," +
                    " `Page_Error` INT NULL DEFAULT 0 ," +
                    " `gunningFogIndex` FLOAT NULL DEFAULT 0 ," +
                    " `webLinkErrors` INT NULL DEFAULT 0 ," +
                    " `redundantLinks` INT NULL DEFAULT 0 ," +
                    " `popUps` INT NULL DEFAULT 0 ," +
                    " `colorCount` INT NULL DEFAULT 0 ," +
                    " `bodyWordCount` INT NULL DEFAULT 0 ," +
                    " `italicWordCount` INT NULL DEFAULT 0 ," +
                    " INDEX `fk_Metrics_lodata` (`lodata_ID_LO` ASC) ," +
                    " PRIMARY KEY (`lodata_ID_LO`) ," +
                    " CONSTRAINT `fk_Metrics_lodata`" +
                    " FOREIGN KEY (`lodata_ID_LO` )" +
                    " REFERENCES `dbcrawlermerlot`.`lodata` (`ID_LO` )" +
                    " ON DELETE NO ACTION ON UPDATE NO ACTION)" +
                    "ENGINE = InnoDB DEFAULT CHARSET=utf8;");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    /**
     * Count the URLs from lodata table
     * 
     * @return number of URLs from lodata table
     */
    public int countUrlsMerlot() {
        int nUrls = 0;
        try {
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT count(Location) FROM dbcrawlermerlot.lodata");
            res.next();
            nUrls = res.getInt(1);
            _stmt.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
        return nUrls;
    }

    /**
     * Count the number of users from users table.
     * 
     * @return number of users from users table
     */
    public int countUsersMerlot() {
        int nUsers = 0;
        try {
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT count(ID_User) FROM dbcrawlermerlot.users");
            res.next();
            nUsers = res.getInt(1);
            _stmt.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
        return nUsers;
    }

    /**
     * Create a String list of materials IDs. The list will be a class' attribute
     * 
     */
    public void createListIdLoMerlot() {
        try {
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT * FROM dbcrawlermerlot.lodata");
            while (res.next()) {
                _idLoList.addLast(res.getString("ID_LO"));
            }
            _stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    /**
     * Creates a list of Users' ids. The list will be a class' attribute
     */
    public void createListIdUserMerlot() {
        try {
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT * FROM dbcrawlermerlot.users");
            while (res.next()) {
                _idUserList.addLast(res.getString("ID_User"));
            }
            _stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    /**
     * Create a list of merlot's locations. The list will be a class' local attribute
     */
    public void createListLocationMerlot() {

        try {
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT * FROM dbcrawlermerlot.lodata");
            while (res.next()) {
                _locationList.addLast(res.getString("Location"));
            }
            _stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    /**
     * Get the next id from the list of LO's ids
     * @return id lo
     */
    public String getNextIdLo() {
        return _idLoList.removeFirst();
    }

    /**
     * Get the next id from the users' list
     * @return id user
     */
    public int getNextUserId() {
        return Integer.parseInt(_idUserList.removeFirst());
    }

    /**
     * Get the next location from the location's list
     * @return location
     */
    public String getNextLocation() {
        return _locationList.removeFirst();
    }

    /**
     * Inserts into metrics table
     * @param IdLo
     * @param nLinks
     * @param nUniqueLinks
     * @param nInternalLinks
     * @param nUniqueInternalLinks
     * @param nExternalLinks
     * @param nUniqueExternalLinks
     * @param nImgs
     * @param htmlSize
     * @param imagesSize
     * @param nScripts
     * @param nApplets
     * @param displayWordsCount
     * @param linkWordsCount
     * @param nPages
     * @param downloadFiles
     * @param audioFiles
     * @param videoFiles
     * @param multimediaFiles
     * @param averageNumberUniqueInternalLinks
     * @param averageNumberInternalLinks
     * @param averageNumberUniqueExternalLinks
     * @param averageNumberExternalLinks
     * @param averageNumberUniqueLinks
     * @param averageNumberLinks
     * @param averageWords
     * @param averageDownloadFiles
     * @param averageAudioFiles
     * @param averageVideoFiles
     * @param averageMultimediaFiles
     * @param averageApplets
     * @param averageImages
     * @param averageHtmlBytes
     * @param averageImagesSize
     * @param averageScripts
     * @param gunningFogIndex
     * @param weblinkErrors
     * @param redundantLinks
     * @param popUps
     * @param colorCount
     * @param bodyWordCount
     * @param italicWordCount
     * @throws SQLException
     */
    public void setDataMerlot(String IdLo, int nLinks, int nUniqueLinks, int nInternalLinks,
            int nUniqueInternalLinks, int nExternalLinks, int nUniqueExternalLinks, int nImgs, int htmlSize,
            int imagesSize, int nScripts, int nApplets, int displayWordsCount,
            int linkWordsCount, int nPages, int downloadFiles, int audioFiles,
            int videoFiles, int multimediaFiles, float averageNumberUniqueInternalLinks,
            float averageNumberInternalLinks, float averageNumberUniqueExternalLinks,
            float averageNumberExternalLinks, float averageNumberUniqueLinks, float averageNumberLinks,
            float averageWords, float averageDownloadFiles, float averageAudioFiles,
            float averageVideoFiles, float averageMultimediaFiles, float averageApplets, float averageImages,
            float averageHtmlBytes, float averageImagesSize, float averageScripts,
            float gunningFogIndex,int weblinkErrors,int redundantLinks,int popUps,
            int colorCount,int bodyWordCount,int italicWordCount) throws SQLException {

        try {
            int pageError = 0;
            if (htmlSize == 0) {
                pageError = 1;
            }
            _stmt = _con.createStatement();
            ResultSet res = _stmt.executeQuery("SELECT count(*) FROM dbcrawlermerlot.Metrics where lodata_ID_LO=" + IdLo);
            res.next();
            if ((res.getInt(1)) == 0) { //If learning object no exists, create it
                String insertScript = ("INSERT INTO dbcrawlermerlot.Metrics VALUES " +
                 "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                PreparedStatement addData = _con.prepareStatement(insertScript);
                addData.setString(1, IdLo);
                addData.setInt(2, nLinks);
                addData.setInt(3, nUniqueLinks);
                addData.setInt(4, nInternalLinks);
                addData.setInt(5, nUniqueInternalLinks);
                addData.setInt(6, nExternalLinks);
                addData.setInt(7, nUniqueExternalLinks);
                addData.setInt(8, nImgs);
                addData.setInt(9, htmlSize);
                addData.setInt(10, imagesSize);
                addData.setInt(11, nScripts);
                addData.setInt(12, nApplets);
                addData.setInt(13, displayWordsCount);
                addData.setInt(14, linkWordsCount);
                addData.setInt(15, nPages);
                addData.setInt(16, downloadFiles);
                addData.setInt(17, audioFiles);
                addData.setInt(18, videoFiles);
                addData.setInt(19, multimediaFiles);
                addData.setFloat(20, averageNumberUniqueInternalLinks);
                addData.setFloat(21, averageNumberInternalLinks);
                addData.setFloat(22, averageNumberUniqueExternalLinks);
                addData.setFloat(23, averageNumberExternalLinks);
                addData.setFloat(24, averageNumberUniqueLinks);
                addData.setFloat(25, averageNumberLinks);
                addData.setFloat(26, averageWords);
                addData.setFloat(27, averageDownloadFiles);
                addData.setFloat(28, averageAudioFiles);
                addData.setFloat(29, averageVideoFiles);
                addData.setFloat(30, averageMultimediaFiles);
                addData.setFloat(31, averageApplets);
                addData.setFloat(32, averageImages);
                addData.setFloat(33, averageHtmlBytes);
                addData.setFloat(34, averageImagesSize);
                addData.setFloat(35, averageScripts);
                addData.setFloat(36, pageError);
                //vactar
                addData.setFloat(37, gunningFogIndex);
                addData.setInt(38, weblinkErrors);
                addData.setInt(39, redundantLinks);
                addData.setInt(40, popUps);
                addData.setInt(41, colorCount);
                addData.setInt(42, bodyWordCount);
                addData.setInt(43, italicWordCount);
                addData.executeUpdate();
                addData.close();
            } else { //If learning Object exists, update it
                System.out.println("Learning Object: " + IdLo + " already exist, Updating...");
                String UpdateScript = ("UPDATE dbcrawlermerlot.Metrics SET Links_Number=" + nLinks +
                        ", Links_unique_number=" + nUniqueLinks +
                        ", Internal_links=" + nInternalLinks +
                        ", Unique_internal_links=" + nUniqueInternalLinks +
                        ", External_links=" + nExternalLinks +
                        ", Unique_external_links=" + nUniqueExternalLinks +
                        ", Image_Number=" + nImgs +
                        ", Html_bytes=" + htmlSize +
                        ", Images_Size=" + imagesSize +
                        ", Script_Number=" + nScripts +
                        ", Applets_Number=" + nApplets +
                        ", Display_word_count=" + displayWordsCount +
                        ", Link_word_count=" + linkWordsCount +
                        ", Number_of_pages=" + nPages +
                        ", download_files=" + downloadFiles +
                        ", audio_files=" + audioFiles +
                        ", video_files=" + videoFiles +
                        ", multimedia_files=" + multimediaFiles +
                        ", average_number_unique_internal_links=" + averageNumberUniqueInternalLinks +
                        ", average_number_internal_links=" + averageNumberInternalLinks +
                        ", average_number_unique_external_links=" + averageNumberUniqueExternalLinks +
                        ", average_number_external_links=" + averageNumberExternalLinks +
                        ", average_number_unique_links=" + averageNumberUniqueLinks +
                        ", average_number_links=" + averageNumberLinks +
                        ", average_number_words=" + averageWords +
                        ", average_files_download=" + averageDownloadFiles +
                        ", average_audio_files=" + averageAudioFiles +
                        ", average_video_files=" + averageVideoFiles +
                        ", average_multimedia_files=" + averageMultimediaFiles +
                        ", average_applets_number=" + averageApplets +
                        ", average_image_number=" + averageImages +
                        ", average_html_bytes=" + averageHtmlBytes +
                        ", average_images_size=" + averageImagesSize +
                        ", average_scripts_number=" + averageScripts +
                        ", Page_Error=" + pageError +
                        " WHERE lodata_ID_LO=" + IdLo);
                PreparedStatement addData = _con.prepareStatement(UpdateScript);
                addData.executeUpdate();
                addData.close();
            }
            _stmt.close();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    /*
     * Function: updateErrorFieldMerlot
     * Description: Sets the error type
     * In: id_Lo to set the error and typeError to set the concrete error.
     * Out: void
     *
     * **********ERROR DEFINITION:****************
     * 1-> Page not accesible (Not found).       *
     * 2-> Page not recognized (like .mhtml).    *
     * 3-> Unknown error                         *
     * *******************************************
     */
    /**
     *
     * @param idLo
     * @param typeError
     * @throws SQLException
     */
    public void updateErrorFieldMerlot(String idLo, int typeError) throws SQLException {
        String PageNotFoundScript = ("UPDATE dbcrawlermerlot.Metrics SET Page_Error=" + typeError + " WHERE lodata_ID_LO=" + idLo);
        _stmt = _con.createStatement();
        _stmt.executeUpdate(PageNotFoundScript);
        _stmt.close();
    }

    /**
     * Closes the connection to the database
     */
    public void closeConnection() {

        try {
            System.out.println("Connection with DB closed");
            _con.close();
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

}
