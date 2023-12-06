package views;

import AdventureModel.*;
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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
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
    private Voice voice;

    AdventureGame model; //model of the game

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

    Label objLabel =  new Label();

    Label invLabel =  new Label();

    Label battleLabel =  new Label();

    boolean moveListening = false;

    boolean moveListening2 = false;
    ImageView roomImageView; //to hold room image

    ImageView opponentPokemonView; // to hold image of opponent pokemon in battle

    ImageView battleView; // left hand display

    Label opponentPokemonLabel= new Label();


    TextField inputTextField; //for user input

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    private AdventureModel.Battle battle;

    private ArrayList<AdventureModel.Moves> validMoves = new ArrayList<>();

    private boolean isHighContrast = false;
    private int numSelectedPokemon = 0;

    public ArrayList<Pokemon> arraySelectedPokemon = new ArrayList<>();

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
        this.stage.setTitle("Group 35's Pokemon Game");

        //Configure layout and styling for inventory and room item displays
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        // Define grid layout constraints for columns and rows
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Initialize and customize UI buttons with event handlers
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        // Set up the text field for user input with accessibility features
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        // Set up the text field for user input with accessibility features
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

        // Configure labels for inventory, room items, and battle information
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        battleLabel.setText("");
        battleLabel.setAlignment(Pos.CENTER);
        battleLabel.setStyle("-fx-text-fill: white;");
        battleLabel.setFont(new Font("Arial", 16));

        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        // Add UI components to the grid layout
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms


        // Set up command label and text entry area for user input
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Initialize and add contrast toggle and zoom buttons
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.9);
        Button toggleButton = new Button("Toggle High Contrast");
        makeButtonAccessible(toggleButton,"Toggle High Contrast","Set Contrast to 0.9","Set Contrast to 0.9");
        toggleButton.setOnAction(event -> {
            if (isHighContrast) {
                gridPane.setEffect(null); // Turn off high contrast
            } else {
                gridPane.setEffect(colorAdjust); // Turn on high contrast
            }
            isHighContrast = !isHighContrast; // Toggle the flag
        });
        // Zoom in and out using buttons
        Button zoomInButton = new Button("Zoom In");
        Button zoomOutButton = new Button("Zoom Out");

        zoomInButton.setOnAction(e -> zoom(gridPane, true));
        zoomOutButton.setOnAction(e -> zoom(gridPane, false));

        HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
        buttonBox.getChildren().addAll(zoomInButton, zoomOutButton);
        gridPane.add(buttonBox, 0, 0); // Adjust column and row indices as needed
        gridPane.add(toggleButton, 0, 0); // Adjust column and row indices as needed

        // Finalize the scene setup and show the stage
        var scene = new Scene(gridPane, 1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

    }

    /**
     * zoom
     * ----------------------------
     *
     * Adjusts the zoom level of the given GridPane.
     * This method alters the scale of the GridPane to zoom in or out,
     * based on the boolean parameter 'zoomIn'. It uses a scale transformation
     * to increase or decrease the size of the GridPane's content.
     *
     * @param pane The GridPane whose zoom level is to be adjusted.
     * @param zoomIn A boolean indicating whether to zoom in (true) or zoom out (false).
     */
    private void zoom(GridPane pane, boolean zoomIn) {
        double zoomFactor = 1.5; // The factor by which the zoom level is changed
        Scale newScale = new Scale();

        // Calculate the new scale based on the current scale and the zoom factor
        newScale.setX(pane.getScaleX() * (zoomIn ? zoomFactor : 1/zoomFactor));
        newScale.setY(pane.getScaleY() * (zoomIn ? zoomFactor : 1/zoomFactor));

        // Update the current scale tracking variable
        currentScale = zoomIn ? currentScale * zoomFactor : currentScale * (1 / zoomFactor);

        // Apply the new scale transformation to the GridPane
        pane.getTransforms().add(newScale);

        // Update the UI bounds or layout after the zoom operation
        updateBounds();
    }

    private double initialMinX = -100.0;
    private double initialMaxX = 100.0;
    private double initialMinY = -100.0;
    private double initialMaxY = 100.0;
    private double currentScale = 1.0;
    private void updateBounds() {
        System.out.println(initialMaxX);
        initialMaxX = initialMaxX * currentScale;
        initialMaxY = initialMaxY * currentScale;
        initialMinX = initialMinX * currentScale;
        initialMinY = initialMinY * currentScale;
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
        // Set an event handler for key presses in the input text field
        inputTextField.setOnKeyPressed(e -> {
            // Check if the 'Enter' key is pressed
            if (e.getCode() == KeyCode.ENTER) {
                // Get the user input from the text field
                String userInput = inputTextField.getText();

                // Determine the game state and call the corresponding event handler
                if (moveListening){
                    getMoveEvent2(userInput.strip());
                } else if (moveListening2){
                    getMoveEvent3(userInput.strip());
                } else {
                    // Default action for processing user input
                    submitEvent(userInput.strip());
                }

                // Clear the input field for new input
                inputTextField.clear();
                return;
            }
            // Check if the 'Tab' key is pressed to change focus
            else if (e.getCode() == KeyCode.TAB) {
                gridPane.requestFocus();
            }
        });
    }



    /**
     * submitEvent
     * --------------------------
     *
     * Processes the given text command in the context of the game.
     * This method handles various game commands, including 'LOOK', 'HELP', 'COMMANDS', and 'TALK',
     * and executes the appropriate actions. It also processes other general commands
     * through the game's model and updates the game state accordingly.
     *
     * @param text The command text entered by the user that needs to be processed.
     */
    private void submitEvent(String text) {
        // Strip any leading or trailing whitespace from the command
        text = text.strip();

        // Stop any ongoing speech synthesis
        stopSpeaking();

        //Process specific commands
        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            // Handle 'HELP' command: show game instructions
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            textToSpeech(roomDesc);
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            // Handle 'HELP' command: show game instructions
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            // Handle 'COMMANDS' command: display available commands
            showCommands(); //this is new!  We did not have this command in A1
            return;
        } else if (text.equalsIgnoreCase("TALK") || text.equalsIgnoreCase("T")) {
            // Handle 'TALK' command: interact with a villager in the room, if present
            Villager speaker = this.model.getPlayer().getCurrentRoom().villagerInRoom;

            if (speaker == null){
                formatText("INVALID COMMAND. There are no villagers in this scene to talk to.");
                System.out.println("SHOULD BE HERE");
                return;
            }

            String speech = speaker.talk();
            this.updateItems();
            this.updateScene(speech);
            String pImage = speaker.getImage();
            System.out.println("villager image: " + pImage);

            Image roomImageFile = new Image(pImage);
            roomImageView = new ImageView(roomImageFile);
            roomImageView.setPreserveRatio(true);
            roomImageView.setFitWidth(400);
            roomImageView.setFitHeight(400);

            roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
            roomImageView.setAccessibleText(speaker.getName() + "is talking with you.");
            roomImageView.setFocusTraversable(true);

            roomDescLabel.setPrefWidth(500);
            roomDescLabel.setPrefHeight(500);
            roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
            roomDescLabel.setWrapText(true);
            VBox roomPane = new VBox(roomImageView,roomDescLabel);
            roomPane.setPadding(new Insets(10));
            roomPane.setAlignment(Pos.TOP_CENTER);
            roomPane.setStyle("-fx-background-color: #000000;");

            gridPane.add(roomPane, 1, 1);
            return;
        }
        // Handle other general commands
        String output = this.model.interpretAction(text);
        // Update the game scene and items based on the command's output
        System.out.println("Interpret action finished, output: " + output);
        // Special handling for game states like 'BATTLE', 'GAME OVER', and 'FORCED'
        if (Objects.equals(output, "BATTLE")){
            return;
        }

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            System.out.println("w20");
            updateScene(output);
            System.out.println("w30");
            updateItems();
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
        }
        // Log that the submit event processing is complete
        System.out.println("submit event finished");
    }



    /**
     * getMoveEvent
     * ---------------------------
     *
     * Handles the event for a player's move in a Pokemon battle.
     * This method is called to prompt the player to make a move during a battle.
     * It sets up the necessary state for listening to the player's response in the input text field.
     * The player can choose to either execute a move or pass their turn.
     *
     * @param P The Pokemon involved in the battle.
     * @param b The Battle instance in which the event is occurring.
     */
    public void getMoveEvent(Pokemon P, AdventureModel.Battle b) {
        // Logging statement for debugging
        System.out.println("get move event 1");

        // Stop any ongoing speech synthesis
        stopSpeaking();

        // Set the flag to indicate that the game is currently listening for a battle move
        this.moveListening = true;

        // Store the current battle context
        this.battlePokemon = P;
        this.battle = b;

        // Prompt the player for a move or pass decision
        formatText("It is your turn to move in the battle. Would you like to move or pass? Type MOVE to move and type PASS to pass.");
    }


    /**
     * getMoveEvent2
     * --------------------------
     *
     * Processes the player's decision to move or pass in a Pokemon battle.
     * This method is called after the player is prompted to choose between moving or passing their turn.
     * It handles the player's response and executes the corresponding action in the battle.
     *
     * @param text The player's response text from the input field, indicating their choice.
     */
    private void getMoveEvent2(String text) {
        // Logging statement for debugging
        System.out.println("get move event 2");

        // Check if the player chooses to pass their turn
        if (text.equalsIgnoreCase("PASS")) {
            // Set the move to 'PASS' and resume the battle
            this.battle.returnedMove = new AdventureModel.Moves("PASS", 0, 0);
            this.moveListening = false;
            this.battle.resumeBattle();
            return;
        }
        else if (text.equalsIgnoreCase("MOVE")) {
            // Handle the case where the player chooses to make a move
            this.moveListening = false;
            this.validMoves.clear();
            Integer energy = this.battlePokemon.get_energy();
            HashMap<Integer, Moves> moves = this.battlePokemon.get_moves();

            // Build a list of valid moves based on the Pokemon's energy
            StringBuilder Out = new StringBuilder();
            Out.append("Which move would you like to use? Please enter the name of the move.");
            boolean empty = true;
            for (Map.Entry<Integer, Moves> m : moves.entrySet()) {
                if (m.getValue().get_energy() <= energy) {
                    empty = false;
                    Out.append(m.getValue().get_description());
                    this.validMoves.add(m.getValue());
                }
            }

            // Handle the case where there are no valid moves due to insufficient energy
            if (empty) {
                Out.append("You do not have enough energy to make any moves.");
                this.pause(4);
                this.battle.returnedMove = new AdventureModel.Moves("PASS", 0, 0);
                this.battle.resumeBattle();
                return;
            }

            // Display the valid moves to the player
            formatText(String.valueOf(Out));
            this.moveListening2 = true;
            return;

        } else {
            // Handle invalid commands
            formatText("Invalid command. Please enter a valid command.");
            return;
        }
    }



    /**
     * getMoveEvent3
     * --------------------------
     *
     * Processes the player's specific move choice in a Pokemon battle.
     * After the player is prompted to select a move, this method validates the chosen move
     * against the list of available moves. If the move is valid, it sets the chosen move
     * for the current battle round and resumes the battle. If the move is invalid,
     * it notifies the player and awaits a valid response.
     *
     * @param text The player's response text from the input field, indicating their chosen move.
     */
    private void getMoveEvent3(String text) {
        // Logging statement for debugging
        System.out.println("get move event 3");

        // Initialize variables for validation and move selection
        boolean valid = false;
        Moves chosen = null;

        // Check if the entered move matches any of the valid moves
        for (Moves c : this.validMoves) {
            if (c.get_name().equalsIgnoreCase(text)) {
                valid = true;
                chosen = c;
            }
        }

        // If the move is valid, set it as the chosen move and resume the battle
        if (valid) {
            this.battle.returnedMove = chosen;
            this.moveListening2 = false;
            this.moveListening = false;
            this.battle.resumeBattle();
        } else {
            // Log an invalid move selection for debugging
            System.out.println("invalid move");
        }
    }


    /**
     * pause
     * --------------------------
     *
     * Creates a pause or delay in the execution of the program for a specified duration.
     * This method utilizes the PauseTransition class to create a temporary halt in the
     * execution flow, typically used for timing or synchronization purposes in the UI or game logic.
     *
     * @param d The duration of the pause in seconds.
     */
    public void pause(Integer d) {
        // Create a PauseTransition with the specified duration
        PauseTransition pause = new PauseTransition(Duration.seconds(d));

        // Set an action to perform when the pause is finished. Currently, it does nothing and simply returns.
        pause.setOnFinished(event -> {
            return;
        });

        // Start the pause
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

    /**
     * Sets up the battle scene in the game UI with the images and details of the participating Pokemon.
     * This method prepares the visual elements for a Pokemon battle, including images of the player's Pokemon,
     * the opponent's Pokemon, and any additional battle-related graphics. It also updates relevant labels
     * and ensures the accessibility of the UI components.
     *
     * @param p The player's Pokemon participating in the battle.
     * @param o The opponent's Pokemon participating in the battle.
     */
    public void setBattleScene(Pokemon p, Pokemon o){
        //Configure UI elements for displaying the player's Pokemon
        String pImage = p.getImage();

        invLabel.setText("Opponent Pokemon:");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        objLabel.setText("");

        formatText("Your Pokemon: " + p.getName());

        Image roomImageFile = new Image(pImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(p.getName());
        roomImageView.setFocusTraversable(true);

        // Configure UI elements for displaying the opponent's Pokemon
        String oImage = o.getImage();

        Image oImageFile = new Image(oImage);
        opponentPokemonView = new ImageView(oImageFile); //TODO: I CHANGED THIS
        opponentPokemonView.setPreserveRatio(true);
        opponentPokemonView.setFitWidth(150); //TODO: this probably needs to change
        opponentPokemonView.setFitHeight(150);

        //set accessible text
        opponentPokemonView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        opponentPokemonView.setAccessibleText("The opponent's active Pokemon: " + o.getName());
        opponentPokemonView.setFocusTraversable(true);

        // Configure and set up battle view and related UI components
        String battleImage = "AdventureModel/pokemon_images/pokemon_battle.png";

        Image bImageFile = new Image(battleImage);
        battleView = new ImageView(bImageFile);
        battleView.setPreserveRatio(true);
        battleView.setFitWidth(200); //TODO: this probably needs to change

        //set accessible text
        battleView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        battleView.setAccessibleText("BATTLE");
        battleView.setFocusTraversable(true);

        // Add these elements to the grid layout of the scene and adjust the stage size
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
        oPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(oPane, 2, 1);
        stage.sizeToScene();

        battleLabel.setText("BATTLE");
        VBox bPane = new VBox(battleView,battleLabel);
        bPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(bPane, 0, 1);
        stage.sizeToScene();
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
        // Stop any ongoing speech synthesis
        stopSpeaking();

        // Clear images from battle and opponent Pokemon view, if present
        if (battleView != null) {
            battleView.setImage(null);
        }
        if (opponentPokemonView != null) {
            opponentPokemonView.setImage(null);
        }

        // Reset various labels
        invLabel.setText("");
        objLabel.setText("");
        battleLabel.setText("");
        opponentPokemonLabel.setText("");

        // Logging statement for debugging
        System.out.println("update Scene");

        // Retrieve and display the image of the current room
        getRoomImage();

        // Format and display the provided text in the scene
        formatText(textToDisplay);
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);

        // Add the room image and description to the scene layout
        VBox roomPane = new VBox(roomImageView, roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");
        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        // Use a separate thread for text-to-speech to avoid UI freezing
        new Thread(() -> {
            if (textToDisplay == null || textToDisplay.isBlank()) {
                // Default actions if no specific text is provided
                objLabel.setText("Objects in Room");
                invLabel.setText("Your Inventory");
                textToSpeech(this.model.getPlayer().getCurrentRoom().getRoomDescription());
            } else {
                // Read out the provided text
                textToSpeech(textToDisplay);
            }
        }).start();
    }

    /**
     * formatText
     * --------------------------
     *
     * Formats and sets the text to be displayed in the room description label.
     * This method handles the presentation of text in the game's UI, specifically in the room description area.
     * It either displays the provided text or, if no text is provided, it defaults to displaying the current room's
     * description along with any Pokemons or villagers present in the room.
     *
     * @param textToDisplay The specific text to be displayed, or null/blank to display the room's default description.
     */
    public void formatText(String textToDisplay) {
        // Check if the provided text is null or blank
        if (textToDisplay == null || textToDisplay.isBlank()) {
            // Build the default room description
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String pokemonString = "";
            String villagerString = "";
            String s = "";

            // Append Pokemon present in the room to the description
            if (this.model.getPlayer().getCurrentRoom().pokemonsInRoom.size() > 0) {
                for (Pokemon p : this.model.getPlayer().getCurrentRoom().pokemonsInRoom) {
                    pokemonString += p.getName() + '\n';
                }
            }

            // Append villager present in the room to the description
            if (this.model.getPlayer().getCurrentRoom().villagerInRoom != null) {
                villagerString += this.model.getPlayer().getCurrentRoom().villagerInRoom.getName();
            }

            // Combine Pokemon and villager information
            s = pokemonString + villagerString;

            // Set the appropriate text based on the presence of Pokemon and villagers
            if (s.equals("")) {
                roomDescLabel.setText(roomDesc);
            } else {
                if (pokemonString.equals("")) {
                    roomDescLabel.setText(roomDesc + "\n\nVillagers in this room:\n" + s);
                } else if (villagerString.equals("")) {
                    roomDescLabel.setText(roomDesc + "\n\nPokemons in this room:\n" + s);
                } else {
                    roomDescLabel.setText(roomDesc + "\n\nPokemons and Villagers in this room:\n" + s);
                }
            }
        } else {
            // Set the provided text if it's not null or blank
            roomDescLabel.setText(textToDisplay);
        }

        // Style the room description label
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }


    /**
     * formatOppText
     * --------------------------
     *
     * Formats and sets the text for the opponent Pokemon label in the game UI.
     * This method is used to update the label that displays the name of the opponent's Pokemon.
     * It sets the text, font, color, and alignment to ensure the label is clearly visible and consistent
     * with the game's overall design.
     *
     * @param pokemonName The name of the opponent's Pokemon to be displayed.
     */
    private void formatOppText(String pokemonName) {
        // Set the text of the opponent Pokemon label
        opponentPokemonLabel.setText(pokemonName);

        // Style the opponent Pokemon label
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
        String roomImage = "Games/TinyGame/Images/PokeroomPic/" + roomNumber + ".jpeg";

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
        // Retrieve the list of Pokemons in the room and in the player's inventory
        ArrayList<Pokemon>  objRoom = this.model.player.getCurrentRoom().pokemonsInRoom;
        ArrayList<Pokemon> objInv = this.model.player.getBackpack();

        // Clear existing children from the inventory and room item displays
        objectsInRoom.getChildren().removeAll(objectsInRoom.getChildren());
        objectsInInventory.getChildren().removeAll(objectsInInventory.getChildren());

        // Iterate over inventory items and create buttons for each
        for(Pokemon o: objInv){ // Go through all items in inventory and create button
            // Configure and style the inventory item button
            String name = o.getName();
            Image image = new Image("AdventureModel/pokemon_images/"+Integer.toString(o.getIndex()) + ".png");
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);

            Label label = new Label(name);
            label.setStyle("-fx-text-fill: black; -fx-background-color: transparent;");

            VBox objBox= new VBox(iv, label);
            objBox.setAlignment(Pos.CENTER);

            Button buttonWithLabel = new Button();
            buttonWithLabel.setGraphic(objBox);
            makeButtonAccessible(buttonWithLabel, name, name, name);
            addObjEvent(buttonWithLabel, o.getName());

            // Add the button to the inventory display
            objectsInInventory.getChildren().add(buttonWithLabel);
        }

        // Iterate over room items and create buttons for each
        for(Pokemon o: objRoom){
            // Configure and style the room item button
            String name = o.getName();
            Image image = new Image("AdventureModel/pokemon_images/"+Integer.toString(o.getIndex()) + ".png");
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);

            Label label = new Label(name);
            label.setStyle("-fx-text-fill: black; -fx-background-color: transparent;");

            VBox objBox= new VBox(iv, label);
            objBox.setAlignment(Pos.CENTER);

            Button buttonWithLabel = new Button();
            buttonWithLabel.setGraphic(objBox);
            makeButtonAccessible(buttonWithLabel, name, name, name);
            addObjEvent(buttonWithLabel, o.getName());

            // Add the button to the room item display
            objectsInRoom.getChildren().add(buttonWithLabel);
        }

        // Configure and add scroll panes for inventory and room items
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

    /**
     * addObjEvent
     * ----
     * Adds an event handler to an object button for capturing and releasing Pokemon.
     * This method associates an event handler with a given object button, allowing the player
     * to interact with Pokemon objects in the game by capturing or releasing them.
     *
     * @param o           The object button to which the event handler is added.
     * @param objectName  The name of the Pokemon object associated with the button.
     */
    public void addObjEvent(Button o, String objectName) {
        o.setOnAction(e -> {
            ObservableList<Node> oir = objectsInRoom.getChildren();
            boolean inRoom = false;

            // Iterate through objects in the room to check if the button belongs to the room
            for (Node c : oir) {
                if (c == o) {
                    oir.remove(o);
                    this.model.player.capturePokemon(objectName);
                    objectsInInventory.getChildren().add(o);
                    inRoom = true;
                    break; // Take object
                }
            }

            // If the button doesn't belong to the room, it belongs to the inventory
            if (!inRoom) {
                oir.add(o); // Drop object
                objectsInInventory.getChildren().remove(o);
                this.model.player.releasePokemon(objectName);
            }
        });
    }


    /**
     * Adds an event handler to an object button for moving Pokemon between Selected and Inventory.
     * This method associates an event handler with a given object button, allowing the player to
     * move Pokemon objects between the "Selected" and "Inventory" sections during a battle.
     *
     * @param o           The object button to which the event handler is added.
     * @param objectName  The name of the Pokemon object associated with the button.
     * @param p           The Pokemon object represented by the button.
     */
    public void addObjEvent2(Button o, String objectName, Pokemon p) {
        o.setOnAction(e -> {
            ObservableList<Node> oir = selectedPokemon.getChildren();
            boolean inRoom = false;

            // Iterate through objects in the "Selected" section to check if the button belongs to it
            for (Node c : oir) {
                if (c == o) {
                    oir.remove(o); // Move from "Selected" to "Inventory"
                    System.out.println("Move from 'Selected' to 'Inventory'");

                    objectsInInventoryCopy.getChildren().add(o);
                    this.arraySelectedPokemon.remove(p);
                    inRoom = true;
                    this.numSelectedPokemon -= 1;
                    break; // Take object
                }
            }

            // If the button doesn't belong to the "Selected" section, it belongs to the "Inventory"
            if (!inRoom) {
                System.out.println("Move from 'Inventory' to 'Selected'");
                oir.add(o); // Move from "Inventory" to "Selected"
                this.arraySelectedPokemon.add(p);
                objectsInInventoryCopy.getChildren().remove(o);
                this.numSelectedPokemon += 1;

                // Check if the maximum limit of selected Pokemon (3) has been reached to initiate a battle
                if (this.numSelectedPokemon >= 3) {
                    this.battle.battleInit2();
                }
            }
        });
    }




    /**
     * updateSelectionItems
     * -------------------------
     *
     * Initializes and updates the Pokemon selection interface for a battle.
     * This method is responsible for setting up the initial Pokemon selection interface and updating
     * it as needed during a battle. It clears existing selections and displays the available Pokemon
     * for the player to choose from.
     *
     * @param b The Battle object associated with the selection interface.
     */
    public void updateSelectionItems(Battle b) {
        // Initialize the number of selected Pokemon and Battle object
        this.numSelectedPokemon = 0;
        this.battle = b;
        System.out.println("Updating selection items");

        // Retrieve Pokemon with the player from the inventory
        ArrayList<Pokemon> pokeWithPlayer = this.model.player.getBackpack();
        System.out.println("Pokemon with player: " + pokeWithPlayer);

        // Clear existing displays
        this.objectsInInventoryCopy.getChildren().clear();
        this.selectedPokemon.getChildren().clear();
        this.arraySelectedPokemon.clear();

        // Display instructions for selecting Pokemon
        this.formatText("Select 3 Pokemon for battle.");

        // Populate the right-side pane with Pokemon from the player's inventory
        for (Pokemon o : pokeWithPlayer) {
            String name = o.getName();
            Image image = new Image("AdventureModel/pokemon_images/" + Integer.toString(o.getIndex()) + ".png");
            ImageView iv = new ImageView(image);
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);

            Label label = new Label(name);
            label.setStyle("-fx-text-fill: black; -fx-background-color: transparent;");

            VBox objBox = new VBox(iv, label);
            Button obj = new Button();
            obj.setGraphic(objBox);
            makeButtonAccessible(obj, name, name, name);
            addObjEvent2(obj, name, o);
            this.objectsInInventoryCopy.getChildren().add(obj);
        }

        // Add a scroll pane for the selected Pokemon
        ScrollPane scO = new ScrollPane(selectedPokemon);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO, 0, 1);

        // Add a scroll pane for the available Pokemon in the inventory
        ScrollPane scI = new ScrollPane(objectsInInventoryCopy);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI, 2, 1);
    }

    /**
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
        if (helpToggle) {
            // Instructions are currently shown, hide them
            helpToggle = false;
            // Remove the instructions VBox from the gridPane
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
            // Show the image again
            updateScene("");
        } else {
            // Instructions are currently hidden, show them
            helpToggle = true;
            // Remove any existing instructions from the gridPane
            gridPane.getChildren().removeIf(node -> {
                // Assume default values for row and column if they are null
                Integer rowIndex = GridPane.getRowIndex(node);
                if (rowIndex == null) rowIndex = 0; // Default row index

                Integer columnIndex = GridPane.getColumnIndex(node);
                if (columnIndex == null) columnIndex = 0; // Default column index

                // Remove nodes in row 1 and column 1 (the instructions)
                return rowIndex == 1 && columnIndex == 1;
            });

            // Load instructions from "help.txt" file
            File h = new File(this.model.getDirectoryName() + "/help.txt");
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

            // Create a label to display the instructions
            Label ins = new Label(String.join("\n", content));
            ins.setPrefWidth(500);
            ins.setPrefHeight(500);
            ins.setWrapText(true);
            ins.setStyle("-fx-text-fill: white;");
            ins.setFont(new Font("Arial", 13));
            ins.setAlignment(Pos.CENTER);

            // Create a VBox to hold the instructions label
            VBox instructionsPane = new VBox(ins);
            instructionsPane.setPadding(new Insets(10));
            instructionsPane.setAlignment(Pos.TOP_CENTER);
            instructionsPane.setStyle("-fx-background-color: #000000;");

            // Add the instructions VBox to the gridPane
            gridPane.add(instructionsPane, 1, 1);
            stage.sizeToScene();

            // Convert instructions to a single string
            StringBuilder instructionString = new StringBuilder();
            for (String str : content) {
                instructionString.append(str);
            }

            // Start a new thread to read the instructions aloud using textToSpeech
            new Thread(() -> {
                textToSpeech(instructionString.toString());
            }).start();
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopSpeaking(); //if speaking, stop
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

    public void textToSpeech(String input) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        String VOICENAME_kevin = "kevin16";
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.setRate(135);
        voice.allocate();

        voice.speak(input);
    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */

    public void stopSpeaking(){
        if (this.voice != null){
            voice.deallocate();
        }
    }
}
