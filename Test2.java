import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Test2 {

    public static void main(String[] args) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        Scanner scanner = null;
        ResultSet rs = null;
        Statement st = null;

        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@db12c.qnr.com.gr:1521/p12c.qnr.com.gr");
            ds.setUser("modesto");
            ds.setPassword("modesto");
            con = ds.getConnection();
            int parentId = 0;
            int detailId = 0;

            // create a sql date object
            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            st = con.createStatement();


            rs = st.executeQuery("select party_seq.nextval as newVal1 from dual");
            while(rs.next()) {
                parentId = rs.getInt("newVal1");
                System.out.println(parentId);
            }


            rs = st.executeQuery("select party_details_seq.nextval as newVal2 from dual");
            while(rs.next()) {
                detailId = rs.getInt("newVal2");
                System.out.println(detailId);
            }
            String masterQuery = "insert into PARTY (pr_id) values (" + parentId + ")";

            String query = "insert into PARTY_DETAILS (prd_id,prd_pr_id,prd_name_en, prd_eic, prd_effective_date) values (" + detailId + "," + parentId + ",?,?,?)";
            //System.out.println(query);
            scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Give me the Name ");
                String prd_name_en  = scanner.next();

                System.out.print("Give me the EIC ");
                String prd_eic  = scanner.next();

                System.out.print("Give me the Effective Date ");
                String prd_effective_date  = scanner.next();
                java.sql.Date sqlDate = parseUserDate(prd_effective_date);
                if(sqlDate==null)
                    sqlDate = startDate;

                pst = con.prepareStatement(masterQuery);
                int rowCountMaster = pst.executeUpdate();
                System.out.println("rowCountMaster = "+rowCountMaster);
                pst = con.prepareStatement(query);
                pst.setString(1, prd_name_en);
                pst.setString(2, prd_eic);
                pst.setDate(3, sqlDate);


                int rowCount = pst.executeUpdate();
                if(rowCount == 1) {
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
                scanner.close();
                pst.close();
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static java.sql.Date parseUserDate(String dt){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //09/12/2021
            java.util.Date myDate = sdf.parse(dt);
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            return sqlDate;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
