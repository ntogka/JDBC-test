import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;
import java.util.Scanner;

public class Test2 {

    public static void main(String[] args) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        Scanner scanner = null;
        ResultSet rs = null;

        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@db12c.qnr.com.gr:1521/p12c.qnr.com.gr");
            ds.setUser("modesto");
            ds.setPassword("modesto");
            con = ds.getConnection();

            pst = con.prepareStatement("insert into party (PR_ID)" +
                    "select party_seq.nextval from dual;");
            pst = con.prepareStatement("insert into PARTY_DETAILS (PRD_ID)" +
                    "select party_details_seq.nextval from dual;");
            pst = con.prepareStatement("insert into PARTY_DETAILS (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Give me the Name ");
                String prd_name_en  = scanner.next();

                System.out.print("Give me the EIC ");
                String prd_eic  = scanner.next();

                System.out.print("Give me the Effective Date ");
                String prd_effective_date  = scanner.next();

                //pst.setInt(1, Integer.parseInt("newValue"));
                pst.setString(4, prd_name_en);
                pst.setString(2, prd_eic);
                pst.setString(13, prd_effective_date); //error an einai Date
                //pst.setInt(5, Integer.parseInt("new"));

                int rowCount = pst.executeUpdate(); //error
                if( rowCount == 1) {
                    System.out.println(prd_name_en + "Inserted Successfully");
                    System.out.println("One more Insertion? [Yes/No]    : ");
                    String option = scanner.next();
                    if (option.equalsIgnoreCase("yes")) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    System.out.println(prd_name_en + "Insertion Failure");
                }

            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {
            try {
                //scanner.close();
                pst.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
