import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

            int id = 0;
            int mtrId = 0;

            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            String q1 = "select wec_id from work_energy_control where wec_trade_date = ? and wec_version = ?";
            String q2 = "select mtrd_mtr_id from meter_details where mtrd_rtu = ? and mtrd_main_check = 'M'";
            String query1 = "delete from work_energy where we_wec_id = ? and we_hour = ? and we_mtr_id = ?";
            String query2 = "update work_energy set we_pa_energy = we_pa_energy + ?, we_ma_energy = we_ma_energy + ? where we_wec_id = ? and we_hour = ? and we_mtr_id = ?";

            scanner = new Scanner(System.in);

            System.out.print("Date : ");
                String date = scanner.next();
                java.sql.Date sqlDate = parseUserDate(date);
                if (sqlDate == null)
                    sqlDate = startDate;

                System.out.print("Object Version [1/2/3]: ");
                int version = scanner.nextInt();
                int i = 0;
                while (i == 0) {
                    if (version == 1 || version == 2 || version == 3){
                        System.out.println("Good");
                        i ++;
                    }else {
                        System.out.print("Invalid version. Try again : ");
                        version = scanner.nextInt();
                    }
                }

                System.out.print("RTU : ");
                String rtu = scanner.next();
                if (rtu.equals("0")) {
                    System.exit(0);
                }

                System.out.print("Time : ");
                String hour = scanner.next();

                int m = 0;
                while (m == 0){
                    if (hour.matches("([01]?[1-9]|2[0-4]):([0][0]|[1][5]|[3][0]|[4][5])")) {
                        System.out.println("Good");
                        m++;
                    } else {
                        System.out.println("Invalid time. Try again.");
                        hour = scanner.next();
                    }
                }


                pst = con.prepareStatement(q1);
                pst.setDate(1, sqlDate);
                pst.setInt(2, version);
                rs = pst.executeQuery();
                while (rs.next()) {
                    id = rs.getInt("wec_id");
                    System.out.println(id);
                }

                pst = con.prepareStatement(q2);
                pst.setString(1, rtu);
                rs = pst.executeQuery();
                while (rs.next()) {
                    mtrId = rs.getInt("mtrd_mtr_id");
                    System.out.println(mtrId);
                }

                System.out.print("Action [U/D]: ");
                String action = scanner.next();
                int y = 0;
                while (y == 0) {
                    if (action.equalsIgnoreCase("U")) {

                        System.out.print("+A or -A: ");
                        String A = scanner.next();
                        System.out.print("Price: ");
                        int price = scanner.nextInt();
                        int zero = 0;
                        int z = 0;
                        while (z == 0) {
                            if (A.equalsIgnoreCase("+A")) {
                                pst = con.prepareStatement(query2);
                                pst.setInt(1, price);
                                pst.setInt(2, zero);
                                pst.setInt(3, id);
                                pst.setString(4, hour);
                                pst.setInt(5, mtrId);


                                int rowCountpA = pst.executeUpdate();
                                System.out.println("rowCountpAUpdate = " + rowCountpA);
                                System.out.println("Updated");
                                z++;
                            } else if (A.equalsIgnoreCase("-A")) {
                                pst = con.prepareStatement(query2);
                                pst.setInt(1, zero);
                                pst.setInt(2, price);
                                pst.setInt(3, id);
                                pst.setString(4, hour);
                                pst.setInt(5, mtrId);

                                //System.out.println(query2);
                                int rowCountmA = pst.executeUpdate();
                                System.out.println("rowCountmAUpdate = " + rowCountmA);
                                System.out.println("Updated");
                                z++;
                            } else {
                                System.out.print("Invalid. Try again [-A or +A] : ");
                                A = scanner.next();
                            }
                        }
                        y++;

                    } else if (action.equalsIgnoreCase("D")) {

                        pst = con.prepareStatement(query1);
                        pst.setInt(1, id);
                        pst.setString(2, hour);
                        pst.setInt(3, mtrId);

                        if (pst.execute()) {
                            System.out.println("Deleted");
                        }

                        y++;
                    } else {
                        System.out.print("Invalid. Try again: ");
                        action = scanner.next();
                    }
                }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                scanner.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static java.sql.Date parseUserDate(String dt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //09/12/2021
            java.util.Date myDate = sdf.parse(dt);
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
