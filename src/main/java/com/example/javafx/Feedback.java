package com.example.javafx;

import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.Rating;

import java.sql.SQLException;
import java.sql.Statement;


public class Feedback {

    @FXML
    private Button feedbtn;
    @FXML
    private TextArea feedtextarea;
    @FXML
    private Rating rating;
    @FXML
    private TextField nameinput;
    @FXML
    private  TextField emailinput;
    private  Long star;
    private String msg;
    private String name;
    private String email;
    private  Integer result;
    @FXML
    private Label response;
    public  void submitFeedback() throws SQLException, ClassNotFoundException {
        ConnectionClass connectionclass = new ConnectionClass();
        Connection connection = (Connection) connectionclass.getConnection();
        System.out.println("connection" +connection);
        star =  Math.round(rating.getRating());
        msg = feedtextarea.getText();
        name = nameinput.getText();
        email = emailinput.getText();


        String sql  = "INSERT INTO feedback(name,msg,star,email) VALUES('"+name+"','"+msg+"',"+star+",'"+email+"')";
        Statement st = connection.createStatement();
        System.out.println(sql);
        result = st.executeUpdate(sql);

        if(result == 1)
        {
            System.out.println(result);
            response.setText("Your response has been submitted !");
            rating.setRating(0.0);
            feedtextarea.setText("");
            nameinput.setText("");
            emailinput.setText("");
        }else{
            System.out.println(result);
            response.setText("Something went wrong !");
        }



    }





}
