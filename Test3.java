import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Scanner;

public class Test3 {

    public static void main(String[] args) {
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

            scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Date : ");
                String date  = scanner.next();

                System.out.print("Object Version [1/2/3]: ");
                int version  = scanner.nextInt();
                if (version == 1 || version == 2 || version == 3) {
                    continue;
                } else {
                    System.out.println("Invalid version.");
                    break;
                }

                System.out.print("RTU time : ");
                String hour = scanner.next();



                System.out.print("Action [U/D]: ");
                String action  = scanner.next();
                if (action.equalsIgnoreCase("U")) {
                    //+A or -A kai timi energeias
                    pst = con.prepareStatement("update work_energy set where we_trade_date = " + date +
                            "and we_hour = " + hour + "and we_obj_version = " + version);
                    pst.executeUpdate();
                    System.out.println("Updated");
                    break;
                } else if (action.equalsIgnoreCase("D")) {
                    pst = con.prepareStatement("delete from work_energy where we_trade_date = " + date +
                            "and we_hour = " + hour + "and we_obj_version = " + version);
                    pst.executeUpdate();
                    System.out.println("Deleted");
                    break;
                } else {
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
