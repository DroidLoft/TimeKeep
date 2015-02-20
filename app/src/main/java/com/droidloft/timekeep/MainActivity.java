

/*****************************************************
This is the Main Java Activity for Application
Time Keep. Written By Michael May (founder of DroidLoft)
in December of 2014.This is an exercise in code methodology
and UI design. while creating an application that can be
used to calculate hours worked based on a clock-in time and
clock-out time.
*********************************************************/

package com.droidloft.timekeep;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.SharedPreferences;
        import android.content.pm.ActivityInfo;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;


public class MainActivity extends Activity {

    TextView inHourTV, inMinTV, outHourTV, outMinTV, totalTimeTV, totalTimeDecTV, inAmPmTV, outAmPmTV;
    TextView editLunchHoursTV, editLunchMinutesTV, editLunchSaveTV, editLunchColonTV, lunchTV;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12, b00, b015, b030, b045, timeCalcB;
    int number, inTimeMin, outTimeMin, inHours, inMinutes, outHours, outMinutes, minuteDifference, timeHours, timeMinutes;
    int lunch = 30, lunchHours, lunchMinutes = 30;
    double timeWorkedDec, timeWorkedHours, timeWorkedMinutes;
    boolean inHourSelected = false, inMinSelected = false, outHourSelected = false, outMinSelected = false, inAm = true, outPm = true;
    boolean lunchHourSelected = false, lunchMinuteSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        idTextViews();
        idButtons();
        numberButtonListener();
        textListener();
        blackenTextViews();
        timeCalcListener();
        makeEditLunchInvisible();
        loadLunchPreferences();


    }

    private void loadLunchPreferences() {
        SharedPreferences lunchHourPrefs = getSharedPreferences("lunchhourkey", MODE_PRIVATE);
        lunchHours = lunchHourPrefs.getInt("lunchhourkey", 0);

        SharedPreferences lunchMinutePrefs = getSharedPreferences("lunchminutekey", MODE_PRIVATE);
        lunchMinutes = lunchMinutePrefs.getInt("lunchminutekey", 0);

        if(lunchHours < 10) {
            editLunchHoursTV.setText("0" + lunchHours);
        } else {
            editLunchHoursTV.setText("" + lunchHours);
        }

        if(lunchMinutes < 1){
            editLunchMinutesTV.setText("0" + lunchMinutes);
        } else {
            editLunchMinutesTV.setText("" + lunchMinutes);
        }
        displayLunchSubtracted();
    }

    private void makeEditLunchInvisible() {
        editLunchSaveTV.setVisibility(View.INVISIBLE);
        editLunchHoursTV.setVisibility(View.INVISIBLE);
        editLunchColonTV.setVisibility(View.INVISIBLE);
        editLunchMinutesTV.setVisibility(View.INVISIBLE);
    }

    private void makeEditLunchVisible() {
        editLunchSaveTV.setVisibility(View.VISIBLE);
        editLunchHoursTV.setVisibility(View.VISIBLE);
        editLunchColonTV.setVisibility(View.VISIBLE);
        editLunchMinutesTV.setVisibility(View.VISIBLE);
        displayLunchSubtracted();
    }

    private void timeCalcListener() {

        timeCalcB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLunchMinutes();
                timeCalcB.setTextColor(Color.BLACK);
                //Calculate Decimal Time
                convertTimeToMinutes();
                minuteDifference = ((outTimeMin - inTimeMin) - lunch);
                timeWorkedDec = (double)minuteDifference / 60;
                if(timeWorkedDec < 0){
                    timeWorkedDec = timeWorkedDec + 24;
                }
                // End Calculate Decimal Time

                //Calculate Clock Time
                timeWorkedHours = (int)Math.round(timeWorkedDec - 0.5);
                timeWorkedMinutes = timeWorkedDec - timeWorkedHours;
                timeWorkedMinutes = timeWorkedMinutes * 60;
                timeHours = (int)timeWorkedHours; //Gets rid of the decimal points
                timeMinutes = (int)timeWorkedMinutes;//Gets rid of the decimal points
                //End Calculate Clock Time

                //Displays Hours Worked
                totalTimeDecTV.setText("" + timeWorkedDec); //inDecimal

                if(timeMinutes == 0) { //In Clock Format
                    totalTimeTV.setText("" + timeHours + ":" + timeMinutes + "0"); //(Adds extra 0)
                } else {
                    totalTimeTV.setText("" + timeHours + ":" + timeMinutes); //(Without Extra 0)
                }
            }
        });
    }
    /* This method converts get the hours and minutes from
       the TextViews and converts them to integers. The if/then
       statments below make the conversion for Am/Pm by adding
       12 hours for times in Pm then multiplying hours by 60
       minutes and adding the minutes value to get a minute
       total for the in-time and out-time.
     */
    private void convertTimeToMinutes() {
        inHours = Integer.parseInt(inHourTV.getText().toString());
        inMinutes = Integer.parseInt(inMinTV.getText().toString());
        outHours = Integer.parseInt(outHourTV.getText().toString());
        outMinutes = Integer.parseInt(outMinTV.getText().toString());


        if(inAm == true) {
            inTimeMin = ((inHours * 60) + inMinutes);

        } else {

            inTimeMin = (((inHours + 12) * 60) + inMinutes);
        }

        if(outPm == true) {
            outTimeMin = (((outHours + 12) * 60) + outMinutes);

        } else {

            outTimeMin = ((outHours * 60) + outMinutes);
        }

    }

    //This helps maintain the black text color when TextView are disabled
    private void blackenTextViews() {
        inHourTV.setTextColor(Color.BLACK);
        inMinTV.setTextColor(Color.BLACK);
        outHourTV.setTextColor(Color.BLACK);
        outMinTV.setTextColor(Color.BLACK);
        editLunchHoursTV.setTextColor(Color.BLACK);
        editLunchMinutesTV.setTextColor(Color.BLACK);
    }

    //This is the onClickListener for the selectable TextViews
    private void textListener() {
        View.OnClickListener handlert = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.inHourTextView:
                        inHourTV.setTextColor(Color.RED);
                        inHourSelected = true;
                        disableMinuteButtons();
                        disableTextlisteners();
                        break;

                    case R.id.inMinTextView:
                        inMinTV.setTextColor(Color.RED);
                        inMinSelected = true;
                        disableHourButtons();
                        disableTextlisteners();
                        break;

                    case  R.id.outHourTextView:
                        outHourTV.setTextColor(Color.RED);
                        outHourSelected = true;
                        disableMinuteButtons();
                        disableTextlisteners();
                        break;

                    case  R.id.outMinTextView:
                        outMinTV.setTextColor(Color.RED);
                        outMinSelected = true;
                        disableHourButtons();
                        disableTextlisteners();
                        break;



                    case R.id.inAmPmTextView:
                        if(inAm == true) {
                            inAmPmTV.setText(" PM");
                            inAm = false;

                        } else {
                            inAmPmTV.setText(" AM");
                            inAm = true;
                        }

                        break;

                    case R.id.outAmPmTextView:
                        if(outPm == true) {
                            outAmPmTV.setText(" AM");
                            outPm = false;
                        } else {
                            outAmPmTV.setText(" PM");
                            outPm = true;
                        }
                        break;

                    case  R.id.editLunchHoursTextView:
                        editLunchHoursTV.setTextColor(Color.RED);
                        lunchHourSelected = true;
                        disableMinuteButtons();
                        disableTextlisteners();

                        break;

                    case R.id.editLunchMinutesTextView:
                        editLunchMinutesTV.setTextColor(Color.RED);
                        lunchMinuteSelected = true;
                        disableHourButtons();
                        disableTextlisteners();

                        break;

                    case R.id.editLunchSaveTextView:
                        calculateLunchMinutes();
                        makeEditLunchInvisible();
                        lunchTV.setVisibility(View.VISIBLE);
                        editLunchSaveTV.setTextColor(Color.BLACK);
                        timeCalcB.setTextColor(Color.RED);
                        saveLunchSettings();
                        displayLunchSubtracted();

                        break;


                }

            }
        };

        findViewById(R.id.inMinTextView).setOnClickListener(handlert);
        findViewById(R.id.inHourTextView).setOnClickListener(handlert);
        findViewById(R.id.outMinTextView).setOnClickListener(handlert);
        findViewById(R.id.outHourTextView).setOnClickListener(handlert);
        findViewById(R.id.totalTimeTextView).setOnClickListener(handlert);
        findViewById(R.id.inAmPmTextView).setOnClickListener(handlert);
        findViewById(R.id.outAmPmTextView).setOnClickListener(handlert);
        findViewById(R.id.editLunchHoursTextView).setOnClickListener(handlert);
        findViewById(R.id.editLunchMinutesTextView).setOnClickListener(handlert);
        findViewById(R.id.editLunchSaveTextView).setOnClickListener(handlert);


    }

    private void displayLunchSubtracted() {
        if(lunchHours < 10) {
            lunchTV.setText("0" + lunchHours + ":" + lunchMinutes + " lunch subtracted");
        }
        if(lunchHours < 10 && lunchMinutes < 1) {
            lunchTV.setText("0" + lunchHours + ":" + lunchMinutes + "0" + " lunch subtracted");
        } else {
            lunchTV.setText("" + lunchHours + ":" + lunchMinutes + " lunch subtracted");
        }
    }

    private void calculateLunchMinutes() {
        lunchHours = Integer.parseInt(editLunchHoursTV.getText().toString());
        lunchMinutes = Integer.parseInt(editLunchMinutesTV.getText().toString());
        lunch = ((lunchHours * 60) + lunchMinutes);
        saveLunchSettings();



    }

    private void saveLunchSettings() {

        SharedPreferences lunchHourPrefs = getSharedPreferences("lunchhourkey", MODE_PRIVATE);
        SharedPreferences.Editor lunchHourEditor = lunchHourPrefs.edit();
        lunchHourEditor.putInt("lunchhourkey", lunchHours);
        lunchHourEditor.commit();

        SharedPreferences lunchMinutePrefs = getSharedPreferences("lunchminutekey", MODE_PRIVATE);
        SharedPreferences.Editor lunchMinuteEditor = lunchMinutePrefs.edit();
        lunchMinuteEditor.putInt("lunchminutekey", lunchMinutes);
        lunchMinuteEditor.commit();
    }

    /*Enables and Disables Selectable TextViews
      so only one can be selected at a given time
     */
    private void enableTextListeners() {
        inHourTV.setEnabled(true);
        inMinTV.setEnabled(true);
        outHourTV.setEnabled(true);
        outMinTV.setEnabled(true);
        editLunchHoursTV.setEnabled(true);
        editLunchMinutesTV.setEnabled(true);
    }

    private void disableTextlisteners() {
        if(inHourSelected == true) {
            inMinTV.setEnabled(false);
            outHourTV.setEnabled(false);
            outMinTV.setEnabled(false);
        }

        if(inMinSelected == true) {
            inHourTV.setEnabled(false);
            outHourTV.setEnabled(false);
            outMinTV.setEnabled(false);

        }

        if(outHourSelected == true) {
            inHourTV.setEnabled(false);
            inMinTV.setEnabled(false);
            outMinTV.setEnabled(false);
        }

        if(outMinSelected == true) {
            inHourTV.setEnabled(false);
            inMinTV.setEnabled(false);
            outHourTV.setEnabled(false);

        }

        if(lunchHourSelected == true) {
            inHourTV.setEnabled(false);
            inMinTV.setEnabled(false);
            outHourTV.setEnabled(false);
            outMinTV.setEnabled(false);
            editLunchMinutesTV.setEnabled(false);
        }

        if(lunchMinuteSelected == true){
            inHourTV.setEnabled(false);
            inMinTV.setEnabled(false);
            outHourTV.setEnabled(false);
            outMinTV.setEnabled(false);
            editLunchHoursTV.setEnabled(false);

        }
    }
    /* Enables and Disables Number Buttons
       so that only certain buttons can be used
     */
    private void disableHourButtons() {
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        b10.setEnabled(false);
        b11.setEnabled(false);
        b12.setEnabled(false);
    }

    private void enableHourButton() {
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        b4.setEnabled(true);
        b5.setEnabled(true);
        b6.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);
        b9.setEnabled(true);
        b10.setEnabled(true);
        b11.setEnabled(true);
        b12.setEnabled(true);
    }

    private void disableMinuteButtons() {
        if(lunchHourSelected == true) {
            // b00.setEnabled(true);
            b015.setEnabled(false);
            b030.setEnabled(false);
            b045.setEnabled(false);
            b00.setText("00:");
        } else {

            b00.setEnabled(false);
            b015.setEnabled(false);
            b030.setEnabled(false);
            b045.setEnabled(false);
        }
    }

    private void enableMinuteButtons() {
        b00.setEnabled(true);
        b015.setEnabled(true);
        b030.setEnabled(true);
        b045.setEnabled(true);

    }
    // Listener for the Number Buttons. Sets the variable "number" and displays it.
    private void numberButtonListener() {

        View.OnClickListener handler = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button1:
                        number = 1;
                        showNumber();
                        break;

                    case  R.id.button2:
                        number = 2;
                        showNumber();
                        break;

                    case  R.id.button3:
                        number = 3;
                        showNumber();
                        break;

                    case  R.id.button4:
                        number = 4;
                        showNumber();
                        break;

                    case  R.id.button5:
                        number = 5;
                        showNumber();
                        break;

                    case  R.id.button6:
                        number = 6;
                        showNumber();
                        break;

                    case  R.id.button7:
                        number = 7;
                        showNumber();
                        break;

                    case  R.id.button8:
                        number = 8;
                        showNumber();
                        break;

                    case  R.id.button9:
                        number = 9;
                        showNumber();
                        break;

                    case  R.id.button10:
                        number = 10;
                        showNumber();
                        break;

                    case  R.id.button11:
                        number = 11;
                        showNumber();
                        break;

                    case  R.id.button12:
                        number = 12;
                        showNumber();
                        break;

                    case  R.id.button13: //:00
                        number = 0;
                        showNumber();
                        b00.setText(":00");
                        break;

                    case  R.id.button14: //:015
                        number = 15;
                        showNumber();
                        break;
                    case  R.id.button15: //:30
                        number = 30;
                        showNumber();
                        break;
                    case  R.id.button16: //:45
                        number = 45;
                        showNumber();
                        break;


                }

            }
        };

        findViewById(R.id.button1).setOnClickListener(handler);
        findViewById(R.id.button2).setOnClickListener(handler);
        findViewById(R.id.button3).setOnClickListener(handler);
        findViewById(R.id.button4).setOnClickListener(handler);
        findViewById(R.id.button5).setOnClickListener(handler);
        findViewById(R.id.button6).setOnClickListener(handler);
        findViewById(R.id.button7).setOnClickListener(handler);
        findViewById(R.id.button8).setOnClickListener(handler);
        findViewById(R.id.button9).setOnClickListener(handler);
        findViewById(R.id.button10).setOnClickListener(handler);
        findViewById(R.id.button11).setOnClickListener(handler);
        findViewById(R.id.button12).setOnClickListener(handler);
        findViewById(R.id.button13).setOnClickListener(handler);
        findViewById(R.id.button14).setOnClickListener(handler);
        findViewById(R.id.button15).setOnClickListener(handler);
        findViewById(R.id.button16).setOnClickListener(handler);


    }
    //Method called to display numbers adding "0" for numbers under 10
    private void showNumber() {
        if(inHourSelected == true) {

            if(number < 10) {
                inHourTV.setText("0" + number);
            } else {
                inHourTV.setText("" + number);
            }
            inHourTV.setTextColor(Color.BLACK);
            inHourSelected = false;
            enableMinuteButtons();
            enableTextListeners();
        }

        if(inMinSelected == true) {
            if(number < 1) {
                inMinTV.setText("0" + number);
            } else {
                inMinTV.setText("" + number);
            }

            inMinTV.setTextColor(Color.BLACK);
            inMinSelected = false;
            enableHourButton();
            enableTextListeners();


        }

        if(outHourSelected == true) {
            if(number < 10) {
                outHourTV.setText("0" + number);
            } else {
                outHourTV.setText("" + number);
            }
            outHourTV.setTextColor(Color.BLACK);
            outHourSelected = false;
            enableMinuteButtons();
            enableTextListeners();
        }

        if(outMinSelected == true){
            if(number < 1){
                outMinTV.setText("0" + number);
            } else {
                outMinTV.setText("" + number);
            }
            outMinTV.setTextColor(Color.BLACK);
            outMinSelected = false;
            enableHourButton();
            enableTextListeners();
        }

        if(lunchHourSelected == true) {

            if(number < 10) {
                editLunchHoursTV.setText("0" + number);
            } else {
                editLunchHoursTV.setText("" + number);
            }
            editLunchHoursTV.setTextColor(Color.BLACK);
            lunchHourSelected = false;
            enableMinuteButtons();
            enableTextListeners();
            editLunchSaveTV.setTextColor(Color.RED);


        }

        if(lunchMinuteSelected == true) {
            if(number < 1) {
                editLunchMinutesTV.setText("0" + number);
            } else {
                editLunchMinutesTV.setText("" + number);
            }
            editLunchMinutesTV.setTextColor(Color.BLACK);
            lunchMinuteSelected = false;
            enableMinuteButtons();
            enableHourButton();
            enableTextListeners();
            editLunchSaveTV.setTextColor(Color.RED);

        }
    }



    //Assigns ids linked to layout button widgets
    private void idButtons() {

        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        b5 = (Button)findViewById(R.id.button5);
        b6 = (Button)findViewById(R.id.button6);
        b7 = (Button)findViewById(R.id.button7);
        b8 = (Button)findViewById(R.id.button8);
        b9 = (Button)findViewById(R.id.button9);
        b10 = (Button)findViewById(R.id.button10);
        b11 = (Button)findViewById(R.id.button11);
        b12 = (Button)findViewById(R.id.button12);
        b00 = (Button)findViewById(R.id.button13);
        b015 = (Button)findViewById(R.id.button14);
        b030 = (Button)findViewById(R.id.button15);
        b045 = (Button)findViewById(R.id.button16);
        timeCalcB = (Button)findViewById(R.id.timeCalcButton);

    }
    //Assigns Id's to TextView Widgets in Layout
    private void idTextViews() {
        inHourTV = (TextView)findViewById(R.id.inHourTextView);
        inMinTV = (TextView)findViewById(R.id.inMinTextView);
        outHourTV = (TextView)findViewById(R.id.outHourTextView);
        outMinTV = (TextView)findViewById(R.id.outMinTextView);
        totalTimeTV = (TextView)findViewById(R.id.totalTimeTextView);
        totalTimeDecTV = (TextView)findViewById(R.id.totalTimeDecTextView);
        inAmPmTV = (TextView)findViewById(R.id.inAmPmTextView);
        outAmPmTV = (TextView)findViewById(R.id.outAmPmTextView);
        editLunchHoursTV = (TextView)findViewById(R.id.editLunchHoursTextView);
        editLunchMinutesTV = (TextView)findViewById(R.id.editLunchMinutesTextView);
        editLunchSaveTV = (TextView)findViewById(R.id.editLunchSaveTextView);
        editLunchColonTV = (TextView)findViewById(R.id.editLunchColonTextView);
        lunchTV = (TextView)findViewById(R.id.lunchTextView);

    }

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) { //About Screen Dialog in Options Menu

            AlertDialog.Builder aboutAlert = new AlertDialog.Builder(MainActivity.this);
            aboutAlert.setTitle("Time Keep v1.0");
            aboutAlert.setMessage("All Rights Reserved 2014" + "\n" + "Michael May" + "\n" + "DroidLoft");
            aboutAlert.setIcon(R.drawable.ic_launcher);
            aboutAlert.setCancelable(true);
            aboutAlert.show();

            return true;

        }

        if(id == R.id.lunch) {  //Edit Lunch function in options menu
            lunchTV.setVisibility(View.INVISIBLE);
            makeEditLunchVisible();
            lunchMinutes = 0;

        }
        return super.onOptionsItemSelected(item);
    }
}
