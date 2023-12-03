package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Moves;
import AdventureModel.Pokemon;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * ZOOM LINK: <https://youtu.be/3BVuZkCkQ6g>
 * PASSWORD: <PASSWORD HERE>
 */
public class AdventureGameView {

    // THIS IS A TEST ANOTHER TEST
    private Voice voice;

    AdventureGame model; //model of the game

    boolean resume = false;
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton; //buttons
    Boolean helpToggle = false; //is help on display?

    private Pokemon battlePokemon;
    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items

    VBox objectsInInventoryCopy = new VBox(); //to hold inventory items

    VBox selectedPokemon = new VBox(); //to hold inventory items



    boolean moveListening = false;

    boolean moveListening2 = false;
    ImageView roomImageView; //to hold room image

    ImageView opponentPokemonView; // to hold image of opponent pokemon in battle

    Label opponentPokemonLabel;
    TextField inputTextField; //for user input

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    private AdventureModel.Battle battle;

    private ArrayList<AdventureModel.Moves> validMoves;

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("<Group 35>'s Pokemon Game"); //Replace <YOUR UTORID> with your UtorID

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);


        // Backpack + room pokemons
//        pokeInRoom.setSpacing(10);
//        pokeInRoom.setAlignment(Pos.TOP_CENTER);
//        pokeInBackpack.setSpacing(10);
//        pokeInBackpack.setAlignment(Pos.TOP_CENTER);


        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms


        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        //TODO: implement for battle
//        moveListening = true;
        inputTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String userInput = inputTextField.getText();
                //System.out.println("w1");
                //submitEvent(userInput.strip());
                if (moveListening){
                    //System.out.println("w2");
                    getMoveEvent2(userInput.strip(), () -> {
                        // Code to execute after getMoveEvent2 completion
                        // This can be empty or you can perform additional actions
                    });
                    inputTextField.clear();
                } else if (moveListening2){
                    //System.out.println("w3");
                    getMoveEvent3(userInput.strip(), () -> {
                        // Code to execute after getMoveEvent3 completion
                        // This can be empty or you can perform additional actions
                    });
                    inputTextField.clear();
                } else {
                    //System.out.println("w4");
                    submitEvent(userInput.strip());
                    inputTextField.clear();
                }
                //System.out.println("w5");
                //inputTextField.clear();
            }
////            else if (e.getCode() == KeyCode.TAB) {
////                gridPane.requestFocus();
////            }
        });
    }



    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {
        //System.out.println("w5");
        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            System.out.println("PRINT THIS");
            System.out.println("HERE: " +this.model.getPlayer().getCurrentRoom().getRoomName());
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
//            articulateRoomDescription(roomDesc); //all we want, if we are looking, is to repeat description.
            System.out.println("room Desc HERE:" + roomDesc);
            textToSpeech(roomDesc);
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        } else if (text.equalsIgnoreCase("TALK") || text.equalsIgnoreCase("T")) { // TODO: EDIT
            this.model.getPlayer().getCurrentRoom().villagerInRoom.talk(); //this is new!  We did not have this command in A1
            this.updateItems();
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!
        //System.out.println("output: "+output);
        //System.out.println("w6");

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            System.out.println("w20");
            updateScene(output);
            System.out.println("w30");
            updateItems();
            //System.out.println("w11");
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            System.out.println("ini2");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
           // System.out.println("w10");

            updateScene("");
            System.out.println("ini2");
            updateItems();
            inputTextField.setDisable(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(5)); // Pause for five seconds
            pause.setOnFinished(event -> {
                inputTextField.setDisable(false);
                submitEvent("FORCED"); // Call function again to enter forced room
            });
            pause.play();
            System.out.println("w8");

        }

        System.out.println("w7");

    }



    /**
     * Get the event from textfield
     */
    public void getMoveEvent(Pokemon P, AdventureModel.Battle b){

        stopArticulation();

        this.resume = false;


        this.battlePokemon = P;

        this.battle = b;

        // Since we only have one inputTextField event listener, we'll handle conditional inputs in an if/then
        this.moveListening = true;

        // Format text will display text to prompt the user
        formatText("It is your turn to move in the battle. Would you like to move or pass? Type MOVE to move and type PASS to pass.");

        //do the getMoveEvent2 stuff
        getMoveEvent2("MoveOrPass",()->{
            //No additional logic here, leaving it empty
            // Continue with the rest of your logic in getMoveEvent
            // ...

        });



    }

    interface Callback{
        void execute();
}

    private void getMoveEvent2(String text, Callback callback){

        if(text.equalsIgnoreCase("PASS")){
            this.battle.returnedMove = new AdventureModel.Moves("PASS", 0, 0);
            this.moveListening = false;
            callback.execute();

            return;
        }
        else if(text.equalsIgnoreCase("MOVE")){
            this.moveListening = false;
            this.validMoves.clear();
            Integer energy = this.battlePokemon.get_energy(); // TODO: get_energy()
            HashMap<Integer, Moves> moves = this.battlePokemon.get_moves();
            StringBuilder Out = new StringBuilder();
            Out.append("Which move would you like to use? ");
            boolean empty = true;
            for (Map.Entry<Integer, Moves> m : moves.entrySet()){
                if (m.getValue().get_energy() <= energy){
                    empty = false;
                    Out.append(m.getValue().get_description());
                    this.validMoves.add(m.getValue());
                }
            }
            if (empty){
                Out.append("You do not have enough energy to make any moves.");
                //TODO: PAUSE FOR 2 SECONDS
                this.battle.returnedMove = new AdventureModel.Moves("PASS", 0, 0);

                callback.execute();
                return;

            }

            formatText(String.valueOf(Out));

            this.moveListening2 = true;
        } else {
            formatText("Invalid command. Please enter a valid command.");
            callback.execute();
        }

    }


    private void getMoveEvent3(String text, Callback callback){
        boolean valid = false;
        Moves chosen = null;
        for (Moves c : this.validMoves){
            if (Objects.equals(text, c)){
                valid = true;
                chosen = c;
            }
        }
        if (valid){
            this.battle.returnedMove = chosen;
            this.moveListening2 = false;
            this.moveListening = false;
        }else {
            return;
        }
    }


    public void pause(){
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> {
            return;
        });
        pause.play();
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        String commands = this.model.player.getCurrentRoom().getCommands();
        formatText(commands);
        System.out.println("Commands HERE:" + commands);
        textToSpeech(commands);
    }

    public void setBattleScene(Pokemon p, Pokemon o){

        // Set image views of both Pokémon and set accessibility text:
        String pImage = p.getImage();

        Image roomImageFile = new Image(pImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText("Your active Pokemon: " + p.getName());
        roomImageView.setFocusTraversable(true);

        String oImage = o.getImage();

        Image oImageFile = new Image(oImage);
        opponentPokemonView = new ImageView(roomImageFile);
        opponentPokemonView.setPreserveRatio(true);
        opponentPokemonView.setFitWidth(300); //TODO: this probably needs to change
        opponentPokemonView.setFitHeight(300);

        //set accessible text
        opponentPokemonView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        opponentPokemonView.setAccessibleText("The opponent's active Pokemon: " + o.getName());
        opponentPokemonView.setFocusTraversable(true);

        // Insert the displays into the grid

        formatText("Your active pokemon: " + p.getName());
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 1);

        stage.sizeToScene();
        formatOppText(o.getName());
        VBox oPane = new VBox(opponentPokemonView,opponentPokemonLabel);
        oPane.setPadding(new Insets(10));
        oPane.setAlignment(Pos.TOP_CENTER);
        oPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(oPane, 2, 1);
        stage.sizeToScene();

        //finally, articulate the description TODO: DO I WANT THIS?? HOW TO??
//        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        //System.out.println("a");

        getRoomImage(); //get the image of the current room

       // System.out.println("w30");

        formatText(textToDisplay); //format the text to display
      //  System.out.println("AHHHH");

//        textToSpeech(textToDisplay);
        roomDescLabel.setPrefWidth(500);
      //  System.out.println("AHHHP");

        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
       // System.out.println("b");

        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

       // System.out.println("e");

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
       // System.out.println("c");

//        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription(textToDisplay);
//        if (textToDisplay == null || textToDisplay.isBlank()) textToSpeech(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        //System.out.println("d");
        new Thread(() -> {
            if (textToDisplay == null || textToDisplay.isBlank()) {
                textToSpeech(this.model.getPlayer().getCurrentRoom().getRoomDescription());
            } else {
                textToSpeech(textToDisplay);
            }
        }).start();

    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     * 
     * @param textToDisplay the text to be formatted for display.
     */
    public void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString); // TODO: change this
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    private void formatOppText(String pokemonName) {
        opponentPokemonLabel.setText("Opponent Pokemon: " + pokemonName);
        opponentPokemonLabel.setStyle("-fx-text-fill: white;");
        opponentPokemonLabel.setFont(new Font("Arial", 16));
        opponentPokemonLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView 
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = "Games/TinyGame/Images/Room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {
        System.out.println("in updateItems");
        ArrayList<Pokemon>  objRoom = this.model.player.getCurrentRoom().pokemonsInRoom;
        ArrayList<Pokemon> objInv = this.model.player.getBackpack();

        objectsInRoom.getChildren().removeAll(objectsInRoom.getChildren());
        objectsInInventory.getChildren().removeAll(objectsInInventory.getChildren());

        System.out.println(objInv);
        for(Pokemon o: objInv){ // Go through all items in inventory and create button
            String name = o.getName();
            System.out.println("where my pokes at");
            Image image = new Image("AdventureModel/pokemon_images/"+Integer.toString(o.getIndex()) + ".png");
            System.out.println("wherreeeee");
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);
            Button obj = new Button();
            obj.setGraphic(iv);
            makeButtonAccessible(obj, name, name, name);
            addObjEvent(obj, name);
            objectsInInventory.getChildren().add(obj);
        }
        System.out.println(objRoom);
        for(Pokemon o: objRoom){// Go through all items in room and create button
            String name = o.getName();
            System.out.println("where my pokes at");
            Image image = new Image("AdventureModel/pokemon_images/"+Integer.toString(o.getIndex()) + ".png");
            System.out.println("wheeee2");
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);
            Button obj = new Button();
            obj.setGraphic(iv);
            makeButtonAccessible(obj, name, name, name);
            addObjEvent(obj, o.getName());
            objectsInRoom.getChildren().add(obj);
        }

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }

    public void addObjEvent(Button o, String objectName) { // Add event to object button
        o.setOnAction(e -> {
            ObservableList<Node> oir = objectsInRoom.getChildren();
            boolean inRoom = false;
            for(Node c:oir){
                if (c==o){
                    oir.remove(o);
                    this.model.player.capturePokemon(objectName);
                    objectsInInventory.getChildren().add(o);
                    inRoom = true;
                    break; //Take object
                }
            }
            if (!inRoom){
                oir.add(o); //Drop object
                objectsInInventory.getChildren().remove(o);
                this.model.player.releasePokemon(objectName);
            }
        });
    }

    /**
     * Show scroll panes before entering battle to allow user to select battle pokemon
     */

    public void selectPokemon(){
        objectsInInventoryCopy = objectsInInventory;
        selectedPokemon.getChildren().clear();

        ScrollPane scO = new ScrollPane(selectedPokemon);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventoryCopy);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }





    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        if (helpToggle){
            helpToggle = false;
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
            updateScene(""); // Show image again
        }else{
            helpToggle = true;
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
            File h = new File(this.model.getDirectoryName()+"/help.txt");
            List<String> content = new ArrayList<>();
            try {
                // Create a BufferedReader to read from the file
                BufferedReader reader = new BufferedReader(new FileReader(h));

                String line;
                while ((line = reader.readLine()) != null) {
                    content.add(line);
                }

                reader.close();
            } catch (IOException e) {
                System.out.println("help.txt not found");
            }

            Label ins = new Label(String.join("\n", content));
            ins.setPrefWidth(500);
            ins.setPrefHeight(500);
            ins.setWrapText(true);
            ins.setStyle("-fx-text-fill: white;");
            ins.setFont(new Font("Arial", 13));
            ins.setAlignment(Pos.CENTER);
            VBox instructionsPane = new VBox(ins);
            instructionsPane.setPadding(new Insets(10));
            instructionsPane.setAlignment(Pos.TOP_CENTER);
            instructionsPane.setStyle("-fx-background-color: #000000;");

            gridPane.add(instructionsPane, 1, 1);
            stage.sizeToScene();
        }

    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription(String input) {

        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }
    public void textToSpeech(String input) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
         String VOICENAME_kevin = "kevin16";
         Voice voice;
         VoiceManager voiceManager = VoiceManager.getInstance();
         voice = voiceManager.getVoice(VOICENAME_kevin);
         voice.allocate();

         voice.speak(input);
    }




    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}
