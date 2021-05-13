/*
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */
package controller;

import entities.Datarow;
import entities.Question;
import entities.Score;
import entities.Word;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lewin Hafner
 */
@Named(value = "gameController")
@SessionScoped
public class GameController implements Serializable {

    @EJB
    private beans.WordFacade wordEJBFacade;
    @EJB
    private beans.QuestionFacade questionEJBFacade;
    @EJB
    private beans.ScoreFacade scoreEJBFacade;

    private DatatableDataController dataController = new DatatableDataController();

    @NotNull
    @Size(min = 3, max = 16, message = "Playername should be between 3 to 16 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$", message = "Please only use alphanumeric characters, numbers or scores.")
    private String playername = "";

    private int accountValue = 0;
    private int lifeCount = 3;
    private int riskBet = 0;
    private int numberOfGuesses = 0;
    private int vowelPrice = 0;

    private boolean playernameChosen = false;
    private boolean gameover = false;
    private boolean firstHeart = true;
    private boolean secondHeart = true;
    private boolean thirdHeart = true;
    private boolean canSpin = false;
    private boolean canGuess = false;
    private boolean canGuessEntireWord = false;
    private boolean canBuyVowel = false;
    private boolean showData = false;
    private boolean showRespinOption = false;
    private boolean showHighscoreEntry = false;
    private boolean mustDecide = false;
    private boolean mustBet = false;
    private boolean mustAnswer = false;
    private boolean guessRight = false;
    private boolean foundAllConsonants = false;

    private Random rand = new Random();

    private String rolled = "";
    private String wordStr = "";
    private String category = "";
    private String modalText = "";
    private String modalTitle = "";
    private String modalIcon = "";
    private String answerOne = "";
    private String answerTwo = "";

    @Size(min = 1, max = 1)
    @Pattern(regexp = "[aeiouöü]", message = "Only vowels!")
    private String vowelToBuy = "";

    @Size(min = 1, max = 1)
    @Pattern(regexp = "([b-df-hj-np-tv-zB-DF-HJ-NP-TV-Z])", message = "Only consonants!")
    private String playerGuess;

    @Size(min = 1, max = 42)
    private String entireWordGuess;

    private Word currentWord;
    private Question currentQuestion;

    private List<Word> words;
    private List<Word> usedWords = new ArrayList<>();
    private List<Question> questions;
    private List<Question> usedQuestions = new ArrayList<>();
    private List<Character> vowelsAvailable = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'ü', 'ö'));
    private List<Character> foundLetters = new ArrayList<>();

    private char[] solution;
    private char[] gameboard;

    /**
     * Creates a new instance of GameController
     */
    public GameController() {
        this.playernameChosen = false;
    }

    /**
     * clears variables after a game has been played
     */
    private void init() {
        // re initialize game, keep usedWord, usedQuestions
        this.accountValue = 0;
        this.lifeCount = 3;
        this.riskBet = 0;
        this.numberOfGuesses = 0;
        this.vowelPrice = 0;
        this.vowelToBuy = "";
        this.playerGuess = "";
        this.gameboard = null;
        this.solution = null;
        this.playernameChosen = false;
        this.gameover = false;
        this.firstHeart = true;
        this.secondHeart = true;
        this.thirdHeart = true;
        this.canSpin = false;
        this.canGuess = false;
        this.canGuessEntireWord = false;
        this.canBuyVowel = false;
        this.showData = false;
        this.showHighscoreEntry = false;
        this.showRespinOption = false;
        this.mustDecide = false;
        this.mustBet = false;
        this.mustAnswer = false;
        this.guessRight = false;
        this.foundAllConsonants = false;
        this.rolled = "";
        this.wordStr = "";
        this.category = "";
        this.modalText = "";
        this.modalTitle = "";
        this.modalIcon = "";
        this.answerOne = "";
        this.answerTwo = "";

        vowelsAvailable = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'ü', 'ö'));

        pickWord();
        createDataRows();
        updateGameBoard();
    }

    /**
     * main start method, checks if a game has been played before
     */
    public void start() {
        if (this.gameover) {
            init();
        } else {
            setup();
        }
    }

    /**
     * sets word and adds them to the gameboard
     */
    private void setup() {
        this.words = wordEJBFacade.findAll();
        this.questions = questionEJBFacade.findAll();
        pickWord();
        createDataRows();
    }

    /**
     * picks a random unused word
     */
    private void pickWord() {
        int rndIndex = rand.nextInt(this.words.size());
        this.currentWord = this.words.get(rndIndex);

        while (this.usedWords.contains(this.currentWord)) {
            this.currentWord = this.words.get(rand.nextInt(this.words.size()));
        }
        this.wordStr = currentWord.getWord();
        this.usedWords.add(currentWord);
        this.category = currentWord.getCategoryID().getCategory();
    }

    /**
     * triggers pickQuestion()
     */
    public void prepareQuestion() {
        pickQuestion();
    }

    /**
     * checks if the user answer is correct
     */
    public void answerQuestion() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> parameterMap = (Map<String, String>) context.getExternalContext().getRequestParameterMap();
        String chosenAnswer = parameterMap.get("answer");

        if (chosenAnswer.equalsIgnoreCase(this.currentQuestion.getRightAnswer())) {
            this.accountValue += this.riskBet;
            makeModal("success", "Good job!", "Your answer was correct. $" + this.riskBet + " have been added to your account. \r\n You may now continue..");
        } else {
            this.accountValue -= this.riskBet;
            this.accountValue += this.riskBet;
            makeModal("error", "Bummer!", "Your answer was incorrect. $" + this.riskBet + " have been taken from your account. \r\n The correct answer was: " + this.currentQuestion.getRightAnswer());
        }
        this.mustAnswer = false;
        this.riskBet = 0;
        this.showData = true;
        //this.canGuess = true;
        this.canSpin = true;
    }

    /**
     * picks a random unuse question
     */
    private void pickQuestion() {
        int rndIndex = rand.nextInt(this.questions.size());
        this.currentQuestion = this.questions.get(rndIndex);

        while (this.usedQuestions.contains(this.currentQuestion)) {
            this.currentQuestion = this.questions.get(rand.nextInt(this.questions.size()));
        }
        this.usedQuestions.add(currentQuestion);
        boolean rndBool = rand.nextBoolean();
        if (rndBool) {
            this.answerOne = this.currentQuestion.getRightAnswer();
            this.answerTwo = this.currentQuestion.getWrongAnswer();
        } else {
            this.answerOne = this.currentQuestion.getWrongAnswer();
            this.answerTwo = this.currentQuestion.getRightAnswer();
        }

    }

    /**
     * creates the gameboard array and allocates the letters based on the words
     * length
     */
    private void createDataRows() {
        this.dataController.clearDatarows();
        int wordLength = this.wordStr.length();
        int whitespaceCount = 42 - wordLength;
        int borderleft;
        if (whitespaceCount % 2 == 0) {
            borderleft = (whitespaceCount / 2);
        } else {
            borderleft = ((whitespaceCount + 1) / 2);
        }

        char[] letters = new char[42];

        // initalize with whitespace
        for (int i = 0; i < letters.length; i++) {
            letters[i] = ' ';
        }

        // multiple words
        if (this.wordStr.contains(" ")) {
            int wordCount = 1;

            for (int i = 0; i < wordLength; i++) {
                if (wordStr.charAt(i) == ' ') {
                    wordCount++;
                }
            }
            // find length of individual words
            int[] lengthOfSubWords = new int[wordCount];
            String[] wordSubWords = wordStr.split(" ");
            for (int i = 0; i < wordSubWords.length; i++) {
                lengthOfSubWords[i] = wordSubWords[i].length();
            }

            switch (wordCount) {
                case 2:
                    // passen zusammen auf eine linie
                    if (wordLength < 14) {
                        for (int i = 14; i < (wordLength + 14); i++) {
                            letters[i] = wordStr.charAt(i - 14);
                        }
                    } // passen je auf eine linie
                    else if (lengthOfSubWords[0] < 14 && lengthOfSubWords[1] < 14) {
                        for (int i = 0; i < lengthOfSubWords[0]; i++) {
                            letters[i] = wordSubWords[0].charAt(i);
                        }

                        for (int i = 14; i < (lengthOfSubWords[1] + 14); i++) {
                            letters[i] = wordSubWords[1].charAt(i - 14);
                        }
                    } // passen nicht je auf eine linie
                    else {
                        for (int i = 0; i < wordLength; i++) {
                            letters[i] = wordStr.charAt(i);
                        }
                    }
                    break;
                case 3:
                    // passen zusammen auf eine linie
                    if (wordLength < 14) {
                        for (int i = 14; i < (wordLength + 14); i++) {
                            letters[i] = wordStr.charAt(i - 14);
                        }
                    } // passen je auf eine linie
                    else if (lengthOfSubWords[0] < 14 && lengthOfSubWords[1] < 14 && lengthOfSubWords[2] < 14) {
                        for (int i = 0; i < lengthOfSubWords[0]; i++) {
                            letters[i] = wordSubWords[0].charAt(i);
                        }

                        for (int i = 14; i < (lengthOfSubWords[1] + 14); i++) {
                            letters[i] = wordSubWords[1].charAt(i - 14);
                        }

                        for (int i = 28; i < (lengthOfSubWords[2] + 28); i++) {
                            letters[i] = wordSubWords[2].charAt(i - 28);
                        }
                    } // passen nicht je auf eine linie
                    else {
                        for (int i = 0; i < wordLength; i++) {
                            letters[i] = wordStr.charAt(i);
                        }

                    }
                    break;
                default:
                    for (int i = 0; i < wordLength; i++) {
                        letters[i] = wordStr.charAt(i);
                    }
                    break;
            }
        } else {
            // just one word
            if (borderleft < 14 && borderleft > 11) {
                // first ten letters on first row
                for (int i = 2; i < 12; i++) {
                    letters[i] = wordStr.charAt(i - 2);
                }

                if (wordLength > 20) {
                    // next 10 letters on second row
                    for (int i = 16; i < 25; i++) {
                        letters[i] = wordStr.charAt(i - 6);
                    }

                    // last letters on third row
                    for (int i = 30; i < (30 + (wordLength - 20)); i++) {
                        letters[i] = wordStr.charAt(i - 20);
                    }
                } else {
                    // last 10 letters on second row
                    for (int i = 16; i < (16 + (wordLength - 10)); i++) {
                        letters[i] = wordStr.charAt(i - 6);
                    }
                }

            } else {
                for (int i = borderleft; i < (borderleft + wordLength); i++) {
                    letters[i] = wordStr.charAt(i - borderleft);
                }
            }
        }

        // set solution and make gameboard empty
        this.solution = letters;
        this.gameboard = new char[42];

        // set special characters
        for (int i = 0; i < 42; i++) {
            if (Character.toString(solution[i]).matches("[0-9_*',.]")) {
                gameboard[i] = solution[i];
            } else if (solution[i] != ' ') {
                gameboard[i] = '.';
            } else {
                gameboard[i] = ' ';
            }
        }
        // fill datarows
        this.dataController.createDataRow(String.valueOf(gameboard[0]), String.valueOf(gameboard[1]), String.valueOf(gameboard[2]), String.valueOf(gameboard[3]), String.valueOf(gameboard[4]), String.valueOf(gameboard[5]), String.valueOf(gameboard[6]), String.valueOf(gameboard[7]), String.valueOf(gameboard[8]), String.valueOf(gameboard[9]), String.valueOf(gameboard[10]), String.valueOf(gameboard[11]), String.valueOf(gameboard[12]), String.valueOf(gameboard[13]));
        this.dataController.createDataRow(String.valueOf(gameboard[14]), String.valueOf(gameboard[15]), String.valueOf(gameboard[16]), String.valueOf(gameboard[17]), String.valueOf(gameboard[18]), String.valueOf(gameboard[19]), String.valueOf(gameboard[20]), String.valueOf(gameboard[21]), String.valueOf(gameboard[22]), String.valueOf(gameboard[23]), String.valueOf(gameboard[24]), String.valueOf(gameboard[25]), String.valueOf(gameboard[26]), String.valueOf(gameboard[27]));
        this.dataController.createDataRow(String.valueOf(gameboard[28]), String.valueOf(gameboard[29]), String.valueOf(gameboard[30]), String.valueOf(gameboard[31]), String.valueOf(gameboard[32]), String.valueOf(gameboard[33]), String.valueOf(gameboard[34]), String.valueOf(gameboard[35]), String.valueOf(gameboard[36]), String.valueOf(gameboard[37]), String.valueOf(gameboard[38]), String.valueOf(gameboard[39]), String.valueOf(gameboard[40]), String.valueOf(gameboard[41]));
    }

    /**
     * sets the rolled / spinned value and triggers according process
     */
    public void setRolled(String rolled) {
        this.showRespinOption = false;
        boolean mustReroll = false;
        // prevent resubmiting form
        if (canSpin) {
            switch (rolled) {
                case "Bankrupt":
                    endGame(false);
                    break;
                case "Risk":
                    if (this.accountValue == 0) {
                        mustReroll = true;
                    } else {
                        // ask for bet
                        this.canGuess = false;
                        this.mustBet = true;
                        this.showData = false;
                    }
                    break;
                default:
                    this.canGuess = true;
                    break;
            }
        }

        // if player had to place a bet without any money 
        if (mustReroll) {
            this.canSpin = true;
        } else {
            this.rolled = rolled;
            this.canSpin = false;
            this.showData = true;
        }
    }

    /**
     * updates the gameboard with the updated array
     */
    private void updateGameBoard() {
        Datarow d1 = new Datarow(String.valueOf(gameboard[0]), String.valueOf(gameboard[1]), String.valueOf(gameboard[2]), String.valueOf(gameboard[3]), String.valueOf(gameboard[4]), String.valueOf(gameboard[5]), String.valueOf(gameboard[6]), String.valueOf(gameboard[7]), String.valueOf(gameboard[8]), String.valueOf(gameboard[9]), String.valueOf(gameboard[10]), String.valueOf(gameboard[11]), String.valueOf(gameboard[12]), String.valueOf(gameboard[13]));
        Datarow d2 = new Datarow(String.valueOf(gameboard[14]), String.valueOf(gameboard[15]), String.valueOf(gameboard[16]), String.valueOf(gameboard[17]), String.valueOf(gameboard[18]), String.valueOf(gameboard[19]), String.valueOf(gameboard[20]), String.valueOf(gameboard[21]), String.valueOf(gameboard[22]), String.valueOf(gameboard[23]), String.valueOf(gameboard[24]), String.valueOf(gameboard[25]), String.valueOf(gameboard[26]), String.valueOf(gameboard[27]));
        Datarow d3 = new Datarow(String.valueOf(gameboard[28]), String.valueOf(gameboard[29]), String.valueOf(gameboard[30]), String.valueOf(gameboard[31]), String.valueOf(gameboard[32]), String.valueOf(gameboard[33]), String.valueOf(gameboard[34]), String.valueOf(gameboard[35]), String.valueOf(gameboard[36]), String.valueOf(gameboard[37]), String.valueOf(gameboard[38]), String.valueOf(gameboard[39]), String.valueOf(gameboard[40]), String.valueOf(gameboard[41]));
        this.dataController.updateDataRows(d1, d2, d3);
    }

    /**
     * checks if the entered letter (consonant or vowel) is in the secret word
     */
    public void guess() {

        boolean alreadyFound = false;
        this.numberOfGuesses++;
        int foundLettersCount = 0;

        // if guessed letter has been found before, dont count it as found
        if (this.foundLetters.contains(playerGuess.charAt(0))) {
            alreadyFound = true;
            this.guessRight = false;
        } else {
            // check 
            for (int i = 0; i < 42; i++) {
                if (Character.toString(solution[i]).equalsIgnoreCase(playerGuess)) {
                    gameboard[i] = solution[i];
                    this.guessRight = true;
                    foundLettersCount++;
                    this.foundLetters.add(playerGuess.charAt(0));
                }
            }
        }

        updateGameBoard();
        this.canGuess = false;

        if (this.guessRight) {
            int amountWon = (Integer.parseInt(rolled) * foundLettersCount);
            this.accountValue = this.accountValue + amountWon;
            makeModal("success", "Good job!", "Letter " + this.playerGuess + " was found " + foundLettersCount + " times. $" + amountWon + " have been added to your account");
            this.mustDecide = true;
        } else if (alreadyFound) {
            makeModal("info", "Careful!", "Letter " + this.playerGuess + " has already been found.");

        } else {
            makeModal("error", "Oh no!", "Letter " + this.playerGuess + " was not in the word!");
        }

        if (checkIfSolved()) {
            endGame(true);
        } else {
            checkForAllConsonantsFound();

            if (!this.guessRight) {
                this.canSpin = true;
            }
        }

        // removes lives
        if (!this.guessRight) {
            if (firstHeart) {
                this.firstHeart = false;
            } else if (secondHeart) {
                this.secondHeart = false;
            } else {
                this.thirdHeart = false;
                endGame(false);
            }
        }
        this.playerGuess = "";
        this.guessRight = false;
    }

    /**
     * checks if all consonants have been found
     */
    private void checkForAllConsonantsFound() {
        int[] solutionInd = getNumberOfConsonants(this.wordStr);
        int[] gameboardInd = getNumberOfConsonants(String.valueOf(this.gameboard));
        if (solutionInd[1] == gameboardInd[1]) {
            this.foundAllConsonants = true;
            this.mustDecide = false;
            makeModal("info", "Found all consonants!", "From now on you may only buy vowels or solve the entire word!");

        } else {
            this.foundAllConsonants = false;
        }

    }

    /**
     * returns the number of different letter types
     */
    private int[] getNumberOfConsonants(String w) {
        int[] n = new int[4];
        int vowelCount = 0;
        int consonantCount = 0;
        int digitCount = 0;
        int whitespaceCount = 0;

        for (int i = 0; i < w.length(); i++) {
            String wordLowered = w.toLowerCase();
            char ch = wordLowered.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i'
                    || ch == 'o' || ch == 'u') {
                vowelCount++;
            } else if ((ch >= 'a' && ch <= 'z')) {
                consonantCount++;
            } else if (ch >= '0' && ch <= '9') {
                digitCount++;
            } else if (ch == ' ') {
                whitespaceCount++;
            }
        }

        n[0] = vowelCount;
        n[1] = consonantCount;
        n[2] = digitCount;
        n[3] = whitespaceCount;

        return n;
    }

    /**
     * creates the alert
     */
    private void makeModal(String icon, String title, String text) {
        this.modalIcon = icon;
        this.modalTitle = title;
        this.modalText = text;
        fireAlert();
    }

    /**
     * triggers javascript alert
     */
    private void fireAlert() {
        RequestContext.getCurrentInstance().execute("document.getElementById('showModalBtn').click();");
    }

    private boolean checkIfSolved() {
        return Arrays.equals(this.solution, this.gameboard);
    }

    public void roll() {
        System.out.println("rolled: " + this.rolled);
    }

    public void wantsToGuess() {
        hasDecided();
        this.showRespinOption = true;
        //this.canGuess = true;
    }

    public void wantsToSpin() {
        this.canSpin = true;
        this.showRespinOption = false;
    }

    public void wantsToBuyVocal() {
        hasDecided();
        this.vowelPrice = ((this.accountValue / 4) + 4) / 5 * 5;
        this.foundAllConsonants = false;
        this.canBuyVowel = true;
    }

    /**
     * buying and guessing the vowel
     */
    public void buyVowel() {
        char input = this.vowelToBuy.charAt(0);
        if (this.vowelsAvailable.contains(input)) {
            this.canBuyVowel = false;
            this.accountValue -= vowelPrice;
            int index = this.vowelsAvailable.indexOf(input);
            this.vowelsAvailable.remove(index);
            // buy it
            this.playerGuess = this.vowelToBuy;
            this.vowelToBuy = "";
            guess();
        } else {
            makeModal("info", "Oops", "Looks like " + this.vowelToBuy + " is no longer available.");
        }

    }

    public void wantsToGuessEntireWord() {
        this.canGuessEntireWord = true;
        this.foundAllConsonants = false;
    }

    /**
     * checks if the entire word guess is correct
     */
    public void guessEntireWord() {
        if (this.entireWordGuess.toLowerCase().equals(this.wordStr.toLowerCase())) {
            endGame(true);
        } else {
            endGame(false);
        }

        this.gameboard = this.solution;
        this.canGuessEntireWord = false;
        this.foundAllConsonants = false;
        this.updateGameBoard();
    }

    public void wantsToPlayWithSameWager() {
        this.canGuess = true;
        this.mustDecide = false;
        this.showRespinOption = false;
    }

    private void hasDecided() {
        this.mustDecide = false;
        this.playerGuess = "";
    }

    public void addToHighscore() {
        scoreEJBFacade.create(new Score(0, 0, playername, new Date(), this.accountValue, this.numberOfGuesses));
        this.showHighscoreEntry = false;
        this.gameover = true;
    }

    public void dontAddToHighscore() {
        this.showHighscoreEntry = false;
        this.gameover = true;
    }

    /**
     * ends game with won / lost, adds to scorelist and shows the result
     *
     * @param won
     */
    private void endGame(boolean won) {
        // write to highscore
        this.canSpin = false;
        this.canGuess = false;

        this.foundAllConsonants = false;

        this.mustDecide = false;
        if (!won) {
            makeModal("error", "Game Over", "You played " + numberOfGuesses + " rounds and would have won $" + this.accountValue + ". Maybe next time ;)");
        } else {
            makeModal("success", "Winner Winner Crossword Dinner!", "Amazing! You have found all the letters in the secret word! Your total score is: $" + this.accountValue + " in " + numberOfGuesses + " rounds.");
        }

        this.showHighscoreEntry = true;
        this.accountValue = 0;
    }

    /**
     * clears variables and redirects to homescreen (index.xhtml)
     */
    public void redirectToHomeScreen() {
        init();
        String redirectionUrl = "";
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        final String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length()) + request.getContextPath();

        redirectionUrl = baseUrl + "/index.xhtml";

        try {
            response.sendRedirect(redirectionUrl);

        } catch (IOException ex) {
            Logger.getLogger(GameController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * sets bet, checks if its valid
     *
     * @param riskBet
     */
    public void setRiskBet(int riskBet) {
        if (riskBet >= accountValue) {
            makeModal("warning", "Insufficient balance", "Your bet of $" + riskBet + " is more than you currently have in your account!");
        } else if (riskBet <= 0) {
            makeModal("warning", "Invalid input", "Please respect the min and max values!");
        } else {
            this.riskBet = riskBet;
            this.mustBet = false;
            this.mustAnswer = true;
        }
    }

    public DatatableDataController getDataController() {
        return dataController;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
        this.playernameChosen = true;
        this.canSpin = true;
    }

    public void wantsToQuitPlaying() {
        this.mustDecide = false;
        this.showHighscoreEntry = true;
    }

    public int getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(int accountValue) {
        this.accountValue = accountValue;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
    }

    public boolean isPlayernameChosen() {
        return playernameChosen;
    }

    public void setPlayernameChosen(boolean playernameChosen) {
        this.playernameChosen = playernameChosen;
    }

    public boolean isFirstHeart() {
        return firstHeart;
    }

    public void setFirstHeart(boolean firstHeart) {
        this.firstHeart = firstHeart;
    }

    public boolean isSecondHeart() {
        return secondHeart;
    }

    public void setSecondHeart(boolean secondHeart) {
        this.secondHeart = secondHeart;
    }

    public boolean isThirdHeart() {
        return thirdHeart;
    }

    public void setThirdHeart(boolean thirdHeart) {
        this.thirdHeart = thirdHeart;
    }

    public boolean isCanSpin() {
        return canSpin;
    }

    public void setCanSpin(boolean canSpin) {
        this.canSpin = canSpin;
    }

    public String getRolled() {
        return rolled;
    }

    public String getWordStr() {
        return wordStr;
    }

    public void setWordStr(String wordStr) {
        this.wordStr = wordStr;
    }

    public boolean isShowData() {
        return showData;
    }

    public void setShowData(boolean showData) {
        this.showData = showData;
    }

    public String getPlayerGuess() {
        return playerGuess;
    }

    public void setPlayerGuess(String playerGuess) {
        this.playerGuess = playerGuess;
    }

    public boolean isCanGuess() {
        return canGuess;
    }

    public void setCanGuess(boolean canGuess) {
        this.canGuess = canGuess;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isGameover() {
        return gameover;
    }

    public boolean isMustDecide() {
        return mustDecide;
    }

    public boolean isShowRespinOption() {
        return showRespinOption;
    }

    public boolean isMustBet() {
        return mustBet;
    }

    public int getRiskBet() {
        return riskBet;
    }

    public boolean isGuessRight() {
        return guessRight;
    }

    public String getModalText() {
        return modalText;
    }

    public String getModalTitle() {
        return modalTitle;
    }

    public String getModalIcon() {
        return modalIcon;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public boolean isMustAnswer() {
        return mustAnswer;
    }

    public boolean isFoundAllConsonants() {
        return foundAllConsonants;
    }

    public void setEntireWordGuess(String entireWordGuess) {
        this.entireWordGuess = entireWordGuess;
    }

    public String getEntireWordGuess() {
        return entireWordGuess;
    }

    public boolean isCanGuessEntireWord() {
        return canGuessEntireWord;
    }

    public boolean isCanBuyVowel() {
        return canBuyVowel;
    }

    public void setCanBuyVowel(boolean canBuyVowel) {
        this.canBuyVowel = canBuyVowel;
    }

    public String getVowelToBuy() {
        return vowelToBuy;
    }

    public void setVowelToBuy(String vowelToBuy) {
        this.vowelToBuy = vowelToBuy;
    }

    public List<Character> getVowelsAvailable() {
        return vowelsAvailable;
    }

    public int getVowelPrice() {
        return vowelPrice;
    }

    public boolean isShowHighscoreEntry() {
        return showHighscoreEntry;
    }

}
