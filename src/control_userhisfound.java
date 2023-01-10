import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class control_userhisfound {

    @FXML
    private Label brandname;
    @FXML
    private TextField brandname_tf;
    @FXML
    private Label colour;
    @FXML
    private TextField colour_tf;
    @FXML
    private Label description1;
    @FXML
    private Label location_l;
    @FXML
    private TextField location_tf;
    @FXML
    private Label lostdate;
    @FXML
    private TextField lostdate_tf;
    @FXML
    private Label name;
    @FXML
    private TextField name_tf;
    @FXML
    private TextField notverified;
    @FXML
    private Button nxtbutton;
    @FXML
    private Button previousbutton;
    @FXML
    private Rectangle rectangle;
    @FXML
    private Button remove;
    @FXML
    private TextArea textarea1;
    @FXML
    private Label title;
    @FXML
    private TextField verified;
    @FXML
    private TextField notfound;

    Connection con;
    Statement st;
    Statement stttt;
    static ResultSet rs;
    static ResultSet rs1;
    String s;
    App ob = new App();

    void setresultset() throws Exception {
        rs = st.executeQuery(s);
        if (rs.next())
            load();
    }

    @FXML
    void ongohome(ActionEvent event) throws Exception{
        App ob=new App();
        ob.openfile("option");
    }
    void checkverify() throws Exception {
        notfound.setVisible(false);
        rs1 = stttt.executeQuery("select *from verification where itemid=" + rs.getInt("id"));
        if (!(rs1.next()))
            notfound.setVisible(true);
        else if (rs1.getString("status").equals("yes")) {
            verified.setVisible(true);
            notverified.setVisible(false);
            notfound.setVisible(false);
        } else {
            verified.setVisible(false);
            notverified.setVisible(true);
            notfound.setVisible(false);
        }
    }

    @FXML
    void initialize() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfind", "root", "Karthi@2004");
        st = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        stttt = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        s = "select *from founditem where email='" + Main.mail + "' order by id desc";
        setresultset();
    }

    @FXML
    void nextbutton(ActionEvent event) throws Exception {
        if (rs.next())
            load();
    }

    @FXML
    void onlostitem(ActionEvent event) throws Exception {
        ob.openfile("userhistorylost");
    }

    @FXML
    void onremove(ActionEvent event) throws Exception {
        st.execute("delete from founditem where id=" + rs.getInt("id"));
        if (!notfound.isVisible())
            st.execute("delete from verification where id=" + rs.getInt("id"));
        s = "select *from founditem where email='" + Main.mail + "' order by id desc";
        setresultset();
    }

    @FXML
    void previousbut(ActionEvent event) throws Exception {
        if (rs.previous())
            load();
    }

    void load_dbimg_to_img() throws Exception {
        // fteching image from database and converting to image
        InputStream is = rs.getBinaryStream("image");// converting into binary
        OutputStream os = new FileOutputStream(
                new File("C:/Users/User/OneDrive/Desktop/test/test/testpro/src/img.jpeg"));
        byte[] b = new byte[1024];
        int size = 0;
        while ((size = is.read(b)) != -1) {
            os.write(b, 0, size);
        }
        os.close();
        is.close();
    }

    void load() throws Exception {
        checkverify();
        File f = new File("C:/Users/User/OneDrive/Desktop/test/test/testpro/src/img.jpeg");
        f.delete();

        load_dbimg_to_img();
        // loading image into rectangle
        Image im = new Image("C:/Users/User/OneDrive/Desktop/test/test/testpro/src/img.jpeg");
        rectangle.setFill(new ImagePattern(im));

        // setting up the feilds
        textarea1.setText(rs.getString("descriptions"));
        name_tf.setText(rs.getString("itemname"));
        lostdate_tf.setText(rs.getString("lostdate"));
        location_tf.setText(rs.getString("location"));
        brandname_tf.setText(rs.getString("brandname"));
        colour_tf.setText(rs.getString("color"));
    }
}
