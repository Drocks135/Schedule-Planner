package GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import projEvents.Business;
import projEvents.EventStorage;
import projEvents.Events;
import projEvents.Homework;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;

/**************************************************************************************************
 * @author FredO
 * @version 1.0
 * DayView will show the user the events corresponding to the day they select. Will also give the
 * user the option of creating a new event or edit/delete saved events.
 *************************************************************************************************/
public class DayView {

    /**The css code used to style the visuals of an idle button*/
    private final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";

    /**The css code used to style the visuals of a button that is being hovered over*/
    private final String HOVERED_BUTTON_STYLE = "-fx-background-color: " +
            "-fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;";

    /**This is used to access the events for a given day. I made it static so that I can call it
       from the lambda functions.*/
    private static int index = 0;

    /**********************************************************************************************
     * Display will show the current events and allow for user to edit them.
     * Buttons include:
     *      "<"  --> Shows the previous event on current day
     *      ">"  --> Shows the next event on current day
     *      "Close"  --> Will close the current dayView
     *      "Edit"  --> Calls EventCreatorView.edit**() which allows the user to edit current event
     *      "Add"  --> Calls EventCreatorView.display() which allows for the creation of new event
     * @param date The date for the day the user clicks on
      *********************************************************************************************/
    public void display(LocalDate date) {
        EventCreatorView eventCreatorView = new EventCreatorView();

        LinkedList<Events> events = EventStorage.getInstance().GetListOfDay(date);

        Stage dayView = new Stage();

        Label prettyDay = new Label(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        prettyDay.setPadding(new Insets(5));
        prettyDay.setFont(new Font(15));
        VBox lilBox = new VBox();
        lilBox.setPadding(new Insets(5));
        lilBox.setSpacing(5);

        HBox topStuff = new HBox();
        Label eventNum = new Label();
        eventNum.setTooltip(new Tooltip("Number of Events"));

        Button nextEvent = new Button(">");
        nextEvent.setTooltip(new Tooltip("Next Event"));
        nextEvent.setStyle(IDLE_BUTTON_STYLE);
        nextEvent.setOnMouseEntered(e -> nextEvent.setStyle(HOVERED_BUTTON_STYLE));
        nextEvent.setOnMouseExited(e -> nextEvent.setStyle(IDLE_BUTTON_STYLE));

        Button prevEvent = new Button("<");
        prevEvent.setTooltip(new Tooltip("Previous Event"));
        prevEvent.setStyle(IDLE_BUTTON_STYLE);
        prevEvent.setOnMouseEntered(e -> prevEvent.setStyle(HOVERED_BUTTON_STYLE));
        prevEvent.setOnMouseExited(e -> prevEvent.setStyle(IDLE_BUTTON_STYLE));

        Button closeBtn = new Button("Close");
        closeBtn.setCancelButton(true);
        closeBtn.setOnAction(e -> {
            System.out.println("close button");
            index = 0;
            dayView.close();
        });
        Button addBtn = new Button("Add Event");
        addBtn.setOnAction(e -> {
            dayView.close();
            eventCreatorView.display(date);
        });
        HBox bottomBtns = new HBox(10, closeBtn, addBtn);

        if(events == null || events.size() == 0) {
            lilBox.getChildren().add(new Label("No Events Today"));
            eventNum.setText("None");
        } else {
            eventNum.setText("" + (index + 1) + "/" + events.size());
            nextEvent.setOnAction(e -> {
                if (index + 1 <= events.size() - 1) {
                    index++;
                    showEvent(events.get(index), lilBox);
                } else {
                    index = 0;
                    showEvent(events.get(index), lilBox);
                }
                eventNum.setText("" + (index + 1) + "/" + events.size());
            });

            prevEvent.setOnAction(e -> {
                if (index - 1 >= 0) {
                    index--;
                    showEvent(events.get(index), lilBox);
                } else {
                    index = events.size() - 1;
                    showEvent(events.get(index), lilBox);
                }
                eventNum.setText("" + (index + 1) + "/" + events.size());
            });
            Button editEvent = new Button("Edit Event");
            editEvent.setOnAction(e -> {
                dayView.close();
                if(events.get(index).toString().charAt(0) == 'h') {
                    new EventCreatorView(date).editHomework((Homework) events.get(index));
                } else {
                    new EventCreatorView(date).editBusiness((Business) events.get(index));
                }
            });
            bottomBtns.getChildren().add(1, editEvent);

            showEvent(events.get(index), lilBox);
        }
        topStuff.getChildren().addAll(prettyDay, prevEvent, nextEvent, eventNum);

        VBox bigBox = new VBox(10, topStuff, lilBox, bottomBtns);
        bigBox.setPadding(new Insets(10));

        dayView.setScene(new Scene(bigBox));
        dayView.setOnCloseRequest(e -> {
            index = 0;
        });
        dayView.show();
    }

    /**********************************************************************************************
     * This is method is used to generate all of the information of a given event.
     * @param event The event to be displayed to the user
     * @param eventDisplay The VBox that holds all of the labels for the event information.
     *********************************************************************************************/
    private void showEvent(Events event, VBox eventDisplay){
        eventDisplay.getChildren().clear();

        //Check to see if the event is homework or business
        if(event.toString().charAt(0) == 'h'){
            Homework homework = (Homework) event;

            Label eName = new Label(homework.getName());

            Label className = new Label(homework.getclassFor());

            Label turnInPlace = new Label("Turn in at " + homework.getturnInPlace());

            Label detail = new Label(homework.getDetails());
            detail.wrapTextProperty().setValue(true);

            eventDisplay.getChildren().addAll(eName, className, turnInPlace, detail);

        } else {
            Business business = (Business) event;

            Label eName = new Label(business.getName());

            Label location = new Label(business.getLocation());

            Label duration = new Label("Meeting should last " +
                    (int)business.getDuration() + " hours");

            Label detail = new Label(business.getDetails());
            detail.wrapTextProperty().setValue(true);

            eventDisplay.getChildren().addAll(eName, location, duration, detail);

        }
    }
}

