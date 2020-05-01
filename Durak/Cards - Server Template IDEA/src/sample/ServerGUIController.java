/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;


import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.JOptionPane;
import socketfx.Constants;
import socketfx.FxSocketServer;
import socketfx.SocketListener;

//1) сделать кончаемую деку
//2) добавить возможность делать свой ход, пикать карты
//3) добавить правила в игру
//3) если будет время сделать игру на 3 и более человек

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class ServerGUIController implements Initializable {


//    @FXML
//    private Button sendButton;
//    @FXML
//    private TextField sendTextField;
    @FXML
    private Button connectButton;
    @FXML
    private Button passbtn;
    @FXML
    private Button takebtn;
    @FXML
    private TextField portTextField;
    @FXML
    private Label lblMessages, decknumber, tlbl;
    @FXML
    private ImageView imgS0,imgS1,imgS2,imgS3,imgS4,imgS5,imgS01,imgS11,imgS21,imgS31, imgS41, imgS51,

                      imgC0,imgC1,imgC2,imgC3,imgC4, imgC5,imgC01,imgC11,imgC21,imgC31, imgC41, imgC51,
                              imgT0, imgT1, imgT2, imgT3, imgT4, imgT5,
                              imgT01, imgT11, imgT21, imgT31, imgT41, imgT51, imgD;


    

//    private String n1, n2;
//    private boolean cClicked,sClicked;

    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private boolean isConnected, turn = true, serverUNO = false, clientUNO = false;
    private int counter = 0;
    private String color;

    public enum ConnectionDisplayState {
           
        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketServer socket;

    private void connect() {
        socket = new FxSocketServer(new FxSocketListener(),
                Integer.valueOf(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//                connectButton.setDisable(false);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                connectButton.setDisable(true);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case CONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//            case AUTOCONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passbtn.setDisable(true);
        takebtn.setDisable(true);
        isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            System.out.println("Message received server");
            if(line.substring(0,2).equals("C3")){
                t++;
                ptable(line.substring(2));
                tlbl.setText("Your turn");
        }
            else if(line.substring(0,2).equals("C4")){
                updateD(line.substring(2));
            }
            else if(line.substring(0,2).equals("C6")){
                t=0;
                clean();
            }
            else if(line.substring(0,2).equals("P1")){
                t=0;
                clean();
            }
            else if(line.substring(0,2).equals("F1")){
                fullfill();
                disable();
            }
            else if(line.substring(0,2).equals("A2")){
                attack=2;
            }
            else if(line.substring(0,2).equals("W1")){
                lblMessages.setText("You lost! Press -Deal- to start a new game");
                disableall();
                passbtn.setDisable(true);
                takebtn.setDisable(true);
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
           
        }
    }




    @FXML
    private void handleKeyPress(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER){
            take();
        }
    }
    @FXML
    private void handleConnectButton(ActionEvent event) {
        connectButton.setDisable(true);

        displayState(ConnectionDisplayState.WAITING);
        connect();
    }










    @FXML
    private Button starting;
    @FXML
    private Button dealing;
    @FXML
    private void handleStart(ActionEvent event)
    {
        deck.clear();
        portTextField.setVisible(false);
        connectButton.setVisible(false);
        imgS0.setImage(new Image("resources/BACK-1.jpg"));
        imgS1.setImage(new Image("resources/BACK-1.jpg"));
        imgS2.setImage(new Image("resources/BACK-1.jpg"));
        imgS3.setImage(new Image("resources/BACK-1.jpg"));
        imgS4.setImage(new Image("resources/BACK-1.jpg"));
        imgS5.setImage(new Image("resources/BACK-1.jpg"));
        imgC0.setImage(new Image("resources/BACK-1.jpg"));
        imgC1.setImage(new Image("resources/BACK-1.jpg"));
        imgC2.setImage(new Image("resources/BACK-1.jpg"));
        imgC3.setImage(new Image("resources/BACK-1.jpg"));
        imgC5.setImage(new Image("resources/BACK-1.jpg"));
        imgC4.setImage(new Image("resources/BACK-1.jpg"));
        for(int i = 1;i<10;i++)
        {
            deck.add(new Card("C" +Integer.toString(i+5)));
            deck.add(new Card("D"+Integer.toString(i+5)));
            deck.add(new Card("H"+Integer.toString(i+5)));
            deck.add(new Card("S"+Integer.toString(i+5)));
        }
        hand1I.add(imgC0);
        hand1I.add(imgC1);
        hand1I.add(imgC2);
        hand1I.add(imgC3);
        hand1I.add(imgC4);
        hand1I.add(imgC5);
        hand1I.add(imgC01);
        hand1I.add(imgC11);
        hand1I.add(imgC21);
        hand1I.add(imgC31);
        hand1I.add(imgC41);
        hand1I.add(imgC51);
        hand2I.add(imgS0);
        hand2I.add(imgS1);
        hand2I.add(imgS2);
        hand2I.add(imgS3);
        hand2I.add(imgS4);
        hand2I.add(imgS5);
        hand2I.add(imgS01);
        hand2I.add(imgS11);
        hand2I.add(imgS21);
        hand2I.add(imgS31);
        hand2I.add(imgS41);
        hand2I.add(imgS51);
        table1.add(imgT0);
        table1.add(imgT1);
        table1.add(imgT2);
        table1.add(imgT3);
        table1.add(imgT4);
        table1.add(imgT5);
        table2.add(imgT01);
        table2.add(imgT11);
        table2.add(imgT21);
        table2.add(imgT31);
        table2.add(imgT41);
        table2.add(imgT51);
        starting.setDisable(true);
        msg = "C1";
        socket.sendMessage(msg);
        msg="";

    }
    String msg = "";
    @FXML
    private void handleDeal(ActionEvent event)
    {   passbtn.setDisable(false);
        takebtn.setDisable(false);
        lblMessages.setText("");
        t=0;
        hand1D.clear();
        hand2D.clear();
        table1D.clear();
        table2D.clear();
        deck.clear();
        for(int i = 1;i<10;i++)
        {
            deck.add(new Card("C" +Integer.toString(i+5)));
            deck.add(new Card("D"+Integer.toString(i+5)));
            deck.add(new Card("H"+Integer.toString(i+5)));
            deck.add(new Card("S"+Integer.toString(i+5)));
        }
        for(ImageView x:table1){
            x.setImage(null);
        }
        for(ImageView x:table2){
            x.setImage(null);
        }
        for(ImageView x:hand1I){
            x.setImage(null);
        }
        for(ImageView x:hand2I){
            x.setImage(null);
        }
        msg="C2";
        for(int i = 0;i<6;i++)
        {
            int y = (int)(Math.random()*(deck.size()-i));
            hand1I.get(i).setImage(new Image(deck.get(y).getCardPath()));
            hand1D.add(deck.remove(y));
//            hand1D.add(deck.get(y));

        }
        for(int i = 0;i<6;i++)
        {

            int y = (int)(Math.random()*(deck.size()-i));
            hand2I.get(i).setImage(new Image(deck.get(y).getCardPath()));
            hand2D.add(deck.remove(y));
//            hand2D.add(deck.get(y));
        }
        for (int i=0; i<6;i++)
            hand1I.get(i).setImage(new Image("resources/BACK-1.jpg"));
        for(int i=0; i<6; i++){
            msg+=hand1D.get(i).cName+"_";
        }
        for(int i=0; i<6; i++){
            msg+=hand2D.get(i).cName+"_";
        }
        Collections.shuffle(deck);
        socket.sendMessage(msg);
        msg="";
        Ace=deck.get(deck.size()-1);
        imgD.setImage(new Image(Ace.pathName));
        decknumber.setText(String.valueOf(deck.size()));
        msg="A5";
        msg+=Ace.cName;
        socket.sendMessage(msg);
        disable();
    }
    Card Ace;
    @FXML
    private void handleDiscard(MouseEvent event)
    {
        lblMessages.setText("");
        if(t>=13){
            lblMessages.setText("There is no space");
        }
        else{
        if(t%2==0){

            System.out.println("test");
            int imgClicked;
            int row;

            imgClicked =GridPane.getColumnIndex((ImageView) event.getSource());
            row =GridPane.getRowIndex((ImageView) event.getSource());
            if(((ImageView) event.getSource()).isDisabled()){
                return;
            }
            if(attack==2){
                if(!canattack(hand2D.get(imgClicked+((row)*6)))){
                    lblMessages.setText("You can't choose this card");
                    return;
                }
            }
            if(attack==1){
                if(!candefend(hand2D.get(imgClicked+((row)*6)))){
                    lblMessages.setText("You can't choose this card");
                    return;
                }
            }
            t++;
            discard = hand2D.remove(imgClicked+((row)*6));
            table2D.add(discard);
            for(int i=0;i<table2D.size();i++)
                table2.get(i).setImage(new Image(table2D.get(i).getCardPath()));
            for(ImageView x: hand2I)
                x.setImage(null);
            for(int i = 0;i<hand2D.size();i++)
            {
                hand2I.get(i).setImage(new Image(hand2D.get(i).getCardPath()));
            }
            msg="";
            msg="C3"+discard.cName;
            socket.sendMessage(msg);
            msg="";
            msg="C4";
            for(int i=0; i<hand2D.size(); i++){
                msg+=hand2D.get(i).cName+"_";
            }
            socket.sendMessage(msg);
            disable();
            if(hand2D.size()==0 && deck.size()==0){
                socket.sendMessage("W2");
                passbtn.setDisable(true);
                takebtn.setDisable(true);
                lblMessages.setText("You win! Press -Deal- to start a new game");
            }
        }
        else{
            lblMessages.setText("It is not your turn");
        }
    }}
    public void ptable(String name){
        table1D.add(new Card(name));
        for(int i=0;i<table1D.size();i++)
            table1.get(i).setImage(new Image(table1D.get(i).getCardPath()));
    }
    public void updateD(String msg){
        int length=0;
        String cname="";
        hand1D.clear();
        for (int i=0; i<hand1I.size();i++)
            hand1I.get(i).setImage(null);

        for(int j=0; j<12;j++){
            for(int i=0;i<4;i++){
                if(msg.equals("")){
                    break;
                }
                length++;
                if(!msg.substring(i,i+1).equals("_")){
                    cname+=msg.substring(i,i+1);
                }
                else{
                    hand1D.add(new Card(cname));
                    cname="";
                    break;
                }
            }
            msg=msg.substring(length);
            length=0;
            cname="";
        }
        for (int i=0; i<hand1D.size();i++)
            hand1I.get(i).setImage(new Image("resources/BACK-1.jpg"));
        if(hand1D.size()==0 && deck.size()==0){
            socket.sendMessage("W1");
            lblMessages.setText("You lost! Press -Deal- to start a new game");
            disableall();
        }
    }
    public void clean(){
        for(ImageView x : table2){
            x.setImage(null);
        }
        for(ImageView x : table1){
            x.setImage(null);
        }
        table1D.clear();
        table2D.clear();
    }
    @FXML
    private void take(){
        if(table1D.size()==table2D.size()){
            lblMessages.setText("You can't do it");
        }
        else{
            for(ImageView x : table1){
                x.setImage(null);
            }
            for(ImageView x : table2){
                x.setImage(null);
            }
        for(Card x:table1D){
            if(hand2D.size()==11){
                socket.sendMessage("W1");
                passbtn.setDisable(true);
                takebtn.setDisable(true);
                lblMessages.setText("You lost! Press -Deal- to start a new game");
                disableall();
                break;
            }
            hand2D.add(x);
        }
        table1D.clear();

        for(Card x:table2D){
            if(hand2D.size()==11){
                socket.sendMessage("W1");
                passbtn.setDisable(true);
                takebtn.setDisable(true);
                lblMessages.setText("You lost! Press -Deal- to start a new game");
                disableall();
                break;
            }
            hand2D.add(x);
        }
        table2D.clear();

        for(int i = 0;i<hand2D.size();i++)
        {
            hand2I.get(i).setImage(new Image(hand2D.get(i).getCardPath()));
        }
        msg="";
        msg="C6";
        socket.sendMessage(msg);
        msg="";
        msg="C5";
        for(int i=0; i<hand2D.size(); i++){
            msg+=hand2D.get(i).cName+"_";
        }
        socket.sendMessage(msg);
        t=1;
            msg="C4";
            for(int i=0; i<hand2D.size(); i++){
                msg+=hand2D.get(i).cName+"_";
            }
            socket.sendMessage(msg);
        fullfill();
        disable();
            socket.sendMessage("A1");
            attack=1;
    }}
    @FXML
            private void pass(){
        if(t%2==0 && table1.size()==table2.size()){
            clean();
            msg="";
            msg="P1";
            socket.sendMessage(msg);
            t=1;
            msg="C4";
            for(int i=0; i<hand2D.size(); i++){
                msg+=hand2D.get(i).cName+"_";
            }
            socket.sendMessage(msg);
            fullfill();
            socket.sendMessage("A1");
            attack=1;
        }
        else{
            lblMessages.setText("You can't do it");
        }
        disable();


    }
    public void fullfill(){
        int index = hand2D.size();
        if(index<6){
             for(int i=0; i<6-index;i++){
                 if(deck.size()==0){
                     break;
                 }
                 hand2D.add(deck.remove(0));
             }
            for(ImageView x : hand2I){
                x.setImage(null);
            }
            for(int i = 0;i<hand2D.size();i++)
            {
                hand2I.get(i).setImage(new Image(hand2D.get(i).getCardPath()));
            }
        }
        msg="S2";
        for(int i=0; i<hand2D.size(); i++){
            msg+=hand2D.get(i).cName+"_";
        }
        socket.sendMessage(msg);
        index = hand1D.size();
        if(index<6){
            for(int i=0; i<6-index;i++){
                if(deck.size()==0){
                    break;
                }
                hand1D.add(deck.remove(0));
            }
            for(ImageView x : hand1I){
                x.setImage(null);
            }
            for(int i = 0;i<hand1D.size();i++)
            {
                hand1I.get(i).setImage(new Image("resources/BACK-1.jpg"));
            }
        }
        msg="S1";
        for(int i=0; i<hand1D.size(); i++){
            msg+=hand1D.get(i).cName+"_";
        }
        socket.sendMessage(msg);
        decknumber.setText(String.valueOf(deck.size()));
    }
    public void disable(){
        for(ImageView x:hand2I){
            if(x.getImage()==null){
                x.setDisable(true);
            }
            else{
                x.setDisable(false);
            }
        }

        if(t%2==0){
            tlbl.setText("Your turn");
        }
        else{
            tlbl.setText("Opponent's turn");
        }
    }
    public boolean canattack(Card x){
        if(t==0){
            return true;
        }
        else{
            for(Card c:table1D){
                if(c.cNumber==x.cNumber){
                    return true;
                }
            }for(Card c:table2D){
                if(c.cNumber==x.cNumber){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean candefend(Card x){
        Card o =table1D.get(table1D.size()-1);
        System.out.println("Card "+o.cName);
        if(x.cColor.equals(Ace.cColor)){
            if(o.cColor.equals(Ace.cColor)){
                if(x.cNumber>o.cNumber){
                    return true;
                }
                else{
                    return false;
                }
            }
            return true;
        }
        else{
            if(x.cNumber>o.cNumber && x.cColor.equals(o.cColor)){
                return true;
            }
        }
        return false;
    }
    public void disableall(){
        for(ImageView x:hand2I){
            x.setDisable(true);
        }
    }
    int t=0;
    int attack=2;
    Card discard;
    List<Card> deck = new ArrayList<>();
    List<ImageView> hand1I = new ArrayList<>();
    List<ImageView> hand2I = new ArrayList<>();
    List<ImageView> table1 = new ArrayList<>();
    List<ImageView> table2 = new ArrayList<>();
    List<Card> table1D = new ArrayList<>();
    List<Card> table2D = new ArrayList<>();
    List<Card> hand1D = new ArrayList<>();
    List<Card> hand2D = new ArrayList<>();
}

