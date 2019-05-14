package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.*;

public class Controller {

    // Database information
    private static final String connectionURL = "jdbc:mysql://www.db4free.net:3306/jokedb?verifyServerCertificate=false&useSSL=false";
    private static final String dbUsername = "andersnelson";
    private static final String dbPassword = "Dunwoody1!";

    //Empty arraylist for objects
    public  List<Joke> jokeList = new ArrayList<>();

    @FXML
    private Button newjoke_btn;
    @FXML
    private TextField jokeListText;
    @FXML
    private ListView<Joke> jokeTableView;

    @FXML
    private TextField search_box;
    @FXML
    private Button search_btn;
    @FXML
    private Button clear_btn;
    @FXML
    private Button database_btn;






    public void newjoke_btn_clicked(MouseEvent mouseEvent) {
//      setting up connection to web api
        try{
            URL url = new URL("https://icanhazdadjoke.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if(connection.getResponseCode()!=200){
                throw new RuntimeException("Failed: HTTP error code: "+ connection.getResponseCode());
            }
            //Creating JSON object
            Object obj = new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
            JSONObject jsonObject = (JSONObject) obj;

            // Extracting "joke" object from JSON
            Joke joke = new Joke ((String)jsonObject.get("joke"));

            //Adding object to list
            jokeList.add(joke);
//            jokeList.add(new Joke ("test 1"));
//            jokeList.add(new Joke ("test 2"));
//            jokeList.add(new Joke ("test 3"));
//            jokeList.add(new Joke ("test 4"));

//            joke_lb.setText(jokeList.get(jokeIterator).getJoke());

            //Creating observable list to feed into listview
            ObservableList<Joke> jokeListObservable = FXCollections.observableArrayList(jokeList);
            jokeTableView.setEditable(true);


            jokeTableView.setItems(jokeListObservable);
//            System.out.println(jokeList.size());
//            System.out.println(jokeListObservable.size());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void search_btn_clicked(MouseEvent mouseEvent) {
        String searchText;
        searchText = search_box.getText().toLowerCase();

        //Use of streams, converts jokes to lower case to match against searchText.  Puts jokes in different list to display.
        List<Joke> jokeSearchList = jokeList.stream().filter(p -> p.getJoke().toLowerCase().contains(searchText)).collect(Collectors.toList());
//        System.out.println(searchText);
        ObservableList<Joke>jokeSearchListListObservable = FXCollections.observableArrayList(jokeSearchList);
        jokeTableView.setItems(jokeSearchListListObservable);
    }

    public void clear_btn_clicked(MouseEvent mouseEvent) {
        //Reset to other list, reset search box text
        ObservableList<Joke> jokeListObservable = FXCollections.observableArrayList(jokeList);
        jokeTableView.setItems(jokeListObservable);
        search_box.setText("");

    }

    public void database_btn_clicked(MouseEvent mouseEvent) throws SQLException {

        //Select particular object
        Joke selectedJoke = jokeTableView.getSelectionModel().getSelectedItem();
//        System.out.println(selectedJoke.getJoke());
        //Sets up connection to DB4free SQL database
        Connection conn = null;
        try {


            conn = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
//            System.out.println("connected to database");

            //SQL Insert statement
            String sql = "INSERT INTO Jokes (Joke)" + "VALUES (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, selectedJoke.getJoke());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

            //Alert saying joke has been added
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Database Connection");
            alert.setContentText("Record inserted successfully!");
            alert.show();
        }
        catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        finally {
            if(conn != null){
                conn.close();
            }
        }
    }

    public void retrieve_btn_clicked(MouseEvent mouseEvent) throws SQLException {

        //Setting up SQL connection
        ObservableList<Joke> jokeObservableList = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String joke = "";
        //Selects all jokes from database
        String SQLquery = "SELECT Joke from Jokes";
        try{
            connection = DriverManager.getConnection(connectionURL,dbUsername,dbPassword);
            System.out.println("Successfully connected to database");
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQLquery);
            while (resultSet.next()){
                //Gets string from only column in database
                joke = resultSet.getString(1);
                //Adds all strings to a list
                jokeObservableList.add(new Joke(joke));
            }

            //Displays in table
            jokeTableView.setItems(jokeObservableList);


        }
        catch (SQLException e){
            System.err.println(e);
        }

        //Cleaning up
        finally {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }


    public void delete_btn_clicked(MouseEvent mouseEvent) throws SQLException {
        //Select particular object
        Joke selectedJoke = jokeTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedJoke.getJoke());
        //Sets up connection to DB4free SQL database
        Connection conn = null;
        try {


            conn = DriverManager.getConnection(connectionURL, dbUsername, dbPassword);
            System.out.println("connected to database");

            //SQL Insert statement
            String sql = "DELETE from Jokes where Joke=(?)";
            System.out.println(sql);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            System.out.println(preparedStatement.toString());
            preparedStatement.setString(1, selectedJoke.getJoke());
            System.out.println(preparedStatement.toString());

            preparedStatement.executeUpdate();

            //Alert saying joke has been added
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Database Connection");
            alert.setContentText("Record deleted successfully!");
            alert.show();
        }
        catch (SQLException e){
            System.out.println("error");
            System.out.println(e.getErrorCode());
        }
        finally {
            if(conn != null){
                conn.close();
            }
        }
    }
}
