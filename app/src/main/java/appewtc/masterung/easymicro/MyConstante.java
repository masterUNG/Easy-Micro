package appewtc.masterung.easymicro;

/**
 * Created by masterUNG on 11/6/2016 AD.
 */

public class MyConstante {

    //Explicit
    private String hostString = "ftp.swiftcodingthai.com";
    private int portAnInt = 21;
    private String userString = "mic@swiftcodingthai.com";
    private String passwordString = "Abc12345";
    private String urlAddUser = "http://swiftcodingthai.com/mic/add_user_master_sommai.php";
    private String urlJSoN = "http://swiftcodingthai.com/mic/get_user_master.php";

    public String getUrlJSoN() {
        return urlJSoN;
    }

    public String getUrlAddUser() {
        return urlAddUser;
    }

    public String getHostString() {
        return hostString;
    }

    public int getPortAnInt() {
        return portAnInt;
    }

    public String getUserString() {
        return userString;
    }

    public String getPasswordString() {
        return passwordString;
    }
}   // Main Class
