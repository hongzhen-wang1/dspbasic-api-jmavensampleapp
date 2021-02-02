package sample.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 
import java.util.Properties;  

public class DBUtil {
	//dockerコンテナ上のpropertiesファイルのパス→Dockerfileで設定したもの
	private static final String DIR = "/opt/ibm/wlp/usr/servers/defaultServer/apps/";
	private static final String DEFAULT_ENCODING = "UTF-8";
    private static String url = null; 
    private static String driver = null; 
    private static String username = null; 
    private static String password = null; 
       
    static{ 
        Properties p = new Properties();//プロパティファイルを読み込み
        InputStream inputStream = null; 
        try { 
        	inputStream = new BufferedInputStream(new FileInputStream(new File(DIR +"dataSource.properties"))); 
        	p.load(new InputStreamReader(inputStream, DEFAULT_ENCODING));
            url = p.getProperty("url"); 
            driver = p.getProperty("driver"); 
            username = p.getProperty("username"); 
            password = p.getProperty("password"); 
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        }finally{ 
            try { 
            	if(inputStream != null) {
                inputStream.close();
            	}
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
    //Threadを生成
    protected static ThreadLocal<Connection>  threadLocalCon = new ThreadLocal<Connection>(); 
       
    /*
     * DBに接続
     */ 
    public static Connection getCon() { 
   
        Connection con = threadLocalCon.get(); 
        try { 
            if (con == null || con.isClosed()) { 
            	System.out.print(driver);
                Class.forName(driver); 
                con = DriverManager.getConnection(url, username, password); 
                threadLocalCon.set(con); 
            } 
        } catch (ClassNotFoundException e) { 
            e.printStackTrace(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        return con; 
    } 
       
    /*
     * close ResultSet
     */ 
    public static void closeResultSet(ResultSet rs){ 
        if(rs != null){ 
            try { 
                rs.close(); 
            } catch (SQLException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
    /*
     * close Statement
     */ 
    public static void closeStatement(Statement st){ 
        if(st != null){ 
            try { 
                st.close(); 
            } catch (SQLException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
    /*
     * close Connection
     */ 
    public static void closeConnectionIfAutoCommit(Connection con){ 
        if(con != null){ 
        try { 
            if(con.getAutoCommit()){         
                con.close(); 
            } 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
    } 
    } 
    /*
     * close ResultSet、Statement、Connection
     */ 
    public static void close(ResultSet rs,Statement st,Connection con){ 
        closeResultSet(rs); 
        closeStatement(st); 
        closeConnectionIfAutoCommit(con); 
    }  

}
